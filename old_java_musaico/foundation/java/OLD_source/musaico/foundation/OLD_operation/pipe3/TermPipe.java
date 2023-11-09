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

import musaico.foundation.term.countable.Many;

import musaico.foundation.term.incomplete.Reducible;
import musaico.foundation.term.incomplete.Reduce;
import musaico.foundation.term.incomplete.TermMustBeReducible;

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
 * @see musaico.foundation.operation.MODULE#COPYRIGHT
 * @see musaico.foundation.operation.MODULE#LICENSE
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

        // Nothing to clean up.
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
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               context );

        return (Pipe<?> [])
            new Pipe [ 0 ];
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
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               context );

        return (Pipe<?> [])
            new Pipe [ 0 ];
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
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               context );

        if ( this.term instanceof Reducible )
        {
            final Term<VALUE> cached = context.term ( this );
            if ( cached != null )
            {
                return cached;
            }

            final Reducible<VALUE, ?> reducible =
                (Reducible<VALUE, ?>) this.term;
            final Reduce<VALUE> reduce =
                new Reduce<VALUE> ( this.type ( context ) );
            final Term<VALUE> reduced = reduce.apply ( reducible );
            context.term ( this, reduced );
            return reduced;
        }

        return this.term;
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
                               array, context );
        // We rely on Countable.read ( ... ) to throw violations
        // of the Parameter2, 3 contracts.

        final Term<VALUE> term = this.read ( context );
        if ( term instanceof Cyclical )
        {
            final Cyclical<VALUE> cyclical =
                (Cyclical<VALUE>) term;
            final Countable<VALUE> header =
                cyclical.header ();
            // Throws Parameter2, 3 contract violations:
            final int num_header_read =
                header.read ( array, offset, length,
                              start );
            if ( num_header_read >= length )
            {
                return num_header_read;
            }

            int num_elements_read;
            int new_offset;
            int new_length;
            if ( num_header_read < 0 )
            {
                num_elements_read = 0;
                new_offset = offset;
                new_length = length;
            }
            else
            {
                num_elements_read = num_header_read;
                new_offset = offset + num_header_read;
                new_length = length - num_header_read;
            }

            final Countable<VALUE> cycle = cyclical.cycle ();
            while ( new_length > 0 )
            {
                // Throws Parameter2, 3 contract violations:
                final int num_cycle_read =
                    cycle.read ( array, new_offset, new_length
                                 start );

                if ( num_cycle_read < 0 )
                {
                    return num_elements_read;
                }

                num_elements_read += num_cycle_read;
                new_offset += num_cycle_read;
                new_length -= num_cycle_read;
            }

            return num_elements_read;
        }
        else if ( ! ( term instanceof Countable ) )
        {
            final Iterator<VALUE> iterator =
                term.indefiniteIterator ( (long) length );
            int a = 0;
            while ( a < length
                    && iterator.hasNext () )
            {
                array [ offset + a ] = iterator.next ();
                a ++;
            }

            return a;
        }

        final Countable<VALUE> countable =
            (Countable<VALUE>) term;
        // Throws Parameter2, 3 contract violations:
        final int num_elements_read =
            countable.read ( array, offset, length,
                             start );
        return num_elements_read;
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

        return this.term.type ();
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
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               context );

        return null;
    }
}
