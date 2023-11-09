package musaico.foundation.value.blocking;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.Parameter4;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.value.ElementalOperation;
import musaico.foundation.value.NonBlocking;
import musaico.foundation.value.Operation;
import musaico.foundation.value.Type;
import musaico.foundation.value.Value;

import musaico.foundation.value.abnormal.Error;

import musaico.foundation.value.contracts.ValueMustBeNonBlocking;

import musaico.foundation.value.incomplete.Expression;

import musaico.foundation.value.operations.AbstractOperation;


/**
 * <p>
 * An asynchronous, delayed result which wakes up the parent Blocked
 * object after waiting, successfully or unsuccessfully, to
 * evaluate result(s) (typically from other Blocking Value(s)).
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
 * @see musaico.foundation.value.blocking.MODULE#COPYRIGHT
 * @see musaico.foundation.value.blocking.MODULE#LICENSE
 */
public class MultipleBlockingValuesCollector<VALUE extends Object>
    extends AsynchronousResult<VALUE>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( MultipleBlockingValuesCollector.class );


    /**
     * <p>
     * Collects results asynchronously on our behalf.
     * </p>
     */
    public static class LogicOperation<LOGIC_VALUE extends Object>
        extends AbstractOperation<LOGIC_VALUE, LOGIC_VALUE>
        implements ElementalOperation<LOGIC_VALUE, LOGIC_VALUE>, Serializable
    {
        private static final long serialVersionUID =
            MultipleBlockingValuesCollector.serialVersionUID;

        // Enforces static parameter obligations and so on for us.
        private static final Advocate classContracts =
            new Advocate ( MultipleBlockingValuesCollector.LogicOperation.class );

        // Enforces parameter obligations and so on for us.
        private final Advocate contracts;

        // The MultipleBlockingValuesCollector we are collecting for.
        private final MultipleBlockingValuesCollector<LOGIC_VALUE> collector;

        /**
         * <p>
         * Creates a new MultipleBlockingValuesCollector.LogicOperation
         * to and/or/and so on asynchronous values on the behalf of
         * the specified MultipleBlockingValuesCollector.
         * </p>
         *
         * <p>
         * Once all the expected results have been received
         * (whether by final results or by timeouts), the
         * collector's final value is set.
         * </p>
         *
         * @param collector The MultipleBlockingValuesCollector on
         *                  whose behalf this operation will logically
         *                  and/or/and so on asynchronous results.
         *                  Must not be null.
         */
        public LogicOperation (
                               MultipleBlockingValuesCollector<LOGIC_VALUE> collector
                               )
            throws ParametersMustNotBeNull.Violation
        {
            super ( collector == null
                        ? null // type for input
                        : collector.type (),
                    collector == null
                        ? null // type for output
                        : collector.type () );

            classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                                   collector );

            this.collector = collector;

            this.contracts = new Advocate ( this );
        }
 
        /**
         * @see musaico.foundation.value.Operation#apply(musaico.foundation.value.Value)
         */
        @Override
        @SuppressWarnings("unchecked") // Cast with catch(ClassCastException).
        public Value<LOGIC_VALUE> apply (
                                         Value<LOGIC_VALUE> input
                                         )
            throws ParametersMustNotBeNull.Violation,
                   ReturnNeverNull.Violation
        {
            this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                                   input );

            // We've received a result.  Increment the collector counter.
            final boolean is_final_result;
            synchronized ( this.collector.lock )
            {
                this.collector.numCollectedResults ++;

                if ( this.collector.numCollectedResults
                     == this.collector.numExpectedResults )
                {
                    // Notify the MultipleBlockingValuesCollector.
                    is_final_result = true;
                }
                else
                {
                    // Still waiting for more asynchronous results.
                    is_final_result = false;
                }
            }

            // Only deal with NonBlocking<LOGIC_VALUE> values.
            // Ignore everything else.
            NonBlocking<LOGIC_VALUE> term = null;
            Value<LOGIC_VALUE> output = null;
            NonBlocking<LOGIC_VALUE> current_result = null;
            try
            {
                term = (NonBlocking<LOGIC_VALUE>) input;
                current_result = (NonBlocking<LOGIC_VALUE>)
                    this.collector.logic.apply ( term );
                output = current_result;
            }
            catch ( ClassCastException e )
            {
                // Do nothing with this input, or with
                // output that failed to cast (less likely).
                final Value<?> bad_value;
                if ( current_result != null )
                {
                    bad_value = current_result;
                }
                else if ( term != null )
                {
                    bad_value = term;
                }
                else
                {
                    bad_value = input;
                }

                final ValueMustBeNonBlocking.Violation violation =
                    ValueMustBeNonBlocking.CONTRACT.violation ( this,
                                                                bad_value );

                final Error<LOGIC_VALUE> value_to_evaluate =
                    new Error<LOGIC_VALUE> ( this.collector.type (),
                                             violation );
                current_result = (NonBlocking<LOGIC_VALUE>)
                    this.collector.logic.apply ( value_to_evaluate );
                output = input;
            }

            if ( is_final_result )
            {
                this.collector.setFinalResult ( current_result );
            }

            return output;
        }

        /**
         * @see musaico.foundation.value.Operation#apply(musaico.foundation.value.Value, long)
         */
        @Override
        public final Value<LOGIC_VALUE> apply (
                                               Value<LOGIC_VALUE> input,
                                               long num_elements
                                               )
            throws ParametersMustNotBeNull.Violation,
                   Parameter2.MustBeGreaterThanOrEqualToZero.Violation,
                   ReturnNeverNull.Violation
        {
            this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                                   input );
            this.contracts.check ( Parameter2.MustBeGreaterThanOrEqualToZero.CONTRACT,
                                   num_elements );

            // All or nothing.  We can't stream partial results from
            // multiple Blocking values.
            return this.apply ( input );
        }
    }


    // Enforces parameter obligations and so on for us.
    private final Advocate contracts;

    // Lock critical sections on this token.
    private final Serializable lock = new String ();

    // Accumulates and ands/ors/whatever the child asynchronous values
    // as they come in, and passes them to the logic Operation
    // to AND / OR and so on.
    private final LogicOperation<VALUE> logicOperation;

    // Logically ANDs together or ORs together asynchronous results
    // as they are received.  Invoked by the logicOperation (above).
    private final Operation<VALUE, VALUE> logic;

    // How many asynchrous child results are we waiting for?
    private final int numExpectedResults;

    // MUTABLE:
    // How many asynchronous child results have we received so far?
    private int numCollectedResults = 0;


    /**
     * <p>
     * Creates a new MultipleBlockingValuesCollector, which will block
     * for some maximum amount of time before failing with a Timeout.
     * </p>
     *
     * <p>
     * Once the result has been set (or the result has timed out), any and
     * all Operation(s) are notified, and the result is stored so
     * that it can be retrieved again instantly.
     * </p>
     *
     * @param type The Type of the conditional Value.
     *             Must not be null.
     *
     * @param max_timeout_in_nanoseconds The absolute maximum amount of
     *                                   time to block () for the asynchronous
     *                                   result before giving up.
     *                                   Even if the block() caller
     *                                   specifies a longer timeout,
     *                                   this is the absolute maximum
     *                                   blocking time.  Must be greater
     *                                   than 0L.
     *
     * @param logic The logic to perform on each result as it comes in,
     *              typically <code> AndValues </code>
     *              or <code> OrValues </code>.  Must not be null.
     *              Must always output a NonBlocking value.
     *
     * @param blocking_values 0 or more Blocking values to wait upon
     *                        before this collector's result
     *                        is finalized.  Must not be null.
     *                        Must not contain any null elements.
     */
    public MultipleBlockingValuesCollector (
                                            Type<VALUE> type,
                                            long max_timeout_in_nanoseconds,
                                            Operation<VALUE, VALUE> logic,
                                            Blocking<VALUE> [] blocking_values
                                            )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustBeGreaterThanZero.Violation,
               Parameter4.MustContainNoNulls.Violation
    {
        super ( type,
                max_timeout_in_nanoseconds );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               logic, blocking_values );
        classContracts.check ( Parameter4.MustContainNoNulls.CONTRACT,
                               blocking_values );

        this.logic = logic;
        this.numExpectedResults = blocking_values.length;
        this.numCollectedResults = 0;

        this.logicOperation =
            new MultipleBlockingValuesCollector.LogicOperation<VALUE> ( this );

        this.contracts = new Advocate ( this );

        // Now listen to each child for its result.
        for ( Blocking<VALUE> child_blocking : blocking_values )
        {
            final AsynchronousResult<VALUE> child_async =
                child_blocking.asynchronousResult ();

            // A Value which gets kicked by the AsynchronousResult,
            // triggering the logic Operation (AND/OR/and so on)
            // to do some work.
            final Expression<VALUE, VALUE> child_chain =
                new Expression<VALUE, VALUE> ( child_blocking,
                                               this.logicOperation.outputType (),
                                               this.logicOperation );

            child_async.async ( child_chain );
        }
    }


    /**
     * @return The logical and'ed / or'er of this result collector, such
     *         as an AndValues, or an OrValues, and so on.  Never null.
     */
    public Operation<VALUE, VALUE> logic ()
    {
        return this.logic;
    }


    /**
     * @return The number of asynchronous results which this collector
     *         has received so far from child asynchronous results.
     *         Always 0 or greater.
     */
    public int numCollectedResults ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        return this.numCollectedResults;
    }


    /**
     * @return The number of asynchronous results which this collector
     *         expects to receive from child asynchronous results.
     *         Always 0 or greater.
     */
    public int numExpectedResults ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        return this.numExpectedResults;
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString ()
    {
        final int num_collected_results;
        synchronized ( this.lock )
        {
            num_collected_results = this.numCollectedResults;
        }
        return super.toString () + "\n    ("
            + num_collected_results
            + " / "
            + this.numExpectedResults
            + " results collected)";
    }
}
