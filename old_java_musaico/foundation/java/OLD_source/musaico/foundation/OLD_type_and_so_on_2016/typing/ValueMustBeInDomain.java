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
 *  oerated upon.  For example casting 1.23 to an integer might
 *  fail with a violation of this contract, where the domain
 *  is all numbers between Integer.MIN_VALUE and Integer.MAX_VALUE
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
public class ValueMustBeInDomain<VALUE extends Object>
    extends AbstractTypingContract<VALUE>
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks contracts on static methods and constructors for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( ValueMustBeInDomain.class );

    /** The domain to which all values must belong. */
    private final Filter<VALUE> domain;

    /**
     * <p>
     * Creates a new ValueMustBeIndomain obligation for objects
     * which must be in the specified domain.
     * </p>
     *
     *
     * @param domain The domain to which all values must belong.
     *               Every value must be matched by the domain's
     *               <code> filter () </code> method (KEPT).
     *               Must not be null.
     */
    public ValueMustBeInDomain (
                                Filter<VALUE> domain
                                )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               domain );

        this.domain = domain;
    }

    /**
     * @return The domain to which all values must belong.  Never null.
     */
    public Filter<VALUE> domain ()
    {
        return this.domain;
    }

    /**
     * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
     */
    @Override
    public FilterState filter (
                               VALUE value
                               )
    {
        return this.domain.filter ( value );
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this.getClass () )
                              + " { "
                              + this.domain
                              + " }";
    }
}
