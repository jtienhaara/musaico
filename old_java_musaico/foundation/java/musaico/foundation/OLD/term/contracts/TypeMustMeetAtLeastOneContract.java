package musaico.foundation.term.contracts;

import java.io.Serializable;

import java.util.Collection;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;
import musaico.foundation.contract.UncheckedViolation;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Type;


/**
 * <p>
 * A guarantee that every Type meets at least one Contract in a specific set
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
public class TypeMustMeetAtLeastOneContract
    implements Contract<Type<?>, TypeMustMeetAtLeastOneContract.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final Advocate classContracts =
        new Advocate ( TypeMustMeetAtLeastOneContract.class );


    // The child Contracts used to check that Types meet this Contract.
    private final Contract<Type<?>, ?> [] childContracts;


    /**
     * <p>
     * Creates a new TypeMustMeetAtLeastOneContract.
     * </p>
     *
     * @param child_contracts The child contracts which comprise this
     *                        TypeMustMeetAtLeastOneContract composite
     *                        contract.  Used to check that each Type meets
     *                        this Contract, during calls to
     *                        <code> filter ( Type<?> ) </code>.
     *                        Must not be null.
     */
    @SuppressWarnings({"rawtypes",   // new Contract [] generic array.
                       "unchecked"}) // Possible heap pollution C<V<?>, ?> ...
    public TypeMustMeetAtLeastOneContract (
            Contract<Type<?>, ?> ... child_contracts
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               (Object []) child_contracts );

        classContracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               child_contracts );

        this.childContracts = (Contract<Type<?>, ?> [])
            new Contract [ child_contracts.length ];

        System.arraycopy ( child_contracts, 0,
                           this.childContracts, 0, child_contracts.length );
    }


    /**
     * <p>
     * Creates a new TypeMustMeetAtLeastOneContract.
     * </p>
     *
     * @param child_contracts The child contracts which comprise this
     *                        TypeMustMeetAtLeastOneContract composite
     *                        contract.  Used to check that each Type meets
     *                        this Contract, during calls to
     *                        <code> filter ( Type<?> ) </code>.
     *                        Must not be null.
     */
    @SuppressWarnings({"rawtypes",   // new Contract [] generic array.
                       "unchecked"}) // Cast Contract [] - Contract<V<?>, ?>.
    public TypeMustMeetAtLeastOneContract (
            Collection<Contract<Type<?>, ?>> child_contracts
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               child_contracts );

        classContracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               child_contracts );

        this.childContracts = (Contract<Type<?>, ?> [])
            new Contract [ child_contracts.size () ];

        int c = 0;
        for ( Contract<Type<?>, ?> child_contract : child_contracts )
        {
            this.childContracts [ c ] = child_contract;
            c ++;
        }
    }


    /**
     * <p>
     * Creates a new TypeMustMeetAtLeastOneContract.
     * </p>
     *
     * @param child_contracts The child contracts which comprise this
     *                        TypeMustMeetAtLeastOneContract composite
     *                        contract. Used to check that each Type meets
     *                        this Contract, during calls to
     *                        <code> filter ( Type<?> ) </code>.
     *                        Must not be null.
     */
    @SuppressWarnings({"rawtypes",   // new Contract [] generic array.
                       "unchecked"}) // Cast Contract [] - Contract<V<?>, ?>.
    public TypeMustMeetAtLeastOneContract (
            Countable<Contract<Type<?>, ?>> child_contracts
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               child_contracts );

        this.childContracts = (Contract<Type<?>, ?> [])
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
        for ( Contract<Type<?>, ?> contract : this.childContracts )
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

        return "The type must meet at least one Contract: "
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

        final TypeMustMeetAtLeastOneContract that =
            (TypeMustMeetAtLeastOneContract) obj;
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
            Type<?> type
            )
    {
        if ( type == null )
        {
            return FilterState.DISCARDED;
        }

        FilterState filter_state = FilterState.DISCARDED;
        for ( Contract<Type<?>, ?> contract : this.childContracts )
        {
            filter_state = contract.filter ( type );
            if ( filter_state.isKept () )
            {
                return filter_state;
            }
        }

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
    public TypeMustMeetAtLeastOneContract.Violation violation (
            Object plaintiff,
            Type<?> evidence
            )
    {
        return new TypeMustMeetAtLeastOneContract.Violation (
            this,
            plaintiff,
            evidence );
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object, java.lang.Throwable)
     */
    @Override
    public TypeMustMeetAtLeastOneContract.Violation violation (
            Object plaintiff,
            Type<?> evidence,
            Throwable cause
            )
    {
        final TypeMustMeetAtLeastOneContract.Violation violation =
            this.violation ( plaintiff,
                             evidence,
                             cause );

        return violation;
    }




    /**
     * <p>
     * A violation of the TypeMustMeetAtLeastOneContract contract.
     * </p>
     */
    public static class Violation
        extends UncheckedViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            TypeMustMeetAtLeastOneContract.serialVersionUID;


        /**
         * <p>
         * Creates a new TypeMustMeetAtLeastOneContract.Violation.
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
         * @param evidence The Type which violated the contract.
         *                 Can be null.
         */
        public Violation (
                          Contract<?, ?> contract,
                          Object plaintiff,
                          Type<?> evidence
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
         * Creates a new TypeMustMeetAtLeastOneContract.Violation.
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
         * @param evidence The Type which violated the contract.
         *                 Can be null.
         *
         * @param cause The Throwable which caused this contract violation.
         *              Can be null.
         */
        public Violation (
                          Contract<?, ?> contract,
                          Object plaintiff,
                          Type<?> evidence,
                          Throwable cause
                          )
            throws ParametersMustNotBeNull.Violation
        {
            super ( contract,
                    "The type did not meet at least one Contract.", // description
                    plaintiff,
                    evidence,
                    cause );
        }
    }
}
