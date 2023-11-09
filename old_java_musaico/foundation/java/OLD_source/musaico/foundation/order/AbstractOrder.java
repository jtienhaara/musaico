package musaico.foundation.order;

import java.io.Serializable;

import java.lang.reflect.Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;


/**
 * <p>
 * Provides Java Comparator compatibility for all Orders.
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
public abstract class AbstractOrder<ORDERED extends Object>
    implements Order<ORDERED>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    // Checks constructor and static method parameters for us.
    private static final Advocate classContracts =
        new Advocate ( AbstractOrder.class );


    // Checks method parameter obligations and return value guarantees.
    private final Advocate objectContracts;



    // Every AbstractOrder must implement Order.compareValues().


    /**
     * <p>
     * Creates a new AbstractOrder.
     * </p>
     */
    public AbstractOrder ()
        throws ParametersMustNotBeNull.Violation
    {
        this.objectContracts = new Advocate ( this );
    }


    /**
     * @see java.util.Comparator#compare(java.lang.Object,java.lang.Object)
     *
     * Final for speed.
     */
    @Override
    public final int compare (
                              ORDERED left,
                              ORDERED right
                              )
    {
        if ( left == null )
        {
            if ( right == null )
            {
                // Null == null.
                return 0;
            }
            else
            {
                // null > any value.
                return Integer.MAX_VALUE;
            }
        }
        else if ( right == null )
        {
            // any value < null.
            return Integer.MIN_VALUE + 1;
        }

        final Comparison comparison = this.compareValues ( left, right );
        return comparison.difference ();
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
        else if ( obj == null
                  || obj.getClass () != this.getClass () )
        {
            return false;
        }

        // Everything is identical.
        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return this.getClass ().getName ().hashCode ();
    }


    /**
     * @see musaico.foundation.order.Order#reverseOrder()
     */
    @Override
    public Order<ORDERED> reverseOrder ()
        throws ReturnNeverNull.Violation
    {
        return new ReverseOrder<ORDERED> ( this );
    }


    /**
     * @see musaico.foundation.io.Order#sort(java.lang.Object[])
     */
    @Override
    @SuppressWarnings("unchecked") // cast sorted_array to ORDERED [].
    public ORDERED [] sort (
                            ORDERED [] array
                            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        this.objectContracts.check ( ParametersMustNotBeNull.CONTRACT,
                                     array );
        this.objectContracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                                     array );

        final ORDERED [] sorted_array = (ORDERED [])
            Array.newInstance ( array.getClass ().getComponentType (),
                                array.length );
        Arrays.sort ( sorted_array, this );
        return sorted_array;
    }


    /**
     * @see musaico.foundation.io.Order#sort(java.lang.Iterable)
     */
    @Override
    public List<ORDERED> sort (
                               Iterable<ORDERED> iterable
                               )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        this.objectContracts.check ( ParametersMustNotBeNull.CONTRACT,
                                     iterable );
        this.objectContracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                                     iterable );

        final List<ORDERED> sorted_list;
        if ( iterable instanceof Collection )
        {
            final Collection<ORDERED> collection =
                (Collection<ORDERED>) iterable;
            sorted_list = new ArrayList<ORDERED> ( collection );
        }
        else
        {
            sorted_list = new ArrayList<ORDERED> ();
            for ( ORDERED element : iterable )
            {
                sorted_list.add ( element );
            }
        }

        Collections.sort ( sorted_list, this );

        return sorted_list;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return this.getClass ().getSimpleName ();
    }
}
