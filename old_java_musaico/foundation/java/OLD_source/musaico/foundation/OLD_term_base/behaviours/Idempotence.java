package musaico.foundation.term;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Iterator;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Violation;

import musaico.foundation.contract.guarantees.ReturnNeverNull;
import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.order.Order;


/**
 * <p>
 * An object which might or might not contain the same data from one
 * inspection to the next, depending on whether or not it is idempotent.
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
 * @see musaico.foundation.term.MODULE#COPYRIGHT
 * @see musaico.foundation.term.MODULE#LICENSE
 */
public interface Idempotence<VALUE extends Object>
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * @return An Idempotent snapshot of this Idempotence at the current time.
     *         If this is already Idempotent, then it shall be
     *         returned as-is.  Otherwise, some sort of snapshot copy
     *         shall be made.  Never null.
     */
    public abstract Idempotent<VALUE> idempotent ()
        throws ReturnNeverNull.Violation;


    // Every Idempotence must implement java.lang.Object#equals(java.lang.Object)


    // Every Idempotence must implement java.lang.Object#hashCode()


    /**
     * @return This Idempotence's parent Term.  Never null.
     */
    public abstract Term<VALUE> term ();


    // Every Idempotence must implement java.lang.Object#toString()


    /**
     * @return The value of this Idempotency, which might be the
     *         raw <code> value () </code> returned by the Term itself,
     *         or it might be a snapshot, such as might be returned
     *         by calling <code> Term.idempotent ().value () </code>.
     *         Always an empty value if the parent Term is Unjust.
     *         Never null.
     */
    public abstract Multiplicity<VALUE> value ()
        throws ReturnNeverNull.Violation;
}
