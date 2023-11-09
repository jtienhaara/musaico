package musaico.kernel.objectsystem;

import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Identifier;
import musaico.io.Reference;
import musaico.io.SimpleTypedIdentifier;

import musaico.io.references.SimpleSoftReference;

import musaico.kernel.KernelNamespaces;


/**
 * <p>
 * Uniquely identifies a class of Records (object records, flat
 * records, symbolic links, and so on) within the
 * KernelNamespaces.RECORD_TYPES
 * namespace.
 * </p>
 *
 *
 * <p>
 * See also the derived type identifier for relation record types:
 * </p>
 *
 * @see musaico.kernel.objectsystem.records.RelationTypeIdentifier
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
public class RecordTypeIdentifier
    extends SimpleTypedIdentifier<Class>
    implements Serializable
{
    /** A RecordTypeIdentifier that is not implemented by any Record class.
     *  Useful for stepping through an index of RecordTypeIdentifiers. */
    public static final RecordTypeIdentifier NONE =
        new RecordTypeIdentifier ( new SimpleSoftReference<String> ( "no_record_type" ) );


    /**
     * <p>
     * Creates a new RecordTypeIdentifier with the specified
     * record type name.
     * </p>
     *
     * @param record_type_name The name of the record type, such
     *                         as "object" or "flat_record" or
     *                         "symbolic_link" and so on.
     *                         Must not be null.
     *
     * @throws I18nIllegalArgumentException If the specified parameters
     *                                      are invalid (see above).
     */
    public RecordTypeIdentifier (
                                 String record_type_name
                                 )
        throws I18nIllegalArgumentException
    {
        this ( new SimpleSoftReference<String> ( record_type_name ) );
    }


    /**
     * <p>
     * Creates a new RecordTypeIdentifier with the specified name.
     * </p>
     *
     * <p>
     * This method is provided for sub-classing RecordTypeIdentifier
     * with a different (non-String) type of name.  Use it wisely.
     * </p>
     *
     * @param record_type_name The unique name of the record type.
     *                         Must not be null.
     *
     * @throws I18nIllegalArgumentException If the specified parameters
     *                                      are invalid (see above).
     */
    protected RecordTypeIdentifier (
                                    Reference name
                                    )
    {
        super ( KernelNamespaces.RECORD_TYPES, name, Class.class );
    }


    /**
     * <p>
     * Constructor for the RelationTypeIdentifier class.
     * </p>
     *
     * <p>
     * Protected.
     * </p>
     *
     * <p>
     * DO NOT USE THIS CONSTRUCTOR OUTSIDE OF RelationTypeIdentifier!
     * You have plenty of other constructors to choose from to generate
     * a healthy RecordIdentifier.
     * </p>
     */
    protected RecordTypeIdentifier (
                                    Identifier parent_namespace,
                                    Reference name
                                    )
    {
        super ( parent_namespace, name, Class.class );
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

        return Record.class.isAssignableFrom ( (Class) object );
    }
}
