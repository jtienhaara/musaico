package musaico.foundation.processor;

import java.io.Serializable;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.UncheckedViolation;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.graph.Graph;

import musaico.foundation.transaction.Transaction;


/**
 * <p>
 * A node in a Musaico architecture graph which is capable of executing
 * certain sets of Transactions, possibly on another machine or in a
 * different source programming language or with access to a different
 * set of libraries, and so on.
 * </p>
 *
 * <p>
 * A Processor in a Musaico architecture graph is loosely modeled on
 * the central processing unit and other, more specialize processors
 * in a physical computer.  It provides the ability to execute
 * the Transitions (opcodes) which make the system do things
 * to provide functionality of some kind.
 * </p>
 *
 * <p>
 * Typically a Musaico architecture will have some more or less
 * general purpose Processors, which can execute common types of
 * Transitions in, say, C, Java and JavaScript; as well as zero or
 * more specialized Processors to interact with external libraries
 * or to perform optimized application-specific logic.
 * </p>
 *
 * <p>
 * The architecture graph might contain Processors running in different
 * threads, in different processes, on different physical or
 * virtual machines, and so on.  The nodes of the graph may be inspected
 * from any other Processor; but the Transition arcs might be opaque,
 * not usable except in specific types of Processors.  This is why
 * each Transaction has "affinity" to a particular Processor or type
 * of Processors.
 * </p>
 *
 *
 * <p>
 * In Java, every Processor must be Serializable in order to play
 * nicely over RMI.
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
 * @see musaico.foundation.typing.MODULE#COPYRIGHT
 * @see musaico.foundation.typing.MODULE#LICENSE
 */
public interface Processor
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * @return A Contract which dictates which type(s) of Transactions
     *         can be executed by this Processor.  For example, a
     *         Processor written in C would only be able to execute
     *         C-language Transactions; and it might also only be
     *         capable of executing specific types of Transitions,
     *         to provide access to a specific library.
     *         Never null.
     */
    public abstract Contract<Transaction, ? extends UncheckedViolation> contract ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Executes the specified Transaction, adding to the output builder,
     * and creating zero or more new Transactions to push onto the stack.
     * </p>
     *
     * @param transaction The Transaction for this Processor to execute.
     *                    Must not be null.
     *
     * @return The Transaction(s) for the next step(s) of logic with
     *         the next input(s) and output(s).  Can include
     *         the specified Transaction, if, for example, it has
     *         pushed other Transactions onto the stack, or if it
     *         has been put to sleep in order to wait for the next
     *         step to be executed by a specific (type of) Processor.
     *         Never null.
     */
    public abstract Graph<Transaction, Void> execute (
            Transaction transaction
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;
}
