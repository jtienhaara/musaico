package musaico.state;


import java.io.Serializable;


import musaico.io.Reference;


/**
 * <p>
 * Represents a state machine.
 * </p>
 *
 * <p>
 * After creating a state Machine, make sure to call transition ()
 * in order to execute any initial transitions to the starting state.
 * </p>
 *
 * <p>
 * For example:
 * </p>
 *
 * <pre>
 *     Machine my_machine = new XYZMachine ( my_graph );
 *     my_machine.transition (); // Automatically transitions to start state.
 * </pre>
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
public interface Machine
    extends Serializable
{
    /**
     * <p>
     * Tells the machine to handle the specified event.
     * </p>
     *
     * <p>
     * If the <code>event.label ()</code> trigger would
     * cause a <code>transition ()</code>, then that transition occurs.
     * </p>
     */
    public abstract Machine fire (
                                  Event event
                                  )
        throws TraversalException;


    /**
     * <p>
     * Returns the Graph for this Machine.
     * </p>
     */
    public abstract Graph graph ();


    /**
     * <p>
     * Returns the current state Node for this Machine.
     * </p>
     */
    public abstract Node state ();


    /**
     * <p>
     * Triggers an automatic transition from this Machine's current
     * state, if possible.
     * </p>
     */
    public abstract Machine transition ()
        throws TraversalException;


    /**
     * <p>
     * Triggers a transition from this Machine's current
     * state, given the specified trigger, if possible.
     * </p>
     */
    public abstract Machine transition (
                                        Reference trigger
                                        )
        throws TraversalException;
}
