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

import musaico.foundation.graph.Arc;

import musaico.foundation.value.NotOne;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueViolation;
import musaico.foundation.value.ZeroOrOne;

import musaico.foundation.value.finite.No;
import musaico.foundation.value.finite.One;


/**
 * <p>
 * Copies the current state of one TapeMachine (such as the Tape.INPUT
 * tape machine) to another (such as the Tape.OUTPUT tape machine),
 * not changing anything from the input, but changing the output
 * tape machine to the newly added state.
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
public class Copy
    extends AbstractTransition
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final Advocate classContracts =
        new Advocate ( Copy.class );

    // The index of the machine to copy from.
    private static final int INDEX_FROM = 0;

    // The index of the machine to copy to.
    private static final int INDEX_TO = 1;

    // The index of the Tape.DEBUG machine.
    private static final int INDEX_DEBUG = 2;


    // The Transition for the Arc to write to the tape machine.
    private final Transition transitionToWrite;


    /**
     * <p>
     * Creates a new Copy transition.
     * </p>
     *
     * @param from_tape The Tape to read from.  For example, Tape.INPUT.
     *                  Must not be null.
     *
     * @param to_tape The Tape to write to.  For example, Tape.OUTPUT.
     *                Must not be null.
     *
     * @param transition_to_write The Transition to the input value
     *                            for the Arc to be written to the
     *                            output tape machine.
     *                            Must not be null.
     */
    public Copy (
                 Tape from_tape,
                 Tape to_tape,
                 Transition transition_to_write
                 )
        throws ParametersMustNotBeNull.Violation
    {
        super ( from_tape, to_tape,
                Tape.DEBUG );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               transition_to_write );

        this.transitionToWrite = transition_to_write;
    }


    /**
     * @see java.lang.AbstractTransition#toString()
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this.getClass () )
            + "(" + this.tapes ()
            + ", " + this.transitionToWrite + ")";
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

        // We have exactly two Tapes, so two tape machines.
        final TapeMachine input = tape_machines [ INDEX_FROM ];
        final TapeMachine output = tape_machines [ INDEX_TO ];

        final Value<?> input_value = input.state ();

        if ( tape_machines [ INDEX_DEBUG ] != TapeMachine.NONE )
        {
            this.debug ( tape_machines [ INDEX_DEBUG ],
                         new Date (),
                         "  " + this + " Begin from " + input
                         + " to " + output );
        }

        if ( input_value.orNull () == null )
        {
            // Nothing to copy.
            if ( tape_machines [ INDEX_DEBUG ] != TapeMachine.NONE )
            {
                this.debug ( tape_machines [ INDEX_DEBUG ],
                             new Date (),
                             "  " + this + " Fail (nothing to copy)" );
            }

            return false;
        }

        final Arc<Value<?>, Transition> new_arc =
            Write.write ( output,
                          this.transitionToWrite,
                          input_value,
                          tape_machines [ INDEX_DEBUG ] );

        if ( new_arc == null )
        {
            // Couldn't write to the output tape machine.
            if ( tape_machines [ INDEX_DEBUG ] != TapeMachine.NONE )
            {
                this.debug ( tape_machines [ INDEX_DEBUG ],
                             new Date (),
                             "  " + this + " Fail" );
            }

            return false;
        }

        // Set the output tape machine's state to the node just written.
        output.forceState ( new_arc.to () );

        if ( tape_machines [ INDEX_DEBUG ] != TapeMachine.NONE )
        {
            this.debug ( tape_machines [ INDEX_DEBUG ],
                         new Date (),
                         "  " + this + " Success (" + new_arc + ")" );
        }

        return true;
    }
}
