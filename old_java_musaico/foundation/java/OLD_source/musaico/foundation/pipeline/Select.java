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
 * Selects a subset of zero or more elements from each multi-valued
 * input object (such as a Term), and produces a new
 * multi-valued input object as output.
 * </p>
 *
 * <p>
 * Unlike the Edit pipeline, the Select pipeline always leaves
 * each original input object untouched.  Edit, by contrast, will
 * modify input objects in place, if (and only if) they are mutable.
 * </p>
 *
 * <p>
 * Pipelines are not necessarily specific to Types, Terms
 * and Operations (@see musaico.foundation.term), but since that is
 * what they were designed for, the examples here all related to those
 * classes of objects.
 * </p>
 *
 *
 * <p>
 * In Java every Select must implement <code> equals () </code>,
 * <code> hashCode () </code> and <code> toString () </code>.
 * </p>
 *
 * <p>
 * In Java every Select must be Serializable in order to
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
public interface Select<STREAM extends Object, PARENT extends Parent<STREAM, PARENT>>
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Adds an Operation to the pipeline which outputs all of its inputs
     * (or at least as many as the subsequent Operation(s) in the pipeline
     * request).
     * </p>
     *
     * @return The parent Pipeline or Subsidiary.  Never null.
     */
    public abstract PARENT all ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Adds an Operation to the pipeline which will select at most One
     * element from its input, the element at the specified index.
     * </p>
     *
     * @param index The index of the element to output, such as 0L
     *              for the first element, or 2L for the 3rd.
     *              If the index is greater than or equal to the length
     *              of an input object, then that input generates
     *              an empty output (not an error).
     *              Must be greater than or equal to 0L.
     *
     * @return The parent Pipeline or Subsidiary.  Never null.
     */
    public abstract PARENT at (
            long index
            )
        throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Creates a copy of this Select.
     * </p>
     *
     * <p>
     * The new Select will have exactly the same operations
     * as this one.
     * </p>
     *
     * <p>
     * No parent or ancestor Pipelines / Subsidiaries are copied.
     * </p>
     *
     * @param parent The parent Pipeline or Subsidiary for the new Select.
     *               Must not be null.
     *
     * @return The new copy of this Select.  Never null.
     */
    public abstract Select<STREAM, PARENT> duplicate (
            PARENT parent
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    // Every Select must implement java.lang.Object#equals(java.lang.Object)


    /**
     * <p>
     * Adds an Operation to the pipeline which will output only the first
     * element of each multi-valued input object.
     * </p>
     *
     * @return The parent Pipeline or Subsidiary.  Never null.
     */
    public abstract PARENT first ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Adds an Operation to the pipeline which will output only the first
     * (N) elements of each multi-valued input object.
     * </p>
     *
     * @param num_elements The number of elements to output (1, 2, 3, ...),
     *                     starting with the very first element.
     *                     Must be greater than or equal to 1L.
     *
     * @return The parent Pipeline or Subsidiary.  Never null.
     */
    public abstract PARENT first (
            long num_elements
            )
        throws Parameter1.MustBeGreaterThanOrEqualToOne.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Modifies this Select so that any subsequently requested
     * indices of elements will be offset from the end of each
     * input object.
     * </p>
     *
     * <p>
     * For example, calling
     * <code> pipeline.select ().fromEnd ().at ( 0L ) </code>
     * will select the last element of each input object; or calling
     * <code> pipeline.select ().fromEnd ().range ( 2L, 8L ) </code>
     * will select the 3rd last element to the 9th last element,
     * in the reverse order that they appear in each input object.
     * And so on.
     * </p>
     *
     * @see musaico.foundation.pipeline.Select#at(long)
     * @see musaico.foundation.pipeline.Select#range(long, long) 
     *
     * @return This Select, modified to return indices offset
     *         from the end of each input, rather than from the start.
     *         Never null.
     */
    public abstract Select<STREAM, PARENT> fromEnd ()
        throws ReturnNeverNull.Violation;


    // Every Select must implement java.lang.Object#hashCode()


    /**
     * <p>
     * Adds an Operation to the pipeline which will output only the last
     * element of each multi-valued input object.
     * </p>
     *
     * @return The parent Pipeline or Subsidiary.  Never null.
     */
    public abstract PARENT last ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Adds an Operation to the pipeline which will output only the last
     * (N) element(s) of each multi-valued input object.
     * </p>
     *
     * @param num_elements The number of elements to select from the end
     *                     of input.  Must be greater than or equal to 1L.
     *
     * @return The parent Pipeline or Subsidiary.  Never null.
     */
    public abstract PARENT last (
            long num_elements
            )
        throws Parameter1.MustBeGreaterThanOrEqualToOne.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Adds an Operation to the pipeline which will output only the middle
     * element or the middle two elements of each multi-valued input object.
     * </p>
     *
     * <p>
     * If an input object is empty, then it will generate a failed output
     * (such as an Error Term, or No Term, and so on).
     * </p>
     *
     * <p>
     * If an input object has an odd number of elements, then One
     * output element will be generated.
     * </p>
     *
     * <p>
     * If an input object has a non-zero even number of elements,
     * then two output elements will be generated.
     * </p>
     *
     * @return The parent Pipeline or Subsidiary.  Never null.
     */
    public abstract PARENT middle ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Adds an Operation to the pipeline which will output only the middle
     * (N) or (N+1) element(s) of each multi-valued input object.
     * </p>
     *
     * <p>
     * If an input object has zero elements, then the output object
     * will always be empty as well.
     * </p>
     *
     * <p>
     * If an input object has less than the specified number of elements,
     * then it will generate a failed output (such as an Error Term,
     * or No Term, and so on).
     * </p>
     *
     * <p>
     * If an input object has an odd number of elements,
     * and if <code> num_elements </code> is also odd,
     * then <code> num_elements </code> output elements will be generated.
     * </p>
     *
     * <p>
     * If an input object has an odd number of elements,
     * and if <code> num_elements </code> is even,
     * then <code> num_elements + 1L </code> output elements will be generated.
     * </p>
     *
     * <p>
     * If an input object has an even number of elements,
     * and if <code> num_elements </code> is also even,
     * then <code> num_elements </code> output elements will be generated.
     * </p>
     *
     * <p>
     * If an input object has an even number of elements,
     * and if <code> num_elements </code> is odd,
     * then <code> num_elements + 1L </code> output elements will be generated.
     * </p>
     *
     * @param num_elements The number of elements to select from the middle
     *                     of input.  Must be greater than or equal to 1L.
     *
     * @return The parent Pipeline or Subsidiary.  Never null.
     */
    public abstract PARENT middle (
            long num_elements
            )
        throws Parameter1.MustBeGreaterThanOrEqualToOne.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Modifies this Select so that any subsequently requested
     * length of elements is the minimum length, not necessarily
     * the exact length that will be returned.
     *
     * <p>
     * Any requested start and end indices must be included in each output,
     * but additional indices can be output as well.
     * </p>
     *
     * <p>
     * By default, length specifiers are exact; so a request for the
     * <code> first ( 10L ) </code> elements of an object will
     * result in exactly 10 elements being output (if the given input
     * has 10 or more elements).  This behaviour can be modified
     * to return the first 10 <i> or more </i> elements by calling
     * <code> neighbourhood ().first ( 10L ) </code>.  In this case,
     * if the Select pipeline can easily return exactly 10 elements,
     * it will do so.  However it might sometimes be more efficient
     * to return more elements.  For example, in a pipeline of Terms,
     * if the first Term has 16 elements, and if the caller doesn't
     * care whether more than the specified number of elements
     * are returned, then the Select pipeline can save processing time
     * by simply returning the entire first Term, with all 16 elements,
     * as-is.
     * </p>
     *
     * <p>
     * Similarly, if the caller wants to retrieve all the elements
     * in the <code> range ( 11L, 32L ) </code>, but does not care
     * whether additional elements are also included, then
     * <code> neighbourhood ().range ( 11L, 32L ) </code> can be called.
     * If an input pipes in fragments of an object containing
     * { 7, 11, 13, 17, 19 } elements, respectively, then the
     * strict <code> range ( 11L, 32L ) </code> would return exactly
     * elements 11 to 32, splitting the 11 and 17 fragments along the way.
     * On the other hand, <code> neighbourhood ().range ( 11L, 32L ) </code>
     * with the same input would produce an output containing
     * the fragments with { 11, 13, 17 } elements, covering 11-32
     * as well as 7-10 and 33-47.
     * <p>
     *
     * @see musaico.foundation.pipeline.Select#at(long)
     * @see musaico.foundation.pipeline.Select#first(long)
     * @see musaico.foundation.pipeline.Select#last(long)
     * @see musaico.foundation.pipeline.Select#middle(long)
     * @see musaico.foundation.pipeline.Select#range(long, long) 
     *
     * @return This Select, modified to return neighbourhoods
     *         rather than exactly what is requested whenever a length
     *         and/or index (-ices) are requested.  Never null.
     */
    public abstract Select<STREAM, PARENT> neighbourhood ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Adds an Operation to the pipeline which will select no elements at all.
     * </p>
     *
     * @return The parent Pipeline or Subsidiary.  Never null.
     */
    public abstract PARENT noElements ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Adds an Operation to the pipeline which will select the specified
     * range of element(s) from its input.
     * </p>
     *
     * @param start The index of the first element to output, such as 0L
     *              for the first element, or 2L for the 3rd.
     *              If the index is greater than or equal to the length
     *              of an input object, then that input generates
     *              an empty output (not an error).
     *              If the start index is greater than the end index,
     *              then the selection of elements from each input object
     *              will be reversed in order.
     *              Must be greater than or equal to 0L.
     *
     * @param end The index of the last element to output, such as 0L
     *            for the first element, or 2L for the 3rd.
     *            If the index is greater than or equal to the length
     *            of an input object, then that input generates
     *            an empty output (not an error).
     *            If the end index is less than the start index,
     *            then the selection of elements from each input object
     *            will be reversed in order.
     *            Must be greater than or equal to 0L.
     *
     * @return The parent Pipeline or Subsidiary.  Never null.
     */
    public abstract PARENT range (
            long start,
            long end
            )
        throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
               Parameter2.MustBeGreaterThanOrEqualToZero.Violation,
               ReturnNeverNull.Violation;


    // Every Select must implement java.lang.Object#toString()
}
