package musaico.foundation.typing;

import java.io.Serializable;

import java.util.List;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.value.One;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueBuilder;
import musaico.foundation.value.ZeroOrOne;


/**
 * <p>
 * An Operation with 4 inputs.  Provides boilerplate methods, so that
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
public class StandardOperation4<INPUT1 extends Object, INPUT2 extends Object, INPUT3 extends Object, INPUT4, OUTPUT extends Object>
    extends AbstractSymbol<OperationID<Operation4<INPUT1, INPUT2, INPUT3, INPUT4, OUTPUT>, OUTPUT>, Operation4<INPUT1, INPUT2, INPUT3, INPUT4, OUTPUT>>
    implements CurryableOperation4<INPUT1, INPUT2, INPUT3, INPUT4, OUTPUT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( StandardOperation4.class );




    public static class Curry1Input<CURRY_IN1 extends Object, CURRY_IN2, CURRY_IN3, CURRY_IN4 extends Object, CURRY_OUT extends Object>
        extends StandardOperation1<CURRY_IN4, CURRY_OUT>
        implements OperationBody1<CURRY_IN4, CURRY_OUT>, Serializable
    {
        private static final long serialVersionUID = MODULE.VERSION;

        private final Operation4<CURRY_IN1, CURRY_IN2, CURRY_IN3, CURRY_IN4, CURRY_OUT> operation;

        private final Value<CURRY_IN1> input1;
        private final Value<CURRY_IN2> input2;
        private final Value<CURRY_IN3> input3;

        public Curry1Input (
                            String name,
                            Operation4<CURRY_IN1, CURRY_IN2, CURRY_IN3, CURRY_IN4, CURRY_OUT> operation,
                            Value<CURRY_IN1> input1,
                            Value<CURRY_IN2> input2,
                            Value<CURRY_IN3> input3,
                            Type<CURRY_IN4> input4_type
                            )
        {
            super ( name,
                    input4_type,
                    operation == null
                        ? null
                        : operation.outputType (),
                    null ); // body = this.

            this.operation = operation;
            this.input1 = input1;
            this.input2 = input2;
            this.input3 = input3;
        }

        /**
         * @see musaico.foundation.typing.OperationBody1#evaluateBody(musaico.foundation.value.Value)
         */
        @Override
        public final Value<CURRY_OUT> evaluateBody (
                                                    Value<CURRY_IN4> input4
                                                    )
        {
            return this.operation.evaluate ( this.input1,
                                             this.input2,
                                             this.input3,
                                             input4 );
        }

        /**
         * @return The input # 1 to the operation.  Never null.
         */
        public final Value<CURRY_IN1> input1 ()
        {
            return this.input1;
        }

        /**
         * @return The input # 2 to the operation.  Never null.
         */
        public final Value<CURRY_IN2> input2 ()
        {
            return this.input2;
        }

        /**
         * @return The input # 3 to the operation.  Never null.
         */
        public final Value<CURRY_IN3> input3 ()
        {
            return this.input3;
        }

        /**
         * @return The curried operation.  Never null.
         */
        public final Operation4<CURRY_IN1, CURRY_IN2, CURRY_IN3, CURRY_IN4, CURRY_OUT> operation ()
        {
            return this.operation;
        }

    /**
     * @see musaico.foundation.typing.Symbol#rename(java.lang.String)
     */
    @Override
    public StandardOperation4.Curry1Input<CURRY_IN1, CURRY_IN2, CURRY_IN3, CURRY_IN4, CURRY_OUT> rename (
            String name
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
        {
            classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                                   name );

            return new StandardOperation4.Curry1Input<CURRY_IN1, CURRY_IN2, CURRY_IN3, CURRY_IN4, CURRY_OUT> (
                name,
                this.operation,
                this.input1,
                this.input2,
                this.input3,
                this.input1Type () ); //#4
        }

        /**
         * @see musaico.foundation.typing.Retypable#retype(java.lang.String, musaico.foundation.typing.Type)
         */
        @Override
        @SuppressWarnings("unchecked") // Cast Op<OUT> - Op2<IN1,2,3,4, OUT>,
                                       // Type<?> - Type<CURRY_IN4>.
        public StandardOperation4.Curry1Input<CURRY_IN1, CURRY_IN2, CURRY_IN3, CURRY_IN4, CURRY_OUT> retype (
                String name,
                OperationType<? extends Operation<CURRY_OUT>, CURRY_OUT> type
                )
            throws ParametersMustNotBeNull.Violation,
                   TypesMustHaveSameValueClasses.Violation,
                   ReturnNeverNull.Violation
        {
            this.checkRetype ( name, type );

            final Operation4<CURRY_IN1, CURRY_IN2, CURRY_IN3, CURRY_IN4, CURRY_OUT>
                underlying_operation =
                    (Operation4<CURRY_IN1, CURRY_IN2, CURRY_IN3, CURRY_IN4, CURRY_OUT>)
                    this.operation.retype ( this.operation.id ().name (),
                                            type );

            return new StandardOperation4.Curry1Input<CURRY_IN1, CURRY_IN2, CURRY_IN3, CURRY_IN4, CURRY_OUT> (
                name,
                underlying_operation,
                this.input1,
                this.input2,
                this.input3,
                (Type<CURRY_IN4>) type.inputType ( 3 ) ); //#4
        }
    }




    public static class Curry2Input<CURRY_IN1 extends Object, CURRY_IN2, CURRY_IN3, CURRY_IN4 extends Object, CURRY_OUT extends Object>
        extends StandardOperation2<CURRY_IN3, CURRY_IN4, CURRY_OUT>
        implements OperationBody2<CURRY_IN3, CURRY_IN4, CURRY_OUT>, Serializable
    {
        private static final long serialVersionUID = MODULE.VERSION;

        private final Operation4<CURRY_IN1, CURRY_IN2, CURRY_IN3, CURRY_IN4, CURRY_OUT> operation;

        private final Value<CURRY_IN1> input1;
        private final Value<CURRY_IN2> input2;

        public Curry2Input (
                            String name,
                            Operation4<CURRY_IN1, CURRY_IN2, CURRY_IN3, CURRY_IN4, CURRY_OUT> operation,
                            Value<CURRY_IN1> input1,
                            Value<CURRY_IN2> input2,
                            Type<CURRY_IN3> input3_type,
                            Type<CURRY_IN4> input4_type
                            )
        {
            super ( name,
                    input3_type,
                    input4_type,
                    operation == null
                        ? null
                        : operation.outputType (),
                    null ); // body = this.

            this.operation = operation;
            this.input1 = input1;
            this.input2 = input2;
        }

        /**
         * @see musaico.foundation.typing.OperationBody2#evaluateBody(musaico.foundation.value.Value, musaico.foundation.value.Value)
         */
        @Override
        public final Value<CURRY_OUT> evaluateBody (
                                                    Value<CURRY_IN3> input3,
                                                    Value<CURRY_IN4> input4
                                                    )
        {
            return this.operation.evaluate ( this.input1,
                                             this.input2,
                                             input3,
                                             input4 );
        }

        /**
         * @return The input # 1 to the operation.  Never null.
         */
        public final Value<CURRY_IN1> input1 ()
        {
            return this.input1;
        }

        /**
         * @return The input # 2 to the operation.  Never null.
         */
        public final Value<CURRY_IN2> input2 ()
        {
            return this.input2;
        }

        /**
         * @return The curried operation.  Never null.
         */
        public final Operation4<CURRY_IN1, CURRY_IN2, CURRY_IN3, CURRY_IN4, CURRY_OUT> operation ()
        {
            return this.operation;
        }

    /**
     * @see musaico.foundation.typing.Symbol#rename(java.lang.String)
     */
    @Override
    public StandardOperation4.Curry2Input<CURRY_IN1, CURRY_IN2, CURRY_IN3, CURRY_IN4, CURRY_OUT> rename (
            String name
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
        {
            classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                                   name );

            return new StandardOperation4.Curry2Input<CURRY_IN1, CURRY_IN2, CURRY_IN3, CURRY_IN4, CURRY_OUT> (
                name,
                this.operation,
                this.input1,
                this.input2,
                this.input1Type (), //#3
                this.input2Type () );//#4
        }

        /**
         * @see musaico.foundation.typing.Retypable#retype(java.lang.String, musaico.foundation.typing.Type)
         */
        @Override
        @SuppressWarnings("unchecked") // Cast Op<OUT> - Op4<IN1,2,3,4, OUT>,
                                       // Type<?> - Type<CURRY_IN3>.
                                       // Type<?> - Type<CURRY_IN4>.
        public StandardOperation4.Curry2Input<CURRY_IN1, CURRY_IN2, CURRY_IN3, CURRY_IN4, CURRY_OUT> retype (
                String name,
                OperationType<? extends Operation<CURRY_OUT>, CURRY_OUT> type
                )
            throws ParametersMustNotBeNull.Violation,
                   TypesMustHaveSameValueClasses.Violation,
                   ReturnNeverNull.Violation
        {
            this.checkRetype ( name, type );

            final Operation4<CURRY_IN1, CURRY_IN2, CURRY_IN3, CURRY_IN4, CURRY_OUT>
                underlying_operation =
                    (Operation4<CURRY_IN1, CURRY_IN2, CURRY_IN3, CURRY_IN4, CURRY_OUT>)
                    this.operation.retype ( this.operation.id ().name (),
                                            type );

            return new StandardOperation4.Curry2Input<CURRY_IN1, CURRY_IN2, CURRY_IN3, CURRY_IN4, CURRY_OUT> (
                name,
                underlying_operation,
                this.input1,
                this.input2,
                (Type<CURRY_IN3>) type.inputType ( 2 ), //#3
                (Type<CURRY_IN4>) type.inputType ( 3 ) ); //#4
        }
    }




    public static class Curry3Input<CURRY_IN1 extends Object, CURRY_IN2, CURRY_IN3 extends Object, CURRY_IN4 extends Object, CURRY_OUT extends Object>
        extends StandardOperation3<CURRY_IN2, CURRY_IN3, CURRY_IN4, CURRY_OUT>
        implements OperationBody3<CURRY_IN2, CURRY_IN3, CURRY_IN4, CURRY_OUT>, Serializable
    {
        private static final long serialVersionUID = MODULE.VERSION;

        private final Operation4<CURRY_IN1, CURRY_IN2, CURRY_IN3, CURRY_IN4, CURRY_OUT> operation;

        private final Value<CURRY_IN1> input1;

        public Curry3Input (
                            String name,
                            Operation4<CURRY_IN1, CURRY_IN2, CURRY_IN3, CURRY_IN4, CURRY_OUT> operation,
                            Value<CURRY_IN1> input1,
                            Type<CURRY_IN2> input2_type,
                            Type<CURRY_IN3> input3_type,
                            Type<CURRY_IN4> input4_type
                            )
        {
            super ( name,
                    input2_type,
                    input3_type,
                    input4_type,
                    operation == null
                        ? null
                        : operation.outputType (),
                    null ); // body = this.

            this.operation = operation;
            this.input1 = input1;
        }

        /**
         * @see musaico.foundation.typing.OperationBody3#evaluateBody(musaico.foundation.value.Value, musaico.foundation.value.Value, musaico.foundation.value.Value)
         */
        @Override
        public final Value<CURRY_OUT> evaluateBody (
                                                    Value<CURRY_IN2> input2,
                                                    Value<CURRY_IN3> input3,
                                                    Value<CURRY_IN4> input4
                                                    )
        {
            return this.operation.evaluate ( this.input1,
                                             input2,
                                             input3,
                                             input4 );
        }

        /**
         * @return The input # 1 to the operation.  Never null.
         */
        public final Value<CURRY_IN1> input1 ()
        {
            return this.input1;
        }

        /**
         * @return The curried operation.  Never null.
         */
        public final Operation4<CURRY_IN1, CURRY_IN2, CURRY_IN3, CURRY_IN4, CURRY_OUT> operation ()
        {
            return this.operation;
        }

    /**
     * @see musaico.foundation.typing.Symbol#rename(java.lang.String)
     */
    @Override
    public StandardOperation4.Curry3Input<CURRY_IN1, CURRY_IN2, CURRY_IN3, CURRY_IN4, CURRY_OUT> rename (
            String name
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
        {
            classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                                   name );

            return new StandardOperation4.Curry3Input<CURRY_IN1, CURRY_IN2, CURRY_IN3, CURRY_IN4, CURRY_OUT> (
                name,
                this.operation,
                this.input1,
                this.input1Type (), // #2
                this.input2Type (), // #3
                this.input3Type () );//#4
        }

        /**
         * @see musaico.foundation.typing.Retypable#retype(java.lang.String, musaico.foundation.typing.Type)
         */
        @Override
        @SuppressWarnings("unchecked") // Cast Op<OUT> - Op2<IN1,2,3,4, OUT>,
                                       // Type<?> - Type<CURRY_IN2>,
                                       // Type<?> - Type<CURRY_IN3>,
                                       // Type<?> - Type<CURRY_IN4>.
        public StandardOperation4.Curry3Input<CURRY_IN1, CURRY_IN2, CURRY_IN3, CURRY_IN4, CURRY_OUT> retype (
                String name,
                OperationType<? extends Operation<CURRY_OUT>, CURRY_OUT> type
                )
            throws ParametersMustNotBeNull.Violation,
                   TypesMustHaveSameValueClasses.Violation,
                   ReturnNeverNull.Violation
        {
            this.checkRetype ( name, type );

            final Operation4<CURRY_IN1, CURRY_IN2, CURRY_IN3, CURRY_IN4, CURRY_OUT>
                underlying_operation =
                    (Operation4<CURRY_IN1, CURRY_IN2, CURRY_IN3, CURRY_IN4, CURRY_OUT>)
                    this.operation.retype ( this.operation.id ().name (),
                                            type );

            return new StandardOperation4.Curry3Input<CURRY_IN1, CURRY_IN2, CURRY_IN3, CURRY_IN4, CURRY_OUT> (
                name,
                underlying_operation,
                this.input1,
                (Type<CURRY_IN2>) type.inputType ( 1 ), //#2
                (Type<CURRY_IN3>) type.inputType ( 2 ), //#3
                (Type<CURRY_IN4>) type.inputType ( 3 ) ); //#4
        }
    }




    // Unique identifier for this Operation.
    private final OperationID<Operation4<INPUT1, INPUT2, INPUT3, INPUT4, OUTPUT>, OUTPUT> id;

    // The core of this Operation, does all the work after
    // we've checked input constraints and before we've checked
    // output constraints.
    private final OperationBody4<INPUT1, INPUT2, INPUT3, INPUT4, OUTPUT> body;


    /**
     * <p>
     * Creates a new StandardOperation4 with the specified name
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
     * @param input2_type The type of input # 2 to this operation.
     *                    Must not be null.
     *
     * @param input3_type The type of input # 3 to this operation.
     *                    Must not be null.
     *
     * @param input4_type The type of input # 4 to this operation.
     *                    Must not be null.
     *
     * @param output_type The output type from this operation.
     *                    Must not be null.
     */
    protected StandardOperation4 (
                                  String name,
                                  Type<INPUT1> input1_type,
                                  Type<INPUT2> input2_type,
                                  Type<INPUT3> input3_type,
                                  Type<INPUT4> input4_type,
                                  Type<OUTPUT> output_type
                                  )
        throws ParametersMustNotBeNull.Violation
    {
        this ( name,
               input1_type,
               input2_type,
               input3_type,
               input4_type,
               output_type,
               null ); // body = this.
    }


    /**
     * <p>
     * Creates a new StandardOperation4 with the specified name
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
     * @param input2_type The type of input # 2 to this operation.
     *                    Must not be null.
     *
     * @param input3_type The type of input # 3 to this operation.
     *                    Must not be null.
     *
     * @param input4_type The type of input # 4 to this operation.
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
    public StandardOperation4 (
                               String name,
                               Type<INPUT1> input1_type,
                               Type<INPUT2> input2_type,
                               Type<INPUT3> input3_type,
                               Type<INPUT4> input4_type,
                               Type<OUTPUT> output_type,
                               OperationBody4<INPUT1, INPUT2, INPUT3, INPUT4, OUTPUT> body
                               )
        throws ParametersMustNotBeNull.Violation
    {
        this ( name,
               new OperationType4<INPUT1, INPUT2, INPUT3, INPUT4, OUTPUT> (
                   input1_type,
                   input2_type,
                   input3_type,
                   input4_type,
                   output_type ),
               body );
    }


    /**
     * <p>
     * Creates a new StandardOperation4 with the specified name
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
    protected StandardOperation4 (
                                  String name,
                                  OperationType4<INPUT1, INPUT2, INPUT3, INPUT4, OUTPUT> operation_type
                                  )
        throws ParametersMustNotBeNull.Violation
    {
        this ( name,
               operation_type,
               null ); // body = this.
    }


    /**
     * <p>
     * Creates a new StandardOperation4 with the specified name
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
    public StandardOperation4 (
                               String name,
                               OperationType4<INPUT1, INPUT2, INPUT3, INPUT4, OUTPUT> operation_type,
                               OperationBody4<INPUT1, INPUT2, INPUT3, INPUT4, OUTPUT> body
                               )
        throws ParametersMustNotBeNull.Violation
    {
        this ( new OperationID<Operation4<INPUT1, INPUT2, INPUT3, INPUT4, OUTPUT>, OUTPUT> (
                   name,
                   operation_type ),
               body );
    }


    /**
     * <p>
     * Creates a new StandardOperation4 with the specified id.
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
    protected StandardOperation4 (
                                  OperationID<Operation4<INPUT1, INPUT2, INPUT3, INPUT4, OUTPUT>, OUTPUT> id
                                  )
        throws ParametersMustNotBeNull.Violation
    {
        this ( id,
               null ); // body = this.
    }


    /**
     * <p>
     * Creates a new StandardOperation4 with the specified id.
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
    public StandardOperation4 (
                               OperationID<Operation4<INPUT1, INPUT2, INPUT3, INPUT4, OUTPUT>, OUTPUT> id,
                               OperationBody4<INPUT1, INPUT2, INPUT3, INPUT4, OUTPUT> body
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
                OperationBody4<INPUT1, INPUT2, INPUT3, INPUT4, OUTPUT> this_as_a_body =
                    (OperationBody4<INPUT1, INPUT2, INPUT3, INPUT4, OUTPUT>) this;
                this.body = this_as_a_body;
            }
            catch ( ClassCastException e )
            {
                throw ParametersMustNotBeNull.CONTRACT.violation (
                    StandardOperation4.class,
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
     * @see musaico.foundation.typing.Operation4#evaluate(musaico.value.Value, musaico.value.Value, musaico.value.Value, musaico.value.Value)
     */
    @Override
    public final Value<OUTPUT> evaluate (
                                         Value<INPUT1> input1,
                                         Value<INPUT2> input2,
                                         Value<INPUT3> input3,
                                         Value<INPUT4> input4
                                         )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  input1,
                                  input2,
                                  input3,
                                  input4 );

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
        try
        {
            this.input2Type ().checkValue ( input2 );
        }
        catch ( TypingViolation violation )
        {
            return StandardOperation1.badInput ( this,      // operation
                                                 1,         // Input #
                                                 violation, // cause
                                                 input2 );  // inputs
        }
        try
        {
            this.input3Type ().checkValue ( input3 );
        }
        catch ( TypingViolation violation )
        {
            return StandardOperation1.badInput ( this,      // operation
                                                 2,         // Input #
                                                 violation, // cause
                                                 input3 );  // inputs
        }
        try
        {
            this.input4Type ().checkValue ( input4 );
        }
        catch ( TypingViolation violation )
        {
            return StandardOperation1.badInput ( this,      // operation
                                                 3,         // Input #
                                                 violation, // cause
                                                 input4 );  // inputs
        }

        // Main Operation evaluate:
        final Value<OUTPUT> output;
        try
        {
            output = this.body.evaluateBody ( input1,
                                              input2,
                                              input3,
                                              input4 );
        }
        catch ( RuntimeException e )
        {
            return StandardOperation1.badInput ( this,      // operation
                                                 999,       // Input #
                                                 e,         // cause
                                                 input1,    // inputs
                                                 input2,
                                                 input3,
                                                 input4 );
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
     * @see musaico.foundation.typing.CurryableOperation4#evaluate(musaico.foundation.value.Value)
     */
    @Override
    public final ZeroOrOne<Operation3<INPUT2, INPUT3, INPUT4, OUTPUT>> evaluate (
                     final Value<INPUT1> input1
                     )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  input1 );

        final String curried_name =
            this.id ().name ()
            + " ( "
            + input1
            + " )";
        TypingViolation violation = null;
        try
        {
            this.input1Type ().checkValue ( input1 );
        }
        catch ( TypingViolation v )
        {
            violation = v;
        }

        final StandardOperation4.Curry3Input<INPUT1, INPUT2, INPUT3, INPUT4, OUTPUT> curried_operation =
            new StandardOperation4.Curry3Input<INPUT1, INPUT2, INPUT3, INPUT4, OUTPUT> (
                    curried_name,
                    this,
                    input1,
                    this.input2Type (),
                    this.input3Type (),
                    this.input4Type () );

        final OperationType3<INPUT2, INPUT3, INPUT4, OUTPUT> curried_operation_type =
            curried_operation.type ();

        final ZeroOrOne<Operation3<INPUT2, INPUT3, INPUT4, OUTPUT>> v_curried_operation;
        if ( violation == null )
        {
            v_curried_operation =
                new One<Operation3<INPUT2, INPUT3, INPUT4, OUTPUT>> ( curried_operation_type.valueClass (),
                        curried_operation );
        }
        else
        {
            v_curried_operation =
                curried_operation_type.noValue ( violation );
        }

        return v_curried_operation;
    }


    /**
     * @see musaico.foundation.typing.CurryableOperation4#evaluate(musaico.foundation.value.Value, musaico.foundation.value.Value)
     */
    @Override
    public final ZeroOrOne<Operation2<INPUT3, INPUT4, OUTPUT>> evaluate (
                                                                         final Value<INPUT1> input1,
                                                                         final Value<INPUT2> input2
                                                                         )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  input1,
                                  input2 );

        final String curried_name =
            this.id ().name ()
            + " ( "
            + input1
            + ", "
            + input2
            + " )";
        TypingViolation violation = null;
        try
        {
            this.input1Type ().checkValue ( input1 );
        }
        catch ( TypingViolation v )
        {
            violation = v;
        }
        if ( violation == null )
        {
            try
            {
                this.input2Type ().checkValue ( input2 );
            }
            catch ( TypingViolation v )
            {
                violation = v;
            }
        }

        final StandardOperation4.Curry2Input<INPUT1, INPUT2, INPUT3, INPUT4, OUTPUT> curried_operation =
            new StandardOperation4.Curry2Input<INPUT1, INPUT2, INPUT3, INPUT4, OUTPUT> (
                    curried_name,
                    this,
                    input1,
                    input2,
                    this.input3Type (),
                    this.input4Type () );

        final OperationType2<INPUT3, INPUT4, OUTPUT> curried_operation_type =
            curried_operation.type ();

        final ZeroOrOne<Operation2<INPUT3, INPUT4, OUTPUT>> v_curried_operation;
        if ( violation == null )
        {
            v_curried_operation =
                new One<Operation2<INPUT3, INPUT4, OUTPUT>> ( curried_operation_type.valueClass (),
                                                      curried_operation );
        }
        else
        {
            v_curried_operation =
                curried_operation_type.noValue ( violation );
        }

        return v_curried_operation;
    }


    /**
     * @see musaico.foundation.typing.CurryableOperation4#evaluate(musaico.foundation.value.Value, musaico.foundation.value.Value, musaico.foundation.value.Value)
     */
    @Override
    public final ZeroOrOne<Operation1<INPUT4, OUTPUT>> evaluate (
                     final Value<INPUT1> input1,
                     final Value<INPUT2> input2,
                     final Value<INPUT3> input3
                     )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  input1,
                                  input2,
                                  input3 );

        final String curried_name =
            this.id ().name ()
            + " ( "
            + input1
            + ", "
            + input2
            + ", "
            + input3
            + " )";
        TypingViolation violation = null;
        try
        {
            this.input1Type ().checkValue ( input1 );
        }
        catch ( TypingViolation v )
        {
            violation = v;
        }
        if ( violation == null )
        {
            try
            {
                this.input2Type ().checkValue ( input2 );
            }
            catch ( TypingViolation v )
            {
                violation = v;
            }
        }
        if ( violation == null )
        {
            try
            {
                this.input3Type ().checkValue ( input3 );
            }
            catch ( TypingViolation v )
            {
                violation = v;
            }
        }

        final StandardOperation4.Curry1Input<INPUT1, INPUT2, INPUT3, INPUT4, OUTPUT> curried_operation =
            new StandardOperation4.Curry1Input<INPUT1, INPUT2, INPUT3, INPUT4, OUTPUT> (
                    curried_name,
                    this,
                    input1,
                    input2,
                    input3,
                    this.input4Type () );

        final OperationType1<INPUT4, OUTPUT> curried_operation_type =
            curried_operation.type ();

        final ZeroOrOne<Operation1<INPUT4, OUTPUT>> v_curried_operation;
        if ( violation == null )
        {
            v_curried_operation =
                new One<Operation1<INPUT4, OUTPUT>> ( curried_operation_type.valueClass (),
                                                      curried_operation );
        }
        else
        {
            v_curried_operation =
                curried_operation_type.noValue ( violation );
        }

        return v_curried_operation;
    }


    /**
     * @see musaico.foundation.typing.Operation#evaluate(java.util.List)
     */
    @SuppressWarnings("unchecked") // try...catch Value<Object>-Value<INPUT#>.
    public Value<OUTPUT> evaluate (
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

        final Value<INPUT1> input1;
        try
        {
            input1 = (Value<INPUT1>) inputs.get ( 0 );
        }
        catch ( ClassCastException e )
        {
            return StandardOperation1.badInput ( this,      // operation
                                                 0,         // Input #
                                                 e,         // cause
                                                 inputs );  // inputs
        }

        final Value<INPUT2> input2;
        try
        {
            input2 = (Value<INPUT2>) inputs.get ( 1 );
        }
        catch ( ClassCastException e )
        {
            return StandardOperation1.badInput ( this,      // operation
                                                 1,         // Input #
                                                 e,         // cause
                                                 inputs );  // inputs
        }

        final Value<INPUT3> input3;
        try
        {
            input3 = (Value<INPUT3>) inputs.get ( 2 );
        }
        catch ( ClassCastException e )
        {
            return StandardOperation1.badInput ( this,      // operation
                                                 2,         // Input #
                                                 e,         // cause
                                                 inputs );  // inputs
        }

        final Value<INPUT4> input4;
        try
        {
            input4 = (Value<INPUT4>) inputs.get ( 3 );
        }
        catch ( ClassCastException e )
        {
            return StandardOperation1.badInput ( this,      // operation
                                                 3,         // Input #
                                                 e,         // cause
                                                 inputs );  // inputs
        }

        return this.evaluate ( input1, input2, input3, input4 );
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
     * @see musaico.foundation.typing.OperationInputs2#input2Type()
     */
    @Override
    public final Type<INPUT2> input2Type ()
    {
        return this.type ().input2Type ();
    }


    /**
     * @see musaico.foundation.typing.OperationInputs3#input3Type()
     */
    @Override
    public final Type<INPUT3> input3Type ()
    {
        return this.type ().input3Type ();
    }


    /**
     * @see musaico.foundation.typing.OperationInputs4#input4Type()
     */
    @Override
    public final Type<INPUT4> input4Type ()
    {
        return this.type ().input4Type ();
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
    public StandardOperation4<INPUT1, INPUT2, INPUT3, INPUT4, OUTPUT> rename (
                                       String name
                                       )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               name );

        return new StandardOperation4<INPUT1, INPUT2, INPUT3, INPUT4, OUTPUT> (
            name,
            this.input1Type (),
            this.input2Type (),
            this.input3Type (),
            this.input4Type (),
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
                                   // Cast Type<?> - Type<INPUT4>.
    public StandardOperation4<INPUT1, INPUT2, INPUT3, INPUT4, OUTPUT> retype (
                                       String name,
                                       OperationType<? extends Operation<OUTPUT>, OUTPUT> type
                                       )
        throws ParametersMustNotBeNull.Violation,
               TypesMustHaveSameValueClasses.Violation,
               ReturnNeverNull.Violation
    {
        this.checkRetype ( name, type );

        return new StandardOperation4<INPUT1, INPUT2, INPUT3, INPUT4, OUTPUT> (
            name,
            (Type<INPUT1>) type.inputType ( 0 ),
            (Type<INPUT2>) type.inputType ( 1 ),
            (Type<INPUT3>) type.inputType ( 2 ),
            (Type<INPUT4>) type.inputType ( 3 ),
            type.outputType (),
            this.body );
    }


    /**
     * @see musaico.foundation.typing.Operation#type()
     */
    @Override
    @SuppressWarnings("unchecked") // Cast OpType<OP, OUT> - OpType4
    public final OperationType4<INPUT1, INPUT2, INPUT3, INPUT4, OUTPUT> type ()
    {
        return (OperationType4<INPUT1, INPUT2, INPUT3, INPUT4, OUTPUT>)
            this.id ().operationType ();
    }
}
