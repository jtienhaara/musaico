package musaico.platform.mutex;

import java.io.Serializable;


import musaico.foundation.contract.AbstractContract;
import musaico.foundation.contract.Contracts;
import musaico.foundation.contract.ObjectContracts;
import musaico.foundation.contract.Obligation;
import musaico.foundation.contract.ObligationUncheckedViolation;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * Each Fork passed to this contract must be shared by a specific MutexLock,
 * otherwise the contract is violated.
 * </p>
 *
 * <p>
 * For example, before a MutexLock can remove a Fork from its
 * list of needed Forks, it must first check that it shares the
 * Fork with a neighbour in the first place.
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
public class ForkMustBeNeededByMutexLock
    extends AbstractContract<Fork, ForkMustBeNeededByMutexLock.Violation>
    implements Obligation<Fork, ForkMustBeNeededByMutexLock.Violation>, Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20130320;
    private static final String serialVersionHash =
        "0x9131489179D907E8AD74F6E964BDABEAF733744D";


    /** Checks static and constructor contracts for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( ForkMustBeNeededByMutexLock.class );


    /** The MutexLock which must share every Fork checked against
     *  this contract. */
    private final MutexLock mutexLock;


    /**
     * <p>
     * Creates a new contract which stipulates that every Fork
     * obliged by the contract must be beside the MutexLock specified
     * here, shared with some other MutexLock.
     * </p>
     *
     * @param mutex_lock The MutexLock which every Fork must be beside.
     *                   Must not be null.
     */
    public ForkMustBeNeededByMutexLock (
                                        MutexLock mutex_lock
                                        )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               mutex_lock );

        this.mutexLock = mutex_lock;
    }


    /**
     * @see musaico.foundation.contract.Contract#enforce(java.lang.Object, java.lang.Object)
     */
    @Override
    public void enforce (
                         Object object,
                         Fork fork
                         )
        throws ForkMustBeNeededByMutexLock.Violation
    {
        if ( ! this.mutexLock.needsFork ( fork ) )
        {
            throw new ForkMustBeNeededByMutexLock.Violation ( this,
                                                              object,
                                                              fork );
        }
    }


    /**
     * <p>
     * Returns the MutexLock which every Fork must be beside.
     * </p>
     *
     * @return The MutexLock to which this contract applies.  Never null.
     */
    public MutexLock mutexLock ()
    {
        return this.mutexLock;
    }


    /**
     * <p>
     * A violation of the ForkMustBeNeededByMutexLock contract.
     * </p>
     */
    public static class Violation
        extends ObligationUncheckedViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            ForkMustBeNeededByMutexLock.serialVersionUID;

        /**
         * <p>
         * Creates a ForkMustBeNeededByMutexLock.Violation.
         * </p>
         */
        public Violation (
                          ForkMustBeNeededByMutexLock obligation,
                          Object object_under_contract,
                          Fork fork
                          )
        {
            super ( obligation,
                    Contracts.makeSerializable ( object_under_contract ),
                    Contracts.makeSerializable ( fork ) );
        }
    }
}
