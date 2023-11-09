package musaico.foundation.contract.guarantees;

import java.io.Serializable;


import musaico.foundation.contract.AbstractContract;
import musaico.foundation.contract.Contracts;
import musaico.foundation.contract.Guarantee;
import musaico.foundation.contract.GuaranteeUncheckedViolation;


/**
 * <p>
 * A method which throws ReturnNeverNull.Violation
 * expects the return value to never be null.  If, for some reason,
 * the method cannot keep its own guarantee, it will send itself to
 * arbitration, possibly inducing the runtime exception.
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
public class ReturnNeverNull
    extends AbstractContract<Object, ReturnNeverNull.Violation>
    implements Guarantee<Object, ReturnNeverNull.Violation>, Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20130321L;
    private static final String serialVersionHash =
        "0xAF025F1A9A1589EABCDBC0AF8D5D51C495C90504";


    /** The return-never-null guarantee singleton. */
    public static final ReturnNeverNull CONTRACT =
        new ReturnNeverNull ();


    /**
     * <p>
     * Only the singleton or derived classes can access the constructor
     * directly.  Use ReturnNeverNull.CONTRACT instead.
     * </p>
     */
    protected ReturnNeverNull ()
    {
    }


    /**
     * @see musaico.foundation.contract.Contract#enforce(java.lang.Object, java.lang.Object[])
     */
    @Override
    public void enforce (
                         Object object,
                         Object inspectable_data
                         )
        throws ReturnNeverNull.Violation
    {
        if ( inspectable_data == null )
        {
            throw new ReturnNeverNull.Violation ( this,
                                                  object,
                                                  inspectable_data );
        }
    }


    /**
     * <p>
     * A violation of the return-never-null contract.
     * </p>
     */
    public static class Violation
        extends GuaranteeUncheckedViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            ReturnNeverNull.serialVersionUID;

        /**
         * <p>
         * Creates a ReturnNeverNull.Violation.
         * </p>
         */
        public Violation (
                          ReturnNeverNull guarantee,
                          Object object_under_contract,
                          Object inspectable_data
                          )
        {
            super ( guarantee,
                    Contracts.makeSerializable ( object_under_contract ),
                    Contracts.makeSerializable ( inspectable_data ) );
        }
    }
}
