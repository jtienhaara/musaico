package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.CheckedViolation;
import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;
import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.Parameter7;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

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
 * A SymbolID which is not registered in the SymbolTable being checked.
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
/**
 * <p>
 * </p>
 */
public class Unregistered
    extends AbstractRegistration
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new Unregistered Symbol.
     * </p>
     *
     * @param id The unique identifier for the Symbol that might or
     *           might not be registered in a specific SymbolTable.
     *           Must not be null.
     */
    public Unregistered (
                         SymbolID<?> id
                         )
        throws ParametersMustNotBeNull.Violation
    {
        super ( id );
    }

    /**
     * <p>
     * Creates a new Unregistered Symbol.
     * </p>
     *
     * @param symbol The actual Symbol which is not registered.
     *               Must not be null.
     */
    public Unregistered (
                         Symbol symbol
                         )
        throws ParametersMustNotBeNull.Violation
    {
        super ( symbol.id () );
    }


    /**
     * @see musaico.foundation.typing.AbstractRegistration#isRegistered()
     */
    @Override
    public boolean isRegistered ()
    {
        return false;
    }
}
