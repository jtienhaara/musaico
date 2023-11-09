package musaico.buffer.types;


import java.io.Serializable;


import musaico.buffer.Buffer;
import musaico.buffer.BufferType;

import musaico.time.Time;

import musaico.types.NoTypeCaster;
import musaico.types.Type;
import musaico.types.TypeCaster;
import musaico.types.TypeCastersRegistry;
import musaico.types.TypeException;
import musaico.types.TypeSystem;
import musaico.types.TypingEnvironment;

import musaico.types.casters.ChainCaster;

import musaico.types.primitive.PrimitiveType;
import musaico.types.primitive.PrimitiveTypeSystem;


/**
 * <p>
 * TypeSystem for Buffers.
 * </p>
 *
 * <p>
 * The BufferTypeSystem introduces the Buffer as a new primitive
 * into a typing environment, so that it can be easily cast
 * to and from other primitive types (String, byte[], the IO
 * types such as Reference and Region, and so on).
 * </p>
 *
 * <p>
 * Note that a Buffer which contains non-primitive Fields may
 * not be able to cast to and from other primitive types at
 * runtime!
 * </p>
 *
 * <p>
 * Any Buffer which contains only primitive or other cross-platform
 * Fields can be shared across platforms (C, JavaScript, Tcl, Java,
 * and so on).  This is typically the case anyway.  However a Buffer
 * which contains complex objects for its Fields is not going to
 * be easy to share across platforms.
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
public class BufferTypeSystem
    extends PrimitiveTypeSystem
    implements Serializable
{
    /**
     * <p>
     * Creates a new BufferTypeSystem beneath the specified
     * type system.
     * </p>
     *
     * @param parent_type_system The parent type system, of which this
     *                           is a child.  Must not be null.
     *
     * @throws TypeException If the parent type system is null
     *                       or invalid.
     */
    public BufferTypeSystem (
                             TypeSystem parent_type_system
                             )
        throws TypeException
    {
        super ( parent_type_system );
    }






    /**
     * <p>
     * Static method creates a Buffer type system, and adds
     * the standard buffer type, inside the specified
     * TypingEnvironment.
     * </p>
     */
    public static TypeSystem standard (
                                       TypingEnvironment environment
                                       )
        throws TypeException
    {
        // Create the Buffer type system.
        TypeSystem buffer_types =
            new BufferTypeSystem ( environment.root () );

        // Create the types.
        Type<Buffer> buffer_type = new BufferType ();

        // Primitive types we know about.
        Type<byte[]> bytes_type  = environment.typeOf ( byte[].class );
        Type<String> string_type = environment.typeOf ( String.class );

        // Add the type casters between BufferType and
        // relevant primitive types.
        TypeCaster<Buffer,byte[]> buffer_to_bytes = new CastBufferToBytes ();
        // Type casters:
        //     BufferType X-> BytesType.
        environment.typeCastersRegistry ().put ( buffer_type,
                                                 bytes_type,
                                                 buffer_to_bytes );
        environment.typeCastersRegistry ().put ( bytes_type,
                                                 buffer_type,
                                                 new NoTypeCaster<byte[],Buffer> () );
        //     BufferType X-> StringType.
        environment.typeCastersRegistry ().put ( buffer_type,
                                                 string_type,
                                                 new CastBufferToString () );
        environment.typeCastersRegistry ().put ( string_type,
                                                 buffer_type,
                                                 new NoTypeCaster<String,Buffer> () );

        // Now all the other primitive types: BufferType X-> primitive type.
        Type<?> [] primitives = environment.registry ().types ();

        TypeCastersRegistry casters = environment.typeCastersRegistry ();

        for ( int p = 0; p < primitives.length; p ++ )
        {
            if ( primitives [ p ].equals ( buffer_type ) )
            {
                // No need to make sure there's a caster
                // from itself to itself.
                continue;
            }

            // Make sure this is a PrimitiveType.
            if ( ! ( primitives [ p ] instanceof PrimitiveType ) )
            {
                continue;
            }

            // The new Type must be cast-able to/from all
            // other PrimitiveTypes in all TypeSystems.
            if ( casters.get ( primitives [ p ], buffer_type ) == null )
            {
                NoTypeCaster.register ( primitives [ p ],
                                        buffer_type,
                                        casters );
            }

            if ( casters.get ( buffer_type, primitives [ p ] ) == null )
            {
                ChainCaster.register ( buffer_type,
                                       primitives [ p ],
                                       byte[].class,
                                       environment.registry (),
                                       casters );
            }
        }

        // Add the types to the typing environment.
        environment.register ( Buffer.class, buffer_types, buffer_type );

        return buffer_types;
    }
}
