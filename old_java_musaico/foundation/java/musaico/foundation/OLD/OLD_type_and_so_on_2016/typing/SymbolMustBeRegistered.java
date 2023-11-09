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
public class SymbolMustBeRegistered
    extends AbstractTypingContract<AbstractRegistration>
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    public static final SymbolMustBeRegistered CONTRACT =
        new SymbolMustBeRegistered ();

    /**
     * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
     */
    @Override
    public FilterState filter (
                               AbstractRegistration registration
                               )
    {
        if ( registration == null )
        {
            // Can't do much with this.
            return FilterState.DISCARDED;
        }
        else if ( registration.isRegistered () )
        {
            // Symbol is registered.
            return FilterState.KEPT;
        }
        else
        {
            // Symbol not registered.
            return FilterState.DISCARDED;
        }
    }
}
