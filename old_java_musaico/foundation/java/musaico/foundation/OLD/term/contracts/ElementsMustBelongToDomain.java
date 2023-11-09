package musaico.foundation.term.contracts;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Contract;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Domain;
import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;


/**
 * <p>
 * A general-purpose Contract for Term values: each inspected element
 * of each Term must belong to a specific Domain, or a TermViolation
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
public class ElementsMustBelongToDomain<DOMAIN extends Object>
    implements Contract<Term<?>, ElementsMustBelongToDomain.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces constructor and static method parameter obligations for us.
    private static final Advocate classContracts =
        new Advocate ( ElementsMustBelongToDomain.class );


    // The Domain to which every Term must belong.
    private final Domain<DOMAIN> domain;


    /**
     * <p>
     * Creates a new ElementsMustBelongToDomain contract.
     * </p>
     *
     * @param domain The domain to which every inspected Term
     *               must belong.  Must not be null.
     */
    public ElementsMustBelongToDomain (
                                       Domain<DOMAIN> domain
                                       )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               domain );

        this.domain = domain;
    }


    /**
     * @see musaico.foundation.contract.Contract#description()
     *
     * Can be overridden.
     */
    @Override
    public String description ()
    {
        return "Each element of each term"
            + " must belong to the domain of objects "
            + this.domain
            + ".";
    }


    /**
     * @return The Domain to which every Term must belong.
     *         Never null.
     */
    public final Domain<DOMAIN> domain ()
    {
        return this.domain;
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

        final ElementsMustBelongToDomain<?> that =
            (ElementsMustBelongToDomain<?>) obj;

        if ( this.domain == null )
        {
            if ( that.domain != null )
            {
                return false;
            }
        }
        else if ( that.domain == null )
        {
            return false;
        }
        else if ( ! this.domain.equals ( that.domain ) )
        {
            return false;
        }

        return true;
    }


    /**
     * @see musaico.filter.Filter#filter(java.lang.Object)
     */
    @Override
    @SuppressWarnings("unchecked") // We cast to DOMAIN inside try...catch.
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
            for ( Object non_domain_object : term )
            {
                final DOMAIN domain_object = (DOMAIN) non_domain_object;
                final FilterState filter_state =
                    this.domain.filter ( domain_object );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
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
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return 31 * ClassName.of ( this.getClass () ).hashCode ()
            + this.domain.hashCode ();
    }


    /**
     * @see java.lang.Object#toString()
     *
     * Can be overridden.
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this.getClass () )
            + " { "
            + this.domain
            + " }";
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     */
    @Override
    public final ElementsMustBelongToDomain.Violation violation (
            Object plaintiff,
            Term<?> evidence
            )
    {
        return new ElementsMustBelongToDomain.Violation ( this,
                                                          plaintiff,
                                                          evidence );
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object, java.lang.Throwable)
     */
    @Override
    public final ElementsMustBelongToDomain.Violation violation (
            Object plaintiff,
            Term<?> evidence,
            Throwable cause
            )
    {
        final ElementsMustBelongToDomain.Violation violation =
            this.violation ( plaintiff,
                             evidence,
                             cause );

        return violation;
    }




    /**
     * <p>
     * A violation of the ElementsMustBelongToDomain contract.
     * </p>
     */
    public static class Violation
        extends TermViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            ElementsMustBelongToDomain.serialVersionUID;


        /**
         * <p>
         * Creates a new ElementsMustBelongToDomain.Violation.
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
                          ElementsMustBelongToDomain<?> contract,
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
         * Creates a new ElementsMustBelongToDomain.Violation.
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
                          ElementsMustBelongToDomain<?> contract,
                          Object plaintiff,
                          Term<?> evidence,
                          Throwable cause
                          )
            throws ParametersMustNotBeNull.Violation
        {
            super ( contract,
                    "The term did not belong to the domain: "
                    + ( contract == null
                        ? evidence
                        : nonMembers ( contract.domain (),
                                       evidence ) )
                    + ".", // description
                    plaintiff,
                    evidence,
                    cause );
        }


        @SuppressWarnings("unchecked") // We cast to DOMAIN inside try...catch.
        private static final <VIOLATED_DOMAIN extends Object>
                String nonMembers (
                                   Domain<VIOLATED_DOMAIN> domain,
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
                for ( Object non_domain_object : term )
                {
                    final VIOLATED_DOMAIN domain_object =
                        (VIOLATED_DOMAIN) non_domain_object;
                    if ( ! domain.filter ( domain_object ).isKept () )
                    {
                        if ( is_first )
                        {
                            is_first = false;
                        }
                        else
                        {
                            sbuf.append ( ", " );
                        }

                        sbuf.append ( "" + domain_object );
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
