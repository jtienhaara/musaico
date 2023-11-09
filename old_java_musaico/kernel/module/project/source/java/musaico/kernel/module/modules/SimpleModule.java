package musaico.kernel.module.modules;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java.lang.reflect.Constructor;


import musaico.i18n.exceptions.I18nIllegalArgumentException;
import musaico.i18n.exceptions.I18nIOException;

import musaico.i18n.log.Level;
import musaico.i18n.log.Logger;

import musaico.i18n.text.I18nString;

import musaico.io.TypedIdentifier;

import musaico.field.FieldTypingEnvironment;

import musaico.kernel.Kernel;
import musaico.kernel.KernelObjectException;
import musaico.kernel.KernelObjectsRegistry;
import musaico.kernel.PlatformOperations;

import musaico.kernel.driver.Driver;

import musaico.kernel.memory.Memory;
import musaico.kernel.memory.MemoryException;

import musaico.kernel.module.Module;
import musaico.kernel.module.ModuleDescription;
import musaico.kernel.module.ModuleException;
import musaico.kernel.module.ModuleIdentifier;
import musaico.kernel.module.ModuleOperationException;

import musaico.kernel.module.descriptions.SimpleModuleDescription;

import musaico.kernel.objectsystem.objectsystemtype.ObjectSystemType;

import musaico.security.Credentials;
import musaico.security.SecurityException;


