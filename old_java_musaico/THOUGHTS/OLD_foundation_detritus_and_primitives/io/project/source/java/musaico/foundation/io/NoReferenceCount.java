package musaico.foundation.io;


/**
 * <p>
 * Tracks references to nothing.
 * </p>
 *
 * <p>
 * None of the methods do anything useful.  This is like a reference
 * counter for null memory space.  It represents the reference
 * count to nowhere.
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
public class NoReferenceCount
    implements ReferenceCount
{
    /**
     * @see musaico.foundation.io.ReferenceCount#increment()
     */
    public final long increment ()
    {
        return 0L;
    }


    /**
     * @see musaico.foundation.io.ReferenceCount#decrement()
     */
    public final long decrement ()
    {
        return 0L;
    }


    /**
     * @see musaico.foundation.io.ReferenceCount#count()
     */
    public final long count ()
    {
        return 0L;
    }
}
