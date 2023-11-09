package musaico.kernel_modules.drivers.file;

import java.io.Serializable;


import musaico.kernel.module.Module;
import musaico.kernel.module.ModuleException;


// !!!!!!!!!!!!!!!!!!!!!
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Date;
import musaico.buffer.search.SpecificFieldID;
import musaico.io.references.SimpleSoftReference;
import musaico.buffer.Buffer;
import musaico.buffer.BufferTools;
import musaico.field.FieldTypingEnvironment;
import musaico.kernel.common.oentries.SimpleOEntry;
import musaico.kernel.driver.Driver;
import musaico.kernel.objectsystem.onode.OEntry;
import musaico.kernel.objectsystem.RecordFlag;
import musaico.kernel.objectsystem.RecordPermission;
import musaico.kernel.objectsystem.RecordPermissions;
import musaico.security.Credentials;
import musaico.security.Permissions;
import musaico.kernel.objectsystem.Cursor;
import musaico.kernel.objectsystem.cursors.SimpleCursor;
import musaico.region.Criterion;
import musaico.region.Position;
import musaico.region.Region;
import musaico.region.Space;
import musaico.region.array.ArraySpace;
import musaico.security.NoSecurity;


/**
 * <p>
 * Loads in the file driver module.
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
public class FileModule
    implements Serializable
{
    public static void start (
                              Module module
                              )
        throws ModuleException
    {
        System.out.println ( "Starting file driver module" );

        try
        {
            // Create the file driver.
            FileDriver root_file_driver =
                new FileDriver ( module, new NoSecurity<RecordFlag> () );

            // Register it.
            module.createKernelObject ( FileDriver.DRIVER_ID,
                                        root_file_driver );

            // Now register the driver in the /dev object system.
            // ...!!!





            //////////////// !!!
            Space space = ArraySpace.STANDARD;
            Credentials me = module.credentials ();

            Region buffer_region = space.region ( space.origin (),
                                                  space.expr ( space.origin () )
                                                  .add ( space.expr ( space.one () )
                                                         .multiply ( 15D )
                                                         .size () )
                                                  .position () );
            Buffer config =
                module.physicalMemory ().allocate ( me, buffer_region );
            Position position = config.region ().start ();
            FieldTypingEnvironment env = module.types ();
            config.set ( position, env.create ( "filename", "/tmp/test_file_driver.txt" ) );

            FileDriver specific_file_driver = (FileDriver)
                root_file_driver.configure ( config );
            specific_file_driver.initialize ();

            Buffer bytes =
                module.physicalMemory ().allocate ( me, buffer_region );
            position = bytes.region ().start ();

            OEntry entry = SimpleOEntry.NO_SUCH_OENTRY;
            Permissions<RecordFlag> permissions;
            Cursor cursor;

            // First try truncating then reading the file.
            permissions =
                new RecordPermissions ( me,
                                        specific_file_driver.id (),
                                        RecordPermission.READ,
                                        RecordPermission.WRITE );
            cursor = new SimpleCursor ( entry,
                                        permissions,
                                        specific_file_driver );

            cursor.record ().open ( cursor );

            Criterion filename_field =
                new SpecificFieldID ( config, "filename" );
            final String actual_filename =
                BufferTools.findAndGet ( config, config.region (),
                                         filename_field )
                .value ( String.class );

            Region read_into_region =
                cursor.record ().read ( cursor, bytes,
                                        bytes.region () );

            System.out.println ( "" );
            System.out.println ( "" );
            System.out.println ( "Read into region " + read_into_region + " of buffer:      " + BufferTools.toString ( bytes ) );
            System.out.println ( "" );

            final String expected_empty = "";

            String actual_empty = bytes.value ( String.class );
            if ( ! expected_empty.equals ( actual_empty ) )
            {
                throw new ModuleException ( "Expected text \"[%expected_text%]\" to be read from the input stream for [%filename%], but found \"[%actual_text%]\" instead",
                                            "expected_text", expected_empty,
                                            "filename", actual_filename,
                                            "actual_text", actual_empty );
            }

            cursor.record ().close ( cursor );

            // Now re-open the file, this time appending text to the end.
            permissions =
                new RecordPermissions ( me,
                                        specific_file_driver.id (),
                                        RecordPermission.READ,
                                        RecordPermission.WRITE,
                                        RecordPermission.APPEND );
            cursor = new SimpleCursor ( entry,
                                        permissions,
                                        specific_file_driver );

            cursor.record ().open ( cursor );

            InputStream in_stream =
                specific_file_driver.in ( cursor );
            OutputStream out_stream =
                specific_file_driver.out ( cursor );

            PrintWriter out = new PrintWriter ( new OutputStreamWriter ( out_stream ) );
            final String expected_text =
                "!!! Hello, " + new Date () + " world!";
            out.print ( expected_text );
            out.flush ();

            InputStreamReader in = new InputStreamReader ( in_stream );
            StringBuilder sbuf = new StringBuilder ();
            int ch;
            while ( ( ch = in.read () ) > 0 )
            {
                sbuf.append ( (char) ch );
            }

            final String actual_text_from_input_stream = sbuf.toString ();

            System.out.println ( "!!! Read from " + actual_filename + " : " + actual_text_from_input_stream );

            cursor.record ().close ( cursor );

            if ( ! actual_text_from_input_stream.equals ( expected_text ) )
            {
                throw new ModuleException ( "Expected text \"[%expected_text%]\" to be read from the input stream for [%filename%], but found \"[%actual_text%]\" instead",
                                            "expected_text", expected_text,
                                            "filename", actual_filename,
                                            "actual_text", actual_text_from_input_stream );
            }

            // Now test the underlying ByteStreamDriver.read ().
            cursor = new SimpleCursor ( entry,
                                        permissions,
                                        specific_file_driver );

            cursor.record ().open ( cursor );

            read_into_region =
                cursor.record ().read ( cursor, bytes,
                                        bytes.region () );

            cursor.record ().close ( cursor );

            System.out.println ( "!!! Read " + read_into_region.size () + " fields into the byte buffer: " + BufferTools.toString ( bytes ) );

            String actual_text_from_read = bytes.value ( String.class );

            System.out.println ( "!!! As text: " + actual_text_from_read );

            if ( ! actual_text_from_read.equals ( expected_text ) )
            {
                throw new ModuleException ( "Expected text \"[%expected_text%]\" to be read from the input stream for [%filename%], but found \"[%actual_text%]\" instead",
                                            "expected_text", expected_text,
                                            "filename", actual_filename,
                                            "actual_text", actual_text_from_read );
            }


            //////////////// !!!




        }
        catch ( Throwable t )
        {
            t.printStackTrace ();
            throw new ModuleException ( "Could not start file driver module",
                                        "cause", t );
        }

        System.out.println ( "Done starting file driver module" );
    }

    public static void stop (
                             Module module
                             )
        throws ModuleException
    {
        System.out.println ( "Stopping file driver module" );

        module.deleteKernelObject ( FileDriver.DRIVER_ID );

        System.out.println ( "Done stopping file driver module" );
    }
}
