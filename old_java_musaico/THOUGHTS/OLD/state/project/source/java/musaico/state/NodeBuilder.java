package musaico.state;


import java.io.Serializable;


import musaico.io.Reference;


/**
 * <p>
 * Builds a Node.  (Can be a Node which builds itself.)
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
public interface NodeBuilder
    extends Serializable
{
    /**
     * <p>
     * Creates or returns the automatic Arc which occurs whenever
     * a machine enters this builder's Node.
     * </p>
     *
     * <p>
     * Never null.
     * </p>
     */
    public abstract ArcBuilder automatically ();


    /**
     * <p>
     * Constructs this builder's Node, and all Arcs (if necessary).
     * </p>
     */
    public abstract Node build ();


    /**
     * <p>
     * Creates or returns the default Arc out of this Node.
     * </p>
     *
     * <p>
     * Never null.  Creates a new ArcBuilder if none exists already
     * for the otherwise arc.
     * </p>
     */
    public abstract ArcBuilder otherwise ();


    /**
     * <p>
     * Creates or returns the Arc for the specified label.
     * </p>
     *
     * <p>
     * Never null.
     * </p>
     */
    public abstract ArcBuilder on (
                                   Reference label
                                   );
}
