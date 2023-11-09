package musaico.kernel.module;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.i18n.log.Logger;

import musaico.io.Reference;
import musaico.io.TypedIdentifier;

import musaico.buffer.Buffer;

import musaico.field.FieldTypingEnvironment;

import musaico.kernel.KernelObjectsRegistry;

import musaico.kernel.PlatformOperations;

import musaico.kernel.driver.Driver;

import musaico.kernel.memory.Memory;

import musaico.kernel.objectsystem.objectsystemtype.ObjectSystemType;

import musaico.security.Credentials;


/**
 * <p>
 * Represents a Module loaded into the Musaico kernel.
 * </p>
 *
 * <p>
 * This module is the private internal object used by the
 * code loaded in by the module.  A Module should never ever
 * be exposed to anyone outside the contents of the module
 * itself.  In order to refer to a module externally, rely
 * on its ModuleIdentifier.  For example, if your module depends on
 * module xyz:
 * </p>
 *
 * <pre>
 *     Module my_module = ...;
 *     ModuleIdentifier xyz =
 *         new SimpleModuleIdentifier ( "xyz", Version.ANY );
 *     my_module.addDependency ( xyz );
 *     my_module.resolveDependencies ();
 * </pre>
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
 * Copyright (c) 2010, 2011 Johann Tienhaara
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
public interface Module
{
    /**
     * <p>
     * Adds a dependency from this module to another module.
     * The dependent module is not guaranteed to be started
     * up in the kernel until resolveDependencies () has been
     * invoked.
     * </p>
     *
     * @param module_id The id of the module on which this module
     *                  depends.  Must not be null.
     *
     * @return The ModuleDescription of the requested module, including
     *         the exact version which is (or will be) loaded into
     *         the kernel.  Never null.
     *
     * @throws I18nIllegalArgumentException If the parameters are invalid
     *                                      (see above).
     *
     * @throws ModuleException If the specified module cannot be found.
     */
    public abstract ModuleDescription addDependency (
                                                     ModuleIdentifier dependency
                                                     )
        throws I18nIllegalArgumentException,
               ModuleException;


    /**
     * <p>
     * Returns the Java ClassLoader for this module.
     * </p>
     *
     * <p>
     * Every thread created for use by this module, and every thread
     * used by its contents (for example the startup thread) shall
     * have this class loader as its default.
     * </p>
     *
     * @return The ClassLoader used to load this module's contents.
     *         Never null.
     */
    public abstract ClassLoader classLoader ();


    /**
     * <p>
     * Creates a driver from the specified Java class name.
     * </p>
     *
     * @see musaico.kernel.driver.Driver
     *
     * <p>
     * The driver must have a single argument constructor,
     * allowing its parent module to be passed in.
     * </p>
     *
     * @param driver_class_name The fully qualified name of the
     *                          Java class providing the driver definition.
     *                          Must not be null.
     *
     * @return The newly created Driver.  Never null.
     *
     * @throws ModuleOperationException If the specified driver cannot
     *                                  be created.
     */
    public abstract Driver createDriver (
                                         String driver_class_name
                                         )
        throws ModuleOperationException;


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
     *     Driver driver = ...;
     *     Module my_module = ...;
     *     my_module.createKernelObject ( driver.id (),
     *                                    driver );
     * </pre>
     *
     * <p>
     * Or to tell the kernel about a newly created ONode:
     * </p>
     *
     * <pre>
     *     ONode onode = ...;
     *     Module my_module = ...;
     *     my_module.createKernelObject ( onode.id (),
     *                                    onode );
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
     * @param object_id The unique identifier of the object within
     *                  a specific namespace.  Must not be null.
     *
     * @param object The actual object to register in the kernel.
     *               Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (see above).
     *
     * @throws ModuleOperationException If the credentials are not allowed
     *                                  to store objects of the specified
     *                                  type in this kernel or if
     *                                  an object with the same
     *                                  identifier already exists in
     *                                  the same namespace.
     */
    public abstract
        <KERNEL_OBJECT extends Object>
            void createKernelObject (
                                     TypedIdentifier<KERNEL_OBJECT> object_id,
                                     KERNEL_OBJECT object
                                     )
        throws I18nIllegalArgumentException,
               ModuleOperationException;


    /**
     * <p>
     * Returns the credentials granted to this module by the kernel.
     * </p>
     *
     * @return This module's credentials.  Never null.
     */
    public abstract Credentials credentials ();


    /**
     * <p>
     * Deletes the specified temporary Credentials.
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
     * @param temporary_credentials The credentials whose privileges
     *                              will be revoked.  Must not be null.
     *
     * @throws ModuleException If the specified credentials are
     *                         invalid (see above) or if the
     *                         credentials cannot be deleted
     *                         (for example because they do not
     *                         exist in the kernel or do not
     *                         have privileges to allow
     *                         deleting themselves).
     */
    public abstract void deleteCredentials (
                                            Credentials credentials
                                            )
        throws ModuleException;


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
     *     Driver driver = ...;
     *     Module my_module = ...;
     *     my_module.deleteKernelObject ( driver.id () );
     * </pre>
     *
     * <p>
     * Or to tell the kernel about an ONode which has been deleted:
     * </p>
     *
     * <pre>
     *     ONode onode = ...;
     *     Module my_module = ...;
     *     my_module.deleteKernelObject ( onode.id () );
     * </pre>
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
     * @throws ModuleOperationException If the credentials are not allowed
     *                                  to delete the specified object
     *                                  or if no such object exists in the
     *                                  kernel.
     */
    public abstract
        <KERNEL_OBJECT extends Object>
            KERNEL_OBJECT deleteKernelObject (
                                              TypedIdentifier<KERNEL_OBJECT> object_id
                                              )
        throws I18nIllegalArgumentException,
               ModuleOperationException;


    /**
     * <p>
     * Returns the description of this module.
     * </p>
     *
     * @return This module's description, including the id.
     *         Never null.
     */
    public abstract ModuleDescription description ();


    /**
     * <p>
     * Duplicates this module's Credentials with a different
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
     * @return The new set of credentials with privileges
     *         copied from this module's credentials.  Never null.
     *
     * @throws ModuleException If this module's credentials
     *                         cannot be copied
     *                         (for example because they do not
     *                         have privileges to allow
     *                         creating copies).
     */
    public abstract Credentials duplicateCredentials ()
        throws ModuleException;


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
     *     DriverIdentifier driver_id = ...;
     *     Module my_module = ...;
     *     Driver driver =
     *         my_module.getKernelObject ( driver_id );
     * </pre>
     *
     * <p>
     * Or to retrieve a particular ONode:
     * </p>
     *
     * <pre>
     *     ONodeIdentifier onode_id = ...;
     *     Module my_module = ...;
     *     ONode onode =
     *         my_module.getKernelObject ( onode_id );
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
     * @param object_id The unique identifier of the object within
     *                  a specific namespace.  Must not be null.
     *
     * @return The requested object.  Never null.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (see above).
     *
     * @throws ModuleOperationException If the credentials are not allowed
     *                                  to retrieve the specified object or
     *                                  if the requested object does
     *                                  not exist in the kernel.
     */
    public abstract
        <KERNEL_OBJECT extends Object>
            KERNEL_OBJECT getKernelObject (
                                           TypedIdentifier<KERNEL_OBJECT> object_id
                                           )
        throws I18nIllegalArgumentException,
               ModuleOperationException;


    /**
     * <p>
     * Returns the unique identifier of this module.
     * </p>
     *
     * @return This module's unique (name,version) identifier.
     *         Never null.
     */
    public abstract ModuleIdentifier id ();


    /**
     * <p>
     * Returns the lookup of objects from the kernel.
     * </p>
     *
     * @return The kernel's lookup of objects.  Never null.
     */
    public abstract KernelObjectsRegistry kernelObjects ();


    /**
     * <p>
     * Returns the standard log message printer for this kernel.
     * </p>
     */
    public abstract Logger logger ();


    /**
     * <p>
     * Returns the physical Memory allocator and free-er for this Module.
     * </p>
     *
     * <p>
     * Typically small amounts of memory, anything that needs
     * to be kept un-swapped and memory which would be needed
     * too quickly or too frequently to wait for swapping would
     * be allocated from this physical pool.  Larger amounts of
     * memory and memory which is not time critical or used
     * infrequently should typically be allocated from the virtual
     * memory pool instead.
     * </p>
     *
     * @return This Module's physical Memory manager.  Never null.
     */
    public abstract Memory physicalMemory();


    /**
     * <p>
     * Returns the system and platform operations for the kernel
     * backing this module, providing access to system time and
     * so on.
     * </p>
     *
     * @return The platform operations for the kernel backing
     *         this module.  Never null.
     */
    public abstract PlatformOperations platform ();


    /**
     * <p>
     * Registers an ObjectSystemType, so that the kernel
     * can mount object systems of the specified type.
     * </p>
     *
     * @param object_system_type The ObjectSystemType which
     *                           can be used to mount, unmount and
     *                           maintain object systems of a
     *                           specific type.  Must not be null.
     *
     * @return This Module.  Never null.
     *
     * @throws I18nIllegalArgumentException If the parameters are invalid.
     *
     * @throws ModuleOperationException If the specified object system
     *                                  type has already been registered
     *                                  in the kernel, or if something
     *                                  goes horribly wrong during
     *                                  registration.
     */
    public abstract Module registerObjectSystemType (
                                                     ObjectSystemType object_system_type
                                                     )
        throws ModuleOperationException, I18nIllegalArgumentException;


    /**
     * <p>
     * Resolves all of this module's depenencies, asking the
     * kernel to load and start all dependent modules.
     * </p>
     *
     * @throws ModuleException If loading or starting any of the
     *                         dependencies failed.
     */
    public abstract void resolveDependencies ()
        throws ModuleException;


    /**
     * <p>
     * Starts this module, possibly resolving module dependencies,
     * creating drivers, spawning tasks, and so on.  Typically the
     * kernel calls this method at the end of
     * <code> Kernel.loadModule () </code>.
     * </p>
     *
     * @throws ModuleException If any dependency could not be loaded
     *                         and started, or if any drivers or
     *                         tasks or object system factories and
     *                         so on could not be created and initialized,
     *                         and so on.
     */
    public abstract void start ()
        throws ModuleException;


    /**
     * <p>
     * Stops this module, possibly destroying drivers, killing tasks,
     * and so on.  Typically the kernel calls this method at the end of
     * <code> Kernel.unloadModule () </code>.
     * </p>
     *
     * @throws ModuleException If any drivers or tasks or object
     *                         system factories were in use and so could
     *                         not be shut down, and so on.
     */
    public abstract void stop ()
        throws ModuleException;


    /**
     * <p>
     * Handles notification that a method or block of code
     * has been entered.  Typically a module will, when tracing
     * has been turned on, log this to a kernel log file.
     * </p>
     *
     * @param block_name The name of the block of code entered.
     *                   Typically "source_file.method_name"
     *                   (such as "FooDriver.open()").
     *                   Must not be null.
     */
    public abstract void traceEnter (
                                     String block_name
                                     );


    /**
     * <p>
     * Handles notification that a method or block of code
     * has been exited.  Typically a module will, when tracing
     * has been turned on, log this to a kernel log file.
     * </p>
     *
     * @param block_name The name of the block of code exited.
     *                   Typically "source_file.method_name"
     *                   (such as "FooDriver.open()").
     *                   Must not be null.
     */
    public abstract void traceExit (
                                    String block_name
                                    );


    /**
     * <p>
     * Handles notification that a method or block of code
     * has failed.  Typically a module will, when tracing
     * has been turned on, log this to a kernel log file.
     * </p>
     *
     * @param block_name The name of the block of code which failed.
     *                   Typically "source_file.method_name"
     *                   (such as "FooDriver.open()").
     *                   Must not be null.
     */
    public abstract void traceFail (
                                    String block_name
                                    );


    /**
     * <p>
     * The typing system provided for use inside the module.
     * </p>
     *
     * <p>
     * Fields can be <code>create</code>d using this module's typing
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
     * @return The TypingEnvironment used in this module.
     *         Must not be shared Kernels other than our own,
     *         and must not be shared with applications.
     *         Never null.
     */
    public abstract FieldTypingEnvironment types ();


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
     *     Driver driver = ...;
     *     Module my_module = ...;
     *     my_module.updateKernelObject ( driver.id (),
     *                                    driver );
     * </pre>
     *
     * <p>
     * Or to atomically replace an ONode:
     * </p>
     *
     * <pre>
     *     ONode new_onode = ...;
     *     Module my_module = ...;
     *     ONode old_onode =
     *         my_module.updateKernelObject ( new_onode.id (),
     *                                        new_onode );
     * </pre>
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
     * @throws ModuleOperationException If the credentials are not allowed
     *                                  to update the specified object or
     *                                  if the specified object does not
     *                                  exist in the kernel.
     */
    public abstract
        <KERNEL_OBJECT extends Object>
            KERNEL_OBJECT updateKernelObject (
                                              TypedIdentifier<KERNEL_OBJECT> object_id,
                                              KERNEL_OBJECT new_object
                                              )
        throws I18nIllegalArgumentException,
               ModuleOperationException;


    /**
     * <p>
     * Returns the virtual Memory allocator and free-er for this Module.
     * </p>
     *
     * <p>
     * Typically small amounts of memory, anything that needs
     * to be kept un-swapped and memory which would be needed
     * too quickly or too frequently to wait for swapping would
     * be allocated from the physical pool.  Larger amounts of
     * memory and memory which is not time critical or used
     * infrequently should typically be allocated from this virtual
     * memory pool instead.
     * </p>
     *
     * @return This Module's virtual Memory manager.  Never null.
     */
    public abstract Memory virtualMemory();
}
