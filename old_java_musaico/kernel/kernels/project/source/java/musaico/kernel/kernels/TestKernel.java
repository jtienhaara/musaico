package musaico.kernel.kernels;

import java.io.InputStream;
import java.io.Serializable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;


import musaico.i18n.Internationalized;
import musaico.i18n.LocalizationException;
import musaico.i18n.Localized;

import musaico.i18n.exceptions.I18nIllegalArgumentException;
import musaico.i18n.exceptions.I18nIllegalStateException;
import musaico.i18n.exceptions.I18nIOException;
import musaico.i18n.exceptions.L10n;

import musaico.i18n.log.Level;
import musaico.i18n.log.Logger;
import musaico.i18n.log.StandardOutLogger;

import musaico.i18n.message.Message;
import musaico.i18n.message.SimpleMessageBuilder;
import musaico.i18n.message.StandardMessageFormat;

import musaico.io.Identifier;
import musaico.io.Path;
import musaico.io.Reference;
import musaico.io.SoftReference;
import musaico.io.TypedIdentifier;

import musaico.field.FieldTypingEnvironment;

import musaico.io.references.SimpleSoftReference;
import musaico.io.references.Version;

import musaico.kernel.Kernel;
import musaico.kernel.KernelModulesRegistry;
import musaico.kernel.KernelNamespaces;
import musaico.kernel.KernelObjectException;
import musaico.kernel.KernelObjectsRegistry;
import musaico.kernel.KernelPermission;
import musaico.kernel.KernelPermissions;
import musaico.kernel.KernelQuotaRules;
import musaico.kernel.PlatformOperations;
import musaico.kernel.QuotaCategory;

import musaico.kernel.common.modules.ClassModule;

import musaico.kernel.memory.Memory;
import musaico.kernel.memory.MemoryIdentifier;
import musaico.kernel.memory.MemoryPermission;

import musaico.kernel.memory.physical.SimplePhysicalMemory;

import musaico.kernel.module.Module;
import musaico.kernel.module.ModuleDescription;
import musaico.kernel.module.ModuleIdentifier;
import musaico.kernel.module.SimpleModuleIdentifier;

import musaico.kernel.module.descriptions.SimpleModuleDescription;

import musaico.kernel.platforms.SimplePlatformOperations;

import musaico.kernel.registries.SimpleKernelObjectsRegistry;

import musaico.kernel.rules.SimpleKernelQuotaRules;

import musaico.kernel.types.SimpleKernelTypingEnvironment;

import musaico.security.Credentials;
import musaico.security.NoSecurity;
import musaico.security.Permissions;
import musaico.security.Security;
import musaico.security.SecurityException;


