package musaico.foundation.term.behaviours;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.term.Behaviour;
import musaico.foundation.term.BehaviourControl;
import musaico.foundation.term.BehaviourPipeline;
import musaico.foundation.term.Pipeline;


/**
 * <p>
 * An indicator that a term's value can be retrieved any number of times,
 * and it will always contain the same element(s).
 * </p>
 *
 * <p>
 * Terms with Countable values which have stored state
 * should implement the more specific
 * <code> musaico.foundation.term.Immutable </code> interface.
 * For example, a list of elements would be Mutable or Immutable,
 * whereas generating the digits of pi, one digit at a time, would
 * be Idempotent.  The elements read from a stream would likely
 * be Unidempotent, because, as is well known to both philosophers
 * and pregnant salmon, you cannot enter the same stream twice.
 * </p>
 *
 *
 * <p>
 * In Java every Behaviour must be Serializable in order to
 * play nicely across RMI.
 * </p>
 *
 * <p>
 * In Java every Behaviour must implement equals (), hashCode ()
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
 * @see musaico.foundation.term.behaviours.MODULE#COPYRIGHT
 * @see musaico.foundation.term.behaviours.MODULE#LICENSE
 */
public interface Idempotent
    extends Behaviour, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    !!!! for the control:
    /**
     * <p>
     * Always returns itself.
     * </p>
     */
    @Override
    public abstract Idempotent<VALUE> idempotent ()
        throws ReturnNeverNull.Violation;
}
