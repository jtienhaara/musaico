package musaico.foundation.term.multiplicity;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Edit;
import musaico.foundation.term.Term;


/**
 * <p>
 * Indicates that a Term has a Countable value whose content
 * can change over time, so that its iterator might not step through
 * the same elements from one iteration to the next.
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
public interface Mutable<VALUE extends Object>
    extends Unidempotent<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * @return An Edit pipeline which can be used to edit the parent term's
     *         elements in-place (rather than making a new Term at the
     *         end of the Pipeline, the way the standard
     *         <code> Term.edit () </code> pipeline would).  Never null.
     */
    public abstract Edit<VALUE, Term<VALUE>> edit ()
        throws ReturnNeverNull.Violation;


    /**
     * @see musaico.foundation.term.Multiplicity#idempotent()
     */
    @Override
    public abstract Immutable<VALUE> idempotent ()
        throws ReturnNeverNull.Violation;
}
