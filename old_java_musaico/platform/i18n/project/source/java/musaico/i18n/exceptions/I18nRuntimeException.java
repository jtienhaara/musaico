package musaico.i18n.exceptions;


import java.io.Serializable;

import java.util.Locale;


import musaico.i18n.Internationalized;
import musaico.i18n.LocalizationException;
import musaico.i18n.Localized;
import musaico.i18n.Localizer;
import musaico.i18n.SimpleInternationalized;

import musaico.i18n.message.Message;
import musaico.i18n.message.SimpleMessageBuilder;


/**
 * <p>
 * A runtime exception which has an internationalized message
 * token, localized versions, and international parameters.
 * </p>
 *
 * <p>
 * To specify a Throwable cause for an I18nRuntimeException, add
 * it to the Message passed to the constructor.  For example:
 * </p>
 *
 * <pre>
 *     try
 *     {
 *         ...
 *     }
 *     catch ( MyException ex )
 *     {
 *         // These approaches are equivalent:
 *         new I18nRuntimeException ( new SimpleMessageBuilder ()
 *                                    .text ( "Message text" )
 *                                    .parameter ( "cause", ex )
 *                                    .build () );
 *         new I18nRuntimeException ( "Message text",
 *                                    "cause", ex );
 *     }
 * </pre>
 *
 *
 * <p>
 * Every Musaico exception must extend one of the internationalized
 * Exceptions, such as I18nException,in order for the message
 * text to be localizable.  Default English message text should
 * be placed in Messages.properties in the same package as the
 * Exception class; or Messages_(locale).properties can be created
 * first and the defaults translated later.  Note that Java currently
 * only handles old-fashioned 2-character ISO-639-1 language
 * codes, not the more recent and more complete 3-character codes.
 * </p>
 *
 * <p>
 * Example of usage for a typical Musaico exception:
 * </p>
 *
 * <pre>
 *     // Throw a "Something really bad..." exception and provide
 *     // the parameter values as well as the root cause Throwable.
 *     throw new XYZException ( "Something really bad happened while doing xyz to [%param1%] and [%param2%]",
 *                              "param1", param1,
 *                              "param2", param2,
 *                              "cause", another_exception );
 * </pre>
 *
 * <p>
 * The corresponding default Messages.properties file might contain
 * an entry like:
 * </p>
 *
 * <p>
 *     Something\ really\ bad\ happened\ while\ doing\ xyz\ to\ [%param1%]\ and\ [%param2%] = Something really bad happened while doing xyz to [%param1%] and [%param2%]
 * </p>
 *
 * <p>
 * And a user in the default locale might expect to see output something like:
 * </p>
 *
 * <pre>
 *     Something really bad happened while doing xyz to 1 and 2
 * </pre>
 *
 * <p>
 * Or a user in another locale might expect to see a translated
 * message:
 * </p>
 *
 * <pre>
 *     Ca, ce n'est pas un bon translation!  2 n'est pas 1!
 * </pre>
 *
 * <p>
 * In Java, every Musaico exception must be Serializable, in order
 * to play nicely across RMI calls.
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
public class I18nRuntimeException
    extends RuntimeException
    implements Internationalized<Message,String>, Serializable
{
    /** The Message for this I18nRuntimeException.  Can be localized. */
    private final Internationalized<Message,String> i18nMessage;


    /**
     * <p>
     * Creates a new I18nRuntimeException with the specified internationalized
     * Message.
     * </p>
     *
     * @param i18n_message The internationalized Message for this
     *                     I18nRuntimeException.  The localized Message
     *                     will be looked up by Locale when requested.
     *                     Must not be null.
     */
    public I18nRuntimeException (
                                 Internationalized<Message,String> i18n_message
                                 )
    {
        if ( i18n_message == null )
        {
            // Don't throw exceptions while creating exceptions.
            Message message =
                new SimpleMessageBuilder ()
                .text ( "No message specified while constructing [%class%]" )
                .parameter ( "class", this.getClass () )
                .build ();
            String [] bundles_names =
                L10n.messageBundleNames ( this.getClass () );
            i18n_message =
                new SimpleInternationalized<Message,String> ( message,
                                                              L10n.messageLocalizer ( bundles_names ) );
        }

        this.i18nMessage = i18n_message;

        Object cause_object = this.i18nMessage.value ().parameter ( "cause" );
        if ( cause_object != null
             && ( cause_object instanceof Throwable ) )
        {
            this.initCause ( (Throwable) cause_object );
        }
    }


    /**
     * <p>
     * Creates a new I18nRuntimeException with the specified Message.
     * </p>
     *
     * @param message The Message for this I18nRuntimeException.
     *                The localized Message will be looked up by Locale
     *                when requested.  Must not be null.
     */
    public I18nRuntimeException (
                                 Message message
                                 )
    {
        if ( message == null )
        {
            // Don't throw exceptions while creating exceptions.
            message =
                new SimpleMessageBuilder ()
                .text ( "No message specified while constructing [%class%]" )
                .parameter ( "class", this.getClass () )
                .build ();
        }

        this.i18nMessage = L10n.internationalize ( this, message );

        Object cause_object = this.i18nMessage.value ().parameter ( "cause" );
        if ( cause_object != null
             && ( cause_object instanceof Throwable ) )
        {
            this.initCause ( (Throwable) cause_object );
        }
    }


    /**
     * <p>
     * Creates a new I18nRuntimeException with the specified String text.
     * </p>
     *
     * @param text The text for this I18nRuntimeException.
     *             The localized Message will be looked up by Locale
     *             when requested.  Can contain [%param%] parameter
     *             placeholders.  Must not be null.
     *
     * @param parameters The (name, value) pairs of parameters.
     *                   The first Object is the name of the parameter
     *                   (a String), the second is the value.  Values
     *                   can be null.  If an odd number of parameter
     *                   arguments is passed, the value of the last
     *                   parameter will default to null.  Can be null
     *                   for no parameters.
     */
    public I18nRuntimeException (
                                 String text,
                                 Object ... parameters
                                 )
    {
        final Message message;
        if ( text == null )
        {
            // Don't throw exceptions while creating exceptions.
            message =
                new SimpleMessageBuilder ()
                .text ( "No message specified while constructing [%class%]" )
                .parameter ( "class", this.getClass () )
                .build ();
        }
        else if ( parameters == null
                  || parameters.length == 0 )
        {
            message =
                new SimpleMessageBuilder ()
                .text ( text )
                .parameter ( "stack_trace",
                             this.getStackTrace () )
                .build ();
        }
        else
        {
            final SimpleMessageBuilder message_builder =
                new SimpleMessageBuilder ()
                .text ( text )
                .parameters ( parameters );

            if ( message_builder.hasParameter ( "stack_trace" ) == false )
            {
                message_builder.parameter ( "stack_trace",
                                            this.getStackTrace () );
            }

            message = message_builder.build ();
        }

        this.i18nMessage = L10n.internationalize ( this, message );

        Object cause_object = this.i18nMessage.value ().parameter ( "cause" );
        if ( cause_object != null
             && ( cause_object instanceof Throwable ) )
        {
            this.initCause ( (Throwable) cause_object );
        }
    }


    /**
     * @see musaico.i18n.Internationalized#localize(Locale)
     */
    public Localized<Message,String> localize (
                                               Locale locale
                                               )
    {
        return this.i18nMessage.localize ( locale );
    }


    /**
     * <p>
     * Returns the underlying message.
     * </p>
     */
    public Message message ()
    {
        return this.i18nMessage.value ();
    }


    /**
     * <p>
     * Returns a localized String version of this exception.
     * Will not throw localized exceptions.
     * </p>
     */
    public String toString (
                            Locale locale
                            )
    {
        try
        {
            return this.localize ( locale ).value ();
        }
        catch ( LocalizationException e )
        {
            return this.i18nMessage.value ().toString ();
        }
    }


    /**
     * <p>
     * Returns an internationalized String version of this exception.
     * </p>
     */
    public String toString ()
    {
        return this.i18nMessage.value ().toString ();
    }


    /**
     * @see musaico.i18n.Internationalized#value()
     */
    public Message value ()
    {
        return this.message ();
    }
}
