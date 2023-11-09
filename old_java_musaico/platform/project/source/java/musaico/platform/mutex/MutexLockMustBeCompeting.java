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
 * Each MutexLock passed to this contract must be competing to lock
 * a specific Mutex, otherwise the contract is violated.
 * </p>
 *
 * <p>
 * For example, in order to change the state of a Mutex to locked,
 * the MutexLock is obliged to already be competing against other
 * MutexLocks to lock the Mutex.  If it is not already competing,
 * how can it possibly acquire the lock?
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
public class MutexLockMustBeCompeting
    extends AbstractContract<MutexLock, MutexLockMustBeCompeting.Violation>
    implements Obligation<MutexLock, MutexLockMustBeCompeting.Violation>, Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20130308;
    private static final String serialVersionHash =
        "0x1FB8F540316522731FAC02A34E7F46975E00D505";


    /** Checks static and constructor contracts for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( MutexLockMustBeCompeting.class );


    /** The Mutex for which every MutexLock must be competing. */
    private final Mutex mutex;


    /**
     * <p>
     * Creates a new contract which stipulates that every MutexLock
     * obliged by the contract must already be competing to lock
     * the Mutex specified here.
     * </p>
     *
     * @param mutex The Mutex which every MutexLock must be competing
     *              to lock.  Must not be null.
     */
    public MutexLockMustBeCompeting (
                                     Mutex mutex
                                     )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               mutex );

        this.mutex = mutex;
    }


    /**
     * @see musaico.foundation.contract.Contract#enforce(java.lang.Object, java.lang.Object)
     */
    @Override
    public void enforce (
                         Object object,
                         MutexLock mutex_lock
                         )
        throws MutexLockMustBeCompeting.Violation
    {
        for ( MutexLock competing_philosopher : this.mutex.philosophers () )
        {
            if ( competing_philosopher == mutex_lock )
            {
                // OK, this philosopher is indeed competing
                // to lock the mutex.
                return;
            }
        }

        // This philosopher is not competing for the mutex.
        // Contract violated.
        throw new MutexLockMustBeCompeting.Violation ( this,
                                                   object,
                                                   mutex_lock );
    }


    /**
     * <p>
     * Returns the Mutex which every MutexLock must be competing
     * for the right to lock.
     * </p>
     *
     * @return The Mutex to which this contract applies.  Never null.
     */
    public Mutex mutex ()
    {
        return this.mutex;
    }


    /**
     * <p>
     * A violation of the MutexLockMustBeCompeting contract.
     * </p>
     */
    public static class Violation
        extends ObligationUncheckedViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            MutexLockMustBeCompeting.serialVersionUID;

        /**
         * <p>
         * Creates a MutexLockMustBeCompeting.Violation.
         * </p>
         */
        public Violation (
                          MutexLockMustBeCompeting obligation,
                          Object object_under_contract,
                          MutexLock mutex_lock
                          )
        {
            super ( obligation,
                    Contracts.makeSerializable ( object_under_contract ),
                    Contracts.makeSerializable ( mutex_lock ) );
        }
    }
}
