package musaico.foundation.contract.obligations;

import java.io.Serializable;

import java.lang.reflect.Array;


import musaico.foundation.contract.AbstractContract;
import musaico.foundation.contract.Contracts;
import musaico.foundation.contract.Obligation;
import musaico.foundation.contract.ObligationUncheckedViolation;


/**
 * <p>
 * A method which throws ParametersMustNotBeNull.Violation
 * expects all parameters to be non-null.  Violators will be sent
 * to arbitration, possibly inducing the runtime exception.
 * </p>
 *
 *
 * <p>
 * In Java, every Contract must implement equals () and hashCode ().
 * </p>
 *
 * <p>
 * In Java, every Contract must be Serializable in order to play
 * nicely over RMI.
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
 * Copyright (c) 2012, 2013 Johann Tienhaara
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
public class ParametersMustNotBeNull
    extends AbstractContract<Object [], ParametersMustNotBeNull.Violation>
    implements Obligation<Object [], ParametersMustNotBeNull.Violation>, Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20130321L;
    private static final String serialVersionHash =
        "0x77661EDBBCA589985860BBD273887E971DA95FE6";


    /** The parameters-must-not-be-null obligation singleton. */
    public static final ParametersMustNotBeNull CONTRACT =
        new ParametersMustNotBeNull ();


    /**
     * <p>
     * Only the singleton or derived classes can access the constructor
     * directly.  Use ParametersMustNotBeNull.CONTRACT instead.
     * </p>
     */
    protected ParametersMustNotBeNull ()
    {
    }


    /**
     * @see musaico.foundation.contract.Contract#enforce(java.lang.Object, java.lang.Object)
     *
     * Final for speed.
     */
    @Override
    public final void enforce (
                               Object object,
                               Object [] inspectable_array
                               )
        throws ParametersMustNotBeNull.Violation
    {
        for ( int p = 0; p < inspectable_array.length; p ++ )
        {
            Object parameter = inspectable_array [ p ];
            if ( parameter == null )
            {
                throw new ParametersMustNotBeNull.Violation ( this,
                                                              object,
                                                              "parameter # "
                                                              + p
                                                              + ": "
                                                              + parameter );
            }
        }
    }


    /**
     * <p>
     * A violation of the parameters-must-not-be-null contract.
     * </p>
     */
    public static class Violation
        extends ObligationUncheckedViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            ParametersMustNotBeNull.serialVersionUID;

        /**
         * <p>
         * Creates a ParametersMustNotBeNull.Violation.
         * </p>
         */
        public Violation (
                          ParametersMustNotBeNull obligation,
                          Object object_under_contract,
                          Object inspectable_data
                          )
        {
            super ( obligation,
                    Contracts.makeSerializable ( object_under_contract ),
                    Contracts.makeSerializable ( inspectable_data ) );
        }
    }
}
