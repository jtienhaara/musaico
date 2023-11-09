package musaico.kernel;

import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Identifier;
import musaico.io.Reference;

import musaico.security.Credentials;
import musaico.security.Permission;
import musaico.security.Permissions;
import musaico.security.Security;
import musaico.security.SecurityException;


/**
 * <p>
 * Performs security checks on KernelOperationState objects, in order
 * to keep the Credentials of a request out of reach of the kernel
 * objects which need to verify their permissions before proceeding
 * with an operation.
 * </p>
 *
 * <p>
 * For example, a KernelOperationState representing a request to
 * read from an object system might be verified with something like:
 * </p>
 *
 * <pre>
 *     private KernelSecurity<ObjectSystemPermission> mySecurity = ...;
 *     ...
 *     public Region read ( KernelOperationState op, ... )
 *         throws ...
 *     {
 *         try
 *         {
 *             this.mySecurity.checkPermissions ( op,
 *                                                ObjectSystemPermission.READ );
 *         }
 *         catch ( SecurityException e )
 *         {
 *             ...Deal with lack of permissions...
 *         }
 *         ...Proceed with read operation...
 *     }
 * </pre>
 *
 * <p>
 * The main purpose of KernelSecurity is to access the hidden
 * credentials of a KernelOperationState.  The actual implementation
 * of security is delegated to some other Security mechanism
 * (such as UNIX-style "read-write-other" permissions, access
 * control lists, capabilities and so on).
 * </p>
 *
 *
 * <p>
 * In Java every Security must be Serializable in order
 * to play nicely over RMI.
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
public class KernelSecurity<PERMISSION extends Reference>
    extends Serializable
{
    /** The kernel object which owns this security, such as
     *  an ONode or a Memory or a Driver and so on. */
    private final Identifier kernelObjectId;

    /** The class of PERMISSIONs which this KernelSecurity
     *  handles. */
    private final Class<PERMISSION> permissionClass;

    /** The delegate who handles permission requests for us
     *  according to UNIX-style rules or ACLs or Capabilities
     *  and so on. */
    private final Security<PERMISSION> delegate;


    /**
     * <p>
     * Creates a new KernelSecurity for the specified Kernel
     * and class of permissions.
     * </p>
     *
     * <p>
     * This constructor asks the kernel to provide the appropriate
     * Security&lt;class&gt; delegate for this new KernelSecurity.
     * </p>
     *
     * <p>
     * No reference to the kernel is maintained after this constructor
     * completes.
     * </p>
     *
     * @param kernel The Kernel for whom this KernelSecurity will
     *               check permissions.  Must not be null.
     *
     * @param kernel_object_id The identifier of the kernel object
     *                         whose security checks this KernelSecurity
     *                         will be responsible for.  Must not be null.
     *
     * @param permission_class The class of permissions which this
     *                         KernelSecurity will check.
     *                         Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public final KernelSecurity (
                                 Kernel kernel,
                                 Identifier kernel_object_id,
                                 Class<PERMISSION> permission_class
                                 )
        throws I18nIllegalArgumentException
    {
        if ( kernel == null
             || kernel_object_id == null
             || permission_class == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a KernelSecurity for kernel [%kernel%] kernel object id [%kernel_object_id%] permission class [%permission_class%]",
                                                     "kernel", kernel,
                                                     "kernel_object_id", kernel_object_id,
                                                     "permission_class", permission_class );
        }

        this.kernelObjectId = kernel_object_id;
        this.permissionClass = permission_class;
        try
        {
            this.delegate = kernel.security ( this.permissionClass );
        }
        catch ( I18nIllegalArgumentException e )
        {
            throw e;
        }
    }


    /**
     * <p>
     * Checks the specified kernel operation state to make sure
     * the Credentials originally requesting the operation
     * have all of the specified permissions.
     * </p>
     *
     * <p>
     * On success no exception is thrown, so the operation can
     * proceed.
     * </p>
     *
     * @param op The KernelOperationState whose Credentials
     *           will be checked.  Must not be null.
     *
     * @param permissions The required PERMISSIONs to check.
     *                    Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     *
     * @throws SecurityException If the specified operation cannot
     *                           proceed because the User or Module
     *                           which initiated the operation does
     *                           not have all of the permissions to
     *                           proceed.
     */
    public final void checkPermissions (
                                        KernelOperationState op,
                                        Permission<PERMISSION> ... requested_permissions
                                        )
        throws I18nIllegalArgumentException,
               SecurityException
    {
        if ( op == null
             || requested_permissions == null )
        {
            throw new I18nIllegalArgumentException ( "KernelSecurity [%kernel_security%] cannot check permissions for kernel operation state [%op%] required permissions [%permissions%]",
                                                     "kernel_security", kernel_security,
                                                     "op", op,
                                                     "permissions", requested_permissions );
        }

        Permissions granted_permissions =
            this.request ( 
!!!
    }


    /**
     * @see musaico.security.Security.request(musaico.security.Permissions)
     */
    public final Permissions<PERMISSION> request (
                         Permissions<PERMISSION> requested_permissions
                         )
    {
        return this.delegate.request ( requested_permissions );
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toObject ()
    {
        return
            "KernelSecurity<"
            this.permissionClass.getSimpleName ()
            + "> for "
            + this.kernelObjectId;
    }
}
