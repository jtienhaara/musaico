package musaico.kernel.objectsystem.onode;

import java.io.Serializable;


import musaico.i18n.Internationalized;

import musaico.i18n.message.Message;

import musaico.kernel.objectsystem.RecordOperationException;


/**
 * <p>
 * An exception thrown by an ONode operation, due to security violation
 * or I/O problems and so on.
 * </p>
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
 * Copyright (c) 2009, 2010, 2011 Johann Tienhaara
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
public class ONodeOperationException
    extends RecordOperationException
    implements Serializable
{
    /**
     * <p>
     * Creates a new ONodeOperationException with the specified
     * internationalized Message.
     * </p>
     *
     * @param i18n_message The internationalized Message for this
     *                     ONodeOperationException.  The localized Message
     *                     will be looked up by Locale when requested.
     *                     Must not be null.
     */
    public ONodeOperationException (
                                    Internationalized<Message,String> i18n_message
                                    )
    {
        super ( i18n_message );
    }


    /**
     * <p>
     * Creates a new ONodeOperationException with the specified Message.
     * </p>
     *
     * @param message The Message for this ONodeOperationException.
     *                The localized Message will be looked up by Locale
     *                when requested.  Must not be null.
     */
    public ONodeOperationException (
                                    Message message
                                    )
    {
        super ( message );
    }


    /**
     * <p>
     * Creates a new ONodeOperationException with the specified String text.
     * </p>
     *
     * @param text The text for this ONodeOperationException.
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
    public ONodeOperationException (
                                    String text,
                                    Object ... parameters
                                    )
    {
        super ( text, parameters );
    }
}
