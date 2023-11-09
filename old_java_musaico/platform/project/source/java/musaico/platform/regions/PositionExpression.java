package musaico.region;

import java.io.Serializable;


import musaico.foundatin.condition.Conditional;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * A builder which starts from a specific Position and can be
 * transformed into a new Position.
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
 * no position at all (<code> Failed&lt;Position&gt; </code>) if the resulting
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
 * <pre>
 * Copyright (c) 2011, 2013 Johann Tienhaara
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
public interface PositionExpression
    extends Serializable
{
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
     * result will point to <code> Failed&lt;Position&gt; </code> from
     * the parent space.
     * </p>
     *
     * <p>
     * If the specified Size is <code> Space.none () </code>, or
     * if it does not even belong to the same Space as this
     * position expression, or if this position expression already
     * points to a <code> Failed&lt;Position&gt; </code>, then
     * the result will point to <code> Failed&lt;Position&gt; </code>.
     * </p>
     *
     * @param size The size to add to this position expression.
     *             Must not be null.
     *
     * @return The new PositionExpression, either pointing to
     *         a new Position within the bounding region of the
     *         current position expression, or pointing to
     *         <code> Failed&lt;Position&gt; </code> if any of
     *         the parameters broke the rules.  Never null.
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
     * then the resulting size expression will be <code> size 2 </code>.
     * The resulting size can be used on the position
     * expression <code> x </code>, for example to back up
     * to the previous 8-aligned boundary with
     * <code> x.subtract ( x.modulo ( size 8 ) ) </code>,
     * generating a new position expression at <code> 40 </code>.
     * </p>
     *
     * <p>
     * If the specified Size is <code> Space.none () </code> or
     * does not even belong to the same Space as this PositionExpression,
     * then a SizeExpression with <code> Space.none () </code>
     * is returned.
     * </p>
     *
     * @param size The size by which to modulo divide this position
     *             expression.  Must not be null.
     *
     * @return A new SizeExpression with a size calculated by modulo
     *         dividing this PositionExpression's position by the
     *         specified Size, or Space.none () if the specified
     *         Size is none or not in the same Space.  Never null.
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
     * will be returned at <code> 43 </code>.
     * </p>
     *
     * <p>
     * If the resulting Position would be outside the
     * Region which bounds this PositionExpression, or if
     * this PositionExpression is already out of bounds, then
     * a Failed Position with none = <code> Space.outOfBounds () </code>
     * will be returned.
     * </p>
     *
     * @return The Successful next Position, or a Failed Position
     *         if it would be out of bounds, or the current
     *         PositionExpression is already out of bounds.  Never null.
     */
    public abstract Conditional<Position, RegionViolation> next ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns the Successful current Position, or a
     * Failed Position if it is out of the
     * Region which bounds this PositionExpression.
     * </p>
     *
     * <p>
     * For example, in an integer-based Space, given a
     * PositionExpression <code> x </code> at position
     * <code> 42 </code>, if a call is made to <code> x.position () </code>
     * then a <code> Successful&lt;Position, RegionViolation&gt; </code>
     * will be returned at <code> 42 </code>.
     * </p>
     *
     * <p>
     * If this PositionExpression is already out of bounds, then
     * a Failed Position with none = <code> Space.outOfBounds () </code>
     * will be returned.
     * </p>
     *
     * @return The Successful next Position, or a Failed Position
     *         if it would be out of bounds, or the current
     *         PositionExpression is already out of bounds.  Never null.
     */
    public abstract Conditional<Position, RegionViolation> position ()
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
     * will be returned at <code> 41 </code>.
     * </p>
     *
     * <p>
     * If the resulting Position would be outside the
     * Region which bounds this PositionExpression, or if
     * this PositionExpression is already out of bounds, then
     * a Failed Position with none = <code> Space.outOfBounds () </code>
     * will be returned.
     * </p>
     *
     * @return The Successful previous Position, or a Failed Position
     *         if it would be out of bounds, or the current
     *         PositionExpression is already out of bounds.  Never null.
     */
    public abstract Conditional<Position, RegionViolation> previous ()
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
     * calling <code> x.subtract ( position 8 ) </code> will
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
     * this Space, or if this expression refers to a
     * <code> Failed&lt;Position&gt; </code>, then a SizeExpression
     * pointing to a <code> Failed&lt;Size&gt; </code> will be
     * returned with none = <code> Space.none () </code>.
     * </p>
     *
     * @param that The position to subtract from this PositionExpression.
     *             Must not be null.
     *
     * @return The SizeExpression distance from that position to this
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
     * will result in a SizeExpression of <code> size 34 </code>.
     * </p>
     *
     * <p>
     * If the position would be outside the Region bounding this
     * PositionExpression, or if the specified Size is not even in
     * this Space, or if this PositionExpression already refers to
     * a position outside the Region bounding this expression,
     * then the resulting PositionExpression will point
     * to a <code> Failed&lt;Position&gt; </code>.
     * </p>
     *
     * @param size The size to subtract from this PositionExpression.
     *             Must not be null.
     *
     * @return The PositionExpression pointing to the position arrived
     *         at by subtracting the specified Size from the current
     *         position.  Never null.
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
     * will resut in a continuous Region covering the positions
     * <code> 42, 43, 44, ..., 62, 63, 64 </code>.
     * </p>
     *
     * <p>
     * If the specified Position is outside the Region which
     * bounds this expression, or if the specified Position is
     * not even in the same Space as this position expression,
     * or if this expression refers to a Position which is outside
     * the Region bounding this expression, then a RegionExpression
     * pointing to a <code> Failed&lt;Region&gt; </code> will
     * be returned.
     * </p>
     *
     * <p>
     * If the Region bounding this PositionExpression is a sparse
     * region, then the resulting region expression will exclude
     * all positions excluded by the parent bounding region.
     * </p>
     *
     * @param that The position to which a Region will extend from
     *             this PositionExpression.  Must not be null.
     *
     * @return The RegionExpression covering the specified range of
     *             Positions.  Never null.
     */
    public abstract RegionExpression to (
                                         Position that
                                         )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;
}
