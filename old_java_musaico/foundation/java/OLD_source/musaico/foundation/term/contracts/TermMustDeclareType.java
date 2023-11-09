package musaico.foundation.term.contracts;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Contract;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.FilterState;

import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;
import musaico.foundation.term.Type;


/**
 * <p>
 * Keeps only Terms which declare they are instances of certain Type(s).
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
public class TermMustDeclareType
    implements Contract<Term<?>, TermMustDeclareType.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( TermMustDeclareType.class );


    // The Type(s) which each KEPT term declares.
    // All must be declared by a Term for it to be KEPT.
    private final Type<?> [] types;


    /**
     * <p>
     * Creates a new TermMustDeclareType.
     * </p>
     *
     * @param types The Type(s) which each KEPT term declares.
     *              All must be declared by a Term for it to be KEPT.
     *              Must not be null.  Must not contain any null elements.
     */
    @SuppressWarnings({"unchecked", "rawtypes"}) // Generic array creation.
    public TermMustDeclareType (
            Type<?> ... types
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               (Object) types );
        classContracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               types );

        this.types = (Type<?> [])
            new Type [ types.length ];
        System.arraycopy ( types, 0,
                           this.types, 0, types.length );
    }


    /**
     * @return The Type(s) which each Term must declare.
     *         Never null.  Never contains any null elements.
     */
    public final Type<?> [] types ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        final Type<?> [] types =
            new Type<?> [ this.types.length ];
        System.arraycopy ( this.types, 0,
                           types, 0, this.types.length );

        return types;
    }


    /**
     * @see musaico.foundation.contract.Contract#description()
     */
    @Override
    public final String description ()
    {
        final StringBuilder types_buf = new StringBuilder ();
        boolean is_first = true;
        for ( Type<?> type : this.types )
        {
            if ( is_first )
            {
                is_first = false;
            }
            else
            {
                types_buf.append ( ", " );
            }

            types_buf.append ( "" + type );
        }

        return "Each element of each term"
            + " must declare all of the Type(s): { "
            + types_buf.toString ()
            + " }.";
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
            Object object
            )
    {
        if ( object == null )
        {
            return false;
        }
        else if ( object == this )
        {
            return true;
        }
        else if ( object.getClass () != this.getClass () )
        {
            return false;
        }

        final TermMustDeclareType that =
            (TermMustDeclareType) object;

        if ( this.types == null )
        {
            if ( that.types != null )
            {
                return false;
            }
        }
        else if ( that.types == null )
        {
            return false;
        }
        else if ( this.types.length != that.types.length )
        {
            return false;
        }

        for ( int t = 0; t < this.types.length; t ++ )
        {
            if ( this.types [ t ] == null )
            {
                if ( that.types [ t ] != null )
                {
                    return false;
                }
            }
            else if ( that.types [ t ] == null )
            {
                return false;
            }
            else if ( ! this.types [ t ].equals ( that.types [ t ] ) )
            {
                return false;
            }
        }

        return true;
    }


    /**
     * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
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

        for ( Type<?> type : this.types )
        {
            if ( ! term.declares ( type ) )
            {
                return FilterState.DISCARDED;
            }
        }

        return FilterState.KEPT;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        if ( this.types == null )
        {
            return 0;
        }

        int hash_code = 1;
        for ( Type<?> type : this.types )
        {
            hash_code *= 13;
            hash_code += type.hashCode ();
        }

        return hash_code;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        final StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( "{" );
        boolean is_first = true;
        if ( this.types != null )
        {
            for ( Type<?> type : this.types )
            {
                if ( is_first )
                {
                    is_first = false;
                }
                else
                {
                    sbuf.append ( "," );
                }

                sbuf.append ( " " + type );
            }
        }
        if ( ! is_first )
        {
            sbuf.append ( " " );
        }
        sbuf.append ( "}" );

        return ClassName.of ( this.getClass () )
            + " " + sbuf.toString ();
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     */
    @Override
    public final TermMustDeclareType.Violation violation (
            Object plaintiff,
            Term<?> evidence
            )
    {
        return new TermMustDeclareType.Violation (
            this,
            plaintiff,
            evidence );
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, musaico.foundation.term.Term, java.lang.Throwable)
     */
    @Override
    public final TermMustDeclareType.Violation violation (
            Object plaintiff,
            Term<?> evidence,
            Throwable cause
            )
    {
        final TermMustDeclareType.Violation violation =
            this.violation ( plaintiff,
                             evidence,
                             cause );

        return violation;
    }




    /**
     * <p>
     * A violation of the TermMustDeclareType contract.
     * </p>
     */
    public static class Violation
        extends TermViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            TermMustDeclareType.serialVersionUID;


        /**
         * <p>
         * Creates a new TermMustDeclareType.Violation.
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
                          TermMustDeclareType contract,
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
         * Creates a new TermMustDeclareType.Violation.
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
                          TermMustDeclareType contract,
                          Object plaintiff,
                          Term<?> evidence,
                          Throwable cause
                          )
            throws ParametersMustNotBeNull.Violation
        {
            super ( contract,
                    "The Term did not declare the type(s): "
                    + nonTypes ( contract, evidence )
                    + ".", // description
                    plaintiff,
                    evidence,
                    cause );
        }


        private static final String nonTypes (
                TermMustDeclareType contract,
                Term<?> term
                )
        {
            final Type<?> [] types = contract.types ();

            final StringBuilder sbuf = new StringBuilder ();
            sbuf.append ( "{" );
            boolean is_first = true;
            if ( types != null )
            {
                for ( Type<?> type : types )
                {
                    if ( term.declares ( type ) )
                    {
                        continue;
                    }

                    if ( is_first )
                    {
                        is_first = false;
                    }
                    else
                    {
                        sbuf.append ( "," );
                    }

                    sbuf.append ( " " + type );
                }
            }
            if ( ! is_first )
            {
                sbuf.append ( " " );
            }
            sbuf.append ( "}" );

            return sbuf.toString ();
        }
    }
}
