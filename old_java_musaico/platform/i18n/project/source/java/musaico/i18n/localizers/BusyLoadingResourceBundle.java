package musaico.i18n.localizers;


import java.io.Serializable;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;


import musaico.i18n.LocalizationException;


/**
 * <p>
 * Temporary placeholder for ResourceBundle(s) which are already
 * being loaded in another thread.
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
public class BusyLoadingResourceBundle
    extends ResourceBundle
    implements Serializable
{
    /** Sleep and notify when resource bundle is loaded on this token: */
    private final Serializable wakeUp = new String ();

    /** The resource bundle, gets set once loaded. */
    private ResourceBundle [] resourceBundles = null;


    /**
     * @see java.util.ResourceBundle#getKeys()
     */
    public final Enumeration<String> getKeys ()
    {
        // We don't have any keys.
        return new Enumeration<String> ()
            {
                public boolean hasMoreElements ()
                {
                    return false;
                }
                public String nextElement ()
                {
                    throw new NoSuchElementException ();
                }
            };
    }


    /**
     * <p>
     * Cancels any/all threads waiting to load the resource bundle(s)
     * (for example, because the bundle(s) could not be found).
     * </p>
     */
    public void cancel ()
    {
        synchronized ( this.wakeUp )
        {
            this.resourceBundles = new ResourceBundle [ 0 ];

            this.wakeUp.notifyAll ();
        }
    }


    /**
     * @see java.util.ResourceBundle#handleGetObject(String)
     */
    protected final Object handleGetObject (
                                            String key
                                            )
    {
        return null;
    }


    /**
     * <p>
     * Sets the loaded ResourceBundles, and wakes up any threads which
     * were waiting for it/them.
     * </p>
     *
     * @param resource_bundles The loaded resource bundle(s).  If null
     *                         or empty, the waiting threads (if any)
     *                         will most likely all blow up with
     *                         LocalizationExceptions.
     */
    public void setNotifyResourceBundles (
                                          ResourceBundle [] resource_bundles
                                          )
    {
        synchronized ( this.wakeUp )
        {
            this.resourceBundles = resource_bundles;

            this.wakeUp.notifyAll ();
        }
    }


    /**
     * <p>
     * Waits for the resource bundle to be loaded, and retrieves
     * it as soon as it is ready.
     * </p>
     *
     * @return The loaded resource bundle(s).  One or more, unless
     *         a problem occurred loading them.  Never null.
     *
     * @throws InterruptedException If the waiting thread is interrupted
     *                              by some external force of man or
     *                              nature.
     * @throws LocalizationException If the requested resource bundle
     *                               could not be found.
     */
    public ResourceBundle [] waitForResourceBundles ()
        throws InterruptedException,
               LocalizationException
    {
        synchronized ( this.wakeUp )
        {
            if ( this.resourceBundles != null )
            {
                return this.resourceBundles;
            }

            this.wakeUp.wait ();

            if ( this.resourceBundles == null )
            {
                throw new LocalizationException ( "Failed to load resource "
                                                  + "bundle" );
            }

            return this.resourceBundles;
        }
    }
}
