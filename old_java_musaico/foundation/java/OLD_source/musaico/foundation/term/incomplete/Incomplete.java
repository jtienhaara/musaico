package musaico.foundation.term.incomplete;

import java.io.Serializable;

import java.math.BigDecimal;


import musaico.foundation.domains.time.Clock;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.term.Unjust;


/**
 * <p>
 * A term which is not necessarily Empty or Abnormal but also is not (yet)
 * a Just term.  For example, a Blocking term which is not yet completely
 * read, or a Partial result, or a Timeout or Cancelled blocking term,
 * and so on.
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
 * @see musaico.foundation.term.incomplete.MODULE#COPYRIGHT
 * @see musaico.foundation.term.incomplete.MODULE#LICENSE
 */
public interface Incomplete<VALUE extends Object>
    extends Unjust<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * @return The Clock used by this Incomplete term to measure start
     *         time and end time since 0 UN*X time.  Never null.
     */
    public abstract Clock clock ()
        throws ReturnNeverNull.Violation;


    /**
     * @return The amount of time passed, in seconds, from the start
     *         of processing until completion, if processing has stopped;
     *         or the amount of time from the start of processing
     *         until now, according to the <code> clock () </code>,
     *         if processing has started but not stopped;
     *         or BigDecimal.ZERO if processing has not even
     *         started yet.  Note: can also be BigDecimal.ZERO even if
     *         processing has started and/or stopped.
     *         Always greater than or equal to BigDecimal.ZERO.
     *         Never null.
     */
    public abstract BigDecimal elapsedTimeInSeconds ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysGreaterThanOrEqualToZero.Violation;
}
