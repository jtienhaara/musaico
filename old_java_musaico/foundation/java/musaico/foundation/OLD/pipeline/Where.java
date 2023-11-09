package musaico.foundation.pipeline;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;

import musaico.foundation.order.Order;


/**
 * <p>
 * Filters out elements of each multi-valued input object which
 * do not match certain filters.
 * </p>
 *
 * <p>
 * Pipelines are not necessarily specific to Types, Terms
 * and Operations (@see musaico.foundation.term), but since that is
 * what they were designed for, the examples here all related to those
 * classes of objects.
 * </p>
 *
 * <p>
 * For example, to filter out all elements of a Number term's value which
 * are not in the range 0..10, and return only one element
 * per unique Number, removing duplicates, the following code
 * might be used:
 * </p>
 *
 * <pre>
 *     final Term<Number> term = ...;
 *     final Term<Number> greater_than_or_equal_to_0 =
 *         term.where ( GreaterThanOrEqualToZero.DOMAIN )
 *                 .and ( new LessThanOrEqualToNumber ( 10 ) )
 *                 .unique ()
 *             .end ()
 *             .output ();
 * </pre>
 *
 * <p>
 * The output would be some subset of the Numbers
 * ( 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 ), depending on the input,
 * with no duplicates.
 * </p>
 *
 * @see musaico.foundation.pipeline.Decision
 *
 *
 * <p>
 * In Java every Where must be Serializable in order to
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
 * @see musaico.foundation.pipeline.MODULE#COPYRIGHT
 * @see musaico.foundation.pipeline.MODULE#LICENSE
 */
public interface Where<STREAM extends Object, PARENT extends Parent<STREAM, PARENT>>
    extends Decision<STREAM, Where<STREAM, PARENT>>, Subsidiary<STREAM, Where<STREAM, PARENT>, STREAM, PARENT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    // Every Decision must implement
    // musaico.foundation.pipeline.Decision#and(musaico.foundation.filter.Filter[])

    // Every Decision must implement
    // musaico.foundation.pipeline.Decision#condition()

    // Every Subsidiary must implement
    // musaico.foundation.pipeline.Subsidiary#duplicate(musaico.foundation.pipeline.Parent)

    // Every Decision must implement
    // musaico.foundation.pipeline.Decision#empty()

    // Every Subsidiary must implement
    // musaico.foundation.pipeline.Subsidiary#end()

    // Every Subsidiary must implement java.lang.Object#equals(java.lang.Object)

    // Every Subsidiary must implement java.lang.Object#hashCode()

    // Every Decision must implement
    // musaico.foundation.pipeline.Decision#infinite()

    // Every Subsidiary must implement
    // musaico.foundation.pipeline.Subsidiary#isEnded()

    // Every Decision must implement
    // musaico.foundation.pipeline.Decision#length(long)

    // Every Decision must implement
    // musaico.foundation.pipeline.Decision#length(long, long)

    // Every Decision must implement
    // musaico.foundation.pipeline.Decision#multiple()

    // Every Decision must implement
    // musaico.foundation.pipeline.Decision#or(musaico.foundation.filter.Filter[])

    // Every Subsidiary must implement
    // musaico.foundation.pipeline.Subsidiary#parent()

    // Every Decision must implement
    // musaico.foundation.pipeline.Decision#single()

    // Every Subsidiary must implement java.lang.Object#toString()

    // Every Decision must implement
    // musaico.foundation.pipeline.Decision#unique()


    /**
     * <p>
     * Pushes a new branch on the conditional tree, effectively allowing
     * parenthetical conditions such as:
     * </p>
     *
     * <pre>
     *     pipeline.where ( x )
     *                 .where ( y )
     *                     .or ( z )
     *                 .end ()
     *             .end ()
     * </pre>
     *
     * <p>
     * The above example would be evaluated as <code> x and ( y or z ) </code>.
     * </p>
     */
    public abstract Where<STREAM, Where<STREAM, PARENT>> where (
            Filter<STREAM> condition
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    // Every Decision must implement
    // musaico.foundation.pipeline.Decision#xor(musaico.foundation.filter.Filter[])
}
