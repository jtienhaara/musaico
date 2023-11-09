package musaico.state;


import musaico.io.Reference;


/**
 * <p>
 * Represents an event containing a label.
 * </p>
 *
 * <p>
 * Events are the causes of node-to-node arc Traversals.
 * For example, a state machine might instigate a Traversal
 * caused by an incoming Event.
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
 * Copyright (c) 2009, 2011 Johann Tienhaara
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
public interface Event
{
    /**
     * <p>
     * Returns the label for this Event.
     * </p>
     *
     * <p>
     * Typically the label is used to trigger some node-to-node
     * arc Traversal (such as a state transition in a state machine).
     * </p>
     *
     * @return The label of this Event.
     */
    public abstract Reference label ();
}
