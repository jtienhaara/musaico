package musaico.i18n;


import java.io.Serializable;

import java.util.Locale;


/**
 * <p>
 * A simple implementation of the internationalized representation.
 * </p>
 *
 * <p>
 * The Internationalized knows how to build Localized versions
 * of itself (each of which, in turn, knows how to localize the
 * representation for its own locale).
 * </p>
 *
 * <pre>
 *     Internationalized &lt;X&gt;  --&gt;  Localized &lt;X&gt;
 *     + value (): X                        + value (): X
 *       Returns the international X          Returns the localized X
 * </pre>
 *
 *
 * <p>
 * In Java, every Localized must be Serializable in order to play
 * nicely over RMI.
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
 * Copyright (c) 2010, 2012 Johann Tienhaara
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
public class SimpleInternationalized<INPUT extends Serializable,OUTPUT extends Serializable>
    implements Internationalized<INPUT,OUTPUT>, Serializable
{
    /** The international representation of this Internationalized. */
    private final INPUT internationalRepresentation;

    /** The localizer does the hard work for our Localizeds (constructing the
     *  localized represenation of the international value by looking
     *  it up in a locale database). */
    private final Localizer<INPUT,OUTPUT> localizer;


    /**
     * <p>
     * Creates a new SimpleInternationalized with the specified
     * international representation.
     * </p>
     *
     * @param international_representation The international representation.
     *                                     Must not be null.
     * @param localizer The helper which does our Localizeds' hard work,
     *                  looking up the international representation in
     *                  a locale-specific database and constructing
     *                  the localized representation accordingly.
     *                  Must not be null.
     */
    public SimpleInternationalized (
                                    INPUT international_representation,
                                    Localizer<INPUT,OUTPUT> localizer
                                    )
    {
        if ( international_representation == null )
        {
            throw new IllegalArgumentException ( "SimpleLocalized cannot be "
                                                 + "constructed with "
                                                 + "null international_representation"
                                                 + " ("
                                                 + " localizer = " + localizer
                                                 + ")" );
        }
        else if ( localizer == null )
        {
            throw new IllegalArgumentException ( "SimpleLocalized cannot be "
                                                 + "constructed with "
                                                 + "null localizer"
                                                 + " ("
                                                 + "international_representation = " + international_representation
                                                 + ")" );
        }

        this.internationalRepresentation = international_representation;
        this.localizer = localizer;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals (
                           Object obj
                           )
    {
        if ( obj == this )
        {
            return true;
        }
        else if ( obj == null
                  || ! ( obj instanceof Internationalized ) )
        {
            return false;
        }

        Internationalized<?,?> that =
            (Internationalized<?,?>) obj;
        return this.value ().equals ( that.value () );
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode ()
    {
        return this.value ().hashCode ();
    }


    /**
     * @see musaico.i18n.Internationalized.localize(Locale)
     */
    public Localized<INPUT,OUTPUT> localize (
                                             Locale locale
                                             )
    {
        return new SimpleLocalized<INPUT,OUTPUT> ( this.internationalRepresentation,
                                                   locale,
                                                   this.localizer );
    }


    /**
     * @see musaico.i18n.Internationalized.value()
     */
    public INPUT value ()
    {
        return this.internationalRepresentation;
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString ()
    {
        return "" + this.internationalRepresentation;
    }
}
