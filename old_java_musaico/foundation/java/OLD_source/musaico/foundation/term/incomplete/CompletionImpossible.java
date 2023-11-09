package musaico.foundation.term.incomplete;

import java.io.Serializable;


/**
 * <p>
 * A term which is Incomplete and never can be complete, such as a Partial
 * result, or a Cancelled value or a Timeout.
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
 * @see musaico.foundation.term.incomplete.MODULE#COPYRIGHT
 * @see musaico.foundation.term.incomplete.MODULE#LICENSE
 */
public interface CompletionImpossible<VALUE extends Object>
    extends Incomplete<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;
}
