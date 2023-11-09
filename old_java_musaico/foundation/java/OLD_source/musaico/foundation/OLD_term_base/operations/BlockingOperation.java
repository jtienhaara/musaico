package musaico.foundation.term.operations;

import java.io.Serializable;

import java.math.BigDecimal;


import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.domains.Clock;

import musaico.foundation.term.Operation;


/**
 * <p>
 * An Operation which might block when it is applied to any term,
 * including a NonBlocking term, by performing I/O waiting on threads
 * or processes and so on.
 * </p>
 *
 * @see musaico.foundation.term.operations.operations.NonBlockingOperation
 *
 *
 * <p>
 * In Java, every Operation must implement equals (), hashCode ()
 * and toString().
 * </p>
 *
 * <p>
 * In Java every Operation must be Serializable in order to
 * play nicely across RMI.
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
 * @see musaico.foundation.term.operations.MODULE#COPYRIGHT
 * @see musaico.foundation.term.operations.MODULE#LICENSE
 */
public interface BlockingOperation<INPUT extends Object, OUTPUT extends Object>
    extends Operation<INPUT, OUTPUT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * @return The maximum amount of time, in seconds, that this
     *         operation will block for, before giving up on apply ( ... ).
     *         This maximum time is the maximum amount of time
     *         the operation will take to return a result.
     *         Always greater than or equal to BigDecimal.ZERO seconds.
     */
    public abstract BigDecimal blockingMaxSeconds ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation,
               ReturnNeverNull.Violation;


    /**
     * @return The Clock used by this BlockingOperation to measure start
     *         time and end time since 0 UN*X time.  Never null.
     */
    public abstract Clock clock ()
        throws ReturnNeverNull.Violation;
}
