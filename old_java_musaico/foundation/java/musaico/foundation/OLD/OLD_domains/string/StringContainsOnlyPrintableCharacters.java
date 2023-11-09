package musaico.foundation.domains.string;

import java.io.Serializable;


import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;


/**
 * <p>
 * The domain of all Strings which contain only printable characters,
 * such as a-z, A-Z, 0-9, punctuation, alphabetical characters from
 * outside the Latin-1 set, and so on.
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
public class StringContainsOnlyPrintableCharacters
    extends StringPattern
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The StringContainsOnlyPrintableCharacters domain singleton. */
    public static final StringContainsOnlyPrintableCharacters DOMAIN =
        new StringContainsOnlyPrintableCharacters ();


    protected StringContainsOnlyPrintableCharacters ()
    {
        super ( "^[^\\p{Print}]*$" );
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "string [ printable characters ]";
    }
}
