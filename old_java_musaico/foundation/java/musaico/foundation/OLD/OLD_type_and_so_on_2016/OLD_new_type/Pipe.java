package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.Parameter4;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.value.Value;


/**
 * <p>
 * Performs a chain of Operations to arrive at the final output.
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
public class Pipe<INPUT extends Object, OUTPUT extends Object>
    extends StandardOperation1<INPUT, OUTPUT>
    implements OperationBody1<INPUT, OUTPUT>, CompositeOperation<OUTPUT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( Pipe.class );


    // The Operations in the pipe, in order.
    private final Value<Operation1<?, ?>> pipedOperations;


    /**
     * <p>
     * Creates a new Pipe which uses a chain of operations
     * from each input to an output.
     * </p>
     *
     * @param name The name for this Pipe, which will be used to
     *             create the OperationID which uniquely identifies this
     *             Pipe in any SymbolTable.  Must not be null.
     *
     * @param input_type The input Type for the piped operations.
     *                   Must not be null.
     *
     * @param output_type The output Type for the piped operations.
     *                    Must not be null.
     *
     * @param operations The Operations to pipe together to form this Pipe.
     *                   Must not be null.
     *
     * @throws TypingViolation If the first Operation does not accept this
     *                         Pipe's input Type, or if the last Operation
     *                         does not return this Pipe's output Type,
     *                         or if the output Type of any in-between
     *                         Operation does not match the input Type
     *                         of the next Operation in the pipe.
     */
    public Pipe (
                 String name,
                 Type<INPUT> input_type,
                 Type<OUTPUT> output_type,
                 Value<Operation1<?, ?>> operations
                 )
        throws TypingViolation,
               ParametersMustNotBeNull.Violation,
               Parameter4.MustContainNoNulls.Violation
    {
        this ( new OperationID<Operation1<INPUT, OUTPUT>, OUTPUT> ( name,
                   new OperationType1<INPUT, OUTPUT> ( input_type,
                                                       output_type ) ),
               operations );
    }


    /**
     * <p>
     * Creates a new Pipe which uses a chain of operations to cast
     * from each input to an output.
     * </p>
     *
     * @param id The identifier for this Pipe, unique among all
     *           Operations in a SymbolTable.
     *           Also specifies the input and output types of this Operation.
     *           Must not be null.
     *
     * @param operations The Operations to pipe together to form this Pipe.
     *                   Must not be null.
     *
     * @throws TypingViolation If the first Operation does not accept this
     *                         Pipe's input Type, or if the last Operation
     *                         does not return this Pipe's output Type,
     *                         or if the output Type of any in-between
     *                         Operation does not match the input Type
     *                         of the next Operation in the pipe.
     */
    public Pipe (
                 OperationID<Operation1<INPUT, OUTPUT>, OUTPUT> id,
                 Value<Operation1<?, ?>> operations
                 )
        throws TypingViolation,
               ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation
    {
        super ( id,
                null ); // body = this.

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               (Object) operations );

        // Check that the piped operations have the same types.
        Operation1<?, ?> previous_operation = this;
        Type<?> previous_output_type = this.input1Type ();
        for ( Operation1<?, ?> current_operation : operations )
        {
            final Type<?> current_input_type =
                current_operation.input1Type ();
            final Types output_input_types =
                new Types ( previous_operation,
                            previous_output_type,
                            current_operation,
                            current_input_type );
            if ( ! OperationTypesMustMatch.CONTRACT
                 .filter ( output_input_types ).isKept () )
            {
                throw OperationTypesMustMatch.CONTRACT
                    .violation ( this, output_input_types );
            }

            previous_operation = current_operation;
            previous_output_type = current_operation.outputType ();
        }

        if ( operations.hasValue () )
        {
            // One or more piped operations.
            // Make sure the final output is right.
            final Types output_input_types =
                new Types ( previous_operation,
                            previous_output_type,
                            this,
                            this.outputType () );
            if ( ! OperationTypesMustMatch.CONTRACT
                 .filter ( output_input_types ).isKept () )
            {
                throw OperationTypesMustMatch.CONTRACT
                    .violation ( this, output_input_types );
            }
        }
        else
        {
            // No Operations piped together.
            // Equivalent to an identity operation.
            // Make sure the final output is the same as the input.
            final Types output_input_types =
                new Types ( this,
                            this.input1Type (),
                            this,
                            this.outputType () );
            if ( ! OperationTypesMustMatch.CONTRACT
                 .filter ( output_input_types ).isKept () )
            {
                throw OperationTypesMustMatch.CONTRACT
                    .violation ( this, output_input_types );
            }
        }

        this.pipedOperations = operations;
    }


    /**
     * @see musaico.foundation.typing.OperationBody1#evaluateBody(musaico.foundation.value.Value)
     */
    @Override
    @SuppressWarnings("unchecked") // Operation at end; checked type in constructor.
    public Value<OUTPUT> evaluateBody (
                                       Value<INPUT> input
                                       )
    {
        Value<?> current_input = input;
        for ( Operation1<?, ?> current_operation : this.pipedOperations )
        {
            final Value<?> current_output =
                this.pipeOperation ( current_operation, current_input );
            current_input = current_output;
        }

        final Value<OUTPUT> output = (Value<OUTPUT>) current_input;

        return output;
    }


    /**
     * @see musaico.foundation.typing.CompositeOperation#operations()
     */
    @Override
        public Value<Operation1<?, ?>> operations ()
    {
        return this.pipedOperations;
    }


    /**
     * <p>
     * Does the actual child operation.  Minimizes compiler grief by using
     * generic parameter instead of ?.
     * </p>
     */
    @SuppressWarnings("unchecked") // We checked Types in the constructor.
    private final <PIPE_INPUT extends Object, PIPE_OUTPUT extends Object>
        Value<PIPE_OUTPUT> pipeOperation (
                                          Operation1<PIPE_INPUT, PIPE_OUTPUT> pipe_operation,
                                          Value<?> input
                                          )
    {
        return pipe_operation.evaluate ( (Value<PIPE_INPUT>) input );
    }


    /**
     * @see musaico.foundation.typing.Symbol#rename(java.lang.String)
     */
    @Override
    public Pipe<INPUT, OUTPUT> rename (
                                       String name
                                       )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               name );

        try
        {
            return new Pipe<INPUT, OUTPUT> ( name,
                                             this.input1Type (),
                                             this.outputType (),
                                             this.pipedOperations );
        }
        catch ( TypingViolation violation )
        {
            // This should never happen, since we're passing in all
            // the same parameters that were allowed when this was constructed.
            final ReturnNeverNull.Violation v2 =
                ReturnNeverNull.CONTRACT.violation ( this,
                                                     null );
            v2.initCause ( violation );
            throw v2;
        }
    }


    /**
     * @see musaico.foundation.typing.Retypable#retype(java.lang.String, musaico.foundation.typing.Type)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast Type<?> - Type<INPUT>.
    public Pipe<INPUT, OUTPUT> retype (
                                       String name,
                                       OperationType<? extends Operation<OUTPUT>, OUTPUT> type
                                       )
        throws ParametersMustNotBeNull.Violation,
               TypesMustHaveSameValueClasses.Violation,
               ReturnNeverNull.Violation
    {
        this.checkRetype ( name, type );

        try
        {
            return new Pipe<INPUT, OUTPUT> ( name,
                                             (Type<INPUT>) type.inputType ( 0 ),
                                             type.outputType (),
                                             this.pipedOperations );
        }
        catch ( TypingViolation violation )
        {
            // This should never happen, since we're passing in all
            // the same parameters that were allowed when this was constructed.
            final ReturnNeverNull.Violation v2 =
                ReturnNeverNull.CONTRACT.violation ( this,
                                                     null );
            v2.initCause ( violation );
            throw v2;
        }
    }
}
