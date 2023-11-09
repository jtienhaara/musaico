package musaico.region;

import java.io.Serializable;


/**
 * <p>
 * A search operation on some Region.
 * </p>
 *
 * <p>
 * For example, to search a Region for some Criterion "x":
 * </p>
 *
 * <pre>
 *     Region region = ...;
 *     Criterion x = ...;
 *     Search search = new XYZSearch ( region, x );
 *     Position first_x = search.find ();
 * </pre>
 *
 *
 * <p>
 * In Java, every Search must be Serializable.
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
public interface Search
    extends Serializable
{
    /**
     * <p>
     * Returns the search criterion used by this Search to determine whether
     * the target Position(s) have been found.
     * </p>
     *
     * @return This search's criterion.  Never null.
     */
    public abstract Criterion criterion ();


    /**
     * <p>
     * Searches the Region and returns the first
     * Position which matches the search criterion.
     * </p>
     *
     * @return The first found Position, or
     *         <code> region.space ().outOfBounds () </code> if
     *         no Position in the Region matches the search criterion.
     *         Never null.
     */
    public abstract Position find ();


    /**
     * <p>
     * Searches the Region and returns all
     * Positions which match the criterion.
     * </p>
     *
     * @return The region containing all found Positions, or
     *         <code> region.space ().empty () </code> if
     *         no Position in the Region matches the search criterion.
     *         Never null.
     */
    public abstract Region findAll ();


    /**
     * <p>
     * Searches the SparseRegion and returns the index
     * of the sub-Region containing the Position matching the
     * criterion, or a negative number if no sub-Regions match
     * but the criterion sought after would fit in a region
     * inserted at sub-region index # (-1 - result).
     * </p>
     *
     * <p>
     * If the Region covered by this Search is not a SparseRegion
     * then Long.MIN_VALUE is always returned.
     * </p>
     *
     * @return The matching sub-Region index.  Possibly negative,
     *         if no sub-Region matches but the matching Region
     *         would be found at ( -1 - result ).  For example, if
     *         the result is -1, then a sub-Region inserted at the
     *         beginning of the sparse region could contain the
     *         sought criterion.  Or a result of -2 would indicate
     *         that a sub-Region inserted in the hole between the
     *         first two sub-Regions could contain the specified
     *         criterion.  Or a result of ( -numRegions - 1 )
     *         would indicate that a sub-Region appended to the
     *         end of the sparse region could contain the specified
     *         criterion.  Never less than ( -numRegions - 1 ) unless
     *         the Region covered by this Search is not a SparseRegion,
     *         in which case Long.MIN_VALUE is returned.
     */
    public abstract long findSubRegionIndex ();


    /**
     * <p>
     * Returns the Region to be searched.
     * </p>
     *
     * @return The region to be searched.  Never null.
     */
    public Region region ();
}
