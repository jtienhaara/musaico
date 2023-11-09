package musaico.kernel.objectsystem.objectsystemtype;

import java.io.Serializable;


import musaico.io.Identifier;
import musaico.io.Progress;

import musaico.buffer.Buffer;

import musaico.kernel.objectsystem.onode.OEntry;

import musaico.kernel.objectsystem.superblock.SuperBlock;
import musaico.kernel.objectsystem.superblock.SuperBlockIdentifier;

import musaico.mutex.Mutex;

import musaico.security.Credentials;
import musaico.security.Security;


/**
 * <p>
 * An object system type, used to mount and unmount
 * inidividual object system instances.
 * </p>
 *
 * <p>
 * An object system provides kernel facilities for applications
 * to transmit and receive objects to and from various drivers (including
 * file systems, file formats, database systems, network protocols,
 * and so on).
 * </p>
 *
 * <p>
 * Any given object system type is driver-agnostic.  Some object
 * system types should be designed to work across platforms
 * (for example, in C as well as Java, JavaScript, and so on).
 * Others are by their very nature platform-specific.
 * Typically platform-specific object system types are more
 * useful to application developers, since they are usually
 * efficient and always tailored to the platform (for example,
 * a Java-only object system might provide cached Java objects to
 * manipulate directly through their method calls).
 * </p>
 *
 * <p>
 * An object system type provides methods to mount
 * and unmount super blocks in the object system hierarchy.
 * For example, an object system might be mounted at
 * "/astronomy/stars".  That object system might have
 * numerous hierarchical objects, rooted at "/astronomy/stars",
 * which would all be unmounted when its object system
 * type's unmount method is invoked.
 * </p>
 *
 * <p>
 * Typically ObjectSystemTypes are registered during module
 * initialization, by calling
 * <code> Module.registerObjectSystemType ( ... ) </code>.
 * </p>
 *
 * @see musaico.kernel.module.Module
 *
 * <p>
 * An ObjectSystemType is the Musaico equivalent of a Linux
 * file_system_type.
 * </p>
 *
 *
 * <p>
 * In Java, every ObjectSystemType must be Serializable in order
 * to play nicely across RMI.  Even a non-distributed object system type
 * must have a Serializable ObjectSystemType, so that the
 * knowledge of how to create and manipulate them can be shared across
 * nodes in a distributed kernel.
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
 * Copyright (c) 2009, 2010, 2011 Johann Tienhaara
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
public interface ObjectSystemType
    extends Serializable
{
    /**
     * <p>
     * Returns the object system type identifier which uniquely
     * identifies this ObjectSystemType within the kernel.
     * </p>
     *
     * @return The unique reference to this object system type.
     *         Never null.
     */
    public abstract ObjectSystemTypeIdentifier id ();


    /**
     * <p>
     * All operations which need to use this ObjectSystemType with mutual
     * exclusion must acquire a MutexLock on this (and any ONodes or
     * other objects for locking at the same time) by doing the
     * following:
     * </p>
     *
     * <pre>
     *     ObjectSystemType object_system_type = ...;
     *     MutexLock mutex_lock =
     *       new ObjectSystemTypeLock ( object_system_type.mutex () );
     *     mutex_lock.lock ( ...timeout in milliseconds... );
     *     try
     *     {
     *         ...do stuff to object system type...
     *     }
     *     finally
     *     {
     *         mutex_lock.unlock ();
     *     }
     * </pre>
     *
     * <p>
     * The ObjectSystemType and multiple ONodes can be locked at once with:
     * </p>
     *
     *
     * <pre>
     *     ObjectSystemType object_system_type = ...;
     *     ONode onode1 = ...;
     *     ONode onode2 = ...;
     *     ONode onode3 = ...;
     *     MutexLock mutex_lock =
     *       new ObjectSystemTypeLock ( object_system_type.mutex (),
     *                                  onode1.mutex (),
     *                                  onode2.mutex (),
     *                                  onode3.mutex () );
     *     mutex_lock.lock ( ...timeout in milliseconds... );
     *     try
     *     {
     *         ...do stuff to object system type and onodes...
     *     }
     *     finally
     *     {
     *         mutex_lock.unlock ();
     *     }
     * </pre>
     *
     * <p>
     * The ObjectSystemTypeLock throws an ObjectSystemTypeException if
     * it takes too long to acquire the lock(s).  Alternatively
     * use the RecordLock to throw a RecordOperationException
     * (for example from the methods specified by the Record
     * interface which must be implemented by ONode data and metadata)
     * or ONodeLock to throw an ONodeOperationException.
     * </p>
     *
     * @param credentials The user or module credentials requesting
     *                    this object system type's mutex.
     *                    Must not be null.
     *
     * @return This ObjectSystemType's Mutex.  Never null.
     *
     * @throws ObjectSystemTypeSecurityException If the specified
     *                                           credentials do not
     *                                           have permission to
     *                                           access this object
     *                                           system type's mutex.
     */
    public abstract Mutex mutex (
                                 Credentials credentials
                                 )
        throws ObjectSystemTypeSecurityException;


    /**
     * <p>
     * Mounts an object system of this type at the specified
     * mounting point.
     * </p>
     *
     * <p>
     * A SuperBlock is created and populated with object data
     * from the specified underlying block driver.
     * </p>
     *
     * <p>
     * This operation blocks the caller.
     * </p>
     *
     * <p>
     * This operation takes care to lock the object
     * system type's mutex lock (which may already be
     * locked by this thread, if some larger operation
     * involving the object system type is afoot).
     * </p>
     *
     * @param credentials Who is asking for the operation, such as
     *                    a user Credentials or a Module's Credentials.
     *                    Must not be null.
     *
     * @param progress The progress meter which can be used by
     *                 callers to monitor how close to completion
     *                 the requested operation is.  Must not be null.
     *
     * @param mount_point The entry in the object systems
     *                    hierarchy where the new object
     *                    system will be rooted.  This
     *                    must point to an object (not a flat
     *                    record) which is empty and will,
     *                    after mounting, contain the
     *                    hierarchy of objects and records
     *                    of the new object system.
     *                    Must not be null.
     *
     * @param block_driver_id A reference to the underlying block
     *                        driver which will back the new
     *                        object system.  Must not be null.
     *                        May refer to "no block driver" or
     *                        "anonymous".  Must refer to a valid
     *                        block driver in the kernel.
     *
     * @param mount_options A Buffer containing the options for
     *                      mounting.  Contains both optional
     *                      parameters from user space and
     *                      parameters useful to the kernel
     *                      (such as a Cursor pointing to the
     *                      mount position of the new object
     *                      system).  The Buffer must be allocated
     *                      in kernel space, even if some of the
     *                      data originated in user space.
     *                      Must not be null.
     *
     * @return The newly created, populated and mounted SuperBlock
     *         of this object system type.  Never null.
     *
     * @throws ObjectSystemTypeSecurityException If the specified
     *                                           credentials do not
     *                                           have permission to
     *                                           mount a new instance
     *                                           of this object system
     *                                           type at the specified
     *                                           mount point.
     *
     * @throws ObjectSystemTypeException If the underlying block driver
     *                                   throws any exceptions, or if
     *                                   generally anything goes
     *                                   horribly wrong during the
     *                                   mount operation.
     */
    public abstract SuperBlock mount (
                                      Credentials credentials,
                                      Progress progress,
                                      OEntry mount_point,
                                      OEntry driver_path, // !!!! TEMPORARY HACK -- NEED TO ALLOW MULTIPLE DRIVERS FOR MULTIPLE SWAP LEVELS, MAYBE ALSO STACKING...  MAYBE A GRAPH OF DRIVERS?!?!?
                                      Buffer mount_options
                                      )
        throws ObjectSystemTypeSecurityException,
               ObjectSystemTypeException;


    /**
     * <p>
     * Returns the security manager for this object system type.
     * </p>
     *
     * @return This object system type's security manager,
     *         which controls access to mount () and unmount ()
     *         and so on.  Never null.
     */
    public abstract Security<ObjectSystemTypeFlag> security ();


    /**
     * <p>
     * Unmounts the specified object system.
     * </p>
     *
     * <p>
     * The specified SuperBlock will be synchronized and then
     * all its ONodes freed as part of the unmount operation.
     * </p>
     *
     * <p>
     * This operation blocks the caller.
     * </p>
     *
     * <p>
     * This operation takes care to lock the object
     * system type's mutex lock (which may already be
     * locked by this thread, if some larger operation
     * involving the object system type is afoot).
     * </p>
     *
     * @param credentials Who is asking for the operation, such as
     *                    a user Credentials or a Module's Credentials.
     *                    Must not be null.
     *
     * @param progress The progress meter which can be used by
     *                 callers to monitor how close to completion
     *                 the requested operation is.  Must not be null.
     *
     * @param super_block_id The id of the object system
     *                       to unmount.  Must not be null.
     *                       Must be a valid, mounted
     *                       object system.  Must be
     *                       an object system of this type.
     *
     * @throws ObjectSystemTypeSecurityException If the specified
     *                                           credentials do not
     *                                           have permission to
     *                                           unmount the specified
     *                                           SuperBlock.
     *
     * @throws ObjectSystemTypeException If the underlying block driver
     *                                   throws any exceptions, or if
     *                                   generally anything goes
     *                                   horribly wrong during the
     *                                   mount operation.
     */
    public abstract void unmount (
                                  Credentials credentials,
                                  Progress progress,
                                  SuperBlockIdentifier super_block_id
                                  )
        throws ObjectSystemTypeSecurityException,
               ObjectSystemTypeException;


    /**
     * !!! methods to add in future:
        int fs_flags;
        struct module *owner;
        struct file_system_type * next;
        struct list_head fs_supers;

        struct lock_class_key s_lock_key;
        struct lock_class_key s_umount_key;

        struct lock_class_key i_lock_key;
        struct lock_class_key i_mutex_key;
        struct lock_class_key i_mutex_dir_key;
        struct lock_class_key i_alloc_sem_key;
        !!! */
}
