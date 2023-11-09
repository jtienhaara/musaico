package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.CheckedViolation;
import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;
import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.Parameter7;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

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
 * A tuple containing a cast Operation and either the source someone tried
 * to pass to it, or the target it tried to return.
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
public class CastValue
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks contracts on static methods and constructors for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( CastValue.class );

    /** The cast Operation. */
    private final Operation1<?, ?> cast;

    /** The source of the cast / target created by the cast. */
    private final Value<?> value;

    /** True if the value is source, false if it is the target. */
    private final boolean isSource;

    /**
     * <p>
     * Creates a new CastValue with the specified source/target
     * to/from the specified cast Operation.
     * </p>
     *
     * @param cast The cast.  Must not be null.
     *
     * @param value The source/target to/from the cast.
     *              Must not be null.
     *
     * @param is_source True if the value is source, false if it is target.
     */
    public CastValue (
                      Operation1<?, ?> cast,
                      Value<?> value,
                      boolean is_source
                      )
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               cast, value );

        this.cast = cast;
        this.value = value;
        this.isSource = is_source;
    }

    /**
     * @return The cast Operation.  Never null.
     */
    public Operation1<?, ?> cast ()
    {
        return this.cast;
    }

    /**
     * @return The source Value to / target Value from the cast Operation.
     *         Never null.
     */
    public Value<?> value ()
    {
        return this.value;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
        public String toString ()
    {
        return "" + ClassName.of ( this.getClass () )
            + " "
            + this.cast.id ().name ()
            + ( this.isSource == true ? " from " : " to " )
            + this.value;
    }
}
