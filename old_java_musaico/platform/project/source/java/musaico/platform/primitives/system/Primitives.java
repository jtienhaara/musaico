package musaico.platform.primitives.system;

import java.io.Serializable;

import java.math.BigDecimal;
import java.math.BigInteger;


import musaico.foundation.condition.ConditionalRuntimeException;

import musaico.foundation.types.IdentityCast;
import musaico.foundation.types.Operation;
import musaico.foundation.types.SimpleTypingEnvironment;
import musaico.foundation.types.Tag;
import musaico.foundation.types.Type;

import musaico.platform.primitives.casters.CastBigDecimalToBigInteger;
import musaico.platform.primitives.casters.CastOperationToString;
import musaico.platform.primitives.casters.CastStringToOperation;

import musaico.platform.primitives.tag.string.Partitioned;


/**
 * <p>
 * A type system of primitives (number, text, and so on).
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
 * Copyright (c) 2009, 2012, 2013 Johann Tienhaara
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
public class Primitives
    extends SimpleTypingEnvironment
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20130825L;
    private static final String serialVersionHash =
        "0x0E6978DE53404C243EBDD6558C4A5343CFBAB655";


    /** The default Primitives typing environment. */
    public static final Primitives SYSTEM = new Primitives ();

    /** Binary type. */
    public static final Type<byte[]> BINARY =
        Primitives.SYSTEM.type ( byte[].class );

    /** Number types. */
    public static final Type<BigDecimal> BIG_DECIMAL =
        Primitives.SYSTEM.type ( BigDecimal.class );
    public static final Type<BigInteger> BIG_INTEGER =
        Primitives.SYSTEM.type ( BigInteger.class );
    public static final Type<Double> DOUBLE =
        Primitives.SYSTEM.type ( Double.class );
    public static final Type<Float> FLOAT =
        Primitives.SYSTEM.type ( Float.class );
    public static final Type<Integer> INTEGER =
        Primitives.SYSTEM.type ( Integer.class );
    public static final Type<Long> LONG =
        Primitives.SYSTEM.type ( Long.class );

    /** Operation type. */
    @SuppressWarnings("rawtypes") // Raw Operation.class.
    public static final Type<Operation> OPERATION =
        Primitives.SYSTEM.type ( Operation.class );

    /** String type. */
    public static final Type<String> STRING =
        Primitives.SYSTEM.type ( String.class );


    /**
     * <p>
     * Creates the default Primitives typing environment.
     * </p>
     *
     * @throws ConditionalRuntimeException If this primitives typing
     *                                     environment is horribly borken,
     *                                     so cannot make the default types.
     *                                     Should never happen in production.
     *                                     Allegedly...
     */
    @SuppressWarnings("rawtypes") // Operation.class != Operation<X>.class.
    protected Primitives ()
        throws ConditionalRuntimeException
    {
        super ( "primitives" );

        final Type<Class<?>> root_kind = this.rootKind ();


        // Tags:
        // ===========================
        // Partitioned tags (Strings split into document parts):
        this.register ( Partitioned.PARAGRAPH );
        this.register ( Partitioned.SENTENCE );
        this.register ( Partitioned.SENTENCE_FRAGMENT );
        this.register ( Partitioned.WORD );


        // Create the primitive types:
        // ===========================
        // Binary:
        this.createType ( byte[].class,
                          new Tag [ 0 ],
                          root_kind,
                          new byte [ 0 ] )
            .orThrowUnchecked ();
        // Number types:
        this.createType ( BigDecimal.class,
                          new Tag [ 0 ],
                          root_kind,
                          BigDecimal.ZERO )
            .orThrowUnchecked ();
        this.createType ( BigInteger.class,
                          new Tag [ 0 ],
                          root_kind,
                          BigInteger.ZERO )
            .orThrowUnchecked ();
        this.createType ( Double.class,
                          new Tag [ 0 ],
                          root_kind,
                          0D )
            .orThrowUnchecked ();
        this.createType ( Float.class,
                          new Tag [ 0 ],
                          root_kind,
                          0F )
            .orThrowUnchecked ();
        this.createType ( Integer.class,
                          new Tag [ 0 ],
                          root_kind,
                          0 )
            .orThrowUnchecked ();
        this.createType ( Long.class,
                          new Tag [ 0 ],
                          root_kind,
                          0L )
            .orThrowUnchecked ();
        // Operation:
        this.createType ( Operation.class,
                          new Tag [ 0 ],
                          root_kind,
                          Operation.NONE )
            .orThrowUnchecked ();
        // String:
        this.createType ( String.class,
                          new Tag [ 0 ],
                          root_kind,
                          "" )
            .orThrowUnchecked ();
        // String split into paragraphs:
        this.createType ( String.class,
                          new Tag [] { Partitioned.PARAGRAPH },
                          root_kind,
                          "" )
            .orThrowUnchecked ();
        // String split into sentences:
        this.createType ( String.class,
                          new Tag [] { Partitioned.SENTENCE },
                          root_kind,
                          "" )
            .orThrowUnchecked ();
        // String split into sentence fragments:
        this.createType ( String.class,
                          new Tag [] { Partitioned.SENTENCE_FRAGMENT },
                          root_kind,
                          "" )
            .orThrowUnchecked ();
        // String split into words:
        this.createType ( String.class,
                          new Tag [] { Partitioned.WORD },
                          root_kind,
                          "" )
            .orThrowUnchecked ();


        // Typecasters:
        // ===========================
        // Cast byte [] -> byte []
        this.register ( new IdentityCast<byte[]> ( byte[].class,
                                                   new byte [ 0 ] ) )
            .orThrowUnchecked ();
        // Cast BigDecimal -> BigDecimal
        this.register ( new IdentityCast<BigDecimal> ( BigDecimal.class,
                                                       BigDecimal.ZERO ) )
            .orThrowUnchecked ();
        // Cast BigDecimal -> BigInteger
        this.register ( new CastBigDecimalToBigInteger () )
            .orThrowUnchecked ();
        // Cast BigInteger -> BigInteger
        this.register ( new IdentityCast<BigInteger> ( BigInteger.class,
                                                       BigInteger.ZERO ) )
            .orThrowUnchecked ();
        // Cast double -> double
        this.register ( new IdentityCast<Double> ( Double.class,
                                                   0D ) )
            .orThrowUnchecked ();
        // Cast Float -> Float
        this.register ( new IdentityCast<Float> ( Float.class,
                                                  0F ) )
            .orThrowUnchecked ();
        // Cast Integer -> Integer
        this.register ( new IdentityCast<Integer> ( Integer.class,
                                                    0 ) )
            .orThrowUnchecked ();
        // Cast Long -> Long
        this.register ( new IdentityCast<Long> ( Long.class,
                                                 0L ) )
            .orThrowUnchecked ();
        // Cast Operation -> Operation
        this.register ( new IdentityCast<Operation> ( Operation.class,
                                                      Operation.NONE ) )
            .orThrowUnchecked ();
        // Cast Operation -> String
        this.register ( new CastOperationToString () )
            .orThrowUnchecked ();
        // Cast String -> String
        this.register ( new IdentityCast<String> ( String.class,
                                                   "" ) )
            .orThrowUnchecked ();
        // Cast String -> Operation
        this.register ( new CastStringToOperation ( this ) )
            .orThrowUnchecked ();
    }
}
