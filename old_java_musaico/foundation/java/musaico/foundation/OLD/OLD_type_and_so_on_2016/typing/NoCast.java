package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;
import musaico.foundation.contract.Violation;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.value.Value;


/**
 * <p>
 * Always fails to cast objects of one Type to another Type.
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
public class NoCast<SOURCE extends Object, TARGET extends Object>
    extends StandardOperation1<SOURCE, TARGET>
    implements OperationBody1<SOURCE, TARGET>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( NoCast.class );


    /** The violation which led to this NoCast. */
    private final TypingViolation violation;


    /**
     * <p>
     * Creates a new NoCast which always fails to cast from the specified
     * source type of objects to the specified target
     * type of objects, because no cast Operation has been registered for the
     * specified from and to Types.
     * </p>
     *
     * @param source_type The source type of objects to
     *                    typecast from whenever this NoCast is evaluated.
     *                    Must not be null.
     *
     * @param target_type The target type of objects to
     *                    typecast to whenever this NoCast is evaluated.
     *                    Must not be null.
     */
    public NoCast (
                   Type<SOURCE> source_type,
                   Type<TARGET> target_type
                   )
        throws ParametersMustNotBeNull.Violation
    {
        super ( new OperationID<Operation1<SOURCE, TARGET>, TARGET> ( "nocast",
                    new OperationType1<SOURCE, TARGET> ( source_type,
                                                         target_type ) ),
                null ); // body = this.

        final AbstractRegistration unregistered =
            new Unregistered ( this.id () );
        this.violation = SymbolMustBeRegistered.CONTRACT
            .violation ( Operation.class, // Object under contract.
                         unregistered ); // Inspectable data.
    }


    /**
     * <p>
     * Creates a new NoCast which always fails to cast from the specified
     * source type of objects to the specified target
     * type of objects.
     * </p>
     *
     * @param source_type The source type of objects to
     *                    typecast from whenever this NoCast is evaluated.
     *                    Must not be null.
     *
     * @param target_type The target type of objects to
     *                    typecast to whenever this NoCast is evaluated.
     *                    Must not be null.
     *
     * @param violation The Violation which led to this NoCast.
     *                  Any non-TypingViolation will be wrapped in a
     *                  newly created TypingViolation.
     *                  Must not be null.
     */
    @SuppressWarnings("unchecked") // instanceof then cast -> not checked?!?
    public <VIOLATION extends Throwable & Violation>
        NoCast (
                Type<SOURCE> source_type,
                Type<TARGET> target_type,
                VIOLATION violation
                )
        throws ParametersMustNotBeNull.Violation
    {
        this ( "none", source_type, target_type, violation );
    }


    /**
     * <p>
     * Creates a new NoCast which always fails to cast from the specified
     * source type of objects to the specified target
     * type of objects.
     * </p>
     *
     * @param name The name of the NoCast, which will be used to create
     *             an OperationID unique within each SymbolTable.
     *             Must not be null.
     *
     * @param source_type The source type of objects to
     *                    typecast from whenever this NoCast is evaluated.
     *                    Must not be null.
     *
     * @param target_type The target type of objects to
     *                    typecast to whenever this NoCast is evaluated.
     *                    Must not be null.
     *
     * @param violation The Violation which led to this NoCast.
     *                  Any non-TypingViolation will be wrapped in a
     *                  newly created TypingViolation.
     *                  Must not be null.
     */
    @SuppressWarnings("unchecked") // instanceof then cast -> not checked?!?
    public <VIOLATION extends Throwable & Violation>
        NoCast (
                String name,
                Type<SOURCE> source_type,
                Type<TARGET> target_type,
                VIOLATION violation
                )
        throws ParametersMustNotBeNull.Violation
    {
        super ( new OperationID<Operation1<SOURCE, TARGET>, TARGET> ( name,
                    new OperationType1<SOURCE, TARGET> ( source_type,
                                                         target_type ) ),
                null ); // body = this.

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               violation );

        if ( violation instanceof TypingViolation )
        {
            this.violation = (TypingViolation) violation;
        }
        else
        {
            this.violation = new TypingViolation ( violation );
        }
    }


    /**
     * @see musaico.foundation.typing.OperationBody1#evaluateBody(musaico.foundation.value.Value)
     */
    @Override
    public Value<TARGET> evaluateBody (
                                       Value<SOURCE> source_value
                                       )
    {
        // Failed CastAttempt.
        final CastAttempt<SOURCE, TARGET> cast_attempt =
            new CastAttempt<SOURCE, TARGET> ( this.input1Type (),
                                              source_value,
                                              this.outputType () );
        final TypingViolation no_type_caster =
            SymbolMustBeRegistered.CONTRACT.violation (
                                  Namespace.NONE,
                                  cast_attempt );
        final Value<TARGET> failed_cast =
            this.outputType ().noValue ( no_type_caster );

        return failed_cast;
    }


    /**
     * @see musaico.foundation.typing.Symbol#rename(java.lang.String)
     */
    @Override
    public NoCast<SOURCE, TARGET> rename (
                                          String name
                                          )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  name );

        return new NoCast<SOURCE, TARGET> ( name,
                                            this.input1Type (),
                                            this.outputType (),
                                            this.violation );
    }


    /**
     * @see musaico.foundation.typing.Retypable#retype(java.lang.String, musaico.foundation.typing.Type)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast Type<?> - Type<SOURCE>.
    public NoCast<SOURCE, TARGET> retype (
                                          String name,
                                          OperationType<? extends Operation<TARGET>, TARGET> type
                                          )
        throws ParametersMustNotBeNull.Violation,
               TypesMustHaveSameValueClasses.Violation,
               ReturnNeverNull.Violation
    {
        this.checkRetype ( name, type );

        return new NoCast<SOURCE, TARGET> ( name,
                                            (Type<SOURCE>) type.inputType ( 0 ),
                                            type.outputType (),
                                            this.violation );
    }


    /**
     * @return The TypingViolation which led to this NoCast.  Never null.
     */
    public TypingViolation violation ()
        throws ReturnNeverNull.Violation
    {
        return this.violation;
    }
}
