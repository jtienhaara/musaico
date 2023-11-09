package musaico.i18n.message;


import java.io.Serializable;


/**
 * <p>
 * Represents a message (such as an error message, a GUI pop-up
 * dialog message, and so on).
 * </p>
 *
 *
 * <p>
 * In Java, every Message must be Serializable in order to play
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
public interface Message
    extends Serializable
{
    /**
     * <p>
     * Returns the format of the text for the message.
     * </p>
     *
     * <p>
     * Parameters can be embedded using delimiters.  The
     * MessageFormat tells callers how to replace the delimited
     * parameter sections with parameter values.
     * </p>
     *
     * @return The format of this Message.  Never null.
     */
    public abstract MessageFormat format ();


    /**
     * <p>
     * Returns the identifier for this message
     * (possibly the internationalized message text,
     * or its hash, and so on).
     * </p>
     *
     * @return The unique identifier for this message.
     *         Never null.
     */
    public abstract String id ();


    /**
     * <p>
     * Returns the text for this message.
     * </p>
     *
     * <p>
     * Parameter placeholders may be embedded in the message.
     * </p>
     *
     * @return The base String for this message.
     */
    public abstract String text ();


    /**
     * <p>
     * Returns the parameter value for the specified name.
     * </p>
     *
     * @param name The name of the parameter to return.
     *             Must not be null.
     *
     * @return The value of the specified parameter.
     *         Can be null.
     */
    public abstract Serializable parameter (
                                            String name
                                            );


    /**
     * <p>
     * Returns the names of the parameters included in this message.
     * </p>
     *
     * @return The parameter names in this message.
     *         Never null.  May be zero-length.
     */
    public abstract String [] parameters ();


    /**
     * <p>
     * Builds or returns the String representation of this Message.
     * </p>
     *
     * @return The String representation of this message.
     *         Never null.
     */
    public abstract String toString ();
}
