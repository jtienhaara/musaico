package musaico.i18n.message;


import java.io.Serializable;

import java.util.Arrays;


/**
 * <p>
 * Standard Mesaage implementation, representing a message
 * (such as an error message, a GUI pop-up dialog message, and so on).
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
 * Copyright (c) 2010, 2011 Johann Tienhaara
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
public class SimpleMessage
    implements Message, Serializable
{
    /** The id of this Message. */
    private final String id;

    /** The template text of this Message. */
    private final String text;

    /** The format of the message. */
    private final MessageFormat format;

    /** The parameter names.  Must be same length as parameter values. */
    private final String [] parameterNames;

    /** The parameter values.  Must be same length as parameter names. */
    private final Serializable [] parameterValues;

    /** The formatted Message.  Only non-null once toString () has been
     *  called. */
    private String formattedMessage;


    /**
     * <p>
     * Creates a new SimpleMessage.
     * </p>
     */
    public SimpleMessage (
                          String id,
                          String text,
                          MessageFormat format,
                          String [] parameter_names,
                          Serializable [] parameter_values
                          )
    {
        if ( id == null
             || text == null
             || format == null
             || parameter_names == null
             || parameter_values == null
             || parameter_values.length != parameter_names.length )
        {
            throw new IllegalArgumentException ( "Cannot create a "
                                                 + "SimpleMessage with"
                                                 + " id " + id
                                                 + " text " + text
                                                 + "format " + format
                                                 + " parameter_names " + parameter_names
                                                 + " parameter_values " + parameter_values );
        }

        this.id = id;
        this.text = text;
        this.format = format;
        this.parameterNames = parameter_names;
        this.parameterValues = parameter_values;
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
                  || ! ( obj instanceof Message ) )
        {
            return false;
        }

        Message that = (Message) obj;
        if ( ! this.id ().equals ( that.id () )
             || ! this.text ().equals ( that.text () ) )
        {
            return false;
        }

        String [] this_parameters = this.parameters ();
        String [] that_parameters = that.parameters ();
        if ( this_parameters.length != that_parameters.length )
        {
            return false;
        }

        Arrays.sort ( this_parameters );
        Arrays.sort ( that_parameters );

        for ( int p = 0; p < this_parameters.length; p ++ )
        {
            if ( ! this_parameters [ p ].equals ( that_parameters [ p ] ) )
            {
                return false;
            }

            Serializable this_parameter_value =
                this.parameter ( this_parameters [ p ] );
            Serializable that_parameter_value =
                that.parameter ( that_parameters [ p ] );

            if ( this_parameter_value == null )
            {
                if ( that_parameter_value != null )
                {
                    return false;
                }
            }
            else if ( ! this_parameter_value.equals ( that_parameter_value ) )
            {
                return false;
            }
        }

        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode ()
    {
        return this.id ().hashCode ();
    }


    /**
     * @see musaico.message.Message#format()
     */
    public MessageFormat format()
    {
        return this.format;
    }


    /**
     * @see musaico.message.Message#id()
     */
    public String id ()
    {
        return this.id;
    }


    /**
     * @see musaico.message.Message#text()
     */
    public String text ()
    {
        return this.text;
    }


    /**
     * @see musaico.message.Message#parameter(String)
     */
    public Serializable parameter (
                                   String name
                                   )
    {
        // Slow and stupid.
        for ( int p = 0; p < this.parameterNames.length; p ++ )
        {
            if ( this.parameterNames [ p ].equals ( name ) )
            {
                // Prevent code from stealing protected objects
                // by catching exceptions with the ""+ prefix:
                return "" + this.parameterValues [ p ];
            }
        }

        // No such parameter.
        return null;
    }


    /**
     * @see musaico.message.Message#parameters()
     */
    public String [] parameters ()
    {
        return this.parameterNames;
    }


    /**
     * @see musaico.message.Message#toString()
     */
    public String toString ()
    {
        if ( this.formattedMessage != null )
        {
            return this.formattedMessage;
        }

        // Construct the final message.
        // In a stupid, slow fashion.  :(
        String formatted_message = this.text;
        for ( int p = 0; p < this.parameterNames.length; p ++ )
        {
            formatted_message =
                this.format ().parameter ( formatted_message,
                                           parameterNames [ p ],
                                           this.parameterValues [ p ] );
        }

        this.formattedMessage = formatted_message;

        return this.formattedMessage;
    }
}
