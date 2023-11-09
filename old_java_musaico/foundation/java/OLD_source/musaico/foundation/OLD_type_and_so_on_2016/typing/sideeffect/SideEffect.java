package musaico.foundation.typing.sideeffect;

import java.io.Serializable;


/**
 * <p>
 * A side-effect from some Operation or other.
 * </p>
 *
 *
 * <p>
 * In Java every SideEffect must be Seriaizable in order to play
 * nicely over RMI.
 * </p>
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
 * @see musaico.foundation.typing.sideeffect.MODULE#COPYRIGHT
 * @see musaico.foundation.typing.sideeffect.MODULE#LICENSE
 */
public interface SideEffect
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** No side-effect at all. */
    public static final NoSideEffect NONE = new NoSideEffect ();
}
