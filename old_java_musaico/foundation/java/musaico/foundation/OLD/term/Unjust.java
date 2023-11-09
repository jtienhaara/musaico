package musaico.foundation.term;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.term.Maybe;
import musaico.foundation.term.Term;


/**
 * <p>
 * A Term with no value, possibly due to an Error,
 * or possibly an Incomplete value
 * (such as a Partial result, or a Blocking result, and so on).
 * </p>
 *
 * <p>
 * Never Just anything (one element), and never more
 * than one (non-Partial) elements.
 * </p>
 *
 * <p>
 * Some Unjust values, such as Empty values, are Countable;
 * whereas others, such as Errors and Partial values, are not.
 * </p>
 *
 *
 * <p>
 * In Java every conditional Term must be Serializable in order to
 * play nicely across RMI.  However users of the conditional Term
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
 * @see musaico.foundation.term.MODULE#COPYRIGHT
 * @see musaico.foundation.term.MODULE#LICENSE
 */
public interface Unjust<VALUE extends Object>
    extends Term<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;
}
