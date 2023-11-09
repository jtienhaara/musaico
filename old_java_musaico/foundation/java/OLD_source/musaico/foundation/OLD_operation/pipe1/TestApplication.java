package musaico.foundation.operations;

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

import musaico.foundation.term.contracts.CountableMustIncludeIndices;
import musaico.foundation.term.contracts.ValueMustBeCountable;

import musaico.foundation.term.countable.AbstractCountable;
import musaico.foundation.term.countable.Many;
import musaico.foundation.term.countable.No;
import musaico.foundation.term.countable.One;


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
 * @see musaico.foundation.operations.MODULE#COPYRIGHT
 * @see musaico.foundation.operations.MODULE#LICENSE
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


     // Enforces parameter obligations and so on for us.
    private final Advocate contracts;

    // The main input Pipe.
    private final Pipe<VALUE> mainInput;

    // The "insert at" index.
    private final Pipe<Long> insertAt;

    // What to insert.
    private final Pipe<VALUE> elementsToInsert;


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
     * @param elements_to_insert The element(s) to insert at each
     *                           insert_at index.  Must not be null.
     */
    public TestApplication (
            Pipe<VALUE> main_input,
            Pipe<Long> insert_at,
            Pipe<VALUE> elements_to_insert
            )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               main_input, insert_at, elements_to_insert );

        this.mainInput = main_input;
        this.insertAt = insert_at;
        this.elementsToInsert = elements_to_insert;

        this.contracts = new Advocate ( this );
    }


    /**
     * @see musaico.foundation.operations.Pipe#clamp(long, musaico.foundation.operations.Pipe.Stream, musaico.foundation.operations.Context)
     */
    @Override
    public final long clamp (
            long index,
            Pipe.Stream stream,
            Context context
            )
        throws ParametersMustNotBeNull.Violation,
               Return.AlwaysGreaterThanOrEqualToNegativeOne.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               stream, context );

        if ( this.violation ( context ) != null )
        {
            return -1L;
        }

        if ( this.insertAt.length ( Pipe.Stream.COUNTABLE, context ) != 1L )
        {
            // Error output.
            final CountableMustIncludeIndices must_include_indices =
                new CountableMustIncludeIndices ( 0L );
            context.violation ( this,
                                must_include_indices.violation (
                                    context,                  // plaintiff
                                    new PipeInContext<Long> ( // evidence
                                        this.insertAt,
                                        context ) ) );
            return Countable.NONE;
        }
        else if ( this.elementsToInsert.stream ( context )
                      != Pipe.Stream.COUNTABLE )
        {
            // Error output.
            context.violation ( this,
                                ValueMustBeCountable.CONTRACT.violation (
                                    context,                   // plaintiff
                                    new PipeInContext<VALUE> ( // evidence
                                        this.elementsToInsert,
                                        context ) ) );
            return Countable.NONE;
        }

        final long length = this.length ( stream, context );
        final long clamped_index =
            AbstractCountable.clamp ( index,
                                      0L,
                                      Long.MAX_VALUE,
                                      length );
        return clamped_index;
    }


    /**
     * @see musaico.foundation.operations.Pipe#close(musaico.foundation.operations.Context)
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
        this.elementsToInsert.close ( context );

        // Check for violations, in case closing something caused
        // problems (unused elements etc).
        this.violation ( context );
    }


    /**
     * @see musaico.foundation.operations.Pipe#inputPipes(musaico.foundation.operations.Context)
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
                this.elementsToInsert
            };
    }


    /**
     * @see musaico.foundation.operations.Pipe#length(musaico.foundation.operations.Pipe.Stream, musaico.foundation.operations.Context)
     */
    @Override
    @SuppressWarnings("Unchecked") // Unchecked casts checked by this.stream.
    public final long length (
            Pipe.Stream stream,
            Context context
            )
        throws ParametersMustNotBeNull.Violation,
               Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        if ( this.violation ( context ) != null )
        {
            return 0L;
        }

        if ( this.insertAt.length ( Pipe.Stream.COUNTABLE, context ) != 1L )
        {
            // Error output.
            final CountableMustIncludeIndices must_include_indices =
                new CountableMustIncludeIndices ( 0L );
            context.violation ( this,
                                must_include_indices.violation (
                                    context,                  // plaintiff
                                    new PipeInContext<Long> ( // evidence
                                        this.insertAt,
                                        context ) ) );
            return 0L;
        }
        else if ( this.elementsToInsert.stream ( context )
                      != Pipe.Stream.COUNTABLE )
        {
            // Error output.
            context.violation ( this,
                                ValueMustBeCountable.CONTRACT.violation (
                                    context,                   // plaintiff
                                    new PipeInContext<VALUE> ( // evidence
                                        this.elementsToInsert,
                                        context ) ) );
            return 0L;
        }

        final long main_input_length =
            this.mainInput.length ( stream, context );
        final long insert_at_length =
            this.insertAt.length ( Pipe.Stream.COUNTABLE, context );
        final long elements_to_insert_length =
            this.elementsToInsert.length ( Pipe.Stream.COUNTABLE, context );

        if ( insert_at_length > 0L
             && ( Long.MAX_VALUE / insert_at_length )
                     < elements_to_insert_length )
        {
            // Can't maintain a Countable output with these inputs.
            // We'll output an Error instead.
            context.violation ( this,
                                ValueMustBeCountable.CONTRACT.violation (
                                    context,                   // plaintiff
                                    new PipeInContext<VALUE> ( // evidence
                                        this,
                                        context ) ) );
            return 0L;
        }
        else if ( ( Long.MAX_VALUE - main_input_length )
                  < ( insert_at_length * elements_to_insert_length ) )
        {
            // Can't maintain a Countable output with these inputs.
            // We'll output an Error instead.
            context.violation ( this,
                                ValueMustBeCountable.CONTRACT.violation (
                                    context,                   // plaintiff
                                    new PipeInContext<VALUE> ( // evidence
                                        this,
                                        context ) ) );
            return 0L;
        }

        final long length = main_input_length
            + insert_at_length * elements_to_insert_length;

        return length;
    }


    /**
     * @see musaico.foundation.operations.Application#mainInput(musaico.foundation.operations.Context)
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
     * @see musaico.foundation.operations.Pipe#outputPipes(musaico.foundation.operations.Context)
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
     * @see musaico.foundation.operations.Application#parameters(musaico.foundation.operations.Context)
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
                this.elementsToInsert
            };
    }


    /**
     * @see musaico.foundation.operations.Pipe#read(musaico.foundation.operations.Context)
     */
    @Override
    public final Term<VALUE> read (
            Context context
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        final Type<VALUE> type = this.type ( context );
        final TermViolation violation = this.violation ( context );
        if ( violation != null )
        {
            return new Error<VALUE> ( type, violation );
        }

        final long length = this.length ( this.stream ( context ), context );
        if ( length > (long) Integer.MAX_VALUE )
        {
            final ValueMustBeCountable.Violation length_violation =
                ValueMustBeCountable.CONTRACT.violation (
                    context,                   // plaintiff
                    new PipeInContext<VALUE> ( // evidence
                        this,
                        context ) );
            context.violation ( this,
                                length_violation );
            return new Error<VALUE> ( type, length_violation );
        }

        final Pipe.Stream stream = this.stream ( context );
        final VALUE [] temp_array = type.array ( (int) length );
        final int num_elements =
            this.read ( temp_array, 0, (int) length,
                        0L, stream, context );
        if ( num_elements == 0 )
        {
            return new No<VALUE> ( type );
        }
        else if ( num_elements == 1 )
        {
            return new One<VALUE> ( type, temp_array [ 0 ] );
        }

        final VALUE [] array;
        if ( num_elements == (int) length )
        {
            array = temp_array;
        }
        else
        {
            array = type.array ( num_elements );
            System.arraycopy ( temp_array, 0,
                               array, 0, num_elements );
        }

        final Many<VALUE> many = new Many<VALUE> ( type, array );
        return many;
    }


    /**
     * @see musaico.foundation.operations.Pipe#read(java.lang.Object[], int, int, long, musaico.foundation.operatios.Pipe.Stream, musaico.foundation.operations.Context)
     */
    @Override
    public final int read (
            VALUE [] array,
            int offset,
            int length,
            long first_element_index,
            Pipe.Stream stream,
            Context context
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustBeGreaterThanOrEqualToZero.Violation,
               Parameter3.MustBeGreaterThanOrEqualToZero.Violation,
               Return.AlwaysGreaterThanOrEqualToNegativeOne.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               array, stream );

        if ( this.violation ( context ) != null )
        {
            return -1;
        }

        if ( this.insertAt.length ( Pipe.Stream.COUNTABLE, context ) != 1L )
        {
            // Error output.
            final CountableMustIncludeIndices must_include_indices =
                new CountableMustIncludeIndices ( 0L );
            context.violation ( this,
                                must_include_indices.violation (
                                    context,                  // plaintiff
                                    new PipeInContext<Long> ( // evidence
                                        this.insertAt,
                                        context ) ) );
            return -1;
        }
        else if ( this.elementsToInsert.stream ( context )
                      != Pipe.Stream.COUNTABLE )
        {
            // Error output.
            context.violation ( this,
                                ValueMustBeCountable.CONTRACT.violation (
                                    context,                   // plaintiff
                                    new PipeInContext<VALUE> ( // evidence
                                        this.elementsToInsert,
                                        context ) ) );
            return -1;
        }

        // We rely on this.mainInput.read ( ... ) to throw violations
        // of the Parameter2, 3 contracts.

        final long start = this.clamp ( first_element_index,
                                        stream,
                                        context );
        if ( start < 0L )
        {
            // No such index.
            final CountableMustIncludeIndices must_include_indices =
                new CountableMustIncludeIndices ( first_element_index );
            context.violation ( this,
                                must_include_indices.violation (
                                    context,                   // plaintiff
                                    new PipeInContext<VALUE> ( // evidence
                                        this,
                                        context ) ) );
            return -1;
        }

        final Pipe.Stream this_stream = this.mainInput.stream ( context );
        if ( this_stream == Pipe.Stream.COUNTABLE
             && stream == this_stream )
        {
            final Long [] insert_at = new Long [ 1 ];
            final int num_read =
                this.insertAt.read ( insert_at, 0, 1,
                                     Countable.FROM_START,
                                     Pipe.Stream.COUNTABLE,
                                     context );
            if ( num_read != 1 )
            {
                // Error output.
                final CountableMustIncludeIndices must_have_1_element =
                    new CountableMustIncludeIndices ( Countable.FROM_START );
                context.violation ( this,
                                    must_have_1_element.violation (
                                        context,                  // plaintiff
                                        new PipeInContext<Long> ( // evidence
                                            this.insertAt,
                                            context ) ) );
                return -1;
            }

            final long main_input_length =
                this.mainInput.length ( stream, context );
            final long insert;
            if ( insert_at [ 0 ] == Countable.AFTER_LAST
                 || insert_at [ 0 ] == main_input_length )
            {
                insert = main_input_length;
            }
            else
            {
                insert = this.mainInput.clamp (
                             insert_at [ 0 ],
                             this.mainInput.stream ( context ),
                             context );
                if ( insert < 0L )
                {
                    // Error output.
                    final CountableMustIncludeIndices must_have_1_element =
                        new CountableMustIncludeIndices ( insert_at [ 0 ] );
                    context.violation (
                        this,
                        must_have_1_element.violation (
                            context,                   // plaintiff
                            new PipeInContext<VALUE> ( // evidence
                                this.mainInput,
                                context ) ) );
                    return -1;
                }
            }

            if ( insert > ( start + (long) length ) )
            {
                return this.mainInput.read ( array, offset, length,
                                             first_element_index,
                                             stream,
                                             context );
            }

            final int pre_insert_length = (int) ( insert - start );
            final int pre_insert_read =
                this.mainInput.read ( array, offset, pre_insert_length,
                                      start, stream, context );
            if ( pre_insert_read < 0 )
            {
                final TermViolation violation =
                    context.violation ( this.mainInput );
                if ( violation != null )
                {
                    context.violation ( this, violation );
                }
                return -1;
            }

            final int insert_length =
                length - pre_insert_read;
            final int insert_read =
                this.elementsToInsert.read ( array, offset, insert_length,
                                             start, stream, context );
            if ( insert_read < 0 )
            {
                final TermViolation violation =
                    context.violation ( this.elementsToInsert );
                if ( violation != null )
                {
                    context.violation ( this, violation );
                    return -1;
                }
                else
                {
                    return pre_insert_read;
                }
            }

            final int post_insert_length =
                length - pre_insert_read - insert_read;
            if ( post_insert_length <= 0 )
            {
                return length;
            }
            final int post_insert_read =
                this.mainInput.read ( array, offset, post_insert_length,
                                      start, stream, context );
            if ( post_insert_read < 0 )
            {
                final TermViolation violation =
                    context.violation ( this.mainInput );
                if ( violation != null )
                {
                    context.violation ( this, violation );
                    return -1;
                }
                else
                {
                    return pre_insert_read + insert_read;
                }
            }

            final int total_read =
                pre_insert_read + insert_read + post_insert_read;
            return total_read;
        }
        else
        {
            throw new UnsupportedOperationException("!!! BLA STREAM " + stream + " NOT IMPLEMENTED");
        }
    }


    /**
     * @see musaico.foundation.operations.Pipe#stream(musaico.foundation.operations.Context)
     */
    @Override
    public final Pipe.Stream stream (
            Context context
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return this.mainInput.stream ( context );
    }


    /**
     * @see musaico.foundation.operations.Pipe#type(musaico.foundation.operations.Context)
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
     * @see musaico.foundation.operations.Pipe#violation(musaico.foundation.operations.Context)
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

        violation = context.violation ( this.elementsToInsert );
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
