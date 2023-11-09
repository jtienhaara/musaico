package musaico.kernel.objectsystem.superblock;

import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.kernel.objectsystem.RecordLock;

import musaico.mutex.Mutex;
import musaico.mutex.MutexLock;
import musaico.mutex.MutexTimeoutException;


/**
 * <p>
 * A MutexLock which throws a SuperBlockOperationException if it times
 * out waiting for mutually exclusive access to something(s).
 * </p>
 *
 * <p>
 * Useful for implementing the operations for SuperBlocks.
 * </p>
 *
 * @see musaico.mutex
 *
 * @see musaico.kernel.objectsystem.ONodeLock
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
 * Copyright (c) 2011, 2012 Johann Tienhaara
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
public class SuperBlockLock
    implements Serializable
{
    /** The MutexLock which does the work for us. */
    private final MutexLock mutexLock;


    /**
     * <p>
     * Creates a new SuperBlockLock on the specified mutexes.
     * </p>
     *
     * @param mutexes The Mutexes to lock.
     *                Must not be null.  Must not contain
     *                any null elements.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (see above).
     */
    public SuperBlockLock (
                           Mutex... mutexes
                           )
        throws I18nIllegalArgumentException
    {
        if ( mutexes == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a [%lock_type%] with mutexes [%mutexes%]",
                                                     "lock_type", this.getClass ().getSimpleName (),
                                                     "mutexes", mutexes );
        }

        for ( int m = 0; m < mutexes.length; m ++ )
        {
            if ( mutexes [ m ] == null )
            {
                throw new I18nIllegalArgumentException ( "Cannot create a [%lock_type%] with mutexes [%mutexes%]",
                                                         "lock_type", this.getClass ().getSimpleName (),
                                                         "mutexes", mutexes );
            }
        }

        this.mutexLock = new MutexLock ( mutexes );
    }


    /**
     * @see musaico.mutex.MutexLock#lock()
     */
    public void lock ()
        throws SuperBlockOperationException
    {
        this.lock ( RecordLock.MUTEX_DEFAULT_TIMEOUT_IN_MILLISECONDS );
    }


    /**
     * @see musaico.mutex.MutexLock#lock(long)
     */
    public void lock (
                      long timeout_in_milliseconds
                      )
        throws SuperBlockOperationException
    {
        try
        {
            this.mutexLock.lock ( timeout_in_milliseconds );
        }
        catch ( MutexTimeoutException e )
        {
            throw new SuperBlockOperationException ( "Failed to acquire lock on [%mutexes%] in [%timeout_in_milliseconds%] milliseconds",
                                                     "mutexes", this.mutexLock.mutexes (),
                                                     "timeout_in_milliseconds", timeout_in_milliseconds );
        }

        // OK, lock acquired.
    }


    /**
     * @see java.lang.Object#finalize()
     */
    protected void finalize ()
    {
        this.unlock ();
    }


    /**
     * @see musaico.mutex.MutexLock#unlock()
     */
    public void unlock ()
    {
        this.mutexLock.unlock ();
    }
}
