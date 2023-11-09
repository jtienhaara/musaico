package musaico.kernel.module;

import java.io.Serializable;

import java.lang.reflect.Constructor;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.i18n.log.Logger;
import musaico.i18n.log.StandardOutLogger;

import musaico.i18n.message.StandardMessageFormat;

import musaico.io.TypedIdentifier;

import musaico.field.FieldTypingEnvironment;
import musaico.field.SimpleFieldTypingEnvironment;

import musaico.io.references.Version;

import musaico.kernel.KernelObjectsRegistry;
import musaico.kernel.PlatformOperations;

import musaico.kernel.driver.Driver;

import musaico.kernel.memory.Memory;
import musaico.kernel.memory.MemoryException;
import musaico.kernel.memory.MemoryIdentifier;
import musaico.kernel.memory.MemoryPermission;

import musaico.kernel.memory.physical.SimplePhysicalMemory;

import musaico.kernel.module.Module;
import musaico.kernel.module.ModuleDescription;
import musaico.kernel.module.ModuleIdentifier;
import musaico.kernel.module.SimpleModuleIdentifier;

import musaico.kernel.module.descriptions.Copyright;
import musaico.kernel.module.descriptions.License;
import musaico.kernel.module.descriptions.SimpleModuleDescription;

import musaico.kernel.objectsystem.objectsystemtype.ObjectSystemType;

import musaico.kernel.platforms.SimplePlatformOperations;

import musaico.kernel.registries.SimpleKernelObjectsRegistry;

import musaico.security.Credentials;
import musaico.security.NoSecurity;

import musaico.time.AbsoluteTime;


