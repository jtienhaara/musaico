package musaico.foundation.typing;

import java.io.Serializable;

import java.util.Comparator;


/**
 * <p>
 * Compares Types by their String representations, alphabetically,
 * case-insensitive.
 * </p>
 *
 * <p>
 * For example, type "Number" comes before type "Text" but after
 * type "Boolean".
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
 * @see musaico.foundation.typing.MODULE#COPYRIGHT
 * @see musaico.foundation.typing.MODULE#LICENSE
 */
public class TypeAlphabeticalComparator
    implements Comparator<Type<Object>>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare (
                        Type<Object> left,
                        Type<Object> right
                        )
    {
        if ( left == null )
        {
            if ( right == null )
            {
                // null == null
                return 0;
            }
            else
            {
                // null > any Type
                return Integer.MAX_VALUE;
            }
        }
        else if ( right == null )
        {
            // Any Type < null
            return Integer.MIN_VALUE + 1;
        }

        String left_as_string = "" + left;
        String right_as_string = "" + right;

        return left_as_string.compareToIgnoreCase ( right_as_string );
    }
}
