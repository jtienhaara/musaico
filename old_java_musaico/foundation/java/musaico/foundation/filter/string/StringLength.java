package musaico.foundation.filter.string;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.structure.ClassName;


/**
 * <p>
 * A Filter that keeps only Strings containing a specific number
 * of characters.
 * </p>
 *
 *
 * <p>
 * *** Do not forget to add new Filters to the classes in
 * *** musaico.foundation.contract.obligation
 * *** and musaico.foundation.contract.guarantee!
 * </p>
 *
 * <p>
 * In Java String is the end of the road for class inheritance,
 * so there is no point in making String Filters generic.
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
 * @see musaico.foundation.filter.string.MODULE#COPYRIGHT
 * @see musaico.foundation.filter.string.MODULE#LICENSE
 */
public class StringLength
    implements Filter<String>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The "longer than 1 character" filter singleton. */
    public static final StringLength GREATER_THAN_ONE =
        new StringLength ( 2, Integer.MAX_VALUE );

    /** The "longer than 0 characters" filter singleton. */
    public static final StringLength GREATER_THAN_ZERO =
        new StringLength ( 1, Integer.MAX_VALUE );


    /** The minimum length of Strings in this Filter. */
    private final int minimumLength;

    /** The maximum length of Strings in this Filter. */
    private final int maximumLength;


    /**
     * <p>
     * Creates a new StringLength Filter with the specified exact
     * String length.
     * </p>
     *
     * @param exact_length The exact length of Strings to be kept by
     *                     this Filter.  If greater than or equal to
     *                     0, then the exact length is as specified.
     *                     If less than 0, then every non-null
     *                     String is KEPT (kind of pointless...).
     */
    public StringLength (
            int exact_length
            )
    {
        this ( exact_length, exact_length );
    }


    /**
     * <p>
     * Creates a new StringLength Filter with the specified minimum
     * and maximum string lengths.
     * </p>
     *
     * @param minimum_length The minimum length of Strings that will be
     *                       kept by this Filter.  If greater than
     *                       or equal to 0, then the minimum is as specified.
     *                       If less than 0, then there is no minimum.
     *
     * @param maximum_length The maximum length of Strings that will be
     *                       kept by this Filter.  If greater than 
     *                       or equal to 0, and if the maximum
     *                       is greater than or equal to the minimum length,
     *                       then the maximum is as specified.
     *                       Otherwise, there is no maximum.
     */
    public StringLength (
            int minimum_length,
            int maximum_length
            )
    {
        this.minimumLength = minimum_length;
        this.maximumLength = maximum_length;
    }


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

        final StringLength that = (StringLength) obj;
        if ( this.minimumLength != that.minimumLength )
        {
            return false;
        }
        if ( this.maximumLength != that.maximumLength )
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
            String value
            )
    {
        if ( value == null )
        {
            return FilterState.DISCARDED;
        }

        if ( this.minimumLength >= 0 )
        {
            if ( value.length () < this.minimumLength )
            {
                return FilterState.DISCARDED;
            }
        }

        if ( this.maximumLength >= this.minimumLength )
        {
            if ( value.length () > this.maximumLength )
            {
                return FilterState.DISCARDED;
            }
        }

        return FilterState.KEPT;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return 31 * ClassName.of ( this.getClass () ).hashCode ()
            + 17 * this.minimumLength
            + this.maximumLength;
    }


    /**
     * @return The maximum length of Strings that will be kept by this Filter.
     *         Every String must have a length that is less than or equal to
     *         the maximum bound in order to be kept by this Filter.
     *         Never null.
     */
    public final int maximumLength ()
    {
        return this.maximumLength;
    }


    /**
     * @return The minimum length of Strings that will be kept by this Filter.
     *         Every String must have a length that is greater than
     *         or equal to the minimum bound in order to be kept
     *         by this Filter.  Never null.
     */
    public final int minimumLength ()
    {
        return this.minimumLength;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        final StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( ClassName.of ( this.getClass () ) );
        sbuf.append ( " ( " );

        if ( this.minimumLength < 0 )
        {
            if ( this.maximumLength < 0 )
            {
                sbuf.append ( "any" );
            }
            else
            {
                sbuf.append ( "<= " + this.maximumLength );
            }
        }
        else if ( this.maximumLength == this.minimumLength )
        {
            sbuf.append ( "" + this.minimumLength );
        }
        else
        {
            
            sbuf.append ( "" + this.minimumLength + " - " + this.maximumLength );
        }

        sbuf.append ( " )" );

        return sbuf.toString ();
    }
}
