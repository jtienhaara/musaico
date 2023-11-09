package musaico.foundation.operation.primitive;

import java.io.Serializable;

import java.math.BigDecimal;


import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.operation.AbstractPipe;
import musaico.foundation.operation.Context;
import musaico.foundation.operation.Pipe;
import musaico.foundation.operation.Stream;
import musaico.foundation.operation.TermPipe;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Maybe;
import musaico.foundation.term.Term;
import musaico.foundation.term.Type;

import musaico.foundation.term.countable.Many;
import musaico.foundation.term.countable.No;
import musaico.foundation.term.countable.One;


/**
 * <p>
 * Tests the various primitive Pipes.
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
 * <p>
 * For copyright and licensing information refer to:
 * </p>
 *
 * @see musaico.foundation.operation.MODULE#COPYRIGHT
 * @see musaico.foundation.operation.MODULE#LICENSE
 */
public class TestPrimitives
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    public void testMerge ()
    {
        final Term<String> one = new One<String> ( null, // type
                                                   "1" );
        final Term<String> two = new One<String> ( null, // type
                                                   "2" );
        final Term<String> three = new One<String> ( null, // type
                                                     "3" );
        final Term<String> four = new One<String> ( null, // type
                                                    "4" );
        final Term<String> five = new One<String> ( null, // type
                                                    "5" );
        final Term<String> six = new One<String> ( null, // type
                                                   "6" );

        final TermPipe<String> pipe_one = new TermPipe<String> ( one );
        final TermPipe<String> pipe_two = new TermPipe<String> ( two );
        final TermPipe<String> pipe_three = new TermPipe<String> ( three );
        final TermPipe<String> pipe_four = new TermPipe<String> ( four );
        final TermPipe<String> pipe_five = new TermPipe<String> ( five );
        final TermPipe<String> pipe_six = new TermPipe<String> ( six );

        final Countable<Pipe<String, String>> pipes =
            new Many<Pipe<String, String>> ( null, // type
                                             pipe_one, pipe_two, pipe_three,
                                             pipe_four, pipe_five, pipe_six );

        final Merge<String> merge = new Merge<String> ( pipes,
                                                        null ); // stream_type
    }

    public static final void main (
            String [] args
            )
    {
        new TestPrimitives ().testMerge ();
    }
}
