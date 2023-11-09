package musaico.foundation.wiring;

import java.io.Serializable;

import musaico.foundation.container.Container;

import musaico.foundation.contract.guarantees.Return;


/**
 * <p>
 * The data to be transported between Conductors in Musaico wiring.
 * </p>
 *
 * <p>
 * Unless you know what you are doing (!), implement Carrier as
 * immutable (for example, by extending ImmutableContainer).
 * Carriers are NOT designed to pass updates back over remove wires.
 * So if you implement a mutable Carrier, then pass it over an
 * HTTP wire to be modified, don't expect to see the value updated
 * locally.  Switching between a local simulator wire and a real
 * HTTP wire will then produce very different results...
 * </p>
 *
 *
 * <p>
 * In Java, every Carrier must be Serializable in order to play
 * nicely over RMI.  However, be warned that the elements contained
 * in a Carrier might not be Serializable.
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
 * @see musaico.foundation.wiring.MODULE#COPYRIGHT
 * @see musaico.foundation.wiring.MODULE#LICENSE
 */
public interface Carrier
    extends Container<Object>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** Empty, immutable Carrier. */
    public static final Carrier NONE = new NoCarrier ();

    /** Empty Carriers array.  Can be returned from pull (),
        passed to push (), and so on. */
    public static final Carrier [] NO_CARRIERS = new Carrier [ 0 ];


    // Every Carrier must implement:
    // @see musaico.foundation.container.Container#append(java.lang.Object[])

    // Every Carrier must implement:
    // @see musaico.foundation.container.Container#clear()

    // Every Carrier must implement:
    // @see musaico.foundation.container.Container#contains(java.lang.Object[])

    // Every Carrier must implement:
    // @see musaico.foundation.container.Container#contains(musaico.foundation.filter.Filter)

    // Every Carrier must implement:
    // @see musaico.foundation.container.Container#duplicate()

    // Every Carrier must implement:
    // @see musaico.foundation.container.Container#duplicateImmutable()

    // Every Carrier must implement:
    // @see musaico.foundation.container.Container#duplicateMutable()

    // Every Carrier must implement:
    // @see musaico.foundation.container.Container#elementType()

    // Every Carrier must implement:
    // @see musaico.foundation.container.Container#elements()

    // Every Carrier must implement:
    // @see musaico.foundation.container.Container#elements(java.lang.Class, java.lang.Object[])

    // Every Carrier must implement:
    // @see java.lang.Object#equals(java.lang.Object)

    // Every Carrier must implement:
    // @see musaico.foundation.container.Container#filter(musaico.foundation.filter.elements.ElementsFilter)

    // Every Carrier must implement:
    // @see musaico.foundation.container.Container#filterContainer(musaico.foundation.filter.Filter)

    // Every Carrier must implement:
    // @see musaico.foundation.container.Container#filterElements()

    // Every Carrier must implement:
    // @see java.lang.Object#hashCode()

    // Every Carrier must implement:
    // @see musaico.foundation.container.Container#insert(int, java.lang.Object[])

    // Every Carrier must implement:
    // @see musaico.foundation.container.Container#insert(musaico.foundation.order.Order, java.lang.Object[])

    // Every Carrier must implement:
    // @see java.lang.Iterable#iterator()

    // Every Carrier must implement:
    // @see musaico.foundation.container.Container#numElements()

    // Every Carrier must implement:
    // @see musaico.foundation.container.Container#order()

    // Every Carrier must implement:
    // @see musaico.foundation.container.Container#orThrow()

    // Every Carrier must implement:
    // @see musaico.foundation.container.Container#prepend(java.lang.Object[])

    // Every Carrier must implement:
    // @see musaico.foundation.container.Container#remove(java.lang.Object[])

    // Every Carrier must implement:
    // @see musaico.foundation.container.Container#reverse()

    // Every Carrier must implement:
    // @see musaico.foundation.container.Container#set(java.lang.Object[])

    // Every Carrier must implement:
    // @see musaico.foundation.container.Container#sort(musaico.foundation.order.Order)

    public abstract Tags tags ()
        throws Return.NeverNull.Violation;

    // Every Carrier must implement:
    // @see java.lang.Object#toString()

    // Every Carrier must implement:
    // @see musaico.foundation.container.Container#version()
}
