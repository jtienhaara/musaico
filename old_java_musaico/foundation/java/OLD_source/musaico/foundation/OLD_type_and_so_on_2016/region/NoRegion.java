package musaico.foundation.region;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;

import musaico.foundation.value.NoValue;


/**
 * <p>
 * No region of data at all.
 * </p>
 *
 *
 * <p>
 * In Java every Region must implement hashCode () and equals () in
 * order to play nicely with HashMaps.
 * </p>
 *
 * <p>
 * in Java every Region must be Serializable in order to
 * play nicely over RMI.
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
public class NoRegion
    extends NoValue<Position, RegionViolation>
    implements Region, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( NoRegion.class );


    /**
     * <p>
     * Creates a new NoRegion caused by the specified violation
     * (such as "start/end position is out of bounds" or
     * "start position must be before end position" and so on).
     * </p>
     *
     * @param violation The RegionViolation which led to the
     *                  creation of this NoRegion.  Must not be null.
     */
    public NoRegion (
                     RegionViolation violation
                     )
    {
        super ( Position.class,
                violation,
                Position.NONE );
    }


    /**
     * @see musaico.foundation.region.Region#contains(musaico.foundation.region.Position)
     */
    @Override
    public boolean contains (
                             Position position
                             )
        throws ParametersMustNotBeNull.Violation
    {
        return false;
    }


    /**
     * @see musaico.foundation.region.Region#end()
     */
    @Override
    public Position end ()
        throws ReturnNeverNull.Violation
    {
        return Position.NONE;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals ( Object object )
    {
        // Every NoRegion is considered equal, even
        // though any two NoRegions might have different
        // causes.
        if ( object instanceof NoRegion )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * @see musaico.foundation.region.Region#expr()
     */
    @Override
    public RegionExpression expr ()
    {
        return new StandardRegionExpression ( this );
    }


    /**
     * @see musaico.foundation.region.Region#expr(musaico.foundation.region.Position)
     */
    @Override
    public FailedPositionExpression expr (
                                          Position position
                                          )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return new FailedPositionExpression ( this.checkedException () );
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return 0;
    }


    /**
     * @see musaico.foundation.region.Region#searcher(musaico.foundation.region.RegionExpression, musaico.foundation.filter.Filter[])
     */
    @Override
    @SuppressWarnings("unchecked") // Heap pollution<generic varargs>
    public Searcher searcher (
                              RegionExpression region_expression,
                              Filter<Position> ... criteria
                              )
    {
        return new FailedSearcher ( region_expression, criteria,
                                    this.checkedException () );
    }


    /**
     * @see musaico.foundation.region.Region#size()
     */
    @Override
    public Size size ()
        throws ReturnNeverNull.Violation
    {
        return Size.NONE;
    }


    /**
     * @see musaico.foundation.region.SpatialElement#space()
     */
    @Override
    public Space space ()
        throws ReturnNeverNull.Violation
    {
        return Space.NONE;
    }


    /**
     * @see musaico.foundation.region.Region#start()
     */
    @Override
    public Position start ()
        throws ReturnNeverNull.Violation
    {
        return Position.NONE;
    }
}
