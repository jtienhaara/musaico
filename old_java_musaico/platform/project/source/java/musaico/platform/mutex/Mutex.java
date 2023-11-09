package musaico.platform.mutex;

import java.io.Serializable;

import java.rmi.RemoteException;

import java.rmi.server.UnicastRemoteObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import musaico.foundation.condition.Conditional;
import musaico.foundation.condition.Failed;
import musaico.foundation.condition.Successful;

import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.security.Capability;
import musaico.foundation.security.CompositeCapability;
import musaico.foundation.security.Permissions;
import musaico.foundation.security.SecurityPolicy;
import musaico.foundation.security.SecurityViolation;


/**
 * <p>
 * A Mutex which can be locked to gain exclusive access to its
 * owning resource.  To lock the resource:
 * </p>
 *
 * <pre>
 *     Object resource = ...;
 *     Mutex mutex = ...get the mutex out of the resource...
 *     MutexLock mutex_lock = new MutexLock ( mutex );
 *     mutex_lock.lock ();
 *     try
 *     {
 *         ...do stuff...
 *     }
 *     finally
 *     {
 *         mutex_lock.unlock ();
 *     }
 * </pre>
 *
 * <p>
 * Or to lock multiple resources:
 * </p>
 *
 * <pre>
 *     MutexLock mutex_lock = new MutexLock ( resource1_mutex,
 *                                            resource2_mutex,
 *                                            ... );
 *     mutex_lock.lock ();
 *     try
 *     {
 *         ...do stuff...
 *     }
 *     finally
 *     {
 *         mutex_lock.unlock ();
 *     }
 * </pre>
 *
 * <p>
 * In Chandry / Misra terminology, a Mutex is a plate of food
 * for which 0 or more philosophers (MutexLocks) are competing
 * for a Fork to eat it with (perform some operation on the
 * object which owns the Mutex).
 * </p>
 *
 * <p>
 * <b>
 * Warning to RMI users:
 * </b>
 * The Mutex for a serialized object is
 * a remote singleton, but that does NOT mean that the method
 * calls on the serialized copy will have any effect whatsoever
 * on the original.  For example, if a Person object is created
 * on machine 192.168.0.1, then serialized and sent over RMI to machine
 * 192.168.0.99, the *mutex* for both copies will reside on the
 * original machine, 192.168.0.1.  However if you lock the Person
 * object on one machine and call waveArms(), it will not affect
 * the Person object on the other machine.  Many Musaico classes
 * do not allow this sort of behaviour, and assume that the RMI
 * owner will migrate (rather than share) objects.  If your class
 * must be shared, then your class should extend the UnicastRemoteObject,
 * or you can call UnicastRemoteObject.exportObject () on the object
 * you wish to share.  As always with RMI, if you are very careful,
 * you might be able to achieve half of what you hope to, at twice
 * the cost you expected.
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
 * Copyright (c) 2011, 2012, 2013 Johann Tienhaara
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
public class Mutex
    extends UnicastRemoteObject
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20130224L;
    private static final String serialVersionHash =
        "0xB33CDC431DE8E26D3241497A483A1549FCFE442E";


    /** Checks contracts on static methods and constructors for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( Mutex.class );


    /** No Mutex at all.  Not very useful for locking. */
    public static final Mutex NONE;
    static
    {
        try
        {
            NONE = new Mutex ( "parent object", SecurityPolicy.NONE );
        }
        catch ( RemoteException e ) // Ugh RMI ugliness
        {
            // Try to make the JVM fail.
            throw new RuntimeException ( e );
        }
    }

    /** Permission to create a mutex lock for this Mutex must be
     *  granted during every MutexLock constructor. */
    public static enum Capabilities
        implements Capability
    {
        MUTEX_LOCK_CREATE,
        MUTEX_LOCK,
        MUTEX_UNLOCK,
        MUTEX_LOCK_DESTROY;
    };

    public static final Capability ALL_MUTEX_LOCK_CAPABILITIES =
        new CompositeCapability ( Capabilities.MUTEX_LOCK_CREATE,
                                  Capabilities.MUTEX_LOCK,
                                  Capabilities.MUTEX_UNLOCK,
                                  Capabilities.MUTEX_LOCK_DESTROY );


    /** Lock all critical sections on this token. */
    private final Serializable lock = new String ();

    /** Checks obligations and guarantees on methods for us. */
    private final ObjectContracts contracts;

    /** Contract to ensure a given MutexLock is not yet competing
     *  to lock this Mutex. */
    private final MutexLockMustBeEligible mutexLockMustBeEligibleContract;

    /** Contract to ensure a given MutexLock is competing
     *  to lock this Mutex. */
    private final MutexLockMustBeCompeting mutexLockMustBeCompetingContract;

    /** Contract to ensure a given MutexLock has locked this Mutex
     *  exclusively. */
    private final MutexLockMustBeLockOwner mutexLockMustBeLockOwnerContract;

    /** Contract to ensure a given MutexLock has NOT locked this Mutex
     *  exclusively. */
    private final MutexLockMustNotBeLockOwner mutexLockMustNotBeLockOwnerContract;

    /** Security policy for this Mutex.  Determines which locks
     *  are allowed to lock this mutex and who is or isn't allowed
     *  to query internal details. */
    private final SecurityPolicy security;

    /** A (possibly not unique) serializable reference to the resource
     *  which can be locked for mutually exclusive access by locking
     *  this Mutex. */
    private final String id;

    /** All philosophers (MutexLocks) who want to lock this Mutex.
     *  Exactly 0 or 1 of them has this mutex locked currently. */
    private final List<MutexLock> competingPhilosophers =
        new ArrayList<MutexLock> ();

    /** The philosopher (MutexLock) currently eating / who has
     *  locked this Mutex.  Always a MutexLock from the list of
     *  philosophers.  CAN be null.  Changes over time. */
    private MutexLock eatingPhilosopher = null;


    /**
     * <p>
     * Creates a new Mutex which can be used to lock the specified
     * resource.
     * </p>
     *
     * <p>
     * The id of the newly created Mutex will be
     * <code> resource.toString () </code>.
     *
     * @param resource The object which will be locked for mutually exclusive
     *                 access when this newly created Mutex is locked.
     *                 Must not be null.
     */
    public Mutex (
                  Object resource,
                  SecurityPolicy security
                  )
        throws RemoteException, // Ugh RMI ugliness
               ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               resource, security );

        this.security = security;
        this.id = "" + resource;

        this.contracts = new ObjectContracts ( this );

        this.mutexLockMustBeEligibleContract =
            new MutexLockMustBeEligible ( this );
        this.mutexLockMustBeCompetingContract =
            new MutexLockMustBeCompeting ( this );
        this.mutexLockMustBeLockOwnerContract =
            new MutexLockMustBeLockOwner ( this );
        this.mutexLockMustNotBeLockOwnerContract =
            new MutexLockMustNotBeLockOwner ( this );
    }


    /**
     * <p>
     * Adds the specified philosopher (MutexLock) to the list of
     * philosophers competing to lock this Mutex, and then
     * creates a new Fork between the specified philosopher and
     * each of its neighbours competing to eat this Mutex.
     * </p>
     *
     * <p>
     * Only classes in the mutex package, and Mutex-derived classes,
     * should access this method (and carefully at that).
     * </p>
     *
     * @param philosopher The MutexLock which is attempting to
     *                    lock this Mutex for exclusive access to the
     *                    parent resource.  Must not be null.
     */
    protected void addPhilosopher (
                                   MutexLock philosopher
                                   )
        throws ParametersMustNotBeNull.Violation,
               MutexLockMustBeEligible.Violation,
               SecurityViolation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               philosopher );
        this.contracts.check ( this.mutexLockMustBeEligibleContract,
                               philosopher );

        // Check permissions or throw a SecurityViolation.
        Permissions required_permissions =
            new Permissions ( philosopher.securityContext (),
                              Capabilities.MUTEX_LOCK_CREATE );
        this.security.request ( required_permissions ).orThrowChecked ();

        synchronized ( this.lock )
        {
            this.competingPhilosophers.add ( philosopher );

            Set<MutexLock> parents = new HashSet<MutexLock> ();
            for ( MutexLock thinker : this.competingPhilosophers )
            {
                if ( thinker == philosopher )
                {
                    // Same philosopher.
                    continue;
                }
                else if ( thinker.isSameThreadAs ( philosopher ) )
                {
                    // We don't bother taking the fork from another
                    // philosopher on the same thread.  We assume
                    // they are spoonfeeding the philosopher with
                    // their fork.
                    // Plato in a highchair.
                    parents.add ( thinker );
                }
            }

            // Now that we have all of philosopher's parents assembled,
            // Create a Fork between the philosopher and each of its
            // neighbours.
            Set<MutexLock> already_neighbours = new HashSet<MutexLock> ();
            for ( MutexLock thinker : this.competingPhilosophers )
            {
                if ( thinker == philosopher )
                {
                    // Not a neighbour.
                    continue;
                }
                else if ( ! thinker.isSameThreadAs ( philosopher )
                          && ! already_neighbours.contains ( thinker ) )
                {
                    MutexLock neighbour = thinker;
                    Fork fork = new Fork ( this,
                                           parents,
                                           philosopher,
                                           neighbour );
                    already_neighbours.add ( neighbour );
                }
            }
        }
    }


    /**
     * <p>
     * Returns true if this Mutex is currently locked.
     * </p>
     *
     * <p>
     * If this Mutex is not locked at all then false is returned.
     * </p>
     *
     * @return True if this Mutex is locked; false if this Mutex is
     *         not currently ocked for mutual exclusion.  Warning:
     *         by the time the caller gets the result, the Mutex's
     *         state might have changed concurrently.
     *         Use isLockedByCurrentThread () instead to determine
     *         whether the current thread has the lock (in which case
     *         a sane security policy will ensure it is not released
     *         concurrently).
     */
    public boolean isLocked ()
    {
        synchronized ( this.lock )
        {
            if ( this.eatingPhilosopher != null )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }


    /**
     * <p>
     * Returns true if this Mutex is currently locked and if the
     * lock was performed in the current system on the current thread.
     * </p>
     *
     * <p>
     * If this Mutex is not locked at all, or if it was locked by
     * another Thread or a different system, then false is returned.
     * </p>
     *
     * @return True if this Mutex is locked by the current thread in the
     *         current system; false if the current thread does not have
     *         this Mutex locked for mutual exclusion.  Warning:
     *         by the time the caller gets the result, the Mutex's
     *         state might have changed concurrently.  A sane security
     *         policy will prevent another Thread from releasing the
     *         current Thread's lock concurrently.
     */
    public boolean isLockedByCurrentThread ()
    {
        synchronized ( this.lock )
        {
            if ( this.eatingPhilosopher != null
                 && this.eatingPhilosopher.isSameThread ( MutexLock.LOCAL_SYSTEM_ID,
                                                          Thread.currentThread ().getId () ) )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }


    /**
     * <p>
     * Sets the philosopher (MutexLock) which is currently eating
     * (has this Mutex locked to perform work).
     * </p>
     *
     * @param philosopher The philosopher (lock) which now owns this Mutex.
     *                    Must not be null.  Must be in the list of
     *                    philosophers currently contending for this
     *                    Mutex.
     */
    protected void lockedBy (
                             MutexLock philosopher
                             )
        throws ParametersMustNotBeNull.Violation,
               MutexMustBeUnlocked.Violation,
               MutexLockMustBeCompeting.Violation,
               SecurityViolation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               philosopher );

        // Check permissions or throw a SecurityViolation.
        Permissions required_permissions =
            new Permissions ( philosopher.securityContext (),
                              Capabilities.MUTEX_LOCK );
        this.security.request ( required_permissions ).orThrowChecked ();
        
        synchronized ( this.lock )
        {
            // Make sure this mutex isn't already locked, and
            // that the specified MutexLock is currently competing
            // to lock this Mutex.
            // These checks are defensive, the base MutexLock code
            // should never violate these contracts.
            // Check mutex / mutex lock state contracts inside sync block
            // so that this Mutex can't change state mid-contract-inspection.
            this.contracts.check ( MutexMustBeUnlocked.CONTRACT,
                                   this );
            this.contracts.check ( this.mutexLockMustBeCompetingContract,
                                   philosopher );

            this.eatingPhilosopher = philosopher;
        }
    }


    /**
     * <p>
     * Returns the MutexLock which currently has this Mutex
     * exclusively locked.
     * </p>
     *
     * @return The conditional MutexLock which currently owns the
     *         lock on this Mutex.  If this Mutex is not currently
     *         locked then the failed result will contain a
     *         MutexMustBeLocked.Violation.  You can use
     *         <code> .orNull () </code> to get null when this
     *         Mutex is not locked, or <code> .orNone () </code>
     *         to get <code> MutexLock.NONE </code>, or just
     *         throw the violation up the stack, depending on your
     *         needs.
     */
    public Conditional<MutexLock, MutexMustBeLocked.Violation> lockOwner ()
    {
        final MutexLock lock_owner;
        synchronized ( this.lock )
        {
            lock_owner = this.eatingPhilosopher;
        }

        final Conditional<MutexLock, MutexMustBeLocked.Violation> result;
        if ( lock_owner != null )
        {
            result =
                new Successful<MutexLock, MutexMustBeLocked.Violation> ( MutexLock.class,
                                                                         lock_owner );
        }
        else
        {
            MutexMustBeLocked.Violation violation =
                new MutexMustBeLocked.Violation ( MutexMustBeLocked.CONTRACT,
                                                  this,
                                                  this );
            result =
                new Failed<MutexLock, MutexMustBeLocked.Violation> ( MutexLock.class,
                                                                     violation,
                                                                     MutexLock.NONE );
        }

        return result;
    }


    /**
     * <p>
     * Returns an array copy of this Mutex's list of contending
     * philosophers (MutexLocks).
     * </p>
     *
     * @return All MutexLocks currently contending for this Mutex.
     *         Never null.  Never contains any null elements.
     */
    protected MutexLock [] philosophers ()
    {
        final MutexLock [] philosophers;
        synchronized ( this.lock )
        {
            MutexLock [] template =
                new MutexLock [ this.competingPhilosophers.size () ];
            philosophers = this.competingPhilosophers.toArray ( template );
        }

        return philosophers;
    }


    /**
     * <p>
     * Removes the specified philosopher (MutexLock) from
     * the list of contenders for this Mutex.
     * </p>
     *
     * <p>
     * Only classes in the mutex package, and Mutex-derived classes,
     * should access this method (and carefully at that).
     * </p>
     *
     * @param philosopher The MutexLock which is no longer attempting to
     *                    lock this Mutex for exclusive access to the
     *                    parent resource.  Must not be null.
     */
    protected void removePhilosopher (
                                      MutexLock philosopher
                                      )
        throws MutexLockMustBeCompeting.Violation,
               MutexLockMustNotBeLockOwner.Violation,
               ParametersMustNotBeNull.Violation,
               SecurityViolation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               philosopher );

        // Check permissions or throw a SecurityViolation.
        Permissions required_permissions =
            new Permissions ( philosopher.securityContext (),
                              Capabilities.MUTEX_LOCK_DESTROY );
        this.security.request ( required_permissions ).orThrowChecked ();

        synchronized ( this.lock )
        {
            // First ensure philosopher is competing, but not the current
            // lock owner.
            // Just a defensive check.
            this.contracts.check ( this.mutexLockMustBeCompetingContract,
                                   philosopher );
            this.contracts.check ( this.mutexLockMustNotBeLockOwnerContract,
                                   philosopher );

            this.competingPhilosophers.remove ( philosopher );
        }
    }


    /**
     * <p>
     * Returns this Mutex's security policy, controlling lock access
     * as well as information retrieval.
     * </p>
     *
     * @return This Mutex's security policy.  Never null.
     */
    public SecurityPolicy security ()
    {
        return this.security;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "Mutex_" + this.id;
    }


    /**
     * <p>
     * Unsets the philosopher (MutexLock) which is finished eating
     * (after having this Mutex locked to perform work).
     * </p>
     *
     * @param philosopher The philosopher (lock) which no longer owns
     *                    this Mutex.  Must not be null.  Must be the
     *                    philosopher which currently has this mutex
     *                    locked.
     */
    protected void unlockedBy (
                               MutexLock philosopher
                               )
        throws MutexLockMustBeLockOwner.Violation,
               ParametersMustNotBeNull.Violation,
               SecurityViolation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               philosopher );

        // Check permissions or throw a SecurityViolation.
        Permissions required_permissions =
            new Permissions ( philosopher.securityContext (),
                              Capabilities.MUTEX_UNLOCK );
        this.security.request ( required_permissions ).orThrowChecked ();
        
        synchronized ( this.lock )
        {
            // Make sure the mutex lock actually has this Mutex locked
            // right now.
            // Defensive check.
            this.contracts.check ( this.mutexLockMustBeLockOwnerContract,
                                   philosopher );

            this.eatingPhilosopher = null;
        }
    }
}
