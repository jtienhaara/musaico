package musaico.foundation.operations.order;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;

import musaico.foundation.operations.AbstractOperation;

import musaico.foundation.term.Countable;
import musaico.foundation.term.ElementalOperation;
import musaico.foundation.term.Just;
import musaico.foundation.term.NonBlocking;
import musaico.foundation.term.Pipeline;
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
 * !!!
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
 * @see musaico.foundation.operations.order.MODULE#COPYRIGHT
 * @see musaico.foundation.operations.order.MODULE#LICENSE
 */
public class !!!<VALUE extends Object>
    extends AbstractOperation<VALUE, VALUE>
    implements ElementalOperation<VALUE, VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( !!!.class );


    // Enforces parameter obligations and so on for us.
    private final Advocate contracts;


    /**
     * <p>
     * Creates a new !!! operation.
     * </p>
     *
     * @param type The Type of Terms accepted as inputs to, and returned
     *             as outputs from, this Operation, such as
     *             a <code> Type<Integer> </code> or
     *             a Type<String> and so on.  Must not be null.
     */
    @SuppressWarnings({"rawtypes", "unchecked"}) // Generic array creation.
    public !!! (
                     Type<VALUE> type
                     )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        super ( type,
                type );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               !!! );

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

        final !!!<?> that = (!!!<?>) object;

        if ( this.!!! == null )
        {
            if ( that.!!! != null )
            {
                return false;
            }
        }
        else if ( that.!!! == null )
        {
            return false;
        }
        else if ( ! this.!!!.equals ( that.!!! ) )
        {
            return false;
        }

        // Everything is all matchy-matchy.
        return true;
    }


    /**
     * @return !!!
     *         Never null.
     */
    public !!! !!! ()
        throws ReturnNeverNull.Violation
    {
        return this.!!!;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return this.inputType ().hashCode ()
            + 29 * ( this.!!! == null
                         ? 0
                         : this.!!!.length );
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
        final Iterable<VALUE> before_elements;
        if ( input instanceof Cyclical )
        {
            final Cyclical<VALUE> cyclical = (Cyclical<VALUE>) input;
            before_elements = cyclical.cycle ();
            is_cyclical = true;
        }
        else if ( ( input instanceof Countable )
                  || num_requested_elements <= (long) Integer.MAX_VALUE )
        {
            before_elements = input;
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

        final List<VALUE> after_elements =
            new ArrayList<VALUE> ();
        long num_after_elements = 0L;
        for ( VALUE element : before_elements )
        {
            !!!;
            after_elements.add ( element );
            num_after_elements ++;
            if ( num_after_elements >= num_requested_elements )
            {
                break;
            }
        }

        final Term<VALUE> after_term;
        if ( num_after_elements == 0L )
        {
            // No elements !!!.
            after_term =
                new No<VALUE> ( this.outputType (),
                                input,
                                new !!!<VALUE> ( !!! )
                                    .violation ( this,
                                                 input ) );
        }
        else if ( num_requested_elements <= 1L )
        {
            // Return exactly One element to satisfy the caller's
            // minimum requirement.
            after_term =
                new One<VALUE> ( this.outputType (),
                                 input,
                                 after_elements.get ( 0 ) );
        }
        else if ( is_cyclical
                  && num_requested_elements > num_after_elements )
        {
            // The input is infinitely Cyclical repeating elements, and
            // the caller requested more elements than one single cycle.
            after_term =
                new Cyclical<VALUE> ( this.outputType (),
                                      input,
                                      after_elements );
        }
        else
        {
            // We have either a finite Countable number of elements
            // !!!, or we started with an infinite
            // Cyclical pattern of elements, but the caller only
            // requested a number of elements less than or equal to
            // the length of one single cycle.
            // Either way, return finitely Many elements.
            after_term =
                new Many<VALUE> ( this.outputType (),
                                  input,
                                  after_elements );
        }

        return after_term;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return ClassName.of ( this.getClass () )
            + " { "
            + this.!!!
            + " }";
    }
}
