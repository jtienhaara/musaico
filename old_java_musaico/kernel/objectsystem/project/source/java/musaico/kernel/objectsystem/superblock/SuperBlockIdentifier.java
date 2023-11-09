package musaico.kernel.objectsystem.superblock;

import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Path;
import musaico.io.Reference;
import musaico.io.SimpleTypedIdentifier;

import musaico.io.references.SimpleSoftReference;

import musaico.kernel.KernelNamespaces;


/**
 * <p>
 * Uniquely identifies a SuperBlock (object system) within the
 * KernelNamespaces.SUPER_BLOCKS namespace.
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
 * Copyright (c) 2011 Johann Tienhaara
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
public class SuperBlockIdentifier
    extends SimpleTypedIdentifier<SuperBlock>
    implements Serializable
{
    /** A SuperBlockIdentifier pointing to no SuperBlock.
     *  Useful for stepping through an index of SuperBlockIdentifiers. */
    public static final SuperBlockIdentifier NONE =
        new SuperBlockIdentifier ( new Path ( "/no/super/block" ) );


    /**
     * <p>
     * Creates a new SuperBlockIdentifier with the specified
     * super block mount point.
     * </p>
     *
     * @param mount_point The path to the mount point of the SuperBlock.
     *                    Must not be null.
     *
     * @throws I18nIllegalArgumentException If the specified parameters
     *                                      are invalid (see above).
     */
    public SuperBlockIdentifier (
                                 Path mount_point
                                 )
        throws I18nIllegalArgumentException
    {
        super ( KernelNamespaces.SUPER_BLOCKS, mount_point,
                SuperBlock.class );
    }
}
