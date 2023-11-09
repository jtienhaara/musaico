package musaico.foundation.io;

import java.io.Serializable;


/**
 * <p>
 * Represents a reference to some object inside some namespace or other.
 * </p>
 *
 * <p>
 * The format of a Reference is origin-dependent.
 * </p>
 *
 * <p>
 * For example, a Reference might be some combination of text and
 * numeric indexes into some array of objects.  Or it might be
 * a hard reference directly to an object itself.
 * </p>
 *
 * <p>
 * And so on.
 * </p>
 *
 *
 * <p>
 * In Java, all References are Serializable in order to play nicely
 * with RMI.
 * </p>
 *
 * <p>
 * Also every Reference must implement equals() and hashCode() in Java,
 * in order to ensure easy use in hash tables.
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
 * Copyright (c) 2009, 2011 Johann Tienhaara
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
public interface Reference
    extends Serializable
{
    /**
     * @see java.lang.Object#equals(Object)
     */
    public abstract boolean equals (
                                    Object obj
                                    );


    /**
     * @see java.lang.Object#hashCode()
     */
    public abstract int hashCode ();


    /**
     * @see java.lang.Object#toString()
     */
    public abstract String toString ();
}
