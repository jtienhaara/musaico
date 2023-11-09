package musaico.kernel.task;

import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Path;
import musaico.io.Reference;
import musaico.io.Sequence;


/**
 * <p>
 * An ordered "region" of priorites which can be stepped through.
 * </p>
 *
 *
 * <p>
 * In Java every Priority must be Serializable, in order to
 * play nicely over RMI.
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
 * Copyright (c) 2011, 2012 Johann Tienhaara
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
public class Priorities
    extends Sequence<Priority>
    implements Serializable
{
    /** Standard priorities (low, medium, high). */
    public static final Priorities STANDARD_PRIORITIES =
        new Priorities ( new Path ( "/priorities/standard" ),
                         new Priority [] {
                             Priority.BATCH,
                             Priority.MEDIUM,
                             Priority.REALTIME
                         },
                         Priority.MEDIUM );


    /** The "normal" priority for this list. */
    private final Priority normalPriority;


    /**
     * <p>
     * Creates a new Priorities with the specified identifier
     * and array of Priority's.
     * </p>
     *
     * @param id The unique identifier for this Priorities,
     *           such as an id for the kernel which allows
     *           this range of Priorities.  Must not be null.
     *
     * @param priorities The Priority objects making up this
     *                   Priorities.  Must not be null.  Each
     *                   member must not be null.
     *
     * @param normal_priority The "normal" priority for the
     *                        system.  Typically this would
     *                        be the "middle" priority (such
     *                        as <code> Priority.MEDIUM </code>).
     *                        However in a real-time system, the
     *                        highest priority might be considered
     *                        normal.  Or in a batch processing
     *                        system, the lowest priority might be
     *                        considered normal.  And so on.
     *                        Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (such as null).
     */
    public Priorities (
                       Reference id,
                       Priority [] priorities,
                       Priority normal_priority
                       )
    {
        super ( Priority.NO_SUCH_PRIORITY, priorities );

        if ( normal_priority == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a Priorities with id [%id%] priorities [%priorities%] normal priority [%normal_priority%]",
                                                     "id", id,
                                                     "priorities", priorities,
                                                     "normal_priority", normal_priority );
        }

        this.normalPriority = normal_priority;
    }


    /**
     * <p>
     * Returns the "normal" priority for this list.
     * </p>
     *
     * <p>
     * For example, in a priorities list of { BATCH, MEDIUM, REAL_TIME },
     * the MEDIUM priority would typically be returned.  However
     * if the system is set up for real-time operations, REAL_TIME
     * might be the "normal" priority.  And if the system is set up for
     * slow batch operations, BATCH might be the "normal" priority.
     * and so on.
     * </p>
     *
     * @return This Priorities list's "normal" priority.  Never null.
     */
    public Priority normal ()
    {
        return this.normalPriority;
    }
}
