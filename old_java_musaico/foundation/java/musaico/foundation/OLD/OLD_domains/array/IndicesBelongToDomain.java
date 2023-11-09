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
 * indices all belong to a specific domain.
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
public class IndicesBelongToDomain
    extends AbstractArrayDomain
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // The domain to which every index must belong.
    private final Filter<Number> indexDomain;


    /**
     * <p>
     * Creates a new domain of arrays/collections containing only indices
     * which belong to the specified domain.
     * </p>
     *
     * @param index_domain The domain to which every index of every
     *                     array/collection must belong.  Must not be null.
     */
    public IndicesBelongToDomain (
                                  Filter<Number> index_domain
                                  )
        throws NullPointerException
    {
        if ( index_domain == null )
        {
            throw new NullPointerException ( "Cannot create a IndicesBelongToDomain ( " + index_domain + " )" );
        }

        this.indexDomain = index_domain;
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

        final IndicesBelongToDomain that =
            (IndicesBelongToDomain) array_domain;

        final boolean same_index_domain =
            this.indexDomain.equals ( that.indexDomain );

        return same_index_domain;
    }


    /**
     * @see musaico.foundation.domains.array.AbstractArrayDomain#filterElements(musaico.foundation.domains.array.Elements, java.util.BitSet, boolean)
     */
    @Override
    public final FilterState filterElements (
            Elements<?> array,
            BitSet kept_indices,
            boolean is_abort_on_discard
            )
    {
        final long length = array.length ();

        FilterState result = FilterState.KEPT;
        for ( long index = 0L; index < length; index ++ )
        {
            final FilterState index_filter_state =
                this.indexDomain.filter ( index );

            kept_indices.set ( (int) index,
                               index_filter_state.isKept () );

            if ( ! index_filter_state.isKept () )
            {
                result = index_filter_state;
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
        return this.indexDomain.hashCode ();
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
            + this.indexDomain
            + " )";
    }
}
