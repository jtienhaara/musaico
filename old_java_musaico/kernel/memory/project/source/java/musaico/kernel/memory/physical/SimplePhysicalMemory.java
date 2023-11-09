package musaico.kernel.memory.physical;


import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.buffer.Buffer;
import musaico.buffer.BufferException;
import musaico.buffer.BufferTools;
import musaico.buffer.SimpleBuffer;

import musaico.field.Field;
import musaico.field.FieldTypingEnvironment;

import musaico.kernel.memory.Memory;
import musaico.kernel.memory.MemoryException;
import musaico.kernel.memory.MemoryIdentifier;
import musaico.kernel.memory.MemoryPermission;
import musaico.kernel.memory.MemoryPermissions;
import musaico.kernel.memory.MemorySecurityException;

import musaico.region.Region;
import musaico.region.Size;

import musaico.security.Credentials;
import musaico.security.Permissions;
import musaico.security.Security;


/**
 * <p>
 * A simple memory manager, with no constraints on memory usage
 * and no re-use of freed buffers.
 * </p>
 *
 *
 * <br> </br>
 * <br> </br>
 *
 * <hr> </hr>
 *
 * <br> </br>
 * <br> </br>
 *
 *
 * <pre>
 * Copyright (c) 2009, 2010, 2012 Johann Tienhaara
 * All rights reserved.
 *
 * This file is part of Musaico.
 *
 * Musaico is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Musaico is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Musaico.  If not, see &lt;http://www.gnu.org/licenses/&gt;.
 * </pre>
 */
