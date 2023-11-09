package musaico.foundation.operation;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.Parameter3;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;
import musaico.foundation.term.Type;

import musaico.foundation.term.abnormal.Error;

import musaico.foundation.term.contracts.CountableLengthMustBe;
import musaico.foundation.term.contracts.ValueMustBeCountable;

import musaico.foundation.term.countable.AbstractCountable;
import musaico.foundation.term.countable.Many;
import musaico.foundation.term.countable.No;
import musaico.foundation.term.countable.One;

import musaico.foundation.term.infinite.Cyclical;
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
    public static final TermMustMeetAtLeastOneContract CONTRACT =
        new TermMustMeetAtLeastOneContract (
            new TermMustMeetAllContracts (
                TermMustBeCountable.CONTRACT,
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


    private static class InsertInduction<ELEMENT extends Object>
        implements Induction<ELEMENT>, Serializable
    {
        /** The MusaicoModule to which this class or interface belongs. */
        public static final MODULE PARENT_MODULE = MODULE.MODULE;

        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID = MODULE.VERSION;

        private final TestApplication<ELEMENT> insert;

        private final Cursor<ELEMENT> input;
        private final Cursor<Long> insertAt;
        private final Cursor<ELEMENT> elements;

        private final Output<ELEMENT> output;

        public InsertInduction (
                TestApplication<ELEMENT> insert,
                Cursor<ELEMENT> input,
                Cursor<Long> insert_at,
                Cursor<ELEMENT> elements,
                Output<ELEMENT> output
                )
        {
            this.insert = insert;
            this.input = input;
            this.insertAt = insert_at;
            this.elements = elements;
            this.output = output;
        }


        @Override
        public Induction<ELEMENT> step ()
        {
            final Long insert_at_index = this.insertAt.head ();
            if ( insert_at_index == null )
            {
                this.output.read ( this.input );
                return null;
            }
            else if ( insert_at_index != Countable.LAST
                      && insert_at_index != this.input.index ()
                      && this.input.isEmpty () )
            {
                return null;
            }
            else if ( this.input.isEmpty () )
            {
                this.elements.resetIndex ();
                this.output.read ( elements );
                return null;
            }

            long input_index = this.input.index ();
            if ( insert_at_index <= input_index )
            {
                this.elements.resetIndex ();
                this.output.read ( this.elements );
                this.insertAt.step ();
                return this;
            }

            final int num_elements_read =
                this.output.read ( this.input,
                                   insert_at_index - 1L );
            this.elements.resetIndex ();
            return this;
        }
    }


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

        // Check for violations, in case closing something caused
        // problems (unused elements etc).
        this.violation ( context );
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
     * @see musaico.foundation.operation.Pipe#read(musaico.foundation.operation.Context)
     */
    @Override
    public final Term<VALUE> read (
            Context context
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        final Term<VALUE> cached = context.term ( this );
        if ( cached != null )
        {
            return cached;
        }

        final Type<VALUE> type = this.type ( context );
        final TermViolation violation = this.violation ( context );
        if ( violation != null )
        {
            final Error<VALUE> error = new Error<VALUE> ( type, violation );
            context.term ( this, error );
            return error;
        }

        final Term<Long> maybe_insert_at = this.insertAt.read ( context );
        this.contracts.check ( ValueMustBeCountable.CONTRACT,
                               maybe_insert_at );
        this.contracts.check ( CountableLengthMustBe.INT_32_SIZED,
                               maybe_insert_at );

        final Term<VALUE> input = this.mainInput.read ( context );
        final Term<VALUE> elements = this.elements.read ( context );

        final Countable<Long> insert_at = (Countable<Long>) maybe_insert_at;

        final Countable<VALUE> input_header;
        final Countable<VALUE> input_cycle;
        if ( input instanceof Countable )
        {
            input_header = (Countable<VALUE>) input;
            input_cycle = null;
        }
        else if ( input instanceof Cyclical )
        {
            final Cyclical<VALUE> cyclical = (Cyclical<VALUE>) input;
            input_header = cyclical.header ();
            input_cycle = cyclical.cycle ();
        }
        else
        {
            final TermViolation violation =
                TestApplication.CONTRACT.violation (
                    this,    // plaintiff
                    input ); // evidence
            context.violation ( this, violation );
            final Error<VALUE> error = new Error<VALUE> ( type, violation );
            return error;
        }

        final Countable<VALUE> elements_header;
        final Countable<VALUE> elements_cycle;
        if ( elements instanceof Countable )
        {
            elements_header = (Countable<VALUE>) elements;
            elements_cycle = null;
        }
        else if ( elements instanceof Cyclical )
        {
            final Cyclical<VALUE> cyclical = (Cyclical<VALUE>) elements;
            elements_header = cyclical.header ();
            elements_cycle = cyclical.cycle ();
        }
        else
        {
            final TermViolation violation =
                TestApplication.CONTRACT.violation (
                    this,       // plaintiff
                    elements ); // evidence
            context.violation ( this, violation );
            final Error<VALUE> error = new Error<VALUE> ( type, violation );
            return error;
        }

        !!!!!;
        if ( ( (long) Integer.MAX_VALUE - input_header.length () )
             < ( insert_at.length () * elements_header.length () ) )
        {
            throw CountableLengthMustBe.INT_32_SIZED.violation (
                this,           // plaintiff
                input_header ); // evidence
        }

        final long total_length = input_header.length ()
            + insert_at.length () * countable_elements.length ();

        final VALUE [] array = !!!!!!!!
        throw new RuntimeException ( "!!!" );
    }


    /**
     * @see musaico.foundation.operation.Pipe#read(java.lang.Object[], int, int, long, musaico.foundation.operation.Context)
     */
    @Override
    public final int read (
            VALUE [] array,
            int offset,
            int length,
            long start,
            Context context
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustBeGreaterThanOrEqualToZero.Violation,
               Parameter3.MustBeGreaterThanOrEqualToZero.Violation,
               Return.AlwaysGreaterThanOrEqualToNegativeOne.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               array );

        if ( this.violation ( context ) != null )
        {
            return -1;
        }

        final Cursor<VALUE> input =
            new Cursor<VALUE> ( this.mainInput, context );
        final Cursor<Long> insert_at =
            new Cursor<Long> ( this.insertAt, context );
        final Cursor<VALUE> elements =
            new Cursor<VALUE> ( this.elements, context );
        final Output<VALUE> output =
            new Output<VALUE> (
                array,
                offset,
                length );

        Induction<VALUE> induction =
            new TestApplication.InsertInduction<VALUE> ( this,
                                                         input,
                                                         insert_at,
                                                         elements,
                                                         output );
        int num_read = 0;
        int infinite_loop_protector = 0;
        while ( induction != null )
        {
            if ( output.isComplete () )
            {
                return output.numElementsRead ();
            }

            if ( infinite_loop_protector > 32768 )
            {
                throw new RuntimeException ( "!!!" );
            }

            induction = induction.step ();

            infinite_loop_protector ++;
        }

        return output.numElementsRead ();
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


    /**
     * @see musaico.foundation.operation.Pipe#violation(musaico.foundation.operation.Context)
     */
    @Override
    public final TermViolation violation (
            Context context
            )
        throws ParametersMustNotBeNull.Violation
    {
        TermViolation violation = context.violation ( this );
        if ( violation != null )
        {
            return violation;
        }

        violation = context.violation ( this.insertAt );
        if ( violation != null )
        {
            context.violation ( this, violation );
            return violation;
        }

        violation = context.violation ( this.elements );
        if ( violation != null )
        {
            context.violation ( this, violation );
            return violation;
        }

        violation = context.violation ( this.mainInput );
        if ( violation != null )
        {
            context.violation ( this, violation );
            return violation;
        }

        return null;
    }
}
