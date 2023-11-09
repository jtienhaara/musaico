package musaico.foundation.topology;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;

import musaico.foundation.value.Value;
import musaico.foundation.value.ZeroOrOne;


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
     * @return One or more pairs of endpoints defining the boundaries
     *         of this Region with holes in between pairs, or No endpoints
     *         if this Region is empty.  For example, the
     *         endpoints of an integer array index Region
     *         including points { 0, 1 2 and 3 } might { 0, 3 }.
     *         Or the index Region including points { 0, 1, 2, 7, 8, 9 }
     *         might be { 0, 2, 7, 9 } defining the two sub-regions
     *         { 0, 2 } and { 7, 9 } and the hole in between.
     *         Or the empty index Region { } would have endpoints { }.
     *         And so on.  Even if this Region is infinite, its bounds are
     *         finite and Countable, so that the first and last endpoint
     *         and the number of endpoints can be easily retrieved.
     *         Never null.
     */
    public abstract Countable<POINT> bounds ()
        throws ReturnNeverNull.Violation;


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
    public abstract RegionExpression<POINT, MEASURE> expression ()
        throws ReturnNeverNull.Violation;


    // Every Region must implement java.lang.Object#hashCode().

    // Every Region must implement java.lang.Iterable#iterator().


    /**
     * <p>
     * Creates a new Expression starting from the specified
     * point, operating only on the points in this Region.
     * </p>
     *
     * @see musaico.foundation.topology.Topology#point(java.lang.Object)
     *
     * @param point The initial input to the Expression to be built.
     *              Must not be null.
     *
     * @return A newly created PointExpression, with the
     *         specified point as the initial input.
     *         Never null.
     */
    public abstract PointExpression<POINT, MEASURE> point (
                                                           POINT point
                                                           )
        throws ParametersMustNotBeNull.Violation,
        ReturnNeverNull.Violation;


    /**
     * <p>
     * Creates a new expression starting from the specified
     * point, operating only on the points in this Region.
     * </p>
     *
     * @see musaico.foundation.topology.Topology#point(musaico.foundation.value.Value)
     *
     * @param point_value The initial input Value to the Expression
     *                    to be built.  Must not be null.
     *
     * @return A newly created PointExpression, with the
     *         specified point(s) Value as the initial input.
     *         Never null.
     */
    public abstract PointExpression<POINT, MEASURE> point (
                                                           Value<POINT> point_value
                                                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation


    /**
     * <p>
     * Creates a new expression starting from the specified
     * point, operating only on the points in this Region.
     * </p>
     *
     * @see musaico.foundation.topology.Topology#point(musaico.foundation.typing.Term)
     *
     * @param point_term The initial input Term of the Expression to be built.
     *                   Must not be null.
     *
     * @return A newly created PointExpression, with the
     *         specified point(s) Term as the initial input.
     *         Never null.
     */
    public abstract PointExpression<POINT, MEASURE> point (
                                                           Term<POINT> point_term
                                                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation


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
        Part of the Region signature:
        "find";
        Operation2<Region<POINT, MEASURE>, Filter<POINT, MEASURE>, POINT>
        {
            public Value<POINT> evaluate ( Value<Region<POINT, MEASURE>>,
                                           Value<Filter<POINT, MEASURE>> );
        }
        "findAll";
        Operation2<Region<POINT, MEASURE>, Filter<POINT, MEASURE>,
            Region<POINT, MEASURE>>
        {
            public Value<Region<POINT, MEASURE>> evaluate ( Value<Region<POINT, MEASURE>>,
                                                            Value<Filter<POINT, MEASURE>> );
        }
    @SuppressWarnings("unchecked") // Heap pollution<generic varargs>
    public abstract Searcher<POINT, MEASURE> searcher (
                                                       RegionExpression<POINT, MEASURE> region_expression,
                                                       Filter<POINT> ... criteria
                                                       );


    /**
     * @return The size of this Region.  For example, an integer
     *         array index Region with indices { 0, 1, 2, 3 } might
     *         return an integer size 4.  Never null.
     */
    public abstract MEASURE size ()
        throws ReturnNeverNull.Violation;


    /**
     * @return The Topology describing the points and measures of
     *         this Region.  For example, an integer index Topology,
     *         or a String field / column name space, or a date/time
     *         Topology, or a 4-dimensional Topology,
     *         and so on.  Never null.
     */
    public abstract Topology<POINT, MEASURE> topology ();
}
