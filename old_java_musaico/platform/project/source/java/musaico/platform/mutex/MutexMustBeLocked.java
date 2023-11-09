package musaico.platform.mutex;

import java.io.Serializable;


import musaico.foundation.contract.AbstractContract;
import musaico.foundation.contract.Contracts;
import musaico.foundation.contract.Obligation;
import musaico.foundation.contract.ObligationUncheckedViolation;


/**
 * <p>
 * Ensures a Mutex is currently locked by some MutexLock or other.
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
public class MutexMustBeLocked
    extends AbstractContract<Mutex, MutexMustBeLocked.Violation>
    implements Obligation<Mutex, MutexMustBeLocked.Violation>, Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20130118;
    private static final String serialVersionHash =
        "0x681F3F8C4B791993C1AD28D50EBB3BF59817DD3C";


    /** The parameters-must-not-be-null obligation singleton. */
    public static final MutexMustBeLocked CONTRACT =
        new MutexMustBeLocked ();


    /**
     * <p>
     * Only the singleton or derived classes can access the constructor
     * directly.  Use MutexMustBeLocked.CONTRACT instead.
     * </p>
     */
    protected MutexMustBeLocked ()
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
        throws MutexMustBeLocked.Violation
    {
        boolean is_mutex_locked = true;
        if ( mutex == null
             || ! mutex.isLocked () )
        {
            throw new MutexMustBeLocked.Violation ( this,
                                                    object,
                                                    mutex );
        }
    }


    /**
     * <p>
     * A violation of the MutexMustBeLocked contract.
     * </p>
     */
    public static class Violation
        extends ObligationUncheckedViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            MutexMustBeLocked.serialVersionUID;

        /**
         * <p>
         * Creates a MutexMustBeLocked.Violation.
         * </p>
         */
        public Violation (
                          MutexMustBeLocked obligation,
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
