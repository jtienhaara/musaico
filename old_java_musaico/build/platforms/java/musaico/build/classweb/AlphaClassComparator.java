package musaico.build.classweb;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * <p>
 * Sorts classes more or less alphabetically (ASCii sort).
 * </p>
 */
public class AlphaClassComparator
    implements Comparator<Class<?>>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    public int compare ( Class<?> class_or_interface1, Class<?> class_or_interface2 )
    {
        if ( class_or_interface1 == null )
        {
            if ( class_or_interface2 == null )
            {
                // null == null
                return 0;
            }

            // null > class_or_interface2
            return 1;
        }
        else if ( class_or_interface2 == null )
        {
            // class_or_interface1 < null
            return -1;
        }

        String name1 = class_or_interface1.getName ();
        String name2 = class_or_interface2.getName ();

        return name1.compareTo ( name2 );
    }
}
