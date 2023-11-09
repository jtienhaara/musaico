package musaico.region.types;


import java.io.Serializable;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


import musaico.hash.Hash;

import musaico.region.Position;
import musaico.region.Region;
import musaico.region.Size;

import musaico.region.array.ArrayPosition;
import musaico.region.array.ArrayRegion;
import musaico.region.array.ArraySize;

import musaico.region.time.AbsoluteTimePosition;
import musaico.region.time.RelativeTimePosition;
import musaico.region.time.TimePosition;
import musaico.region.time.TimeRegion;
import musaico.region.time.TimeSize;

import musaico.time.Time;

import musaico.types.NoTypeCaster;
import musaico.types.SimpleTypeSystem;
import musaico.types.Type;
import musaico.types.TypeCaster;
import musaico.types.TypeCastersRegistry;
import musaico.types.TypeException;
import musaico.types.TypeSystem;
import musaico.types.TypingEnvironment;

import musaico.types.casters.ChainCaster;

import musaico.types.primitive.CastableToPrimitiveTypeSystem;

import musaico.types.primitive.types.CastStringToHash;


/**
 * <p>
 * TypeSystem of region types, such as Position and Region
 * and so on.
 * </p>
 *
 * <p>
 * Region types are all cross-platform,
 * so they can be shared between C, JavaScript, Tcl, Java,
 * and so on platforms.
 * </p>
 *
 * <p>
 * Although RegionTypeSystem is not itself derived from
 * PrimitiveTypeSystem, every region type must still be directly
 * cast-able to/from every other region type and every primitive type.
 * </p>
 *
 * @see musaico.types.primitive.PrimitiveTypeSystem
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
public class RegionTypeSystem
    extends CastableToPrimitiveTypeSystem
    implements Serializable
{
    /**
     * <p>
     * Creates a new RegionTypeSystem beneath the specified
     * type system.
     * </p>
     *
     * @param parent_type_system The parent type system, of which this
     *                           is a child.  Must not be null.
     *
     * @throws TypeException If the parent type system is null
     *                       or invalid.
     */
    public RegionTypeSystem (
                             TypeSystem parent_type_system
                             )
        throws TypeException
    {
        super ( parent_type_system );
    }






    /**
     * <p>
     * Static method creates an region type system, and adds
     * the standard region types, inside the specified
     * TypingEnvironment.
     * </p>
     */
    public static TypeSystem standard (
                                       TypingEnvironment environment
                                       )
        throws TypeException
    {
        // Create the region type system.
        TypeSystem regions = new RegionTypeSystem ( environment.root () );

        // Create the types.
        Type<Position> position_type = new PositionType ();
        Type<Region> region_type = new RegionType ();
        Type<Size> size_type = new SizeType ();

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
            casters.get ( bytes_type, string_type );
        TypeCaster<String,byte[]> string_to_bytes =
            casters.get ( string_type, bytes_type );

        // Add the type casters between all region and primitive types.
        // Type casters:
        //     BytesType <-> PositionType.
        environment.typeCastersRegistry ().put ( bytes_type,
                                                 position_type,
                                                 new ChainCaster<byte[],String,Position> ( bytes_to_string, String.class, new CastStringToPosition () ) );
        environment.typeCastersRegistry ().put ( position_type,
                                                 bytes_type,
                                                 new ChainCaster<Position,String,byte[]> ( new CastPositionToString (), String.class, string_to_bytes ) );
        //     BytesType <-> RegionType.
        environment.typeCastersRegistry ().put ( bytes_type,
                                                 region_type,
                                                 new ChainCaster<byte[],String,Region> ( bytes_to_string, String.class, new CastStringToRegion () ) );
        environment.typeCastersRegistry ().put ( region_type,
                                                 bytes_type,
                                                 new ChainCaster<Region,String,byte[]> ( new CastRegionToString (), String.class, string_to_bytes ) );
        //     BytesType <-> SizeType.
        environment.typeCastersRegistry ().put ( bytes_type,
                                                 size_type,
                                                 new ChainCaster<byte[],String,Size> ( bytes_to_string, String.class, new CastStringToSize () ) );
        environment.typeCastersRegistry ().put ( size_type,
                                                 bytes_type,
                                                 new ChainCaster<Size,String,byte[]> ( new CastSizeToString (), String.class, string_to_bytes ) );
        //     HashType <-X PositionType.
        environment.typeCastersRegistry ().put ( hash_type,
                                                 position_type,
                                                 new NoTypeCaster<Hash,Position> () );
        ChainCaster<Position,String,Hash> position_to_hash =
            new ChainCaster<Position,String,Hash> ( new CastPositionToString (),
                                                    String.class,
                                                    new CastStringToHash () );
        environment.typeCastersRegistry ().put ( position_type,
                                                 hash_type,
                                                 position_to_hash );
        //     HashType <-X RegionType.
        environment.typeCastersRegistry ().put ( hash_type,
                                                 region_type,
                                                 new NoTypeCaster<Hash,Region> () );
        environment.typeCastersRegistry ().put ( region_type,
                                                 hash_type,
                                                 new CastRegionToHash () );
        //     HashType <-X SizeType.
        environment.typeCastersRegistry ().put ( hash_type,
                                                 size_type,
                                                 new NoTypeCaster<Hash,Size> () );
        ChainCaster<Size,String,Hash> size_to_hash =
            new ChainCaster<Size,String,Hash> ( new CastSizeToString (),
                                                    String.class,
                                                    new CastStringToHash () );
        environment.typeCastersRegistry ().put ( size_type,
                                                 hash_type,
                                                 size_to_hash );
        //     IntType <-> PositionType.
        environment.typeCastersRegistry ().put ( int_type,
                                                 position_type,
                                                 new CastLongToPosition () );
        environment.typeCastersRegistry ().put ( position_type,
                                                 int_type,
                                                 new CastPositionToLong () );
        //     IntType X-X RegionType.
        environment.typeCastersRegistry ().put ( int_type,
                                                 region_type,
                                                 new NoTypeCaster<Long,Region> () );
        environment.typeCastersRegistry ().put ( region_type,
                                                 int_type,
                                                 new NoTypeCaster<Region,Long> () );
        //     IntType <-> SizeType.
        environment.typeCastersRegistry ().put ( int_type,
                                                 size_type,
                                                 new CastLongToSize () );
        environment.typeCastersRegistry ().put ( size_type,
                                                 int_type,
                                                 new CastSizeToLong () );
        //     RealType X-X PositionType.
        environment.typeCastersRegistry ().put ( position_type,
                                                 real_type,
                                                 new NoTypeCaster<Position,Double> () );
        environment.typeCastersRegistry ().put ( real_type,
                                                 position_type,
                                                 new NoTypeCaster<Double,Position> () );
        //     RealType X-X RegionType.
        environment.typeCastersRegistry ().put ( region_type,
                                                 real_type,
                                                 new NoTypeCaster<Region,Double> () );
        environment.typeCastersRegistry ().put ( real_type,
                                                 region_type,
                                                 new NoTypeCaster<Double,Region> () );
        //     RealType X-X SizeType.
        environment.typeCastersRegistry ().put ( size_type,
                                                 real_type,
                                                 new NoTypeCaster<Size,Double> () );
        environment.typeCastersRegistry ().put ( real_type,
                                                 size_type,
                                                 new NoTypeCaster<Double,Size> () );
        //     StringType <-> PositionType.
        environment.typeCastersRegistry ().put ( position_type,
                                                 string_type,
                                                 new CastPositionToString () );

        environment.typeCastersRegistry ().put ( string_type,
                                                 position_type,
                                                 new CastStringToPosition () );
        //     StringType <-> RegionType.
        environment.typeCastersRegistry ().put ( region_type,
                                                 string_type,
                                                 new CastRegionToString () );

        environment.typeCastersRegistry ().put ( string_type,
                                                 region_type,
                                                 new CastStringToRegion () );
        //     StringType <-> SizeType.
        environment.typeCastersRegistry ().put ( size_type,
                                                 string_type,
                                                 new CastSizeToString () );

        environment.typeCastersRegistry ().put ( string_type,
                                                 size_type,
                                                 new CastStringToSize () );
        //     TimeType <-> PositionType.
        environment.typeCastersRegistry ().put ( position_type,
                                                 time_type,
                                                 new CastPositionToTime () );
        environment.typeCastersRegistry ().put ( time_type,
                                                 position_type,
                                                 new CastTimeToPosition () );
        //     TimeType X-X RegionType.
        environment.typeCastersRegistry ().put ( region_type,
                                                 time_type,
                                                 new NoTypeCaster<Region,Time> () );
        environment.typeCastersRegistry ().put ( time_type,
                                                 region_type,
                                                 new NoTypeCaster<Time,Region> () );
        //     TimeType X-X SizeType.
        environment.typeCastersRegistry ().put ( size_type,
                                                 time_type,
                                                 new CastSizeToTime () );
        environment.typeCastersRegistry ().put ( time_type,
                                                 size_type,
                                                 new CastTimeToSize () );

        //     !!! for now PositionType X-X RegionType.
        environment.typeCastersRegistry ().put ( position_type,
                                                 region_type,
                                                 new NoTypeCaster<Position,Region> () );
        environment.typeCastersRegistry ().put ( region_type,
                                                 position_type,
                                                 new NoTypeCaster<Region,Position> () );
        //     !!! for now PositionType X-X SizeType.
        environment.typeCastersRegistry ().put ( position_type,
                                                 size_type,
                                                 new NoTypeCaster<Position,Size> () );
        environment.typeCastersRegistry ().put ( size_type,
                                                 position_type,
                                                 new NoTypeCaster<Size,Position> () );
        //     !!! for now RegionType X-X SizeType.
        environment.typeCastersRegistry ().put ( region_type,
                                                 size_type,
                                                 new NoTypeCaster<Region,Size> () );
        environment.typeCastersRegistry ().put ( size_type,
                                                 region_type,
                                                 new NoTypeCaster<Size,Region> () );

        // Add the types to the typing environment.
        environment.register ( ArrayPosition.class, regions, position_type );

        environment.register ( AbsoluteTimePosition.class, regions,
                               position_type );
        environment.register ( RelativeTimePosition.class, regions,
                               position_type );
        environment.register ( TimePosition.class, regions, position_type );

        environment.register ( Position.class, regions, position_type );


        environment.register ( ArrayRegion.class, regions, region_type );
        environment.register ( TimeRegion.class, regions, region_type );

        environment.register ( Region.class, regions, region_type );


        environment.register ( ArraySize.class, regions, size_type );
        environment.register ( TimeSize.class, regions, size_type );

        environment.register ( Size.class, regions, size_type );

        return regions;
    }
}
