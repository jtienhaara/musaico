package musaico.foundation.term.contracts;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Contract;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Domain;
import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;


/**
 * <p>
 * A general-purpose Contract for Terms: each inspected element
 * of each Term must NOT belong to a specific Domain, or a TermViolation
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
public class TermMustNotBelongToDomain
    implements Contract<Term<?>, TermMustNotBelongToDomain.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces constructor and static method parameter obligations for us.
    private static final Advocate classContracts =
        new Advocate ( TermMustNotBelongToDomain.class );


    // The Domain to which every Term must belong.
    private final Domain<Term<?>> domain;


    /**
     * <p>
     * Creates a new TermMustNotBelongToDomain contract.
     * </p>
     *
     * @param domain The domain to which every inspected Term must belong.
     *               Must not be null.
     */
    public TermMustNotBelongToDomain (
                                       Domain<Term<?>> domain
                                       )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               domain );

        this.domain = domain;
    }


    /**
     * @see musaico.foundation.contract.Contract#description()
     */
    @Override
    public final String description ()
    {
        return "Each element of each term"
            + " must NOT belong to the domain of objects "
            + this.domain
            + ".";
    }


    /**
     * @return The Domain to which every Term must NOT belong.
     *         Never null.
     */
    public final Domain<Term<?>> domain ()
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

        final TermMustNotBelongToDomain that =
            (TermMustNotBelongToDomain) obj;

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
    public final FilterState filter (
                                     Term<?> term
                                     )
    {
        if ( term == null )
        {
            return FilterState.DISCARDED;
        }

        final FilterState result = this.domain.filter ( term )
            .opposite ();
        return result;
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
     */
    @Override
    public final String toString ()
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
    public final TermMustNotBelongToDomain.Violation violation (
            Object plaintiff,
            Term<?> evidence
            )
    {
        return new TermMustNotBelongToDomain.Violation ( this,
                                                          plaintiff,
                                                          evidence );
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object, java.lang.Throwable)
     */
    @Override
    public final TermMustNotBelongToDomain.Violation violation (
            Object plaintiff,
            Term<?> evidence,
            Throwable cause
            )
    {
        final TermMustNotBelongToDomain.Violation violation =
            this.violation ( plaintiff,
                             evidence,
                             cause );

        return violation;
    }




    /**
     * <p>
     * A violation of the TermMustNotBelongToDomain contract.
     * </p>
     */
    public static class Violation
        extends TermViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            TermMustNotBelongToDomain.serialVersionUID;


        /**
         * <p>
         * Creates a new TermMustNotBelongToDomain.Violation.
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
                          TermMustNotBelongToDomain contract,
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
         * Creates a new TermMustNotBelongToDomain.Violation.
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
                          TermMustNotBelongToDomain contract,
                          Object plaintiff,
                          Term<?> evidence,
                          Throwable cause
                          )
            throws ParametersMustNotBeNull.Violation
        {
            super ( contract,
                    "The term belongs to the domain: "
                    + evidence
                    + ".", // description
                    plaintiff,
                    evidence,
                    cause );
        }
    }
}
