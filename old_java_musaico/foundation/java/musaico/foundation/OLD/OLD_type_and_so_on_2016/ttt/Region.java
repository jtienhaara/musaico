package musaico.foundation.topology;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;

import musaico.foundation.value.Value;


/**
 * <p>
 * A Region of points, including a start point, an end point,
 * and zero or more points in between.
 * </p>
 *
 * <p>
 * The Region class is meant to be a flexible representation of the
 * regions of different data structures, much like
 * a pointer, an integer offset, and a <code> size_t </code>
 * integer representation of size are collectively used to
 * describe the regions of many different types of data
 * structures in C.  For example, an integer Region
 * might represent the indices of an array; or a
 * 4-dimensional real valued Region might represent the vertices
 * of objects placed in a 4-D Region; or a time Region
 * might be used to represent the second and nanosecond points
 * of digital audio or video; and so on.
 * </p>
 *
 *
 * <p>
 * In Java every Region must implement hashCode () and equals () in
 * order to play nicely with HashMaps.
 * </p>
 *
 * <p>
 * in Java every Region must be Serializable in order to
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
 * <p>
 * For copyright and licensing information refer to:
 * </p>
 *
 * @see musaico.foundation.topology.MODULE#COPYRIGHT
 * @see musaico.foundation.topology.MODULE#LICENSE
 */
public interface Region<POINT extends Serializable, MEASURE extends Serializable>
    extends Value<POINT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Returns true if this Region contains the specified Point.
     * </p>
     *
     * <p>
     * If the specified point is null then false is always returned.
     * </p>
     *
     * @param point The point which might or might not be
     *              contained in this Region.  Must not be null.
     *
     * @return True if the specified point is contained in this Region;
     *         false if the specified point is not contained in
     *         this Region, or if the specified point is null.
     */
    public abstract boolean contains (
                                      POINT point
                                      )
        throws ParametersMustNotBeNull.Violation;


    /**
     * @return The end point of this Region.  For example, the
     *         end point of an integer array index Region
     *         including points { 0, 1 2 and 3 } might be 3.
     *         Always belongs to the same Space as this Region.
     *         Always greater than or equal to the <code> start () </code>
     *         point of this Region.  Never null.
     */
    public abstract POINT end ()
        throws ReturnNeverNull.Violation;


    // Every Region must implement
    // java.lang.Object#equals(java.lang.Object).


    /**
     * <p>
     * Creates a new RegionExpression starting from this Region.
     * </p>
     *
     * @return A new RegionExpression for this region.
     *         Can be an EmptyRegionExpression if this Region
     *         is a NoRegion.  Never null.
     */
    public abstract RegionExpression<POINT, MEASURE> expr ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Creates a new PointExpression starting from the specified
     * point within this Region.
     * </p>
     *
     * <p>
     * If the specified point is null, or if it does not belong
     * to the same Space as this Region, or if the specified point
     * is not contained in this Region, then a failed PointExpression
     * will be returned.
     * </p>
     *
     * <p>
     * If this Region is a NoRegion, then the result will always be
     * a failed PointExpression.
     * </p>
     *
     * @param point The point from this Region on which the
     *              requested expression will operate.
     *              Must belong to the same Space as this Region.
     *              Must not be null.
     *
     * @return A new PointExpression for the specified point.
     *         Can be a failed PointExpression if the specified
     *         point is invalid, or if this Region is a NoRegion.
     *         Never null.
     */
    public abstract PointExpression<POINT, MEASURE> expr (
                                                          POINT point
                                                          )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    // Every Region must implement java.lang.Object#hashCode().

    // Every Region must implement java.lang.Iterable#iterator().


    /**
     * <p>
     * Returns a default Searcher, which can be used to search for
     * point(s) matching the specified criteria within the
     * specified RegionExpression.
     * </p>
     *
     * <p>
     * Many Region implementations will return a LinearSearcher for
     * the general case, but a more specialized searcher for certain
     * criteria.
     * </p>
     *
     * <p>
     * For example, many Region implementations will return the
     * BinarySearcher strategy whenever a single OrderedFilter
     * criterion is passed in.
     * </p>
     *
     * <p>
     * However a Region which is specific to a data source, such
     * as a Region covering rows in a database table, might
     * convert the specified criteria into parts of an SQL query,
     * and return a Searcher which is capable of executing that
     * SQL query against some database or other.
     * </p>
     *
     * <p>
     * Or a Region which covers large, multi-dimensional
     * data might return a more loosey goosey search strategy,
     * such as a stochastic local search implementation.
     * </p>
     *
     * <p>
     * And so on.
     * </p>
     *
     * <p>
     * This is only the default Searcher algorithm for this Region.
     * Context-dependent search strategies and optimized search
     * strategies should be implemented in RegionExpression.search(),
     * depending on the Region and on the criteria passed in.
     * For example, a Filter might specify "max satisfied
     * criteria", which might induce a change from the regular
     * (say BinarySearcher) strategy to a more loosey goosey
     * strategy (such as a stochastic local search).
     * </p>
     *
     * @return The default search strategy for this Region for the
     *         specified criteria within the specified RegionExpression.
     *         Never null.
     */
    @SuppressWarnings("unchecked") // Heap pollution<generic varargs>
    public abstract Searcher searcher (
                                       RegionExpression<POINT, MEASURE> region_expression,
                                       Filter<POINT> ... criteria
                                       );


    /**
     * @return The size of this Region.  For example, an integer
     *         array index Region with indices { 0, 1, 2, 3 } might
     *         return an integer size 4.  Always belongs to the same
     *         Space as this Region.  Never null.
     */
    public abstract MEASURE size ()
        throws ReturnNeverNull.Violation;


    /**
     * @return The addressing Space whence this sptial element sprang, such
     *         as an integer index space, or a String field / column
     *         name space, or a date/time space, or a 4-dimensional
     *         space and so on.  Never null.
     */
    public abstract Space space ();


    /**
     * @return The start point of this Region.  For example, an
     *         integer array index Region might return integer point 0.
     *         Always belongs to the same Space as this Region.
     *         Never null.
     */
    public abstract POINT start ()
        throws ReturnNeverNull.Violation;
}