/**
 * <p>
 * A small, simple test kernel for trying things out.
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
public class TestKernel
    implements Kernel
{
    /** Number of fields which each module is allowed to allocate
     *  at once. */
    public static final long MAX_MEMORY_PER_MODULE = 1024L;


    /** Lock all critical sections on this token: */
    private final Object lock = new Object ();

    /** Kernel logger, straight to stdout: */
    private final Logger logger = new StandardOutLogger ();

    /** Typing and typecasting for this kernel: */
    private final FieldTypingEnvironment types =
        new SimpleKernelTypingEnvironment ();

    /** Modules loaded into this kernel: */
    private final TestKernelModulesRegistry modules =
        new TestKernelModulesRegistry ( this );

    /** All objects in this kernel (drivers, memory, ONodes, and so on). */
    private final KernelObjectsRegistry objects =
        new SimpleKernelObjectsRegistry ( this );

    /** Access to system time and other platform data. */
    private final PlatformOperations platform =
	new SimplePlatformOperations ();

    /** Who can have quota limits applied, per object system,
     *  in this kernel. */
    private final KernelQuotaRules quotaRules =
	new SimpleKernelQuotaRules ( QuotaCategory.USER,
				     QuotaCategory.GROUP,
				     QuotaCategory.MODULE );

    /** This kernel's own credentials, allowing it to do whatever
     *  it likes to itself. */
    private final Credentials kernelCredentials = new TestKernelCredentials ();

    /** The set of all original security managers (by credentials) known in
     *  this kernel.  Duplicates are looked up by their originals. */
    private final Map<Credentials,Security<KernelPermission>> credentials =
        new HashMap<Credentials,Security<KernelPermission>> ();

    /** The security for duplicate credentials is followed back to
     *  the source. */
    private final Map<DuplicateCredentials,Credentials> duplicateCredentials =
        new HashMap<DuplicateCredentials,Credentials> ();


    public TestKernel ()
    {
        this.credentials.put ( this.kernelCredentials,
                               new NoSecurity<KernelPermission> () );
    }


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
    @Override
    public void deleteCredentials (
                                   Credentials credentials
                                   )
        throws SecurityException
    {
        synchronized ( this.lock )
        {
            if ( ! this.duplicateCredentials.containsKey ( credentials ) )
            {
                throw new SecurityException ( "Cannot delete credentials [%credentials%]: no such credentials",
                                              "credentials", credentials );
            }

            DuplicateCredentials duplicate = (DuplicateCredentials) credentials;
            this.duplicateCredentials.remove ( duplicate );
        }
    }


    /**
     * @see musaico.kernel.Kernel#duplicateCredentials(musaico.security.Credentials)
     */
    @Override
    public Credentials duplicateCredentials (
                                             Credentials credentials
                                             )
        throws SecurityException
    {
        final DuplicateCredentials duplicate;
        synchronized ( this.lock )
        {
            if ( ! this.credentials.containsKey ( credentials )
                 && ! this.duplicateCredentials.containsKey ( credentials ) )
            {
                throw new SecurityException ( "Cannot duplicate credentials [%credentials%]: no such credentials",
                                              "credentials", credentials );
            }

            duplicate = new DuplicateCredentials ( credentials );

            this.duplicateCredentials.put ( duplicate, credentials );
        }

        return duplicate;
    }


    /**
     * Loads the specified module in.  Quick hack.
     */
    public void findAndLoadModule (
                                   String module_name,
                                   String module_version
                                   )
        throws SecurityException,
               I18nIOException
    {
        Version version = new Version ( module_version );
        ModuleIdentifier requested_module_id =
            new SimpleModuleIdentifier ( module_name, version );

        ModuleDescription description =
            this.modules ().find ( this.kernelCredentials,
                                   requested_module_id );
        this.modules ().load ( this.kernelCredentials, description );
    }


    /**
     * @see musaico.kernel.Kernel#locale()
     */
    @Override
    public Locale locale ()
    {
        return Locale.getDefault ();
    }


    /**
     * @see musaico.kernel.Kernel#logger()
     */
    @Override
    public Logger logger ()
    {
        return this.logger;
    }


    /**
     * @see musaico.kernel.Kernel#modules()
     */
    @Override
    public TestKernelModulesRegistry modules ()
    {
        return this.modules;
    }


    /**
     * @see musaico.kernel.Kernel#objects()
     */
    @Override
    public KernelObjectsRegistry objects ()
    {
        return this.objects;
    }


    /**
     * @see musaico.kernel.Kernel#platform()
     */
    @Override
    public PlatformOperations platform ()
    {
        return this.platform;
    }


    /**
     * @see musaico.kernel.Kernel#quotaRules()
     */
    @Override
    public KernelQuotaRules quotaRules ()
    {
        return this.quotaRules;
    }


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
    @Override
    public Security<KernelPermission> securityFor (
                                                   Credentials credentials
                                                   )
        throws SecurityException
    {
        Security<KernelPermission> security;
        synchronized ( this.lock )
        {
            security = this.credentials.get ( credentials );
            if ( security == null )
            {
                if ( credentials instanceof DuplicateCredentials )
                {
                    DuplicateCredentials duplicate =
                        (DuplicateCredentials) credentials;
                    Credentials original =
                        this.duplicateCredentials.get ( duplicate );
                    security = this.credentials.get ( original );
                }
            }
        }

        if ( security == null )
        {
            throw new SecurityException ( "Invalid credentials [%credentials%] in kernel [%kernel%]",
                                          "credentials", credentials,
                                          "kernel", this );
        }

        return security;
    }


    /**
     * @see musaico.kernel.Kernel#types()
     */
    @Override
    public FieldTypingEnvironment types ()
    {
        return this.types;
    }




    static class TestKernelModulesRegistry
        implements KernelModulesRegistry
    {
        /** The parent kernel. */
        private final TestKernel kernel;

        /** Lock all critical sections on this token: */
        private final Object lock = new Object ();


        /**
         * <p>
         * Creates a new TestKernelModulesRegistry for the specified
         * TestKernel.
         * </p>
         *
         * @param kernel The TestKernel.  Must not be null.
         */
        TestKernelModulesRegistry (
                                   TestKernel kernel
                                   )
        {
            this.kernel = kernel;
        }


        /**
         * @see musaico.kernel.KernelModulesRegistry#find(musaico.security.Credentials,musaico.io.Identifier)
         */
        @Override
        public ModuleDescription find (
                                       Credentials credentials,
                                       Identifier requested_module_id
                                       )
            throws SecurityException,
                   I18nIOException
        {
            try
            {
                ModuleIdentifier module_id = (ModuleIdentifier)
                    requested_module_id;
                String package_name = module_id.moduleName ();
                String module_description_properties_name =
                    package_name.replaceAll ( "\\.", "/" ) + "/Module.properties";
                InputStream module_description_in =
                    this.kernel.getClass ().getClassLoader ().getResourceAsStream ( module_description_properties_name );
                if ( module_description_in == null )
                {
                    throw new I18nIOException ( "No such module [%module_id%] (properties name [%properties_name%])",
                                                "module_id", requested_module_id,
                                                "properties_name", module_description_properties_name );
                }

                Properties module_description_properties =
                    new Properties ();
                module_description_properties.load ( module_description_in );

                ModuleDescription description =
                    new SimpleModuleDescription ( new StandardMessageFormat (),
                                                  module_description_properties );
                return description;
            }
            catch ( Exception e )
            {
                throw new I18nIOException ( "Error while finding module [%module_id%]",
                                            "module_id", requested_module_id,
                                            "cause", e );
            }
        }


        /**
         * @see musaico.kernel.KernelModulesRegistry#load(musaico.security.Credentials,musaico.i18n.message.Message)
         */
        @Override
        public void load (
                          Credentials credentials,
                          Message module_ref
                          )
            throws I18nIOException,
                   SecurityException
        {
            try
            {
                ModuleDescription description = (ModuleDescription) module_ref;

                // !!! just physical memory for now, add virtual later...
                Memory module_memory =
                    new SimplePhysicalMemory ( MemoryIdentifier.PHYSICAL_FOR_KERNEL_MODULES,
                                               new NoSecurity<MemoryPermission> (), // !!!
                                               this.kernel.types () );

                Credentials module_credentials =
                    new TestKernelModuleCredentials ( description );

                // We should eventually handle more module varieties!
                Module module = new ClassModule ( description,
                                                  this.kernel,
                                                  module_credentials,
                                                  module_memory, // physical
                                                  module_memory ); // virtual

                synchronized ( this.lock )
                {
                    this.kernel.credentials.put ( module_credentials,
                                                  new NoSecurity<KernelPermission> () ); // !!! NO SECURITY
                }

                module.start ();
            }
            catch ( Exception e )
            {
                throw new I18nIOException ( "Could not load module [%module_id%]",
                                            "module_id", module_ref.parameter ( ModuleDescription.ID ),
                                            "cause", e );
            }
        }


        /**
         * @see musaico.kernel.KernelModulesRegistry#unload(musaico.security.Credentials,musaico.i18n.message.Message)
         */
        @Override
        public void unload (
                            Credentials credentials,
                            Message module_ref
                            )
            throws I18nIOException,
                   SecurityException
        {
            // not yet implemented!!!
            throw new I18nIOException ( "!!! NOT YET IMPLEMENTED" );
        }
    }




    /**
     * <p>
     * Runs the test kernel, and loads the modules specified at the
     * commandline.
     * </p>
     */
    public static void main (
                             String [] args
                             )
        throws Exception
    {
        TestKernel kernel = new TestKernel ();
        for ( int a = 0; a < args.length; a ++ )
        {
            System.out.println ( "Loading module " + args [ a ] + " version 1" );
            kernel.findAndLoadModule ( args [ a ], "1" );
        }
    }




    /**
     * <p>
     * Credentials for this kernel to do whatever it likes to
     * itself.
     * </p>
     */
    static class TestKernelCredentials
        implements Credentials
    {
        private final Reference id =
            new SimpleSoftReference<String> ( "TestKernel" );

        /**
         * @see musaico.security.Credentials#id()
         */
        @Override
        public Reference id ()
        {
            return this.id;
        }
    }


    /**
     * <p>
     * Credentials which we create for a specific module.
     * </p>
     */
    static class TestKernelModuleCredentials
        implements Credentials
    {
        /** The module identifier. */
        private final ModuleIdentifier moduleID;


        public TestKernelModuleCredentials (
                                            ModuleDescription description
                                            )
        {
            this.moduleID = (ModuleIdentifier)
                description.parameter ( ModuleDescription.ID );
        }


        /**
         * @see musaico.security.Credentials#id()
         */
        @Override
        public ModuleIdentifier id ()
        {
            return this.moduleID;
        }
    }


    /**
     * <p>
     * Credentials which have been duplicated from somewhere else,
     * in order to expose them to others (for example by setting
     * the owner of a Cursor to be a duplicate of a module's real
     * credentials, and then deleting them when finished).
     * </p>
     */
    static class DuplicateCredentials
        implements Credentials
    {
        private final Credentials originalCredentials;

        public DuplicateCredentials (
                                     Credentials original_credentials
                                     )
        {
            this.originalCredentials = original_credentials;
        }

        /**
         * @see musaico.security.Credentials#id()
         */
        @Override
        public Reference id ()
        {
            return this.originalCredentials.id ();
        }
    }
}
