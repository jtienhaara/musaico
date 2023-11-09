package musaico.kernel.common.onodes;

import java.io.Serializable;


import musaico.kernel.memory.Segment;

import musaico.kernel.module.Module;

import musaico.kernel.objectsystem.RecordFlag;

import musaico.kernel.objectsystem.records.FlatRecord;

import musaico.kernel.common.records.SegmentBackedRecord;

import musaico.security.Security;


/**
 * <p>
 * The data for a flat record ONode (no children, only field data).
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
public class GenericFlatRecordData
    extends SegmentBackedRecord
    implements FlatRecord, Serializable
{
    /** The kernel module which loaded this data area and gives
     *  controlled access to the kernel. */
    private final Module module;


    /**
     * <p>
     * Creates a new data area for a flat record ONode, backed
     * by the specified segment.
     * </p>
     *
     * @param module The module which loaded this data area.
     *               Must not be null.
     *
     * @param segment The segment which will provide data for the ONode.
     *                Must not be null.
     *
     * @param security The security for this ONode data,
     *                 which dictates who is (and who is not) allowed
     *                 to open, close, read or write this data area.
     *                 Must not be null.
     */
    public GenericFlatRecordData (
                                  Module module,
                                  Segment segment,
                                  Security<RecordFlag> security
                                  )
    {
        super ( segment, security );

        this.module = module;
    }
}
