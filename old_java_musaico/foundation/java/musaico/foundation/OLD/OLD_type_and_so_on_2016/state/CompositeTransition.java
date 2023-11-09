package musaico.foundation.state;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Contract;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;
import musaico.foundation.domains.EqualTo;

import musaico.foundation.filter.Domain;
import musaico.foundation.filter.composite.Or;

import musaico.foundation.graph.Arc;
import musaico.foundation.graph.Graph;

import musaico.foundation.value.Countable;
import musaico.foundation.value.NotOne;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueClass;
import musaico.foundation.value.ValueViolation;
import musaico.foundation.value.ZeroOrOne;

import musaico.foundation.value.classes.StandardValueClass;

import musaico.foundation.value.finite.No;
import musaico.foundation.value.finite.One;

import musaico.foundation.value.iterators.IteratorMustBeFinite;


/**
 * <p>
 * A Transition which encapsulates an entire TapeMachine.
 * </p>
 *
 *
 * <p>
 * In Java every Transition must be Serializable in order to
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
 * @see musaico.foundation.typing.MODULE#COPYRIGHT
 * @see musaico.foundation.typing.MODULE#LICENSE
 */
public class CompositeTransition
    extends AbstractTransition
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final Advocate classContracts =
        new Advocate ( CompositeTransition.class );

    // ValueClass for String step names.
    private static final ValueClass<String> STEP_NAME_VALUE_CLASS =
        new StandardValueClass<String> ( // value_class
            String.class, // element_class
            "" );         // none


    // The graph to traverse whenever a transition attempt is made.
    private final Graph<Value<?>, Transition> graph;

    // The index of the Tape.DEBUG machine.
    private final int indexDebug;


    /**
     * <p>
     * Creates a new CompositeTransition sequence of component Transitions.
     * </p>
     *
     * @param components The sequence of Transitions to attempt whenever
     *                   a transition attempt is made.  Must not be null.
     */
    public CompositeTransition (
                                Transition ... components
                                )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        this ( createGraph (
                            components ) );
    }

    private static final Graph<Value<?>, Transition> createGraph (
            Transition ... sequence
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               (Object) sequence );
        classContracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               sequence );

        final StateGraphBuilder builder =
            new StateGraphBuilder ( "composite" );

        int transition_num = 0;
        Value<?> previous_state = builder.entry ();
        for ( Transition transition : sequence )
        {
            transition_num ++;
            final String step_name = "composite[" + transition_num + "]";
            final One<String> next_state =
                new One<String> (
                    STEP_NAME_VALUE_CLASS, // value_class
                    step_name );

            builder.add ( previous_state,
                          transition,
                          next_state );

            previous_state = next_state;
        }

        builder.from ( previous_state );
        builder.toExit ();

        final Graph<Value<?>, Transition> graph =
            builder.build ();
        return graph;
    }


    /**
     * <p>
     * Creates a new CompositeTransition sequence of component Transitions.
     * </p>
     *
     * @param components The sequence of Transitions to attempt whenever
     *                   a transition attempt is made.  Must not be null.
     */
    public CompositeTransition (
                                Iterable<Transition> components
                                )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        this ( createGraph (
                            components ) );
    }

    private static final Graph<Value<?>, Transition> createGraph (
            Iterable<Transition> sequence
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               sequence );
        classContracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               sequence );

        final StateGraphBuilder builder =
            new StateGraphBuilder ( "composite" );

        int transition_num = 0;
        Value<?> previous_state = builder.entry ();
        for ( Transition transition : sequence )
        {
            transition_num ++;
            final String step_name = "composite[" + transition_num + "]";
            final One<String> next_state =
                new One<String> (
                    STEP_NAME_VALUE_CLASS, // value_class
                    step_name );

            builder.add ( previous_state,
                          transition,
                          next_state );

            previous_state = next_state;
        }

        builder.from ( previous_state );
        builder.toExit ();

        final Graph<Value<?>, Transition> graph =
            builder.build ();
        return graph;
    }


    /**
     * <p>
     * Creates a new CompositeTransition.
     * </p>
     *
     * @param graph The graph to traverse whenever a transition attempt
     *              is made.  Must not be null.
     */
    public CompositeTransition (
                                Graph<Value<?>, Transition> graph
                                )
        throws ParametersMustNotBeNull.Violation
    {
        super ( collectTapes ( graph ) );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               graph );

        this.graph = graph;

        final Countable<Tape> tapes = this.tapes ();
        int index_debug = -1;
        int t = 0;
        for ( Tape tape : tapes )
        {
            if ( tape == Tape.DEBUG )
            {
                index_debug = t;
                break;
            }

            t ++;
        }

        this.indexDebug = index_debug;
    }

    private static final Tape [] collectTapes (
            Graph<Value<?>, Transition> graph
            )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               graph );

        final TapeMachine transition_machine =
            new TapeMachine ( graph,
                              false ); // is_automatically_transition_input

        final Countable<Tape> required_tapes =
            transition_machine.tapes ();
        final Countable<Tape> optional_tapes =
            transition_machine.optionalTapes ();

        final Tape [] tapes = new Tape [
                                        (int) required_tapes.length ()
                                        + (int) optional_tapes.length ()
                                        ];

        int t = 0;
        for ( Tape required_tape : required_tapes )
        {
            tapes [ t ] = required_tape;
            t ++;
        }
        for ( Tape optional_tape : optional_tapes )
        {
            tapes [ t ] = optional_tape;
            t ++;
        }

        return tapes;
    }


    /**
     * @see java.lang.AbstractTransition#toString()
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this.getClass () )
            + " ( " + this.graph + " )";
    }


    /**
     * @see musaico.foundation.tape.Transition#transition(musaico.foundation.tape.TapeMachine...)
     */
    @Override
    public final boolean transition (
                                     TapeMachine ... tape_machines
                                     )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  (Object) tape_machines );
        this.contracts ().check ( Parameter1.MustContainNoNulls.CONTRACT,
                                  tape_machines );

        final TapeMachine transition_machine =
            new TapeMachine ( this.graph,
                              false ); // is_automatically_transition_input

        if ( tape_machines [ this.indexDebug ] != TapeMachine.NONE )
        {
            this.debug ( tape_machines [ this.indexDebug ],
                         new Date (),
                         "  " + this + " Begin" );
        }

        int infinite_loop_protector = 16384;
        for ( int it = 0; it <= infinite_loop_protector; it ++ )
        {
            if ( it == infinite_loop_protector )
            {
                if ( tape_machines [ this.indexDebug ] != TapeMachine.NONE )
                {
                    this.debug ( tape_machines [ this.indexDebug ],
                                 new Date (),
                                 "  " + this + " Infinite loop" );
                }

                // Hit the infinite loop watermark.  Abort.
                return false;
            }

            if ( tape_machines [ this.indexDebug ] != TapeMachine.NONE )
            {
                this.debug ( tape_machines [ this.indexDebug ],
                             new Date (),
                             "  " + this + " Attempting to transition from "
                             + transition_machine.state () );
            }

            if ( transition_machine.transition ( tape_machines ).orNull ()
                     != null )
            {
                // Successful transition.
                // Keep transitioning through all the component transitions.
                if ( tape_machines [ this.indexDebug ] != TapeMachine.NONE )
                {
                    this.debug ( tape_machines [ this.indexDebug ],
                                 new Date (),
                                 "  " + this + " Child transition success" );
                }

                continue;
            }

            // No transition.  Why?
            if ( transition_machine.isAtExit () )
            {
                // Because we're done the composite transition.
                if ( tape_machines [ this.indexDebug ] != TapeMachine.NONE )
                {
                    this.debug ( tape_machines [ this.indexDebug ],
                                 new Date (),
                                 "  " + this + " Exit" );
                }

                break;
            }
            else
            {
                // Couldn't transition.
                if ( tape_machines [ this.indexDebug ] != TapeMachine.NONE )
                {
                    this.debug ( tape_machines [ this.indexDebug ],
                                 new Date (),
                                 "  " + this + " Fail" );
                }

                return false;
            }
        }

        if ( tape_machines [ this.indexDebug ] != TapeMachine.NONE )
        {
            this.debug ( tape_machines [ this.indexDebug ],
                         new Date (),
                         "  " + this + " Success" );
        }

        // Success.
        return true;
    }
}
