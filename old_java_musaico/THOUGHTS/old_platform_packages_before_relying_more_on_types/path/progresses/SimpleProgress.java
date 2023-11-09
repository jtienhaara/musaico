package musaico.foundation.io.progresses;


import java.io.Serializable;


import musaico.foundation.i18n.exceptions.I18nIllegalArgumentException;

import musaico.foundation.io.Progress;
import musaico.foundation.io.Reference;

import musaico.foundation.io.references.SimpleSoftReference;


/**
 * <p>
 * A bare bones Progress implementation providing
 * asynchronous progress reports on operations (such as opening,
 * closing, and so on).
 * </p>
 *
 * <p>
 * A SimpleProgress is always mutable by nature.
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
public class SimpleProgress
    implements Progress, Serializable
{
    /** Lock all set()s on this flag. */
    private final Serializable lock = new String ();

    /** The name of this frame, 0L for the bottom frame in the stack,
     *  1L for 1 up, 2L up from there, and so on. */
    private final SimpleSoftReference<Long> frameName;

    /** The number of steps to complete within this frame. */
    private final long numSteps;

    /** The current step number of this frame. */
    private final long stepNum;

    /** How much to increment the completed amount by for each step. */
    private final double completionPerStep;

    /** The next frame up the stack, or null if this is currently
     *  the top stack frame. */
    private SimpleProgress upFrame = null;

    /** Completion percentage (0-1).  Changes with each push (),
     *  pop () and step (). */
    private double completed = 0.0D;


    /**
     * <p>
     * Creates a new SimpleProgress.
     * </p>
     */
    public SimpleProgress ()
    {
        this ( new SimpleSoftReference<Long> ( new Long ( 0L ) ),
               1L );
    }


    /**
     * <p>
     * Creates a new frame within a parent SimpleProgress.
     * </p>
     *
     * @param frame_name The name of this frame.  Must not be null.
     *
     * @param num_steps The number of steps before this Progress
     *                  frame is completed.  Must be 1 or greater.
     */
    private SimpleProgress (
                            SimpleSoftReference<Long> frame_name,
                            long num_steps
                            )
    {
        this.frameName = frame_name;
        this.numSteps = num_steps;
        this.stepNum = 0L;
        this.completionPerStep = 1.0D / (double) this.numSteps;
    }


    /**
     * @see musaico.foundation.io.Progress#completed()
     */
    public double completed ()
    {
        synchronized ( this.lock )
        {
            if ( this.upFrame == null )
            {
                return this.completed;
            }

            double up_completion = this.upFrame.completed ();
            double partial_step = up_completion * this.completionPerStep;

            return this.completed + partial_step;
        }
    }


    /**
     * @see musaico.foundation.io.Progress#currentFrame()
     */
    public final Reference currentFrame ()
    {
        synchronized ( this.lock )
        {
            if ( this.upFrame != null )
            {
                return this.upFrame.currentFrame ();
            }

            return this.frameName;
        }
    }


    /**
     * @see musaico.foundation.io.Progress#frame(musaico.foundation.io.Reference)
     */
    public final Progress frame (
                                 Reference frame
                                 )
        throws I18nIllegalArgumentException
    {
        synchronized ( this.lock )
        {
            if ( this.frameName.equals ( frame ) )
            {
                return new ReadOnlyProgress ( this );
            }
            else if ( this.upFrame != null )
            {
                return this.upFrame.frame ( frame );
            }
            else
            {
                throw new I18nIllegalArgumentException ( "No such frame '[%frame%]' in progress [%progress%]",
                                                         "frame", frame,
                                                         "progress", this );
            }
        }
    }


    /**
     * @see musaico.foundation.io.Progress#frames()
     */
    public final Reference [] frames ()
    {
        final Reference [] frames;
        synchronized ( this.lock )
        {
            // Skip the top frame, it's just the root of the progress tree.
            int num_frames = 0;
            SimpleProgress curr_frame = this.upFrame;
            while ( curr_frame != null
                    && num_frames < 1024 ) // Infinite loop protection
            {
                curr_frame = curr_frame.upFrame;
                num_frames ++;
            }

            frames = new Reference [ num_frames ];
            curr_frame = this.upFrame;
            for ( int frame_num = 0; frame_num < num_frames; frame_num ++ )
            {
                frames [ frame_num ] = curr_frame.frameName;
                curr_frame = curr_frame.upFrame;
            }
        }

        return frames;
    }


    /**
     * @see musaico.foundation.io.Progress#numSteps(musaico.foundation.io.Reference)
     */
    public final long numSteps (
                                Reference frame
                                )
        throws I18nIllegalArgumentException
    {
        synchronized ( this.lock )
        {
            if ( this.frameName.equals ( frame ) )
            {
                return this.numSteps;
            }
            else if ( this.upFrame != null )
            {
                return this.upFrame.numSteps ( frame );
            }
            else
            {
                throw new I18nIllegalArgumentException ( "No such frame '[%frame%]' in progress [%progress%]",
                                                         "frame", frame,
                                                         "progress", this );
            }
        }
    }


    /**
     * @see musaico.foundation.io.Progress#pop(musaico.foundation.io.Reference)
     */
    public void pop (
                     Reference frame
                     )
    {
        synchronized ( this.lock )
        {
            if ( this.frameName.equals ( frame ) )
            {
                this.completed = 1.0D;
                if ( this.upFrame != null )
                {
                    this.upFrame.pop ( this.upFrame.frameName );
                    this.upFrame = null;
                }

                return;
            }

            if ( this.upFrame == null )
            {
                // The requested frame does not exist, do nothing.
                return;
            }

            this.upFrame.pop ( frame );
            if ( this.upFrame.frameName.equals ( frame ) )
            {
                this.upFrame = null;
                this.completed += this.completionPerStep;
                if ( this.completed > 1.0D )
                {
                    this.completed = 1.0D;
                }
            }
        }
    }


    /**
     * @see musaico.foundation.io.Progress#push(long)
     */
    public Reference push (
                           long num_steps
                           )
    {
        synchronized ( this.lock )
        {
            if ( this.upFrame != null )
            {
                return this.upFrame.push ( num_steps );
            }

            long new_frame_id = this.frameName.id ().longValue () + 1L;
            SimpleSoftReference<Long> new_frame_name =
                new SimpleSoftReference<Long> ( new Long ( new_frame_id ) );
            this.upFrame = new SimpleProgress ( new_frame_name, num_steps );

            return new_frame_name;
        }
    }


    /**
     * @see musaico.foundation.io.Progress#step(musaico.foundation.io.Reference)
     */
    public void step (
                      Reference frame
                      )
    {
        synchronized ( this.lock )
        {
            if ( ! this.frameName.equals ( frame ) )
            {
                if ( this.upFrame == null )
                {
                    // No such frame.  Ignore.
                    return;
                }
                else
                {
                    this.upFrame.step ( frame );
                    return;
                }
            }

            // Step within this frame.
            if ( this.upFrame != null )
            {
                this.pop ( this.upFrame.frameName );
            }

            this.completed += this.completionPerStep;
            if ( this.completed > 1.0D )
            {
                this.completed = 1.0D;
            }
        }
    }


    /**
     * @see musaico.foundation.io.Progress#stepNum(musaico.foundation.io.Reference)
     */
    public final long stepNum (
                               Reference frame
                               )
        throws I18nIllegalArgumentException
    {
        synchronized ( this.lock )
        {
            if ( this.frameName.equals ( frame ) )
            {
                return this.stepNum;
            }
            else if ( this.upFrame != null )
            {
                return this.upFrame.stepNum ( frame );
            }
            else
            {
                throw new I18nIllegalArgumentException ( "No such frame '[%frame%]' in progress [%progress%]",
                                                         "frame", frame,
                                                         "progress", this );
            }
        }
    }
}
