package musaico.i18n.log;


import java.util.Locale;


import musaico.i18n.Localized;

import musaico.i18n.message.Message;


/**
 * <p>
 * Provides an easy-to-use interface for printing localized messages
 * (whether as logs, email alerts, and so on).
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
 * Copyright (c) 2009, 2010 Johann Tienhaara
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
public interface Logger
{
    /**
     * <p>
     * Writes an Localized&lt;Message&gt; to the target device.
     * </p>
     *
     * @param log_level The level of logging for the message.
     *                  For example, Level.ERROR or Level.FATAL or
     *                  Level.WARNING or Level.INFO or Level.DEBUG
     *                  or some custom Level.
     *
     * @param i18n_message The localized message to log.  Can be produced
     *                     from an Internationalized&lt;Message&gt;
     *                     (including any Musaico exception) by calling
     *                     its <code> localize ( Locale ) </code>
     *                     method.  Must not be null.
     */
    public abstract void log (
                              Level log_level,
                              Localized<Message,String> l10n_message
                              );


    /**
     * <p>
     * Writes a localized message to the target device.
     * </p>
     *
     * @param log_level The level of logging for the message.
     *                  For example, Level.ERROR or Level.FATAL or
     *                  Level.WARNING or Level.INFO or Level.DEBUG
     *                  or some custom Level.
     *
     * @param locale The locale to log the internationalized message in.
     *               Can be <code> Locale.getDefault () </code>, or a
     *               specific locale for a user, and so on.  Must not be
     *               null.
     *
     * @param i18n_message_text The international message to log.  The
     *                          message will be localized if a localized
     *                          version exists.  Otherwise it will be
     *                          logged in its international form.  Can
     *                          contain "[%parameter%]" references to
     *                          the parameter_value_pairs passed in.
     *                          Must not be null.
     *
     * @param parameter_value_pairs 0 or more pairs of parameter, values.
     *                              Each parameter String can appear in
     *                              the localized message text and will be
     *                              replaced with the specified value.
     *                              For example, if the pair
     *                              <code> "cause", new Exception () </code>
     *                              is passed, then if "[%cause%]" appears
     *                              in the localized message text, it
     *                              will be replaced with the String
     *                              representation of the exception.
     *                              Must be a multiple of 2 in length
     *                              (0, 2, 4, ...).  No parameter may be
     *                              null, though values may be null.
     */
    public abstract void log (
                              Level log_level,
                              Locale locale,
                              String i18n_message_text,
                              Object ... parameter_value_pairs
                              );
}
