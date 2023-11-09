package musaico.foundation.order;


import java.io.Serializable;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * Orders Comparables by their <code> compareTo () </code> methods, and
 * all other objects are compared by their <code> hashCode () </code>s.
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
public class ArbitraryOrder<ORDERED extends Object>
    extends AbstractOrder<ORDERED>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks constructor and static method parameters for us. */
    private static final Advocate classContracts =
        new Advocate ( ArbitraryOrder.class );


    /** Checks method parameter obligations and return value guarantees. */
    private final Advocate contracts;


    /**
     * <p>
     * Creates a new ArbitraryOrder.
     * </p>
     */
    public ArbitraryOrder ()
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
                                           ORDERED left,
                                           ORDERED right
                                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               left, right );

        if ( ( left instanceof Comparable )
             && ( right instanceof Comparable ) )
        {
            try
            {
                final Comparable<ORDERED> left_comparable =
                    (Comparable<ORDERED>) left;
                final int int_comparison =
                    left_comparable.compareTo ( right );
                final Comparison comparison =
                    Comparison.fromDifference ( int_comparison );
                return comparison;
            }
            catch ( Exception e )
            {
                // Can't compare left and right, maybe a ClassCastException.
                // Carry on to using the hash code instead.
            }
        }

        final int int_comparison =
            left.hashCode () - right.hashCode ();
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
        else if ( ! ( obj instanceof ArbitraryOrder ) )
        {
            return false;
        }

        // Every ArbitraryOrder is the same as every other ArbitraryOrder,
        // regardless of generic type, since they all behave the same way.
        return true;
    }
}
