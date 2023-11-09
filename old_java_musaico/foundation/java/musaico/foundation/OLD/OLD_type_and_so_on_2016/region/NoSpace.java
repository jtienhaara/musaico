package musaico.foundation.region;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.order.Order;


/**
 * <p>
 * No Space at all.  Use Space.NONE instead.
 * </p>
 *
 *
 * <p>
 * In Java, every Space must be Serializable in order to play nicely
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
 * @see musaico.foundation.region.MODULE#COPYRIGHT
 * @see musaico.foundation.region.MODULE#LICENSE
 */
/* Package private: */
class NoSpace
    implements Space, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( NoSpace.class );


    /** The violation we re-use every time we have to create a new
     *  failed spatial element expression (FailedPositionExpression,
     *  FailedRegionExpression, and so on). */
    private final RegionViolation violation =
        new RegionViolation ( RegionObligation.REGION_MUST_NOT_BE_EMPTY,
                              Space.class,
                              this );

    /** The order of the Positions in this Space (which is empty,
     *  so the order is most likely irrelevant). */
    private final Order<Position> order;


    /**
     * <p>
     * Creates a new NoSpace with the default order (by ASCii string
     * comparison of Position.toString () ).
     * </p>
     */
    public NoSpace ()
    {
        this ( Space.ORDER_BY_POSITION_STRING );
    }


    /**
     * <p>
     * Creates a new NoSpace with the specified ordering of Positions.
     * </p>
     *
     * <p>
     * The order of a NoSpace is almost always irrelevant, since there
     * are no Positions to sort anyway.
     * </p>
     *
     * @param order The order of Positions in this NoSpace.
     *              Must not be null.
     */
    public NoSpace (
                    Order<Position> order
                    )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               order );

        this.order = order;
    }


    /**
     * @see musaico.foundation.region.Space#all()
     */
    @Override
    public Region all ()
        throws ReturnNeverNull.Violation
    {
        return new NoRegion ( this.violation );
    }


    /**
     * @see musaico.foundation.region.Space#empty()
     */
    @Override
    public Region empty ()
        throws ReturnNeverNull.Violation
    {
        return new NoRegion ( this.violation );
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals ( Object object )
    {
        if ( object instanceof NoSpace )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * @see musaico.foundation.region.Space#expr(musaico.foundation.region.Position)
     */
    @Override
    public PositionExpression expr (
                                    Position position
                                    )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        final Space that_space = position.space ();
        if ( that_space != this )
        {
            return that_space.expr ( position );
        }

        return new FailedPositionExpression ( this.violation );
    }


    /**
     * @see musaico.foundation.region.Space#expr(musaico.foundation.region.Region)
     */
    @Override
    public RegionExpression expr (
                                  Region region
                                  )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        final Space that_space = region.space ();
        if ( that_space != this )
        {
            return that_space.expr ( region );
        }

        return new FailedRegionExpression ( this.violation );
    }


    /**
     * @see musaico.foundation.region.Space#expr(musaico.foundation.region.Size)
     */
    @Override
    public SizeExpression expr (
                                Size size
                                )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        final Space that_space = size.space ();
        if ( that_space != this )
        {
            return that_space.expr ( size );
        }

        return new FailedSizeExpression ( this.violation );
    }


    /**
     * @see musaico.foundation.region.Space#from(musaico.foundation.region.Position)
     */
    @Override
    public FailedPositionExpression from (
                                          Position that_position
                                          )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return new FailedPositionExpression ( this.violation );
    }


    /**
     * @see musaico.foundation.region.Space#from(musaico.foundation.region.Region)
     */
    @Override
    public FailedRegionExpression from (
                                        Region that_region
                                        )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return new FailedRegionExpression ( this.violation );
    }


    /**
     * @see musaico.foundation.region.Space#from(musaico.foundation.region.Size)
     */
    @Override
    public FailedSizeExpression from (
                                      Size that_size
                                      )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return new FailedSizeExpression ( this.violation );
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
     * @see musaico.foundation.region.Space#max()
     */
    @Override
    public Position max ()
        throws ReturnNeverNull.Violation
    {
        return Position.NONE;
    }


    /**
     * @see musaico.foundation.region.Space#min()
     */
    @Override
    public Position min ()
        throws ReturnNeverNull.Violation
    {
        return Position.NONE;
    }


    /**
     * @see musaico.foundation.region.Space#one()
     */
    @Override
    public Size one ()
        throws ReturnNeverNull.Violation
    {
        return Size.NONE;
    }


    /**
     * @see musaico.foundation.region.Space#order()
     */
    @Override
    public Order<Position> order ()
        throws ReturnNeverNull.Violation
    {
        return this.order;
    }


    /**
     * @see musaico.foundation.region.Space#order(musaico.foundation.order.Order)
     */
    @Override
    public Space order (
                        Order<Position> order
                        )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return new NoSpace ( order );
    }


    /**
     * @see musaico.foundation.region.Space#origin()
     */
    @Override
    public Position origin ()
        throws ReturnNeverNull.Violation
    {
        return Position.NONE;
    }


    /**
     * @see musaico.foundation.region.Space#region(musaico.foundation.region.Position, musaico.foundation.region.Position)
     */
    @Override
    public RegionExpression region (
                                          Position start,
                                          Position end
                                          )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        final Space that_space = start.space ();
        if ( that_space != this )
        {
            return that_space.region ( start, end );
        }

        return new FailedRegionExpression ( this.violation );
    }


    /**
     * @see musaico.foundation.region.Space#sparseRegionBuilder()
     */
    @Override
    public SparseRegionBuilder sparseRegionBuilder ()
        throws ReturnNeverNull.Violation
    {
        return new SparseRegionBuilder ( this );
    }
}
