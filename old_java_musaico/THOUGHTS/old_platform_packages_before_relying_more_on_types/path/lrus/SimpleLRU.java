package musaico.foundation.io.lrus;


import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


import musaico.foundation.io.LRU;


/**
 * <p>
 * A simple, fast LRU (Least-Recently-Used) list.
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
public class SimpleLRU<ITEM>
    implements LRU<ITEM>, Serializable
{
    /** Synchronize on this lock for all critical sections. */
    private final Serializable lock = new String ();

    /** The list of least-recently-used items.
     *  The head (0) is the least-recently-used item. */
    private final List<ITEM> leastRecentlyUsed =
        new ArrayList<ITEM> ();


    /**
     * @see musaico.foundation.io.LRU#recent(Object)
     */
    public void recent (
                        ITEM item
                        )
    {
        synchronized ( this.lock )
        {
            this.leastRecentlyUsed.remove ( item );
            this.leastRecentlyUsed.add ( item );
        }
    }


    /**
     * @see musaico.foundation.io.LRU#leastRecent()
     */
    public ITEM leastRecent ()
    {
        return this.leastRecentlyUsed.get ( 0 );
    }


    /**
     * @see musaico.foundation.io.LRU#remove(Object)
     */
    public ITEM remove (
                        ITEM item
                        )
    {
        synchronized ( this.lock )
        {
            if ( this.leastRecentlyUsed.remove ( item ) )
            {
                return item;
            }
            else
            {
                return null;
            }
        }
    }
}
