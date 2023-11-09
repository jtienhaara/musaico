package musaico.foundation.typing.requirement;

import java.io.Serializable;

import java.util.LinkedHashSet;
import java.util.Set;
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
 * !!!
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
 * @see musaico.foundation.typing.requirement.MODULE#COPYRIGHT
 * @see musaico.foundation.typing.requirement.MODULE#LICENSE
 */
public final class Unsatisfier<OPERATION extends Operation<OUTPUT>, OUTPUT extends Object>
    extends StandardOperationVarArgs<OPERATION, OUTPUT>
    implements OperationBodyVarArgs<OUTPUT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( Unsatisfier.class );


    final Set<Requirement<?>> requirements =
        new LinkedHashSet<Requirement<?>> ();
    private final OPERATION operation;

    public Unsatisfier ( OPERATION operation )
    {
        this ( operation == null
                   ? null
                   : operation.id ().name (),
               operation );
    }

    @SuppressWarnings("unchecked") // Cast OpType<?> - OpType<OP>
    public Unsatisfier ( String name, OPERATION operation )
    {
        super ( name,
                operation == null
                    ? null
                    : (OperationType<OPERATION, OUTPUT>) operation.type (),
                null ); // body = this.

        this.operation = operation;
    }


    /**
     * @see musaico.foundation.typing.OperationBodyVarArgs#evaluateBody(java.util.List)
     */
    @Override
    public final Value<OUTPUT> evaluateBody (
                                             List<Value<Object>> inputs
                                             )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               Parameter1.Length.MustBeGreaterThanZero.Violation,
               ReturnNeverNull.Violation
    {
        final Unsatisfied<OUTPUT> unsatisfied =
            new Unsatisfied<OUTPUT> ( this.operation,
                                      inputs,
                                      this.requirements );

        return unsatisfied;
    }

    public void requires ( Requirement<?> requirement )
    {
        if ( this.requirements.contains ( requirement ) )
        {
            // Set once, no overwriting for security purposes.
            return;
        }

        this.requirements.add ( requirement );
    }


    /**
     * @see musaico.foundation.typing.Symbol#rename(java.lang.String)
     */
    @Override
    public Unsatisfier<OPERATION, OUTPUT> rename (
                                                  String name
                                                  )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  name );

        return new Unsatisfier<OPERATION, OUTPUT> ( name,
                                                    this.operation );
    }


    /**
     * @see musaico.foundation.typing.Retypable#retype(java.lang.String, musaico.foundation.typing.Type)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast operation - OPERATION.
    public Unsatisfier<OPERATION, OUTPUT> retype (
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

        return new Unsatisfier<OPERATION, OUTPUT> ( name,
                                                    renamed_operation );
    }
}
