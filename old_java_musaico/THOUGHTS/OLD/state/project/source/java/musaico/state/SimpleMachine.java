package musaico.state;


import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Reference;


/**
 * <p>
 * Represents a simple state machine.
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
 *     Machine my_machine = new SimpleMachine ( my_graph );
 *     my_machine.transition (); // Automatically transitions to start state.
 * </pre>
 *
 * <p>
 * Note that the SimpleMachine <i>is</i> thread-safe (unlike all the other
 * "Simple" classes in this library).  It can safely be used from
 * multiple threads.
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
public class SimpleMachine
    implements Machine, Serializable
{
    /** Synchronize critical sections on this token: */
    private final Serializable lock = new String ();

    /** This machine's graph. */
    private final Graph graph;

    /** The current state (null to begin). */
    private Node state = null;


    /**
     * <p>
     * Creates a new SimpleMachine from the specified Graph.
     * </p>
     */
    public SimpleMachine (
                          Graph graph
                          )
    {
        if ( graph == null )
        {
            throw new I18nIllegalArgumentException ( "No graph specified for SimpleMachine constructor" );
        }

        this.graph = graph;
    }


    /**
     * @see musaico.state.Machine#fire(Event)
     */
    public Machine fire (
                         Event event
                         )
        throws TraversalException
    {
        Reference trigger = event.label ();
        if ( trigger == null )
        {
            throw new TraversalException ( "Null label for event [%event%] passed to machine [%machine%] .fire ()",
                                           "event", event,
                                           "machine", this );
        }

        synchronized ( this.lock )
        {
            // If this is the entry event or an automatic
            // event, call the no-args transition () method instead.
            if ( this.state == null
                 && EnterLabel.get ().equals ( trigger ) )
            {
                return this.transition ();
            }
            else if ( this.state != null
                      && AutomaticLabel.get ().equals ( trigger ) )
            {
                return this.transition ();
            }

            // Fail if we haven't yet entered the starting state.
            if ( this.state == null )
            {
                throw new TraversalException ( "Machine [%machine%] has not been initialized by calling transition () into the starting state",
                                               "machine", this );
            }

            // Find the arc for this trigger.
            Arc arc = this.state.arc ( trigger );
            if ( arc == null )
            {
                // No trigger-specific arc.
                // Get the default one instead.
                arc = this.state.otherwiseArc ();
            }

            if ( arc != null )
            {
                // Transition the machine into its new state.
                Traversal transition = new SimpleTraversal ()
                    .context ( this )
                    .label ( trigger )
                    .cause ( event );
                this.state = arc.traverse ( transition ).target ();
            }
        }

        return this;
    }


    /**
     * @see musaico.state.Machine#graph()
     */
    public Graph graph ()
    {
        return this.graph;
    }


    /**
     * @see musaico.state.Machine#state()
     */
    public Node state ()
    {
        return this.state;
    }


    /**
     * @see musaico.state.Machine#transition()
     */
    public Machine transition ()
        throws TraversalException
    {
        synchronized ( this.lock )
        {
            // We have not yet transitioned into the starting state.
            // Do so now.
            if ( this.state == null )
            {
                Arc enterArc = this.graph.enter ();
                if ( enterArc == null )
                {
                    throw new TraversalException( "Graph for machine [%machine%] has no entry transition",
                                                  "machine", this );
                }

                Traversal enterTransition =
                    new SimpleTraversal ()
                    .context ( this )
                    .label ( EnterLabel.get () );

                this.state = enterArc
                    .traverse ( enterTransition )
                    .target ();
            }
            // If possible, transition automatically into the next state.
            // Otherwise, remain in the current state.
            else
            {
                Arc arc = this.state.automatic ();
                if ( arc != null )
                {
                    Traversal automaticTransition =
                        new SimpleTraversal ()
                        .context ( this )
                        .label ( AutomaticLabel.get () );

                    this.state = arc
                        .traverse ( automaticTransition )
                        .target ();
                }
            }
        }

        return this;
    }


    /**
     * @see musaico.state.Machine#transition(musaico.io.Reference)
     */
    public Machine transition (
                               Reference trigger
                               )
        throws TraversalException
    {
        synchronized ( this.lock )
        {
            // If this is the entry transition or an automatic
            // transition, call the other transition method instead.
            if ( this.state == null
                 && trigger != null
                 && trigger.equals ( EnterLabel.get () ) )
            {
                return this.transition ();
            }
            else if ( this.state != null
                      && trigger != null
                      && trigger.equals ( AutomaticLabel.get () ) )
            {
                return this.transition ();
            }

            // Fail if we haven't yet entered the starting state.
            if ( this.state == null )
            {
                throw new TraversalException( "Machine [%machine%] has not been initialized by calling transition () into the starting state",
                                              "machine", this );
            }

            // Find the arc for this trigger.
            Arc arc = this.state.arc ( trigger );
            if ( arc == null )
            {
                // No trigger-specific arc.
                // Get the default one instead.
                arc = this.state.otherwiseArc ();
            }

            if ( arc != null )
            {
                // Transition the machine into its new state.
                Traversal transition = new SimpleTraversal ()
                    .context ( this )
                    .label ( trigger );
                this.state = arc.traverse ( transition ).target ();
            }
        }

        return this;
    }
}
