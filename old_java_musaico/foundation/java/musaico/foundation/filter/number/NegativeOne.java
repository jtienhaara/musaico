package musaico.foundation.filter.number;

import java.io.Serializable;

import java.math.BigDecimal;
import java.math.BigInteger;


/**
 * <p>
 * Constants representing -1.
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
public final class NegativeOne
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** -1 as a BigDecimal. */
    public static final BigDecimal BIG_DECIMAL = BigDecimal.ONE.negate ();

    /** -1 as a BigInteger. */
    public static final BigInteger BIG_INTEGER = BigInteger.ONE.negate ();


    // No way of constructing this static class.
    private NegativeOne ()
    {
    }
}
