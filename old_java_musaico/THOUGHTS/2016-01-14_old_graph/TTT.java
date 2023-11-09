package musaico.foundation.graph;

import java.util.Iterator;


import musaico.foundation.contract.Contract;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.value.Countable;
import musaico.foundation.value.Maybe;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueViolation;

import musaico.foundation.value.builder.ValueBuilder;

import musaico.foundation.value.finite.No;
import musaico.foundation.value.finite.One;

import musaico.foundation.value.normal.ValueMustNotBeEmpty;
import musaico.foundation.value.normal.ValueMustNotBeInfinite;


public class TTT
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    @SuppressWarnings("unchecked") // sigh generic array creation param types.
    public static void main ( String [] args )
    {
        final Op concatenate =
            new Op ( ValueMustNotBeInfinite.CONTRACT,
                     ValueMustNotBeInfinite.CONTRACT )
            {
                private static final long serialVersionUID = 1L;

                protected Value<?> evaluate ( Value<?> ... parameters )
                {
                    final Iterator<?> left = parameters [ 0 ].iterator ();
                    final Iterator<?> right = parameters [ 1 ].iterator ();
                    final ValueBuilder<String> builder =
                        new ValueBuilder<String> ( String.class );
                    while ( left.hasNext ()
                            && right.hasNext () )
                    {
                        final String left_string =
                            "" + left.next ();
                        final String right_string =
                            "" + right.next ();

                        final String concatenated = left_string + right_string;

                        builder.add ( concatenated );
                    }

                    while ( left.hasNext () )
                    {
                        builder.add ( "" + left.next () );
                    }

                    while ( right.hasNext () )
                    {
                        builder.add ( "" + right.next () );
                    }

                    final Countable<String> concatenated =
                        builder.build ();

                    return concatenated;
                }
            };


        Value<?> [] inputs;
        Maybe<Value<?>> maybe_output;

        inputs = new Value<?> []
        {
            new One<String> ( String.class, "Hello, " ),
            new One<String> ( String.class, "world" )
        };

        maybe_output = concatenate.traverse ( concatenate.enter (),
                                              inputs );
        System.out.println ( "!!! MAYBE OUTPUT = " + maybe_output );
        for ( Value<?> output : maybe_output )
        {
            System.err.println ( "!!! OUTPUT = '" + output + "'" );
        }
    }
}
