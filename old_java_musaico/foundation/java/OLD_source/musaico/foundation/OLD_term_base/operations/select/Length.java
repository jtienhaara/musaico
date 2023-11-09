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
 * <p>
 * Returns every input which
 * contains exactly the specified number of elements, or No output
 * for any input which does not have exactly that many elements.
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
public class Length<VALUE extends Object>
    extends AbstractOperation<VALUE, VALUE>
    implements ElementalOperation<VALUE, VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( Length.class );


    // Enforces parameter obligations and so on for us.
    private final Advocate contracts;

    // The minimum length of each input.
    private final long minimum;

    // The maximum length of each input.
    private final long maximum;


    /**
     * <p>
     * Creates a new Length operation.
     * </p>
     *
     * @param type The Type of Terms accepted as inputs to, and returned
     *             as outputs from, this Operation, such as
     *             a <code> Type<Integer> </code> or
     *             a Type<String> and so on.  Must not be null.
     *
     * @param minimum The minimum length of each input.
     *                If an input is not Countable, or if its
     *                length is less than the specified number of elements,
     *                then No Term shall be output.
     *                Must be greater than or equal to 0L.
     *
     * @param maximum The maximum length of each input.
     *                If an input is not Countable, or if its
     *                length is greater than the specified number of elements,
     *                then No Term shall be output.
     *                Must be greater than or equal to the minimum parameter.
     */
    public Length (
                   Type<VALUE> type,
                   long minimum,
                   long maximum
                   )
        throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
               Parameter2.MustBeGreaterThanOrEqualTo.Violation
    {
        super ( type,
                type );

        classContracts.check ( Parameter1.MustBeGreaterThanOrEqualToZero.CONTRACT,
                               minimum );
        classContracts.check ( new Parameter2.MustBeGreaterThanOrEqualTo ( minimum ),
                               maximum );

        this.minimum = minimum;
        this.maximum = maximum;

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

        final Length<?> that = (Length<?>) object;

        if ( this.minimum != that.minimum )
        {
            return false;
        }
        else if ( this.maximum != that.maximum )
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
        return (int) ( this.minimum + this.maximum );
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
            // EITHER the first N inputs are the entire Term and
            // fit within the (min,max) bounds OR the head of the input
            // term will contain TOO FEW elements
            // OR the head of the input term will contain
            // TOO MANY elements (max + 1).
            countable = input.head ( this.maximum + 1L );
        }

        final long length = countable.length ();
        if ( length < this.minimum )
        {
            // Not enough elements.
            return input.head ( 0L ); // No Term.
        }
        else if ( length > this.maximum )
        {
            // Too many elements.
            return input.head ( 0L ); // No Term.
        }

        return input;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        final String length_string;
        if ( this.minimum == 0L )
        {
            length_string = "<= " + this.maximum;
        }
        else if ( this.minimum == this.maximum )
        {
            length_string = "== " + this.maximum;
        }
        else
        {
            length_string = "(" + this.minimum + ".." + this.maximum;
        }

        return ClassName.of ( this.getClass () )
            + length_string;
    }
}