public class SimplePhysicalMemory
    implements PhysicalMemory, Serializable
{
    /** Synchronize critical sections on this token: */
    private final Serializable lock = new String ();

    /** The unique identifier for this memory inside the kernel. */
    private final MemoryIdentifier id;

    /** The security manager for this memory, controls who
     *  can and can't allocate or free memory. */
    private final Security<MemoryPermission> security;

    /** The maximum number of Fields to allocate.
     *  Zero means unlimited. */
    private final long maxAllocatedFields;

    /** The Buffers which are currently in use. */
    private final List<Buffer> allocatedBuffers;

    /** Each Buffer's size, in terms of # of fields. */
    private final Map<Buffer,Long> bufferLengths;

    /** The Field typing environment for this physical memory.
     *  Controls casting between field types and so on for the
     *  buffers allocated by this memory. */
    private final FieldTypingEnvironment environment;

    /** The current total number of allocated Fields.
     *  Mutable and changes over time. */
    private long numAllocatedFields;


    /**
     * <p>
     * Creates a new SimplePhysicalMemory with no limit to the number
     * of Fields which can be allocated.
     * </p>
     *
     * @param id The unique identifier for this memory.
     *           Must not be null.
     *
     * @param security The security manager for this memory.
     *                 Controls who can and who can't allocate
     *                 or free memory.  Must not be null.
     *
     * @param environment The kernel's Field typing environment,
     *                    which controls casting between Field types.
     *                    Must not be null.  Can be specific to this
     *                    Memory, though usually it is shared between
     *                    trusted kernel modules.
     */
    public SimplePhysicalMemory (
                                 MemoryIdentifier id,
                                 Security<MemoryPermission> security,
                                 FieldTypingEnvironment environment
                                 )
    {
        this ( id, security, environment, 0L );  // Unlimited # of fields.
    }


    /**
     * <p>
     * Creates a new SimplePhysicalMemory which can allocate
     * no more than the specified number of Fields.
     * </p>
     *
     * @param id The unique identifier for this memory.
     *           Must not be null.
     *
     * @param security The security manager for this memory.
     *                 Controls who can and who can't allocate
     *                 or free memory.  Must not be null.
     *
     * @param environment The kernel's Field typing environment,
     *                    which controls casting between Field types.
     *                    Must not be null.  Can be specific to this
     *                    Memory, though usually it is shared between
     *                    trusted kernel modules.
     *
     * @param max_fields The maximum number of Fields which can
     *                   be allocated as Buffer space at any
     *                   given time, or 0 for no limit.  Must be
     *                   0 or greater.
     */
    public SimplePhysicalMemory (
                                 MemoryIdentifier id,
                                 Security<MemoryPermission> security,
                                 FieldTypingEnvironment environment,
                                 long max_fields
                                 )
    {
        if ( id == null
             || security == null
             || environment == null
             || max_fields < 0L )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a SimplePhysicalMemory with id [%id%] security [%security%] environment [%environment%] max_fields [%max_fields%]",
                                                     "id", id,
                                                     "security", security,
                                                     "environment", environment,
                                                     "max_fields", max_fields );
        }

        this.id = id;
        this.security = security;
        this.maxAllocatedFields = max_fields;
        this.allocatedBuffers = new ArrayList<Buffer> ();
        this.bufferLengths = new HashMap<Buffer,Long> ();
        this.environment = environment;

        this.numAllocatedFields = 0L;
    }


    /**
     * @see musaico.kernel.memory.Memory#allocate(musaico.security.Credentials,musaico.region.Region)
     */
    @Override
    public Buffer allocate (
                            Credentials credentials,
                            Region region
                            )
        throws MemorySecurityException,
               MemoryException
    {
        if ( credentials == null
             || region == null )
        {
            throw new I18nIllegalArgumentException ( "Memory [%memory%] cannot allocate Buffer for credentials [%credentials%] region [%region%]",
                                                     "memory", this,
                                                     "credentials", credentials,
                                                     "region", region );
        }

        Permissions<MemoryPermission> requested_permissions =
            new MemoryPermissions ( credentials, this.id (),
                                    MemoryPermission.ALLOCATE );
        Permissions<MemoryPermission> granted_permissions =
            this.security ().request ( requested_permissions );
        if ( ! granted_permissions.isAllowed ( requested_permissions ) )
        {
            throw new MemorySecurityException ( "Permission denied.  Memory [%memory%] does not allow [%credentials%] to allocate memory",
                                                "memory", this,
                                                "credentials", credentials );
        }

        Size size = region.size ();
        long buffer_length = (long)
            region.space ().expr ( size ).ratio ( region.space ().one () );
        if ( buffer_length <= 0L )
        {
            throw new MemoryException ( "Memory [%memory%] cannot allocate Buffer for credentials [%credentials%] region [%region%]",
                                        "memory", this,
                                        "credentials", credentials,
                                        "region", region );
        }

        Buffer buffer;
        synchronized ( this.lock )
        {
            if ( this.maxAllocatedFields > 0 )
            {
                long num_remaining_fields =
                    this.maxAllocatedFields - this.numAllocatedFields;
                if ( buffer_length > num_remaining_fields )
                {
                    throw new MemoryException ( "Memory [%memory%] cannot allocate [%region_size%] fields ([%num_allocated_fields%] allocated, [%max_allocated_fields%] max)",
                                                "memory", this,
                                                "region_size", size,
                                                "num_allocated_fields", this.numAllocatedFields,
                                                "max_allocated_fields", this.maxAllocatedFields );
                }
            }

            if ( buffer_length > Integer.MAX_VALUE )
            {
                throw new MemoryException ( "Memory [%memory%] cannot allocate [%region_size%] fields (max SimpleBuffer size [%max_buffer_length%])",
                                            "memory", this,
                                            "region_size", size,
                                            "max_buffer_length", Integer.MAX_VALUE );
            }

            buffer = new SimpleBuffer ( this.environment,
                                        region );

            this.allocatedBuffers.add ( buffer );
            this.bufferLengths.put ( buffer, buffer_length );

            this.numAllocatedFields += buffer_length;
        }

        return buffer;
    }


    /**
     * @see musaico.kernel.memory.Memory#environment()
     */
    @Override
    public FieldTypingEnvironment environment ()
    {
        return this.environment;
    }


    /**
     * @see musaico.kernel.memory.Memory#free(musaico.security.Credentials,musaico.buffer.Buffer)
     */
    @Override
    public void free (
                      Credentials credentials,
                      Buffer buffer
                      )
        throws MemorySecurityException,
               MemoryException
    {
        if ( credentials == null
             || buffer == null )
        {
            throw new I18nIllegalArgumentException ( "Memory [%memory%] cannot free buffer [%buffer%] with credentials [%credentials%]",
                                                     "memory", this,
                                                     "buffer", buffer,
                                                     "credentials", credentials );
        }

        Permissions<MemoryPermission> requested_permissions =
            new MemoryPermissions ( credentials, this.id (),
                                    MemoryPermission.FREE );
        Permissions<MemoryPermission> granted_permissions =
            this.security ().request ( requested_permissions );
        if ( ! granted_permissions.isAllowed ( requested_permissions ) )
        {
            throw new MemorySecurityException ( "Permission denied.  Memory [%memory%] does not allow [%credentials%] to free memory",
                                                "memory", this,
                                                "credentials", credentials );
        }

        synchronized ( this.lock )
        {
            int buffer_index = this.allocatedBuffers.indexOf ( buffer );
            if ( buffer_index < 0 )
            {
                throw new MemoryException ( "Memory [%memory%] cannot free buffer [%buffer%]: No such buffer in this memory",
                                            "memory", this,
                                            "buffer", buffer );
            }

            long buffer_length = this.bufferLengths.get ( buffer );

            try
            {
                BufferTools.clear ( buffer );
            }
            catch ( BufferException e )
            {
                throw new MemoryException ( "Memory [%memory%] failed to clear freed buffer [%buffer%]",
                                            "memory", this,
                                            "buffer", buffer,
                                            "cause", e );
            }

            this.numAllocatedFields -= buffer_length;

            this.bufferLengths.remove ( buffer );
            this.allocatedBuffers.remove ( buffer_index );
        }
    }


    /**
     * @see musaico.kernel.memory.Memory#id()
     */
    @Override
    public MemoryIdentifier id ()
    {
        return this.id;
    }


    /**
     * @see musaico.kernel.memory.Memory#security()
     */
    @Override
    public Security<MemoryPermission> security ()
    {
        return this.security;
    }
}
