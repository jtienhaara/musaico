package musaico.foundation.type;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.graph.Arc;
import musaico.foundation.graph.Graph;

import musaico.foundation.machine.InputsMachine;

import musaico.foundation.value.Countable;
import musaico.foundation.value.Maybe;
import musaico.foundation.value.NotOne;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueViolation;

import musaico.foundation.value.builder.ValueBuilder;

import musaico.foundation.value.abnormal.Error;

import musaico.foundation.value.finite.One;


/**
 * <p>
 * An Operation transforms an input value into an output value.
 * </p>
 *
 * <p>
 * An Operation must not have any side-effects (reading or writing state,
 * performing input/output operations, and so on) unless they are
 * explicitly declared.
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
 * @see musaico.foundation.type.MODULE#COPYRIGHT
 * @see musaico.foundation.type.MODULE#LICENSE
 */
public class Operation
    extends BasicSymbol
    implements Namespace, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final Advocate classContracts =
        new Advocate ( Operation.class );


    // The actual implementation of this Operation.  Performs the
    // work for each call to invoke ().
    private final OperationBody<?> body;

    // The inputs state machine for this Operation.
    // Most Operations start in the entry state.  But a curried Operation
    // starts in some state after one or more inputs have already
    // induced transitions.  This machine will never change state itself;
    // but a copy of it will feed the OperationBody during invoke (),
    // and a copy will be returned any time someone calls machine ().
    private final InputsMachine<Object, Symbol, Type> machine;


    /**
     * <p>
     * Creates a new Operation.
     * </p>
     *
     * @param name The name of this Operation, such as
     *             "accelerate" or "count_chickens" and so on.
     *             Must not be null.
     *
     * @param inputs The inputs graph for this Operation.
     *               Determines the type(s) and order of
     *               the inputs allowed to the invoke () method.
     *               This Operation will begin in the entry ()
     *               state of the specified graph (the normal
     *               starting state for an Operation, not curried).
     *               Must not be null.
     *
     * @param body The actual implementation of this Operation.
     *             Performs the work for each call to invoke ().
     *             Must not be null.
     */
    @SuppressWarnings("unchecked") // Generic varargs new InputsMachine( ... )
    public Operation (
                      String name,
                      Graph<Symbol, Type> inputs,
                      OperationBody<?> body
                      )
        throws ParametersMustNotBeNull.Violation
    {
        this ( name,
               new InputsMachine<Object, Symbol, Type> (
                   inputs,
                   inputs == null
                       ? null
                       : inputs.entry () ),
               body );
    }


    /**
     * <p>
     * Creates a new Operation.
     * </p>
     *
     * <p>
     * Protected.  Creating an Operation in a specific state is
     * reserved for callers we know are not going to mess with the
     * machine after creating this Operation.
     * </p>
     *
     * @param name The name of this Operation, such as
     *             "accelerate" or "count_chickens" and so on.
     *             Must not be null.
     *
     * @param machine The inputs state machine for this Operation.
     *                Most Operations start in the entry state.
     *                But a curried Operation starts in some state
     *                after one or more inputs have already induced
     *                transitions.  This machine will
     *                never change state itself; but a copy of it will
     *                feed the OperationBody during invoke (), and
     *                a copy will be returned any time someone
     *                calls machine ().  Must not be null.
     *
     * @param body The actual implementation of this Operation.
     *             Performs the work for each call to invoke ().
     *             Must not be null.
     */
    protected Operation (
                         String name,
                         InputsMachine<Object, Symbol, Type> machine,
                         OperationBody<?> body
                         )
        throws ParametersMustNotBeNull.Violation,
               ExitNodeMustBeAType.Violation
    {
        super ( name ); // checks for nulls.

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               body, machine );

        final Graph<Symbol, Type> inputs = machine.graph ();
        final Symbol exit_node =
            inputs.exit ()
                  .orNull ();
        if ( ! ( exit_node instanceof Type ) )
        {
            final ExitNodeMustBeAType.Violation violation =
                ExitNodeMustBeAType.CONTRACT.violation ( this,
                                                         inputs );
            throw violation;
        }

        this.machine = machine;
        this.body = body;
    }


    /**
     * @return The body of this Operation, which is evaluated
     *         whenever this Operation's invocation <code> graph () </code>
     *         is satisfied by the inputs to <code> invoke () </code>.
     *         Never null.
     */
    public final OperationBody<?> body ()
    {
        return this.body;
    }


    /**
     * <p>
     * Compiles this Operation, along with the specified inputs,
     * into a state machine which can be passed to the
     * <code> invoke ( machine ) </code> method.
     * </p>
     */
    @SuppressWarnings({"unchecked", "rawtypes"}) // Contortions to allow
    // the caller to call compile ( Value<String>, Value<Integer>, ... )
    // instead of compile ( Value<Object>, Value<Object>, ... ).
    public final InputsMachine<Object, Symbol, Type> compile (
            Value<?> ... inputs
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  (Object) inputs );
        this.contracts ().check ( Parameter1.MustContainNoNulls.CONTRACT,
                                  inputs );

        // Create a new state machine and step through the input states.
        final InputsMachine<Object, Symbol, Type> invocation =
            this.machine ();
        // Downcast from Value<?> to Value<Object>.
        final Value<Object> [] input_objects =
            new Value [ inputs.length ];
        for ( int i = 0; i < inputs.length; i ++ )
        {
            input_objects [ i ] = (Value<Object>) inputs [ i ];
        }

        // Transition the state machine, but ignore the new state.
        // The caller can always call invocation.state ().
        invocation.transition ( input_objects );

        return invocation;
    }


    /**
     * <p>
     * Returns the inputs graph for this Operation.
     * </p>
     *
     * @see musaico.foundation.type.Operation#graph()
     */
    @Override
    public final Graph<Symbol, Type> graph ()
        throws ReturnNeverNull.Violation
    {
        return this.machine.graph ();
    }


    /**
     * @return The name of the (first/next) input to this Operation.
     *         Never null.
     */
    public final Maybe<String> inputName ()
    {
        final Maybe<Symbol> state =
            this.machine.state ();
        if ( state instanceof NotOne )
        {
            final NotOne<Symbol> broken_state = (NotOne<Symbol>) state;
            final ValueViolation violation = broken_state.valueViolation ();
            final Error<String> error =
                new Error<String> ( String.class,
                                    violation );
            return error;
        }

        final Symbol next_input = state.orNull ();
        final One<String> next_input_name =
            new One<String> ( String.class,
                              next_input.name () );

        return next_input_name;
    }


    /**
     * @return The (first/next) input to this Operation.
     *         Never null.
     */
    public final Maybe<Symbol> inputSymbol ()
    {
        return this.machine.state ();
    }


    /**
     * @return The allowed Type(s) of the (first/next) input to this operation.
     *         Returns One Type if the (first/next) input can be of only
     *         one specific type; or possibly Many Types, if any of a number
     *         of input Types is allowed; and so on.  Never null.
     */
    public final Value<Type> inputTypes ()
    {
        final ValueBuilder<Type> builder =
            new ValueBuilder<Type> ( Type.class );
        final Maybe<Symbol> state = this.machine.state ();
        if ( state instanceof NotOne )
        {
            final NotOne<Symbol> broken_state = (NotOne<Symbol>) state;
            final ValueViolation violation = broken_state.valueViolation ();
            final Error<Type> error =
                new Error<Type> ( Type.class,
                                  violation );
            return error;
        }

        final Symbol input_symbol = state.orNull ();
        for ( Arc<Symbol, Type> arc
                  : this.machine.graph ().arcs ( input_symbol ) )
        {
            final Type input_type = arc.arc ();
            builder.add ( input_type );
        }

        final Countable<Type> input_types = builder.build ();

        return input_types;
    }


    /**
     * <p>
     * Invokes this Operation with the specified InputsMachine inputs.
     * </p>
     *
     * <p>
     * If the inputs machine is at the exit state in this machine's
     * <code> graph () </code>, then the result
     * will be the output from invoking this Operation (which
     * might be Just a value, or could be No value, an Error,
     * and so on).
     * </p>
     *
     * <p>
     * If the inputs machine is not at the exit state in this
     * Operation's graph, then the result will
     * be One new curried Operation, containing the inputs so
     * far, and the resultant state of the inputs.
     * </p>
     *
     * <p>
     * If the inputs machine has anything but One current state,
     * then the output will be an Error.
     * </p>
     *
     * @param invocation The inputs machines to this Operation, including
     *                   all inputs, and the state which resulted from
     *                   compiling those inputs.  Must not be null.
     *
     * @return The output from invoking this Operation with the
     *         specified inputs machine.  Never null.
     */
    @SuppressWarnings("unchecked") // Contortions to allow
    // the provider to return Value<String> from a "get name" type
    // OperationBody vs. Value<Integer> from a "get age" type operationBody.
    public final Value<Object> invoke (
            InputsMachine<Object, Symbol, Type> invocation
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  invocation );

        final Maybe<Symbol> state = invocation.state ();

        if ( state instanceof NotOne )
        {
            // Invalid input(s).
            final NotOne<Symbol> broken_state = (NotOne<Symbol>) state;
            final ValueViolation violation = broken_state.valueViolation ();
            final Error<Object> error = new Error<Object> ( Object.class,
                                                            violation );
            return error;
        }
        else if ( this.machine.graph ().exit ().equals ( state ) )
        {
            // Complete invocation.  Let the OperationBody do the
            // rest of the work.
            final Value<?> output =
                this.body.execute ( invocation );
            return (Value<Object>) output;
        }

        // Incomplete invocation -- inputs missing.
        // We return a new curried Operation with the inputs so far.
        // Later someone can invoke () it with the remaining inputs.
        final Operation curried = new Operation ( this.name (),
                                                  invocation,
                                                  this.body );

        final One<?> one_extra_spicy_curried_coming_right_up =
            new One<Operation> ( Operation.class,
                                 curried );

        return (One<Object>) one_extra_spicy_curried_coming_right_up;
    }


    /**
     * <p>
     * Invokes this Operation with the specified inputs.
     * </p>
     *
     * <p>
     * If the inputs completely satisfy this Operation's
     * invocation <code> graph () </code>, then the result
     * will be the output from invoking this Operation (which
     * might be Just a value, or could be No value, an Error,
     * and so on).
     * </p>
     *
     * <p>
     * If the inputs are valid but do not completely satisfy
     * this Operations invocation graph, then the result will
     * be One new curried Operation, containing the inputs so
     * far, and the resultant state of the inputs.
     * </p>
     *
     * <p>
     * If any of the specified input(s) is invalid, then the output
     * will be an Error.
     * </p>
     *
     * @param inputs Zero or more inputs to this Operation.
     *               Must not be null.  Must not contain any null elements.
     *
     * @return The output from invoking this Operation with the
     *         specified input(s).  Never null.
     */
    public Value<Object> invoke (
                                 Value<?> ... inputs
                                 )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation,
        // Parameter1.MustContainNoNulls.Violation:
        // Create a new state machine and step through the input states.
        final InputsMachine<Object, Symbol, Type> invocation =
            this.compile ( inputs );
        final Value<Object> output =
            this.invoke ( invocation );

        return output;
    }


    /**
     * @see musaico.foundation.type.Operation#machine()
     */
    @Override
    public final InputsMachine<Object, Symbol, Type> machine ()
        throws ReturnNeverNull.Violation
    {
        // Create a copy of this Operation's inputs machine,
        // so that the caller can modify the state however they
        // want without affecting us.
        return this.machine.state ( this.machine.state () );
    }


    /**
     * @return The Type of output returned by this Operation whenever
     *         the input(s) satisfy the invocation <code> graph () <code>.
     *         Never null.
     */
    @SuppressWarnings("unchecked") // Compiler doesn't know that we checked
    // the exit symbol to be a Type during our constructor, and it hasn't
    // changed since then.
    public final One<Type> outputType ()
    {
        final One<Symbol> one_exit_symbol = this.machine.graph ().exit ();
        final One<Type> one_output_type =
            new One<Type> ( Type.class,
                            (Type) one_exit_symbol.orNull () );
        return one_output_type;
    }


    /**
     * @see java.lang.Object.toString()
     */
    @Override
    public String toString ()
    {
        final StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( this.name () );
        sbuf.append ( " (" );

        // During BasicSymbol constructor:
        if ( this.machine == null )
        {
            sbuf.append ( "...)" );
            return sbuf.toString ();
        }

        boolean is_first = true;
        for ( Value<Object> input : this.machine.inputs () )
        {
            if ( is_first )
            {
                is_first = false;
            }
            else
            {
                sbuf.append ( "," );
            }

            if ( input.hasValue () )
            {
                sbuf.append ( input.orNull () );
            }
            else
            {
                final String class_name = ClassName.of ( input.getClass () );
                sbuf.append ( class_name );
            }
        }

        sbuf.append ( ")" );

        return sbuf.toString ();
    }
}
