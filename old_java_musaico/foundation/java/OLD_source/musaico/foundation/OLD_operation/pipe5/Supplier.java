package musaico.foundation.operation;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.term.Term;


/**
 * <p>
 * The head of a line of Pipes, which feeds an input
 * into the pipeline.
 * </p>
 *
 * <p>
 * A Supplier has no stateless template, it is always a stateful instance.
 * </p>
 *
 *
 * <p>
 * In Java, every Supplier must implement equals (), hashCode ()
 * and toString().
 * </p>
 *
 * <p>
 * In Java every Supplier must be Serializable in order to
 * play nicely across RMI.  However be warned that the Term(s)
 * stored by a Supplier, although Serializable themselves,
 * can contain non-Serializable elements.
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
 * @see musaico.foundation.operation.MODULE#COPYRIGHT
 * @see musaico.foundation.operation.MODULE#LICENSE
 */
public interface Supplier<VALUE extends Object>
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Applies the Term(s) to the specified Pipe,
     * processing inductively until completion, before returning.
     * </p>
     *
     * @param pipe The Pipe which will receive input(s) from this Supplier,
     *             and which will be executed inductively
     *             until it is complete or there are no more Terms
     *             to feed, at which point <code> close () </code>
     *             is invoked on the Pipe.  Must not be null.
     */
    public abstract void applyTo (
            Pipe<VALUE, ?> pipe
            )
        throws ParametersMustNotBeNull.Violation;


    // Every Supplier must implement java.lang.Object#equals(java.lang.Object)

    // Every Supplier must implement java.lang.Object#hashCode()

    // Every Supplier must implement java.lang.Object#toString()
}
