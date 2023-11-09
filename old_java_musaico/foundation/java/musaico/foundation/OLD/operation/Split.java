package musaico.foundation.operation;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;

import musaico.foundation.term.abnormal.Error;

import musaico.foundation.term.countable.No;

import musaico.foundation.term.infinite.Cyclical;

import musaico.foundation.term.multiplicities.OneOrMore;


/**
 * <p>
 * Splits each Countable or Cyclical Term into two: the first (N)
 * Countable elements, followed by the remaining (possibly Countable,
 * possibly Cyclical) elements.
 * </p>
 *
 * <p>
 * Several Pipe implementations use Split to simplify their tasks.
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
public class Split<VALUE>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks method obligations and guarantees.
    private final Advocate contracts;


    /**
     * <p>
     * Creates a new Split.
     * </p>
     */
    public Split ()
    {
        this.contracts = new Advocate ( this );
    }


    /**
     * <p>
     * Splits the specified Term into 2: (0..N - 1), (N..end); then
     * writes out the first half to the specified stream (if any),
     * the second half to the specified stream (if any), errors
     * to the specified stream (if any), and returns either the
     * second half term or an error.
     * </p>
     *
     *
     * @param input The Term to split.  Must be Countable or Cyclical.
     *              Must not be null.
     *
     * @param split_at_index The index at which to start the second half Term.
     *                       Must be 0L or greater.
     *
     * @param first_half_or_null The Stream to which the first half of the
     *                           split Term will be written, or null
     *                           if the first half Term(s) will not be
     *                           written out to any stream.  CAN be null.
     *
     * @param second_half_or_null The Stream to which the second half of the
     *                            split Term will be written, or null
     *                            if the second half Term(s) will not be
     *                            written out to any stream.  CAN be null.
     *
     * @param error_or_null The Stream to which any Error will be written,
     *                      or null if the first half Term(s) will not be
     *                      written out to any stream.  CAN be null.
     */
    public final Term<VALUE> write (
            Term<VALUE> input,
            long split_at_index,
            Stream<VALUE> first_half_or_null,
            Stream<VALUE> second_half_or_null,
            Stream<VALUE> error_or_null
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustBeGreaterThanOrEqualToZero.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               input );
        this.contracts.check ( Parameter2.MustBeGreaterThanOrEqualToZero.CONTRACT,
                               split_at_index );

        final Countable<VALUE> countable_input;
        final OneOrMore<VALUE> input_cycle;
        if ( input instanceof Countable )
        {
            countable_input = (Countable<VALUE>) input;
            input_cycle = null;
        }
        else if ( input instanceof Cyclical )
        {
            final Cyclical<VALUE> cyclical_input = (Cyclical<VALUE>) input;
            countable_input = cyclical_input.header ();
            input_cycle = cyclical_input.cycle ();
        }
        else
        {
            // Error, don't know what this input term is.
            // Can't insert into something we can't understand.
            // The input must be either:
            // 1) Countable; or
            // 2) Cyclical.
            final TermViolation violation =
                Stream.TERM_MUST_BE_COUNTABLE_OR_CYCLICAL.violation (
                    this, // plaintiff
                    input );
            final Error<VALUE> error =
                new Error<VALUE> ( input.type (),
                                   violation );
            if ( error_or_null != null )
            {
                error_or_null.write ( error );
            }

            return error;
        }

        if ( countable_input.length () <= split_at_index )
        {
            // The next insert at index comes after the (countable part of)
            // this input.
            if ( first_half_or_null != null )
            {
                first_half_or_null.write ( countable_input );
            }

            final long num_remaining_to_write =
                split_at_index - countable_input.length ();

            // Is there an infinite cycle?  If so, write out its elements
            // until we hit the next insert at index.
            if ( input_cycle != null )
            {
                final long cycle_length = input_cycle.length ();
                final long full_repetitions =
                    num_remaining_to_write / cycle_length;
                final long partial_length =
                    num_remaining_to_write % cycle_length;

                if ( first_half_or_null != null )
                {
                    for ( long c = 0L; c < full_repetitions; c++ )
                    {
                        first_half_or_null.write ( input_cycle );
                    }
                }

                final Countable<VALUE> partial_header;
                if ( partial_length == 0L )
                {
                    partial_header =
                        new No<VALUE> ( input.type () );
                }
                else
                {
                    if ( first_half_or_null != null )
                    {
                        final Countable<VALUE> partial_cycle_to_write =
                            input_cycle.range ( 0L, partial_length - 1L );
                        first_half_or_null.write ( partial_cycle_to_write );
                    }

                    partial_header = input_cycle.range ( partial_length,
                                                         Countable.LAST );
                }

                final Cyclical<VALUE> after_split =
                    new Cyclical<VALUE> ( input.type (),
                                          partial_header,
                                          input_cycle );
                if ( second_half_or_null != null )
                {
                    second_half_or_null.write ( after_split );
                }

                return after_split;
            }

            // Nothing left of this Term.
            return null;
        }

        if ( first_half_or_null != null )
        {
            final Countable<VALUE> partial_countable_to_write =
                countable_input.range ( 0L, split_at_index - 1L );
            first_half_or_null.write ( partial_countable_to_write );
        }

        final Countable<VALUE> countable_remainder =
            countable_input.range ( split_at_index, Countable.LAST );
        final Term<VALUE> remainder;
        if ( input_cycle == null )
        {
            remainder = countable_remainder;
        }
        else
        {
            remainder = new Cyclical<VALUE> ( input.type (),
                                              countable_remainder,
                                              input_cycle );
        }

        if ( second_half_or_null != null )
        {
            second_half_or_null.write ( remainder );
        }

        return remainder;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return ClassName.of ( this.getClass () );
    }
}
