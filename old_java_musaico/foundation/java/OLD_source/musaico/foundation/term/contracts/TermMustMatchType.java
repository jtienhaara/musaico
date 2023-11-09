package musaico.foundation.term.contracts;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Contract;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.term.Type;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;


/**
 * <p>
 * A general-purpose Contract for Term each whole Term
 * must match a specific Type, even if it is of a different Type,
 * or a TermViolation will be generated.
 * </p>
 *
 * <p>
 * For example, a Term of Type&lt;String&gt; with minimum length 8
 * will match a generic Type&lt;String&gt;.  However not all Terms
 * of generic Type&lt;String&gt; will match the Type&lt;String&gt;
 * of minimum length 8, since they might be too short.
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
public class TermMustMatchType
    implements Contract<Term<?>, TermMustMatchType.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces constructor and static method parameter obligations for us.
    private static final Advocate classContracts =
        new Advocate ( TermMustMatchType.class );


    // The Type which every Term must match.
    private final Type<?> type;


    /**
     * <p>
     * Creates a new TermMustMatchType contract.
     * </p>
     *
     * @param type The Type which every inspected Term must match.
     *             Must not be null.
     */
    public TermMustMatchType (
                               Type<?> type
                               )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               type );

        this.type = type;
    }


    /**
     * @see musaico.foundation.contract.Contract#description()
     */
    @Override
    public final String description ()
    {
        return "Each term must match "
            + this.type
            + ".";
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

        final TermMustMatchType that =
            (TermMustMatchType) obj;

        if ( this.type == null )
        {
            if ( that.type != null )
            {
                return false;
            }
        }
        else if ( that.type == null )
        {
            return false;
        }
        else if ( ! this.type.equals ( that.type ) )
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

        FilterState result = FilterState.KEPT;
        for ( Contract<Term<?>, ?> term_contract : this.type.constraints () )
        {
            result = term_contract.filter ( term );
            if ( ! result.isKept () )
            {
                break;
            }
        }

        return result;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return 31 * ClassName.of ( this.getClass () ).hashCode ()
            + this.type.hashCode ();
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return ClassName.of ( this.getClass () )
            + " { "
            + this.type
            + " }";
    }


    /**
     * @return The Type which every Term must match.
     *         Never null.
     */
    public final Type<?> type ()
    {
        return this.type;
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     */
    @Override
    public final TermMustMatchType.Violation violation (
            Object plaintiff,
            Term<?> evidence
            )
    {
        return new TermMustMatchType.Violation ( this,
                                                  plaintiff,
                                                  evidence );
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object, java.lang.Throwable)
     */
    @Override
    public final TermMustMatchType.Violation violation (
            Object plaintiff,
            Term<?> evidence,
            Throwable cause
            )
    {
        final TermMustMatchType.Violation violation =
            this.violation ( plaintiff,
                             evidence,
                             cause );

        return violation;
    }




    /**
     * <p>
     * A violation of the TermMustMatchType contract.
     * </p>
     */
    public static class Violation
        extends TermViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            TermMustMatchType.serialVersionUID;


        /**
         * <p>
         * Creates a new TermMustMatchType.Violation.
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
                          TermMustMatchType contract,
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
         * Creates a new TermMustMatchType.Violation.
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
                          TermMustMatchType contract,
                          Object plaintiff,
                          Term<?> evidence,
                          Throwable cause
                          )
            throws ParametersMustNotBeNull.Violation
        {
            super ( contract,
                    "The term did not match the Type: "
                    + evidence
                    + ".", // description
                    plaintiff,
                    evidence,
                    cause );
        }
    }
}
