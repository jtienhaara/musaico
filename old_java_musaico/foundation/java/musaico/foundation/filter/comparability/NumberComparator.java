package musaico.foundation.filter.comparability;

import java.io.Serializable;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


import musaico.foundation.structure.ClassName;


/**
 * <p>
 * Compares any type of Number to any type of Number.
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
public class NumberComparator<NUMBER extends Number>
    implements Comparator<NUMBER>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Generic singleton NumberComparator for all Numbers, for when
     *  a more specific Comparator is not needed. */
    public static final NumberComparator<Number> COMPARATOR =
        new NumberComparator<Number> ();

    /** Generic NumberComparator for BigDecimals compared to BigDecimals. */
    public static final NumberComparator<BigDecimal> BIG_DECIMAL_COMPARATOR =
        new NumberComparator<BigDecimal> ();

    /** Generic NumberComparator for BigIntegers compared to BigIntegers. */
    public static final NumberComparator<BigInteger> BIG_INTEGER_COMPARATOR =
        new NumberComparator<BigInteger> ();

    /** Generic NumberComparator for Bytes compared to Bytes. */
    public static final NumberComparator<Byte> BYTE_COMPARATOR =
        new NumberComparator<Byte> ();

    /** Generic NumberComparator for Doubles compared to Doubles. */
    public static final NumberComparator<Double> DOUBLE_COMPARATOR =
        new NumberComparator<Double> ();

    /** Generic NumberComparator for Floats compared to Floats. */
    public static final NumberComparator<Float> FLOAT_COMPARATOR =
        new NumberComparator<Float> ();

    /** Generic NumberComparator for Integers compared to Integers. */
    public static final NumberComparator<Integer> INTEGER_COMPARATOR =
        new NumberComparator<Integer> ();

    /** Generic NumberComparator for Longs compared to Longs. */
    public static final NumberComparator<Long> LONG_COMPARATOR =
        new NumberComparator<Long> ();

    /** Generic NumberComparator for Shorts compared to Shorts. */
    public static final NumberComparator<Short> SHORT_COMPARATOR =
        new NumberComparator<Short> ();




    // Comparing Numbers:
    // Left           Right       Compare using
    // ----------------------------------------
    // Byte           Byte        Byte
    //                Short       Short
    //                Integer     Integer
    //                Long        Long
    //                BigInteger  BigInteger
    //                Float       Float
    //                Double      Double
    //                BigDecimal  BigDecimal
    // Short          Byte        Short
    //                Short       Short
    //                Integer     Integer
    //                Long        Long
    //                BigInteger  BigInteger
    //                Float       Float
    //                Double      Double
    //                BigDecimal  BigDecimal
    // Integer        Byte        Integer
    //                Short       Integer
    //                Integer     Integer
    //                Long        Long
    //                BigInteger  BigInteger
    //                Float       BigDecimal
    //                Double      Double
    //                BigDecimal  BigDecimal
    // Long           Byte        Long
    //                Short       Long
    //                Integer     Long
    //                Long        Long
    //                BigInteger  BigInteger
    //                Float       BigDecimal
    //                Double      BigDecimal
    //                BigDecimal  BigDecimal
    // BigInteger     Byte        BigInteger
    //                Short       BigInteger
    //                Integer     BigInteger
    //                Long        BigInteger
    //                BigInteger  BigInteger
    //                Float       BigDecimal
    //                Double      BigDecimal
    //                BigDecimal  BigDecimal
    // Float          Byte        Float
    //                Short       Float
    //                Integer     BigDecimal
    //                Long        BigDecimal
    //                BigInteger  BigDecimal
    //                Float       Float
    //                Double      Double
    //                BigDecimal  BigDecimal
    // Double         Byte        Double
    //                Short       Double
    //                Integer     Double
    //                Long        BigDecimal
    //                BigInteger  BigInteger
    //                Float       Double
    //                Double      Double
    //                BigDecimal  BigDecimal
    // BigDecimal     Byte        BigDecimal
    //                Short       BigDecimal
    //                Integer     BigDecimal
    //                Long        BigDecimal
    //                BigInteger  BigDecimal
    //                Float       BigDecimal
    //                Double      BigDecimal
    //                BigDecimal  BigDecimal
    private static final Map<Class<? extends Number>, Map<Class<? extends Number>, Comparator<Number>>> NUMBER_COMPARATORS;
    static
    {
        NUMBER_COMPARATORS =
            new HashMap<Class<? extends Number>, Map<Class<? extends Number>, Comparator<Number>>> ();

        // Byte compared to N:
        final Map<Class<? extends Number>, Comparator<Number>> left_byte =
            new HashMap<Class<? extends Number>, Comparator<Number>> ();
        left_byte.put ( Byte.class,
                        ByteComparator.COMPARATOR );
        left_byte.put ( Short.class,
                        ShortComparator.COMPARATOR );
        left_byte.put ( Integer.class,
                        IntegerComparator.COMPARATOR );
        left_byte.put ( Long.class,
                        LongComparator.COMPARATOR );
        left_byte.put ( BigInteger.class,
                        BigIntegerComparator.COMPARATOR );
        left_byte.put ( Float.class,
                        FloatComparator.COMPARATOR );
        left_byte.put ( Double.class,
                        DoubleComparator.COMPARATOR );
        left_byte.put ( BigDecimal.class,
                        BigDecimalComparator.COMPARATOR );

        // Short compared to N:
        final Map<Class<? extends Number>, Comparator<Number>> left_short =
            new HashMap<Class<? extends Number>, Comparator<Number>> ();
        left_short.put ( Byte.class,
                         ShortComparator.COMPARATOR );
        left_short.put ( Short.class,
                         ShortComparator.COMPARATOR );
        left_short.put ( Integer.class,
                         IntegerComparator.COMPARATOR );
        left_short.put ( Long.class,
                         LongComparator.COMPARATOR );
        left_short.put ( BigInteger.class,
                         BigIntegerComparator.COMPARATOR );
        left_short.put ( Float.class,
                         FloatComparator.COMPARATOR );
        left_short.put ( Double.class,
                         DoubleComparator.COMPARATOR );
        left_short.put ( BigDecimal.class,
                         BigDecimalComparator.COMPARATOR );

        // Integer compared to N:
        final Map<Class<? extends Number>, Comparator<Number>> left_integer =
            new HashMap<Class<? extends Number>, Comparator<Number>> ();
        left_integer.put ( Byte.class,
                           IntegerComparator.COMPARATOR );
        left_integer.put ( Short.class,
                           IntegerComparator.COMPARATOR );
        left_integer.put ( Integer.class,
                           IntegerComparator.COMPARATOR );
        left_integer.put ( Long.class,
                           LongComparator.COMPARATOR );
        left_integer.put ( BigInteger.class,
                           BigIntegerComparator.COMPARATOR );
        left_integer.put ( Float.class,
                           DoubleComparator.COMPARATOR );
        left_integer.put ( Double.class,
                           DoubleComparator.COMPARATOR );
        left_integer.put ( BigDecimal.class,
                           BigDecimalComparator.COMPARATOR );

        // Long compared to N:
        final Map<Class<? extends Number>, Comparator<Number>> left_long =
            new HashMap<Class<? extends Number>, Comparator<Number>> ();
        left_long.put ( Byte.class,
                        LongComparator.COMPARATOR );
        left_long.put ( Short.class,
                        LongComparator.COMPARATOR );
        left_long.put ( Integer.class,
                        LongComparator.COMPARATOR );
        left_long.put ( Long.class,
                        LongComparator.COMPARATOR );
        left_long.put ( BigInteger.class,
                        BigIntegerComparator.COMPARATOR );
        left_long.put ( Float.class,
                        BigDecimalComparator.COMPARATOR );
        left_long.put ( Double.class,
                        BigDecimalComparator.COMPARATOR );
        left_long.put ( BigDecimal.class,
                        BigDecimalComparator.COMPARATOR );

        // BigInteger compared to N:
        final Map<Class<? extends Number>, Comparator<Number>> left_big_integer =
            new HashMap<Class<? extends Number>, Comparator<Number>> ();
        left_big_integer.put ( Byte.class,
                               BigIntegerComparator.COMPARATOR );
        left_big_integer.put ( Short.class,
                               BigIntegerComparator.COMPARATOR );
        left_big_integer.put ( Integer.class,
                               BigIntegerComparator.COMPARATOR );
        left_big_integer.put ( Long.class,
                               BigIntegerComparator.COMPARATOR );
        left_big_integer.put ( BigInteger.class,
                               BigIntegerComparator.COMPARATOR );
        left_big_integer.put ( Float.class,
                               BigDecimalComparator.COMPARATOR );
        left_big_integer.put ( Double.class,
                               BigDecimalComparator.COMPARATOR );
        left_big_integer.put ( BigDecimal.class,
                               BigDecimalComparator.COMPARATOR );

        // Float compared to N:
        final Map<Class<? extends Number>, Comparator<Number>> left_float =
            new HashMap<Class<? extends Number>, Comparator<Number>> ();
        left_float.put ( Byte.class,
                         FloatComparator.COMPARATOR );
        left_float.put ( Short.class,
                         FloatComparator.COMPARATOR );
        left_float.put ( Integer.class,
                         DoubleComparator.COMPARATOR );
        left_float.put ( Long.class,
                         BigDecimalComparator.COMPARATOR );
        left_float.put ( BigInteger.class,
                         BigDecimalComparator.COMPARATOR );
        left_float.put ( Float.class,
                         FloatComparator.COMPARATOR );
        left_float.put ( Double.class,
                         DoubleComparator.COMPARATOR );
        left_float.put ( BigDecimal.class,
                         BigDecimalComparator.COMPARATOR );

        // Double compared to N:
        final Map<Class<? extends Number>, Comparator<Number>> left_double =
            new HashMap<Class<? extends Number>, Comparator<Number>> ();
        left_double.put ( Byte.class,
                          DoubleComparator.COMPARATOR );
        left_double.put ( Short.class,
                          DoubleComparator.COMPARATOR );
        left_double.put ( Integer.class,
                          DoubleComparator.COMPARATOR );
        left_double.put ( Long.class,
                          BigDecimalComparator.COMPARATOR );
        left_double.put ( BigInteger.class,
                          BigDecimalComparator.COMPARATOR );
        left_double.put ( Float.class,
                          DoubleComparator.COMPARATOR );
        left_double.put ( Double.class,
                          DoubleComparator.COMPARATOR );
        left_double.put ( BigDecimal.class,
                          BigDecimalComparator.COMPARATOR );

        // BigDecimal compared to N:
        final Map<Class<? extends Number>, Comparator<Number>> left_big_decimal =
            new HashMap<Class<? extends Number>, Comparator<Number>> ();
        left_big_decimal.put ( Byte.class,
                               BigDecimalComparator.COMPARATOR );
        left_big_decimal.put ( Short.class,
                               BigDecimalComparator.COMPARATOR );
        left_big_decimal.put ( Integer.class,
                               BigDecimalComparator.COMPARATOR );
        left_big_decimal.put ( Long.class,
                               BigDecimalComparator.COMPARATOR );
        left_big_decimal.put ( BigInteger.class,
                               BigDecimalComparator.COMPARATOR );
        left_big_decimal.put ( Float.class,
                               BigDecimalComparator.COMPARATOR );
        left_big_decimal.put ( Double.class,
                               BigDecimalComparator.COMPARATOR );
        left_big_decimal.put ( BigDecimal.class,
                               BigDecimalComparator.COMPARATOR );

        NUMBER_COMPARATORS.put ( Byte.class, left_byte );
        NUMBER_COMPARATORS.put ( Short.class, left_short );
        NUMBER_COMPARATORS.put ( Integer.class, left_integer );
        NUMBER_COMPARATORS.put ( Long.class, left_long );
        NUMBER_COMPARATORS.put ( BigInteger.class, left_big_integer );
        NUMBER_COMPARATORS.put ( Float.class, left_float );
        NUMBER_COMPARATORS.put ( Double.class, left_double );
        NUMBER_COMPARATORS.put ( BigDecimal.class, left_big_decimal );
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
                return Integer.MAX_VALUE;
            }
        }
        else if ( right == null )
        {
            return Integer.MIN_VALUE + 1;
        }

        final Map<Class<? extends Number>, Comparator<Number>> comparators =
            NumberComparator.NUMBER_COMPARATORS.get ( left.getClass () );
        final Comparator<Number> comparator;
        if ( comparators == null )
        {
            comparator = NumberComparator.COMPARATOR;
        }
        else
        {
            final Comparator<Number> maybe_comparator =
                comparators.get ( right.getClass () );
            if ( maybe_comparator == null )
            {
                comparator = NumberComparator.COMPARATOR;
            }
            else
            {
                comparator = maybe_comparator;
            }
        }

        final int comparison =
            comparator.compare ( left, right );
        return comparison;
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
