package musaico.foundation.term;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * A pipeline which analyses and manipulates the reducibility of Terms.
 * </p>
 *
 *
 * <p>
 * In Java every Pipeline must be Serializable in order to
 * play nicely across RMI.  However users of the Pipeline
 * must be careful, since the values and expected data stored inside
 * might not be Serializable.
 * </p>
 *
 * <p>
 * In Java every Pipeline must implement equals (), hashCode ()
 * and toString ().
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
 * @see musaico.foundation.term.MODULE#COPYRIGHT
 * @see musaico.foundation.term.MODULE#LICENSE
 */
public interface Reduce<VALUE extends Object, BUILT extends Object>
    extends Pipeline<VALUE, BUILT, Reduce<VALUE, BUILT>>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Adds an operation to the chain which will reduce
     * each input Term to an Irreducible stage.
     * </p>
     *
     * <p>
     * The full reduction might or might not be a value
     * (Multiplicity), since it could be an error or other
     * failure which really has no value.  If you want to reduce
     * to a value in all cases (a default Empty value in
     * cases such as errors and other failures), then use
     * the <code> toValue () </code> method instead.
     * </p>
     *
     * <p>
     * Note that this method might lead to different outputs
     * at different times for the same input term, depending
     * on the Terms between each input and the final Irreducible output.
     * For example, a Clock might reduce eventually to
     * One&lt;Time&gt;, but each time it is reduced, it might
     * return the current time.  Never null.
     * </p>
     *
     * @return This Reduce pipeline.  Never null.
     */
    public abstract Reduce<VALUE, BUILT> all ()
        throws ReturnNeverNull.Violation;


    // Every Pipeline must implement java.lang.Object#equals(java.lang.Object)

    // Every Pipeline must implement java.lang.Object#hashCode()


    /**
     * <p>
     * Adds an operation to the chain which will reduce
     * each input Term by at most one stage.
     * </p>
     *
     * <p>
     * Note that this reduction method might return different results
     * from the same input Term at different times, depending
     * on the idempotency of the Terms between each input Term
     * and its final Irreducible conclusion.
     * For example, a Clock might reduce eventually to
     * One&lt;Time&gt;, but each time it is reduced, it might
     * return the current time.  Never null.
     * </p>
     *
     * @return This Reduce pipeline.  Never null.
     */
    public abstract Reduce<VALUE, BUILT> once ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Prevents the Pipeline from reducing a Term beyond any stage
     * of the specified Behaviour, such as blockable Terms and so on.
     * </p>
     *
     * @param behaviour The Behaviour of Terms beyond which
     *                  the reduction process will not go.
     *                  Must not be null.
     *
     * @return This Reduce pipeline.  Never null.
     */
    public abstract Reduce<VALUE, BUILT> to (
            Behaviour behaviour
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    // Every Pipeline must implement java.lang.Object#toString()


    /**
     * <p>
     * Adds an operation to the chain which will reduce
     * each input Term to its irreducible stage, then take
     * the <code> value () </code> from that stage as the output
     * reduction.
     * </p>
     *
     * <p>
     * The Multiplicity value can be used to iterate over the element(s),
     * or conditionally return values or throw exceptions,
     * and so on.  An input Term is always reduced to an empty value
     * if it cannot be reduced without special intervention (such as by
     * <code> await ( ... ) </code>ing a Blocking result).
     * Some Irreducible Terms, such as errors and other failures,
     * do not have values; in each such case, a default
     * Empty value is generated.
     * </p>
     *
     * <p>
     * The <code> toValue () </code> method is a convenience,
     * but the <code> once () </code> or <code> all () </code> methods
     * should be used when more fine-grained control, without potential
     * default Empty values, is required.
     * </p>
     *
     * <p>
     * Note that this reduction method might return different results
     * from the same input Term at different times, depending
     * on the idempotency of the Terms between each input Term
     * and its final Irreducible conclusion.
     * For example, a Clock might reduce eventually to
     * One&lt;Time&gt;, but each time it is reduced, it might
     * return the current time.  Never null.
     * </p>
     *
     * @return This Reduce pipeline.  Never null.
     */
    public abstract Reduce<VALUE, BUILT> toValue ()
        throws ReturnNeverNull.Violation;
}
