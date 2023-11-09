package musaico.foundation.construct;

import java.io.Serializable;


import musaico.foundation.structure.ClassName;


/**
 * <p>
 * The housing for the final output Object produced by a (sequence of)
 * Constructor(s), simply containing an already-build output object.
 * </p>
 *
 *
 * <p>
 * In Java every Construct must be Serializable in order to
 * play nicely across RMI.  However users of the Construct
 * must be careful, since it could contain non-Serializable elements.
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
 * @see musaico.foundation.construct.MODULE#COPYRIGHT
 * @see musaico.foundation.construct.MODULE#LICENSE
 */
public class StandardConstructed<OUTPUT extends Object>
    implements Constructed<OUTPUT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    // The pre-build output object.
    private final OUTPUT output;


    /**
     * <p>
     * Creates a new StandardConstructed output object.
     * </p>
     *
     * @param output The pre-build object to output.
     *               Must not be null.
     *
     * @throws NullPointerException If the specified output object
     *                              is null.
     */
    public StandardConstructed (
            OUTPUT output
            )
        throws NullPointerException
    {
        if ( output == null )
        {
            throw new NullPointerException ( "Cannot create " + ClassName.of ( this.getClass () ) + " from null output object" );
        }

        this.output = output;
    }


    /**
     * @see musaico.foundation.construct.Constructed#output()
     */
    @Override
    public final OUTPUT output ()
    {
        return this.output;
    }
}
