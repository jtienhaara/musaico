package musaico.foundation.region;

import java.io.Serializable;

import java.math.BigDecimal;


import musaico.foundation.condition.Successful;

import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * A Successful SizeExpression for a NoSize.
 * </p>
 *
 * <p>
 * Like a FailedSizeExpression, a ZeroSizeExpression always
 * represents Size.NONE.  However, unlike a FailedSizeExpression,
 * a ZeroSizeExpression can be used to calculate new Sizes.
 * Typically an error or exception results in a FailedSizeExpression,
 * whereas an operation that simply leaves no Positions remaining
 * (such as subtracting two identical Sizes) is allowed
 * and results in a successful EmptySizeExpression.
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
public class ZeroSizeExpression
    extends Successful<Size, RegionViolation>
    implements SizeExpression, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( ZeroSizeExpression.class );


    /**
     * <p>
     * Creates a new ZeroSizeExpression.
     * </p>
     */
    public ZeroSizeExpression ()
    {
        super ( Size.class,
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
        return that.expr ();
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
