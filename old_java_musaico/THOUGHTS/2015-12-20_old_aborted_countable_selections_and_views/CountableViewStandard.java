package musaico.foundation.value;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.ObjectContracts;
import musaico.foundation.contract.Violation;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.Parameter3;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.domains.BoundedDomain;

import musaico.foundation.filter.Filter;

import musaico.foundation.filter.composite.Not;

import musaico.foundation.order.Order;


/**
 * <p>
 * An offshoot from a Countable value, possibly mutable, which can be used
 * to filter, sort, replace, and so on, and build new Countable values.
 * </p>
 *
 * <p>
 * A CountableView can be created to work with any type of "view items",
 * such as the elements of the Countable value, or the indices of the
 * Countable value (0, 1, 2, ...), and so on.
 * </p>
 *
 * <p>
 * Derived classes can override the methods to, for example, create
 * filtered selections and sorted selections and so on which are efficient
 * for specific types or quantities of data.
 * </p>
 *
 *
 * <p>
 * In Java every CountableView must be Serializable in order to
 * play nicely across RMI.  However users of the CountableView
 * must be careful, since the values and expected data stored inside
 * might not be Serializable.
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
 * @see musaico.foundation.value.MODULE#COPYRIGHT
 * @see musaico.foundation.value.MODULE#LICENSE
 */
