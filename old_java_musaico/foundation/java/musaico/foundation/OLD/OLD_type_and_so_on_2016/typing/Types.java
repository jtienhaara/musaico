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
 * A tuple containing two Types, typically used to ensure the
 * output Type of an Operation matches the input Type of the next
 * Operation in a Pipe, and so on.
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
public class Types
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks contracts on static methods and constructors for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( Types.class );


    /** The first Operation whose Type must match. */
    private final Serializable operation1;

    /** The first Type which must match the second Type. */
    private final Type<?> type1;

    /** The second Operation whose Type must match. */
    private final Serializable operation2;

    /** The second Type which must match the second Type. */
    private final Type<?> type2;

    /**
     * <p>
     * Creates a new Types pair.
     * </p>
     *
     * @param operation1 The first Operation whose
     *                   Type must match the second object's Type.
     *                   Must not be null.
     *
     * @param type1 The first Type which must match the second Type.
     *              Must not be null.
     *
     * @param operation2 The second Operation whose
     *                   Type must match the first object's Type.
     *                   Must not be null.
     *
     * @param type2 The second Type which must match the first Type.
     *              Must not be null.
     */
    public Types (
                  Operation<?> operation1,
                  Type<?> type1,
                  Serializable operation2,
                  Type<?> type2
                  )
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               operation1, type1,
                               operation2, type2 );

        this.operation1 = operation1;
        this.type1 = type1;

        this.operation2 = operation2;
        this.type2 = type2;
    }

    /**
     * @return The first Operation.
     *         Never null.
     */
    public Serializable operation1 ()
    {
        return this.operation1;
    }

    /**
     * @return The first type.  Never null.
     */
    public Type<?> type1 ()
    {
        return this.type1;
    }

    /**
     * @return The second Operation.
     *         Never null.
     */
    public Serializable operation2 ()
    {
        return this.operation2;
    }

    /**
     * @return The second type.  Never null.
     */
    public Type<?> type2 ()
    {
        return this.type2;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
        public String toString ()
    {
        return "" + ClassName.of ( this.getClass () )
            + " "
            + this.type1
            + " [ "
            + this.operation1
            + " ] vs. "
            + this.type2
            + " [ "
            + this.operation2
            + " ]";
    }
}
