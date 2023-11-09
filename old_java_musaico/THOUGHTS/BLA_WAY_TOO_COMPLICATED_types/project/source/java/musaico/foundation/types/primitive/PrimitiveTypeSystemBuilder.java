package musaico.types.primitive;


import java.io.Serializable;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


import musaico.hash.Hash;
import musaico.hash.MD5;
import musaico.hash.SHA1;
import musaico.hash.SHA256;
import musaico.hash.SHA512;

import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.time.Time;

import musaico.types.NoTypeCaster;
import musaico.types.SimpleTypeSystem;
import musaico.types.Type;
import musaico.types.TypeBuilder;
import musaico.types.TypeCaster;
import musaico.types.TypeCastersRegistry;
import musaico.types.TypeException;
import musaico.types.TypeSystem;
import musaico.types.TypeSystemBuilder;
import musaico.types.TypingEnvironment;


/**
 * <p>
 * TypeSystem of primitive types, such as text, number, time,
 * hash, and so on.  Primitives are all cross-platform,
 * so they can be shared between C, JavaScript, Tcl, Java,
 * and so on platforms.
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
 * Copyright (c) 2009, 2012 Johann Tienhaara
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
public class PrimitiveTypeSystemBuilder
    extends CastableToPrimitiveTypeSystemBuilder
    implements Serializable
{
    /**
     * <p>
     * Creates a new PrimitiveTypeSystemBuilder in the specified
     * tying environment to build a new type system beneath the specified
     * one.
     * </p>
     *
     * @param environment The lookup of all Type's and TypeSystem's.
     *                    Must have a non-null root type system.
     *                    Must not be null.
     *
     * @param parent_type_system The parent type system.  This builder will
     *                           construct a child type system.
     *                           For example, the root type
     *                           system of the specified environment,
     *                           or perhaps another type system which
     *                           this builder will extend with a child
     *                           type system.  Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public PrimitiveTypeSystemBuilder (
                                       TypingEnvironment environment,
                                       TypeSystem parent_type_system
                                       )
        throws I18nIllegalArgumentException
    {
        // Throws I18nIllegalArgumentException:
        super ( environment, parent_type_system );

        // Reusable typecasters.
        final TypeCaster<BigInteger, BigDecimal> big_integer_to_big_decimal =
            new CastBigIntegerToBigDecimal ();

        final TypeCaster<byte[], MD5> bytes_to_md5 =
            new CastBytesToMD5 ();
        final TypeCaster<byte[], SHA1> bytes_to_sha1 =
            new CastBytesToSHA1 ();
        final TypeCaster<byte[], SHA256> bytes_to_sha256 =
            new CastBytesToSHA256 ();
        final TypeCaster<byte[], SHA512> bytes_to_sha512 =
            new CastBytesToSHA512 ();

        final TypeCaster<MD5, BigInteger> md5_to_big_integer =
            new CastHashToBigInteger<MD5> ( MD5.class );
        final TypeCaster<SHA1, BigInteger> sha1_to_big_integer =
            new CastHashToBigInteger<SHA1> ( SHA1.class );
        final TypeCaster<SHA256, BigInteger> sha256_to_big_integer =
            new CastHashToBigInteger<SHA256> ( SHA256.class );
        final TypeCaster<SHA512, BigInteger> sha512_to_big_integer =
            new CastHashToBigInteger<SHA512> ( SHA512.class );

        final TypeCaster<String, BigDecimal> string_to_big_decimal =
            new CastStringToBigDecimal ();
        final TypeCaster<String, BigInteger> string_to_big_integer =
            new CastStringToBigInteger ();
        final TypeCaster<String, byte []> string_to_bytes =
            new CastStringToBytes ();

        // Create the default primitive types.
        Type<byte[]> binary_type = this.prepare ( "binary",          // name
                                                  byte [].class,     // class
                                                  new byte [ 0 ] )   // none
            .chainFrom ( byte [].class, string_to_big_decimal )
            .chainFrom ( byte [].class, string_to_big_integer )
            .addTypeCasterFrom ( new CastBytesToDouble () )
            .addTypeCasterFrom ( new CastBytesToFloat () )
            .addTypeCasterFrom ( new CastBytesToInteger () )
            .addTypeCasterFrom ( new CastBytesToLong () )
            .addTypeCasterFrom ( bytes_to_md5 )
            .addTypeCasterFrom ( bytes_to_sha1 )
            .addTypeCasterFrom ( bytes_to_sha256 )
            .addTypeCasterFrom ( bytes_to_sha512 )
            .addTypeCasterFrom ( new CastBytesToString () )
            .addTypeCasterFrom ( new CastBytesToTime() )
            .build ();
        this.add ( binary_type );

        Type<Hash> hash_type     = this.prepare ( "hash",            // name
                                                  Hash.class,        // class
                                                  Hash.NONE )        // none
            .addRawClass ( MD5.class )
            .addRawClass ( SHA1.class )
            .addRawClass ( SHA256.class )
            .addRawClass ( SHA512.class )
            .addTypeCasterFrom ( md5_to_big_integer )
            .addTypeCasterFrom ( sha1_to_big_integer )
            .addTypeCasterFrom ( sha256_to_big_integer )
            .addTypeCasterFrom ( sha512_to_big_integer )
            .chainFrom ( MD5.class, big_integer_to_big_decimal )
            .chainFrom ( SHA1.class, big_integer_to_big_decimal )
            .chainFrom ( SHA256.class, big_integer_to_big_decimal )
            .chainFrom ( SHA512.class, big_integer_to_big_decimal )
            .addTypeCasterFrom ( new CastHashToBytes<MD5> ( MD5.class ) )
            .addTypeCasterFrom ( new CastHashToBytes<SHA1> ( SHA1.class ) )
            .addTypeCasterFrom ( new CastHashToBytes<SHA256> ( SHA256.class ) )
            .addTypeCasterFrom ( new CastHashToBytes<SHA512> ( SHA512.class ) )
            .noTypeCasterFrom ( MD5.class, Double.class )
            .noTypeCasterFrom ( SHA1.class, Float.class )
            .noTypeCasterFrom ( SHA256.class, Integer.class )
            .noTypeCasterFrom ( SHA512.class, Long.class )
            .addTypeCasterFrom ( new CastHashToString<MD5> ( MD5.class ) )
            .addTypeCasterFrom ( new CastHashToString<SHA1> ( SHA1.class ) )
            .addTypeCasterFrom ( new CastHashToString<SHA256> ( SHA256.class ) )
            .addTypeCasterFrom ( new CastHashToString<SHA512> ( SHA512.class ) )
            .noTypeCasterFrom ( MD5.class, Time.class )
            .noTypeCasterFrom ( SHA1.class, Time.class )
            .noTypeCasterFrom ( SHA256.class, Time.class )
            .noTypeCasterFrom ( SHA512.class, Time.class )
            .build ();
        this.add ( hash_type );

        Type<Number> number_type = this.prepare ( "number",          // name
                                                  Number.class,      // class
                                                  new NoNumber () )  // none
            .addRawClass ( BigDecimal.class )
            .addRawClass ( BigInteger.class )
            .addRawClass ( Double.class )
            .addRawClass ( Float.class )
            .addRawClass ( Integer.class )
            .addRawClass ( Long.class )
            .addTypeCasterFrom ( new CastBigDecimalToBigInteger () )
            .chainFrom ( BigDecimal.class, string_to_bytes )
            .addTypeCasterFrom ( new CastBigDecimalToDouble () )
            .addTypeCasterFrom ( new CastBigDecimalToFloat () )
            .addTypeCasterFrom ( new CastBigDecimalToInteger () )
            .addTypeCasterFrom ( new CastBigDecimalToLong () )
            .chainFrom ( BigDecimal.class, bytes_to_md5 )
            .chainFrom ( BigDecimal.class, bytes_to_sha1 )
            .chainFrom ( BigDecimal.class, bytes_to_sha256 )
            .chainFrom ( BigDecimal.class, bytes_to_sha512 )
            .addTypeCasterFrom ( new CastBigDecimalToString () )
            .noTypeCasterFrom ( BigDecimal.class, Time.class )
            .addTypeCasterFrom ( big_integer_to_big_decimal )
            .chainFrom ( BigInteger.class, string_to_bytes )
            .addTypeCasterFrom ( new CastBigIntegerToDouble () )
            .addTypeCasterFrom ( new CastBigIntegerToFloat () )
            .addTypeCasterFrom ( new CastBigIntegerToInteger () )
            .addTypeCasterFrom ( new CastBigIntegerToLong () )
            .chainFrom ( BigInteger.class, bytes_to_md5 )
            .chainFrom ( BigInteger.class, bytes_to_sha1 )
            .chainFrom ( BigInteger.class, bytes_to_sha256 )
            .chainFrom ( BigInteger.class, bytes_to_sha512 )
            .addTypeCasterFrom ( new CastBigIntegerToString () )
            .noTypeCasterFrom ( BigInteger.class, Time.class )
            .addTypeCasterFrom ( new CastDoubleToBigDecimal () )
            .addTypeCasterFrom ( new CastDoubleToBigInteger () )
            .addTypeCasterFrom ( new CastDoubleToBytes () )
            .addTypeCasterFrom ( new CastDoubleToFloat () )
            .addTypeCasterFrom ( new CastDoubleToInteger () )
            .addTypeCasterFrom ( new CastDoubleToLong () )
            .chainFrom ( Double.class, bytes_to_md5 )
            .chainFrom ( Double.class, bytes_to_sha1 )
            .chainFrom ( Double.class, bytes_to_sha256 )
            .chainFrom ( Double.class, bytes_to_sha512 )
            .addTypeCasterFrom ( new CastDoubleToString () )
            .noTypeCasterFrom ( Double.class, Time.class )
            .addTypeCasterFrom ( new CastFloatToBigDecimal () )
            .addTypeCasterFrom ( new CastFloatToBigInteger () )
            .addTypeCasterFrom ( new CastFloatToBytes () )
            .addTypeCasterFrom ( new CastFloatToDouble () )
            .addTypeCasterFrom ( new CastFloatToInteger () )
            .addTypeCasterFrom ( new CastFloatToLong () )
            .chainFrom ( Float.class, bytes_to_md5 )
            .chainFrom ( Float.class, bytes_to_sha1 )
            .chainFrom ( Float.class, bytes_to_sha256 )
            .chainFrom ( Float.class, bytes_to_sha512 )
            .addTypeCasterFrom ( new CastFloatToString () )
            .noTypeCasterFrom ( Float.class, Time.class )
            .addTypeCasterFrom ( new CastIntegerToBigDecimal () )
            .addTypeCasterFrom ( new CastIntegerToBigInteger () )
            .addTypeCasterFrom ( new CastIntegerToBytes () )
            .addTypeCasterFrom ( new CastIntegerToDouble () )
            .addTypeCasterFrom ( new CastIntegerToFloat () )
            .addTypeCasterFrom ( new CastIntegerToLong () )
            .chainFrom ( Integer.class, bytes_to_md5 )
            .chainFrom ( Integer.class, bytes_to_sha1 )
            .chainFrom ( Integer.class, bytes_to_sha256 )
            .chainFrom ( Integer.class, bytes_to_sha512 )
            .addTypeCasterFrom ( new CastIntegerToString () )
            .noTypeCasterFrom ( Integer.class, Time.class )
            .addTypeCasterFrom ( new CastLongToBigDecimal () )
            .addTypeCasterFrom ( new CastLongToBigInteger () )
            .addTypeCasterFrom ( new CastLongToBytes () )
            .addTypeCasterFrom ( new CastLongToDouble () )
            .addTypeCasterFrom ( new CastLongToFloat () )
            .addTypeCasterFrom ( new CastLongToInteger () )
            .chainFrom ( Long.class, bytes_to_md5 )
            .chainFrom ( Long.class, bytes_to_sha1 )
            .chainFrom ( Long.class, bytes_to_sha256 )
            .chainFrom ( Long.class, bytes_to_sha512 )
            .addTypeCasterFrom ( new CastLongToString () )
            .noTypeCasterFrom ( Long.class, Time.class )
            .build ();
        this.add ( number_type );

        Type<String> text_type   = this.prepare ( "text",            // name
                                                  String.class,      // class
                                                  "~EOF~" )          // none
            .addTypeCasterFrom ( string_to_big_decimal )
            .addTypeCasterFrom ( string_to_big_integer )
            .addTypeCasterFrom ( new CastStringToBytes () )
            .addTypeCasterFrom ( new CastStringToDouble () )
            .addTypeCasterFrom ( new CastStringToFloat () )
            .addTypeCasterFrom ( new CastStringToInteger () )
            .addTypeCasterFrom ( new CastStringToLong () )
            .addTypeCasterFrom ( new CastStringToTime () )
            .build ();
        this.add ( text_type );

        Type<Time> time_type     = this.prepare ( "time",            // name
                                                  Time.class,        // class
                                                  Time.NEVER )       // none
            .noTypeCasterFrom ( Time.class, BigDecimal.class )
            .noTypeCasterFrom ( Time.class, BigDecimal.class )
            .addTypeCasterFrom ( new CastTimeToBytes () )
            .addTypeCasterFrom ( new CastTimeToCalendar () )
            .addTypeCasterFrom ( new CastTimeToDate () )
            .noTypeCasterFrom ( Time.class, Double.class )
            .noTypeCasterFrom ( Time.class, Float.class )
            .chainFrom ( Time.class, bytes_to_md5 )
            .chainFrom ( Time.class, bytes_to_sha1 )
            .chainFrom ( Time.class, bytes_to_sha256 )
            .chainFrom ( Time.class, bytes_to_sha512 )
            .noTypeCasterFrom ( Time.class, Integer.class )
            .noTypeCasterFrom ( Time.class, Long.class )
            .addTypeCasterFrom ( new CastTimeToString () )
            .addTypeCasterTo ( new CastCalendarToTime () )
            .addTypeCasterTo ( new CastDateToTime () )
            .build ();
        this.add ( time_type );
    }


    /**
     * @see musaico.types.SimpleTypeSystemBuilder#prepare(java.lang.String,java.lang.Class,java.lang.Object)
     */
    public
        <STORAGE_VALUE extends Serializable>
            TypeBuilder<STORAGE_VALUE> prepare (
                                                String name,
                                                Class<STORAGE_VALUE> storage_class,
                                                STORAGE_VALUE none
                                                )
    {
        return new PrimitiveTypeBuilder<STORAGE_VALUE> ( name,
                                                         storage_class,
                                                         none );
    }


    /**
     * @see musaico.types.TypeSystemBuilder#validate(Class<RAW_VALUE>,Type<STORAGE_VALUE>)
     */
    public 
        <STORAGE_VALUE extends Object>
            TypeSystemBuilder validate (
                                        Type<STORAGE_VALUE> type
                                        )
        throws TypeException
    {
        // CastableToPrimitiveTypeSystemBuilder provides validation of
        // the casting between primitive types
        super.validate ( type );

        // Not actually a PrimitiveType?  Can't be in a primitive
        // type system.  Sorry, bub.
        if ( ! ( type instanceof PrimitiveType ) )
        {
            throw new TypeException ( "Only PrimitiveTypes can be used in PrimitiveTypeSystems.  [%type%] is not a PrimitiveType so cannot be added to type system [%type_system%]",
                                      "type", type,
                                      "type_system", this );
        }

        return this;
    }
}
