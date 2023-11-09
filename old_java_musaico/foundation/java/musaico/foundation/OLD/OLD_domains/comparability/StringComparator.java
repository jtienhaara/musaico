package musaico.foundation.domains.comparability;

import java.io.Serializable;

import java.util.Comparator;


import musaico.foundation.domains.ClassName;


/**
 * <p>
 * Compares Strings.
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
 * @see musaico.foundation.domains.comparability.MODULE#COPYRIGHT
 * @see musaico.foundation.domains.comparability.MODULE#LICENSE
 */
public class StringComparator
    implements Comparator<String>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The singleton StringComparator. */
    public static final StringComparator COMPARATOR =
        new StringComparator ();




    // Creates the StringComparator singleton.
    // Use StringComparator.COMPARATOR instead.
    private StringComparator ()
    {
    }


    /**
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public final int compare (
                              String left,
                              String right
                              )
    {
        if ( left == null )
        {
            if ( right == null )
            {
                return 0;
            }
            else
            {
                return Integer.MAX_VALUE;
            }
        }
        else if ( right == null )
        {
            return Integer.MIN_VALUE + 1;
        }

        final int string_comparison = left.compareTo ( right );
        if ( string_comparison == 0 )
        {
            return 0;
        }

        final int lower_case_comparison =
            left.toLowerCase ().compareTo ( right.toLowerCase () );

        if ( lower_case_comparison != 0 )
        {
            return lower_case_comparison;
        }

        // Lower case matches, but case-sensitive doesn't.
        if ( string_comparison < 0 )
        {
            return -1;
        }
        else
        {
            return 1;
        }
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
                                 Object object
                                 )
    {
        if ( object == this )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return ClassName.of ( this.getClass () )
            .hashCode ();
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return ClassName.of ( this.getClass () );
    }
}
