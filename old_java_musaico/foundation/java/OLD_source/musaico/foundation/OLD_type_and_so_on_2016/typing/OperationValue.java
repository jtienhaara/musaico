package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.CheckedViolation;
import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;
import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.Parameter7;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.value.Finite;
import musaico.foundation.value.Just;
import musaico.foundation.value.One;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueBuilder;
import musaico.foundation.value.ValueViolation;


/**
 * <p>
 * A tuple containing an Operation and either the input someone tried
 * to pass to it, or the output it tried to return.
 * </p>

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
public class OperationValue
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks contracts on static methods and constructors for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( OperationValue.class );

    /** The ID of the Operation. */
    private final OperationID<Operation<?>, ?> operationID;

    /** The input to the Operation / output from the Operation. */
    private final Value<?> value;

    /** 0 or greater if the value is input, -1 if it is output. */
    private final int inputNum;

    /**
     * <p>
     * Creates a new OperationValue with the specified input/output
     * to/from the specified Operation.
     * </p>
     *
     * @param operation The operation.  Must not be null.
     *
     * @param value The input/output to/from the operation.
     *              Must not be null.
     *
     * @param input_num The index of the input parameter, 0 or greater
     *                  if this is an input value, or -1 if it is
     *                  an outout value.  Must not be null.
     */
    @SuppressWarnings("unchecked") // op.id () to OperationID<Op<?>, ?>.
        public OperationValue (
                               Operation<?> operation,
                               Value<?> value,
                               int input_num
                               )
    {
        this ( (OperationID<Operation<?>, ?>) ( operation == null
                                                ? null
                                                : operation.id () ),
               value,
               input_num );
    }

    /**
     * <p>
     * Creates a new OperationValue with the specified input/output
     * to/from the specified OperationID.
     * </p>
     *
     * @param operation_id The identifier of the operation.
     *                     Must not be null.
     *
     * @param value The input/output to/from the operation.
     *              Must not be null.
     *
     * @param input_num The index of the input parameter, 0 or greater
     *                  if this is an input value, or -1 if it is
     *                  an outout value.  Must not be null.
     */
    public OperationValue (
                           OperationID<Operation<?>, ?> operation_id,
                           Value<?> value,
                           int input_num
                           )
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               operation_id, value );

        this.operationID = operation_id;
        this.value = value;
        this.inputNum = input_num;
    }

    /**
     * @return The index of the input parameter represented by
     *         this operation value, or -1 if it is an output value.
     */
    public int inputNum ()
    {
        return this.inputNum;
    }

    /**
     * @return True if this operation value is an input value;
     *         false if it is an output value.
     */
    public boolean isInput ()
    {
        if ( inputNum >= 0 )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * @return The OperationID.  Never null.
     */
    public OperationID<?, ?> operationID ()
    {
        return this.operationID;
    }

    /**
     * @return The input to / output from the Operation.
     *         Never null.
     */
    public Value<?> value ()
    {
        return this.value;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
        public String toString ()
    {
        return "" + ClassName.of ( this.getClass () )
            + " "
            + this.operationID.name ()
            + ( this.inputNum >= 0 ? " ( ..., # " + (this.inputNum + 1) +": " : " (...): " )
            + this.value
            + ( this.inputNum >= 0 ? ", ... )" : "" );
    }
}
