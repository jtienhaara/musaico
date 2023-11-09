package musaico.foundation.term.idempotence;

import java.io.Serializable;


/**
 * <p>
 * A term whose iterator can be retrieved only once; thereafter,
 * the term behaves as No value.
 * </p>
 *
 * <p>
 * If <code> idempotent () </code> is invoked before the term has
 * been read, then the term is read in and stored in a new
 * Idempotent term.  If <code> idempotent () </code> is invoked after
 * the term has already been read, then No term is returned.
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
 * @see musaico.foundation.term.idempotence.MODULE#COPYRIGHT
 * @see musaico.foundation.term.idempotence.MODULE#LICENSE
 */
public interface ReadOnceThenNo<VALUE extends Object>
    extends Unidempotent<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;
}
