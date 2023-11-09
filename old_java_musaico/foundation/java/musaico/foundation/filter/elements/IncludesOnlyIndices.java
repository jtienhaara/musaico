package musaico.foundation.filter.elements;

import java.io.Serializable;

import java.util.Collection;


import musaico.foundation.filter.KeepAll;
import musaico.foundation.filter.NotNullFilter;

import musaico.foundation.filter.membership.MemberOf;

import musaico.foundation.filter.stream.KeptAllMembers;

import musaico.foundation.structure.Numbers;


/**
 * <p>
 * A Filter that keeps any container that has element(s) only
 * at specific indices.
 * </p>
 *
 * <p>
 * If an included index is outside the bounds of the container
 * being filtered (greater than or equal to its length),
 * then that indexed element will be discarded.
 * </p>
 *
 * <p>
 * If an included index points to a null element in the container
 * being filtered, then that indexed element will be discarded.
 * </p>
 *
 * <p>
 * Otherwise, if an included index points to a non-null element
 * in the container being filtered, then that index will be kept.
 * </p>
 *
 * <p>
 * A container will be kept only if all of the included indices
 * were kept during filtering of the container, and no other
 * indices from outside the included set were kept.
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
 * @see musaico.foundation.filter.elements.MODULE#COPYRIGHT
 * @see musaico.foundation.filter.elements.MODULE#LICENSE
 */
public class IncludesOnlyIndices<ELEMENT extends Object>
    extends AllIndicesFilter<ELEMENT>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // The indices that must be included in a container for it to be kept.
    // No other indices may appear in the container.
    private final long [] indices;


    /**
     * <p>
     * Creates a new IncludesOnlyIndices filter.
     * </p>
     *
     * @param included_indices The array of indices at which there can be
     *                         no elements, or else the conainer being
     *                         filtered will be DISCARDED.  A container
     *                         is KEPT if every index either is outside
     *                         the container's bounds (greater than or
     *                         equal to its length) or points to a null
     *                         element.
     */
    public IncludesOnlyIndices (
            long ... included_indices
            )
    {
        this ( new MemberOf<Long> ( // All members must be kept, and only members will be kept.
                   Numbers.asLongObjects ( included_indices )
                   ),
               IncludesOnlyIndices.duplicate ( included_indices ) );
    }

    private static final long [] duplicate (
            long ... included_indices
            )
    {
        final long [] indices = new long [ included_indices.length ];
        System.arraycopy ( included_indices, 0,
                           indices, 0, included_indices.length );
        return indices;
    }


    /**
     * <p>
     * Creates a new IncludesOnlyIndices filter.
     * </p>
     *
     * @param included_indices The array of indices at which there can be
     *                         no elements, or else the conainer being
     *                         filtered will be DISCARDED.  A container
     *                         is KEPT if every index either is outside
     *                         the container's bounds (greater than or
     *                         equal to its length) or points to a null
     *                         element.
     */
    public IncludesOnlyIndices (
            int ... included_indices
            )
    {
        this ( Numbers.intsToLongs ( included_indices ) );
    }


    /**
     * <p>
     * Creates a new IncludesOnlyIndices filter.
     * </p>
     *
     * @param included_indices The Collection of indices at which there can be
     *                         no elements, or else the conainer being
     *                         filtered will be DISCARDED.  A container
     *                         is KEPT if every index either is outside
     *                         the container's bounds (greater than or
     *                         equal to its length) or points to a null
     *                         element.
     */
    public IncludesOnlyIndices (
            Collection<Long> included_indices
            )
    {
        this ( new MemberOf<Long> ( // All members must be kept, and only members will be kept.
                   included_indices
                   ),
               IncludesOnlyIndices.toPrimitives ( included_indices ) );
    }

    private static final long [] toPrimitives (
            Collection<Long> included_indices
            )
    {
        final long [] indices = new long [ included_indices.size () ];
        int i = 0;
        for ( Long included_index : included_indices )
        {
            if ( included_index == null )
            {
                indices [ i ] = Long.MIN_VALUE;
            }
            else
            {
                indices [ i ] = included_index.longValue ();
            }

            i ++;
        }
        return indices;
    }


    private IncludesOnlyIndices (
            MemberOf<Long> index_filter,
            long [] indices
            )
    {
        super ( index_filter, // The index qualifier ensures only specific indices are present in a container.
                new NotNullFilter<ELEMENT> (), // The element qualifier filters out indices whose elements are null.
                new KeptAllMembers<Long> ( // The quantifier makes sure all indices in the set are included.
                           index_filter
                           ) );

        this.indices = indices;
    }


    /**
     * @return The indices that all must be included in a container in
     *         order for the container to be kept.  Only these indices
     *         may appear in a container; any other indices will lead
     *         to the container being discarded.  Never null.
     */
    public final long [] indices ()
    {
        final long [] indices = new long [ this.indices.length ];
        System.arraycopy ( this.indices, 0,
                           indices, 0, this.indices.length );
        return indices;
    }
}
