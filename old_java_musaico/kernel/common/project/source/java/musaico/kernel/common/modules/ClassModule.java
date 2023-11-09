package musaico.kernel.common.modules;

import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.List;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.kernel.Kernel;

import musaico.kernel.memory.Memory;

import musaico.kernel.module.Module;
import musaico.kernel.module.ModuleDescription;
import musaico.kernel.module.ModuleException;

import musaico.kernel.module.modules.SimpleModule;

import musaico.security.Credentials;


/**
 * <p>
 * A Module which loads a class in its own sandbox,
 * relying on the system class loader for anything which has
 * been loaded previously (such as String and so on).
 * When this module is unloaded, the class(es) loaded disappear
 * along with the class loader.
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
public class ClassModule
    extends SimpleModule
{
    /** This parameter must be provided in the ModuleDescription
     *  in order to load the module class. */
    public static final String CLASS_NAME = "module_class";


    /** When told to start, we invoke this method from the
     *  loaded class.  We are careful to do so inside this
     *  Module's class loader. */
    private final Method startMethod;

    /** When told to stop, we invoke this method from the
     *  loaded class.  We are careful to do so inside this
     *  Module's class loader. */
    private final Method stopMethod;


    /**
     * <p>
     * Creates a new ClassModule with the specified identifier,
     * kernel, module credentials and memory manager.
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
    public ClassModule (
                        ModuleDescription description,
                        Kernel kernel,
                        Credentials credentials,
                        Memory physical_memory,
                        Memory virtual_memory
                        )
        throws ModuleException
    {
        super ( description, kernel, credentials, new ModuleClassLoader (),
                physical_memory, virtual_memory );

        try
        {
            final String class_name = (String)
                description.parameter ( ClassModule.CLASS_NAME );
            if ( class_name == null )
            {
                throw new I18nIllegalArgumentException ( "Class name property 'module_class' was not specified in the module description" );
            }

            final Class cl = this.classLoader ().loadClass ( class_name );

            // public static void start ( Module )
            this.startMethod = cl.getMethod ( "start", Module.class );
            // public static void stop ( Module )
            this.stopMethod = cl.getMethod ( "stop", Module.class );
        }
        catch ( Exception e )
        {
            throw new I18nIllegalArgumentException ( "Cannot create ClassModule with description [%description_class%][%description_hash_code%] [%kernel [%kernel%] credentials [%credentials%] physical memory manager [%physical_memory%] virtual memory manager [%virtual_memory%]",
                                                     "description_class", ( description == null ? null : description.getClass ().getSimpleName () ),
                                                     "description_hash_code", ( description == null ? null : description.hashCode () ),
                                                     "kernel", kernel,
                                                     "credentials", credentials,
                                                     "physical_memory", physical_memory,
                                                     "virtual_memory", virtual_memory,
                                                     "cause", e );
        }
    }


    /**
     * @see musaico.module.Module#start()
     */
    @Override
    public void start ()
        throws ModuleException
    {
        final Module module = this;
        final Method start_method = this.startMethod;
        final List<Throwable> start_problems = new ArrayList<Throwable> ();
        Runnable runnable = new Runnable ()
            {
                public void run ()
                {
                    try
                    {
                        // public static void start ( Module )
                        start_method.invoke ( null, module );
                    }
                    catch ( Throwable t )
                    {
                        Throwable root_cause = t;
                        while ( root_cause.getCause () != null )
                        {
                            root_cause = root_cause.getCause ();
                        }

                        start_problems.add ( root_cause );
                    }
                }
            };

        Thread start_thread =
            new Thread ( runnable, "" + this.id () + " start" );

        start_thread.setContextClassLoader ( this.classLoader () );
        start_thread.run ();
        try
        {
            start_thread.join ();
        }
        catch ( InterruptedException e )
        {
            throw new ModuleException ( "Failed to start module id [%module_id%] module [%module%] start_method [%start_method%] first exception: [%first_exception%] stack trace: [%stack_trace%]",
                                        "module_id", this.id (),
                                        "module", this,
                                        "start_method", start_method,
                                        "first_exception", e,
                                        "stack_trace", e.getStackTrace (),
                                        "cause", e );
        }

        if ( start_problems.size () > 0 )
        {
            throw new ModuleException ( "Failed to start module id [%module_id%] module [%module%] start_method [%start_method%] first exception: [%first_exception%] stack trace: [%stack_trace%]",
                                        "module_id", this.id (),
                                        "module", this,
                                        "start_method", start_method,
                                        "first_exception", start_problems.get ( 0 ),
                                        "stack_trace", start_problems.get ( 0 ).getStackTrace (),
                                        "cause", start_problems.get ( 0 ) );
        }
    }


    /**
     * @see musaico.module.Module#stop()
     */
    @Override
    public void stop ()
        throws ModuleException
    {
        final Module module = this;
        final Method stop_method = this.stopMethod;
        final List<Throwable> stop_problems = new ArrayList<Throwable> ();
        Runnable runnable = new Runnable ()
            {
                public void run ()
                {
                    try
                    {
                        // public static void stop ( Module )
                        stop_method.invoke ( null, module );
                    }
                    catch ( Throwable t )
                    {
                        Throwable root_cause = t;
                        while ( root_cause.getCause () != null )
                        {
                            root_cause = root_cause.getCause ();
                        }

                        stop_problems.add ( root_cause );
                    }
                }
            };

        Thread stop_thread =
            new Thread ( runnable, "" + this.id () + " stop" );

        stop_thread.setContextClassLoader ( this.classLoader () );
        stop_thread.run ();
        try
        {
            stop_thread.join ();
        }
        catch ( InterruptedException e )
        {
            throw new ModuleException ( "Failed to stop module id [%module_id%] module [%module%] stop_method [%stop_method%] first exception: [%first_exception%] stack trace: [%stack_trace%]",
                                        "module_id", this.id (),
                                        "module", this,
                                        "stop_method", stop_method,
                                        "first_exception", e,
                                        "stack_trace", e.getStackTrace (),
                                        "cause", e );
        }

        if ( stop_problems.size () > 0 )
        {
            throw new ModuleException ( "Failed to stop module id [%module_id%] module [%module%] stop_method [%stop_method%] first exception: [%first_exception%] stack trace: [%stack_trace%]",
                                        "module_id", this.id (),
                                        "module", this,
                                        "stop_method", stop_method,
                                        "first_exception", stop_problems.get ( 0 ),
                                        "stack_trace", stop_problems.get ( 0 ).getStackTrace (),
                                        "cause", stop_problems.get ( 0 ) );
        }
    }
}
