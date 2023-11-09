package musaico.foundation.domains.string;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Domain;
import musaico.foundation.filter.FilterState;


/**
 * <p>
 * The domain of all Strings which are zero-length.
 * </p>
 *
 *
 * <p>
 * *** Do not forget to add new domains to the classes in
 * *** musaico.foundation.contract.obligation
 * *** and musaico.foundation.contract.guarantee!
 * </p>
 *
 * <p>
 * In Java every Domain must be Serializable in order to play nicely
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
 * @see musaico.foundation.domains.string.MODULE#COPYRIGHT
 * @see musaico.foundation.domains.string.MODULE#LICENSE
 */
public class StringLength
    implements Domain<String>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The minimum length of Strings in this domain. */
    private final int minimumLength;

    /** The maximum length of Strings in this domain. */
    private final int maximumLength;


    /**
     * <p>
     * Creates a new StringLength domain with the specified exact
     * String length.
     * </p>
     *
     * @param exact_length The exact length of Strings in this
     *                     domain.  If greater than or equal to
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
     * Creates a new StringLength domain with the specified minimum
     * and maximum string lengths.
     * </p>
     *
     * @param minimum_length The minimum length of Strings in this
     *                       domain.  If greater than or equal to
     *                       0, then the minimum is as specified.
     *                       If less than 0, then there is no minimum.
     *
     * @param maximum_length The maximum length of Strings in this
     *                       domain.  If greater than or equal to
     *                       0, and if the maximum is greater than or
     *                       equal to the minimum length, then the maximum is
     *                       as specified.  Otherwise, there is no maximum.
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
        return 31 * this.getClass ().getName ().hashCode ()
            + 17 * this.minimumLength
            + this.maximumLength;
    }


    /**
     * @see musaico.foundation.filter.Domain#member(java.lang.Object)
     */
    @Override
    public final List<String> member (
                                      String maybe_member
                                      )
    {
        final List<String> members = new ArrayList<String> ();
        if ( this.filter ( maybe_member ).isKept () )
        {
            members.add ( maybe_member );
        }

        return members;
    }


    /**
     * @see musaico.foundation.filter.Domain#nonMember(java.lang.Object)
     */
    @Override
    public final List<String> nonMember (
                                         String maybe_non_member
                                         )
    {
        final List<String> non_members = new ArrayList<String> ();
        if ( ! this.filter ( maybe_non_member ).isKept () )
        {
            non_members.add ( maybe_non_member );
        }

        return non_members;
    }


    /**
     * @return The maximum length for Strings of this domain.
     *         Every String of this domain must have a length
     *         that is less than or equal to the maximum bound.
     *         Never null.
     */
    public final int maximumLength ()
    {
        return this.maximumLength;
    }


    /**
     * @return The minimum length for Strings of this domain.
     *         Every String of this domain must have a length
     *         that is greater than or equal to the minimum bound.
     *         Never null.
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
        sbuf.append ( "( " );

        if ( this.minimumLength >= 0 )
        {
            sbuf.append ( "" + this.minimumLength + " <= " );
        }

        sbuf.append ( "string length" );

        if ( this.maximumLength >= this.minimumLength )
        {
            sbuf.append ( " <= " + this.maximumLength );
        }

        sbuf.append ( " )" );

        return sbuf.toString ();
    }
}
