package musaico.foundation.state;

import java.io.Serializable;

import java.util.Date;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.graph.Arc;

import musaico.foundation.machine.Machine;

import musaico.foundation.value.Value;
import musaico.foundation.value.ZeroOrOne;

import musaico.foundation.value.finite.One;


/**
 * <p>
 * An arc across which every set of tape machines induces
 * an automatic transition to the next State, without consuming
 * any inputs or otherwise causing any of the tape machines
 * to change states.
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
public class AutomaticTransition
    extends AbstractTransition
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // The index of the Tape.DEBUG machine.
    private static final int INDEX_DEBUG = 0;


    // Creates a new AutomaticTransition.
    // Use Transition.AUTOMATIC instead.
    protected AutomaticTransition ()
    {
        // No tapes are required by the AutomaticTransition,
        // though DEBUG is allowed.
        super ( Tape.DEBUG );
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
        if ( tape_machines [ INDEX_DEBUG ] != TapeMachine.NONE )
        {
            this.debug ( tape_machines [ INDEX_DEBUG ],
                         new Date (),
                         "  " + this + " Success" );
        }

        return true;
    }
}
