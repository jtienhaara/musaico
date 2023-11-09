package musaico.foundation.term.abnormal;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.term.Definite;
import musaico.foundation.term.Term;


/**
 * <p>
 * An Abnormal Term which wraps some other Term, and can be unwrapped
 * to return the other Term.
 * </p>
 *
 * <p>
 * The Wrapped Term is always Unjust, containing no values itself,
 * but the wrapped Term can be anything, including a Countable value.
 * </p>
 *
 * <p>
 * For example, a Warning can be unwrapped to return the Term
 * that was returned with a warning; or a Partial value can be
 * unwrapped to return the Countable value(s) that were gathered
 * up until the point the Partial value was created; and so on.
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
public interface Wrapped<VALUE extends Object, WRAPPED extends Term<VALUE>>
    extends Abnormal<VALUE>, Definite<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * @return The wrapped Term.  Never null.
     */
    public abstract WRAPPED unwrap ()
        throws ReturnNeverNull.Violation;
}
