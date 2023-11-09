package musaico.foundation.term;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;


/**
 * <p>
 * Either Just one value, or an Empty value.  Never multiple values,
 * and never an Error or Partial value and so on.
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
public interface Maybe<VALUE extends Object>
    extends Countable<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;
}
