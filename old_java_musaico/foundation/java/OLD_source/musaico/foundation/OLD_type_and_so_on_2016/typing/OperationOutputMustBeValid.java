package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.CheckedViolation;
import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;
import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.Parameter7;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.InstanceOfClass;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.value.Finite;
import musaico.foundation.value.Just;
import musaico.foundation.value.One;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueBuilder;
import musaico.foundation.value.ValueViolation;


/**
 *  advance what class an Operation expects as output, so we might

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
public class OperationOutputMustBeValid
    extends AbstractTypingContract<OperationValue>
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    public static final OperationOutputMustBeValid CONTRACT =
        new OperationOutputMustBeValid ();

    /**
     * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
     */
    @Override
    public FilterState filter (
                               OperationValue operation_output
                               )
    {
        if ( operation_output == null )
        {
            // Filter out null operation outputs.
            return FilterState.DISCARDED;
        }

        final Type<?> expected_type =
            operation_output.operationID ().operationType ().outputType ();
        final Value<?> actual_value = operation_output.value ();
        final FilterState result;
        if ( expected_type.isInstance ( actual_value ) )
        {
            result = FilterState.KEPT;
        }
        else
        {
            result = FilterState.DISCARDED;
        }

        return result;
    }
}
