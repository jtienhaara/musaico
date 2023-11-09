package musaico.region.types;


import java.io.Serializable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.region.Position;
import musaico.region.Region;

import musaico.region.array.ArrayPosition;
import musaico.region.array.ArrayRegion;
import musaico.region.array.ArraySpace;

import musaico.region.time.AbsoluteTimePosition;
import musaico.region.time.RelativeTimePosition;
import musaico.region.time.TimeRegion;
import musaico.region.time.TimeSpace;

import musaico.types.TypeCastException;
import musaico.types.TypeCaster;
import musaico.types.TypeException;


/**
 * <p>
 * Casts values from String to Region.
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
public class CastStringToRegion
    implements TypeCaster<String,Region>, Serializable
{
    private static final Pattern REGION_PREFIX =
        Pattern.compile ( Pattern.quote ( "region:" ) );

    private static final Pattern SPARSE_REGION_PREFIX =
        Pattern.compile ( Pattern.quote ( "sparse_region:" ) );

    private static final Pattern OPEN_REGION =
        Pattern.compile ( "[ ]*" + Pattern.quote ( "{" ) + "[ ]*" );

    private static final Pattern START_END_SEPARATOR =
        Pattern.compile ( "[ ]*" + Pattern.quote ( ".." ) + "[ ]*" );

    private static final Pattern SPARSE_SUB_REGION_SEPARATOR =
        Pattern.compile ( "[ ]*" + Pattern.quote ( "," ) + "[ ]*" );

    private static final Pattern CLOSE_REGION =
        Pattern.compile ( "[ ]*" + Pattern.quote ( "}" ) );

    private static final Pattern OPEN_PARAMETERS =
        Pattern.compile ( "[ ]*" + Pattern.quote ( "[" ) + "[ ]*" );

    private static final Pattern PARAMETER_SEPARATOR =
        Pattern.compile ( "[ ]*" + Pattern.quote ( "," ) + "[ ]*" );

    private static final Pattern PARAMETER_NAME_VALUE_SEPARATOR =
        Pattern.compile ( "[ ]*" + Pattern.quote ( "=" ) + "[ ]*" );

    private static final Pattern CLOSE_PARAMETERS =
        Pattern.compile ( "[ ]*" + Pattern.quote ( "]" ) );

    private static final Pattern CLOSE_WHOLE_REGION =
        Pattern.compile ( "[ ]*" + Pattern.quote ( ";" ) + "[ ]*" );


    /** Casts Strings to Positions for us. */
    private final CastStringToPosition stringToPosition =
        new CastStringToPosition ();


    /**
     * @see musaico.types.TypeCaster#cast(FROM,Class)
     */
    public Region cast (
                        String from,
                        Class to_class
                        )
        throws TypeException
    {
        String remaining = from;

        final Matcher region_matcher =
            REGION_PREFIX.matcher ( remaining );
        final Matcher sparse_region_matcher =
            SPARSE_REGION_PREFIX.matcher ( remaining );

        TypeCastException cannot_cast =
            new TypeCastException ( "Cannot cast text [%string%] as Region: expected region:{start..end}; or sparse_region:{{start1..end1},{start2..end2},...};",
                                    "string", from );

        final Region region;
        if ( region_matcher.find () )
        {
            // Regular region.
            remaining = remaining.substring ( region_matcher.end () + 1 );
            Matcher matcher = OPEN_REGION.matcher ( remaining );
            if ( ! matcher.find ()
                 || matcher.start () != 0 )
            {
                throw cannot_cast;
            }

            remaining = remaining.substring ( matcher.end () + 1 );
            matcher = CLOSE_REGION.matcher ( remaining );
            if ( ! matcher.find () )
            {
                throw cannot_cast;
            }

            String start_end_string = remaining.substring ( matcher.start (),
                                                            matcher.end () - 1 );
            String [] start_end =
                START_END_SEPARATOR.split ( start_end_string );
            if ( start_end.length != 2 )
            {
                throw cannot_cast;
            }

            Position start = this.stringToPosition.cast ( start_end [ 0 ],
                                                          Position.class );
            Position end = this.stringToPosition.cast ( start_end [ 1 ],
                                                        Position.class );

            remaining = remaining.substring ( matcher.end () + 1 );
            matcher = CLOSE_WHOLE_REGION.matcher ( remaining );
            if ( ! matcher.find ()
                 || matcher.start () != 0
                 || matcher.end () != ( remaining.length () - 1 ) )
            {
                throw cannot_cast;
            }

            if ( start instanceof ArrayPosition )
            {
                region = ArraySpace.STANDARD.region ( start, end );
            }
            else if ( start instanceof AbsoluteTimePosition )
            {
                region = TimeSpace.ABSOLUTE.region ( start, end );
            }
            else if ( start instanceof RelativeTimePosition )
            {
                region = TimeSpace.RELATIVE.region ( start, end );
            }
            else
            {
                throw cannot_cast;
            }
        }
        else if ( sparse_region_matcher.find () )
        {
            // Sparse region.
            // NOT DONE YET.  :(     !!!!!!!!!!!!!!!!!!!!!!!!!!
            throw cannot_cast;
        }
        else
        {
            // Unrecognized region type!
            throw cannot_cast;
        }

        return region;
    }
}
