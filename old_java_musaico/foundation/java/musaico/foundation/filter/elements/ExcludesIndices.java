package musaico.foundation.filter.elements;

import java.io.Serializable;

import java.util.Collection;


import musaico.foundation.filter.KeepAll;
import musaico.foundation.filter.NotNullFilter;

import musaico.foundation.filter.stream.KeptNoMembers;

import musaico.foundation.structure.Numbers;


/**
 * <p>
 * A Filter that keeps any container that does not have any elements
 * at a specific set of indices.
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
 * A container will be kept only if none of the included indices
 * were kept during filtering of the container.
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
public class ExcludesIndices<ELEMENT extends Object>
    extends AllIndicesFilter<ELEMENT>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // The indices that must be excluded in a container for it to be kept.
    private final long [] indices;


    /**
     * <p>
     * Creates a new ExcludesIndices filter.
     * </p>
     *
     * @param excluded_indices The array of indices at which there can be
     *                         no elements, or else the conainer being
     *                         filtered will be DISCARDED.  A container
     *                         is KEPT if every index either is outside
     *                         the container's bounds (greater than or
     *                         equal to its length) or points to a null
     *                         element.
     */
    public ExcludesIndices (
            long ... excluded_indices
            )
    {
        super ( new KeepAll<Long> (), // The index qualifier just keeps every index.
                new NotNullFilter<ELEMENT> (), // The element qualifier filters out indices whose elements are null.
                new KeptNoMembers<Long> ( // The quantifier makes sure all indices in the set are excluded.
                           Numbers.asLongObjects ( excluded_indices )
                           ) );

        this.indices = new long [ excluded_indices.length ];
        System.arraycopy ( excluded_indices, 0,
                           this.indices, 0, excluded_indices.length );
    }


    /**
     * <p>
     * Creates a new ExcludesIndices filter.
     * </p>
     *
     * @param excluded_indices The array of indices at which there can be
     *                         no elements, or else the conainer being
     *                         filtered will be DISCARDED.  A container
     *                         is KEPT if every index either is outside
     *                         the container's bounds (greater than or
     *                         equal to its length) or points to a null
     *                         element.
     */
    public ExcludesIndices (
            int ... excluded_indices
            )
    {
        this ( Numbers.intsToLongs ( excluded_indices ) );
    }


    /**
     * <p>
     * Creates a new ExcludesIndices filter.
     * </p>
     *
     * @param excluded_indices The Collection of indices at which there can be
     *                         no elements, or else the conainer being
     *                         filtered will be DISCARDED.  A container
     *                         is KEPT if every index either is outside
     *                         the container's bounds (greater than or
     *                         equal to its length) or points to a null
     *                         element.
     */
    public ExcludesIndices (
            Collection<Long> excluded_indices
            )
    {
        super ( new KeepAll<Long> (), // The index qualifier just keeps every index.
                new NotNullFilter<ELEMENT> (), // The element qualifier filters out indices whose elements are null.
                new KeptNoMembers<Long> ( // The quantifier makes sure all indices in the set are excluded.
                           excluded_indices
                           ) );

        this.indices = new long [ excluded_indices.size () ];
        int i = 0;
        for ( Long excluded_index : excluded_indices )
        {
            if ( excluded_index == null )
            {
                this.indices [ i ] = Long.MIN_VALUE;
            }
            else
            {
                this.indices [ i ] = excluded_index.longValue ();
            }

            i ++;
        }
    }


    /**
     * @return The indices that must be excluded from a container in
     *         order for the container to be kept.  Never null.
     */
    public final long [] indices ()
    {
        final long [] indices = new long [ this.indices.length ];
        System.arraycopy ( this.indices, 0,
                           indices, 0, this.indices.length );
        return indices;
    }
}
