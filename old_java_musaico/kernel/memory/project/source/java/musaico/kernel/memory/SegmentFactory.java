package musaico.kernel.memory;

import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.security.Credentials;


/**
 * <p>
 * A factory which creates Segments and their PagedAreas when
 * requested.
 * </p>
 *
 * <p>
 * The SegmentFactory must not maintain any references to the
 * Segments or PagedAreas it creates.
 * </p>
 *
 *
 * <p>
 * In Java, every SegmentFactory must be Serializable in order to
 * play nicely over RMI, even if the segments it creates contain
 * non-serializable data.
 * </p>
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
 * Copyright (c) 2011 Johann Tienhaara
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
public interface SegmentFactory
{
    /**
     * <p>
     * Creates a new Segment and its underlying PagedArea.
     * </p>
     *
     * @param id The unique identifier of the Segment to create.
     *           Must not be null.  Must be unique throughout the
     *           running kernel.
     *
     * @param security Permissions management for the segment,
     *                 such as copy-on-write, read-only, and so on.
     *                 Must not be null.
     *
     * @param owner The owneer of the segment to be created,
     *              such as a module owner's credentials, a user,
     *              and so on.  Must not be null.
     *
     * @return The newly created Segment, containing the newly
     *         created PagedArea.  Never null.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      specified are invalid
     *                                      (such as null).
     *
     * @throws MemoryException If the memory requested cannot be
     *                         allocated due to physical constraints
     *                         or low-level error.
     */
    public abstract Segment createSegment (
                                           SegmentIdentifier id,
                                           SegmentSecurity security,
                                           Credentials owner
                                           )
        throws I18nIllegalArgumentException,
               MemoryException;
}
