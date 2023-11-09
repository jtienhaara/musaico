package musaico.foundation.region;

import java.io.Serializable;


import musaico.foundation.filter.Filter;


/**
 * <p>
 * A search operation, which will search through the Region
 * underlying some specific RegionExpression for matching Positions.
 * </p>
 *
 * <p>
 * For example, to search a Region for some Filter "x":
 * </p>
 *
 * <pre>
 *     Region region = ...;
 *     Filter<Position> x = ...;
 *     Searcher searcher = region.expr ().search ( x );
 *     Position first_x = searcher.find ().orNone ();
 *     Region all_xes = searcher.findAll ().orNone ();
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
public interface Searcher
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Returns the search criteri(on/a) used by this Searcher
     * to determine whether the target Position(s) have been found.
     * </p>
     *
     * @return This search's criteria.  Always contains one or more elements.
     *         Never null.  Never contains any null elements.
     */
    public abstract Filter<Position> [] criteria ();


    /**
     * <p>
     * Searches the Region and returns an expression for the first
     * Position which matches the search criteria.
     * </p>
     *
     * @return An expression for the first found Position on success,
     *         or a FailedPositionExpression if no Position in the
     *         Region matches the search criteria, or some RegionViolation
     *         prevented a successful search result (such as searching
     *         with criteria which operate within a different Space
     *         than the Region being searched).  Never null.
     */
    public abstract PositionExpression find ();


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
     *         Positions on success, or a FailedPositionExpression
     *         if no Position in the Region matches the search criteria,
     *         or some RegionViolation prevented a successful
     *         search result (such as searching with criteria which
     *         operate within a different Space than the Region
     *         being searched).  Never null.
     */
    public abstract RegionExpression findAll ();


    /**
     * <p>
     * Returns the RegionExpression to be searched.
     * </p>
     *
     * @return The region expression to be searched.  Never null.
     */
    public abstract RegionExpression regionSearchArea ();
}
