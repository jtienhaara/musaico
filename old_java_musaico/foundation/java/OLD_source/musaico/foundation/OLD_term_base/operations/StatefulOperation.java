package musaico.foundation.term.operations;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.term.Operation;
import musaico.foundation.term.Term;


/**
 * <p>
 * An Operation which has a Term representing its state.
 * </p>
 *
 * <p>
 * The Operation might or might not be idempotent.  One StatefulOperation
 * might have an immutable state (and it might conceivably return
 * new StatefulOperations as its output type); whereas another
 * StatefulOperation might change its state every time
 * <code> apply ( ... ) </code> is invoked.
 * </p>
 *
 *
 * <p>
 * In Java, every Operation must implement equals (), hashCode ()
 * and toString().
 * </p>
 *
 * <p>
 * In Java every Operation must be Serializable in order to
 * play nicely across RMI.
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
 * @see musaico.foundation.term.operations.MODULE#COPYRIGHT
 * @see musaico.foundation.term.operations.MODULE#LICENSE
 */
public interface StatefulOperation<INPUT extends Object, OUTPUT extends Object, STATE extends Object>
    extends Operation<INPUT, OUTPUT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** Every Operation must implement equals ().
     *  @see java.lang.Object#equals(java.lang.Object) */

    /** Every Operation must implement hashCode ().
     *  @see java.lang.Object#hashCode() */

    /** Every Operation must implement apply (Term).
     *  @see musaico.foundation.term.Operation#apply(musaico.foundation.term.Term) */


    /**
     * @return The state of this StatefulOperation, which might or might not
     *         change over time, depending on whether or not this operation
     *         is idempotent.  Never null.
     */
    public abstract Term<STATE> state ();


    /** Every Operation must implement toString ().
     *  @see java.lang.Object#toString() */


    /** Every Operation must implement type ().
     *  @see musaico.foundation.term.Operation#type() */
}
