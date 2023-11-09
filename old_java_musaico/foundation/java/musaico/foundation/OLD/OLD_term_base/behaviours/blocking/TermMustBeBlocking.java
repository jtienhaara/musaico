package musaico.foundation.term.blocking;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;


/**
 * <p>
 * A guarantee of a blocking term.
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
 * @see musaico.foundation.term.blocking.MODULE#COPYRIGHT
 * @see musaico.foundation.term.blocking.MODULE#LICENSE
 */
public class TermMustBeBlocking
    implements Contract<Term<?>, TermMustBeBlocking.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The TermMustBeBlocking contract. */
    public static final TermMustBeBlocking CONTRACT =
        new TermMustBeBlocking ();


    /**
     * @see musaico.foundation.contract.Contract#description()
     */
    @Override
    public String description ()
    {
        return "The term must be Blocking, a term which is initially"
            + " incomplete, but can be waited upon for the eventual"
            + " complete, non-blocking result.";
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
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

        return true;
    }


    /**
     * @see musaico.filter.Filter#filter(java.lang.Object)
     */
    @Override
    public FilterState filter (
                               Term<?> term
                               )
    {
        if ( term == null )
        {
            return FilterState.DISCARDED;
        }
        else if ( term instanceof Blocking )
        {
            return FilterState.KEPT;
        }
        else
        {
            return FilterState.DISCARDED;
        }
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return ClassName.of ( this.getClass () ).hashCode ();
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this.getClass () );
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     */
    @Override
    public TermMustBeBlocking.Violation violation (
                                                    Object plaintiff,
                                                    Term<?> evidence
                                                    )
    {
        return new TermMustBeBlocking.Violation ( this,
                                                   plaintiff,
                                                   evidence );
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, musaico.foundation.term.Term, java.lang.Throwable)
     */
    @Override
    public TermMustBeBlocking.Violation violation (
                                                    Object plaintiff,
                                                    Term<?> evidence,
                                                    Throwable cause
                                                    )
    {
        final TermMustBeBlocking.Violation violation =
            this.violation (                              plaintiff,
                             evidence );

        if ( cause != null )
        {
            violation.initCause ( cause );
        }

        return violation;
    }




    /**
     * <p>
     * A violation of the TermMustBeBlocking contract.
     * </p>
     */
    public static class Violation
        extends TermViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            TermMustBeBlocking.serialVersionUID;


        /**
         * <p>
         * Creates a new TermMustBeBlocking.Violation.
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
         * Creates a new TermMustBeBlocking.Violation.
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
                    "The term was not Blocking.", // description
                    plaintiff,
                    evidence,
                    cause );
        }
    }
}
