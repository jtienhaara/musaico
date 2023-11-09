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
 * Each Invocation of each Operation must return an output which meets
 * some other Contract (such as TermMustNotBeEmpty or TermMustBeMultiple
 * and so on).
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
public class OperationOutputMustMeet
    implements Contract<Invocation<?, ?>, OperationOutputMustMeet.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces constructor and static method parameter obligations for us.
    private static final Advocate classContracts =
        new Advocate ( OperationOutputMustMeet.class );


    // The Contract which must be met by the output Term of
    // every operation Invocation.
    private final Contract<Object, ?> outputContract;


    /**
     * <p>
     * Creates a new OperationOutputMustMeet(C) contract.
     * </p>
     *
     * @param output_contract The Contract which must be met
     *                        by the output Term of
     *                        each operation Invocation.
     *                        Must not be null.
     */
    @SuppressWarnings("unchecked") // Cast Contract<?,?> - Contract<Obj,?>.
    public OperationOutputMustMeet (
                                    Contract<?, ?> output_contract
                                    )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               output_contract );

        this.outputContract = (Contract<Object, ?>) output_contract;
    }


    /**
     * @see musaico.foundation.contract.Contract#description()
     */
    @Override
    public String description ()
    {
        return "The output of each invocation of each operation"
            + " must meet: "
            + this.outputContract.description ();
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

        final OperationOutputMustMeet that = (OperationOutputMustMeet) obj;
        if ( this.outputContract == null )
        {
            if ( that.outputContract != null )
            {
                return false;
            }
        }
        else if ( that.outputContract == null )
        {
            return false;
        }
        else if ( ! this.outputContract.equals ( that.outputContract ) )
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
            final Term<?> output = invocation.execute ();

            final FilterState result =
                this.outputContract.filter ( output );

            return result;
        }
        catch ( Exception e )
        {
            // Exception thrown.  Failed.
            return FilterState.DISCARDED;
        }
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return 17 * ClassName.of ( this.getClass () ).hashCode ()
            + ( this.outputContract == null
                    ? 0
                    : this.outputContract.hashCode () );
    }


    /**
     * @return The Contract which must be met by the output Term of
     *         every operation Invocation.  Never null.
     */
    public final Contract<?, ?> outputContract ()
    {
        return this.outputContract;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this.getClass () )
            + " "
            + this.outputContract;
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     */
    @Override
    public OperationOutputMustMeet.Violation violation (
            Object plaintiff,
            Invocation<?, ?> evidence
            )
    {
        return new OperationOutputMustMeet.Violation (
            this,
            plaintiff,
            evidence );
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object, java.lang.Throwable)
     */
    @Override
    public OperationOutputMustMeet.Violation violation (
            Object plaintiff,
            Invocation<?, ?> evidence,
            Throwable cause
            )
    {
        final OperationOutputMustMeet.Violation violation =
            this.violation ( plaintiff,
                             evidence,
                             cause );

        return violation;
    }




    /**
     * <p>
     * A violation of the OperationOutputMustMeet contract.
     * </p>
     */
    public static class Violation
        extends UncheckedViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            OperationOutputMustMeet.serialVersionUID;


        /**
         * <p>
         * Creates a new OperationOutputMustMeet.Violation.
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
                          OperationOutputMustMeet contract,
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
         * Creates a new OperationOutputMustMeet.Violation.
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
        @SuppressWarnings("unchecked") // Cast Contract<?,?> - Contract<Obj,?>.
        public Violation (
                          OperationOutputMustMeet contract,
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
                    + " returned an unacceptable output when invoked with input "
                    + ( evidence == null
                            ? "null"
                            : evidence.input () )
                    + " output = "
                    + ( evidence == null
                            ? "null"
                            : evidence.output () )
                    + ".", // description
                    plaintiff,
                    evidence,
                    contract.outputContract () == null
                        ? cause
                        : ( (Contract<Object, ?>) contract.outputContract () )
                              .violation (
                                  contract,
                                  evidence == null
                                      ? null
                                      : evidence.output (),
                                  cause
                                  )
                              );
        }
    }
}
