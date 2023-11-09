package musaico.kernel;

import java.util.Locale;


import musaico.i18n.exceptions.I18nIllegalArgumentException;
import musaico.i18n.exceptions.I18nIOException;

import musaico.i18n.log.Logger;

import musaico.i18n.message.Message;

import musaico.io.Identifier;
import musaico.io.TypedIdentifier;

import musaico.field.FieldTypingEnvironment;

import musaico.security.Credentials;
import musaico.security.Security;
import musaico.security.SecurityException;


/**
 * <p>
 * Provides a library of routines safe for use inside Musaico
 * kernel modules (such as logging).
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
 * Copyright (c) 2009, 2012 Johann Tienhaara
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
public interface Kernel
{
    /**
     * <p>
     * Deletes the specified Credentials from the kernel.
     * </p>
     *
     * <p>
     * For example, it is usually a good idea to delete temporary
     * credentials when finished using a Cursor to navigate the object
     * system, since the credentials for the Cursor will be
     * exposed to numerous other kernel modules (object systems,
     * drivers, and so on)..
     * </p>
     *
     * @param credentials The credentials whose privileges
     *                    will be revoked.  Must not be null.
     *
     * @throws SecurityException If the specified credentials are
     *                           invalid (see above) or if the
     *                           credentials cannot be deleted
     *                           (for example because they do not
     *                           exist in the kernel or do not
     *                           have privileges to allow
     *                           deleting themselves).
     */
    public abstract void deleteCredentials (
                                            Credentials credentials
                                            )
        throws SecurityException;


    /**
     * <p>
     * Duplicates the specified Credentials with a different
     * one which has the same privileges.
     * </p>
     *
     * <p>
     * For example, it is usually a good idea to use a temporary
     * credentials when creating a Cursor to navigate the object
     * system, since the credentials for the Cursor will be
     * exposed to numerous other kernel modules (object systems,
     * drivers, and so on).
     * </p>
     *
     * @param credentials The source credentials whose privileges
     *                    will be copied to a new set of
     *                    credentials.  Must not be null.
     *
     * @return The new set of credentials with privileges
     *         copied from the specified source.  Never null.
     *
     * @throws SecurityException If the specified credentials are
     *                           invalid (see above) or if the
     *                           credentials cannot be copied
     *                           (for example because they do not
     *                           exist in the kernel or do not
     *                           have privileges to allow
     *                           creating copies).
     */
    public abstract Credentials duplicateCredentials (
                                                      Credentials credentials
                                                      )
        throws SecurityException;


    /**
     * <p>
     * Returns the default locale for kernel logging.
     * </p>
     *
     * @return The default locale of the kernel for logging purposes only.
     *         Never null.
     */
    public abstract Locale locale ();


    /**
     * <p>
     * Returns the standard log message printer for this kernel.
     * </p>
     */
    public abstract Logger logger ();


    /**
     * <p>
     * Returns the registry of kernel Modules, which is used to
     * find, load and unload modules into the kernel.
     * </p>
     *
     * @return This kernel's modules registry.  Never null.
     */
    public abstract KernelModulesRegistry modules ();


    /**
     * <p>
     * Returns the registry of kernel objects, which is used to
     * register Memory, Segments, ONodes, Drivers and so on after
     * they are created, and unregister them before they are deleted.
     * </p>
     *
     * <p>
     * Every kernel object has a <code> TypedIdentifier&lt;Class&gt; </code>
     * which belongs to a kernel namespace.
     * </p>
     *
     * @see musaico.kernel.KernelNamespaces
     *
     * @return This kernel's objects registry.  Never null.
     */
    public abstract KernelObjectsRegistry objects ();


    /**
     * <p>
     * Returns the kernel's abstraction layer for the platform,
     * providing platform-dependent state such as the current system
     * time.
     * </p>
     *
     * @return This kernel's platform operations.  Never null.
     */
    public abstract PlatformOperations platform ();


    /**
     * <p>
     * Returns the quota rules for this kernel, such as which
     * types of credentials can have quota limits on object
     * system usage.
     * </p>
     *
     * @return This kernel's quota rules.  Never null.
     */
    public abstract KernelQuotaRules quotaRules ();


    /**
     * <p>
     * Validates the credentials, then returns the Security
     * for them.
     * <p>
     *
     * @param credentials The credentials whose Security will be
     *                    returned.  Must not be null.
     *
     * @return The Security for the specified credentials.  Never null.
     *
     * @throws SecurityException If the specified credentials are
     *                           invalid.
     */
    public abstract Security<KernelPermission> securityFor (
                                                            Credentials credentials
                                                            )
        throws SecurityException;


    /**
     * <p>
     * The typing system provided for use inside the kernel.
     * </p>
     *
     * <p>
     * Fields can be <code>create</code>d using this kernel's typing
     * environment.
     * </p>
     *
     * <p>
     * Every Field read from or written to a Driver is an instance
     * of some Type or other (such as a String type, or an integer Type,
     * and so on).
     * </p>
     *
     * @see musaico.types
     *
     * @return The TypingEnvironment unique to this Kernel.
     *         Must not be shared with other Kernels or with
     *         applications.
     *         Never null.
     */
    public abstract FieldTypingEnvironment types ();
}
