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
 * A SymbolID which is registered in the SymbolTable being checked.
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
public class Registered
    extends AbstractRegistration
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks contracts on static methods and constructors for us.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( Registered.class );

    // The Symbol which is registered in the SymbolTable being checked.
    private final Symbol symbol;

    /**
     * <p>
     * Creates a new Registered Symbol.
     * </p>
     *
     * @param symbol The Symbol which is registered in the SymbolTable
     *               being checked.  Must not be null.
     */
    public Registered (
                       Symbol symbol
                       )
        throws ParametersMustNotBeNull.Violation
    {
        super ( symbol == null ? null
                               : symbol.id () );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               symbol );

        this.symbol = symbol;
    }


    /**
     * @see musaico.foundation.typing.AbstractRegistration#isRegistered()
     */
    @Override
    public boolean isRegistered ()
    {
        return true;
    }


    /**
     * @return The Symbol which is registered in the SymbolTable
     *         being checked.  Never null.
     */
    public final Symbol symbol ()
    {
        return this.symbol;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return super.toString ()
            + ": "
            + this.symbol;
    }
}
