package musaico.foundation.contract.guarantees;

import java.io.Serializable;


import musaico.foundation.contract.AbstractContract;
import musaico.foundation.contract.Contracts;
import musaico.foundation.contract.Guarantee;
import musaico.foundation.contract.GuaranteeUncheckedViolation;
import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;


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
 * <pre>
 * Copyright (c) 2012, 2013 Johann Tienhaara
 * All rights reserved.
 *
 * This file is part of Musaico.
 *
 * Musaico is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Musaico is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Musaico.  If not, see &lt;http://www.gnu.org/licenses/&gt;.
 * </pre>
 */
public class TimeoutGuarantee
    extends AbstractContract<Object, TimeoutGuarantee.Violation>
    implements Guarantee<Object, TimeoutGuarantee.Violation>, Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20130321L;
    private static final String serialVersionHash =
        "0xE6B458480492336A118D2DDB49C0EBD3BF807140";


    /** The start time, from which the timeout takes effect, in
     *  nanoseconds since some arbitrary starting point. */
    private final long startTimeInNanoseconds;

    /** The guaranteed timeout, in nanoseconds. */
    private final long timeoutInNanoseconds;

    /** Checks on our own parameter obligations for us. */
    private final ObjectContracts contracts = new ObjectContracts ( this );


    /**
     * <p>
     * Creates a new TimeoutGuarantee with the specified timeout
     * in nanoseconds.
     * </p>
     *
     * @param timeout_in_nanoseconds The timeout in nanoseconds, after
     *                               which the guarantor will throw
     *                               a TimeoutGuarantee.Violation if
     *                               the guaranteed task is not complete.
     *                               Must be greater than or equal to 1
     *                               nanosecond.
     */
    public TimeoutGuarantee (
                             long timeout_in_nanoseconds
                             )
        throws Parameter1.MustBeGreaterThanZero.Violation
    {
        this.contracts.check ( Parameter1.MustBeGreaterThanZero.CONTRACT,
                               timeout_in_nanoseconds );

        this.startTimeInNanoseconds = System.nanoTime ();
        this.timeoutInNanoseconds = timeout_in_nanoseconds;
    }


    /**
     * <p>
     * Creates a new TimeoutGuarantee with the specified timeout
     * in nanoseconds.
     * </p>
     *
     * @param start_time_in_nanoseconds The system time from which to
     *                                  start counting, since some
     *                                  arbitrary starting time.
     *                                  Can be any value.
     *                                  @see java.lang.System#nanoTime ()
     *
     * @param timeout_in_nanoseconds The timeout in nanoseconds, after
     *                               which the guarantor will throw
     *                               a TimeoutGuarantee.Violation if
     *                               the guaranteed task is not complete.
     *                               Must be greater than or equal to 1
     *                               nanosecond.
     */
    public TimeoutGuarantee (
                             long start_time_in_nanoseconds,
                             long timeout_in_nanoseconds
                             )
        throws Parameter2.MustBeGreaterThanZero.Violation
    {
        this.contracts.check ( Parameter2.MustBeGreaterThanZero.CONTRACT,
                               timeout_in_nanoseconds );

        this.startTimeInNanoseconds = start_time_in_nanoseconds;
        this.timeoutInNanoseconds = timeout_in_nanoseconds;
    }


    /**
     * @see musaico.foundation.contract.Contract#enforce(java.lang.Object, java.lang.Object[])
     */
    @Override
    public void enforce (
                         Object object,
                         Object inspectable_data
                         )
        throws TimeoutGuarantee.Violation
    {
        this.enforce ( System.nanoTime (),
                       object,
                       inspectable_data );
    }


    /**
     * <p>
     * Enforces the timeout at the specified time.
     * </p>
     *
     * @param current_time_in_nanoseconds The time at which the timeout
     *                                    will be enforced, relative to
     *                                    some arbitrary time.
     *                                    Can be any value.
     *                                    @see java.lang.System#nanoTime()
     *
     *
     * @param object_under_contract The object under contract, such
     *                              as an object whose method enforces
     *                              this contract.  Must not be null.
     *
     * @param inspectable_data The inspectable data, not used.
     *                         Must not be null.
     *
     * @throws TimeoutGuarantee.Violation If the elapsed time violates
     *                                    this contract.
     */
    public void enforce (
                         long current_time_in_nanoseconds,
                         Object object,
                         Object inspectable_data
                         )
        throws TimeoutGuarantee.Violation
    {
        final long end_time;
        if ( this.timeoutInNanoseconds
             <= ( Long.MAX_VALUE - this.startTimeInNanoseconds ) )
        {
            end_time = this.startTimeInNanoseconds + this.timeoutInNanoseconds;
            if ( current_time_in_nanoseconds >= end_time
                 || current_time_in_nanoseconds < this.startTimeInNanoseconds )
            {
                throw new TimeoutGuarantee.Violation ( this,
                                                       object,
                                                       inspectable_data );
            }
        }
        else
        {
            end_time = Long.MIN_VALUE
                + this.timeoutInNanoseconds
                - ( Long.MAX_VALUE - this.startTimeInNanoseconds );
            if ( current_time_in_nanoseconds >= end_time
                 && current_time_in_nanoseconds < this.startTimeInNanoseconds )
            {
                throw new TimeoutGuarantee.Violation ( this,
                                                       object,
                                                       inspectable_data );
            }
        }

        // Not timed out yet.
    }


    /**
     * <p>
     * Returns the start time of the timeout, in nanoseconds since some
     * arbitrary time.
     * </p>
     *
     * @see java.lang.System#nanoTime()
     *
     * @return The start time, in nanoseconds since some arbitrary starting
     *         point.  Can be any value.
     */
    public long startTimeInNanoseconds ()
    {
        return this.startTimeInNanoseconds;
    }


    /**
     * <p>
     * Returns the timeout in nanoseconds.
     * </p>
     *
     * @return The elapsed time before a timeout violation
     *         can be enforced.  Always greater than or equal to 1.
     */
    public long timeoutInNanoseconds ()
    {
        return this.timeoutInNanoseconds;
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString ()
    {
        return ""
            + ( (double) this.timeoutInNanoseconds () / 1000000000D )
            + " seconds timeout guarantee";
    }


    /**
     * <p>
     * A violation of the return-never-null contract.
     * </p>
     */
    public static class Violation
        extends GuaranteeUncheckedViolation
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
                          Object object_under_contract,
                          Object inspectable_data
                          )
        {
            super ( guarantee,
                    Contracts.makeSerializable ( object_under_contract ),
                    Contracts.makeSerializable ( inspectable_data ) );
        }
    }
}