public class CountableView<VALUE extends Object, VIEW_ITEM extends Object>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Every aspect of a CountableView is a CountableViewItem type: the
     * countable elements comprise a CountableViewItem type, the
     * countable indices comprise another CountableViewItem type, and so on.
     * A CountableView can be created to operate on the elements, or to
     * operate on the indices, and so on.
     * </p>
     */
    public static enum Items
    {
        /** The countable element values. */
        ELEMENTS,

        /** The countable indices. */
        INDICES;


        /**
         * @return The class of view items for the specified Countable
         *         value, such as String.class for the ELEMENTS of
         *         a Countable<String> value, or Long.class for the
         *         indices.  Never null.
         */
        public final Class<?> itemClass (
                                         IdempotentAndCountable<?> countable
                                         )
        {
            switch ( this )
            {
            case INDICES:
                return Long.class;

            case ELEMENTS:
            default:
                return countable.expectedClass ();
            }
        }


        /**
         * <p>
         * Creates a new CountableView of the elements of the
         * specified selection.
         * </p>
         *
         * @param selection The selected items (elements or indices)
         *                  which will be turned into an elements view.
         *                  Must not be null.
         *
         * @return A newly created view of the elements from the selection.
         *         Never null.
         */
        @SuppressWarnings("unchecked") // Cast selection-<VALUE, X> as need be.
        public final <VALUE extends Object>
            CountableView<VALUE, VALUE> viewElements (
                                                      CountableSelection<VALUE, ?> selection
                                                      )
        {
            final CountableSelection<VALUE, VALUE> elements;
            final CountableView<VALUE, VALUE> view;
            switch ( this )
            {
            case INDICES:
                final CountableSelection<VALUE, Long> indices =
                    (CountableSelection<VALUE, Long>) selection;
                elements = new CountableElements<VALUE> ( indices );
                view = new CountableView<VALUE, VALUE> ( elements );
                break;

            case ELEMENTS:
            default:
                elements = (CountableSelection<VALUE, VALUE>) selection;
                view = new CountableView<VALUE, VALUE> ( elements );
            }

            return view;
        }


        /**
         * <p>
         * Creates a new CountableView of the indices of the
         * specified selection.
         * </p>
         *
         * @param selection The selected items (elements or indices)
         *                  which will be turned into an indices view.
         *                  Must not be null.
         *
         * @return A newly created view of the indices from the selection.
         *         Never null.
         */
        @SuppressWarnings("unchecked") // Cast selection-<VALUE, X> as need be.
        public final <VALUE extends Object>
            CountableView<VALUE, Long> viewIndices (
                                                    CountableSelection<VALUE, ?> selection
                                                    )
        {
            final CountableSelection<VALUE, Long> indices;
            final CountableView<VALUE, Long> view;
            switch ( this )
            {
            case INDICES:
                indices = (CountableSelection<VALUE, Long>) selection;
                view = new CountableView<VALUE, Long> ( indices );
                break;

            case ELEMENTS:
            default:
                final CountableSelection<VALUE, VALUE> elements =
                    (CountableSelection<VALUE, VALUE>) selection;
                indices = new CountableIndices<VALUE> ( elements );
                view = new CountableView<VALUE, Long> ( indices );
            }

            return view;
        }
    }


    // Enforces constructor and static method parameter obligations for us.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( CountableView.class );


    // Enforces parameter obligations and so on for us.
    private final ObjectContracts contracts;

    // How to get elements from the countable (possibly by querying
    // the previous view in the chain, or possibly by querying the
    // original Countable, and possibly with filters and so on).
    private final CountableSelection<VALUE, VIEW_ITEM> selection;


    /**
     * <p>
     * Creates a new CountableView around the specified selection of
     * view items (elements or indices) of a Countable value.
     * </p>
     *
     * @param selection The selection of view items (elements or indices)
     *                  to view.  Must not be null.
     */
    public CountableView (
                          CountableSelection<VALUE, VIEW_ITEM> selection
                          )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               selection );

        this.selection = selection;

        this.contracts = new ObjectContracts ( this );
    }




    /**
     * <p>
     * Converts the specified index (possibly negative, such as
     * <code> Countable.FROM_END </code>) according to the specified
     * # of items.
     * </p>
     *
     * <p>
     * For example, <code> CountableView.index ( 9L, 10L ) </code>
     * will return <code> 9L </code>.  So will
     * <code> CountableView.index ( Countable.FROM_END, 10L ) </code>.
     * </p>
     *
     * <p>
     * This method will return -1L if the index passed in is
     * <code> Countable.AFTER_END </code>.  The caller is expected to
     * treat that index specially, since it is not within bounds of
     * the existing specified length.
     * </p>
     *
     * @param unconverted_index The index to convert.  Can be negative.
     *
     * @param length The number of items against which the specified
     *               unconverted index will be converted.
     *               Must be greater than or equal to 0L.
     *
     * @return The converted index.  Always greater than or equal to 0L
     *         as long as the unconverted index is within bounds;
     *         or -1L if the unconverted index is out of bounds.
     *         Never less than -1L.
     */
    public static long index (
                              long unconverted_index,
                              long length
                              )
        throws Parameter2.MustBeGreaterThanOrEqualToZero.Violation,
               Return.AlwaysGreaterThanOrEqualToNegativeOne.Violation
    {
        if ( unconverted_index == Countable.AFTER_END )
        {
            // Out of bounds (past the end of the countable items).
            return -1L;
        }
        else if ( unconverted_index >= length )
        {
            // Out of bounds (past end).
            return -1L;
        }
        else if ( unconverted_index >= 0L )
        {
            // In bounds and positive.
            return unconverted_index;
        }

        final long offset_from_length =
            unconverted_index - Countable.AFTER_END;
        if ( offset_from_length > length )
        {
            // Out of bounds (before beginning).
            return -1L;
        }

        final long index = length - offset_from_length;
        return index;
    }




    /**
     * <p>
     * Removes the elements of the specified countable value that are
     * contained in this countable elements, and adds the elements
     * which are not contained in this countable elements.
     * </p>
     *
     * <p>
     * Multiple copies of the same element will be kept, even though
     * this is a set operation at heart.
     * </p>
     *
     * @param that The counbtable value whose element differences will
     *             be stored in this countable elements.
     *             Must not be null.
     *
     * @return This countable elements, containing only the
     *         elements that are different between the input and
     *         that specified value.
     *         If this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         If the specified value is an Error or Warning,
     *         then a new CountableView constructed from one such,
     *         with the same violation, is returned.
     *         Never null.
     */
    public CountableView<VALUE, VIEW_ITEM> difference (
                                                       IdempotentAndCountable<VIEW_ITEM> that
                                                       )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        final CountableDifference<VALUE, VIEW_ITEM> difference =
            new CountableDifference<VALUE, VIEW_ITEM> ( this.select (),
                                                        that );
        return new CountableView<VALUE, VIEW_ITEM> ( difference );
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
                                 Object obj
                                 )
    {
        if ( obj == null )
        {
            return false;
        }
        else if ( obj == this )
        {
            return true;
        }
        else if ( ! ( obj instanceof CountableView ) )
        {
            return false;
        }

        final CountableView<?, ?> that = (CountableView<?, ?>) obj;
        final CountableSelection<?, ?> that_selection =
            that.select ();
        if ( this.selection == null )
        {
            if ( that_selection == null )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else if ( that_selection == null )
        {
            return false;
        }
        else if ( ! this.selection.equals ( that_selection ) )
        {
            return false;
        }

        // Everything is all matchy-matchy.
        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return 31 * this.selection.hashCode ();
    }


    /**
     * <p>
     * Adds the specified countable value(s) to (each of) the specified
     * location(s) in the value being built.
     * </p>
     *
     * <p>
     * For example, if the value being built is <code> 1, 2, 3, 4, 5 </code>
     * and the sub-value <code> A, B </code> is inserted at
     * index <code> { 2 } </code> then the resulting value being built
     * will be <code> { 1, 2, A, B, 3, 4, 5 } </code>.
     * </p>
     *
     * <p>
     * Or if the same sub-value is inserted into the same value being
     * built at indices <code> { 0, 1, 2, 3, 4, 5 } </code> then
     * the resulting value being built will be
     * <code> { A, B, 1, A, B, 2, A, B, 3, A, B, 4, A, B, 5, A, B } <code>.
     * </p>
     *
     * @param sub_value The element(s) to insert into the value being
     *                  built.  Must not be null.
     *
     * @param insert_at_indices The location(s) in the value being built
     *                          at which to insert the specified sub-value.
     *                          If an index is less than or equal to 0L
     *                          or greater than or equal to the length
     *                          of the countable value being built
     *                          then it is ignored.  If an index is greater
     *                          than <code> Integer.MAX_VALUE </code>
     *                          then the sub-value is appended AFTER the
     *                          element counted backward from
     *                          <code> Countable.FROM_END </code>.
     *                          If an index appears more
     *                          than once then the sub-values will be
     *                          inserted multiple times at the same
     *                          location.  Must not be null.
     *
     * @return This countable elements, with the sub-value(s) inserted
     *         at the specified index/indices.
     *         If this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         If either of the specified values is an Error or Warning,
     *         then a new CountableView constructed from one such,
     *         with the same violation, is returned.
     *         Never null.
     */
    public CountableView<VALUE, VIEW_ITEM> insert (
                                                   IdempotentAndCountable<VIEW_ITEM> sub_value,
                                                   long insert_at_index
                                                   )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        final CountableReplace<VALUE, VIEW_ITEM> insert =
            new CountableReplace<VALUE, VIEW_ITEM> ( this.select (),
                                                     insert_at_index,
                                                     insert_at_index - 1L,
                                                     sub_value );
        return new CountableView<VALUE, VIEW_ITEM> ( insert );
    }


    /**
     * <p>
     * Removes the elements of the specified countable value that are
     * not contained in both this countable elements, leaving
     * behind only the intersecting or overlapping elements.
     * </p>
     *
     * <p>
     * Multiple copies of the same element will be kept, even though
     * this is a set operation at heart.
     * </p>
     *
     * @param that The counbtable value whose element intersections will
     *             be stored in this countable elements.
     *             Must not be null.
     *
     * @return This countable elements, containing only the
     *         elements that are contained in both the input and
     *         that specified value.
     *         If this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         If the specified value is an Error or Warning,
     *         then a new CountableView constructed from one such,
     *         with the same violation, is returned.
     *         Never null.
     */
    public CountableView<VALUE, VIEW_ITEM> intersection (
                                                         IdempotentAndCountable<VIEW_ITEM> that
                                                         )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        final CountableIntersection<VALUE, VIEW_ITEM> intersection =
            new CountableIntersection<VALUE, VIEW_ITEM> ( this.select (),
                                                          that );
        return new CountableView<VALUE, VIEW_ITEM> ( intersection );
    }


    /**
     * <p>
     * Keeps only the view item(s) (elements / indices / and so on)
     * from this Countable view which match the specified filter,
     * removing all other view item(s).
     * </p>
     *
     * @param filter The Filter which will keep or discard view item(s)
     *               of this Countable view.
     *               Must not be null.
     *
     * @return A filtered view of the source CountableView, containing only
     *         the view item(s) that were KEPT by the specified filter.
     *         Can result in an empty countable view, if
     *         no view item(s) were kept by the filter.
     *         If the source Countable view was constructed from
     *         an Error or Warning then it is returned as-is.
     *         Never null.
     */
    public CountableView<VALUE, VIEW_ITEM> keep (
                                                 Filter<VIEW_ITEM> filter
                                                 )
        throws ParametersMustNotBeNull.Violation,
        ReturnNeverNull.Violation
    {
        final CountableFilter<VALUE, VIEW_ITEM> keep =
            new CountableFilter<VALUE, VIEW_ITEM> ( this.select (),
                                                    filter );
        return new CountableView<VALUE, VIEW_ITEM> ( keep );
    }


    /**
     * <p>
     * Inserts padding elements to make the value being built
     * a specific length.
     * </p>
     *
     * <p>
     * For example, if the countable elements <code> { 1, 2, 3 } </code>
     * is padded with element <code> { 0 } </code>
     * at index <code> { 0 } </code> to reach target length <code> 6 </code>,
     * the result will be <code> { 0, 0, 0, 1, 2, 3 } </code>.
     * </p>
     *
     * <p>
     * If there are multiple padding elements then each will be added
     * in sequence, followed by a repeat of the sequence, and so on,
     * until one of the elements is added to reach the final target length.
     * For example, starting from countable elements
     * <code> { 1, 2, 3 } </code>, padding the start index <code> { 0 } </code>
     * with <code> { A, B, C } </code> to reach target length <code> 7 </code>
     * would result in <code> { A, B, C, A, 1, 2, 3 } </code>.
     * </p>
     *
     * <p>
     * If the padding is to be inserted at multiple locations, then
     * one sequence of padding is inserted at the first index, then
     * one sequence at the seond index, and so on, until one sequence
     * has been inserted at each of the specified indices; then another
     * sequence is inserted at each index; and so on, until one of the
     * values inserted at one of the elements causes the countable elements
     * to reach the target length.  For example, with countable
     * countable elements <code> { 1, 2, 3 } </code>, padding both the
     * start and the end indices <code> { 0, 3 } </code> with
     * padding <code> { A, B, C } </code> to reach target length
     * <code> 11 </code> would result in
     * <code> { A, B, C, A, B, 1, 2, 3, A, B, C } </code>.
     * </p>
     *
     * @param padding The element(s) to insert at specific indices
     *                in this countable elements.  Must not be null.
     *
     * @param pad_at_indices Where to insert the padding in the
     *                       value being built.  Indices less than 0L
     *                       or greater than or equal to the length
     *                       of the countable value being built are
     *                       ignored.  An index greater than
     *                       <code> Integer.MAX_VALUE </code>
     *                       causes the the padding to be appended
     *                       AFTER the index counting backward from
     *                       <code> Countable.FROM_END </code>.
     *                       Must not be null.
     *
     * @param target_length How many elements this countable elements
     *                      should have after the padding has
     *                      been inserted.  If less than or equal to
     *                      the length of the value being built
     *                      (for example, if the target length is 0 or
     *                      or -1 or length - 1) then no padding
     *                      will be added.
     *
     * @return This CountableView, with the specified padding added.
     *         However if this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         If either of the specified values is an Error or Warning,
     *         then a new CountableView constructed from one such,
     *         with the same violation, is returned.
     *         Never null.
     */
    public CountableView<VALUE, VIEW_ITEM> pad (
                                                IdempotentAndCountable<VALUE> padding,
                                                long pad_at_index,
                                                long target_length
                                                )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        final CountablePad<VALUE, VIEW_ITEM> pad =
            new CountablePad<VALUE, VIEW_ITEM> ( this.select (),
                                                 padding,
                                                 pad_at_index,
                                                 target_length );
        return new CountableView<VALUE, VIEW_ITEM> ( pad );
    }


    /**
     * <p>
     * Returns all elements in the given range.
     * </p>
     *
     * <p>
     * For example, a Countable elements with elements
     * { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, when the range 1-3
     * is requested, would result in: { 2, 3, 4 }.
     * </p>
     *
     * <p>
     * If the end index is less than the right index then the result
     * is empty.
     * </p>
     *
     * @param start_index !!!
     *
     * @param end_index !!!
     *
     * @return This Countable elements, containing only
     *         the elements in the specified index ranges.
     *         If this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         If the specified value is an Error or Warning,
     *         then a new CountableElements constructed from one such,
     *         with the same violation, is returned.
     *         Never null.
     */
    public CountableView<VALUE, VIEW_ITEM> range (
                                                  long start_index,
                                                  long end_index
                                                  )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        final CountableRange<VALUE, VIEW_ITEM> range =
            new CountableRange<VALUE, VIEW_ITEM> ( this.select (),
                                                   start_index,
                                                   end_index );
        return new CountableView<VALUE, VIEW_ITEM> ( range );
    }


    /**
     * <p>
     * Removes the view item(s) (elements / indices / and so on)
     * from this Countable view which match the specified filter,
     * keeping all other view item(s).
     * </p>
     *
     * @param filter The Filter which will remove or keep view item(s)
     *               of this Countable view.
     *               Must not be null.
     *
     * @return A filtered view of the source CountableView, containing only
     *         the view item(s) that were not REMOVED by the specified filter.
     *         Can result in an empty countable view, if
     *         no view item(s) were kept by the filter.
     *         If the source Countable view was constructed from
     *         an Error or Warning then it is returned as-is.
     *         Never null.
     */
    public CountableView<VALUE, VIEW_ITEM> remove (
                                                   long start_index,
                                                   long end_index
                                                   )
        throws ParametersMustNotBeNull.Violation,
        ReturnNeverNull.Violation
    {
        final No<VIEW_ITEM> replace_with_nothing =
            new ValueBuilder<VIEW_ITEM> ( this.select ().itemClass () )
                                 .build ();
        final CountableReplace<VALUE, VIEW_ITEM> remove =
            new CountableReplace<VALUE, VIEW_ITEM> ( this.select (),
                                                     replace_with_nothing,
                                                     start_index,
                                                     end_index );
        return new CountableView<VALUE, VIEW_ITEM> ( remove );
    }


    /**
     * <p>
     * Creates a new Countable elements which contains the same
     * element(s) as this value, repeated the specified number
     * of times.
     * </p>
     *
     * <p>
     * Specifying 1 repeat leaves the elements as-is.
     * </p>
     *
     * <p>
     * For example, if this is a CountableView<Integer, Integer> whose
     * elements are <code> { A, B } </code>, then calling
     * <code> repeat ( 3 ) </code> would result in a new Countable
     * with elements <code> { A, B, A, B, A, B } </code>.
     * </p>
     *
     * @param repetitions The number of times to repeat the pattern
     *                    of elements.  If the number is less than
     *                    or equal to 1L then it is ignored and no
     *                    repetitions are performed.
     *
     * @return This countable elements, with its elements
     *         repeated the specified number of times.
     *         If this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         Never null.
     */
    public CountableView<VALUE, VIEW_ITEM> repeat (
                                                   long repetitions
                                                   )
        throws ReturnNeverNull.Violation
    {
        final CountableRepeat<VALUE, VIEW_ITEM> repeat =
            new CountableRepeat<VALUE, VIEW_ITEM> ( this.select (),
                                                    repetitions );
        return new CountableView<VALUE, VIEW_ITEM> ( repeat );
    }


    /**
     * <p>
     * Replaces any and all view items (elements / indices and so on)
     * matching the specified filter with the specified element value(s).
     * </p>
     *
     * <p>
     * For example, replacing all instances of <code> { B, C, D } </code>
     * with replacement <code> { X } </code> inside the countable elements
     * <code> { A, B, C, D, A, B, C, D, E } </code> would result
     * in <code> { A, X, A, X, E } </code>.
     * </p>
     *
     * @param sub_value The elements to find and replace in this
     *                  countable elements.  Must not be null.
     *
     * @param replacement The elements to insert in place of each matching
     *                    sequence.  Can be empty.  Must not be null.
     *
     * @return This countable elements, with each and every instance
     *         of the specified sub value replaced by the specified
     *         replacement sequence.
     *         If this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         If either of the specified values is an Error or Warning,
     *         then a new CountableView constructed from one such,
     *         with the same violation, is returned.
     *         Never null.
     */
    public CountableView<VALUE, VIEW_ITEM> replace (
                                                    long start_index,
                                                    long end_index,
                                                    IdempotentAndCountable<VALUE> replacement
                                                    )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        final CountableReplace<VALUE, VIEW_ITEM> replace =
            new CountableReplace<VALUE, VIEW_ITEM> ( this.select (),
                                                     start_index,
                                                     end_index,
                                                     replacement );
        return new CountableView<VALUE, VIEW_ITEM> ( replace );
    }


    /**
     * <p>
     * Reverses the order of the element(s) of this Countable elements.
     * </p>
     *
     * @return This countable elements, with its elements
     *         in reverse order.
     *         If this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         Never null.
     */
    public CountableView<VALUE, VIEW_ITEM> reverse ()
        throws ReturnNeverNull.Violation
    {
        final CountableReverse<VALUE, VIEW_ITEM> reverse =
            new CountableReverse<VALUE, VIEW_ITEM> ( this.select () );
        return new CountableView<VALUE, VIEW_ITEM> ( reverse );
    }


    // !!!;
    public CountableSelection<VALUE, VIEW_ITEM> select ()
    {
        return this.selection;
    }


    /**
     * <p>
     * Re-orders the element(s) of this Countable elements using
     * the specified rank generator to assign a rank to each indexed
     * element of this Countable elements.
     * </p>
     *
     * @param ranker Generates the rank for each index'ed element of
     *               this Countable elements.  For example, a
     *               PseudoRandom number generator could be used.
     *               The lower the ranking number generated, the earlier
     *               in the sequence the corresponding indexed element
     *               will be placed.  Ties will result in the tied
     *               indexed elements maintaining their existing relative
     *               ordering.  If the ranker runs out of ranks, then
     *               subsequent indexed elements are placed at the end.
     *               Must not be null.
     *
     * @return This Countable elements, re-ordered so that the
     *         each indexed element is ordered according to the rank
     *         generated for it by the specified ranker.
     *         If this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         Never null.
     */
    public <RANK extends Comparable<? super RANK>>
        CountableView<VALUE, VIEW_ITEM> shuffle (
                                                 Value<RANK> ranker
                                                 )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        final CountableShuffle<VALUE, VIEW_ITEM> shuffle =
            new CountableShuffle<VALUE, VIEW_ITEM> ( this.select (),
                                                     ranker );
        return new CountableView<VALUE, VIEW_ITEM> ( shuffle );
    }


    /**
     * <p>
     * Sorts the element(s) of this Countable elements according to the
     * specified Order.
     * </p>
     *
     * @param order The Order in which to sort the element(s) of this
     *              Countable elements.  Must not be null.
     *
     * @return This countable elements, with its elements sorted
     *         according to the specified order.
     *         If this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         Never null.
     */
    public CountableView<VALUE, VIEW_ITEM> sort (
                                                 Order<VIEW_ITEM> order
                                                 )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        final CountableSort<VALUE, VIEW_ITEM> sort =
            new CountableSort<VALUE, VIEW_ITEM> ( this.select (),
                                                  order );
        return new CountableView<VALUE, VIEW_ITEM> ( sort );
    }


    /**
     * <p>
     * Splits the countable value being built at the specified indices,
     * and returns a list of Countables, each one containing
     * one split segment.
     * </p>
     *
     * <p>
     * If a split is requested at index 0L, then the left side of the split
     * at index 0 will always contain No value.  If a split is requested
     * at an index greater than <code> Integer.MAX_VALUE </code>, then
     * the split will occur AFTER the element counting backward from
     * <code> Countable.FROM_END </code> (resulting in No value to the right
     * if the split is at <code> Countable.FROM_END </code>).
     * </p>
     *
     * <p>
     * For example, if the countable elements <code> { A, B, C } </code>
     * is split at indices <code> { 0, 2, LAST } </code> then the
     * four resulting Countables will be <code> {} </code> (empty),
     * <code> A, B </code>, <code> C </code> and <code> {} </code> (empty).
     * </p>
     *
     * @param split_at_indices The index or indices at which to split the
     *                         countable value being built.  Must not be null.
     *
     * @param split_index_goes_to Where to place the index around which the
     *                            split is happening: either at the start
     *                            of the subsequent list (Countable.FROM_START),
     *                            at the end of the previous list
     *                            (Countable.FROM_END), or removed (any other
     *                            index).
     *
     * @return 0 or more split Countable values.
     *         If this Countable elements was constructed from
     *         an Error or Warning then an empty list will be returned.
     *         If the specified indices countable value is an Error or Warning
     *         then an empty list will be returned.
     *         Never null.  Never contains any null elements.
     */
    public CountableView<VALUE, VIEW_ITEM>[] splitAt (
                                                      long split_at_index
                                                      )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        final CountableSplitAt<VALUE, VIEW_ITEM> split_at =
            new CountableSplitAt<VALUE, VIEW_ITEM> ( this.select (),
                                                     split_at_index );
        return split_at.split ();
    }


    /**
     * <p>
     * Splits the countable value being built by the specified element(s)
     * pattern, and returns a list of Countables, each one containing
     * one split segment.
/     * </p>
     *
     * <p>
     * For example, if the countable elements <code> { A, B, C } </code>
     * is split by pattern <code> { A, B } </code> then the
     * two resulting Countables will be <code> {} </code> (empty)
     * and <code> { C } </code>.
     * </p>
     *
     * @param split_by_sequence The pattern of element(s) which will
     *                          divide the elements of this
     *                          countable elements into a list of
     *                          countable values.  Must not be null.
     *
     * @return 0 or more split Countables.
     *         If this Countable elements was constructed from
     *         an Error or Warning then an empty list will be returned.
     *         If the specified sequence is an Error or Warning
     *         then an empty list will be returned.
     *         Never null.  Never contains any null elements.
     */
    public CountableView<VALUE, VIEW_ITEM>[]  splitBy (
                                                       IdempotentAndCountable<VIEW_ITEM> split_by_sequence
                                                       )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        final CountableSplitBy<VALUE, VIEW_ITEM> split_by =
            new CountableSplitBy<VALUE, VIEW_ITEM> ( this.select (),
                                                     split_by_sequence );
        return split_by.split ();
    }


    // !!!
    public CountableView<VALUE, VIEW_ITEM>[] splitInto (
                                                        long num_countable_views
                                                        )
    {
        final CountableSplitInto<VALUE, VIEW_ITEM> split_into =
            new CountableSplitInto<VALUE, VIEW_ITEM> ( this.select (),
                                                       num_countable_views );
        return split_into.split ();
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString ()
    {
        return "View of " + this.selection;
    }


    /**
     * @return The type of view items available in this CountableView:
     *         elements, indices, and so on.  Never null.
     */
    public CountableView.Items type ()
    {
        return this.select ().type ();
    }


    /**
     * <p>
     * Adds all elements from that specified value which are not already
     * contained in this countable value being built, leaving the union
     * of this and that.
     * </p>
     *
     * <p>
     * Multiple copies of the same element will be kept, even though
     * this is a set operation at heart.
     * </p>
     *
     * @param that The counbtable value whose element unions will
     *             be stored in this countable elements.
     *             Must not be null.
     *
     * @return This countable elements, containing all the
     *         elements from the input and that specified value.
     *         If this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         If the specified value is an Error or Warning,
     *         then a new CountableView constructed from one such,
     *         with the same violation, is returned.
     *         Never null.
     */
    public CountableView<VALUE, VIEW_ITEM> union (
                                                  IdempotentAndCountable<VIEW_ITEM> that
                                                  )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        final CountableUnion<VALUE, VIEW_ITEM> union =
            new CountableUnion<VALUE, VIEW_ITEM> ( this.select (),
                                                   that );
        return new CountableView<VALUE, VIEW_ITEM> ( union );
    }
}
