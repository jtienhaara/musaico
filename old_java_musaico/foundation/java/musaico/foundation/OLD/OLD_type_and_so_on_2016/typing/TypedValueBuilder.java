package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;
import musaico.foundation.contract.Violation;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;
import musaico.foundation.contract.obligations.Parameter2;

import musaico.foundation.domains.ClassName;

import musaico.foundation.value.Error;
import musaico.foundation.value.IdempotentAndCountable;
import musaico.foundation.value.No;
import musaico.foundation.value.NonBlocking;
import musaico.foundation.value.ValueBuilder;
import musaico.foundation.value.ZeroOrOne;


/**
 * <p>
 * Builds up a Value for a specific Type, one element at a time.
 * Can create a No value (zero elements), a One (one element), or a
 * Many (many elements).
 * </p>
 *
 * <p>
 * The <code> build () </code> method will fail if the Value being
 * built does not abide by the constraints made by the Type.  In this
 * case, No value will be returned, and the constraint violation
 * will be encapsulated in the No result.
 * </p>
 *
 * <p>
 * This TypedValueBuilder is NOT thread-safe.  Do not use it from multiple
 * threads.
 * </p>
 *
 *
 * <p>
 * In Java every ValueBuilder must be Serializable in order to play
 * nicely over RMI.  However be aware that the objects stored inside
 * a ValueBuilder need not be Serializable.  If non-Serializable
 * objects are used in an RMI environment, be prepared for trouble.
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
public class TypedValueBuilder<VALUE extends Object>
    extends ValueBuilder<VALUE>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks constructor and static method obligations. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( TypedValueBuilder.class );


    /** The Type of Value being built. */
    private final Type<VALUE> type;


    /**
     * <p>
     * Creates a new TypedValueBuilder for the specified Type.
     * </p>
     *
     * @param type The Type of Value being built.  Must not be null.
     */
    public TypedValueBuilder (
                             Type<VALUE> type
                             )
        throws ParametersMustNotBeNull.Violation
    {
        super ( type == null
                    ? null
                    : type.valueClass () );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               type );

        this.type = type;
    }


    /**
     * <p>
     * Creates a new TypedValueBuilder for the specified Type
     * and adds the specified Iterable elements.
     * </p>
     *
     * @param type The Type of Value being built.  Must not be null.
     *
     * @param elements The elements to add to this ValueBuilder.
     *                 Must not be null.  Must not contain any null elements.
     */
    @SuppressWarnings("unchecked") // Cast Object - Iterable b/c MustContainNoNulls accepts Object input
    public TypedValueBuilder (
                             Type<VALUE> type,
                             Iterable<VALUE> elements
                             )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation
    {
        super ( type == null
                    ? null
                    : type.valueClass (),
                classContracts.check ( Parameter2.MustContainNoNulls.CONTRACT,
                                       elements ) );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               type );

        this.type = type;
    }


    /**
     * <p>
     * Creates a new TypedValueBuilder for the specified Type
     * and adds the specified array of elements.
     * </p>
     *
     * @param type The Type of Value being built.  Must not be null.
     *
     * @param elements The elements to add to this ValueBuilder.
     *                 Must not be null.  Must not contain any null elements.
     */
    @SuppressWarnings("unchecked") // Cast Object - array b/c MustContainNoNulls accepts Object input
    public TypedValueBuilder (
                             Type<VALUE> type,
                             VALUE [] elements
                             )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation
    {
        super ( type == null
                    ? null
                    : type.valueClass (),
                classContracts.check ( Parameter2.MustContainNoNulls.CONTRACT,
                                       elements ) );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               type );

        this.type = type;
    }


    /**
     * <p>
     * Adds the specified Constant to the value being built.
     * </p>
     *
     * <p>
     * NOT thread-safe.
     * </p>
     *
     * @param constant The Constant whose values will be added.
     *                 Must not be null.
     *
     * @return This ValueBuilder.  Never null.
     */
    public ValueBuilder<VALUE> add (
                                    Constant<VALUE> constant
                                    )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  constant );

        final NonBlocking<VALUE> value = constant.value ().await ();
        return this.addAll ( value );
    }


    /**
     * @see musaico.foundation.value.ValueBuilder#build()
     */
    @Override
    public IdempotentAndCountable<VALUE> build ()
    {
        final IdempotentAndCountable<VALUE> maybe_value = super.build ();
        try
        {
            this.type.checkValue ( maybe_value );
        }
        catch ( TypingViolation violation )
        {
            return this.type.errorValue ( violation );
        }

        return maybe_value;
    }


    /**
     * @see musaico.foundation.value.ValueBuilder#buildViolation(java.lang.Throwable)
     */
    @Override
    public <VIOLATION extends Throwable & Violation>
        Error<VALUE> buildViolation (
                                     VIOLATION violation
                                     )
            throws ReturnNeverNull.Violation
    {
        final Error<VALUE> maybe_value = super.buildViolation ( violation );
        try
        {
            this.type.checkValue ( maybe_value );
        }
        catch ( TypingViolation violation2 )
        {
            Throwable root_cause = violation2;
            int loop_protector = 0;
            while ( root_cause.getCause () != null
                    && loop_protector < 16 )
            {
                root_cause = root_cause.getCause ();
                loop_protector ++;
            }

            root_cause.initCause ( violation );

            return this.type.errorValue ( violation2 );
        }

        return maybe_value;
    }


    /**
     * @see musaico.foundation.value.ValueBuilder#buildZeroOrOne()
     */
    @Override
    public ZeroOrOne<VALUE> buildZeroOrOne ()
    {
        final ZeroOrOne<VALUE> maybe_value = super.buildZeroOrOne ();
        try
        {
            this.type.checkValue ( maybe_value );
        }
        catch ( TypingViolation violation )
        {
            return this.type.noValue ( violation );
        }

        return maybe_value;
    }


    /**
     * @return The Type of Values built by this TypedValueBuilder.
     *         Never null.
     */
    public Type<VALUE> type ()
    {
        return this.type;
    }


    /**
     * @see musaico.foundation.value.ValueBuilder#valueBuilderName()
     */
    @Override
    protected String valueBuilderName ()
    {
        if ( this.type == null )
        {
            // During ValueBuilder constructor, toString () is called
            // in order to create a nice name for the object
            // under ObjectContracts.  Give it something nice.
            return ClassName.of ( this.getClass () );
        }

        return "TypedValueBuilder " + this.type.id ().name ();
    }
}
