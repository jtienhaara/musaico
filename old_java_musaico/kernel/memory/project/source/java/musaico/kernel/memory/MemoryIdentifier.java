package musaico.kernel.memory;

import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Reference;
import musaico.io.SimpleTypedIdentifier;

import musaico.io.references.SimpleSoftReference;

import musaico.kernel.KernelNamespaces;


/**
 * <p>
 * Uniquely identifies a Memory within the
 * KernelNamespaces.MEMORY namespace, such as "physical
 * memory for kernel modules", "virtual memory for users", and
 * so on.
 * </p>
 *
 *
 * <p>
 * In Java, every Identifier must be Serializable in order
 * to play nicely across RMI.
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
 * Copyright (c) 2012 Johann Tienhaara
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
public class MemoryIdentifier
    extends SimpleTypedIdentifier<Memory>
    implements Serializable
{
    /** A MemoryIdentifier pointing to physical memory for kernel Modules. */
    public static final MemoryIdentifier PHYSICAL_FOR_KERNEL_MODULES =
        new MemoryIdentifier ( "physical_memory_for_kernel_modules" );

    /** A MemoryIdentifier pointing to virtual memory for user space. */
    public static final MemoryIdentifier VIRTUAL_MEMORY_FOR_USERS =
        new MemoryIdentifier ( "virtual_memory_for_users" );


    /**
     * <p>
     * Creates a new MemoryIdentifier with the specified
     * super block mount point.
     * </p>
     *
     * @param memory_name The name of the Memory whose identifier
     *                    is being created.  Must not be null.
     *
     * @throws I18nIllegalArgumentException If the specified parameters
     *                                      are invalid (see above).
     */
    public MemoryIdentifier (
                             String memory_name
                             )
        throws I18nIllegalArgumentException
    {
        this ( new SimpleSoftReference<String> ( memory_name ) );
    }


    /**
     * <p>
     * Creates a new MemoryIdentifier with the specified
     * super block mount point.
     * </p>
     *
     * @param memory_name The name of the Memory whose identifier
     *                    is being created.  Must not be null.
     *
     * @throws I18nIllegalArgumentException If the specified parameters
     *                                      are invalid (see above).
     */
    protected MemoryIdentifier (
                                Reference memory_name
                                )
        throws I18nIllegalArgumentException
    {
        super ( KernelNamespaces.MEMORY,
                memory_name,
                Memory.class );
    }
}
