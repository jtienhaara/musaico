package musaico.foundation.domains;

import java.io.Serializable;


/**
 * <p>
 * One node in a linked list, possibly with totally different
 * generic elements from link to link.
 * </p>
 *
 * @see musaico.foundation.domains.Chain
 *
 *
 * <p>
 * In Java every Chain must be Serializable in order to
 * play nicely across RMI.  However users of the Chain
 * must be careful, since any values stored inside
 * might not be Serializable.
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
 * @see musaico.foundation.domains.MODULE#COPYRIGHT
 * @see musaico.foundation.domains.MODULE#LICENSE
 */
public interface Link<NEXT extends Chain<END>, END extends Chain<END>>
    extends Chain<END>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;
}
