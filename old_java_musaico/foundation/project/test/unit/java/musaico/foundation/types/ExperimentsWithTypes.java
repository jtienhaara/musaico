package musaico.foundation.types;

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
 * Copyright (c) 2013-2018 Johann Tienhaara
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
public class ExperimentsWithTypes
{
    /**
     * <p>
     * Experiments with types and typing environments.
     * </p>
     */
    @Test
    public void experiments1 ()
        throws Typing.Violation
    {
        System.out.println ( "" );

        final TypingEnvironment env1 =
            new SimpleTypingEnvironment ( "env1" );

        final Type<String> string = env1.type ( String.class,
                                                new Tag [ 0 ] )
            .orCreateType ( "" );
        final Instance<String> i1 = string.scalar ( "Hello, world" );
        System.out.println ( "i1.value () = "
                             + i1.value ().orThrowChecked () );

        final Operation<Integer, String> cast_integer_to_string =
            new Cast<Integer, String> ( Integer.class, String.class, "" )
        {
            private static final long serialVersionUID = 1L;
            @Override
            public Value<String> evaluate ( Value<Integer> input )
            {
                final ValueBuilder<String> builder =
                    new ValueBuilder<String> ( String.class, "" );
                for ( Integer input_element : input )
                {
                    final String output_element =
                        "" + input_element;
                    builder.add ( output_element );
                }

                return builder.build ();
            }
        };

        env1.register ( cast_integer_to_string )
            .orThrowChecked ();

        final Type<Integer> integer = env1.type ( Integer.class,
                                                  new Tag [ 0 ] )
            .orCreateType ( 0 );

        final Instance<Integer> i2 = integer.scalar ( 42 );
        final int i2_value = (int) i2.value ().orThrowChecked ();
        final String i2_as_string =
            i2.value ( String.class ).orThrowChecked ();
        System.out.println ( "i2 = " + i2_value
                             + " -> string = " + i2_as_string );

        final Value<Integer> i3_values = integer.builder ().add ( 1, 2, 3 )
            .build ();
        final Instance<Integer> i3 = integer.instance ( i3_values );
        final String [] i3_as_strings =
            i3.value ( String.class ).toArray ();
        final StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( "{" );
        boolean is_first = true;
        for ( String i3_str : i3_as_strings )
        {
            if ( is_first )
            {
                is_first = false;
            }
            else
            {
                sbuf.append ( "," );
            }

            sbuf.append ( " " + i3_str );
        }
        if ( ! is_first )
        {
            sbuf.append ( " " );
        }
        sbuf.append ( "}" );
        final String i3_as_strings_text = sbuf.toString ();
        System.out.println ( "i3 = " + i3_values
                             + " -> string [] = " + i3_as_strings_text );

        final int i4_int = 42;
        final String i4_as_string =
            string.from ( Integer.class, i4_int ).orThrowChecked ();
        System.out.println ( "i4 int = " + i4_int
                             + " --> string = " + i4_as_string );
    }
}
