package musaico.region;

import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Comparison;


/**
 * <p>
 * Searches for a specific position in Region, ordered by Position.
 * </p>
 *
 *
 * <p>
 * In Java every Criterion must be Serializable in order
 * to play nicely over RMI.
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
public class SpecificPosition
    implements Criterion, Serializable
{
    /** The specific position to search for. */
    private final Position specificPosition;


    /**
     * <p>
     * Creates a new SpecificPosition to search for the specified
     * Position in a Region.
     * </p>
     *
     * @param specific_position The position to search for.
     *                          Must not be null.
     *
     * @throws I18nIllegalArgumentException If the parameter(s)
     *                                      specified are invalid
     *                                      (see above for details).
     */
    public SpecificPosition (
                             Position specific_position
                             )
        throws I18nIllegalArgumentException
    {
        if ( specific_position == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a SpecificPosition search criterion for [%position%]",
                                                     "position", specific_position );
        }

        this.specificPosition = specific_position;
    }


    /**
     * <p>
     * Returns the position being sought by this SpecificPosition
     * search criterion.
     * </p>
     *
     * @return The position being sought by this SpecificPosition
     *         search criterion.  Never null.
     */
    public Position position ()
    {
        return this.specificPosition;
    }


    /**
     * @see musaico.io.Criterion#compare(musaico.region.Position)
     */
    @Override
    public Comparison compare (
                               Position position
                               )
    {
        Position specific_position = this.position ();
        return specific_position.space ().order ()
            .compareValues ( position, specific_position );
    }


    /**
     * @see musaico.region.Position#space()
     */
    @Override
    public Space space ()
    {
        return this.position ().space ();
    }
}
