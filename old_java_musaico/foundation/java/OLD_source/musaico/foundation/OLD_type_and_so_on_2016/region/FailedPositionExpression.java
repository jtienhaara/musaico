package musaico.foundation.region;

import java.io.Serializable;


import musaico.foundation.condition.Failed;

import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * A failed PositionExpression, caused by a specific RegionViolation,
 * which always returns Position.NONE and failed expressions.
 * </p>
 *
 * <p>
 * For example, if a successful PositionExpression's next () method
 * is called, and the next Position would be out of bounds, then
 * a FailedPositionExpression would be returned.  Subsequently, any
 * calls to this failed expression would result in failure: next (),
 * previous (), and so on.  The orNone () method would result in
 * Position.NONE.
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
public class FailedPositionExpression
    extends Failed<Position, RegionViolation>
    implements PositionExpression, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( FailedPositionExpression.class );


    /**
     * <p>
     * Creates a new FailedPositionExpression caused by the
     * specified RegionViolation.
     * </p>
     *
     * @param violation The RegionViolation which caused this
     *                  failed position expression in the first
     *                  place.  This is the original cause of
     *                  failure, which might have been compounded
     *                  by further operations creating new
     *                  failed expressions (such as calling
     *                  next () repeatedly on a FailedPositionExpression).
     *                  The cause of the first failed expression
     *                  must be passed in, regardless of how
     *                  the failure has since been compounded.
     *                  Must not be null.
     */
    public FailedPositionExpression (
                                     RegionViolation violation
                                     )
        throws ParametersMustNotBeNull.Violation
    {
        super ( Position.class,
                violation,
                Position.NONE );
    }


    /**
     * @see musaico.foundation.region.PositionExpression#add(musaico.foundation.region.Size)
     */
    @Override
    public PositionExpression add (
                                   Size size
                                   )
    {
        return this;
    }


    /**
     * @see musaico.foundation.region.PositionExpression#modulo(musaico.foundation.region.Size)
     */
    @Override
    public SizeExpression modulo (
                                  Size size
                                  )
    {
        return new FailedSizeExpression ( this.checkedException () );
    }


    /**
     * @see musaico.foundation.region.PositionExpression#next()
     */
    @Override
    public PositionExpression next ()
    {
        return this;
    }


    /**
     * @see musaico.foundation.region.PositionExpression#previous()
     */
    @Override
    public PositionExpression previous ()
    {
        return this;
    }


    /**
     * @see musaico.foundation.region.PositionExpression#subtract(musaico.foundation.region.Position)
     */
    @Override
    public SizeExpression subtract (
                                    Position that
                                    )
    {
        return new FailedSizeExpression ( this.checkedException () );
    }


    /**
     * @see musaico.foundation.region.PositionExpression#subtract(musaico.foundation.region.Size)
     */
    @Override
    public PositionExpression subtract (
                                        Size size
                                        )
    {
        return this;
    }


    /**
     * @see musaico.foundation.region.PositionExpression#to(musaico.foundation.region.Position)
     */
    @Override
    public RegionExpression to (
                                Position that
                                )
    {
        return new FailedRegionExpression ( this.checkedException () );
    }
}
