package musaico.foundation.typing;

import java.io.Serializable;

import java.util.List;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.value.Value;


/**
 * <p>
 * An operation which always fails with a specific TypingViolation,
 * no matter what the input.
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
public class AlwaysFail<OUTPUT extends Object>
    extends StandardOperationVarArgs<Operation<OUTPUT>, OUTPUT>
    implements OperationBodyVarArgs<OUTPUT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( AlwaysFail.class );


    /** The typing violation which induced this perennial failure. */
    private final TypingViolation violation;


    /**
     * <p>
     * Creates a new AlwaysFail to always create failed output
     * of the specified output type, regardless of the input.
     * </p>
     *
     * @param name The name of this Operation, unique among all Operations
     *             of the same Type in each containing SymbolTable.
     *             For example, "sort" or "read" and so on.
     *             Must not be null.
     *
     * @param output_type The Type of output Value returned by this
     *                    Operation.  Can provide Constraints to guarantee
     *                    output values from this Operation.
     *                    Must not be null.
     *
     * @param violation The typing exception which led to this
     *                  AlwaysFail operation.  Must not be null.
     */
    @SuppressWarnings("unchecked") // Cast OpType for OpID constructor,
    public <OPERATION extends Operation<OUTPUT>>
        AlwaysFail (
                    String name,
                    OperationType<OPERATION, OUTPUT> operation_type,
                    TypingViolation violation
                    )
        throws ParametersMustNotBeNull.Violation
    {
        this ( new OperationID<OPERATION, OUTPUT> ( name,
                                                    operation_type ),
               violation );
    }


    /**
     * <p>
     * Creates a new AlwaysFail to always create failed output
     * of the specified output type, regardless of the input.
     * </p>
     *
     * @param id The unique identifier of this operation.  Every Operation
     *           must have a unique ID within its SymbolTable.
     *           Also defines the input and output types of this AlwaysFail.
     *           Must not be null.
     *
     * @param violation The typing exception which led to this
     *                  AlwaysFail operation.  Must not be null.
     */
    @SuppressWarnings("unchecked") // cast OpID<OP, OUT> to OpID<Op,OUT>.
    public <OPERATION extends Operation<OUTPUT>>
        AlwaysFail (
                    OperationID<OPERATION, OUTPUT> id,
                    TypingViolation violation
                    )
        throws ParametersMustNotBeNull.Violation
    {
        super ( (OperationID<Operation<OUTPUT>, OUTPUT>) id,
                null ); // body = this.

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               violation );

        this.violation = violation;
    }


    /**
     * <p>
     * Creates a new AlwaysFail to always create failed output
     * of the specified output type, regardless of the input,
     * with a default "symbol must be registered" violation.
     * </p>
     *
     * @param name The name of this AlwaysFail operation, unique
     *             among all Operations in each containing SymbolTable.
     *             For example, "sort" or 
     *             Must not be null.
     *
     * @param output_type The Type of output Value returned by this
     *                    Operation.  Provides a Guarantor to check
     *                    output Value guarantees.
     */
    @SuppressWarnings("unchecked") // Cast OpType for OpID constructor,
    public <OPERATION extends Operation<OUTPUT>>
        AlwaysFail (
                    String name,
                    OperationType<OPERATION, OUTPUT> operation_type
                    )
    {
        this ( new OperationID<OPERATION, OUTPUT> ( name,
                   operation_type ) );
    }


    /**
     * <p>
     * Creates a new AlwaysFail to always create failed output
     * of the specified output type, regardless of the input,
     * with a default "symbol must be registered" violation.
     * </p>
     *
     * @param id The unique identifier of this operation.  Every Operation
     *           must have a unique ID within its SymbolTable.
     *           Also defines the input and output types of this AlwaysFail.
     *           Must not be null.
     */
    public <OPERATION extends Operation<OUTPUT>>
        AlwaysFail (
                    OperationID<OPERATION, OUTPUT> id
                    )
    {
        this ( id,
               SymbolMustBeRegistered.CONTRACT.violation (
                   AlwaysFail.class,
                   new Unregistered ( id )
                   ) );
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
        return this.outputType ().errorValue ( this.violation );
    }


    /**
     * @see musaico.foundation.typing.Symbol#rename(java.lang.String)
     */
    @Override
    public AlwaysFail<OUTPUT> rename (
                                      String name
                                      )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  name );

        return new AlwaysFail<OUTPUT> ( name,
                                        this.type (),
                                        this.violation );
    }


    /**
     * @see musaico.foundation.typing.Retypable#retype(java.lang.String, musaico.foundation.typing.Type)
     */
    @Override
    public AlwaysFail<OUTPUT> retype (
                                      String name,
                                      OperationType<? extends Operation<OUTPUT>, OUTPUT> type
                                      )
        throws ParametersMustNotBeNull.Violation,
               TypesMustHaveSameValueClasses.Violation,
               ReturnNeverNull.Violation
    {
        this.checkRetype ( name, type );

        return new AlwaysFail<OUTPUT> ( name,
                                        type,
                                        this.violation );
    }


    /**
     * @return The typing violation which caused this operation
     *         to always fail.  Never null.
     */
    public TypingViolation violation ()
    {
        return this.violation;
    }
}
