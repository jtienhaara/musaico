package musaico.foundation.region;

import java.io.Serializable;


import musaico.foundation.filter.Filter;


/**
 * <p>
 * A search operation, which will search through the Region
 * underlying some specific RegionExpression for matching points.
 * </p>
 *
 * <p>
 * For example, to search a Region of integers for the first point
 * greater than 10 but less than 20:
 * </p>
 *
 * <pre>
 *     Region region = ...;
 *     Filter<Integer, Integer> greater_than_10 = ...;
 *     Filter<Integer, Integer> less_than_20 = ...;
 *     Searcher searcher =
 *         region.expression ().search ( greater_than_10,
 *                                       less_than_20 );
 *     int first_point =
 *         searcher.find ().point ().orNone ();
 *     Region<Integer, Integer> all_points =
 *         searcher.findAll ().region ().orNone ();
 * </pre>
 *
 *
 * <p>
 * In Java, every Searcher must be Serializable.
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
 * @see musaico.foundation.region.MODULE#COPYRIGHT
 * @see musaico.foundation.region.MODULE#LICENSE
 */
public interface Searcher<POINT extends Serializable, MEASURE extends Serializable>
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Returns the search criteri(on/a) used by this Searcher
     * to determine whether the target point(s) have been found.
     * </p>
     *
     * @return This search's criteria.  Always contains one or more elements.
     *         Never null.  Never contains any null elements.
     */
    public abstract Countable<Filter<POINT>> criteria ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Searches the Region and returns an expression for the first
     * point which matches the search criteria.
     * </p>
     *
     * @return An expression for the One first found point on success,
     *         or for No point if none was found.  Never null.
     */
    public abstract PositionExpression<POINT, MEASURE> find ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Searches the Region and returns a RegionExpression containing all
     * Positions which match the criteria.
     * </p>
     *
     * <p>
     * The resulting RegionExpression might be lazily initlaized, as
     * long as it is only Successful if at least one matching Position
     * has definitely been found (subsequent matching Positions can
     * be found later); and as long as it is only Failed if the Region
     * definitely does not have any matching Positions.
     * </p>
     *
     * @return An expression for the Region containing all matching
     *         Positions (possibly empty
     *         if no Position in the Region matches the search criteria,
     *         or some RegionViolation prevented a successful
     *         search result).  Never null.
     */
    public abstract RegionExpression<POINT, MEASURE> findAll ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns the RegionExpression to be searched.
     * </p>
     *
     * @return The region expression to be searched.  Never null.
     */
    public abstract RegionExpression<POINT, MEASURE> regionToSearech ()
        throws ReturnNeverNull.Violation;
}
