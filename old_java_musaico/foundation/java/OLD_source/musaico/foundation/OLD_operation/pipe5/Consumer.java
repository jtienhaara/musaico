package musaico.foundation.operation;

import java.io.Serializable;

import java.util.Iterator;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.term.Maybe;
import musaico.foundation.term.Term;


/**
 * <p>
 * The tail of a line of Pipes, which is fed by the rest of the pipeline,
 * and buffers the output.
 * </p>
 *
 * <p>
 * A Consumer has no stateless template, it is always a stateful instance.
 * </p>
 *
 *
 * <p>
 * In Java, every Operation must implement equals (), hashCode ()
 * and toString().
 * </p>
 *
 * <p>
 * In Java every Operation must be Serializable in order to
 * play nicely across RMI.  However in general it is recommended
 * that only stateless Pipes be passed around over RMI, and even
 * then, do so with caution.  The Terms which feed parameters
 * and inputs to Pipes, although Serializable themselves,
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
public interface Consumer<VALUE extends Object>
    extends Pipe<VALUE, VALUE>, Iterator<Term<VALUE>>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    // Every Pipe must implement
    // musaico.foundation.term.Operation#apply(musaico.foundation.term.Term)

    // Every Pipe must implement
    // musaico.foundation.operation.Pipe#close()

    // Every Operation must implement java.lang.Object#equals(java.lang.Object)

    // Every Operation must implement
    // musaico.foundation.term.Operation#errorType()

    // Every Operation must implement java.lang.Object#hashCode()


    /**
     * @see java.util.Iterator#hasNext()
     *
     * @return True if there is/are any un-consumed Term(s)
     *         in this Consumer's buffer; false if there are
     *         no more buffered Terms to be had.
     */
    public abstract boolean hasNext ();


    // Every Pipe must implement
    // musaico.foundation.operation.Pipe#input()

    // Every Operation must implement
    // musaico.foundation.term.Operation#inputType()


    /**
     * @see java.util.Iterator#next()
     *
     * @return The next Term in this Consumer's buffer.  Never null.
     *
     * @throws ReturnNeverNull.Violation If there are no more Terms
     *                                   in this Consumer's buffer.
     */
    public abstract Term<VALUE> next ()
        throws ReturnNeverNull.Violation;


    /**
     * @see musaico.foundation.operation.Pipe#output()
     *
     * @return This Consumer.  Never null.
     */
    public abstract Consumer<VALUE> output ()
        throws ReturnNeverNull.Violation;


    // Every Operation must implement
    // musaico.foundation.term.Operation#outputType()

    // Every Pipe must implement
    // musaico.foundation.operation.Pipe#parameters()


    /**
     * @see java.util.Iterator#remove()
     *
     * @throws UnsupportedOperationException This is NOT a mutable Iterator.
     */
    public abstract void remove ()
        throws UnsupportedOperationException;


    // Every Pipe must implement
    // musaico.foundation.operation.Pipe#sideEffects()

    // Every Operation must implement java.lang.Object#toString()
}
