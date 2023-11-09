package musaico.foundation.pipeline;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.Parameter3;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;

import musaico.foundation.order.Order;


/**
 * <p>
 * Captures either a set or a sequence of or a selected range of element(s),
 * helping to build up an Edit subsidiary pipeline.
 * </p>
 *
 * @see musaico.foundation.pipeline.EditSequence
 * @see musaico.foundation.pipeline.EditSet
 * @see musaico.foundation.pipeline.Select
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
public interface EditVarious<STREAM extends Object, PARENT extends Parent<STREAM, PARENT>, POOL extends Object, SUB extends EditVarious<STREAM, PARENT, POOL, SUB>>
    extends EditSequence<STREAM, PARENT, POOL, SUB>, EditSet<STREAM, PARENT, POOL, SUB>, Select<STREAM, PARENT>, Subsidiary<STREAM, SUB, STREAM, PARENT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    // Every Select must implement
    // musaico.foundation.pipeline.Select#all()

    // Every Select must implement
    // musaico.foundation.pipeline.Select#at(long)

    /**
     * @see musaico.foundation.pipeline.Select#duplicate(musaico.foundation.pipeline.Parent)
     *
     * @see musaico.foundation.pipeline.Subsidiary#duplicate(musaico.foundation.pipeline.Parent)
     */
    @Override
    public abstract SUB duplicate (
            PARENT parent
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    // Every Subsidiary must implement
    // musaico.foundation.pipeline.Subsidiary#end()

    // Every Subsidiary must implement java.lang.Object#equals(java.lang.Object)

    // Every Select must implement
    // musaico.foundation.pipeline.Select#first()

    // Every Select must implement
    // musaico.foundation.pipeline.Select#first(long)

    // Every Select must implement
    // musaico.foundation.pipeline.Select#fromEnd()

    // Every Subsidiary must implement java.lang.Object#hashCode()

    // Every Subsidiary must implement
    // musaico.foundation.pipeline.Subsidiary#isEnded()

    // Every Select must implement
    // musaico.foundation.pipeline.Select#last()

    // Every Select must implement
    // musaico.foundation.pipeline.Select#last(long)

    // Every Select must implement
    // musaico.foundation.pipeline.Select#middle()

    // Every Select must implement
    // musaico.foundation.pipeline.Select#middle(long)

    // Every Select must implement
    // musaico.foundation.pipeline.Select#neighbourhood()

    // Every Select must implement
    // musaico.foundation.pipeline.Select#noElements()

    // Every Subsidiary must implement
    // musaico.foundation.pipeline.Subsidiary#parent()

    // Every Select must implement
    // musaico.foundation.pipeline.Select#range(long, long)

    // Every EditSequence must implement
    // musaico.foundation.pipeline.EditSequence#sequence(java.lang.Object)

    // Every EditSequence must implement
    // musaico.foundation.pipeline.EditSequence#sequence(java.lang.Object[])

    // Every EditSet must implement
    // musaico.foundation.pipeline.EditSet#set(java.lang.Object)

    // Every EditSet must implement
    // musaico.foundation.pipeline.EditSet#set(java.lang.Object[])

    // Every Subsidiary must implement java.lang.Object#toString()


    /**
     * <p>
     * Returns a Where pipeline, which can be used
     * to remove or replace elements based on filter(s).
     * </p>
     *
     * @param filters Zero or more Filters to be applied.  Can be empty.
     *                Must not be null.  Must not contain any null elements.
     *
     * @return A new Where sub-pipeline.  Never null.
     */
    @SuppressWarnings("unchecked") // Generic varargs possible heap pollution.
    public abstract Where<STREAM, PARENT> where (
            Filter<STREAM> ... filters
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation;
}
