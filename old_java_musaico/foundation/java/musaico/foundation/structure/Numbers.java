package musaico.foundation.structure;


/**
 * <p>
 * Does the mindless casting necessary in order to use "primitive"
 * types with full-fledged classes.
 * </p>
 *
 * <p>
 * For example, suppose you want to create a new
 * <code> Vector&lt;Double&gt; </code>, but you want to pass in
 * an array of <code> double </code>s:
 * </p>
 *
 * <pre>
 *     final double [] array = ...;
 *     final Vector<Double> vector =
 *         new Vector<Double> ( Numbers.asObjects ( array ) );
 * </pre>
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
 * @see musaico.foundation.structure.MODULE#COPYRIGHT
 * @see musaico.foundation.structure.MODULE#LICENSE
 */
public class Numbers
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Converts the specified array of <code> byte </code>s to an array of
     * <code> Byte </code>s.
     * </p>
     *
     * @param primitives The primitives to convert to full-fledged Objects.
     *                   If null, then null will be returned.
     *
     * @return The primitives converted to full-fledged Objects.
     *         If null was passed in, then null is returned.
     *         CAN BE null.
     */
    public static final Byte [] asByteObjects (
            byte ... primitives
            )
    {
        if ( primitives == null )
        {
            return null;
        }

        final Byte [] objects = new Byte [ primitives.length ];
        int o = 0;
        for ( byte primitive : primitives )
        {
            objects [ o ] = primitive;
            o ++;
        }

        return objects;
    }


    /**
     * <p>
     * Converts the specified array of <code> double </code>s to an array of
     * <code> Double </code>s.
     * </p>
     *
     * @param primitives The primitives to convert to full-fledged Objects.
     *                   If null, then null will be returned.
     *
     * @return The primitives converted to full-fledged Objects.
     *         If null was passed in, then null is returned.
     *         CAN BE null.
     */
    public static final Double [] asDoubleObjects (
            double ... primitives
            )
    {
        if ( primitives == null )
        {
            return null;
        }

        final Double [] objects = new Double [ primitives.length ];
        int o = 0;
        for ( double primitive : primitives )
        {
            objects [ o ] = primitive;
            o ++;
        }

        return objects;
    }


    /**
     * <p>
     * Converts the specified array of <code> float </code>s to an array of
     * <code> Float </code>s.
     * </p>
     *
     * @param primitives The primitives to convert to full-fledged Objects.
     *                   If null, then null will be returned.
     *
     * @return The primitives converted to full-fledged Objects.
     *         If null was passed in, then null is returned.
     *         CAN BE null.
     */
    public static final Float [] asFloatObjects (
            float ... primitives
            )
    {
        if ( primitives == null )
        {
            return null;
        }

        final Float [] objects = new Float [ primitives.length ];
        int o = 0;
        for ( float primitive : primitives )
        {
            objects [ o ] = primitive;
            o ++;
        }

        return objects;
    }


    /**
     * <p>
     * Converts the specified array of <code> int </code>s to an array of
     * <code> Integer </code>s.
     * </p>
     *
     * @param primitives The primitives to convert to full-fledged Objects.
     *                   If null, then null will be returned.
     *
     * @return The primitives converted to full-fledged Objects.
     *         If null was passed in, then null is returned.
     *         CAN BE null.
     */
    public static final Integer [] asIntegerObjects (
            int ... primitives
            )
    {
        if ( primitives == null )
        {
            return null;
        }

        final Integer [] objects = new Integer [ primitives.length ];
        int o = 0;
        for ( int primitive : primitives )
        {
            objects [ o ] = primitive;
            o ++;
        }

        return objects;
    }


    /**
     * <p>
     * Converts the specified array of <code> long </code>s to an array of
     * <code> Long </code>s.
     * </p>
     *
     * @param primitives The primitives to convert to full-fledged Objects.
     *                   If null, then null will be returned.
     *
     * @return The primitives converted to full-fledged Objects.
     *         If null was passed in, then null is returned.
     *         CAN BE null.
     */
    public static final Long [] asLongObjects (
            long ... primitives
            )
    {
        if ( primitives == null )
        {
            return null;
        }

        final Long [] objects = new Long [ primitives.length ];
        int o = 0;
        for ( long primitive : primitives )
        {
            objects [ o ] = primitive;
            o ++;
        }

        return objects;
    }


    /**
     * <p>
     * Converts the specified array of <code> short </code>s to an array of
     * <code> Short </code>s.
     * </p>
     *
     * @param primitives The primitives to convert to full-fledged Objects.
     *                   If null, then null will be returned.
     *
     * @return The primitives converted to full-fledged Objects.
     *         If null was passed in, then null is returned.
     *         CAN BE null.
     */
    public static final Short [] asShortObjects (
            short ... primitives
            )
    {
        if ( primitives == null )
        {
            return null;
        }

        final Short [] objects = new Short [ primitives.length ];
        int o = 0;
        for ( short primitive : primitives )
        {
            objects [ o ] = primitive;
            o ++;
        }

        return objects;
    }


    /**
     * <p>
     * Converts the specified array of <code> Byte </code>s to an array of
     * <code> byte </code>s.
     * </p>
     *
     * @param objects The full-fledged objects to convert to primitives.
     *                If the array contains any nulls, then null will be
     *                returned for the whole array.
     *                If the whole objects array is null, then null
     *                will be returned.
     *
     * @return The full-fledged Objects converted to primitives.
     *         If null was passed in, or if any null elements were
     *         passed in, then null is returned.  CAN BE null.
     */
    public static final byte [] asBytePrimitives (
            Byte ... objects
            )
    {
        if ( objects == null )
        {
            return null;
        }

        final byte [] primitives = new byte [ objects.length ];
        int p = 0;
        for ( Byte object : objects )
        {
            if ( object == null )
            {
                return null;
            }

            primitives [ p ] = object.byteValue ();
            p ++;
        }

        return primitives;
    }


    /**
     * <p>
     * Converts the specified array of <code> Double </code>s to an array of
     * <code> double </code>s.
     * </p>
     *
     * @param objects The full-fledged objects to convert to primitives.
     *                If the array contains any nulls, then null will be
     *                returned for the whole array.
     *                If the whole objects array is null, then null
     *                will be returned.
     *
     * @return The full-fledged Objects converted to primitives.
     *         If null was passed in, or if any null elements were
     *         passed in, then null is returned.  CAN BE null.
     */
    public static final double [] asDoublePrimitives (
            Double ... objects
            )
    {
        if ( objects == null )
        {
            return null;
        }

        final double [] primitives = new double [ objects.length ];
        int p = 0;
        for ( Double object : objects )
        {
            if ( object == null )
            {
                return null;
            }

            primitives [ p ] = object.doubleValue ();
            p ++;
        }

        return primitives;
    }


    /**
     * <p>
     * Converts the specified array of <code> Float </code>s to an array of
     * <code> float </code>s.
     * </p>
     *
     * @param objects The full-fledged objects to convert to primitives.
     *                If the array contains any nulls, then null will be
     *                returned for the whole array.
     *                If the whole objects array is null, then null
     *                will be returned.
     *
     * @return The full-fledged Objects converted to primitives.
     *         If null was passed in, or if any null elements were
     *         passed in, then null is returned.  CAN BE null.
     */
    public static final float [] asFloatPrimitives (
            Float ... objects
            )
    {
        if ( objects == null )
        {
            return null;
        }

        final float [] primitives = new float [ objects.length ];
        int p = 0;
        for ( Float object : objects )
        {
            if ( object == null )
            {
                return null;
            }

            primitives [ p ] = object.floatValue ();
            p ++;
        }

        return primitives;
    }


    /**
     * <p>
     * Converts the specified array of <code> Integer </code>s to an array of
     * <code> int </code>s.
     * </p>
     *
     * @param objects The full-fledged objects to convert to primitives.
     *                If the array contains any nulls, then null will be
     *                returned for the whole array.
     *                If the whole objects array is null, then null
     *                will be returned.
     *
     * @return The full-fledged Objects converted to primitives.
     *         If null was passed in, or if any null elements were
     *         passed in, then null is returned.  CAN BE null.
     */
    public static final int [] asIntPrimitives (
            Integer ... objects
            )
    {
        if ( objects == null )
        {
            return null;
        }

        final int [] primitives = new int [ objects.length ];
        int p = 0;
        for ( Integer object : objects )
        {
            if ( object == null )
            {
                return null;
            }

            primitives [ p ] = object.intValue ();
            p ++;
        }

        return primitives;
    }


    /**
     * <p>
     * Converts the specified array of <code> Long </code>s to an array of
     * <code> long </code>s.
     * </p>
     *
     * @param objects The full-fledged objects to convert to primitives.
     *                If the array contains any nulls, then null will be
     *                returned for the whole array.
     *                If the whole objects array is null, then null
     *                will be returned.
     *
     * @return The full-fledged Objects converted to primitives.
     *         If null was passed in, or if any null elements were
     *         passed in, then null is returned.  CAN BE null.
     */
    public static final long [] asLongPrimitives (
            Long ... objects
            )
    {
        if ( objects == null )
        {
            return null;
        }

        final long [] primitives = new long [ objects.length ];
        int p = 0;
        for ( Long object : objects )
        {
            if ( object == null )
            {
                return null;
            }

            primitives [ p ] = object.longValue ();
            p ++;
        }

        return primitives;
    }


    /**
     * <p>
     * Converts the specified array of <code> Short </code>s to an array of
     * <code> short </code>s.
     * </p>
     *
     * @param objects The full-fledged objects to convert to primitives.
     *                If the array contains any nulls, then null will be
     *                returned for the whole array.
     *                If the whole objects array is null, then null
     *                will be returned.
     *
     * @return The full-fledged Objects converted to primitives.
     *         If null was passed in, or if any null elements were
     *         passed in, then null is returned.  CAN BE null.
     */
    public static final short [] asShortPrimitives (
            Short ... objects
            )
    {
        if ( objects == null )
        {
            return null;
        }

        final short [] primitives = new short [ objects.length ];
        int p = 0;
        for ( Short object : objects )
        {
            if ( object == null )
            {
                return null;
            }

            primitives [ p ] = object.shortValue ();
            p ++;
        }

        return primitives;
    }


    /**
     * <p>
     * Converts the specified array from doubles to bytes.
     * </p>
     *
     * <p>
     * Causes rounding.  The decimal portion of each double element
     * is truncated.
     * </p>
     *
     * @param min_value The value to use for any element that is
     *                  less than <code> Byte.MIN_VALUE </code>.
     *
     * @param max_value The value to use for any element that is
     *                  greater than <code> Byte.MAX_VALUE </code>.
     *
     * @param primitives The array of doubles to convert.
     *                   If null, then null is returned.
     *                   DO NOT PASS NULL.
     *
     * @return The newly created array of bytes, or null if
     *         the specified primitives array was null.
     */
    public static final byte [] doublesToBytes (
            byte min_value,
            byte max_value,
            double ... primitives
            )
    {
        if ( primitives == null )
        {
            return null;
        }

        final byte [] byte_primitives = new byte [ primitives.length ];
        for ( int p = 0; p < primitives.length; p ++ )
        {
            if ( primitives [ p ] < (double) Byte.MIN_VALUE )
            {
                byte_primitives [ p ] = min_value;
            }
            else if ( primitives [ p ] > (double) Byte.MAX_VALUE )
            {
                byte_primitives [ p ] = max_value;
            }
            else
            {
                byte_primitives [ p ] = (byte) primitives [ p ];
            }
        }

        return byte_primitives;
    }


    /**
     * <p>
     * Converts the specified array from doubles to floats.
     * </p>
     *
     * <p>
     * Can cause rounding, since there are doubles that are not
     * representable as floats.
     * </p>
     *
     * @param min_value The value to use for any element that is
     *                  less than <code> Float.MIN_VALUE </code>.
     *
     * @param max_value The value to use for any element that is
     *                  greater than <code> Float.MAX_VALUE </code>.
     *
     * @param primitives The array of doubles to convert.
     *                   If null, then null is returned.
     *                   DO NOT PASS NULL.
     *
     * @return The newly created array of floats, or null if
     *         the specified primitives array was null.
     */
    public static final float [] doublesToFloats (
            float min_value,
            float max_value,
            double ... primitives
            )
    {
        if ( primitives == null )
        {
            return null;
        }

        final float [] float_primitives = new float [ primitives.length ];
        for ( int p = 0; p < primitives.length; p ++ )
        {
            if ( primitives [ p ] < (double) Float.MIN_VALUE )
            {
                float_primitives [ p ] = min_value;
            }
            else if ( primitives [ p ] > (double) Float.MAX_VALUE )
            {
                float_primitives [ p ] = max_value;
            }
            else
            {
                float_primitives [ p ] = (float) primitives [ p ];
            }
        }

        return float_primitives;
    }


    /**
     * <p>
     * Converts the specified array from doubles to ints.
     * </p>
     *
     * <p>
     * Causes rounding.  The decimal portion of each double element
     * is truncated.
     * </p>
     *
     * @param min_value The value to use for any element that is
     *                  less than <code> Integer.MIN_VALUE </code>.
     *
     * @param max_value The value to use for any element that is
     *                  greater than <code> Integer.MAX_VALUE </code>.
     *
     * @param primitives The array of doubles to convert.
     *                   If null, then null is returned.
     *                   DO NOT PASS NULL.
     *
     * @return The newly created array of ints, or null if
     *         the specified primitives array was null.
     */
    public static final int [] doublesToInts (
            int min_value,
            int max_value,
            double ... primitives
            )
    {
        if ( primitives == null )
        {
            return null;
        }

        final int [] int_primitives = new int [ primitives.length ];
        for ( int p = 0; p < primitives.length; p ++ )
        {
            if ( primitives [ p ] < (double) Integer.MIN_VALUE )
            {
                int_primitives [ p ] = min_value;
            }
            else if ( primitives [ p ] > (double) Integer.MAX_VALUE )
            {
                int_primitives [ p ] = max_value;
            }
            else
            {
                int_primitives [ p ] = (int) primitives [ p ];
            }
        }

        return int_primitives;
    }


    /**
     * <p>
     * Converts the specified array from doubles to longs.
     * </p>
     *
     * <p>
     * Causes rounding.  The decimal portion of each double element
     * is truncated.
     * </p>
     *
     * @param min_value The value to use for any element that is
     *                  less than <code> Long.MIN_VALUE </code>.
     *
     * @param max_value The value to use for any element that is
     *                  greater than <code> Long.MAX_VALUE </code>.
     *
     * @param primitives The array of doubles to convert.
     *                   If null, then null is returned.
     *                   DO NOT PASS NULL.
     *
     * @return The newly created array of longs, or null if
     *         the specified primitives array was null.
     */
    public static final long [] doublesToLongs (
            long min_value,
            long max_value,
            double ... primitives
            )
    {
        if ( primitives == null )
        {
            return null;
        }

        final long [] long_primitives = new long [ primitives.length ];
        for ( int p = 0; p < primitives.length; p ++ )
        {
            if ( primitives [ p ] < (double) Long.MIN_VALUE )
            {
                long_primitives [ p ] = min_value;
            }
            else if ( primitives [ p ] > (double) Long.MAX_VALUE )
            {
                long_primitives [ p ] = max_value;
            }
            else
            {
                long_primitives [ p ] = (long) primitives [ p ];
            }
        }

        return long_primitives;
    }


    /**
     * <p>
     * Converts the specified array from doubles to shorts.
     * </p>
     *
     * <p>
     * Causes rounding.  The decimal portion of each double element
     * is truncated.
     * </p>
     *
     * @param min_value The value to use for any element that is
     *                  less than <code> Short.MIN_VALUE </code>.
     *
     * @param max_value The value to use for any element that is
     *                  greater than <code> Short.MAX_VALUE </code>.
     *
     * @param primitives The array of doubles to convert.
     *                   If null, then null is returned.
     *                   DO NOT PASS NULL.
     *
     * @return The newly created array of shorts, or null if
     *         the specified primitives array was null.
     */
    public static final short [] doublesToShorts (
            short min_value,
            short max_value,
            double ... primitives
            )
    {
        if ( primitives == null )
        {
            return null;
        }

        final short [] short_primitives = new short [ primitives.length ];
        for ( int p = 0; p < primitives.length; p ++ )
        {
            if ( primitives [ p ] < (double) Short.MIN_VALUE )
            {
                short_primitives [ p ] = min_value;
            }
            else if ( primitives [ p ] > (double) Short.MAX_VALUE )
            {
                short_primitives [ p ] = max_value;
            }
            else
            {
                short_primitives [ p ] = (short) primitives [ p ];
            }
        }

        return short_primitives;
    }


    /**
     * <p>
     * Converts the specified array from floats to bytes.
     * </p>
     *
     * <p>
     * Causes rounding.  The decimal portion of each float element
     * is truncated.
     * </p>
     *
     * @param min_value The value to use for any element that is
     *                  less than <code> Byte.MIN_VALUE </code>.
     *
     * @param max_value The value to use for any element that is
     *                  greater than <code> Byte.MAX_VALUE </code>.
     *
     * @param primitives The array of floats to convert.
     *                   If null, then null is returned.
     *                   DO NOT PASS NULL.
     *
     * @return The newly created array of bytes, or null if
     *         the specified primitives array was null.
     */
    public static final byte [] floatsToBytes (
            byte min_value,
            byte max_value,
            float ... primitives
            )
    {
        if ( primitives == null )
        {
            return null;
        }

        final byte [] byte_primitives = new byte [ primitives.length ];
        for ( int p = 0; p < primitives.length; p ++ )
        {
            if ( primitives [ p ] < (float) Byte.MIN_VALUE )
            {
                byte_primitives [ p ] = min_value;
            }
            else if ( primitives [ p ] > (float) Byte.MAX_VALUE )
            {
                byte_primitives [ p ] = max_value;
            }
            else
            {
                byte_primitives [ p ] = (byte) primitives [ p ];
            }
        }

        return byte_primitives;
    }


    /**
     * <p>
     * Converts the specified array from floats to doubles.
     * </p>
     *
     * @param primitives The array of floats to convert.
     *                   If null, then null is returned.
     *                   DO NOT PASS NULL.
     *
     * @return The newly created array of doubles, or null if
     *         the specified primitives array was null.
     */
    public static final double [] floatsToDoubles (
            float ... primitives
            )
    {
        if ( primitives == null )
        {
            return null;
        }

        final double [] double_primitives = new double [ primitives.length ];
        for ( int p = 0; p < primitives.length; p ++ )
        {
            double_primitives [ p ] = (double) primitives [ p ];
        }

        return double_primitives;
    }


    /**
     * <p>
     * Converts the specified array from floats to ints.
     * </p>
     *
     * <p>
     * Causes rounding.  The decimal portion of each float element
     * is truncated.
     * </p>
     *
     * @param min_value The value to use for any element that is
     *                  less than <code> Integer.MIN_VALUE </code>.
     *
     * @param max_value The value to use for any element that is
     *                  greater than <code> Integer.MAX_VALUE </code>.
     *
     * @param primitives The array of floats to convert.
     *                   If null, then null is returned.
     *                   DO NOT PASS NULL.
     *
     * @return The newly created array of ints, or null if
     *         the specified primitives array was null.
     */
    public static final int [] floatsToInts (
            int min_value,
            int max_value,
            float ... primitives
            )
    {
        if ( primitives == null )
        {
            return null;
        }

        final int [] int_primitives = new int [ primitives.length ];
        for ( int p = 0; p < primitives.length; p ++ )
        {
            if ( primitives [ p ] < (float) Integer.MIN_VALUE )
            {
                int_primitives [ p ] = min_value;
            }
            else if ( primitives [ p ] > (float) Integer.MAX_VALUE )
            {
                int_primitives [ p ] = max_value;
            }
            else
            {
                int_primitives [ p ] = (int) primitives [ p ];
            }
        }

        return int_primitives;
    }


    /**
     * <p>
     * Converts the specified array from floats to longs.
     * </p>
     *
     * <p>
     * Causes rounding.  The decimal portion of each float element
     * is truncated.
     * </p>
     *
     * @param min_value The value to use for any element that is
     *                  less than <code> Long.MIN_VALUE </code>.
     *
     * @param max_value The value to use for any element that is
     *                  greater than <code> Long.MAX_VALUE </code>.
     *
     * @param primitives The array of floats to convert.
     *                   If null, then null is returned.
     *                   DO NOT PASS NULL.
     *
     * @return The newly created array of longs, or null if
     *         the specified primitives array was null.
     */
    public static final long [] floatsToLongs (
            long min_value,
            long max_value,
            float ... primitives
            )
    {
        if ( primitives == null )
        {
            return null;
        }

        final long [] long_primitives = new long [ primitives.length ];
        for ( int p = 0; p < primitives.length; p ++ )
        {
            if ( primitives [ p ] < (float) Long.MIN_VALUE )
            {
                long_primitives [ p ] = min_value;
            }
            else if ( primitives [ p ] > (float) Long.MAX_VALUE )
            {
                long_primitives [ p ] = max_value;
            }
            else
            {
                long_primitives [ p ] = (long) primitives [ p ];
            }
        }

        return long_primitives;
    }


    /**
     * <p>
     * Converts the specified array from floats to shorts.
     * </p>
     *
     * <p>
     * Causes rounding.  The decimal portion of each float element
     * is truncated.
     * </p>
     *
     * @param min_value The value to use for any element that is
     *                  less than <code> Short.MIN_VALUE </code>.
     *
     * @param max_value The value to use for any element that is
     *                  greater than <code> Short.MAX_VALUE </code>.
     *
     * @param primitives The array of floats to convert.
     *                   If null, then null is returned.
     *                   DO NOT PASS NULL.
     *
     * @return The newly created array of shorts, or null if
     *         the specified primitives array was null.
     */
    public static final short [] floatsToShorts (
            short min_value,
            short max_value,
            float ... primitives
            )
    {
        if ( primitives == null )
        {
            return null;
        }

        final short [] short_primitives = new short [ primitives.length ];
        for ( int p = 0; p < primitives.length; p ++ )
        {
            if ( primitives [ p ] < (float) Short.MIN_VALUE )
            {
                short_primitives [ p ] = min_value;
            }
            else if ( primitives [ p ] > (float) Short.MAX_VALUE )
            {
                short_primitives [ p ] = max_value;
            }
            else
            {
                short_primitives [ p ] = (short) primitives [ p ];
            }
        }

        return short_primitives;
    }


    /**
     * <p>
     * Converts the specified array from ints to bytes.
     * </p>
     *
     * @param min_value The value to use for any element that is
     *                  less than <code> Byte.MIN_VALUE </code>.
     *
     * @param max_value The value to use for any element that is
     *                  greater than <code> Byte.MAX_VALUE </code>.
     *
     * @param primitives The array of ints to convert.
     *                   If null, then null is returned.
     *                   DO NOT PASS NULL.
     *
     * @return The newly created array of bytes, or null if
     *         the specified primitives array was null.
     */
    public static final byte [] intsToBytes (
            byte min_value,
            byte max_value,
            int ... primitives
            )
    {
        if ( primitives == null )
        {
            return null;
        }

        final byte [] byte_primitives = new byte [ primitives.length ];
        for ( int p = 0; p < primitives.length; p ++ )
        {
            if ( primitives [ p ] < (int) Byte.MIN_VALUE )
            {
                byte_primitives [ p ] = min_value;
            }
            else if ( primitives [ p ] > (int) Byte.MAX_VALUE )
            {
                byte_primitives [ p ] = max_value;
            }
            else
            {
                byte_primitives [ p ] = (byte) primitives [ p ];
            }
        }

        return byte_primitives;
    }


    /**
     * <p>
     * Converts the specified array from ints to doubles.
     * </p>
     *
     * <p>
     * Can cause rounding, since there are ints that are not
     * representable as doubles.
     * </p>
     *
     * @param primitives The array of ints to convert.
     *                   If null, then null is returned.
     *                   DO NOT PASS NULL.
     *
     * @return The newly created array of doubles, or null if
     *         the specified primitives array was null.
     */
    public static final double [] intsToDoubles (
            int ... primitives
            )
    {
        if ( primitives == null )
        {
            return null;
        }

        final double [] double_primitives = new double [ primitives.length ];
        for ( int p = 0; p < primitives.length; p ++ )
        {
            double_primitives [ p ] = (double) primitives [ p ];
        }

        return double_primitives;
    }


    /**
     * <p>
     * Converts the specified array from ints to floats.
     * </p>
     *
     * <p>
     * Can cause rounding, since there are ints that are not
     * representable as floats.
     * </p>
     *
     * @param primitives The array of ints to convert.
     *                   If null, then null is returned.
     *                   DO NOT PASS NULL.
     *
     * @return The newly created array of floats, or null if
     *         the specified primitives array was null.
     */
    public static final float [] intsToFloats (
            int ... primitives
            )
    {
        if ( primitives == null )
        {
            return null;
        }

        final float [] float_primitives = new float [ primitives.length ];
        for ( int p = 0; p < primitives.length; p ++ )
        {
            float_primitives [ p ] = (float) primitives [ p ];
        }

        return float_primitives;
    }


    /**
     * <p>
     * Converts the specified array from ints to longs.
     * </p>
     *
     * @param primitives The array of ints to convert.
     *                   If null, then null is returned.
     *                   DO NOT PASS NULL.
     *
     * @return The newly created array of longs, or null if
     *         the specified primitives array was null.
     */
    public static final long [] intsToLongs (
            int ... primitives
            )
    {
        if ( primitives == null )
        {
            return null;
        }

        final long [] long_primitives = new long [ primitives.length ];
        for ( int p = 0; p < primitives.length; p ++ )
        {
            long_primitives [ p ] = (long) primitives [ p ];
        }

        return long_primitives;
    }


    /**
     * <p>
     * Converts the specified array from ints to shorts.
     * </p>
     *
     * @param min_value The value to use for any element that is
     *                  less than <code> Short.MIN_VALUE </code>.
     *
     * @param max_value The value to use for any element that is
     *                  greater than <code> Short.MAX_VALUE </code>.
     *
     * @param primitives The array of ints to convert.
     *                   If null, then null is returned.
     *                   DO NOT PASS NULL.
     *
     * @return The newly created array of shorts, or null if
     *         the specified primitives array was null.
     */
    public static final short [] intsToShorts (
            short min_value,
            short max_value,
            int ... primitives
            )
    {
        if ( primitives == null )
        {
            return null;
        }

        final short [] short_primitives = new short [ primitives.length ];
        for ( int p = 0; p < primitives.length; p ++ )
        {
            if ( primitives [ p ] < (int) Short.MIN_VALUE )
            {
                short_primitives [ p ] = min_value;
            }
            else if ( primitives [ p ] > (int) Short.MAX_VALUE )
            {
                short_primitives [ p ] = max_value;
            }
            else
            {
                short_primitives [ p ] = (short) primitives [ p ];
            }
        }

        return short_primitives;
    }


    /**
     * <p>
     * Converts the specified array from longs to bytes.
     * </p>
     *
     * @param min_value The value to use for any element that is
     *                  less than <code> Byte.MIN_VALUE </code>.
     *
     * @param max_value The value to use for any element that is
     *                  greater than <code> Byte.MAX_VALUE </code>.
     *
     * @param primitives The array of longs to convert.
     *                   If null, then null is returned.
     *                   DO NOT PASS NULL.
     *
     * @return The newly created array of bytes, or null if
     *         the specified primitives array was null.
     */
    public static final byte [] longsToBytes (
            byte min_value,
            byte max_value,
            long ... primitives
            )
    {
        if ( primitives == null )
        {
            return null;
        }

        final byte [] byte_primitives = new byte [ primitives.length ];
        for ( int p = 0; p < primitives.length; p ++ )
        {
            if ( primitives [ p ] < (long) Byte.MIN_VALUE )
            {
                byte_primitives [ p ] = min_value;
            }
            else if ( primitives [ p ] > (long) Byte.MAX_VALUE )
            {
                byte_primitives [ p ] = max_value;
            }
            else
            {
                byte_primitives [ p ] = (byte) primitives [ p ];
            }
        }

        return byte_primitives;
    }


    /**
     * <p>
     * Converts the specified array from longs to doubles.
     * </p>
     *
     * <p>
     * Can cause rounding, since there are longs that are not
     * representable as doubles.
     * </p>
     *
     * @param primitives The array of longs to convert.
     *                   If null, then null is returned.
     *                   DO NOT PASS NULL.
     *
     * @return The newly created array of doubles, or null if
     *         the specified primitives array was null.
     */
    public static final double [] longsToDoubles (
            long ... primitives
            )
    {
        if ( primitives == null )
        {
            return null;
        }

        final double [] double_primitives = new double [ primitives.length ];
        for ( int p = 0; p < primitives.length; p ++ )
        {
            double_primitives [ p ] = (double) primitives [ p ];
        }

        return double_primitives;
    }


    /**
     * <p>
     * Converts the specified array from longs to floats.
     * </p>
     *
     * <p>
     * Can cause rounding, since there are longs that are not
     * representable as floats.
     * </p>
     *
     * @param primitives The array of longs to convert.
     *                   If null, then null is returned.
     *                   DO NOT PASS NULL.
     *
     * @return The newly created array of floats, or null if
     *         the specified primitives array was null.
     */
    public static final float [] longsToFloats (
            long ... primitives
            )
    {
        if ( primitives == null )
        {
            return null;
        }

        final float [] float_primitives = new float [ primitives.length ];
        for ( int p = 0; p < primitives.length; p ++ )
        {
            float_primitives [ p ] = (float) primitives [ p ];
        }

        return float_primitives;
    }


    /**
     * <p>
     * Converts the specified array from longs to ints.
     * </p>
     *
     * @param min_value The value to use for any element that is
     *                  less than <code> Integer.MIN_VALUE </code>.
     *
     * @param max_value The value to use for any element that is
     *                  greater than <code> Integer.MAX_VALUE </code>.
     *
     * @param primitives The array of longs to convert.
     *                   If null, then null is returned.
     *                   DO NOT PASS NULL.
     *
     * @return The newly created array of ints, or null if
     *         the specified primitives array was null.
     */
    public static final int [] longsToInts (
            int min_value,
            int max_value,
            long ... primitives
            )
    {
        if ( primitives == null )
        {
            return null;
        }

        final int [] int_primitives = new int [ primitives.length ];
        for ( int p = 0; p < primitives.length; p ++ )
        {
            if ( primitives [ p ] < (long) Integer.MIN_VALUE )
            {
                int_primitives [ p ] = min_value;
            }
            else if ( primitives [ p ] > (long) Integer.MAX_VALUE )
            {
                int_primitives [ p ] = max_value;
            }
            else
            {
                int_primitives [ p ] = (int) primitives [ p ];
            }
        }

        return int_primitives;
    }


    /**
     * <p>
     * Converts the specified array from longs to shorts.
     * </p>
     *
     * @param min_value The value to use for any element that is
     *                  less than <code> Short.MIN_VALUE </code>.
     *
     * @param max_value The value to use for any element that is
     *                  greater than <code> Short.MAX_VALUE </code>.
     *
     * @param primitives The array of longs to convert.
     *                   If null, then null is returned.
     *                   DO NOT PASS NULL.
     *
     * @return The newly created array of shorts, or null if
     *         the specified primitives array was null.
     */
    public static final short [] longsToShorts (
            short min_value,
            short max_value,
            long ... primitives
            )
    {
        if ( primitives == null )
        {
            return null;
        }

        final short [] short_primitives = new short [ primitives.length ];
        for ( int p = 0; p < primitives.length; p ++ )
        {
            if ( primitives [ p ] < (long) Short.MIN_VALUE )
            {
                short_primitives [ p ] = min_value;
            }
            else if ( primitives [ p ] > (long) Short.MAX_VALUE )
            {
                short_primitives [ p ] = max_value;
            }
            else
            {
                short_primitives [ p ] = (short) primitives [ p ];
            }
        }

        return short_primitives;
    }


    /**
     * <p>
     * Converts the specified array from shorts to bytes.
     * </p>
     *
     * @param min_value The value to use for any element that is
     *                  less than <code> Byte.MIN_VALUE </code>.
     *
     * @param max_value The value to use for any element that is
     *                  greater than <code> Byte.MAX_VALUE </code>.
     *
     * @param primitives The array of shorts to convert.
     *                   If null, then null is returned.
     *                   DO NOT PASS NULL.
     *
     * @return The newly created array of bytes, or null if
     *         the specified primitives array was null.
     */
    public static final byte [] shortsToBytes (
            byte min_value,
            byte max_value,
            short ... primitives
            )
    {
        if ( primitives == null )
        {
            return null;
        }

        final byte [] byte_primitives = new byte [ primitives.length ];
        for ( int p = 0; p < primitives.length; p ++ )
        {
            if ( primitives [ p ] < (short) Byte.MIN_VALUE )
            {
                byte_primitives [ p ] = min_value;
            }
            else if ( primitives [ p ] > (short) Byte.MAX_VALUE )
            {
                byte_primitives [ p ] = max_value;
            }
            else
            {
                byte_primitives [ p ] = (byte) primitives [ p ];
            }
        }

        return byte_primitives;
    }


    /**
     * <p>
     * Converts the specified array from shorts to doubles.
     * </p>
     *
     * <p>
     * Can cause rounding, since there are shorts that are not
     * representable as doubles.
     * </p>
     *
     * @param primitives The array of shorts to convert.
     *                   If null, then null is returned.
     *                   DO NOT PASS NULL.
     *
     * @return The newly created array of doubles, or null if
     *         the specified primitives array was null.
     */
    public static final double [] shortsToDoubles (
            short ... primitives
            )
    {
        if ( primitives == null )
        {
            return null;
        }

        final double [] double_primitives = new double [ primitives.length ];
        for ( int p = 0; p < primitives.length; p ++ )
        {
            double_primitives [ p ] = (double) primitives [ p ];
        }

        return double_primitives;
    }


    /**
     * <p>
     * Converts the specified array from shorts to floats.
     * </p>
     *
     * <p>
     * Can cause rounding, since there are shorts that are not
     * representable as floats.
     * </p>
     *
     * @param primitives The array of shorts to convert.
     *                   If null, then null is returned.
     *                   DO NOT PASS NULL.
     *
     * @return The newly created array of floats, or null if
     *         the specified primitives array was null.
     */
    public static final float [] shortsToFloats (
            short ... primitives
            )
    {
        if ( primitives == null )
        {
            return null;
        }

        final float [] float_primitives = new float [ primitives.length ];
        for ( int p = 0; p < primitives.length; p ++ )
        {
            float_primitives [ p ] = (float) primitives [ p ];
        }

        return float_primitives;
    }


    /**
     * <p>
     * Converts the specified array from shorts to ints.
     * </p>
     *
     * @param primitives The array of shorts to convert.
     *                   If null, then null is returned.
     *                   DO NOT PASS NULL.
     *
     * @return The newly created array of ints, or null if
     *         the specified primitives array was null.
     */
    public static final int [] shortsToInts (
            short ... primitives
            )
    {
        if ( primitives == null )
        {
            return null;
        }

        final int [] int_primitives = new int [ primitives.length ];
        for ( int p = 0; p < primitives.length; p ++ )
        {
            int_primitives [ p ] = (int) primitives [ p ];
        }

        return int_primitives;
    }


    /**
     * <p>
     * Converts the specified array from shorts to longs.
     * </p>
     *
     * @param primitives The array of shorts to convert.
     *                   If null, then null is returned.
     *                   DO NOT PASS NULL.
     *
     * @return The newly created array of longs, or null if
     *         the specified primitives array was null.
     */
    public static final long [] shortsToLongs (
            short ... primitives
            )
    {
        if ( primitives == null )
        {
            return null;
        }

        final long [] long_primitives = new long [ primitives.length ];
        for ( int p = 0; p < primitives.length; p ++ )
        {
            long_primitives [ p ] = (long) primitives [ p ];
        }

        return long_primitives;
    }
}
