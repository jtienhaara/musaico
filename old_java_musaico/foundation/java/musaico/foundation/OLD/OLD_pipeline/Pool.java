package musaico.foundation.pipeline;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.Parameter3;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;

import musaico.foundation.order.Order;


/**
 * <p>
 * Adds elements to and/or removes elements from multi-valued
 * input objects, such as Terms and Multiplicities.
 * </p>
 *
 * <p>
 * Pipelines are not necessarily specific to Types, Terms, Multiplicities
 * and Operations (@see musaico.foundation.term), but since that is
 * what they were designed for, the examples here all related to those
 * classes of objects.
 * </p>
 *
 * <p>
 * For example, to concatenate all elements of two Terms' values, and then
 * add another Term's value to the end, the following code might be used:
 * </p>
 *
 * <pre>
 *     final Term<Object> left = ...;
 *     final Term<Object> middle = ...;
 *     final Term<Object> right = ...;
 *     final Term<Object> concatenation =
 *         middle.pool ()
 *                 .prepend ( left )
 *                 .append ( right )
 *               .end ()
 *               .output ();
 * </pre>
 *
 * <p>
 * The output of the <code> output () </code> method would be
 * the concatenation of the three Terms left, middle and right.
 * </p>
 *
 *
 * <p>
 * In Java every Pool must implement <code> equals () </code>,
 * <code> hashCode () </code> and <code> toString () </code>.
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
 * @see musaico.foundation.pipeline.MODULE#COPYRIGHT
 * @see musaico.foundation.pipeline.MODULE#LICENSE
 */
