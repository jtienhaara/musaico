package musaico.foundation.typing;

import java.io.Serializable;

import java.util.List;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.value.Value;
import musaico.foundation.value.ValueBuilder;


/**
 * <p>
 * The default "readBlocking" Operation for Types.  Returns the input parameter
 * as-is, even if it is a Blocking Value.
 * </p>
 *
 * <p>
 * Some Types, and especially certain Tags, might want to mutate the
 * ReadBlocking Operation to protect private data, or encrypt confidential
 * values, and so on.  So typically it is good for every Type to
 * have the default ReadBlocking Operation in its SymbolTable.
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
public class ReadBlocking<VALUE extends Object>
    extends AbstractSymbol<OperationID<Operation1<VALUE, VALUE>, VALUE>, Operation1<VALUE, VALUE>>
    implements Operation1<VALUE, VALUE>, OperationBody1<VALUE, VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Pre-processes every input we read.  Can be null.
    private final Operation1<VALUE, VALUE> preProcessor;


    /**
     * <p>
     * Creates a new ReadBlocking operation to return inputs as-is, with
     * the default OperationID "read".
     * </p>
     *
     * @param type The Type of values input to and output from this
     *             ReadBlocking.  Must not be null.
     */
    public ReadBlocking (
                         Type<VALUE> type
                         )
        throws ParametersMustNotBeNull.Violation
    {
        this ( "read", type, null ); // No pre_processor.
    }


    /**
     * <p>
     * Creates a new ReadBlocking operation to return inputs, optionally
     * pre-processed by the specified Operation, with
     * the default OperationID "read".
     * </p>
     *
     * @param pre_processor The optional Operation to pre-process the
     *                      value to be read.  Can be null, if this
     *                      ReadBlocking Operation has no pre-processors.
     *                      Can be null.
     */
    public ReadBlocking (
                         Operation1<VALUE, VALUE> pre_processor
                         )
        throws ParametersMustNotBeNull.Violation
    {
        this ( "read",
               pre_processor == null
                   ? null
                   : pre_processor.outputType (),
               pre_processor );
    }


    /**
     * <p>
     * Creates a new ReadBlocking operation to return inputs, optionally
     * pre-processed by the specified Operation, with
     * the default OperationID "read".
     * </p>
     *
     * @param type The Type of values input to and output from this
     *             ReadBlocking.  Must not be null.
     *
     * @param pre_processor The optional Operation to pre-process the
     *                      value to be read.  Can be null, if this
     *                      ReadBlocking Operation has no pre-processors.
     *                      Can be null.
     */
    public ReadBlocking (
                         Type<VALUE> type,
                         Operation1<VALUE, VALUE> pre_processor
                         )
        throws ParametersMustNotBeNull.Violation
    {
        this ( "read", type, pre_processor );
    }


    /**
     * <p>
     * Creates a new ReadBlocking operation to return inputs as-is.
     * </p>
     *
     * @param name The name of this ReadBlocking, which will be used to
     *             construct an OperationID unique in every SymbolTable.
     *             Must not be null.
     *
     * @param type The Type of values input to and output from this
     *             ReadBlocking.  Must not be null.
     */
    public ReadBlocking (
                         String name,
                         Type<VALUE> type
                         )
        throws ParametersMustNotBeNull.Violation
    {
        this ( name, type, null ); // No pre_processor.
    }


    /**
     * <p>
     * Creates a new ReadBlocking operation to return inputs, optionally
     * pre-processed by the specified Operation.
     * </p>
     *
     * @param name The name of this ReadBlocking, which will be used to
     *             construct an OperationID unique in every SymbolTable.
     *             Must not be null.
     *
     * @param type The Type of values input to and output from this
     *             ReadBlocking.  Must not be null.
     *
     * @param pre_processor The optional Operation to pre-process the
     *                      value to be read.  Can be null, if this
     *                      ReadBlocking Operation has no pre-processors.
     *                      Can be null.
     */
    public ReadBlocking (
                         String name,
                         Type<VALUE> type,
                         Operation1<VALUE, VALUE> pre_processor
                         )
        throws ParametersMustNotBeNull.Violation
    {
        this ( new OperationID<Operation1<VALUE, VALUE>, VALUE> ( name,
                   new OperationType1<VALUE, VALUE> ( type, type ) ),
               pre_processor );
    }


    /**
     * <p>
     * Creates a new ReadBlocking operation to return inputs, optionally
     * pre-processed by the specified Operation.
     * </p>
     *
     * @param id The Operation identifier for this ReadBlocking, which
     *           uniquely identifies this operation in every SymbolTable.
     *           Must not be null.
     *
     * @param pre_processor The optional Operation to pre-process the
     *                      value to be read.  Can be null, if this
     *                      ReadBlocking Operation has no pre-processors.
     *                      Can be null.
     */
    public ReadBlocking (
                         OperationID<Operation1<VALUE, VALUE>, VALUE> id,
                         Operation1<VALUE, VALUE> pre_processor
                         )
        throws ParametersMustNotBeNull.Violation
    {
        // Use our Type's metadata for tracking contract violations.
        super ( id );

        // Can be null:
        this.preProcessor = pre_processor;
    }


    /**
     * @see musaico.foundation.typing.OperationBody1#evaluateBody(musaico.foundation.value.Value)
     */
    @Override
    public final Value<VALUE> evaluateBody (
                                            Value<VALUE> processed_input
                                            )
    {
        return processed_input;
    }


    /**
     * @see musaico.foundation.typing.Operation1#evaluate(musaico.foundation.value.Value)
     */
    @Override
    public final Value<VALUE> evaluate (
                                        Value<VALUE> input
                                        )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  input );

        // Either perform pre-processing, and let the pre-processor
        // check all constraints, or perform input constraint checking:
        Value<VALUE> processed_input;
        if ( this.preProcessor == null )
        {
            processed_input = input;

            // Check input Constraints:
            try
            {
                this.input1Type ().checkValue ( input );
            }
            catch ( TypingViolation violation )
            {
                return this.outputType ().errorValue ( violation );
            }
        }
        else
        {
            // Let the pre-processor check constraints:
            processed_input = this.preProcessor.evaluate ( input );
        }

        // Main Operation evaluate:
        final Value<VALUE> output;
        try
        {
            output = this.evaluateBody ( processed_input );
        }
        catch ( RuntimeException e )
        {
            final OperationValue operation_input =
                new OperationValue ( this,
                                     processed_input,
                                     0 ); // input_num
            final TypingViolation violation =
                OperationInputMustBeValid.CONTRACT.violation ( this,
                                                               operation_input,
                                                               e );
            return this.outputType ().errorValue ( violation );
        }

        // Check output Constraints unless the pre-processor was
        // responsible for checking them:
        if ( this.preProcessor == null )
        {
            try
            {
                this.outputType ().checkValue ( output );
            }
            catch ( TypingViolation violation )
            {
                return this.outputType ().errorValue ( violation );
            }
        }

        return output;
    }


    /**
     * @see musaico.foundation.typing.Operation#evaluate(java.util.List)
     */
    public Value<VALUE> evaluate (
                                  List<Value<Object>> inputs
                                  )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        return StandardOperation1.evaluateOperation1 ( this, inputs );
    }


    /**
     * @see musaico.foundation.typing.OperationInputs1#input1Type()
     */
    @Override
    public final Type<VALUE> input1Type ()
    {
        return this.type ().input1Type ();
    }


    /**
     * @see musaico.foundation.typing.Operation#outputType()
     */
    @Override
    public final Type<VALUE> outputType ()
    {
        return this.type ().outputType ();
    }


    /**
     * @see musaico.foundation.typing.Symbol#rename(java.lang.String)
     */
    @Override
    public ReadBlocking<VALUE> rename (
                                       String name
                                       )
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  name );

        return new ReadBlocking<VALUE> ( name,
                                         this.input1Type () );
    }


    /**
     * @see musaico.foundation.typing.Retypable#retype(java.lang.String, musaico.foundation.typing.Type)
     */
    @Override
    public ReadBlocking<VALUE> retype (
                                       String name,
                                       OperationType<? extends Operation<VALUE>, VALUE> type
                                       )
        throws ParametersMustNotBeNull.Violation,
               TypesMustHaveSameValueClasses.Violation,
               ReturnNeverNull.Violation
    {
        this.checkRetype ( name, type );

        return new ReadBlocking<VALUE> ( name,
                                         type.outputType (),
                                         this.preProcessor );
    }


    /**
     * @see musaico.foundation.typing.Operation#type()
     */
    @Override
    @SuppressWarnings("unchecked") // Cast OpType<OP, OUT> - OpType1
    public final OperationType1<VALUE, VALUE> type ()
    {
        return (OperationType1<VALUE, VALUE>)
            this.id ().operationType ();
    }
}
