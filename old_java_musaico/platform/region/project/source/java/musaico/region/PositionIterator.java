package musaico.region;

import java.util.Iterator;
import java.util.NoSuchElementException;


import musaico.io.Comparison;
import musaico.io.Order;


/**
 * <p>
 * Steps through a Region from start to end and every Position
 * in between.
 * </p>
 *
 * <p>
 * This iterator is NOT thread-safe.  If you call its methods
 * from multiple threads, expect a concurrent modification exception.
 * Instead, you should create one Iterator for each thread.
 * (Multiple iterators stepping over a single Region is fine, since
 * each Region is immutable.)
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
public class PositionIterator
    implements Iterator<Position>
{
    /** The region over which we step. */
    private final Region region;

    /** The next position within the region to return from next (). */
    private Position nextPosition;


    /**
     * <p>
     * Creates a new iterator over the specified Region.
     * </p>
     *
     * @param region The region over which to step.  Must not be null.
     */
    public PositionIterator (
                             Region region
                             )
    {
        this.region = region;
        this.nextPosition = this.region.start ();
    }


    /**
     * @see java.util.Iterator#hasNext()
     */
    @Override
    public boolean hasNext ()
    {
        if ( this.nextPosition.equals ( this.region.space ().outOfBounds () ) )
        {
            return false;
        }
        else
        {
            return true;
        }
    }


    /**
     * @see java.util.Iterator#next()
     */
    @Override
    public Position next ()
        throws NoSuchElementException
    {
        Space space = this.region.space ();
        Position current_position = this.nextPosition;

        if ( current_position.equals ( space.outOfBounds () ) )
        {
            throw new NoSuchElementException ( "PositionIterator "
                                               + this
                                               + " for region "
                                               + region
                                               + " has no more elements" );
        }

        this.nextPosition = this.region.expr ( current_position ).next ();

        return current_position;
    }


    /**
     * @see java.util.Iterator#remove()
     *
     * <p>
     * Always throws UnsupportedOperationException.  It does
     * not make any sense to remove a Position from an immutable
     * Region.
     * </p>
     */
    @Override
    public void remove ()
        throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException ( "Remove is not supported by " + this.getClass () + " " + this );
    }
}
