package musaico.foundation.contract;

import java.io.Serializable;


/**
 * <p>
 * No guarantee contract at all.
 * </p>
 *
 *
 * <p>
 * In Java, every Contract and every ContractViolation must be
 * Serializable in order to play nicely over RMI.
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
public class NoGuarantee
    extends AbstractContract<Object, GuaranteeCheckedViolation>
    implements Guarantee<Object, GuaranteeCheckedViolation>, Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20130321;
    private static final String serialVersionHash =
        "0xA30CD4ECE02535E5FDBFC994DDFB25D4A21EEB13";


    /**
     * @see musaico.foundation.contract.Contract#enforce(java.lang.Object, java.lang.Object[])
     */
    @Override
    public void enforce (
                         Object object_under_contract,
                         Object inspectable_data
                         )
        // Never throws an exception.
    {
        // No guarantee to enforce, nothing to do.
    }
}
