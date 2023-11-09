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
 * An abstract value containing a SymbolID which either is or isn't
 * registered in a SymbolTable.  Concrete implementations are:
 * Registered (which has the registered value), Unregistered
 * (which is just the SymbolID), Registration
 * (which has the SymbolID and the container of the SymbolTable
 * to check against dynamically, whenever asked), CastAttempt
 * (a registered or unregistered cast Operation from a specific type to
 * a specific type with a specific input).
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
public abstract class AbstractRegistration
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks contracts on static methods and constructors for us.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( AbstractRegistration.class );

    // The SymbolID which might or might not be registered in the
    // SymbolTable being checked against.
    private final SymbolID<?> id;

    /**
     * <p>
     * Creates a new AbstractRegistration for the specified SymbolID.
     * </p>
     *
     * @param id The unique identifier for the Symbol that might or
     *           might not be registered in a specific SymbolTable.
     *           Must not be null.
     */
    public AbstractRegistration (
                                 SymbolID<?> id
                                 )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               id );

        this.id = id;
    }

    /**
     * @return The identifier of the Symbol, unique within each
     *         SymbolTable, which might or might not be registered.
     *         Never null.
     */
    public final SymbolID<?> id ()
    {
        return this.id;
    }

    /**
     * @return True if the SymbolID is registered in the SymbolTable
     *         being checked against, false if not.
     */
    public abstract boolean isRegistered ();


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "" + ClassName.of ( this.getClass () )
            + " "
            + this.id;
    }
}
