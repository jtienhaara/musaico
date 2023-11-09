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
 * An offshoot from a Countable value which can be used to filter
 * the elements, sort them, replace them, and so on, and build new
 * Countable values.
 * </p>
 *
 * <p>
 * The CountableElements is mutable.  It potentially changes with
 * every operation.  You can create a defensive copy of
 * a CountableElements with <code> duplicate () </code>, if you
 * are worried about someone else messing with your elements.
 * </p>
 *
 *
 * <p>
 * In Java every CountableElements must be Serializable in order to
 * play nicely across RMI.  However users of the CountableElements
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
public interface CountableElements<VALUE extends Object>
    extends CountableView<VALUE, VALUE, CountableElements<VALUE>>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** Contract which is violated whenever all ( ... ) receives
     *  Abnormal input. */
    public static class All
        extends ValueMustNotBeAbnormal
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for all ( ... ) operations. */
        public static final All CONTRACT =
            new All ();
    }

    /** Contract which is violated whenever all ( ... ) does not
     *  produce any results. */
    public static class MustNotBeEmpty
        extends ValueMustNotBeEmpty
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for all ( ... ) operations. */
        public static final MustNotBeEmpty CONTRACT =
            new MustNotBeEmpty ();
    }

    /**
     * <p>
     * Returns the current countable elements.
     * </p>
     *
     * @return The complete countable elements.
     *         If this Countable indices was constructed from
     *         an Error or Warning then one such is returned.
     *         Never null.
     */
    public abstract Countable<VALUE> all ()
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns the element at the specified index (if any).
     * </p>
     *
     * <p>
     * This is just a convenience version of:
     * </p>
     *
     * <pre>
     *     indices ().filter ().keep ( new One<Long> ( Long.class, index ) )
     *         .elements ().one ();
     * </pre>
     *
     * @param index The index of the element to return.  Zero-indexed.
     *              If less than or equal to <code> Countable.LAST </code>
     *              then it is treated as an offset from the last element
     *              of the countable view.  For example
     *              <code> Countable.LAST </code> will return
     *              the last element, <code> Countable.LAST - 1L </code>
     *              will return the second last element, and so on.
     *
     * @return The element at the specified index, or No value if
     *         the index is out of range.  Never null.
     */
    public abstract ZeroOrOne<VALUE> at (
                                         long index
                                         )
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns the elements at the specified indices (if any).
     * </p>
     *
     * <p>
     * This is just a convenience version of:
     * </p>
     *
     * <pre>
     *     indices ().filter ().keep ( indices )
     *         .elements ().all ();
     * </pre>
     *
     * <p>
     * If a given index is out of range then it is ignored.  For example,
     * a countable view of length 3 with elements <code> { A, B, C } </code>
     * when asked for <code> in ( { 0, 5, 1, 4, 2, 3 } ) </code>
     * will return elements <code> { A, B, C } </code>, the elements
     * at the indices <code> { 0, 1, 2 } </code> but not any elements
     * from the indices <code> { 5, 4, 3 } </code>.
     * </p>
     *
     * @param indices The countable indices of the elements to return.
     *                Zero-indexed.  If any index is
     *                less than or equal to <code> Countable.LAST </code>
     *                then it is treated as an offset from the last element
     *                of the countable view.  For example
     *                <code> Countable.LAST </code> will return
     *                the last element, <code> Countable.LAST - 1L </code>
     *                will return the second last element, and so on.
     *
     * @return The elements at the specified indices, or No value if
     *         the indices are all out of range.
     */
    public abstract Countable<VALUE> in (
                                         Countable<Long> indices
                                         )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;
}
