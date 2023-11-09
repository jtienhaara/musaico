// This class file needs to be copied into /tmp/foo/bar.

package foo.bar;


import java.util.Locale;


import musaico.i18n.Internationalized;

import musaico.i18n.exceptions.I18nException;

import musaico.i18n.message.Message;


/**
 * <p>
 * Test localized exceptions
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
public class TestExceptionsFromTmp
    extends I18nException
{
    /**
     * <p>
     * Creates a new Exception with the specified
     * internationalized Message.
     * </p>
     *
     * @param i18n_message The internationalized Message for this
     *                     Exception.  The localized Message
     *                     will be looked up by Locale when requested.
     *                     Must not be null.
     */
    public TestExceptionsFromTmp (
                                  Internationalized<Message,String> i18n_message
                                  )
    {
        super ( i18n_message );
    }


    /**
     * <p>
     * Creates a new Exception with the specified Message.
     * </p>
     *
     * @param message The Message for this Exception.
     *                The localized Message will be looked up by Locale
     *                when requested.  Must not be null.
     */
    public TestExceptionsFromTmp (
                                  Message message
                                  )
    {
        super ( message );
    }


    /**
     * <p>
     * Creates a new Exception with the specified String text.
     * </p>
     *
     * @param text The text for this Exception.
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
    public TestExceptionsFromTmp (
                                  String text,
                                  Object ... parameters
                                  )
    {
        super ( text, parameters );
    }




    public static void main (
                             String[] args
                             )
        throws Exception
    {
        if ( args == null
             || args.length != 1 )
        {
            System.out.println( "Error: specify the locale (e.g. en)" );
            return;
        }

        Locale locale = new Locale ( args [ 0 ] );
        I18nException [] exceptions = new I18nException []
        {
            new TestExceptionsFromTmp ( (Internationalized<Message,String>) null ),
            new I18nException ( "TEST Just text" ),
            new TestExceptionsFromTmp ( "TEST Text foo = '[%foo%]'", "foo", "$5.0" ),
            new I18nException ( "TEST Text foo = '[%foo%]'", "foo" )
        };

        System.out.println ( "Checking to see if 4th exception "
                             + "has parameter 'foo' == null." );
        String [] parameters = exceptions [ 3 ].message ().parameters ();
        for ( int p = 0; p < parameters.length; p ++ )
        {
            if ( parameters [ p ].equals ( "foo" ) )
            {
                System.out.println ( "    4th exception has parameter 'foo'." );
                break;
            }
        }
        System.out.println ( "    4th exception parameter 'foo' == null?  "
                             + ( exceptions [ 3 ].message ().parameter ( "foo" ) == null ) );

        for ( int e = 0; e < exceptions.length; e ++ )
        {
            System.out.println ( "Localized exception:    "
                                 + exceptions [ e ]
                                 .localize ( locale )
                                 .value () );
        }

        System.out.println ( "Using the Messages.properties file from the call stack's package," );
        System.out.println ( "the following message should be French (not English):" );
        I18nException standard_exception =
            new I18nException ( (Internationalized<Message,String>) null );
        System.out.println ( "    " + standard_exception.localize ( locale ).value () );
    }
}
