package musaico.buffer.search;

import java.io.Serializable;


import musaico.buffer.Buffer;

import musaico.field.Field;

import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Comparison;
import musaico.io.Order;

import musaico.region.Criterion;
import musaico.region.Position;
import musaico.region.Space;


/**
 * <p>
 * Compares field identifiers in a specific buffer alphabetically,
 * so that the buffer can be sorted alphabetically.
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
 * Copyright (c) 2011 Johann Tienhaara
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
public class SpecificFieldID
    implements Criterion, Serializable
{
    /** The buffer being searched. */
    private final Buffer buffer;

    /** The specific field id to search for. */
    private final String fieldID;


    /**
     * <p>
     * Creates a new SpecificFieldID to search for the specified
     * field in a Buffer.
     * </p>
     *
     * @param buffer The buffer to search through for the
     *               specified field.  Must not be null.
     *
     * @param field_id The id of the field to search for.
     *                 Must not be null.
     *
     * @throws I18nIllegalArgumentException If the parameter(s)
     *                                      specified are invalid
     *                                      (see above for details).
     */
    public SpecificFieldID (
                            Buffer buffer,
                            String field_id
                            )
        throws I18nIllegalArgumentException
    {
        if ( buffer == null
             || field_id == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a SpecificFieldID search criterion for buffer [%buffer%] field id [%field_id%]",
                                                     "buffer", buffer,
                                                     "field_id", field_id );
        }

        this.buffer = buffer;
        this.fieldID = field_id;
    }


    /**
     * <p>
     * Returns the buffer whose fields are being sought by
     * this SpecificFieldID search criterion.
     * </p>
     *
     * @return The buffer being searched through by this SpecificFieldID
     *         search criterion.  Never null.
     */
    public Buffer buffer ()
    {
        return this.buffer;
    }


    /**
     * <p>
     * Returns the field id being sought by this SpecificFieldID
     * search criterion.
     * </p>
     *
     * @return The field id being sought by this SpecificFieldID
     *         search criterion.  Never null.
     */
    public String fieldID ()
    {
        return this.fieldID;
    }


    /**
     * @see musaico.region.Criterion#compare(musaico.region.Position)
     */
    public Comparison compare (
                               Position position
                               )
    {
        Field field = this.buffer.get ( position );
        final Comparison comparison =
            Order.DICTIONARY.compareValues ( field.id (),
                                             this.fieldID () );

        return comparison;
    }


    /**
     * @see musaico.region.Position#space()
     */
    @Override
    public Space space ()
    {
        return this.buffer ().region ().space ();
    }
}
