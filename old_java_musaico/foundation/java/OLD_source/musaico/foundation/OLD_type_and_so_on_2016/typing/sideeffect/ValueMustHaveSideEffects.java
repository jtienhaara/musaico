package musaico.foundation.typing.sideeffect;

import java.io.Serializable;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;
import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.typing.TypingViolation;

import musaico.foundation.value.Value;


/**
 * <p>
 * A guarantee of a value with SideEffects.
 * </p>
 *
 *
 * <p>
 * In Java, every Contract must implement equals () and hashCode ().
 * </p>
 *
 * <p>
 * In Java, every Contract must be Serializable in order to play
 * nicely over RMI.
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
 * @see musaico.foundation.typing.sideeffect.MODULE#COPYRIGHT
 * @see musaico.foundation.typing.sideeffect.MODULE#LICENSE
 */
public class ValueMustHaveSideEffects
    implements Contract<Value<?>, ValueMustHaveSideEffects.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The ValueMustHaveSideEffects contract. */
    public static final ValueMustHaveSideEffects CONTRACT =
        new ValueMustHaveSideEffects ();


    public static class Violation
        extends TypingViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            ValueMustHaveSideEffects.serialVersionUID;


        /**
         * <p>
         * Creates a new ValueMustHaveSideEffects.Violation with the specified
         * details.
         * </p>
         *
         * @param contract The violated contract.  Must not be null.
         *
         * @param plaintiff The object whose contract was
         *                              violated.  Must not be null.
         *
         * @param inspectable_data The data which violated the contract.
         *                         Must not be null.
         */
        public Violation (
                          Contract<Value<?>, ValueMustHaveSideEffects.Violation> contract,
                          Object plaintiff,
                          Value<?> inspectable_data
                          )
        {
            super ( contract,
                    Contracts.makeSerializable ( plaintiff ),
                    inspectable_data );
        }
    }




    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
                           Object obj
                           )
    {
        if ( obj == null )
        {
            return false;
        }
        else if ( obj.getClass () != this.getClass () )
        {
            return false;
        }

        return true;
    }


    /**
     * @see musaico.filter.Filter#filter(java.lang.Object)
     */
    @Override
    public FilterState filter (
                               Value<?> value
                               )
    {
        if ( value == null )
        {
            return FilterState.DISCARDED;
        }
        else if ( value instanceof SideEffects )
        {
            return FilterState.KEPT;
        }
        else
        {
            return FilterState.DISCARDED;
        }
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return 1;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this.getClass () );
    }


    /**
     * @see musaico.contract.Contract#violation(java.lang.Object, java.lang.Object)
     */
    @Override
    public ValueMustHaveSideEffects.Violation violation (
        Object plaintiff,
        Value<?> inspectable_data
        )
    {
        return new ValueMustHaveSideEffects.Violation ( this,
                                                        plaintiff,
                                                        inspectable_data );
    }
}
