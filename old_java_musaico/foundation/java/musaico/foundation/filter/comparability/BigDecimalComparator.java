package musaico.foundation.filter.comparability;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Comparator;


import musaico.foundation.structure.ClassName;


/**
 * <p>
 * Compares a BigDecimal to any type of Number.
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
public class BigDecimalComparator<NUMBER extends Number>
    implements Comparator<NUMBER>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The singleton generic BigDecimalComparator, for comparing
     *  BigDecimals to other Numbers. */
    public static final BigDecimalComparator<Number> COMPARATOR =
        new BigDecimalComparator<Number> ();

    /** The singleton specific BigDecimalComparator, for comparing
     *  BigDecimals to BigDecimals. */
    public static final BigDecimalComparator<BigDecimal> BIG_DECIMAL_COMPARATOR =
        new BigDecimalComparator<BigDecimal> ();


    // Creates the BigDecimalComparator singletons.
    // Use BigDecimalComparator.COMPARATOR
    // or BigDecimalComparator.BIG_DECIMAL_COMPARATOR instead.
    private BigDecimalComparator ()
    {
    }


    /**
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public final int compare (
            NUMBER left,
            NUMBER right
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

        final BigDecimal left_value = new BigDecimal ( "" + left );
        final BigDecimal right_value = new BigDecimal ( "" + right );

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
