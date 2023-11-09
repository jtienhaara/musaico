package musaico.platform.mutex;

import java.io.Serializable;


import musaico.foundation.contract.AbstractContract;
import musaico.foundation.contract.Contracts;
import musaico.foundation.contract.Obligation;
import musaico.foundation.contract.ObligationUncheckedViolation;


/**
 * <p>
 * A method which requires a specific Mutex object to be locked
 * (for example in order to ensure data is read or written atomically)
 * can declare the MutexMustBeLockedByCurrentThread and throw a
 * MutexMustBeLockedByCurrentThread.Violation runtime exception any time
 * it is invoked withOUT first locking the mutex.
 * </p>
 *
 * <p>
 * Example of use:
 * </p>
 *
 * <pre>
 *     private final Mutex mutex = ...;
 *     private final ObjectContracts contracts = new ObjectContracts ( this );
 *     private BigDecimal volumeInLitres = BigDecimal.ZERO;
 *
 *     public BigDecimal addVolume ( BigDecimal offset_in_litres )
 *         throws MutexMustBeLockedByCurrentThread.Violation
 *     {
 *         // Throws a runtime exception if the mutex is not
 *         // locked by the current thread:
 *         this.contracts.check ( MutexMustBeLockedByCurrentThread.CONTRACT,
 *                                this.mutex );
 *
 *         this.volumeInLitres = this.volumeInLitres.add ( offset_in_litres );
 *         return this.volumeInLitres;
 *     }
 * </pre>
 *
 * <p>
 * For an object which must atomically modify only its own data,
 * traditional synchronization methods should be used since they
 * are faster.  For example, if the object which can increment
 * or decrement its own counter does so without any requirements
 * to synchronize with other objects, it should just use Java's
 * synchronized { ... } blocks instead.  The advantage of the
 * MutexMustBeLockedByCurrentThread obligation is that the caller can decide
 * which objects must be synchronized.  For example the following
 * code locks two Containers at once, meeting the obligation of each
 * one's add () method; a synchronized block inside add () would
 * not provide the same atomicity:
 * </p>
 *
 * <pre>
 *     public void pour ( Container from, Container into,
 *                        BigDecimal litres_to_pour )
 *     {
 *         MutexLock mutex_lock =
 *             new MutexLock ( from.mutex, into.mutex );
 *         mutex_lock.lock ( TIMEOUT_MILLISECONDS );
 *         try
 *         {
 *             from.add ( litres_to_pour.negate () );
 *             into.add ( litres_to_pour );
 *         }
 *         finally
 *         {
 *             mutex_lock.unlock ();
 *         }
 *     }
 * </pre>
 *
 * <p>
 * The MutexMustBeLockedByCurrentThread obligation means that any
 * complex operation, involving any number of objects, can be
 * performed atomically.
 * </p>
 *
 * <p>
 * Of course failures and rollback are still the caller's problem...
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
public class MutexMustBeLockedByCurrentThread
    extends AbstractContract<Mutex, MutexMustBeLockedByCurrentThread.Violation>
    implements Obligation<Mutex, MutexMustBeLockedByCurrentThread.Violation>, Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20130118;
    private static final String serialVersionHash =
        "0x74275A5711F550AED593823535D0D81F97B2234B";


    /** The parameters-must-not-be-null obligation singleton. */
    public static final MutexMustBeLockedByCurrentThread CONTRACT =
        new MutexMustBeLockedByCurrentThread ();


    /**
     * <p>
     * Only the singleton or derived classes can access the constructor
     * directly.  Use MutexMustBeLockedByCurrentThread.CONTRACT instead.
     * </p>
     */
    protected MutexMustBeLockedByCurrentThread ()
    {
    }


    /**
     * @see musaico.foundation.contract.Contract#enforce(java.lang.Object, java.lang.Object)
     */
    @Override
    public void enforce (
                         Object object,
                         Mutex mutex
                         )
        throws MutexMustBeLockedByCurrentThread.Violation
    {
        boolean is_mutex_locked = true;
        if ( mutex == null
             || ! mutex.isLockedByCurrentThread () )
        {
            throw new MutexMustBeLockedByCurrentThread.Violation ( this,
                                                                   object,
                                                                   mutex );
        }
    }


    /**
     * <p>
     * A violation of the MutexMustBeLockedByCurrentThread contract.
     * </p>
     */
    public static class Violation
        extends ObligationUncheckedViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            MutexMustBeLockedByCurrentThread.serialVersionUID;

        /**
         * <p>
         * Creates a MutexMustBeLockedByCurrentThread.Violation.
         * </p>
         */
        public Violation (
                          MutexMustBeLockedByCurrentThread obligation,
                          Object object_under_contract,
                          Mutex mutex
                          )
        {
            super ( obligation,
                    Contracts.makeSerializable ( object_under_contract ),
                    Contracts.makeSerializable ( mutex ) );
        }
    }
}
