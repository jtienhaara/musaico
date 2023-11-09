package musaico.foundation.term.incomplete;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.term.Term;


/**
 * <p>
 * A term which can be reduced to its canonical form, such as an
 * Expression which is reduced by evaluating it.
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
public interface Reducible<VALUE extends Object, CANONICAL extends Term<VALUE>>
    extends CompletionPossible<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Reduces this term to its canonical form, possibly performing
     * (non-blocking) Operations along the way.
     * </p>
     *
     * <p>
     * Depending on the term, the canonical form might change over time.
     * For example, an Expression which reduces to the number of seconds
     * elapsed since its input Term was created will return a different
     * value each time it is reduced.
     * </p>
     *
     * @return This Reducible term's canonical form.  Never null.
     */
    public abstract CANONICAL reduce ()
        throws ReturnNeverNull.Violation;
}
