package musaico.kernel.objectsystem.onode;


import musaico.io.Path;
import musaico.io.Reference;
import musaico.io.SoftReference;

import musaico.io.references.SimpleSoftReference;

import musaico.state.Graph;
import musaico.state.Node;
import musaico.state.SimpleGraph;
import musaico.state.SimpleNode;


/**
 * <p>
 * Represents the standard states of an ONode.
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
 * Copyright (c) 2009 Johann Tienhaara
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
public final class ONodeStates
    extends SimpleGraph
{
    /** Triggers state transitions when a new ONode's
     *  initialization is complete. */
    public static final SoftReference<Path> EVENT_INITIALIZED =
        new SimpleSoftReference<Path> ( new Path ( "/onode/events/initialized" ) );

    /** Triggers state transitions whenever an ONode becomes dirty. */
    public static final SoftReference<Path> EVENT_DIRTY =
        new SimpleSoftReference<Path> ( new Path ( "/onode/events/dirty" ) );

    /** Triggers state transitions whenever an ONode acquires dirty data. */
    public static final SoftReference<Path> EVENT_DIRTY_DATA =
        new SimpleSoftReference<Path> ( new Path ( "/onode/events/dirty_data" ) );

    /** Triggers state transitions whenever an ONode's pages become dirty. */
    public static final SoftReference<Path> EVENT_DIRTY_PAGES =
        new SimpleSoftReference<Path> ( new Path ( "/onode/events/dirty_pages" ) );

    /** Triggers state transitions whenever the ONode's pages and
     *  the ONode itself have been cleaned. */
    public static final SoftReference<Path> EVENT_CLEAN =
        new SimpleSoftReference<Path> ( new Path ( "/onode/events/clean" ) );

    /** Triggers state transitions whenever the ONode's pages, data and
     *  the ONode itself have all been cleaned. */
    public static final SoftReference<Path> EVENT_CLEAN_DATA =
        new SimpleSoftReference<Path> ( new Path ( "/onode/events/clean_data" ) );

    /** Triggers state transitions whenever the ONode's pages
     *  have been cleaned. */
    public static final SoftReference<Path> EVENT_CLEAN_PAGES =
        new SimpleSoftReference<Path> ( new Path ( "/onode/events/clean_pages" ) );

    /** Triggers state transitions whenever an ONode is ready to be freed. */
    public static final SoftReference<Path> EVENT_READY_TO_FREE =
        new SimpleSoftReference<Path> ( new Path ( "/onode/events/free" ) );

    /** Triggers state transitions whenever an ONode is being freed. */
    public static final SoftReference<Path> EVENT_FREEING =
        new SimpleSoftReference<Path> ( new Path ( "/onode/events/freeing" ) );

    /** Triggers state transitions whenever an ONode has been freed. */
    public static final SoftReference<Path> EVENT_FREED =
        new SimpleSoftReference<Path> ( new Path ( "/onode/events/freed" ) );


    /** A newly allocated ONode.  Must be transitioned to ACTIVE
     *  before anything can be done with the ONode. */
    public static final SimpleNode NEW =
        new SimpleNode ( "onode_new" );

    /** An active ONode. */
    public static final SimpleNode ACTIVE =
        new SimpleNode ( "onode_active" );

    /** An ONode which is dirty, but does not need to be written on
     *  data sync. */
    public static final SimpleNode DIRTY =
        new SimpleNode ( "onode_dirty" );

    /** An ONode which has dirty data, and so must be synchronized
     *  on data sync. */
    public static final SimpleNode DIRTY_DATA =
        new SimpleNode ( "onode_dirty_data" );

    /** An ONode which has dirty pages, but the ONode itself and its
     *  data is clean.  No need for synchronization on data sync. */
    public static final SimpleNode DIRTY_PAGES =
        new SimpleNode ( "onode_dirty_pages" );

    /** An ONode which is currently ready to be freed. */
    public static final SimpleNode READY_TO_FREE =
        new SimpleNode ( "onode_ready_to_free" );

    /** An ONode which is being freed. */
    public static final SimpleNode FREEING =
        new SimpleNode ( "onode_freeing" );

    /** An ONode which has been freed and so can be destroyed. */
    public static final SimpleNode FREED =
        new SimpleNode ( "onode_freed" );

    /** A new ONode which is corrupted (probably because it did not
     *  receive an EVENT_INITIALIZED trigger before operations on it began). */
    public static final SimpleNode ERROR =
        new SimpleNode ( "onode_error" );


    /** The standard state graph for ONodes. */
    public static final Graph GRAPH = new ONodeStates ();


    /**
     * <p>
     * Creates the standard ONodeStates graph.
     * </p>
     */
    private ONodeStates ()
    {
        super ( "onode_states" );

        // Initially: go to NEW state.
        this.onEnter ().go ( ONodeStates.NEW );


        // NEW         -> ACTIVE
        ONodeStates.NEW
            .on ( ONodeStates.EVENT_INITIALIZED )
            .go ( ONodeStates.ACTIVE );
        //             -> ERROR
        ONodeStates.NEW
            .otherwise ()
            .go ( ONodeStates.ERROR );

        // ACTIVE      -> DIRTY_PAGES
        ONodeStates.ACTIVE
            .on ( ONodeStates.EVENT_DIRTY_PAGES )
            .go ( ONodeStates.DIRTY_PAGES );
        //             -> DIRTY
        ONodeStates.ACTIVE
            .on ( ONodeStates.EVENT_DIRTY )
            .go ( ONodeStates.DIRTY );
        //             -> DIRTY_DATA
        ONodeStates.ACTIVE
            .on ( ONodeStates.EVENT_DIRTY_DATA )
            .go ( ONodeStates.DIRTY_DATA );

        //             -> READY_TO_FREE
        ONodeStates.ACTIVE
            .on ( ONodeStates.EVENT_READY_TO_FREE )
            .go ( ONodeStates.READY_TO_FREE );
        // Ignore other triggers.

        // DIRTY_PAGES -> DIRTY
        ONodeStates.DIRTY_PAGES
            .on ( ONodeStates.EVENT_DIRTY )
            .go ( ONodeStates.DIRTY );
        //             -> DIRTY_DATA
        ONodeStates.DIRTY_PAGES
            .on ( ONodeStates.EVENT_DIRTY_DATA )
            .go ( ONodeStates.DIRTY_DATA );
        //             -> ACTIVE
        ONodeStates.DIRTY_PAGES
            .on ( ONodeStates.EVENT_CLEAN_PAGES )
            .go ( ONodeStates.ACTIVE );
        //             -> READY_TO_FREE
        ONodeStates.ACTIVE
            .on ( ONodeStates.EVENT_READY_TO_FREE )
            .go ( ONodeStates.READY_TO_FREE );
        // Ignore other triggers.

        // DIRTY       -> DIRTY_DATA
        ONodeStates.DIRTY
            .on ( ONodeStates.EVENT_DIRTY_DATA )
            .go ( ONodeStates.DIRTY_DATA );
        //             -> ACTIVE
        ONodeStates.DIRTY
            .on ( ONodeStates.EVENT_CLEAN )
            .go ( ONodeStates.ACTIVE );
        // Ignore other triggers.

        // DIRTY_DATA  -> ACTIVE
        ONodeStates.DIRTY_DATA
            .on ( ONodeStates.EVENT_CLEAN_DATA )
            .go ( ONodeStates.ACTIVE );
        // Ignore other triggers.
    }
