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
 * Each MutexLock passed to this contract must NOT have a specific Mutex
 * locked exclusively, otherwise the contract is violated.
 * </p>
 *
 * <p>
 * For example, in order to stop competing to lock a Mutex, a MutexLock
 * must not be the current lock owner.
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
public class MutexLockMustNotBeLockOwner
    extends AbstractContract<MutexLock, MutexLockMustNotBeLockOwner.Violation>
    implements Obligation<MutexLock, MutexLockMustNotBeLockOwner.Violation>, Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20130308;
    private static final String serialVersionHash =
        "0xAAEA31C5059312DF948D84F0015E0D3DFD84BD9C";


    /** Checks static and constructor contracts for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( MutexLockMustNotBeLockOwner.class );


    /** The Mutex for which every MutexLock must NOT be the owner
     *  of the Mutex. */
    private final Mutex mutex;


    /**
     * <p>
     * Creates a new contract which stipulates that every MutexLock
     * obliged by the contract must NOT be the lock owner of
     * the Mutex specified here.
     * </p>
     *
     * @param mutex The Mutex which every MutexLock must NOT have locked.
     *              Must not be null.
     */
    public MutexLockMustNotBeLockOwner (
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
        throws MutexLockMustNotBeLockOwner.Violation
    {
        if ( this.mutex.lockOwner ().orNull () != mutex_lock )
        {
            // OK, this philosopher does NOT own the lock on
            // the Mutex.
            return;
        }

        // This philosopher owns the exclusive lock on the mutex.
        // Contract violated.
        throw new MutexLockMustNotBeLockOwner.Violation ( this,
                                                          object,
                                                          mutex_lock );
    }


    /**
     * <p>
     * Returns the Mutex which every MutexLock must NOT have
     * locked excousively.
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
     * A violation of the MutexLockMustNotBeLockOwner contract.
     * </p>
     */
    public static class Violation
        extends ObligationUncheckedViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            MutexLockMustNotBeLockOwner.serialVersionUID;

        /**
         * <p>
         * Creates a MutexLockMustNotBeLockOwner.Violation.
         * </p>
         */
        public Violation (
                          MutexLockMustNotBeLockOwner obligation,
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
