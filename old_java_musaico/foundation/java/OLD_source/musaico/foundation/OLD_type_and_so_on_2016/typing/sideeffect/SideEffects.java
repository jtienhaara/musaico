package musaico.foundation.typing.sideeffect;

import java.io.Serializable;

import java.lang.reflect.Constructor;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.ObjectContracts;
import musaico.foundation.contract.Violation;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.typing.TypingViolation;

import musaico.foundation.value.Blocking;
import musaico.foundation.value.DefaultValuesRegistry;
import musaico.foundation.value.Dimensional;
import musaico.foundation.value.DimensionalMustHaveAtLeastNDimensions;
import musaico.foundation.value.Empty;
import musaico.foundation.value.Error;
import musaico.foundation.value.Idempotent;
import musaico.foundation.value.Just;
import musaico.foundation.value.No;
import musaico.foundation.value.Partial;
import musaico.foundation.value.Timeout;
import musaico.foundation.value.UncheckedValueViolation;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueBuilder;
import musaico.foundation.value.ValueIterator;
import musaico.foundation.value.ValueProcessor;
import musaico.foundation.value.ValueViolation;
import musaico.foundation.value.Warning;


/**
 * <p>
 * Input to or output from an Operation, which carries along with
 * it side-effects: reading/writing state; performing input/output;
 * and so on.
 * </p>
 *
 * <p>
 * Values with SideEffects are treated as failed (No) results
 * unless the receiver explicitly retrieves the main <code> value () </code>
 * from the SideEffects.
 * </p>
 *
 * <pre>
 *     Value<Integer> input = new One<Integer> ( Integer.class, 1 );
 *     SideEffectReadableState state_to_read = ...;
 *     SideEffectInput input_from_keyboard = ...;
 *     SideEffects<Integer> input_with_side_effects =
 *         new SideEffects<Integer> ( Integer.class,  // value_class
 *                                    input,          // value
 *                                    state_to_read,  // side_effects...
 *                                    input_from_keyboard );
 *     Operation<Integer> operation = ...;
 *     SideEffects<Integer> output_with_side_effects =
 *         operation.evaluate ( input_with_side_effects );
 *     Value<Integer> output = output_with_side_effects.value ();
 * </pre>
 *
 * <p>
 * Be warned that, although the SideEffects container itself is
 * Idempotent (since calls to <code> length () </code>
 * and <code> head () </code> and so on always return the same
 * content), the main value and the side effect value contained in
 * the SideEffects value might not be Idempotent.
 * Calling <code> Idempotent () </code> on the SideEffects will return
 * a new SideEffects that contains an Idempotent main value and
 * an Idempotent side effect value.
 * </p>
 *
 * <p>
 * In order to do anything useful with SideEffects as input, an Operation
 * must deal with them explicitly.
 * </p>
 *
 * <pre>
 *     public SideEffects<Integer> evaluate (
 *                                           Value<Integer> input_value
 *                                           )
 *     {
 *         final SideEffects<Integer> input_with_side_effects;
 *         try
 *         {
 *             input_with_side_effects =
 *                 ValueMustHaveSideEffects.check (
 *                     this,
 *                     input_value,
 *                     SideEffectReadableState.TYPE,
 *                     SideEffectInput.TYPE );
 *         }
 *         catch ( TypingViolation violation )
 *         {
 *             return this.outputType ().noValue ( violation );
 *         }
 *
 *         final Value<Integer> input = input_with_side_effects.value ();
 *         final Value<SideEffect> input_side_effects =
 *             input_with_side_effects.sideEffects ();
 *
 *         ... Perform Operation ...
 *
 *         final Value<Integer> output = ...;
 *         final Value<SideEffects> output_side_effects = input_side_effects;
 *
 *         final SideEffects<Integer> output_with_side_effects =
 *             new SideEffects<Integer> ( Integer.class,         // value_class
 *                                        output,                // value
 *                                        output_side_effects ); //side_effects
 *         return output_with_side_effects;
 *     }
 * </pre>
 *
 *
 * <p>
 * In Java every Value must be Serializable in order to
 * play nicely across RMI.  However users of the Value
 * must be careful, since the values and expected data stored inside
 * might not be Serializable.
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
 * @see musaico.foundation.typing.sideeffect.MODULE#COPYRIGHT
 * @see musaico.foundation.typing.sideeffect.MODULE#LICENSE
 */
