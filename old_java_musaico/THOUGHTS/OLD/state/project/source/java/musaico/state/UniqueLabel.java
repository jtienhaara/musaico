package musaico.state;

import java.io.Serializable;


import musaico.io.Reference;


/**
 * <p>
 * Represents a unique label of an Arc, which is not equal to any
 * other Reference except itself.
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
public class UniqueLabel
    implements Reference, Serializable
{
    /**
     * @see java.lang.Object#equals(Object)
     */
    public final boolean equals (
                                 Object obj
                                 )
    {
        if ( obj == this )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    public final int hashCode ()
    {
        // The Object's hashCode() will do.
        return super.hashCode();
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString ()
    {
        return "UniqueLabel" + this.hashCode ();
    }
}
