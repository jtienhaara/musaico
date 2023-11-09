package musaico.i18n.exceptions;

import java.io.Serializable;


import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


import musaico.i18n.Internationalized;
import musaico.i18n.SimpleInternationalized;

import musaico.i18n.localizers.MessageLocalizer;

import musaico.i18n.message.Message;


/**
 * <p>
 * Localization for the Musaico i18n exceptions package
 * (resource bundles and localizers for the exceptions
 * which need to use them).
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
public class L10n
{
    /** The name of the Musaico exceptions localized resource bundles. */
    public static final String DEFAULT_LOCALIZED_EXCEPTIONS_BUNDLE =
        "musaico.i18n.exceptions.Messages";

    /** The name of the Message parameter which specifies where to
     *  find the message bundle for the Message (such as "x.y.z.Messages"
     *  or { "a.b.c.Messages", "x.y.z.Messages", ... }).
     *  Any Message which does not provide this parameter shall induce
     *  a search for an appropriate message bundle.  Each message bundle
     *  is a Java message bundle with a name like "x.y.z.Messages.properties"
     *  "x.y.z.Messages_en.properties" or "x.y.z.Messages.en_CA.properties"
     *  and so on. */
    public static final String MESSAGE_BUNDLE_PARAMETER =
        "i18n_message_bundle_names";


    /**
     * <p>
     * Internationalizes the specified Message, returning an
     * <code> Internationalized </code> object which can be
     * localized and the full message text (including parameters)
     * retrieved as a String.
     * </p>
     *
     * @param message_holder The object which is creating
     *                       the internationalized Message.  Used to
     *                       lookup localized resource bundles in
     *                       the message_holder's package.  Must not
     *                       be null.
     * @param message The message to internationalized.  Must not be null.
     */
    public static Internationalized<Message,String> internationalize (
                                                                      Object message_holder,
                                                                      Message message
                                                                      )
    {
        String [] bundles_names =
            L10n.messageBundleNames ( message,
                                      message_holder.getClass () );

        Internationalized<Message,String> i18n_message =
            new SimpleInternationalized<Message,String> ( message,
                                                          L10n.messageLocalizer ( bundles_names ) );

        return i18n_message;
    }


    /**
     * <p>
     * Given a class and the specified Message, returns the base name(s)
     * for localized bundles.
     * </p>
     *
     * <p>
     * If the specified Message has a MESSAGE_BUNDLE_PARAMETER parameter
     * in it, then the specified message bundle will be loaded whenever
     * localization occurs.  Otherwise, a best-guess approach will be
     * taken by the localizer, trying to find an appropriate message
     * bundle.  In the latter case, <code> messageBundleNames ( class ) </code
     * is invoked to list possible message bundle qualified names.
     * </p>
     *
     * <p>
     * This method is final only so that the compiler will treat it
     * as a macro / inline method.
     * </p>
     */
    public static final String [] messageBundleNames (
                                                      Message message,
                                                      Class cl
                                                      )
    {
        if ( message == null )
        {
            return L10n.messageBundleNames ( cl );
        }

        Serializable i18n_message_bundle_names =
            message.parameter ( L10n.MESSAGE_BUNDLE_PARAMETER );
        if ( i18n_message_bundle_names != null )
        {
            if ( i18n_message_bundle_names instanceof String )
            {
                return new String [] { (String) i18n_message_bundle_names };
            }
            else if ( i18n_message_bundle_names instanceof String [] )
            {
                return (String []) i18n_message_bundle_names;
            }
        }

        return L10n.messageBundleNames ( cl );
    }


    /**
     * <p>
     * Given a class, returns the base name(s) for localized bundles
     * for that class.
     * </p>
     *
     * <p>
     * For example, the class <code> com.foo.Bar </code> might have
     * its localized bundles stored in <code> com.foo.Messages </code>
     * by default.  Or the class constructing the exception in the
     * first place (say x.y.ZObject) might have its own localized
     * bundles for the <code> com.foo.Bar </code> class, located in
     * <code> x.y.Messages </code>.
     * </p>
     *
     * <p>
     * This method returns an array of 3 qualified message bundle
     * names: first the stack message bundle name (in the package of the
     * caller), followed by the class's bundle name
     * (<code> com.foo.Bar </code>), followed by the default for all
     * i18n exceptions
     * (<code> musaico.i18n.exceptions.L10n.DEFAULT_LOCALIZED_EXCEPTIONS_BUNDLE </code>).
     * </p>
     *
     * <p>
     * This method is final only so that the compiler will treat it
     * as a macro / inline method.
     * </p>
     */
    public static final String [] messageBundleNames (
                                                      Class cl
                                                      )
    {
        // First the bundle name for the caller's package.
        String bundle_for_caller = null;
        final StackTraceElement [] stack =
            Thread.currentThread ().getStackTrace ();
        for ( int st = 0; st < stack.length; st ++ )
        {
            final String class_name = stack [ st ].getClassName ();
            if ( ! class_name.startsWith ( "musaico.i18n." )
                 && ! class_name.startsWith ( "java." ) )
            {
                // It's not one of ours.  Presumably it's from the
                // caller.
                int last_dot = class_name.lastIndexOf ( '.' );
                if ( last_dot <= 0 )
                {
                    bundle_for_caller = "Messages";
                }
                else
                {
                    String package_name = class_name.substring ( 0, last_dot );
                    bundle_for_caller = package_name + ".Messages";
                }

                break;
            }
        }

        // Next the bundle name for the specified class.
        final String bundle_for_class;
        String class_name = cl.getName ();
        int last_dot = class_name.lastIndexOf ( '.' );
        if ( last_dot <= 0 )
        {
            bundle_for_class = "Messages";
        }
        else
        {
            String package_name = class_name.substring ( 0, last_dot );
            bundle_for_class = package_name + ".Messages";
        }

        // Finally, we'll add the default for all i18n exceptions.
        final String bundle_default =
            L10n.DEFAULT_LOCALIZED_EXCEPTIONS_BUNDLE;

        // Assemble the array.  Don't bother including nulls or duplicates.
        final List<String> bundle_names_list = new ArrayList<String> ();
        if ( bundle_for_caller != null
             && ! bundle_names_list.contains ( bundle_for_caller ) )
        {
            bundle_names_list.add ( bundle_for_caller );
        }
        if ( bundle_for_class != null
             && ! bundle_names_list.contains ( bundle_for_class ) )
        {
            bundle_names_list.add ( bundle_for_class );
        }
        if ( bundle_default != null
             && ! bundle_names_list.contains ( bundle_default ) )
        {
            bundle_names_list.add ( bundle_default );
        }

        final String [] bundle_names =
            bundle_names_list.toArray ( new String [ bundle_names_list.size () ] );

        return bundle_names;
    }


    /**
     * <p>
     * Creates a new Localizer to load the Musaico exceptions
     * localized resource bundles.
     * </p>
     */
    public static MessageLocalizer messageLocalizer ()
    {
        final String [] bundles_names = new String []
        {
            L10n.DEFAULT_LOCALIZED_EXCEPTIONS_BUNDLE
        };

        return L10n.messageLocalizer ( bundles_names );
    }


    /**
     * <p>
     * Creates a new Localizer to load the Musaico exceptions
     * localized resource bundles.
     * </p>
     *
     * @param bundle_name One or more fully qualified x.y.z properties or
     *                    class name(s) which serves as the basis for the
     *                    names of the bundles of localized messages.
     */
    public static MessageLocalizer messageLocalizer (
                                                     String[] bundles_names
                                                     )
    {
        ClassLoader class_loader =
            Thread.currentThread ().getContextClassLoader ();
        ResourceBundle.Control localized_classes =
            ResourceBundle.Control.getControl ( ResourceBundle.Control.FORMAT_DEFAULT );

        return new MessageLocalizer ( bundles_names,
                                      class_loader,
                                      localized_classes );
    }
}
