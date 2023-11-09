package musaico.foundation.region;

import java.io.Serializable;


import musaico.foundation.condition.Successful;

import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * A successful PositionExpression which starts from a specific Position
 * inside a specific Region, and can be transformed into a new Position.
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
public class RegionalPositionExpression
    extends Successful<Position, RegionViolation>
    implements PositionExpression, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( RegionalPositionExpression.class );


    /** All Positions, Regions and Sizes passed to this expression must
     *  belong to the same Space. */
    private final RegionObligation<SpatialElement> spaceContract;

    /** All Positions passed to this expression must belong to the
     *  underlying Region. */
    private final RegionObligation<Position> regionContract;

    /** The region in which to perform the operations.
     *  Can be a Space.all () region (everything). */
    private final Region region;

    /** Checks parameters to constructors and static methods for us. */
    private final ObjectContracts contracts;


    /**
     * <p>
     * Creates a new successful RegionalPositionExpression, taking the
     * specified term as the start of the expression.
     * </p>
     *
     * @param position The position from which to start
     *                 the expression.  Must not be null.
     *
     * @param region The region over which to perform
     *               calculations for this expression.
     *               Must not be null.  Must have the
     *               same Space as the specified Position.
     */
    public RegionalPositionExpression (
                                       Position position,
                                       Region region
                                       )
        throws ParametersMustNotBeNull.Violation,
               RegionViolation
    {
        super ( Position.class,
                position );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               position, region );

        this.spaceContract =
            new RegionObligation<SpatialElement> ( new DomainElementBelongsToSpace ( position.space () ) );
        this.regionContract =
            new RegionObligation<Position> ( new DomainPositionBelongsToRegion ( region ) );

        classContracts.check ( this.spaceContract,
                               region );
        classContracts.check ( this.regionContract,
                               position );

        this.region = region;
        // We can retrieve the position by calling this.orNone().

        this.contracts = new ObjectContracts ( this );
    }


    /**
     * @see musaico.foundation.region.PositionExpression#add(musaico.foundation.region.Size)
     */
    @Override
    public PositionExpression add (
                                   Size size
                                   )
    {
        try
        {
            this.contracts.check ( this.spaceContract,
                                   size );
        }
        catch ( RegionViolation violation )
        {
            return new FailedPositionExpression ( violation );
        }

        final Position position = this.orNone ();
        PositionExpression added = position.expr ().add ( size );
        if ( ! ( added instanceof Successful ) )
        {
            return added;
        }

        try
        {
            this.contracts.check ( this.regionContract,
                                   added.orNone () );
        }
        catch ( RegionViolation violation )
        {
            return new FailedPositionExpression ( violation );
        }

        return added;
    }


    /**
     * @see musaico.foundation.region.PositionExpression#modulo(musaico.foundation.region.Size)
     */
    @Override
    public SizeExpression modulo (
                                  Size size
                                  )
    {
        try
        {
            this.contracts.check ( this.spaceContract,
                                   size );
        }
        catch ( RegionViolation violation )
        {
            return new FailedSizeExpression ( violation );
        }

        // Just use the regular (non-regional) modulo operation.
        final Position position = this.orNone ();
        return position.expr ().modulo ( size );
    }


    /**
     * @see musaico.foundation.region.PositionExpression#next()
     */
    @Override
    public PositionExpression next ()
    {
        final Position position = this.orNone ();
        Position stepped_position =
            position.expr ().next ().orNone ();
        final PositionExpression next;
        try
        {
            this.contracts.check ( this.regionContract,
                                   stepped_position );
            next = new RegionalPositionExpression ( stepped_position,
                                                    this.region );
        }
        catch ( RegionViolation violation )
        {
            return new FailedPositionExpression ( violation );
        }

        return next;
    }


    /**
     * @see musaico.foundation.region.PositionExpression#previous()
     */
    @Override
    public PositionExpression previous ()
    {
        final Position position = this.orNone ();
        Position stepped_position =
            position.expr ().previous ().orNone ();
        final PositionExpression previous;
        try
        {
            this.contracts.check ( this.regionContract,
                                   stepped_position );
            previous = new RegionalPositionExpression ( stepped_position,
                                                        this.region );
        }
        catch ( RegionViolation violation )
        {
            return new FailedPositionExpression ( violation );
        }

        return previous;
    }


    /**
     * @see musaico.foundation.region.PositionExpression#subtract(musaico.foundation.region.Position)
     */
    @Override
    public SizeExpression subtract (
                                    Position that
                                    )
    {
        try
        {
            this.contracts.check ( this.spaceContract,
                                   that );
        }
        catch ( RegionViolation violation )
        {
            return new FailedSizeExpression ( violation );
        }

        // Just use the regular (non-regional) subtract operation.
        final Position position = this.orNone ();
        return position.expr ().subtract ( that );
    }


    /**
     * @see musaico.foundation.region.PositionExpression#subtract(musaico.foundation.region.Size)
     */
    @Override
    public PositionExpression subtract (
                                        Size size
                                        )
    {
        try
        {
            this.contracts.check ( this.spaceContract,
                                   size );
        }
        catch ( RegionViolation violation )
        {
            return new FailedPositionExpression ( violation );
        }

        final Position position = this.orNone ();
        final PositionExpression subtracted =
            position.expr ().subtract ( size );

        if ( ! ( subtracted instanceof Successful ) )
        {
            return subtracted;
        }

        try
        {
            this.contracts.check ( this.regionContract,
                                   subtracted.orNone () );
        }
        catch ( RegionViolation violation )
        {
            return new FailedPositionExpression ( violation );
        }

        return subtracted;
    }


    /**
     * @see musaico.foundation.region.PositionExpression#to(musaico.foundation.region.Position)
     */
    @Override
    public RegionExpression to (
                                Position that
                                )
    {
        try
        {
            this.contracts.check ( this.spaceContract,
                                   that );
            this.contracts.check ( this.regionContract,
                                   that );
        }
        catch ( RegionViolation violation )
        {
            return new FailedRegionExpression ( violation );
        }

        // Start with the regular (non-regional) region operation.
        final Position position = this.orNone ();
        final RegionExpression region_expression =
            position.expr ().to ( that );

        // Now in case our Region is sparse (has holes in it),
        // find the intersection between our region and the newly
        // generated one.
        if ( this.region instanceof SparseRegion )
        {
            final RegionExpression sparse_region_expression =
                region_expression.intersection ( this.region );
            return sparse_region_expression;
        }

        return region_expression;
    }
}
