package musaico.foundation.io;

import java.io.Serializable;


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
public class SimpleReferenceCount
    implements ReferenceCount, Serializable
{
    /** Synchronize all critical sections on this lock: */
    private final Serializable lock = new String ();

    /** Count the references to the object: */
    private long numReferences = 0;


    /**
     * @see musaico.foundation.io.ReferenceCount#count()
     */
    public long count ()
    {
        return this.numReferences;
    }


    /**
     * @see musaico.foundation.io.ReferenceCount#increment()
     */
    public long increment ()
    {
        synchronized ( this.lock )
        {
            this.numReferences ++;
            return this.numReferences;
        }
    }


    /**
     * @see musaico.foundation.io.ReferenceCount#decrement()
     */
    public long decrement ()
    {
        synchronized ( this.lock )
        {
            this.numReferences --;
            return this.numReferences;
        }
    }
}
