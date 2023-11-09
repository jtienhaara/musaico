package musaico.foundation.order;


import java.io.Serializable;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * Orders Comparables by their <code> compareTo () </code> methods.
 * </p>
 *
 *
 * <p>
 * In Java, every Order is Serializable in order to play nicely
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
 * @see musaico.foundation.order.MODULE#COPYRIGHT
 * @see musaico.foundation.order.MODULE#LICENSE
 */
public class NaturalOrder<COMPARABLE extends Comparable<COMPARABLE>>
    extends AbstractOrder<COMPARABLE>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks constructor and static method parameters for us. */
    private static final Advocate classContracts =
        new Advocate ( NaturalOrder.class );


    /** Checks method parameter obligations and return value guarantees. */
    private final Advocate contracts;


    /**
     * <p>
     * Creates a new NaturalOrder.
     * </p>
     */
    public NaturalOrder ()
    {
        this.contracts = new Advocate ( this );
    }


    /**
     * @see musaico.foundation.io.Order#compare(java.lang.Object,java.lang.Object)
     *
     * Final for speed.
     */
    @Override
    @SuppressWarnings("unchecked") // Try...cast...catch.
    public final Comparison compareValues (
                                           COMPARABLE left,
                                           COMPARABLE right
                                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               left, right );

        final int int_comparison = left.compareTo ( right );
        final Comparison comparison =
            Comparison.fromDifference ( int_comparison );
        return comparison;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
                           Object obj
                           )
    {
        if ( obj == this )
        {
            return true;
        }
        else if ( obj == null )
        {
            return false;
        }
        else if ( ! super.equals ( obj ) )
        {
            return false;
        }
        else if ( ! ( obj instanceof NaturalOrder ) )
        {
            return false;
        }

        // Every NaturalOrder is the same as every other NaturalOrder,
        // regardless of generic type, since they all behave the same way.
        return true;
    }
}
