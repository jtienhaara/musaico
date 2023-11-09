package musaico.platform.order;

import java.io.Serializable;

import java.lang.reflect.Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


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
 * <pre>
 * Copyright (c) 2011, 2013 Johann Tienhaara
 * All rights reserved.
 *
 * This file is part of Musaico.
 *
 * Musaico is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Musaico is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Musaico.  If not, see &lt;http://www.gnu.org/licenses/&gt;.
 * </pre>
 */
public abstract class AbstractOrder<ORDER extends Object>
    implements Order<ORDER>, Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20131006L;
    private static final String serialVersionHash =
        "0xDB99F857427F0ACBE060FE5082C9E229A06E1D54";


    /** Checks constructor and static method parameters for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( AbstractOrder.class );


    /** The unique id of this order. */
    private final String id;

    /** Checks method parameter obligations and return value guarantees. */
    private final ObjectContracts objectContracts;



    // Every AbstractOrder must implement Order.compareValues().


    /**
     * <p>
     * Creates a new AbstractOrder with the specified id.
     * </p>
     *
     * @param id The unique identifier for this order.  Must not be null.
     */
    public AbstractOrder (
                          String id
                          )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               id );

        this.id = id;

        this.objectContracts = new ObjectContracts ( this );
    }


    /**
     * <p>
     * Creates a new AbstractOrder using this order's class name as
     * the unique identifier.
     * </p>
     */
    public AbstractOrder ()
    {
        this.id = this.getClass ().getSimpleName ();

        this.objectContracts = new ObjectContracts ( this );
    }


    /**
     * @see java.util.Comparator#compare(java.lang.Object,java.lang.Object)
     *
     * Final for speed.
     */
    @Override
    public final int compare (
                              ORDER left,
                              ORDER right
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
        return comparison.comparatorValue ();
    }


    /**
     * @see musaico.foundation.io.Order#id()
     */
    @Override
    public final String id ()
        throws ReturnNeverNull.Violation
    {
        return this.id;
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

        final AbstractOrder<?> that = (AbstractOrder) obj;
        final String that_id = that.id ();
        if ( ! this.id.equals ( that_id ) )
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
        return this.id ().hashCode ();
    }


    /**
     * @see musaico.foundation.io.Order#sort(java.lang.Object[])
     */
    @Override
    @SuppressWarnings("unchecked") // cast sorted_array to ORDER [].
    public ORDER [] sort (
                          ORDER [] array
                          )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        this.objectContracts.check ( ParametersMustNotBeNull.CONTRACT,
                                     array );
        this.objectContracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                                     array );

        ORDER [] sorted_array = (ORDER [])
            Array.newInstance ( array.getClass ().getComponentType (),
                                array.length );
        Arrays.sort ( sorted_array, this );
        return sorted_array;
    }


    /**
     * @see musaico.foundation.io.Order#sort(java.util.Collection)
     */
    @Override
    public List<ORDER> sort (
                             Collection<ORDER> collection
                             )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        this.objectContracts.check ( ParametersMustNotBeNull.CONTRACT,
                                     collection );
        this.objectContracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                                     collection );

        List<ORDER> sorted_list = new ArrayList<ORDER> ( collection );
        Collections.sort ( sorted_list, this );
        return sorted_list;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return this.id ();
    }
}
