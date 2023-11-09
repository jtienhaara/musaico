package musaico.foundation.term.incomplete;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.term.Term;


/**
 * <p>
 * A term which is Incomplete and which might or might not eventually
 * be complete, such as a Blocking term, which could result in a final,
 * complete value, or it could instead timeout or be cancelled.
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
public interface CompletionPossible<VALUE extends Object>
    extends Incomplete<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;
}
