package musaico.i18n.message;


import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * Builds SimpleMessages, each one representing a message
 * (such as an error message, a GUI pop-up dialog message, and so on).
 * </p>
 *
 * <p>
 * This builder is NOT thread-safe.
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
public class SimpleMessageBuilder
    implements Serializable
{
    /** The id for the Message to build. */
    private String messageID;

    /** The text for the Message to build. */
    private String messageText;

    /** The format of the Message to build.  Default uses [%param%]. */
    private MessageFormat messageFormat = new StandardMessageFormat ();

    /** The parameter names for the Message to build. */
    private final List<String> messageParameters = new ArrayList<String> ();

    /** The values of the parameters for the Message to build. */
    private final Map<String,Serializable> messageParameterValues =
        new HashMap<String,Serializable> ();


    /**
     * <p>
     * Sets the format for the Message to be built.
     * </p>
     *
     * <p>
     * The default format uses [%param%] as a placeholder
     * for the parameter named "param".
     * </p>
     *
     * @param format The format of Message.  Must not be null.
     *
     * @return This SimpleMessageBuilder.
     */
    public SimpleMessageBuilder format (
                                        MessageFormat format
                                        )
    {
        if ( format == null )
        {
            throw new IllegalArgumentException ( "Cannot build Message with "
                                                 + "null format" );
        }

        this.messageFormat = format;

        return this;
    }


    /**
     * <p>
     * Sets the identifier for the Message to be built.
     * </p>
     */
    public SimpleMessageBuilder id (
                                    String id
                                    )
    {
        if ( id == null )
        {
            throw new IllegalArgumentException ( "Cannot build Message with "
                                                 + "null id" );
        }

        this.messageID = id;

        if ( this.messageText == null )
        {
            this.messageText = this.messageID;
        }

        return this;
    }


    /**
     * <p>
     * Sets the text for the Message to be built.
     * </p>
     */
    public SimpleMessageBuilder text (
                                      String text
                                      )
    {
        if ( text == null )
        {
            throw new IllegalArgumentException ( "Cannot build Message with "
                                                 + "null text" );
        }

        this.messageText = text;

        if ( this.messageID == null )
        {
            this.messageID = this.messageText;
        }

        return this;
    }


    /**
     * <p>
     * Returns the serializable value of the specified parameter in
     * this builder, or null if it does not exist.
     * </p>
     */
    public Serializable getParameter (
                                      String name
                                      )
    {
        if ( name == null )
        {
            return null;
        }

        return this.messageParameterValues.get ( name );
    }


    /**
     * <p>
     * Returns true if the specified parameter has been added
     * to this message builder, false if it does not exist in
     * this message builder.
     * </p>
     */
    public boolean hasParameter (
                                 String name
                                 )
    {
        if ( name == null )
        {
            return false;
        }

        return this.messageParameterValues.containsKey ( name );
    }


    /**
     * <p>
     * Adds a parameter to the Message to be built.
     * </p>
     *
     * <p>
     * The unserialized_parameter is converted to a String if
     * does not implement the Serializable interface, in order
     * to play nicely across RMI.
     * </p>
     *
     * <p>
     * Mutable parameters should be converted to immutable
     * ones before calling this method.  For example, rather
     * than passing in a mutable Java Date object, convert
     * it to an immutable Time object and pass that into the builder.
     * </p>
     *
     * <p>
     * The unserialized value of the parameter can be null.
     * </p>
     */
    public SimpleMessageBuilder parameter (
                                           String name,
                                           Object unserialized_value
                                           )
    {
        if ( name == null )
        {
            throw new IllegalArgumentException ( "Cannot build Message with "
                                                 + "null parameter name" );
        }

        final Serializable serialized_value;
        if ( unserialized_value == null )
        {
            serialized_value = (Serializable) null;
        }
        else if ( unserialized_value instanceof Serializable )
        {
            serialized_value = (Serializable) unserialized_value;
        }
        else
        {
            // Cast to (Serializable) String.
            serialized_value = "" + unserialized_value;
        }

        this.messageParameters.add ( name );
        this.messageParameterValues.put ( name, serialized_value );
        return this;
    }


    /**
     * <p>
     * Adds the specified <code> parameter, value </code> pairs to the
     * message builder.
     * </p>
     *
     * @param parameter_value_pairs 0 or more pairs of parameter, values.
     *                              For example, if the pair
     *                              <code> "cause", new Exception () </code>
     *                              is passed, then if "[%cause%]" appears
     *                              in the message, it will be replaced
     *                              with the String representation of the
     *                              exception.
     *                              Must be a multiple of 2 in length
     *                              (0, 2, 4, ...).  No parameter may be
     *                              null, though values may be null.
     */
    public SimpleMessageBuilder parameters (
                                            Object ... parameter_value_pairs
                                            )
    {
        if ( parameter_value_pairs == null
             || parameter_value_pairs.length == 0 )
        {
            return this;
        }

        for ( int p = 0; p < parameter_value_pairs.length; p += 2 )
        {
            final String name = "" + parameter_value_pairs [ p ];
            final Object value;
            if ( ( p + 1 ) < parameter_value_pairs.length )
            {
                value = parameter_value_pairs [ p + 1 ];
            }
            else
            {
                value = null;
            }

            this.parameter ( name, value );
        }

        return this;
    }


    /**
     * <p>
     * Builds the message.
     * </p>
     *
     * @throws IllegalStateException If the builder does not have enough
     *                               information (id, text, and so on)
     *                               to build the Message.
     */
    public Message build ()
    {
        // Convert the parameter names to an array of Strings.
        final String [] message_parameters =
            new String [ this.messageParameters.size () ];
        this.messageParameters.toArray ( message_parameters );

        // Convert the parameter values to an array of Serializables.
        Serializable [] message_values =
            new Serializable [ message_parameters.length ];
        for ( int p = 0; p < message_parameters.length; p ++ )
        {
            message_values [ p ] =
                this.messageParameterValues.get ( message_parameters [ p ] );
        }

        return new SimpleMessage ( this.messageID,
                                   this.messageText,
                                   this.messageFormat,
                                   message_parameters,
                                   message_values );
    }
}
