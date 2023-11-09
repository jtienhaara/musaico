package musaico.foundation.term.contracts;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Contract;
import musaico.foundation.contract.UncheckedViolation;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.domains.comparability.LeftAndRight;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.term.Type;


/**
 * <p>
 * Each "left" Type must not be compatible with each "right" Type.
 * </p>
 *
 * <p>
 * If the left Type and the right Type are of the same
 * element class, they are considered compatible.  Note, however, that
 * this does not guarantee overlapping element domains or whole term domains.
 * For example, if the left is a Type covering Strings of length
 * 1 to 7, and the right Type covers Strings of at least length 8,
 * then even though actual Terms of the left Type will never
 * satisfy the requirements of the right Type, the Types
 * will still be considered compatible, and TypesMustNotMatch will
 * consider the Types matching.
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
public class TypesMustNotMatch
    implements Contract<LeftAndRight<Type<?>, Type<?>>, TypesMustNotMatch.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The singleton TypesMustNotMatch contract. */
    public static final TypesMustNotMatch CONTRACT =
        new TypesMustNotMatch ();


    /**
     * <p>
     * Creates TypesMustNotMatch.CONTRACT.
     * </p>
     */
    private TypesMustNotMatch ()
    {
    }


    /**
     * @see musaico.foundation.contract.Contract#description()
     */
    @Override
    public final String description ()
    {
        return "Each left Type must NOT be compatible with the right Type.";
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
            LeftAndRight<Type<?>, Type<?>> types
            )
    {
        if ( types == null )
        {
            return FilterState.DISCARDED;
        }

        final Type<?> left = types.left ();
        final Type<?> right = types.right ();

        final Class<?> left_element_class =
            left.elementClass ();
        final Class<?> right_element_class =
            right.elementClass ();

        if ( left_element_class.equals ( right_element_class ) )
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
        return 19 * ClassName.of ( this.getClass () ).hashCode ();
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
    public TypesMustNotMatch.Violation violation (
            Object plaintiff,
            LeftAndRight<Type<?>, Type<?>> evidence
            )
    {
        return new TypesMustNotMatch.Violation (
            this,
            plaintiff,
            evidence );
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object, java.lang.Throwable)
     */
    @Override
    public TypesMustNotMatch.Violation violation (
            Object plaintiff,
            LeftAndRight<Type<?>, Type<?>> evidence,
            Throwable cause
            )
    {
        final TypesMustNotMatch.Violation violation =
            this.violation ( plaintiff,
                             evidence,
                             cause );

        return violation;
    }




    /**
     * <p>
     * A violation of the TypesMustNotMatch contract.
     * </p>
     */
    public static class Violation
        extends UncheckedViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            TypesMustNotMatch.serialVersionUID;


        /**
         * <p>
         * Creates a new TypesMustNotMatch.Violation.
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
         * @param evidence The Types which violated the contract.
         *                 Can be null.
         */
        public Violation (
                          TypesMustNotMatch contract,
                          Object plaintiff,
                          LeftAndRight<Type<?>, Type<?>> evidence
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
         * Creates a new TypesMustNotMatch.Violation.
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
         * @param evidence The Types which violated the contract.
         *                 Can be null.
         *
         * @param cause The Throwable which caused this contract violation.
         *              Can be null.
         */
        @SuppressWarnings("unchecked") // Cast Contract<?,?> - Contract<Obj,?>.
        public Violation (
                          TypesMustNotMatch contract,
                          Object plaintiff,
                          LeftAndRight<Type<?>, Type<?>> evidence,
                          Throwable cause
                          )
            throws ParametersMustNotBeNull.Violation
        {
            super ( contract,
                    "The right Type "
                    + ( evidence == null
                            ? "null"
                            : evidence.right () + " (" + evidence.right () + ")" )
                    + " is compatible with the left Type "
                    + ( evidence == null
                            ? "null"
                            : evidence.left () + " (" + evidence.left () + ")" )
                    + ".", // description
                    plaintiff,
                    evidence,
                    cause );
        }
    }
}
