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
 * Each MutexLock passed to this contract must be eligible to compete
 * to lock a specific Mutex, otherwise the contract is violated.
 * </p>
 *
 * <p>
 * For example, before a MutexLock can begin competing to lock a
 * particular Mutex, it must be eligible to compete.
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
public class MutexLockMustBeEligible
    extends AbstractContract<MutexLock, MutexLockMustBeEligible.Violation>
    implements Obligation<MutexLock, MutexLockMustBeEligible.Violation>, Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20130308;
    private static final String serialVersionHash =
        "0xD941B51177FE1CF40B35E2CF0C94EA05453BD652";


    /** Checks static and constructor contracts for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( MutexLockMustBeEligible.class );


    /** The Mutex for which every MutexLock must be eligible to compete. */
    private final Mutex mutex;


    /**
     * <p>
     * Creates a new contract which stipulates that every MutexLock
     * obliged by the contract must be eligible to compete to lock
     * the Mutex specified here.
     * </p>
     *
     * @param mutex The Mutex which every MutexLock must be eligible
     *              to compete to lock.  Must not be null.
     */
    public MutexLockMustBeEligible (
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
        throws MutexLockMustBeEligible.Violation
    {
        for ( Mutex eligible_for_mutex : mutex_lock.mutexes () )
        {
            if ( eligible_for_mutex == this.mutex )
            {
                // OK, this philosopher is indeed eligible to compete
                // to lock the mutex.
                return;
            }
        }

        // This philosopher is not eligible to compete for the mutex.
        // Contract violated.
        throw new MutexLockMustBeEligible.Violation ( this,
                                                      object,
                                                      mutex_lock );
    }


    /**
     * <p>
     * Returns the Mutex which every MutexLock must be eligible to compete
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
     * A violation of the MutexLockMustBeEligible contract.
     * </p>
     */
    public static class Violation
        extends ObligationUncheckedViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            MutexLockMustBeEligible.serialVersionUID;

        /**
         * <p>
         * Creates a MutexLockMustBeEligible.Violation.
         * </p>
         */
        public Violation (
                          MutexLockMustBeEligible obligation,
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
