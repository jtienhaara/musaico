!!! NOT USED AT PRESENT...

    package musaico.foundation.domains.array;

import java.io.Serializable;

import java.util.Arrays;
import java.util.BitSet;


import musaico.foundation.domains.elements.Elements;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;



/**
 * <p>
 * A Domain which can vary from array to array, but which must
 * remain constant throughout elements of the array; each element
 * depends on all the others.
 * </p>
 *
 * <p>
 * For example "an array of integers which is either all positive, or
 * all negative".  If the first element in the array is GreaterThanZero,
 * then all of the remaining elements must be GreaterThanZero as well, or
 * the array is not part of this domain.  Conversely if the
 * first element of the array is LessThanZero, then the remaining elements
 * must also be LessThanZero.
 * </p>
 *
 *
 * <p>
 * In Java every Domain must be Serializable in order to play nicely
 * over RMI.
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
public class CoDependentDomain
    extends AbstractArrayDomain
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // The domains which are allowed for elements of arrays in this
    // domain.  For example, GreaterThanZero and LessThanZero for
    // arrays which are all either greater than zero or less than zero,
    // but not mixed.
    private final Filter<? extends Object> [] domains;


    /**
     * <p>
     * Creates a new CoDependentDomain for arrays which can be
     * made up of elements all of the same domain, from any one of
     * the domains specified.
     * </p>
     *
     * @param domains The domains allowed for array elements.
     *                Each element of an array must be a member of
     *                the same single domain in order for the array
     *                to be valid for this overall domain.  Mixing
     *                elements from <code> domain [ 0 ] </code>
     *                and elements from <code> domain [ 1 ] </code>
     *                is invalid.  Must contain at least 2 Domains.
     *                Must not be null.  Must not contain
     *                any null elements.
     *
     * @throws IllegalArgumentException If any of the domains is invalid
     *                                  (see above).
     */
    @SuppressWarnings("unchecked") // Possible heap pollution / generic varargs
    public CoDependentDomain (
            Filter<? extends Object> ... domains
            )
        throws IllegalArgumentException
    {
        boolean is_legal = true;
        if ( domains == null )
        {
            is_legal = false;
        }
        else if ( domains.length < 2 )
        {
            is_legal = false;
        }
        else
        {
            for ( Filter<? extends Object> domain : domains )
            {
                if ( domain == null )
                {
                    is_legal = false;
                    break;
                }
            }
        }

        if ( ! is_legal )
        {
            final StringBuilder sbuf = new StringBuilder ();
            if ( domains == null )
            {
                sbuf.append ( "null" );
            }
            else
            {
                sbuf.append ( "{" );
                boolean is_first = true;
                for ( Filter<? extends Object> domain : domains )
                {
                    if ( is_first )
                    {
                        is_first = false;
                    }
                    else
                    {
                        sbuf.append ( "," );
                    }

                    sbuf.append ( " " + domain );
                }

                if ( ! is_first )
                {
                    sbuf.append ( " " );
                }

                sbuf.append ( "}" );
            }

            throw new IllegalArgumentException ( "Cannot create a CoDependentDomain from domains " + sbuf );
        }

        this.domains = new Filter<?> [ domains.length ];
        System.arraycopy ( domains, 0,
                           this.domains, 0, domains.length );
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
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

        !!!;

        return true;
    }


    /**
     * @see musaico.foundation.domains.array.AbstractArrayDomain#equalsDetails(musaico.foundation.domains.array.AbstractArrayDomain)
     */
    @Override
    protected final boolean equalsDetails(
            AbstractArrayDomain object
            )
    {
        final CoDependentDomain that = (CoDependentDomain) object;
        return Arrays.equals ( this.domains, that.domains );
    }


    /**
     * @see musaico.foundation.domains.array.AbstractArrayDomain#filterElements(musaico.foundation.domains.array.Elements, java.util.BitSet, boolean)
     */
    @Override
    public final FilterState filterElements (
            Elements<?> array,
            BitSet kept_elements,
            boolean is_abort_on_discard
            )
    {
        final long length = array.length ();

        Filter<? extends Object> common_domain = null;

        FilterState result = FilterState.KEPT;
        for ( long e = 0L; e < length; e ++ )
        {
            final Object element = array.at ( e ) [ 0 ];

            final Filter<? extends Object> element_domain =
                this.findDomain ( element );

            if ( common_domain == null )
            {
                // First element.
                // Kept.
                common_domain = element_domain;
                kept_elements.set ( (int) e, true );
            }
            else if ( common_domain.equals ( element_domain ) )
            {
                // Subsequent element in the same domain as the first one.
                // Kept.
                kept_elements.set ( (int) e, true );
            }
            else
            {
                // Subsequent element NOT in the same domain as the first one.
                // Discarded.
                kept_elements.set ( (int) e, false );

                result = FilterState.DISCARDED;
                if ( is_abort_on_discard )
                {
                    return result;
                }
            }
        }

        return result;
    }


    /**
     * @return The first domain matching the specified value, or null
     *         if the value is not in any of the domains.
     */
    private Filter<? extends Object> findDomain (
            Object value
            )
    {
        for ( Filter<? extends Object> domain : this.domains )
        {
            if ( isMember ( value, domain ) )
            {
                return domain;
            }
        }

        // The specified value is not a member of any of the domains.
        return null;
    }


    /**
     * @see musaico.foundation.domains.array.AbstractArrayDomain#hashCodeDetails()
     */
    @Override
    protected final int hashCodeDetails ()
    {
        return Arrays.hashCode ( this.domains );
    }


    /**
     * @return True if the specified value is a member of the specified
     *         Domain, false if not.
     */
    @SuppressWarnings("unchecked") // We do check the cast, with a catch().
    private <SUB_DOMAIN_TYPE extends Object>
        boolean isMember (
            Object value,
            Filter<SUB_DOMAIN_TYPE> domain
            )
    {
        final FilterState filtered;
        try
        {
            SUB_DOMAIN_TYPE sub_domain_value =
                (SUB_DOMAIN_TYPE) value;
            filtered = domain.filter ( sub_domain_value );
        }
        catch ( ClassCastException e )
        {
            // Clearly the value is not a member of the domain, since
            // it isn't even the right class.
            return false;
        }

        if ( filtered == FilterState.KEPT )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * @see musaico.foundation.domains.array.AbstractArrayDomain#toStringDetails(java.lang.String)
     */
    @Override
    protected String toStringDetails (
            String class_name
            )
    {
        final StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( "{" );
        boolean is_first = true;
        for ( Filter<? extends Object> domain : this.domains )
        {
            if ( is_first )
            {
                is_first = false;
            }
            else
            {
                sbuf.append ( "," );
            }

            sbuf.append ( " " + domain );
        }

        if ( ! is_first )
        {
            sbuf.append ( " " );
        }

        sbuf.append ( "}" );

        return class_name
            + ": all elements must belong to the same domain, any one of "
            + sbuf.toString ();
    }
}
