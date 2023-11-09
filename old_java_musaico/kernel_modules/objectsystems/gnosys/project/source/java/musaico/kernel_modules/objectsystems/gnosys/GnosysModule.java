package musaico.kernel_modules.objectsystems.gnosys;

import java.io.Serializable;


import musaico.kernel.module.Module;
import musaico.kernel.module.ModuleException;

import musaico.kernel.objectsystem.ObjectSystemTypeFlag;

import musaico.security.NoSecurity; // !!! NO SECURITY


/**
 * <p>
 * Loads in the gnosys object system type.
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
public class GnosysModule
    implements Serializable
{
    public static void start (
                              Module module
                              )
        throws ModuleException
    {
        System.out.println ( "Starting gnosys module" );

        try
        {
            // Create the gnosys object system factory.
            Gnosys gnosys_factory =
                new Gnosys ( module,
                             new NoSecurity<ObjectSystemTypeFlag> () ); // !!! SECURITY

            // Register it.
            module.createKernelObject ( Gnosys.OBJECT_SYSTEM_TYPE,
                                        gnosys_factory );
        }
        catch ( Throwable t )
        {
            throw new ModuleException ( "Could not start Gnosys module",
                                        "cause", t );
        }

        System.out.println ( "Done starting gnosys object system type" );
    }

    public static void stop (
                             Module module
                             )
        throws ModuleException
    {
        System.out.println ( "Stopping gnosys module" );

        module.deleteKernelObject ( Gnosys.OBJECT_SYSTEM_TYPE );

        System.out.println ( "Done stopping gnosys module" );
    }
}
