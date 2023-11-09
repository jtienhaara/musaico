package musaico.foundation.contract;

import java.io.Serializable;


/**
 * <p>
 * Decides when to inspect contracts for possible violations,
 * and how to enforce them (such as by throwing runtime exceptions,
 * stopping the entire system, logging an error then carrying on
 * and letting things blow up, and so on).
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
public interface Arbiter
    extends Serializable
{
    /**
     * <p>
     * Inspects and then enforces the specified contract for the specified
     * object and inspectable data.
     * </p>
     *
     * <p>
     * If the contract has been breached and this arbiter enforces
     * contracts by throwing exceptions, then a violation will
     * be thrown.
     * </p>
     *
     * @param contract The contract to inspect.  Must not be null.
     *
     * @param object_under_contract The object which is under contract.
     *                              Must not be null.
     *
     * @param inspectable_data The data which might breach the contract.
     *                         Must not be null.
     *
     * @throws ContractViolation If the arbiter has inspected the contract,
     *                           determined it was breached,
     *                           and enforced the contract by throwing
     *                           a runtime exception.
     */
    public abstract
        <INSPECTABLE extends Object, VIOLATION extends Violation>
                           void inspect (
                                         Contract<INSPECTABLE, VIOLATION> contract,
                                         Object object_under_contract,
                                         INSPECTABLE inspectable_data
                                         )
        throws VIOLATION;
}
