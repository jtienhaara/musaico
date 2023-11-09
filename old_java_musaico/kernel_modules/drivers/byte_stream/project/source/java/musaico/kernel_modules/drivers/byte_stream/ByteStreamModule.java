package musaico.kernel_modules.drivers.file;

import java.io.Serializable;


import musaico.kernel.module.Module;
import musaico.kernel.module.ModuleException;


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
            FileDriver file_driver =
                new FileDriver ( module );

            // Register it.
            module.createKernelObject ( FileDriver.DRIVER_ID,
                                        file_driver );

            // Now register the driver in the /dev object system.
            // ...!!!


        }
        catch ( Throwable t )
        {
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
