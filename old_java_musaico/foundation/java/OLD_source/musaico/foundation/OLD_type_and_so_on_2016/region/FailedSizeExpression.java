package musaico.foundation.region;

import java.io.Serializable;

import java.math.BigDecimal;


import musaico.foundation.condition.Failed;

import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * A failed Size expression, which always returns Size.NONE, and
 * always generates failed expressions.
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
public class FailedSizeExpression
    extends Failed<Size, RegionViolation>
    implements SizeExpression, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( FailedSizeExpression.class );


    /**
     * <p>
     * Creates a new FailedSizeExpression caused by the
     * specified RegionViolation.
     * </p>
     *
     * @param violation The RegionViolation which caused this
     *                  failed size expression in the first
     *                  place.  This is the original cause of
     *                  failure, which might have been compounded
     *                  by further operations creating new
     *                  failed expressions (such as calling
     *                  add ( ... ) repeatedly on a FailedSizeExpression).
     *                  The cause of the first failed expression
     *                  must be passed in, regardless of how
     *                  the failure has since been compounded.
     *                  Must not be null.
     */
    public FailedSizeExpression (
                                 RegionViolation violation
                                 )
        throws ParametersMustNotBeNull.Violation
    {
        super ( Size.class,
                violation,
                Size.NONE );
    }


    /**
     * @see musaico.foundation.region.SizeExpression#add(musaico.foundation.region.Size)
     */
    public SizeExpression add (
                               Size that
                               )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return this;
    }


    /**
     * @see musaico.foundation.region.SizeExpression#divide(java.math.BigDecimal)
     */
    public SizeExpression divide (
                                  BigDecimal divisor
                                  )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return this;
    }


    /**
     * @see musaico.foundation.region.SizeExpression#modulo(musaico.foundation.region.Size)
     */
   public SizeExpression modulo (
                                 Size size
                                 )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
   {
       return this;
   }


    /**
     * @see musaico.foundation.region.SizeExpression#multiply(java.math.BigDecimal)
     */
    public SizeExpression multiply (
                                    BigDecimal factor
                                    )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return this;
    }


    /**
     * @see musaico.foundation.region.SizeExpression#ratio(musaico.foundation.region.Size)
     */
    public BigDecimal ratio (
                             Size that
                             )
        throws ParametersMustNotBeNull.Violation
    {
        return BigDecimal.ZERO;
    }


    /**
     * @see musaico.foundation.region.SizeExpression#subtract(musaico.foundation.region.Size)
     */
    public SizeExpression subtract (
                                    Size that
                                    )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return this;
    }
}
