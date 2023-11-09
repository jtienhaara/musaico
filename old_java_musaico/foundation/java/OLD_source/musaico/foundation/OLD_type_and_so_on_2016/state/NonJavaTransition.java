package musaico.foundation.state;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.value.finite.No;


/**
 * <p>
 * A Transition which cannot be executed in a Java Virtual Machine.
 * </p>
 *
 * <p>
 * A NonJavaTransition might be executable inside a C program, or
 * inside a JavaScript interpreter, and so on.  However its representation
 * in Java is strictly used as a structural feature of a state graph;
 * attempting to transition across it inside a JVM shall always fail.
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
public interface NonJavaTransition
    extends Transition, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * @see musaico.foundation.tape.Transition#transition(musaico.foundation.tape.TapeMachine...)
     *
     * <p>
     * Always fails when a transition is attempted inside
     * a Java Virtual Machine.
     * </p>
     */
    @Override
    public abstract boolean transition (
                                        TapeMachine ... tape_machines
                                        )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation;
}
