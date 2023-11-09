package musaico.foundation.io.types;


import java.io.Serializable;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


import musaico.foundation.hash.Hash;

import musaico.foundation.io.HardReference;
import musaico.foundation.io.Progress;
import musaico.foundation.io.Reference;
import musaico.foundation.io.SoftReference;

import musaico.foundation.io.progresses.SimpleProgress;

import musaico.foundation.io.references.SimpleSoftReference;
import musaico.foundation.io.references.UnicastHardReference;
import musaico.foundation.io.references.UUIDReference;

import musaico.foundation.time.Time;

import musaico.foundation.types.NoTypeCaster;
import musaico.foundation.types.SimpleTypeSystem;
import musaico.foundation.types.Type;
import musaico.foundation.types.TypeCaster;
import musaico.foundation.types.TypeCastersRegistry;
import musaico.foundation.types.TypeException;
import musaico.foundation.types.TypeSystem;
import musaico.foundation.types.TypingEnvironment;

import musaico.foundation.types.casters.ChainCaster;

import musaico.foundation.types.primitive.CastableToPrimitiveTypeSystem;


/**
 * <p>
 * TypeSystem of I/O types, such as Progress and Reference
 * and so on.
 * </p>
 *
 * <p>
 * I/O types are all cross-platform,
 * so they can be shared between C, JavaScript, Tcl, Java,
 * and so on platforms.
 * </p>
 *
 * <p>
 * Although each I/O type is not itself a PrimitiveType,
 * informally every I/O type must be directly cast-able
 * to/from every other I/O type and every primitive type.
 * </p>
 *
 * @see musaico.foundation.types.primitive.PrimitiveTypeSystem
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
 * Copyright (c) 2009, 2011 Johann Tienhaara
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
public class IOTypeSystem
    extends CastableToPrimitiveTypeSystem
    implements Serializable
{
    /**
     * <p>
     * Creates a new IOTypeSystem beneath the specified
     * type system.
     * </p>
     *
     * @param parent_type_system The parent type system, of which this
     *                           is a child.  Must not be null.
     *
     * @throws TypeException If the parent type system is null
     *                       or invalid.
     */
    public IOTypeSystem (
                         TypeSystem parent_type_system
                         )
        throws TypeException
    {
        super ( parent_type_system );
    }






    /**
     * <p>
     * Static method creates an I/O type system, and adds
     * the standard I/O types, inside the specified
     * TypingEnvironment.
     * </p>
     */
    public static TypeSystem standard (
                                       TypingEnvironment environment
                                       )
        throws TypeException
    {
        // Create the I/O type system.
        TypeSystem io = new IOTypeSystem ( environment.root () );

        // Create the types.
        Type<Progress> progress_type = new ProgressType ();
        Type<Reference> reference_type = new ReferenceType ();

        // Primitive types we know about.
        Type<byte[]> bytes_type  = environment.typeOf ( byte[].class );
        Type<Hash> hash_type     = environment.typeOf ( Hash.class );
        Type<Long> int_type      = environment.typeOf ( Long.class );
        Type<Double> real_type   = environment.typeOf ( Double.class );
        Type<String> string_type = environment.typeOf ( String.class );
        Type<Time> time_type     = environment.typeOf ( Time.class );

        // Type casters provided by the base primitives.
        TypeCastersRegistry casters = environment.typeCastersRegistry ();
        TypeCaster<byte[],String> bytes_to_string =
            casters.get ( bytes_type, string_type ); // byte[]->String
        TypeCaster<String,byte[]> string_to_bytes =
            casters.get ( string_type, bytes_type ); // String->byte[]

        // Add the type casters between all I/O and primitive types.
        // Type casters:
        //     BytesType <-> ProgressType.
        environment.typeCastersRegistry ().put ( bytes_type,
                                                 progress_type,
                                                 new ChainCaster<byte[],String,Progress> ( bytes_to_string, String.class, new CastStringToProgress () ) );
        environment.typeCastersRegistry ().put ( progress_type,
                                                 bytes_type,
                                                 new ChainCaster<Progress,String,byte[]> ( new CastProgressToString (), String.class, string_to_bytes ) );
        //     HashType <-X ProgressType.
        environment.typeCastersRegistry ().put ( hash_type,
                                                 progress_type,
                                                 new NoTypeCaster<Hash,Progress> () );
        environment.typeCastersRegistry ().put ( progress_type,
                                                 hash_type,
                                                 new CastProgressToHash () );
        //     IntType <-> ProgressType.
        environment.typeCastersRegistry ().put ( int_type,
                                                 progress_type,
                                                 new CastLongToProgress () );
        environment.typeCastersRegistry ().put ( progress_type,
                                                 int_type,
                                                 new CastProgressToLong () );
        //     RealType X-X ProgressType.
        environment.typeCastersRegistry ().put ( progress_type,
                                                 real_type,
                                                 new NoTypeCaster<Progress,Double> () );
        environment.typeCastersRegistry ().put ( real_type,
                                                 progress_type,
                                                 new NoTypeCaster<Double,Progress> () );
        //     StringType <-> ProgressType.
        environment.typeCastersRegistry ().put ( progress_type,
                                                 string_type,
                                                 new CastProgressToString () );

        environment.typeCastersRegistry ().put ( string_type,
                                                 progress_type,
                                                 new CastStringToProgress () );
        //     TimeType X-X ProgressType.
        environment.typeCastersRegistry ().put ( progress_type,
                                                 time_type,
                                                 new NoTypeCaster<Progress,Time> () );
        environment.typeCastersRegistry ().put ( time_type,
                                                 progress_type,
                                                 new NoTypeCaster<Time,Progress> () );

        //     BytesType <-> ReferenceType.
        environment.typeCastersRegistry ().put ( bytes_type,
                                                 reference_type,
                                                 new ChainCaster<byte[],String,Reference> ( bytes_to_string, String.class, new CastStringToReference () ) );
        environment.typeCastersRegistry ().put ( reference_type,
                                                 bytes_type,
                                                 new ChainCaster<Reference,String,byte[]> ( new CastReferenceToString (), String.class, string_to_bytes ) );
        //     HashType <-X ReferenceType.
        environment.typeCastersRegistry ().put ( hash_type,
                                                 reference_type,
                                                 new NoTypeCaster<Hash,Reference> () );
        environment.typeCastersRegistry ().put ( reference_type,
                                                 hash_type,
                                                 new CastReferenceToHash () );
        //     IntType <-> ReferenceType.
        environment.typeCastersRegistry ().put ( int_type,
                                                 reference_type,
                                                 new CastLongToReference () );
        environment.typeCastersRegistry ().put ( reference_type,
                                                 int_type,
                                                 new CastReferenceToLong () );
        //     RealType X-X ReferenceType.
        environment.typeCastersRegistry ().put ( reference_type,
                                                 real_type,
                                                 new NoTypeCaster<Reference,Double> () );
        environment.typeCastersRegistry ().put ( real_type,
                                                 reference_type,
                                                 new NoTypeCaster<Double,Reference> () );
        //     StringType <-> ReferenceType.
        environment.typeCastersRegistry ().put ( reference_type,
                                                 string_type,
                                                 new CastReferenceToString () );

        environment.typeCastersRegistry ().put ( string_type,
                                                 reference_type,
                                                 new CastStringToReference () );
        //     TimeType X-X ReferenceType.
        environment.typeCastersRegistry ().put ( reference_type,
                                                 time_type,
                                                 new NoTypeCaster<Reference,Time> () );
        environment.typeCastersRegistry ().put ( time_type,
                                                 reference_type,
                                                 new NoTypeCaster<Time,Reference> () );

        //     !!! for now ProgressType X-X ReferenceType.
        environment.typeCastersRegistry ().put ( progress_type,
                                                 reference_type,
                                                 new NoTypeCaster<Progress,Reference> () );
        environment.typeCastersRegistry ().put ( reference_type,
                                                 progress_type,
                                                 new NoTypeCaster<Reference,Progress> () );

        // Add the types to the typing environment.
        environment.register ( SimpleProgress.class, io, progress_type );

        environment.register ( Progress.class, io, progress_type );

        environment.register ( SimpleSoftReference.class, io, reference_type );
        environment.register ( UUIDReference.class, io, reference_type );
        environment.register ( UnicastHardReference.class, io, reference_type );

        environment.register ( Reference.class, io, reference_type );
        environment.register ( SoftReference.class, io, reference_type );
        environment.register ( HardReference.class, io, reference_type );

        return io;
    }
}
