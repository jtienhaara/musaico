package musaico.foundation.filter.comparability;

import java.io.Serializable;

import java.util.Comparator;


import musaico.foundation.structure.ClassName;


/**
 * <p>
 * Compares a Byte to any type of Number.
 * </p>
 *
 *
 * <p>
 * In Java every Comparator must be Serializable in order to play nicely
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
 * @see musaico.foundation.filter.comparability.MODULE#COPYRIGHT
 * @see musaico.foundation.filter.comparability.MODULE#LICENSE
 */
public class ByteComparator<NUMBER extends Number>
    implements Comparator<NUMBER>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The singleton generic ByteComparator, for comparing
     *  Bytes to other Numbers. */
    public static final ByteComparator<Number> COMPARATOR =
        new ByteComparator<Number> ();

    /** The singleton specific ByteComparator, for comparing
     *  Bytes to Bytes. */
    public static final ByteComparator<Byte> BYTE_COMPARATOR =
        new ByteComparator<Byte> ();


    // Creates the ByteComparator singletons.
    // Use ByteComparator.COMPARATOR
    // or ByteComparator.BYTE_COMPARATOR instead.
    private ByteComparator ()
    {
    }


    /**
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public final int compare (
            Number left,
            Number right
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
                // null > any Number.
                return Integer.MAX_VALUE;
            }
        }
        else if ( right == null )
        {
            // Any Number < null.
            return Integer.MIN_VALUE + 1;
        }

        final byte left_value = left.byteValue ();
        final byte right_value = right.byteValue ();

        final int int_difference = left_value - right_value;

        return int_difference;
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
        else if ( object == null )
        {
            return false;
        }
        else if ( object.getClass () != this.getClass () )
        {
            return false;
        }

        return true;
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
