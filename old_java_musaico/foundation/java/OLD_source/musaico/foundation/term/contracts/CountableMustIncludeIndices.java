package musaico.foundation.term.contracts;

import java.io.Serializable;

import java.util.HashSet;
import java.util.Set;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Contract;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;
import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;

import musaico.foundation.domains.ClassName;

import musaico.foundation.domains.array.ArrayObject;
import musaico.foundation.domains.array.ContainsIndices;

import musaico.foundation.domains.number.GreaterThanOrEqualToZero;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;


/**
 * <p>
 * Each inspected Term must be <code> Countable </code> and must
 * include indexable elements at specific indices.
 * </p>
 *
 * @see musaico.foundation.domains.array.ContainsIndices
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
public class CountableMustIncludeIndices
    implements Contract<Term<?>, CountableMustIncludeIndices.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces constructor and static method parameter obligations for us.
    private static final Advocate classContracts =
        new Advocate ( CountableMustIncludeIndices.class );


    // The ContainsIndices Domain to which ever Term must belong.
    private final ContainsIndices domain;


    /**
     * <p>
     * Creates a new CountableMustIncludeIndices contract.
     * </p>
     *
     * @param indices The indices which must contain indexable elements
     *                in every Countable term in this domain.
     *                Each required index should be greater than or equal to 0,
     *                otherwise the contract is impossible to meet
     *                by any Countable term.
     */
    public CountableMustIncludeIndices (
                                        int ... indices
                                        )
    {
        this (
              new ContainsIndices ( indices )
              );
    }


    /**
     * <p>
     * Creates a new CountableMustIncludeIndices contract.
     * </p>
     *
     * @param indices The indices which must contain indexable elements
     *                in every Countable term in this domain.
     *                Each required index must be greater than or equal to 0.
     */
    public CountableMustIncludeIndices (
                                        long ... indices
                                        )
    {
        this (
              new ContainsIndices ( indices )
              );
    }


    /**
     * <p>
     * Creates a new CountableMustIncludeIndices contract.
     * </p>
     *
     * @param domain The ContainsIndices Domain to which every inspected
     *               Term must belong.  Must not be null.
     */
    public CountableMustIncludeIndices (
                                        ContainsIndices domain
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
        return "The term must Countable, with elements of "
            + this.domain
            + ".";
    }


    /**
     * @return The Domain to which every Term must belong.
     *         Never null.
     */
    public final ContainsIndices domain ()
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

        final CountableMustIncludeIndices that =
            (CountableMustIncludeIndices) obj;

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
    @SuppressWarnings("unchecked") // Cast Term<?> (which is an
        // Iterable<?>) to Iterable<Object>.
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
            return FilterState.DISCARDED;
        }

        final Countable<?> countable = (Countable<?>) term;
        @SuppressWarnings("unchecked") // Cast C<?> - C<Object>.
        final ArrayObject<?> array_object =
            new ArrayObject<Object> (
                Object.class,
                (Countable<Object>) countable );
        return this.domain.filter ( array_object );
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
        return "Term must be "
            + this.domain;
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     */
    @Override
    public CountableMustIncludeIndices.Violation violation (
            Object plaintiff,
            Term<?> evidence
            )
    {
        return new CountableMustIncludeIndices.Violation ( this,
                                                     plaintiff,
                                                     evidence );
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object, java.lang.Throwable)
     */
    @Override
    public CountableMustIncludeIndices.Violation violation (
            Object plaintiff,
            Term<?> evidence,
            Throwable cause
            )
    {
        final CountableMustIncludeIndices.Violation violation =
            this.violation ( plaintiff,
                             evidence,
                             cause );

        return violation;
    }




    /**
     * <p>
     * A violation of the CountableMustIncludeIndices contract.
     * </p>
     */
    public static class Violation
        extends TermViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            CountableMustIncludeIndices.serialVersionUID;


        /**
         * <p>
         * Creates a new CountableMustIncludeIndices.Violation.
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
                          CountableMustIncludeIndices contract,
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
         * Creates a new CountableMustIncludeIndices.Violation.
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
        @SuppressWarnings("unchecked") // Cast Countable<?> - Countable<Object>
        public Violation (
                          CountableMustIncludeIndices contract,
                          Object plaintiff,
                          Term<?> evidence,
                          Throwable cause
                          )
            throws ParametersMustNotBeNull.Violation
        {
            super ( contract,
                    "The term did not belong to the domain: "
                    + ( ( evidence instanceof Countable )
                              ? contract.domain ().nonMember (
                                    new ArrayObject<Object> (
                                        Object.class,
                                        (Countable<Object>)
                                            evidence
                                        )
                                )
                              : evidence )
                    + ".", // description
                    plaintiff,
                    evidence,
                    cause );
        }
    }
}