/**
 * <p>
 * Module which can't actually do anything.  For testing.
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
 * Copyright (c) 2011 Johann Tienhaara
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
public class FakeModule
    implements Module
{
    /** The unique identifier for this Module within its Kernel. */
    private final ModuleIdentifier id =
        new SimpleModuleIdentifier ( "Fake", new Version ( "1.0.0" ) );

    /** The full description of this Module, including author,
     *  license, and so on. */
    private final ModuleDescription description =
        new SimpleModuleDescription ( new StandardMessageFormat (),
                                      ModuleDescription.AUTHORS,
                                      new String [] { "Johann Tienhaara" },
                                      ModuleDescription.COPYRIGHTS,
                                      new Copyright [] { new Copyright ( 2011, "Johann Tienhaara" ) },
                                      ModuleDescription.DEPRECATED_SINCE,
                                      AbsoluteTime.now (),
                                      ModuleDescription.ID,
                                      this.id,
                                      // Let the module description fill
                                      // in the full license text.
                                      ModuleDescription.LICENSE_TYPES,
                                      new License [] { License.GPL_3 },
                                      ModuleDescription.MAINTAINERS,
                                      new String [] { "Johann Tienhaara" },
                                      ModuleDescription.OVERVIEW,
                                      "Fake module.  Used for testing only!",
                                      ModuleDescription.URL_BINARY,
                                      "file:///dev/null",
                                      ModuleDescription.URL_SOURCE,
                                      "file:///dev/null" );

    /** The typing environment in which to run this module. */
    private final FieldTypingEnvironment env =
            new SimpleFieldTypingEnvironment ();

    /** The Memory manager for this module.  Serves for both
     *  physical and virtual memory! */
    MemoryIdentifier memory_id =
        new MemoryIdentifier ( "physical_memory" );
    private final Memory memory =
        new SimplePhysicalMemory ( memory_id,
                                   new NoSecurity<MemoryPermission> (),
                                   env, 0L ); // Unlimited

    /** The logger for this module. */
    private final Logger logger = new StandardOutLogger ();

    /** The platform/system operations for this module. */
    private final PlatformOperations platform =
        new SimplePlatformOperations ();


    /**
     * @see musaico.module.Module#addDependency(musaico.kernel.module.ModuleIdentifier)
     */
    @Override
    public ModuleDescription addDependency (
                                            ModuleIdentifier dependency
                                            )
        throws ModuleException
    {
        throw new ModuleException ( "Module [%module%] could not add dependency [%module_id%]",
                                    "module", this,
                                    "module_id", dependency );
    }


    /**
     * @see musaico.module.Module#classLoader()
     */
    @Override
    public ClassLoader classLoader ()
    {
        return Thread.currentThread ().getContextClassLoader();
    }


    /**
     * @see musaico.module.Module#createDriver(String)
     */
    @Override
    public Driver createDriver (
                                String driver_class_name
                                )
        throws ModuleOperationException
    {
        throw new ModuleOperationException ( "!!! FAKE MODULE Cannot create driver for class [%driver_class%] from module [%module%]",
                                             "driver_class", driver_class_name,
                                             "module", this.id () );
    }


    /**
     * @see musaico.kernel.module#createKernelObject(TypedIdentifier,Object)
     */
    @Override
    public
        <KERNEL_OBJECT extends Object>
            void createKernelObject (
                                     TypedIdentifier<KERNEL_OBJECT> object_id,
                                     KERNEL_OBJECT object
                                     )
        throws I18nIllegalArgumentException,
               ModuleOperationException
    {
        // Do nothing.
    }


    /**
     * @see musaico.module.Module#credentials()
     */
    @Override
    public Credentials credentials ()
    {
        return new FakeCredentials ( this.id () );
    }


    /**
     * @see musaico.module.Module#deleteCredentials()
     */
    @Override
    public void deleteCredentials (
                                   Credentials credentials
                                   )
        throws ModuleException
    {
        // What do we care, we're just a fake!
    }


    /**
     * @see musaico.kernel.Module#deleteKernelObject(TypedIdentifier)
     */
    @Override
    public
        <KERNEL_OBJECT extends Object>
            KERNEL_OBJECT deleteKernelObject (
                                              TypedIdentifier<KERNEL_OBJECT> object_id
                                              )
        throws I18nIllegalArgumentException,
               ModuleOperationException
    {
        return null;
    }


    /**
     * @see musaico.module.Module#description()
     */
    @Override
    public ModuleDescription description ()
    {
        return this.description;
    }


    /**
     * @see musaico.module.Module#duplicateCredentials()
     */
    @Override
    public Credentials duplicateCredentials ()
        throws ModuleException
    {
        // What do we care, we're just a fake!
        return this.credentials ();
    }


    /**
     * @see musaico.kernel.module.Module#getKernelObject(TypedIdentifier)
     */
    @Override
    public
        <KERNEL_OBJECT extends Object>
            KERNEL_OBJECT getKernelObject (
                                           TypedIdentifier<KERNEL_OBJECT> object_id
                                           )
        throws I18nIllegalArgumentException,
               ModuleOperationException
    {
        return null;
    }


    /**
     * @see musaico.module.Module#id()
     */
    @Override
    public ModuleIdentifier id ()
    {
        return this.id;
    }


    /**
     * @see musaico.module.Module#kernelObjects()
     */
    @Override
    public KernelObjectsRegistry kernelObjects ()
    {
        return new SimpleKernelObjectsRegistry ( null );
    }


    /**
     * @see musaico.module.Module#logger()
     */
    @Override
    public Logger logger ()
    {
        return this.logger;
    }


    /**
     * @see musaico.module.Module#physicalMemory()
     */
    @Override
    public Memory physicalMemory ()
    {
        return this.memory;
    }


    /**
     * @see musaico.module.Module#platform()
     */
    @Override
    public PlatformOperations platform ()
    {
        return this.platform;
    }


    /**
     * @see musaico.module.Module#registerObjectSystemType(musaico.kernel.objectsystem.ObjectSystemType)
     */
    @Override
    public Module registerObjectSystemType (
                                            ObjectSystemType object_system_type
                                            )
        throws ModuleOperationException, I18nIllegalArgumentException
    {
        // Do nothing.
        return this;
    }


    /**
     * @see musaico.module.Module#resolveDependencies()
     */
    @Override
    public void resolveDependencies ()
        throws ModuleException
    {
        // Sorry bub, you're on your own!
    }


    /**
     * @see musaico.module.Module#start()
     */
    @Override
    public void start ()
        throws ModuleException
    {
        // Do nothing.
    }


    /**
     * @see musaico.module.Module#stop()
     */
    @Override
    public void stop ()
        throws ModuleException
    {
        // Do nothing.
    }


    /**
     * @see musaico.module.Module#traceEnter(java.lang.String)
     */
    @Override
    public void traceEnter (
                            String block_name
                            )
    {
        // Do nothing.
    }


    /**
     * @see musaico.module.Module#traceExit(java.lang.String)
     */
    @Override
    public void traceExit (
                           String block_name
                           )
    {
        // Do nothing.
    }


    /**
     * @see musaico.module.Module#traceFail(java.lang.String)
     */
    @Override
    public void traceFail (
                           String block_name
                           )
    {
        // Do nothing.
    }


    /**
     * @see musaico.module.Module#types()
     */
    @Override
    public FieldTypingEnvironment types ()
    {
        return this.memory.environment ();
    }


    /**
     * @see musaico.module.Module#updateKernelObject(TypeIdentifier,Object)
     */
    @Override
    public
        <KERNEL_OBJECT extends Object>
            KERNEL_OBJECT updateKernelObject (
                                              TypedIdentifier<KERNEL_OBJECT> object_id,
                                              KERNEL_OBJECT new_object
                                              )
        throws I18nIllegalArgumentException,
               ModuleOperationException
    {
        return null;
    }


    /**
     * @see musaico.module.Module#virtualMemory()
     */
    @Override
    public Memory virtualMemory ()
    {
        return this.memory;
    }
}
