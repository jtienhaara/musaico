package musaico.foundation.region;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.order.Comparison;
import musaico.foundation.order.Order;


/**
 * <p>
 * Orders Sizes according to their toString () representations,
 * performing a simple String.compare ().
 * </p>
 *
 *
 * <p>
 * In Java, every Order can be used as a Java Comparator.
 * </p>
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
 * @see musaico.foundation.region.MODULE#COPYRIGHT
 * @see musaico.foundation.region.MODULE#LICENSE
 */
public class SizeStringOrder
    implements Order<Size>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( SizeStringOrder.class );


    /**
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare (
                        Size left,
                        Size right
                        )
    {
        if ( left == null )
        {
            if ( right == null )
            {
                // null == null.
                return 0;
            }
            else
            {
                // null > any Size.
                return Integer.MAX_VALUE;
            }
        }
        else if ( right == null )
        {
            // Any Size < null.
            return Integer.MIN_VALUE + 1;
        }

        if ( left instanceof NoSize )
        {
            if ( right instanceof NoSize )
            {
                // NoSize == NoSize.
                return 0;
            }
            else
            {
                // NoSize > any non-null Size.
                return Integer.MAX_VALUE;
            }
        }
        else if ( right instanceof NoSize )
        {
            // Any non-null Size < NoSize.
            return Integer.MIN_VALUE + 1;
        }

        return this.compareValues ( left, right ).comparatorValue ();
    }


    /**
     * @see musaico.foundation.order.Order#compareValues(java.lang.Object, java.lang.Object)
     */
    @Override
    public final Comparison compareValues (
                                           Size left,
                                           Size right
                                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        if ( left instanceof NoSize )
        {
            if ( right instanceof NoSize )
            {
                // NoSize == NoSize.
                return Comparison.LEFT_EQUALS_RIGHT;
            }
            else
            {
                // NoSize > any non-null Size.
                return Comparison.LEFT_GREATER_THAN_RIGHT;
            }
        }
        else if ( right instanceof NoSize )
        {
            // Any non-null Size < NoSize.
            return Comparison.LEFT_LESS_THAN_RIGHT;
        }

        final String left_as_string = "" + left;
        final String right_as_string = "" + right;
        final int comp = left_as_string.compareToIgnoreCase ( right_as_string );

        return Comparison.fromComparatorValue ( comp );
    }


    /**
     * @see musaico.foundation.order.Order#id()
     */
    @Override
    public String id ()
        throws ReturnNeverNull.Violation
    {
        return ClassName.of ( this.getClass () );
    }


    /**
     * @see musaico.foundation.order.Order#sort(java.lang.Object[])
     */
    @Override
    public Size [] sort (
                         Size [] array
                         )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        final Size [] sorted_array = new Size [ array.length ];
        System.arraycopy ( array, 0,
                           sorted_array, 0, array.length );

        Arrays.sort ( sorted_array, this );

        return sorted_array;
    }


    /**
     * @see musaico.foundation.order.Order#sort(java.util.Collection)
     */
    @Override
    public List<Size> sort (
                            Collection<Size> collection
                            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        final List<Size> sorted_list =
            new ArrayList<Size> ( collection );

        Collections.sort ( sorted_list, this );

        return sorted_list;
    }
}
