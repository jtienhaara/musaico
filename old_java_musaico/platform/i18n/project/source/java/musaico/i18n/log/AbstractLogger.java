package musaico.i18n.log;


import java.util.Locale;


import musaico.i18n.Internationalized;
import musaico.i18n.Localized;

import musaico.i18n.exceptions.L10n;

import musaico.i18n.message.Message;
import musaico.i18n.message.SimpleMessageBuilder;


/**
 * <p>
 * Provides the boilerplate code for the most frequently used
 * API method of Logger.  A logger implementation thus has to
 * only provide one method if it extends this class,
 * <code> log ( Level, Localized&lt;Message,String&gt; ) </code>.
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
public abstract class AbstractLogger
    implements Logger
{
    // Every AbstractLogger must implement
    //     log ( Level, Localized<Message,String> ).


    /**
     * @see musaico.i18n.log.Logger#log(musaico.i18n.log.Level,java.util.Locale,java.lang.String,java.lang.Object...)
     */
    public void log (
                     Level log_level,
                     Locale locale,
                     String i18n_message_text,
                     Object ... parameter_value_pairs
                     )
    {
        Message message =
            new SimpleMessageBuilder ()
            .text ( i18n_message_text )
            .parameters ( parameter_value_pairs )
            .build ();
        Internationalized<Message,String> i18n_message =
            L10n.internationalize ( this /*should be caller object */,
                                    message );
        Localized<Message,String> l10n_message =
            i18n_message.localize ( locale );

        this.log ( log_level, l10n_message );
    }
}
