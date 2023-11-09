package musaico.kernel.memory.virtual;


import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;
import musaico.i18n.exceptions.I18nIllegalStateException;
import musaico.i18n.exceptions.TimeoutException;

import musaico.io.ReferenceCount;
import musaico.io.SimpleReferenceCount;

import musaico.buffer.Buffer;
import musaico.buffer.BufferException;
import musaico.buffer.BufferOverflowException;
import musaico.buffer.BufferType;

import musaico.field.Field;
import musaico.field.FieldTypingEnvironment;

import musaico.kernel.memory.Memory;
import musaico.kernel.memory.MemoryException;
import musaico.kernel.memory.MemoryRequest;
import musaico.kernel.memory.Segment;
import musaico.kernel.memory.SegmentFlag;
import musaico.kernel.memory.SegmentOperationException;
import musaico.kernel.memory.SegmentPermission;
import musaico.kernel.memory.SegmentPermissions;
import musaico.kernel.memory.SegmentSecurity;
import musaico.kernel.memory.SegmentSecurityException;

import musaico.kernel.memory.requests.ReadFieldRequest;
import musaico.kernel.memory.requests.WriteFieldRequest;

import musaico.region.Position;
import musaico.region.Region;

import musaico.security.Credentials;
import musaico.security.Permissions;
import musaico.security.SecurityRuntimeException;

import musaico.time.RelativeTime;

import musaico.types.Instance;
import musaico.types.RuntimeTypeException;
import musaico.types.Tags;
import musaico.types.Type;
import musaico.types.TypeException;

import musaico.types.instances.SimpleInstance;


