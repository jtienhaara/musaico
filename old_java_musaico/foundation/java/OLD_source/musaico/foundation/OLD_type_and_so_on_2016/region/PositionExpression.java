package musaico.foundation.region;

import java.io.Serializable;


import musaico.foundation.condition.Conditional;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * A builder which starts from a specific Position and can be
 * transformed into a new Position.
 * </p>
 *
 * <p>
 * A PositionExpression is either successful or failed.  For example,
 * creating a PositionExpression from the first Position in a Region
 * might result in a successful PositionExpression.  But if the
 * Region is only one Position long, then calling <code> next () </code>
 * on that PositionExpression will return a failed one.
 * </p>
 *
 * <p>
 * PositionExpressions are often more or less invisible to the
 * caller, used to step through a Region.  For example the following
 * code might be used to step through the Positions of a Region
 * in reverse order:
 * </p>
 *
 * <pre>
 *     Region region = ...;
 *     for ( Position position = region.end ();
 *           region.contains ( position );
 *           position = region.expr ( position ).previous ().orNone () )
 *     {
 *         ...Do something at position...
 *     }
 * </pre>
 *
 * <p>
 * However more complex possibilities are certainly permitted.
 * </p>
 *
 * <p>
 * Every PositionExpression is bound within a specific Region, so
 * that, for example, adding to the expression will result in
 * a failed PositionExpression if the resulting
 * position would have been outside the bounding Region.
 * </p>
 *
 * <p>
 * Thus, for example, <code> large_region.expr ( position ) </code>
 * might have different results that
 * <code> small_region.expr ( position ) </code> for the same
 * call to <code> add ( size ) </code>.
 * </p>
 *
 * <p>
 * The PositionExpression class is designed to bring to the world
 * of abstract Positions the simple mathematical mechanisms
 * provided in the world of integer indices, used as offsets into
 * arrays and pointers and so on.  Each of the same operations
 * most commonly performed on an int index in C shall have an
 * equivalent in PositionExpression.  For example: add (+),
 * subtract (-), next (++), previous (--), modulo (%).
 * </p>
 *
 * <p>
 * Also the equivalent of creating a new pointer is provided with "to",
 * allowing a new RegionExpression to be created from a PositionExpression
 * starting position and a Position ending position.  The resulting
 * RegionExpression is bound within the same parent Region
 * as the PositionExpression, so operations cannot expand outside
 * the original Region.
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
public interface PositionExpression
    extends Conditional<Position, RegionViolation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Adds the specified Size to this PositionExpression,
     * and returns a new PositionExpression pointing to the offset
     * position.
     * </p>
     *
     * <p>
     * For example, in an integer-based Space, a PositionExpression
     * <code> x </code> at position <code> 42 </code> might handle
     * the call <code> x.add ( size 8 ) </code> by returning a
     * new PositionExpression at <code> 50 </code>.
     * </p>
     *
     * <p>
     * If the resulting Position would be outside the bounds
     * of this PositionExpression's parent Region, then the
     * result will be a failed PositionExpression.
     * </p>
     *
     * <p>
     * If the specified Size is <code> Size.NONE </code>, or
     * if it does not even belong to the same Space as this
     * position expression, or if this is a failed PositionExpression,
     * then a failed PositionExpression will be returned.
     * </p>
     *
     * @param size The size to add to this position expression.
     *             Must not be null.
     *
     * @return The new PositionExpression, pointing to
     *         a new Position within the bounding region of the
     *         current position expression.  Can be a failed
     *         PositionExpression if any of the parameters
     *         broke the rules.  On success, always belongs
     *         to the same Region and Space as this
     *         PositionExpression.  Never null.
     */
    public abstract PositionExpression add (
                                            Size size
                                            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns the Size that is left when the position is
     * modulo divided by the specified Size.
     * </p>
     *
     * <p>
     * For example, in an integer-based space, if position
     * expression <code> x </code> is at position <code> 42 </code>,
     * and a call is made to <code> x.modulo ( size 8 ) </code>,
     * then the resulting size expression might be <code> size 2 </code>.
     * The resulting size can be used on the position
     * expression <code> x </code>, for example to back up
     * to the previous 8-aligned boundary with
     * <code> x.subtract ( x.modulo ( size 8 ) ) </code>,
     * generating a new position expression at <code> 40 </code>.
     * </p>
     *
     * <p>
     * If the specified Size is <code> Size.NONE </code> or
     * does not even belong to the same Space as this PositionExpression,
     * then a failed SizeExpression is returned.
     * </p>
     *
     * @param size The size by which to modulo divide this position
     *             expression.  Must not be null.
     *
     * @return A new SizeExpression with a size calculated by modulo
     *         dividing this PositionExpression's position by the
     *         specified Size.  Can be a failed SizeExpression
     *         if the specified Size is Size.NONE or not in the same
     *         Space, or if this is a failed PositionExpression.
     *         On success, always belongs to the same Space as
     *         this PositionExpression.  Never null.
     */
    public abstract SizeExpression modulo (
                                           Size size
                                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns the Successful next Position after the current
     * one, or a Failed Position if it would be out of the
     * Region which bounds this PositionExpression.
     * </p>
     *
     * <p>
     * For example, in an integer-based Space, given a
     * PositionExpression <code> x </code> at position
     * <code> 42 </code>, if a call is made to <code> x.next () </code>
     * then a <code> Successful&lt;Position, RegionViolation&gt; </code>
     * might be returned at <code> 43 </code>.
     * </p>
     *
     * <p>
     * If the resulting Position would be outside the
     * Region which bounds this PositionExpression, or if
     * this PositionExpression is already out of bounds, then
     * a failed PositionExpression will be returned.
     * </p>
     *
     * @return The Successful next PositionExpression, or a
     *         Failed PositionExpression if it would be out
     *         of bounds, or the current PositionExpression
     *         is already out of bounds.  On success, always
     *         belongs to the same Region and Space as this
     *         PositionExpression.  Never null.
     */
    public abstract PositionExpression next ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns the Successful previous Position before the current
     * one, or a Failed Position if it would be out of the
     * Region which bounds this PositionExpression.
     * </p>
     *
     * <p>
     * For example, in an integer-based Space, given a
     * PositionExpression <code> x </code> at position
     * <code> 42 </code>, if a call is made to <code> x.previous () </code>
     * then a <code> Successful&lt;Position, RegionViolation&gt; </code>
     * might be returned at <code> 41 </code>.
     * </p>
     *
     * <p>
     * If the resulting Position would be outside the
     * Region which bounds this PositionExpression, or if
     * this PositionExpression is already out of bounds, then
     * a failed PositionExpression will be returned.
     * </p>
     *
     * @return The Successful previous PositionExpression,
     *         or a Failed PositionExpression if it would be
     *         out of bounds, or the current PositionExpression
     *         is already out of bounds.  On success, always
     *         belongs to the same Region and Space as this
     *         PositionExpression.  Never null.
     */
    public abstract PositionExpression previous ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns the Size or distance from the specified Position to
     * this PositionExpression.
     * </p>
     *
     * <p>
     * For example, in an integer-based Space, given a
     * PositionExpression <code> x </code> at position <code> 42 </code>,
     * calling <code> x.subtract ( position 8 ) </code> might
     * return a SizeExpression of <code> size 34 </code>.
     * </p>
     *
     * <p>
     * Even if the specified Position is outside the region
     * bounding this PositionExpression, this method will still
     * return a valid SizeExpression.
     * </p>
     *
     * <p>
     * If the Size between the specified Position and this
     * PositionExpression would be bigger than is possible in
     * this Space, or if this is a failed PositionExpression,
     * or if the specified Position is Position.NONE, then a
     * failed SizeExpression will be returned.
     * </p>
     *
     * @param that The position to subtract from this PositionExpression.
     *             Must not be null.
     *
     * @return The SizeExpression distance from that position to this
     *         PositionExpression.  Can be a failed SizeExpression
     *         if the specified Position does not belong to the same
     *         Space as this PositionExpression, or if it is greater
     *         than this PositionExpression, or if it is Position.NONE,
     *         or if this is a failed PositionExpression.  On success,
     *         always belongs to the same Space as this
     *         PositionExpression.  Never null.
     */
    public abstract SizeExpression subtract (
                                             Position that
                                             )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns the position arrived at by traveling the specified Size
     * back from this PositionExpression.
     * </p>
     *
     * <p>
     * For example, in an integer-based Space, given a
     * PositionExpression <code> x </code> at position <code> 42 </code>,
     * a call to <code> x.subtract ( position 8 ) </code>
     * might result in a SizeExpression of <code> size 34 </code>.
     * </p>
     *
     * <p>
     * If the position would be outside the Region bounding this
     * PositionExpression, or if the specified Size is not even in
     * this Space, or if this is a failedd PositionExpression,
     * then the result will be a failed PositionExpression.
     * </p>
     *
     * @param size The size to subtract from this PositionExpression.
     *             Must not be null.
     *
     * @return The PositionExpression pointing to the position arrived
     *         at by subtracting the specified Size from the current
     *         position.  Can be a failed PositionExpression, if
     *         specified parameter is invalid, or if the resulting
     *         Position would be out of bounds, or if this is a failed
     *         PositionExpression to begin with.  On success, always
     *         belongs to the same Region and Space as this
     *         PositionExpression.  Never null.
     */
    public abstract PositionExpression subtract (
                                                 Size size
                                                 )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Creates a new Region from this position expression to the
     * specified Position.
     * </p>
     *
     * <p>
     * For example, in an integer-based Space, given a
     * PositionExpression <code> x </code> pointing to position
     * <code> 42 </code>, calling <code> x.to ( position 64 ) </code>
     * might resut in a continuous Region covering the positions
     * <code> 42, 43, 44, ..., 62, 63, 64 </code>.
     * </p>
     *
     * <p>
     * If the specified Position is outside the Region which
     * bounds this expression, or if the specified Position is
     * not even in the same Space as this position expression,
     * or if this is a failed PositionExpression, then a
     * failed RegionExpression will be returned.
     * </p>
     *
     * <p>
     * If the Region bounding this PositionExpression is a sparse
     * region, then the resulting region expression will exclude
     * all positions excluded by the parent bounding region.
     * </p>
     *
     * @param that The position to which a Region will extend from
     *             this PositionExpression.  Must belong to the
     *             same Space as this PositionExpression.  Must
     *             be greater than / after this PositionExpression.
     *             Must be inside the bounds of this PositionExpression's
     *             parent Region.  Must not be null.
     *
     * @return The RegionExpression covering the specified range of
     *         Positions.  Can be a failed RegionExpression, if the
     *         specified Position is invalid, or if this is already
     *         a failed PositionExpression.  On success, always
     *         belongs to the same Region and Space as this
     *         PositionExpression.  Never null.
     */
    public abstract RegionExpression to (
                                         Position that
                                         )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;
}
