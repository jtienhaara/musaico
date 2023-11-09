package musaico.platform.mutex;

import java.io.Serializable;

import java.util.HashSet;
import java.util.Set;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.Parameter3;
import musaico.foundation.contract.obligations.ParameterN;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * A Fork in the dining philosophers problem.
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
public class Fork
    implements Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20130214L;
    private static final String serialVersionHash =
        "0x8B73F0FEFE59A4A67FC6461218FADDB8B797C0B6";


    /** Checks contracts on static methods and constructors for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( Fork.class );


    /** No Fork at all (not useful to a hungry philosopher!) */
    public static final Fork NONE = new Fork ( Mutex.NONE,
                                               new HashSet<MutexLock> (),
                                               MutexLock.NONE,
                                               MutexLock.NONE2 );


    /** Checks obligations and guarantees on methods for us. */
    private final ObjectContracts contracts;

    private final Mutex mutex;

    private final MutexLock left;

    private final MutexLock right;

    private final Dibs dibs;

    private boolean isDirty;


    /**
     * <p>
     * Every new Fork must be constructed with a unique left-side
     * and right-side philosopher (MutexLock).  This obligation
     * enforces the obligation.
     * </p>
     */
    public static class LeftMustNotEqualRight
        extends Parameter3.MustContainNoDuplicates
        implements Serializable
    {
        private static final long serialVersionUID =
            Fork.serialVersionUID;

        public static final LeftMustNotEqualRight CONTRACT =
            new LeftMustNotEqualRight ();

        public static class Violation
            extends Parameter3.MustContainNoDuplicates.Violation
        {
            private static final long serialVersionUID =
                Fork.serialVersionUID;
        }
    }


    public Fork (
                 Mutex mutex,
                 Set<MutexLock> left_parents,
                 MutexLock left,
                 MutexLock right
                 )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation,
               LeftMustNotEqualRight.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               mutex, left_parents, left, right );
        classContracts.check ( Parameter2.MustContainNoNulls.CONTRACT,
                               left_parents );
        classContracts.check ( LeftMustNotEqualRight.CONTRACT,
                               new Serializable [] { left, right } );

        this.mutex = mutex;
        this.left = left;
        this.right = right;
        this.isDirty = true;
        this.dibs = new Dibs ( this );
        final boolean is_left_fork;

        this.left.addNeededFork ( this );
        this.right.addNeededFork ( this );

        this.contracts = new ObjectContracts ( this );

        // If the philosopher on the left has parents, give
        // the child the same forks and dibs that her parent(s)
        // have.
        boolean is_parent_fork_allocation = false;
        boolean is_parent_fork = false;
        for ( MutexLock parent : left_parents )
        {
            if ( parent.needs ( this.mutex ) )
            {
                if ( parent.hasDibsFor ( this.mutex ) )
                {
                    this.left.take ( this.dibs );
                }
                else
                {
                    this.right.take ( this.dibs );
                }

                if ( parent.hasForkFor ( this.mutex ) )
                {
                    this.left.take ( this );
                }
                else
                {
                    this.right.take ( this );
                }

                return;
            }
        }

        if ( is_parent_fork_allocation )
        {
            is_left_fork = is_parent_fork;
        }
        else if ( this.left.isEating () )
        {
            is_left_fork = true;
        }
        else if ( this.right.isEating () )
        {
            is_left_fork = false;
        }
        // The right philosopher was created first.  But
        // we give the fork to the left philosopher to start,
        // if the left philosopher's hash code is lower.
        else if ( this.left.hashCode () < this.right.hashCode () )
        {
            is_left_fork = true;
        }
        else
        {
            is_left_fork = false;
        }

        if ( is_left_fork )
        {
            // Left has this fork,
            // right has this fork's request token.
            this.left.take ( this );
            this.right.take ( this.dibs );
        }
        else
        {
            // Right has this fork,
            // left has this fork's request token.
            this.right.take ( this );
            this.left.take ( this.dibs );
        }
    }


    public Mutex mutex ()
    {
        return this.mutex;
    }


    public boolean dirty ()
    {
        return this.isDirty;
    }


    public void dirty (
                       boolean dirty
                       )
    {
        this.isDirty = dirty;
    }


    public MutexLock left ()
    {
        return this.left;
    }


    /**
     * <p>
!!!
     * Every new Fork must be constructed with a unique left-side
     * and right-side philosopher (MutexLock).  This obligation
     * enforces the obligation.
     * </p>
     */
    public static class MutexLockMustBeBesideThisFork
        extends Parameter1.MustContain
        implements Serializable
    {
        private static final long serialVersionUID =
            Fork.serialVersionUID;

        public MutexLockMustBeBesideThisFork ( MutexLock mutex_lock )
        {
            super ( mutex_lock );
        }

        public static class Violation
            extends Parameter1.MustContain.Violation
        {
            private static final long serialVersionUID =
                Fork.serialVersionUID;
        }
    }


    /**
     * <p>
     * Given one of the two neighbours of this fork (the philosopher
     * to the left or right of this fork), returns the philosopher
     * on the opposite side of the fork -- the given philosopher's
     * neighbour.
     * </p>
     *
     * @param philosopher The philosopher whose neighbour will be
     *                    returned.  Must be a philosopher (MutexLock)
     *                    who is either to the left or to the right
     *                    of this particular Fork.  Must not be null.
     *
     * @return The specified neighbouring philosopher (MutexLock).
     *         Never null.
     */
    public MutexLock neighbourOf (
                                  MutexLock philosopher
                                  )
        throws ParametersMustNotBeNull.Violation,
               MutexLockMustBeBesideThisFork.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               philosopher );
        this.contracts.check ( new MutexLockMustBeBesideThisFork ( philosopher ),
                               new Serializable [] { this.left, this.right } );

        if ( this.left == philosopher )
        {
            return this.right;
        }
        else // if ( this.right == philosopher )
        {
            return this.left;
        }
    }


    public Dibs dibs ()
    {
        return this.dibs;
    }


    public MutexLock right ()
    {
        return this.right;
    }


    public String toString ()
    {
        return "Fork_" + this.left + "_" + this.right;
    }
}
