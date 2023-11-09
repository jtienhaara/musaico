package musaico.i18n.text;

import java.io.Serializable;


import musaico.i18n.Internationalized;
import musaico.i18n.Localized;
import musaico.i18n.SimpleInternationalized;

import musaico.i18n.exceptions.L10n;

import musaico.i18n.message.Message;
import musaico.i18n.message.MessageFormat;
import musaico.i18n.message.SimpleMessageBuilder;


/**
 * <p>
 * An easy-to-use internationalized text string, which implements
 * <code> Internationalized<Message> </code>.
 * </p>
 *
 * <p>
 * In Java every I18nString must be Serializable in order to play nicely
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
public class I18nString
    extends SimpleInternationalized<Message,String>
    implements Serializable
{
    /** The name of the Message parameter which specifies where to
     *  find the message bundle for the Message (such as "x.y.z.Messages"
     *  or { "a.b.c.Messages", "x.y.z.Messages", ... }).
     *  Any Message which does not provide this parameter shall induce
     *  a search for an appropriate message bundle.  Each message bundle
     *  is a Java message bundle with a name like "x.y.z.Messages.properties"
     *  or "x.y.z.Messages_en.properties" or
     *  "x.y.z.Messages.en_CA.properties" and so on. */
    public static final String MESSAGE_BUNDLE_PARAMETER =
        L10n.MESSAGE_BUNDLE_PARAMETER;


    /**
     * <p>
     * Creates a new I18nString with the specified String internationalized
     * text and (String, Object) parameter pairs.
     * </p>
     *
     * @param i18n_text The internationalized text for this I18nString.
     *                  The localized Message will be looked up by Locale
     *                  when requested.  Can contain [%param%] parameter
     *                  placeholders.  Must not be null.
     *
     * @param parameters The (name, value) pairs of parameters.
     *                   The first Object is the name of the parameter
     *                   (a String), the second is the value.  Values
     *                   can be null.  If an odd number of parameter
     *                   arguments is passed, the value of the last
     *                   parameter will default to null.  Can be null
     *                   for no parameters.
     */
    public I18nString (
                       String i18n_text,
                       Object... parameters
                       )
    {
        this ( new SimpleMessageBuilder ()
               .text ( i18n_text )
               .parameters ( parameters )
               .build () );
    }


    /**
     * <p>
     * Creates a new I18nString with the specified String MessageFormat
     * (overriding the default which treats "[%xyz%]" as a parameter
     * inside the internationalized text), the specified internationalized
     * text and (String, Object) parameter pairs.
     * </p>
     *
     * @param message_format The format parser for the i18n_text,
     *                       which is used to match parameters inside
     *                       the i18n_text.  Must not be null.
     *
     * @param i18n_text The internationalized text for this I18nString.
     *                  The localized Message will be looked up by Locale
     *                  when requested.  Can contain [%param%] parameter
     *                  placeholders.  Must not be null.
     *
     * @param parameters The (name, value) pairs of parameters.
     *                   The first Object is the name of the parameter
     *                   (a String), the second is the value.  Values
     *                   can be null.  If an odd number of parameter
     *                   arguments is passed, the value of the last
     *                   parameter will default to null.  Can be null
     *                   for no parameters.
     */
    public I18nString (
                       MessageFormat message_format,
                       String i18n_text,
                       Object... parameters
                       )
    {
        this ( new SimpleMessageBuilder ()
               .format ( message_format )
               .text ( i18n_text )
               .parameters ( parameters )
               .build () );
    }


    /**
     * <p>
     * Creates a new I18nString with the specified international Message.
     * </p>
     *
     * @param i18n_message The international Message for this I18nString.
     *                     The localized Message will be looked up by Locale
     *                     when requested.  Must not be null.
     */
    public I18nString (
                       Message message
                       )
    {
        // We don't have a Class for the context in which this is being
        // called, so we just use I18nString.class, and let L10n look
        // through the stack for an x.y.z.Messages bundle name.
        super ( message,
                L10n.messageLocalizer ( L10n.messageBundleNames ( message, I18nString.class ) ) );
    }
}
