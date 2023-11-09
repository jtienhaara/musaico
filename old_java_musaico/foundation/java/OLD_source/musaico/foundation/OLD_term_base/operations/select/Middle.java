package musaico.foundation.operations.select;

import java.io.Serializable;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
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
 * Outputs only the middle (N) or (N+1) element(s) of the input term.
 * Outputs non-Just Terms (such as Errors) as-is, without attempting
 * to process them at all.
 * </p>
 *
 * <p>
 * If the input Term has an odd number of elements,
 * and if <code> num_elements </code> is also odd,
 * then <code> num_elements </code> output elements will be generated.
 * </p>
 *
 * <p>
 * If the input Term has an odd number of elements,
 * and if <code> num_elements </code> is even,
 * then <code> num_elements + 1L </code> output elements will be generated.
 * </p>
 *
 * <p>
 * If the input Term has an even number of elements,
 * and if <code> num_elements </code> is also even,
 * then <code> num_elements </code> output elements will be generated.
 * </p>
 *
 * <p>
 * If the input Term has an even number of elements,
 * and if <code> num_elements </code> is odd,
 * then <code> num_elements + 1L </code> output elements will be generated.
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
public class Middle<VALUE extends Object>
    extends AbstractOperation<VALUE, VALUE>
    implements ElementalOperation<VALUE, VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( Middle.class );


    // Enforces parameter obligations and so on for us.
    private final Advocate contracts;

    // The number of elements to output from the middle of the input.
    // @see musaico.foundation.term.Countable.FIRST
    // @see musaico.foundation.term.Countable.LAST
    // @see musaico.foundation.term.Countable.NONE
    // @see musaico.foundation.term.Countable.AFTER_LAST
    private final long numElements;


    /**
     * <p>
     * Creates a new Middle operation.
     * </p>
     *
     * @param type The Type of Terms accepted as inputs to, and returned
     *             as outputs from, this Operation, such as
     *             a <code> Type<Integer> </code> or
     *             a Type<String> and so on.  Must not be null.
     *
     * @param num_elements The number of elements to select from the end
     *                     of input.  Must be greater than or equal to 1L.
     */
    public Middle (
                   Type<VALUE> type,
                   long num_elements
                   )
        throws Parameter1.MustBeGreaterThanOrEqualToOne.Violation
    {
        super ( type,
                type );

        classContracts.check ( Parameter1.MustBeGreaterThanOrEqualToOne.CONTRACT,
                               num_elements );

        this.numElements = num_elements;

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

        final Middle<?> that = (Middle<?>) object;

        if ( this.numElements != that.numElements )
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
        return (int) this.numElements;
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


        if ( ! ( input instanceof Countable ) ) // e.g. Infinite
        {
            // No way of figuring out the middle if we can't count it.
            return input.head ( 0L ); // No middle Term.
        }

        final Countable<VALUE> countable = (Countable<VALUE>) input;

        if ( num_elements == 0L )
        {
            return input.head ( 0L ); // No elements.
        }

        final long length = countable.length ();

        final long extra;
        if ( ( length % 2 ) == ( this.numElements % 2 ) )
        {
            // Odd+odd or even+even.
            extra = 0L;
        }
        else
        {
            // Odd+even or even+odd.
            extra = 1L;
        }

        final long middle_index = length / 2L;
        long start = middle_index - ( this.numElements / 2L );
        long end = start + this.numElements + extra;

        if ( start < 0L )
        {
            start = 0L;
        }

        if ( end >= length )
        {
            end = length - 1L;
        }

        return countable.range ( start,
                                 end );
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return ClassName.of ( this.getClass () )
            + "(" + this.numElements + ")";
    }
}