/*
!!!
 * Inode state bits.  Protected by inode_lock.
 *
 * Three bits determine the dirty state of the inode, I_DIRTY_SYNC,
 * I_DIRTY_DATASYNC and I_DIRTY_PAGES.
 *
 * Four bits define the lifetime of an inode.  Initially, inodes are I_NEW,
 * until that flag is cleared.  I_WILL_FREE, I_FREEING and I_CLEAR are set at
 * various stages of removing an inode.
 *
 * Two bits are used for locking and completion notification, I_LOCK and I_SYNC.
 *
 * I_DIRTY_SYNC         Inode is dirty, but doesn't have to be written on
 *                      fdatasync().  i_atime is the usual cause.
 * I_DIRTY_DATASYNC     Data-related inode changes pending. We keep track of
 *                      these changes separately from I_DIRTY_SYNC so that we
 *                      don't have to write inode on fdatasync() when only
 *                      mtime has changed in it.
 * I_DIRTY_PAGES        Inode has dirty pages.  Inode itself may be clean.
 * I_NEW                get_new_inode() sets i_state to I_LOCK|I_NEW.  Both
 *                      are cleared by unlock_new_inode(), called from iget().
 * I_WILL_FREE          Must be set when calling write_inode_now() if i_count
 *                      is zero.  I_FREEING must be set when I_WILL_FREE is
 *                      cleared.
 * I_FREEING            Set when inode is about to be freed but still has dirty
 *                      pages or buffers attached or the inode itself is still
 *                      dirty.
 * I_CLEAR              Set by clear_inode().  In this state the inode is clean
 *                      and can be destroyed.
 *
 *                      Inodes that are I_WILL_FREE, I_FREEING or I_CLEAR are
 *                      prohibited for many purposes.  iget() must wait for
 *                      the inode to be completely released, then create it
 *                      anew.  Other functions will just ignore such inodes,
 *                      if appropriate.  I_LOCK is used for waiting.
 *
 * I_LOCK               Serves as both a mutex and completion notification.
 *                      New inodes set I_LOCK.  If two processes both create
 *                      the same inode, one of them will release its inode and
 *                      wait for I_LOCK to be released before returning.
 *                      Inodes in I_WILL_FREE, I_FREEING or I_CLEAR state can
 *                      also cause waiting on I_LOCK, without I_LOCK actually
 *                      being set.  find_inode() uses this to prevent returning
 *                      nearly-dead inodes.
 * I_SYNC               Similar to I_LOCK, but limited in scope to writeback
 *                      of inode dirty data.  Having a separate lock for this
 *                      purpose reduces latency and prevents some filesystem-
 *                      specific deadlocks.
 *
 * Q: What is the difference between I_WILL_FREE and I_FREEING?
 * Q: igrab() only checks on (I_FREEING|I_WILL_FREE).  Should it also check on
 *    I_CLEAR?  If not, why?
 */
}
