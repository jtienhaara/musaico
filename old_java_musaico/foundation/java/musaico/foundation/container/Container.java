package musaico.foundation.container;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.filter.Filter;

import musaico.foundation.filter.elements.ElementsFilter;

import musaico.foundation.order.Order;


/**
 * <p>
 * An iterable, mutable or immutable list of elements,
 * all of the same base class.
 * </p>
 *
 *
 * <p>
 * In Java, every Container must be Serializable in order to play
 * nicely over RMI.  However, be warned that the elements contained
 * in a Container might not be Serializable.
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
 * @see musaico.foundation.container.MODULE#COPYRIGHT
 * @see musaico.foundation.container.MODULE#LICENSE
 */
public interface Container<ELEMENT extends Object>
    extends Iterable<ELEMENT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * During <code> Container.insert ( ... ) </code>, should
     * an existing value at the same index be overwritten, or
     * pushed back?
     * </p>
     */
    public static enum InsertMode
    {
        /** Push the element at the same index back. */
        INSERT,

        /** Overwrite the element at the same index. */
        OVERWRITE;
    }


    /**
     * <p>
     * Appends the specified element(s) to the end.
     * </p>
     *
     * @param elements The elements to add to the end.
     *                 Must not be null.
     *                 CAN contain null elements.
     *
     * @return Either a new Container, if this Container is
     *         immutable and the specified elements are not
     *         zero-length; or this Container, if the
     *         elements have been added in-place.
     *         If the specified elements array is null,
     *         then an empty Container will be returned.
     *         Never null.
     */
    @SuppressWarnings("unchecked") // heap pollution generic varargs.
    public abstract Container<ELEMENT> append (
            ELEMENT ... elements
            )
        throws Return.NeverNull.Violation;

    /**
     * <p>
     * Removes all of the elements.
     * </p>
     *
     * @return Either a new Container, if this Container is
     *         immutable and a new, empty Container has been
     *         constructed; or this Container, if the elements
     *         have been cleared in-place.  Never null.
     */
    public abstract Container<ELEMENT> clear ()
        throws Return.NeverNull.Violation;

    /**
     * <p>
     * Returns true if this Container contains all of
     * the specified element(s).
     * </p>
     *
     * @param elements 0 or more elements, all of which must
     *                 be in this Container.  Must not be null.
     *                 CAN contain null elements.
     *
     * @return True if this Container contains all of the
     *         specified element(s); false if at least 1
     *         of the specified element(s) is not in this
     *         Container.  Returns true if he specified
     *         elements array is empty.
     */
    @SuppressWarnings("unchecked") // heap pollution generic varargs.
    public abstract boolean contains (
            ELEMENT ... elements
            );

    /**
     * <p>
     * Returns true if this Container contains 1 or more
     * element(s) matching the specified Filter.
     * </p>
     *
     * @param element_filter The Filter to apply to each
     *                       element of this Container.
     *                       The specified filter is applied
     *                       to every element, even if the
     *                       filter is an ElementsFilter.
     *                       Must not be null.
     *
     * @return True if this Container has at least 1 element
     *         matching the specified element_filter;
     *         false if no elements were KEPT by the filter.
     *         If element_filter is null or misbehaves,
     *         then false will be returned.
     */
    public abstract boolean contains (
            Filter<?> filter
            );

    /**
     * <p>
     * Creates an identical duplicate of this Container.
     * </p>
     *
     * @return A new Container with the same elements in the
     *         same order as this one.  Never null.
     */
    public abstract Container<ELEMENT> duplicate ()
        throws Return.NeverNull.Violation;

    /**
     * <p>
     * Creates an immutable duplicate of this Container.
     * </p>
     *
     * @return A new Container with the same elements in the
     *         same order as this one, immutable.  Never null.
     */
    public abstract Container<ELEMENT> duplicateImmutable ()
        throws Return.NeverNull.Violation;

    /**
     * <p>
     * Creates an mutable duplicate of this Container.
     * </p>
     *
     * @return A new Container with the same elements in the
     *         same order as this one, mutable.  Never null.
     */
    public abstract Container<ELEMENT> duplicateMutable ()
        throws Return.NeverNull.Violation;

    /**
     * @return The common base Class describing every element
     *         of this Container.  Never null.
     */
    public abstract Class<ELEMENT> elementType ()
        throws Return.NeverNull.Violation;

    /**
     * @return The elements in this Container.  Never null.
     *         CAN include null elements.
     */
    public abstract ELEMENT [] elements ()
        throws Return.NeverNull.Violation;

    /**
     * <p>
     * Casts the elements in this Container to the specified
     * type.
     * </p>
     *
     * @param as_type The type of elements to return.  Must be
     *                assignable from this Container's
     *                <code> elementType () </code>.
     *
     * @param default_value The array to return if the specified
     *                      element_type is not assignable from
     *                      this Container's <code> element_type </code>.
     *                      Can be null or contain null elements,
     *                      if you're foolish.
     *
     * @return The elements in this Container, each cast
     *         to the specified element type; or the specified
     *         default value, if the specified type is
     *         incompatible with this Container's
     *         <code> elementType () </code>.  Can return
     *         null only if the specified default_value is
     *         null, and only if the default_value is returned.
     *         CAN contain null elements.
     */
    @SuppressWarnings("unchecked") // heap pollution generic varargs.
    public abstract <AS extends Object> AS [] elements (
            Class<AS> as_type,
            AS ... default_value
            );

    // Every Container must implement:
    // @see java.lang.Object#equals(java.lang.Object)

    /**
     * <p>
     * Filters the elements of this Container.
     * </p>
     *
     * <p>
     * If this Container is immutable, then a new Container
     * will be returned unless all elements are KEPT.
     * </p>
     *
     * <p>
     * If this Container is mutable, then the elements will
     * be filtered in-place.
     * </p>
     *
     * @param elements_filter The ElementsFilter to apply to all
     *                        the elements of this Container.
     *                        Must not be null.
     *
     * @return The filtered Container, containing only
     *         the KEPT elements.
     *         If container_filter is null or misbehaves,
     *         then an empty Container will be returned.
     *         Never null.
     */
    public abstract Container<ELEMENT> filter (
            ElementsFilter<ELEMENT> filter
            )
        throws Return.NeverNull.Violation;

    /**
     * <p>
     * Filters this Container as one container.
     * </p>
     *
     * @param container_filter The Filter to apply to this
     *                         whole Container.  Typically --
     *                         though not necessarily -- a
     *                         <code> ContainerFilter </code>.
     *                         Must not be null.
     *
     * @return 0 Containers if this Container is DISCARDed by
     *         the specified Filter; or 1 Container if this
     *         Container is KEPT by the specified Filter.
     *         If container_filter is null or misbehaves,
     *         then 0 Containers will be returned.
     *         Never null.
     */
    public abstract Container<ELEMENT> [] filterContainer (
            Filter<?> filter
            )
        throws Return.NeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation;

    /**
     * <p>
     * Filters the elements of this Container.
     * </p>
     *
     * <p>
     * If this Container is immutable, then a new Container
     * will be returned unless all elements are KEPT.
     * </p>
     *
     * <p>
     * If this Container is mutable, then the elements will
     * be filtered in-place.
     * </p>
     *
     * @param element_filter The Filter to apply to each
     *                       element of this Container.
     *                       The specified filter is applied
     *                       to every element, even if the
     *                       filter is an ElementsFilter.
     *                       Must not be null.
     *
     * @return The filtered Container, containing only
     *         the KEPT elements.
     *         If container_filter is null or misbehaves,
     *         then an empty Container will be returned.
     *         Never null.
     */
    public abstract Container<ELEMENT> filterElements (
            Filter<?> filter
            )
        throws Return.NeverNull.Violation;

    /**
     * <p>
     * Returns a new Container with the element(s) considered,
     * by the sort order of this haystack Container, to be equal to
     * the specified needle element.
     * </p>
     *
     * @param order The Order to search by.  Must be equal to
     *              this Container's Order.  This Container's
     *              Order will be used to perform the actual
     *              search.  Must not be null.
     *
     * @param needle The element to search for.  Must not be null.
     *
     * @return Either a Container containing the matching
     *         element(s), or an empty Container, if no
     *         elements matched, or an ErrorContainer if the
     *         parameters were invalid.  Never null.
     */
    public abstract Container<ELEMENT> find (
            Order<ELEMENT> order,
            ELEMENT needle
            )
        throws Return.NeverNull.Violation;

    // Every Container must implement:
    // @see java.lang.Object#hashCode()

    /**
     * <p>
     * Returns a Container holding the first element(s)
     * of this Container.
     * </p>
     *
     * @param num_elements How many of the first ("head")
     *                     elements of this Container to
     *                     return.  Must be greater than
     *                     or equal to 0.
     *
     * @return Either a new Container, if this Container is
     *         immutable and between 1 and  numElements () - 1
     *         head elements have been requested; or an
     *         ErrorContainer, if less than 1 or more than
     *         numElements () have been requested; or
     *         this Container, either if numElements ()
     *         head elements have been requested or if this
     *         Container is a MutableContainer.
     *         Never null.
     */
    public abstract Container<ELEMENT> head (
            int num_elements
            )
        throws Return.NeverNull.Violation;

    /**
     * <p>
     * Inserts the specified element(s) at the specified
     * index.
     * </p>
     *
     * @param insert_mode Whether to insert (push back)
     *                    an element at the same index,
     *                    or overwrite (replace) it.
     *                    Must not be null.
     *
     * @param index The index at which to insert the
     *              elements.  If the index is greater
     *              than or equal to 0, then it is
     *              relative to the start of the Container.
     *              If the index is less than 0, then it
     *              is added to <code> numElements </code>
     *              to find an offset from the end of
     *              the Container.  If non-negative, then
     *              the index must be between 0 and
     *              <code> numElements () - 1 </code>.
     *              If negative, then the index must be
     *              between <code> -1 </code> (the end
     *              of the Container) and
     *              <code> 0 - numElements () </code>
     *              (the start of the Container).
     *
     * @param elements The elements to insert.
     *                 Must not be null.
     *                 CAN contain null elements.
     *
     * @return Either a new Container, if this Container is
     *         immutable and the specified elements are not
     *         zero-length; or this Container, if the
     *         elements have been added in-place.
     *         If the elements array is null or the index is
     *         invalid, then an empty Container will be
     *         returned.  Never null.
     */
    @SuppressWarnings("unchecked") // heap pollution generic varargs.
    public abstract Container<ELEMENT> insert (
            Container.InsertMode insert_mode,
            int index,
            ELEMENT ... elements
            )
        throws Return.NeverNull.Violation;

    /**
     * <p>
     * Inserts the specified element(s) according to
     * the specified sorting Order.
     * </p>
     *
     * @param insert_mode Whether to insert (push back)
     *                    an element at the same index,
     *                    or overwrite (replace) it.
     *                    Must not be null.
     *
     * @param order The sort Order by which to search for
     *              the insertion index for each element.
     *              Must not be null.  If the order is
     *              not the same as this Container's
     *              1 <code> order () </code>, then an
     *              empty Container will be returned.
     *
     * @param elements The elements to insert.
     *                 Must not be null.
     *                 CAN contain null elements.
     *
     * @return Either a new Container, if this Container is
     *         immutable and the specified elements are not
     *         zero-length; or this Container, if the
     *         elements have been added in-place.
     *         If the elements array is null or the order
     *         is not the same as this Container's 1
     *         <code> order () </code>, then an empty Container
     *         will be returned.  Never null.
     */
    @SuppressWarnings("unchecked") // heap pollution generic varargs.
    public abstract Container<ELEMENT> insert (
            Container.InsertMode insert_mode,
            Order<ELEMENT> order,
            ELEMENT ... elements
            )
        throws Return.NeverNull.Violation;

    // Every Container must implement:
    // @see java.lang.Iterable#iterator()

    /**
     * @return The number of elements in this Container.
     *         Always 0 or greater.
     */
    public abstract int numElements ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation;

    /**
     * @return The 1 sort Order of this Container, if any;
     *         or 0 sort Orders, if this Container has
     *         not been kept in a specific sorted Order.
     *         Never null.  Never contains any null elements.
     */
    public abstract Order<ELEMENT> [] order ()
        throws Return.NeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation;

    /**
     * <p>
     * Throws a ContainerException if this is an ErrorContainer;
     * otherwise returns this Container.
     * </p>
     *
     * @return This Container, unless it is an ErrorContainer.
     *         Never null.
     *
     * @throws The Exception that caused this ErrorContainer,
     *         such as a Contract Violation or a runtime
     *         Exception.
     */
    public abstract Container<ELEMENT> orThrow ()
        throws RuntimeException,
               Return.NeverNull.Violation;

    /**
     * <p>
     * Prepends the specified element(s) to the beginning.
     * </p>
     *
     * @param elements The elements to add to the start.
     *                 Must not be null.
     *                 CAN contain null elements.
     *
     * @return Either a new Container, if this Container is
     *         immutable and the specified elements are not
     *         zero-length; or this Container, if the
     *         elements have been added in-place.
     *         If the specified elements array is null,
     *         then an empty Container will be returned.
     *         Never null.
     */
    @SuppressWarnings("unchecked") // heap pollution generic varargs.
    public abstract Container<ELEMENT> prepend (
            ELEMENT ... elements
            )
        throws Return.NeverNull.Violation;

    /**
     * <p>
     * Removes the specified elements.
     * </p>
     *
     * @param elements The elements to remove.
     *                 Must not be null.
     *                 CAN contain null elements.
     *
     * @return Either a new Container, if this Container is
     *         immutable and the specified elements array
     *         is not zero-length; or this Container,
     *         if the elements have been removed in-place.
     *         If the specified elements array is null,
     *         then an empty Container will be returned.
     *         Never null.
     */
    @SuppressWarnings("unchecked") // heap pollution generic varargs.
    public abstract Container<ELEMENT> remove (
            ELEMENT ... elements
            )
        throws Return.NeverNull.Violation;

    /**
     * <p>
     * Reverses the order of elements.
     * </p>
     *
     * <p>
     * If this is an ordered Container (<code> order ().length == 1 </code>)
     * then the order is reversed.
     * </p>
     *
     * @return Either a new Container, if this Container is
     *         immutable and the elements have been reversed
     *         into a new Container; or this Container, if the
     *         elements have been reversed in-place.
     *         Never null.
     */
    public abstract Container<ELEMENT> reverse ()
        throws Return.NeverNull.Violation;

    /**
     * <p>
     * Replaces all elements with the specified list.
     * </p>
     *
     * @param elements The new list of elements.
     *                 Must not be null.
     *                 CAN contain null elements.
     *
     * @return Either a new Container, if this Container is
     *         immutable and the specified elements have
     *         been set in a new Container; or this Container,
     *         if the elements have been set in-place.
     *         If the specified elements array is null,
     *         then an empty Container will be returned.
     *         Never null.
     */
    @SuppressWarnings("unchecked") // heap pollution generic varargs.
    public abstract Container<ELEMENT> set (
            ELEMENT ... elements
            )
        throws Return.NeverNull.Violation;

    /**
     * <p>
     * Sorts the elements of this Container.
     * </p>
     *
     * <p>
     * If this Container is immutable, then a new Container
     * will be returned unless the order remains unchanged.
     * </p>
     *
     * <p>
     * If this Container is mutable, then the elements will
     * be filtered in-place.
     * </p>
     *
     * @param order The Order of elements to apply to this Container.
     *              Must not be null.
     *
     * @return The sorted Container, containing the elements
     *         arranged in the specified order.
     *         If order is null or misbehaves,
     *         then an empty Container will be returned.
     *         Never null.
     */
    public abstract Container<ELEMENT> sort (
            Order<ELEMENT> order
            )
        throws Return.NeverNull.Violation;

    /**
     * <p>
     * Returns a Container holding the last element(s)
     * of this Container.
     * </p>
     *
     * @param num_elements How many of the last ("tail")
     *                     elements of this Container to
     *                     return.  Must be greater than
     *                     or equal to 0.
     *
     * @return Either a new Container, if this Container is
     *         immutable and between 1 and  numElements () - 1
     *         tail elements have been requested; or an
     *         ErrorContainer, if less than 1 or more than
     *         numElements () have been requested; or
     *         this Container, either if numElements ()
     *         tail elements have been requested or if this
     *         Container is a MutableContainer.
     *         Never null.
     */
    public abstract Container<ELEMENT> tail (
            int num_elements
            )
        throws Return.NeverNull.Violation;

    // Every Container must implement:
    // @see java.lang.Object#toString()

    /**
     * @return A version number for this Container.  Immediately after
     *         construction, a Container might have version 0.  Every
     *         time a change is made to a mutable Container, its
     *         version is incremented.  Every time a new Container is
     *         returned from an operation on an immutable Container,
     *         it has a version 1 greater than its antecedent.
     *         However the version stops incrementing at
     *         <code> Integer.MAX_VALUE </code>.  If you've seriously
     *         produced that many versions of a Container, then
     *         congratulations!  Always greater than or equal to 0.
     */
    public abstract int version ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation;
}
