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
 * Returns only the element at a specific index of the input, or
 * No element at all if the index is outside the bounds of the input.
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
public class At<VALUE extends Object>
    extends AbstractOperation<VALUE, VALUE>
    implements ElementalOperation<VALUE, VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( At.class );


    // Enforces parameter obligations and so on for us.
    private final Advocate contracts;

    // The index at which to retrieve the element.
    // @see musaico.foundation.term.Countable.FIRST
    // @see musaico.foundation.term.Countable.LAST
    // @see musaico.foundation.term.Countable.NONE
    // @see musaico.foundation.term.Countable.AFTER_LAST
    private final long index;


    /**
     * <p>
     * Creates a new At operation.
     * </p>
     *
     * @param type The Type of Terms accepted as inputs to, and returned
     *             as outputs from, this Operation, such as
     *             a <code> Type<Integer> </code> or
     *             a Type<String> and so on.  Must not be null.
     *
     * @param index The index of the element to output.
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
     */
    public At (
               Type<VALUE> type,
               long index
               )
    {
        super ( type,
                type );

        this.index = index;

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

        final At<?> that = (At<?>) object;

        if ( this.index != that.index )
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
        return (int) this.index;
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
            if ( this.index < 0L )
            {
                // No way of figuring out indices offset from the end
                // if the Term is not even Countable.
                return input.head ( 0L ); // No Term.
            }

            // The index is an offset from the start of the Term.
            // Get the first N elements as a Countable, so that we
            // can use Countable's methods to do the rest of the work.
            countable = input.head ( this.index + 1L );
        }

        return countable.at ( this.index );
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        final String index_string;
        if ( this.index >= 0L )
        {
            index_string = "" + this.index;
        }
        else if ( this.index == Countable.NONE )
        {
            index_string = "NONE";
        }
        else if ( this.index == Countable.AFTER_LAST )
        {
            index_string = "AFTER_LAST";
        }
        else
        {
            final long from_last = this.index - Countable.LAST;
            index_string = "end-" + from_last;
        }

        return ClassName.of ( this.getClass () )
            + "(" + index_string + ")";
    }
}
