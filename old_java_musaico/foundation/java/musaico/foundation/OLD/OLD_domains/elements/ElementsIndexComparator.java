package musaico.foundation.domains.elements;

import java.io.Serializable;

import java.util.Comparator;


import musaico.foundation.domains.ClassName;


/**
 * <p>
 * An index comparator that uses an element comparator to compare
 * Elements.
 * </p>
 *
 * <p>
 * Typically used to sort the indices of some sequence of Elements,
 * placing the indices into order according to the element comparator.
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
 * @see musaico.foundation.!!!.MODULE#COPYRIGHT
 * @see musaico.foundation.!!!.MODULE#LICENSE
 */
public class ElementsIndexComparator<ELEMENT extends Object>
    implements Comparator<Long>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // The Elements whose elements are indexed by each pair of long numbers.
    private final Elements<ELEMENT> elements;

    // The comparator used to compare each pair of indexed elements.
    private final Comparator<ELEMENT> comparator;


    /**
     * <p>
     * Creates a new ElementsIndexComparator.
     * </p>
     *
     * @param elements The Elements whose elements are indexed
     *                 by each pair of long numbers.
     *                 Must not be null.
     *
     * @param comparator The comparator used to compare each pair
     *                   of indexed elements.  Must not be null.
     *
     * @throws NullPointerException If any of the arguments is null.
     */
    public ElementsIndexComparator (
            Elements<ELEMENT> elements,
            Comparator<ELEMENT> comparator
            )
        throws NullPointerException
    {
        if ( elements == null
             || comparator == null )
        {
            throw new NullPointerException ( "ERROR Cannot create new "
                                             + ClassName.of ( this.getClass () )
                                             + " with elements = "
                                             + elements
                                             + " and comparator = "
                                             + comparator );
        }

        this.elements = elements;
        this.comparator = comparator;
    }


    /**
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public final int compare (
            Long left_index,
            Long right_index
            )
    {
        if ( left_index == null )
        {
            if ( right_index == null )
            {
                return 0; // elements [ null ] == elements [ null ]
            }
            else
            {
                return Integer.MAX_VALUE; // elements [ null ] > elements [ x ]
            }
        }
        else if ( right_index == null )
        {
            return Integer.MIN_VALUE; // elements [ x ] < elements [ null ]
        }

        final long left = left_index.longValue ();
        final long right = right_index.longValue ();
        if ( left == right )
        {
            return 0; // elements [ x ] == elements [ x ]
        }

        final ELEMENT [] left_elements = this.elements.at ( left );
        final ELEMENT [] right_elements = this.elements.at ( right );
        if ( left_elements.length == 0 )
        {
            if ( right_elements.length == 0 )
            {
                if ( left < right )
                {
                    return -1; // elements [ end + x ] < elements [ end + y ]
                }
                else if ( left > right )
                {
                    return 1; // elements [ end + y ] > elements [ end + x ]
                }
                else // left == right, should have been caught above...
                {
                    return 0; // elements [ end + x ] < elements [ end + x ]
                }
            }
            else
            {
                return Integer.MAX_VALUE; // elements [ end + x ] > elements [ x ]
            }
        }
        else if ( right_elements.length == 0 )
        {
            return Integer.MIN_VALUE; // elements [ x ] < elements [ end + x ]
        }

        final ELEMENT left_element = left_elements [ 0 ];
        final ELEMENT right_element = right_elements [ 0 ];

        final int comparison =
            this.comparator.compare ( left_element, right_element );
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
        else if ( this.getClass () != object.getClass () )
        {
            return false;
        }

        final ElementsIndexComparator<?> that =
            (ElementsIndexComparator<?>) object;
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

        if ( this.elements == null )
        {
            if ( that.elements != null )
            {
                return false;
            }
        }
        else if ( that.elements == null )
        {
            return false;
        }
        else if ( ! this.elements.equals ( that.elements ) )
        {
            return false;
        }

        // Everything is all matchy-matchy.
        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        int hash_code = 0;
        if ( this.elements != null )
        {
            hash_code = this.elements.hashCode ();
        }

        if ( this.comparator != null )
        {
            hash_code = 7 * this.comparator.hashCode ();
        }

        return hash_code;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return ClassName.of ( this.getClass () )
            + " ( " + this.elements + ", " + this.comparator + " )";
    }
}
