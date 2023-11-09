package musaico.state;


import java.io.Serializable;


/**
 * <p>
 * A graph of nodes.
 * </p>
 *
 * <p>
 * A state machine's transitions can be represented by a Graph.
 * However the Graph can be used to represent data structures
 * or other concepts instead.
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
public interface Graph
    extends Serializable
{
    /**
     * <p>
     * Returns the arc into this Graph (from before the starting
     * or root node, or from a stack of graphs).
     * </p>
     *
     * <p>
     * This arc leads to the starting or root node.
     * </p>
     *
     * @return The entry Arc.  Never null.
     */
    public abstract Arc enter ();


    /**
     * <p>
     * Returns the arc out from this Graph (from a final
     * node, or before leaving to pop this graph from a stack).
     * </p>
     *
     * <p>
     * This arc leads from any and all final nodes in the
     * graph.
     * </p>
     *
     * @return The exit Arc.  Possibly null.
     */
    public abstract Arc exit ();
}
