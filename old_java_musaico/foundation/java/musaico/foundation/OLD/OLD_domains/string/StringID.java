package musaico.foundation.domains.string;

import java.io.Serializable;


import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;


/**
 * <p>
 * The domain of all Strings which follow a simple ASCii identifier
 * pattern.
 * </p>
 *
 * <p>
 * Each string must start with a-z, A-Z or the underscore (_),
 * followed by 0 or more characters (a-z, A-Z, the underscore, or 0-9).
 * </p>
 *
 *
 * <p>
 * *** Do not forget to add new domains to the classes in
 * *** musaico.foundation.contract.obligation
 * *** and musaico.foundation.contract.guarantee!
 * </p>
 *
 * <p>
 * In Java every Domain must be Serializable in order to play nicely
 * across RMI.
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
 * @see musaico.foundation.domains.string.MODULE#COPYRIGHT
 * @see musaico.foundation.domains.string.MODULE#LICENSE
 */
public class StringID
    extends StringPattern
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The StringID domain singleton. */
    public static final StringID DOMAIN =
        new StringID ();


    protected StringID ()
    {
        super ( "^[a-zA-Z_][a-zA-Z_0-9]*$" );
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "string [ id ]";
    }
}
