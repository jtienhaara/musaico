package musaico.foundation.pipeline;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;

import musaico.foundation.filter.Filter;

import musaico.foundation.order.Order;


/**
 * <p>
 * Adds elements to and/or removes elements from multi-valued
 * input objects, such as Terms.
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
 * For example, to concatenate all elements of two Terms' values, and then
 * add another Term's value to the end, the following contrived
 * code might be used:
 * </p>
 *
 * <pre>
 *     final Term<String> middle = ... "brave", "new", ...;
 *     final Term<String> concatenation =
 *         middle.edit ()
 *                 .prepend ().sequence ( "Hello," )
 *                 .append ().sequence ( "world!" )
 *               .end ()
 *               .output ();
 * </pre>
 *
 * <p>
 * The output of the <code> output () </code> method would be
 * a Term&lt;String&gt; along the lines of { "Hello,", "brave",
 * "new", "world!".
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
public interface Edit<STREAM extends Object, PARENT extends Pipeline<STREAM, PARENT>, POOL extends Object>
    extends Subsidiary<STREAM, Edit<STREAM, PARENT, POOL>, STREAM, PARENT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Edit operation that concatenates a sequence of elements
     * to the end of the multi-valued object.
     * </p>
     *
     * <p>
     * For example, to append several Date elements to a Term,
     * something like the following might be used:
     * </p>
     *
     * <pre>
     *     final Date family_day = ...;
     *     final Date easter = ...;
     *     final Date victoria_day = ...;
     *     final Term<Date> holidays = ...
     *         .edit ()
     *             .append ().sequence ( family_day,
     *                                   easter,
     *                                   victoria_day )
     *         .end ()
     *         .output ();
     * </p>
     *
     * <p>
     * In this case, the append operation would produce a new Term
     * from <code> holidays </code> with 3 additional elements:
     * <code> { family_day, easter, victoria_day } </code>.
     * </p>
     *
     * <p>
     * Alternatively, two Terms could be appended:
     * </p>
     *
     * <pre>
     *     Term<Team> bloomfield_division = ...;
     *     Term<Team> needham_division = ...;
     *     Term<Team> league =
     *         bloomfield_division
     *             .edit ()
     *                 .append ().sequence ( needham_division )
     *             .end ()
     *             .output ();
     * </p>
     *
     * <p>
     * In this example, the append operation would produce a new Term
     * with all of the <code> Team <code> elements from
     * <code> bloomfield_division </code> followed by all of the
     * elements of <code> needham_division </code>.
     * </p>
     *
     * @return A new EditSequence sub-Pipeline that will be used to specify
     *         the element(s) or multi-valued object to append,
     *         after which control returns to this Edit pipeline.
     *         Never null.
     */
    public abstract EditSequence<STREAM, Edit<STREAM, PARENT, POOL>, POOL, ?> append ()
        throws ReturnNeverNull.Violation;


    // Every Subsidiary must implement
    // musaico.foundation.pipeline.Subsidiary#duplicate(musaico.foundation.pipeline.Parent)

    // Every Subsidiary must implement
    // musaico.foundation.pipeline.Subsidiary#end()

    // Every Subsidiary must implement java.lang.Object#equals(java.lang.Object)


    /**
     * <p>
     * Adds an operation to the pipeline that will remove from each input
     * any and all element(s) found in a specific set of elements
     * to exclude.
     * </p>
     *
     * <p>
     * For example, to remove the Strings "Hello" and "world"
     * from an input object, <code> except ().set ( "Hello", "world" ) </code>
     * might be called.  Then an object containing the Strings
     * <code> { "Hello", "world", "how", "are", "you" } </code>
     * would result in <code> { "how", "are", "you" } </code>,
     * and the input containing the Strings
     * <code> { "Hello", "there", "brave", "new", "world", "Hello" } </code>
     * would result in <code> { "there", "brave", "new" } </code>.
     * </p>
     *
     * @return A new EditSet sub-Pipeline that will be used to specify
     *         the set to exclude, after which control returns
     *         to this Edit pipeline.
     *         Never null.
     */
    public abstract EditSet<STREAM, Edit<STREAM, PARENT, POOL>, POOL, ?> except ()
        throws ReturnNeverNull.Violation;


    // Every Subsidiary must implement java.lang.Object#hashCode()


    /**
     * <p>
     * Adds an operation to the pipeline that will insert a sequence
     * of element(s) into each input object, at a specific index
     * or according to some Order, and so on.
     * </p>
     *
     * <p>
     * For example, if an input Term contains the elements
     * <code> { "Herder", "Nietzsche", "Hitler" } </code> and the
     * sequence <code> { "Sterne", "Poe" } </code> is inserted
     * <code> at ( 1L ) </code>, then the result will be
     * <code> { "Herder", "Sterne", "Poe", "Nietzsche", "Hitler" } </code>.
     * </p>
     *
     * <p>
     * Or if an input Term contains the elements
     * <code> { "Herder", "Hitler", "Nietzsche" } </code> and the
     * sequence <code> { "Berlin" } </code> is inserted
     * <code> orderBy ( ... ) </code> an alphabetical ordering,
     * then the result will be
     * <code> { "Berlin", "Herder", "Hitler", "Nietzsche" } </code>.
     * </p>
     *
     * <p>
     * According to the logic of some philosophers, you end up
     * passing through Hitler no matter where you start.
     * </p>
     *
     * <p>
     * (Of course if we then <code> append () </code> the elements
     * <code> { "Tolkien", "Pratchett", "Rowling" } </code> to the end,
     * then we have produced hard evidence of Eternal Recurrence,
     * not to mention an unbreakable chain of causality that leads
     * from Herder to Hitler to Lord Voldemort, with
     * a stop along the way at the Unseen University to say "hi"
     * to Berlin.)
     * </p>
     *
     * @return A new EditSequence sub-Pipeline that will be used to specify
     *         the element(s) or multi-valued object to insert,
     *         after which control will pass to a new EditInsert
     *         sub-Pipeline that will be used to set either an index
     *         at which to insert the sequence, or an ordering by
     *         which to place it, and so on; after which control returns
     *         to this Edit pipeline.  Never null.
     */
    public abstract <SEQUENCE extends EditSequence<STREAM, Edit<STREAM, PARENT, POOL>, POOL, SEQUENCE>>
        EditInsert<STREAM, SEQUENCE, ?> insert ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Adds an operation to the pipeline that will output the set
     * intersection of each multi-valued input object with a
     * specific set of elements.
     * </p>
     *
     * <p>
     * Multiple copies of the same element will be kept, even though
     * this is a set operation at heart.  The number of copies of element E
     * is always equal to the number of E's in the input object, if
     * there are 0 or more E's in the specific set.
     * </p>
     *
     * <p>
     * For example, if the input contains the element "A" twice,
     * and the specific set contains three instances of "A",
     * then "A" shall appear twice in the output.
     * </p>
     *
     * <p>
     * For example, if <code> intersection ().set ( 2, 3, 5, 7, 11 ) </code>
     * is applied to the input
     * <code> { 1, 2, 2, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 5 } </code>
     * then the result will contain
     * <code> { 2, 2, 3, 3, 3, 5, 5, 5, 5, 5 } </code>.
     * </p>
     *
     * @return A new EditSet sub-Pipeline that will be used to specify
     *         the element(s) or multi-valued object whose elements
     *         wll be intersected with each input,
     *         after which control returns to this Edit pipeline.
     *         Never null.
     */
    public abstract EditSet<STREAM, Edit<STREAM, PARENT, POOL>, POOL, ?> intersection ()
        throws ReturnNeverNull.Violation;


    // Every Subsidiary must implement
    // musaico.foundation.pipeline.Subsidiary#isEnded()


    /**
     * <p>
     * Adds an operation to the pipeline that will move a selection
     * of elements forward or backward a specific number of elements,
     * or to a specific new absolute index.
     * </p>
     *
     * @return A new sub-Pipeline that will be used to specify
     *         the chunk of elements to move, after which control
     *         will pass to a new EditMove sub-Pipeline that will
     *         be used to set either an offset by which to move the chunk,
     *         or an absolute index where the chunk will be relocated;
     *         after which control returns to this Edit pipeline.
     *         Never null.
     */
    public abstract <MOVE extends EditMove<STREAM, Edit<STREAM, PARENT, POOL>, MOVE>>
        EditVarious<STREAM, MOVE, POOL, ?> move ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Adds an operation to the pipeline that will insert padding elements
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
     * @param pad_at_index Where to insert the padding in the
     *                     value being built.  For example, to pad
     *                     before the first element,
     *                     use index <code> 0L </code>.
     *                     Or to pad before the 9th element,
     *                     use index <code> 8L </code>.
     *                     If the index is greater than the length
     *                     of an input object, then that input
     *                     generates an error.
     *                     Must be greater than or equal to 0L.
     *
     * @param target_length How many elements the input object
     *                      should have after the padding has
     *                      been inserted.  If less than or equal to
     *                      the length of the value being built
     *                      (for example, if the target length is 0 or
     *                      length - 1) then no padding
     *                      will be added.  Must be greater than
     *                      or equal to 0L.
     *
     * @return A new EditSequence sub-Pipeline that will be used to specify
     *         the individual element(s) or multi-valued object to
     *         use as padding, after which control returns to
     *         this Edit pipeline.  Never null.
     */
    public abstract <SEQUENCE extends EditSequence<STREAM, Edit<STREAM, PARENT, POOL>, POOL, SEQUENCE>>
        EditInsert<STREAM, SEQUENCE, ?> pad (
            long target_length
            )
        throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
               ReturnNeverNull.Violation;


    // Every Subsidiary must implement
    // musaico.foundation.pipeline.Subsidiary#parent()


    /**
     * <p>
     * Edit operation that concatenates a sequence of elements
     * to the end of the multi-valued object.
     * </p>
     *
     * @return A new EditSequence sub-Pipeline that will be used to specify
     *         the element(s) or multi-valued object to prepend,
     *         after which control returns to this Edit pipeline.
     *         Never null.
     */
    public abstract EditSequence<STREAM, Edit<STREAM, PARENT, POOL>, POOL, ?> prepend ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Adds an operation to the pipeline that will remove
     * elements from each each input object, if they are present.
     * </p>
     *
     * <p>
     * An entire sequence can be removed, or any element from a set
     * of elements can be removed, or a range of indices can be removed,
     * and so on.
     * </p>
     *
     * <p>
     * For example, to remove the sequence of Strings
     * <code> { "Hello", "world" } </code> from an input object,
     * <code> remove ().sequence ( "Hello", "world" ) </code>
     * might be called.  Then an object containing the Strings
     * <code> { "Hello", "world", "how", "are", "you" } </code>
     * would be output as <code> { "how", "are", "you" } </code>,
     * whereas an input containing the Strings
     * <code> { "Hello", "there", "brave", "new", "world", "Hello" } </code>
     * would be output unchanged, since the exact sequence does
     * not appear.
     * </p>
     *
     * <p>
     * By contrast, calling 
     * <code> remove ().set ( "Hello", "world" ) </code>
     * removes any element from the specified set.  So the examples above
     * would become: 
     * <code> { "Hello", "world", "how", "are", "you" } </code>
     * would be output as <code> { "how", "are", "you" } </code>,
     * and an input containing the Strings
     * <code> { "Hello", "there", "brave", "new", "world", "Hello" } </code>
     * would be output as <code> { "there", "brave", "new" } </code>.
     * </p>
     *
     * @return A new sub-Pipeline that will be used to specify
     *         the chunk of element(s) to remove from each input object,
     *         after which control returns to this Edit pipeline.
     *         Never null.
     */
    public abstract EditVarious<STREAM, Edit<STREAM, PARENT, POOL>, POOL, ?> remove ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Adds an operation to the pipeline that will remove
     * any and all elements from each each input object.
     * </p>
     *
     * <p>
     * For example, calling <code> removeAll () </code>
     * on an object containing the Strings
     * <code> { "Hello", "world", "how", "are", "you" } </code>
     * would be output as <code> {} </code>,
     * and the input containing the Strings
     * <code> { "Hello", "there", "brave", "new", "world", "Hello" } </code>
     * would also be output as <code> {} </code>.
     * </p>
     *
     * @return This Edit.  Never null.
     */
    public abstract Edit<STREAM, PARENT, POOL> removeAll ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Adds an operation to the pipeline that will replicate the elements
     * of each input object a certain number of times.
     * </p>
     *
     * <p>
     * Calling <code> repeat ( 0L ) </code> results in an empty output,
     * no matter the input.
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
     * @return This Edit.  Never null.
     */
    public abstract Edit<STREAM, PARENT, POOL> repeat (
            long repetitions
            )
        throws Parameter1.MustBeGreaterThanOrEqualToOne.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Adds an operation to the pipeline that will
     * replace a chunk of element(s) in each input object
     * with a specific sequence of replacement element(s).
     * </p>
     *
     * <p>
     * For example, calling
     * <code> replace ().select ().first ( 3L ).sequence ( 1, 2, 3 ) </code>
     * would replace the first 3 elements of each input object
     * with the numbers <code> { 1, 2, 3 } </code>.  So if an input
     * contains <code> { 10, 9, 8, 7, 6, 5 } </code>, the output will be
     * <code> { 1, 2, 3, 7, 6, 5 } </code>.
     * </p>
     *
     * @return A new sub-Pipeline that will be used to specify
     *         the chunk of element(s) to replace,
     *         after which control will pass on to an EditSequence
     *         sub-Pipeline that will be used to specify
     *         the element(s) or multi-valued object of the replacement,
     *         after which control returns to this Edit pipeline.
     *         Never null.
     */
    public abstract <SEQUENCE extends EditSequence<STREAM, Edit<STREAM, PARENT, POOL>, POOL, SEQUENCE>>
        EditVarious<STREAM, SEQUENCE, POOL, ?> replace ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Adds an operation to the pipeline that will swap one selection
     * of elements with another one.
     * </p>
     *
     * <p>
     * For example, if
     * <code> swap ().select ().first ( 3L ).select ().last ( 3L ) </code>
     * is called, then the input <code> { 10, 9, 8, 7, 6, 5, 4 } </code>
     * will produce output <code> { 6, 5, 4, 7, 10, 9, 8 } </code>.
     * </p>
     *
     * @return A new sub-Pipeline that will be used to specify
     *         the first chunk of elements to be swapped, after which
     *         control will pass to another sub-Pipeline used to
     *         specify the second chunk of elements to be swapped,
     *         after which control returns to this Edit pipeline.
     *         Never null.
     */
    public abstract <SEQUENCE_OR_SET extends EditVarious<STREAM, Edit<STREAM, PARENT, POOL>, POOL, SEQUENCE_OR_SET>>
        EditVarious<STREAM, SEQUENCE_OR_SET, POOL, ?> swap ()
        throws ReturnNeverNull.Violation;


    // Every Subsidiary must implement java.lang.Object#toString()


    /**
     * <p>
     * Adds an operation to the pipeline that will output the set union
     * (join) of a specific set with each input object.
     * </p>
     *
     * <p>
     * Multiple copies of the same element will be kept, even though
     * this is a set operation at heart.  The number of copies of element E
     * is equal to the number of E's in the input set, if any; or, if
     * the input set has no E members, then the number of copies in the
     * set parameter.
     * </p>
     *
     * <p>
     * For example, if the input contains the element "A" twice,
     * and the specified value contains three instances of "A",
     * then "A" shall appear twice in the output, since it is in the input
     * set.  On the other hand, if "B" does not appear in the input set,
     * but it appears twice in the specified set parameter, then
     * "B" will be output twice.
     * </p>
     *
     * <p>
     * For example, if <code> union ().set ( 2, 2, 3, 5, 7, 11 ) </code>
     * is applied to the input
     * <code> { 1, 3, 3, 5, 5 } </code>
     * then the result will contain
     * <code> { 1, 2, 2, 3, 3, 5, 5, 7, 11 } </code>.
     * </p>
     *
     * @return A new EditSet sub-Pipeline that will be used to specify
     *         the element(s) or multi-valued object to append,
     *         after which control returns to this Edit pipeline.
     *         Never null.
     */
    public abstract EditSet<STREAM, Edit<STREAM, PARENT, POOL>, POOL, ?> union ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Adds an operation to the pipeline that will return
     * the symmetric difference ("exclusive or")
     * between a specific set and each input object,
     * the result containing each element that is present either in
     * the specified set or in the input object,
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
     * <p>
     * For example, if <code> xor ().set ( 2, 3, 5, 7, 11, 11, 11 ) </code>
     * is applied to the input
     * <code> { 1, 2, 2, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 5 } </code>
     * then the result will contain
     * <code> { 1, 4, 4, 4, 4, 7, 11, 11, 11 } </code>.
     * </p>
     *
     * @return A new EditSet sub-Pipeline that will be used to specify
     *         the element(s) or multi-valued object to append,
     *         after which control returns to this Edit pipeline.
     *         Never null.
     */
    public abstract EditSet<STREAM, Edit<STREAM, PARENT, POOL>, POOL, ?> xor ()
        throws ReturnNeverNull.Violation;
}
