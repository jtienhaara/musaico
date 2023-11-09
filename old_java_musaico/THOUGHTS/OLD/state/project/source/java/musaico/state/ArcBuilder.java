package musaico.state;


import java.io.Serializable;


/**
 * <p>
 * Builds an Arc.  (Can be an Arc which builds itself.)
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
public interface ArcBuilder
    extends Serializable
{
    /**
     * <p>
     * Constructs this builder's Arc (if necessary)
     * and returns it.
     * </p>
     */
    public abstract Arc build ();


    /**
     * <p>
     * Sets the logic to execute when traversing this Arc (if any).
     * </p>
     */
    public abstract ArcBuilder executes (
                                         Traverser traverser
                                         );


    /**
     * <p>
     * Sets the source Node of this ArcBuilder (possibly
     * NullNode).
     * </p>
     *
     * <p>
     * Write-once, then read-only.
     * </p>
     */
    public abstract ArcBuilder from (
                                     Node source
                                     );


    /**
     * <p>
     * Sets the target Node of this ArcBuilder, constucts
     * the Arc (if necessary), and returns the target Node.
     * </p>
     */
    public abstract Node go (
                             Node target
                             );


    /**
     * <p>
     * Sets the target Node of this ArcBuilder (possibly
     * NullNode).
     * </p>
     *
     * <p>
     * Write-once, then read-only.
     * </p>
     */
    public abstract ArcBuilder to (
                                   Node target
                                   );
}
