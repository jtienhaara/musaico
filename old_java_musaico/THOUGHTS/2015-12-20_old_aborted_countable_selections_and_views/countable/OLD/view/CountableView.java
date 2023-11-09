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
 * An offshoot from a Countable value, possibly mutable, which can be used
 * to filter, sort, replace, and so on, and build new Countable values.
 * </p>
 *
 * <p>
 * The CountableView is mutable.  It potentially changes
 * with every operation, depending on the view.
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
public interface CountableView<VALUE extends Object, VIEW_ITEM extends Object, VIEW extends CountableView<VALUE, VIEW_ITEM, VIEW>>
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * @return A newly created copy of this CountableView, which
     *         can be modified independently of this one.
     *         Never null.
     */
    public abstract VIEW duplicate ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns the value (element or index and so on)
     * of this CountableView at a specific index.
     * </p>
     *
     * @param value_index The index of the value to return.
     *                    0 is the index of the first value, 1 the index
     *                    of the second value, and so on.
     *                    Negative indices are treated as offsets from the end
     *                    of this CountableView, where Long.MIN_VALUE
     *                    is after the last value, Long.MIN_VALUE + 1
     *                    is the Nth value, Long.MIN_VALUE + 2
     *                    is the (N - 1)th value, and so on.
     *
     * @return The One value at the specified index in this CountableView,
     *         or No value if no such index exists in this
     *         CountableView.  Never null.
     */
    public abstract ZeroOrOne<VIEW_ITEM> at (
                                             long value_index
                                             )
        throws ReturnNeverNull.Violation;


    /**
     * @return A view of the elements, allowing the caller to query,
     *         filter, compare or modify the contents based on element
     *         criteria.  Note that the view(s) never modify the original
     *         value; any mutable views operate on their own copies of
     *         the value's element(s).  Can be this view, if this
     *         is already an element view; otherwise a newly created one.
     *         Never null.
     */
    public abstract <NEW_VIEW extends CountableView<VALUE, VALUE, NEW_VIEW>>
        CountableView<VALUE, VALUE, NEW_VIEW> elements ()
        throws ReturnNeverNull.Violation;


    /**
     * @return A view of the indices, allowing the caller to query,
     *         filter, compare or modify the contents based on index
     *         criteria.  Note that the view(s) never modify the original
     *         value; any mutable views operate on their own copies of
     *         the value's index(ices).  Can be this view, if this
     *         is already an indices view; otherwise a newly created one.
     *         Never null.
     */
    public abstract <NEW_VIEW extends CountableView<VALUE, Long, NEW_VIEW>>
        CountableView<VALUE, Long, NEW_VIEW> indices ()
        throws ReturnNeverNull.Violation;


    /**
     * @return The number of items (elements / indices / and so on,
     *         depending on this class) in this Countable view.
     *         Always 0 or greater unless this
     *         countable value view was constructed from an Error or Warning.
     *         Never null.
     */
    public abstract long length ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation;


    /**
     * <p>
     * Returns an array of items (elements / indices / and so on,
     * depending on this class) contructed from the specified template
     * array and the contents of this countable view.
     * </p>
     *
     * @see java.util.Collection#toArray(java.lang.Object[])
     *
     * @param template The array template used to fill or construct the
     *                 array to return.  Can contain null values.
     *                 Must not be null.
     *
     * @return The array of values from this countable view.
     *         A defensive copy is returned, so modification is fine.
     *         Never null.
     */
    public abstract VIEW_ITEM [] toArray (
                                          VIEW_ITEM [] template
                                          )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns a list of items (elements / indices / and so on,
     * depending on this class) from this countable view.
     * </p>
     *
     * @return The list of elements / indices / and so on from
     *         this countable view, in the same order
     *         as they are in this countable value view.
     *         A defensive copy is returned, so modification is fine.
     *         Never null.
     */
    public abstract List<VIEW_ITEM> toList ()
        throws ReturnNeverNull.Violation;

    /**
     * <p>
     * Returns a map of items (elements / indices / and so on,
     * depending on this class) from this countable view,
     * each one initially mapped to the same default value.
     * </p>
     *
     * @param default_value The value to which each element/index/and so on
     *                      key will be mapped.  Must not be null.
     *
     * @return The mapped values from this countable view,
     *         with the key set in the same order as they are
     *         in this countable value view.  A LinkedHashMap
     *         is returned in order to ensure consistent
     *         ordering when iterating through the element/index/and so on
     *         keys.  A defensive copy is returned, so modification is fine.
     *         Never null.
     */
    public abstract <MAP_TO_VALUE extends Object>
        LinkedHashMap<VIEW_ITEM, MAP_TO_VALUE> toMap (
                                                      MAP_TO_VALUE default_value
                                                      )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;

    /**
     * <p>
     * Returns a set of items (elements / indices / and so on,
     * depending on this class) from this countable view.
     * </p>
     *
     * @return The set of values from this countable view,
     *         in the same order as they are in this countable value view.
     *         A LinkedHashSet is returned in order to ensure consistent
     *         ordering when iterating through the elements/indices/
     *         and so on.  A defensive copy is returned, so modification
     *         is fine.  Never null.
     */
    public abstract LinkedHashSet<VIEW_ITEM> toSet ()
        throws ReturnNeverNull.Violation;


    /**
     * @return A newly created Countable value containing the items
     *         (elements / indices / and so on, depending on this class)
     *         from this countable view.
     *         If this Countable view was constructed from
     *         an Error or Warning then one such is returned.
     *         Never null.
     */
    public abstract Countable<VIEW_ITEM> value ()
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;
}
