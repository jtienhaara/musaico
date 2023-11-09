package musaico.foundation.term.contracts;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Contract;
import musaico.foundation.contract.UncheckedViolation;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;
import musaico.foundation.domains.LeftAndRight;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.term.Operation;


/**
 * <p>
 * The output type of each "left" Operation must not be compatible with
 * the input type of each "right" Operation.
 * </p>
 *
 * <p>
 * If the left output Type and the right input Type are of the same
 * element class, they are considered compatible.  Note, however, that
 * this does not guarantee overlapping element domains or whole term domains.
 * For example, if the left's output is a Type covering Strings of length
 * 1 to 7, and the right's input Type covers Strings of at least length 8,
 * then even though the actual outputs from the left Operation will never
 * satisfy the input requirements of the right Operation, the Types
 * will still be considered compatible, and OperationTypesMustNotMatch will
 * consider the Operations matching.
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
public class OperationTypesMustNotMatch
    implements Contract<LeftAndRight<Operation<?, ?>, Operation<?, ?>>, OperationTypesMustNotMatch.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The singleton OperationTypesMustNotMatch contract. */
    public static final OperationTypesMustNotMatch CONTRACT =
        new OperationTypesMustNotMatch ();


    /**
     * <p>
     * Creates OperationTypesMustNotMatch.CONTRACT.
     * </p>
     */
    private OperationTypesMustNotMatch ()
    {
    }


    /**
     * @see musaico.foundation.contract.Contract#description()
     */
    @Override
    public String description ()
    {
        return "Each left Operation's output Type must NOT be compatible with the right Operation's input Type.";
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
                               LeftAndRight<Operation<?, ?>, Operation<?, ?>> operations
                               )
    {
        if ( operations == null )
        {
            return FilterState.DISCARDED;
        }

        final Operation<?, ?> left = operations.left ();
        final Operation<?, ?> right = operations.right ();

        final Class<?> left_element_class =
            left.outputType ()
                .elementClass ();
        final Class<?> right_element_class =
            right.inputType ()
                 .elementClass ();

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
    public int hashCode ()
    {
        return 19 * ClassName.of ( this.getClass () ).hashCode ();
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
    public OperationTypesMustNotMatch.Violation violation (
            Object plaintiff,
            LeftAndRight<Operation<?, ?>, Operation<?, ?>> evidence
            )
    {
        return new OperationTypesMustNotMatch.Violation (
            this,
            plaintiff,
            evidence );
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object, java.lang.Throwable)
     */
    @Override
    public OperationTypesMustNotMatch.Violation violation (
            Object plaintiff,
            LeftAndRight<Operation<?, ?>, Operation<?, ?>> evidence,
            Throwable cause
            )
    {
        final OperationTypesMustNotMatch.Violation violation =
            this.violation ( plaintiff,
                             evidence,
                             cause );

        return violation;
    }




    /**
     * <p>
     * A violation of the OperationTypesMustNotMatch contract.
     * </p>
     */
    public static class Violation
        extends UncheckedViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            OperationTypesMustNotMatch.serialVersionUID;


        /**
         * <p>
         * Creates a new OperationTypesMustNotMatch.Violation.
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
         * @param evidence The Operations which violated the contract.
         *                 Can be null.
         */
        public Violation (
                          OperationTypesMustNotMatch contract,
                          Object plaintiff,
                          LeftAndRight<Operation<?, ?>, Operation<?, ?>> evidence
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
         * Creates a new OperationTypesMustNotMatch.Violation.
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
         * @param evidence The Operations which violated the contract.
         *                 Can be null.
         *
         * @param cause The Throwable which caused this contract violation.
         *              Can be null.
         */
        @SuppressWarnings("unchecked") // Cast Contract<?,?> - Contract<Obj,?>.
        public Violation (
                          OperationTypesMustNotMatch contract,
                          Object plaintiff,
                          LeftAndRight<Operation<?, ?>, Operation<?, ?>> evidence,
                          Throwable cause
                          )
            throws ParametersMustNotBeNull.Violation
        {
            super ( contract,
                    "The input Type of the right Operation "
                    + ( evidence == null
                            ? "null"
                            : evidence.right () + " (" + evidence.right ().inputType () + ")" )
                    + " is compatible with the output Type of the left Operation "
                    + ( evidence == null
                            ? "null"
                            : evidence.left () + " (" + evidence.left ().outputType () + ")" )
                    + ".", // description
                    plaintiff,
                    evidence,
                    cause );
        }
    }
}
