package musaico.foundation.term.operations;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.term.NonBlocking;
import musaico.foundation.term.Operation;


/**
 * <p>
 * An Operation which receives partial results during a Blocking
 * conditional Term, and either acts on those partial results
 * or builds a new Operation which will continue processing when the
 * Blocking Term has more data.
 * </p>
 *
 * <p>
 * Typically, a ProgressiveOperation will spawn a new Operation in order
 * to set state during an Operation on a long Blocking Term.  The
 * spawned Operation contains the new state, and is stored by the
 * Blocking caller so that the next time data is available (partial
 * or complete), the new Operation will be piped.
 * </p>
 *
 * <p>
 * Alternatively, a ProgressiveOperation can act directly upon partial
 * results, for example by printing out the data, or logging some information
 * about it, and so on.  In such a case, the ProgressiveOperation would
 * return itself to the caller.
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
public interface ProgressiveOperation<INPUT extends Object, OUTPUT extends Object>
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
     * <p>
     * Works on a partial Term while waiting for a Blocking final result.
     * </p>
     *
     * <p>
     * The partial input is the complete result so far.  For example,
     * a Blocking final result might call
     * <code> progress ( new One<String> ( ..., "a" ) ) </code>
     * when it reads the first letter of the alphabet, then
     * <code> progress ( new One<String> ( ..., "ab" ) ) </code>
     * when it reads the second, and
     * <code> progress ( new One<String> ( ..., "abcdef" ) ) </code>
     * after it has read six.  And so on.
     * </p>
     *
     * <p>
     * This method is generally used to set the state of an Operation
     * during a long Operation, updating it periodically.
     * Many Operations will simply do nothing with a partial input.
     * </p>
     *
     * @param partial The incomplete Term from the Blocking Term.
     *                Must not be null.
     *
     * @return The Operation which will continue processing the result
     *         of the Blocking Term (possibly this Operation).
     *         Must not be null.
     */
    public abstract Operation<INPUT, OUTPUT>
        progress (
                  NonBlocking<INPUT> partial
                  )
        throws ParametersMustNotBeNull.Violation;


    /** Every Operation must implement toString ().
     *  @see java.lang.Object#toString() */


    /** Every Operation must implement type ().
     *  @see musaico.foundation.term.Operation#type() */
}
