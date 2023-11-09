package musaico.state;

import java.io.Serializable;


import musaico.io.Reference;


/**
 * <p>
 * Represents a numeric label of an Arc.
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
public class IntLabel
    implements Reference, Serializable
{
    /** The unique numeric label. */
    private int label;


    /**
     * <p>
     * Creates a new label with the specified number.
     * </p>
     */
    public IntLabel (
                     int label
                     )
    {
        this.label = label;
    }


    /**
     * @see java.lang.Object#equals(Object)
     */
    @Override
    public final boolean equals (
                                 Object obj
                                 )
    {
        if ( obj == null )
        {
            return false;
        }
        else if ( obj == this )
        {
            return true;
        }
        else if ( ! ( obj instanceof IntLabel ) )
        {
            return false;
        }

        IntLabel other = (IntLabel) obj;
        int my_label = this.label ();
        int other_label = other.label ();

        if ( my_label == other_label )
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
    @Override
    public final int hashCode ()
    {
        return this.label ();
    }


    /**
     * <p>
     * Returns the numeric label for this IntLabel.
     * </p>
     *
     * @return The numeric label.
     */
    public int label ()
    {
        return this.label;
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString ()
    {
        return "IntLabel-" + this.label ();
    }
}
