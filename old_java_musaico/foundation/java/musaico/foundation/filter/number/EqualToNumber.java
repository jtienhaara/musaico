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
 * Keeps all numbers which are equal to a specific number.
 * </p>
 *
 *
 * <p>
 * *** Do not forget to add useful new Filters to the classes in
 * *** musaico.foundation.contract.obligation
 * *** and musaico.foundation.contract.guarantee!
 * </p>
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
 * @see musaico.foundation.filter.MODULE#COPYRIGHT
 * @see musaico.foundation.filter.MODULE#LICENSE
 */
public class EqualToNumber<NUMBER extends Number>
    implements Filter<NUMBER>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // The Number to which each number must be equal in order to be
    // kept by this filter.
    private final NUMBER number;


    /**
     * @return The specific number, or zero, if null is passed.
     *         Never null.
     */
    @SuppressWarnings("unchecked") // casts checked with try...catch.
    protected static final <NUMBER extends Number>
        NUMBER number (
            Class<?> filter_class,
            NUMBER number
            )
    {
        if ( number == null )
        {
            try
            {
                return (NUMBER) BigDecimal.ZERO;
            }
            catch ( Exception eBD )
            {
                try
                {
                    return (NUMBER) BigInteger.ZERO;
                }
                catch ( Exception eBI )
                {
                    try
                    {
                        return (NUMBER) new Byte ( (byte) 0 );
                    }
                    catch ( Exception eBy )
                    {
                        try
                        {
                            return (NUMBER) new Double ( 0D );
                        }
                        catch ( Exception eDo )
                        {
                            try
                            {
                                return (NUMBER) new Float ( 0F );
                            }
                            catch ( Exception eFl )
                            {
                                try
                                {
                                    return (NUMBER) new Integer ( 0 );
                                }
                                catch ( Exception eIn )
                                {
                                    try
                                    {
                                        return (NUMBER) new Long ( 0L );
                                    }
                                    catch ( Exception eLo )
                                    {
                                        try
                                        {
                                            return (NUMBER) new Short ( (short) 0 );
                                        }
                                        catch ( Exception eSh )
                                        {
                                            // Shouldn't ever happen...  Allegedly...
                                            throw new IllegalArgumentException ( ClassName.of ( filter_class )
                                                                                 + ": Unrecognized NUMBER class, and null passed to constructor" );
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        else
        {
            return number;
        }
    }


    /**
     * <p>
     * Creates a new EqualToNumber the specified number.
     * </p>
     *
     * @param number The number to which each number must be equal
     *               in order to be kept (according to
     *               <code> NumberComparator.COMPARATOR.compare ( ... ) </code>).
     *               If null, then 0 will be used by default.
     *               DO NOT PASS NULL.  Can be null.
     */
    public EqualToNumber (
            NUMBER number
            )
        throws NullPointerException
    {
        this.number = EqualToNumber.number ( this.getClass (),
                                             number );
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

        final EqualToNumber<?> that = (EqualToNumber<?>) obj;
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
        else if ( NumberComparator.COMPARATOR.compare ( this.number,
                                                        that.number ) != 0 )
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
        // Never match numeric comparisons to null.
        if ( value == null )
        {
            return FilterState.DISCARDED;
        }

        final int comparison =
            NumberComparator.COMPARATOR.compare ( this.number, value );
        if ( comparison == 0 )
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
        int hash_code = 17 * ClassName.of ( this.getClass () ).hashCode ();
        if ( this.number != null )
        {
            hash_code += this.number.hashCode ();
        }

        return hash_code;
    }


    /**
     * @return The number to which every filtered value must be equal
     *         in order to be kept.  Never null.
     */
    public final NUMBER number ()
    {
        return this.number;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "equal to " + this.number;
    }
}
