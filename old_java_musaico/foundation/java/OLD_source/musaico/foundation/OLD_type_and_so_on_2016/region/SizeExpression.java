package musaico.foundation.region;

import java.io.Serializable;

import java.math.BigDecimal;


import musaico.foundation.condition.Conditional;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * A builder which starts from a specific Size and can build a new
 * Size.
 * </p>
 *
 * <p>
 * A SizeExpression is either successful or failed.  For example,
 * creating a SizeExpression from the Size of a Region
 * might result in a successful PositionExpression.  But if the
 * Region is only one Position long, then calling <code> divide ( 2 ) </code>
 * on that SizeExpression will return a failed one.
 * </p>
 *
 * <p>
 * A SizeExpression's methods are typically invoked without maintaining
 * a reference to the expression.  For example:
 * </p>
 *
 * <pre>
 *     Region region = ...;
 *     Position middle = ...;
 *     Size half_of_region_size =
 *         region.expr ( middle ).subtract ( region.start () )
 *         .orNone ();
 * </pre>
 *
 * <p>
 * In the above code, subtracting the <code> region.start () </code>
 * position from the <code> middle </code> position results in
 * a SizeExpression.  The <code> size () </code> may then be retrieved.
 * In case of invalid SizeExpression (such as position 10
 * subtracted from position 1, which would be a negative size),
 * the <code> orNone () </code> failure branch would return an
 * empty Size.
 * </p>
 *
 * <p>
 * The SizeExpression class is designed to bring to the world
 * of abstract Sizes the simple mathematical mechanisms
 * provided in the world of integer size_ts, used as lengths of
 * arrays and sizes of structures and so on.  Each of the same operations
 * most commonly performed on a size_t in C shall have an
 * equivalent in PositionExpression.  For example: add (+),
 * subtract (-), multiply (*), modulo (%), divide by floating
 * point ((size_t) ( size_t / double )), and ratio between two
 * sizes ((double) ( size_t / size_t )).
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
public interface SizeExpression
    extends Conditional<Size, RegionViolation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Adds the specified Size to this one to create a new sum Size.
     * </p>
     *
     * <p>
     * For example, adding an integer size of 1 to an integer
     * size of 2 might result in an integer size of 3.  Or adding
     * a time size of 30 seconds to a time size of 1 minute might
     * result in a time size of 90 seconds.  And so on.
     * </p>
     *
     * <p>
     * If the specified Size to add is <code> Size.NONE </code>,
     * or if the specified Size does not belong to the same Space
     * as this Size, then a new failed SizeExpression
     * is returned.
     * </p>
     *
     * @param that The Size to add to this Size.  Must be part of the
     *             same Space as this Size.  Must not be null.
     *
     * @return A new SizeExpression with the resulting sum Size.
     *         Note that a failed SizeExpression will be returned if
     *         the specified parameter is invalid.  Never null.
     */
    public abstract SizeExpression add (
                                        Size that
                                        )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns a new Size expression for a Size that is
     * <code> 1 / divisor </code> the current Size.
     * </p>
     *
     * <p>
     * For example, dividing a SizeExpression of integer Size 10
     * by 3.0 might result in a SizeExpression for new integer
     * Size 3.  Or dividing a SizeExpression of time Size 60 seconds
     * by 2.0 might result in a SizeExpression for new time
     * Size 30 seconds.  Or dividing a SizeExpression for 4-D
     * Size { 10.0, 20.0, 30.0, 0.0 } by 5.0 might result in a
     * SizeExpression for new 4-D Size { 2.0, 4.0, 6.0, 0.0 }.
     * And so on.
     * </p>
     *
     * <p>
     * If the specified divisor is less than or equal to 0, then
     * the result will be a failed SizeExpression.
     * </p>
     *
     * <p>
     * Fractional SizeExpressions will be rounded down.
     * For example, dividing a SizeExpression for integer Size 5
     * by divisor 2 might result in a SizeExpression for integer Size 2
     * (rounded down from 2.5).  Or dividing a SizeExpression
     * for integer Size 2 by divisor 3 might result in a failed
     * SizeExpression (0.67 rounded down to nothing).
     * </p>
     *
     * @param divisor The amount to divide this SizeExpression by.
     *                Must be greater than 0.  Must not be null.
     *
     * @return A new SizeExpression for the Size calculated by dividing
     *         this SizeExpression by the specified divisor.  If this
     *         SizeExpression is already for Size.NONE, or if the
     *         specified divisor is less than or equal to 0, then the
     *         result will be a failed SizeExpression.
     *         If the resulting SizeExpression is for a fractional Size,
     *         then the Size is rounded down.  For example, dividing
     *         integer Size 3 by divisor 2 might result in integer
     *         Size 1; or dividing integer Size 1 by divisor 2 might
     *         result in a failed SizeExpression; and so on.  Never null.
     */
    public abstract SizeExpression divide (
                                           BigDecimal divisor
                                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Modulo divides this SizeExpression by the specified Size.
     * </p>
     *
     * <p>
     * For example, a SizeExpression for integer Size 42 modulo
     * divided by integer Size 10 might result in a SizeExpression
     * for integer Size 2.  Or a SizeExpression for time Size
     * 79 seconds modulo divided by time Size 60 seconds might
     * result in a SizeExpression for time Size 19 seconds.
     * And so on.
     * </p>
     *
     * <p>
     * If the specified Size is not part of the same Space as this
     * SizeExpression, then the result is always a failed SizeExpression.
     * </p>
     *
     * @param size The Size to modulo divide this current expression by.
     *             For example, an integer Size 10, or a time Size 60
     *             seconds, and so on.  Must belong to the same Space
     *             as this SizeExpression.  Must not be null.
     *
     * @return A new SizeExpression, calculated by modulo dividing the
     *         current SizeExpression by the specified Size.
     *         If the current SizeExpression is for Size.NONE, then
     *         a failed SizeExpression will be returned.
     *         If the specified Size is not part of the same Space as
     *         this SizeExpression, then a failed SizeExpression
     *         will be returned.  Never null.
     */
   public abstract SizeExpression modulo (
                                           Size size
                                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Creates a new SizeExpression for a Size that is <i>factor</i>
     * times the Size of this expression.
     * </p>
     *
     * <p>
     * For example, multiplying a SizeExpression for integer Size 10
     * by a factor of 3 might result in a SizeExpression for integer
     * Size 30.  Or multiplying a SizeExpression for a time Size of
     * 20 seconds by 3 might result in a SizeExpression for a time Size
     * of 1 minute.  Or multiplying a SizeExpression for a 4-D
     * Size { 10.0, 20.0, 30.0, 0.0 } by 0.5 might result in a
     * SizeExpression for a 4-D size { 5.0, 10.0, 15.0, 0.0 }.
     * And so on.
     * </p>
     *
     * <p>
     * If the factor is less than or equal to 0 then a failed
     * SizeExpression will always be returned.  If multiplying this
     * SizeExpression by the specified factor would lead to a SizeExpression
     * beyond the bounds of the Size type (such as an integer Size
     * greater than Long.MAX_VALUE), then a failed SizeExpression
     * will always be returned.  If this SizeExpression is already
     * for Size.NONE, then a failed SizeExpression will be returned.
     * </p>
     *
     * <p>
     * The Size represented by the resulting SizeExpression is rounded
     * down if there is any fractional component.  For example,
     * multiplying a SizeExpression for an integer Size 3 by
     * factor 2.5 might result in a SizeExpression for integer Size 7
     * (rounded down from 7.5).
     * </p>
     *
     * @param factor The amount to multiply this SizeExpression by.
     *               Must be greater than 0.  Must not be null.
     *
     * @return A new SizeExpression for the Size that is <i> factor </i>
     *         times the Size of this expression.  If this expression
     *         is for Size.NONE, or if the specified factor is less than
     *         or equal to 0, or if the resulting size would be too large
     *         for the Size type (such as an integer Size greater than
     *         Long.MAX_VALUE), then a failed SizeExpression will be
     *         returned.  Never null.
     */
    public abstract SizeExpression multiply (
                                             BigDecimal factor
                                             )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns the ratio calculated by dividing this SizeExpression
     * by the specified Size.
     * </p>
     *
     * <pre>
     *     ratio = this / that
     * </pre>
     *
     * <p>
     * For example, if this is a SizeExpression for integer Size 10,
     * that that is integer Size 5, then a ratio of 2.0 might be
     * returned.  Or if this is a SizeExpression for time Size 60 seconds,
     * and that is time Size 180 seconds, then a ratio of 0.333...
     * might be returned.  Or if this is a SizeExpression for
     * 4-D Size { 10.0, 20.0, 30.0, 0.0 } and that is 4-D Size
     * { 1.0, 5.0, 30.0, 0.0 }, then a ratio of 17.4928... might be
     * returned (sqrt( 9^2 + 15^2 + 0^2 + 0^2)).
     * </p>
     *
     * <p>
     * If the specified Size does not belong to the same Space as this
     * SizeExpression, or if the specified Size is Size.NONE, or if
     * this is a failed SizeExpression, then a ratio of 0 will be
     * returned.
     * </p>
     *
     * @param that The Size to which this SizeExpression's ratio will
     *             be calculated.  Must belong to the same Space as
     *             this SizeExpression.  Must not be null.
     *
     * @return The ratio between this SizeExpression and the specified
     *         Size.  Can be 0 if the specified Size does not belong
     *         to the same Space as this SizeExpression, or if the
     *         specified Size is Size.NONE, or if this is a failed
     *         SizeExpression.  Never negative.  Never null.
     */
    public abstract BigDecimal ratio (
                                      Size that
                                      )
        throws ParametersMustNotBeNull.Violation;


    /**
     * <p>
     * Returns a new SizeExpression for the Size gap between that
     * and this.
     * </p>
     *
     * <pre>
     *     gap = this - that
     * </pre>
     *
     * <p>
     * For example, if this is a SizeExpression for integer Size 42,
     * and that is integer Size 10, then a SizeExpression for
     * integer Size 32 might be returned.  Or if this is a SizeExpression
     * for time Size 60 seconds, and that is time Size 15 seconds, then
     * a SizeExpression for time Size 45 seconds might be returned.
     * Or if this is a SizeExpression for 4-D Size { 10.0, 20.0, 30.0, 0.0 }
     * and that is 4-D Size { 8.0, 13.0, 30.0, 0.0 } then a SizeExpression
     * for 4-D Size { 2.0, 7.0, 0.0, 0.0 } might be returned.
     * </p>
     *
     * <p>
     * If the specified Size does not belong to the same Space as this\
     * SizeExpression, or if the specified Size is Size.NONE, or
     * if this SizeExpression is a failed SizeExpression, then
     * a failed SizeExpression will be returned.
     * </p>
     *
     * @param that The Size to subtract from this SizeExpression.
     *             Must belong to the same Space as this SizeExpression.
     *             Must not be null.
     *
     * @return A new SizeExpression for the Size calculated by subtracting
     *         the specified Size from the Size in this SizeExpression.
     *         Can be a failed SizeExpression, if the specified Size
     *         is Size.NONE or does not belong to the same Space as this
     *         SizeExpression, or if this SizeExpression is failed.
     *         Never null.
     */
    public abstract SizeExpression subtract (
                                             Size that
                                             )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;
}
