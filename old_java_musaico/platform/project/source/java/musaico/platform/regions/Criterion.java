package musaico.region;

import java.io.Serializable;


import musaico.io.Comparison;


/**
 * <p>
 * What to search for inside a Region.
 * </p>
 *
 * <p>
 * A Criterion is comparable to Positions in any Region.
 * </p>
 *
 * <p>
 * For example, a buffer of sorted names might be
 * searched for a specific name.  In such a case, the Criterion
 * would compare the name at each Position in the buffer to the
 * name being searched for.  It would not know in advance the
 * Position being sought, but it would know how to
 * pull the name at a specific Position out of the buffer and
 * compare it to the Criterion's target name.
 * </p>
 *
 *
 * <p>
 * In Java, every Criterion must be Serializable in order
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
public interface Criterion
    extends Position, Serializable
{
    /**
     * <p>
     * Checks the specified Position against this Criterion, and
     * returns <code> Comparison.LEFT_EQUALS_RIGHT </code> if the
     * criterion has been met, <code> Comparison.LEFT_LESS_THAN_RIGHT </code>
     * if the criterion would be later in the region than the specified
     * Position (<code> position &lt; criterion </code>),
     * or <code> Comparison.LEFT_GREATER_THAN_RIGHT </code>
     * if the criterion would be earlier in the region than the
     * specified Position (<code> position &gt; criterion </code>).
     * </p>
     *
     * @param position The position to check against this Criterion.
     *                 Must not be null.
     *
     * @return Comparison.LEFT_EQUALS_RIGHT if the position matches this
     *         Criterion, Comparison.LEFT_LESS_THAN_RIGHT if the position
     *         is too early in the region, or
     *         Comparison.LEFT_GREATER_THAN_RIGHT if the position
     *         is too late in the region.  Never null.
     */
    public abstract Comparison compare (
                                        Position position
                                        );
}
