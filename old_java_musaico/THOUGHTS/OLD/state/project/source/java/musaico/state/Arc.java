package musaico.state;


import java.io.Serializable;


/**
 * <p>
 * Represents an arc from one Node to another.
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
public interface Arc
    extends Serializable
{
    /**
     * <p>
     * Returns the Node from which this Arc begins.
     * </p>
     *
     * @return The source node for this Arc.  Never null,
     *         but can be NullNode (for example, when traversing
     *         from a stack of Graphs into a new Graph).
     */
    public abstract Node source ();


    /**
     * <p>
     * Returns the Node toward which this Arc moves.
     * </p>
     *
     * @return The target node for this Arc.  Never null,
     *         but can be NullNode (for example, when traversing
     *         back to a stack of Graphs from the final node of a Graph).
     */
    public abstract Node target ();


    /**
     * <p>
     * Traverses this Arc, executing any logic necessary along the way.
     * </p>
     *
     * @param traversal The traversal description (including
     *                  a label and optionally other implementation-dependent
     *                  data).
     *
     * @return This Arc.
     */
    public abstract Arc traverse (
                                  Traversal traversal
                                  )
        throws TraversalException;
}
