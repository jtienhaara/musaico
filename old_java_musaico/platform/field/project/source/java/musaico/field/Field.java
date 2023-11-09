package musaico.field;


import java.io.Serializable;


import musaico.types.Instance;
import musaico.types.Type;
import musaico.types.TypeException;


/**
 * <p>
 * Represents an atomic field (identifier and value).
 * </p>
 *
 * <p>
 * Each field must have a String id () and some sort of
 * value ().  For example, an integer field in C or Java
 * might have an int value().
 * </p>
 *
 * <p>
 * The Musaico type systems provide the means for casting
 * between Types.  For instance, if a Field stores a String
 * but the developer wants to cast it to an int, simply
 * call <code> int x = field.value ( Integer.class ) </code>.
 * </p>
 *
 * @see musaico.types.Type
 *
 * <p>
 * No Field may ever throw an exception unless it was thrown
 * by the Field's underlying exception handler.  A Field is
 * used to represent atomic data akin to a byte.  Imagine
 * how difficult it would be to code a specific file format
 * reader, if each byte read necessitated compile-time
 * and run-time exception checking!
 * </p>
 *
 *
 * <p>
 * Platforms may impose additional constraints on Fields.
 * </p>
 *
 * <p>
 * In Java, it is recommended that Fields and their contents
 * be made Serializable whenever possible, in order to play
 * nicely over RMI.
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
public interface Field
    extends Instance, Serializable
{
    /** Null field. */
    public static final Field NULL =
        new NullField ();


    /**
     * <p>
     * Returns the identifier for this Field.
     * </p>
     *
     * <p>
     * The meaning of the identifier, and its optional uniqueness,
     * is dependent on the source of the field.  Generally
     * speaking, Fields are not unique.
     * </p>
     *
     * @return the String identifier for this Field.
     */
    public abstract String id ();


    // Each Field implementation must also implement all the
    // methods of the musaico.types.Instance interface.
}
