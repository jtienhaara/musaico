package musaico.foundation.machine;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Contract;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.graph.Arc;
import musaico.foundation.graph.Graph;
import musaico.foundation.graph.NodeMustBeInGraph;
import musaico.foundation.graph.SubGraph;

import musaico.foundation.value.Countable;
import musaico.foundation.value.Maybe;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueClass;
import musaico.foundation.value.ValueViolation;

import musaico.foundation.value.builder.ValueBuilder;

import musaico.foundation.value.classes.StandardValueClass;

import musaico.foundation.value.finite.No;



/**
 * <p>
 * A simple Machine which transitions across an Arc only when the input(s)
 * or trigger(s) meet the Contract requirements of that Arc.
 * </p>
 *
 *
 * <p>
 * In Java, every Machine must be Serializable in order
 * to play nicely over RMI.
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
 * @see musaico.foundation.machine.MODULE#COPYRIGHT
 * @see musaico.foundation.machine.MODULE#LICENSE
 */
public class InputsMachine<INPUT_VALUE extends Object, STATE extends Object>
    extends AbstractMachine<Value<INPUT_VALUE>, STATE, Contract<Value<?>, ?>>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new InputsMachine.
     * </p>
     *
     * @param name The name of this Machine.  Must not be null.
     *
     * @param graph The graph whose nodes are the possible states of
     *              this new InputsMachine, and whose arcs can be
     *              traversed by this InputsMachine.  Must not be null.
     *
     * @param input_value_class The type of inputs which induce state
     *                          transitions in this machine, such as
     *                          StandardValueClass<String> for a Machine
     *                          triggered by text inputs, and so on.
     *                          Must not be null.
     */
    public InputsMachine (
                          String name,
                          Graph<STATE, Contract<Value<?>, ?>> graph,
                          ValueClass<INPUT_VALUE> input_value_class
                          )
        throws ParametersMustNotBeNull.Violation
    {
        super ( name,                  // name
                graph,                 // graph
                new StandardValueClass<Value<INPUT_VALUE>> (
                    Value.class,          // value_class
                    new No<INPUT_VALUE> ( // none
                                         input_value_class // value_class
                                          ) ) );
    }


    /**
     * <p>
     * Creates a new InputsMachine.
     * </p>
     *
     * @param name The name of this Machine.  Must not be null.
     *
     * @param graph The graph whose nodes are the possible states of
     *              this new InputsMachine, and whose arcs can be
     *              traversed by this InputsMachine.  Must not be null.
     *
     * @param input_value_class The type of inputs which induce state
     *                          transitions in this machine, such as
     *                          StandardValueClass<String> for a Machine
     *                          triggered by text inputs, and so on.
     *                          Must not be null.
     *
     * @param state The initial state of this new InputsMachine.
     *              Must be a node in the specified graph.
     *              Must not be null.
     */
    public InputsMachine (
                          String name,
                          Graph<STATE, Contract<Value<?>, ?>> graph,
                          ValueClass<INPUT_VALUE> input_value_class,
                          STATE state
                          )
        throws ParametersMustNotBeNull.Violation,
               NodeMustBeInGraph.Violation
    {
        super ( name,                  // name
                graph,                 // graph
                new StandardValueClass<Value<INPUT_VALUE>> (//input_value_class
                    Value.class,          // value_class
                    new No<INPUT_VALUE> ( // none
                                         input_value_class // value_class
                                          ) ),
                state );               // state
    }


    /**
     * <p>
     * Creates a new InputsMachine.
     * </p>
     *
     * @param name The name of this Machine.  Must not be null.
     *
     * @param graph The graph whose nodes are the possible states of
     *              this new InputsMachine, and whose arcs can be
     *              traversed by this InputsMachine.  Must not be null.
     *
     * @param input_value_class The type of inputs which induce state
     *                          transitions in this machine, such as
     *                          StandardValueClass<String> for a Machine
     *                          triggered by text inputs, and so on.
     *                          Must not be null.
     *
     * @param state The initial state of this new InputsMachine.
     *              Must be a node in the specified graph.
     *              Must not be null.
     *
     * @param graph_value_class The ValueClass describing the
     *                          Graph underpinning this machine.
     *                          Must not be null.
     */
    public InputsMachine (
                          String name,
                          Graph<STATE, Contract<Value<?>, ?>> graph,
                          ValueClass<INPUT_VALUE> input_value_class,
                          STATE state,
                          ValueClass<Graph<STATE, Contract<Value<?>, ?>>> graph_value_class
                          )
        throws ParametersMustNotBeNull.Violation,
               NodeMustBeInGraph.Violation
    {
        super ( name,                  // name
                graph,                 // graph
                new StandardValueClass<Value<INPUT_VALUE>> (//input_value_class
                    Value.class,          // value_class
                    new No<INPUT_VALUE> ( // none
                                         input_value_class // value_class
                                          ) ),
                state,                 // state
                graph_value_class );   // graph_value_class
    }


    /**
     * @see musaico.foundation.machine.Machine#state(java.lang.Object)
     */
    @Override
    public InputsMachine<INPUT_VALUE, STATE> state (
            STATE state
            )
        throws ParametersMustNotBeNull.Violation,
               NodeMustBeInGraph.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  state );

        this.contracts ().check ( new NodeMustBeInGraph<STATE> ( this.graph () ),
                                  state );

        return new InputsMachine<INPUT_VALUE, STATE> (
                       this.name (),              // name
                       this.graph (),             // graph
                       this.inputValueClass ().none ().valueClass (), // input_value_class
                       state,                     // state
                       this.graphValueClass () ); // graph_value_class
    }


    /**
     * @see musaico.foundation.machine.AbstractMachine#transitionFromArc(java.lang.Object, java.lang.Object[])
     */
    @Override
    @SuppressWarnings("unchecked") // Possible heap pollution V<INPUT_VALUE>...
    protected Value<Arc<STATE, Contract<Value<?>, ?>>> transitionArcs (
            STATE start_state,
            Value<INPUT_VALUE> ... input_values
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        STATE state = start_state;
        final ValueClass<Arc<STATE, Contract<Value<?>, ?>>> arc_value_class =
            new StandardValueClass<Arc<STATE, Contract<Value<?>, ?>>> (
                Arc.class,                              // value_class
                new Arc<STATE, Contract<Value<?>, ?>> ( // None
                    this.graph ().nodeValueClass ().none (),
                    this.graph ().arcValueClass ().none (),
                    this.graph ().nodeValueClass ().none () ) );
        final ValueBuilder<Arc<STATE, Contract<Value<?>, ?>>> builder =
            new ValueBuilder<Arc<STATE, Contract<Value<?>, ?>>> (
                arc_value_class
                );

        final List<Arc<STATE, Contract<Value<?>, ?>>> transition_arcs =
            new ArrayList<Arc<STATE, Contract<Value<?>, ?>>> ();

        for ( Value<INPUT_VALUE> input_value : input_values )
        {
            final Value<Arc<STATE, Contract<Value<?>, ?>>> arcs =
                this.graph ().arcs ( state );

            Arc<STATE, Contract<Value<?>, ?>> input_matches_arc = null;
            ValueViolation violation = null;
            for ( Arc<STATE, Contract<Value<?>, ?>> arc : arcs )
            {
                final Contract<Value<?>, ?> contract = arc.arc ();
                try
                {
                    if ( contract.filter ( input_value ).isKept () )
                    {
                        input_matches_arc = arc;
                        break;
                    }
                }
                catch ( ValueViolation v )
                {
                    if ( violation == null )
                    {
                        violation = v;
                    }
                }
                catch ( Exception e )
                {
                    // Could be a ClassCastException...
                    // For example expects Value<String> but is Value<Integer>.
                    // Sigh.
                    if ( violation == null )
                    {
                        violation =
                            new ValueViolation (
                                                contract,        // contract
                                                e.getMessage (), // description
                                                arc,             // plaintiff
                                                input_value,     // evidence
                                                e );             // cause
                    }
                }
            }

            if ( input_matches_arc == null )
            {
                // Can't go any further, the input is invalid.
                final Maybe<Arc<STATE, Contract<Value<?>, ?>>> transition_arc;
                if ( violation != null )
                {
                    // Something blew up while checking input against contract.
                    transition_arc = builder.buildViolation ( violation );
                }
                else
                {
                    // If we have more than one successful transition
                    // triggered by multiple inputs, then return
                    // a Partial result with the Arcs that will be
                    // successfully triggered.  If the first input
                    // fails to trigger any transitions then return No Arcs.
                    final Graph<STATE, Contract<Value<?>, ?>> graph =
                        new StandardGraph<STATE, Contract<Value<?>, ?>> (
                            this.graph ().nodeValueClass (), // node_class
                            this.graph ().arcValueClass (),  // arc_class
                            start_state,                     // entry_node
                            state,                           // exit_node
                            transition_arcs );               // arcs
                    final Arc<STATE, Contract<Value<?>, ?>> partial_arc =
                        new Arc<STATE, Contract<Value<?>, ?>> (
                            start_state, // from
                            
                    final SubGraph<STATE, Contract<Value<?>, ?>> sub_graph =
                        new SubGraph<STATE, Contract<Value<?>, ?>> (
                            graph,         // graph
                            partial_arc ); // arc
        throws ParametersMustNotBeNull.Violation,
               Parameter3.MustContainNoNulls.Violation,
               NodesMustBeConnected.Violation,
               ArcMustNotLeadToEntryNode.Violation,
               ArcMustNotLeadFromExitNode.Violation
                    final Arc<STATE, Contract<Value<?>, ?>> []
                    transition_arc = builder.buildPartial ();
                }

                return transition_arcs;
            }
        }

        // Return zero or more transition Arcs (one per input).
        final Countable<Arc<STATE, Contract<Value<?>, ?>>> transition_arcs =
            builder.build ();

    public StandardGraph (
                          ValueClass<NODE> node_class,
                          ValueClass<ARC> arc_class,
                          NODE entry_node,
                          NODE exit_node,
                          Arc<NODE, ARC> ... arcs
                          )
        throws ParametersMustNotBeNull.Violation,
               Parameter3.MustContainNoNulls.Violation,
               NodesMustBeConnected.Violation,
               ArcMustNotLeadToEntryNode.Violation,
               ArcMustNotLeadFromExitNode.Violation

        return transition_arcs;
    }
}
