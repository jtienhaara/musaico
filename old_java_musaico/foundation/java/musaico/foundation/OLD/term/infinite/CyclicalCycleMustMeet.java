package musaico.foundation.term.infinite;

import java.io.Serializable;

import java.util.Collection;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Contract;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;


/**
 * <p>
 * A guarantee that every Cyclical Term's <code> cycle () </code>
 * meets a specific Term contract.
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
 * @see musaico.foundation.term.infinite.MODULE#COPYRIGHT
 * @see musaico.foundation.term.infinite.MODULE#LICENSE
 */
public class CyclicalCycleMustMeet
    implements Contract<Term<?>, CyclicalCycleMustMeet.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final Advocate classContracts =
        new Advocate ( CyclicalCycleMustMeet.class );


    // The child Contract used to check that each Cyclical cycle
    // meets this Contract.
    private final Contract<Term<?>, ?> childContract;


    /**
     * <p>
     * Creates a new CyclicalCycleMustMeet.
     * </p>
     *
     * @param child_contract The contract which must be met by each
     *                       Cyclical term's cycle () term.
     *                       Used to check that each Term meets this
     *                       Contract, during calls to
     *                       <code> filter ( Term<?> ) </code>.
     *                       Must not be null.
     */
    public CyclicalCycleMustMeet (
        Contract<Term<?>, ?> child_contract
        )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               child_contract );

        this.childContract = child_contract;
    }


    /**
     * @see musaico.foundation.contract.Contract#description()
     */
    @Override
    public final String description ()
    {
        return "The cycle of each Cyclical term must meet: "
            + this.childContract;
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

        final CyclicalCycleMustMeet that = (CyclicalCycleMustMeet) obj;
        if ( this.childContract == null )
        {
            if ( that.childContract != null )
            {
                return false;
            }
        }
        else if ( that.childContract == null )
        {
            return false;
        }
        else if ( ! this.childContract.equals ( that.childContract ) )
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
        else if ( ! ( term instanceof Cyclical ) )
        {
            return FilterState.DISCARDED;
        }

        final Cyclical<?> cyclical = (Cyclical<?>) term;
        final Countable<?> cycle = cyclical.cycle ();
        FilterState filter_state =
            this.childContract.filter ( cycle );

        return filter_state;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return 13 * ClassName.of ( this.getClass () ).hashCode ()
            + ( this.childContract == null
                    ? 0
                    : this.childContract.hashCode () );
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return ClassName.of ( this.getClass () )
            + " ( " + this.childContract + " )";
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     */
    @Override
    public CyclicalCycleMustMeet.Violation violation (
            Object plaintiff,
            Term<?> evidence
            )
    {
        return new CyclicalCycleMustMeet.Violation (
                       this,
                       plaintiff,
                       evidence );
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object, java.lang.Throwable)
     */
    @Override
    public CyclicalCycleMustMeet.Violation violation (
            Object plaintiff,
            Term<?> evidence,
            Throwable cause
            )
    {
        final CyclicalCycleMustMeet.Violation violation =
            this.violation ( plaintiff,
                             evidence,
                             cause );

        return violation;
    }




    /**
     * <p>
     * A violation of the CyclicalCycleMustMeet contract.
     * </p>
     */
    public static class Violation
        extends TermViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            CyclicalCycleMustMeet.serialVersionUID;


        /**
         * <p>
         * Creates a new CyclicalCycleMustMeet.Violation.
         * </p>
         *
         * @param contract The contract which was violated.
         *                 Must not be null.
         *
         * @param description A human-readable, non-internationalized
         *                    explanation of why the contract was violated.
         *                    Used by developers, testers and maintainers
         *                    to troubleshoot and debug exceptions and errors.
         *                    Must not be null.
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
                          Contract<?, ?> contract,
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
         * Creates a new CyclicalCycleMustMeet.Violation.
         * </p>
         *
         * @param contract The contract which was violated.
         *                 Must not be null.
         *
         * @param description A human-readable, non-internationalized
         *                    explanation of why the contract was violated.
         *                    Used by developers, testers and maintainers
         *                    to troubleshoot and debug exceptions and errors.
         *                    Must not be null.
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
                          Contract<?, ?> contract,
                          Object plaintiff,
                          Term<?> evidence,
                          Throwable cause
                          )
            throws ParametersMustNotBeNull.Violation
        {
            super ( contract,
                    "The term did not meet"
                    + " the Cyclical cycle Contract.", // description
                    plaintiff,
                    evidence,
                    cause );
        }
    }
}
