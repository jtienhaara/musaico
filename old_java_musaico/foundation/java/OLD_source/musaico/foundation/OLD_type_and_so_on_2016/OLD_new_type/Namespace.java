package musaico.foundation.type;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.graph.Graph;

import musaico.foundation.machine.Machine;

import musaico.foundation.value.Value;


/**
 * <p>
 * A Namespace is any Symbol which has a state Graph and can produce
 * a Machine on demand to transition through that state Graph.
 * </p>
 *
 * <p>
 * For example, a Type is a Namespace whose graph is its symbol table,
 * containing Operations, constants, and so on.
 * </p>
 *
 * <p>
 * An Operation is a Namespace whose graph determines the type(s) of
 * inputs it accepts, and in what order.
 * </p>
 *
 * <p>
 * And so on.
 * </p>
 *
 *
 * <p>
 * In Java every Symbol must be Serializable in order to play
 * nicely with RMI.
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
 * @see musaico.foundation.type.MODULE#COPYRIGHT
 * @see musaico.foundation.type.MODULE#LICENSE
 */
public interface Namespace
    extends Symbol, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * @return This Namespace's graph, which specifies the network of
     *         Symbols through which each <code> machine () </code>
     *         can transition.  A Type's graph is its symbol table,
     *         including all its Operations, constants, and so on;
     *         an Operation's graph determines the order and type(s)
     *         of input(s) it accepts; and so on.  Never null.
     */
    public abstract Graph<Symbol, Type> graph ()
        throws ReturnNeverNull.Violation;


    /**
     * @return A newly created state Machine which can transition through the
     *         Symbols in this Namespace's state <code> graph () </code>.
     *         For example, a Type returns a Machine which can be
     *         used to traverse its symbol table, looking for Operations
     *         or constants and so on.  An Operation returns a Machine
     *         which can navigate its invocation graph, which determines
     *         the type(s) and order of input(s) allowed for the Operation.
     *         And so on.  Never null.
     */
    public abstract Machine<Value<Object>, Symbol> machine ()
        throws ReturnNeverNull.Violation;
}
