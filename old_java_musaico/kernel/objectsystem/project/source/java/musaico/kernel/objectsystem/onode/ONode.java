// !!! When it's time to do quotas consider carrying
// !!! the list of quotas applicable to this node
// !!! (so that lookup from the superblock isn't necessary).
package musaico.kernel.objectsystem.onode;

import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Identifier;
import musaico.io.Path;

import musaico.kernel.memory.SegmentIdentifier;

import musaico.kernel.objectsystem.Record;
import musaico.kernel.objectsystem.RecordFlag;

import musaico.mutex.Mutex;

import musaico.region.Region;

import musaico.security.Credentials;
import musaico.security.Security;


/**
 * <p>
 * The interface implemented by ONodes (object nodes in a tree
 * of hierarchical objects, flat records, drivers, symbolic links
 * and other object relations).  Roughly
 * equivalent to inodes (or vnodes) in a UNIX-like file system.
 * </p>
 *
 * <p>
 * Every ONode implementation must be sure to keep its own
 * metadata up to date.  In particular a number of metadata
 * attributes must be set through the ONodeMetadata object
 * returned from <code> onode.metadata () </code>.
 * </p>
 *
 * @see musaico.kernel.objectsystem.onode.ONodeMetadata
 *
 * <p>
 * Note that security is not provided directly by the ONode interface,
 * but rather through the extensible Security and Permissions interfaces
 * (@see musaico.security.Security).
 * This may be used to implement traditional UNIX-style ugo+-rwx permissions,
 * or a capabilities framework, and so on.
 * (
 * @link{http://zesty.ca/capmyths/},
 * @link{http://www.imperialviolet.org/binary/pucs.pdf})
 * Nothing akin to the Linux Security Modules (LSM) has been implemented
 * (and will not be in the foreseeable future -- it's way too much work!).
 * </p>
 *
 *
 * <p>
 * Because object systems can conceivably be distributed, every ONode
 * must be Serializable in order to play nicely over RMI.
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
 * Copyright (c) 2009, 2010, 2011, 2012 Johann Tienhaara
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
public interface ONode
    extends Serializable
{
    /** No ONode.  Useful for data structures such as Sequence,
     *  which require an ONode alternative to NULL. */
    public static final ONode NONE = new NoONode ();


    /**
     * <p>
     * Returns this ONode's data and data operations, allowing
     * callers to open, close, read and write Fields from/to
     * this ONode.
     * </p>
     *
     * @return This ONOde's data and data operations.  Never null.
     */
    public abstract Record data ();


    /**
     * <p>
     * Returns the unique identifier for this ONode.  The id is unique
     * everywhere within the SuperBlock containing the ONode.
     * </p>
     *
     * <p>
     * Note that an ONode id is <i>not</i> the same as an OEntry id.
     * Typically an ONode has an identifier which helps the Driver
     * driver find its Fields (like an inode number), whereas an OEntry
     * has a path identifier (such as "/foo/bar").
     * </p>
     *
     * @return This ONode's id.  Never null.
     */
    public abstract ONodeIdentifier id ();


    /**
     * <p>
     * Creates a hard link to this ONode.
     * </p>
     *
     * @param credentials Who is asking for this operation
     *                    to be performed.  Must have link access
     *                    for this ONOde.  Must not be null.
     *
     * @return The total number of references remaining to the target
     *         ONode, including readers, writers and hard links.
     *         Always greater than 0.
     *
     * @throws ONodeOperationException If any failure occurs linking the
     *                                 target ONode.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      specified is invalid
     *                                      (see above).
     */
    public abstract long link (
                               Credentials credentials,
                               OEntry entry
                               )
        throws ONodeOperationException,
               I18nIllegalArgumentException;


    /**
     * <p>
     * Looks up an ONode by name (relative to the specified OEntry,
     * or absolute if it starts with a "/").
     * </p>
     *
     * @param credentials Who is asking for this operation
     *                    to be performed.  Must have lookup access
     *                    for this ONode.  Must not be null.
     *
     * @param entry The OEntry which points to this ONode.
     *              Must not be null.
     *
     * @param path The OEntry entry to lookup.
     *             For example, if the entry parameter is
     *             the top level OEntry of an object and
     *             the child "foo/bar" is looked up, then
     *             the entry's grandchild ONode will be
     *             retrieved.  Or if the entry parameter is
     *             some deeply nested OEntry, and the path
     *             "/" is looked up, then the root of all
     *             object systems will be returned. And so on.
     *             Must not be null.
     *
     * @return The specified OEntry, or null if no OEntry
     *         exists at the specified path (if relative then it
     *         is relative to the specified entry).
     *
     * @throws ONodeOperationException if any failure occurs.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (null and so on).
     */
    public abstract OEntry lookup (
                                   Credentials credentials,
                                   OEntry entry,
                                   Path path
                                   )
        throws ONodeOperationException,
               I18nIllegalArgumentException;


    /**
     * <p>
     * Returns this ONode's metadata and metadata operations, allowing
     * callers to open, close, read and write metadata Fields from/to
     * this ONode, such as the created and modified times, the
     * number of Fields used, and so on.
     * </p>
     *
     * @return This ONOde's metadata and metadata operations.
     *         Never null.
     */
    public abstract ONodeMetadata metadata ();


    /**
     * <p>
     * Renames this ONode and/or moves it to a different parent OEntry.
     * </p>
     *
     * @param credentials Who is asking for this operation
     *                    to be performed.  Must have move
     *                    access for this ONode as well as read
     *                    and write access for the old and new
     *                    parent ONodes.  Must not be null.
     *
     * @param old_entry The OEntry which will point to this ONode.
     *                  Must not be null.
     *
     * @param new_entry The new entry at which to place this ONode
     *                  (possibly with the same or a different
     *                  parent OEntry, depending on whether it's a
     *                  full move or just a rename).  Must not be null.
     *
     * @throws ONodeOperationException if any failure occurs.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (null and so on).
     */
    public abstract void move (
                               Credentials credentials,
                               OEntry old_entry,
                               OEntry new_entry
                               )
        throws ONodeOperationException,
               I18nIllegalArgumentException;


    /**
     * <p>
     * All operations which need to use this ONode with mutual
     * exclusion must acquire a MutexLock on this and any other
     * ONodes by doing the following:
     * </p>
     *
     * <pre>
     *     ONode onode = ...;
     *     MutexLock mutex_lock = new ONodeLock ( onode.mutex () );
     *     mutex_lock.lock ( ...timeout in milliseconds... );
     *     try
     *     {
     *         ...do stuff to onode...
     *     }
     *     finally
     *     {
     *         mutex_lock.unlock ();
     *     }
     * </pre>
     *
     * <p>
     * Multiple ONodes can be locked at once with:
     * </p>
     *
     *
     * <pre>
     *     ONode onode1 = ...;
     *     ONode onode2 = ...;
     *     ONode onode3 = ...;
     *     MutexLock mutex_lock = new ONodeLock ( onode1.mutex (),
     *                                            onode2.mutex (),
     *                                            onode3.mutex () );
     *     mutex_lock.lock ( ...timeout in milliseconds... );
     *     try
     *     {
     *         ...do stuff to onodes...
     *     }
     *     finally
     *     {
     *         mutex_lock.unlock ();
     *     }
     * </pre>
     *
     * <p>
     * The ONodeLock throws an ONodeOperationException if
     * it takes too long to acquire the lock(s).  Alternatively
     * use the RecordLock to throw a RecordOperationException
     * (for example from the methods specified by the Record
     * interface which must be implemented by the data and metadat
     * areas of the ONode) or SuperBlockLock to throw a
     * SuperBlockOperationException.
     * </p>
     *
     * <p>
     * The only difference between such mutex locks is the type of
     * exception thrown, for convenience.
     * </p>
     *
     * @param credentials The User or Module identifier requesting
     *                    this ONode's mutex.  Must not be null.
     *
     * @return This ONode's Mutex.  Never null.
     *
     * @throws ONodeSecurityException If the specified credentials
     *                                may not retrieve this ONode's
     *                                Mutex.
     */
    public abstract Mutex mutex (
                                 Credentials credentials
                                 )
        throws ONodeSecurityException;


    /**
     * <p>
     * Returns the reference counters for this ONode (one for
     * number of open readers, one for number of open writers, one
     * for number of hard links, and so on).
     * </p>
     *
     * @return The references to this ONode.  Never null.
     */
    public abstract ONodeReferences references ();


    /**
     * <p>
     * Returns the security for this ONode, which
     * determines who is (and who isn't) allowed to do what.
     * </p>
     *
     * <p>
     * A UNIX-like ONode might be implemented with simple
     * RecordFlags (read, write, exec, append, and so on),
     * and simply check against the various permissions when
     * performing operations.  However more complex
     * security frameworks can also be provided, at the
     * object system designer's discretion.
     * </p>
     *
     * <p>
     * Advisory flags such as
     * <code> RecordFlag.RANDOM_ACCESS </code> or
     * <code> RecordFlag.SEQUENTIAL_ACCESS </code> and so on
     * can also be included in the permissions requested and/or
     * allowed.
     * </p>
     *
     * @return The Security for this ONode.  Never null.
     */
    public abstract Security<RecordFlag> security ();


    /**
     * <p>
     * Returns a reference to the SuperBlock to which this
     * ONode belongs.
     * </p>
     *
     * <p>
     * Typically the actual SuperBlock is then retrieved with
     * <code> my_module.kernelObject ( SuperBlock.class, ref ) </code>,
     * where <code> ref </code> is the identifier returned by
     * this method.
     * </p>
     *
     * <p>
     * Because the SuperBlockIdentifier has not yet been
     * defined, this interface must declare a generic Identifier
     * as the return value.  However every implementation shall
     * declare and return <code> SuperBlockIdentifier </code>.
     * </p>
     *
     * @return This ONode's SuperBlockIdentifier.  Always a
     *         SuperBlockIdentifier.  Never null.
     */
    public abstract Identifier superBlockRef ();


    /**
     * <p>
     * Truncates the Fields in an ONode to a specific number of
     * Fields by removing all Fields outside the specified region.
     * </p>
     *
     * <p>
     * This operation does not affect the child ONodes of an ObjectONode,
     * only the Fields in an object, flat record and so on.
     * </p>
     *
     * @param credentials Who is asking for this operation
     *                    to be performed.  Must have truncate
     *                    and write access for this ONode.
     *                    Must not be null.
     *
     * @param entry The OEntry which will point to this ONode.
     *              Must not be null.
     *
     * @param truncate_all_fields_outside_region The region to keep,
     *                                           while all Fields outside
     *                                           this area shall be deleted.
     *                                           Must not be null.
     *                                           One common way to
     *                                           truncate to zero-length
     *                                           is to pass an empty
     *                                           Region.  Does not affect
     *                                           child ONodes if this is
     *                                           an hierarchical object
     *                                           ONode.
     *
     * @throws ONodeOperationException if any failure occurs. 
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (null and so on).
    */
    public abstract void truncate (
                                   Credentials credentials,
                                   OEntry entry,
                                   Region truncate_all_fields_outside_region
                                   )
        throws ONodeOperationException,
               I18nIllegalArgumentException;


    /**
     * <p>
     * Removes a reference to this ONode.  Once the reference count
     * reaches 0, this ONode is deleted from the SuperBlock.
     * </p>
     *
     * <p>
     * Because each hierarchical object ONode always has a "." OEntry
     * hard linked to it, the removeObject () method in ObjectONode
     * is more used for permanently removing an hierarchical object
     * from an object system.
     * </p>
     *
     * @param credentials Who is asking for this operation
     *                    to be performed.  Must have unlink
     *                    access for this ONode.  Must not be null.
     *
     * @param entry The OEntry which will point to this ONode.
     *              Must not be null.
     *
     * @return The total number of references remaining to the
     *         ONode, including readers, writers and hard links.
     *         At zero, the ONode has been removed from the system
     *         completely by the time this call returns.
     *         Always 0 or greater.
     *
     * @throws ONodeOperationException if any failure occurs.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (null and so on).
     */
    public abstract long unlink (
                                 Credentials credentials,
                                 OEntry entry
                                 )
        throws ONodeOperationException,
               I18nIllegalArgumentException;
}
