package musaico.foundation.term.abnormal;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.term.Unjust;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;


/**
 * <p>
 * An Unjust term with exceptional results, such as an Error or a Warning.
 * </p>
 *
 *
 * <p>
 * In Java every Pipeline must be Serializable in order to
 * play nicely across RMI.  However users of the Pipeline
 * must be careful, since the values and expected data stored inside
 * might not be Serializable.
 * </p>
 *
 * <p>
 * In Java every Pipeline must implement equals (), hashCode ()
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
 * @see musaico.foundation.term.abnormal.MODULE#COPYRIGHT
 * @see musaico.foundation.term.abnormal.MODULE#LICENSE
 */
public interface Abnormal<VALUE extends Object>
    extends Unjust<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * @return The TermViolation which led to this Abnormal term,
     *         such as "can't extract the middle element(s)
     *         from an Empty term" or "warning: deprecated operation"
     *         or "timed out waiting for partial result
     *         to be completed" or that sort of thing.
     *         Never null.
     */
    public abstract TermViolation violation ()
        throws ReturnNeverNull.Violation;
}
