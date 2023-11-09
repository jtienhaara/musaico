package musaico.kernel.objectsystem.objectsystemtype;

import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Reference;
import musaico.io.SimpleTypedIdentifier;

import musaico.io.references.SimpleSoftReference;

import musaico.kernel.KernelNamespaces;


/**
 * <p>
 * Uniquely identifies an ObjectSystemType within the
 * KernelNamespaces.OBJECT_SYSTEM_TYPES namespace.
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
public class ObjectSystemTypeIdentifier
    extends SimpleTypedIdentifier<ObjectSystemType>
    implements Serializable
{
    /** An ObjectSystemTypeIdentifier pointing to no object system type.
     *  Useful for stepping through an index of ObjectSystemTypeIdentifiers. */
    public static final ObjectSystemTypeIdentifier NONE =
        new ObjectSystemTypeIdentifier ( new SimpleSoftReference<String> ( "no_object_system_type" ) );


    /**
     * <p>
     * Creates a new ObjectSystemTypeIdentifier with the specified
     * object system type name.
     * </p>
     *
     * @param object_system_type_name The name of the object system type.
     *                                Must not be null.
     *
     * @throws I18nIllegalArgumentException If the specified parameters
     *                                      are invalid (see above).
     */
    public ObjectSystemTypeIdentifier (
                                       String object_system_type_name
                                       )
        throws I18nIllegalArgumentException
    {
        this ( new SimpleSoftReference<String> ( object_system_type_name ) );
    }


    /**
     * <p>
     * Creates a new ObjectSystemTypeIdentifier with the specified name.
     * </p>
     *
     * <p>
     * This method is provided for sub-classing ObjectSystemTypeIdentifier
     * with a different (non-String) type of name.  Use it wisely.
     * </p>
     *
     * @param object_system_type_name The unique name of the object
     *                                system type.
     *                                Must not be null.
     *
     * @throws I18nIllegalArgumentException If the specified parameters
     *                                      are invalid (see above).
     */
    protected ObjectSystemTypeIdentifier (
                                          Reference name
                                          )
    {
        super ( KernelNamespaces.OBJECT_SYSTEM_TYPES, name,
                ObjectSystemType.class );
    }
}