/**
 * <p>
 * Represents a bare bones Module loaded into the Musaico kernel.
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
public class SimpleModule
    implements Module
{
    /** Synchronize critical sections on this lock: */
    private final Serializable lock = new String ();

    /** The description for this Module, including license, copyright
     *  holders, and so on. */
    private final ModuleDescription description;

    /** The Kernel in which this Module resides. */
    private final Kernel kernel;

    /** The credentials we can use to access the kernel.
     *  Do NOT expose these to any component outside this module unless
     *  you want other componets traipsing aroud pretending to be us! */
    private final Credentials credentials;

    /** Temporary credentials which we can expose to outside
     *  components temporarily, and then delete them when
     *  we are done an operation. */
    private final Set<Credentials> temporaryCredentials =
        new HashSet<Credentials> ();

    /** The ClassLoader to use when loading classes or other
     *  resources. */
    private final ClassLoader classLoader;

    /** The physical Memory manager for this module. */
    private final Memory physicalMemory;

    /** The virtual Memory manager for this module. */
    private final Memory virtualMemory;

    /** Any dependencies which this module has on other modules. */
    private final List<ModuleDescription> dependencies =
        new ArrayList<ModuleDescription> ();


    /**
     * <p>
     * Creates a new SimpleModule with the specified identifier,
     * kernel, class loader, and memory manager.
     * </p>
     *
     * @param description The full description of this Module, including
     *                    license, copyright holders, and so on.
     *                    Must not be null.  Must have all the
     *                    parameters listed in ModuleDescription.REQUIRED.
     *
     * @param kernel The Kernel which is loading this module.
     *               Provides typing environment, logging and so on.
     *               Must not be null.
     *
     * @param credentials The kernel-provided credentials which allow
     *                    us to make requests of the kernel.
     *                    Must not be null.
     *
     * @param class_loader The ClassLoader to load classes in with.
     *                     Must not be null.
     *
     * @param physical_memory The physical Memory manager for
     *                        this module.  Controls allocation
     *                        and freeing of physical memory.
     *                        Must not be null.
     *
     * @param virtual_memory The virtual Memory manager for
     *                       this module.  Controls allocation
     *                       and freeing of virtual memory.
     *                       Must not be null.
     */
    public SimpleModule (
                         ModuleDescription description,
                         Kernel kernel,
                         Credentials credentials,
                         ClassLoader class_loader,
                         Memory physical_memory,
                         Memory virtual_memory
                         )
    {
        if ( description == null
             || kernel == null
             || credentials == null
             || class_loader == null
             || physical_memory == null
             || virtual_memory == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create SimpleModule with description [%description_class%][%description_hash_code%] kernel [%kernel%] credentials [%credentials%] class loader [%class_loader%] physical memory manager [%physical_memory%] virtual memory manager [%virtual_memory%]",
                                                     "description_class", ( description == null ? null : description.getClass () ),
                                                     "description_hash_code", ( description == null ? null : description.hashCode () ),
                                                     "kernel", kernel,
                                                     "credentials", credentials,
                                                     "class_loader", class_loader,
                                                     "physical_memory", physical_memory,
                                                     "virtual_memory", virtual_memory );
        }

        // Make sure the description is complete.
        // Throw an I18nIllegalArgumentException if anything is missing.
        SimpleModuleDescription.validateDescription ( description );

        this.description = description;
        this.kernel = kernel;
        this.credentials = credentials;
        this.classLoader = class_loader;
        this.physicalMemory = physical_memory;
        this.virtualMemory = virtual_memory;
    }


    /**
     * @see musaico.module.Module#addDependency(musaico.kernel.module.ModuleIdentifier)
     */
    @Override
    public ModuleDescription addDependency (
                                            ModuleIdentifier dependency
                                            )
        throws I18nIllegalArgumentException,
               ModuleException
    {
        if ( dependency == null )
        {
            throw new I18nIllegalArgumentException ( "Module [%module%] could not add dependency [%module_id%]",
                                                     "module", this,
                                                     "module_id", dependency );
        }

        final ModuleDescription actual_module_description;
        try
        {
            actual_module_description = (ModuleDescription)
                this.kernel.modules ().find ( this.credentials,
                                              dependency );
        }
        catch ( ClassCastException e )
        {
            throw new ModuleException ( "Module [%module%] could not add dependency [%module_id%]",
                                        "module", this,
                                        "module_id", dependency,
                                        "cause", e );
        }
        catch ( SecurityException e )
        {
            throw new ModuleException ( "Module [%module%] could not add dependency [%module_id%]",
                                        "module", this,
                                        "module_id", dependency,
                                        "cause", e );
        }
        catch ( I18nIOException e )
        {
            throw new ModuleException ( "Module [%module%] could not add dependency [%module_id%]",
                                        "module", this,
                                        "module_id", dependency,
                                        "cause", e );
        }

        synchronized ( this.lock )
        {
            this.dependencies.add ( actual_module_description );
        }

        return actual_module_description;
    }


    /**
     * @see musaico.module.Module#classLoader()
     */
    @Override
    public ClassLoader classLoader ()
    {
        return this.classLoader;
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
        try
        {
            Class<Driver> driver_class = (Class<Driver>)
                this.classLoader.loadClass ( driver_class_name );

            Constructor<Driver> constructor = (Constructor<Driver>)
                driver_class.getConstructor ( new Class [] { Module.class } );

            Driver driver = constructor.newInstance ( new Object [] { this } );

            return driver;
        }
        catch ( Exception e )
        {
            throw new ModuleOperationException ( "Cannot create driver for class [%driver_class%] from module [%module%]",
                                                 "driver_class", driver_class_name,
                                                 "module", this.id (),
                                                 "cause", e );
        }
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
        try
        {
            this.kernel.objects ().register ( this.credentials (),
                                              object_id,
                                              object );
        }
        catch ( SecurityException e )
        {
            throw new ModuleOperationException ( "Module [%module%] could not create kernel object [%object_id%]",
                                                 "module", this,
                                                 "object_id", object_id,
                                                 "cause", e );
        }
        catch ( KernelObjectException e )
        {
            throw new ModuleOperationException ( "Module [%module%] could not create kernel object [%object_id%]",
                                                 "module", this,
                                                 "object_id", object_id,
                                                 "cause", e );
        }
    }


    /**
     * @see musaico.module.Module#credentials()
     */
    @Override
    public Credentials credentials ()
    {
        return this.credentials;
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
        this.traceEnter ( "SimpleModule.deleteCredentials()" );
        synchronized ( this.lock )
        {
            if ( credentials == null
                 || this.credentials.equals ( credentials )
                 || ! this.temporaryCredentials.contains ( credentials ) )
            {
                this.traceFail ( "SimpleModule.deleteCredentials()" );
                throw new ModuleException ( "Module [%module%] cannot delete credentials [%credentials%]",
                                            "module", this,
                                            "credentials", credentials );
            }

            try
            {
                this.kernel.deleteCredentials ( credentials );
            }
            catch ( SecurityException e )
            {
                this.traceFail ( "SimpleModule.deleteCredentials()" );
                throw new ModuleException ( "Module [%module%] cannot delete credentials [%credentials%]",
                                            "module", this,
                                            "credentials", credentials,
                                            "cause", e );
            }

            this.temporaryCredentials.remove ( credentials );
        }

        this.traceExit ( "SimpleModule.deleteCredentials()" );
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
        try
        {
            return this.kernel.objects ().unregister ( this.credentials (),
                                                       object_id );
        }
        catch ( SecurityException e )
        {
            throw new ModuleOperationException ( "Module [%module%] could not delete kernel object [%object_id%]",
                                                 "module", this,
                                                 "object_id", object_id,
                                                 "cause", e );
        }
        catch ( KernelObjectException e )
        {
            throw new ModuleOperationException ( "Module [%module%] could not delete kernel object [%object_id%]",
                                                 "module", this,
                                                 "object_id", object_id,
                                                 "cause", e );
        }
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
        this.traceEnter ( "SimpleModule.duplicateCredentials()" );
        final Credentials credentials;
        synchronized ( this.lock )
        {
            try
            {
                credentials =
                    this.kernel.duplicateCredentials ( this.credentials );
            }
            catch ( SecurityException e )
            {
                this.traceFail ( "SimpleModule.duplicateCredentials()" );
                throw new ModuleException ( "Module [%module%] cannot create temporary credentials",
                                            "module", this,
                                            "cause", e );
            }

            if ( credentials == null
                 || this.credentials.equals ( credentials )
                 || this.temporaryCredentials.contains ( credentials ) )
            {
                this.traceFail ( "SimpleModule.duplicateCredentials()" );
                throw new ModuleException ( "Module [%module%] cannot create temporary credentials",
                                            "module", this );
            }

            this.temporaryCredentials.add ( credentials );
        }

        this.traceExit ( "SimpleModule.duplicateCredentials()" );
        return credentials;
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
        try
        {
            return this.kernel.objects ().get ( this.credentials (),
                                                object_id );
        }
        catch ( SecurityException e )
        {
            throw new ModuleOperationException ( "Module [%module%] could not retrieve kernel object [%object_id%]",
                                                 "module", this,
                                                 "object_id", object_id,
                                                 "cause", e );
        }
        catch ( KernelObjectException e )
        {
            throw new ModuleOperationException ( "Module [%module%] could not retrieve kernel object [%object_id%]",
                                                 "module", this,
                                                 "object_id", object_id,
                                                 "cause", e );
        }
    }


    /**
     * @see musaico.module.Module#id()
     */
    @Override
    public ModuleIdentifier id ()
    {
        return (ModuleIdentifier) this.description.parameter ( ModuleDescription.ID );
    }


    /**
     * @see musaico.module.Module#kernelObjects()
     */
    @Override
    public KernelObjectsRegistry kernelObjects ()
    {
        return this.kernel.objects ();
    }


    /**
     * @see musaico.module.Module#logger()
     */
    @Override
    public Logger logger ()
    {
        return this.kernel.logger ();
    }


    /**
     * @see musaico.module.Module#physicalMemory()
     */
    @Override
    public Memory physicalMemory ()
    {
        return this.physicalMemory;
    }


    /**
     * @see musaico.module.Module#platform()
     */
    @Override
    public PlatformOperations platform ()
    {
        return this.kernel.platform ();
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
        if ( object_system_type == null )
        {
            throw new I18nIllegalArgumentException ( "Module [%module_id%] cannot register ObjectSystemType [%object_system_type%]",
                                                     "module_id", this.id (),
                                                     "object_system_type", object_system_type );
        }

        // !!! WHAT TO DO NOW???

        return this;
    }


    /**
     * @see musaico.module.Module#resolveDependencies()
     */
    @Override
    public void resolveDependencies ()
        throws ModuleException
    {
        synchronized ( this.lock )
        {
            for ( int d = 0; d < this.dependencies.size (); d ++ )
            {
                ModuleDescription dependency = this.dependencies.get ( d );
                try
                {
                    this.kernel.modules ().load ( this.credentials,
                                                  dependency );
                }
                catch ( SecurityException e )
                {
                    throw new ModuleException ( "Module [%module%] could not load dependency [%module_id%]",
                                                "module", this,
                                                "module_id", dependency,
                                                "cause", e );
                }
                catch ( I18nIOException e )
                {
                    throw new ModuleException ( "Module [%module%] could not load dependency [%module_id%]",
                                                "module", this,
                                                "module_id", dependency,
                                                "cause", e );
                }
            }
        }
    }


    /**
     * @see musaico.module.Module#start()
     */
    @Override
    public void start ()
        throws ModuleException
    {
        throw new ModuleException ( "SimpleModule [%module%] (id [%module_id%]) cannot be started or stopped",
                                    "module", this,
                                    "module_id", this.id () );
    }


    /**
     * @see musaico.module.Module#stop()
     */
    @Override
    public void stop ()
        throws ModuleException
    {
        throw new ModuleException ( "SimpleModule [%module%] (id [%module_id%]) cannot be started or stopped",
                                    "module", this,
                                    "module_id", this.id () );
    }


    /**
     * @see musaico.module.Module#traceEnter(java.lang.String)
     */
    @Override
    public void traceEnter (
                            String block_name
                            )
    {
        // For now we always log the trace information.
        I18nString message =
            new I18nString ( "Entering [%block_name%]",
                             "block_name", block_name );
        this.logger ().log ( Level.TRACE,
                             message.localize ( this.kernel.locale () ) );
    }


    /**
     * @see musaico.module.Module#traceExit(java.lang.String)
     */
    @Override
    public void traceExit (
                           String block_name
                           )
    {
        // For now we always log the trace information.
        I18nString message =
            new I18nString ( "Exiting  [%block_name%]",
                             "block_name", block_name );
        this.logger ().log ( Level.TRACE,
                             message.localize ( this.kernel.locale () ) );
    }


    /**
     * @see musaico.module.Module#traceFail(java.lang.String)
     */
    @Override
    public void traceFail (
                           String block_name
                           )
    {
        // For now we always log the trace information.
        I18nString message =
            new I18nString ( "Failed [%block_name%]",
                             "block_name", block_name );
        this.logger ().log ( Level.TRACE,
                             message.localize ( this.kernel.locale () ) );
    }


    /**
     * @see musaico.module.Module#types()
     */
    @Override
    public FieldTypingEnvironment types ()
    {
        return this.physicalMemory.environment ();
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
        try
        {
            return this.kernel.objects ().update ( this.credentials (),
                                                   object_id,
                                                   new_object );
        }
        catch ( SecurityException e )
        {
            throw new ModuleOperationException ( "Module [%module%] could not update kernel object [%object_id%]",
                                                 "module", this,
                                                 "object_id", object_id,
                                                 "cause", e );
        }
        catch ( KernelObjectException e )
        {
            throw new ModuleOperationException ( "Module [%module%] could not update kernel object [%object_id%]",
                                                 "module", this,
                                                 "object_id", object_id,
                                                 "cause", e );
        }
    }


    /**
     * @see musaico.module.Module#virtualMemory()
     */
    @Override
    public Memory virtualMemory ()
    {
        return this.virtualMemory;
    }
}
