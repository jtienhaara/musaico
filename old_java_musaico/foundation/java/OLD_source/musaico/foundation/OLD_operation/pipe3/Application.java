package musaico.foundation.operation;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.term.Term;


/**
 * <p>
 * The application of an Operation, which can provide raw individual elements
 * or cooked entire Terms as output.
 * </p>
 *
 * <p>
 * Each Application is itself the piped output from an
 * <code> Operation.apply ( ... ) </code>.  It also has:
 * </p>
 *
 * <ul>
 *   <li> 0 or more parameter input Pipe(s); </li>
 *   <li> 1 main input Pipe to <code> Operation.apply ( ... ) </code>; </li>
 *   <li> 0 or more extra output Pipe(s). </code>
 * </ul>
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
 * @see musaico.foundation.operation.MODULE#COPYRIGHT
 * @see musaico.foundation.operation.MODULE#LICENSE
 */
public interface Application<INPUT extends Object, OUTPUT extends Object>
    extends Pipe<OUTPUT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    // Every Pipe must implement
    // musaico.foundation.operation.Pipe#close(musaico.foundation.operation.Context)

    // Every Pipe must implement
    // musaico.foundation.operation.Pipe#inputPipes(musaico.foundation.operation.Context)


    /**
     * <p>
     * Returns the main input to this Application, if any.
     * </p>
     *
     * @param context The Context in which to retrieve the main input Pipe.
     *                The Context might contain a variable
     *                binding required by this Pipe, and possibly other
     *                state required to evaluate this Pipe.
     *                Must not be null.
     * 
     * @return The Pipe to which the Operation is being applied.
     *         Never null.
     */
    public abstract Pipe<INPUT> mainInput (
            Context context
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    // Every Pipe must implement
    // musaico.foundation.operation.Pipe#outputPipes(musaico.foundation.operation.Context)


    /**
     * <p>
     * Returns the parameter input(s) to this Application, if any.
     * </p>
     *
     * @param context The Context in which to retrieve the parameter pipe(s).
     *                The Context might contain a variable
     *                binding required by this Pipe, and possibly other
     *                state required to evaluate this Pipe.
     *                Must not be null.
     *
     * @return The parameter Pipe(s) to this operation Application, if any.
     *         Can be empty.  Never null.  Never contains any null elements.
     */
    public abstract Pipe<?> [] parameters (
            Context context
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation;


    // Every Pipe must implement
    // musaico.foundation.operation.Pipe#read(musaico.foundation.operation.Context)

    // Every Pipe must implement
    // musaico.foundation.operation.Pipe#read(java.lang.Object[], int, int, long, musaico.foundation.operation.Context)

    // Every Pipe must implement
    // musaico.foundation.operation.Pipe#violation(musaico.foundation.operation.Context)
}
