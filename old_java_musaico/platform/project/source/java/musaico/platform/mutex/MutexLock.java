package musaico.platform.mutex;

import java.io.Serializable;

import java.util.UUID;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.Parameter3;
import musaico.foundation.contract.obligations.Parameter6;
import musaico.foundation.contract.obligations.ParameterN;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.contract.guarantees.TimeoutGuarantee;

import musaico.foundation.security.Permissions;
import musaico.foundation.security.SecurityContext;
import musaico.foundation.security.SecurityPolicy;
import musaico.foundation.security.SecurityViolation;


/**
 * <p>
 * A lock which can atomically gain exclusive access to one or
 * more Mutexes.
 * </p>
 *
 * <p>
 * For example, to lock 3 nodes in a tree, in order to atomically
 * move a child node from one parent to another:
 * </p>
 *
 * <pre>
 *     MyTreeNode old_parent = ...;
 *     MyTreeNode child = ...;
 *     MyTreeNode new_parent = ...;
 *
 *     SecurityContext security_context = ...; // System-dependent.
 *     SecurityPolicy mutex_lock_security_policy = ...; // Up to the creator.
 *
 *     // Create a mutex to lock the old parent, the child and
 *     // the new parent for exclusive atomic access.
 *     MutexLock mutex_lock =
 *         new MutexLock ( security_context,
 *                         mutex_lock_security_policy,
 *                         old_parent.mutex (),
 *                         child.mutex (),
 *                         new_parent.mutex () );
 *
 *     // Now lock and do the work atomically.
 *     // Don't wait more than 1 second to acquire the lock.
 *     try
 *     {
 *         mutex_lock.lock ( security_context, 1000L );
 *
 *         try ( mutex_lock ) // Automagically unlock () at block end
 *         {
 *             old_parent.removeChild ( child );
 *             new_parent.addChild ( child );
 *             child.setParent ( new_parent );
 *         }
 *     }
 *     catch ( TimeoutGuarantee.Violation v )
 *     {
 *         System.err.println ( "Could not acquire Mutex in 1 second, exiting" );
 *         ...
 *     }
 * </pre>
 *
 * <p>
 * If you are using this library in an RMI environment, be very
 * careful about locking serializable objects.
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
public class MutexLock
    implements AutoCloseable, Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20130213L;
    private static final String serialVersionHash =
        "0x795BB7C2C14C43945083F9AF5C6A47EA82B1509B";


    /** A unique identifier for the local JVM. */
    public static final String LOCAL_SYSTEM_ID = "" + UUID.randomUUID ();


    /** Checks contracts on static methods and constructors for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( MutexLock.class );

    /** No MutexLock at all.  Not very useful for critical sections. */
    public static final MutexLock NONE =
        new MutexLock ( 1L );

    /** 2nd no MutexLock at all for use by Fork.NONE. */
    public static final MutexLock NONE2 =
        new MutexLock ( 2L );


    /** The default lock timeout, if we can't acquire a lock for
     *  a long time. */
    public static final long DEFAULT_TIMEOUT_IN_MILLISECONDS = 1000L;


    /** Checks our method obligations and guarantees for us. */
    private final ObjectContracts contracts;

    /** When adding a needed Fork to this MutexLock, it must not be
     *  already needed by this MutexLock.
     *  Just a sanity check, Fork is the only class to call our
     *  addNeededFork () method. */
    private final ForkMustNotBeNeededByMutexLock forkMustNotBeNeededByMutexLockContract;

    /** When removing a needed Fork from this MutexLock, it has to be
     *  needed by this MutexLock in the first place.
     *  Just a sanity check, Fork is the only class to call our
     *  removeNeededFork () method. */
    private final ForkMustBeNeededByMutexLock forkMustBeNeededByMutexLockContract;

    /** The security context in which this MutexLock was created,
     *  including the Credentials of the creator.  Used to
     *  securely access the Mutex. */
    private final SecurityContext securityContext;

    /** Security policy for this MutexLock, defining who is allowed to
     *  lock it, unlock it, and so on. */
    private final SecurityPolicy security;

    /** Lock all critical sections on this token: */
    private final Serializable lock = new String ();

    /** The 1 or more Mutexes which will be locked for exclusive access. */
    private final Mutex [] mutexes;

    /** The Forks between this philosopher and her neighbours.
     *  Once this philosopher has received these Forks, she can eat
     *  (lock the Mutexes).
     *  <code> neededForks = dirtyForks + cleanForks
     *                       + receivedDibs + myDibs </code>. */
    private final Set<Fork> neededForks = new HashSet<Fork> ();

    /** The Forks which this philosopher has recently received from her
     *  neighbours.  These forks are dirty, so the philosopher must
     *  clean them before putting them into her own forks list. */
    private final List<Fork> dirtyForks = new ArrayList<Fork> ();

    /** Dibs which have recently been received from this philosopher's
     *  neighbours.  When a neighbouring philosopher is done eating,
     *  they will send the Dibs for the shared Fork to this philosopher.
     *  After receiving each Dibs, this philosopher must move the Dibs
     *  into her own dibs pile. */
    private final List<Dibs> receivedDibs = new ArrayList<Dibs> ();

    /** The clean forks with which this philosopher may eat, once
     *  she has acquired and cleaned every fork from every neighbour. */
    private final Set<Fork> cleanForks = new HashSet<Fork> ();

    /** All Dibs for forks which are currently in a pile in front of
     *  this philosopher. */
    private final Set<Dibs> myDibs =
        new HashSet<Dibs> ();

    /** This philosopher is hungry until she has finished eating
     *  (unlocked all the Mutexes). */
    private boolean isHungry = false;

    /** This philosopher does not start eating until she has received
     *  all of the Forks from all of her neighbouring philosophers
     *  (ergo all the Mutexes have been locked by this philosopher).
     *  Once this philosopher is done eating the Mutexes are unlocked. */
    private boolean isEating = false;

    /** The JVM in which this MutexLock exists.  A remote object might
     *  get a serialized version of this MutexLock, in which case
     *  the local system ID will not be the same as the MutexLock's
     *  system ID. */
    private final String systemID;

    /** The thread # in which this MutexLock exists (in the JVM
     *  identified by systemID). */
    private final long threadNum;

    /** A name for this MutexLock, typically the name of the thread
     *  in which it exists. */
    private final String name;


    /**
     * <p>
     * Creates a new MutexLock in the specified SecurityContext,
     * with the specified security policy, which can be used to
     * atomically lock all of the specified Mutexes at once, in the
     * current Thread of execution.
     * </p>
     *
     * @param security_context The owner of this MutexLock.  Used to securely
     *                         access each and every specified Mutex.
     *                         If any of the specified Mutexes do not allow
     *                         access from the specified security context then
     *                         a SecurityViolation will be thrown now, rather
     *                         than waiting to try to lock or unlock later.
     *                         Must not be null.
     *
     * @param security The security policy determining who can and who
     *                 cannot access this MutexLock.  Must not be null.
     *
     * @param mutexes The Mutexes which will be locked atomically, all
     *                at once, when lock () is invoked, and unlocked
     *                atomically, all at once, when unlock () is called.
     *                Must not be null.  Must not contain any null elements.
     */
    public MutexLock (
                      SecurityContext security_context,
                      SecurityPolicy security,
                      Mutex... mutexes
                      )
        throws ParametersMustNotBeNull.Violation,
               Parameter3.MustContainNoNulls.Violation,
               SecurityViolation
    {
        this ( security_context,
               security,
               LOCAL_SYSTEM_ID,
               Thread.currentThread ().getId (),
               Thread.currentThread ().getName (),
               mutexes );
    }


    /**
     * <p>
     * Creates a new MutexLock in the specified SecurityContext,
     * with the specified security policy, which can be used to
     * atomically lock all of the specified Mutexes at once,
     * in the thread specified by the given system id and thread number.
     * </p>
     *
     * @param security_context The owner of this MutexLock.  Used to securely
     *                         access each and every specified Mutex.
     *                         If any of the specified Mutexes do not allow
     *                         access from the specified security context then
     *                         a SecurityViolation will be thrown now, rather
     *                         than waiting to try to lock or unlock later.
     *                         Must not be null.
     *
     * @param security The security policy determining who can and who
     *                 cannot access this MutexLock.  Must not be null.
     *
     * @param system_id An identifier for the JVM from which this
     *                  lock was created.  Must not be null.
     *
     * @param thread_id The unique identifier of the Thread, in the given
     *                  system, for which this lock was created.
     *                  Must be a valid Thread identifier.
     *
     * @param name A human-readable name for this MutexLock.
     *             Typically the name of the thread in which this,
     *             lock exists, <code> Thread.getName () </code>.
     *             Used for logging and debugging only.
     *             Must not be null.
     *
     * @param mutexes The Mutexes which will be locked atomically, all
     *                at once, when lock () is invoked, and unlocked
     *                atomically, all at once, when unlock () is called.
     *                Must not be null.  Must not contain any null elements.
     *
     * @throws SecurityViolation If any of the specified Mutexes does
     *                           not allow locking from the specified
     *                           SecurityContext.
     */
    public MutexLock (
                      SecurityContext security_context,
                      SecurityPolicy security,
                      String system_id,
                      long thread_id,
                      String name,
                      Mutex... mutexes
                      )
        throws ParametersMustNotBeNull.Violation,
               Parameter6.MustContainNoNulls.Violation,
               SecurityViolation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               security_context,
                               security,
                               system_id,
                               thread_id,
                               name,
                               mutexes );
        classContracts.check ( Parameter6.MustContainNoNulls.CONTRACT,
                               mutexes );

        this.securityContext = security_context;
        this.security = security;

        this.systemID = system_id;
        this.threadNum = thread_id;
        this.name = name;

        // This MutexLock is a philosopher.
        // Every philosopher starts out hungry.
        // Typically once he finishes drinking and eating,
        // this philosopher will drink a cup of hemlock
        // (after a lengthy and melodramatic trial of course).
        this.isHungry = true;

        // Check to make sure all of the Mutexes allow this lock
        // to be created.
        // Throw a SecurityViolation if any Mutex does not like us.
        Permissions required_permissions =
            new Permissions ( this.securityContext,
                              Mutex.ALL_MUTEX_LOCK_CAPABILITIES );
        for ( Mutex mutex : mutexes )
        {
            mutex.security ()
                .request ( required_permissions )
                .orThrowChecked ();
        }

        // For each fork shared between each pair
        // of competing philosophers, 
        this.mutexes = new Mutex [ mutexes.length ];
        System.arraycopy ( mutexes, 0, this.mutexes, 0, this.mutexes.length );

        this.contracts = new ObjectContracts ( this );

        this.forkMustNotBeNeededByMutexLockContract =
            new ForkMustNotBeNeededByMutexLock ( this );
        this.forkMustBeNeededByMutexLockContract =
            new ForkMustBeNeededByMutexLock ( this );
    }


    /**
     * <p>
     * Creates a "no MutexLock at all" supposedly acting on Mutex.NONE.
     * </p>
     */
    private MutexLock (
                       long id
                       )
    {
        this.securityContext = SecurityContext.NONE;
        this.security = SecurityPolicy.NONE;

        this.systemID = "no_system";
        this.threadNum = id;
        this.name = "thread_" + id;

        // For each fork shared between each pair
        // of competing philosophers, 
        this.mutexes = new Mutex [] { Mutex.NONE };

        this.contracts = new ObjectContracts ( this );

        this.forkMustNotBeNeededByMutexLockContract =
            new ForkMustNotBeNeededByMutexLock ( this );
        this.forkMustBeNeededByMutexLockContract =
            new ForkMustBeNeededByMutexLock ( this );
    }


    /**
     * <p>
     * Adds the specified needed fork to the count.  This philosopher
     * must have (count) clean forks in her hand before she is ready
     * to eat.
     * </p>
     *
     * <p>
     * Each needed Fork is shared with one other philosopher, so
     * only one of the two philosophers can eat at once.
     * </p>
     *
     * @param fork The fork which lies between this philosopher and
     *             her neighbour.  Must not be null.
     */
    protected void addNeededFork (
                                  Fork fork
                                  )
        throws ParametersMustNotBeNull.Violation,
               ForkMustBeNeededByMutexLock.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               fork );

        synchronized ( this.lock )
        {
            this.contracts.check ( this.forkMustNotBeNeededByMutexLockContract,
                                   fork );

            this.neededForks.add ( fork );
        }
    }


    /**
     * <p>
     * Applies the rules specified by Chandry and Misra to give or
     * receive shared Forks and Dibs for forks.
     * </p>
     *
     * <p>
     * The rules are commented with identifiers R1 - R4, as in the paper.
     * </p>
     *
     * @return All neighbouring philosophers (MutexLocks) who should be
     *         woken up as a result of being given Dibs or Forks by
     *         this philosopher as she applied her rules.  Never null.
     *         Never contains any null elements.
     *
     * @throws SecurityViolation If a Mutex refuses to let us lock it.
     *                           In practice this should never ever happen,
     *                           except perhaps due to buggy or
     *                           malicious Mutex code.  A SecurityViolation
     *                           thrown from this method will ALWAYS
     *                           result in this MutexLock aborting (no
     *                           longer hungry, no longer eating).
     */
    private Set<MutexLock> applyRules ()
        throws SecurityViolation
    {
        final Set<MutexLock> wake_ups = new HashSet<MutexLock> ();
        final boolean is_time_to_eat;
        // Rule R1:
        if ( this.isHungry )
        {
            List<Dibs> requests =
                new ArrayList<Dibs> ();
            synchronized ( this.lock )
            {
                Set<Dibs> all_dibs =
                    new HashSet<Dibs> ();
                all_dibs.addAll ( this.myDibs );
                for ( Dibs dibs_on_fork : all_dibs )
                {
                    if ( ! this.cleanForks.contains ( dibs_on_fork.fork () ) )
                    {
                        requests.add ( dibs_on_fork );
                        this.myDibs.remove ( dibs_on_fork );
                    }
                }
            }

            for ( Dibs dibs_on_fork : requests )
            {
                MutexLock neighbour =
                    dibs_on_fork.fork ().neighbourOf ( this );
                neighbour.take ( dibs_on_fork );
                wake_ups.add ( neighbour );
            }
        }

        // Rule R2:
        if ( ! this.isEating )
        {
            List<Fork> give_forks = new ArrayList<Fork> ();
            synchronized ( this.lock )
            {
                Set<Dibs> all_dibs_on_forks =
                    new HashSet<Dibs> ();
                all_dibs_on_forks.addAll ( this.myDibs );
                for ( Dibs dibs_on_fork : all_dibs_on_forks )
                {
                    Fork fork = dibs_on_fork.fork ();
                    if ( this.cleanForks.contains ( fork ) )
                    {
                        give_forks.add ( fork );
                        this.cleanForks.remove ( fork );
                    }
                }
            }

            for ( Fork fork : give_forks )
            {
                MutexLock neighbour = fork.neighbourOf ( this );
                neighbour.take ( fork );
                fork.dirty ( false );

                wake_ups.add ( neighbour );
            }
        }

        // Rule R3:
        synchronized ( this.lock )
        {
            for ( Dibs dibs_on_fork : this.receivedDibs )
            {
                this.myDibs.add ( dibs_on_fork );
            }
            this.receivedDibs.clear ();
        }

        // Rule R4:
        synchronized ( this.lock )
        {
            for ( Fork fork : this.dirtyForks )
            {
                this.cleanForks.add ( fork );
                fork.dirty ( false );
            }
            this.dirtyForks.clear ();
        }


        // At this point if we have all the forks we need, then
        // we should wake up so we can eat.
        synchronized ( this.lock )
        {
            if ( this.isHungry
                 && this.cleanForks.size () >= this.neededForks.size () )
            {
                this.isEating = true;
                for ( Mutex mutex : mutexes )
                {
                    try
                    {
                        mutex.lockedBy ( this );
                    }
                    catch ( SecurityViolation v )
                    {
                        // Uh oh.  Bad code.  Try to abort cleanly.
                        this.isHungry = false;
                        this.isEating = false;
                        throw v;
                    }
                }
            }
        }

        return wake_ups;
    }


    /**
     * @see java.lang.AutoCloseable#close()
     *
     * Unlocks this MutexLock.
     */
    @Override
    public void close ()
    {
        try
        {
            this.unlock ( this.securityContext );
        }
        catch ( SecurityViolation v )
        {
            // Keep on closing, nothing else we can do now anyway.
        }
    }


    /**
     * @see java.lang.Object#finalize()
     *
     * Unlocks this MutexLock.
     */
    @Override
    protected void finalize ()
    {
        if ( this.isEating )
        {
            try
            {
                this.unlock ( this.securityContext );
            }
            catch ( SecurityViolation v )
            {
                // Keep on finalizing, nothing else we can do now.
            }
        }
    }


    /**
     * <p>
     * Returns true if this MutexLock currently has any Dibs
     * for the specified Mutex shared with any of its neighbouring
     * philosophers (MutexLocks).
     * </p>
     *
     * <p>
     * TODO Re-evaluate: do we need to be more specific, and
     * ask if this philosopher has a Dibs for the specified Mutex
     * <i> shared with a specific neighbour </i> ?  Maybe...
     * </p>
     *
     * @param mutex The shared Mutex for which this lock might have Dibs.
     *              Must not be null.
     *
     * @return True if this MutexLock has any Dibs for the specified Mutex,
     *         either newly received, or in the pile;
     *         false if it does not.
     */
    protected boolean hasDibsFor (
                                  Mutex mutex
                                  )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               mutex );

        for ( Dibs dibs : this.myDibs )
        {
            if ( dibs.fork ().mutex () == mutex )
            {
                return true;
            }
        }

        for ( Dibs dibs : this.receivedDibs )
        {
            if ( dibs.fork ().mutex () == mutex )
            {
                return true;
            }
        }

        return false;
    }


    /**
     * <p>
     * Returns true if this MutexLock currently has any Forks
     * for the specified Mutex shared with any of its neighbouring
     * philosophers (MutexLocks).
     * </p>
     *
     * <p>
     * TODO Re-evaluate: do we need to be more specific, and
     * ask if this philosopher has a Fork for the specified Mutex
     * <i> shared with a specific neighbour </i> ?  Maybe...
     * </p>
     *
     * @param mutex The shared Mutex for which this lock might have Forks.
     *              Must not be null.
     *
     * @return True if this MutexLock has any Forks for the specified Mutex,
     *         either newly received and dirty, or clean;
     *         false if it does not.
     */
    protected boolean hasForkFor (
                                  Mutex mutex
                                  )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               mutex );

        for ( Fork fork : this.cleanForks )
        {
            if ( fork.mutex () == mutex )
            {
                return true;
            }
        }

        for ( Fork fork : this.dirtyForks )
        {
            if ( fork.mutex () == mutex )
            {
                return true;
            }
        }

        return false;
    }


    /**
     * <p>
     * Returns true if this philosopher is eating, that is, it has all
     * its Mutexes locked exclusively.
     * </p>
     *
     * @return True if all Mutexes are locked by this MutexLock;
     *         false if none of the Mutexes are locked by this MutexLock.
     *         (There is no middle ground, it's either all or nothing.)
     */
    protected boolean isEating ()
    {
        return this.isEating;
    }


    /**
     * <p>
     * Returns true if this MutexLock exists in the specified JVM
     * in the specified thread, false if not.
     * </p>
     *
     * @param system_id The unique identifier of the system (Java Virtual
     *                  Machine) in which this MutexLock exists.
     *                  Must not be null.
     *
     * @param thread_num The thread in which this MutexLock exists.
     *
     * @return True if this MutexLock exists in the specified thread
     *         in the specified system; false if not.
     */
    public boolean isSameThread (
                                 String system_id,
                                 long thread_num
                                 )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               system_id );

        if ( this.systemID ().equals ( system_id ) )
        {
            if ( this.threadNum () == thread_num )
            {
                return true;
            }
        }

        return false;
    }


    /**
     * <p>
     * Returns true if this MutexLock exists in the same system and
     * in the same thread as the specified MutexLock.
     * </p>
     *
     * @param that The other MutexLock, whose system ID and thread #
     *             will be compared.  Must not be null.
     */
    public boolean isSameThreadAs (
                                   MutexLock that
                                   )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               that );

        if ( this.systemID ().equals ( that.systemID () )
             && this.threadNum () == that.threadNum () )
        {
            return true;
        }

        return false;
    }


    /**
     * <p>
     * Locks all of the Mutexes for this MutexLock, or times
     * out trying to acquire the lock.
     * </p>
     *
     * @param security_context The security context in which to lock
     *                         this Mutex.  Typically this is the same
     *                         security context with which the MutexLock
     *                         was created.  Must have LOCK capability
     *                         on all the underlying Mutexes, or
     *                         a SecurityViolation will be thrown.
     *                         Must not be null.
     *
     * @param timeout_in_milliseconds How long to keep competing to
     *                                lock all of the Mutexes, in
     *                                milliseconds.  Must be greater
     *                                than 0.
     *
     * @throws TimeoutGuarantee.Violation If the Mutexes cannot be locked
     *                                    atomically within the time
     *                                    specified.
     */
    public void lock (
                      SecurityContext security_context,
                      long timeout_in_milliseconds
                      )
        throws Parameter2.MustBeGreaterThanZero.Violation,
               SecurityViolation,
               TimeoutGuarantee.Violation
    {
        // Set to false until we have successfully navigated through
        // parameter checks and security checks and so on:
        this.isHungry = false;
        this.isEating = false;

        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               security_context );
        this.contracts.check ( Parameter2.MustBeGreaterThanZero.CONTRACT,
                               timeout_in_milliseconds );

        // Check permissions or throw a SecurityViolation.
        Permissions required_permissions =
            new Permissions ( security_context,
                              Mutex.Capabilities.MUTEX_LOCK );
        this.security.request ( required_permissions ).orThrowChecked ();

        // Create the timeout guarantee.
        final TimeoutGuarantee timeout_guarantee =
            new TimeoutGuarantee ( timeout_in_milliseconds * 1000000L );

        // Any resources that our parent threads already have, we take
        // the forks for them from our neighbours.
        for ( Mutex mutex : this.mutexes )
        {
            mutex.addPhilosopher ( this );
        }

        // This MutexLock is a philosopher.
        // Every philosopher starts out hungry.
        // Typically once he finishes drinking and eating,
        // this philosopher will drink a cup of hemlock
        // (after a lengthy and melodramatic trial of course).
        this.isHungry = true;

        long start_time = timeout_guarantee.startTimeInNanoseconds ()
            / 1000000L;

        // We have to wait until we have all the forks.
        while ( true )
        {
            // Loop until we have nobody left to wake up, then we'll
            // go to sleep.
            while ( true )
            {
                // Don't synchronize on applyRules () or we'll
                // induce a race condition.
                final Set<MutexLock> wake_ups = this.applyRules ();

                if ( wake_ups.size () == 0 )
                {
                    break;
                }

                // Wake up the neighbours to apply their rules.
                for ( MutexLock neighbour : wake_ups )
                {
                    synchronized ( neighbour.lock )
                    {
                        neighbour.lock.notify ();
                    }
                }
            }

            if ( this.isEating )
            {
                // Stop waiting, eat now.
                break;
            }
            else if ( ! this.isHungry )
            {
                // Error condition, stop waiting.
                throw new TimeoutGuarantee.Violation ( timeout_guarantee,
                                                       this,
                                                       new Serializable [] {
                                                           mutexes,
                                                           this.name,
                                                           this.neededForks.size ()
                                                       } );
            }

            // Now sleep until we're woken up by another thread..
            synchronized ( this.lock )
            {
                // Keep waiting.
                try
                {
                    long current_time = System.currentTimeMillis ();
                    long remaining_timeout_in_milliseconds =
                        timeout_in_milliseconds - ( current_time - start_time );
                    if ( remaining_timeout_in_milliseconds <= 0L )
                    {
                        throw new InterruptedException ();
                    }

                    this.lock.wait ( remaining_timeout_in_milliseconds );
                }
                catch ( InterruptedException e )
                {
                    this.isHungry = false;
                    this.isEating = false;
                    for ( Mutex mutex : mutexes )
                    {
                        try
                        {
                            mutex.unlockedBy ( this );
                        }
                        catch ( Exception e2 )
                        {
                            // Keep trying to free up the other mutexes.
                            // We're going to throw an exception anyway.
                        }

                        try
                        {
                            mutex.removePhilosopher ( this );
                        }
                        catch ( Exception e2 )
                        {
                            // Keep trying to free up the other mutexes.
                            // We're going to throw an exception anyway.
                        }
                    }
                    throw new TimeoutGuarantee.Violation ( timeout_guarantee,
                                                           this,
                                                           new Serializable [] {
                                                               mutexes,
                                                               this.name,
                                                               this.neededForks.size ()
                                                           } );
                }

                if ( this.isEating )
                {
                    // Stop waiting, eat now.
                    break;
                }
            }
        }

        // OK it is finally time to eat!  Return to the
        // caller to eat.  They must then call unlock(),
        // but just in case they forget, we call it
        // from our finalize() method.
    }


    /**
     * <p>
     * Returns all of the Mutexes which this MutexLock is competing
     * to locK.
     * </p>
     *
     * @return A copy of the Mutexes which this MutexLock is competing
     *         to atomically lock.  Never null.  Never contains any
     *         null elements.
     */
    public Mutex [] mutexes ()
    {
        Mutex [] mutexes = new Mutex [ this.mutexes.length ];
        System.arraycopy ( this.mutexes, 0,
                           mutexes, 0, this.mutexes.length );
        return mutexes;
    }


    /**
     * <p>
     * Returns the name of this MutexLock (typically the name of the
     * thread in which it exists).
     * </p>
     *
     * @return This MutexLock's name.  Never null.
     */
    public String name ()
    {
        return this.name;
    }


    /**
     * <p>
     * Returns true if this MutexLock is competing to lock
     * the specified Mutex.
     * </p>
     *
     * @param needed_mutex The Mutex which this MutexLock might or might
     *                     not be competing to lock.  Must not be null.
     *
     * @return True if this MutexLock is competing to lock the specified
     *         Mutex; false if not.
     */
    public boolean needs (
                          Mutex needed_mutex
                          )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               needed_mutex );

        for ( Mutex mutex : this.mutexes )
        {
            if ( mutex == needed_mutex )
            {
                return true;
            }
        }

        return false;
    }


    /**
     * <p>
     * Returns true if this MutexLock needs the specified Fork in
     * order to atomically lock a Mutex.
     * </p>
     *
     * @param fork The Fork to check.  Must not be null.
     *
     * @return True if this MutexLock needs the specified Fork;
     *         false if it is not currently competing for the
     *         specified Fork.
     */
    public boolean needsFork (
                              Fork fork
                              )
        throws ParametersMustNotBeNull.Violation
    {
        synchronized ( this.lock )
        {
            if ( this.neededForks.contains ( fork ) )
            {
                return true;
            }

        }

        // Nope, not needed.
        return false;
    }


    /**
     * <p>
     * Puts the specified Fork back in the philosophical cutlery drawer.
     * </p>
     *
     * <p>
     * Once the Fork has been removed, this philosopher can no longer
     * compete with the neighbouring philosopher who was previously on
     * the other side of the fork.
     * </p>
     *
     * <p>
     * Typically this method is only ever called when either this
     * philosopher or her neighbour has finished eating (during unlock).
     * </p>
     *
     * @param fork The fork to put away.  Must not be null.
     */
    protected void removeNeededFork (
                                     Fork fork
                                     )
        throws ParametersMustNotBeNull.Violation,
               ForkMustBeNeededByMutexLock.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               fork );

        synchronized ( this.lock )
        {
            this.contracts.check ( this.forkMustBeNeededByMutexLockContract,
                                   fork );

            this.cleanForks.remove ( fork );
            this.dirtyForks.remove ( fork );
            this.myDibs.remove ( fork.dibs () );
            this.receivedDibs.remove ( fork.dibs () );
            this.neededForks.remove ( fork );
        }
    }


    /**
     * <p>
     * Returns the context in which this MutexLock was created, including
     * the Credentials to use for securely accessing the Mutexes to
     * lock.
     * </p>
     *
     * @return The SecurityContext in which this MutexLock was created.
     *         Never null.
     */
    protected SecurityContext securityContext ()
    {
        return this.securityContext;
    }


    /**
     * <p>
     * Returns the unique identifier for the JVM in which
     * this MutexLock exists.
     * </p>
     *
     * @return The unique ID of the JVM in which this MutexLock
     *         exists.  Never null.
     */
    public String systemID ()
    {
        return this.systemID;
    }


    /**
     * <p>
     * Puts the specified Fork into the dirty pile of Forks
     * for this philosopher.
     * </p>
     *
     * <p>
     * Later, this philosopher will clean the fork, and then
     * she'll be one fork closer to being ready to eat.
     * </p>
     *
     * @param fork The fork which this philosopher will take.
     *             Must be a fork that is needed by this MutexLock
     *             but which is not already in this MutexLock's
     *             dirty or clean piles.  Must not be null.
     */
    protected void take (
                         Fork fork
                         )
        throws ParametersMustNotBeNull.Violation,
               ForkMustBeNeededByMutexLock.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               fork );

        synchronized ( this.lock )
        {
            this.contracts.check ( this.forkMustBeNeededByMutexLockContract,
                                   fork );

            this.dirtyForks.add ( fork );
        }
    }


    /**
     * <p>
     * Puts the specified Dibs for a shared fork in this philosopher's
     * received pile, to be moved into her "my pile" later.
     * </p>
     *
     * <p>
     * Any given Dibs essentially means the philosopher is 2nd in line
     * to eat (lock a resource).
     * </p>
     *
     * @param dibs_on_fork The dibs to put into this philosopher's
     *                     receiving pile.  Must be the Dibs for one
     *                     of the Forks which this MutexLock needs.
     *                     Must not be null.
     */
    protected void take (
                         Dibs dibs_on_fork
                         )
        throws ParametersMustNotBeNull.Violation,
               ForkMustBeNeededByMutexLock.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               dibs_on_fork );

        synchronized ( this.lock )
        {
            this.contracts.check ( this.forkMustBeNeededByMutexLockContract,
                                   dibs_on_fork.fork () );

            this.receivedDibs.add ( dibs_on_fork );
        }
    }


    /**
     * <p>
     * Returns the unique identifier of the thread in which this
     * MutexLock exists in a specific JVM (the <code> systemID () </code>).
     * </p>
     *
     * <p>
     * Once this MutexLock has locked all Mutexes atomically,
     * then the thread with the specified id in the particular JVM
     * can safely access the resources behind the Mutexes without
     * any other threads acquiring MutexLocks on any of the same
     * Mutexes.
     * </p>
     *
     * @return The id of the thread in which this MutexLock exists,
     *         in a particular JVM.  Never null.
     */
    public long threadNum ()
    {
        return this.threadNum;
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString ()
    {
        return "MutexLock_" + this.name ();
    }


    /**
     * <p>
     * Unlocks this MutexLock, so that all of its locked Mutexes are freed
     * for locking by other MutexLocks.
     * </p>
     *
     * <p>
     * If this MutexLock is already unlocked then this method call
     * has no effect.
     * </p>
     *
     * @param security_context The subject requesting that this MutexLock
     *                         be unlocked.  Must not be null.
     *
     * @throws SecurityViolation If the specified security context does
     *                           not have permission to unlock this
     *                           MutexLock.
     */
    public void unlock (
                        SecurityContext security_context
                        )
        throws ParametersMustNotBeNull.Violation,
               SecurityViolation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               security_context );

        // Check permissions or throw a SecurityViolation.
        Permissions required_permissions =
            new Permissions ( security_context,
                              Mutex.Capabilities.MUTEX_UNLOCK );
        this.security.request ( required_permissions ).orThrowChecked ();

        final Set<MutexLock> wake_ups = new HashSet<MutexLock> ();

        final Set<Fork> all_forks = new HashSet<Fork> ();
        final Set<Fork> received_forks = new HashSet<Fork> ();
        synchronized ( this.lock )
        {
            // Already unlocked?  Don't do anything.
            if ( ! this.isHungry
                 && ! this.isEating )
            {
                return;
            }

            this.isHungry = false;
            this.isEating = false;

            for ( Mutex mutex : this.mutexes )
            {
                // Throw exceptions up the stack:
                mutex.unlockedBy ( this );

                // Throw exceptions up the stack:
                mutex.removePhilosopher ( this );
            }

            all_forks.addAll ( this.cleanForks );
            for ( Fork fork : all_forks )
            {
                this.removeNeededFork ( fork );
            }

            received_forks.addAll ( this.dirtyForks );
            for ( Fork fork : received_forks )
            {
                this.removeNeededFork ( fork );
            }
        }

        // Now that we're outside our own critical section,
        // remove the other philosophers' forks.
        // (It requires each other philosopher to go into
        // a critical section.)
        for ( Fork fork : all_forks )
        {
            MutexLock neighbour = fork.neighbourOf ( this );
            neighbour.removeNeededFork ( fork );
            wake_ups.add ( neighbour );
        }

        for ( Fork fork : received_forks )
        {
            MutexLock neighbour = fork.neighbourOf ( this );
            neighbour.removeNeededFork ( fork );
            wake_ups.add ( neighbour );
        }

        // Wake up the neighbours to apply their rules.
        for ( MutexLock neighbour : wake_ups )
        {
            synchronized ( neighbour.lock )
            {
                neighbour.lock.notify ();
            }
        }
    }
}
