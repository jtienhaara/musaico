package musaico.pubsub;


import java.io.Serializable;


/**
 * <p>
 * Represents a Result from publishing or handling an event.
 * </p>
 *
 * <p>
 * Result provides a simple mechanism for reporting on whether
 * publications were handled by subscribers.  They are not a
 * mandatory piece of the pubsub puzzle, though, since any
 * Serializable object (or null) can be returned from
 * <code> Subscriber.handle () </code> and <code> Hub.publish () </code>.
 * Therefore care should be taken to avoid assuming return types
 * of Result.
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
public interface Result
    extends Serializable
{
}
