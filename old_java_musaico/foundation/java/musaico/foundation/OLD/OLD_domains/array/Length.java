package musaico.foundation.domains.array;

import java.io.Serializable;


import musaico.foundation.domains.ClassName;

import musaico.foundation.domains.comparability.BoundedDomain;

import musaico.foundation.domains.number.EqualToNumber;
import musaico.foundation.domains.number.GreaterThanOne;
import musaico.foundation.domains.number.GreaterThanZero;

import musaico.foundation.domains.elements.Elements;

import musaico.foundation.filter.AbstractDomain;
import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;


/**
 * <p>
 * The domain of all objects, arrays and collections whose lengths
 * fall into a specific domain of numbers (such as GreaterThanZero.DOMAIN).
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
 * @see musaico.foundation.domains.array.MODULE#COPYRIGHT
 * @see musaico.foundation.domains.array.MODULE#LICENSE
 */
public class Length
    extends AbstractDomain<Elements<?>>
    implements ArrayDomain, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The "longer than 1 element" domain singleton. */
    public static final Length GREATER_THAN_ONE_DOMAIN =
        new Length ( GreaterThanOne.DOMAIN );

    /** The "longer than 0 elements" domain singleton. */
    public static final Length GREATER_THAN_ZERO_DOMAIN =
        new Length ( GreaterThanZero.DOMAIN );

    /** The "less than or equal to Integer.MAX_SIZE" domain singleton. */
    public static final Length INT_32_SIZED =
        new Length ( 0, Integer.MAX_VALUE );


    // The domain of lengths allowed for arrays in this domain.
    private final Filter<Number> lengthDomain;


    /**
     * <p>
     * Creates a new Length array domain.
     * </p>
     *
     * @param exact_length The exact length of every array in this domain.
     *                     Must be greater than or equal to 0.
     */
    public Length (
                   int exact_length
                   )
        throws IllegalArgumentException
    {
        this ( (long) exact_length );
    }


    /**
     * <p>
     * Creates a new Length array domain.
     * </p>
     *
     * @param minimum_length The closed minimum length of every array
     *                       in this domain.  Every array must have
     *                       greater than or equal to this many elements.
     *                       Must be greater than or equal to 0.
     *
     * @param maximum_length The closed maximum length of every array
     *                       in this domain.  Every array must have
     *                       less than or equal to this many elements.
     *                       Must be greater than or equal to minimum_length.
     */
    public Length (
                   int minimum_length,
                   int maximum_length
                   )
        throws IllegalArgumentException
    {
        // [ minimum_length, maximum_length ]
        this ( BoundedDomain.EndPoint.CLOSED,
               (long) minimum_length,
               BoundedDomain.EndPoint.CLOSED,
               (long) maximum_length );
    }


    /**
     * <p>
     * Creates a new Length array domain.
     * </p>
     *
     * @param minimum_end_point Whether the minimum is open, closed,
     *                          or none (ignored completely).
     *                          Must not be null.
     *
     * @param minimum_length The minimum length of every array in this domain.
     *                       Must be greater than or equal to 0L.
     *
     * @param maximum_end_point Whether the maximum is open, closed,
     *                          or none (ignored completely).
     *                          Must not be null.
     *
     * @param maximum_length The maximum length of every array in this domain.
     *                       Must be greater than or equal to minimum_length.
     *
     * @throws NullPointerException If any of the parameters is null.
     *
     * @throws IllegalArgumentException If the specified minimum is
     *                                  negative, or if the specified minimum
     *                                  is greater than the specified
     *                                  maximum length.
     */
    public Length (
            BoundedDomain.EndPoint minimum_end_point,
            int minimum_length,
            BoundedDomain.EndPoint maximum_end_point,
            int maximum_length
            )
        throws NullPointerException,
               IllegalArgumentException
    {
        this ( minimum_end_point,
               (long) minimum_length,
               maximum_end_point,
               (long) maximum_length );
    }

    /**
     * <p>
     * Creates a new Length array domain.
     * </p>
     *
     * @param exact_length The exact length of every array in this domain.
     *                     Must be greater than or equal to 0L.
     */
    public Length (
                   long exact_length
                   )
        throws IllegalArgumentException
    {
        if ( exact_length < 0L )
        {
            throw new IllegalArgumentException ( "Length constructor: exact_length must be greater than or equal to 0L" );
        }

        this.lengthDomain = new EqualToNumber ( exact_length );
    }


    /**
     * <p>
     * Creates a new Length array domain.
     * </p>
     *
     * @param minimum_end_point Whether the minimum is open, closed,
     *                          or none (ignored completely).
     *                          Must not be null.
     *
     * @param minimum_length The minimum length of every array in this domain.
     *                       Must be greater than or equal to 0L.
     *
     * @param maximum_end_point Whether the maximum is open, closed,
     *                          or none (ignored completely).
     *                          Must not be null.
     *
     * @param maximum_length The maximum length of every array in this domain.
     *                       Must be greater than or equal to minimum_length.
     *
     * @throws NullPointerException If any of the parameters is null.
     *
     * @throws IllegalArgumentException If the specified minimum is
     *                                  negative, or if the specified minimum
     *                                  is greater than the specified
     *                                  maximum length.
     */
    public Length (
            BoundedDomain.EndPoint minimum_end_point,
            long minimum_length,
            BoundedDomain.EndPoint maximum_end_point,
            long maximum_length
            )
        throws NullPointerException,
               IllegalArgumentException
    {
        if ( minimum_end_point == null
             || maximum_end_point == null )
        {
            throw new NullPointerException ( "Length constructor:"
                                             +" cannot have minimum endpoint "
                                             + minimum_end_point
                                             + ", maximum endpoint "
                                             + maximum_end_point );
        }
        else if ( minimum_length < 0L )
        {
            throw new IllegalArgumentException ( "Length constructor: minimum_length must be greater than or equal to 0L" );
        }
        else if ( maximum_length < minimum_length )
        {
            throw new IllegalArgumentException ( "Length constructor: maximum_length must be greater than or equal to minimum_length" );
        }

        this.lengthDomain =
            BoundedDomain.overNumbers (
                minimum_end_point,
                minimum_length,
                maximum_end_point,
                maximum_length );
    }


    /**
     * <p>
     * Creates a new Length array domain with the specified domain of
     * array lengths.
     * </p>
     *
     * @param length_domain The domain of lengths of arrays, such as
     *                      GreaterThanZero.DOMAIN.  Must not be null.
     */
    public Length (
                   Filter<Number> length_domain
                   )
    {
        this.lengthDomain = length_domain;
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

        final Length that = (Length) obj;
        if ( this.lengthDomain == null )
        {
            if ( that.lengthDomain != null )
            {
                return false;
            }
        }
        else if ( that.lengthDomain == null )
        {
            return false;
        }
        else if ( ! this.lengthDomain.equals ( that.lengthDomain ) )
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
            Elements<?> array
            )
    {
        if ( array == null )
        {
            return FilterState.DISCARDED;
        }

        final long length = array.length ();
        final FilterState length_matches =
            this.lengthDomain.filter ( length );

        return length_matches;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return 17 * this.getClass ().getName ().hashCode ()
            + this.lengthDomain.hashCode ();
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this.getClass () )
            + " "
            + this.lengthDomain;
    }
}
