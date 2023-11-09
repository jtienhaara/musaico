package musaico.foundation.term.contracts;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Contract;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;


/**
 * <p>
 * A general-purpose Contract for Terms: each inspected element
 * of each Term must be KEPT by specific Filter(s), or a TermViolation
 * will be generated.
 * </p>
 *
 *
 * <p>
 * In Java, every Contract must implement equals () and hashCode ().
 * </p>
 *
 * <p>
 * In Java, every Contract must be Serializable in order to play
 * nicely over RMI.
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
 * @see musaico.foundation.term.contracts.MODULE#COPYRIGHT
 * @see musaico.foundation.term.contracts.MODULE#LICENSE
 */
public class ElementsMustMatchFilters<ELEMENT extends Object>
    implements Contract<Term<?>, ElementsMustMatchFilters.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces constructor and static method parameter obligations for us.
    private static final Advocate classContracts =
        new Advocate ( ElementsMustMatchFilters.class );


    // The Filter(s) which must match each element of every Term.
    private final Filter<ELEMENT> [] filters;


    /**
     * <p>
     * Creates a new ElementsMustMatchFilters contract.
     * </p>
     *
     * @param filters The Filter(s) which must all match each element
     *                of every Term.  Must not be null.
     *                Must not contain any null elements.
     */
    @SuppressWarnings({"rawtypes", "unchecked"}) // Generic array creation.
    public ElementsMustMatchFilters (
                                     Filter<ELEMENT> ... filters
                                     )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               (Object) filters );
        classContracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               filters );

        this.filters = (Filter<ELEMENT> [])
            new Filter [ filters.length ];
        System.arraycopy ( filters, 0,
                           this.filters, 0, filters.length );
    }


    /**
     * @see musaico.foundation.contract.Contract#description()
     */
    @Override
    public final String description ()
    {
        final StringBuilder filters_buf = new StringBuilder ();
        boolean is_first = true;
        for ( Filter<ELEMENT> filter : this.filters )
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

        return "Each element of each term"
            + " must be matched by all of the filter(s) { "
            + filters_buf.toString ()
            + " }.";
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
                                 Object obj
                                 )
    {
        if ( obj == this )
        {
            return true;
        }
        else if ( obj == null )
        {
            return false;
        }
        else if ( obj.getClass () != this.getClass () )
        {
            return false;
        }

        final ElementsMustMatchFilters<?> that =
            (ElementsMustMatchFilters<?>) obj;

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

            if ( ! this.filters [ f ].equals ( that.filters [ f ] ) )
            {
                return false;
            }
        }

        return true;
    }


    /**
     * @see musaico.filter.Filter#filter(java.lang.Object)
     */
    @Override
    @SuppressWarnings("unchecked") // We cast to ELEMENT inside try...catch.
    public final FilterState filter (
                                     Term<?> term
                                     )
    {
        if ( term == null )
        {
            return FilterState.DISCARDED;
        }
        else if ( ! ( term instanceof Countable ) )
        {
            // Can't iterate through the term's elements, so fail.
            return FilterState.DISCARDED;
        }

        try
        {
            for ( Object element_object : term )
            {
                final ELEMENT element = (ELEMENT) element_object;
                for ( Filter<ELEMENT> filter : this.filters )
                {
                    final FilterState filter_state =
                        filter.filter ( element );
                    if ( ! filter_state.isKept () )
                    {
                        return filter_state;
                    }
                }
            }
        }
        catch ( ClassCastException e )
        {
            return FilterState.DISCARDED;
        }

        return FilterState.KEPT;
    }


    /**
     * @return The Filter(s) which must match each element
     *         of every Term.  Never null.
     *         Never contains any null elements.
     */
    @SuppressWarnings({"rawtypes", "unchecked"}) // Generic array creation.
    public final Filter<ELEMENT> [] filters ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        final Filter<ELEMENT> [] filters =
            (Filter<ELEMENT> [])
            new Filter [ this.filters.length ];
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
        return 41 * ClassName.of ( this.getClass () ).hashCode ()
            + this.filters.length;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        final StringBuilder filters_buf = new StringBuilder ();
        boolean is_first = true;
        for ( Filter<ELEMENT> filter : this.filters )
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


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     */
    @Override
    public final ElementsMustMatchFilters.Violation violation (
            Object plaintiff,
            Term<?> evidence
            )
    {
        return new ElementsMustMatchFilters.Violation ( this,
                                                        plaintiff,
                                                        evidence );
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object, java.lang.Throwable)
     */
    @Override
    public final ElementsMustMatchFilters.Violation violation (
            Object plaintiff,
            Term<?> evidence,
            Throwable cause
            )
    {
        final ElementsMustMatchFilters.Violation violation =
            this.violation ( plaintiff,
                             evidence,
                             cause );

        return violation;
    }




    /**
     * <p>
     * A violation of the ElementsMustMatchFilters contract.
     * </p>
     */
    public static class Violation
        extends TermViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            ElementsMustMatchFilters.serialVersionUID;


        /**
         * <p>
         * Creates a new ElementsMustMatchFilters.Violation.
         * </p>
         *
         * @param contract The contract which was violated.
         *                 Must not be null.
         *
         * @param plaintiff The object under contract, such as the object
         *                  whose method obligation was violated, or which
         *                  violated its own method guarantee.
         *                  Must not be null.
         *
         * @param evidence The Term which violated the contract.
         *                 Can be null.
         */
        public Violation (
                          ElementsMustMatchFilters<?> contract,
                          Object plaintiff,
                          Term<?> evidence
                          )
            throws ParametersMustNotBeNull.Violation
        {
            this ( contract,
                   plaintiff,
                   evidence,
                   null ); // cause
        }


        /**
         * <p>
         * Creates a new ElementsMustMatchFilters.Violation.
         * </p>
         *
         * @param contract The contract which was violated.
         *                 Must not be null.
         *
         * @param plaintiff The object under contract, such as the object
         *                  whose method obligation was violated, or which
         *                  violated its own method guarantee.
         *                  Must not be null.
         *
         * @param evidence The Term which violated the contract.
         *                 Can be null.
         *
         * @param cause The Throwable which caused this contract violation.
         *              Can be null.
         */
        public Violation (
                          ElementsMustMatchFilters<?> contract,
                          Object plaintiff,
                          Term<?> evidence,
                          Throwable cause
                          )
            throws ParametersMustNotBeNull.Violation
        {
            super ( contract,
                    "The filter did not match all elements of the Term: "
                    + ( contract == null
                        ? evidence
                        : nonMembers ( contract.filters (),
                                       evidence ) )
                    + ".", // description
                    plaintiff,
                    evidence,
                    cause );
        }


        @SuppressWarnings("unchecked") // Cast to ELEMENT inside try...catch.
        private static final <VIOLATED_ELEMENT extends Object>
                String nonMembers (
                                   Filter<VIOLATED_ELEMENT> [] filters,
                                   Term<?> term
                                   )
        {
            if ( term == null )
            {
                return "" + term;
            }

            final StringBuilder sbuf = new StringBuilder ();
            try
            {
                boolean is_first = true;
                for ( Object element_object : term )
                {
                    final VIOLATED_ELEMENT element =
                        (VIOLATED_ELEMENT) element_object;
                    for ( Filter<VIOLATED_ELEMENT> filter : filters )
                    {
                        if ( ! filter.filter ( element ).isKept () )
                        {
                            if ( is_first )
                            {
                                is_first = false;
                            }
                            else
                            {
                                sbuf.append ( ", " );
                            }

                            sbuf.append ( "" + element );
                        }
                    }
                }
            }
            catch ( ClassCastException e )
            {
                sbuf.append ( "" + term );
            }

            return sbuf.toString ();
        }
    }
}
