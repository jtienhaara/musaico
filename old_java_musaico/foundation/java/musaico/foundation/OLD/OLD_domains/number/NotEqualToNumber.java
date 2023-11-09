package musaico.foundation.domains.number;

import java.io.Serializable;


import musaico.foundation.domains.comparability.LeftAndRight;

import musaico.foundation.filter.AbstractDomain;
import musaico.foundation.filter.FilterState;


/**
 * <p>
 * The domain of all Numbers which are not equal to a specific Number.
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
 * @see musaico.foundation.domains.MODULE#COPYRIGHT
 * @see musaico.foundation.domains.MODULE#LICENSE
 */
public class NotEqualToNumber
    extends AbstractDomain<Number>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The Number to which nothing in this domain is equal. */
    private final Number number;


    /**
     * <p>
     * Creates a new NotEqualToNumber the specified Number, a domain
     * containing all possible Numbers except the specified one.
     * </p>
     *
     * @param number The Number to which nothing in this domain is equal.
     *               Must not be null.
     */
    public NotEqualToNumber (
                             Number number
                             )
        throws NullPointerException
    {
        if ( number == null )
        {
            throw new NullPointerException ( "Cannot create a NotEqualToNumber null domain" );
        }

        this.number = number;
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

        final NotEqualToNumber that = (NotEqualToNumber) obj;
        if ( this.number == null )
        {
            if ( that.number != null )
            {
                return false;
            }
        }
        else if ( that.number == null )
        {
            return false;
        }
        // Note that this domain is NOT equal to that domain if
        // this.number = BigDecimal.ZERO
        // and that.number = new BigDecimal ( "0.00" ), for example.
        else if ( ! this.number.equals ( that.number ) )
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
                                     Number value
                                     )
    {
        if ( value == null )
        {
            return FilterState.DISCARDED;
        }

        final LeftAndRight<Number, Number> left_and_right =
            new LeftAndRight<Number, Number> ( value, this.number );
        if ( left_and_right.isEqual () )
        {
            return FilterState.DISCARDED;
        }
        else
        {
            return FilterState.KEPT;
        }
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        int hash_code = 17 * this.getClass ().getName ().hashCode ();
        if ( this.number != null )
        {
            hash_code += this.number.hashCode ();
        }

        return hash_code;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "not equal to " + this.number;
    }


    /**
     * @return The value to which each input must not be equal.
     *         Never null.
     */
    public Number value ()
    {
        return this.number;
    }
}
