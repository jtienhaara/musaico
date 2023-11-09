package musaico.state;

import java.io.Serializable;


import musaico.io.Reference;


/**
 * <p>
 * A simple Traversal representing the act of crossing an Arc from one
 * Node to the next.
 * </p>
 *
 * <p>
 * Not thread-safe!  Synchronize externally before building
 * up a SimpleTraversal from multiple threads.
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
public class SimpleTraversal
    implements Serializable, Traversal
{
    /** The Arc across which this Traversal occurs. */
    private Arc arc;

    /** The cause Event (if any) which caused this Traversal. */
    private Event cause;

    /** The context of this Traversal (implementation-dependent,
     *  for example, the machine whose graph is being traversed). */
    private Serializable context;

    /** The label of the Arc being traversed (which might be used
     *  as a trigger by a state machine). */
    private Reference label;


    /**
     * @see musaico.state.Traversal#arc()
     */
    public Arc arc ()
    {
        return this.arc;
    }


    /**
     * @see musaico.state.Traversal#arc(Arc)
     */
    public Traversal arc (
                          Arc arc
                          )
    {
        if ( this.arc != null )
        {
            return this;
        }

        this.arc = arc;
        return this;
    }


    /**
     * @see musaico.state.Traversal#cause()
     */
    public Event cause ()
    {
        return this.cause;
    }


    /**
     * @see musaico.state.Traversal#cause(Event)
     */
    public Traversal cause (
                            Event cause
                            )
    {
        if ( this.cause != null )
        {
            return this;
        }

        this.cause = cause;
        return this;
    }


    /**
     * @see musaico.state.Traversal#context()
     */
    public Serializable context ()
    {
        return this.context;
    }


    /**
     * @see musaico.state.Traversal#context(Serializable)
     */
    public Traversal context (
                              Serializable context
                              )
    {
        if ( this.context != null )
        {
            return this;
        }

        this.context = context;
        return this;
    }


    /**
     * @see musaico.state.Traversal#label()
     */
    public Reference label ()
    {
        return this.label;
    }


    /**
     * @see musaico.state.Traversal#label(musaico.io.Reference)
     */
    public Traversal label (
                            Reference label
                            )
    {
        if ( this.label != null )
        {
            return this;
        }

        this.label = label;
        return this;
    }
}
