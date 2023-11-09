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
public abstract class AbstractTypingContract<INSPECTABLE extends Object>
    implements Contract<INSPECTABLE, TypingViolation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Convenience method.  Creates a violation then sets the root
     * cause of the newly created violation.
     * </p>
     *
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     */
    public TypingViolation violation (
                                      Object plaintiff,
                                      INSPECTABLE inspectable_data,
                                      Throwable cause
                                      )
    {
        final TypingViolation violation =
            this.violation ( plaintiff,
                             inspectable_data );

        violation.initCause ( cause );

        return violation;
    }

    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     */
    @Override
    public TypingViolation violation (
                                      Object plaintiff,
                                      INSPECTABLE inspectable_data
                                      )
    {
        return new TypingViolation (
                   this,
                   Contracts.makeSerializable ( plaintiff ),
                   Contracts.makeSerializable ( inspectable_data ) );
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "" + ClassName.of ( this.getClass () );
    }
}
