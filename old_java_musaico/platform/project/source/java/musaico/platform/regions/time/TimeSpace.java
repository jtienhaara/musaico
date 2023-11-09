package musaico.region.time;

import java.io.Serializable;

import java.math.BigInteger;

import java.util.Calendar;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Order;

import musaico.region.Position;
import musaico.region.Region;
import musaico.region.RegionExpression;
import musaico.region.Size;
import musaico.region.Space;
import musaico.region.SparseRegionBuilder;
import musaico.region.StandardRegionExpression;

import musaico.time.AbsoluteTime;
import musaico.time.RelativeTime;
import musaico.time.Time;


/**
 * <p>
 * The space in which a TimeRegion, TimePosition, TimeSize,
 * and so on resides.
 * </p>
 *
 * <p>
 * Not to be confused with the Yin and Yang of programming
 * (Space and Time).
 * </p>
 *
 * <p>
 * For example, Regions, Positions and Sizes describing times
 * might be described with non-negative integers, whereas the Regions,
 * Positions and Sizes describing a 3-dimensional volume might
 * be described with 3-tuples doube[] positions in space.
 * </p>
 *
 *
 * <p>
 * In Java, every Space must be Serializable in order to play nicely
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
public class TimeSpace
    implements Space, Serializable
{
    /** Today at midnight minus 5 years.  Used by DAY_PLANNER. */
    private static final Calendar ONE_HUNDRED_YEARS_AGO_AT_MIDNIGHT =
        Calendar.getInstance ();
    static
    {
        ONE_HUNDRED_YEARS_AGO_AT_MIDNIGHT.set ( Calendar.HOUR_OF_DAY, 0 );
        ONE_HUNDRED_YEARS_AGO_AT_MIDNIGHT.set ( Calendar.MINUTE, 0 );
        ONE_HUNDRED_YEARS_AGO_AT_MIDNIGHT.set ( Calendar.SECOND, 0 );
        ONE_HUNDRED_YEARS_AGO_AT_MIDNIGHT.set ( Calendar.MILLISECOND, 0 );
        ONE_HUNDRED_YEARS_AGO_AT_MIDNIGHT.add ( Calendar.YEAR, -100 );
    }

    /** Today at midnight plus 10 years.  Used by DAY_PLANNER. */
    private static final Calendar ONE_HUNDRED_YEARS_FROM_NOW_AT_MIDNIGHT =
        Calendar.getInstance ();
    static
    {
        ONE_HUNDRED_YEARS_FROM_NOW_AT_MIDNIGHT.set ( Calendar.HOUR_OF_DAY, 0 );
        ONE_HUNDRED_YEARS_FROM_NOW_AT_MIDNIGHT.set ( Calendar.MINUTE, 0 );
        ONE_HUNDRED_YEARS_FROM_NOW_AT_MIDNIGHT.set ( Calendar.SECOND, 0 );
        ONE_HUNDRED_YEARS_FROM_NOW_AT_MIDNIGHT.set ( Calendar.MILLISECOND, 0 );
        ONE_HUNDRED_YEARS_FROM_NOW_AT_MIDNIGHT.add ( Calendar.YEAR, 100 );
    }


    /** Any old absolute time can be used in this space. */
    public static final TimeSpace ABSOLUTE =
        new TimeSpace ( new RelativeTime ( 0L, 1L ), // Nanosecond divisions
                        new AbsoluteTime ( 0L, 0L ), // UTC 0 (Jan 1 1970)
                        Long.MAX_VALUE, // ~9.2x10^9 seconds (~292 yrs)
                        new AbsoluteTimePositionFactory () );

    /** Any old relative time can be used in this space. */
    public static final TimeSpace RELATIVE =
        new TimeSpace ( new RelativeTime ( 0L, 1L ), // Nanosecond divisions
                        new RelativeTime ( 0L, 0L ), // 0 seconds
                        Long.MAX_VALUE, // ~9.2x10^9 seconds (~292 yrs)
                        new RelativeTimePositionFactory () );

    /** An example TimeSpace useful for 44.1KHz digital audio. */
    public static final TimeSpace AUDIO =
        new TimeSpace ( RelativeTime.fromFrequency ( 44100D ),
                        new RelativeTime ( 0L, 0L ),
                        400000000000000L, // ~9,070,000 seconds (~287yrs)
                        new RelativeTimePositionFactory () );

    /** An example TimeSpace useful for 120bpm music. */
    public static final TimeSpace MUSIC =
        new TimeSpace ( RelativeTime.fromFrequency ( 120D / 60D ),
                        new RelativeTime ( 0L, 0L ),
                        4000000000000000000L, // (~3,044,140,030,441 yrs)
                        new RelativeTimePositionFactory () );

    /** An example TimeSpace useful for day planners with
     *  15 minute intervals. */
    public static final TimeSpace DAY_PLANNER =
        new TimeSpace ( new RelativeTime ( 15L * 60L, 0L ), // 15 minutes
                        new AbsoluteTime ( ONE_HUNDRED_YEARS_AGO_AT_MIDNIGHT ),
                        new AbsoluteTime ( ONE_HUNDRED_YEARS_FROM_NOW_AT_MIDNIGHT ),
                        new AbsoluteTimePositionFactory () );

    /** An example TimeSpace useful for 29.997 fps digital video. */
    public static final TimeSpace VIDEO =
        new TimeSpace ( RelativeTime.fromFrequency ( 29.997D ),
                        new RelativeTime ( 0L, 0L ),
                        200000000000L, // ~6,667,333,400,000,000,000 seconds (~211,419,755,200 years)
                        new RelativeTimePositionFactory () );


    /** The factory used to create (absolute or relative)
     *  TimePositions for this space. */
    private final TimePositionFactory positionFactory;

    /** The minimum time possible in this space. */
    private final TimePosition min;

    /** The maximum time possible in this space. */
    private final TimePosition max;

    /** The empty region for this TimeSpace. */
    private final TimeRegion empty;

    /** The none size for this TimeSpace. */
    private final TimeSize none;

    /** The one size for this TimeSpace. */
    private final TimeSize one;

    /** The out of bounds position for this TimeSpace. */
    private final TimePosition outOfBounds;


    /**
     * <p>
     * Creates a new TimeSpace with the specified interval between
     * positions.
     * </p>
     *
     * <p>
     * For sampling applications such as audio and video the
     * interval will reflect the sampling frequency, such as
     * ( 1 / 44100 ) seconds for audio or ( 1 / 29.997 ) seconds
     * for video, and so on.
     * </p>
     *
     * <p>
     * For calendars and schedules the interval will reflect the
     * schedule block size, such as 15 minute intervals for
     * a day planner, or day intervals for a shift schedule,
     * and so on.
     * </p>
     *
     * <p>
     * The default Time regions and spaces do not deal with
     * daylight savings time or calendar time, so be careful!
     * </p>
     *
     * <p>
     * The smallest interval possible is dependent on the
     * RelativeTime class, which, at the time of writing, means
     * intervals must be at least 1 nanosecond.
     * </p>
     *
     * @param interval The smallest interval between TimePositions in
     *                 this space.  Must not be null.
     *
     * @param min_time The earliest time possible in this space.
     *                 Must not be null.
     *
     * @param max_time The latest time possible in this space.
     *                 Must not be null.  Must be on a boundary with
     *                 the specified interval from min.
     *
     * @param position_factory The factory used by this space to
     *                         create TimePositions (either absolute
     *                         times or relative times, depending
     *                         on the space).  Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid.
     */
    public TimeSpace (
                      RelativeTime interval,
                      Time min_time,
                      Time max_time,
                      TimePositionFactory position_factory
                      )
        throws I18nIllegalArgumentException
    {
        if ( interval == null
             || min_time == null
             || max_time == null
             || position_factory == null
             || ! max_time.secondsAndNanoseconds ().subtract ( min_time.secondsAndNanoseconds () ).mod ( interval.secondsAndNanoseconds () ).equals ( BigInteger.ZERO ) )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a TimeSpace with interval [%interval%] min time [%min_time%] max time [%max_time%] position factory [%position_factory%]",
                                                     "interval", interval,
                                                     "min_time", min_time,
                                                     "max_time", max_time,
                                                     "position_factory", position_factory );
        }

        this.positionFactory = position_factory;
        this.min = this.positionFactory.position ( this, min_time );
        this.max = this.positionFactory.position ( this, max_time );

        this.none = new TimeSize ( this );
        this.one = new TimeSize ( this, interval );
        this.outOfBounds = new TimePosition ( this );
        this.empty = new TimeRegion ( this );
    }


    /**
     * <p>
     * Creates a new TimeSpace with the specified interval between
     * positions, and a max time based on that interval.
     * </p>
     *
     * <p>
     * For sampling applications such as audio and video the
     * interval will reflect the sampling frequency, such as
     * ( 1 / 44100 ) seconds for audio or ( 1 / 29.997 ) seconds
     * for video, and so on.
     * </p>
     *
     * <p>
     * For calendars and schedules the interval will reflect the
     * schedule block size, such as 15 minute intervals for
     * a day planner, or day intervals for a shift schedule,
     * and so on.
     * </p>
     *
     * <p>
     * The default Time regions and spaces do not deal with
     * daylight savings time or calendar time, so be careful!
     * </p>
     *
     * <p>
     * The smallest interval possible is dependent on the
     * RelativeTime class, which, at the time of writing, means
     * intervals must be at least 1 nanosecond.
     * </p>
     *
     * @param interval The smallest interval between TimePositions in
     *                 this space.  Must not be null.
     *
     * @param min_time The earliest time possible in this space.
     *                 Must not be null.
     *
     * @param num_intervals The number of intervals between the minimum
     *                      and maximum times in this space.
     *                      Must not be null.
     *
     * @param position_factory The factory used by this space to
     *                         create TimePositions (either absolute
     *                         times or relative times, depending
     *                         on the space).  Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid.
     */
    public TimeSpace (
                      RelativeTime interval,
                      Time min_time,
                      long num_intervals,
                      TimePositionFactory position_factory
                      )
        throws I18nIllegalArgumentException
    {
        if ( interval == null
             || min_time == null
             || num_intervals < 0L
             || position_factory == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a TimeSpace with interval [%interval%] min time [%min_time%] # intervals [%num_intervals%] position factory [%position_factory%]",
                                                     "interval", interval,
                                                     "min_time", min_time,
                                                     "num_intervals", num_intervals,
                                                     "position_factory", position_factory );
        }

        this.positionFactory = position_factory;
        this.min = this.positionFactory.position ( this, min_time );

        this.none = new TimeSize ( this );
        this.one = new TimeSize ( this, interval );
        this.outOfBounds = new TimePosition ( this );
        this.empty = new TimeRegion ( this );

        this.max = this.expr ( this.min )
            .add ( this.expr ( this.one )
                   .multiply ( (double) num_intervals ).size () ).position ();
    }


    /**
     * @see musaico.region.Space#all()
     */
    @Override
    public TimeRegion all ()
    {
        return new TimeRegion ( this.min (), this.max () );
    }


    /**
     * @see musaico.region.Space#empty()
     */
    @Override
    public TimeRegion empty ()
    {
        return this.empty;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
                           Object obj
                           )
    {
        if ( obj == null )
        {
            return false;
        }
        else if ( ! ( obj instanceof TimeSpace ) )
        {
            return false;
        }
        else if ( obj == this )
        {
            return true;
        }

        TimeSpace that = (TimeSpace) obj;
        if ( ! this.min ().equals ( that.min () )
             || ! this.max ().equals ( that.max () ) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * @see musaico.region.Space#expr(musaico.region.Position)
     */
    @Override
    public TimePositionExpression expr (
                                         Position position
                                         )
    {
        final TimePosition time_position;
        if ( position instanceof TimePosition )
        {
            time_position = (TimePosition) position;
        }
        else
        {
            time_position = this.outOfBounds ();
        }

        return new TimePositionExpression ( time_position );
    }


    /**
     * @see musaico.region.Space#expr(musaico.region.Region)
     */
    @Override
    public RegionExpression expr (
                                  Region region
                                  )
    {
        final TimeRegion time_region;
        if ( region instanceof TimeRegion )
        {
            time_region = (TimeRegion) region;
        }
        else
        {
            time_region = this.empty ();
        }

        return new StandardRegionExpression ( time_region );
    }


    /**
     * @see musaico.region.Space#expr(musaico.region.Size)
     */
    @Override
    public TimeSizeExpression expr (
                                     Size size
                                     )
    {
        final TimeSize time_size;
        if ( size instanceof TimeSize )
        {
            time_size = (TimeSize) size;
        }
        else
        {
            time_size = this.none ();
        }

        return new TimeSizeExpression ( time_size );
    }


    /**
     * @see musaico.region.Space#from(musaico.region.Position)
     *
     * Final for speed.
     */
    @Override
    public final Position from (
                                Position that_position
                                )
    {
        if ( that_position == null )
        {
            return this.outOfBounds ();
        }

        Space that = that_position.space ();
        if ( that == this )
        {
            // Only if the references are identical can
            // we return that_position directly.
            return that_position;
        }
        else if ( this.equals ( that )
                  && ( that_position instanceof TimePosition ) )
        {
            // Exact same space, but different reference.
            // Recreate the position to point to our space.
            TimePosition that_time_position = (TimePosition)
                that_position;
            TimePosition this_position =
                this.positionFactory ().position ( this,
                                                   that_time_position.time () );
            return this_position;
        }

        Size that_offset_from_origin = that.expr ( that_position )
            .subtract ( that.origin () ).size ();

        Size this_offset_from_origin =
            this.from ( that_offset_from_origin );
        Position this_position = this.expr ( this.origin () )
            .add ( this_offset_from_origin ).position ();

        return this_position;
    }


    /**
     * @see musaico.region.Space#from(musaico.region.Region)
     *
     * Final for speed.
     */
    @Override
    public final Region from (
                              Region that_region
                              )
    {
        if ( that_region == null )
        {
            return this.empty ();
        }
        else if ( that_region.space () == this )
        {
            // Only if the references are identical can
            // we return that_region directly.
            return that_region;
        }

        // In case it is a SparseRegion, we don't try to match any
        // gaps.  We just make a region of equivalent size starting
        // at the equivalent start position.
        Position that_start = that_region.start ();
        Size that_size = that_region.size ();
        Size this_size = this.from ( that_size );
        Position this_start = this.from ( that_region.start () );
        Position this_end   =
            this.expr ( this_start ).add ( this_size ).previous ();
        Region this_region = this.region ( this_start, that_start );
        return this_region;
    }


    /**
     * @see musaico.region.Space#from(musaico.region.Size)
     *
     * Final for speed.
     */
    @Override
    public final Size from (
                            Size that_size
                            )
    {
        if ( that_size == null )
        {
            return this.none ();
        }

        Space that = that_size.space ();
        if ( that == this )
        {
            // Only if the references are identical can
            // we return that_size directly.
            return that_size;
        }
        else if ( this.equals ( that )
                  && ( that_size instanceof TimeSize ) )
        {
            // Exact same space, but different reference.
            // Recreate the size to point to our space.
            TimeSize that_time_size = (TimeSize)
                that_size;
            TimeSize this_size =
                this.size ( that_time_size.duration () );
            return this_size;
        }

        double num_elements = that.expr ( that_size )
            .ratio ( that.one () );

        Size this_size = this.expr ( this.one () )
            .multiply ( num_elements ).size ();

        return this_size;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        int hash_code = this.min ().hashCode () + this.max ().hashCode ();
        return hash_code;
    }


    /**
     * @see musaico.region.Space#max()
     */
    @Override
    public TimePosition max ()
    {
        return this.max;
    }


    /**
     * @see musaico.region.Space#min()
     */
    @Override
    public TimePosition min ()
    {
        return this.min;
    }


    /**
     * @see musaico.region.Space#none()
     */
    @Override
    public TimeSize none ()
    {
        return this.none;
    }


    /**
     * @see musaico.region.Space#one()
     */
    @Override
    public TimeSize one ()
    {
        return this.one;
    }


    /**
     * @see musaico.region.Space#order()
     */
    @Override
    public Order<Position> order ()
    {
        return TimePositionOrder.DEFAULT;
    }


    /**
     * @see musaico.region.Space#origin()
     */
    @Override
    public TimePosition origin ()
    {
        return this.min ();
    }


    /**
     * @see musaico.region.Space#outOfBounds()
     */
    @Override
    public TimePosition outOfBounds ()
    {
        return this.outOfBounds;
    }


    /**
     * <p>
     * Returns the TimePositionFactory for this space, which creates
     * either absolute or relative time positions.
     * </p>
     *
     * @return The time position factory for this space.  Never null.
     */
    public TimePositionFactory positionFactory ()
    {
        return this.positionFactory;
    }


    /**
     * @see musaico.region.Space#region(musaico.region.Position,musaico.region.Position)
     */
    @Override
    public TimeRegion region (
                               Position start,
                               Position end
                               )
    {
        if ( ! ( start instanceof TimePosition )
             || ! ( end instanceof TimePosition ) )
        {
            return this.empty ();
        }

        TimePosition time_start = (TimePosition) start;
        TimePosition time_end = (TimePosition) end;

        return new TimeRegion ( time_start, time_end );
    }


    /**
     * <p>
     * Creates a new TimeSize from the specified time duration.
     * </p>
     *
     * @param duration The duration of time to create a TimeSize
     *                 to represent.  Must not be null.
     *
     * @return The newly created TimeSize.  Never null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified are invalid
     *                                      (see above).
     */
    public TimeSize size (
                          RelativeTime duration
                          )
        throws I18nIllegalArgumentException
    {
        // Let the TimeSize class throw the exception if the
        // duration parameter is invalid.
        return new TimeSize ( this, duration );
    }


    /**
     * @see musaico.region.Space#sparseRegionBuilder()
     */
    @Override
    public SparseRegionBuilder sparseRegionBuilder ()
    {
        return new TimeSparseRegionBuilder ( this );
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "Space{" + this.min () + ".." + this.max () + ", interval=" + this.one () + "}";
    }


    public static void main ( String [] args )
    {
        System.out.println ( "!!! STARTING" );
        System.out.println ( "!!! AUDIO:" );
        System.out.println ( "" + TimeSpace.AUDIO );
        System.out.println ( "!!! DAY_PLANNER:" );
        System.out.println ( "" + TimeSpace.DAY_PLANNER );
        System.out.println ( "!!! VIDEO:" );
        System.out.println ( "" + TimeSpace.VIDEO );
    }
}
