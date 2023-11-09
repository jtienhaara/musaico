package musaico.foundation.io;


import java.io.Serializable;


/**
 * <p>
 * A Least-Recently-Used list of objects.
 * </p>
 *
 * <p>
 * For example, in an LRU of memory pages, the page at the start
 * of the LRU is ripe for swapping, when memory starts to dwindle.
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
 * Copyright (c) 2010, 2012 Johann Tienhaara
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
public interface LRU<ITEM extends Object>
    extends Serializable
{
    /**
     * <p>
     * Adds or moves the specified item to the end of the list.
     * </p>
     *
     * @param item The ITEM to add or move to the end of the list.
     *             Must not be null.
     */
    public abstract void recent (
                                 ITEM item
                                 );


    /**
     * <p>
     * Returns the least-recently-used ITEM in the list,
     * if any.
     * </p>
     *
     * @return The least-recently-used ITEM, or null if
     *         there are currently no items in the list.
     */
    public abstract ITEM leastRecent ();


    /**
     * <p>
     * Removes the specified item from the LRU,
     * or does nothing if it is not in the list.
     * </p>
     *
     * @return The removed item, or null if it was not in the list.
     */
    public abstract ITEM remove (
                                 ITEM item
                                 );
}
