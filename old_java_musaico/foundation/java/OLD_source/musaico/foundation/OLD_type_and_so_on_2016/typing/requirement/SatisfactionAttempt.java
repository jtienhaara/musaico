package musaico.foundation.typing.requirement;


import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.typing.Operation;


/**
 * <p>
 * !!!
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
 * @see musaico.foundation.typing.requirement.MODULE#COPYRIGHT
 * @see musaico.foundation.typing.requirement.MODULE#LICENSE
 */
public class SatisfactionAttempt<KEY extends Object>
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    private final Operation<?> operation;
    private final KEY requiredValue;

    public SatisfactionAttempt (
                                Operation<?> operation,
                                KEY required_value
                                )
        throws ParametersMustNotBeNull.Violation
    {
        this.operation = operation;
        this.requiredValue = required_value;
    }

    public final Operation<?> operation ()
    {
        return this.operation;
    }

    public final KEY requiredValue ()
    {
        return this.requiredValue;
    }
}
