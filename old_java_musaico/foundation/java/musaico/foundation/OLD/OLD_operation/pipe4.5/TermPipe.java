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


    private final Term<VALUE> read (
            Context context
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               context );

        final Term<VALUE> cached = context.term ( this );
        if ( cached != null )
        {
            return cached;
        }

        if ( this.term instanceof Reducible )
        {
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

        final Term<VALUE> term = this.read ( context );
        if ( term instanceof Cyclical )
        {
            final Cyclical<VALUE> cyclical =
                (Cyclical<VALUE>) term;
            final Countable<VALUE> header =
                cyclical.header ();

            long remaining_finite = 0L;
            final Countable<VALUE> finite;
            if ( finite_offset == 0L )
            {
                // Start from the beginning of the finite header.
                finite = header;
            }
            else if ( ! output.isInfiniteCycleRequired () )
            {
                // We're not going to read in the infinite cycle,
                // so only read what's necessary.
                final long end =
                    finite_offset + minimum_finite_elements - 1L;
                if ( end < header.length () )
                {
                    // Start from the middle somewhere,
                    // and read everything we need.
                    finite = header.range ( finite_offset,
                                            end );
                }
                else
                {
                    // Start from the middle somewhere and read to the end.
                    // we still need to read remaining_finite elements
                    // from the cycle.
                    finite = header.range ( finite_offset,
                                            Countable.LAST );
                    remaining_finite = end - header.length () + 1L;
                }
            }
            else
            {
                // Start from the requested index, and read the whole
                // thing, because we're going to output the infinite cycle.
                finite = header.range ( finite_offset,
                                        Countable.LAST );
            }

            output.write ( finite);


            final Countable<VALUE> cycle = cyclical.cycle ();
            if ( output.isInfiniteCycleRequired () )
            {
                output.writeInfiniteCycle ( cycle );
            }
            else
            {
                final long cycle_length = cycle.length ();
                for ( long r = 0L;
                      r < remaining_finite;
                      r += cycle_length )
                {
                    // Output the full cycle, even if it's more
                    // than what was requested.
                    output.write ( cycle );
                }
            }

            return finite.length () + remaining_finite;
        }
        else if ( term instanceof Countable )
        {
            final Countable<VALUE> countable = (Countable<VALUE>) term;

            final Countable<VALUE> finite;
            if ( finite_offset == 0L )
            {
                finite = countable;
            }
            else if ( ! output.isInfiniteCycleRequired () )
            {
                final long end =
                    finite_offset + minimum_finite_elements - 1L;
                finite = countable.range ( finite_offset,
                                           end );
            }
            else
            {
                finite = countable.range ( finite_offset,
                                           Countable.LAST );
            }

            output.write ( finite);

            return finite.length ();
        }
        else
        {
            // Can't represent this Term in the lists, so return it as-is.
            output.write ( term );

            return -1L;
        }
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
}
