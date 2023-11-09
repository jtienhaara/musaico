package musaico.foundation.hash;


import java.io.Serializable;


/**
 * <p>
 * Represents a hash of data (such as one-way hashes like SHA-1,
 * MD5, and so on).
 * </p>
 *
 *
 * <p>
 * In Java, every Hash must be Serializable in order to play nicely
 * over RMI.
 * </p>
 *
 * <p>
 * In addition, every Hash must override the following method, in order
 * to play nicely in Collections such as Maps:
 * </p>
 *
 * <ul>
 *   <li> <code> equals ( Object ) </code > </li>
 *   <li> <code> hashCode () </code > </li>
 *   <li> <code> toString () </code > </li>
 * </ul>
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
 * Copyright (c) 2010 Johann Tienhaara
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
public interface Hash
    extends Serializable
{
    /** No hash (an empty useless hash of any object). */
    public static final Hash NONE = new NoHash ();


    /**
     * <p>
     * Returns the array of bytes which this Hash represents.
     * </p>
     *
     * <p>
     * The Hash must create a copy of the internal byte array it stores
     * (if any) before returning a value to the caller.
     * </p>
     */
    public abstract byte [] bytes ();
}
