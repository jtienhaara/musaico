package musaico.kernel.types;


import java.io.Serializable;


import musaico.buffer.types.BufferTypeSystem;

import musaico.field.SimpleFieldTypingEnvironment;

import musaico.io.types.IOTypeSystem;

import musaico.kernel.memory.types.MemoryTypeSystem;

import musaico.kernel.objectsystem.types.ObjectSystemTypeSystem;

import musaico.region.types.RegionTypeSystem;

import musaico.types.RuntimeTypeException;

import musaico.types.opaque.OpaqueTypeSystem;

import musaico.types.primitive.PrimitiveTypeSystem;


/**
 * <p>
 * Provides a simple kernel typing environment with primitives and
 * I/O types and field creation.
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
 * Copyright (c) 2009 Johann Tienhaara
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
public class SimpleKernelTypingEnvironment
    extends SimpleFieldTypingEnvironment
    implements Serializable
{
    /**
     * <p>
     * Creates a new SimpleKernelTypingEnvironment with the
     * standard type systems (primitive, I/O, and so on) and
     * simple field creation and duplication facilities.
     * </p>
     *
     * @throws RuntimeTypeException If any TypeSystem(s) cannot
     *                              be created (fatal bug or resource
     *                              constraints).
     */
    public SimpleKernelTypingEnvironment ()
        throws RuntimeTypeException
    {
        // Create primitive type system etc.
        try
        {
            // Non-kernel type systems:
            PrimitiveTypeSystem.standard ( this );
            OpaqueTypeSystem.standard ( this );
            IOTypeSystem.standard ( this );
            RegionTypeSystem.standard ( this );
            BufferTypeSystem.standard ( this );

            // Kernel type systems:
            MemoryTypeSystem.standard ( this );
            ObjectSystemTypeSystem.standard ( this );
        }
        catch ( RuntimeTypeException e )
        {
            throw e;
        }
        catch ( Throwable t )
        {
            this.exceptionHandler ().handle ( t );

            // If the exception handler didn't throw a
            // RuntimeTypeException, then blow up anyway.
            // This is fatal.
            throw new RuntimeTypeException ( "Cannot create SimpleKernelTypingEnvironment because of failed TypeSystem(s)",
                                             "cause", t );
        }
    }
}
