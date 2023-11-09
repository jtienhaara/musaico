package musaico.state;


import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;
import musaico.i18n.exceptions.I18nIllegalStateException;


/**
 * <p>
 * Represents a simple arc from one node to another.
 * </p>
 *
 * <p>
 * Not thread-safe!  Synchronize externally before building
 * up a SimpleArc from multiple threads.
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
public class SimpleArc
    implements Arc, ArcBuilder, Serializable
{
    /** The source Node. */
    private Node sourceNode = null;

    /** The target Node. */
    private Node targetNode = null;

    /** The logic to run during traversal across this arc (if any). */
    private Traverser traverser = null;


    /**
     * @see musaico.state.ArcBuilder#arc()
     */
    public Arc build ()
    {
        if ( this.sourceNode == null )
        {
            // Not ready to build yet.
            throw new I18nIllegalStateException ( "No source node specified for Arc [%arc%]",
                                                  "arc", this );
        }
        else if ( this.targetNode == null )
        {
            // Not ready to build yet.
            throw new I18nIllegalStateException ( "No target node specified for Arc [%arc%]",
                                                  "arc", this );
        }

        return this;
    }


    /**
     * @see musaico.state.ArcBuilder#executes(musaico.state.Traverser)
     */
    public ArcBuilder executes (
                                Traverser traverser
                                )
    {
        this.traverser = traverser;

        return this;
    }


    /**
     * @see musaico.state.ArcBuilder#from(Node)
     */
    public ArcBuilder from (
                            Node source
                            )
    {
        if ( source == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot connect arc [%arc%] to null node",
                                                     "arc", this );
        }

        if ( this.sourceNode == null )
        {
            this.sourceNode = source;
        }

        return this;
    }


    /**
     * @see musaico.state.ArcBuilder#go(Node)
     */
    public Node go (
                    Node target
                    )
    {
        this.to ( target );
        this.build ();

        return this.target ();
    }


    /**
     * @see musaico.state.Arc#source()
     */
    public Node source ()
    {
        return this.sourceNode;
    }


    /**
     * @see musaico.state.Arc#target()
     */
    public Node target ()
    {
        return this.targetNode;
    }


    /**
     * @see musaico.state.ArcBuilder#to(Node)
     */
    public ArcBuilder to (
                          Node target
                          )
    {
        if ( target == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot connect arc [%arc%] to null node",
                                                     "arc", this );
        }

        if ( this.targetNode == null
             || this.targetNode == this.sourceNode )
        {
            this.targetNode = target;
        }

        return this;
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return "" + this.source () + " -> " + this.target ();
    }


    /**
     * @see musaico.state.Arc#traverse(musaico.state.Traversal)
     */
    public Arc traverse (
                         Traversal traversal
                         )
        throws TraversalException
    {
        if ( this.traverser != null )
        {
            if ( traversal == null )
            {
                throw new TraversalException ( "Cannot traverse arc [%arc%] with a null traversal",
                                               "arc", this );
            }

            traversal.arc ( this );

            this.traverser.execute (
                                    traversal
                                    );
        }

        return this;
    }
}
