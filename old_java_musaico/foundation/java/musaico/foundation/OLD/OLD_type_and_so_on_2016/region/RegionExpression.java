package musaico.foundation.region;

import java.io.Serializable;


import musaico.foundation.condition.Conditional;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;

import musaico.foundation.order.Order;


/**
 * <p>
 * A RegionExpression allows the caller to manipulate the shape,
 * size, order and contents of a Region of data, without the need
 * to make a copy of that data.
 * </p>
 *
 * <p>
 * For example, filtering, sorting and basic set manipulations
 * can all be performed to create an alternate Region.
 * </p>
 *
 *
 * <p>
 * In Java every RegionExpression must be Serializable in order
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
 * <p>
 * For copyright and licensing information refer to:
 * </p>
 *
 * @see musaico.foundation.region.MODULE#COPYRIGHT
 * @see musaico.foundation.region.MODULE#LICENSE
 */
public interface RegionExpression
    extends Conditional<Region, RegionViolation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Concatenates the specified Region onto the end of this
     * RegionExpression.
     * </p>
     *
     * <p>
     * The specified Region may contain Positions that are already
     * contained in this RegionExpression.  For example, if this
     * is a RegionExpression for integer Region { 0, 1, 2, 3 },
     * and integer Region { 2, 3, 4 } is concatenated, then the
     * resulting RegionExpression might be for integer Region
     * { 0, 1, 2, 3, 2, 3, 4 } with Positions 2 and 3 duplicated.
     * </p>
     *
     * <p>
     * If the specified Region is null, or does not belong to the
     * same Space as this RegionExpression, then a failed
     * RegionExpression will be returned.
     * </p>
     *
     * @param that The Region to concatenate to the end of this
     *             RegionExpression.  Must belong to the same
     *             Space as this RegionExpression.  Must not be null.
     *
     * @return The resulting RegionExpression, this with that
     *         concatenated.  Can be a FailedRegionExpression, for
     *         example if that does not belong to the same space
     *         as this RegionExpression.  Never null.
     */
    public abstract RegionExpression concatenate (
                                                  Region that
                                                  )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns the set difference between this and that, as
     * a new RegionExpression for a Region containing only the
     * Positions which are in on or the other but not both Regions.
     * </p>
     *
     * <p>
     * For example, if this is a RegionExpression for integer
     * Region { 0, 1, 2 }, and that is an integer Region { 1, 2, 3 }
     * then the difference might be a RegionExpression for
     * the integer Region { 0, 3 }.
     * </p>
     *
     * <p>
     * Similar to the <code> exclude ( Region ) </code> method,
     * except that Positions from this and that Region are
     * included in the result, not just from this one.
     * </p>
     *
     * <p>
     * If the specified Region is null or belongs to a different
     * Space than this RegionExpression, or if this is a failed
     * RegionExpression, then the result will be a failed
     * RegionExpression.
     * </p>
     *
     * <p>
     * If this RegionExpression contains exactly the same Positions
     * as that Region, in any order, and ignoring duplicate
     * Positions, then a successful RegionExpression for an empty
     * Region will be returned.
     * </p>
     *
     * @param that The Region whose set difference from this
     *             expression's Region will be returned.
     *             Must belong to the same Space as this
     *             RegionExpression.  Must not be null.
     *
     * @return A new RegionExpression for a Region containing
     *         only the set difference between this and that.
     *         That is, only Positions which are in either
     *         this expression's Region or that Region, but
     *         not both, will be contained in the resulting
     *         expression's Region.  Can be a successful
     *         empty Region, if this RegionExpression contains
     *         the same set of Positions as that Region.
     *         Can be a failed RegionExpression, if the
     *         specified parameter is invalid, or if this
     *         RegionExpression is already failed.  On success,
     *         always belongs to the same Space as this
     *         RegionExpression.  Never null.
     */
    public abstract RegionExpression difference (
                                                 Region that
                                                 )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns a new RegionExpression for a Region containing only
     * the Positions from this RegionExpression which are not
     * also in the specified Region.
     * </p>
     *
     * <p>
     * For example, if this is a RegionExpression for integer
     * Region { 0, 1, 2 }, then excluding integer Region { 1, 2, 3 }
     * might result in a RegionExpression for the integer Region { 0 }.
     * </p>
     *
     * <p>
     * Similar to the <code> difference ( Region ) </code> method,
     * but only this Region's positions are included in the result,
     * no Positions from that Region are included.
     * </p>
     *
     * <p>
     * If the specified Region is null or belongs to a different
     * Space than this RegionExpression, or if this is a failed
     * RegionExpression, then the result will be a failed
     * RegionExpression.
     * </p>
     *
     * <p>
     * If that Region contains all of the Positions from this
     * RegionExpression, in any order, and ignoring duplicate
     * Positions, then a successful RegionExpression for an
     * empty Region will be returned.
     * </p>
     *
     * @param that The Region of Positions to exclude from this
     *             expression's Region.  Must belong to the
     *             same Space as this RegionExpression.
     *             Must not be null.
     *
     * @return A new RegionExpression for a Region containing
     *         only the Positions from this RegionExpression which
     *         are not also in the specified Region.  Can
     *         be a successful RegionExpression for an empty
     *         Region, if that Region contains all of the
     *         Positions from this RegionExpression.
     *         Can be a failed RegionExpression, if the specified parameter
     *         is invalid, or if this RegionExpression is
     *         already failed.  On success, always belongs to
     *         the same Space as this RegionExpression.
     *         Never null.
     */
    public abstract RegionExpression exclude (
                                              Region that
                                              )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns a new RegionExpression for a Region containing
     * only the common Positions from this expression and
     * the specified Region.
     * </p>
     *
     * <p>
     * For example if this is a RegionExpression for
     * integer Region { 0, 1, 2 } and that is an integer
     * Region { 1, 2, 3 } then the intersection might be
     * a RegionExpression for integer Region { 1, 2 }.
     * <p>
     *
     * <p>
     * If the specified Region is null or belongs to a
     * difference Space than this RegionExpression, then
     * a failed RegionExpression will be returned.
     * </p>
     *
     * <p>
     * If none of the Positions from this RegionExpression
     * overlap those in that Region, then a successful
     * RegionExpression for an empty Region is returned.
     * </p>
     *
     * @param that The Region whose set intersection with
     *             this RegionExpression will be returned.
     *             Must belong to the same Space as this
     *             RegionExpression.  Must not be null.
     *
     * @return A new RegionExpression for a Region containing
     *         only the Positions which are in both this
     *         RegionExpression and the specified Region.
     *         Can be a succcessful empty RegionExpression,
     *         if there are no common Positions.  Can be a
     *         failed RegionExpression if the parameter is
     *         invalid.  On success, always belongs to the
     *         same Space as this RegionExpression.  Never null.
     */
    public abstract RegionExpression intersection (
                                                   Region that
                                                   )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Inverts this RegionExpression relative to the entire Space
     * it belongs to.
     * </p>
     *
     * <p>
     * For example, in the space of all integers in the range {0..10}
     * inclusive, a Region from {2-4} would be inverted as a
     * SparseRegion {0-1, 5-10}.
     * </p>
     *
     * <p>
     * If this RegionExpression represents a NoRegion, then the entire
     * <code> Space.all () </code> region is returned.
     * </p>
     *
     * <p>
     * If this RegionExpression is a FailedRegionExpression, then a
     * FailedRegionExpression results.
     * </p>
     *
     * @return The newly created RegionExpression which represents the
     *         inversion of this RegionExpression relative to the entire
     *         Space it belongs to.  Can be a FailedRegionExpression, if
     *         this is a FailedRegionExpression.  Never null.
     */
    public abstract RegionExpression invert ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Scales this RegionExpression up or down to the specified Size,
     * adding or removing Positions as necessary.
     * </p>
     *
     * <p>
     * For example, if a RegionExpression of positive integers
     * {2..4}, Size 3, is scaled to Size 9, it might be scaled to
     * positive integers {1..9}.
     * </p>
     *
     * <p>
     * An expression representing a NoRegion is always scaled to
     * an EmptyRegionExpression, regardless of the specified size.
     * </p>
     *
     * <p>
     * A FailedRegionExpression is always scaled to itself,
     * regardless of the specified size.
     * </p>
     *
     * <p>
     * Otherwise, scaling to Size.NONE always results in an
     * EmptyRegionExpression.
     * </p>
     *
     * <p>
     * If the specified Size belongs to a different Space than this
     * RegionExpression, then a FailedRegionExpression results.
     * </p>
     *
     * @param size The Size of the Region to scale to.  Must belong
     *             to the same Space as this RegionExpression.
     *             Must not be null.
     */
    public abstract RegionExpression scale (
                                            Size size
                                            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns a search engine which will search for Position(s)
     * matching the specified criteria within this RegionExpression.
     * </p>
     *
     * <p>
     * Depending on this RegionExpression, the search algorithm
     * might be implemented in different ways.  For example,
     * an integer array index Region might return a binary search
     * algorithm, while a Region of 4-dimensional Positions in
     * Cartesian space might return a multi-dimensional search
     * algorithm.
     * </p>
     *
     * @param criteria The Filter(s) to search for in this Region.
     *                 Must be capable of searching through Positions
     *                 in the same Space as this Region.
     *                 Must not be zero-length.  Must not be null.
     *                 Must not contain any null null elements.
     *
     * @return A newly created Search engine to search through
     *         the underlying Region for the specified criteria.
     *         Never null.
     */
    @SuppressWarnings("unchecked") // Heap pollution<generic varargs>
    public abstract Searcher search (
                                     Filter<Position> ... criteria
                                     )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Creates a new RegionExpression sorted by the specified
     * Order of Positions.
     * </p>
     *
     * <p>
     * For example, a RegionExpression for a scrambled integer
     * array index Region might be sorted numerically.
     * </p>
     *
     * @param order The order in which the resulting RegionExpression
     *              will be sorted.  Must be able to handle
     *              Positions from the same Space as this
     *              RegionExpression.  Must not be null.
     *
     * @return A newly created RegionExpression for a Region sorted
     *         by the specified Order.  If this is a failed
     *         RegionExpression then a failed RegionExpression
     *         will be returned.  Never null.
     */
    public abstract RegionExpression sort (
                                           Order<Position> order
                                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * @return The Region underlying this expression as a SparseRegion.
     *         If the Region is a SparseRegion, it will be returned as-is.
     *         If not, it will be built into a SparseRegion with one
     *         sub-Region, and returned.  If this is a FailedRegionExpression,
     *         then a NoSparseRegion will be returned.  This method
     *         is similar to the <code> orNone () </code> method,
     *         but always returns a SparseRegion.  Never null.
     */
    public abstract SparseRegion sparseRegionOrNone ();


    /**
     * <p>
     * Returns a new SparseRegion created by splitting this RegionExpression
     * into sub-Regions, each of the specified size.
     * </p>
     *
     * <p>
     * Note that the last sub-Region might be smaller than the specified
     * Size, if this Region does not divide without a remainder.
     * </p>
     *
     * @param size The size of each sub-Region.  Must belong to the same
     *             Space as this RegionExpression.  Must not be null.
     *
     * @return The newly created SparseRegion, split into sub-Regions each
     *         of the specified Size.  If the specified size is Size.NONE,
     *         or if the specified Size is of a different Space than this
     *         RegionExpression, then a NoSparseRegion will be returned.
     *         Never null.
     */
    public abstract SparseRegion splitBy (
                                          Size size
                                          )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns a new SparseRegion created by splitting this RegionExpression
     * into the specified number of sub-Regions, each of the same size.
     * </p>
     *
     * <p>
     * Note that the last sub-Region might be smaller than the others,
     * if this Region does not divide without a remainder.
     * </p>
     *
     * @param into The number of sub-Regions to split this Region into.
     *             Must be greater than 0.
     *
     * @return The newly created SparseRegion, split into the specified
     *         number of sub-Regions.  If the specified number is 0
     *         then a NoSparseRegion will be returned.  If the specified
     *         number is greater than the number of discrete Positions
     *         in this RegionExpression, then a SparseRegion with fewer
     *         sub-Regions will be returned, each sub-Region with exactly
     *         one Position.  Never null.
     */
    public abstract SparseRegion splitInto (
                                            long into
                                            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns a new RegionExpression representing the join of all
     * Positions contained in either or both of this RegionExpression
     * and the specified Region.
     * <p>
     *
     * <p>
     * If this RegionExpression is a FailedRegionExpression then it
     * is always returned.
     * </p>
     *
     * <p>
     * If this RegionExpression is empty then a RegionExpression
     * representing the specified Region is returned.  If the specified
     * Region is a NoRegion then this RegionExpression is returned.
     * </p>
     *
     * @param that The Region to join with this RegionExpression.
     *             Must belong to the same Space as this RegionExpression.
     *             Must not be null.
     *
     * @return The RegionExpression union of this RegionExpression and
     *         the specified Region, containing all Positions from both
     *         Regions.  Can be a FailedRegionExpression, if this
     *         is a FailedRegionExpression, or if the specified Region
     *         belongs to a different Space.  Never null.
     */
    public abstract RegionExpression union (
                                            Region that
                                            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;
}
