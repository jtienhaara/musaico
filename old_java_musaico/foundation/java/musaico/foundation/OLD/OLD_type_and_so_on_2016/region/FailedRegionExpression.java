package musaico.foundation.region;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


import musaico.foundation.condition.Failed;

import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;

import musaico.foundation.order.Order;


/**
 * <p>
 * A failed RegionExpression, which always returns Region.NONE and
 * failed expressions.
 * </p>
 *
 * <p>
 * A FailedRegionExpression might be created, for example, by
 * attempting to create a new region from an invalid start and
 * end position.
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
 * @see musaico.foundation.region.MODULE#COPYRIGHT
 * @see musaico.foundation.region.MODULE#LICENSE
 */
public class FailedRegionExpression
    extends Failed<Region, RegionViolation>
    implements RegionExpression, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( FailedRegionExpression.class );


    /**
     * <p>
     * Creates a new FailedRegionExpression caused by the
     * specified RegionViolation.
     * </p>
     *
     * @param violation The RegionViolation which caused this
     *                  failed region expression in the first
     *                  place.  This is the original cause of
     *                  failure, which might have been compounded
     *                  by further operations creating new
     *                  failed expressions (such as calling
     *                  next () repeatedly on a FailedRegionExpression).
     *                  The cause of the first failed expression
     *                  must be passed in, regardless of how
     *                  the failure has since been compounded.
     *                  Must not be null.
     */
    public FailedRegionExpression (
                                   RegionViolation violation
                                   )
        throws ParametersMustNotBeNull.Violation
    {
        super ( Region.class,
                violation,
                new NoRegion ( violation ) );
    }


    /**
     * @see musaico.foundation.region.RegionExpression#concatenate(musaico.foundation.region.Region)
     */
    @Override
    public RegionExpression concatenate (
                                         Region that
                                         )
    {
        return new FailedRegionExpression ( this.checkedException () );
    }


    /**
     * @see musaico.foundation.region.RegionExpression#difference(musaico.foundation.region.Region)
     */
    @Override
    public RegionExpression difference (
                                        Region that
                                        )
    {
        return this;
    }


    /**
     * @see musaico.foundation.region.RegionExpression#exclude(musaico.foundation.region.Region)
     */
    @Override
    public RegionExpression exclude (
                                     Region that
                                     )
    {
        return this;
    }


    /**
     * @see musaico.foundation.region.RegionExpression#intersection(musaico.foundation.region.Region)
     */
    @Override
    public RegionExpression intersection (
                                          Region that
                                          )
    {
        return this;
    }


    /**
     * @see musaico.foundation.region.RegionExpression#invert()
     */
    @Override
    public RegionExpression invert ()
    {
        return this;
    }


    /**
     * @see musaico.foundation.region.RegionExpression#scale(musaico.foundation.region.Size)
     */
    @Override
    public RegionExpression scale (
                                   Size size
                                   )
    {
        return this;
    }


    /**
     * @see musaico.foundation.region.RegionExpression#search(musaico.foundation.region.Filter[])
     */
    @Override
    @SuppressWarnings("unchecked") // Heap pollution<generic varargs>
    public Searcher search (
                            Filter<Position> ... criteria
                            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        // Nothing to search through.
        return new FailedSearcher ( this, criteria, this.checkedException () );
    }


    /**
     * @see musaico.foundation.region.RegionExpression#sort(musaico.foundation.order.Order)
     */
    @Override
    public RegionExpression sort (
                                  Order<Position> order
                                  )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        // Nothing to sort.
        return this;
    }


    /**
     * @see musaico.foundation.region.RegionExpression#sparseRegionOrNone()
     */
    public SparseRegion sparseRegionOrNone ()
    {
        return new NoSparseRegion ( this.checkedException () );
    }


    /**
     * @see musaico.foundation.region.RegionExpression#splitBy(musaico.foundation.region.Size)
     */
    @Override
    public SparseRegion splitBy (
                                 Size size
                                 )
    {
        return new NoSparseRegion ( this.checkedException () );
    }


    /**
     * @see musaico.foundation.region.RegionExpression#splitInto(long)
     */
    @Override
    public SparseRegion splitInto (
                                   long into
                                   )
    {
        return new NoSparseRegion ( this.checkedException () );
    }


    /**
     * @see musaico.foundation.region.RegionExpression#union(musaico.foundation.region.Region)
     */
    @Override
    public RegionExpression union (
                                   Region that
                                   )
    {
        return this;
    }
}
