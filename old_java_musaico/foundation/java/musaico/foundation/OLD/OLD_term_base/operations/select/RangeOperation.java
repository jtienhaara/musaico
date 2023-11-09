package musaico.foundation.operations.select;

import java.io.Serializable;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.term.Countable;
import musaico.foundation.term.ElementalOperation;
import musaico.foundation.term.Just;
import musaico.foundation.term.NonBlocking;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;
import musaico.foundation.term.Type;

import musaico.foundation.operations.AbstractOperation;


/**
 * <p>
 * Returns only the elements in a specific range of indices from the input,
 * No element at all if the start index is outside the bounds of the input,
 * or No element at all if the end index is Countable.NONE or
 * Countable.AFTER_LAST.
 * Outputs non-Just Terms (such as Errors) as-is, without attempting
 * to process them at all.
 * </p>
 *
 *
 * <p>
 * In Java every Operation must be Serializable in order to
 * play nicely across RMI.
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
 * @see musaico.foundation.operations.select.MODULE#COPYRIGHT
 * @see musaico.foundation.operations.select.MODULE#LICENSE
 */
public class RangeOperation<VALUE extends Object>
    extends AbstractOperation<VALUE, VALUE>
    implements ElementalOperation<VALUE, VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( RangeOperation.class );


    // Enforces parameter obligations and so on for us.
    private final Advocate contracts;

    // The start index from which to retrieve the element(s) in range.
    // @see musaico.foundation.term.Countable.FIRST
    // @see musaico.foundation.term.Countable.LAST
    // @see musaico.foundation.term.Countable.NONE
    // @see musaico.foundation.term.Countable.AFTER_LAST
    private final long start;

    // The end index of the range of element(s) to retrieve.
    // @see musaico.foundation.term.Countable.FIRST
    // @see musaico.foundation.term.Countable.LAST
    // @see musaico.foundation.term.Countable.NONE
    // @see musaico.foundation.term.Countable.AFTER_LAST
    private final long end;


    /**
     * <p>
     * Creates a new RangeOperation.
     * </p>
     *
     * @param type The Type of Terms accepted as inputs to, and returned
     *             as outputs from, this Operation, such as
     *             a <code> Type<Integer> </code> or
     *             a Type<String> and so on.  Must not be null.
     *
     * @param start The index of the first element to output.
     *              0L is the index of the first element, 1L the index
     *              of the second element, and so on.
     *              Negative indices are treated as offsets from the end
     *              of the output Term, where Countable.AFTER_LAST
     *              is after the end (No element), Countable.LAST
     *              is the last element, Countable.BACKWARD + 1L
     *              is the (last - 1)th element, and so on.
     *              If the input does not contain enough elements,
     *              then No output will be generated.
     *              Can be any number.
     *
     * @param end The index of the last element to output.
     *            The end index is interpreted using the same rules
     *            by which the start index is interpreted.
     *            If the end index is the same as the start index, then
     *            at most One element will be output.  If the end
     *            index is later than the start element, then the
     *            output elements will be in the same order as the
     *            input elements.  If the end index is earlier than
     *            the start index, then the output elements will be
     *            in reverse order from the input elements.
     *            Can be any number.
     */
    public RangeOperation (
                           Type<VALUE> type,
                           long start,
                           long end
                           )
    {
        super ( type,
                type );

        this.start = start;
        this.end = end;

        this.contracts = new Advocate ( this );
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
                                 Object object
                                 )
    {
        if ( object == this )
        {
            return true;
        }
        else if ( object == null )
        {
            return false;
        }
        else if ( this.getClass () != object.getClass () )
        {
            return false;
        }

        final RangeOperation<?> that = (RangeOperation<?>) object;

        if ( this.start != that.start )
        {
            return false;
        }
        else if ( this.end != that.end )
        {
            return false;
        }

        // Everything is all matchy-matchy.
        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return (int) ( this.start + this.end );
    }


    /**
     * @see musaico.foundation.term.Operation#apply(musaico.foundation.term.Term)
     */
    public final Term<VALUE> apply (
                                    Term<VALUE> input
                                    )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return this.apply ( input,
                            Long.MAX_VALUE );
    }


    /**
     * @see musaico.foundation.term.ElementalOperation#apply(musaico.foundation.term.Term, long)
     */
    @Override
    public final Term<VALUE> apply (
                                    Term<VALUE> input,
                                    long num_elements
                                    )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustBeGreaterThanOrEqualToZero.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               input );
        this.contracts.check ( Parameter2.MustBeGreaterThanOrEqualToZero.CONTRACT,
                               num_elements );

        if ( ! ( input instanceof Just ) // e.g. Error
             || ! input.hasValue () ) // e.g. No
        {
            return input;
        }

        if ( num_elements == 0L )
        {
            // No elements are needed by the next Operation
            // in the chain anyway, so return No Term.
            return input.head ( 0L ); // No range Term.
        }

        final Countable<VALUE> countable;
        if ( input instanceof Countable )
        {
            countable = (Countable<VALUE>) input;
        }
        else // e.g. Infinite
        {
            if ( this.start < 0L
                 || this.end < 0L )
            {
                // No way of figuring out indices offset from the end
                // if the Term is not even Countable.
                return input.head ( 0L ); // No Term.
            }

            // The start and end indices are both offset from the
            // start of the Term.
            // Get the first N elements as a Countable, so that we
            // can use Countable's methods to do the rest of the work.
            final long head_elements;
            if ( this.end >= this.start )
            {
                final long maybe_head_elements = this.end + 1L;
                if ( maybe_head_elements >= num_elements )
                {
                    head_elements = maybe_head_elements;
                }
                else
                {
                    // The next Operation in the chain doesn't
                    // need the entire range anyway AND the start
                    // index comes first, so we can start counting
                    // only num_elements (instead of the full length
                    // of the range start - end) from the start index.
                    head_elements = num_elements;
                }
            }
            else
            {
                head_elements = this.start + 1L;
            }
            countable = input.head ( head_elements );
        }

        return countable.range ( this.start,
                                 this.end );
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        final String start_string;
        if ( this.start >= 0L )
        {
            start_string = "" + this.start;
        }
        else if ( this.start == Countable.NONE )
        {
            start_string = "NONE";
        }
        else if ( this.start == Countable.AFTER_LAST )
        {
            start_string = "AFTER_LAST";
        }
        else
        {
            final long from_last = this.start - Countable.LAST;
            start_string = "end-" + from_last;
        }

        final String end_string;
        if ( this.end >= 0L )
        {
            end_string = "" + this.end;
        }
        else if ( this.end == Countable.NONE )
        {
            end_string = "NONE";
        }
        else if ( this.end == Countable.AFTER_LAST )
        {
            end_string = "AFTER_LAST";
        }
        else
        {
            final long from_last = this.end - Countable.LAST;
            end_string = "end-" + from_last;
        }

        return ClassName.of ( this.getClass () )
            + "(" + start_string + ".." + end_string + ")";
    }
}
