package musaico.foundation.term.contracts;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.domains.equality.EqualTo;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;


/**
 * <p>
 * Each inspected Term must return a <code> filterState () </code>
 * that returns <code> isKept () == true </code>.
 * </p>
 *
 * @see musaico.foundation.filter.FilterState
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
public class TermFilterStateMustBeKept
    implements Contract<Term<?>, TermFilterStateMustBeKept.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The TermFilterStateMustBeKept contract. */
    public static final TermFilterStateMustBeKept CONTRACT =
        new TermFilterStateMustBeKept ();


    // Creates the TermFilterStateMustBeKept singleton contract.
    private TermFilterStateMustBeKept ()
    {
    }


    /**
     * @see musaico.foundation.contract.Contract#description()
     */
    @Override
    public final String description ()
    {
        return "The term's filterState () must return isKept () == true.";
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
        else if ( term.filterState ().isKept () )
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
    public final int hashCode ()
    {
        return 31 * ClassName.of ( this.getClass () ).hashCode ();
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return ClassName.of ( this.getClass () );
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     */
    @Override
    public TermFilterStateMustBeKept.Violation violation (
            Object plaintiff,
            Term<?> evidence
            )
    {
        return new TermFilterStateMustBeKept.Violation (
            this,
            plaintiff,
            evidence );
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object, java.lang.Throwable)
     */
    @Override
    public TermFilterStateMustBeKept.Violation violation (
            Object plaintiff,
            Term<?> evidence,
            Throwable cause
            )
    {
        final TermFilterStateMustBeKept.Violation violation =
            this.violation ( plaintiff,
                             evidence,
                             cause );

        return violation;
    }




    /**
     * <p>
     * A violation of the TermFilterStateMustBeKept contract.
     * </p>
     */
    public static class Violation
        extends TermViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            TermFilterStateMustBeKept.serialVersionUID;


        /**
         * <p>
         * Creates a new TermFilterStateMustBeKept.Violation.
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
                          TermFilterStateMustBeKept contract,
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
         * Creates a new TermFilterStateMustBeKept.Violation.
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
                          TermFilterStateMustBeKept contract,
                          Object plaintiff,
                          Term<?> evidence,
                          Throwable cause
                          )
            throws ParametersMustNotBeNull.Violation
        {
            super ( contract,
                    "The term's filterState () did not return"
                    + " isKept () == true.", // description
                    plaintiff,
                    evidence,
                    cause );
        }
    }
}
