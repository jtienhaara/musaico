package musaico.buffer.filters;


import java.io.Serializable;

import java.util.regex.Pattern;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.FilterState;

import musaico.buffer.Buffer;
import musaico.buffer.BufferFilter;

import musaico.field.Field;

import musaico.region.Position;


/**
 * <p>
 * Filters the Positions in a Buffer by the Field id.
 * </p>
 *
 * <p>
 * For example, to create a Filter which will find
 * all Fields called "foo" in a Buffer:
 * </p>
 *
 * <pre>
 *     Buffer buffer = ...;
 *     Filter only_foo_fields = new FieldIDFilter ( buffer, "foo" );
 * </pre>
 *
 * <p>
 * Such a filter can then be used to filter the positions
 * in a Buffer:
 * </p>
 *
 * <pre>
 *     Region foo_fields = buffer.region ().filter ( only_foo_fields );
 * </pre>
 *
 * <p>
 * The region may then be stepped through, or used to copy the
 * "foo" fields into another Buffer, and so on.
 * </p>
 *
 * <p>
 * Field identifiers may be matched by regular expression, too.
 * For example, to keep all Positions corresponding to Fields
 * whose identifiers start with "address":
 * </p>
 *
 * <pre>
 *     Buffer buffer = ...;
 *     Filter only_address_fields =
 *         new FieldIDFilter ( buffer, Pattern.compile ( "address*" ) );
 * </pre>
 *
 * <p>
 * Then to retrieve the Region of the Buffer which contains the
 * Positions of Fields "address1", "address2", and so on:
 * </p>
 *
 * <pre>
 *     Region address_fields = buffer.region ().filter ( only_address_fields );
 * </pre>
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
public class FieldIDFilter
{
    /** The buffer whose field ids will be filtered by. */
    private final Buffer buffer;

    /** The identifier of the Fields whose corresponding Positions
     *  will be kept.  All other Positions will be filtered out. */
    private final Pattern fieldIDPattern;


    /**
     * <p>
     * Creates a new FieldIDFilter for the specified Buffer
     * to keep only Positions corresponding to fields with the
     * specified ID, and discard all other positions.
     * </p>
     *
     * @param buffer The Buffer whose Fields will be filtered.
     *               Must not be null.
     *
     * @param field_id The identifier of the Field whose corresponding
     *                 Position(s) will be kept.  All other Positions
     *                 will be filtered out.  Must not be null.
     */
    public FieldIDFilter (
                          Buffer buffer,
                          String field_id
                          )
    {
        if ( buffer == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a FieldIDFilter with buffer [%buffer%], field_id [%field_id%]",
                                                     "buffer", buffer,
                                                     "field_id", field_id );
        }
        else if ( field_id == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a FieldIDFilter with buffer [%buffer%], field_id [%field_id%]",
                                                     "buffer", buffer,
                                                     "field_id", field_id );
        }

        this.buffer = buffer;
        this.fieldIDPattern = Pattern.compile ( "^" + Pattern.quote ( field_id ) + "$" );
    }


    /**
     * <p>
     * Creates a new FieldIDFilter for the specified Buffer
     * to keep only Positions corresponding to fields matching the
     * specified ID regular expression, and discard all other positions.
     * </p>
     *
     * @param buffer The Buffer whose Fields will be filtered.
     *               Must not be null.
     *
     * @param field_id_regex The regular expression for Field identifiers
     *                       whose corresponding Position(s) will be kept.
     *                       All other Positions will be filtered out.
     *                       Must not be null.
     */
    public FieldIDFilter (
                          Buffer buffer,
                          Pattern field_id_regex
                          )
    {
        if ( buffer == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a FieldIDFilter with buffer [%buffer%], field_id_regex [%field_id_regex%]",
                                                     "buffer", buffer,
                                                     "field_id_regex", field_id_regex );
        }
        else if ( field_id_regex == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a FieldIDFilter with buffer [%buffer%], field_id_regex [%field_id_regex%]",
                                                     "buffer", buffer,
                                                     "field_id_regex", field_id_regex );
        }

        this.buffer = buffer;
        this.fieldIDPattern = field_id_regex;
    }


    /**
     * @see musaico.buffer.BufferFilter#buffer()
     */
    public Buffer buffer ()
    {
        return this.buffer;
    }


    /**
     * @see musaico.io.Filter#filter(Position)
     */
    public FilterState filter (
                               Position position
                               )
    {
        if ( position == null
             || position.equals ( this.buffer ().region ().space ().outOfBounds () ) )
        {
            // No such position in any buffer.
            return FilterState.DISCARD;
        }

        Field field = this.buffer ().get ( position );

        if ( field == Field.NULL )
        {
            // No such position in this buffer.
            return FilterState.DISCARD;
        }

        String field_id = field.id ();

        if ( this.fieldIDPattern.matcher ( field_id ).find () )
        {
            // The field identifier matches the pattern.
            return FilterState.KEEP;
        }
        else
        {
            // The field identifier does not match the pattern.
            return FilterState.DISCARD;
        }
    }
}
