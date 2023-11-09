package musaico.region;

import java.io.Serializable;


/**
 * <p>
 * A position within a region.
 * </p>
 *
 * <p>
 * Depending on the implementation, a Position might represent an integer
 * index, or a String field/column name, or a date/time, or a position in
 * 4-dimensional space, and so on.
 * </p>
 *
 * <p>
 * A Position is often used as a pointer into a data structure.
 * For example, the 5th element of an array, or the "first_name"
 * field of a structure, or the "last_name" column of a row from a table,
 * or 11:00pm Thursday Sept 19 in a calendar, or { 1, 0, -1, 0 } in a
 * 4-dimensional spatial dataset, and so on.
 * </p>
 *
 * <p>
 * The Position abstraction allows different data structures and
 * organizations to be accessed with the same basic type, the same way
 * the int address offset is used in C to index myriad data structures.
 * The user of the data structure need not worry what the underlying
 * organization is, while the structure itself need not be tied down
 * to an overly primitive addressing mechanism.
 * </p>
 *
 *
 * <p>
 * In Java every position must implement <code> equals () </code>
 * and <code> hashCode () </code>, in order to play well with
 * hash sets and hash maps.
 * </p>
 *
 * <p>
 * In Java every Position must be Serializable in order to
 * play nicely over RMI.
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
 * Copyright (c) 2011, 2012, 2013 Johann Tienhaara
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
public interface Position
    extends Serializable
{
    // Every Position must implement
    // java.lang.Object#equals(java.lang.Object).

    // Every Position must implement java.lang.Object#hashCode().

    /**
     * @return The addressing Space whence this Position sprang, such
     *         as an integer index space, or a String field / column
     *         name space, or a date/time space, or a 4-dimensional
     *         space and so on.  Never null.
     */
    public abstract Space space ();
}
