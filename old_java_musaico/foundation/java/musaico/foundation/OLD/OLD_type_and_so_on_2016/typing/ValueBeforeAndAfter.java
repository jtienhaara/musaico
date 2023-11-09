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
 * A tuple containing "before" and "after" states of some value
 * (for example the input and output of an Operation).
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
public class ValueBeforeAndAfter
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks contracts on static methods and constructors for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( ValueBeforeAndAfter.class );

    /** The "before" value. */
    private final Value<?> before;

    /** The "after" value. */
    private final Value<?> after;

    /**
     * <p>
     * Creates a new ValueBeforeAndAfter with the specified "before"
     * and "after" values.
     * </p>
     *
     * @param before The value before something happened
     *               (such as the input to an Operation).
     *               Must not be null.
     *
     * @param after The value after something happened
     *              (such as the output from an Operation).
     *              Must not be null.
     */
    public ValueBeforeAndAfter (
                                Value<?> before,
                                Value<?> after
                                )
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               before, after );

        this.before = before;
        this.after = after;
    }

    /**
     * @return The "before" value.  Never null.
     */
    public Value<?> before ()
    {
        return this.before;
    }

    /**
     * @return The "after" value.  Never null.
     */
    public Value<?> after ()
    {
        return this.after;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
        public String toString ()
    {
        return "" + ClassName.of ( this.getClass () )
            + " before = "
            + this.before
            + " after = "
            + this.after;
    }
}
