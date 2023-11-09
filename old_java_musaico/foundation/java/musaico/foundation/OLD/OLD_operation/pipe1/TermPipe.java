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

import musaico.foundation.term.countable.Many;

import musaico.foundation.term.incomplete.Reducible;

import musaico.foundation.term.infinite.Continuous;
import musaico.foundation.term.infinite.Cyclical;

import musaico.foundation.term.multiplicities.Infinite;


/**
 * <p>
 * A Pipe from a Term.
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
public class TermPipe<VALUE extends Object>
    implements Pipe<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( TermPipe.class );


     // Enforces parameter obligations and so on for us.
    private final Advocate contracts;

    // The Term from which we pipe elements.
    private final Term<VALUE> term;

    // The output stream provided by this TermPipe (COUNTABLE, CYCLICAL, ...).
    private final Pipe.Stream stream;


    /**
     * <p>
     * Creates a new TermPipe.
     * </p>
     *
     * @param term The Term source to this pipe.  Must not be null.
     */
    public TermPipe (
            Term<VALUE> term
            )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               term );

        this.term = term;

        if ( this.term instanceof Countable )
        {
            this.stream = Pipe.Stream.COUNTABLE;
        }
        else if ( this.term instanceof Cyclical )
        {
            this.stream = Pipe.Stream.CYCLICAL;
        }
        else if ( this.term instanceof Continuous )
        {
            this.stream = Pipe.Stream.CONTINUOUS;
        }
        else if ( this.term instanceof Infinite )
        {
            this.stream = Pipe.Stream.INFINITE;
        }
        else if ( this.term instanceof Reducible )
        {
            this.stream = Pipe.Stream.REDUCIBLE;
        }
        else
        {
            this.stream = Pipe.Stream.OTHER;
        }

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

        if ( stream != this.stream )
        {
            if ( this.stream == Pipe.Stream.CYCLICAL )
            {
                if ( stream == Pipe.Stream.CYCLICAL_HEADER )
                {
                    final Cyclical<VALUE> cyclical =
                        (Cyclical<VALUE>) this.term;
                    final Countable<VALUE> header =
                        cyclical.header ();
                    final long header_index = header.clamp ( index );
                    return header_index;
                }
                else if ( stream == Pipe.Stream.CYCLICAL_CYCLE )
                {
                    final Cyclical<VALUE> cyclical =
                        (Cyclical<VALUE>) this.term;
                    final Many<VALUE> cycle = cyclical.cycle ();
                    final long cycle_index = cycle.clamp ( index );
                    return cycle_index;
                }
            }

            return 0L;
        }
        else if ( this.stream == Pipe.Stream.COUNTABLE )
        {
            final Countable<VALUE> countable =
                (Countable<VALUE>) this.term;
            final long clamped_index = countable.clamp ( index );
            return clamped_index;
        }
        else
        {
            // No other Term has any discrete elements, so no indices either.
            return 0L;
        }
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

        // Nothing to clean up.
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
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               context );

        return (Pipe<?> [])
            new Pipe [ 0 ];
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
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               stream, context );

        if ( stream != this.stream )
        {
            if ( this.stream == Pipe.Stream.CYCLICAL )
            {
                if ( stream == Pipe.Stream.CYCLICAL_HEADER )
                {
                    final Cyclical<VALUE> cyclical =
                        (Cyclical<VALUE>) this.term;
                    final Countable<VALUE> header =
                        cyclical.header ();
                    final long header_length = header.length ();
                    return header_length;
                }
                else if ( stream == Pipe.Stream.CYCLICAL_CYCLE )
                {
                    final Cyclical<VALUE> cyclical =
                        (Cyclical<VALUE>) this.term;
                    final Many<VALUE> cycle = cyclical.cycle ();
                    final long cycle_length = cycle.length ();
                    return cycle_length;
                }
            }

            return 0L;
        }
        else if ( this.stream == Pipe.Stream.COUNTABLE )
        {
            final Countable<VALUE> countable =
                (Countable<VALUE>) this.term;
            final long length = countable.length ();
            return length;
        }
        else
        {
            // No other Term has a length of discrete elements.
            return 0L;
        }
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
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               context );

        return (Pipe<?> [])
            new Pipe [ 0 ];
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
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               context );

        return this.term;
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
                               array, stream, context );
        // We rely on Countable.read ( ... ) to throw violations
        // of the Parameter2, 3 contracts.

        if ( stream != this.stream )
        {
            if ( this.stream == Pipe.Stream.CYCLICAL )
            {
                if ( stream == Pipe.Stream.CYCLICAL_HEADER )
                {
                    final Cyclical<VALUE> cyclical =
                        (Cyclical<VALUE>) this.term;
                    final Countable<VALUE> header =
                        cyclical.header ();
                    // Throws Parameter2, 3 contract violations:
                    final int num_elements_read =
                        header.read ( array, offset, length,
                                      first_element_index );
                    return num_elements_read;
                }
                else if ( stream == Pipe.Stream.CYCLICAL_CYCLE )
                {
                    final Cyclical<VALUE> cyclical =
                        (Cyclical<VALUE>) this.term;
                    final Many<VALUE> cycle = cyclical.cycle ();
                    // Throws Parameter2, 3 contract violations:
                    final int num_elements_read =
                        cycle.read ( array, offset, length,
                                     first_element_index );
                    return num_elements_read;
                }
            }

            return -1;
        }
        else if ( this.stream == Pipe.Stream.COUNTABLE )
        {
            final Countable<VALUE> countable =
                (Countable<VALUE>) this.term;
            // Throws Parameter2, 3 contract violations:
            final int num_elements_read =
                countable.read ( array, offset, length,
                                 first_element_index );
            return num_elements_read;
        }
        else
        {
            // No other Term has any discrete elements, so we can't
            // fill in the array.
            return -1;
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
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               context );

        return this.stream;
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

        return this.term.type ();
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
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               context );

        return null;
    }
}
