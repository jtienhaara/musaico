package musaico.foundation.filter.comparability;

import java.io.Serializable;

import java.math.BigInteger;

import java.util.Comparator;


import musaico.foundation.structure.ClassName;


/**
 * <p>
 * Compares a BigInteger to any type of Number.
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
public class BigIntegerComparator<NUMBER extends Number>
    implements Comparator<NUMBER>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The singleton generic BigIntegerComparator, for comparing
     *  BigIntegers to other Numbers. */
    public static final BigIntegerComparator<Number> COMPARATOR =
        new BigIntegerComparator<Number> ();

    /** The singleton specific BigIntegerComparator, for comparing
     *  BigIntegers to BigIntegers. */
    public static final BigIntegerComparator<BigInteger> BIG_INTEGER_COMPARATOR =
        new BigIntegerComparator<BigInteger> ();


    // Creates the BigIntegerComparator singletons.
    // Use BigIntegerComparator.COMPARATOR
    // or BigIntegerComparator.BIG_INTEGER_COMPARATOR instead.
    private BigIntegerComparator ()
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

        final BigInteger left_value = new BigInteger ( "" + left );
        final BigInteger right_value = new BigInteger ( "" + right );

        return left_value.compareTo ( right_value );
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
