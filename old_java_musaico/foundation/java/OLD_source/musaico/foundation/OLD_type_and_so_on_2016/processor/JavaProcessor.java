package musaico.foundation.processor;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Contract;
import musaico.foundation.contract.UncheckedViolation;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.graph.Arc;
import musaico.foundation.graph.Graph;

import musaico.foundation.state.StateGraphBuilder;
import musaico.foundation.state.StateMachine;

import musaico.foundation.transaction.Transaction;


/**
 * <p>
 * A Processor which can execute any Transaction with logic designed
 * for execution inside a Java Virtual Machine.
 * </p>
 *
 * <p>
 * A Transaction whose logic graph contains NonJavaTransitions cannot
 * be executed in a JavaProcessor.
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
public class JavaProcessor
    implements Processor, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    // Checks constructor and static method obligations.
    private static final Advocate classContracts =
        new Advocate ( JavaProcessor.class );


    // Checks method obligations and guarantees.
    private final Advocate contracts;

    // The contract which ensures only Transactions which can be
    // executed inside a JVM are accepted.
    private final Contract<Transaction, ? extends UncheckedViolation> contract;


    /**
     * <p>
     * Creates a new JavaProcessor with the default "must be executable
     * inside a JVM" contract.
     * </p>
     */
    public JavaProcessor ()
    {
        this ( LogicMustBeExecutableInJava.CONTRACT );
    }


    /**
     * <p>
     * Creates a new JavaProcessor.
     * </p>
     *
     * @param contract The contract which ensures all logic
     *                 executed by the new JavaProcessor can
     *                 be executed in Java Virtual Machine.
     *                 Must not be null.
     */
    public JavaProcessor (
                          Contract<Transaction, ? extends UncheckedViolation> contract
                          )
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               contract );

        this.contract = contract;

        this.contracts = new Advocate ( this );
    }


    /**
     * @see musaico.foundation.processor.Processor#contract()
     */
    @Override
    public final Contract<Transaction, ? extends UncheckedViolation> contract ()
        throws ReturnNeverNull.Violation
    {
        return this.contract;
    }


    /**
     * @return This JavaProcessor's Advocate, which can be used by
     *         derived classes to enforce parameter obligations and
     *         return value guarantees and so on are met.  Never null.
     */
    protected final Advocate contracts ()
    {
        return this.contracts;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
                           Object object
                           )
    {
        if ( object == this )
        {
            return true;
        }
        else if ( object == null )
        {
            return false;
        }
        else if ( ! ( object instanceof JavaProcessor ) )
        {
            return false;
        }

        final JavaProcessor that = (JavaProcessor) object;
    }


    /**
     * @see musaico.foundation.processor.Processor#execute(musaico.foundation.transaction.Transaction)
     */
    @Override
    public Graph<Transaction, Void> execute (
            Transaction transaction
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               transaction );

        final StateMachine logic = transaction.logic ();
        this.contracts.check ( this.contract,
                               logic );

        final StateMachine input = transaction.input ();
        final StateGraphBuilder output = transaction.output ();
        Value<?> last_output = transaction.lastOutput ();

        Maybe<Value<?>> maybe_state;
        while ( ( maybe_state =
                  machine.transition ( input ) )
                instanceof ZeroOrOne )
        {
            // Either 0 or 1 outputs:
            final Value<?> next_output = maybe_state.orNull ();
            if ( next_output != null )
            {
                output.from ( last_output )
                      .to ( next_output );
                last_output = next_output;
            }
        }

        if ( logic.isAtExitState () )
        {
            if ( input.isAtExitState () )
            {
                // Successful execution.
                final Graph<Value<?>, Transition> output_graph =
                    output.build ();
                !!! Something something stack something something;
            }
            else
            {
                // Too many inputs.
                // Maybe more Transactions to push onto the stack?
                !!!!;
            }
        }
        else if ( input.isAtExitState () )
        {
            // We ran out of inputs before the logic finished
            // executing.
            !!!;
        }
        else
        {
            // We still have more inputs and more logic.
            // Maybe more Transactions to push onto the stack?
            !!!!!;
        }

        !!!;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        int hash_code = 31 * this.getClass ().getName ().hashCode ();
        hash_code += this.contract.getClass ().getName ().hashCode ();
        return hash_code;
    }


    /**
     * @see java.lang.Object#toString ()
     */
    @Override
    public String toString ()
    {
        final String maybe_special_contract;
        if ( this.contract == LogicMustBeExecutableInJava.class
             && this.getClass () == JavaProcessor.class )
        {
            maybe_special_contract = "";
        }
        else
        {
            // A standard JavaProcessor with some kind of special
            // contract on the kinds of Transactions it allows.
            // For example, maybe this processor only works inside
            // a servlet environment.
            maybe_special_contract =
                + " [ " + this.contract.toString () + " ]";
        }

        return this.getClass ().getSimpleName () + maybe_special_contract;
    }
}
