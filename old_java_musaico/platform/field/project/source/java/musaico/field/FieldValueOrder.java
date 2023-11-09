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

import musaico.time.Time;


/**
 * <p>
 * Orders Fields by their values, relying on some other Order
 * to do the comparisons between field values, and possibly also
 * inducing casts under the hood (for example if a field's storage
 * value is Integer and the order is for Strings, then the field's
 * value will be cast to a String before comparing).
 * </p>
 *
 * <p>
 * For example, to order fields by their values by dictionary order:
 * </p>
 *
 * <pre>
 *     Order<Field> dictionary_order_field_values =
 *         new FieldValueOrder ( Order.DICTIONARY );
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
public class FieldValueOrder<ORDER extends Object>
    extends AbstractOrder<Field>
    implements Serializable
{
    /** Orders fields by value by dictionary order. */
    public static final Order<Field> DICTIONARY =
        new FieldValueOrder ( Order.DICTIONARY, String.class );

    /** Compares fields by value by their character encoding
     *  values (ASCII etc). */
    public static final Order<Field> CHARACTER_ENCODING =
        new FieldValueOrder ( Order.CHARACTER_ENCODING, String.class );

    /** Orders fields by value by numeric order. */
    public static final Order<Field> NUMERIC =
        new FieldValueOrder ( Order.NUMERIC, Number.class );

    /** Compares fields by value by time. */
    public static final Order<Field> TIME =
        new FieldValueOrder ( Order.TIME, Time.class );


    /** The Order for field values.  We delegate value comparisons
     *  to this order. */
    private final Order<ORDER> valueOrder;

    /** The class of values to compare by, such as String.class,
     *  Integer.class and so on. */
    private final Class<ORDER> valueClass;


    /**
     * <p>
     * Creates a new FieldValueOrder with the specified order
     * to compare values.
     * </p>
     *
     * @param value_order The Order used to compare field values,
     *                    such as Order.DICTIONARY.  Must not be null.
     *
     * @param value_class The class of values to compare by,
     *                    such as String.class or Integer.class.
     *                    Must not be null.
     */
    public FieldValueOrder (
                            Order<ORDER> value_order,
                            Class<ORDER> value_class
                            )
    {
        super ( new SimpleMessageBuilder ()
                .text ( "Field value order by: [%value_order%]" )
                .parameter ( "value_order", value_order.description () )
                .build () );

        this.valueOrder = value_order;
        this.valueClass = value_class;
    }


    /**
     * @see musaico.io.Order#compareValues(java.lang.Object,java.lang.Object)
     *
     * Final for speed.
     */
    public final Comparison compareValues (
                                           Field left,
                                           Field right
                                           )
    {
        return this.valueOrder
            .compareValues ( left.value ( this.valueClass ),
                             right.value ( this.valueClass ) );
    }
}
