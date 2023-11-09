package musaico.foundation.term.multiplicity;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.term.Countable;


/**
 * <p>
 * A stateful term whose content is Countable and never changes,
 * so that its iterators will always step through the same elements.
 * </p>
 *
 * <p>
 * Countable Idempotent terms which have stored state should implement
 * the more specified Immutable interface.  For example, a list of elements
 * would be Mutable or Immutable, whereas the digits of pi would likely be
 * Idempotent, and the elements read from a stream would likely
 * be Unidempotent.
 * </p>
 *
 *
 * <p>
 * In Java every Idempotence must be Serializable in order to
 * play nicely across RMI.  However users of the Idempotence
 * must be careful, since the values and expected data stored inside
 * might not be Serializable.
 * </p>
 *
 * <p>
 * In Java every Idempotence must implement equals (), hashCode ()
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
 * @see musaico.foundation.term.multiplicity.MODULE#COPYRIGHT
 * @see musaico.foundation.term.multiplicity.MODULE#LICENSE
 */
public interface Immutable<VALUE extends Object>
    extends Idempotent<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Always returns itself.
     * </p>
     *
     * @see musaico.foundation.term.Multiplicity#idempotent()
     */
    @Override
    public abstract Immutable<VALUE> idempotent ()
        throws ReturnNeverNull.Violation;
}
