package musaico.foundation.term.blocking;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import java.math.BigDecimal;


import musaico.foundation.capability.operating.ErrorState;
import musaico.foundation.capability.operating.OperatingState;

import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter3;
import musaico.foundation.contract.obligations.Parameter5;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;
import musaico.foundation.domains.Clock;

import musaico.foundation.term.ElementalOperation;
import musaico.foundation.term.NonBlocking;
import musaico.foundation.term.Operation;
import musaico.foundation.term.Type;
import musaico.foundation.term.Term;

import musaico.foundation.term.abnormal.Error;

import musaico.foundation.term.contracts.TermMustBeNonBlocking;

import musaico.foundation.term.expression.Expression;

import musaico.foundation.term.finite.No;
import musaico.foundation.term.finite.One;

import musaico.foundation.term.operations.CumulativeOperation;


/**
 * <p>
 * An asynchronous, delayed result which wakes up the parent Blocked
 * object after waiting, successfully or unsuccessfully, for other
 * Blocking terms to finish and their final values to be merged.
 * </p>
 *
 * <p>
 * Typically the final results of the Blocking terms are merged
 * together with an AND or OR operation.
 * </p>
 *
 *
 * <p>
 * In Java, every Result must be Serializable in order to play nicely
 * over RMI.  WARNING: The final and partial or intermediate values
 * of a Result need not be Serializable.  If a Result containing a
 * non-Serializable value is passed to or from a method over an RMI
 * remote request, it will generate a RemoteException.
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
public class MultipleBlockingTermsResult<VALUE extends Object>
    extends LocalResult<VALUE>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( MultipleBlockingTermsResult.class );


    /**
     * <p>
     * Collects results asynchronously on our behalf.
     * </p>
     */
    public static class LogicOperation<LOGIC_VALUE extends Object>
        implements ElementalOperation<LOGIC_VALUE, LOGIC_VALUE>, Serializable
    {
        private static final long serialVersionUID =
            MultipleBlockingTermsResult.serialVersionUID;

        // Enforces static parameter obligations and so on for us.
        private static final Advocate classContracts =
            new Advocate ( MultipleBlockingTermsResult.LogicOperation.class );

        // Lock critical sections on this token:
        private final Serializable lock = new String ( "lock" );

        // Enforces parameter obligations and so on for us.
        private final Advocate contracts;

        // The MultipleBlockingTermsResult we are collecting for.
        private final MultipleBlockingTermsResult<LOGIC_VALUE> collector;

        // MUTABLE:
        // The current logic to AND or OR together results.
        // Each application returns a new CumulativeOperation
        // with new state.  When we're done, we set the final
        // blocking result to the state of the cumulative logic operation.
        private CumulativeOperation<LOGIC_VALUE, LOGIC_VALUE> logic = null;


        /**
         * <p>
         * Creates a new MultipleBlockingTermsResult.LogicOperation
         * to merge asynchronous values on the behalf of
         * the specified MultipleBlockingTermsResult, by
         * and'ing them / or'ing them / and so on.
         * </p>
         *
         * <p>
         * Once all the expected results have been received
         * (whether by final results or by timeouts), the
         * collector's final value is set.
         * </p>
         *
         * @param collector The MultipleBlockingTermsResult on
         *                  whose behalf this operation will logically
         *                  and/or/and so on asynchronous results.
         *                  Must not be null.
         *
         * @param logic The AND or OR operation to apply.
         *              Must not be null.
         */
        public LogicOperation (
                MultipleBlockingTermsResult<LOGIC_VALUE> collector,
                CumulativeOperation<LOGIC_VALUE, LOGIC_VALUE> logic
                )
            throws ParametersMustNotBeNull.Violation
        {
            classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                                   collector, logic );

            this.collector = collector;
            this.logic = logic;

            this.contracts = new Advocate ( this );
        }
 
        /**
         * @see musaico.foundation.term.Operation#apply(musaico.foundation.term.Term)
         */
        @Override
        @SuppressWarnings("unchecked") // Cast with catch(ClassCastException).
        public Term<LOGIC_VALUE> apply (
                                         Term<LOGIC_VALUE> input
                                         )
            throws ParametersMustNotBeNull.Violation,
                   ReturnNeverNull.Violation
        {
            this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                                   input );

            // We've received a result.  Increment the collector counter.
            final boolean is_final_value;
            synchronized ( this.collector.lock )
            {
                this.collector.numCollectedResults ++;

                if ( this.collector.numCollectedResults
                     == this.collector.blockingTerms.length )
                {
                    // Notify the MultipleBlockingTermsResult.
                    is_final_value = true;
                }
                else
                {
                    // Still waiting for more asynchronous results.
                    is_final_value = false;
                }
            }

            // Only deal with NonBlocking<LOGIC_VALUE> values.
            // Ignore everything else.
            NonBlocking<LOGIC_VALUE> term = null;
            Term<LOGIC_VALUE> output = null;
            NonBlocking<LOGIC_VALUE> current_result = null;
            try
            {
                term = (NonBlocking<LOGIC_VALUE>) input;

                synchronized ( this.lock )
                {
                    this.logic = this.logic.apply ( term )
                                           .orNone ();
                    current_result = (NonBlocking<LOGIC_VALUE>)
                        this.logic.state ();
                }

                output = current_result;
            }
            catch ( ClassCastException e )
            {
                // Do nothing with this input, or with
                // output that failed to cast (less likely).
                final Term<?> bad_term;
                if ( current_result != null )
                {
                    bad_term = current_result;
                }
                else if ( term != null )
                {
                    bad_term = term;
                }
                else
                {
                    bad_term = input;
                }

                final TermMustBeNonBlocking.Violation violation =
                    TermMustBeNonBlocking.CONTRACT.violation ( this,
                                                                bad_term );

                final Error<LOGIC_VALUE> error =
                    new Error<LOGIC_VALUE> ( this.collector.type (),
                                             violation );
                synchronized ( this.lock )
                {
                    this.logic = this.logic.apply ( error )
                                           .orNone ();
                    current_result = (NonBlocking<LOGIC_VALUE>)
                        this.logic.state ();
                }

                output = input;
            }

            if ( is_final_value )
            {
                this.collector.setFinalTerm ( current_result );
            }
            else
            {
                this.collector.setPartialTerm ( current_result );
            }

            return output;
        }

        /**
         * @see musaico.foundation.term.ElementalOperation#apply(musaico.foundation.term.Term, musaico.foundation.term.ElementalOperation.BlockSize)
         */
        @Override
        public final Term<LOGIC_VALUE> apply (
                Term<LOGIC_VALUE> input,
                ElementalOperation.BlockSize minimum_output_size
                )
            throws ParametersMustNotBeNull.Violation,
                   ReturnNeverNull.Violation
        {
            this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                                   input, minimum_output_size );

            // All or nothing.  We can't stream partial results from
            // multiple Blocking terms.
            return this.apply ( input );
        }


        /**
         * @see java.lang.Object#equals(java.lang.Object)
         *
         * Can be overridden.
         */
        @Override
        public boolean equals (
                               Object object
                               )
        {
            if ( object == this )
            {
                return true;
            }
            else if ( object == null )
            {
                return false;
            }
            else if ( object.getClass () != this.getClass () )
            {
                return false;
            }

            final MultipleBlockingTermsResult.LogicOperation<?> that =
                (MultipleBlockingTermsResult.LogicOperation<?>) object;

            if ( this.collector == null )
            {
                if ( that.collector != null )
                {
                    return false;
                }
            }
            else if ( that.collector == null )
            {
                return false;
            }
            else if ( ! this.collector.equals ( that.collector ) )
            {
                return false;
            }

            final Class<?> this_logic_class;
            synchronized ( this.lock )
            {
                if ( this.logic == null )
                {
                    this_logic_class = null;
                }
                else
                {
                    this_logic_class = this.logic.getClass ();
                }
            }

            final Class<?> that_logic_class;
            synchronized ( that.lock )
            {
                if ( that.logic == null )
                {
                    that_logic_class = null;
                }
                else
                {
                    that_logic_class = that.logic.getClass ();
                }
            }

            if ( this_logic_class != that_logic_class )
            {
                return false;
            }

            return true;
        }


        /**
         * @see java.lang.Object#hashCode()
         *
         * Can be overridden.
         */
        @Override
        public int hashCode ()
        {
            return
                ( this.collector == null
                      ? 0
                      : this.collector.hashCode () );
        }


        /**
         * @see musaico.foundation.term.Operation#inputType()
         */
        @Override
        public final Type<LOGIC_VALUE> inputType ()
            throws ReturnNeverNull.Violation
        {
            return this.collector.type ();
        }


        /**
         * @see musaico.foundation.term.ElementalOperation#minimumInputSize(musaico.foundation.term.ElementalOperation.BlockSize)
         */
        @Override
        public final ElementalOperation.BlockSize minimumInputSize (
                ElementalOperation.BlockSize for_output_size
                )
            throws ParametersMustNotBeNull.Violation,
                   ReturnNeverNull.Violation
        {
            this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                                   for_output_size );

            // All or nothing.
            return ElementalOperation.BlockSize.ALL;
        }


        /**
         * @see musaico.foundation.term.Operation#outputType()
         */
        @Override
        public final Type<LOGIC_VALUE> outputType ()
            throws ReturnNeverNull.Violation
        {
            return this.collector.type ();
        }


        /**
         * @see java.lang.Object#toString()
         *
         * Can be overridden.
         */
        @Override
        public String toString ()
        {
            return ClassName.of ( this.getClass () ) + "( "
                + this.collector.type ()
                + " ) --> "
                + this.collector.type ();
        }
    }


    // Enforces parameter obligations and so on for us.
    private final Advocate contracts;

    // Lock critical sections on this token.
    private final Serializable lock = new String ( "lock" );

    // The Blocking terms to collect.
    private final Blocking<VALUE> [] blockingTerms;


    // MUTABLE:
    // How many asynchronous child results have we received so far?
    private int numCollectedResults = 0;


    /**
     * <p>
     * Creates a new MultipleBlockingTermsResult, which will block
     * for some maximum amount of time before failing with a Timeout.
     * </p>
     *
     * <p>
     * Once the result has been set (or the result has timed out), any and
     * all Operation(s) are notified, and the result is stored so
     * that it can be retrieved again instantly.
     * </p>
     *
     * @param term_type The Type common to all the Blocking terms.
     *                  Must not be null.
     *
     * @param clock The Clock used to measure start time and elapsed time.
     *              Also used to calculate the remainder of await ( ... ) time
     *              after the input is ready (if any at all), and await the
     *              remainder of the time on the output, in case it is also
     *              a Blocking or similar Incomplete result.
     *              Must not be null.
     *
     * @param max_timeout_in_seconds The absolute maximum amount of
     *                               time to block () for the asynchronous
     *                               result before giving up.
     *                               Even if the block() caller
     *                               specifies a longer timeout,
     *                               this is the absolute maximum
     *                               blocking time.  Must be greater
     *                               than BigDecimal.ZERO.
     *
     * @param logic The logic to perform on each result as it comes in,
     *              typically <code> AndTerms </code>
     *              or <code> OrTerms </code>.  Must not be null.
     *              Must always output a NonBlocking term.
     *
     * @param blocking_terms 0 or more Blocking terms to wait upon
     *                       before this collector's result
     *                       is finalized.  Must not be null.
     *                       Must not contain any null elements.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
        // Cast new Val [] -> new Val<V, V> [].
        // Java forces us to create generic arrays.
    public MultipleBlockingTermsResult (
                                         Type<VALUE> term_type,
                                         Clock clock,
                                         BigDecimal max_timeout_in_seconds,
                                         CumulativeOperation<VALUE, VALUE> logic,
                                         Blocking<VALUE> ... blocking_terms
                                         )
        throws ParametersMustNotBeNull.Violation,
               Parameter3.MustBeGreaterThanZero.Violation,
               Parameter5.MustContainNoNulls.Violation
    {
        super ( term_type,
                clock,
                max_timeout_in_seconds );
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               logic, blocking_terms );
        classContracts.check ( Parameter5.MustContainNoNulls.CONTRACT,
                               blocking_terms );

        this.numCollectedResults = 0;

        // Accumulates and ands/ors/whatever the child asynchronous results
        // as they come in, and passes them to the logic Operation
        // to AND / OR and so on.
        final LogicOperation<VALUE> logic_operation =
            new MultipleBlockingTermsResult.LogicOperation<VALUE> ( this,
                                                                     logic  );

        this.blockingTerms = (Blocking<VALUE> [])
            new Blocking [ blocking_terms.length ];
        System.arraycopy ( blocking_terms, 0,
                           this.blockingTerms, 0, blocking_terms.length );

        this.contracts = new Advocate ( this );

        // Now listen to each child for its result.
        for ( int b = 0; b < blocking_terms.length; b ++ )
        {
            // When the child Blocking term's Result is done,
            // pipe that output to the logical operation.
            // The logic operation will AND or OR each result as it
            // comes in with the AND'ed or OR'ed together other results.
            final Blocking<VALUE> child_blocking = blocking_terms [ b ];
            this.async ( child_blocking,
                         logic_operation );
        }
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     *
     * Can be overridden.
     */
    @Override
    public boolean equals (
                           Object object
                           )
    {
        if ( object == this )
        {
            return true;
        }
        else if ( object == null )
        {
            return false;
        }
        else if ( ! super.equals ( object ) )
        {
            return false;
        }
        else if ( object.getClass () != this.getClass () )
        {
            return false;
        }

        final MultipleBlockingTermsResult<?> that =
            (MultipleBlockingTermsResult<?>) object;

        if ( this.blockingTerms == null )
        {
            if ( that.blockingTerms != null )
            {
                return false;
            }
        }
        else if ( that.blockingTerms == null )
        {
            return false;
        }
        else if ( this.blockingTerms.length != that.blockingTerms.length )
        {
            return false;
        }

        for ( int b = 0; b < this.blockingTerms.length; b ++ )
        {
            if ( this.blockingTerms [ b ] == null )
            {
                if ( that.blockingTerms [ b ] != null )
                {
                    return false;
                }
            }
            else if ( that.blockingTerms [ b ] == null )
            {
                return false;
            }
            else if ( ! this.blockingTerms [ b ].equals ( that.blockingTerms [ b ] ) )
            {
                return false;
            }
        }

        return true;
    }


    /**
     * @see musaico.foundation.term.blocking.LocalResult#handleStateChange(musaico.foundation.capability.operating.OperatingState, musaico.foundation.capability.operating.OperatingState)
     */
    @Override
    protected final OperatingState handleStateChange (
            OperatingState old_state,
            OperatingState new_state
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        // Should we return immediately if new_state == old_state?
        OperatingState final_state = new_state;
        for ( Blocking<VALUE> blocking_term : this.blockingTerms )
        {
            final Result<VALUE> result = blocking_term.result ();
            OperatingState child_new_state;
            if ( new_state == OperatingState.PAUSED )
            {
                child_new_state = result.pause ();
            }
            else if ( new_state == OperatingState.STARTED
                      && old_state == OperatingState.PAUSED )
            {
                child_new_state = result.resume ();
            }
            else if ( new_state == OperatingState.STARTED )
            {
                child_new_state = result.start ();
            }
            else if ( new_state == OperatingState.STOPPED )
            {
                child_new_state = result.stop ();

                final BlockingMustNotBeCancelled.Violation violation =
                    BlockingMustNotBeCancelled.CONTRACT.violation (
                        this,
                        blocking_term );

                blocking_term.cancel ( violation );
            }
            else
            {
                // Unknown new state.  Do nothing.
                return new_state;
            }

            if ( final_state == new_state )
            {
                final_state = child_new_state;
            }
        }

        return final_state;
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
        return this.blockingTerms.length;
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString ()
    {
        if ( this.hasFinalTerm () )
        {
            return super.toString ();
        }

        final int num_collected_results;
        synchronized ( this.lock )
        {
            num_collected_results = this.numCollectedResults;
        }

        // Not yet finished.  Show progress of a sort.
        final StringBuilder sbuf = new StringBuilder ();

        sbuf.append ( super.toString () );

        sbuf.append ( "\n    (" );
        sbuf.append ( num_collected_results );
        sbuf.append ( " / " );
        sbuf.append ( this.blockingTerms.length );
        sbuf.append ( " results collected)" );

        return sbuf.toString ();
    }
}
