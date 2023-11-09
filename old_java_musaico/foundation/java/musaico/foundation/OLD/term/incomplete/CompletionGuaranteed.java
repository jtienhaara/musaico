package musaico.foundation.term.incomplete;

import java.io.Serializable;


/**
 * <p>
 * A term which is Incomplete but will definitely, at some point
 * in the future, be complete, such as an Expression applying
 * an operation to a complete input term.
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
public interface CompletionGuaranteed<VALUE extends Object>
    extends CompletionPossible<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;
}
