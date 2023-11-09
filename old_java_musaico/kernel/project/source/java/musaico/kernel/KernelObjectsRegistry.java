package musaico.kernel;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Identifier;
import musaico.io.TypedIdentifier;

import musaico.security.Credentials;
import musaico.security.SecurityException;


/**
 * <p>
 * Keeps track of all the objects managed by the kernel, such as
 * Memory, Segments, SuperBlocks, OEntries, Drivers and so on.
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
public interface KernelObjectsRegistry
{
    /**
     * <p>
     * Retrieves the specified kernel object from the Kernel.
     * </p>
     *
     * <p>
     * For example, to retrieve a Driver from the kernel:
     * </p>
     *
     * <pre>
     *     Kernel kernel = ...;
     *     DriverIdentifier driver_id = ...;
     *     Module my_module = ...;
     *     Driver driver =
     *         kernel.objects ().get ( my_module.credentials (),
     *                                 driver_id );
     * </pre>
     *
     * <p>
     * Or to retrieve a particular ONode:
     * </p>
     *
     * <pre>
     *     Kernel kernel = ...;
     *     ONodeIdentifier onode_id = ...;
     *     Module my_module = ...;
     *     ONode onode =
     *         kernel.objects ().get ( my_module.credentials (),
     *                                 onode_id );
     * </pre>
     *
     * <p>
     * Depending on the permissions granted, a read-only proxy
     * might be returned.  When at least some sort of access
     * to the requested object is granted, the kernel looks
     * up the object and returns one of:
     * </p>
     *
     * <ul>
     *     <li> The requested object, if it is found and the
     *          credentials are authorized to access it
     *          without restriction. </li>
     *     <li> A proxy object, if it is found but the credentials
     *          have only limited access to it (such as read-only). </li>
     * </ul>
     *
     * @param credentials The credentials of the owner of the
     *                    object to be retrieved.  Typically the
     *                    credentials of the kernel Module whose
     *                    code is retrieving the object.  User
     *                    credentials should never be used to
     *                    try to access kernel objects.
     *                    Must not be null.
     *
     * @param object_id The unique identifier of the object within
     *                  a specific namespace.  Must not be null.
     *
     * @return The requested object.  Never null.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (see above).
     *
     * @throws SecurityException If the credentials are not allowed
     *                           to retrieve the specified object.
     *
     * @throws KernelObjectException If the requested object does
     *                               not exist in the kernel.
     */
    public abstract
        <KERNEL_OBJECT extends Object>
            KERNEL_OBJECT get (
                               Credentials credentials,
                               TypedIdentifier<KERNEL_OBJECT> object_id
                               )
        throws I18nIllegalArgumentException,
               SecurityException,
               KernelObjectException;


    /**
     * <p>
     * Tells the Kernel to store the specified object in the
     * specified kernel namespace.
     * </p>
     *
     * <p>
     * For example, passing a newly created Driver to the kernel:
     * </p>
     *
     * <pre>
     *     Kernel kernel = ...;
     *     Driver driver = ...;
     *     Module my_module = ...;
     *     kernel.objects ().register ( my_module.credentials (),
     *                                  driver.id (),
     *                                  driver );
     * </pre>
     *
     * <p>
     * Or to tell the kernel about a newly created ONode:
     * </p>
     *
     * <pre>
     *     Kernel kernel = ...;
     *     ONode onode = ...;
     *     Module my_module = ...;
     *     kernel.objects ().register ( my_module.credentials (),
     *                                  onode.id (),
     *                                  onode );
     * </pre>
     *
     * <p>
     * Every kernel object lives in some namespace or other.
     * For example, every driver lives in the
     * KernelNamespaces.DRIVERS namespace.  Every ONode lives
     * in the namespace of its parent SuperBlock.  And so on.
     * Each identifier (Driver id, ONode id, and so on) must
     * be unique within its namespace.
     * </p>
     *
     * @see musaico.kernel.KernelNamespaces
     *
     * @param credentials The credentials of the owner of the
     *                    object to be stored.  Typically the
     *                    credentials of the kernel Module whose
     *                    code is registering the object.  User
     *                    credentials should never be used to
     *                    try to access kernel objects.
     *                    Must not be null.
     *
     * @param object_id The unique identifier of the object within
     *                  a specific namespace.  Must not be null.
     *
     * @param object The actual object to register in the kernel.
     *               Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (see above).
     *
     * @throws SecurityException If the credentials are not allowed
     *                           to store objects of the specified
     *                           type in this kernel.
     *
     * @throws KernelObjectException If an object with the same
     *                               identifier already exists in
     *                               the same namespace.
     */
    public abstract
        <KERNEL_OBJECT extends Object>
            void register (
                           Credentials credentials,
                           TypedIdentifier<KERNEL_OBJECT> object_id,
                           KERNEL_OBJECT object
                           )
        throws I18nIllegalArgumentException,
               SecurityException,
               KernelObjectException;


    /**
     * <p>
     * Removes the specified kernel object from the Kernel.
     * </p>
     *
     * <p>
     * For example, to remove a Driver from the kernel as the
     * Module which provides it is being unloaded:
     * </p>
     *
     * <pre>
     *     Kernel kernel = ...;
     *     Driver driver = ...;
     *     Module my_module = ...;
     *     kernel.objects ().unregister ( my_module.credentials (),
     *                                    driver.id () );
     * </pre>
     *
     * <p>
     * Or to tell the kernel about an ONode which has been deleted:
     * </p>
     *
     * <pre>
     *     Kernel kernel = ...;
     *     ONode onode = ...;
     *     Module my_module = ...;
     *     kernel.objects ().unregister ( my_module.credentials (),
     *                                    onode.id () );
     * </pre>
     *
     * @param credentials The credentials of the owner of the
     *                    object to be deleted.  Typically the
     *                    credentials of the kernel Module whose
     *                    code is deleting the object.  User
     *                    credentials should never be used to
     *                    try to access kernel objects.
     *                    Must not be null.
     *
     * @param object_id The unique identifier of the object within
     *                  a specific namespace.  Must not be null.
     *
     * @return The actual object which was un-registered from
     *         the kernel.  Never null.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (see above).
     *
     * @throws SecurityException If the credentials are not allowed
     *                           to delete the specified object.
     *
     * @throws KernelObjectException If no such object exists in the
     *                               kernel.
     */
    public abstract
        <KERNEL_OBJECT extends Object>
            KERNEL_OBJECT unregister (
                                      Credentials credentials,
                                      TypedIdentifier<KERNEL_OBJECT> object_id
                                      )
        throws I18nIllegalArgumentException,
               SecurityException,
               KernelObjectException;


    /**
     * <p>
     * Updates the specified kernel object.
     * </p>
     *
     * <p>
     * This method can be used to atomically replace the object
     * pointed to by an identifier.  Some kernels may also provide
     * "object updated" notifications to interested parties, so
     * even without replacing the object, important state changes
     * can be broadcast by this mechanism, if the kernel supports
     * kernel object notifications.
     * </p>
     *
     * <p>
     * For example, to notify the kernel of a major state change
     * to a Driver:
     * </p>
     *
     * <pre>
     *     Kernel kernel = ...;
     *     Driver driver = ...;
     *     Module my_module = ...;
     *     kernel.objects ().update ( my_module.credentials (),
     *                                driver.id (),
     *                                driver );
     * </pre>
     *
     * <p>
     * Or to atomically replace an ONode:
     * </p>
     *
     * <pre>
     *     Kernel kernel = ...;
     *     ONode new_onode = ...;
     *     Module my_module = ...;
     *     ONode old_onode =
     *         kernel.objects ().update ( my_module.credentials (),
     *                                    new_onode.id (),
     *                                    new_onode );
     * </pre>
     *
     * @param credentials The credentials of the owner of the
     *                    object to be updated.  Typically the
     *                    credentials of the kernel Module whose
     *                    code is updating the object.  User
     *                    credentials should never be used to
     *                    try to access kernel objects.
     *                    Must not be null.
     *
     * @param object_id The unique identifier of the object within
     *                  a specific namespace.  Must not be null.
     *
     * @param new_object The new object which will be referenced
     *                   by the specified identifier, replacing
     *                   the old object.  Must not be null.
     *
     * @return The old object which was referred to by the specified
     *         identifier before it was replaced by the new object.
     *         Never null.  (Note that old_object may equal new_object,
     *         in which case no real replacement was done; the method
     *         was meant to notify the kernel of some change of state
     *         in the object.)
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (see above).
     *
     * @throws SecurityException If the credentials are not allowed
     *                           to update the specified object.
     *
     * @throws KernelObjectException If the specified object does not
     *                               exist in the kernel.
     */
    public abstract
        <KERNEL_OBJECT extends Object>
            KERNEL_OBJECT update (
                                  Credentials credentials,
                                  TypedIdentifier<KERNEL_OBJECT> object_id,
                                  KERNEL_OBJECT new_object
                                  )
        throws I18nIllegalArgumentException,
               SecurityException,
               KernelObjectException;
}
