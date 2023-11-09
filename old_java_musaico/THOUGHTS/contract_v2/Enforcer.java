package musaico.foundation.contract;

import java.io.Serializable;


/**
 * <p>
 * Creates violations of contracts, so that each violation contains
 * the details of the contract, who was under contract, why the
 * inspectable data violated the contract, and so on.
 * </p>
 *
 *
 * <p>
 * In Java, every Enforcer must be Serializable in order to play
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
public interface Enforcer<INSPECTABLE extends Object, VIOLATION extends Throwable>
    extends Serializable
{
    /**
     * <p>
     * Blindly creates a violation of the specific Contract by the
     * specified inspectable data.
     * </p>
     *
     * <p>
     * The Enforcer's job is to beat people up, not look for justice.
     * This method never checks the inspectable data for contract
     * validity.  Use
     * <code> Contract.domain ().isValid ( inspectable_data ) </code>
     * instead.  This method always returns a violation, even if
     * the caller is daft enough to pass in valid inspectable data.
     * </p>
     *
     * @param contract The Contract which is being blindly enforced.
     *                 Must not be null.
     *
     * @param object_under_contract The object under contract.  For
     *                              example, if object x has a method
     *                              obligation "parameters must not be
     *                              null" and someone calls x.method ( p )
     *                              with a null parameter p, then object x
     *                              would be the object under contract.
     *                              Must not be null.
     *
     * @param inspectable_data The inspectable data, to be checked by
     *                         this contract.  For
     *                         example, if object x has a method
     *                         obligation "parameters must not be
     *                         null" and someone calls x.method ( p )
     *                         with a null parameter p, then parameter p
     *                         would be the inspectable data.
     *                         Enforcer implementors must be very careful
     *                         to guard against null values.
     *                         Can be null.  Can contain null elements.
     *
     * @return The newly created violation of the specified Contract
     *         by the specified inspectable data, enforced for the
     *         specified object under contract.  Even if the
     *         inspectable data is legal by the specified contract,
     *         a non-null violation will be returned.  Never null.
     */
    public abstract VIOLATION violation (
                                         Contract<INSPECTABLE, VIOLATION> contract,
                                         Object object_under_contract,
                                         INSPECTABLE inspectable_data
                                         );
}
