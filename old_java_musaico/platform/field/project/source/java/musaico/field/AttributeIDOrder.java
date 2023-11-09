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


/**
 * <p>
 * Orders Attributes by their IDs, relying on some other Order&lt;String&gt;
 * to do the comparisons between attribute ids.
 * </p>
 *
 * <p>
 * For example, to order attributes by their ids by dictionary order:
 * </p>
 *
 * <pre>
 *     Order<Attribute> dictionary_order_attribute_ids =
 *         new AttributeIDOrder ( Order.DICTIONARY );
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
public class AttributeIDOrder
    extends AbstractOrder<Attribute>
    implements Serializable
{
    /** Orders attributes by id by dictionary order. */
    public static final Order<Attribute> DICTIONARY =
        new AttributeIDOrder ( Order.DICTIONARY );

    /** Compares attributes by id by their character encoding
     *  values (ASCII etc). */
    public static final Order<Attribute> CHARACTER_ENCODING =
        new AttributeIDOrder ( Order.CHARACTER_ENCODING );


    /** The Order for attribute ids.  We delegate id comparisons
     *  to this order. */
    private final Order<String> idOrder;


    /**
     * <p>
     * Creates a new AttributeIDOrder with the specified String
     * order to compare ids.
     * </p>
     *
     * @param id_order The String Order used to compare attribute ids,
     *                 such as Order.DICTIONARY.  Must not be null.
     */
    public AttributeIDOrder (
                             Order<String> id_order
                             )
    {
        super ( new SimpleMessageBuilder ()
                .text ( "Attribute id order by: [%id_order%]" )
                .parameter ( "id_order", id_order.description () )
                .build () );

        this.idOrder = id_order;
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
        return this.idOrder.compareValues ( left.id (), right.id () );
    }
}
