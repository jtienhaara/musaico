package musaico.kernel.registries;

import java.util.HashMap;
import java.util.Map;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.TypedIdentifier;

import musaico.kernel.Kernel;
import musaico.kernel.KernelObjectException;
import musaico.kernel.KernelObjectsRegistry;
import musaico.kernel.KernelPermission;
import musaico.kernel.KernelPermissions;

import musaico.security.Credentials;
import musaico.security.Permissions;
import musaico.security.Security;
import musaico.security.SecurityException;


/**
 * <p>
 * A simple in-memory database of all the objects stored
 * in the kernel.
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
public class SimpleKernelObjectsRegistry
    implements KernelObjectsRegistry
{
    /** The parent kernel. */
    private final Kernel kernel;

    /** Lock all critical sections on this token: */
    private final Object lock = new Object ();

    /** All kernel objects (ONodes, Drivers, and so on). */
    private final Map<TypedIdentifier,Object> kernelObjects = new
        HashMap<TypedIdentifier,Object> ();


    /**
     * <p>
     * Creates a new SimpleKernelObjectsRegistry for the specified
     * Kernel.
     * </p>
     *
     * @param kernel The Kernel.  Must not be null.
     */
    public SimpleKernelObjectsRegistry (
                                        Kernel kernel
                                        )
    {
        this.kernel = kernel;
    }


    /**
     * @see musaico.kernel.KernelObjectsRegistry#register(musaico.security.Credentials,musaico.io.TypedIdentifier,java.lang.Object)
     */
    @Override
    public
        <KERNEL_OBJECT extends Object>
            void register (
                           Credentials credentials,
                           TypedIdentifier<KERNEL_OBJECT> object_id,
                           KERNEL_OBJECT object
                           )
        throws I18nIllegalArgumentException,
               SecurityException,
               KernelObjectException
    {
        // Throws SecurityException if the Credentials are invalid:
        Security<KernelPermission> security =
            this.kernel.securityFor ( credentials );

        Permissions<KernelPermission> requested_permissions =
            new KernelPermissions ( credentials,
                                    object_id,
                                    KernelPermission.CREATE_OBJECT );
        Permissions<KernelPermission> granted_permissions =
            security.request ( requested_permissions );

        if ( ! granted_permissions.isAllowed ( requested_permissions ) )
        {
            throw new SecurityException ( "Security violation: kernel [%kernel%] will not allow [%credentials%] to createKernelObject [%object_id%] object [%object%]",
                                          "kernel", this.kernel,
                                          "credentials", credentials,
                                          "object_id", object_id,
                                          "object", object );
        }

        synchronized ( this.lock )
        {
            if ( this.kernelObjects.containsKey ( object_id ) )
            {
                throw new KernelObjectException ( "Cannot create kernel object [%object_id%] object [%object%]: Already exists",
                                                  "object_id", object_id,
                                                  "object", object );
            }

            this.kernelObjects.put ( object_id, object );
        }
    }


    /**
     * @see musaico.kernel.KernelObjectsRegistry#get(musaico.security.Credentials,musaico.io.TypedIdentifier)
     */
    @Override
    public
        <KERNEL_OBJECT extends Object>
            KERNEL_OBJECT get (
                               Credentials credentials,
                               TypedIdentifier<KERNEL_OBJECT> object_id
                               )
        throws I18nIllegalArgumentException,
               SecurityException,
               KernelObjectException
    {
        // Throws SecurityException if the Credentials are invalid:
        Security<KernelPermission> security =
            this.kernel.securityFor ( credentials );

        Permissions<KernelPermission> requested_permissions =
            new KernelPermissions ( credentials,
                                    object_id,
                                    KernelPermission.RETRIEVE_OBJECT );
        Permissions<KernelPermission> granted_permissions =
            security.request ( requested_permissions );

        if ( ! granted_permissions.isAllowed ( requested_permissions ) )
        {
            throw new SecurityException ( "Security violation: kernel [%kernel%] will not allow [%credentials%] to getKernelObject [%object_id%]",
                                          "kernel", this.kernel,
                                          "credentials", credentials,
                                          "object_id", object_id );
        }

        synchronized ( this.lock )
        {
            if ( ! this.kernelObjects.containsKey ( object_id ) )
            {
                throw new KernelObjectException ( "Cannot retrieve kernel object [%object_id%]: No such kernel object",
                                                  "object_id", object_id );
            }

            return object_id.identifiedObjectClass ().cast ( this.kernelObjects.get ( object_id ) );
        }
    }


    /**
     * @see musaico.kernel.KernelObjectsRegistry#unregister(musaico.security.Credentials,musaico.io.TypedIdentifier)
     */
    @Override
    public
        <KERNEL_OBJECT extends Object>
            KERNEL_OBJECT unregister (
                                      Credentials credentials,
                                      TypedIdentifier<KERNEL_OBJECT> object_id
                                      )
        throws I18nIllegalArgumentException,
               SecurityException,
               KernelObjectException
    {
        // Throws SecurityException if the Credentials are invalid:
        Security<KernelPermission> security =
            this.kernel.securityFor ( credentials );

        Permissions<KernelPermission> requested_permissions =
            new KernelPermissions ( credentials,
                                    object_id,
                                    KernelPermission.DELETE_OBJECT );
        Permissions<KernelPermission> granted_permissions =
            security.request ( requested_permissions );

        if ( ! granted_permissions.isAllowed ( requested_permissions ) )
        {
            throw new SecurityException ( "Security violation: kernel [%kernel%] will not allow [%credentials%] to deleteKernelObject [%object_id%]",
                                          "kernel", this.kernel,
                                          "credentials", credentials,
                                          "object_id", object_id );
        }

        synchronized ( this.lock )
        {
            if ( ! this.kernelObjects.containsKey ( object_id ) )
            {
                throw new KernelObjectException ( "Cannot delete kernel object [%object_id%]: No such kernel object",
                                                  "object_id", object_id );
            }

            return object_id.identifiedObjectClass ().cast ( this.kernelObjects.remove ( object_id ) );
        }
    }


    /**
     * @see musaico.kernel.KernelObjectsRegistry#update(musaico.security.Credentials,musaico.io.TypedIdentifier,java.lang.Object)
     */
    @Override
    public
        <KERNEL_OBJECT extends Object>
            KERNEL_OBJECT update (
                                  Credentials credentials,
                                  TypedIdentifier<KERNEL_OBJECT> object_id,
                                  KERNEL_OBJECT new_object
                                  )
        throws I18nIllegalArgumentException,
               SecurityException,
               KernelObjectException
    {
        // Throws SecurityException if the Credentials are invalid:
        Security<KernelPermission> security =
            this.kernel.securityFor ( credentials );

        Permissions<KernelPermission> requested_permissions =
            new KernelPermissions ( credentials,
                                    object_id,
                                    KernelPermission.UPDATE_OBJECT );
        Permissions<KernelPermission> granted_permissions =
            security.request ( requested_permissions );

        if ( ! granted_permissions.isAllowed ( requested_permissions ) )
        {
            throw new SecurityException ( "Security violation: kernel [%kernel%] will not allow [%credentials%] to updateKernelObject [%object_id%] object [%object%]",
                                          "kernel", this.kernel,
                                          "credentials", credentials,
                                          "object_id", object_id,
                                          "object", new_object );
        }

        final KERNEL_OBJECT old_object;
        synchronized ( this.lock )
        {
            if ( ! this.kernelObjects.containsKey ( object_id ) )
            {
                throw new KernelObjectException ( "Cannot update kernel object [%object_id%] object [%object%]: No such kernel object",
                                                  "object_id", object_id,
                                                  "object", new_object );
            }

            old_object =
                object_id.identifiedObjectClass ().cast ( this.kernelObjects.get ( object_id ) );
            this.kernelObjects.put ( object_id, new_object );
        }

        return old_object;
    }
}
