package musaico.i18n.localizers;


import java.io.Serializable;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;


import musaico.i18n.LocalizationException;
import musaico.i18n.Localizer;


/**
 * <p>
 * Given an INPUT value and a Locale, localizes the input to an
 * OUTPUT.
 * </p>
 *
 *
 * <p>
 * In Java, all Localizers must be serializable in order to play
 * nicely across RMI.
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
public abstract class AbstractLocalizer<INPUT extends Serializable,OUTPUT extends Serializable>
    implements Localizer<INPUT,OUTPUT>, Serializable
{
    /** Synchronize all critical sections on this lock. */
    private final Serializable lock = new String ();

    /** The lookup of resource bundles by locales.
     *  There can be multiple ResourceBundles for each locale
     *  (for example, one provided by an internationalized Exception,
     *  another by a user of that exception class, and so on). */
    private final Map<Locale,ResourceBundle[]> locales =
        new HashMap<Locale,ResourceBundle[]> ();

    /** Details for loading Java resource bundles.
     *  Can be a list of 1 or more, we search the bundles in order for the
     *  matching resource. */
    private final String [] resourceBundleNames;

    /** Details for loading Java resource bundles. */
    private final ClassLoader resourceBundleLoader;

    /** Details for loading Java resource bundles. */
    private final ResourceBundle.Control resourceBundleSource;


    /**
     * <p>
     * Creates a new AbstractLocalizer with the specified parameters
     * as guides for loading Java resource bundles.
     * </p>
     *
     * @param resource_bundle_names The base name(s) of resource bundle(s),
     *                              each a fully qualified class name
     *                              (such as "musaico.l10n.Messages"
     *                              or "musaico.l10n.TimeFormatters"
     *                              and so on).  Searched through in order
     *                              to find a resource bundle with the
     *                              right message.  Must not be null.
     * @param loader The class loader from which to load the
     *               resource bundle.  For example,
     *               <code> Thread.current ().getClassLoader () </code>.
     *               Must not be null.
     * @param source The control which gives information for the
     *               resource bundle loading process.  For example,
     *               <code> ResourceBundle.Control.FORMAT_DEFAULT </code>.
     *               Must not be null.
     */
    public AbstractLocalizer (
                              String [] resource_bundle_names,
                              ClassLoader loader,
                              ResourceBundle.Control source
                              )
    {
        if ( resource_bundle_names == null )
        {
            throw new IllegalArgumentException ( "Cannot create a "
                                                 + "AbstractLocalizer with"
                                                 + "null resource_bundle_name"
                                                 + "(loader = " + loader
                                                 + " source = "+ source
                                                 + ")" );
        }
        else if ( resource_bundle_names.length == 0 )
        {
            throw new IllegalArgumentException ( "Cannot create a "
                                                 + "AbstractLocalizer with"
                                                 + "0 resource_bundle_names"
                                                 + "(loader = " + loader
                                                 + " source = "+ source
                                                 + ")" );
        }
        else if ( loader == null )
        {
            throw new IllegalArgumentException ( "Cannot create a "
                                                 + "AbstractLocalizer with"
                                                 + "null loader"
                                                 + "(resource_bundle_names = "+ resource_bundle_names
                                                 + " source = "+ source
                                                 + ")" );
        }
        else if ( source == null )
        {
            throw new IllegalArgumentException ( "Cannot create a "
                                                 + "AbstractLocalizer with"
                                                 + "null source"
                                                 + "(resource_bundle_names = "+ resource_bundle_names
                                                 + " loader = " + loader
                                                 + ")" );
        }

        this.resourceBundleNames = resource_bundle_names;
        this.resourceBundleLoader = loader;
        this.resourceBundleSource = source;
    }


    /**
     * <p>
     * Loads (possibly from cache) the resource bundle(s) for the
     * specified locale.
     * </p>
     *
     * @param locale The locale to load resource bundles for.
     *               Must not be null.
     *
     * @return The ResourceBundles for the locale.  Never null, always
     *         at least one element.
     *
     * @throws LocalizationException If the resource bundles cannot
     *                               be found.
     */
    public ResourceBundle [] getResourceBundles (
                                                 Locale locale
                                                 )
        throws LocalizationException
    {
        // Try to get the ResourceBundles out of our cache.
        ResourceBundle [] resource_bundles;
        BusyLoadingResourceBundle busy_on_this_thread = null;
        synchronized ( this.lock )
        {
            resource_bundles = this.locales.get ( locale );
            if ( resource_bundles == null )
            {
                busy_on_this_thread = new BusyLoadingResourceBundle ();
                ResourceBundle [] store_for_other_threads =
                    new ResourceBundle []
                    {
                        busy_on_this_thread
                    };
                this.locales.put ( locale, store_for_other_threads );
            }
        }

        // Only if another thread is loading the requested bundle(s):
        if ( resource_bundles != null
             && resource_bundles.length == 1
             && ( resource_bundles [ 0 ] instanceof BusyLoadingResourceBundle ) )
        {
            BusyLoadingResourceBundle loaded =
                (BusyLoadingResourceBundle) resource_bundles [ 0 ];
            // Wait for the resource bundle, and blow up if it
            // gets set as null.
            try
            {
                resource_bundles = loaded.waitForResourceBundles ();
            }
            catch ( InterruptedException e )
            {
                // Gave up waiting for some reason.
                throw new LocalizationException ( "Interrupted while waiting "
                                                  + "for resource bundles "
                                                  + this.resourceBundleNames
                                                  + " locale "
                                                  + locale
                                                  + " to be loaded" );
            }

            if ( resource_bundles == null
                 || resource_bundles.length == 0 )
            {
                throw new LocalizationException ( "Could not load "
                                                  + "resource bundles for "
                                                  + "locale " + locale
                                                  + " in another thread" );
            }

            return resource_bundles;
        }

        // Only if the ResourceBundles were in our cache:
        if ( resource_bundles != null
             && resource_bundles.length > 0 )
        {
            return resource_bundles;
        }

        // Only if THIS thread is (re-)loading the requested bundles:
        //
        // Load the resource bundle(s) for the specified locale.
        List<ResourceBundle> loaded_bundles =
            new ArrayList<ResourceBundle> ();
        Exception first_thrown_exception = null;
        for ( int rbn = 0; rbn < this.resourceBundleNames.length; rbn ++ )
        {
            try
            {
                ResourceBundle loaded_bundle =
                    ResourceBundle.getBundle ( this.resourceBundleNames [ rbn ],
                                               locale,
                                               this.resourceBundleLoader,
                                               this.resourceBundleSource );

                loaded_bundles.add ( loaded_bundle );
            }
            catch ( Exception e )
            {
                if ( first_thrown_exception == null )
                {
                    first_thrown_exception = e;
                }
            }
        }

        // Nothing was loaded!
        if ( loaded_bundles.size () == 0 )
        {
            synchronized ( this.lock )
            {
                this.locales.remove ( locale );
                busy_on_this_thread.cancel ();
            }

            if ( first_thrown_exception == null )
            {
                throw new LocalizationException ( "Failed to load "
                                                  + "resource bundle for "
                                                  + "locale " + locale );
            }
            else
            {
                throw new LocalizationException ( "Failed to load "
                                                  + "resource bundle for "
                                                  + "locale " + locale,
                                                  first_thrown_exception );
            }
        }

        // Now put the loaded bundle(s) into the lookup.
        resource_bundles =
            loaded_bundles.toArray ( new ResourceBundle [ loaded_bundles.size () ] );
        synchronized ( this.lock )
        {
            this.locales.put ( locale, resource_bundles );
            busy_on_this_thread.setNotifyResourceBundles ( resource_bundles );
        }

        return resource_bundles;
    }


    /**
     * @see musaico.i18n.Localizer#localize(INPUT,Locale)
     */
    public OUTPUT localize (
                            INPUT international_input,
                            Locale locale
                            )
        throws LocalizationException
    {
        ResourceBundle [] resource_bundles = getResourceBundles ( locale );

        // Get the identifier for the international INPUT.
        String international_id =
            this.getInternationalID ( international_input );

        // Now lookup the international INPUT in each resource bundle.
        // If it doesn't exist in any of them, give up and throw the
        // first thrown exception.
        OUTPUT localized_output;
        Exception first_thrown_exception = null;
        for ( int rb = 0; rb < resource_bundles.length; rb ++ )
        {
            try
            {
                localized_output = (OUTPUT)
                    resource_bundles [ rb ].getObject ( international_id );

                return localized_output;
            }
            catch ( Exception e )
            {
                if ( first_thrown_exception == null )
                {
                    first_thrown_exception =
                        new LocalizationException ( "Failed to retrieve "
                                                    + "localized output for "
                                                    + "input "
                                                    + international_input
                                                    + " locale "
                                                    + locale,
                                                    e );
                }
            }
        }

        // No localized output found.  Throw the first exception
        // we hit.
        if ( first_thrown_exception == null )
        {
            throw new LocalizationException ( "No localized "
                                              + international_input
                                              + " for locale "
                                              + locale );
        }
        else
        {
            throw new LocalizationException ( "No localized "
                                              + international_input
                                              + " for locale "
                                              + locale,
                                              first_thrown_exception );
        }
    }


    /**
     * <p>
     * Returns the unique String identifier (index into a ResourceBundle)
     * for the specified international INPUT.
     * </p>
     */
    public abstract String getInternationalID (
                                               INPUT international_input
                                               );
}