/**
 * <p>
 * An area of memory backed by virtual Pages.
 * </p>
 *
 * <p>
 * Buffers are NOT thread-safe!  Create a ThreadSafeBufferWrapper
 * if you wish to call <code> set ( ... ) </code> from concurrent
 * threads.
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
 * Copyright (c) 2009, 2011, 2012 Johann Tienhaara
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
public class VirtualBuffer
    implements Buffer, Serializable
{
    /** The Segment of virtual Pages to which this virtual buffer
     *  points. */
    private final Segment segment;

    /** The credentials we use to access the segment securely. */
    private final Credentials owner;

    /** The Field typing environment for this virtual buffer.
     *  Controls casting between field types and so on. */
    private final FieldTypingEnvironment environment;

    /** The reference count of references pointing to this buffer. */
    private final ReferenceCount references;

    /** The Instance which takes care of typing and typecasting
     *  for us. */
    private final Instance            instance;


    /**
     * <p>
     * Creates a new VirtualBuffer for the specified Segment.
     * </p>
     *
     * @param environment The environment which dictates how
     *                    Fields in this Buffer are cast between
     *                    Types.  This might be the kernel's
     *                    private environment, or, in user
     *                    space, a user field typing environment,
     *                    and so on.  Must not be null.
     *
     * @param segment The Segment of memory to which this virtual
     *                buffer refers.  Must not be null.
     *
     * @param owner The owner of this VirtualBuffer, such as a
     *              module credentials or a user.  Must not be null.
     *
     * @throws I18nIllegalStateException If the underlying Musaico
     *                                   typing environment is borken.
     */
    public VirtualBuffer (
                          FieldTypingEnvironment environment,
                          Segment segment,
                          Credentials owner
                          )
        throws I18nIllegalStateException
    {
        this ( environment, segment, owner,
               new BufferType () );
    }


    /**
     * <p>
     * Creates a new VirtualBuffer for the specified Segment.
     * </p>
     *
     * @param environment The environment which dictates how
     *                    Fields in this Buffer are cast between
     *                    Types.  This might be the kernel's
     *                    private environment, or, in user
     *                    space, a user field typing environment,
     *                    and so on.  Must not be null.
     *
     * @param segment The Segment of memory to which this virtual
     *                buffer refers.  Must not be null.
     *
     * @param owner The owner of this VirtualBuffer, such as a
     *              module credentials or a user.  Must not be null.
     *
     * @param buffer_type The type describing this Buffer, possibly
     *                    just vanilla BufferType.  Must not be null.
     *
     * @throws I18nIllegalStateException If the underlying Musaico
     *                                   typing environment is borken.
     */
    public VirtualBuffer (
                          FieldTypingEnvironment environment,
                          Segment segment,
                          Credentials owner,
                          BufferType buffer_type
                          )
        throws I18nIllegalStateException
    {
        if ( environment == null
             || segment == null
             || owner == null
             || buffer_type == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a VirtualBuffer with environment [%environment%] segment [%segment%] owner [%owner%] buffer type [%buffer_type%]",
                                                     "environment", environment,
                                                     "segment", segment,
                                                     "owner", owner,
                                                     "buffer_type", buffer_type );
        }

        this.environment = environment;
        this.segment = segment;

        // !!! this.segment.references ().increment ();

        this.owner = owner;
        this.references = new SimpleReferenceCount ();

        try
        {
            this.instance = new SimpleInstance ( this.environment,
                                                 buffer_type,
                                                 this );
        }
        catch ( TypeException e )
        {
            throw new I18nIllegalStateException ( "Cannot create a VirtualBuffer with environment [%environment%] segment [%segment%] owner [%owner%] buffer type [%buffer_type%]",
                                                  "environment", environment,
                                                  "segment", segment,
                                                  "owner", owner,
                                                  "buffer_type", buffer_type,
                                                  "cause", e );
        }
    }


    /**
     * @see java.lang.Object#finalize()
     */
    /* !!!
    protected void finalize ()
        throws Throwable
    {
        this.segment.references ().decrement ();

        super.finalize ();
    }
    !!! */


    /**
     * @see musaico.buffer.Buffer#environment()
     */
    public FieldTypingEnvironment environment ()
    {
        return this.environment;
    }


    /**
     * <p>
     * When this buffer is to be freed, the segment backing
     * it must be freed as well.  Called by SimpleVirtualMemory.free().
     * </p>
     */
    protected void freeSegment ()
    {
        this.segment.free ();
    }


    /**
     * @see musaico.buffer.Buffer#get(Position)
     *
     * @throw SecurityRuntimeException If the segment backing this
     *                                 virtual buffer is marked
     *                                 un-readable.
     */
    public Field get (
                      Position position
                      )
        throws SecurityRuntimeException
    {
        // Request the Segment to swap in (if necessary) then read
        // the specified position.
        ReadFieldRequest read =
            new ReadFieldRequest ( this.owner,
                                   this.segment.id (),
                                   new RelativeTime ( 1L, 0L ), // 1s !!!!!!!
                                   position );
        try
        {
            this.segment.request ( read ).waitFor ();
        }
        catch ( MemoryException e )
        {
            // !!! Log it
            return Field.NULL;
        }
        catch ( TimeoutException e )
        {
            // !!! Log it.
            return Field.NULL;
        }

        return read.response ();
    }


    /**
     * @see musaico.types.Instance#isa(java.lang.Class)
     */
    @Override
    public boolean isa (
                        Class<Type<?>> type_class
                        )
    {
        return this.instance.isa ( type_class );
    }


    /**
     * @see musaico.buffer.Buffer#references()
     */
    public ReferenceCount references ()
    {
        return this.references;
    }


    /**
     * @see musaico.buffer.Buffer#region()
     */
    public Region region ()
    {
        return this.segment.region ();
    }


    /**
     * @see musaico.buffer.Buffer#set(Position,Field)
     */
    public VirtualBuffer set (
                              Position position,
                              Field field
                              )
        throws BufferException
    {
        // Request the Segment to swap in (if necessary) then write
        // the specified field to the specified position.
        WriteFieldRequest write =
            new WriteFieldRequest ( this.owner,
                                    this.segment.id (),
                                    new RelativeTime ( 1L, 0L ), // 1s !!!!!!!
                                    position,
                                    field );
        try
        {
            this.segment.request ( write ).waitFor ();
        }
        catch ( MemoryException e )
        {
            // !!! Log it
            return this;
        }
        catch ( TimeoutException e )
        {
            // !!! Log it.
            return this;
        }

        return this;
    }


    /**
     * @see musaico.types.Instance#tags()
     */
    @Override
    public Tags tags ()
    {
        return this.instance.tags ();
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString ()
    {
        return "VirtualBuffer"
            + this.hashCode () + " [ " + this.region ().size () + " ]";
    }


    /**
     * @see musaico.types.Instance#type()
     */
    @Override
    public 
        <STORAGE_VALUE extends Object>
                               Type<STORAGE_VALUE> type ()
    {
        return this.instance.type ();
    }


    /**
     * @see musaico.types.Instance#validate()
     */
    @Override
    public boolean validate ()
        throws RuntimeTypeException
    {
        return this.instance.validate ();
    }


    /**
     * @see musaico.types.Instance#value()
     */
    @Override
    public Object value ()
    {
        return this.instance.value ();
    }


    /**
     * @see musaico.types.Instance#value(java.lang.Class)
     */
    @Override
    public 
        <RAW_VALUE extends Object,STORAGE_VALUE extends Object>
                           RAW_VALUE value (
                                            Class<RAW_VALUE> raw_class
                                            )
        throws RuntimeTypeException
    {
        return this.instance.value ( raw_class );
    }


    /**
     * @see musaico.types.Instance#value(Type<STORAGE_VALUE>)
     */
    @Override
    public 
        <STORAGE_VALUE extends Object>
                               STORAGE_VALUE value (
                                                    Type<STORAGE_VALUE> type
                                                    )
        throws RuntimeTypeException
    {
        return this.instance.value ( type );
    }


    /**
     * @see musaico.types.Instance#value(Class<RAW_VALUE>,Type<STORAGE_TYPE>)
     */
    @Override
    public 
        <RAW_VALUE extends Object,STORAGE_VALUE extends Object>
                           RAW_VALUE value (
                                            Class<RAW_VALUE> raw_class,
                                            Type<STORAGE_VALUE> type
                                            )
        throws RuntimeTypeException
    {
        return this.instance.value ( raw_class, type );
    }
}
