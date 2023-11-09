package musaico.platform.order;


import musaico.foundation.contract.guarantees.ReturnNeverNull;


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
public interface NaturallyOrdered<ORDER extends NaturallyOrdered<?>>
{
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
     * numeric orders which discard decimal point values,
     * performing only integer comparisons.  And so on.
     * </p>
     *
     * @return This object's natural order.  Never null.
     */
    public abstract Order<ORDER> order ()
        throws ReturnNeverNull.Violation;
}
