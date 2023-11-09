package musaico.foundation.term;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;


/**
 * <p>
 * A Term which cannot be reduced any further, such as a constant
 * value (Multiplicity), or an Error, and so on.
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
 * @see musaico.foundation.term.MODULE#COPYRIGHT
 * @see musaico.foundation.term.MODULE#LICENSE
 */
public interface Irreducible<VALUE extends Object>
    extends Term<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * @return This Irreducible term.  Never null.
     *
     * @see musaico.foundation.term.Term#reduceTerm()
     */
    @Override
    public abstract Irreducible<VALUE> reduceTerm ()
        throws ReturnNeverNull.Violation;
}
