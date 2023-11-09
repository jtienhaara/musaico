package musaico.foundation.io.progresses;


import java.io.Serializable;


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
public class NoProgress
    implements Progress, Serializable
{
    /** The one frame reference for this NoProgress. */
    public static final Reference FRAME =
        new SimpleSoftReference<String> ( "no_frame" );


    /**
     * @see musaico.foundation.io.Progress#completed()
     */
    public final double completed ()
    {
        return 1.0D;
    }


    /**
     * @see musaico.foundation.io.Progress#currentFrame()
     */
    public final Reference currentFrame ()
    {
        return NoProgress.FRAME;
    }


    /**
     * @see musaico.foundation.io.Progress#frame(musaico.foundation.io.Reference)
     */
    public final Progress frame (
                                 Reference frame
                                 )
    {
        return this;
    }


    /**
     * @see musaico.foundation.io.Progress#frames()
     */
    public final Reference [] frames ()
    {
        return new Reference [] { NoProgress.FRAME };
    }


    /**
     * @see musaico.foundation.io.Progress#numSteps(musaico.foundation.io.Reference)
     */
    public final long numSteps (
                                Reference frame
                                )
    {
        return 0L;
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
    {
        return 0L;
    }
}
