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
 * A SymbolID which might or might not be registered in a specific
 * container's SymbolTable.  The container's SymbolTable is checked
 * dynamically, whenever asked.
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
public class Registration
    extends AbstractRegistration
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;

    // Checks contracts on static methods and constructors for us.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( Registration.class );

    // The container of the SymbolTable to check against.
    private final Namespace namespace;

    /**
     * <p>
     * Creates a new Registration to check against the specified
     * container of a SymbolTable.
     * </p>
     *
     * @param id The unique identifier for the Symbol that might or
     *           might not be registered in the namespace.
     *           Must not be null.
     *
     * @param namespace The Namespace to check for, whenever asked.
     *                  Must not be null.
     */
    public Registration (
                         SymbolID<?> id,
                         Namespace namespace
                         )
        throws ParametersMustNotBeNull.Violation
    {
        super ( id );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               namespace );

        this.namespace = namespace;
    }


    /**
     * @see musaico.foundation.typing.AbstractRegistration#isRegistered()
     */
    @Override
    public boolean isRegistered ()
    {
        return this.namespace.containsSymbol ( this.id () );
    }


    /**
     * @return The Namespace being checked.  Never null.
     */
    public final Namespace namespace ()
    {
        return this.namespace;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return super.toString ()
            + " in Namespace "
            + this.namespace;
    }
}
