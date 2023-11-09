package musaico.time;

import java.io.Serializable;


/**
 * <p>
 * Time range representation.
 * </p>
 *
 * <p>
 * A TimeRange is immutable.
 * </p>
 *
 * <p>
 * Each Time in a TimeRange typically represents an absolute (epoch)
 * time.  However a range of relative time intervals is also possible.
 * </p>
 *
 * <p>
 * A TimeRange itself must implement the Time interface to return
 * the difference of end () - start ().
 * </p>
 *
 * <p>
 * A TimeRange of TimeRanges is possible.  For example, start () and
 * end () might bothreturn TimeRanges.  In this
 * case, there may be "holes" in a TimeRange.  For example:
 * </p>
 *
 * <pre>
 *     TimeRange
 *     start---------------------end
 *       |                        |
 *       v                        v
 *       TimeRange                TimeRange
 *       start--------end         start--------end
 *         |           |            |           |
 *         v           v            v           v
 *         Time        Time         Time        Time
 *         1247326848  1247326958   1247327050  1247327061
 *            s e c o n d s   s i n c e   E p o c h   0
 * </pre>
 *
 * <p>
 * In this example, the toplevel TimeRange has a "hole" from
 * (1247326958 + 1 nanosecond) to (1247327050 - 1 nanosecond),
 * inclusive.
 * </p>
 *
 * <p>
 * Therefore, the Time 1247327000 seconds since Epoch 0 is
 * <i>not</i> contained in the toplevel TimeRange.
 * </p>
 *
 * <p>
 * A TimeRange which contains a TimeRange start and a non-TimeRange
 * end contains both the TimeRange and the single instant in time
 * represented by the end ().  (And so on for start () = Time and
 * end () = TimeRange.)
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
public interface TimeRange
    extends Serializable, Time
{
    /**
     * <p>
     * Returns the start time of the time range.
     * </p>
     *
     * @return The start time of the range.  Never null.
     */
    public abstract Time start ();


    /**
     * <p>
     * Returns the end time of the time range.
     * </p>
     *
     * <p>
     * Note that the end time could be before the start time.
     * This is valid, a "backwards range".
     * </p>
     *
     * @return The end time of the range.  Never null.
     */
    public abstract Time end ();


    /**
     * <p>
     * Returns true if the specified Time is contained in the
     * range start () to end ().
     * </p>
     *
     * <p>
     * If the specified Time is a TimeRange, then the result is
     * true only if the entire range (from start to end) is
     * contained in this range.
     * </p>
     *
     * <p>
     * Note that a range of ranges might contain holes.
     * </p>
     *
     * @return True if the specified time is contained in this
     *         range, false if not.
     */
    public abstract boolean contains (
                                      Time time
                                      );


    /**
     * <p>
     * Returns a new TimeRange starting from the specified starting
     * time to end ().
     * </p>
     *
     * <p>
     * Note that if this is a range of ranges, then a number of
     * new Time and TimeRange objects might be created.
     * </p>
     *
     * @return The newly created TimeRange starting at the specified
     *         start time and ending at end ().
     */
    public abstract TimeRange from (
                                    Time start_time
                                    );


    /**
     * <p>
     * Returns a new TimeRange starting from start () and ending
     * at the specified end time.
     * </p>
     *
     * <p>
     * Note that if this is a range of ranges, then a number of
     * new Time and TimeRange objects might be created.
     * </p>
     *
     * @return The newly created TimeRange starting start () and
     *         ending at the specified end time.
     */
    public abstract TimeRange to (
                                  Time end_time
                                  );


    /**
     * <p>
     * Returns a new TimeRange representing the intersection
     * of this TimeRange and the specified TimeRange.
     * </p>
     *
     * <p>
     * All TimeRanges which are contained in both this and the
     * other_range are placed into a new hierarchy of TimeRange(s)
     * and returned.
     * </p>
     *
     * <p>
     * Note that if this or the specified TimeRange is a range
     * of ranges, then a number of new Time and TimeRange objects
     * might be created.
     * </p>
     *
     * @return The newly created intersection TimeRange.
     */
    public abstract TimeRange intersection (
                                            TimeRange other_range
                                            );


    /**
     * <p>
     * Returns a new TimeRange representing the union
     * of this TimeRange and the specified TimeRange.
     * </p>
     *
     * <p>
     * All TimeRanges from both this and the other_range
     * are joined to form a new hierarchy of TimeRange(s).
     * </p>
     *
     * <p>
     * Note that if this or the specified TimeRange is a range
     * of ranges, then a number of new Time and TimeRange objects
     * might be created.
     * </p>
     *
     * @return The newly created union (join) TimeRange.
     */
    public abstract TimeRange union (
                                     TimeRange other_range
                                     );


    /**
     * <p>
     * Returns a new TimeRange representing the difference
     * of this TimeRange and the specified TimeRange.
     * </p>
     *
     * <p>
     * All TimeRanges in either this or other_range (but not both)
     * are placed into a hierarchy of TimeRange(s) and returned.
     * </p>
     *
     * <p>
     * Note that if this or the specified TimeRange is a range
     * of ranges, then a number of new Time and TimeRange objects
     * might be created.
     * </p>
     *
     * @return The newly created difference TimeRange.
     */
    public abstract TimeRange difference (
                                          TimeRange other_range
                                          );
}
