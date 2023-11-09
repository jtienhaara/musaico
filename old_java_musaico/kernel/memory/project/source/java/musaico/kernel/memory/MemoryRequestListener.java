package musaico.kernel.memory;

import java.io.Serializable;


/**
 * <p>
 * A listener which is notified by some memory object (such as a Segment)
 * whenever it has received a MemoryRequest to be fulfilled.
 * </p>
 *
 * <p>
 * Typically a kernel Task implements this listener, and it wakes up
 * to execute all requests in a Segment's queue every time the
 * Segment notifies the listener that a request has been received.
 * </p>
 *
 *
 * <p>
 * In Java, because every memory object can be used across RMI
 * environment, every MemoryRequestListener must either be Serializable
 * or a UnicastRemoteObject and so on.
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
 * Copyright (c) 2012 Johann Tienhaara
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
public interface MemoryRequestListener
    extends Serializable
{
    /**
     * <p>
     * Adds a segment to this listener, so that when it receives
     * memory requests, this listener will immediately trigger
     * the segment to handle them.
     * </p>
     *
     * <p>
     * Has no effect if the segment is already registered.
     * </p>
     *
     * @param segment The segment to register.  Must not be null.
     */
    public abstract void registerSegment (
                                          Segment segment
                                          );


    /**
     * <p>
     * A MemoryRequest has been received by one of the memory
     * objects being listened to.
     * </p>
     *
     * @param request The MemoryRequest to a specific memory object,
     *                such as a request to write Fields to a Segment,
     *                or a request to resize a Segment, and so on.
     *                Must not be null.
     */
    public abstract <RESPONSE extends Serializable>
        void requestNotification (
                                  MemoryRequest<RESPONSE> request
                                  );


    /**
     * <p>
     * Removes a segment to this listener, so that when it receives
     * memory requests, this listener will no longer trigger
     * the segment to handle them.
     * </p>
     *
     * <p>
     * Has no effect if the segment is NOT already registered.
     * </p>
     *
     * @param segment The segment to un-register.  Must not be null.
     */
    public abstract void unregisterSegment (
                                            Segment segment
                                            );
}
