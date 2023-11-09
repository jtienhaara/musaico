package musaico.foundation.term.contracts;

import java.io.Serializable;

import java.util.Collection;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;

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
 * A guarantee that every Term meets none of the Contracts in a specific set
 * of child Contracts.
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
public class TermMustNotMeetAnyContract
    implements Contract<Term<?>, TermMustNotMeetAnyContract.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final Advocate classContracts =
        new Advocate ( TermMustNotMeetAnyContract.class );


    // The child Contracts used to check that Terms meet this Contract.
    private final Contract<Term<?>, ?> [] childContracts;


    /**
     * <p>
     * Creates a new TermMustNotMeetAnyContract.
     * </p>
     *
     * @param child_contracts The child contracts which comprise this
     *                        TermMustNotMeetAnyContract composite contract.
     *                        Used to check that each Term meets this
     *                        Contract, during calls to
     *                        <code> filter ( Term<?> ) </code>.
     *                        Must not be null.
     */
    @SuppressWarnings({"rawtypes",   // new Contract [] generic array.
                       "unchecked"}) // Possible heap pollution C<V<?>, ?> ...
    public TermMustNotMeetAnyContract (
            Contract<Term<?>, ?> ... child_contracts
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               (Object []) child_contracts );

        classContracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               child_contracts );

        this.childContracts = (Contract<Term<?>, ?> [])
            new Contract [ child_contracts.length ];

        System.arraycopy ( child_contracts, 0,
                           this.childContracts, 0, child_contracts.length );
    }


    /**
     * <p>
     * Creates a new TermMustNotMeetAnyContract.
     * </p>
     *
     * @param child_contracts The child contracts which comprise this
     *                        TermMustNotMeetAnyContract composite contract.
     *                        Used to check that each Term meets this
     *                        Contract, during calls to
     *                        <code> filter ( Term<?> ) </code>.
     *                        Must not be null.
     */
    @SuppressWarnings({"rawtypes",   // new Contract [] generic array.
                       "unchecked"}) // Cast Contract [] - Contract<V<?>, ?>.
    public TermMustNotMeetAnyContract (
            Collection<Contract<Term<?>, ?>> child_contracts
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               child_contracts );

        classContracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               child_contracts );

        this.childContracts = (Contract<Term<?>, ?> [])
            new Contract [ child_contracts.size () ];

        int c = 0;
        for ( Contract<Term<?>, ?> child_contract : child_contracts )
        {
            this.childContracts [ c ] = child_contract;
            c ++;
        }
    }


    /**
     * <p>
     * Creates a new TermMustNotMeetAnyContract.
     * </p>
     *
     * @param child_contracts The child contracts which comprise this
     *                        TermMustNotMeetAnyContract composite contract.
     *                        Used to check that each Term meets this
     *                        Contract, during calls to
     *                        <code> filter ( Term<?> ) </code>.
     *                        Must not be null.
     */
    @SuppressWarnings({"rawtypes",   // new Contract [] generic array.
                       "unchecked"}) // Cast Contract [] - Contract<V<?>, ?>.
    public TermMustNotMeetAnyContract (
            Countable<Contract<Term<?>, ?>> child_contracts
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               child_contracts );

        this.childContracts = (Contract<Term<?>, ?> [])
            new Contract [ (int) child_contracts.length () ];

        for ( long c = 0; c < child_contracts.length (); c ++ )
        {
            this.childContracts [ (int) c ] =
                child_contracts.at ( c )
                               .orViolation ( Parameter1.MustContainNoNulls.CONTRACT,
                                              this,
                                              child_contracts );
        }
    }


    /**
     * @see musaico.foundation.contract.Contract#description()
     */
    @Override
    public final String description ()
    {
        final StringBuilder sbuf = new StringBuilder ();
        boolean is_first = true;
        for ( Contract<Term<?>, ?> contract : this.childContracts )
        {
            if ( is_first )
            {
                is_first = false;
            }
            else
            {
                sbuf.append ( ", " );
            }

            sbuf.append ( " ( " );
            sbuf.append ( contract.description () );
            sbuf.append ( " )" );
        }

        return "The term must not meet any Contract: "
            + sbuf.toString ();
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

        final TermMustNotMeetAnyContract that =
            (TermMustNotMeetAnyContract) obj;
        for ( int c = 0;
              c < this.childContracts.length || c < that.childContracts.length;
              c ++ )
        {
            if ( this.childContracts.length <= c
                 || that.childContracts.length <= c
                 || ! this.childContracts [ c ].equals ( that.childContracts [ c ] ) )
            {
                // At least one Contract is different.
                return false;
            }
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

        FilterState filter_state = FilterState.DISCARDED;
        for ( Contract<Term<?>, ?> contract : this.childContracts )
        {
            filter_state = contract.filter ( term );
            if ( filter_state.isKept () )
            {
                filter_state = filter_state.opposite ();
                return filter_state;
            }
        }

        filter_state = filter_state.opposite ();
        return filter_state;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return ClassName.of ( this.getClass () ).hashCode ()
            * this.childContracts.length;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return ClassName.of ( this.getClass () )
            + " [ "
            + this.childContracts.length
            + " ]";
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     */
    @Override
    public TermMustNotMeetAnyContract.Violation violation (
            Object plaintiff,
            Term<?> evidence
            )
    {
        return new TermMustNotMeetAnyContract.Violation (
            this,
            plaintiff,
            evidence );
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object, java.lang.Throwable)
     */
    @Override
    public TermMustNotMeetAnyContract.Violation violation (
            Object plaintiff,
            Term<?> evidence,
            Throwable cause
            )
    {
        final TermMustNotMeetAnyContract.Violation violation =
            this.violation ( plaintiff,
                             evidence,
                             cause );

        return violation;
    }




    /**
     * <p>
     * A violation of the TermMustNotMeetAnyContract contract.
     * </p>
     */
    public static class Violation
        extends TermViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            TermMustNotMeetAnyContract.serialVersionUID;


        /**
         * <p>
         * Creates a new TermMustNotMeetAnyContract.Violation.
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
         * Creates a new TermMustNotMeetAnyContract.Violation.
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
                    "The term met one (or more) verboten Contract(s).", // description
                    plaintiff,
                    evidence,
                    cause );
        }
    }
}
