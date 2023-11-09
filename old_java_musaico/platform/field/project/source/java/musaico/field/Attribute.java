package musaico.field;

import java.io.Serializable;


import musaico.region.Position;

import musaico.region.array.ArraySpace;


/**
 * <p>
 * An Attribute declares the name, type and position of
 * a Field in each instance of some data structure (typically
 * a Buffer).
 * </p>
 *
 * <p>
 * For example, an <code> Attribute&lt;String&gt; </code>
 * might define the name, type and position of a
 * "first name" attribute in a "person" data structure.
 * Or an <code> Attribute&lt;AbsoluteTime&gt; </code>
 * might define the name, type and position of a
 * "date of birth" attribute in the same "person" data
 * structure.  And so on.
 * </p>
 *
 * <p>
 * The Attribute for a known data structure can be used
 * to quickly retrieve the Field instance from an instance
 * of that data structure.  For example:
 * </p>
 *
 * <pre>
 *     Attribute<String> first_name_attr = ...;
 *     Attribute<AbsoluteTime>   date_of_birth_attr = ...;
 *
 *     Buffer person = ...;
 *     Field first_name = person.get ( first_name_attr.position () );
 *     Field date_of_birth = person.get ( date_of_birth_attr.position () );
 * </pre>
 *
 *
 * <p>
 * In Java every Attribute must be Serializable in order
 * to play nicely over RMI in a distributed object system
 * or kernel.
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
public class Attribute<STORAGE_TYPE extends Serializable>
    implements Serializable
{
    /** No attribute at all.  Use instead of null. */
    public static final Attribute<Serializable> NONE =
        new Attribute ( Serializable.class );


    /** The id of the attribute, such as "first_name"
     *  or "date_of_birth" and so on. */
    private final String id;

    /** The default Class of the attribute, such as
     *  <code> String.class </code> for first name,
     * or <code> AbsoluteTime.class </code> for date of
     * birth, and so on. */
    private final Class<STORAGE_TYPE> type;

    /** The Position of the attribute within the data
     *  structure, such as <code> ArrayPosition ( 13L ) </code>
     *  and so on. */
    private final Position position;


    /**
     * <p>
     * Creates a new Attribute definition with the
     * specified id, type and position within a data structure.
     * </p>
     *
     * @param id The id of the attribute, such as "first_name"
     *           or "date_of_birth" and so on.  Must not be null.
     *
     * @param type The storage type for attribute, such
     *             as String.class or AbsoluteTime.class and so on.
     *             Must not be null.
     *
     * @param position The position within the data structure
     *                 where the attribute resides,
     *                 such as ArrayPosition(17) and so on.
     *                 Must not be null.
     */
    public Attribute (
                      String id,
                      Class<STORAGE_TYPE> type,
                      Position position
                      )
    {
        this.id = id;
        this.type = type;
        this.position = position;
    }


    /**
     * <p>
     * Creates the "no attribute" attribute.
     * </p>
     */
    private Attribute (
                       Class<STORAGE_TYPE> storage_type_class
                       )
    {
        this ( "NONE",
               storage_type_class,
               ArraySpace.STANDARD.outOfBounds () );
    }


    /**
     * <p>
     * Returns this attribute definition's identifier,
     * such as "first_name" or "date_of_birth" and so on.
     * </p>
     *
     * @return This attribute definition's id.
     *         Never null.
     */
    public String id ()
    {
        return this.id;
    }


    /**
     * <p>
     * Returns this attribute definition's storage type,
     * such as String.class or AbsoluteTime.class and so on.
     * </p>
     *
     * @return This attribute definition's type.
     *         Never null.
     */
    public Class<STORAGE_TYPE> type ()
    {
        return this.type;
    }


    /**
     * <p>
     * Returns this attribute definition's position within a
     * data structure, such as ArrayIndex(11) and so on.
     * </p>
     *
     * @return This attribute definition's position within
     *         each data structure.  Never null.
     */
    public Position position ()
    {
        return this.position;
    }
}
