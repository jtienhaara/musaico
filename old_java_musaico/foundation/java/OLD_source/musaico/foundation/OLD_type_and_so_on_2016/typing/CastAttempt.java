package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.CheckedViolation;
import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;
import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.Parameter7;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;
import musaico.foundation.domains.InstanceOfClass;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.value.Finite;
import musaico.foundation.value.Just;
import musaico.foundation.value.One;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueBuilder;
import musaico.foundation.value.ValueViolation;


/**
 * <p>
 * An attempted cast from a specific object of some specific type
 * to another type.
 * </p>
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
public class CastAttempt<FROM extends Object, TO extends Object>
    extends AbstractRegistration
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;

    // Checks contracts on static methods and constructors for us.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( CastAttempt.class );

    // The Type to cast from.
    private final Type<FROM> inputType;

    // The source data to be cast.
    private final Serializable sourceObject;

    // The Type to cast to.
    private final Type<TO> outputType;

    // The registration of the cast Operation.  Registered, Unregistered
    // or an as yet unknown Registration.
    private final AbstractRegistration registration;


    /**
     * <p>
     * Creates a new CastAttempt which fails because there is no cast
     * Operation from the specified type of the specified object to the
     * specified target type, while attempting to cast the
     * specified source object.
     * </p>
     *
     * @param source_type The type of the source object.
     *                    Must not be null.
     *
     * @param source_object The object to be cast.  Must not be null.
     *
     * @param target_type The type to cast to.  Must not be null.
     */
    public CastAttempt (
                        Type<FROM> source_type,
                        Object source_object,
                        Type<TO> target_type
                        )
    {
        super ( new OperationID<Operation1<FROM, TO>, TO> ( Operation.CAST,
                    new OperationType1<FROM, TO> ( source_type,
                                                   target_type ) ) );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               source_type,
                               source_object,
                               target_type );

        this.inputType = source_type;
        this.sourceObject = Contracts.makeSerializable ( source_object );
        this.outputType = target_type;

        this.registration = new Unregistered ( this.id () );
    }


    /**
     * <p>
     * Creates a new CastAttempt for a cast Operation which is registered,
     * from the specified type of the specified object to the
     * specified target type, while attempting to cast the
     * specified source object.
     * </p>
     *
     * @param source_type The type of the source object.
     *                    Must not be null.
     *
     * @param source_object The object to be cast.  Must not be null.
     *
     * @param target_type The type to cast to.  Must not be null.
     *
     * @param cast The cast Operation which is registered in the
     *             SymbolTable being checked.  Must not be null.
     */
    public CastAttempt (
                        Type<FROM> source_type,
                        Object source_object,
                        Type<TO> target_type,
                        Operation1<FROM, TO> cast
                        )
    {
        super ( new OperationID<Operation1<FROM, TO>, TO> ( Operation.CAST,
                    new OperationType1<FROM, TO> ( source_type,
                                                   target_type ) ) );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               source_type,
                               source_object,
                               target_type,
                               cast );

        this.inputType = source_type;
        this.sourceObject = Contracts.makeSerializable ( source_object );
        this.outputType = target_type;

        this.registration = new Registered ( cast );
    }


    /**
     * <p>
     * Creates a new CastAttempt for a cast Operation which might or might
     * not be registered in the specified Namespace's SymbolTable,
     * from the specified type of the specified object to the
     * specified target type, while attempting to cast the
     * specified source object.
     * </p>
     *
     * @param source_type The type of the source object.
     *                    Must not be null.
     *
     * @param source_object The object to be cast.  Must not be null.
     *
     * @param target_type The type to cast to.  Must not be null.
     *
     * @param namespace The Namespace to check for, whenever asked.
     *                  Must not be null.
     */
    public CastAttempt (
                        Type<FROM> source_type,
                        Object source_object,
                        Type<TO> target_type,
                        Namespace namespace
                        )
    {
        super ( new OperationID<Operation1<FROM, TO>, TO> ( Operation.CAST,
                    new OperationType1<FROM, TO> ( source_type,
                                                   target_type ) ) );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               source_type,
                               source_object,
                               target_type,
                               namespace );

        this.inputType = source_type;
        this.sourceObject = Contracts.makeSerializable ( source_object );
        this.outputType = target_type;

        this.registration = new Registration ( this.id (), namespace );
    }

    /**
     * @return The type to cast from.  Never null.
     */
    public Type<FROM> inputType ()
    {
        return this.inputType;
    }

    /**
     * @return A serializable representation of the object to
     *         cast from.  Never null.
     */
    public Serializable sourceObject ()
    {
        return this.sourceObject;
    }

    /**
     * @return The target type of the cast.  Never null.
     */
    public Type<TO> outputType ()
    {
        return this.outputType;
    }


    /**
     * @see musaico.foundation.typing.AbstractRegistration#isRegistered()
     */
    @Override
    public boolean isRegistered ()
    {
        return this.registration.isRegistered ();
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "" + ClassName.of ( this.getClass () )
            + " from "
            + this.inputType.id ().name ()
            + " "
            + this.sourceObject
            + " "
            + " to "
            + this.outputType.id ().name ()
            + " (registration: "
            + this.registration
            + ")";
    }
}
