package musaico.foundation.operations.filter;

import java.io.Serializable;

import java.util.LinkedHashSet;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.operations.AbstractOperation;

import musaico.foundation.term.Countable;
import musaico.foundation.term.ElementalOperation;
import musaico.foundation.term.Just;
import musaico.foundation.term.NonBlocking;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;
import musaico.foundation.term.Type;

import musaico.foundation.term.abnormal.Error;

import musaico.foundation.term.contracts.TermMustBeCountable;
import musaico.foundation.term.contracts.TermMustMeetAtLeastOneContract;

import musaico.foundation.term.finite.Many;
import musaico.foundation.term.finite.No;
import musaico.foundation.term.finite.One;

import musaico.foundation.term.infinite.Cyclical;
import musaico.foundation.term.infinite.TermMustBeCyclical;


/**
 * <p>
 * Outputs only one instance of each input element, even
 * if duplicates appear in the input.
 * Outputs non-Just Terms (such as Errors) as-is, without attempting
 * to evaluate them at all.
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
 * @see musaico.foundation.operations.filter.MODULE#COPYRIGHT
 * @see musaico.foundation.operations.filter.MODULE#LICENSE
 */
public class Unique<VALUE extends Object>
    extends AbstractOperation<VALUE, VALUE>
    implements ElementalOperation<VALUE, VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( Unique.class );


    // Enforces parameter obligations and so on for us.
    private final Advocate contracts;


    /**
     * <p>
     * Creates a new Unique operation.
     * </p>
     *
     * @param type The Type of Terms accepted as inputs to, and returned
     *             as outputs from, this Operation, such as
     *             a <code> Type<Integer> </code> or
     *             a Type<String> and so on.  Must not be null.
     */
    public Unique (
                   Type<VALUE> type
                   )
    {
        super ( type,   // input_type
                type ); // output_type

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

        // Everything is all matchy-matchy.
        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return 1;
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
    @SuppressWarnings("unchecked") // TermMustMeetAtLeastOneContract constr.
    public final Term<VALUE> apply (
                                    Term<VALUE> input,
                                    long num_requested_elements
                                    )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustBeGreaterThanOrEqualToZero.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               input );
        this.contracts.check ( Parameter2.MustBeGreaterThanOrEqualToZero.CONTRACT,
                               num_requested_elements );

        if ( ! ( input instanceof Just ) // e.g. Error
             || ! input.hasValue () ) // e.g. No
        {
            return input;
        }
        else if ( num_requested_elements == 0L )
        {
            // No elements are needed by the next Operation
            // in the chain anyway, so return No Term.
            return new No<VALUE> ( this.outputType (),
                                   input );
        }

        final boolean is_cyclical;
        final Iterable<VALUE> not_unique_elements;
        if ( input instanceof Cyclical )
        {
            final Cyclical<VALUE> cyclical = (Cyclical<VALUE>) input;
            not_unique_elements = cyclical.cycle ();
            is_cyclical = true;
        }
        else if ( ( input instanceof Countable )
                  || num_requested_elements <= (long) Integer.MAX_VALUE )
        {
            not_unique_elements = input;
            is_cyclical = false;
        }
        else
        {
            // Don't know what to do with this Term.
            final Error<VALUE> error =
                new Error<VALUE> ( this.outputType (),
                                   input,
                                   new TermMustMeetAtLeastOneContract (
                                           TermMustBeCountable.CONTRACT,
                                           TermMustBeCyclical.CONTRACT )
                                       .violation ( this,
                                                    input ) );
            return error;
        }

        final LinkedHashSet<VALUE> unique_elements =
            new LinkedHashSet<VALUE> ();
        long num_unique_elements = 0L;
        for ( VALUE element : not_unique_elements )
        {
            if ( ! unique_elements.contains ( element ) )
            {
                unique_elements.add ( element );

                num_unique_elements ++;
                if ( num_unique_elements >= num_requested_elements )
                {
                    // No point in continuing, since the next Operation
                    // in the chain isn't going to look at any more elements
                    // anyway.
                    break;
                }
            }
        }

        final Term<VALUE> unique;
        if ( num_unique_elements == 0L )
        {
            // No elements at all.
            unique =
                new No<VALUE> ( this.outputType (),
                                input );
        }
        else if ( num_requested_elements <= 1L )
        {
            // Return exactly One element to satisfy the caller's
            // minimum requirement.
            unique =
                new One<VALUE> ( this.outputType (),
                                 input,
                                 unique_elements.iterator ()
                                                .next () );
        }
        else if ( is_cyclical
                  && num_requested_elements > num_unique_elements )
        {
            // The input is infinitely Cyclical repeating elements, and
            // the caller requested more elements than one single cycle.
            unique =
                new Cyclical<VALUE> ( this.outputType (),
                                      input,
                                      unique_elements );
        }
        else
        {
            // We have a finite Countable number of elements.
            unique =
                new Many<VALUE> ( this.outputType (),
                                  input,
                                  unique_elements );
        }

        return unique;
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
