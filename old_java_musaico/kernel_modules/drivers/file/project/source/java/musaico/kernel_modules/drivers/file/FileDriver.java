package musaico.kernel_modules.drivers.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


import musaico.buffer.Buffer;
import musaico.buffer.BufferException;
import musaico.buffer.BufferTools;

import musaico.buffer.search.SpecificFieldID;

import musaico.field.Field;
import musaico.field.FieldTypingEnvironment;

import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Filter;
import musaico.io.FilterState;
import musaico.io.Path;
import musaico.io.Reference;
import musaico.io.ReferenceCount;
import musaico.io.SimpleReferenceCount;

import musaico.io.references.SimpleSoftReference;

import musaico.kernel.driver.BlockDriver;
import musaico.kernel.driver.Driver;
import musaico.kernel.driver.DriverIdentifier;
import musaico.kernel.driver.DriverException;
import musaico.kernel.driver.EjectableDriver;
import musaico.kernel.driver.SchedulableDriver;
import musaico.kernel.driver.SpecialDriver;
import musaico.kernel.driver.SuspendableDriver;
import musaico.kernel.driver.TransactionalDriver;

import musaico.kernel.module.Module;

import musaico.kernel.objectsystem.Cursor;
import musaico.kernel.objectsystem.RecordFlag;
import musaico.kernel.objectsystem.RecordOperationException;
import musaico.kernel.objectsystem.RecordPermission;
import musaico.kernel.objectsystem.RecordPermissions;

import musaico.kernel.objectsystem.cursors.SimpleCursor;

import musaico.kernel.task.Scheduler;

import musaico.kernel.types.SimpleKernelTypingEnvironment;

import musaico.kernel_modules.drivers.byte_stream.ByteStreamDriver;

import musaico.region.Criterion;

import musaico.security.Credentials;
import musaico.security.Permissions;
import musaico.security.Security;


