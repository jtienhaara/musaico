package musaico.pubsub;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Reference;


/**
 * <p>
 * A simple hub for sending publications to local subscribers.
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
public class SimpleHub
    implements Hub, Serializable
{
    /** Synchronize critical sections on this token: */
    private final Serializable lock = new String ();

    /** The lookup (by publication type) of Subscriptions. */
    private Map<Reference,List<Subscription>> subscriptions;


    /**
     * <p>
     * Creates a new SimpleHub.
     * </p>
     */
    public SimpleHub ()
    {
        this.subscriptions = new HashMap<Reference,List<Subscription>> ();
    }


    /**
     * @see musaico.pubsub.Hub#add(Subscription)
     */
    public Hub add (
                    Subscription subscription
                    )
    {
        if ( subscription == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot add null subscription to Hub [%hub%]",
                                                     "hub", this );
        }

        Reference event_class = subscription.eventClass ();
        if ( event_class == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot add subscription [%subscription%] with null event class to Hub [%hub%]",
                                                     "subscription", subscription,
                                                     "hub", this );
        }

        Subscriber subscriber = subscription.subscriber ();
        if ( subscriber == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot add subscription [%subscription%] event class [%event_class%] with null subscriber to Hub [%hub%]",
                                                     "subscription", subscription,
                                                     "event_class", event_class,
                                                     "hub", this );
        }

        synchronized ( this.lock )
        {
            List<Subscription> event_subscriptions =
                this.subscriptions.get ( event_class );
            if ( event_subscriptions == null )
            {
                event_subscriptions = new ArrayList<Subscription> ();
                this.subscriptions.put ( event_class, event_subscriptions );
            }

            event_subscriptions.add ( subscription );
        }

        return this;
    }


    /**
     * @see musaico.pubsub.Susbcriber#handle(Publication)
     */
    public Serializable handle (
                                Publication event
                                )
    {
        if ( event == null )
        {
            throw new I18nIllegalArgumentException ( "Hub [%hub%] cannothandle null event",
                                                     "hub", this );
        }

        Reference event_class = event.eventClass ();
        if ( event_class == null )
        {
            throw new I18nIllegalArgumentException ( "Hub [%hub%] cannot handle event [%event%] with null event class",
                                                     "hub", this,
                                                     "event", event );
        }

        List<Subscription> event_subscriptions =
            this.subscriptions.get ( event_class );
        if ( event_subscriptions == null )
        {
            return SimpleResult.unhandled ();
        }

        Result result = SimpleResult.unhandled ();
        for ( int s = 0; s < event_subscriptions.size (); s ++ )
        {
            Subscription subscription = event_subscriptions.get ( s );
            if ( subscription == null )
            {
                continue;
            }

            Subscriber subscriber = subscription.subscriber ();
            if ( subscriber == null )
            {
                continue;
            }

            try
            {
                Serializable subscriber_result =
                    subscriber.handle ( event );
                result = SimpleResult.or ( result, subscriber_result );
            }
            catch ( Throwable t )
            {
                result = SimpleResult.or ( result, t );
            }
        }

        return result;
    }


    /**
     * @see musaico.pubsub.Hub#numSubscriptions()
     */
    public long numSubscriptions ()
    {
        long num_subscriptions = 0L;
        synchronized ( this.lock )
        {
            Iterator<Reference> it = this.subscriptions.keySet ().iterator ();
            while ( it.hasNext () == true )
            {
                Reference event_class = it.next ();
                List<Subscription> event_subscriptions =
                    this.subscriptions.get ( event_class );

                if ( event_subscriptions == null )
                {
                    continue;
                }

                num_subscriptions += (long) event_subscriptions.size ();
            }
        }

        return num_subscriptions;
    }


    /**
     * @see musaico.pubsub.Hub#publish(Publication)
     */
    public void publish (
                         Publication publication
                         )
    {
        this.handle ( publication );
    }


    /**
     * @see musaico.pubsub.Hub#remove(Subscription)
     */
    public Subscription remove (
                                Subscription subscription
                                )
    {
        if ( subscription == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot remove null subscription from hub [%hub%]",
                                                     "hub", this );
        }

        Reference event_class = subscription.eventClass ();
        if ( event_class == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot remove subscription [%subscription%] with null event class from hub [%hub%]",
                                                     "subscription", subscription,
                                                     "hub", this );
        }

        Subscriber subscriber = subscription.subscriber ();
        if ( subscriber == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot remove subscription [%subscription%] with null subscriber from hub [%hub%]",
                                                     "subscription", subscription,
                                                     "hub", this );
        }

        synchronized ( this.lock )
        {
            List<Subscription> event_subscriptions =
                this.subscriptions.get ( event_class );
            if ( event_subscriptions != null )
            {
                if ( event_subscriptions.remove ( subscription ) == true )
                {
                    return subscription;
                }
            }
        }

        // Could not remove the subscription.
        return null;
    }


    /**
     * @see musaico.pubsub.Hub#subscriptions()
     */
    public Subscription [] subscriptions ()
    {
        List<Subscription> all_subscriptions = new ArrayList<Subscription> ();
        synchronized ( this.lock )
        {
            Iterator<Reference> it = this.subscriptions.keySet ().iterator ();
            while ( it.hasNext () == true )
            {
                Reference event_class = it.next ();
                List<Subscription> event_subscriptions =
                    this.subscriptions.get ( event_class );

                if ( event_subscriptions == null )
                {
                    continue;
                }

                all_subscriptions.addAll ( event_subscriptions );
            }
        }

        Subscription [] template = new Subscription [ 0 ];
        Subscription [] all_subscriptions_array =
            all_subscriptions.toArray ( template );

        return all_subscriptions_array;
    }
}
