package musaico.kernel.task.queue;

import java.io.Serializable;


import musaico.io.AbstractOrder;
import musaico.io.Comparison;

import musaico.time.AbsoluteTime;
import musaico.time.Time;


/**
 * <p>
 * Compares ScheduledTasks by their next scheduled execution times.
 * </p>
 *
 *
 * <p>
 * In Java, every Order must be Serializable in
 * order to play nicely over RMI.
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
public class ScheduledTaskOrder
    extends AbstractOrder<ScheduledTask>
    implements Serializable
{
    /** The typical order of scheduled tasks is in order
     *  of earliest execution. */
    public static final ScheduledTaskOrder STANDARD =
        new ScheduledTaskOrder ();


    /**
     * <p>
     * Creates a new ScheduledTaskOrder.
     * </p>
     */
    public ScheduledTaskOrder ()
    {
        this ( "Scheduled task order" );
    }


    /**
     * <p>
     * Creates a new ScheduledTaskOrder with the specified
     * description internationalized text.
     * </p>
     *
     * <p>
     * Make sure you put internationalized_description_text
     * into your Messages.properties file, and translate it
     * for other locales!
     * </p>
     *
     * @param internationalized_description_text The international
     *                                           identifier for the
     *                                           description message
     *                                           for this order.
     *                                           This text will be
     *                                           internationalized, and
     *                                           then the Messages.properties
     *                                           files will be searched any
     *                                           time the description of
     *                                           this Order is localized
     *                                           (for example, by a client
     *                                           application).
     *                                           Must not be null.
     */
    protected ScheduledTaskOrder (
                                  String internationalized_description_text
                                  )
    {
        super ( internationalized_description_text );
    }


    /**
     * @see musaico.io.Order#compareValues(java.lang.Object,java.lang.Object)
     */
    @Override
    public Comparison compareValues (
                                     ScheduledTask task1,
                                     ScheduledTask task2
                                     )
    {
        if ( task1 == null )
        {
            if ( task2 == null )
            {
                // null == null.
                return Comparison.LEFT_EQUALS_RIGHT;
            }
            else
            {
                // null > any ScheduledTask.
                return Comparison.LEFT_GREATER_THAN_RIGHT;
            }
        }
        else if ( task2 == null )
        {
            // Any ScheduledTask < null.
            return Comparison.LEFT_LESS_THAN_RIGHT;
        }

        Time next_time1 = task1.nextExecutionTime ();
        Time next_time2 = task2.nextExecutionTime ();

        if ( next_time1 == null
             || ! ( next_time1 instanceof AbsoluteTime ) )
        {
            if ( next_time2 == null
                 || ! ( next_time2 instanceof AbsoluteTime ) )
            {
                // ImpossibleTime = ImpossibleTime.
                return Comparison.LEFT_EQUALS_RIGHT;
            }
            else
            {
                // ImpossibleTime > any AbsoluteTime.
                return Comparison.LEFT_GREATER_THAN_RIGHT;
            }
        }
        else if ( next_time2 == null
                  || ! ( next_time2 instanceof AbsoluteTime ) )
        {
            // Any AbsoluteTime < ImpossibleTime.
            return Comparison.LEFT_LESS_THAN_RIGHT;
        }

        AbsoluteTime next_abs_time1 = (AbsoluteTime) next_time1;
        AbsoluteTime next_abs_time2 = (AbsoluteTime) next_time2;

        int comparator_value = next_abs_time1.compareTo ( next_abs_time2 );
        Comparison comparison =
            Comparison.fromComparatorValue ( comparator_value );

        return comparison;
    }
}
