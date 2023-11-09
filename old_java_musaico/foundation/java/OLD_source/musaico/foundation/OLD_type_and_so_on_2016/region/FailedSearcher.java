package musaico.foundation.region;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;


/**
 * <p>
 * A failed search operation.
 * </p>
 *
 * <p>
 * For example, a search request performed on a FailedRegionExpression.
 * </p>
 *
 *
 * <p>
 * In Java, every Searcher must be Serializable.
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
public class FailedSearcher
    implements Searcher, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( FailedSearcher.class );


    /** The RegionExpression on which the search failed. */
    private final RegionExpression regionExpression;

    /** The search criteri(on/a) which failed. */
    private final Filter<Position> [] criteria;

    /** The cause of the failure. */
    private final RegionViolation violation;


    /**
     * <p>
     * Creates a new FailedSearcher for the specified criteria on the
     * specified Region, which failed because of the specified
     * RegionViolation.
     * </p>
     *
     * @param region_expression The RegionExpression which was to be
     *                          searched.  Must not be null.
     *
     * @param criteria The criteria to search for, each a Filter
     *                 which matches positions in the region search area.
     *                 Must not be null.  Must not contain any null elements.
     *
     * @param violation The violation which induced this failed search.
     *                  Must not be null.
     */
    public FailedSearcher (
                           RegionExpression region_expression,
                           Filter<Position> [] criteria,
                           RegionViolation violation
                           )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               region_expression, criteria, violation );
        classContracts.check ( Parameter2.MustContainNoNulls.CONTRACT,
                               criteria );

        this.regionExpression = region_expression;
        this.criteria = criteria;
        this.violation = violation;
    }


    /**
     * @see musaico.foundation.region.Searcher#criteria()
     */
    @Override
    public Filter<Position> [] criteria ()
    {
        return this.criteria;
    }


    /**
     * @see musaico.foundation.region.Searcher#find()
     */
    @Override
    public PositionExpression find ()
    {
        return new FailedPositionExpression ( this.violation );
    }


    /**
     * @see musaico.foundation.region.Searcher#findAll()
     */
    @Override
    public RegionExpression findAll ()
    {
        return new FailedRegionExpression ( this.violation );
    }


    /**
     * @see musaico.foundation.region.Searcher#regionSearchArea()
     */
    @Override
    public RegionExpression regionSearchArea ()
    {
        return this.regionExpression;
    }
}
