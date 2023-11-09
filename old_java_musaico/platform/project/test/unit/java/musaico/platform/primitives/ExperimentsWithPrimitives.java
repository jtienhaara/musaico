package musaico.platform.primitives;

import java.math.BigDecimal;
import java.math.BigInteger;


import junit.framework.JUnit4TestAdapter;

import org.jmock.Mockery;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


import musaico.foundation.types.Instance;
import musaico.foundation.types.NoValue;
import musaico.foundation.types.Operation;
import musaico.foundation.types.Type;
import musaico.foundation.types.Typing;
import musaico.foundation.types.TypingEnvironment;
import musaico.foundation.types.Value;
import musaico.foundation.types.Vector;

import musaico.platform.primitives.casters.CastBigDecimalToBigInteger;

import musaico.platform.primitives.system.Primitives;

import musaico.platform.primitives.tag.string.Partitioned;


/**
 * <p>
 * A few random experiments with typing.
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
 * Copyright (c) 2013 Johann Tienhaara
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
public class ExperimentsWithPrimitives
{
    /**
     * <p>
     * Experiments with the primitives typing environment.
     * </p>
     */
    @Test
    public void experiments1 ()
        throws Typing.Violation
    {
        System.out.println ( "" );

        final TypingEnvironment env1 = Primitives.SYSTEM;

        final Type<String> string = env1.type ( String.class )
            .orCreateType ( "" );

        final Instance<String> document =
            string.scalar ( "Hello, world!\n"
                            + "The quick brown fox jumps over the lazy dog...\n"
                            + "Don't 'look' at me?" );

        System.out.println ( "Document:" );
        System.out.println ( "<Document>\n"
                             + document.value ().orThrowUnchecked ()
                             + "\n</Document>" );

        System.out.println ( "" );
        System.out.println ( "Paragraphs:" );
        for ( String paragraph : document.value ( String.class,
                                                  Partitioned.PARAGRAPH ) )
        {
            System.out.println ( "<Paragraph> "
                                 + paragraph
                                 + " </Paragraph>" );
        }

        System.out.println ( "" );
        System.out.println ( "Sentences:" );
        for ( String sentence : document.value ( String.class,
                                                 Partitioned.SENTENCE ) )
        {
            System.out.println ( "<Sentence> "
                                 + sentence
                                 + " </Sentence>" );
        }

        System.out.println ( "" );
        System.out.println ( "Sentence fragments:" );
        for ( String sentence_fragment : document.value ( String.class,
                                                          Partitioned.SENTENCE_FRAGMENT ) )
        {
            System.out.println ( "<Sentence_fragment> "
                                 + sentence_fragment
                                 + " </Sentence_fragment>" );
        }

        System.out.println ( "" );
        System.out.println ( "Words:" );
        for ( String word : document.value ( String.class,
                                             Partitioned.WORD ) )
        {
            System.out.println ( "<Word> "
                                 + word
                                 + " </Word>" );
        }
    }


    /**
     * Test typecasting.
     */
    @Test
    public void testTypeCasters ()
        throws Typing.Violation
    {
        // Type-casting between various Numbers.
        Instance<? extends Number> instance;
        Value<? extends Number> expected_value;
        Value<? extends Number> actual_value;
        NoValue<? extends Number> no_value;
        Vector<? extends Number> vector;

        // Cast BigDecimal...
        instance =
            Primitives.BIG_DECIMAL.scalar ( BigDecimal.TEN );
        // ...to BigInteger:
        expected_value =
            Primitives.BIG_INTEGER.scalar ( BigInteger.TEN ).value ();
        actual_value = instance.value ( BigInteger.class );;
        assertEquals ( "Incorrect cast from BigDecimal to BigInteger",
                       expected_value, actual_value );
    }


    /**
     * Tests operation types and typecasting.
     */
    @Test
    public void testOperationType ()
    {
        // Look up the "cast BigDecimal to BigInteger" type caster
        // by its id, and make sure it comes out right.
        Operation<?, ?> cast_big_decimal_to_big_integer =
            Primitives.STRING.scalar ( "cast[java.math.BigDecimal->java.math.BigInteger]" )
            .value ( Operation.class )
            .orNone (); // Don't throw exception, let assertion fail instead.
        assertEquals ( "Wrong type caster",
                       CastBigDecimalToBigInteger.class,
                       cast_big_decimal_to_big_integer.getClass () );
    }
}
