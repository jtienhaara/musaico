package musaico.foundation.term.blocking;

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

import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;


/**
 * <p>
 * A guarantee that a Blocking Term will block for no longer than
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
 * @see musaico.foundation.term.blocking.MODULE#COPYRIGHT
 * @see musaico.foundation.term.blocking.MODULE#LICENSE
 */
public class BlockingMustComplete
    implements Contract<BlockingMustComplete.ElapsedTime<?>, BlockingMustComplete.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The amount of time that has elapsed waiting for a specific
     *  Blocking Term to complete blocking and return the result. */
    public static class ElapsedTime<VALUE extends Object>
        implements Serializable
    {
        private static final long serialVersionUID =
            BlockingMustComplete.serialVersionUID;

        // Checks contracts on constructors and static methods for us.
        private static final Advocate classContracts =
            new Advocate ( BlockingMustComplete.ElapsedTime.class );


        // The Result which has been blocking.
        private final Result<VALUE> blockingResult;

        // How long, in seconds, the term has blocked for so far.
        private final BigDecimal blockingSeconds;

        /**
         * <p>
         * Creates a new BlockingMustComplete.ElapsedTime with the
         * specified Blocking Term which has so far taken the
         * specified length of time and not yet returned a result.
         * </p>
         *
         * @param blocking_result The Result which has been blocking.
         *                        Must not be null.
         *
         * @param blocking_seconds How long the Blocking Term has
         *                         been blocking for without
         *                         returning a result.  Must be
         *                         greater than or equal to 0L.
         */
        public ElapsedTime (
                            Result<VALUE> blocking_result,
                            BigDecimal blocking_seconds
                            )
            throws ParametersMustNotBeNull.Violation,
                   Parameter2.MustBeGreaterThanOrEqualToZero.Violation
        {
            classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                                   blocking_result );
            classContracts.check ( Parameter2.MustBeGreaterThanOrEqualToZero.CONTRACT,
                                   blocking_seconds );

            this.blockingResult = blocking_result;
            this.blockingSeconds = blocking_seconds;
        }

        /**
         * @return The blocking Result which must complete.  Never null.
         */
        public final Result<VALUE> blockingResult ()
            throws ReturnNeverNull.Violation
        {
            return this.blockingResult;
        }

        /**
         * @return The amount of time the term has been blocking for
         *         without returning a result.  Never null.
         *         Always greater than or equal to BigDecimal.ZERO.
         */
        public final BigDecimal blockingSeconds ()
            throws ReturnNeverNull.Violation,
                   Return.AlwaysGreaterThanOrEqualToZero.Violation
        {
            return this.blockingSeconds;
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

            if ( this.blockingSeconds ().compareTo ( that.blockingSeconds () ) != 0 )
            {
                // ElapsedTime ( N1 ns ) != ElapsedTime ( N2 ns )
                return false;
            }

            final Result<VALUE> this_blocking_result =
                this.blockingResult ();
            final Result<?> that_blocking_result =
                that.blockingResult ();
            if ( this_blocking_result == null )
            {
                if ( that_blocking_result != null )
                {
                    // ElapsedTime with null result !=
                    //     ElapsedTime with result X.
                    return false;
                }
            }
            else if ( that_blocking_result == null )
            {
                // ElapsedTime with result X != ElapsedTime with null result.
                return false;
            }
            else if ( ! this_blocking_result.equals ( that_blocking_result ) )
            {
                // ElapsedTime with result X != ElapsedTime with result Y.
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
            return this.blockingResult.hashCode () * 17
                + this.blockingSeconds.hashCode ();
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
     *  Request from completing its blocking and returning
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
         * specified Result which took the
         * specified length of time before blocking was cancelled.
         * </p>
         *
         * @param blocking_result The Term which has been blocking.
         *                        Must not be null.
         *
         * @param blocking_seconds How long the Result was
         *                         blocking before it was cancelled.
         *                         Must not be null.  Must be greater
         *                         than or equal to BigDecimal.ZERO.
         */
        public CancelledTime (
                              Result<VALUE> blocking_result,
                              BigDecimal blocking_seconds
                              )
            throws ParametersMustNotBeNull.Violation,
                   Parameter2.MustBeGreaterThanOrEqualToZero.Violation
        {
            super ( blocking_result,
                    blocking_seconds );
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
     *  causing the Result's work to be aborted. */
    public static class ErrorTime<VALUE extends Object>
        extends ElapsedTime<VALUE>
        implements Serializable
    {
        private static final long serialVersionUID =
            BlockingMustComplete.serialVersionUID;


        /**
         * <p>
         * Creates a new BlockingMustComplete.ErrorTime with the
         * specified blocking Result which took the
         * specified length of time before work was aborted by error.
         * </p>
         *
         * @param blocking_result The Result which has
         *                        been blocking on a final result.
         *                        Must not be null.
         *
         * @param working_seconds How long the Result was
         *                        blocking before it was aborted by error.
         *                        Must not be null.  Must be
         *                        greater than or equal to
         *                        BigDecimal.ZERO.
         */
        public ErrorTime (
                          Result<VALUE> blocking_result,
                          BigDecimal working_seconds
                          )
            throws ParametersMustNotBeNull.Violation,
                   Parameter2.MustBeGreaterThanOrEqualToZero.Violation
        {
            super ( blocking_result,
                    working_seconds );
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


    // How long to wait, maximum, for a Result to return a final value,
    // before this contract has been violated.
    private final BigDecimal maxBlockingSeconds;


    /**
     * <p>
     * Creates a new BlockingMustComplete contract with the specified
     * maximum amount of time to block waiting for a Result
     * to return a result.
     * </p>
     *
     * @param max_blocking_seconds The maximum amount of time, in
     *                             seconds, to block waiting
     *                             for any Result to return
     *                             a result.  Must not be null.
     *                             Must be greater than or equal to
     *                             BigDecimal.ZERO.
     */
    public BlockingMustComplete (
                                 BigDecimal max_blocking_seconds
                                 )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustBeGreaterThanOrEqualToZero.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               max_blocking_seconds );
        classContracts.check ( Parameter1.MustBeGreaterThanOrEqualToZero.CONTRACT,
                               max_blocking_seconds );

        this.maxBlockingSeconds = max_blocking_seconds;
    }


    /**
     * @see musaico.foundation.contract.Contract#description()
     */
    @Override
    public String description ()
    {
        return "A Result must be resolved eventually"
            + " to a complete, non-blocking term.  Blocking"
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
        if ( this.maxBlockingSeconds.compareTo ( that.maxBlockingSeconds ) == 0 )
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
        else if ( elapsed.blockingSeconds ().compareTo ( this.maxBlockingSeconds ) < 0 )
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
        return ( this.maxBlockingSeconds == null
                     ? 0
                     : this.maxBlockingSeconds.hashCode () );
    }


    /**
     * @return The maximum amount of time, in seconds, a
     *         Result may block before violating this contract.
     *         Never null.  Always greater than BigDecimal.ZERO.
     */
    public final BigDecimal maxBlockingSeconds ()
    {
        return this.maxBlockingSeconds;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this.getClass () );
    }


    /** Creates a TermViolation after the specified maximum amount
     *  of time, with the specified ElapsedTime (which could be
     *  a CancelledTime or an ErrorTime and so on, not just a timeout).
     *  Package-private, for use by this class
     *  and by AsynchronousResult.java. */
    static final <BLOCKING_VALUE extends Object>
        TermViolation violation (
            Object plaintiff,
            BigDecimal timeout_in_seconds,
            BlockingMustComplete.ElapsedTime<BLOCKING_VALUE> elapsed_time
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        final BlockingMustComplete contract =
            new BlockingMustComplete ( timeout_in_seconds );
        final BlockingMustComplete.Violation violation =
            contract.violation ( plaintiff, // plaintiff
                                 elapsed_time ); // evidence
        final TermViolation term_violation =
            new TermViolation ( violation );

        return term_violation;
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
                    "The specified term did not resolve to a complete,"
                    + " non-blocking term within "
                    + contract.maxBlockingSeconds ()
                    + " seconds: "
                    + evidence
                    + ".", // description
                    plaintiff,
                    evidence,
                    cause );
        }
    }
}
