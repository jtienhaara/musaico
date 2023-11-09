package musaico.foundation.term.contracts;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Contract;
import musaico.foundation.contract.UncheckedViolation;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.term.Term;


/**
 * <p>
 * Each Invocation of each Operation must throw an Exception.
 * </p>
 *
 * <p>
 * For example, an Operation might be expected to throw a
 * ParametersMustNotBeNull.Violation when it is invoked with a null input.
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
public class OperationMustThrowException
    implements Contract<Invocation<?, ?>, OperationMustThrowException.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The OperationMustThrowException contract. */
    public static final OperationMustThrowException CONTRACT =
        new OperationMustThrowException ();


    // Enforces constructor and static method parameter obligations for us.
    private static final Advocate classContracts =
        new Advocate ( OperationMustThrowException.class );


    // Private constructor.  Use the singleton instead:
    // OperationMustThrowException.CONTRACT
    private OperationMustThrowException ()
    {
    }


    /**
     * @see musaico.foundation.contract.Contract#description()
     */
    @Override
    public String description ()
    {
        return "Each invocation of each operation must throw an Exception.";
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
                               Invocation<?, ?> invocation
                               )
    {
        if ( invocation == null )
        {
            return FilterState.DISCARDED;
        }

        try
        {
            // We don't care what the output is.
            invocation.execute ();
        }
        catch ( Exception e )
        {
            // Exception thrown.  Failed.
            return FilterState.DISCARDED;
        }

        // No exception thrown.  Succeeded.
        return FilterState.KEPT;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return 31 * ClassName.of ( this.getClass () ).hashCode ();
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
    public OperationMustThrowException.Violation violation (
            Object plaintiff,
            Invocation<?, ?> evidence
            )
    {
        return new OperationMustThrowException.Violation (
            this,
            plaintiff,
            evidence );
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object, java.lang.Throwable)
     */
    @Override
    public OperationMustThrowException.Violation violation (
            Object plaintiff,
            Invocation<?, ?> evidence,
            Throwable cause
            )
    {
        final OperationMustThrowException.Violation violation =
            this.violation ( plaintiff,
                             evidence,
                             cause );

        return violation;
    }




    /**
     * <p>
     * A violation of the OperationMustThrowException contract.
     * </p>
     */
    public static class Violation
        extends UncheckedViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            OperationMustThrowException.serialVersionUID;


        /**
         * <p>
         * Creates a new OperationMustThrowException.Violation.
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
         * @param evidence The operation Invocation which violated
         *                 the contract.  Can be null.
         */
        public Violation (
                          Contract<?, ?> contract,
                          Object plaintiff,
                          Invocation<?, ?> evidence
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
         * Creates a new OperationMustThrowException.Violation.
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
         * @param evidence The operation Invocation which violated
         *                 the contract.  Can be null.
         *
         * @param cause The Throwable which caused this contract violation.
         *              Can be null.
         */
        public Violation (
                          Contract<?, ?> contract,
                          Object plaintiff,
                          Invocation<?, ?> evidence,
                          Throwable cause
                          )
            throws ParametersMustNotBeNull.Violation
        {
            super ( contract,
                    "The Operation "
                    + ( evidence == null
                            ? "null"
                            : evidence.operation () )
                    + " threw an Exception when invoked with input "
                    + ( evidence == null
                            ? "null"
                            : evidence.input () )
                    + ".", // description
                    plaintiff,
                    evidence,
                    cause );
        }
    }
}
