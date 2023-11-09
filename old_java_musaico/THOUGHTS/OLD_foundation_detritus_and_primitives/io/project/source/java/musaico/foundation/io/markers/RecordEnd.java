package musaico.foundation.io.markers;

import java.io.Serializable;


import musaico.foundation.i18n.exceptions.I18nIllegalArgumentException;

import musaico.foundation.io.Marker;
import musaico.foundation.io.Reference;

import musaico.foundation.io.references.SimpleSoftReference;


/**
 * <p>
 * The end of a record inside some data stream.
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
public class RecordEnd
    implements Marker, Serializable
{
    /** The record name. */
    private final Reference record;


    /**
     * <p>
     * Creates a new RecordEnd for the specified
     * record name.
     * </p>
     *
     * @param record_name The name of the record for which this is the
     *                    end indicator.
     *                    Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public RecordEnd (
                        String record_name
                        )
        throws I18nIllegalArgumentException
    {
        if ( record_name == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a RecordEnd for record [%record%]",
                                                     "record", record_name );
        }

        this.record = new SimpleSoftReference<String> ( record_name );
    }


    /**
     * <p>
     * Creates a new RecordEnd with the specified reference as a
     * record name.
     * </p>
     *
     * @param record The label for this marker.
     *               Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public RecordEnd (
                        Reference record
                        )
        throws I18nIllegalArgumentException
    {
        if ( record == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a RecordEnd for record [%record%]",
                                                     "record", record );
        }

        this.record = record;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals (
                           Object obj
                           )
    {
        if ( obj == null
             || ! ( obj instanceof RecordEnd ) )
        {
            return false;
        }

        RecordEnd that = (RecordEnd) obj;
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
        return this.record;
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString ()
    {
        return "}:" + this.label ();
    }
}
