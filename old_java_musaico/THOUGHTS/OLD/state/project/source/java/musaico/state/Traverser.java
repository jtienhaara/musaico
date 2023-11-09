package musaico.state;


/**
 * <p>
 * Executes some logic while traversing an Arc in a Graph.
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
public interface Traverser
{
    /**
     * <p>
     * Executes some logic while traversing an Arc in a Graph.
     * </p>
     *
     * @param traversal The traversal description.
     *                  Implementation-dependent, but always has
     *                  a non-null <code>arc ()</code> representing
     *                  the Arc being traversed, and the Arc always
     *                  has non-null <code>target ()</code> and
     *                  <code>source ()</code> Nodes.
     *
     * @throws TraversalException If the traversal must not occur.
     */
    public abstract void execute (
                                  Traversal traversal
                                  )
        throws TraversalException;
}
