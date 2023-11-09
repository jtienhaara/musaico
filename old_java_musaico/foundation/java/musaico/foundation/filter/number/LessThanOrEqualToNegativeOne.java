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
 * Keeps all numbers less than or equal to -1.
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
public class LessThanOrEqualToNegativeOne<NUMBER extends Number>
    implements Filter<NUMBER>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Singleton LessThanOrEqualToNegativeOne covering raw Numbers,
     *  for when generics are unnecessary. */
    public static final LessThanOrEqualToNegativeOne<Number> FILTER =
        new LessThanOrEqualToNegativeOne<Number> ();

    /** Singleton LessThanOrEqualToNegativeOne covering BigDecimals,
     *  for when generics are unnecessary. */
    public static final LessThanOrEqualToNegativeOne<BigDecimal> BIG_DECIMAL_FILTER =
        new LessThanOrEqualToNegativeOne<BigDecimal> ();

    /** Singleton LessThanOrEqualToNegativeOne covering BigIntegers,
     *  for when generics are unnecessary. */
    public static final LessThanOrEqualToNegativeOne<BigInteger> BIG_INTEGER_FILTER =
        new LessThanOrEqualToNegativeOne<BigInteger> ();

    /** Singleton LessThanOrEqualToNegativeOne covering Bytes,
     *  for when generics are unnecessary. */
    public static final LessThanOrEqualToNegativeOne<Byte> BYTE_FILTER =
        new LessThanOrEqualToNegativeOne<Byte> ();

    /** Singleton LessThanOrEqualToNegativeOne covering Doubles,
     *  for when generics are unnecessary. */
    public static final LessThanOrEqualToNegativeOne<Double> DOUBLE_FILTER =
        new LessThanOrEqualToNegativeOne<Double> ();

    /** Singleton LessThanOrEqualToNegativeOne covering Floats,
     *  for when generics are unnecessary. */
    public static final LessThanOrEqualToNegativeOne<Float> FLOAT_FILTER =
        new LessThanOrEqualToNegativeOne<Float> ();

    /** Singleton LessThanOrEqualToNegativeOne covering Integers,
     *  for when generics are unnecessary. */
    public static final LessThanOrEqualToNegativeOne<Integer> INTEGER_FILTER =
        new LessThanOrEqualToNegativeOne<Integer> ();

    /** Singleton LessThanOrEqualToNegativeOne covering Longs,
     *  for when generics are unnecessary. */
    public static final LessThanOrEqualToNegativeOne<Long> LONG_FILTER =
        new LessThanOrEqualToNegativeOne<Long> ();

    /** Singleton LessThanOrEqualToNegativeOne covering Shorts,
     *  for when generics are unnecessary. */
    public static final LessThanOrEqualToNegativeOne<Short> SHORT_FILTER =
        new LessThanOrEqualToNegativeOne<Short> ();


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
            if ( num.compareTo ( NegativeOne.BIG_DECIMAL ) <= 0 )
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
            if ( num.compareTo ( NegativeOne.BIG_INTEGER ) <= 0 )
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
            else if ( num.doubleValue () <= -1D )
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
            else if ( num.floatValue () <= -1F )
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
            if ( num <= -1L )
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
        if ( comparison <= 0 )
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
        return "<= -1";
    }
}
