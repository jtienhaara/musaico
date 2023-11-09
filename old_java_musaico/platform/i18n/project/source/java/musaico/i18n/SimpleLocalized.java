package musaico.i18n;


import java.io.Serializable;

import java.util.Locale;


/**
 * <p>
 * A basic localized representation of a particular class of objects
 * (such as Number, Time, Message, String, and so on).
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
 * Copyright (c) 2010 Johann Tienhaara
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
public class SimpleLocalized<INPUT extends Serializable,OUTPUT extends Serializable>
    implements Localized<INPUT,OUTPUT>, Serializable
{
    /** The international representation of this Localized. */
    private final INPUT internationalRepresentation;

    /** The localizer does the hard work for us (constructing the
     *  localized represenation of the international value by looking
     *  it up in a locale database). */
    private final Localizer<INPUT,OUTPUT> localizer;

    /** The locale to which the international representation has been
     *  localized. */
    private final Locale locale;

    /** The localized representation.  Null until someone asks us to
     *  look for it. */
    private OUTPUT localizedRepresentation;


    /**
     * <p>
     * Creates a new SimpleLocalized with the specified
     * international representation.
     * </p>
     *
     * <p>
     * When the <code> value () </code> method is called
     * on this Localized, the international representation
     * will be looked up in a locale database, and the
     * localized representation returned.
     * </p>
     *
     * @param international_representation The international representation
     *                                     to be localized.  Must not be null.
     * @param locale The locale to localize to (such as a country code,
     *               a language code, a timezone, or some combination,
     *               and so on).  Must not be null.
     * @param localizer The helper which does our hard work for us,
     *                  looking up the international representation in
     *                  a locale-specific database and constructing
     *                  the localized representation accordingly.
     *                  Must not be null.
     */
    public SimpleLocalized (
                            INPUT international_representation,
                            Locale locale,
                            Localizer<INPUT,OUTPUT> localizer
                            )
    {
        if ( international_representation == null )
        {
            throw new IllegalArgumentException ( "SimpleLocalized cannot be "
                                                 + "constructed with "
                                                 + "null international_representation"
                                                 + " ("
                                                 + "locale = " + locale
                                                 + " localizer = " + localizer
                                                 + ")" );
        }
        else if ( locale == null )
        {
            throw new IllegalArgumentException ( "SimpleLocalized cannot be "
                                                 + "constructed with "
                                                 + "null locale"
                                                 + " ("
                                                 + "international_representation = " + international_representation
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
                                                 + " locale = " + locale
                                                 + ")" );
        }

        this.internationalRepresentation = international_representation;
        this.localizer = localizer;
        this.locale = locale;
        this.localizedRepresentation = null;
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
                  || ! ( obj instanceof Localized ) )
        {
            return false;
        }

        Localized<?,?> that = (Localized<?,?>) obj;

        if ( this.locale ().equals ( that.locale () ) )
        {
            // Localize but avoid exceptions:
            return ( "" + this ).equals ( "" + that );
        }
        else
        {
            // Different locales.
            return false;
        }
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode ()
    {
        return this.internationalRepresentation.hashCode ()
            + this.locale ().hashCode ();
    }


    /**
     * @see musaico.i18n.Localized#internationalize()
     */
        public Internationalized<INPUT,OUTPUT> internationalize ()
    {
        // Create a new Internationalized each time.
        return new SimpleInternationalized<INPUT,OUTPUT> ( this.internationalRepresentation,
                                                           this.localizer );
    }


    /**
     * @see musaico.i18n.Localized#locale()
     */
    public Locale locale ()
    {
        return this.locale;
    }


    /**
     * @see musaico.i18n.Localized#value()
     */
    public OUTPUT value ()
        throws LocalizationException
    {
        // If we've never generated the localized representation,
        // do so now.
        if ( this.localizedRepresentation == null )
        {
            // Generate the localized representation.
            final OUTPUT localized_representation;
            try
            {
                localized_representation =
                    this.localizer.localize ( this.internationalRepresentation,
                                              this.locale );
            }
            catch ( Throwable t )
            {
                // Localizer blew up.  Throw an exception.
                throw new LocalizationException ( "Failed while localizing "
                                                  + this.internationalRepresentation
                                                  + " into locale '"
                                                  + this.locale
                                                  + "' using localizer "
                                                  + this.localizer,
                                                  t );
            }

            // Fail if no representation was found.
            if ( localized_representation == null )
            {
                throw new LocalizationException ( "Could not localize "
                                                  + this.internationalRepresentation
                                                  + " into locale '"
                                                  + this.locale
                                                  + "' using localizer "
                                                  + this.localizer );
            }

            // Now cache the localized representation in
            // case someone calls us again.
            this.localizedRepresentation = localized_representation;
        }

        // Now return the cached value.
        return this.localizedRepresentation;
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString ()
    {
        // !!! This is dangerous, making a blocking call
        // !!! on toString, but oh well.
        try
        {
            return "" + this.value ();
        }
        catch ( LocalizationException e )
        {
            return "Unlocalizable: " + this.internationalRepresentation;
        }
    }
}
