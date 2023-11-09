package musaico.foundation.value;

import java.io.Serializable;

import java.lang.reflect.Constructor;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;


/**
 * <p>
 * Forces the user to decide whether to block or asynchronously handle
 * a Value which might or might not be Blocking.
 * </p>
 *
 * @see musaico.foundation.value.Blocking
 *
 *
 * <p>
 * In Java every Synchronicity must be Serializable in order to
 * play nicely across RMI.  However users of the Asynchronous class
 * must be careful, since the values and expected data stored inside
 * might not be Serializable.
 * </p>
 *
 * <p>
 * In Java every Synchronicity must implement equals (), hashCode ()
 * and toString ().
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
 * @see musaico.foundation.value.MODULE#COPYRIGHT
 * @see musaico.foundation.value.MODULE#LICENSE
 */
public interface Synchronicity<VALUE extends Object>
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * @return The (possibly Blocking) Value underpinning this
     *         Synchronous or Asynchronous result.  Never null.
     */
    public abstract Value<VALUE> async ()
        throws ReturnNeverNull.Violation;


    /**
     * @see musaico.foundation.value.Value#pipe(musaico.foundation.value.ValueProcessor)
     * @see musaico.foundation.value.Blocking#pipe(musaico.foundation.value.ValueProcessor)
     */
    public abstract Value<VALUE> async (
                                        ValueProcessor<VALUE> value_processor
                                        )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Waits essentially forever for the result.
     * </p>
     *
     * @see musaico.foundation.value.Value#onBlocking(long)
     * @see musaico.foundation.value.Blocking#onBlocking(long)
     */
    public abstract NonBlocking<VALUE> await ()
        throws ReturnNeverNull.Violation;


    /**
     * @see musaico.foundation.value.Value#onBlocking(long)
     * @see musaico.foundation.value.Blocking#onBlocking(long)
     */
    public abstract NonBlocking<VALUE> await (
                                              long timeout_in_nanoseconds
                                              )
        throws ReturnNeverNull.Violation;


    /**
     * @return The maximum amount of time to return a result before
     *         a failure is returned.  Any NonBlocking Value will have
     *         a maximum blocking time of 0 nanoseconds, but a Blocking
     *         Value will provide some upper bound after which the
     *         implementer gives up and returns a failed result.
     *         Always greater than or equal to 0L.
     */
    public abstract long blockingMaxNanoseconds ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation;
}
