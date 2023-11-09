package musaico.foundation.operations.filter;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter2;
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

import musaico.foundation.term.contracts.ElementsMustMatchFilters;
import musaico.foundation.term.contracts.TermMustBeCountable;
import musaico.foundation.term.contracts.TermMustMeetAtLeastOneContract;

import musaico.foundation.term.finite.Many;
import musaico.foundation.term.finite.No;
import musaico.foundation.term.finite.One;

import musaico.foundation.term.infinite.Cyclical;
import musaico.foundation.term.infinite.TermMustBeCyclical;


/**
 * <p>
 * Returns the subset of elements of each input Term which match
 * a specific set of Filters.
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
public class FilterBy<VALUE extends Object>
    extends AbstractOperation<VALUE, VALUE>
    implements ElementalOperation<VALUE, VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( FilterBy.class );


    // Enforces parameter obligations and so on for us.
    private final Advocate contracts;

    // The Filters to filter by.
    private final Filter<VALUE> [] filters;


    /**
     * <p>
     * Creates a new FilterBy operation.
     * </p>
     *
     * @param type The Type of Terms accepted as inputs to, and returned
     *             as outputs from, this Operation, such as
     *             a <code> Type<Integer> </code> or
     *             a Type<String> and so on.  Must not be null.
     *
     * @param filters Zero or more Filters to be applied.  Can be empty.
     *                Must not be null.  Must not contain any null elements.
     */
    @SuppressWarnings({"rawtypes", "unchecked"}) // Generic array creation.
    public FilterBy (
                     Type<VALUE> type,
                     Filter<VALUE> ... filters
                     )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        super ( type,
                type );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               (Object) filters );
        classContracts.check ( Parameter2.MustContainNoNulls.CONTRACT,
                               filters );

        this.filters = (Filter<VALUE> [])
            new Filter [ filters.length ];
        System.arraycopy ( filters, 0,
                           this.filters, 0, filters.length );

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

        final FilterBy<?> that = (FilterBy<?>) object;

        if ( this.filters == null )
        {
            if ( that.filters != null )
            {
                return false;
            }
        }
        else if ( that.filters == null )
        {
            return false;
        }
        else if ( this.filters.length != that.filters.length )
        {
            return false;
        }

        for ( int f = 0; f < this.filters.length; f ++ )
        {
            if ( this.filters [ f ] == null )
            {
                if ( that.filters [ f ] != null )
                {
                    return false;
                }
            }
            else if ( that.filters [ f ] == null )
            {
                return false;
            }
            else if ( ! this.filters [ f ].equals ( that.filters [ f ] ) )
            {
                return false;
            }
        }

        // Everything is all matchy-matchy.
        return true;
    }


    /**
     * @return The Filters by which terms are filtered with this operation.
     *         Never null.  Never contains any null elements.
     */
    @SuppressWarnings({"rawtypes", "unchecked"}) // Generic array creation.
    public Filter<VALUE> [] filters ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        final Filter<VALUE> [] filters =
            (Filter<VALUE> []) new Filter [ this.filters.length ];
        System.arraycopy ( this.filters, 0,
                           filters, 0, this.filters.length );

        return filters;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return this.inputType ().hashCode ()
            + 37 * ( this.filters == null
                         ? 0
                         : this.filters.length );
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
        final Iterable<VALUE> unfiltered_elements;
        if ( input instanceof Cyclical )
        {
            final Cyclical<VALUE> cyclical = (Cyclical<VALUE>) input;
            unfiltered_elements = cyclical.cycle ();
            is_cyclical = true;
        }
        else if ( ( input instanceof Countable )
                  || num_requested_elements <= (long) Integer.MAX_VALUE )
        {
            unfiltered_elements = input;
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

        final List<VALUE> filtered_elements =
            new ArrayList<VALUE> ();
        long num_filtered_elements = 0L;
        for ( VALUE element : unfiltered_elements )
        {
            boolean is_kept = true;
            for ( Filter<VALUE> filter : this.filters )
            {
                is_kept = filter.filter ( element ).isKept ();
                if ( ! is_kept )
                {
                    break;
                }
            }

            if ( ! is_kept )
            {
                continue;
            }

            filtered_elements.add ( element );
            num_filtered_elements ++;
            if ( num_filtered_elements >= num_requested_elements )
            {
                break;
            }
        }

        final Term<VALUE> filtered;
        if ( num_filtered_elements == 0L )
        {
            // No elements matched the filter.
            filtered =
                new No<VALUE> ( this.outputType (),
                                input,
                                new ElementsMustMatchFilters<VALUE> (
                                        this.filters )
                                    .violation ( this,
                                                 input ) );
        }
        else if ( num_requested_elements <= 1L )
        {
            // Return exactly One element to satisfy the caller's
            // minimum requirement.
            filtered =
                new One<VALUE> ( this.outputType (),
                                 input,
                                 filtered_elements.get ( 0 ) );
        }
        else if ( is_cyclical
                  && num_requested_elements > num_filtered_elements )
        {
            // The input is infinitely Cyclical repeating elements, and
            // the caller requested more elements than one single cycle.
            filtered =
                new Cyclical<VALUE> ( this.outputType (),
                                      input,
                                      filtered_elements );
        }
        else
        {
            // We have either a finite Countable number of elements
            // matched by the filter, or we started with an infinite
            // Cyclical pattern of elements, but the caller only
            // requested a number of elements less than or equal to
            // the length of one single cycle.
            // Either way, return finitely Many elements.
            filtered =
                new Many<VALUE> ( this.outputType (),
                                  input,
                                  filtered_elements );
        }

        return filtered;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        final StringBuilder filters_buf = new StringBuilder ();
        boolean is_first = true;
        for ( Filter<VALUE> filter : this.filters )
        {
            if ( is_first )
            {
                is_first = false;
            }
            else
            {
                filters_buf.append ( ", " );
            }

            filters_buf.append ( "" + filter );
        }

        return ClassName.of ( this.getClass () )
            + " { "
            + filters_buf.toString ()
            + " }";
    }
}
