package musaico.i18n.localizers;


import java.io.Serializable;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;


import musaico.i18n.Internationalized;
import musaico.i18n.LocalizationException;
import musaico.i18n.Localizer;

import musaico.i18n.message.Message;


/**
 * <p>
 * Given a Message input value and a Locale, localizes the input to an
 * output String.
 * </p>
 *
 *
 * <p>
 * In Java, all Localizers must be serializable in order to play
 * nicely across RMI.
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
public class MessageLocalizer
    extends AbstractLocalizer<Message,String>
    implements Serializable
{
    /**
     * <p>
     * Creates a new MessageLocalizer with the specified parameters
     * as guides for loading Java resource bundles.
     * </p>
     *
     * @param resource_bundle_names The base name(s) of the resource bundle,
     *                              each a fully qualified class name
     *                              (such as "musaico.l10n.Messages"
     *                              or "musaico.l10n.TimeFormatters"
     *                              and so on).  Searched through in order
     *                              to find a resource bundle with the
     *                              right message.  Must not be null.
     * @param loader The class loader from which to load the
     *               resource bundle.  For example,
     *               <code> Thread.current ().getClassLoader () </code>.
     *               Must not be null.
     * @param source The control which gives information for the
     *               resource bundle loading process.  For example,
     *               <code> ResourceBundle.Control.FORMAT_DEFAULT </code>.
     *               Must not be null.
     */
    public MessageLocalizer (
                             String [] resource_bundle_names,
                             ClassLoader loader,
                             ResourceBundle.Control source
                             )
    {
        super ( resource_bundle_names,
                loader,
                source );
    }


    /**
     * @see musaico.i18n.localizers.AbstractLocalizer#getInternationalID(INPUT)
     */
    public String getInternationalID (
                                      Message international_input
                                      )
    {
        return international_input.id ();
    }


    /**
     * @see musaico.i18n.Localizer#localize(Message,Locale)
     */
    public String localize (
                            Message message,
                            Locale locale
                            )
        throws LocalizationException
    {
        // First get the localized String.
        String localized_text;
        try
        {
            localized_text = super.localize ( message, locale );
        }
        catch ( LocalizationException e )
        {
            // Don't ever fail to localize a Message.
            // Just its international text by default.
            localized_text = message.text ();
        }

        // Now for each parameter in the message, replace the
        // corresponding [%param%] in the localized message text.
        String [] parameter_names = message.parameters ();
        for ( int p = 0; p < parameter_names.length; p ++ )
        {
            Serializable parameter_value =
                message.parameter ( parameter_names [ p ] );

            // If the parameter is itself internationalized, localize
            // it too.  Hopefully no infinite loops...!
            if ( parameter_value instanceof Internationalized )
            {
                Internationalized<?,?> international_value =
                    (Internationalized<?,?>) parameter_value;
                parameter_value = international_value.localize ( locale );
            }

            // Now replace all the parameters in the text.
            localized_text =
                message.format ().parameter ( localized_text,
                                              parameter_names [ p ],
                                              parameter_value );
        }

        return localized_text;
    }
}
