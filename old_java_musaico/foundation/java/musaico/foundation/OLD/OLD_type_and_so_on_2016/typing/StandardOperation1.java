package musaico.foundation.typing;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.value.Error;
import musaico.foundation.value.No;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueBuilder;


/**
 * <p>
 * An Operation with 1 input.  Provides boilerplate methods, so that
 * all the developer must provide is the core OperationBody.
 * </p>
 *
 *
 * <p>
 * In Java every Symbol must be Serializable in order to play
 * nicely with RMI.
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
@SuppressWarnings("rawtypes") // OpID extends Op, can't use Op<X, Y>.
public class StandardOperation1<INPUT1 extends Object, OUTPUT extends Object>
    extends AbstractSymbol<OperationID<Operation1<INPUT1, OUTPUT>, OUTPUT>, Operation1<INPUT1, OUTPUT>>
    implements Operation1<INPUT1, OUTPUT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( StandardOperation1.class );


    // Unique identifier for this Operation.
    private final OperationID<Operation1<INPUT1, OUTPUT>, OUTPUT> id;

    // The core of this Operation, does all the work after
    // we've checked input constraints and before we've checked
    // output constraints.
    private final OperationBody1<INPUT1, OUTPUT> body;


    /**
     * <p>
     * Creates a new StandardOperation1 with the specified name
     * and input/output Types.
     * </p>
     *
     * <p>
     * This constructor may ONLY be called on a StandardOperation1
     * class which also implements OperationBody1.
     * </p>
     *
     * @param name The name which will be used to create an OperationID,
     *             an identifier which is unique in each SymbolTable
     *             Must not be null.
     *
     * @param input1_type The type of input # 1 to this operation.
     *                    Must not be null.
     *
     * @param output_type The output type from this operation.
     *                    Must not be null.
     */
    protected StandardOperation1 (
                                  String name,
                                  Type<INPUT1> input1_type,
                                  Type<OUTPUT> output_type
                                  )
        throws ParametersMustNotBeNull.Violation
    {
        this ( name,
               input1_type,
               output_type,
               null ); // body = this.
    }


    /**
     * <p>
     * Creates a new StandardOperation1 with the specified name
     * and input/output Types.
     * </p>
     *
     * @param name The name which will be used to create an OperationID,
     *             an identifier which is unique in each SymbolTable
     *             Must not be null.
     *
     * @param input1_type The type of input # 1 to this operation.
     *                    Must not be null.
     *
     * @param output_type The output type from this operation.
     *                    Must not be null.
     *
     * @param body Evaluates the core of this Operation after the
     *             input contracts and input Type Constraints have
     *             been checked, and before the output Type Constraints
     *             and output contracts have been checked.
     *             Can be null ONLY if this operation class implements
     *             the OperationBody interface.
     */
    public StandardOperation1 (
                               String name,
                               Type<INPUT1> input1_type,
                               Type<OUTPUT> output_type,
                               OperationBody1<INPUT1, OUTPUT> body
                               )
        throws ParametersMustNotBeNull.Violation
    {
        this ( name,
               new OperationType1<INPUT1, OUTPUT> ( input1_type,
                   output_type ),
               body );
    }


    /**
     * <p>
     * Creates a new StandardOperation1 with the specified name
     * and input/output Types.
     * </p>
     *
     * <p>
     * This constructor may ONLY be called on a StandardOperation1
     * class which also implements OperationBody1.
     * </p>
     *
     * @param name The name which will be used to create an OperationID,
     *             an identifier which is unique in each SymbolTable
     *             Must not be null.
     *
     * @param operation_type The OperationType describing one or more
     *                       input Type(s) required by this Operation,
     *                       one output Type returned by this Operation,
     *                       and optionally Tags and other Symbols to
     *                       provide, for example, execution hints about
     *                       this Operation (such as "long and slow"
     *                       versus "lightweight and quick").
     *                       The input and output Types are used to
     *                       check Constraints on the inputs and outputs
     *                       passed to / returned from this Operation
     *                       at runtime.  Must not be null.
     */
    protected StandardOperation1 (
                                  String name,
                                  OperationType1<INPUT1, OUTPUT> operation_type
                                  )
        throws ParametersMustNotBeNull.Violation
    {
        this ( name,
               operation_type,
               null ); // body = this.
    }


    /**
     * <p>
     * Creates a new StandardOperation1 with the specified name
     * and input/output Types.
     * </p>
     *
     * @param name The name which will be used to create an OperationID,
     *             an identifier which is unique in each SymbolTable
     *             Must not be null.
     *
     * @param operation_type The OperationType describing one or more
     *                       input Type(s) required by this Operation,
     *                       one output Type returned by this Operation,
     *                       and optionally Tags and other Symbols to
     *                       provide, for example, execution hints about
     *                       this Operation (such as "long and slow"
     *                       versus "lightweight and quick").
     *                       The input and output Types are used to
     *                       check Constraints on the inputs and outputs
     *                       passed to / returned from this Operation
     *                       at runtime.  Must not be null.
     *
     * @param body Evaluates the core of this Operation after the
     *             input contracts and input Type Constraints have
     *             been checked, and before the output Type Constraints
     *             and output contracts have been checked.
     *             Can be null ONLY if this operation class implements
     *             the OperationBody interface.
     */
    public StandardOperation1 (
                               String name,
                               OperationType1<INPUT1, OUTPUT> operation_type,
                               OperationBody1<INPUT1, OUTPUT> body
                               )
        throws ParametersMustNotBeNull.Violation
    {
        this ( new OperationID<Operation1<INPUT1, OUTPUT>, OUTPUT> ( name,
                   operation_type ),
               body );
    }


    /**
     * <p>
     * Creates a new StandardOperation1 with the specified id.
     * </p>
     *
     * <p>
     * This constructor may ONLY be called on a StandardOperation1
     * class which also implements OperationBody1.
     * </p>
     *
     * @param id The unique identifier of this operation.  Every Operation
     *           must have a unique ID within its SymbolTable.
     *           Also identifies the input and output types of this Operation.
     *           Must not be null.
     */
    protected StandardOperation1 (
                                  OperationID<Operation1<INPUT1, OUTPUT>, OUTPUT> id
                                  )
        throws ParametersMustNotBeNull.Violation
    {
        this ( id,
               null ); // body = null.
    }


    /**
     * <p>
     * Creates a new StandardOperation1 with the specified id.
     * </p>
     *
     * @param id The unique identifier of this operation.  Every Operation
     *           must have a unique ID within its SymbolTable.
     *           Also identifies the input and output types of this Operation.
     *           Must not be null.
     *
     * @param body Evaluates the core of this Operation after the
     *             input contracts and input Type Constraints have
     *             been checked, and before the output Type Constraints
     *             and output contracts have been checked.
     *             Can be null ONLY if this operation class implements
     *             the OperationBody interface.
     */
    public StandardOperation1 (
                               OperationID<Operation1<INPUT1, OUTPUT>, OUTPUT> id,
                               OperationBody1<INPUT1, OUTPUT> body
                               )
        throws ParametersMustNotBeNull.Violation
    {
        // Use our Type's metadata for tracking contract violations.
        super ( id );

        this.id = id;

        if ( body == null )
        {
            try
            {
                OperationBody1<INPUT1, OUTPUT> this_as_a_body =
                    (OperationBody1<INPUT1, OUTPUT>) this;
                this.body = this_as_a_body;
            }
            catch ( ClassCastException e )
            {
                throw ParametersMustNotBeNull.CONTRACT.violation (
                    StandardOperation1.class,
                    new Object [] { id, body } );
            }
        }
        else
        {
            this.body = body;
        }

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               this.body );
    }


    /**
     * @return An Error value with an "invalid inputs" TypingViolation.
     *         This method is used by all the StandardOperation* classes.
     *         Never null.
     */
    @SuppressWarnings("unchecked") // Cast Value<?> - Value<Object>.
    /* Package-private */
    static final <OPERATION_OUTPUT extends Object>
        Error<OPERATION_OUTPUT> badInput (
                                          Operation<OPERATION_OUTPUT> operation,
                                          int input_num,
                                          Exception cause,
                                          Value<?> ... inputs
                                          )
    {
        final List<Value<Object>> inputs_list =
            new ArrayList<Value<Object>> ();
        for ( Value<?> input : inputs )
        {
            final Value<Object> input_object = (Value<Object>) input;
            inputs_list.add ( input_object );
        }

        return StandardOperation1.badInput ( operation,
                                             input_num,
                                             cause,
                                             inputs_list );
    }


    /**
     * @return An Error value with an "invalid inputs" TypingViolation.
     *         This method is used by all the StandardOperation* classes.
     *         Never null.
     */
    public static final <OPERATION_OUTPUT extends Object>
        Error<OPERATION_OUTPUT> badInput (
                                          Operation<OPERATION_OUTPUT> operation,
                                          int input_num,
                                          Exception cause,
                                          List<Value<Object>> inputs
                                          )
    {
        final Value<Object> inputs_value =
            new ValueBuilder<Object> ( Object.class,
                                       "none" )
            .addValues ( inputs )
            .build ();
        final OperationValue operation_inputs =
            new OperationValue ( operation,
                                 inputs_value,
                                 input_num /* Input # N */ );
        final TypingViolation violation =
            OperationInputMustBeValid.CONTRACT.violation ( operation,
                                                           operation_inputs );
        if ( cause != null )
        {
            violation.initCause ( cause );
        }

        final Error<OPERATION_OUTPUT> error_output =
            operation.outputType ().errorValue ( violation );

        return error_output;
    }


    /**
     * @see musaico.foundation.typing.Operation1#evaluate(java.lang.Object, java.lang.Object)
     */
    @Override
    public final Value<OUTPUT> evaluate (
                                         Value<INPUT1> input1
                                         )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  input1 );

        // Check input Constraints:
        try
        {
            this.input1Type ().checkValue ( input1 );
        }
        catch ( TypingViolation violation )
        {
            return StandardOperation1.badInput ( this,      // operation
                                                 0,         // Input #
                                                 violation, // cause
                                                 input1 );  // inputs
        }

        // Main Operation evaluate:
        final Value<OUTPUT> output;
        try
        {
            output = this.body.evaluateBody ( input1 );
        }
        catch ( RuntimeException e )
        {
            return StandardOperation1.badInput ( this,     // operation
                                                 999,      // Input #
                                                 e,        // cause
                                                 input1 ); // inputs
        }

        // Check output Constraints:
        try
        {
            this.outputType ().checkValue ( output );
        }
        catch ( TypingViolation violation )
        {
            return this.outputType ().errorValue ( violation );
        }

        return output;
    }


    /**
     * @see musaico.foundation.typing.Operation#evaluate(java.util.List)
     */
    public Value<OUTPUT> evaluate (
                                   List<Value<Object>> inputs
                                   )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               Parameter1.Length.MustBeGreaterThanZero.Violation,
               ReturnNeverNull.Violation
    {
        return StandardOperation1.evaluateOperation1 ( this, inputs );
    }


    /**
     * <p>
     * Static helper method for Operation1s.
     * </p>
     *
     * <p>
     * This method is used by StandardOperation1, Read, ReadBlocking,
     * Write and so on.
     * </p>
     *
     * @see musaico.foundation.typing.Operation#evaluate(java.util.List)
     */
    @SuppressWarnings("unchecked") // try...catch around cast is "unchecked".
    public static final <OPERATION_INPUT1 extends Object, OPERATION_OUTPUT extends Object>
        Value<OPERATION_OUTPUT> evaluateOperation1 (
                                                    Operation1<OPERATION_INPUT1, OPERATION_OUTPUT> operation,
                                                    List<Value<Object>> inputs
                                                    )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               Parameter1.Length.MustBeGreaterThanZero.Violation,
               ReturnNeverNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               inputs );
        classContracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               inputs );
        classContracts.check ( Parameter1.Length.MustBeGreaterThanZero.CONTRACT,
                               inputs );

        final Value<OPERATION_INPUT1> input1;
        try
        {
            input1 = (Value<OPERATION_INPUT1>) inputs.get ( 0 );
        }
        catch ( ClassCastException e )
        {
            return StandardOperation1.badInput ( operation, // operation
                                                 0,         // Input #
                                                 e,         // cause
                                                 inputs );  // inputs
        }

        return operation.evaluate ( input1 );
    }


    /**
     * @see musaico.foundation.typing.OperationInputs1#input1Type()
     */
    @Override
    public final Type<INPUT1> input1Type ()
    {
        return this.type ().input1Type ();
    }


    /**
     * @see musaico.foundation.typing.Operation#outputType()
     */
    @Override
    public final Type<OUTPUT> outputType ()
    {
        return this.type ().outputType ();
    }


    /**
     * @see musaico.foundation.typing.Symbol#rename(java.lang.String)
     */
    @Override
    public StandardOperation1<INPUT1, OUTPUT> rename (
                                                      String name
                                                      )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               name );

        return new StandardOperation1<INPUT1, OUTPUT> (
            name,
            this.input1Type (),
            this.outputType (),
            this.body );
    }


    /**
     * @see musaico.foundation.typing.Retypable#retype(java.lang.String, musaico.foundation.typing.Type)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast Type<?> - Type<INPUT1>,
                                   // Cast Type<?> - Type<INPUT2>,
                                   // Cast Type<?> - Type<INPUT3>,
                                   // Cast Type<?> - Type<INPUT4>,
                                   // Cast Type<?> - Type<INPUT5>,
                                   // Cast Type<?> - Type<INPUT6>.
    public StandardOperation1<INPUT1, OUTPUT> retype (
            String name,
            OperationType<? extends Operation<OUTPUT>, OUTPUT> type
            )
        throws ParametersMustNotBeNull.Violation,
               TypesMustHaveSameValueClasses.Violation,
               ReturnNeverNull.Violation
    {
        this.checkRetype ( name, type );

        return new StandardOperation1<INPUT1, OUTPUT> (
            name,
            (Type<INPUT1>) type.inputType ( 0 ),
            type.outputType (),
            this.body );
    }


    /**
     * @see musaico.foundation.typing.Operation#type()
     */
    @Override
    @SuppressWarnings("unchecked") // Cast OpType<OP, OUT> - OpType1
    public final OperationType1<INPUT1, OUTPUT> type ()
    {
        return (OperationType1<INPUT1, OUTPUT>)
            this.id ().operationType ();
    }
}