public class SideEffects<VALUE extends Object>
    implements Empty<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Enforces static parameter obligations and so on for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( SideEffects.class );


    /** Enforces parameter obligations and so on for us. */
    private final ObjectContracts contracts;

    /** The expected class of the conditional value. */
    private final Class<VALUE> expectedClass;

    /** The main value, excluding the side-effects.
     *  Never itself a SideEffects (any SideEffects passed in to
     *  our constructor are merged). */
    private final Value<VALUE> value;

    /** The side-effects, excluding the main value.
     *  Never itself a SideEffects. */
    private final Value<SideEffect> sideEffects;

    /** The violation whenever a caller is not expecting SideEffects. */
    private final ValueViolation valueViolation;


    /**
     * <p>
     * Creates a new SideEffects value with the specified
     * expected class, and a violation of a "must be one value"
     * contract to explain to callers why the value at the heart
     * of the side effects cannot be accessed.
     * </p>
     *
     * @param expected_class The expected class of the conditional Value.
     *                       For example, the expected class for a
     *                       <code> Value&lt;Number&gt; </code>
     *                       is always <code> Number.class </code>.
     *                       Must not be null.
     *
     * @param value The main Value, excluding the side-effects.
     *              If the value is itself a SideEffects then the side-effects
     *              will be merged into this new SideEffects, and the
     *              <code> value () </code> taken to be the new main value.
     *              Must not be null.
     *
     * @param side_effects The side-effects, excluding the main value.
     *                     Must not contain any null elements.
     *                     Must not be null.
     */
    public SideEffects (
                        Class<VALUE> expected_class,
                        Value<VALUE> value,
                        SideEffect ... side_effects
                        )
        throws ParametersMustNotBeNull.Violation,
               ValueMustNotHaveSideEffects.Violation
    {
        this ( expected_class,
               value,
               new ValueBuilder<SideEffect> ( SideEffect.class,
                                              side_effects ).build () );
    }


    /**
     * <p>
     * Creates a new SideEffects value with the specified
     * expected class, and a violation of a "must be one value"
     * contract to explain to callers why the value at the heart
     * of the side effects cannot be accessed.
     * </p>
     *
     * @param expected_class The expected class of the conditional Value.
     *                       For example, the expected class for a
     *                       <code> Value&lt;Number&gt; </code>
     *                       is always <code> Number.class </code>.
     *                       Must not be null.
     *
     * @param value The main Value, excluding the side-effects.
     *              If the value is itself a SideEffects then the side-effects
     *              will be merged into this new SideEffects, and the
     *              <code> value () </code> taken to be the new main value.
     *              Must not be null.
     *
     * @param side_effects The side-effects, excluding the main value.
     *                     Must not be a SideEffects.  Must not be null.
     */
    public SideEffects (
                        Class<VALUE> expected_class,
                        Value<VALUE> value,
                        Value<SideEffect> side_effects
                        )
        throws ParametersMustNotBeNull.Violation,
               ValueMustNotHaveSideEffects.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               expected_class,
                               value,
                               side_effects );
        classContracts.check ( ValueMustNotHaveSideEffects.CONTRACT,
                               side_effects );

        this.expectedClass = expected_class;
        if ( value instanceof SideEffects )
        {
            final SideEffects<VALUE> value_with_side_effects =
                (SideEffects<VALUE>) value;
            this.value = value_with_side_effects.value ();
            final Value<SideEffect> copy_side_effects =
                value_with_side_effects.sideEffects ();
            this.sideEffects =
                new ValueBuilder<SideEffect> ( SideEffect.class,
                                               copy_side_effects )
                .addAll ( side_effects )
                .build ();
        }
        else
        {
            this.value = value;
            this.sideEffects = side_effects;
        }

        final TypingViolation violation =
            ValueMustNotHaveSideEffects.CONTRACT.violation ( this, this );
        this.valueViolation = new ValueViolation ( violation );

        this.contracts = new ObjectContracts ( this );
    }


    /**
     * @see musaico.foundation.value.Value#and(musaico.foundation.value.Value)
     *
     * <p>
     * Note: SideEffects follows the same rules as No value.
     * See the truth table for No for details.
     * </p>
     */
    @Override
    public final Value<VALUE> and (
                                   Value<VALUE> that
                                   )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               that );

        if ( that instanceof Error )
        {
            // SideEffects value this & Error value that = that.
            return that;
        }
        else if ( that instanceof Timeout )
        {
            // SideEffects value this & Timeout value that = that.
            return that;
        }
        else if ( that instanceof Blocking )
        {
            // SideEffects value this & Blocking value that = this.
            return this;
        }
        else if ( that instanceof Warning )
        {
            // SideEffects value this & Warning value that = that.
            return that;
        }
        else if ( that instanceof Partial )
        {
            // SideEffects value this & Partial value that = this.
            return this;
        }
        else if ( that instanceof Just )
        {
            // SideEffects value this & Just value that = this.
            return this;
        }
        else // Any other Unjust value, including No and SideEffects.
        {
            // SideEffects value this & Unjust value that = this.
            return this;
        }
    }


    /**
     * @see musaico.foundation.value.Dimensional#at(long[])
     */
    @Override
    public final Dimensional<VALUE> at (
                                        long... indices
                                        )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               indices );

        if ( indices.length > 0 )
        {
            final DimensionalMustHaveAtLeastNDimensions contract =
                new DimensionalMustHaveAtLeastNDimensions ( (long) indices.length );
            final DimensionalMustHaveAtLeastNDimensions.Violation violation =
                contract.violation ( this, this );
            return new Error<VALUE> ( this.expectedClass (),
                                      violation );
        }

        return this;
    }


    /**
     * @see musaico.foundation.value.Value#blockingMaxNanoseconds()
     */
    @Override
    public final long blockingMaxNanoseconds ()
    {
        return 0L;
    }


    /**
     * @see musaico.foundation.value.Value#countable()
     */
    @Override
    public final SideEffects<VALUE> countable ()
        throws ReturnNeverNull.Violation
    {
        return this;
    }


    /**
     * @see musaico.foundation.value.Dimensional#dimensions()
     */
    @Override
    public final long dimensions ()
    {
        return 0L;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
                           Object obj
                           )
    {
        if ( obj == null
             || obj.getClass () != this.getClass () )
        {
            return false;
        }

        SideEffects<?> that = (SideEffects<?>) obj;
        if ( ! this.expectedClass ().equals ( that.expectedClass () ) )
        {
            return false;
        }
        if ( this.valueViolation == null )
        {
            if ( that.valueViolation != null )
            {
                return false;
            }
        }
        else if ( that.valueViolation == null )
        {
            return false;
        }
        else if ( ! this.valueViolation.equals ( that.valueViolation ) )
        {
            return false;
        }

        // Everything matches.
        return true;
    }


    /**
     * @see musaico.foundation.value.Value#expectedClass()
     */
    @Override
    public Class<VALUE> expectedClass ()
    {
        return this.expectedClass;
    }


    /**
     * @see musaico.foundation.value.Value#filter()
     */
    @Override
    public FilterState filter ()
    {
        return FilterState.DISCARDED;
    }


    /**
     * @see musaico.foundation.value.Dimensional#has(long[])
     */
    @Override
    public final boolean has (
                              long... indices
                              )
        throws ParametersMustNotBeNull.Violation,
               Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               indices );

        return false;
    }


    /**
     * @see musaico.foundation.value.Value#hasValue()
     */
    @Override
    public boolean hasValue ()
    {
        return false;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        int hash_code = 0;
        hash_code += this.getClass ().hashCode ();
        hash_code += ( this.expectedClass == null ? 0 : this.expectedClass.hashCode () );
        hash_code += ( this.valueViolation == null ? 0 : this.valueViolation.hashCode () );

        return hash_code;
    }


    /**
     * @see musaico.foundation.value.Value#head()
     */
    @Override
    public final No<VALUE> head ()
        throws ReturnNeverNull.Violation
    {
        return new No<VALUE> ( this.expectedClass,
                               this.valueViolation );
    }


    /**
     * @see musaico.foundation.value.Value#idempotent()
     */
    @Override
    public final SideEffects<VALUE> idempotent ()
        throws ReturnNeverNull.Violation
    {
        // If the main value is not Idempotent, return a
        // new SideEffects.
        if ( ! ( this.value instanceof Idempotent ) )
        {
            final Idempotent<VALUE> idempotent_value =
                this.value.idempotent ();
            if ( ! ( this.sideEffects instanceof Idempotent ) )
            {
                final Idempotent<SideEffect> idempotent_side_effects =
                    this.sideEffects.idempotent ();

                try
                {
                    return new SideEffects<VALUE> ( this.expectedClass,
                                                    idempotent_value,
                                                    idempotent_side_effects );
                }
                catch ( ValueMustNotHaveSideEffects.Violation cause )
                {
                    // Can't create a SideEffects because the
                    // side effects have SideEffects...!
                    final ReturnNeverNull.Violation violation =
                        ReturnNeverNull.CONTRACT.violation ( this,
                                                             idempotent_side_effects );
                    violation.initCause ( cause );

                    throw violation;
                }
            }
            else
            {
                try
                {
                    return new SideEffects<VALUE> ( this.expectedClass,
                                                    idempotent_value,
                                                    this.sideEffects );
                }
                catch ( ValueMustNotHaveSideEffects.Violation cause )
                {
                    // Can't create a SideEffects because the
                    // side effects have SideEffects...!
                    // Technically this should never happen, since
                    // we checked this.sideEffects during our
                    // own constructor.
                    final ReturnNeverNull.Violation violation =
                        ReturnNeverNull.CONTRACT.violation ( this,
                                                             this.sideEffects );
                    violation.initCause ( cause );

                    throw violation;
                }
            }
        }
        // If the side effect value is not Idempotent,
        // return a new SideEffects.
        else if ( ! ( this.sideEffects instanceof Idempotent ) )
        {
            final Idempotent<SideEffect> idempotent_side_effects =
                this.sideEffects.idempotent ();

            try
            {
                return new SideEffects<VALUE> ( this.expectedClass,
                                                this.value,
                                                idempotent_side_effects );
            }
            catch ( ValueMustNotHaveSideEffects.Violation cause )
            {
                // Can't create a SideEffects because the
                // side effects have SideEffects...!
                final ReturnNeverNull.Violation violation =
                    ReturnNeverNull.CONTRACT.violation ( this,
                                                         idempotent_side_effects );
                violation.initCause ( cause );

                throw violation;
            }
        }

        // The main value and the side effect value
        // are both Idempotent, so return this as-is.
        return this;
    }


    /**
     * @see musaico.foundation.value.Dimensional#indices(long[])
     */
    @Override
    public final long indices (
                               long... indices
                               )
        throws ParametersMustNotBeNull.Violation,
               Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               indices );

        return 0L;
    }


    /**
     * @see musaico.foundation.value.Value#iterator()
     */
    @Override
    public final ValueIterator<VALUE> iterator ()
    {
        return new ValueIterator<VALUE> ();
    }


    /**
     * @see musaico.foundation.value.Countable#length()
     */
    @Override
    public final long length ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        return 0L;
    }


    /**
     * @see musaico.foundation.value.Value#onBlocking(long)
     */
    @Override
    public SideEffects<VALUE> onBlocking (
                                          long timeout_in_nanoseconds
                                          )
    {
        // We aren't waiting for a result, we already know what we are.
        return this;
    }


    /**
     * @see musaico.foundation.value.Value#or(musaico.foundation.value.Value)
     *
     * <p>
     * Note: SideEffects follows the same rules as No value.
     * See the truth table for No for details.
     * </p>
     */
    @Override
    public final Value<VALUE> or (
                                  Value<VALUE> that
                                  )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               that );

        if ( that instanceof Error )
        {
            // SideEffects value this | Error value that = that.
            return that;
        }
        else if ( that instanceof Timeout )
        {
            // SideEffects value this | Timeout value that = that.
            return that;
        }
        else if ( that instanceof Blocking )
        {
            // SideEffects value this | Blocking value that = that.
            return that;
        }
        else if ( that instanceof Warning )
        {
            // SideEffects value this | Warning value that = that.
            return that;
        }
        else if ( that instanceof Partial )
        {
            // SideEffects value this | Partial value that = that.
            return that;
        }
        else if ( that instanceof Just )
        {
            // SideEffects value this | Just value that = that.
            return that;
        }
        else // Any other Unjust value, including No and SideEffects.
        {
            // SideEffects value this | Unjust value that = this.
            return this;
        }
    }


    /**
     * @see musaico.foundation.value.Value#orDefault(java.lang.Object)
     */
    @Override
    public VALUE orDefault (
                            VALUE default_value
                            )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               default_value );

        return default_value;
    }


    /**
     * @see musaico.foundation.value.Value#orNone()
     */
    @Override
    public VALUE orNone ()
    {
        return DefaultValuesRegistry.get ().noneGenerator ( this ).none ();
    }


    /**
     * @see musaico.foundation.value.Value#orNull()
     */
    @Override
    public VALUE orNull ()
    {
        return null;
    }


    /**
     * @see musaico.foundation.value.Value#orPartial()
     */
    @Override
    public SideEffects<VALUE> orPartial ()
    {
        return this;
    }


    /**
     * @see musaico.foundation.value.Value#orThrow(java.lang.Class)
     */
    @Override
    public <EXCEPTION extends Exception>
        VALUE orThrow (
                       Class<EXCEPTION> exception_class
                       )
        throws EXCEPTION,
               ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               exception_class );

        Exception cause = null;
        EXCEPTION exception = null;
        try
        {
            // First try single arg MyException ( String message ).
            final Constructor<EXCEPTION> constructor =
                exception_class.getConstructor ( String.class );
            exception =
                constructor.newInstance ( this.valueViolation ().getMessage () );
        }
        catch ( Exception e )
        {
            cause = e;
            try
            {
                // Now try 0 args constructor.
                final Constructor<EXCEPTION> constructor =
                    exception_class.getConstructor ();
                exception = constructor.newInstance ();
            }
            catch ( Exception e2 )
            {
                exception = null;
            }
        }

        if ( exception == null )
        {
            final ReturnNeverNull.Violation violation =
                ReturnNeverNull.CONTRACT.violation ( this,
                                                     "Could not instantiate exception class " + exception_class.getName () );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            throw violation;
        }

        exception.initCause ( this.valueViolation () );

        throw exception;
    }


    /**
     * @see musaico.foundation.value.Value#orThrow(java.lang.Exception)
     */
    @Override
    public <EXCEPTION extends Exception>
        VALUE orThrow (
                       EXCEPTION exception
                       )
        throws EXCEPTION,
               ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               exception );

        if ( exception.getCause () == null )
        {
            exception.initCause ( this.valueViolation () );
        }

        throw exception;
    }


    /**
     * @see musaico.foundation.value.Value#orThrowChecked()
     */
    @Override
    public VALUE orThrowChecked ()
        throws ValueViolation
    {
        throw this.valueViolation;
    }


    /**
     * @see musaico.foundation.value.Value#orThrowUnchecked()
     */
    @Override
    public VALUE orThrowUnchecked ()
        throws UncheckedValueViolation
    {
        throw new UncheckedValueViolation ( this.valueViolation );
    }


    /**
     * @see musaico.foundation.value.Value#orViolation(musaico.foundation.contract.Contract,java.lang.Object, java.lang.Object)
     */
    @Override
    public <INSPECTABLE extends Object, VIOLATION extends Throwable & Violation>
        VALUE orViolation (
                           Contract<INSPECTABLE, VIOLATION> contract,
                           Object plaintiff,
                           INSPECTABLE inspectable_data
                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation,
               VIOLATION
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               contract, plaintiff, inspectable_data );

        final VIOLATION violation =
            contract.violation ( plaintiff,
                                 inspectable_data );
        violation.initCause ( this.valueViolation );
        throw violation;
    }


    /**
     * @see musaico.foundation.value.Value#pipe(musaico.foundation.value.ValueProcessor)
     */
    @Override
    public Value<VALUE> pipe (
                              ValueProcessor<VALUE> value_processor
                              )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               value_processor );

        Value<VALUE> processed_result =
            value_processor.process ( this );

        this.contracts.check ( ReturnNeverNull.CONTRACT,
                               processed_result );

        return processed_result;
    }


    /**
     * @return The side-effects, excluding the main value.  Never null.
     */
    public Value<SideEffect> sideEffects ()
        throws ReturnNeverNull.Violation
    {
        return this.sideEffects;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        if ( this.contracts == null ) // Constructor time only.
        {
            return "SideEffects";
        }

        return "SideEffects<" + ClassName.of ( this.expectedClass () ) + "> : "
            + this.valueViolation ();
    }


    /**
     * @return The main value, excluding the SideEffects.  Never null.
     */
    public Value<VALUE> value ()
        throws ReturnNeverNull.Violation
    {
        return this.value;
    }


    /**
     * @see musaico.foundation.value.NotOne#valueViolation()
     */
    @Override
    public ValueViolation valueViolation ()
        throws ReturnNeverNull.Violation
    {
        this.contracts.check ( ReturnNeverNull.CONTRACT,
                               this.valueViolation );

        return this.valueViolation;
    }
}
