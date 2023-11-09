package musaico.foundation.filter.string;

import java.io.Serializable;


import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;


/**
 * <p>
 * Filters Strings which do not include any space characters
 * (such as ' ', tab, newline, and so on).
 * </p>
 *
 *
 * <p>
 * *** Do not forget to add new Filters to the classes in
 * *** musaico.foundation.contract.obligation
 * *** and musaico.foundation.contract.guarantee!
 * </p>
 *
 * <p>
 * In Java String is the end of the road for class inheritance,
 * so there is no point in making String Filters generic.
 * </p>
 *
 * <p>
 * In Java every Filter must be Serializable in order to play nicely
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
 * @see musaico.foundation.filter.string.MODULE#COPYRIGHT
 * @see musaico.foundation.filter.string.MODULE#LICENSE
 */
public class StringExcludesSpaces
    extends StringPattern
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The StringExcludesSpaces Filter singleton. */
    public static final StringExcludesSpaces FILTER =
        new StringExcludesSpaces ();


    private StringExcludesSpaces ()
    {
        super ( "^[^\\s]*$" );
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "string [ excludes spaces ]";
    }
}
