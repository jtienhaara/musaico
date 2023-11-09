package musaico.foundation.io.types;


import java.io.Serializable;

import musaico.foundation.io.Progress;
import musaico.foundation.io.Reference;

import musaico.foundation.types.TypeCastException;
import musaico.foundation.types.TypeCaster;
import musaico.foundation.types.TypeException;


import musaico.foundation.types.primitive.types.CastLongToString;


/**
 * <p>
 * Casts Progresses to Strings.
 * </p>
 *
 * <p>
 * Each Progress is cast to a string of the form:
 * <code> progress: frame1step#/frame1#steps.frame2step#/frame2#steps... </code>.
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
 * Copyright (c) 2010, 2011 Johann Tienhaara
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
public class CastProgressToString
    implements TypeCaster<Progress,String>, Serializable
{
    /** The prefix which makes this string a "progress" string. */
    private static final String STRING_PREFIX = "progress: ";

    /** Separates frame1step#/frame1#steps from frame2step#/frame2#steps... */
    private static final String STRING_FRAME_SEPARATOR = ".";

    /** Separates frameXstep# from frameX#steps. */
    private static final String STRING_STEP_SEPARATOR = " /";


    /** Casts step # and # steps to Strings. */
    private final CastLongToString castLongToString =
        new CastLongToString ();


    /**
     * @see musaico.foundation.types.TypeCaster#cast(FROM,Class)
     */
    public String cast (
                        Progress from,
                        Class to_class
                        )
        throws TypeException
    {
        if ( from == null )
        {
            throw new TypeCastException ( "Cannot cast from progress [%progress%] to [%to_class%]",
                                          "progress", from,
                                          "to_class", to_class );
        }

        Reference [] frames = from.frames ();
        StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( "progress: " );
        boolean is_first_frame = true;
        for ( Reference frame : frames )
        {
            if ( is_first_frame )
            {
                is_first_frame = false;
            }
            else
            {
                sbuf.append ( CastProgressToString.STRING_FRAME_SEPARATOR );
            }

            long step_num = from.stepNum ( frame );
            long num_steps = from.numSteps ( frame );

            String step_num_str =
                this.castLongToString.cast ( step_num, String.class );
            String num_steps_str =
                this.castLongToString.cast ( num_steps, String.class );
            sbuf.append ( "" + step_num_str + "/" + num_steps_str );
        }

        String as_string = sbuf.toString ();

        return as_string;
    }
}