public interface Pool<STREAM extends Object, PARENT extends Pipeline<STREAM, PARENT>, POOL extends Object>
    extends SubPipeline<STREAM, PARENT, Pool<STREAM, PARENT, POOL>>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Synonym for <code> insert ( Stream.AFTER_LAST, sequence ) </code>.
     * </p>
     *
     * @see musaico.foundation.pipeline.Pool#insert(long, java.lang.Iterable)
     *
     *
     * @return This Pool.  Never null.
     */
    @SuppressWarnings("unchecked") // Heap pollution varargs.
    public abstract Pool<STREAM, PARENT, POOL> append (
            POOL sequence
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Adds an operation to the pipeline which will return the set difference
     * between the specified value and each input object,
     * containing each element which is present either in
     * the specified value or in the input object,
     * but not in both.
     * </p>
     *
     * <p>
     * Multiple copies of the same element will be kept, even though
     * this is a set operation at heart.  The number of copies of element E
     * is equal to the # of E's in whichever value (the specified value
     * or the input object) contains them.
     * </p>
     *
     * <p>
     * For example, if an input Term's value contains the element "A" twice,
     * but the specified term's value does not contain "A",
     * then "A" shall appear twice in the output Term's value.
     * </p>
     *
     * @param set The value whose elements shall be diff'ed
     *            against each input object's elements.  Must not be null.
     *            Must not contain any null elements.
     *
     * @return This Pool.  Never null.
     */
    public abstract Pool<STREAM, PARENT, POOL> difference (
            POOL set
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation;


    // Every SubPipeline must implement
    // musaico.foundation.pipeline.SubPipeline#end()


    // Every SubPipeline must implement java.lang.Object#equals(java.lang.Object)


    // Every SubPipeline must implement java.lang.Object#hashCode()


    /**
     * <p>
     * Adds an operation to the pipeline which will add the specified
     * value to each input object, at the specified offset index.
     * </p>
     *
     * <p>
     * For example, if an input Term contains the elements
     * <code> A, B, C, D, E </code> and the sub-value <code> 100, 200 </code>
     * is inserted at index <code> 2 </code> then the output
     * for that input will contain
     * <code> { A, B, 100, 200, C, D, E } </code>.
     * </p>
     *
     * @param sequence The sequence of element(s) to insert
     *                 into each input object.  Can be empty.
     *                 Must not be null.  Must not contain any null elements.
     *
     * @param index The index at which to insert the specified sub-value,
     *              starting at <code> 0L </code> to insert before the
     *              first element, and ending at <code> length () </code> to
     *              insert after the last element.  To insert before
     *              the 3rd element, <code> 2L </code> could be used, or
     *              to insert before the 3rd last element,
     *              <code> Stream.FROM_END + 2L </code> could be used.
     *              Can be positive or negative.
     *
     * @return This Pool.  Never null.
     */
    public abstract Pool<STREAM, PARENT, POOL> insert (
            POOL sequence,
            long index
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Adds an operation to the pipeline which will output the set intersection
     * of the specified value with each input object.
     * </p>
     *
     * <p>
     * Multiple copies of the same element will be kept, even though
     * this is a set operation at heart.  The number of copies of element E
     * is equal to the minimum of ( # of E's in the specified value,
     * # of E's in the input object ).
     * </p>
     *
     * <p>
     * For example, if the input contains the element "A" twice,
     * and the specified value contains three instances of "A",
     * then "A" shall appear twice in the output.
     * </p>
     *
     * @param set The value(s) to intersect with each input object.
     *            Must not be null.  Must not contain any null elements.
     *
     * @return This Pool.  Never null.
     */
    public abstract Pool<STREAM, PARENT, POOL> intersection (
            POOL set
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation;


    // Every SubPipeline must implement
    // musaico.foundation.pipeline.SubPipeline#operations()


    /**
     * <p>
     * Adds an operation to the pipeline which will insert padding elements
     * to bulk up each input object to a specific length.
     * </p>
     *
     * <p>
     * For example, if the term with value <code> { 1, 2, 3 } </code>
     * is padded with element <code> { 0 } </code>
     * at index <code> 0 </code> to reach target length <code> 6 </code>,
     * the result will be <code> { 0, 0, 0, 1, 2, 3 } </code>.
     * </p>
     *
     * <p>
     * If there are multiple padding elements then each will be added
     * in sequence, followed by a repeat of the sequence, and so on,
     * until one of the elements is added to reach the final target length.
     * For example, starting from input object
     * <code> { 1, 2, 3 } </code>, padding the start index <code> 0 </code>
     * with <code> { A, B, C } </code> to reach target length <code> 7 </code>
     * would result in <code> { A, B, C, A, 1, 2, 3 } </code>.
     * </p>
     *
     * @param padding The element(s) to insert at the specific index
     *                in each input object.  Must not be null.
     *                Must not contain any null elements.
     *
     * @param pad_at_index Where to insert the padding in the
     *                     value being built.  For example, to pad
     *                     before the first element,
     *                     use index <code> 0L </code>.
     *                     Or to pad before the 9th element,
     *                     use index <code> 8L </code>.
     *                     Or to pad before the 3rd last element,
     *                     use index <code> Stream.FROM_END + 2L </code>.
     *                     Or to pad after the last element,
     *                     use index <code> Stream.AFTER_LAST </code>.
     *                     Can be positive or negative.
     *
     * @param target_length How many elements the input object
     *                      should have after the padding has
     *                      been inserted.  If less than or equal to
     *                      the length of the value being built
     *                      (for example, if the target length is 0 or
     *                      or -1 or length - 1) then no padding
     *                      will be added.
     *
     * @return This Pool.  Never null.
     */
    public abstract Pool<STREAM, PARENT, POOL> pad (
            long pad_at_index,
            long target_length,
            POOL padding
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter3.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation;


    // Every SubPipeline must implement
    // musaico.foundation.pipeline.SubPipeline#parent()


    // Every SubPipeline must implement
    // musaico.foundation.pipeline.SubPipeline#pipe(musaico.foundation.pipeline.ElementalOperation, musaico.foundation.pipeline.ElementalOperation.BlockSize)


    /**
     * <p>
     * Synonym for <code> insert ( Stream.FIRST, sequence ) </code>.
     * </p>
     *
     * @see musaico.foundation.pipeline.Pool#insert(long, java.lang.Iterable)
     *
     *
     * @return This Pool.  Never null.
     */
    public abstract Pool<STREAM, PARENT, POOL> prepend (
            POOL sequence
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Adds an operation to the pipeline which will remove the elements
     * from each each input object within the specified index range.
     * </p>
     *
     * <p>
     * For example, to remove the first and second elements
     * from each input, <code> remove ( 0L, 1L ) </code>
     * could be called.  Or to remove the 10th through
     * 20th elements <code> remove ( 9L, 19L ) </code>
     * could be called.  Or to remove the 2nd last
     * through 8th last elements,
     * <code> remove ( Stream.FROM_END + 1L,
     *                 Stream.FROM_END + 7L ) </code>
     * could be called.  And so on.
     * </p>
     *
     * @param start_index The beginning of the range of elements to
     *                    remove from each input object.  
     *                    Can be any number.
     *
     * @param end_index The last index of the range of elements to
     *                  remove from each input object.  
     *                  Can be any number.
     *
     * @return This Pool.  Never null.
     */
    public abstract Pool<STREAM, PARENT, POOL> remove (
            long start_index,
            long end_index
            )
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Adds an operation to the pipeline which will remove
     * each of the specified sequences of consecutive elements
     * from each each input object, if they are present.
     * </p>
     *
     * <p>
     * For example, to remove the sequence of Strings
     * <code> { "Hello", "world" } </code> from an input object,
     * <code> remove ( { "Hello", "world" } ) </code>
     * might be called.  Then an object containing the Strings
     * <code> { "Hello", "world", "how", "are", "you" } </code>
     * would be output as <code> { "how", "are", "you" } </code>,
     * whereas an input containing the Strings
     * <code> { "Hello", "there", "brave", "new", "world", "Hello" } </code>
     * would be output unchanged, since the exact sequence does
     * not appear.
     * </p>
     *
     * @param sequence The sequence of element(s) to remove
     *                 from each input object.  Can be empty,
     *                 in whic case nothing is removed.
     *                 Must not be null.
     *
     * @return This Pool.  Never null.
     */
    public abstract Pool<STREAM, PARENT, POOL> remove (
            POOL sequence
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Adds an operation to the pipeline which will remove
     * any of the specified elements from each each input object,
     * if they are present.
     * </p>
     *
     * <p>
     * For example, to remove the Strings "Hello" and "world"
     * from an input object, <code> remove ( "Hello", "world" ) </code>
     * might be called.  Then an object containing the Strings
     * <code> { "Hello", "world", "how", "are", "you" } </code>
     * would be output as <code> { "how", "are", "you" } </code>,
     * and the input containing the Strings
     * <code> { "Hello", "there", "brave", "new", "world", "Hello" } </code>
     * would be output as <code> { "there", "brave", "new" } </code>.
     * </p>
     *
     * @param set The element(s) to remove from each input object.
     *            Can be empty, in which case nothing is removed.
     *            Must not be null.
     *
     * @return This Pool.  Never null.
     */
    public abstract Pool<STREAM, PARENT, POOL> removeAny (
            POOL set
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Adds an operation to the pipeline which will replicate the elements
     * of each input object a certain number of times.
     * </p>
     *
     * <p>
     * Calling <code> repeat ( 1L ) </code> leaves the elements
     * of each input as-is.
     * </p>
     *
     * <p>
     * Calling <code> repeat ( 3L ) </code> will produce an output
     * Term whose value is three times as long as the input object.
     * For example, with input Term value <code> { "a", "b" } </code>, the
     * output Term would have value
     * <code> { "a", "b", "a", "b", "a", "b" } </code>.
     * </p>
     *
     * <p>
     * And so on.
     * </p>
     *
     * @param repetitions The number of times to repeat each input pattern
     *                    of elements.  Must be greater than or equal to 1L.
     *
     * @return This Pool.  Never null.
     */
    public abstract Pool<STREAM, PARENT, POOL> repeat (
            long repetitions
            )
        throws Parameter1.MustBeGreaterThanOrEqualToOne.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Adds an operation to the pipeline which will
     * remove each specific sequence of elements, then
     * insert a specific replacement sequence of elements in its place.
     * </p>
     *
     * @param sequence The exact sequence of elements to replace.
     *                    Can be empty, in which case no replacements
     *                    will occur.  Must not be null.
     *                    Must not contain any null elements.
     *
     * @param replacement The element(s) to insert in place of the
     *                    range of elements in each input object.
     *                    Can be empty.  Must not be null.
     *                    Must not contain any null elements.
     *
     * @return This Pool.  Never null.
     */
    public abstract Pool<STREAM, PARENT, POOL> replace (
            POOL sequence,
            POOL replacement
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               Parameter2.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Adds an operation to the pipeline which will remove elements
     * in a specific range of indices from each input object, then
     * insert a specific replacement sequence of elements in its place.
     * </p>
     *
     * <p>
     * Has the same end effect as first calling
     * <code> remove ( start_index, end_index ) </code> then calling
     * <code> insert ( start_index, replacement ) </code>.
     * </p>
     *
     * @see musaico.foundation.pipeline.Pool#remove(long, long)
     * @see musaico.foundation.pipeline.Pool#insert(long, java.lang.Iterable)
     *
     * @param start_index The beginning of the range of elements to
     *                    replace in each input object.  
     *                    Can be any number.
     *
     * @param end_index The last index of the range of elements to
     *                  replace in each input object.  
     *                  Can be any number.
     *
     * @param replacement The element(s) to insert in place of the
     *                    range of elements in each input object.
     *                    Can be empty.  Must not be null.
     *                    Must not contain any null elements.
     *
     * @return This Pool.  Never null.
     */
    public abstract Pool<STREAM, PARENT, POOL> replace (
            long start_index,
            long end_index,
            POOL replacement
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter3.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation;


    // Every SubPipeline must implement java.lang.Object#toString()


    /**
     * <p>
     * Adds an operation to the pipeline which will output the set union
     * (join) of the specified value with each input object.
     * </p>
     *
     * <p>
     * Multiple copies of the same element will be kept, even though
     * this is a set operation at heart.  The number of copies of element E
     * is equal to the maximum of ( # of E's in the specified value,
     * # of E's in the input object ).
     * </p>
     *
     * <p>
     * For example, if the input contains the element "A" twice,
     * and the specified value contains one instance of "A",
     * then "A" shall appear twice in the output.
     * </p>
     *
     * @param set The value(s) to join with each input object.
     *            Must not be null.  Must not contain any null elements.
     *
     * @return This Pool.  Never null.
     */
    public abstract Pool<STREAM, PARENT, POOL> union (
            POOL set
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation;
}
