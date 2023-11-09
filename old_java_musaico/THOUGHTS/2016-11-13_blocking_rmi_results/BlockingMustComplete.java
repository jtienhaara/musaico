package musaico.foundation.value.blocking;

import java.io.Serializable;

import java.math.BigDecimal;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;
import musaico.foundation.contract.UncheckedViolation;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.value.Value;
import musaico.foundation.value.ValueViolation;


/**
 * <p>
 * A guarantee that a Blocking Value will block for no longer than
 * some specific amount of time.
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
 * @see musaico.foundation.value.blocking.MODULE#COPYRIGHT
 * @see musaico.foundation.value.blocking.MODULE#LICENSE
 */
public class BlockingMustComplete
    implements Contract<BlockingMustComplete.ElapsedTime<?>, BlockingMustComplete.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The amount of time that has elapsed waiting for a specific
     *  Blocking Value to complete blocking and return the result. */
    public static class ElapsedTime<VALUE extends Object>
        implements Serializable
    {
        private static final long serialVersionUID =
            BlockingMustComplete.serialVersionUID;

        // Checks contracts on constructors and static methods for us.
        private static final Advocate classContracts =
            new Advocate ( BlockingMustComplete.ElapsedTime.class );


        // The Blocking Value.
        private final Blocking<VALUE> blockingValue;

        // How long, in nanoseconds, the value has blocked for so far.
        private final long blockingNanoseconds;

        /**
         * <p>
         * Creates a new BlockingMustComplete.ElapsedTime with the
         * specified Blocking Value which has so far taken the
         * specified length of time and not yet returned a result.
         * </p>
         *
         * @param blocking_value The Value which has been blocking.
         *                       Must not be null.
         *
         * @param blocking_nanoseconds How long the Blocking Value has
         *                             been blocking for without
         *                             returning a result.  Must be
         *                             greater than or equal to 0L.
         */
        public ElapsedTime (
                            Blocking<VALUE> blocking_value,
                            long blocking_nanoseconds
                            )
            throws ParametersMustNotBeNull.Violation,
                   Parameter2.MustBeGreaterThanOrEqualToZero.Violation
        {
            classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                                   blocking_value );
            classContracts.check ( Parameter2.MustBeGreaterThanOrEqualToZero.CONTRACT,
                                   blocking_nanoseconds );

            this.blockingValue = blocking_value;
            this.blockingNanoseconds = blocking_nanoseconds;
        }

        /**
         * @return The Blocking Value which must complete.  Never null.
         */
        public final Blocking<VALUE> blockingValue ()
            throws ReturnNeverNull.Violation
        {
            return this.blockingValue;
        }

        /**
         * @return The amount of time the value has been blocking for
         *         without returning a result.  Always greater than
         *         or equal to 0L.
         */
        public final long blockingNanoseconds ()
            throws Return.AlwaysGreaterThanOrEqualToZero.Violation
        {
            return this.blockingNanoseconds;
        }

        /**
         * @return The number of seconds of elapsed time.  Never null.
         */
        public final BigDecimal blockingSeconds ()
        {
            return BlockingMustComplete.secondsFromNanoseconds ( this.blockingNanoseconds () );
        }

        /**
         * @see java.lang.Object#equals(java.lang.Object)
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
                // Any BlockingMustComplete.ElapsedTime != null.
                return false;
            }
            else if ( this.getClass () != object.getClass () )
            {
                // Any BlockingMustComplete.ElapsedTime !=
                //     any object of another class
                return false;
            }

            final BlockingMustComplete.ElapsedTime<?> that =
                (BlockingMustComplete.ElapsedTime<?>) object;

            if ( this.blockingNanoseconds () != that.blockingNanoseconds () )
            {
                // ElapsedTime ( N1 ns ) != ElapsedTime ( N2 ns )
                return false;
            }

            final Blocking<VALUE> this_blocking_value = this.blockingValue ();
            final Blocking<?> that_blocking_value = that.blockingValue ();
            if ( this_blocking_value == null )
            {
                if ( that_blocking_value == null )
                {
                    // ElapsedTime with null value =
                    //     ElapsedTime with null value.
                    return true;
                }
                else
                {
                    // ElapsedTime with null value !=
                    //     ElapsedTime with value X.
                    return false;
                }
            }
            else if ( that_blocking_value == null )
            {
                // ElapsedTime with value X != ElapsedTime with null value.
                return false;
            }
            else if ( ! this_blocking_value.equals ( that_blocking_value ) )
            {
                // ElapsedTime with value X != ElapsedTime with value Y.
                return false;
            }

            // Everything is all matchy-matchy.
            return true;
        }

        /**
         * @see java.lang.Object#hashCode()
         */
        @Override
        public final int hashCode ()
        {
            return this.blockingValue.hashCode () * 17
                + (int)
                ( this.blockingNanoseconds % (long) Integer.MAX_VALUE );
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString ()
        {
            return "Elapsed time: " + this.blockingSeconds () + " seconds";
        }
    }


    /** The amount of time that elapsed before someone cancelled a specific
     *  Blocking Value from completing its blocking and returning
     *  the result. */
    public static class CancelledTime<VALUE extends Object>
        extends ElapsedTime<VALUE>
        implements Serializable
    {
        private static final long serialVersionUID =
            BlockingMustComplete.serialVersionUID;


        /**
         * <p>
         * Creates a new BlockingMustComplete.CancelledTime with the
         * specified Blocking Value which took the
         * specified length of time before blocking was cancelled.
         * </p>
         *
         * @param blocking_value The Value which has been blocking.
         *                       Must not be null.
         *
         * @param blocking_nanoseconds How long the Blocking Value was
         *                             blocking before it was cancelled.
         *                             Must be greater than or equal to 0L.
         */
        public CancelledTime (
                              Blocking<VALUE> blocking_value,
                              long blocking_nanoseconds
                              )
            throws ParametersMustNotBeNull.Violation,
                   Parameter2.MustBeGreaterThanOrEqualToZero.Violation
        {
            super ( blocking_value,
                    blocking_nanoseconds );
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString ()
        {
            return "Cancelled after " + this.blockingSeconds () + " seconds";
        }
    }


    /** The amount of time that elapsed before an error occurred,
     *  causing the Blocking Value's work to be aborted. */
    public static class ErrorTime<VALUE extends Object>
        extends ElapsedTime<VALUE>
        implements Serializable
    {
        private static final long serialVersionUID =
            BlockingMustComplete.serialVersionUID;


        /**
         * <p>
         * Creates a new BlockingMustComplete.ErrorTime with the
         * specified Blocking Value which took the
         * specified length of time before work was aborted by error.
         * </p>
         *
         * @param blocking_value The Blocking Value which has
         *                       been blocking on a final result.
         *                       Must not be null.
         *
         * @param working_nanoseconds How long the Blocking Value was
         *                            blocking before it was aborted by error.
         *                            Must be greater than or equal to 0L.
         */
        public ErrorTime (
                          Blocking<VALUE> blocking_value,
                          long working_nanoseconds
                          )
            throws ParametersMustNotBeNull.Violation,
                   Parameter2.MustBeGreaterThanOrEqualToZero.Violation
        {
            super ( blocking_value,
                    working_nanoseconds );
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString ()
        {
            return "Error after " + this.blockingSeconds () + " seconds";
        }
    }


    // Checks contracts on constructors and static methods for us.
    private static final Advocate classContracts =
        new Advocate ( BlockingMustComplete.class );


    // How long to wait, maximum, for a Blocking Value to return a result,
    // before this contract has been violated.
    private final long maxBlockingNanoseconds;


    /**
     * <p>
     * Creates a new BlockingMustComplete contract with the specified
     * maximum amount of time to block waiting for a Blocking Value
     * to return a result.
     * </p>
     *
     * @param max_blocking_nanoseconds The maximum amount of time, in
     *                                 nanoseconds, to block waiting
     *                                 for any Blocking Value to return
     *                                 a result.  Must be greater than
     *                                 or equal to 0L.
     */
    public BlockingMustComplete (
                                 long max_blocking_nanoseconds
                                 )
    {
        classContracts.check ( Parameter1.MustBeGreaterThanOrEqualToZero.CONTRACT,
                               max_blocking_nanoseconds );

        this.maxBlockingNanoseconds = max_blocking_nanoseconds;
    }


    /**
     * @see musaico.foundation.contract.Contract#description()
     */
    @Override
    public String description ()
    {
        return "A Blocking value must be resolved eventually"
            + " to a complete, non-blocking value.  Blocking"
            + " must not be cancelled and the underlying worker"
            + " must not error out.";
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
                           Object obj
                           )
    {
        if ( obj == null )
        {
            return false;
        }
        else if ( obj.getClass () != this.getClass () )
        {
            return false;
        }

        final BlockingMustComplete that = (BlockingMustComplete) obj;
        if ( this.maxBlockingNanoseconds == that.maxBlockingNanoseconds )
        {
            return true;
        }

        return true;
    }


    /**
     * @see musaico.filter.Filter#filter(java.lang.Object)
     */
    @Override
    public FilterState filter (
                                BlockingMustComplete.ElapsedTime<?> elapsed
                                )
    {
        if ( elapsed == null )
        {
            return FilterState.DISCARDED;
        }
        else if ( elapsed instanceof BlockingMustComplete.CancelledTime )
        {
            return FilterState.DISCARDED;
        }
        else if ( elapsed instanceof BlockingMustComplete.ErrorTime )
        {
            return FilterState.DISCARDED;
        }
        else if ( elapsed.blockingNanoseconds () < this.maxBlockingNanoseconds )
        {
            // Max blocking time has not yet elapsed.
            return FilterState.KEPT;
        }
        else
        {
            // Longer than the max blocking time, throw a violation.
            return FilterState.DISCARDED;
        }
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return (int) this.maxBlockingNanoseconds;
    }


    /**
     * @return The maximum amount of time, in nanoseconds, a
     *         Blocking Value may block before violating this contract.
     *         Always greater than 0L.
     */
    public final long maxBlockingNanoseconds ()
    {
        return this.maxBlockingNanoseconds;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this.getClass () );
    }


    /** Creates a ValueViolation after the specified maximum amount
     *  of time, with the specified ElapsedTime (which could be
     *  a CancelledTime or an ErrorTime and so on, not just a timeout).
     *  Package-private, for use by this class
     *  and by AsynchronousResult.java. */
    static final <BLOCKING_VALUE extends Object>
        ValueViolation violation (
            long timeout_in_nanoseconds,
            BlockingMustComplete.ElapsedTime<BLOCKING_VALUE> elapsed_time
            )
    {
        final BlockingMustComplete contract =
            new BlockingMustComplete ( timeout_in_nanoseconds );
        final BlockingMustComplete.Violation violation =
            contract.violation ( elapsed_time.blockingValue (),
                                 elapsed_time );
        final ValueViolation value_violation =
            new ValueViolation ( violation );

        return value_violation;
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     */
    @Override
    public BlockingMustComplete.Violation violation (
                                                     Object plaintiff,
                                                     BlockingMustComplete.ElapsedTime<?> evidence
                                                     )
    {
        return new BlockingMustComplete.Violation ( this,
                                                    plaintiff,
                                                    evidence );
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object, java.lang.Throwable)
     */
    @Override
    public BlockingMustComplete.Violation violation (
            Object plaintiff,
            BlockingMustComplete.ElapsedTime<?> evidence,
            Throwable cause
            )
    {
        return new BlockingMustComplete.Violation ( this,
                                                    plaintiff,
                                                    evidence,
                                                    cause );
    }




    /**
     * <p>
     * A violation of the BlockingMustComplete contract.
     * </p>
     */
    public static class Violation
        extends UncheckedViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            BlockingMustComplete.serialVersionUID;


        /**
         * <p>
         * Creates a new BlockingMustComplete.Violation.
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
         * @param evidence The elapsed time which violated the contract.
         *                 Can be null.
         */
        public Violation (
                          BlockingMustComplete contract,
                          Object plaintiff,
                          BlockingMustComplete.ElapsedTime<?> evidence
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
         * Creates a new BlockingMustComplete.Violation.
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
         * @param evidence The ElapsedTime which violated the contract.
         *                 Can be null.
         *
         * @param cause The Throwable which caused this contract violation.
         *              Can be null.
         */
        public Violation (
                          BlockingMustComplete contract,
                          Object plaintiff,
                          BlockingMustComplete.ElapsedTime<?> evidence,
                          Throwable cause
                          )
            throws ParametersMustNotBeNull.Violation
        {
            super ( contract,
                    "The specified value did not resolve to a complete,"
                    + " non-blocking value within "
                    + BlockingMustComplete.secondsFromNanoseconds (
                          contract.maxBlockingNanoseconds ()
                                                                   )
                    + " seconds: "
                    + evidence
                    + ".", // description
                    plaintiff,
                    evidence,
                    cause );
        }
    }


    private static final BigDecimal secondsFromNanoseconds (
                                                            long nanoseconds
                                                            )
    {
        final BigDecimal seconds =
            new BigDecimal ( "" + nanoseconds )
            .divide ( new BigDecimal ( "1000000000000" ),
                      2,
                      BigDecimal.ROUND_HALF_UP );
        return seconds;
    }
}