/**
 * <p>
 * A driver which provides access to files.
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
 * Copyright (c) 2011, 2012 Johann Tienhaara
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
public class FileDriver
    extends ByteStreamDriver
{
    /** The master file driver id. */
    public static final DriverIdentifier DRIVER_ID =
        new DriverIdentifier ( "file" );


    /** The toplevel, master FileDriver.  It does not
     *  point to a file, but all other drivers do. */
    private final FileDriver root;

    /** Synchronize all state changes on this token: */
    private final Serializable lock = new String ();

    /** The children FileDrivers of the root (unconfigurable)
     *  FileDriver.  Every time someone configures a FileDriver,
     *  we create a new instance and put it in this list inside
     *  the root FileDriver. */
    private final List<FileDriver> rootChildren;

    /** The Module of which this file driver is a part.
     *  Provides logger, field typing environment, and so on. */
    private final Module module;

    /** The File. */
    private final File file;

    /** The state of this driver (INITIALIZED, SHUTDOWN, and so on). */
    private Reference state = Driver.UNINITIALIZED;

    /** Reads the file.  Created on first open () with read
     *  mode, nullified on last close () with read mode. */
    private FileInputStream streamIn;

    /** Writes the file.  Created on first open () with write
     *  mode, nullified on last close () with write mode. */
    private FileOutputStream streamOut;


    /**
     * <p>
     * Creates a new root FileDriver.  When configure () is called,
     * instances of FileDriver are created and added to the object
     * system beneath /dev/file.
     * </p>
     *
     * <p>
     * Multiple root FileDrivers can be created, but it's pointless
     * to do so.
     * </p>
     *
     * @param module The module which loaded this FileDriver
     *               and provides logging, field typing, and so
     *               on for this driver.  Must not be null.
     *
     * @param security The security for this root FileDriver, used
     *                 to determine who is allowed to do what.
     *                 Must not be null.
     */
    public FileDriver (
                       Module module,
		       Security<RecordFlag> security
                       )
    {
        super ( module, FileDriver.DRIVER_ID, security );

        if ( module == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a new FileDriver with module [%module%] security [%security%]",
                                                     "module", module,
						     "security", security );
        }

        this.root = this;
        this.module = module;
        this.rootChildren = new ArrayList<FileDriver> ();
        this.file = null;
    }


    /**
     * <p>
     * Creates a file driver for a specific file.
     * </p>
     *
     * @param security The security for this FileDriver, used
     *                 to determine who is allowed to do what.
     *                 Must not be null.
     */
    protected FileDriver (
                          Module module,
                          FileDriver root_file_driver,
                          File file,
			  Security<RecordFlag> security
                          )
    {
        // This super constructor can cause NPE because of file.getPath ()...
        super ( module, new DriverIdentifier ( "file/" + file.getPath () ),
		security );

        if ( module == null
             || root_file_driver == null
             || file == null
	     || security == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a new FileDriver with module [%module%] root FileDriver [%root_file_driver%] file [%file%] security [%security%]",
                                                     "module", module,
                                                     "root_file_driver", root_file_driver,
                                                     "file", file,
						     "security", security );
        }

        this.root = root_file_driver;
        this.module = module;
        this.rootChildren = null;
        this.file = file;
        this.state = Driver.CONFIGURED;
    }


    /**
     * @see musaico.kernel_modules.drivers.byte_stream.ByteStreamDriver#closeInputStream(java.io.InputStream)
     */
    @Override
    protected void closeInputStream (
                                     InputStream in
                                     )
        throws DriverException
    {
        synchronized ( this.lock )
        {
            if ( in != this.streamIn )
            {
                throw new DriverException ( "File [%filename%] could not close unknown input stream [%in%]",
                                            "filename", this.file.getPath (),
                                            "in", in );
            }

            this.streamIn = null;
        }

        try
        {
            in.close ();
        }
        catch ( IOException e )
        {
            throw new DriverException ( "Could not close file [%filename%] input stream [%in%]",
                                        "filename", this.file.getPath (),
                                        "in", in,
                                        "cause", e );
        }
    }


    /**
     * @see musaico.kernel_modules.drivers.byte_stream.ByteStreamDriver#closeOutputStream(java.io.OutputStream)
     */
    @Override
    protected void closeOutputStream (
                                      OutputStream out
                                      )
        throws DriverException
    {
        synchronized ( this.lock )
        {
            if ( out != this.streamOut )
            {
                throw new DriverException ( "File [%filename%] could not close unknown output stream [%out%]",
                                            "filename", this.file.getPath (),
                                            "out", out );
            }

            this.streamOut = null;
        }

        try
        {
            out.close ();
        }
        catch ( IOException e )
        {
            throw new DriverException ( "Could not close file [%filename%] output stream [%out%]",
                                        "filename", this.file.getPath (),
                                        "out", out,
                                        "cause", e );
        }
    }


    /**
     * @see musaico.kernel.driver.Driver#autoConfigure()
     */
    @Override
    public Driver autoConfigure ()
        throws DriverException
    {
        this.module.traceEnter ( "FileDriver.autoConfigure ()" );
        this.module.traceFail ( "FileDriver.autoConfigure ()" );
        throw new DriverException ( "FileDriver cannot be autoConfigured" );
    }


    /**
     * @see musaico.kernel.driver.Driver#configure(musaico.buffer.Buffer)
     */
    @Override
    public Driver configure (
                             Buffer configuration_buffer
                             )
        throws DriverException
    {
        this.module.traceEnter ( "FileDriver.configure ()" );
        if ( this.file != null )
        {
            // Send it to the parent to create a new driver.
            this.module.traceExit ( "FileDriver.configure ()" );
            return this.root.configure ( configuration_buffer );
        }

        // This is the root driver.  We'll create a new instance.
        final String filename;
        try
        {
            Criterion filename_field =
                new SpecificFieldID ( configuration_buffer, "filename" );
            filename =
                BufferTools.findAndGet ( configuration_buffer,
                                         configuration_buffer.region (),
                                         filename_field )
                .value ( String.class );
        }
        catch ( Exception e ) // NullPointer, type exceptions
        {
            this.module.traceFail ( "FileDriver.configure ()" );
            throw new DriverException ( "No filename specified while configuring FileDriver [%file_driver%]: configuration options [%configuration_buffer%]",
                                        "file_driver", this,
                                        "configuration_buffer", configuration_buffer );
        }

        FileDriver configured_file_driver =
            new FileDriver ( this.module,
                             this,
                             new File ( filename ),
			     this.security () );

        synchronized ( this.lock )
        {
            this.rootChildren.add ( configured_file_driver );
        }

        this.module.traceExit ( "FileDriver.configure ()" );

        return configured_file_driver;
    }


    /**
     * @see musaico.kernel.driver.Driver#initialize()
     */
    @Override
    public void initialize ()
        throws DriverException
    {
        this.module.traceEnter ( "FileDriver.initialize ()" );

        synchronized ( this.lock )
        {
            if ( ! this.state.equals ( Driver.CONFIGURED ) )
            {
                throw new DriverException ( "FileDriver [%file_driver%] must be in state [%expected_state%] but is [%actual_state%]",
                                            "file_driver", this,
                                            "expected_state", Driver.CONFIGURED,
                                            "actual_state", this.state );
            }

            this.state = Driver.INITIALIZED;
        }

        this.module.traceExit ( "FileDriver.initialize ()" );
    }


    /**
     * @see musaico.kernel_modules.drivers.byte_stream.ByteStreamDriver#openInputStream(musaico.kernel.objectsystem.Cursor)
     */
    protected InputStream openInputStream (
                                           Cursor cursor
                                           )
        throws DriverException
    {
        this.module.traceEnter ( "FileDriver.openInputStream ()" );

        try
        {
            synchronized ( this.lock )
            {
                if ( this.streamIn == null )
                {
                    this.streamIn = new FileInputStream ( this.file );
                }

                this.module.traceExit ( "FileDriver.openInputStream ()" );

                return this.streamIn;
            }
        }
        catch ( IOException e )
        {
            this.module.traceFail ( "FileDriver.openInputStream ()" );
            throw new DriverException ( "Could not open file [%filename%] input stream for cursor [%cursor%]",
                                        "filename", file.getPath (),
                                        "cursor", cursor );
        }
    }


    /**
     * @see musaico.kernel_modules.drivers.byte_stream.ByteStreamDriver#openInputStream(musaico.kernel.objectsystem.Cursor)
     */
    protected OutputStream openOutputStream (
                                             Cursor cursor
                                             )
        throws DriverException
    {
        this.module.traceEnter ( "FileDriver.openOutputStream ()" );
        try
        {
            synchronized ( this.lock )
            {
                if ( this.streamOut == null )
                {
                    if ( cursor.permissions ().isAllowed ( RecordPermission.APPEND ) )
                    {
                        // Append to the end of the file.
                        this.streamOut =
                            new FileOutputStream ( this.file, true );
                    }
                    else
                    {
                        // Truncate the file.
                        this.streamOut =
                            new FileOutputStream ( this.file, false );
                    }
                }

                this.module.traceExit ( "FileDriver.openOutputStream ()" );

                return this.streamOut;
            }
        }
        catch ( IOException e )
        {
            this.module.traceFail ( "FileDriver.openOutputStream ()" );
            throw new DriverException ( "Could not open file [%filename%] output stream for cursor [%cursor%]",
                                        "filename", file.getPath (),
                                        "cursor", cursor );
        }
    }


    /**
     * @see musaico.kernel.driver.Driver#shutdown()
     */
    @Override
    public void shutdown ()
    {
        this.module.traceEnter ( "FileDriver.shutdown ()" );

        synchronized ( this.lock )
        {
            this.state = Driver.CONFIGURED;

            if ( this.rootChildren != null )
            {
                for ( FileDriver child_file_driver : this.rootChildren )
                {
                    child_file_driver.shutdown ();
                }
            }
        }

        this.module.traceExit ( "FileDriver.shutdown ()" );
    }


    /**
     * @see musaico.kernel.driver.Driver#state()
     */
    @Override
    public Reference state ()
    {
        synchronized ( this.lock )
        {
            return this.state;
        }
    }
}
