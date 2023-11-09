package musaico.foundation.typing.aspect;

import java.io.Serializable;

import java.util.List;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.typing.Operation;
import musaico.foundation.typing.OperationBodyVarArgs;
import musaico.foundation.typing.OperationType;
import musaico.foundation.typing.StandardOperationVarArgs;
import musaico.foundation.typing.Symbol;
import musaico.foundation.typing.SymbolID;
import musaico.foundation.typing.TypesMustHaveSameValueClasses;
import musaico.foundation.typing.TypingViolation;

import musaico.foundation.value.Value;


/**
 * <p>
 * Carries out the aspect-oriented "pre" and "post" whenever an
 * Operation is evaluated.
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
 * @see musaico.foundation.typing.aspect.MODULE#COPYRIGHT
 * @see musaico.foundation.typing.aspect.MODULE#LICENSE
 */
public class AspectOperation<OPERATION extends Operation<OUTPUT>, OUTPUT extends Object>
    extends StandardOperationVarArgs<OPERATION, OUTPUT>
    implements OperationBodyVarArgs<OUTPUT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( AspectOperation.class );


    private final OPERATION operation;
    private final Aspect aspect;

    public AspectOperation ( OPERATION operation,
                             Aspect aspect )
        throws ParametersMustNotBeNull.Violation
    {
        this ( ( operation == null
                     ? null
                     : operation.id ().name () ), // name
               operation,
               aspect );
    }


    @SuppressWarnings("unchecked") // Cast op.id ().type () - OpType<OP>.
    public AspectOperation ( String name,
                             OPERATION operation,
                             Aspect aspect )
        throws ParametersMustNotBeNull.Violation
    {
        super ( name,
                (OperationType<OPERATION, OUTPUT>) operation.id ().type (),
                null ); // body = this.

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               operation, aspect );

        this.operation = operation;
        this.aspect = aspect;
    }

    @Override   
    public final Value<OUTPUT> evaluateBody (
                                             List<Value<Object>> inputs
                                             )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               Parameter1.Length.MustBeGreaterThanZero.Violation,
               ReturnNeverNull.Violation
    {
        final List<Value<Object>> aspect_inputs =
            this.aspect.prePost ().pre ( this.operation, inputs );
        final Value<OUTPUT> output =
            this.operation.evaluate ( aspect_inputs );
        final Value<OUTPUT> aspect_output =
            this.aspect.prePost ().post ( this.operation, output );

        return aspect_output;
    }


    /**
     * @see musaico.foundation.typing.Symbol#rename(java.lang.String)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast operation - OPERATION.
    public AspectOperation<OPERATION, OUTPUT> rename (
                                                      String name
                                                      )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  name );

        // Rename the underlying operation, too.
        final OPERATION renamed_operation = (OPERATION)
            this.operation.rename ( name );
        return new AspectOperation<OPERATION, OUTPUT> ( name,
                                                        renamed_operation,
                                                        this.aspect );
    }


    /**
     * @see musaico.foundation.typing.Retypable#retype(java.lang.String, musaico.foundation.typing.Type)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast operation - OPERATION.
    public AspectOperation<OPERATION, OUTPUT> retype (
                                                      String name,
                                                      OperationType<? extends Operation<OUTPUT>, OUTPUT> type
                                    )
        throws ParametersMustNotBeNull.Violation,
               TypesMustHaveSameValueClasses.Violation,
               ReturnNeverNull.Violation
    {
        this.checkRetype ( name, type );

        // Rename the underlying operation, too.
        final OPERATION renamed_operation = (OPERATION)
            this.operation.retype ( name, type );
        return new AspectOperation<OPERATION, OUTPUT> ( name,
                                                        renamed_operation,
                                                        this.aspect );
    }
}
