package musaico.foundation.region;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


import musaico.foundation.condition.Successful;

import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;

import musaico.foundation.order.Order;


/**
 * <p>
 * A Successful RegionExpression for an empty region.
 * </p>
 *
 * <p>
 * Like a FailedRegionExpression, an EmptyRegionExpression always
 * represents a NoRegion.  However, unlike a FailedRegionExpression,
 * an EmptyRegionExpression can be used to calculate new Regions.
 * Typically an error or exception results in a FailedRegionExpression,
 * whereas an operation that simply leaves no Positions remaining
 * (such as taking the difference of two identical Regions) is allowed
 * and results in a successful EmptyRegionExpression.
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
public class EmptyRegionExpression
    extends Successful<Region, RegionViolation>
    implements RegionExpression, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( EmptyRegionExpression.class );


    /** The RegionViolation to use when creating new NoRegions. */
    private final RegionViolation violation;


    /**
     * <p>
     * Creates a new EmptyRegionExpression for the specified
     * empty Region.
     * </p>
     *
     * @param no_region The empty region which this expression represents.
     *                  Must not be null.
     */
    public EmptyRegionExpression (
                                  NoRegion empty_region
                                  )
        throws ParametersMustNotBeNull.Violation
    {
        super ( Region.class,
                empty_region );

        this.violation = empty_region.checkedException ();
    }


    /**
     * @see musaico.foundation.region.RegionExpression#concatenate(musaico.foundation.region.Region)
     */
    @Override
    public RegionExpression concatenate (
                                         Region that
                                         )
    {
        return that.expr ();
    }


    /**
     * @see musaico.foundation.region.RegionExpression#difference(musaico.foundation.region.Region)
     */
    @Override
    public RegionExpression difference (
                                        Region that
                                        )
    {
        return that.expr ();
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
        return this.orNone ().space ().all ().expr ();
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
        return new FailedSearcher ( this, criteria,
                                    this.violation );
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
        return new NoSparseRegion ( this.violation );
    }


    /**
     * @see musaico.foundation.region.RegionExpression#splitBy(musaico.foundation.region.Size)
     */
    @Override
    public SparseRegion splitBy (
                                 Size size
                                 )
    {
        return new NoSparseRegion ( this.violation );
    }


    /**
     * @see musaico.foundation.region.RegionExpression#splitInto(long)
     */
    @Override
    public SparseRegion splitInto (
                                   long into
                                   )
    {
        return new NoSparseRegion ( this.violation );
    }


    /**
     * @see musaico.foundation.region.RegionExpression#union(musaico.foundation.region.Region)
     */
    @Override
    public RegionExpression union (
                                   Region that
                                   )
    {
        return that.expr ();
    }
}
