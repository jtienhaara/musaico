package musaico.foundation.contract;

import java.io.Serializable;


/**
 * <p>
 * Inspects all contracts and enforces them by throwing their
 * respective contract violations.
 * </p>
 *
 *
 * <p>
 * In Java, every Arbiter must be Serializable in order to play
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
 * Copyright (c) 2012-2014 Johann Tienhaara
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
public final class StandardArbiter
    implements Arbiter
{
    private static final long serialVersionUID = 20140413L;
    private static final String serialVersionHash =
        "0xB965EDFF1CDFA2DC2B78CA1A2301013BEAD05D18";


    /**
     * @see musaico.foundation.contract.Arbiter#inspect(musaico.foundation.contract.Contract, java.lang.Object, java.lang.Object)
     */
    @Override
    public final
        <INSPECTABLE extends Object, VIOLATION extends Throwable>
                           void inspect (
                                         Contract<INSPECTABLE, VIOLATION> contract,
                                         Object object_under_contract,
                                         INSPECTABLE inspectable_data
                                         )
        throws VIOLATION
    {
        // Throws exception up the stack:
        final Domain<INSPECTABLE> domain = contract.domain ();
        if ( ! Contracts.isInDomain ( inspectable_data ) )
        {
            final Enforcer<INSPECTABLE, VIOLATION> enforcer =
                contract.enforcer ();
            final VIOLATION violation =
                enforcer.enforce ( contract,
                                   object_under_contract,
                                   inspectable_data );
            throw violation;
        }
    }
}
