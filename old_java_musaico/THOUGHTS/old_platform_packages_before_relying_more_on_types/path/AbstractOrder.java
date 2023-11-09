package musaico.foundation.io;

import java.io.Serializable;

import java.lang.reflect.Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


import musaico.foundation.i18n.Internationalized;

import musaico.foundation.i18n.exceptions.I18nIllegalArgumentException;

import musaico.foundation.i18n.message.Message;
import musaico.foundation.i18n.message.SimpleMessageBuilder;

import musaico.foundation.i18n.exceptions.L10n;


/**
 * <p>
 * Provides Java Comparator compatibility for all Orders.
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
public abstract class AbstractOrder<ORDER extends Object>
    implements Order<ORDER>, Serializable
{
    /** The internationalized description of this order.
     *  Can be localized for display to end users. */
    private final Internationalized<Message,String> internationalizedDescription;


    // Every AbstractOrder must implement Order.compareValues().


    /**
     * <p>
     * Creates a new AbstractOrder with the specified
     * description international text.
     * </p>
     *
     * <p>
     * Make sure you put internationalized_description_text
     * into your Messages.properties file, and translate it
     * for other locales!
     * </p>
     *
     * @param internationalized_description_text The international
     *                                           identifier for the
     *                                           description message
     *                                           for this order.
     *                                           This text will be
     *                                           internationalized, and
     *                                           then the Messages.properties
     *                                           files will be searched any
     *                                           time the description of
     *                                           this Order is localized
     *                                           (for example, by a client
     *                                           application).
     *                                           Must not be null.
     */
    public AbstractOrder (
                          String internationalized_description_text
                          )
    {
        this ( new SimpleMessageBuilder ()
               .text ( internationalized_description_text )
               .build () );
    }


    /**
     * <p>
     * Creates a new AbstractOrder with the specified
     * description international text.
     * </p>
     *
     * @param description The internationalized Message for the
     *                    description of this order.  This
     *                    Message will be internationalized, and
     *                    then the Messages.properties files
     *                    will be searched any time the description
     *                    of this Order is localized (for
     *                    example, by a client application).
     *                    Must not be null.
     */
    public AbstractOrder (
                          Message description_message
                          )
    {
        this.internationalizedDescription =
            L10n.internationalize ( this, description_message );
    }


    /**
     * @see java.util.Comparator#compare(java.lang.Object,java.lang.Object)
     *
     * Final for speed.
     */
    @Override
    public final int compare (
                              ORDER left,
                              ORDER right
                              )
    {
        Comparison comparison = this.compareValues ( left, right );
        return comparison.comparatorValue ();
    }


    /**
     * @see musaico.foundation.io.Order#description()
     */
    @Override
    public final Internationalized<Message,String> description ()
    {
        return this.internationalizedDescription;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
                           Object obj
                           )
    {
        if ( obj == this )
        {
            return true;
        }
        else if ( obj == null
                  || obj.getClass () == this.getClass () )
        {
            return false;
        }
        else
        {
            // Same class, so same order.
            return true;
        }
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return this.description ().hashCode ();
    }


    /**
     * @see musaico.foundation.io.Order#sort(java.lang.Object[])
     */
    @Override
    public ORDER [] sort (
                          ORDER [] array
                          )
        throws I18nIllegalArgumentException
    {
        ORDER [] sorted_array = (ORDER [])
            Array.newInstance ( array.getClass ().getComponentType (),
                                array.length );
        Arrays.sort ( sorted_array, this );
        return sorted_array;
    }


    /**
     * @see musaico.foundation.io.Order#sort(java.util.Collection)
     */
    @Override
    public List<ORDER> sort (
                             Collection<ORDER> collection
                             )
        throws I18nIllegalArgumentException
    {
        List<ORDER> sorted_list = new ArrayList<ORDER> ( collection );
        Collections.sort ( sorted_list, this );
        return sorted_list;
    }
}
