package musaico.foundation.io.progresses;


import java.io.Serializable;


import musaico.foundation.i18n.exceptions.I18nIllegalArgumentException;

import musaico.foundation.io.Progress;
import musaico.foundation.io.Reference;

import musaico.foundation.io.references.SimpleSoftReference;


/**
 * <p>
 * A Progress implementation which is never used, and so does
 * not actually keep track of anything.
 * </p>
 *
 *
 * <p>
 * In Java, all Progress implementations must be Serializable in order to
 * play nicely over RMI.
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
public class ReadOnlyProgress
    implements Progress, Serializable
{
    /** The writable Progress which we wrap with read-only-ness. */
    private final Progress wrappedProgress;


    /**
     * <p>
     * Creates a new read-only wrapper for the specified progress.
     * </p>
     *
     * @param wrapped_progress The progress which will be wrapped
     *                         in read-only-ness.  Must not be null.
     */
    public ReadOnlyProgress (
                             Progress wrapped_progress
                             )
    {
        if ( wrapped_progress == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a ReadOnlyProgress wrapper for [%wrapped_progress%]",
                                                     wrapped_progress );
        }

        this.wrappedProgress = wrapped_progress;
    }


    /**
     * @see musaico.foundation.io.Progress#completed()
     */
    public final double completed ()
    {
        return this.wrappedProgress.completed ();
    }


    /**
     * @see musaico.foundation.io.Progress#currentFrame()
     */
    public final Reference currentFrame ()
    {
        return this.wrappedProgress.currentFrame ();
    }


    /**
     * @see musaico.foundation.io.Progress#frame(musaico.foundation.io.Reference)
     */
    public final ReadOnlyProgress frame (
                                         Reference frame
                                         )
    {
        Progress unwrapped_frame = this.wrappedProgress.frame ( frame );
        if ( unwrapped_frame instanceof ReadOnlyProgress )
        {
            return (ReadOnlyProgress) unwrapped_frame;
        }
        else
        {
            return new ReadOnlyProgress ( unwrapped_frame );
        }
    }


    /**
     * @see musaico.foundation.io.Progress#frames()
     */
    public final Reference [] frames ()
    {
        return this.wrappedProgress.frames ();
    }


    /**
     * @see musaico.foundation.io.Progress#numSteps(musaico.foundation.io.Reference)
     */
    public final long numSteps (
                                Reference frame
                                )
        throws I18nIllegalArgumentException
    {
        return this.wrappedProgress.numSteps ( frame );
    }


    /**
     * @see musaico.foundation.io.Progress#pop(musaico.foundation.io.Reference)
     */
    public final void pop (
                           Reference frame
                           )
    {
        // Do nothing.
    }


    /**
     * @see musaico.foundation.io.Progress#push(long)
     */
    public final Reference push (
                                 long num_steps
                                 )
    {
        // Do nothing.
        return NoProgress.FRAME;
    }


    /**
     * @see musaico.foundation.io.Progress#step(musaico.foundation.io.Reference)
     */
    public final void step (
                            Reference frame
                            )
    {
        // Do nothing.
    }


    /**
     * @see musaico.foundation.io.Progress#stepNum(musaico.foundation.io.Reference)
     */
    public final long stepNum (
                               Reference frame
                               )
        throws I18nIllegalArgumentException
    {
        return this.wrappedProgress.stepNum ( frame );
    }
}
