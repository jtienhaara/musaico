package musaico.region.time;

import java.io.Serializable;

import java.util.Iterator;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Comparison;

import musaico.region.Criterion;
import musaico.region.Position;
import musaico.region.PositionExpression;
import musaico.region.PositionIterator;
import musaico.region.Region;
import musaico.region.RegionalPositionExpression;
import musaico.region.Search;
import musaico.region.Size;

import musaico.region.search.BinarySearch;

import musaico.time.RelativeTime;


/**
 * <p>
 * A region with a start time and an end time.
 * </p>
 *
 * <p>
 * All time positions within this TimeRegion are discrete, but
 * the region is still considered to be contiguous as long as
 * the positions fall on periodic boundaries.  The interval is
 * determined by the TimeSpace, and might be fractions of a second,
 * 1 day, and so on.
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
public class TimeRegion
    implements Region, Serializable
{
    /** The space to which this region belongs. */
    private final TimeSpace space;

    /** The start index of this time region. */
    private final TimePosition start;

    /** The last index of this time region. */
    private final TimePosition end;

    /** The size of this time region. */
    private final TimeSize size;


    /**
     * <p>
     * Creates a new TimeRegion with the specified start and end
     * Positions.
     * </p>
     *
     * @param start The starting position of this time region,
     *              such as an AbsoluteTime or a RelativeTime.
     *              Must not be null.
     *
     * @param end The ending position of this time region,
     *            such as an AbsoluteTime or a RelativeTime.
     *            Must not be null.
     *            Must be greater than the start position.
     *            Must have the same space as the start position.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      specified are invalid
     *                                      (see above).
     */
    public TimeRegion (
                        TimePosition start,
                        TimePosition end
                        )
        throws I18nIllegalArgumentException
    {
        if ( start == null
             || end == null
             || ! start.space ().equals ( end.space () )
             || ! start.space ().order ().compareValues ( start, end )
             .isIn ( Comparison.LEFT_LESS_THAN_RIGHT,
                     Comparison.LEFT_EQUALS_RIGHT )
             || ! ( ( end.time ().subtract ( start.time () ) ) instanceof RelativeTime ) )
        {
            throw new I18nIllegalArgumentException ( "Invalid time region [%start%] .. [%end%]",
                                                     "start", start,
                                                     "end", end );
        }

        this.space = start.space ();
        this.start = start;
        this.end = end;
        this.size =
            new TimeSize ( this.space,
                           (RelativeTime) this.end.time ().subtract ( this.start.time () ) );
    }


    /**
     * <p>
     * Creates a new empty TimeRegion.
     * </p>
     *
     * <p>
     * Package private so each TimeSpace can create an empty region.
     * </p>
     */
    TimeRegion (
                TimeSpace space
                )
    {
        this.space = space;
        this.start = this.space.origin ();
        this.end = this.start;
        this.size = this.space.none ();
    }


    /**
     * @see musaico.region.TimeRegion#contains(musaico.region.Position)
     */
    @Override
    public boolean contains (
                             Position position
                             )
    {
        if ( ! ( position instanceof TimePosition ) )
        {
            return false;
        }
        else if ( this.size ().equals ( this.space.none () ) )
        {
            // Empty region.
            return false;
        }

        TimePosition time_position = (TimePosition) position;

        TimePosition start = this.start ();
        Comparison start_comparison =
            this.space ().order ().compareValues ( start, time_position );
        if ( ! start_comparison.isIn ( Comparison.LEFT_LESS_THAN_RIGHT,
                                       Comparison.LEFT_EQUALS_RIGHT ) )
        {
            return false;
        }

        TimePosition end = this.end ();
        Comparison end_comparison =
            this.space ().order ().compareValues ( end, time_position );
        if ( ! end_comparison.isIn ( Comparison.LEFT_LESS_THAN_RIGHT,
                                     Comparison.LEFT_EQUALS_RIGHT ) )
        {
            return false;
        }

        return true;
    }


    /**
     * @see musaico.region.TimeRegion#end()
     */
    @Override
    public TimePosition end ()
    {
        return this.end;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals (
                           Object obj
                           )
    {
        if ( obj == null )
        {
            return false;
        }
        else if ( ! ( obj instanceof TimeRegion ) )
        {
            return false;
        }
        else if ( obj == this )
        {
            return true;
        }
        else if ( obj instanceof TimeSparseRegion
                  && ( (TimeSparseRegion) obj ).numRegions () > 1L )
        {
            return false;
        }

        TimeRegion that = (TimeRegion) obj;
        if ( ! this.start ().equals ( that.start () )
             || ! this.end ().equals ( that.end () )
             || ! this.size ().equals ( that.size () ) )
        {
            return false;
        }
        else
        {
            return true;
        }
    }


    /**
     * @see musaico.region.Region#expr(musaico.region.Position)
     */
    @Override
    public PositionExpression expr (
                                    Position position
                                    )
    {
        if ( ! ( position instanceof TimePosition )
             || this.size ().equals ( this.space ().none () ) )
        {
            return new RegionalPositionExpression ( this.space ().outOfBounds (),
                                                    this );
        }

        return new RegionalPositionExpression ( position, this );
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode ()
    {
        int hash_code = this.start ().time ().hashCode () * 4099;
        hash_code += this.end ().time ().hashCode ();
        if ( this.size ().equals ( this.space ().none () ) )
        {
            hash_code --;
        }
        return hash_code;
    }


    /**
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<Position> iterator ()
    {
        return new PositionIterator ( this );
    }


    /**
     * @see musaico.region.Space#search(musaico.region.Criterion)
     */
    @Override
    public Search search (
                          Criterion criterion
                          )
    {
        return new BinarySearch ( this, criterion );
    }


    /**
     * @see musaico.region.TimeRegion#size()
     */
    @Override
    public TimeSize size ()
    {
        return this.size;
    }


    /**
     * @see musaico.region.TimeRegion#space()
     */
    @Override
    public TimeSpace space ()
    {
        return this.space;
    }


    /**
     * @see musaico.region.TimeRegion#start()
     */
    @Override
    public TimePosition start ()
    {
        return this.start;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        if ( this.size ().equals ( this.space ().none () ) )
        {
            return "{}";
        }
        else
        {
            return "{" + this.start ().time () + ".."
                + this.end ().time () + "}";
        }
    }
}
