package musaico.foundation.security;

import java.io.Serializable;


/**
 * <p>
 * The ability to perform some operation, such as the ability to
 * read or write a file.
 * </p>
 *
 *
 * <p>
 * In Java every Capability must implement <code> equals ( Object ) </code>,
 * <code> hashCode () </code> and <code> toString () </code>.
 * </p>
 *
 * <p>
 * In Java every Capability must be Serializable in order to play nicely
 * across RMI.
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
 * @see musaico.foundation.security.MODULE#COPYRIGHT
 * @see musaico.foundation.security.MODULE#LICENSE
 */
public interface Capability
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** No capability at all.  Not very useful! */
    public static final NoCapability NONE = new NoCapability ();
}
