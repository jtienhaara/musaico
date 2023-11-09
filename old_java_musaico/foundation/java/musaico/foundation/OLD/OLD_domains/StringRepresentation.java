package musaico.foundation.domains;

import java.io.Serializable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;


/**
 * <p>
 * Converts single objects, arrays and Iterables to Strings for printing debug
 * information and so on.
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
 * @see musaico.foundation.domains.MODULE#COPYRIGHT
 * @see musaico.foundation.domains.MODULE#LICENSE
 */
public class StringRepresentation
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Default max length for StringRepresentation.of ( object ),
     *  after which the value's String representation is reduced
     *  in length by hollowing out the middle of the String.
     *  For example, if the max length is 8, then the value
     *  9876543210 would be reduced to "987...10" as a String. */
    public static final int DEFAULT_OBJECT_LENGTH = 20;


    /** Default max length for StringRepresentation.of ( array )
     *  or StringRepresentation.of ( iterable ),
     *  after which the value's String representation is reduced
     *  in length by leaving off the last elements of the array.
     *  For example, if the max length is 16, then the value
     *  9876543210, 12345 would be reduced to "{ 9876543210, ...[1 more]... }"
     *  as a String. */
    public static final int DEFAULT_ARRAY_LENGTH = 40;


    // Rounds to 0 decimal places, for BigDecimal division for time.
    private static final MathContext INTEGER_ROUNDING =
        new MathContext ( 0, RoundingMode.HALF_UP );


    /**
     * <p>
     * Creates a String representation of the specified Object.
     * Can be overridden by derived classes to avoid onerous
     * String conversions of big data.
     * </p>
     *
     * @param element The Object to convert into a human-readable String.
     *                Must not be null.  Must not contain any
     *                null elements.
     *
     * @param max_length The maximum length of the Object's String
     *                   representation, beyond which a "..." type String
     *                   will be placed in the middle instead of text.
     *                   Can be 0 or less for no maximum.
     *
     * @return The String representation of the specified elements.
     *         Never null.
     */
    public static final String of (
            Object object,
            int max_length
            )
    {
        if ( object == null )
        {
            return "null";
        }
        else if ( object instanceof Iterable )
        {
            final Iterable<?> iterable = (Iterable<?>) object;
            return StringRepresentation.of ( iterable,
                                             max_length );
        }
        else if ( object instanceof BigDecimal [] )
        {
            final BigDecimal [] array = (BigDecimal []) object;
            return StringRepresentation.of ( array,
                                             max_length );
        }
        else if ( object instanceof BigInteger [] )
        {
            final BigInteger [] array = (BigInteger []) object;
            return StringRepresentation.of ( array,
                                             max_length );
        }
        else if ( object instanceof byte [] )
        {
            final byte [] array = (byte []) object;
            return StringRepresentation.of ( array,
                                             max_length );
        }
        else if ( object instanceof char [] )
        {
            final char [] array = (char []) object;
            return StringRepresentation.of ( array,
                                             max_length );
        }
        else if ( object instanceof double [] )
        {
            final double [] array = (double []) object;
            return StringRepresentation.of ( array,
                                             max_length );
        }
        else if ( object instanceof float [] )
        {
            final float [] array = (float []) object;
            return StringRepresentation.of ( array,
                                             max_length );
        }
        else if ( object instanceof int [] )
        {
            final int [] array = (int []) object;
            return StringRepresentation.of ( array,
                                             max_length );
        }
        else if ( object instanceof long[] )
        {
            final long [] array = (long []) object;
            return StringRepresentation.of ( array,
                                             max_length );
        }
        else if ( object instanceof short [] )
        {
            final short [] array = (short []) object;
            return StringRepresentation.of ( array,
                                             max_length );
        }
        else if ( object.getClass ().isArray () )
        {
            final Object [] array = (Object []) object;
            return StringRepresentation.of ( array,
                                             max_length );
        }

        String object_as_string = "" + object;
        final int length = object_as_string.length ();

        if ( object instanceof CharSequence )
        {
            max_length -= 2;
        }
        else if ( object instanceof Character )
        {
            max_length -= 2;
        }
        else
        {
            max_length -= 2;
        }

        if ( max_length > 0
             && length > max_length )
        {
            final int first_half = ( max_length + 1 ) / 2;
            final int second_half = max_length - first_half - 3;
            object_as_string =
                object_as_string.substring ( 0,
                                             first_half )
                + "..."
                + object_as_string.substring ( length - second_half,
                                               length );
        }

        if ( object instanceof CharSequence )
        {
            object_as_string = "\"" + object_as_string + "\"";
        }
        else if ( object instanceof Character )
        {
            object_as_string = "'" + object_as_string + "'";
        }

        return object_as_string;
    }


    /**
     * <p>
     * Converts the specified iterable elements into human-readable text.
     * </p>
     *
     * @param elements The value(s) to convert into a human-readable String.
     *                 Must not be null.  Must not contain any
     *                 null elements.
     *
     * @param max_total_length The maximum total String length of
     *                         all the elements with ", " separators,
     *                         after which a "..." type String will be
     *                         appended in place of further elements.
     *                         Can be 0 or less for no maximum.
     *
     * @return The String representation of the specified elements.
     *         Never null.
     */
    public static final <ELEMENT extends Object>
            String of (
                       Iterable<ELEMENT> elements,
                       int max_total_length
                       )
    {
        if ( elements == null )
        {
            return "null";
        }

        int max_remaining_length = max_total_length;

        final StringBuilder sbuf = new StringBuilder ();
        boolean is_first = true;
        sbuf.append ( "{" );
        int num_unprinted_items = 0;
        for ( ELEMENT element : elements ) // Don't bother preventing infinity.
        {
            if ( num_unprinted_items > 0 )
            {
                num_unprinted_items ++;
                continue;
            }

            final int separator_length;
            if ( is_first )
            {
                is_first = false;
                separator_length = 1; // 1 for space below.
            }
            else
            {
                sbuf.append ( "," );
                separator_length = 2; // 1 for comma above, 1 for space below.
            }

            if ( max_remaining_length > 0 )
            {
                if ( max_remaining_length <= separator_length )
                {
                    num_unprinted_items ++;
                    continue;
                }

                max_remaining_length -= separator_length;
            }

            final int max_length;
            if ( max_remaining_length > StringRepresentation.DEFAULT_OBJECT_LENGTH )
            {
                max_length = StringRepresentation.DEFAULT_OBJECT_LENGTH;
            }
            else
            {
                max_length = max_remaining_length;
            }

            final String element_as_string =
                StringRepresentation.of ( element, max_length );

            final int length = element_as_string.length ();
            if ( max_remaining_length > 0
                 && max_remaining_length < length )
            {
                num_unprinted_items = 1;
                continue;
            }

            max_remaining_length -= length;

            sbuf.append ( " " + element_as_string );
        }

        if ( ! is_first )
        {
            if ( num_unprinted_items > 0 )
            {
                sbuf.append ( "...[" + num_unprinted_items + " more]..." );
            }

            sbuf.append ( " " );
        }

        sbuf.append ( "}" );

        return sbuf.toString ();
    }


    /**
     * <p>
     * Converts the specified array of elements into human-readable text.
     * </p>
     *
     * @param elements The value(s) to convert into a human-readable String.
     *                 Must not be null.
     *
     * @param max_total_length The maximum total String length of
     *                         all the elements with ", " separators,
     *                         after which a "..." type String will be
     *                         appended in place of further elements.
     *                         Can be 0 or less for no maximum.
     *
     * @return The String representation of the specified elements.
     *         Never null.
     */
    public static final <ELEMENT extends Object>
            String of (
                       ELEMENT [] elements,
                       int max_total_length
                       )
    {
        if ( elements == null )
        {
            return "null";
        }

        int max_remaining_length = max_total_length;

        final StringBuilder sbuf = new StringBuilder ();
        boolean is_first = true;
        sbuf.append ( "{" );
        int num_unprinted_items = 0;
        for ( ELEMENT element : elements )
        {
            if ( num_unprinted_items > 0 )
            {
                num_unprinted_items ++;
                continue;
            }

            final int separator_length;
            if ( is_first )
            {
                is_first = false;
                separator_length = 1; // 1 for space below.
            }
            else
            {
                sbuf.append ( "," );
                separator_length = 2; // 1 for comma above, 1 for space below.
            }

            if ( max_remaining_length > 0 )
            {
                if ( max_remaining_length <= separator_length )
                {
                    num_unprinted_items ++;
                    continue;
                }

                max_remaining_length -= separator_length;
            }

            final int max_length;
            if ( max_remaining_length > StringRepresentation.DEFAULT_OBJECT_LENGTH )
            {
                max_length = StringRepresentation.DEFAULT_OBJECT_LENGTH;
            }
            else
            {
                max_length = max_remaining_length;
            }

            final String element_as_string =
                StringRepresentation.of ( element,
                                          max_length );

            final int length = element_as_string.length ();
            if ( max_remaining_length > 0
                 && max_remaining_length < length )
            {
                num_unprinted_items = 1;
                continue;
            }

            max_remaining_length -= length;

            sbuf.append ( " " + element_as_string );
        }

        if ( ! is_first )
        {
            if ( num_unprinted_items > 0 )
            {
                sbuf.append ( "...[" + num_unprinted_items + " more]..." );
            }

            sbuf.append ( " " );
        }

        sbuf.append ( "}" );

        return sbuf.toString ();
    }


    /**
     * <p>
     * Converts the specified array of elements into human-readable text.
     * </p>
     *
     * @param elements The value(s) to convert into a human-readable String.
     *                 Must not be null.
     *
     * @param max_total_length The maximum total String length of
     *                         all the elements with ", " separators,
     *                         after which a "..." type String will be
     *                         appended in place of further elements.
     *                         Can be 0 or less for no maximum.
     *
     * @return The String representation of the specified elements.
     *         Never null.
     */
    public static final String of (
            boolean [] elements,
            int max_total_length
            )
    {
        if ( elements == null )
        {
            return "null";
        }

        int max_remaining_length = max_total_length;

        final StringBuilder sbuf = new StringBuilder ();
        boolean is_first = true;
        sbuf.append ( "{" );
        int num_unprinted_items = 0;
        for ( boolean element : elements )
        {
            if ( num_unprinted_items > 0 )
            {
                num_unprinted_items ++;
                continue;
            }

            final int separator_length;
            if ( is_first )
            {
                is_first = false;
                separator_length = 1; // 1 for space below.
            }
            else
            {
                sbuf.append ( "," );
                separator_length = 2; // 1 for comma above, 1 for space below.
            }

            if ( max_remaining_length > 0 )
            {
                if ( max_remaining_length <= separator_length )
                {
                    num_unprinted_items ++;
                    continue;
                }

                max_remaining_length -= separator_length;
            }

            final int max_length;
            if ( max_remaining_length > StringRepresentation.DEFAULT_OBJECT_LENGTH )
            {
                max_length = StringRepresentation.DEFAULT_OBJECT_LENGTH;
            }
            else
            {
                max_length = max_remaining_length;
            }

            final String element_as_string =
                StringRepresentation.of ( new Boolean ( element ),
                                          max_length );

            final int length = element_as_string.length ();
            if ( max_remaining_length > 0
                 && max_remaining_length < length )
            {
                num_unprinted_items = 1;
                continue;
            }

            max_remaining_length -= length;

            sbuf.append ( " " + element_as_string );
        }

        if ( ! is_first )
        {
            if ( num_unprinted_items > 0 )
            {
                sbuf.append ( "...[" + num_unprinted_items + " more]..." );
            }

            sbuf.append ( " " );
        }

        sbuf.append ( "}" );

        return sbuf.toString ();
    }


    /**
     * <p>
     * Converts the specified array of elements into human-readable text.
     * </p>
     *
     * @param elements The value(s) to convert into a human-readable String.
     *                 Must not be null.
     *
     * @param max_total_length The maximum total String length of
     *                         all the elements with ", " separators,
     *                         after which a "..." type String will be
     *                         appended in place of further elements.
     *                         Can be 0 or less for no maximum.
     *
     * @return The String representation of the specified elements.
     *         Never null.
     */
    public static final String of (
            byte [] elements,
            int max_total_length
            )
    {
        if ( elements == null )
        {
            return "null";
        }

        int max_remaining_length = max_total_length;

        final StringBuilder sbuf = new StringBuilder ();
        boolean is_first = true;
        sbuf.append ( "{" );
        int num_unprinted_items = 0;
        for ( byte element : elements )
        {
            if ( num_unprinted_items > 0 )
            {
                num_unprinted_items ++;
                continue;
            }

            final int separator_length;
            if ( is_first )
            {
                is_first = false;
                separator_length = 1; // 1 for space below.
            }
            else
            {
                sbuf.append ( "," );
                separator_length = 2; // 1 for comma above, 1 for space below.
            }

            if ( max_remaining_length > 0 )
            {
                if ( max_remaining_length <= separator_length )
                {
                    num_unprinted_items ++;
                    continue;
                }

                max_remaining_length -= separator_length;
            }

            final int max_length;
            if ( max_remaining_length > StringRepresentation.DEFAULT_OBJECT_LENGTH )
            {
                max_length = StringRepresentation.DEFAULT_OBJECT_LENGTH;
            }
            else
            {
                max_length = max_remaining_length;
            }

            final String element_as_string =
                StringRepresentation.of ( new Byte ( element ),
                                          max_length );

            final int length = element_as_string.length ();
            if ( max_remaining_length > 0
                 && max_remaining_length < length )
            {
                num_unprinted_items = 1;
                continue;
            }

            max_remaining_length -= length;

            sbuf.append ( " " + element_as_string );
        }

        if ( ! is_first )
        {
            if ( num_unprinted_items > 0 )
            {
                sbuf.append ( "...[" + num_unprinted_items + " more]..." );
            }

            sbuf.append ( " " );
        }

        sbuf.append ( "}" );

        return sbuf.toString ();
    }


    /**
     * <p>
     * Converts the specified array of elements into human-readable text.
     * </p>
     *
     * @param elements The value(s) to convert into a human-readable String.
     *                 Must not be null.
     *
     * @param max_total_length The maximum total String length of
     *                         all the elements with ", " separators,
     *                         after which a "..." type String will be
     *                         appended in place of further elements.
     *                         Can be 0 or less for no maximum.
     *
     * @return The String representation of the specified elements.
     *         Never null.
     */
    public static final String of (
            char [] elements,
            int max_total_length
            )
    {
        if ( elements == null )
        {
            return "null";
        }

        int max_remaining_length = max_total_length;

        final StringBuilder sbuf = new StringBuilder ();
        boolean is_first = true;
        sbuf.append ( "{" );
        int num_unprinted_items = 0;
        for ( char element : elements )
        {
            if ( num_unprinted_items > 0 )
            {
                num_unprinted_items ++;
                continue;
            }

            final int separator_length;
            if ( is_first )
            {
                is_first = false;
                separator_length = 1; // 1 for space below.
            }
            else
            {
                sbuf.append ( "," );
                separator_length = 2; // 1 for comma above, 1 for space below.
            }

            if ( max_remaining_length > 0 )
            {
                if ( max_remaining_length <= separator_length )
                {
                    num_unprinted_items ++;
                    continue;
                }

                max_remaining_length -= separator_length;
            }

            final int max_length;
            if ( max_remaining_length > StringRepresentation.DEFAULT_OBJECT_LENGTH )
            {
                max_length = StringRepresentation.DEFAULT_OBJECT_LENGTH;
            }
            else
            {
                max_length = max_remaining_length;
            }

            final String element_as_string =
                StringRepresentation.of ( new Character ( element ),
                                          max_length );

            final int length = element_as_string.length ();
            if ( max_remaining_length > 0
                 && max_remaining_length < length )
            {
                num_unprinted_items = 1;
                continue;
            }

            max_remaining_length -= length;

            sbuf.append ( " " + element_as_string );
        }

        if ( ! is_first )
        {
            if ( num_unprinted_items > 0 )
            {
                sbuf.append ( "...[" + num_unprinted_items + " more]..." );
            }

            sbuf.append ( " " );
        }

        sbuf.append ( "}" );

        return sbuf.toString ();
    }


    /**
     * <p>
     * Converts the specified array of elements into human-readable text.
     * </p>
     *
     * @param elements The value(s) to convert into a human-readable String.
     *                 Must not be null.
     *
     * @param max_total_length The maximum total String length of
     *                         all the elements with ", " separators,
     *                         after which a "..." type String will be
     *                         appended in place of further elements.
     *                         Can be 0 or less for no maximum.
     *
     * @return The String representation of the specified elements.
     *         Never null.
     */
    public static final String of (
            double [] elements,
            int max_total_length
            )
    {
        if ( elements == null )
        {
            return "null";
        }

        int max_remaining_length = max_total_length;

        final StringBuilder sbuf = new StringBuilder ();
        boolean is_first = true;
        sbuf.append ( "{" );
        int num_unprinted_items = 0;
        for ( double element : elements )
        {
            if ( num_unprinted_items > 0 )
            {
                num_unprinted_items ++;
                continue;
            }

            final int separator_length;
            if ( is_first )
            {
                is_first = false;
                separator_length = 1; // 1 for space below.
            }
            else
            {
                sbuf.append ( "," );
                separator_length = 2; // 1 for comma above, 1 for space below.
            }

            if ( max_remaining_length > 0 )
            {
                if ( max_remaining_length <= separator_length )
                {
                    num_unprinted_items ++;
                    continue;
                }

                max_remaining_length -= separator_length;
            }

            final int max_length;
            if ( max_remaining_length > StringRepresentation.DEFAULT_OBJECT_LENGTH )
            {
                max_length = StringRepresentation.DEFAULT_OBJECT_LENGTH;
            }
            else
            {
                max_length = max_remaining_length;
            }

            final String element_as_string =
                StringRepresentation.of ( new Double ( element ),
                                          max_length );

            final int length = element_as_string.length ();
            if ( max_remaining_length > 0
                 && max_remaining_length < length )
            {
                num_unprinted_items = 1;
                continue;
            }

            max_remaining_length -= length;

            sbuf.append ( " " + element_as_string );
        }

        if ( ! is_first )
        {
            if ( num_unprinted_items > 0 )
            {
                sbuf.append ( "...[" + num_unprinted_items + " more]..." );
            }

            sbuf.append ( " " );
        }

        sbuf.append ( "}" );

        return sbuf.toString ();
    }


    /**
     * <p>
     * Converts the specified array of elements into human-readable text.
     * </p>
     *
     * @param elements The value(s) to convert into a human-readable String.
     *                 Must not be null.
     *
     * @param max_total_length The maximum total String length of
     *                         all the elements with ", " separators,
     *                         after which a "..." type String will be
     *                         appended in place of further elements.
     *                         Can be 0 or less for no maximum.
     *
     * @return The String representation of the specified elements.
     *         Never null.
     */
    public static final String of (
            float [] elements,
            int max_total_length
            )
    {
        if ( elements == null )
        {
            return "null";
        }

        int max_remaining_length = max_total_length;

        final StringBuilder sbuf = new StringBuilder ();
        boolean is_first = true;
        sbuf.append ( "{" );
        int num_unprinted_items = 0;
        for ( float element : elements )
        {
            if ( num_unprinted_items > 0 )
            {
                num_unprinted_items ++;
                continue;
            }

            final int separator_length;
            if ( is_first )
            {
                is_first = false;
                separator_length = 1; // 1 for space below.
            }
            else
            {
                sbuf.append ( "," );
                separator_length = 2; // 1 for comma above, 1 for space below.
            }

            if ( max_remaining_length > 0 )
            {
                if ( max_remaining_length <= separator_length )
                {
                    num_unprinted_items ++;
                    continue;
                }

                max_remaining_length -= separator_length;
            }

            final int max_length;
            if ( max_remaining_length > StringRepresentation.DEFAULT_OBJECT_LENGTH )
            {
                max_length = StringRepresentation.DEFAULT_OBJECT_LENGTH;
            }
            else
            {
                max_length = max_remaining_length;
            }

            final String element_as_string =
                StringRepresentation.of ( new Float ( element ),
                                          max_length );

            final int length = element_as_string.length ();
            if ( max_remaining_length > 0
                 && max_remaining_length < length )
            {
                num_unprinted_items = 1;
                continue;
            }

            max_remaining_length -= length;

            sbuf.append ( " " + element_as_string );
        }

        if ( ! is_first )
        {
            if ( num_unprinted_items > 0 )
            {
                sbuf.append ( "...[" + num_unprinted_items + " more]..." );
            }

            sbuf.append ( " " );
        }

        sbuf.append ( "}" );

        return sbuf.toString ();
    }


    /**
     * <p>
     * Converts the specified array of elements into human-readable text.
     * </p>
     *
     * @param elements The value(s) to convert into a human-readable String.
     *                 Must not be null.
     *
     * @param max_total_length The maximum total String length of
     *                         all the elements with ", " separators,
     *                         after which a "..." type String will be
     *                         appended in place of further elements.
     *                         Can be 0 or less for no maximum.
     *
     * @return The String representation of the specified elements.
     *         Never null.
     */
    public static final String of (
            int [] elements,
            int max_total_length
            )
    {
        if ( elements == null )
        {
            return "null";
        }

        int max_remaining_length = max_total_length;

        final StringBuilder sbuf = new StringBuilder ();
        boolean is_first = true;
        sbuf.append ( "{" );
        int num_unprinted_items = 0;
        for ( int element : elements )
        {
            if ( num_unprinted_items > 0 )
            {
                num_unprinted_items ++;
                continue;
            }

            final int separator_length;
            if ( is_first )
            {
                is_first = false;
                separator_length = 1; // 1 for space below.
            }
            else
            {
                sbuf.append ( "," );
                separator_length = 2; // 1 for comma above, 1 for space below.
            }

            if ( max_remaining_length > 0 )
            {
                if ( max_remaining_length <= separator_length )
                {
                    num_unprinted_items ++;
                    continue;
                }

                max_remaining_length -= separator_length;
            }

            final int max_length;
            if ( max_remaining_length > StringRepresentation.DEFAULT_OBJECT_LENGTH )
            {
                max_length = StringRepresentation.DEFAULT_OBJECT_LENGTH;
            }
            else
            {
                max_length = max_remaining_length;
            }

            final String element_as_string =
                StringRepresentation.of ( new Integer ( element ),
                                          max_length );

            final int length = element_as_string.length ();
            if ( max_remaining_length > 0
                 && max_remaining_length < length )
            {
                num_unprinted_items = 1;
                continue;
            }

            max_remaining_length -= length;

            sbuf.append ( " " + element_as_string );
        }

        if ( ! is_first )
        {
            if ( num_unprinted_items > 0 )
            {
                sbuf.append ( "...[" + num_unprinted_items + " more]..." );
            }

            sbuf.append ( " " );
        }

        sbuf.append ( "}" );

        return sbuf.toString ();
    }


    /**
     * <p>
     * Converts the specified array of elements into human-readable text.
     * </p>
     *
     * @param elements The value(s) to convert into a human-readable String.
     *                 Must not be null.
     *
     * @param max_total_length The maximum total String length of
     *                         all the elements with ", " separators,
     *                         after which a "..." type String will be
     *                         appended in place of further elements.
     *                         Can be 0 or less for no maximum.
     *
     * @return The String representation of the specified elements.
     *         Never null.
     */
    public static final String of (
            long [] elements,
            int max_total_length
            )
    {
        if ( elements == null )
        {
            return "null";
        }

        int max_remaining_length = max_total_length;

        final StringBuilder sbuf = new StringBuilder ();
        boolean is_first = true;
        sbuf.append ( "{" );
        int num_unprinted_items = 0;
        for ( long element : elements )
        {
            if ( num_unprinted_items > 0 )
            {
                num_unprinted_items ++;
                continue;
            }

            final int separator_length;
            if ( is_first )
            {
                is_first = false;
                separator_length = 1; // 1 for space below.
            }
            else
            {
                sbuf.append ( "," );
                separator_length = 2; // 1 for comma above, 1 for space below.
            }

            if ( max_remaining_length > 0 )
            {
                if ( max_remaining_length <= separator_length )
                {
                    num_unprinted_items ++;
                    continue;
                }

                max_remaining_length -= separator_length;
            }

            final int max_length;
            if ( max_remaining_length > StringRepresentation.DEFAULT_OBJECT_LENGTH )
            {
                max_length = StringRepresentation.DEFAULT_OBJECT_LENGTH;
            }
            else
            {
                max_length = max_remaining_length;
            }

            final String element_as_string =
                StringRepresentation.of ( new Long ( element ),
                                          max_length );

            final int length = element_as_string.length ();
            if ( max_remaining_length > 0
                 && max_remaining_length < length )
            {
                num_unprinted_items = 1;
                continue;
            }

            max_remaining_length -= length;

            sbuf.append ( " " + element_as_string );
        }

        if ( ! is_first )
        {
            if ( num_unprinted_items > 0 )
            {
                sbuf.append ( "...[" + num_unprinted_items + " more]..." );
            }

            sbuf.append ( " " );
        }

        sbuf.append ( "}" );

        return sbuf.toString ();
    }


    /**
     * <p>
     * Converts the specified array of elements into human-readable text.
     * </p>
     *
     * @param elements The value(s) to convert into a human-readable String.
     *                 Must not be null.
     *
     * @param max_total_length The maximum total String length of
     *                         all the elements with ", " separators,
     *                         after which a "..." type String will be
     *                         appended in place of further elements.
     *                         Can be 0 or less for no maximum.
     *
     * @return The String representation of the specified elements.
     *         Never null.
     */
    public static final String of (
            short [] elements,
            int max_total_length
            )
    {
        if ( elements == null )
        {
            return "null";
        }

        int max_remaining_length = max_total_length;

        final StringBuilder sbuf = new StringBuilder ();
        boolean is_first = true;
        sbuf.append ( "{" );
        int num_unprinted_items = 0;
        for ( short element : elements )
        {
            if ( num_unprinted_items > 0 )
            {
                num_unprinted_items ++;
                continue;
            }

            final int separator_length;
            if ( is_first )
            {
                is_first = false;
                separator_length = 1; // 1 for space below.
            }
            else
            {
                sbuf.append ( "," );
                separator_length = 2; // 1 for comma above, 1 for space below.
            }

            if ( max_remaining_length > 0 )
            {
                if ( max_remaining_length <= separator_length )
                {
                    num_unprinted_items ++;
                    continue;
                }

                max_remaining_length -= separator_length;
            }

            final int max_length;
            if ( max_remaining_length > StringRepresentation.DEFAULT_OBJECT_LENGTH )
            {
                max_length = StringRepresentation.DEFAULT_OBJECT_LENGTH;
            }
            else
            {
                max_length = max_remaining_length;
            }

            final String element_as_string =
                StringRepresentation.of ( new Short ( element ),
                                          max_length );

            final int length = element_as_string.length ();
            if ( max_remaining_length > 0
                 && max_remaining_length < length )
            {
                num_unprinted_items = 1;
                continue;
            }

            max_remaining_length -= length;

            sbuf.append ( " " + element_as_string );
        }

        if ( ! is_first )
        {
            if ( num_unprinted_items > 0 )
            {
                sbuf.append ( "...[" + num_unprinted_items + " more]..." );
            }

            sbuf.append ( " " );
        }

        sbuf.append ( "}" );

        return sbuf.toString ();
    }


    /**
     * <p>
     * Returns the specified number of seconds as a human-readble String,
     * such as "100 nanoseconds" or "3 milliseconds" or "1 hour"
     * and so on.
     * </p>
     *
     * @param seconds The time interval in seconds.
     *                Must be greater than or equal to
     *                BigDecimal.ZERO.
     *
     * @return A human-readable String representing the amount of time
     *         in reasonable units (nanoseconds, seconds, hours, and so on).
     *         Never null.
     */
    public static final String ofTime (
                                       BigDecimal seconds
                                       )
    {
        final String time_units_singular;
        final String time_units_plural;
        final BigDecimal divisor;
        if ( seconds.compareTo ( BigDecimal.ZERO ) == 0 )
        {
            return "0 nanoseconds";
        }
        else if ( seconds.compareTo ( BigDecimal.ONE ) == 0 )
        {
            return "1 second";
        }
        else if ( seconds.compareTo ( Seconds.PER_MICROSECOND ) < 0 )
        {
            time_units_singular = "nanosecond";
            time_units_plural = "nanoseconds";
            divisor = Seconds.PER_NANOSECOND;
        }
        else if ( seconds.compareTo ( Seconds.PER_MILLISECOND ) < 0 )
        {
            time_units_singular = "microsecond";
            time_units_plural = "microseconds";
            divisor = Seconds.PER_MICROSECOND;
        }
        else if ( seconds.compareTo ( BigDecimal.ONE ) < 0 )
        {
            time_units_singular = "millisecond";
            time_units_plural = "milliseconds";
            divisor = Seconds.PER_MILLISECOND;
        }
        else if ( seconds.compareTo ( Seconds.PER_MINUTE ) < 0 )
        {
            time_units_singular = "second";
            time_units_plural = "seconds";
            divisor = BigDecimal.ONE;
        }
        else if ( seconds.compareTo ( Seconds.PER_HOUR ) < 0 )
        {
            time_units_singular = "minute";
            time_units_plural = "minutes";
            divisor = Seconds.PER_MINUTE;
        }
        else if ( seconds.compareTo ( Seconds.PER_DAY ) < 0 )
        {
            time_units_singular = "hour";
            time_units_plural = "hours";
            divisor = Seconds.PER_HOUR;
        }
        else if ( seconds.compareTo ( Seconds.PER_WEEK ) < 0 )
        {
            time_units_singular = "day";
            time_units_plural = "days";
            divisor = Seconds.PER_DAY;
        }
        else if ( seconds.compareTo ( Seconds.PER_YEAR_GREGORIAN ) < 0 )
        {
            time_units_singular = "week";
            time_units_plural = "weeks";
            divisor = Seconds.PER_WEEK;
        }
        else if ( seconds.compareTo ( Seconds.PER_DECADE_GREGORIAN ) < 0 )
        {
            time_units_singular = "year";
            time_units_plural = "years";
            divisor = Seconds.PER_YEAR;
        }
        else if ( seconds.compareTo ( Seconds.PER_CENTURY_GREGORIAN ) < 0 )
        {
            time_units_singular = "decade";
            time_units_plural = "decades";
            divisor = Seconds.PER_DECADE;
        }
        else if ( seconds.compareTo ( Seconds.PER_MILLENIUM_GREGORIAN ) < 0 )
        {
            time_units_singular = "century";
            time_units_plural = "centuries";
            divisor = Seconds.PER_CENTURY;
        }
        else
        {
            time_units_singular = "millenium";
            time_units_plural = "millenia";
            divisor = Seconds.PER_MILLENIUM_GREGORIAN;
        }

        final BigDecimal amount =
            seconds.divide ( divisor,
                             StringRepresentation.INTEGER_ROUNDING );
        final BigDecimal remainder =
            seconds.remainder ( divisor,
                                StringRepresentation.INTEGER_ROUNDING );

        final StringBuilder representation = new StringBuilder ();

        final int amount_comparison = amount.compareTo ( BigDecimal.ONE );
        if ( amount_comparison == 0 )
        {
            representation.append ( "1 " );
            representation.append ( time_units_singular );
        }
        else if ( amount_comparison != 0 )
        {
            representation.append ( "" + amount );
            representation.append ( " " );
            representation.append ( time_units_plural );
        }

        if ( remainder.compareTo ( BigDecimal.ZERO ) != 0 )
        {
            final String remainder_representation =
                StringRepresentation.ofTime ( remainder );
            representation.append ( " " );
            representation.append ( remainder_representation );
        }

        return representation.toString ();
    }


    /**
     * <p>
     * Converts the specified iterable elements, each one an elapsed amount
     * of time in seconds, into human-readable text.
     * </p>
     *
     * @param secondses The time(s) in seconds to convert into
     *                  a human-readable String.   Must not be null.
     *                  Must not contain any null elements.
     *
     * @param max_total_length The maximum total String length of
     *                         all the times with ", " separators,
     *                         after which a "..." type String will be
     *                         appended in place of further elements.
     *                         Can be 0 or less for no maximum.
     *
     * @return The String representation of the specified elements.
     *         Never null.
     */
    public static final String ofTime (
                                       Iterable<BigDecimal> secondses,
                                       int max_total_length
                                       )
    {
        return StringRepresentation.of ( secondses,
                                         max_total_length )
            + " seconds";
    }


    /**
     * <p>
     * Converts the specified iterable elements, each one an elapsed amount
     * of time in seconds, into human-readable text.
     * </p>
     *
     * @param secondses The time(s) in seconds to convert into
     *                  a human-readable String.   Must not be null.
     *                  Must not contain any null elements.
     *
     * @param max_total_length The maximum total String length of
     *                         all the times with ", " separators,
     *                         after which a "..." type String will be
     *                         appended in place of further elements.
     *                         Can be 0 or less for no maximum.
     *
     * @return The String representation of the specified elements.
     *         Never null.
     */
    public static final String ofTime (
                                       BigDecimal [] secondses,
                                       int max_total_length
                                       )
    {
        return StringRepresentation.of ( secondses,
                                         max_total_length )
            + " seconds";
    }
}
