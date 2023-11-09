package musaico.state;


import java.io.Serializable;


/**
 * <p>
 * A builder of Graphs.  (Can itself be a Graph, which builds itself.)
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
public interface GraphBuilder
    extends Serializable
{
    /**
     * <p>
     * Constructs the Graph (if necessary) and returns it.
     * </p>
     */
    public abstract Graph build ();


    /**
     * <p>
     * Creates or returns the arc into this Graph (from
     * before the starting or root node, or from a stack of graphs).
     * </p>
     *
     * <p>
     * This arc leads to the starting or root node.
     * </p>
     *
     * @return The entry ArcBuilder.  Never null.
     */
    public abstract ArcBuilder onEnter ();


    /**
     * <p>
     * Creates or returns the arc out from this Graph (from a final
     * node, or before leaving to pop this graph from a stack).
     * </p>
     *
     * <p>
     * This arc leads from any and all final nodess in the
     * graph.
     * </p>
     *
     * @return The exit ArcBuilder.  Never null.
     */
    public abstract ArcBuilder onExit ();


    /**
     * <p>
     * Creates a new disembodied Node with the specified name.
     * </p>
     *
     * @param node_name The name of the Node to create.  Never null.
     */
    public abstract Node node (
                               String node_name
                               );
}
