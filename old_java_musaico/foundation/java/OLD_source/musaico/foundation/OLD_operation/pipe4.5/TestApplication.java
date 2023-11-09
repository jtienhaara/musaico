package musaico.foundation.operation;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter4;
import musaico.foundation.contract.obligations.Parameter5;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;
import musaico.foundation.term.Type;

import musaico.foundation.term.abnormal.Error;

import musaico.foundation.term.builder.TermBuilder;

import musaico.foundation.term.contracts.CountableLengthMustBe;
import musaico.foundation.term.contracts.TermMustMeetAllContracts;
import musaico.foundation.term.contracts.TermMustMeetAtLeastOneContract;
import musaico.foundation.term.contracts.ValueMustBeCountable;

import musaico.foundation.term.countable.AbstractCountable;
import musaico.foundation.term.countable.Many;
import musaico.foundation.term.countable.No;
import musaico.foundation.term.countable.One;

import musaico.foundation.term.infinite.Cyclical;
import musaico.foundation.term.infinite.CyclicalCycleMustMeet;
import musaico.foundation.term.infinite.CyclicalHeaderMustMeet;
import musaico.foundation.term.infinite.TermMustBeCyclical;


/**
 * <p>
 * A test Application.
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
public class TestApplication<VALUE extends Object>
    implements Application<VALUE, VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( TestApplication.class );

    // The contract for the elements to insert, for each input,
    // and for the output.
    // The elements to insert, the input, and the output must each be either:
    // 1) Countable with 0 - Integer.MAX_VALUE elements; or
    // 2) Cyclical, with header and cycle that are
    //    each less than or equal to Integer.MAX_VALUE in length.
    @SuppressWarnings("unchecked") // Generic array creation varargs.
    public static final TermMustMeetAtLeastOneContract CONTRACT =
        new TermMustMeetAtLeastOneContract (
            new TermMustMeetAllContracts (
                ValueMustBeCountable.CONTRACT,
                CountableLengthMustBe.INT_32_SIZED ),
            new TermMustMeetAllContracts (
                TermMustBeCyclical.CONTRACT,
                new CyclicalHeaderMustMeet (
                    CountableLengthMustBe.INT_32_SIZED
                ),
                new CyclicalCycleMustMeet (
                    CountableLengthMustBe.INT_32_SIZED
                )
            )
        );




     // Enforces parameter obligations and so on for us.
    private final Advocate contracts;

    // The main input Pipe.
    private final Pipe<VALUE> mainInput;

    // The "insert at" index.
    private final Pipe<Long> insertAt;

    // What to insert.
    private final Pipe<VALUE> elements;


    /**
     * <p>
     * Creates a new TestApplication.
     * </p>
     *
     * @param main_input The input to this insert operation.
     *                   Must not be null.
     *
     * @param insert_at Where to insert the elements.  Must not be null.
     *
     * @param elements The element(s) to insert at each
     *                 insert_at index.  Must not be null.
     */
    public TestApplication (
            Pipe<VALUE> main_input,
            Pipe<Long> insert_at,
            Pipe<VALUE> elements
            )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               main_input, insert_at, elements );

        this.mainInput = main_input;
        this.insertAt = insert_at;
        this.elements = elements;

        this.contracts = new Advocate ( this );
    }


    /**
     * @see musaico.foundation.operation.Pipe#close(musaico.foundation.operation.Context)
     */
    @Override
    public final void close (
            Context context
            )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               context );

        this.mainInput.close ( context );
        this.insertAt.close ( context );
        this.elements.close ( context );
    }


    /**
     * @see musaico.foundation.operation.Pipe#inputPipes(musaico.foundation.operation.Context)
     */
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"}) // Generic array creation.
    public final Pipe<?> [] inputPipes (
            Context context
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        return (Pipe<?> [])
            new Pipe []
            {
                this.mainInput,
                this.insertAt,
                this.elements
            };
    }


    /**
     * @see musaico.foundation.operation.Application#mainInput(musaico.foundation.operation.Context)
     */
    @Override
    public final Pipe<VALUE> mainInput (
            Context context
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return this.mainInput;
    }


    /**
     * @see musaico.foundation.operation.Pipe#outputPipes(musaico.foundation.operation.Context)
     */
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"}) // Generic array creation.
    public final Pipe<?> [] outputPipes (
            Context context
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        return (Pipe<?> [])
            new Pipe [ 0 ];
    }


    /**
     * @see musaico.foundation.operation.Application#parameters(musaico.foundation.operation.Context)
     */
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"}) // Generic array creation.
    public final Pipe<?> [] parameters (
            Context context
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        return (Pipe<?> [])
            new Pipe []
            {
                this.insertAt,
                this.elements
            };
    }


    /**
     * @see musaico.foundation.operation.Pipe#read(musaico.foundation.operation.Stream)
     */
    @Override
    public final long read (
            Stream<VALUE> output
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter4.MustBeGreaterThanOrEqualToZero.Violation,
               Parameter5.MustBeGreaterThanOrEqualToZero.Violation,
               Return.AlwaysGreaterThanOrEqualToNegativeOne.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               output );

        final Context context = output.context ();
        final long minimum_finite_elements = output.minimumFiniteElements ();

        final Term<VALUE> cached = context.term ( this );
        if ( cached != null
             && ! ( cached instanceof Countable )
             && ! ( cached instanceof Cyclical ) )
        {
            output_other.add ( cached );
            return -1L;
        }

        final Stream<VALUE> input =
            new Stream<VALUE> (
                this.mainInput,                     // pipe
                context,                            // context
                0L,                                 // finite_offset,
                minimum_finite_elements );          // minimum_finite_elements
        final Stream<Long> insert_at =
            new Stream<Long> (
                this.insertAt,                      // pipe
                context,                            // context
                0L,                                 // finite_offset,
                minimum_finite_elements );          // minimum_finite_elements
        final Stream<VALUE> elements =
            new Stream<VALUE> (
                this.elements,                      // pipe
                context,                            // context
                0L,                                 // finite_offset,
                minimum_finite_elements );          // minimum_finite_elements

        TestApplication<VALUE> induction = this;

        int infinite_loop_protector = 0;
        while ( induction != null )
        {
            if ( output.isComplete () )
            {
                break;
            }

            if ( infinite_loop_protector > 32768 )
            {
                throw new RuntimeException ( "!!!" );
            }

            induction =
                induction.step ( input,
                                 insert_at,
                                 elements,
                                 output );

            infinite_loop_protector ++;
        }

        return output.numElementsWritten ();
    }


    public TestApplication<VALUE> step (
            Stream<VALUE> input,
            Stream<Long> insert_at,
            Stream<VALUE> elements,
            Stream<VALUE> output 
            )
    {
        final Long insert_at_index = insert_at.head ();
        if ( insert_at_index == null )
        {
            // No more inserts.
            output.pipe ( input );
            return null;
        }
        else if ( insert_at_index != Countable.LAST
                  && insert_at_index != input.index ()
                  && input.isAtEnd () )
        {
            // The input isn't long enough for all the inserts.
            return null;
        }
        else if ( input.isAtEnd () )
        {
            // Insert after the end of the input.
            elements.resetIndex ();
            output.pipe ( elements );
            return null;
        }

        long input_index = input.index ();
        if ( insert_at_index <= input_index )
        {
            // Insert before the start of the input.
            elements.resetIndex ();
            output.pipe ( elements );
            insert_at.step ();
            return this;
        }

        // Some elements from the main input before the next insert.
        // Split the main input Term.
        final long num_elements_written =
            output.pipe ( input,
                          insert_at_index - 1L );
        elements.resetIndex ();
        return this;
    }


    /**
     * @see musaico.foundation.operation.Pipe#type(musaico.foundation.operation.Context)
     */
    @Override
    public final Type<VALUE> type (
            Context context
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               context );

        return this.mainInput.type ( context );
    }



    public static void main ( String [] args )
    {
        final String usage =
            "Usage:\n"
            + "java TestApplication input... insert_at... insert_text...";

        final String [] inputs;
        final Long [] insert_ats;
        final String [] inserts;
        try
        {
            int insert_ats_start = -1;
            for ( int a = 1; a < args.length; a ++ )
            {
                try
                {
                    Long.parseLong ( args [ a ] );
                    insert_ats_start = a;
                    break;
                }
                catch ( NumberFormatException e )
                {
                    // Still inputs.
                }
            }

            if ( insert_ats_start <= 0 )
            {
                System.out.println ( "No insert-at index(es)." );
                System.out.println ( " " );
                System.out.println ( usage );
                System.out.println ( " " );
                return;
            }

            int inserts_start = -1;
            for ( int a = insert_ats_start + 1; a < args.length; a ++ )
            {
                try
                {
                    Long.parseLong ( args [ a ] );
                    // Still insert at indices.
                }
                catch ( NumberFormatException e )
                {
                    inserts_start = a;
                    break;
                }
            }

            if ( inserts_start <= insert_ats_start )
            {
                System.out.println ( "No text(s) to insert." );
                System.out.println ( " " );
                System.out.println ( usage );
                System.out.println ( " " );
                return;
            }

            inputs = new String [ insert_ats_start ];
            insert_ats = new Long [ inserts_start - insert_ats_start ];
            inserts = new String [ args.length - inserts_start ];

            System.arraycopy ( args, 0,
                               inputs, 0, inputs.length );
            for ( int ia = 0; ia < insert_ats.length; ia ++ )
            {
                insert_ats [ ia ] =
                    Long.parseLong ( args [ ia + insert_ats_start ] );
            }
            System.arraycopy ( args, inserts_start,
                               inserts, 0, inserts.length );
        }
        catch ( Exception e )
        {
            System.err.println ( "Error:" );
            e.printStackTrace ();
            System.out.println ( " " );
            return;
        }




        final Countable<String> inputs_term;
        if ( inputs.length == 0 )
        {
            inputs_term = new No<String> ( null ); // type
        }
        else if ( inputs.length == 1 )
        {
            inputs_term = new One<String> ( null, // type
                                            inputs [ 0 ] );
        }
        else
        {
            inputs_term = new Many<String> ( null, // type
                                             inputs );
        }

        final Countable<Long> insert_ats_term;
        if ( insert_ats.length == 0 )
        {
            insert_ats_term = new No<Long> ( null ); // type
        }
        else if ( insert_ats.length == 1 )
        {
            insert_ats_term = new One<Long> ( null, // type
                                              insert_ats [ 0 ] );
        }
        else
        {
            insert_ats_term = new Many<Long> ( null, // type
                                               insert_ats );
        }

        final Countable<String> inserts_term;
        if ( inserts.length == 0 )
        {
            inserts_term = new No<String> ( null ); // type
        }
        else if ( inserts.length == 1 )
        {
            inserts_term = new One<String> ( null, // type
                                             inserts [ 0 ] );
        }
        else
        {
            inserts_term = new Many<String> ( null, // type
                                              inserts );
        }
    }
}
