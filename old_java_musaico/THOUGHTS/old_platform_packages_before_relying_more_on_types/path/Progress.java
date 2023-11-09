package musaico.foundation.io;


import java.io.Serializable;


import musaico.foundation.i18n.exceptions.I18nIllegalArgumentException;


/**
 * <p>
 * Used by I/O objects to provide asynchronous progress reports
 * on operations (such as opening, closing, and so on).
 * </p>
 *
 * <p>
 * A Progress is typically mutable by nature.  It should be polled
 * periodically, if updates are desirable, since it is likely to
 * change underneath the observer.
 * </p>
 *
 *
 * <p>
 * In Java, all Progress implementations must be Serializable in order to
 * play nicely over RMI.  <b> However it is strongly recommended
 * that all Progress implementations extend the UnicastRemoteObject
 * class </b> in order to ensure that a caller who provides the
 * Progress object as a parameter to a remote service will be able
 * monitor the progress of the service execution.
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
public interface Progress
    extends Serializable
{
    /**
     * <p>
     * Returns the percentage (0-1) completion of this Progress.
     * </p>
     *
     * @return The percentage of all steps completed by this Progress
     *         in all frames.  Always between 0.0D and 1.0D inclusive.
     */
    public abstract double completed ();


    /**
     * <p>
     * Returns a Reference to the current frame in the stack.
     * </p>
     *
     * @return The current frame.  Never null.
     */
    public abstract Reference currentFrame ();


    /**
     * <p>
     * Returns the frame identified by the specified Reference.
     * </p>
     *
     * @param frame The Reference to the frame which will be returned.
     *              Must not be null.  Must be a valid frame within
     *              this Progress.
     *
     * @return A Progress representing the specified frame.
     *         Read-only access.  Never null.
     *
     * @throws I18nIllegalArgumentException If the specified frame
     *                                      does not exist in this Progress.
     */
    public abstract Progress frame (
                                    Reference frame
                                    )
        throws I18nIllegalArgumentException;


    /**
     * <p>
     * Returns the References pointing to all frames in this Progress.
     * </p>
     *
     * @return References to the frames on the stack of this Progress.
     *         Can be used with <code> frame ( ... ) </code>.
     *         Never null.  Never contains any null elements.
     */
    public abstract Reference [] frames ();


    /**
     * <p>
     * Returns number of steps for the specified frame.
     * </p>
     *
     * @param frame A reference to the frame from which the number
     *              of steps is to be retured.  If this is not the
     *              current frame for this progress, then frames
     *              are inspected up the stack.  Must not be null.
     *              Must be a valid frame for this Progress.
     *
     * @return The number of steps for the specified frame.  Always
     *         0 or greater.
     *
     * @throws I18nIllegalArgumentException If the specified
     *                                      frame does not exist
     *                                      in this Progress.
     */
    public abstract long numSteps (
                                   Reference frame
                                   )
        throws I18nIllegalArgumentException;


    /**
     * <p>
     * Pops the specified frame from the Progress, completing or
     * aborting it.
     * </p>
     *
     * <p>
     * If ths specified frame is not the top frame in this Progress,
     * then frames are popped (aborted) until it is found.
     * </p>
     *
     * @param frame The frame to pop.  Must not be null.
     *              Must be a valid frame in this Progress.
     */
    public abstract void pop (
                              Reference frame
                              );


    /**
     * <p>
     * Pushes a new frame onto the Progress, with the specified
     * expected number of steps toward completion of the frame.
     * </p>
     *
     * @param num_steps The size in terms of total number of steps
     *                  expected for this frame of progress.
     *                  Must be 1 or greater.
     *
     * @return A reference to the frame pushed.  Never null.
     */
    public abstract Reference push (
                                    long num_steps
                                    );


    /**
     * <p>
     * Sets the current "position" of progress one step closer
     * to completion.
     * </p>
     *
     * @param frame A reference to the frame in which a step
     *              has been taken.  If this is not the current
     *              frame for this progress, then frames are
     *              popped (aborted) until the specified frame is
     *              found and stepped.  Must not be null.
     *              Must be a valid frame for this Progress.
     */
    public abstract void step (
                               Reference frame
                               );


    /**
     * <p>
     * Returns the current step # for the specified frame.
     * </p>
     *
     * @param frame A reference to the frame from which the step
     *              count is to be retured.  If this is not the
     *              current frame for this progress, then frames
     *              are inspected up the stack.  Must not be null.
     *              Must be a valid frame for this Progress.
     *
     * @return The step number of the specified frame.  Always
     *         0 or greater.  Always less than or equal to the
     *         number of steps for the frame.
     *
     * @throws I18nIllegalArgumentException If the specified
     *                                      frame does not exist
     *                                      in this Progress.
     */
    public abstract long stepNum (
                                  Reference frame
                                  )
        throws I18nIllegalArgumentException;
}
