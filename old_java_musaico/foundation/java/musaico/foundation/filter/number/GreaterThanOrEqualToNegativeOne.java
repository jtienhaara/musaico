package musaico.foundation.filter.number;

import java.io.Serializable;


import java.math.BigDecimal;
import java.math.BigInteger;


import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.filter.comparability.NumberComparator;

import musaico.foundation.structure.ClassName;


/**
 * <p>
 * Keeps all non-infinite numbers greater than or equal to -1.
 * </p>
 *
 *
 * <p>
 * *** Do not forget to add useful new Filters to the classes in
 * *** musaico.foundation.contract.obligation
 * *** and musaico.foundation.contract.guarantee!
 * </p>
 *
 *
 * <p>
 * In Java every Filter must be generic in order to
 * enable composability.
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
 * @see musaico.foundation.filter.number.MODULE#COPYRIGHT
 * @see musaico.foundation.filter.number.MODULE#LICENSE
 */
public class GreaterThanOrEqualToNegativeOne<NUMBER extends Number>
    implements Filter<NUMBER>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Singleton GreaterThanOrEqualToNegativeOne covering raw Numbers,
     *  for when generics are unnecessary. */
    public static final GreaterThanOrEqualToNegativeOne<Number> FILTER =
        new GreaterThanOrEqualToNegativeOne<Number> ();

    /** Singleton GreaterThanOrEqualToNegativeOne covering BigDecimals,
     *  for when generics are unnecessary. */
    public static final GreaterThanOrEqualToNegativeOne<BigDecimal> BIG_DECIMAL_FILTER =
        new GreaterThanOrEqualToNegativeOne<BigDecimal> ();

    /** Singleton GreaterThanOrEqualToNegativeOne covering BigIntegers,
     *  for when generics are unnecessary. */
    public static final GreaterThanOrEqualToNegativeOne<BigInteger> BIG_INTEGER_FILTER =
        new GreaterThanOrEqualToNegativeOne<BigInteger> ();

    /** Singleton GreaterThanOrEqualToNegativeOne covering Bytes,
     *  for when generics are unnecessary. */
    public static final GreaterThanOrEqualToNegativeOne<Byte> BYTE_FILTER =
        new GreaterThanOrEqualToNegativeOne<Byte> ();

    /** Singleton GreaterThanOrEqualToNegativeOne covering Doubles,
     *  for when generics are unnecessary. */
    public static final GreaterThanOrEqualToNegativeOne<Double> DOUBLE_FILTER =
        new GreaterThanOrEqualToNegativeOne<Double> ();

    /** Singleton GreaterThanOrEqualToNegativeOne covering Floats,
     *  for when generics are unnecessary. */
    public static final GreaterThanOrEqualToNegativeOne<Float> FLOAT_FILTER =
        new GreaterThanOrEqualToNegativeOne<Float> ();

    /** Singleton GreaterThanOrEqualToNegativeOne covering Integers,
     *  for when generics are unnecessary. */
    public static final GreaterThanOrEqualToNegativeOne<Integer> INTEGER_FILTER =
        new GreaterThanOrEqualToNegativeOne<Integer> ();

    /** Singleton GreaterThanOrEqualToNegativeOne covering Longs,
     *  for when generics are unnecessary. */
    public static final GreaterThanOrEqualToNegativeOne<Long> LONG_FILTER =
        new GreaterThanOrEqualToNegativeOne<Long> ();

    /** Singleton GreaterThanOrEqualToNegativeOne covering Shorts,
     *  for when generics are unnecessary. */
    public static final GreaterThanOrEqualToNegativeOne<Short> SHORT_FILTER =
        new GreaterThanOrEqualToNegativeOne<Short> ();


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
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
     * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
     */
    @Override
    public final FilterState filter (
            NUMBER value
            )
    {
        if ( value == null )
        {
            return FilterState.DISCARDED;
        }
        else if ( value instanceof BigDecimal )
        {
            BigDecimal num = (BigDecimal) value;
            if ( num.compareTo ( NegativeOne.BIG_DECIMAL ) >= 0 )
            {
                return FilterState.KEPT;
            }
            else
            {
                return FilterState.DISCARDED;
            }
        }
        else if ( value instanceof BigInteger )
        {
            BigInteger num = (BigInteger) value;
            if ( num.compareTo ( NegativeOne.BIG_INTEGER ) >= 0 )
            {
                return FilterState.KEPT;
            }
            else
            {
                return FilterState.DISCARDED;
            }
        }
        else if ( value instanceof Double )
        {
            Double num = (Double) value;
            if ( num.isNaN () )
            {
                return FilterState.DISCARDED;
            }
            else if ( num.isInfinite () )
            {
                return FilterState.DISCARDED;
            }
            else if ( num.doubleValue () >= -1D )
            {
                return FilterState.KEPT;
            }
            else
            {
                return FilterState.DISCARDED;
            }
        }
        else if ( value instanceof Float )
        {
            Float num = (Float) value;
            if ( num.isNaN () )
            {
                return FilterState.DISCARDED;
            }
            else if ( num.isInfinite () )
            {
                return FilterState.DISCARDED;
            }
            else if ( num.floatValue () >= -1F )
            {
                return FilterState.KEPT;
            }
            else
            {
                return FilterState.DISCARDED;
            }
        }
        else if ( ( value instanceof Integer )
                  || ( value instanceof Long ) )
        {
            long num = value.longValue ();
            if ( num >= -1L )
            {
                return FilterState.KEPT;
            }
            else
            {
                return FilterState.DISCARDED;
            }
        }

        // Otherwise fall back on NumberComparator to do the work for us.
        final int comparison = NumberComparator.COMPARATOR
            .compare ( value, NegativeOne.BIG_DECIMAL );
        if ( comparison >= 0 )
        {
            return FilterState.KEPT;
        }
        else
        {
            return FilterState.DISCARDED;
        }
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return ClassName.of ( this.getClass () ).hashCode ();
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return ">= -1";
    }
}
