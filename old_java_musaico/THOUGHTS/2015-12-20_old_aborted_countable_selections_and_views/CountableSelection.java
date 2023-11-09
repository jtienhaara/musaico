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

import musaico.foundation.order.Order;


/**
 * <p>
 * An offshoot from a Countable value which can create views to filter,
 * sort, replace, and so on, the elements or indices of the value,
 * or to create other new representations of the Countable value,
 * such as arrays, Lists, and so on.
 * </p>
 *
 * <p>
 * A CountableSelection can be created to select any type of "selected items",
 * such as the elements of the Countable value, or the indices of the
 * Countable value (0, 1, 2, ...), and so on.
 * </p>
 *
 *
 * <p>
 * In Java every CountableSelection must be Serializable in order to
 * play nicely across RMI.  However users of the CountableSelection
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
public interface CountableSelection<VALUE extends Object, SELECTED_ITEM extends Object>
    extends Iterable<SELECTED_ITEM>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Returns all selected item(s) (elements or indices and so on).
     * </p>
     *
     * @return A new Countable value containing all selected items
     *         (elements or indices and so on) from this CountableSelection.
     *         If this CountableSelection was constructed from
     *         an Error or Warning then a new such will be returned.
     *         Never null.
     */
    public abstract IdempotentAndCountable<SELECTED_ITEM> all ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns the selected item (element or index and so on)
     * of this CountableSelection at a specific index.
     * </p>
     *
     * @param index The index of the selected item to return.
     *              0 is the index of the first selected item, 1 the index
     *              of the second selected item, and so on.
     *              Negative indices are treated as offsets from the end
     *              of this CountableSelection, where Long.MIN_VALUE
     *              is after the last selected item, Long.MIN_VALUE + 1
     *              is the Nth selected item, Long.MIN_VALUE + 2
     *              is the (N - 1)th selected item, and so on.
     *
     * @return The One selected item at the specified index in this
     *         CountableSelection, or No value if no such index exists
     *         in this CountableSelection.  Never null.
     */
    public abstract ZeroOrOne<SELECTED_ITEM> at (
                                                 long index
                                                 )
        throws ReturnNeverNull.Violation;


    /**
     * @return A view of the elements, allowing the caller to query,
     *         filter, compare or modify the contents based on element
     *         criteria.  Note that the view(s) never modify the original
     *         value.  Never null.
     */
    public abstract CountableView<VALUE, VALUE> elements ()
        throws ReturnNeverNull.Violation;


    /**
     * @return The One very first selected item (element or index and so on),
     *         or No selected item if this CountableSelection is empty.
     *         Never null.
     */
    public abstract ZeroOrOne<SELECTED_ITEM> first ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns true if this selection contains a selected item
     * (element or index and so on) at the specified index.
     * </p>
     *
     * @param index The index of the selected item to check.
     *              0 is the index of the first selected item, 1 the index
     *              of the second selected item, and so on.
     *              Negative indices are treated as offsets from the end
     *              of this CountableSelection, where Long.MIN_VALUE
     *              is after the last selected item, Long.MIN_VALUE + 1
     *              is the Nth selected item, Long.MIN_VALUE + 2
     *              is the (N - 1)th selected item, and so on.
     *
     * @return True if this selection contains the specified selected item
     *         index, false otherwise.
     */
    public abstract boolean has (
                                 long index
                                 )
        throws ReturnNeverNull.Violation;


    /**
     * @return A view of the indices, allowing the caller to query,
     *         filter, compare or modify the contents based on index
     *         criteria.  Note that the view(s) never modify the original
     *         value.  Never null.
     */
    public abstract CountableView<VALUE, Long> indices ()
        throws ReturnNeverNull.Violation;


    /**
     * @return The class of selected items (elements or indices)
     *         in this selection.  Never null.
     */
    public Class<SELECTED_ITEM> itemClass ()
        throws ReturnNeverNull.Violation;


    /**
     * @return The One very last selected item (element or index and so on),
     *         or No selected item if this CountableSelection is empty.
     *         Never null.
     */
    public abstract ZeroOrOne<SELECTED_ITEM> last ()
        throws ReturnNeverNull.Violation;


    /**
     * @return The number of items (elements / indices / and so on,
     *         depending on this class) in this CountableSelection.
     *         Always 0 or greater unless this
     *         countable value selection was constructed from
     *         an Error or Warning.  Never null.
     */
    public abstract long length ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation;


    /**
     * @return The one or two middle selected item(s) (elements or indices
     *         and so on), or No selected item if this CountableSelection is
     *         empty.  If this selection has an odd number of elements,
     *         then one middle selected item will be returned.  If this
     *         selection has an even number of elements, then two middle
     *         selected items will be returned (except in the case of 0
     *         elements, in which case No selected item is returned).
     *         Never null.
     */
    public abstract IdempotentAndCountable<SELECTED_ITEM> middle ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns exactly One selected item (element / index / and so on)
     * if this CountableSelection has one selected item,
     * or No selected items if this selection has less or more
     * than one selected items.
     * </p>
     *
     * @return The One selected item in this selection,
     *         or No selected item if this selection does not have
     *         exactly one selected item.  Never null.
     */
    public abstract ZeroOrOne<SELECTED_ITEM> one ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns a Countable value with all of the selected items
     * (elements or indices) from the selection that are in the
     * specified range of indices.
     * </p>
     *
     * <p>
     * For example, to retrieve all the selected items with indices
     * (3, 4, 5, 6, 7), call <code> CountableSelection.range ( 3, 7 ) </code>.
     * </p>
     *
     * <p>
     * Indices are interpreted the same way as for the
     * <code> at () </code> method.  For example, invoking
     * <code> CountableSelection.range ( Countable.FROM_END,
     *                                   Countable.FROM_END + 2L ) </code>,
     * the selected items in the range (very last item in the selection) to
     * (3rd last item in the selection) would be returned.
     * </p>
     *
     * <p>
     * If any part of the range is outside the bounds of this selection,
     * then it will be clamped accordingly.
     * </p>
     *
     * @param start_index The first index in the range of selected items
     *                    (elements or indices) to be returned.
     *                    Can be negative.
     *
     * @param end_index The last index in the range of selected items
     *                  (elements or indices) to be returned.
     *                  Can be negative.
     *
     * @return A Countable value containing the selected items
     *         (elements or indices) in the specified range.
     *         Can be empty.  Never null.
     */
    public abstract IdempotentAndCountable<SELECTED_ITEM> range (
                                                                 long start_index,
                                                                 long end_index
                                                                 )
        throws ReturnNeverNull.Violation;


    /**
     * @return The IdempotentAndCountable value from which this selection
     *         was originally derived.  For example, if this is
     *         a selection of the indices of an Idempotent Countable value,
     *         then the original value will be returned.
     *         Or if this is a selection of elements from an Immutable
     *         Countable value, filtered by some criteria, then the original,
     *         un-filtered value will be returned.  Or if this is
     *         a selection of a Mutable or otherwise Unidempotent
     *         value's elements, then a snapshot of that value, taken
     *         at or before the time this selection was created, shall be
     *         returned.  And so on.  Never null.
     */
    public abstract IdempotentAndCountable<VALUE> source ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns an array of items (elements / indices / and so on,
     * depending on this class) contructed from the specified template
     * array and the contents of this CountableSelection.
     * </p>
     *
     * @see java.util.Collection#toArray(java.lang.Object[])
     *
     * @param template The array template used to fill or construct the
     *                 array to return.  Can contain null values.
     *                 Must not be null.
     *
     * @return The array of selected items from this CountableSelection.
     *         A defensive copy is returned, so modification is fine.
     *         Never null.
     */
    public abstract SELECTED_ITEM [] toArray (
                                              SELECTED_ITEM [] template
                                              )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns a list of items (elements / indices / and so on,
     * depending on this class) from this CountableSelection.
     * </p>
     *
     * @return The list of elements / indices / and so on from
     *         this CountableSelection, in the same order
     *         as they are in this selection.
     *         A defensive copy is returned, so modification is fine.
     *         Never null.
     */
    public abstract List<SELECTED_ITEM> toList ()
        throws ReturnNeverNull.Violation;

    /**
     * <p>
     * Returns a map of selected items (elements / indices / and so on,
     * depending on this class) from this CountableSelection,
     * each one initially mapped to the same default value.
     * </p>
     *
     * @param default_value The value to which each element/index/and so on
     *                      key will be mapped.  Must not be null.
     *
     * @return The mapped selected items from this CountableSelection,
     *         with the key set in the same order as they are
     *         in this selection.  A LinkedHashMap is returned in
     *         order to ensure consistent ordering when iterating
     *         through the element/index/and so on keys.
     *         A defensive copy is returned, so modification is fine.
     *         Never null.
     */
    public abstract <MAP_TO_VALUE extends Object>
        LinkedHashMap<SELECTED_ITEM, MAP_TO_VALUE> toMap (
                                                          MAP_TO_VALUE default_value
                                                          )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;

    /**
     * <p>
     * Returns a set of items (elements / indices / and so on,
     * depending on this class) from this CountableSelection.
     * </p>
     *
     * @return The set of selected items from this CountableSelection,
     *         in the same order as they are in this selection.
     *         A LinkedHashSet is returned in order to ensure consistent
     *         ordering when iterating through the elements/indices/
     *         and so on.  A defensive copy is returned, so modification
     *         is fine.  Never null.
     */
    public abstract LinkedHashSet<SELECTED_ITEM> toSet ()
        throws ReturnNeverNull.Violation;


    /**
     * @return The type of selected items available in this CountableSelection:
     *         elements, indices, and so on.  Never null.
     */
    public abstract CountableView.Items type ()
        throws ReturnNeverNull.Violation;


    /**
     * @return A new Countable value containing only unique
     *         selected items (elements or indices and so on), with
     *         all duplicates removed.
     *         If this CountableSelection was constructed from
     *         an Error or Warning then one such is returned.
     *         Never null.
     */
    public abstract IdempotentAndCountable<SELECTED_ITEM> unique ()
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;

}
