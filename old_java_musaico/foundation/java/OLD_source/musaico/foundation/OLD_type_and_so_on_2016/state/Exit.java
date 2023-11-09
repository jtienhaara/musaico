package musaico.foundation.state;

import java.io.Serializable;


import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.value.classes.StandardValueClass;

import musaico.foundation.value.finite.One;


/**
 * <p>
 * The exit state from a state graph.
 * </p>
 *
 *
 * <p>
 * In Java every conditional Value must be Serializable in order to
 * play nicely across RMI.  However users of the conditional Value
 * must be careful, since the values and expected data stored inside
 * might not be Serializable.
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
 * @see musaico.foundation.value.finite.MODULE#COPYRIGHT
 * @see musaico.foundation.value.finite.MODULE#LICENSE
 */
public class Exit
    extends One<String>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new Exit from a state graph.
     * </p>
     *
     * @param name The name of the state graph, such as "on_off"
     *             or "flowchart" and so on.  Must not be null.
     */
    public Exit (
                 String name
                 )
        throws ParametersMustNotBeNull.Violation
    {
        super ( new StandardValueClass<String> ( // value_class
                    String.class, // element_class
                    "" ),         // none
                null, // cause
                name == null
                    ? null
                    : name );
    }
}
