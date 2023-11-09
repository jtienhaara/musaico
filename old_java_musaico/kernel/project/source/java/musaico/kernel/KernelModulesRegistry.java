package musaico.kernel;


import musaico.i18n.exceptions.I18nIOException;

import musaico.i18n.message.Message;

import musaico.io.Identifier;

import musaico.security.Credentials;
import musaico.security.SecurityException;


/**
 * <p>
 * Finds, resolves and manages the Modules loaded by a Kernel.
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
 * Copyright (c) 2012 Johann Tienhaara
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
public interface KernelModulesRegistry
{
    /**
     * <p>
     * Given the specified module identifier, tracks down the
     * location and exact version of the module.
     * </p>
     *
     * <p>
     * This method may or may not result in the module being loaded
     * into memory.  It is not guaranteed until load ()
     * has been invoked on the identifier returned from this method.
     * </p>
     *
     * @param credentials The credentials of the requester.  Typically
     *                    another module's credentials, though a user
     *                    can also request a module to be tracked down.
     *                    Must not be null.
     *
     * @param module_id The identifier of the module to find.
     *                  Must be a musaico.kernel.module.ModuleID.
     *                  Must not be null.  Can include Version.ANY
     *                  or a VersionRange.
     *
     * @return The highest version of the requested module that
     *         could be found, or null if no matching module could
     *         be found.  When a non-null reference is returned, always a
     *         musaico.kernel.module.ModuleDescription.
     *         Never includes Version.ANY or a VersionRange.
     *
     * @throws SecurityException If the specified credentials are not
     *                           allowed to find modules in this kernel.
     *
     * @throws I18nIOException If finding the module failed due to
     *                         an I/O problem (failed disk or networking
     *                         and so on).
     */
    public abstract Message find (
                                  Credentials credentials,
                                  Identifier module_id
                                  )
        throws SecurityException,
               I18nIOException;


    /**
     * <p>
     * Ensures the specified module is loaded and started up
     * (it has created all its initial drivers and object system
     * factories and so on).
     * </p>
     *
     * <p>
     * This call could conceivably block for a while, as the module
     * being loaded may need to resolve dependencies, each of
     * which needs to resolve dependencies, and so on...
     * </p>
     *
     * @param credentials The credentials of the requester.  Typically
     *                    another module's credentials, though a user
     *                    can also request a module to be loaded.
     *                    Must not be null.
     *
     * @param module_description The exact description of the module to load.
     *                           Must be a
     *                           musaico.kernel.module.ModuleDescription.
     *                           Must not be null.  Must not include
     *                           Version.ANY or a VersionRange.
     *
     * @throws SecurityException If the specified credentials are not
     *                           allowed to load modules in this kernel.
     *
     * @throws I18nIOException If loading the module failed, or if
     *                         the module threw an exception during
     *                         startup.
     */
    public abstract void load (
                               Credentials credentials,
                               Message module_description
                               )
        throws SecurityException,
               I18nIOException;


    /**
     * <p>
     * Stops the specified module and removes it from the kernel
     * (including any and all drivers it created, object system
     * factories, and so on).
     * </p>
     *
     * <p>
     * This call could conceivably block for a while, as the module
     * being unloaded may need to shut down tasks and so on.
     * </p>
     *
     * @param credentials The credentials of the requester.  Typically
     *                    another module's credentials or a user.
     *                    Must not be null.
     *
     * @param module_description The exact description of the module to unload.
     *                           Must be a
     *                           musaico.kernel.module.ModuleDescription.
     *                           Must not be null.  Must not include
     *                           Version.ANY or a VersionRange.
     *
     * @throws SecurityException If the specified credentials are not
     *                           allowed to unload modules in this kernel.
     *
     * @throws I18nIOException If unloading the module failed, or if
     *                         the module threw an exception during
     *                         shutdown, or if there are dependencies
     *                         on the specified module which prevent
     *                         it from shutting down (other modules,
     *                         processes, and so on).
     */
    public abstract void unload (
                                 Credentials credentials,
                                 Message module_description
                                 )
        throws SecurityException,
               I18nIOException;
}
