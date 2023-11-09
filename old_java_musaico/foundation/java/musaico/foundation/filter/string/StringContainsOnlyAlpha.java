package musaico.foundation.filter.string;

import java.io.Serializable;


import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;


/**
 * <p>
 * A Filter that keeps only Strings containing only alpha characters, such
 * as a-z, A-Z, and alphabetical characters from outside the Latin-1 set.
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
public class StringContainsOnlyAlpha
    extends StringPattern
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The StringContainsOnlyAlpha Filter singleton. */
    public static final StringContainsOnlyAlpha FILTER =
        new StringContainsOnlyAlpha ();


    private StringContainsOnlyAlpha ()
    {
        super ( "^[\\p{Alpha}]*$" );
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "string [ alpha ]";
    }
}
