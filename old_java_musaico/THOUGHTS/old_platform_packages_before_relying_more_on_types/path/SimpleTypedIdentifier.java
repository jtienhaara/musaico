package musaico.foundation.io;

import java.io.Serializable;


import musaico.foundation.i18n.exceptions.I18nIllegalArgumentException;


/**
 * <p>
 * Uniquely identifies something within a specific namespace and
 * which must be of a particular class (String, Integer, Date,
 * and so on).
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
public class SimpleTypedIdentifier<IDENTIFIED_OBJECT>
    extends SimpleIdentifier
    implements TypedIdentifier<IDENTIFIED_OBJECT>
{
    /** The class of object which is identified by this typed
     *  identifier. */
    private final Class<IDENTIFIED_OBJECT> identifiedObjectClass;


    /**
     * <p>
     * Creates a new SimpleTypedIdentifier with the specified parent
     * namespace and unique name within that namespace and
     * class of object which is identified.
     * </p>
     *
     * @param parent_namespace The namespace in which the specified
     *                         name is unique.  Must not be null.
     *
     * @param name The unique name within the namespace.
     *             Must not be null.  Note that multiple copies of
     *             the same unique identifier may be created; checking
     *             for uniqueness must be done elsewhere (such as
     *             in a lookup of objects by unique identifiers).
     *
     * @param identified_object_class The class of object which is
     *                                identified by this typed identifier,
     *                                such as String.class or
     *                                Integer.class and so on.
     *
     * @throws I18nIllegalArgumentException If the parameters are
     *                                      invalid (see above).
     */
    public SimpleTypedIdentifier (
                                  Identifier parent_namespace,
                                  Reference name,
                                  Class<IDENTIFIED_OBJECT> identified_object_class
                                  )
    {
        super ( parent_namespace, name );

        if ( identified_object_class == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a SimpleTypedIdentifier with parent namespace [%parent_namespace%] name [%name%] identified object class [%identified_object_class%]",
                                                     "parent_namespace", parent_namespace,
                                                     "name", name,
                                                     "identified_object_class", identified_object_class );
        }

        this.identifiedObjectClass = identified_object_class;
    }


    /**
     * @see musaico.foundation.io.TypedIdentifier#identifiedObjectClass()
     */
    public Class<IDENTIFIED_OBJECT> identifiedObjectClass ()
    {
        return this.identifiedObjectClass;
    }


    /**
     * @see musaico.foundation.io.TypedIdentifier#identifiedObjectClass()
     */
    public boolean isIdentifiable (
                                   Object object
                                   )
    {
        return this.identifiedObjectClass.isInstance ( object );
    }
}
