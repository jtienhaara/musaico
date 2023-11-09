package musaico.foundation.io;


/**
 * <p>
 * Represents some class of objects which have their own natural
 * order, and can be compared, sorted and so on to one another.
 * </p>
 *
 * <p>
 * Every naturally ordered class must provide an Order, such as
 * <code> Order.DICTIONARY </code>,
 * <code> Order.TIME </code>,
 * <code> Order.NUMERIC </code>,
 * <code> Order.CHARACTER_ENCODING </code>, and
 * so on, or possibly an order of its own.
 * </p>
 *
 * <p>
 * When <code> x.order ().compareValues ( x, y ) </code>
 * is invoked, each ordered object's <code> orderIndex () </code> is
 * retrieved and compared via its
 * <code> order ().compareValues ( x_order, y_order ) </code>.
 * </p>
 *
 * <p>
 * For example, given the following class:
 * </p>
 *
 * <pre>
 *     public class Foo
 *         implements NaturallyOrdered<Integer>
 *     {
 *         private final Integer order;
 *
 *         public Foo ( int order )
 *         {
 *             this.order = order;
 *         }
 *
 *         public final Order<Integer> order ()
 *         {
 *             return Order.NUMERIC;
 *         }
 *
 *         public final Integer orderIndex ()
 *         {
 *             return this.order;
 *         }
 *     }
 * </pre>
 *
 * <p>
 * Then the following code:
 * </p>
 *
 * <pre>
 *     Foo foo_11 = new Foo ( 11 );
 *     Foo foo_32 = new Foo ( 32 );
 *     Comparison comparison = foo_11.order ().compareValues ( foo_11, foo_32 );
 * </pre>
 *
 * <p>
 * Will return <code> Comparison.LEFT_LESS_THAN_RIGHT </code>.
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
 * Copyright (c) 2011 Johann Tienhaara
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
public interface NaturallyOrdered<ORDER extends Object>
{
    /**
     * <p>
     * Returns the value to compare for natural order
     * purposes.
     * </p>
     *
     * @return This object's index, for comparison
     *         purposes.  Never null.
     */
    public abstract ORDER orderIndex ();


    /**
     * <p>
     * Returns the natural order of this object
     * (such as Order.NUMERIC, Order.TIME,
     * Order.DICTIONARY, and so on).
     * </p>
     *
     * <p>
     * There may be different orders for any class
     * of ORDER primitives.  For example, Strings can
     * be dictionary ordered or character encoding ordered
     * (ASCII, and so on).
     * Some objects may also wish to create their own
     * numeric orders which discard decimal values,
     * performing only integer comparisons.  And so on.
     * </p>
     *
     * @return This object's natural order.  Never null.
     */
    public abstract Order<ORDER> order ();
}
