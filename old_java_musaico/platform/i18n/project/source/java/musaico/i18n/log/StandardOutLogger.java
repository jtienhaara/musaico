package musaico.i18n.log;


import java.util.Locale;


import musaico.i18n.LocalizationException;
import musaico.i18n.Localized;

import musaico.i18n.message.Message;


/**
 * <p>
 * Provides an easy-to-use implementation for printing localized messages
 * to stdout.
 * </p>
 *
 * <p>
 * All messages logged through this interface are
 * Internationalized&lt;Message&gt;s, which means that they should
 * be placed in a localized text resource in the module calling
 * the <code> log ( ... ) </code> methods.  This paves the
 * way to localization in other languages.  However when first
 * experimenting with new code, the messages will be logged as-is,
 * without any need for localized resource bundles.
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
public class StandardOutLogger
    extends AbstractLogger
{
    /**
     * @see musaico.i18n.log.Logger#log(musaico.i18n.log.Level,musaico.i18n.Localized)
     */
    public void log (
                     Level log_level,
                     Localized<Message,String> l10n_message
                     )
    {
        String output_text;
        try
        {
            output_text = ""
                + log_level
                + ": "
                + l10n_message.value ();
        }
        catch ( LocalizationException le )
        {
            // Couldn't localize the message.
            // Oh well, spit it out as-is.
            output_text =
                "" + l10n_message.internationalize ().value ();
        }

        System.out.println ( output_text );
    }


    // AbstractLogger implements
    //     log ( Level, Locale, String, Object... )
    // Which is typically what people invoke.
}
