package musaico.kernel.objectsystem.onodes;

import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Reference;
import musaico.io.SimpleTypedIdentifier;

import musaico.io.references.SimpleSoftReference;

import musaico.kernel.KernelNamespaces;

import musaico.kernel.objectsystem.RecordTypeIdentifier;


/**
 * <p>
 * Uniquely identifies a class of Relations (symbolic links,
 * unions, and so on) within the KernelNamespaces.RELATION_TYPES
 * namespace.
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
public class RelationTypeIdentifier
    extends RecordTypeIdentifier
    implements Serializable
{
    /** A RelationTypeIdentifier that is not implemented by any
     *  Relation class.  Useful for stepping through an index
     *  of RelationTypeIdentifiers. */
    public static final RelationTypeIdentifier NONE =
        new RelationTypeIdentifier ( new SimpleSoftReference<String> ( "no_relation_type" ) );


    /**
     * <p>
     * Creates a new RelationTypeIdentifier with the specified
     * relation type name.
     * </p>
     *
     * @param relation_type_name The name of the relation type, such
     *                           as "symbolic_link" and so on.
     *                           Must not be null.
     *
     * @throws I18nIllegalArgumentException If the specified parameters
     *                                      are invalid (see above).
     */
    public RelationTypeIdentifier (
                                   String relation_type_name
                                   )
        throws I18nIllegalArgumentException
    {
        this ( new SimpleSoftReference<String> ( relation_type_name ) );
    }


    /**
     * <p>
     * Creates a new RelationTypeIdentifier with the specified name.
     * </p>
     *
     * <p>
     * This method is provided for sub-classing RelationTypeIdentifier
     * with a different (non-String) type of name.  Use it wisely.
     * </p>
     *
     * @param relation_type_name The unique name of the relation type.
     *                           Must not be null.
     *
     * @throws I18nIllegalArgumentException If the specified parameters
     *                                      are invalid (see above).
     */
    protected RelationTypeIdentifier (
                                      Reference name
                                      )
    {
        super ( KernelNamespaces.RELATION_TYPES, name );
    }


    /**
     * @see musaico.io.TypedIdentifier#isIdentifiable(Object)
     */
    @Override
    public boolean isIdentifiable (
                                   Object object
                                   )
    {
        if ( object == null
             || ! ( object instanceof Class ) )
        {
            return false;
        }

        return Relation.class.isAssignableFrom ( (Class) object );
    }
}
