package musaico.state;


import java.io.Serializable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Reference;


/**
 * <p>
 * Represents one simple Node in a Graph.
 * </p>
 *
 * <p>
 * Not thread-safe!  Synchronize externally before building
 * up a SimpleNode from multiple threads.
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
public class SimpleNode
    implements Node, Serializable
{
    /** The name of this node. */
    private final String name;

    /** Unordered lookup of Arc by label.
     *  Includes AutomaticLabel and our otherwiseLabel. */
    private final Map<Reference,SimpleArc> arcs =
        new HashMap<Reference,SimpleArc> ();

    /** The "otherwise label", used for looking up the default
     *  "last chance" arc. */
    private final Reference otherwiseLabel = new UniqueLabel ();


    /**
     * <p>
     * Creates a new SimpleNode with the specified node name.
     * </p>
     */
    public SimpleNode( String node_name )
    {
        if ( node_name == null )
        {
            throw new I18nIllegalArgumentException( "SimpleNode cannot have a null name" );
        }

        this.name = node_name;
    }


    /**
     * @see musaico.state.Node#arc(musaico.io.Reference)
     */
    public Arc arc (
                    Reference label
                    )
    {
        return this.arcs.get ( label );
    }


    /**
     * @see musaico.state.Node#automatic()
     */
    public Arc automatic ()
    {
        return this.arcs.get ( AutomaticLabel.get () );
    }


    /**
     * @see musaico.state.NodeBuilder#automatically()
     */
    public ArcBuilder automatically ()
    {
        SimpleArc automatic_arc = (SimpleArc)
            this.arc ( AutomaticLabel.get () );
        if ( automatic_arc == null )
        {
            automatic_arc = new SimpleArc ();
            automatic_arc.from ( this );
            // Let the caller specify where the arc automatically
            // goes to.
            this.arcs.put ( AutomaticLabel.get (), automatic_arc );
        }

        return automatic_arc;
    }


    /**
     * @see musaico.state.NodeBuilder#build()
     */
    public Node build ()
    {
        return this;
    }


    /**
     * @see musaico.state.Node#hasArcTo( Node )
     */
    public boolean hasArcTo (
                             Node target
                             )
    {
        if ( target == null )
        {
            return false;
        }

        for ( int t = 0; t < this.arcs.size (); t ++ )
        {
            Arc arc = this.arcs.get ( t );
            if ( arc != null )
            {
                Node curr_target = arc.target ();
                if ( target.equals ( curr_target ) )
                {
                    return true;
                }
            }
        }

        // Nope, none of the Arcs lead to the specified Node.
        return false;
    }


    /**
     * @see musaico.state.Node#labels()
     */
    public Reference [] labels ()
    {
        Reference [] labels = new Reference [ this.arcs.size () ];
        int l = 0;
        for ( Reference label : this.arcs.keySet () )
        {
            labels [ l ] = label;

            l ++;
        }

        return labels;
    }


    /**
     * @see musaico.state.NodeBuilder#on(musaico.io.Reference)
     */
    public ArcBuilder on (
                          Reference label
                          )
    {
        // Send the existing arc back, if any exists.
        SimpleArc arc = (SimpleArc) this.arc ( label );
        if ( arc == null )
        {
            arc = new SimpleArc ();
            arc.from ( this );
            arc.to ( this );
            this.arcs.put ( label, arc );
        }

        return arc;
    }


    /**
     * @see musaico.state.Node#otherwiseArc()
     */
    public Arc otherwiseArc ()
    {
        return this.arc ( this.otherwiseLabel );
    }


    /**
     * @see musaico.state.NodeBuilder#otherwise()
     */
    public ArcBuilder otherwise ()
    {
        SimpleArc otherwise_arc = this.arcs.get ( this.otherwiseLabel );
        if ( otherwise_arc == null )
        {
            otherwise_arc = new SimpleArc ();
            otherwise_arc.from ( this );
            otherwise_arc.to ( this );

            this.arcs.put ( this.otherwiseLabel, otherwise_arc );
        }

        return otherwise_arc;
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return this.name;
    }
}
