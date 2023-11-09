package musaico.foundation.order;


import java.io.Serializable;

import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * Order based on two or more delegate orders, such as
 * "order by X then by Y then by Z".
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
public class CompositeOrder<ORDER extends Object>
    extends AbstractOrder<ORDER>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method parameters for us.
    private static final Advocate classContracts =
        new Advocate ( CompositeOrder.class );


    // The delegate orders.  Each one is used in turn to compare
    //  values, until one returns a non-equal comparison, then the
    //  result is returned.
    private final Order<ORDER> [] orders;

    // Checks method parameter obligations and return value guarantees.
    private final Advocate contracts;


    /**
     * <p>
     * Creates a new CompositeOrder with the specified delegate orders.
     * </p>
     *
     * <p>
     * For example, to create an order which compares first using
     * some order x, then by some order y, then by some order z,
     * until one of them returns a non-equal comparison:
     * </p>
     *
     * <pre>
     *     Order<Foo> x = ...;
     *     Order<Foo> y = ...;
     *     Order<Foo> z = ...;
     *     Order<Foo> composite = new CompositeOrder<Foo> ( x, y, z );
     * </pre>
     *
     * @param orders The delegate orders to compare with.
     *               Must not be null.  Must not contain any null elements.
     */
    @SuppressWarnings ( { "unchecked", // Heap pollution from generic array.
                "rawtypes" } ) // Java insists on raw array constructor.
    public CompositeOrder (
                           Order<ORDER> ... orders
                           )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               (Object []) orders );
        classContracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               orders );

        this.orders = new Order [ orders.length ];
        System.arraycopy ( orders, 0,
                           this.orders, 0, orders.length );

        this.contracts = new Advocate ( this );
    }


    /**
     * @see musaico.foundation.io.Order#compareValues(java.lang.Object,java.lang.Object)
     *
     * Final for speed.
     */
    @Override
    public final Comparison compareValues (
                                           ORDER left,
                                           ORDER right
                                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               left, right );

        for ( Order<ORDER> order : orders )
        {
            Comparison comparison = order.compareValues ( left, right );
            if ( ! comparison.equals ( Comparison.LEFT_EQUALS_RIGHT ) )
            {
                return comparison;
            }

            // Otherwise keep comparing...
        }

        // All orders returned equal.
        return Comparison.LEFT_EQUALS_RIGHT;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
                           Object obj
                           )
    {
        if ( ! super.equals ( obj ) )
        {
            return false;
        }
        else if ( ! ( obj instanceof CompositeOrder ) )
        {
            return false;
        }

        final CompositeOrder<?> that = (CompositeOrder<?>) obj;
        // Paranoid safety check:
        if ( this.orders == null )
        {
            if ( that.orders == null )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else if ( that.orders == null )
        {
            return false;
        }

        if ( this.orders.length != that.orders.length )
        {
            return false;
        }

        for ( int o = 0; o < this.orders.length; o ++ )
        {
            if ( ! this.orders [ o ].equals ( that.orders [ o ] ) )
            {
                return false;
            }
        }

        // Everything is matchy-matchy.
        return true;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        if ( this.orders == null )
        {
            return "{ null }";
        }

        final StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( this.getClass ().getSimpleName () );
        sbuf.append ( " {" );
        boolean is_first = true;
        for ( Order<?> order : this.orders )
        {
            if ( is_first )
            {
                is_first = false;
            }
            else
            {
                sbuf.append ( "," );
            }

            sbuf.append ( " " + order );
        }

        if ( ! is_first )
        {
            sbuf.append ( " " );
        }

        sbuf.append ( "}" );

        return sbuf.toString ();
    }
}
