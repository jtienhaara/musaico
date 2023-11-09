package musaico.foundation.io.markers;

import java.io.Serializable;


import musaico.foundation.i18n.exceptions.I18nIllegalArgumentException;

import musaico.foundation.io.Marker;
import musaico.foundation.io.Reference;

import musaico.foundation.io.references.SimpleSoftReference;


/**
 * <p>
 * A human-readable label marker inside some data stream.
 * </p>
 *
 *
 * <p>
 * In Java every Marker must be Serializable in order to play
 * nicely over RMI.
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
public class LabelMarker
    implements Marker, Serializable
{
    /** The marker text, created by some human in whatever locale
     *  they prefer. */
    private final Reference label;


    /**
     * <p>
     * Creates a new LabelMarker with the specified
     * non-internataionalizable text.
     * </p>
     *
     * @param localized_text The human-readable text for this marker.
     *                       Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public LabelMarker (
                        String localized_text
                        )
        throws I18nIllegalArgumentException
    {
        if ( localized_text == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a LabelMarker with label [%label%]",
                                                     "label", localized_text );
        }

        this.label = new SimpleSoftReference<String> ( localized_text );
    }


    /**
     * <p>
     * Creates a new LabelMarker with the specified reference as a
     * label.
     * </p>
     *
     * @param label The label for this marker.
     *              Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public LabelMarker (
                        Reference label
                        )
        throws I18nIllegalArgumentException
    {
        if ( label == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a LabelMarker with label [%label%]",
                                                     "label", label );
        }

        this.label = label;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals (
                           Object obj
                           )
    {
        if ( obj == null
             || ! ( obj instanceof LabelMarker ) )
        {
            return false;
        }

        LabelMarker that = (LabelMarker) obj;
        if ( this.label ().equals ( that.label () ) )
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
    public int hashCode ()
    {
        return this.label ().hashCode ();
    }


    /**
     * @see musaico.foundation.io.Marker#label()
     */
    public Reference label ()
    {
        return this.label;
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString ()
    {
        return "label(" + this.label () + ")";
    }
}
