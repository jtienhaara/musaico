package musaico.state;

import java.io.Serializable;


import musaico.io.Reference;


/**
 * <p>
 * Represents the act of traversing from one Node to another across
 * an Arc.
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
public interface Traversal
{
    /**
     * <p>
     * Returns the arc being traversed from one
     * Node to the next.
     * </p>
     *
     * @return The Arc being traversed.  Never null
     *         for a properly constructed Traversal.
     */
    public abstract Arc arc ();


    /**
     * <p>
     * Sets the arc being traversed from one
     * Node to the next.
     * </p>
     *
     * <p>
     * Write-once, then read-only.
     * </p>
     *
     * @return This Traversal.  Never null.
     */
    public abstract Traversal arc (
                                   Arc arc
                                   );


    /**
     * <p>
     * Returns the Event which caused the traversal (if any).
     * </p>
     *
     * <p>
     * For example, a "search" Event might cause a traversal
     * from one node to another in a data structure graph; or
     * some kind of "instigator" Event might cause a state
     * machine to transition from one state to another in a
     * state graph; and so on.
     * </p>
     *
     * @return The cause of this traversal.  Can be null, if
     *         no Event caused the traversal.
     */
    public abstract Event cause ();


    /**
     * <p>
     * Sets the cause Event for this Traversal.
     * </p>
     *
     * <p>
     * Write-once, then read-only.
     * </p>
     *
     * <p>
     * For example, a "search" Event might cause a traversal
     * from one node to another in a data structure graph; or
     * some kind of "instigator" Event might cause a state
     * machine to transition from one state to another in a
     * state graph; and so on.
     * </p>
     *
     * @return This Traversal.  Never null.
     */
    public abstract Traversal cause (
                                     Event cause
                                     );


    /**
     * <p>
     * Returns the context for the current Traversal.
     * </p>
     *
     * <p>
     * The context is implementation-dependent.
     * </p>
     *
     * <p>
     * For example, the context might be the state machine
     * when transitioning from one state node to another.
     * </p>
     *
     * @return The context for this Traversal, or null if none has been
     *         specified.
     */
    public abstract Serializable context ();


    /**
     * <p>
     * Sets the context for the current Traversal.
     * </p>
     *
     * <p>
     * The context is implementation-dependent.
     * </p>
     *
     * <p>
     * For example, the context might be the state machine
     * when transitioning from one state node to another.
     * </p>
     *
     * <p>
     * Write-once, then read-only.
     * </p>
     *
     * @return This Traversal.  Never null.
     */
    public abstract Traversal context (
                                       Serializable context
                                       );


    /**
     * <p>
     * Returns the label of the arc being traversed from one
     * Node to the next.
     * </p>
     *
     * @return The label of the Arc being traversed.  Never null
     *         for a properly constructed Traversal.
     */
    public abstract Reference label ();


    /**
     * <p>
     * Sets the label of the arc being traversed from one
     * Node to the next.
     * </p>
     *
     * <p>
     * Write-once, then read-only.
     * </p>
     *
     * @return This Traversal.  Never null.
     */
    public abstract Traversal label (
                                     Reference label
                                     );
}
