package musaico.foundation.domains.comparability;

import java.io.Serializable;

import java.util.Comparator;


import musaico.foundation.filter.AbstractDomain;
import musaico.foundation.filter.FilterState;


import musaico.foundation.domains.ClassName;



/**
 <p>
 * A domain of objects which must be within { minimum, maximum }
 * bounds.
 * </p>
 *
 * <p>
 * Each bound is optional, so a { minimum only } or a { maximum only }
 * BoundedDomain is possible.
 * </p>
 *
 * <p>
 * Each bound can be open or closed.  So, for example, the bounds
 * <code> [ 0, 1 ] </code> allow any number that is greater than
 * or equal to 0 and also less than or equal to 1; or the bounds
 * <code> ( 0, 1 ) </code> allow any number that is strictly greater
 * than 0 and also strictly less than 1; or the bounds <code> ( 0, 1 ] </code>
 * allow any number that is strictly greater than 0 and also
 * less than or equal to 1; and so on.
 * </p>
 *
 *
 * <p>
 * *** Do not forget to add new domains to the classes in
 * *** musaico.foundation.contract.obligation
 * *** and musaico.foundation.contract.guarantee!
 * </p>
 *
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
 * @see musaico.foundation.domains.comparability.MODULE#COPYRIGHT
 * @see musaico.foundation.domains.comparability.MODULE#LICENSE
 */
