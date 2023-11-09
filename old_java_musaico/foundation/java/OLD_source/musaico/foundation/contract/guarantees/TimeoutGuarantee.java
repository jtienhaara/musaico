package musaico.foundation.contract.guarantees;

import java.io.Serializable;

import java.math.BigDecimal;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;
import musaico.foundation.contract.UncheckedViolation;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;
import musaico.foundation.contract.obligations.Parameter2;

import musaico.foundation.domains.Seconds;
import musaico.foundation.domains.StringRepresentation;

import musaico.foundation.domains.time.Clock;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;


/**
 * <p>
 * A method which throws TimeoutGuarantee.Violation
 * expects the return value to never be null.  If, for some reason,
 * the method cannot keep its own guarantee, it will send itself to
 * arbitration, possibly inducing the runtime exception.
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
 * @see musaico.foundation.contract.guarantees.MODULE#COPYRIGHT
 * @see musaico.foundation.contract.guarantees.MODULE#LICENSE
 */
public class TimeoutGuarantee
    implements Contract<Object, TimeoutGuarantee.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks obligations on constructor static method parameters and so on.
    private static final Advocate classContracts =
        new Advocate ( TimeoutGuarantee.class );


    // Checks our own parameter obligations for us.
    private final Advocate contracts;

    // The clock which generates the current time for us, whenever we ask
    // it to.
    private final Clock clock;

    // The start time, from which the timeout takes effect, in
    // seconds since UN*X time 0.
    private final BigDecimal startTimeInSeconds;

    // The guaranteed timeout, in seconds.
    private final BigDecimal timeoutInSeconds;

    // The end time, at which the timeout will occur.
    private final BigDecimal endTimeInSeconds;


    /**
     * <p>
     * Creates a new TimeoutGuarantee.
     * </p>
     *
     * @param clock Generates the current time for us, any time we
     *              ask it to.  Must not be null.
     *
     * @param timeout_in_seconds The timeout in seconds, after
     *                           which the guarantor will throw
     *                           a TimeoutGuarantee.Violation if
     *                           the guaranteed task is not complete.
     *                           Must be greater than 0 seconds.
     */
    public TimeoutGuarantee (
                             Clock clock,
                             BigDecimal timeout_in_seconds
                             )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustBeGreaterThanZero.Violation
    {
        this ( clock,
               timeout_in_seconds,
               clock == null // start_time_in_seconds
                   ? null
                   : clock.currentTimeInSeconds () );
    }


    /**
     * <p>
     * Creates a new TimeoutGuarantee with the specified timeout
     * in seconds.
     * </p>
     *
     * @param clock Generates the current time for us, any time we
     *              ask it to.  Must not be null.
     *
     * @param start_time_in_seconds The system time from which to
     *                              start counting, since some
     *                              arbitrary starting time.
     *                              Can be any value.
     *
     * @param timeout_in_seconds The timeout in seconds, after
     *                           which the guarantor will throw
     *                           a TimeoutGuarantee.Violation if
     *                           the guaranteed task is not complete.
     *                           Must be greater than 0 seconds.
     */
    public TimeoutGuarantee (
                             Clock clock,
                             BigDecimal timeout_in_seconds,
                             BigDecimal start_time_in_seconds
                             )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustBeGreaterThanZero.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               clock, timeout_in_seconds,
                               start_time_in_seconds );
        classContracts.check ( Parameter2.MustBeGreaterThanZero.CONTRACT,
                               timeout_in_seconds );

        this.timeoutInSeconds = timeout_in_seconds;
        this.startTimeInSeconds = start_time_in_seconds;
        this.endTimeInSeconds =
            this.startTimeInSeconds.add ( this.timeoutInSeconds );

        this.clock = clock;

        this.contracts = new Advocate ( this );
    }


    /**
     * @return A summary of the starting time, the current time,
     *         and the difference between the two, as a String.
     *         Never null.
     */
    public final String currentTimeDifference ()
    {
        final BigDecimal current_time =
            this.clock.currentTimeInSeconds ();
        final BigDecimal difference =
            current_time.subtract ( this.startTimeInSeconds () );
        final String difference_string =
            StringRepresentation.ofTime ( difference );
        return difference_string;
    }


    /**
     * @see musaico.foundation.contract.Contract#description()
     */
    @Override
    public final String description ()
    {
        final String timeout_string =
            StringRepresentation.ofTime ( this.timeoutInSeconds );
        return "A return value is guaranteed to be generated within "
            + timeout_string;
    }


    /**
     * <p>
     * Returns the time at which the timeout will occur / did occur,
     * in seconds since UN*X 0 time.
     * </p>
     *
     * @return The end time, in seconds since UN*X 0 time.
     */
    public BigDecimal endTimeInSeconds ()
    {
        return this.endTimeInSeconds;
    }


    /**
     * @see musaico.foundation.filter.Filter#filter(java.lang.eObject)
     */
    @Override
    public final FilterState filter (
                                     Object evidence
                                     )
    {
        final BigDecimal current_time_in_seconds =
            this.clock.currentTimeInSeconds ();

        if ( current_time_in_seconds.compareTo ( this.endTimeInSeconds ) > 0 )
        {
            // The end time has been passed.
            return FilterState.DISCARDED;
        }
        else
        {
            // Not timed out yet.
            return FilterState.KEPT;
        }
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     *
     * Final for speed.
     */
    @Override
    public final TimeoutGuarantee.Violation violation (
                                                       Object plaintiff,
                                                       Object evidence
                                                       )
    {
        return new TimeoutGuarantee.Violation ( this,
                                                plaintiff,
                                                evidence );
    }

    @Override
    public final TimeoutGuarantee.Violation violation (
                                                       Object plaintiff,
                                                       Object evidence,
                                                       Throwable cause
                                                       )
    {
        final TimeoutGuarantee.Violation violation =
            this.violation ( plaintiff, evidence );
        if ( cause != null )
        {
            violation.initCause ( cause );
        }

        return violation;
    }


    /**
     * <p>
     * Returns the start time of the timeout, in seconds since UN*X 0 time.
     * </p>
     *
     * @return The start time, in seconds since UN*X 0 time.
     */
    public BigDecimal startTimeInSeconds ()
    {
        return this.startTimeInSeconds;
    }


    /**
     * <p>
     * Returns the timeout in seconds.
     * </p>
     *
     * @return The elapsed time before a timeout violation
     *         can be enforced.  Always greater than or equal to 1.
     */
    public BigDecimal timeoutInSeconds ()
    {
        return this.timeoutInSeconds;
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString ()
    {
        return "Timeout guarantee: "
            + StringRepresentation.ofTime ( this.timeoutInSeconds );
    }


    /**
     * <p>
     * A violation of the return-never-null contract.
     * </p>
     */
    public static class Violation
        extends UncheckedViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            TimeoutGuarantee.serialVersionUID;

        /**
         * <p>
         * Creates a TimeoutGuarantee.Violation.
         * </p>
         */
        public Violation (
                          TimeoutGuarantee guarantee,
                          Object plaintiff,
                          Object evidence
                          )
        {
            super ( guarantee,
                    "The return value took too long: "
                    + guarantee.currentTimeDifference ()
                    + ".", // description
                    Contracts.makeSerializable ( plaintiff ),
                    Contracts.makeSerializable ( evidence ) );
        }
    }
}
