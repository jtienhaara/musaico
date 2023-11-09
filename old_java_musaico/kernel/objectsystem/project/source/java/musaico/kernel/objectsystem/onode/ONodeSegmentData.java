package musaico.kernel.objectsystem.onode;

import java.io.Serializable;


import musaico.kernel.memory.SegmentIdentifier;


/**
 * <p>
 * ONodeData built on top of a Segment of paged
 * memory.
 * </p>
 *
 * <p>
 * Every ONodeData must implement the Record interface, to provide
 * open, close, read and write access to the data for the ONode.
 * </p>
 *
 *
 * <p>
 * Because object systems can conceivably be distributed and using
 * RMI, every Record must be Serializable in order to play nicely
 * over RMI.
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
 * Copyright (c) 2012 Johann Tienhaara
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
public interface ONodeSegmentData
    extends Serializable
{
    /**
     * <p>
     * Returns the unique identifier of the paged area backing
     * this ONodeData.
     * </p>
     *
     * <p>
     * The Segment provides swapping in from the backing driver(s),
     * and synchronization between swap states.
     * </p>
     *
     * <p>
     * It can be retrieved from the kernel with
     * <code> my_module.kernelObject ( Segment.class, ref ) </code>,
     * where <code> ref </code> is the SegmentIdentifier
     * returned by this method.
     * </p>
     *
     * <p>
     * Note that some ONodeData are not actually backed by
     * Segments, so SegmentIdentifier.NONE shall be returned.
     * For example the configuration ONodeData for a SwapSystemONode
     * might write directly to values in the data structures,
     * rather than writing to a Segment of memory.
     * </p>
     *
     * @return The identifier which specifies the Segment
     *         backing this ONodeData.  Never null.
     */
    public abstract SegmentIdentifier segmentRef ();
}
