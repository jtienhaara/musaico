package musaico.platform.order;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * Compares Strings by character encoding value
 * (such as their ASCII numeric character values).
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
public class CharacterEncodingOrder
    extends AbstractOrder<String>
    implements Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20131006L;
    private static final String serialVersionHash =
        "0xDB99F857427F0ACBE060FE5082C9E229A06E1D54";


    /** Checks method parameter obligations and return value guarantees. */
    private final ObjectContracts objectContracts;


    /**
     * <p>
     * Creates a new CharacterEncodingOrder.
     * </p>
     */
    public CharacterEncodingOrder ()
    {
        super ( "Character encoding order" );

        this.objectContracts = new ObjectContracts ( this );
    }


    /**
     * @see musaico.foundation.io.Order#compare(java.lang.Object,java.lang.Object)
     *
     * Final for speed.
     */
    public final Comparison compareValues (
                                           String left,
                                           String right
                                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.objectContracts.check ( ParametersMustNotBeNull.CONTRACT,
                                     left, right );

        // any String < / == / > any String.
        int comparator_value = left.compareTo ( right );
        Comparison comparison =
            Comparison.fromComparatorValue ( comparator_value );

        return comparison;
    }
}
