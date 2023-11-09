package musaico.foundation.filter.number;

import java.io.Serializable;


import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.filter.comparability.NumberComparator;

import musaico.foundation.structure.ClassName;


/**
 * <p>
 * Keeps all numbers which are less than a specific number.
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
public class LessThanNumber<NUMBER extends Number>
    implements Filter<NUMBER>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // The Number than which each number must be less in order to be kept.
    private final NUMBER number;


    /**
     * <p>
     * Creates a new LessThanNumber filter.
     * </p>
     *
     * @param number The number than which each number must be less
     *               in order to be kept (according to
     *               <code> NumberComparator.COMPARATOR.compare ( ... ) </code>).
     *               If null, then 0 will be used by default.
     *               DO NOT PASS NULL.  Can be null.
     */
    public LessThanNumber (
            NUMBER number
            )
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

        final LessThanNumber<?> that = (LessThanNumber<?>) obj;
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


        final int comparison = NumberComparator.COMPARATOR
            .compare ( this.number, that.number );
        if ( comparison == 0 )
        {
            return true;
        }
        else
        {
            return false;
        }
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

        final int comparison = NumberComparator.COMPARATOR
            .compare ( value, this.number );
        if ( comparison < 0 )
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
        return "less than " + this.number;
    }
}
