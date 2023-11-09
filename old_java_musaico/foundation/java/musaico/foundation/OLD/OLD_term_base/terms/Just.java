package musaico.foundation.term.behaviours;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.term.Term;
import musaico.foundation.term.ZeroOrOne;


/**
 * <p>
 * A successful term, with OneOrMore elements in its value.
 * Never has an Empty value, and never blocks.
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
 * @see musaico.foundation.term.behaviours.MODULE#COPYRIGHT
 * @see musaico.foundation.term.behaviours.MODULE#LICENSE
 */
public interface Just<VALUE extends Object>
    extends Term<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * @see musaico.foundation.term.Term#orThrowChecked()
     */
    @Override
    public abstract Just<VALUE> orThrowChecked ()
        throws ReturnNeverNull.Violation;


    /**
     * @see musaico.foundation.term.Term#orThrowUnchecked()
     */
    @Override
    public abstract Just<VALUE> orThrowUnchecked ()
        throws ReturnNeverNull.Violation;


    /**
     * @see musaico.foundation.term.Term#value()
     */
    @Override
    public abstract OneOrMore<VALUE> value ()
            throws ReturnNeverNull.Violation;
}
