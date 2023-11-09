package musaico.pubsub;


/**
 * <p>
 * Represents a hub for publishers and subscribers of events.
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
public interface Hub
    extends Subscriber
{
    /**
     * <p>
     * Adds a subscription to this Hub.
     * </p>
     *
     * @return Always returns this Hub.
     */
    public abstract Hub add (
                             Subscription subscription
                             );


    /**
     * <p>
     * Returns the number of subscriptions in this Hub.
     * </p>
     *
     * @return The number of subscriptions.  Never negative.
     */
    public abstract long numSubscriptions ();


    /**
     * <p>
     * Publishes an event through the Hub.
     * </p>
     *
     * <p>
     * Depending on the implementation, this method may block
     * or it may return asynchronously.  If the caller depends
     * on blocking, then the correct sequence of calls is:
     * </p>
     *
     * <pre>
     *     hub.publish ( publication );
     *     publication.waitFor ();
     * </pre>
     *
     * <p>
     * However no caller should ever assume asynchronous operation.
     * </p>
     */
    public void publish (
                         Publication publication
                         );


    /**
     * <p>
     * Removes a subscription from this Hub.
     * </p>
     *
     * @return The specified Subscription, if removed, or null
     *         if it did not exist in this Hub.
     */
    public abstract Subscription remove (
                                         Subscription subscription
                                         );


    /**
     * <p>
     * Returns the Subscriptions handled by this Hub.
     * </p>
     *
     * @return The array of Subscriptions handled by this Hub.
     *         The array is never used internally by the Hub, so it
     *         may be manipulated by the caller.  Never null.
     */
    public abstract Subscription [] subscriptions ();
}
