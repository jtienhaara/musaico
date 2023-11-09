package musaico.i18n.message;

import java.io.Serializable;

import java.lang.reflect.Array;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * <p>
 * The format of messages in the resource bundles.
 * </p>
 *
 * <p>
 * This implementation allows parameters matching [%param%].
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
public class StandardMessageFormat
    implements MessageFormat, Serializable
{
    /** The open delimiter for parsing parameters in the text template. */
    public static final String OPEN_PARAM = "[%";
    public static final String OPEN_PARAM_REGEX = Pattern.quote ( OPEN_PARAM );

    /** The close delimiter for parsing parameters in the text template. */
    public static final String CLOSE_PARAM = "%]";
    public static final String CLOSE_PARAM_REGEX = Pattern.quote ( CLOSE_PARAM );


    /**
     * @see musaico.i18n.message.MessageFormat#parameter(String,String,Serializable)
     */
    public String parameter (
                             String text,
                             String parameter_name,
                             Serializable parameter_value
                             )
    {
        // Replace arrays with details, max 16 elements.
        if ( parameter_value != null
             && parameter_value.getClass ().isArray () )
        {
            int num_elements = Array.getLength ( parameter_value );
            StringBuilder sbuf = new StringBuilder ();
            sbuf.append ( "[" );
            for ( int e = 0; e < num_elements && e < 16; e ++ )
            {
                if ( e > 0 )
                {
                    sbuf.append ( "," );
                }
                Object array_element = Array.get ( parameter_value, e );
                sbuf.append ( " " + array_element );
            }

            if ( num_elements > 16 )
            {
                sbuf.append ( ", ..." );
            }

            if ( num_elements > 0 )
            {
                sbuf.append ( " " );
            }

            sbuf.append ( "]" );

            parameter_value = sbuf.toString ();
        }

        String parameter_name_regex =
                this.parameterNameRegex ( parameter_name );

        String parameter_value_regex =
            this.parameterValueRegex ( parameter_value );

        String parameterized_text =
            text.replaceAll ( parameter_name_regex,
                              parameter_value_regex );

        // If there were no matches in the text, append the
        // replaced parameter info to the end of the message text.
        if ( text != null
             && text.equals ( parameterized_text ) )
        {
            parameterized_text += "\n    "
                + parameter_name + " = "+ parameter_value;
        }

        return parameterized_text;
    }


    /**
     * @see musaico.i18n.message.MessageFormat#parameterName(String)
     */
    public String parameterName (
                                 String parameter_name
                                 )
    {
        return StandardMessageFormat.OPEN_PARAM
            + parameter_name
            + StandardMessageFormat.CLOSE_PARAM;
    }


    /**
     * @see musaico.i18n.message.MessageFormat#parameterNameRegex(String)
     */
    public String parameterNameRegex (
                                      String parameter_name
                                      )
    {
        return
            StandardMessageFormat.OPEN_PARAM_REGEX
            + Pattern.quote ( parameter_name )
            + StandardMessageFormat.CLOSE_PARAM_REGEX;
    }


    /**
     * @see musaico.i18n.message.MessageFormat#parameterValueRegex(Serializable)
     */
    public String parameterValueRegex (
                                       Serializable parameter_value
                                       )
    {
        return
            Matcher.quoteReplacement ( "" + parameter_value );
    }
}
