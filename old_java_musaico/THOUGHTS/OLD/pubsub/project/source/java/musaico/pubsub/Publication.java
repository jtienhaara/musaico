package musaico.pubsub;

import java.io.Serializable;


import musaico.io.Reference;


/**
 * <p>
 * Represents an event published into a publish/subscribe system.
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
 * Copyright (c) 2009 Johann Tienhaara
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
public interface Publication
    extends Serializable
{
    /**
     * <p>
     * Returns a reference to the Publisher who published this event.
     * </p>
     *
     * <p>
     * Typically a String identifier can be used to identify
     * the Publisher.  However the type of return value
     * is implementation-dependent.  The hashCode() and
     * equals() methods of the returned type must work properly
     * to allow the publisher's identifier to be stored in (for
     * example) a hash table.
     * </p>
     *
     * @return a reference to the Publisher who published this event.
     *         Never null.
     */
    public abstract Serializable publishedBy ();


    /**
     * <p>
     * Returns the class of this event.
     * </p>
     *
     * @return the class of event.  Never null.
     */
    public abstract Reference eventClass ();


    /**
     * <p>
     * Called after a Subscriber has handled the event.
     * </p>
     *
     * <p>
     * Can be used to count the number of Susbcribers who handle the
     * event.
     * </p>
     *
     * <p>
     * In systems where a result (such as "handled" or "ignored")
     * is desirable, the result returned by Subscriber.handle ()
     * is non-null.  All Publications should be able to handle
     * null results or results of unexpected classes from Subscribers
     * who do not know about a given Publication class's result format
     * (for example, by treating them by default as "handled").
     * </p>
     */
    public abstract void handledBy (
                                    Subscriber subscriber,
                                    Serializable result
                                    );


    /**
     * <p>
     * Called to block the current thread until the event has
     * been handled by all subscribers.
     * </p>
     *
     * @return Always returns this Publication.
     */
    public Publication waitFor ();
}
