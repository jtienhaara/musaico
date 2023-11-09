package musaico.kernel_modules.drivers.excel;

import java.io.Serializable;


import musaico.kernel.module.Module;
import musaico.kernel.module.ModuleException;


/**
 * <p>
 * Loads in the Excel driver module.
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
public class ExcelModule
    implements Serializable
{
    public static void start (
                              Module module
                              )
        throws ModuleException
    {
        System.out.println ( "Starting Excel driver module" );

        try
        {
            // Create the Excel driver.
            ExcelDriver excel_driver =
                new ExcelDriver ( module );

            // Register it.
            module.createKernelObject ( ExcelDriver.DRIVER_ID,
                                        excel_driver );

            // Now register the driver in the /dev object system.
            // ...!!!
        }
        catch ( Throwable t )
        {
            throw new ModuleException ( "Could not start Excel driver module",
                                        "cause", t );
        }

        System.out.println ( "Done starting Excel driver module" );
    }

    public static void stop (
                             Module module
                             )
        throws ModuleException
    {
        System.out.println ( "Stopping Excel driver module" );

        module.deleteKernelObject ( ExcelDriver.DRIVER_ID );

        System.out.println ( "Done stopping Excel driver module" );
    }
}
