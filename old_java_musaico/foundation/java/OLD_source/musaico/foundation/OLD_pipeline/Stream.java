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
 *         middle.stream ()
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
 * In Java every Stream must implement <code> equals () </code>,
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
public interface Stream<STREAM extends Object, PARENT extends Pipeline<STREAM, PARENT>, POOL extends Object, STREAM_OPS extends Stream<STREAM, PARENT, POOL, STREAM_OPS>>
    extends SubPipeline<STREAM, PARENT, STREAM_OPS>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** Indices which can be used to address the first (start) element
     *  (if any) of each multi-valued object, or to be used
     *  as starting points for counting forward frpm the start
     *  (FORWARD + 0L, FORWARD + 1L, ..., FORWARD + 999L, and so on). */
    public static final long FIRST = 0L;
    public static final long FORWARD = Stream.FIRST;
    public static final long FROM_START = Stream.FIRST;

    /** The index AFTER the last one.  There is always No element
     *  at this index, but it can be used, for example, with Operations
     *  which "insert" new elements at the end. */
    public static final long AFTER_LAST = Long.MIN_VALUE;

    /** Indices which can be used to address the last (end) element
     *  (if any), or to be used as a starting point to count backward
     *  from the end (BACKWARD + 0L, BACKWARD + 1L, ...,
     *  BACKWARD + 999L, and so on). */
    public static final long LAST = Stream.AFTER_LAST + 1L;
    public static final long BACKWARD = Stream.LAST;
    public static final long FROM_END = Stream.LAST;

    /** No index at all; element # -1L is nowhere to be found in
     *  input object, no matter how large. */
    public static final long NONE = -1L;


    /**
     * <p>
     * Synonym for <code> insert ( Stream.AFTER_LAST, sequences ) </code>.
     * </p>
     *
     * @see musaico.foundation.pipeline.Stream#insert(long, java.lang.Object[])
     *
     * @return This Stream.  Never null.
     */
    @SuppressWarnings("unchecked") // Heap pollution varargs.
    public abstract STREAM_OPS append (
            POOL... sequences
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
     * sequence(s) of value(s) to each input object, at the specified
     * offset index.
     * </p>
     *
     * <p>
     * For example, if an input Term contains the elements
     * <code> A, B, C, D, E </code> and <code> 100, 200 </code>
     * are inserted at index <code> 2 </code> then the output
     * for that input will contain
     * <code> { A, B, 100, 200, C, D, E } </code>.
     * </p>
     *
     * @param index The index at which to insert the specified sequence(s),
     *              starting at <code> 0L </code> to insert before the
     *              first element, and ending at <code> length () </code> to
     *              insert after the last element.  To insert before
     *              the 3rd element, <code> 2L </code> could be used, or
     *              to insert before the 3rd last element,
     *              <code> Stream.FROM_END + 2L </code> could be used.
     *              Can be positive or negative.
     *
     * @param sequences The sequence(s) of element(s) to insert
     *                  into each input object.  Can be empty.
     *                  Must not be null.  Must not contain any null elements.
     *                  Each sequence must not contain any null elements.
     *
     * @return This Stream.  Never null.
     */
    @SuppressWarnings("unchecked") // Heap pollution varargs.
    public abstract STREAM_OPS insert (
            long index,
            POOL... sequences
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
     * is padded with the single element <code> { 0 } </code>
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
     * @param padding The element(s) to insert at the specific index
     *                in each input object.  Must not be null.
     *                Must not contain any null elements.
     *
     * @return This Stream.  Never null.
     */
    public abstract STREAM_OPS pad (
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
     * Synonym for <code> insert ( Stream.FIRST, sequences ) </code>.
     * </p>
     *
     * @see musaico.foundation.pipeline.Stream#insert(long, java.lang.Object[])
     *
     *
     * @return This Stream.  Never null.
     */
    public abstract STREAM_OPS prepend (
            POOL... sequences
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
     * @return This Stream.  Never null.
     */
    public abstract STREAM_OPS remove (
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
     * @param sequences The sequence(s) of element(s) to remove
     *                  from each input object.  Can be empty,
     *                  in which case nothing is removed.
     *                  Each sequence can be empty.
     *                  Must not be null.  Must not contain any null elements.
     *                  Each sequence must not contain any null elements.
     *
     * @return This Stream.  Never null.
     */
    public abstract STREAM_OPS remove (
            POOL... sequences
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
     * @return This Stream.  Never null.
     */
    public abstract Stream<STREAM, PARENT, POOL> removeAny (
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
     * @return This Stream.  Never null.
     */
    public abstract Stream<STREAM, PARENT, POOL> repeat (
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
     * @return This Stream.  Never null.
     */
    public abstract Stream<STREAM, PARENT, POOL> replace (
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
     * @see musaico.foundation.pipeline.Stream#remove(long, long)
     * @see musaico.foundation.pipeline.Stream#insert(long, java.lang.Iterable)
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
     * @return This Stream.  Never null.
     */
    public abstract Stream<STREAM, PARENT, POOL> replace (
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
     * @return This Stream.  Never null.
     */
    public abstract Stream<STREAM, PARENT, POOL> union (
            POOL set
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation;
}
