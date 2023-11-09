package musaico.state;


import java.io.Serializable;


import musaico.io.Reference;


/**
 * <p>
 * Represents one Node in a Graph.
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
public interface Node
    extends Serializable
{
    /**
     * <p>
     * Returns the Arc representing the specified label.
     * </p>
     *
     * <p>
     * Returns null if and only if the specified label is not
     * included in the result of labels ().
     * </p>
     */
    public abstract Arc arc (
                             Reference label
                             );


    /**
     * <p>
     * Returns the automatic Arc which occurs whenever
     * a machine enters this Node.
     * </p>
     *
     * <p>
     * If null then there is no automatic arc, and the machine
     * remains in this Node until an event causes it to transition
     * to a new Node.
     * </p>
     */
    public abstract Arc automatic ();


    /**
     * <p>
     * Returns true if this Node contains an Arc to the
     * specified target Node.
     * </p>
     */
    public abstract boolean hasArcTo (
                                      Node target
                                      );


    /**
     * <p>
     * Returns the default "last chance" Arc out of this Node.
     * </p>
     *
     * <p>
     * If null then there is no default Arc out of this Node.
     * </p>
     */
    public abstract Arc otherwiseArc ();


    /**
     * <p>
     * Returns the labels of the Arcs out o this Node.
     * </p>
     *
     * <p>
     * Never null (though a zero-length array indicates no
     * Arcs out of this Node).
     * </p>
     *
     * <p>
     * No duplicate labels will ever be returned.
     * </p>
     *
     * <p>
     * Order may or may not be important, depending on the Node.
     * </p>
     */
    public abstract Reference [] labels ();
}
