package musaico.foundation.state;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
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

import musaico.foundation.graph.Graph;

import musaico.foundation.value.NotOne;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueViolation;
import musaico.foundation.value.ZeroOrOne;

import musaico.foundation.value.finite.No;
import musaico.foundation.value.finite.One;

import musaico.foundation.value.iterators.IteratorMustBeFinite;


/**
 * <p>
 * A Transition which causes the input tape to change state, effectively
 * choosing a branch of the input tape.
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
public class ChooseInputTransition
    extends AbstractTransition
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // The index of the Tape.INPUT machine.
    private static final int INDEX_INPUT = 0;

    // The index of the Tape.DEBUG machine.
    private static final int INDEX_DEBUG = 1;


    // Checks constructor and static method obligations.
    private static final Advocate classContracts =
        new Advocate ( ChooseInputTransition.class );


    // Used to choose which branch to take from the input state.
    private final Graph<Value<?>, Transition> choice;


    /**
     * <p>
     * Creates a new ChooseInputTransition.
     * </p>
     *
     * @param values The sequence of value(s) used to choose which arc
     *               to follow in the input machine.  Must not be null.
     *               Must not contain any null elements.
     */
    public ChooseInputTransition (
            Value<?> ... values
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.Length.MustBeGreaterThanZero.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        this (
              createSequence (
                              values ) );
    }


    // Creates a straight graph of one or more nodes, connected in sequence.
    // Package private.  Used by StateGraphBuilder.
    static final Graph<Value<?>, Transition> createSequence (
            Value<?> ... values
            )
    {
        final StringBuilder sbuf = new StringBuilder ();
        boolean is_first = true;
        sbuf.append ( "{" );
        for ( Value<?> value : values )
        {
            if ( is_first )
            {
                is_first = false;
            }
            else
            {
                sbuf.append ( "," );
            }

            sbuf.append ( " " + value );
        }

        if ( ! is_first )
        {
            sbuf.append ( " " );
        }

        sbuf.append ( "}" );

        final StateGraphBuilder builder =
            new StateGraphBuilder ( sbuf.toString () );
        builder.fromEntry ();
        for ( Value<?> state : values )
        {
            builder.to ( state );
            builder.from ( state );
        }
        builder.toExit ();

        final Graph<Value<?>, Transition> graph = builder.build ();
        return graph;
    }


    /**
     * <p>
     * Creates a new ChooseInputTransition.
     * </p>
     *
     * @param choice The graph of nodes which will feed
     *               the input machine, causing it to transition.
     *               Must not be null.
     */
    public ChooseInputTransition (
                                  Graph<Value<?>, Transition> choice
                                  )
        throws ParametersMustNotBeNull.Violation
    {
        super ( Tape.INPUT, Tape.DEBUG );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               choice );

        this.choice = choice;
    }


    /**
     * @see java.lang.AbstractTransition#toString()
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this.getClass () )
            + " ( " + this.choice + " )";
    }


    /**
     * @see musaico.foundation.tape.Transition#transition(musaico.foundation.tape.TapeMachine[])
     */
    @Override
    public final boolean transition (
                                     TapeMachine... tape_machines
                                     )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  (Object) tape_machines );
        this.contracts ().check ( Parameter1.MustContainNoNulls.CONTRACT,
                                  tape_machines );

        // We have exactly one Tape, so one tape machine.
        final TapeMachine input = tape_machines [ INDEX_INPUT ];

        final TapeMachine choice_machine =
            new TapeMachine ( this.choice,
                              false ); // is_automatically_transition_input

        if ( tape_machines [ INDEX_DEBUG ] != TapeMachine.NONE )
        {
            this.debug ( tape_machines [ INDEX_DEBUG ],
                         new Date (),
                         "  " + this + " Begin choice "
                         + this.choice.entry () );
        }

        int infinite_loop_protector = 256;
        for ( int it = 0; it <= infinite_loop_protector; it ++ )
        {
            if ( it == infinite_loop_protector )
            {
                if ( tape_machines [ INDEX_DEBUG ] != TapeMachine.NONE )
                {
                    this.debug ( tape_machines [ INDEX_DEBUG ],
                                 new Date (),
                                 "  " + this + " Infinite loop" );
                }

                return false;
            }

            if ( input.transition ( choice_machine ).orNull () == null )
            {
                if ( choice_machine.isAtExit () )
                {
                    if ( tape_machines [ INDEX_DEBUG ] != TapeMachine.NONE )
                    {
                        this.debug ( tape_machines [ INDEX_DEBUG ],
                                     new Date (),
                                     "  " + this + " Exit" );
                    }

                    break;
                }
                else
                {
                    if ( tape_machines [ INDEX_DEBUG ] != TapeMachine.NONE )
                    {
                        this.debug ( tape_machines [ INDEX_DEBUG ],
                                     new Date (),
                                     "  " + this + " Fail" );
                    }

                    return false;
                }
            }

            if ( tape_machines [ INDEX_DEBUG ] != TapeMachine.NONE )
            {
                this.debug ( tape_machines [ INDEX_DEBUG ],
                             new Date (),
                             "  " + this + " Made choice" );
            }
        }

        if ( tape_machines [ INDEX_DEBUG ] != TapeMachine.NONE )
        {
            this.debug ( tape_machines [ INDEX_DEBUG ],
                         new Date (),
                         "  " + this + " Success" );
        }

        return true;
    }
}
