package musaico.kernel.memory;


import musaico.buffer.Buffer;

import musaico.field.FieldTypingEnvironment;

import musaico.io.Reference;
import musaico.io.ReferenceCount;

import musaico.region.Region;

import musaico.security.Credentials;
import musaico.security.Security;


/**
 * <p>
 * Provides buffer management for memory allocation and freeing
 * and so on.
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
 * Copyright (c) 2009 Johann Tienhaara
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
public interface Memory
{
    /**
     * <p>
     * Allocates physical or virtual memory, depending on the
     * implementation, by creating a Buffer of the specified
     * size (in terms of Fields) and layout.
     * </p>
     *
     * <p>
     * Typically the Region's Size is the only aspect the
     * caller is interested in specifying.  However other
     * aspects of the layout of the Buffer to be allocated
     * (such as the start and end Positions, for a Buffer
     * representing paged memory, for example) can also be
     * set if needed.
     * </p>
     *
     * @param credentials The identifier of the caller which
     *                    will be used to grant (or not)
     *                    permission.  For example, a module
     *                    credentials, or a user credentials.
     *                    Must not be null.
     *
     * @param region The Region describing the size and layout
     *               of the Buffer to allocate.  Must not be null.
     *
     * @return The newly allocated Buffer.  Never null.
     *         Always contains the specified Region.
     *
     * @throws MemorySecurityException If this Memory does not
     *                                 allow the specified
     *                                 Credentials to perform
     *                                 this operation.
     *
     * @throws MemoryException If the buffer cannot be allocated
     *                         for some reason (such as lack of
     *                         available physical memory).
     */
    public abstract Buffer allocate (
                                     Credentials credentials,
                                     Region region
                                     )
        throws MemorySecurityException, MemoryException;


    /**
     * <p>
     * Returns the fields typing environment which can be used to create
     * new Fields for this Memory's buffers.
     * </p>
     *
     * @return This Memory's typing environment.  Never null.
     */
    public abstract FieldTypingEnvironment environment ();


    /**
     * <p>
     * Frees the specified Buffer of memory.
     * </p>
     *
     * @param credentials The identifier of the caller which
     *                    will be used to grant (or not)
     *                    permission.  For example, a module
     *                    credentials, or a user credentials.
     *                    Must not be null.
     *
     * @param buffer The Buffer to free.  Must not be null.
     *               Must have been allocated by this Memory.
     *
     * @throws MemorySecurityException If this Memory does not
     *                                 allow the specified
     *                                 Credentials to perform
     *                                 this operation.
     *
     * @throws MemoryException If the specified buffer cannot be
     *                         freed (for example, because it is
     *                         still in use, or because it was
     *                         not allocated by this Memory).
     */
    public abstract void free (
                               Credentials credentials,
                               Buffer buffer
                               )
        throws MemorySecurityException, MemoryException;


    /**
     * <p>
     * Returns the identifier for this memory (such as "physical
     * memory for kernel modules", "virtual memory for users",
     * and so on).
     * </p>
     *
     * @return This memory's unique identifier within the kernel.
     *         Never null.
     */
    public abstract MemoryIdentifier id ();


    /**
     * <p>
     * Returns the security manager for this memory, which grants
     * allocate and free permissions to potential users of this
     * memory.
     * </p>
     *
     * @return This Memory's security manager.  Never null.
     */
    public abstract Security<MemoryPermission> security ();
}
