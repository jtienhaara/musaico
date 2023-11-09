package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.value.No;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueBuilder;


/**
 * <p>
 * Always builds up a No value, no matter what elements are added.
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
public class NoTypedValueBuilder<VALUE extends Object>
    extends TypedValueBuilder<VALUE>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks constructor and static method obligations. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( NoTypedValueBuilder.class );


    /** The typing violation which led to this NoTypedValueBuilder. */
    private final TypingViolation violation;


    /**
     * <p>
     * Creates a new TypedValueBuilder for the specified Type.
     * </p>
     *
     * @param type The Type of No value being built.  Must not be null.
     */
    public NoTypedValueBuilder (
                                Type<VALUE> type,
                                TypingViolation violation
                                )
        throws ParametersMustNotBeNull.Violation
    {
        super ( type );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               type, violation );

        this.violation = violation;
    }


    /**
     * @see musaico.foundation.value.ValueBuilder#build()
     */
    @Override
    public No<VALUE> build ()
    {
        return this.type ().noValue ( this.violation );
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "NoTypedValueBuilder " + this.type ();
    }
}
