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
 * Sorts or re-arranges the elements in multi-values input objects,
 * such as Types, Terms and Operations.
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
 * For example, to arrange the elements of a String Term by some
 * specific "dictionary" sort Order, then, whenever the "dictionary"
 * Order considers two elements to be the same, by the "ascii"
 * Order, and then reverse the sorted order, the following code
 * might be used:
 * </p>
 *
 * <pre>
 *     final Term<String> term = ...;
 *     final Order<String> dictionary = ...;
 *     final Order<String> ascii = ...;
 *     final Term<String>  =
 *         term.orderBy ( dictionary, ascii )
 *             .reverse ()
 *             .apply ();
 * </pre>
 *
 * <p>
 * The output of the <code> apply () </code> method would be
 * the a term whose value contains the reverse-sorted elements
 * of the input term's value.
 * </p>
 *
 *
 * <p>
 * In Java every Subsidiary must implement <code> equals () </code>,
 * <code> hashCode () </code> and <code> toString () </code>.
 * </p>
 *
 * <p>
 * In Java every Subsidiary must be Serializable in order to
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
public interface OrderBy<STREAM extends Object, PARENT extends Parent<STREAM, PARENT>>
    extends Order<STREAM>, Subsidiary<STREAM, OrderBy<STREAM, PARENT>, STREAM, PARENT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    // Every Subsidiary must implement
    // musaico.foundation.pipeline.Subsidiary#duplicate(musaico.foundation.pipeline.Parent)

    // Every Subsidiary must implement
    // musaico.foundation.pipeline.Subsidiary#end()

    // Every Subsidiary must implement java.lang.Object#equals(java.lang.Object)

    // Every Subsidiary must implement java.lang.Object#hashCode()

    // Every Subsidiary must implement
    // musaico.foundation.pipeline.Subsidiary#isEnded()

    // Every Subsidiary must implement
    // musaico.foundation.pipeline.Subsidiary#parent()


    /**
     * <p>
     * Adds an operation to the pipeline which will reverse the order
     * of the element(s) of each multi-valued input object.
     * </p>
     *
     * @return This OrderBy operation.  Never null.
     */
    public abstract OrderBy<STREAM, PARENT> reverse ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Adds an operation to the pipeline which will re-order
     * the element(s) of each multi-valued input object using the specified
     * rank generator to assign a rank to each element.
     * </p>
     *
     * @param ranker Generates the rank for each index # i of
     *               each multi-valued input object.  For example, a
     *               PseudoRandom number generator could be used.
     *               The lower the ranking number generated, the earlier
     *               in the sequence the corresponding indexed element # i
     *               will be placed.  Ties will result in the tied
     *               indexed elements maintaining their existing relative
     *               ordering.  For example, if index # i and index # ( i + 1 )
     *               both generate the same rank, then indexed element # i
     *               will come before indexed element # ( i + 1 ).
     *               If the ranker runs out of ranks, then
     *               subsequent indexed elements are placed at the end.
     *               For example if an input object contains 10 elements,
     *               but the ranker only generates 8 ranks, then the
     *               first 8 elements will be shuffled, and the 9th and 10th
     *               elements will stay in their respective indexed positions.
     *               Must not be null.
     *
     * @return This OrderBy operation.  Never null.
     */
    public abstract <RANK extends Comparable<? super RANK>>
        OrderBy<STREAM, PARENT> shuffle (
            Iterable<RANK> ranker
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    // Every Subsidiary must implement java.lang.Object#toString()
}
