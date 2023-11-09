package musaico.foundation.io.references;

import java.io.Serializable;


import musaico.foundation.i18n.exceptions.I18nIllegalArgumentException;

import musaico.foundation.io.ReferenceException;
import musaico.foundation.io.SoftReference;


/**
 * <p>
 * Represents a soft reference to some object inside some
 * namespace or other.
 * </p>
 *
 * <p>
 * A SoftReference is NOT directly resolve-able into the object
 * referred to.  However the creator of the reference can typically
 * be contacted somehow to query or modify the original object.
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
public class SimpleSoftReference<REF_ID extends Serializable>
    implements SoftReference<REF_ID>
{
    /** The unique identifier of this soft reference within some namespace. */
    private final REF_ID id;


    /**
     * <p>
     * Creates a new soft reference to an object.
     * </p>
     *
     * @param id The unique identifier of the object in some namespace.
     *           Must not be null.
     */
    public SimpleSoftReference (
                                REF_ID id
                                )
    {
        if ( id == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a SimpleSoftReference with id [%id%]",
                                                     "id", id );
        }

        this.id = id;
    }


    /**
     * @see musaico.foundation.io.Reference#equals(Object)
     */
    public boolean equals (
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
        else if ( ! ( obj instanceof SoftReference ) )
        {
            return false;
        }

        REF_ID my_id = this.id ();
        if ( my_id == null )
        {
            return false;
        }

        SoftReference<?> other_reference = (SoftReference<?>) obj;
        Object other_id = (Object) other_reference.id ();
        if ( other_id == null )
        {
            return false;
        }

        if ( my_id.equals ( other_id ) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * @see musaico.foundation.io.Reference#hashCode()
     */
    public int hashCode ()
    {
        return this.id ().hashCode ();
    }


    /**
     * @see musaico.foundation.io.SoftReference#id()
     */
    public REF_ID id ()
    {
        return this.id;
    }


    /**
     * @see musaico.foundation.io.Reference#toString()
     */
    public String toString ()
    {
        final String prefix;
        if ( this.getClass () == SimpleSoftReference.class )
        {
            prefix = "";
        }
        else
        {
            prefix = this.getClass ().getSimpleName () + "_";
        }

        return "" + prefix + this.id ();
    }
}
