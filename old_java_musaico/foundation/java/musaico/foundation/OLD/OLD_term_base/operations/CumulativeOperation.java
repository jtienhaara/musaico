package musaico.foundation.term.operations;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.term.Operation;
import musaico.foundation.term.Term;


/**
 * <p>
 * An Operation which returns a new instance of itself with the output
 * from the previous run stored in its <code> state () </code>.
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
public interface CumulativeOperation<INPUT extends Object, CUMULATIVE extends Object>
    extends StatefulOperation<INPUT, CumulativeOperation<INPUT, CUMULATIVE>, CUMULATIVE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** Every Operation must implement equals ().
     *  @see java.lang.Object#equals(java.lang.Object) */

    /** Every Operation must implement hashCode ().
     *  @see java.lang.Object#hashCode() */

    /** Every Operation must implement apply (Term).
     *  @see musaico.foundation.term.Operation#apply(musaico.foundation.term.Term) */

    /** Every StatefulOperation must implement state ().
     *  @see musaico.foundation.term.operations.StatefulOperation#state() */

    /** Every Operation must implement toString ().
     *  @see java.lang.Object#toString() */

    /** Every Operation must implement type ().
     *  @see musaico.foundation.term.Operation#type() */
}
