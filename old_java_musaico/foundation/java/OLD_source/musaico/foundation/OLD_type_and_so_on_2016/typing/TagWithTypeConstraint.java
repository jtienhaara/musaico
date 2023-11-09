package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.Contract;


/**
 * <p>
 * A Tag which insists on specific requirements being met by any
 * Type in whose SymbolTable the Tag resides.
 * </p>
 *
 * <p>
 * Each TagWithTypeConstraint's requirements are checked at the end
 * of sub-typing, before the newly create sub-type is returned.
 * </p>
 *
 *
 * <p>
 * In Java every Symbol must be Serializable in order to play
 * nicely with RMI.
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
 * @see musaico.foundation.typing.MODULE#COPYRIGHT
 * @see musaico.foundation.typing.MODULE#LICENSE
 */
public interface TagWithTypeConstraint
    extends Tag, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** Every Tag must implement all the methods of Namespace. */

    /** Every Tag must implement hashCode (), equals () and toString (). */


    /**
     * @return The Contract governing Types which have this Tag.
     *         Never null.
     */
    public abstract Contract<Type<?>, TypingViolation> typeConstraint ();
}
