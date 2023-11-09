package musaico.state;


import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;


/**
 * <p>
 * A simple graph of nodes.
 * </p>
 *
 * <p>
 * Not thread-safe!  Synchronize externally before building
 * up a SimpleGraph from multiple threads.
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
public class SimpleGraph
    implements Graph, GraphBuilder, Serializable
{
    /** The name of this graph. */
    private final String graphName;

    /** Entry arc. */
    private SimpleArc enterArc = null;

    /** Exit arc (if any). */
    private SimpleArc exitArc = null;


    /**
     * <p>
     * Creates a new SimpleGraph with the specified name.
     * </p>
     */
    public SimpleGraph(
                       String graph_name
                       )
    {
        if ( graph_name == null )
        {
            throw new I18nIllegalArgumentException( "SimpleGraph cannot have a null name" );
        }

        this.graphName = graph_name;
    }


    /**
     * @see musaico.state.GraphBuilder#graph()
     */
    public Graph build ()
    {
        return this;
    }


    /**
     * @see musaico.state.Graph#enter()
     */
    public Arc enter ()
    {
        return this.enterArc;
    }


    /**
     * @see musaico.state.Graph#exit()
     */
    public Arc exit ()
    {
        return this.exitArc;
    }


    /**
     * @see musaico.state.GraphBuilder#onEnter()
     */
    public ArcBuilder onEnter ()
    {
        if ( this.enterArc == null )
        {
            this.enterArc = new SimpleArc ();
            this.enterArc.from ( NullNode.get () );

            // Let the caller call ArcBuilder.to ()
            // or ArcBuilder.go () to set the target node.
        }

        return this.enterArc;
    }


    /**
     * @see musaico.state.GraphBuilder#onExit()
     */
    public ArcBuilder onExit ()
    {
        if ( this.exitArc == null )
        {
            this.exitArc = new SimpleArc ();

            // Arc goes to and from nowhere.
            this.exitArc.from ( NullNode.get () );
            this.exitArc.go ( NullNode.get () );
        }

        return this.exitArc;
    }


    /**
     * @see musaico.state.GraphBuilder#node(String)
     */
    public Node node (
                      String node_name
                      )
    {
        return new SimpleNode ( node_name );
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return this.graphName;
    }
}
