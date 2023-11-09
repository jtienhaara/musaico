package musaico.pubsub;


import musaico.io.Reference;


/**
 * <p>
 * Represents one class of events subscribed to by one Subscriber.
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
public interface Subscription
{
    /**
     * <p>
     * Returns the Subscriber whose subscription this is.
     * </p>
     *
     * @return The Subscriber.  Never null.
     */
    public abstract Subscriber subscriber ();


    /**
     * <p>
     * Returns the class of events to subscribe to.
     * </p>
     *
     * @return The class of events to subscribe to.  Never null.
     */
    public abstract Reference eventClass ();
}
