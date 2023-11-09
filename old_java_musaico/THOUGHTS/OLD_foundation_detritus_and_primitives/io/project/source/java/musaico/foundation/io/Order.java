package musaico.foundation.io;


import java.io.Serializable;

import java.util.Comparator;
import java.util.Collection;
import java.util.List;


import musaico.foundation.i18n.Internationalized;

import musaico.foundation.i18n.exceptions.I18nIllegalArgumentException;

import musaico.foundation.i18n.message.Message;

import musaico.foundation.time.Time;


/**
 * <p>
 * A specific order for comparing objects of a specific type,
 * such as a dictionary order or a character encoding (ASCII etc)
 * order for comparing Strings.
 * </p>
 *
 * <p>
 * Every Order must provide an internationalized description message,
 * as well as localized representations of the same.  For example,
 * the dictionary order might be described as
 * "Alphanumeric dictionary order" in English, and something
 * else (!) in French.
 * </p>
 *
 *
 * <p>
 * In Java, every Order can be used as a Java Comparator.
 * </p>
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
public interface Order<ORDER extends Object>
    extends Comparator<ORDER>, Serializable
{
    /** Compares Strings by their character encoding values (ASCII etc). */
    public static final Order<String> CHARACTER_ENCODING =
        new CharacterEncodingOrder ();

    /** Compares Strings by their dictionary values (only considering
     *  spaces and alphanumeric characters, ignoring case). */
    public static final Order<String> DICTIONARY =
        new DictionaryOrder ();

    /** Compares numbers. */
    public static final Order<Number> NUMERIC =
        new NumericOrder ();

    /** Compares times. */
    public static final Order<Time> TIME =
        new TimeOrder ();


    // Every Order must implement Comparator.compare ().

    /**
     * <p>
     * Compares the two order values, and returns the
     * comparison result (Comparison.LEFT_LESS_THAN_RIGHT,
     * Comparison,LEFT_EQUALS_RIGHT, and so on).
     * </p>
     *
     * @param left The left ORDER to compare.  Must not be null.
     *
     * @param right The right ORDER to compare.  Must not be null.
     *
     * @return The comparison of left ORDER to right ORDER values.
     *         Never null.
     */
    public abstract Comparison compareValues (
                                              ORDER left,
                                              ORDER right
                                              );


    /**
     * <p>
     * Returns the internationalized description of this Order.
     * </p>
     *
     * <p>
     * Can be localized in order to display to users.
     * </p>
     *
     * @return This Order's internationalized description.
     *         Never null.
     */
    public abstract Internationalized<Message,String> description ();


    /**
     * <p>
     * Creates a copy of the specified array, sorts it according
     * to this Order, and returns the new sorted array.
     * </p>
     *
     * @param array The array of items to sort.  Must not be null.
     *              Must not contain any null elements.
     *
     * @return A new copy of the array which has been sorted according
     *         to this Order.  Never null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public abstract ORDER [] sort (
                                   ORDER [] array
                                   )
        throws I18nIllegalArgumentException;


    /**
     * <p>
     * Creates a copy of the specified Collection, sorts it according
     * to this Order, and returns the new sorted List.
     * </p>
     *
     * @param collection The collection of items to sort.
     *                   Must not be null. Must not contain
     *                   any null elements.
     *
     * @return A new List containing all the elements of the
     *         Collection, which has been sorted according
     *         to this Order.  Never null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public abstract List<ORDER> sort (
                                      Collection<ORDER> collection
                                      )
        throws I18nIllegalArgumentException;
}
