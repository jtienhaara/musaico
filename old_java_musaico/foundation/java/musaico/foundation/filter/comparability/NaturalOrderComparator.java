package musaico.foundation.filter.comparability;

import java.io.Serializable;

import java.util.Comparator;


import musaico.foundation.structure.ClassName;


/**
 * <p>
 * A Comparator which simply uses the natural order of Comparable
 * objects to make comparisons.
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
 * @see musaico.foundation.filter.comparability.MODULE#COPYRIGHT
 * @see musaico.foundation.filter.comparability.MODULE#LICENSE
 */
public class NaturalOrderComparator<NATURALLY_ORDERED extends Comparable<NATURALLY_ORDERED>>
    implements Comparator<NATURALLY_ORDERED>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public final int compare (
            NATURALLY_ORDERED left,
            NATURALLY_ORDERED right
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
                // null > any object.
                return Integer.MAX_VALUE;
            }
        }
        else if ( right == null )
        {
            // any object < null.
            return Integer.MIN_VALUE + 1;
        }

        return left.compareTo ( right );
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
            Object obj
            )
    {
        if ( obj == this )
        {
            return true;
        }
        else if ( obj == null )
        {
            return false;
        }
        else if ( this.getClass () != obj.getClass () )
        {
            return false;
        }

        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return this.getClass ().getName ().hashCode ();
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
