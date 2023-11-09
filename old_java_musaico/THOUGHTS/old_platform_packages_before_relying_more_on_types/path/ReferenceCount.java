package musaico.foundation.io;


/**
 * <p>
 * Tracks references to an object.
 * </p>
 *
 * <p>
 * Call increment () to add one to the reference count for the object.
 * </p>
 *
 * <p>
 * Call decrement () to subtract one from the reference count.
 * </p>
 *
 * <p>
 * Call count () to return the number of references to the object.
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
public interface ReferenceCount
{
    /**
     * <p>
     * Increments the reference count by one.
     * </p>
     *
     * @return The new reference count.
     */
    public abstract long increment ();


    /**
     * <p>
     * Decrements the reference count by one.
     * </p>
     *
     * @return The new reference count.
     */
    public abstract long decrement ();


    /**
     * <p>
     * Returns the current number of references.
     * </p>
     */
    public abstract long count ();
}