public class BoundedDomain<BOUNDED extends Object>
    extends AbstractDomain<BOUNDED>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Determines whether a bound is closed (all members of the domain
     * are greater/less than OR equal to the bound) or open (all members
     * of the domain are strictly greater/less than the bound).
     * </p>
     */
    public static enum EndPoint
    {
        /** The bounded value must be either (less than / greater than)
         *  OR equal to the endpoint. */
        CLOSED,

        /** There is no (minimum / maximum) endpoint. */
        NONE,

        /** The bounded value must be strictly (less than / greater than)
         *  the endpoint. */
        OPEN;
    }




    // The comparator used to compare possible domain values to
    //  the minimum and maximum values.
    private final Comparator<BOUNDED> comparator;

    // Whether the minimum is open, closed, or none (ignored completely).
    private final BoundedDomain.EndPoint minimumEndPoint;

    // The minimum value allowed for any value in this domain.
    private final BOUNDED minimum;

    // Whether the maximum is open, closed, or none (ignored completely).
    private final BoundedDomain.EndPoint maximumEndPoint;

    // The maximum value allowed for any value in this domain.
    private final BOUNDED maximum;


    /**
     * <p>
     * Creates a new BoundedDomain for the specified naturally
     * ordered class of objects, with the specified minimum and
     * maximum values.
     * </p>
     *
     * @param minimum_end_point Whether the minimum is open, closed,
     *                          or none (ignored completely).
     *                          Must not be null.
     *
     * @param minimum The minimum value for objects in this domain.
     *                Must not be null.
     *
     * @param maximum_end_point Whether the maximum is open, closed,
     *                          or none (ignored completely).
     *                          Must not be null.
     *
     * @param maximum The maximum value for objects in this domain.
     *                Must not be null.
     *
     * @throws NullPointerException If any of the parameters is null.
     *
     * @throws IllegalArgumentException If the specified minimum is
     *                                  greater than the specified
     *                                  maximum according to the
     *                                  natural order.
     */
    public static <NATURALLY_ORDERED extends Comparable<NATURALLY_ORDERED>>
        BoundedDomain<NATURALLY_ORDERED> over (
            BoundedDomain.EndPoint minimum_end_point,
            NATURALLY_ORDERED minimum,
            BoundedDomain.EndPoint maximum_end_point,
            NATURALLY_ORDERED maximum
            )
        throws NullPointerException,
               IllegalArgumentException
    {
        return new BoundedDomain<NATURALLY_ORDERED> (
                       new NaturalOrderComparator<NATURALLY_ORDERED> (),
                       minimum_end_point,
                       minimum,
                       maximum_end_point,
                       maximum );
    }


    /**
     * <p>
     * Creates a new BoundedDomain with the specified minimum and
     * maximum numbers.
     * </p>
     *
     * @param minimum_end_point Whether the minimum is open, closed,
     *                          or none (ignored completely).
     *                          Must not be null.
     *
     * @param minimum The minimum for numbers in this domain.
     *                Must not be null.
     *
     * @param maximum_end_point Whether the maximum is open, closed,
     *                          or none (ignored completely).
     *                          Must not be null.
     *
     * @param maximum The maximum for numbers in this domain.
     *                Must not be null.
     *
     * @throws NullPointerException If any of the parameters is null.
     *
     * @throws IllegalArgumentException If the specified minimum is
     *                                  greater than the specified
     *                                  maximum according to
     *                                  NumberComparator.COMPARATOR.
     */
    public static BoundedDomain<Number> overNumbers (
            BoundedDomain.EndPoint minimum_end_point,
            Number minimum,
            BoundedDomain.EndPoint maximum_end_point,
            Number maximum
            )
        throws NullPointerException,
               IllegalArgumentException
    {
        return new BoundedDomain<Number> (
                       NumberComparator.COMPARATOR,
                       minimum_end_point,
                       minimum,
                       maximum_end_point,
                       maximum );
    }


    /**
     * <p>
     * Creates a new BoundedDomain with the specified minimum and
     * maximum strings.
     * </p>
     *
     * @param minimum_end_point Whether the minimum is open, closed,
     *                          or none (ignored completely).
     *                          Must not be null.
     *
     * @param minimum The minimum for strings in this domain.
     *                Must not be null.
     *
     * @param maximum_end_point Whether the maximum is open, closed,
     *                          or none (ignored completely).
     *                          Must not be null.
     *
     * @param maximum The maximum for strings in this domain.
     *                Must not be null.
     *
     * @throws NullPointerException If any of the parameters is null.
     *
     * @throws IllegalArgumentException If the specified minimum is
     *                                  greater than the specified
     *                                  maximum according to
     *                                  StringComparator.COMPARATOR.
     */
    public static BoundedDomain<String> overStrings (
            BoundedDomain.EndPoint minimum_end_point,
            String minimum,
            BoundedDomain.EndPoint maximum_end_point,
            String maximum
            )
        throws NullPointerException,
               IllegalArgumentException
    {
        return new BoundedDomain<String> (
                       StringComparator.COMPARATOR,
                       minimum_end_point,
                       minimum,
                       maximum_end_point,
                       maximum );
    }


    /**
     * <p>
     * Creates a new BoundedDomain using the specified comparator,
     * with the specified minimum and maximum values for objects
     * in the new domain.
     * </p>
     *
     * @param comparator The comparator to use when checking bounds.
     *                   Must not be null.
     *
     * @param minimum_end_point Whether the minimum is open, closed,
     *                          or none (ignored completely).
     *                          Must not be null.
     *
     * @param minimum The minimum value for objects in this domain.
     *                Must not be null.
     *
     * @param maximum_end_point Whether the maximum is open, closed,
     *                          or none (ignored completely).
     *                          Must not be null.
     *
     * @param maximum The maximum value for objects in this domain.
     *                Must not be null.
     *
     * @throws NullPointerException If any of the parameters is null.
     *
     * @throws IllegalArgumentException If the specified minimum is
     *                                  greater than the specified
     *                                  maximum according to the
     *                                  specified comparator.
     */
    public BoundedDomain (
            Comparator<BOUNDED> comparator,
            BoundedDomain.EndPoint minimum_end_point,
            BOUNDED minimum,
            BoundedDomain.EndPoint maximum_end_point,
            BOUNDED maximum
            )
        throws NullPointerException,
               IllegalArgumentException
    {
        if ( comparator == null
             || minimum_end_point == null
             || minimum == null
             || maximum_end_point == null
             || maximum == null )
        {
            throw new NullPointerException (
                "Cannot create a BoundedDomain with comparator "
                + comparator
                + " minimum value "
                + minimum_end_point
                + " "
                + minimum
                + " maximum value "
                + maximum_end_point
                + " "
                + maximum );
        }
        else if ( comparator.compare ( minimum, maximum ) > 0 )
        {
            throw new IllegalArgumentException (
                "Cannot create a BoundedDomain with comparator "
                + comparator
                + " minimum value "
                + minimum_end_point
                + " "
                + minimum
                + " maximum value "
                + maximum_end_point
                + " "
                + maximum );
        }

        this.comparator = comparator;
        this.minimumEndPoint = minimum_end_point;
        this.minimum = minimum;
        this.maximumEndPoint = maximum_end_point;
        this.maximum = maximum;
    }


    /**
     * @return The Comparator used to determine whether each
     *         element is within bounds.  Never null.
     */
    public final Comparator<BOUNDED> comparator ()
    {
        return this.comparator;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
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

        final BoundedDomain<?> that = (BoundedDomain<?>) obj;
        if ( this.comparator == null )
        {
            if ( that.comparator != null )
            {
                return false;
            }
        }
        else if ( that.comparator == null )
        {
            return false;
        }
        else if ( ! this.comparator.equals ( that.comparator ) )
        {
            return false;
        }

        if ( this.minimumEndPoint != that.minimumEndPoint )
        {
            return false;
        }
        else if ( this.maximumEndPoint != that.maximumEndPoint )
        {
            return false;
        }

        if ( this.minimum == null )
        {
            if ( that.minimum != null )
            {
                return false;
            }
        }
        else if ( that.minimum == null )
        {
            return false;
        }
        else if ( ! this.minimum.equals ( that.minimum ) )
        {
            return false;
        }

        if ( this.maximum == null )
        {
            if ( that.maximum != null )
            {
                return false;
            }
        }
        else if ( that.maximum == null )
        {
            return false;
        }
        else if ( ! this.maximum.equals ( that.maximum ) )
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
        int hash_code = 37 * this.getClass ().getName ().hashCode ();
        hash_code += 31 * this.comparator.hashCode ();
        hash_code += 11 * this.maximum.hashCode ();
        hash_code += this.minimum.hashCode ();

        return hash_code;
    }


    /**
     * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
     */
    @Override
    public final FilterState filter (
                                     BOUNDED value
                                     )
    {
        if ( value == null )
        {
            return FilterState.DISCARDED;
        }

        switch ( this.minimumEndPoint )
        {
        case OPEN:
            if ( this.comparator.compare ( value, this.minimum ) <= 0 )
            {
                // Too low, out of bounds.
                return FilterState.DISCARDED;
            }
            break;

        case NONE:
            break;

        case CLOSED:
        default:
            if ( this.comparator.compare ( value, this.minimum ) < 0 )
            {
                // Too low, out of bounds.
                return FilterState.DISCARDED;
            }
            break;
        }

        switch ( this.maximumEndPoint )
        {
        case OPEN:
            if ( this.comparator.compare ( value, this.maximum ) >= 0 )
            {
                // Too high, out of bounds.
                return FilterState.DISCARDED;
            }
            break;

        case NONE:
            break;

        case CLOSED:
        default:
            if ( this.comparator.compare ( value, this.maximum ) > 0 )
            {
                // Too high, out of bounds.
                return FilterState.DISCARDED;
            }
            break;
        }

        // In bounds.
        return FilterState.KEPT;
    }


    /**
     * @return The maximum value for elements of this domain.
     *         Never null.
     */
    public final BOUNDED maximum ()
    {
        return this.maximum;
    }


    /**
     * @return Whether the maximum is open, closed,
     *         or none (ignored completely).
     */
    public final BoundedDomain.EndPoint maximumEndPoint ()
    {
        return this.maximumEndPoint;
    }


    /**
     * @return The minimum value for elements of this domain.
     *         Never null.
     */
    public final BOUNDED minimum ()
    {
        return this.minimum;
    }


    /**
     * @return Whether the maximum is open, closed,
     *         or none (ignored completely).
     */
    public final BoundedDomain.EndPoint minimumEndPoint ()
    {
        return this.minimumEndPoint;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        final StringBuilder sbuf = new StringBuilder ();
        switch ( this.minimumEndPoint )
        {
        // Minimum endpoint is open:
        case OPEN:
            switch ( this.maximumEndPoint )
            {
            // Maximum endpoint is open:
            case OPEN:
                sbuf.append ( "( " + this.minimum );
                sbuf.append ( ", " + this.maximum );
                sbuf.append ( " )" );
                break;

            // No maximum:
            case NONE:
                sbuf.append ( "> " + this.minimum );
                break;

            // Maximum endpoint is closed:
            case CLOSED:
            default:
                sbuf.append ( "( " + this.minimum );
                sbuf.append ( ", " + this.maximum );
                sbuf.append ( " ]" );
                break;
            }
            break;

        // No minimum:
        case NONE:
            switch ( this.maximumEndPoint )
            {
            // Maximum endpoint is open:
            case OPEN:
                sbuf.append ( "< " + this.maximum );
                break;

            // No maximum:
            case NONE:
                sbuf.append ( " unbounded " );
                break;

            // Maximum endpoint is closed:
            case CLOSED:
            default:
                sbuf.append ( "<= " + this.maximum );
                break;
            }
            break;

        // Minimum endpoint is closed:
        case CLOSED:
        default:
            switch ( this.maximumEndPoint )
            {
            // Maximum endpoint is open:
            case OPEN:
                sbuf.append ( "[ " + this.minimum );
                sbuf.append ( ", " + this.maximum );
                sbuf.append ( " )" );
                break;

            // No maximum:
            case NONE:
                sbuf.append ( ">= " + this.minimum );
                break;

            // Maximum endpoint is closed:
            case CLOSED:
            default:
                sbuf.append ( "[ " + this.minimum );
                sbuf.append ( ", " + this.maximum );
                sbuf.append ( " ]" );
                break;
            }
            break;
        }

        return ClassName.of ( this.getClass () )
            + sbuf.toString ();
    }
}
