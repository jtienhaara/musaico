package musaico.foundation.io.types;


import java.io.Serializable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import musaico.foundation.io.Progress;
import musaico.foundation.io.Reference;

import musaico.foundation.io.progresses.SimpleProgress;

import musaico.foundation.types.TypeCastException;
import musaico.foundation.types.TypeCaster;
import musaico.foundation.types.TypeException;

import musaico.foundation.types.primitive.types.CastStringToLong;


/**
 * <p>
 * Casts values from String to Progress.
 * </p>
 *
 * <p>
 * The String must take the form
 * <code>progress: frame1step#/frame1#steps.frame2step#/frame2#steps...</code>.
 * For example, the following are all Strings which can be
 * cast to valid Progress objects:
 * </p>
 *
 * <pre>
 *     progress: 42/42
 *     progress: 0/42.17/29.3/98
 *     progress: 21/42.33/800
 * </pre>
 *
 * <p>
 * The middle example shows a progress at the first of 42 steps.
 * The first step is a pushed stack frame with 29 steps, 17 of
 * them completed.  The 18th is a pushed stack frame with 800
 * steps, 3 of them completed.  Once the 800 steps are complete,
 * the 3rd stack frame will be popped, and the 2nd stack frame
 * will complete its 29 steps.  Once the 2nd stack frame is
 * popped, the 1st stack frame will continue with its 42 steps.
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
public class CastStringToProgress
    implements TypeCaster<String,Progress>, Serializable
{
    /** The regular expression matching the first chunk
     *  of the progress String, "progress: " out of
     *  the whole "progress: start+17 (42 steps)" etc. */
    private static final Pattern REGEX_PROGRESS_PRE =
        Pattern.compile ( "^progress:[ ]*:[ ]*" );

    /** Splits the "frame1.frame2.frame3".  For example,
     *  splits "21/42.17/29.3/800" into "21/42", "17/29" and "3/800". */
    private static final Pattern REGEX_FRAMES_SPLIT =
        Pattern.compile ( "[ ]*\\.[ ]*" );

    /** Splits the "step# / #steps" in two.  For example,
     *  splits "21 / 42" into "21" and "42". */
    private static final Pattern REGEX_STEPS_SPLIT =
        Pattern.compile ( "[ ]*/[ ]*" );

    /** The regular expression to pull out the number
     *  of steps executed or total in the Progress being parsed.
     *  For example, pulls out "21" or "42". */
    private static final Pattern REGEX_NUM_STEPS =
        Pattern.compile ( "^[0-9]+$" );


    /** Casts String portion to Long for step # and # steps. */
    private final CastStringToLong castStringToLong =
        new CastStringToLong ();


    /**
     * @see musaico.foundation.types.TypeCaster#cast(FROM,Class)
     */
    public Progress cast (
                          String from,
                          Class to_class
                          )
        throws TypeException
    {
        String progress_string = from;

        Matcher regex_pre =
            CastStringToProgress.REGEX_PROGRESS_PRE.matcher ( progress_string );

        int index_start = 0;
        int index_end = progress_string.length ();
        if ( ! regex_pre.matches () )
        {
            throw new TypeCastException ( "Un-parseable progress '[%cast_from_string%]' at [%char_index%]: '[%token%]'",
                                          "cast_from_string", from,
                                          "char_index", index_start,
                                          "token", progress_string );
        }

        index_start = regex_pre.end ();

        String frames_section = progress_string.substring ( index_start );
        String [] frames =
            CastStringToProgress.REGEX_FRAMES_SPLIT.split ( frames_section );

        if ( frames.length < 1 )
        {
            throw new TypeCastException ( "Un-parseable progress '[%cast_from_string%]' at [%char_index%]: '[%token%]'",
                                          "cast_from_string", from,
                                          "char_index", index_start,
                                          "token", progress_string );
        }

        // Now assemble the Progress object.
        Progress progress = new SimpleProgress ();

        for ( String frame : frames )
        {
            String [] stepses =
                CastStringToProgress.REGEX_STEPS_SPLIT.split ( frame );

            if ( stepses.length != 2 )
            {
                throw new TypeCastException ( "Un-parseable progress '[%cast_from_string%]' at [%char_index%]: '[%token%]'",
                                              "cast_from_string", from,
                                              "char_index", index_start,
                                              "token", progress_string );
            }

            String step_num_string = stepses [ 0 ];
            String num_steps_string = stepses [ 1 ];

            long step_num =
                this.castStringToLong.cast ( step_num_string, Long.class );
            long num_steps =
                this.castStringToLong.cast ( num_steps_string, Long.class );

            Reference progress_id = progress.push ( num_steps );
            for ( long step = 0L; step < step_num; step ++ )
            {
                progress.step ( progress_id );
            }
        }

        return progress;
    }
}
