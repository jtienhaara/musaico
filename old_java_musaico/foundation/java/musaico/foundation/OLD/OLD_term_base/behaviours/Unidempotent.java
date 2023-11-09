package musaico.foundation.term.behaviours;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.term.Behaviour;


/**
 * <p>
 * A Term which is Unidempotent cannot guarantee that its
 * value iterators will always step through the same element(s);
 * the elements iterated over may be different from one call
 * to the next.
 * </p>
 *
 * <p>
 * Note, however, that any given iterator will never break mid-iteration
 * nor change the elements it is iterating over.  For example if an
 * Iterator is returned from a Mutable value, then the elements at the
 * time of the <code> iterator () </code> call shall be iterated over,
 * even if elements are added or deleted after the time of the call.
 * See, for example,
 * <code> musaico.foundation.term.iterators.UnchangingIterator </code>.
 * </p>
 *
 *
 * <p>
 * In Java every Behaviour must be Serializable in order to
 * play nicely across RMI.  However users of the Behaviour
 * must be careful, since the values and expected data stored inside
 * might not be Serializable.
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
public interface Unidempotent
    extends Behaviour, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    !!!! for the control:
    !!!;
    /**
     */
    @Override
    public abstract Idempotent<VALUE> idempotent ()
        throws ReturnNeverNull.Violation;
}
