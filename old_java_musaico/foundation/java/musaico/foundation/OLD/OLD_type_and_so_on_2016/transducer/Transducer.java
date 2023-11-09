package musaico.foundation.transducer;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.state.StateMachine;
import musaico.foundation.state.StateMachineBuilder;
import musaico.foundation.state.Transition;

import musaico.foundation.value.Maybe;
import musaico.foundation.value.Value;


/**
 * <p>
 * An instruction set being/to be executed by a Processor in a Musaico system.
 * </p>
 *
 * <p>
 * A Transducer is the current state of some execution, comprising:
 * </p>
 *
 * <ol>
 *   <li> Input; </li>
 *   <li> Logic; </li>
 *   <li> World; and </li>
 *   <li> Output. </li>
 * </ol>
 *
 *
 * <p>
 * The Transducer itself also acts as the input, allowing standard
 * StateMachine logic with InputTransitions to process the input,
 * while more specialized Transitions can be devised to access registers
 * and memory and so on.
 * </p>
 *
 * <p>
 * The input of a Transducer describes the structure of input values
 * to the logic (where a straight line graph describes a sequence,
 * such as parameters), as well as the input's current state,
 * representing a pointer to the last processed input.
 * </p>
 *
 * <p>
 * The logic of a Transducer is a StateMachine whose
 * Transitions describe the instructions and flow of execution,
 * and whose current state represents the current program pointer.
 * </p>
 *
 * <p>
 * The world of a Transducer represents the world at large available
 * to the Transducer, and may include state which outlives the
 * Transducer, such as registers and memory.
 * </p>
 *
 * <p>
 * The output of a Transducer is a StateMachineBuilder describing
 * the graph being built up for output from the Transducer
 * as the logic executes, as well as the state (most recently
 * added output).
 * </p>
 *
 * <p>
 * Note that Transitions are important only to the logic.  The input
 * and output are simply data structures, and their Transitions
 * are ignored completely - though the same Graphs could also be
 * used as logic in different contexts.
 * </p>
 *
 * <p>
 * The Transitions in the world define how to access various parts of
 * that world.  For example, a "registers" arc might lead to one or
 * more registers, and a "memory" arc might lead to a memory area.
 * Note that the Transitions in the world can be important, since
 * they might push down into other graphs (for example, in order to
 * ensure secure access to various parts of the graph).
 * </p>
 *
 *
 * <p>
 * In Java, every Transducer must be Seralizable in order to play
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
public class Transducer
    extends StateMachine
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final Advocate classContracts =
        new Advocate ( Transducer.class );


    // MUTABLE:
    // The node(s) to use as input to the next logic state transition(s),
    // and the last state of input used (the next input(s) are at the other
    // end(s) of the arc(s) from the state node).
    private final StateMachine input;

    // MUTABLE:
    // The logic to execute for this Transducer.  The logic to execute next is
    // embodied in the arc(s) leading out of the current state.
    private final StateMachine logic;

    // MUTABLE:
    // The world at large, including registers, memory, and so on.
    private final StateMachine world;

    // MUTABLE:
    // The output builder and current last output value.  Builds up
    // a graph as output from this Transducer.  The current state
    // of the output is the last Value added to the state machine builder.
    // Note that the state machine and its underlying graph
    // are both mutable.
    private final StateMachineBuilder output;


    /**
     * <p>
     * Creates a new Transducer from the specified input(s) and logic,
     * and creates a new output StateMachineBuilder for the Transducer.
     * </p>
     *
     * @param input The node(s) to use as input to the next
     *              logic state transition(s), and the last state
     *              of input used (the next input(s) are at the other
     *              end(s) of the arc(s) from the state node).
     *              Must not be null.
     *
     * @param logic The logic to execute for this Transducer.
     *              The logic to execute next is embodied in the arc(s)
     *              leading out of the current state.
     *              Must not be null.
     *
     * @param world The world at large, including registers, memory,
     *              and so on.  The current state is not really part
     *              of the world, it is just a pointer into the world graph.
     *              Must not be null.
     */
    public Transducer (
                       StateMachine input,
                       StateMachine logic,
                       StateMachine world
                       )
        throws ParametersMustNotBeNull.Violation
    {
        this ( input,
               logic,
               world,
               new StateMachineBuilder ( "" + logic
                                         + " ( "
                                         + input
                                         + " )" ) );
    }


    /**
     * <p>
     * Creates a new Transducer from the specified input(s), logic and
     * output(s).
     * </p>
     *
     * @param input The node(s) to use as input to the next
     *              logic state transition(s), and the last state
     *              of input used (the next input(s) are at the other
     *              end(s) of the arc(s) from the state node).
     *              Must not be null.
     *
     * @param logic The logic to execute for this Transducer.
     *              The logic to execute next is embodied in the arc(s)
     *              leading out of the current state.
     *              Must not be null.
     *
     * @param world The world at large, including registers, memory,
     *              and so on.The current state is not really part
     *              of the world, it is just a pointer into the world graph.
     *                Must not be null.
     *
     * @param output The output builder and current last output value.
     *               Builds up a graph as output from this Transducer.
     *               The current state of the output is the last Value
     *               added to the state machine builder.
     *               Must not be null.
     */
    public Transducer (
                       StateMachine input,
                       StateMachine logic,
                       StateMachine world,
                       StateMachineBuilder output
                       )
        throws ParametersMustNotBeNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation if input is null:
        super ( input == null
                    ? null
                    : input.graph (),
                input == null
                    ? null
                    : input.state () );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               input,
                               logic,
                               world,
                               output,
                               last_output );

        this.input = input;
        this.logic = logic;
        this.world = world;
        this.output = output;
        this.lastOutput = last_output;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
                                 Object object
                                 )
    {
        if ( this == object )
        {
            return true;
        }
        else if ( object == null )
        {
            return false;
        }
        else if ( this.getClass () != object.getClass () )
        {
            return false;
        }

        final Transducer that = (Transducer) object;
        if ( ! this.input.equals ( that.input ) )
        {
            return false;
        }
        else if ( ! this.logic.equals ( that.logic ) )
        {
            return false;
        }
        else if ( ! this.output.equals ( that.output ) )
        {
            return false;
        }

        final Value<?> this_last_output = this.lastOutput ();
        final Value<?> that_last_output = that.lastOutput ();
        if ( ! this_last_output.equals ( that_last_output ) )
        {
            return false;
        }

        return true;
    }


    /**
     * @see musaico.foundation.machine.AbstractMachine#forceState(java.lang.Object)
     */
    @Override
    public Transducer forceState (
                                   Value<?> state
                                   )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.input.forceState ( state );

        // There is a slim chance of deadlock here:
        // 1. We synchronize on this.lock ().
        // 2. Calling this.input.state () causes this.input to synchronize
        //    on this.input.lock ().
        // However it does protect us against interleaved transitions
        // from multiple threads.  This way we are guaranteed to end
        // up in the same state as this.input, as long as we don't
        // hit deadlock.
        synchronized ( this.lock () )
        {
            final Value<?> input_state = this.input.state ();
            super.forceState ( input_state );
        }

        return this;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        // Deliberately exclude the last output, to avoid having
        // to lock / sychronize.
        return this.logic.graph ().hashCode () * 31
            + this.input.hashCode () * 17
            + this.output.hashCode ();
    }


    /**
     * @return The node(s) to use as input to the next
     *         logic state transition(s), and the last state
     *         of input used (the next input(s) are at the other
     *         end(s) of the arc(s) from the state node).
     *         Never null.
     */
    public final StateMachine input ()
        throws ReturnNeverNull.Violation
    {
        return this.input;
    }


    /**
     * @return The most recently built output Value(s).  Each new output
     *         from executing this Transducer will add
     *         Arc(s) out of the specified states in the output builder.
     *         Never null.
     */
    public final Value<?> lastOutput ()
        throws ReturnNeverNull.Violation
    {
        synchronized ( this.lock () )
        {
            return this.lastOutput;
        }
    }


    /**
     * @param last_output Sets the most recently built output Value(s).
     *                    Each new output from executing
     *                    this Transducer will add
     *                    Arc(s) out of the specified states
     *                    in the output builder.
     *                    Must not be null.
     *
     * @return This Transducer.  Never null.
     */
    public final Transducer lastOutput (
                                         Value<?> last_output
                                         )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  last_output );

        synchronized ( this.lock () )
        {
            this.lastOutput = last_output;
        }

        return this;
    }


    /**
     * @return The logic to execute for this Transducer.
     *         The logic to execute next is embodied in the arc(s)
     *         leading out of the current state.
     *         Never null.
     */
    public final StateMachine logic ()
        throws ReturnNeverNull.Violation
    {
        return this.logic;
    }

    /**
     * @return The output builder, which builds up a graph as output
     *         from this Transducer (and possibly from other Transducers,
     *         as well).  The <code> output () </code> state(s) is/are
     *         "owned" by this Transducer, so no other Transducers will
     *         build arcs leading out of them.  Never null.
     */
    public final StateGraphBuilder output ()
        throws ReturnNeverNull.Violation
    {
        return this.output;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return ClassName.of ( this.getClass () )
            + " "
            + this.logic.graph ()
            + " ( "
            + this.input.graph ()
            + " )";
    }


    /**
     * @see musaico.foundation.Machine#transition(Object...)
     */
    @Override
    public Maybe<Value<?>> transition (
                                       StateMachine... inputs
                                       )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        final Maybe<Value<?>> maybe_state =
            this.input.transition ( inputs );

        // There is a slim chance of deadlock here:
        // 1. We synchronize on this.lock ().
        // 2. Calling this.input.state () causes this.input to synchronize
        //    on this.input.lock ().
        // However it does protect us against interleaved transitions
        // from multiple threads.  This way we are guaranteed to end
        // up in the same state as this.input, as long as we don't
        // hit deadlock.
        synchronized ( this.lock () )
        {
            final Value<?> input_state = this.input.state ();
            super.forceState ( input_state );
        }

        return maybe_state;
    }


    /**
     * @return The world at large, including registers, memory,
     *         and so on.The current state is not really part
     *         of the world, it is just a pointer into the world graph.
     *         Never null.
     */
    public final StateMachine world ()
        throws ReturnNeverNull.Violation
    {
        return this.world;
    }
}
