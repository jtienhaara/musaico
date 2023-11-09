package musaico.foundation.term.idempotence;

import java.io.Serializable;

import musaico.foundation.term.Idempotence;


/**
 * <p>
 * Represents a term whose elements are read, generated
 * or retrieved only once, then stored in memory as an immutable value
 * thereafter.
 * </p>
 *
 * <p>
 * Note that a ReadOnceThenIdempotent term might block to finish
 * "reading" its elements for the first time.  During this time,
 * the term's value is not idempotent.  Therefore, a caller who wishes
 * to work with the eventual idempotent value should block on the term
 * before calling <code> idempotent () </code>.
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
public interface ReadOnceThenIdempotent<VALUE extends Object>
    extends Unidempotent<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;
}
