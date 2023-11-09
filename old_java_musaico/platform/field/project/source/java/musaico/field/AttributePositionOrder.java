package musaico.field;


import java.io.Serializable;

import java.util.Comparator;
import java.util.Collection;
import java.util.List;


import musaico.i18n.Internationalized;

import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.i18n.message.SimpleMessageBuilder;

import musaico.io.AbstractOrder;
import musaico.io.Comparison;
import musaico.io.Order;

import musaico.region.Position;
import musaico.region.Space;


/**
 * <p>
 * Orders Attributes by their positions, delegating the position
 * order to some Order&lt;Position&gt;.
 * </p>
 *
 * <p>
 * For example, to order attributes by their positions:
 * </p>
 *
 * <pre>
 *     Space space = my_attribute.position ().space ();
 *     Order<Attribute> position_order_attributes =
 *         new AttributePositionOrder ( space.order () );
 * </pre>
 *
 * <p>
 * Or to reverse the order:
 * </p>
 *
 * <pre>
 *     Space space = my_attribute.position ().space ();
 *     Order<Attribute> reverse_order_attributes =
 *         new AttributePositionOrder ( new ReverseOrder ( space.order () ) );
 * </pre>
 *
 *
 * <p>
 * In Java, every Order is Serializable in order to play nicely
 * over RMI.
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
 * Copyright (c) 2012 Johann Tienhaara
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
public class AttributePositionOrder
    extends AbstractOrder<Attribute>
    implements Serializable
{
    /** The Order for attribute positions.  We delegate position comparisons
     *  to this order. */
    private final Order<Position> positionOrder;


    /**
     * <p>
     * Creates a new AttributePositionOrder with the specified Position
     * order to compare positions.
     * </p>
     *
     * @param position_order The Position Order used to compare attribute
     *                       positions.  For example,
     *                       <code> my_attribute.position ().space ().order () </code>.
     *                       Must not be null.
     */
    public AttributePositionOrder (
                                   Order<Position> position_order
                                   )
    {
        super ( new SimpleMessageBuilder ()
                .text ( "Attribute position order by: [%position_order%]" )
                .parameter ( "position_order", position_order.description () )
                .build () );

        this.positionOrder = position_order;
    }


    /**
     * <p>
     * Creates a new AttributePositionOrder with the default order
     * of positions for the specified Space.
     * </p>
     *
     * @param space The space whose default order will be used to
     *              compare positions.  Must not be null.
     */
    public AttributePositionOrder (
                                   Space space
                                   )
    {
        this ( space.order () );
    }


    /**
     * @see musaico.io.Order#compareValues(java.lang.Object,java.lang.Object)
     *
     * Final for speed.
     */
    public final Comparison compareValues (
                                           Attribute left,
                                           Attribute right
                                           )
    {
        return this.positionOrder.compareValues ( left.position (),
                                                  right.position () );
    }
}
