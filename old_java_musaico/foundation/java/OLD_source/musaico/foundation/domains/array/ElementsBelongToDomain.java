package musaico.foundation.domains.array;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;


import musaico.foundation.domains.ClassName;

import musaico.foundation.domains.elements.Elements;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;


/**
 * <p>
 * The domain of all single objects, arrays and iterables whose
 * elements all belong to a specific domain.
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
 * across RMI.  However the user of a ElementsBelongToDomain
 * must be careful because the elements in the domain do not
 * have to be Serializable.  Attempting to serialize a ElementsBelongToDomain
 * with non-Serializable elements will cause an exception.
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
public class ElementsBelongToDomain
    extends AbstractArrayDomain
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // The domain to which every element must belong.
    private final Filter<Object> elementDomain;


    /**
     * <p>
     * Creates a new domain of arrays/collections containing only elements
     * which belong to the specified domain.
     * </p>
     *
     * @param element_domain The domain to which every element of every
     *                       array/collection must belong.  Must not be null.
     */
    @SuppressWarnings("unchecked") // Cast Filter<?> to Filter<Object>.
    public ElementsBelongToDomain (
                                   Filter<?> element_domain
                                   )
        throws NullPointerException
    {
        if ( element_domain == null )
        {
            throw new NullPointerException ( "Cannot create a ElementsBelongToDomain ( " + element_domain + " )" );
        }

        this.elementDomain = (Filter<Object>) element_domain;
    }


    /**
     * @see musaico.foundation.domains.array.AbstractArrayDomain#equalsDetails(musaico.foundation.domains.array.AbstractArrayDomain)
     */
    @Override
    protected final boolean equalsDetails (
                                           AbstractArrayDomain array_domain
                                           )
    {
        if ( array_domain == null
             || array_domain.getClass () != this.getClass () )
        {
            return false;
        }

        final ElementsBelongToDomain that =
            (ElementsBelongToDomain) array_domain;

        final boolean same_element_domain =
            this.elementDomain.equals ( that.elementDomain );

        return same_element_domain;
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

        FilterState result = FilterState.KEPT;
        for ( long e = 0L; e < length; e ++ )
        {
            final Object element = array.at ( e ) [ 0 ];

            FilterState element_filter_state = null;
            try
            {
                element_filter_state =
                    this.elementDomain.filter ( element );
            }
            catch ( Exception ex )
            {
                // Probably a ClassCastException.
                element_filter_state = FilterState.DISCARDED;
            }

            kept_elements.set ( (int) e,
                                element_filter_state.isKept () );

            if ( ! element_filter_state.isKept () )
            {
                result = element_filter_state;
                if ( is_abort_on_discard )
                {
                    return result;
                }
            }
        }

        return result;
    }


    /**
     * @see musaico.foundation.domains.array.AbstractArrayDomain#hashCodeDetails()
     */
    @Override
    protected final int hashCodeDetails ()
    {
        return this.elementDomain.hashCode ();
    }


    /**
     * @see musaico.foundation.domains.array.AbstractArrayDomain#toStringDetails(java.lang.String)
     */
    @Override
    protected final String toStringDetails (
                                            String class_name
                                            )
    {
        return class_name
            + " ( "
            + this.elementDomain
            + " )";
    }
}
