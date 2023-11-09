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
 * An operation which allows any number of parameters.
 * Provides boilerplate methods, so that
 * all the developer must provide is the core OperationBody.
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
public class StandardOperationVarArgs<OPERATION extends Operation<OUTPUT>, OUTPUT extends Object>
    extends AbstractSymbol<OperationID<OPERATION, OUTPUT>, OPERATION>
    implements Operation1<Object, OUTPUT>,
               Operation2<Object, Object, OUTPUT>,
               Operation3<Object, Object, Object, OUTPUT>,
               Operation4<Object, Object, Object, Object, OUTPUT>,
               Operation5<Object, Object, Object, Object, Object, OUTPUT>,
               Operation6<Object, Object, Object, Object, Object, Object, OUTPUT>,
               /* !!!
               Operation7<Object, Object, Object, Object, Object, Object, Object, OUTPUT>,
               Operation8<Object, Object, Object, Object, Object, Object, Object, Object, OUTPUT>,
               Operation9<Object, Object, Object, Object, Object, Object, Object, Object, Object, OUTPUT>,
               !!! */
               Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( StandardOperationVarArgs.class );


    // The core of this Operation, does all the work after
    // we've checked input constraints and before we've checked
    // output constraints.
    private final OperationBodyVarArgs<OUTPUT> body;


    /**
     * <p>
     * Creates a new StandardOperationVarArgs.
     * </p>
     *
     * <p>
     * This constructor may ONLY be called on a StandardOperation1
     * class which also implements OperationBody1.
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
     */
    protected StandardOperationVarArgs (
                                        String name,
                                        OperationType<OPERATION, OUTPUT> operation_type
                                        )
        throws ParametersMustNotBeNull.Violation
    {
        this ( name,
               operation_type,
               null ); // body = this.
    }


    /**
     * <p>
     * Creates a new StandardOperationVarArgs.
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
     * @param body Evaluates the core of this Operation after the
     *             input contracts and input Type Constraints have
     *             been checked, and before the output Type Constraints
     *             and output contracts have been checked.
     *             Can be null ONLY if this operation class implements
     *             the OperationBody interface.
     */
    public StandardOperationVarArgs (
               String name,
               OperationType<OPERATION, OUTPUT> operation_type,
               OperationBodyVarArgs<OUTPUT> body
               )
        throws ParametersMustNotBeNull.Violation
    {
        this ( new OperationID<OPERATION, OUTPUT> ( name,
                                                    operation_type ),
               body );
    }


    /**
     * <p>
     * Creates a new StandardOperationVarArgs.
     * </p>
     *
     * <p>
     * This constructor may ONLY be called on a StandardOperation1
     * class which also implements OperationBody1.
     * </p>
     *
     * @param name The unique identifier of this Operation, unique among
     *             all Operations of the same Type in each containing
     *             SymbolTable.  Must not be null.
     */
    protected StandardOperationVarArgs (
                                        OperationID<OPERATION, OUTPUT> id
                                        )
        throws ParametersMustNotBeNull.Violation
    {
        this ( id,
               null ); // body = this.
    }


    /**
     * <p>
     * Creates a new StandardOperationVarArgs.
     * </p>
     *
     * @param name The unique identifier of this Operation, unique among
     *             all Operations of the same Type in each containing
     *             SymbolTable.  Must not be null.
     *
     * @param body Evaluates the core of this Operation after the
     *             input contracts and input Type Constraints have
     *             been checked, and before the output Type Constraints
     *             and output contracts have been checked.
     *             Can be null ONLY if this operation class implements
     *             the OperationBody interface.
     */
    public StandardOperationVarArgs (
                                     OperationID<OPERATION, OUTPUT> id,
                                     OperationBodyVarArgs<OUTPUT> body
                                     )
        throws ParametersMustNotBeNull.Violation
    {
        // Use our Type's metadata for tracking contract violations.
        super ( id );

        if ( body == null )
        {
            try
            {
                OperationBodyVarArgs<OUTPUT> this_as_a_body =
                    (OperationBodyVarArgs<OUTPUT>) this;
                this.body = this_as_a_body;
            }
            catch ( ClassCastException e )
            {
                throw ParametersMustNotBeNull.CONTRACT.violation (
                    StandardOperationVarArgs.class,
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
     * @see musaico.foundation.typing.Operation1#evaluate(musaico.foundation.value.Value)
     */
    @Override
    public final Value<OUTPUT> evaluate (
                                         Value<Object> input1
                                         )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        final List<Value<Object>> inputs = new ArrayList<Value<Object>> ();
        inputs.add ( input1 );

        return this.evaluate ( inputs );
    }


    /**
     * @see musaico.foundation.typing.Operation2#evaluate(musaico.foundation.value.Value, musaico.foundation.value.Value)
     */
    @Override
    public final Value<OUTPUT> evaluate (
                                         Value<Object> input1,
                                         Value<Object> input2
                                         )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        final List<Value<Object>> inputs = new ArrayList<Value<Object>> ();
        inputs.add ( input1 );
        inputs.add ( input2 );

        return this.evaluate ( inputs );
    }


    /**
     * @see musaico.foundation.typing.Operation3#evaluate(musaico.foundation.value.Value, musaico.foundation.value.Value, musaico.foundation.value.Value)
     */
    @Override
    public final Value<OUTPUT> evaluate (
                                         Value<Object> input1,
                                         Value<Object> input2,
                                         Value<Object> input3
                                         )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        final List<Value<Object>> inputs = new ArrayList<Value<Object>> ();
        inputs.add ( input1 );
        inputs.add ( input2 );
        inputs.add ( input3 );

        return this.evaluate ( inputs );
    }


    /**
     * @see musaico.foundation.typing.Operation4#evaluate(musaico.foundation.value.Value, musaico.foundation.value.Value, musaico.foundation.value.Value, musaico.foundation.value.Value)
     */
    @Override
    public final Value<OUTPUT> evaluate (
                                         Value<Object> input1,
                                         Value<Object> input2,
                                         Value<Object> input3,
                                         Value<Object> input4
                                         )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        final List<Value<Object>> inputs = new ArrayList<Value<Object>> ();
        inputs.add ( input1 );
        inputs.add ( input2 );
        inputs.add ( input3 );
        inputs.add ( input4 );

        return this.evaluate ( inputs );
    }


    /**
     * @see musaico.foundation.typing.Operation5#evaluate(musaico.foundation.value.Value, musaico.foundation.value.Value, musaico.foundation.value.Value, musaico.foundation.value.Value, musaico.foundation.value.Value)
     */
    @Override
    public final Value<OUTPUT> evaluate (
                                         Value<Object> input1,
                                         Value<Object> input2,
                                         Value<Object> input3,
                                         Value<Object> input4,
                                         Value<Object> input5
                                         )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        final List<Value<Object>> inputs = new ArrayList<Value<Object>> ();
        inputs.add ( input1 );
        inputs.add ( input2 );
        inputs.add ( input3 );
        inputs.add ( input4 );
        inputs.add ( input5 );

        return this.evaluate ( inputs );
    }


    /**
     * @see musaico.foundation.typing.Operation6#evaluate(musaico.foundation.value.Value, musaico.foundation.value.Value, musaico.foundation.value.Value, musaico.foundation.value.Value, musaico.foundation.value.Value, musaico.foundation.value.Value)
     */
    @Override
    public final Value<OUTPUT> evaluate (
                                         Value<Object> input1,
                                         Value<Object> input2,
                                         Value<Object> input3,
                                         Value<Object> input4,
                                         Value<Object> input5,
                                         Value<Object> input6
                                         )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        final List<Value<Object>> inputs = new ArrayList<Value<Object>> ();
        inputs.add ( input1 );
        inputs.add ( input2 );
        inputs.add ( input3 );
        inputs.add ( input4 );
        inputs.add ( input5 );
        inputs.add ( input6 );

        return this.evaluate ( inputs );
    }


    /**
     * @see musaico.foundation.typing.Operation#evaluate(java.util.List)
     */
    @Override
    public final Value<OUTPUT> evaluate (
                                         List<Value<Object>> inputs
                                         )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               Parameter1.Length.MustBeGreaterThanZero.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  inputs );
        this.contracts ().check ( Parameter1.MustContainNoNulls.CONTRACT,
                                  inputs );
        this.contracts ().check ( Parameter1.Length.MustBeGreaterThanZero.CONTRACT,
                                  inputs );

        final int max_num_inputs = this.type ().numInputs ();
        if ( inputs.size () > max_num_inputs )
        {
            return this.badInput ( inputs,         // inputs
                                   max_num_inputs, // input_num
                                   null );         // No cause
        }

        for ( int i = 0 ; i < inputs.size (); i ++ )
        {
            final Value<Object> input = inputs.get ( i );
            try
            {
                final Type<?> input_type = this.type ().inputType ( i );
                input_type.checkValue ( input );
            }
            catch ( Exception e )
            {
                // Could be a TypingViolation, or even a ClassCastException.
                return this.badInput ( inputs, // inputs
                                       i,      // input_num
                                       e );    // cause
            }
        }

        // Main Operation evaluate:
        final Value<OUTPUT> output;
        try
        {
            output = this.body.evaluateBody ( inputs );
        }
        catch ( RuntimeException e )
        {
            return this.badInput ( inputs, // inputs
                                   999,    // input_num
                                   e );    // cause
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
     * @return A No value with an "invalid inputs" TypingViolation.
     *         Never null.
     */
    private final Error<OUTPUT> badInput (
                                          List<Value<Object>> inputs,
                                          int input_num,
                                          Exception cause
                                          )
    {
        final Value<Object> inputs_value =
            new ValueBuilder<Object> ( Object.class,
                                       "none" )
            .addValues ( inputs )
            .build ();
        final OperationValue operation_inputs =
            new OperationValue ( this,
                                 inputs_value,
                                 input_num /* Input # N */ );
        final TypingViolation violation =
            OperationInputMustBeValid.CONTRACT.violation ( this,
                                                           operation_inputs );
        if ( cause != null )
        {
            violation.initCause ( cause );
        }

        return this.outputType ().errorValue ( violation );
    }


    /**
     * @see musaico.foundation.typing.OperationInputs1#input1Type()
     */
    @Override
    @SuppressWarnings("unchecked") // Cast Type<?> to Type<Object>.
    public final Type<Object> input1Type ()
        throws ReturnNeverNull.Violation
    {
        final OperationType<OPERATION, OUTPUT> operation_type =
            this.type ();
        if ( operation_type.numInputs () >= 1 )
        {
            return (Type<Object>) operation_type.inputType ( 0 );
        }
        else
        {
            return Type.NONE;
        }
    }


    /**
     * @see musaico.foundation.typing.OperationInputs2#input2Type()
     */
    @Override
    @SuppressWarnings("unchecked") // Cast Type<?> to Type<Object>.
    public final Type<Object> input2Type ()
        throws ReturnNeverNull.Violation
    {
        final OperationType<OPERATION, OUTPUT> operation_type =
            this.type ();
        if ( operation_type.numInputs () >= 2 )
        {
            return (Type<Object>) operation_type.inputType ( 0 );
        }
        else
        {
            return Type.NONE;
        }
    }


    /**
     * @see musaico.foundation.typing.OperationInputs3#input3Type()
     */
    @Override
    @SuppressWarnings("unchecked") // Cast Type<?> to Type<Object>.
    public final Type<Object> input3Type ()
        throws ReturnNeverNull.Violation
    {
        final OperationType<OPERATION, OUTPUT> operation_type =
            this.type ();
        if ( operation_type.numInputs () >= 3 )
        {
            return (Type<Object>) operation_type.inputType ( 0 );
        }
        else
        {
            return Type.NONE;
        }
    }


    /**
     * @see musaico.foundation.typing.OperationInputs4#input4Type()
     */
    @Override
    @SuppressWarnings("unchecked") // Cast Type<?> to Type<Object>.
    public final Type<Object> input4Type ()
        throws ReturnNeverNull.Violation
    {
        final OperationType<OPERATION, OUTPUT> operation_type =
            this.type ();
        if ( operation_type.numInputs () >= 4 )
        {
            return (Type<Object>) operation_type.inputType ( 0 );
        }
        else
        {
            return Type.NONE;
        }
    }


    /**
     * @see musaico.foundation.typing.OperationInputs5#input5Type()
     */
    @Override
    @SuppressWarnings("unchecked") // Cast Type<?> to Type<Object>.
    public final Type<Object> input5Type ()
        throws ReturnNeverNull.Violation
    {
        final OperationType<OPERATION, OUTPUT> operation_type =
            this.type ();
        if ( operation_type.numInputs () >= 5 )
        {
            return (Type<Object>) operation_type.inputType ( 0 );
        }
        else
        {
            return Type.NONE;
        }
    }


    /**
     * @see musaico.foundation.typing.OperationInputs6#input6Type()
     */
    @Override
    @SuppressWarnings("unchecked") // Cast Type<?> to Type<Object>.
    public final Type<Object> input6Type ()
        throws ReturnNeverNull.Violation
    {
        final OperationType<OPERATION, OUTPUT> operation_type =
            this.type ();
        if ( operation_type.numInputs () >= 6 )
        {
            return (Type<Object>) operation_type.inputType ( 0 );
        }
        else
        {
            return Type.NONE;
        }
    }


    /**
     * @see musaico.foundation.typing.Operation#outputType()
     */
    @Override
    public Type<OUTPUT> outputType ()
    {
        final OperationType<OPERATION, OUTPUT> operation_type =
            this.id ().type ();
        return operation_type.outputType ();
    }


    /**
     * @see musaico.foundation.typing.Symbol#rename(java.lang.String)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast OpType<?> - OpType<OP, OUT>.
    public StandardOperationVarArgs<OPERATION, OUTPUT> rename (
                                       String name
                                       )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               name );

        return new StandardOperationVarArgs<OPERATION, OUTPUT> (
            name,
            (OperationType<OPERATION, OUTPUT>) this.id ().type (),
            this.body );
    }


    /**
     * @see musaico.foundation.typing.Retypable#retype(java.lang.String, musaico.foundation.typing.Type)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast OpType<?> - OpType<OP, OUT>.
    public StandardOperationVarArgs<OPERATION, OUTPUT> retype (
                                       String name,
                                       OperationType<? extends Operation<OUTPUT>, OUTPUT> type
                                       )
        throws ParametersMustNotBeNull.Violation,
               TypesMustHaveSameValueClasses.Violation,
               ReturnNeverNull.Violation
    {
        this.checkRetype ( name, type );

        return new StandardOperationVarArgs<OPERATION, OUTPUT> (
            name,
            (OperationType<OPERATION, OUTPUT>) type,
            this.body );
    }


    /**
     * @see musaico.foundation.typing.Operation#type()
     */
    @Override
    @SuppressWarnings("unchecked") // Cast Type<Op> to OpType.
    public OperationType<OPERATION, OUTPUT> type ()
        throws ReturnNeverNull.Violation
    {
        return (OperationType<OPERATION, OUTPUT>)
            this.id ().type ();
    }
}
