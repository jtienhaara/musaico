package musaico.kernel.objectsystem.superblock;

import java.io.Serializable;


import musaico.buffer.Buffer;

import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Identifier;
import musaico.io.Progress;
import musaico.io.ReferenceCount;
import musaico.io.Sequence;

import musaico.kernel.objectsystem.CursorIdentifier;
import musaico.kernel.objectsystem.RecordFlag;
import musaico.kernel.objectsystem.RecordTypeIdentifier;

import musaico.kernel.objectsystem.onode.OEntry;
import musaico.kernel.objectsystem.onode.ONode;
import musaico.kernel.objectsystem.onode.ONodeMetadata;

import musaico.kernel.objectsystem.quota.Quotas;

import musaico.mutex.Mutex;

import musaico.region.Region;
import musaico.region.Space;

import musaico.security.Credentials;
import musaico.security.Permissions;
import musaico.security.Security;


/**
 * <p>
 * Represents a mounted object system, containing
 * statistics about the object system, structures for maintaining
 * the system, and so on.
 * </p>
 *
 * <p>
 * Equivalent to the super block of a file system in a UNIX-like
 * operating system.
 * </p>
 *
 *
 * <p>
 * Because object systems can conceivably be distributed, every SuperBlock
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
 * Copyright (c) 2010, 2011 Johann Tienhaara
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
public interface SuperBlock
    extends Serializable
{
    /**
     * <p>
     * Allocates an empty ONode in this object system, with
     * the specified settings.
     * </p>
     *
     * @param credentials Who is asking for the operation, such as
     *                    a user Credentials or a Module's Credentials.
     *                    Must not be null.
     *
     * @param progress The progress meter which can be used by
     *                 callers to monitor how close to completion
     *                 the requested operation is.  Must not be null.
     *
     * @param onode_security The Security which will protect the
     *                       new ONode from unauthorized access,
     *                       providing RecordPermission.READ and so on.
     *                       Note that the SuperBlock might wrap this
     *                       Security in another layer, depending on
     *                       the implementation.  Must not be null.
     *
     * @param onode_type The type of ONode to create (such as
     *                   <code> FlatRecord.TYPE_ID </code> or
     *                   <code> SymbolicLink.TYPE_ID </code>
     *                   and so on.  Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (see above).
     *
     * @throws SuperBlockOperationException If anything goes horribly wrong
     *                                      (for example, this
     *                                      object system does not
     *                                      support the specified
     *                                      onode_type).
     *
     * @see musaico.kernel.objectsystem.SuperBlock#free(ONode)
     */
    public abstract ONode allocate (
                                    Credentials credentials,
                                    Progress progress,
                                    Security<RecordFlag> onode_security,
                                    RecordTypeIdentifier onode_type
                                    )
        throws I18nIllegalArgumentException,
               SuperBlockOperationException;


    /**
     * <p>
     * Returns references to all the Cursors which are currently open on
     * records in this object system.
     * </p>
     *
     * <p>
     * Typically this method returns a copy of the ids of the in-memory
     * region of Cursors.
     * </p>
     *
     * <p>
     * To resolve a particular Cursor, typically invoke:
     * <code> my_module.kernelObject ( Cursor.class, ref ) </code>
     * where <code> ref </code> is one of the references returned
     * by this method.
     * </p>
     *
     * @return The region containing all Cursors open on records
     *         in this object system.  Never null.  May be zero length.
     */
    public abstract Sequence<CursorIdentifier> cursorRefs ();


    /**
     * <p>
     * Invoked when the specified ONode's referencedByXYZ()'s
     * have all reached 0 (hard link references, reader references,
     * and writer references).
     * </p>
     *
     * @param credentials Who is asking for the operation, such as
     *                    a user Credentials or a Module's Credentials.
     *                    Must not be null.
     *
     * @param progress The progress meter which can be used by
     *                 callers to monitor how close to completion
     *                 the requested operation is.  Must not be null.
     *
     * @param onode The object data to delete from the object system.
     *              Must not be null.  Must be a valid ONode from
     *              this SuperBlock.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (see above).
     *
     * @throws SuperBlockOperationException If the specified ONode
     *                                      is invalid or in use,
     *                                      and so on.
     */
    public abstract void delete (
                                 Credentials credentials,
                                 Progress progress,
                                 ONode onode
                                 )
        throws I18nIllegalArgumentException,
               SuperBlockOperationException;


    /**
     * <p>
     * Frees the specified object data from memory.
     * </p>
     *
     * @param credentials Who is asking for the operation, such as
     *                    a user Credentials or a Module's Credentials.
     *                    Must not be null.
     *
     * @param progress The progress meter which can be used by
     *                 callers to monitor how close to completion
     *                 the requested operation is.  Must not be null.
     *
     * @param onode The object node to free.  Must not be null.
     *              Must be a valid ONode from this object system.
     *              Must have 0 reference counts (hard links, readers
     *              and writers).
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (see above).
     *
     * @throws SuperBlockOperationException If anything goes horribly wrong.
     *
     * @see musaico.kernel.objectsystem.SuperBlock#allocate()
     */
    public abstract void free (
                               Credentials credentials,
                               Progress progress,
                               ONode onode
                               )
        throws I18nIllegalArgumentException,
               SuperBlockOperationException;


    /**
     * <p>
     * Marks the specified object data as dirty, so that its
     * backing paged area will be synchronized at the next
     * opportunity.
     * </p>
     *
     * @param credentials Who is asking for the operation, such as
     *                    a user Credentials or a Module's Credentials.
     *                    Must not be null.
     *
     * @param progress The progress meter which can be used by
     *                 callers to monitor how close to completion
     *                 the requested operation is.  Must not be null.
     *
     * @param onode The object node to mark as dirty.
     *              Must not be null.  Must be a valid ONode from
     *              this object system.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (see above).
     *
     * @throws SuperBlockOperationException If anything goes horribly wrong.
     */
    public abstract void dirty (
                                Credentials credentials,
                                Progress progress,
                                ONode onode
                                )
        throws I18nIllegalArgumentException,
               SuperBlockOperationException;


    /**
     * <p>
     * Returns the unique identifier of this SuperBlock, unique across
     * all SuperBlocks in this kernel.
     * </p>
     *
     * @return This super block's unique identifier.  Never null.
     */
    public abstract SuperBlockIdentifier id ();


    /**
     * <p>
     * Returns true if this super block has data or metadata
     * which must be swapped out at the next opportunity because
     * they have been modified while swapped in.
     * </p>
     *
     * @return True if this super block needs to be synchronized,
     *         false if this super block has already written out any
     *         and all changed data swapped out by the backing
     *         paged area.
     */
    public abstract boolean isDirty ();


    /**
     * <p>
     * All operations which need to use this SuperBlock with mutual
     * exclusion must acquire a MutexLock on this (and any ONodes or
     * other objects for locking at the same time) by doing the
     * following:
     * </p>
     *
     * <pre>
     *     SuperBlock super_block = ...;
     *     MutexLock mutex_lock =
     *         new SuperBlockLock ( super_block.mutex () );
     *     mutex_lock.lock ( ...timeout in milliseconds... );
     *     try
     *     {
     *         ...do stuff to super_block...
     *     }
     *     finally
     *     {
     *         mutex_lock.unlock ();
     *     }
     * </pre>
     *
     * <p>
     * The SuperBlock and multiple ONodes can be locked at once with:
     * </p>
     *
     *
     * <pre>
     *     SuperBlock super_block = ...;
     *     ONode onode1 = ...;
     *     ONode onode2 = ...;
     *     ONode onode3 = ...;
     *     MutexLock mutex_lock = new SuperBlockLock ( super_block.mutex (),
     *                                                 onode1.mutex (),
     *                                                 onode2.mutex (),
     *                                                 onode3.mutex () );
     *     mutex_lock.lock ( ...timeout in milliseconds... );
     *     try
     *     {
     *         ...do stuff to super_block and onodes...
     *     }
     *     finally
     *     {
     *         mutex_lock.unlock ();
     *     }
     * </pre>
     *
     * <p>
     * The SuperBlockLock throws a SuperBlockOperationException if
     * it takes too long to acquire the lock(s).  Alternatively
     * use the RecordLock to throw a RecordOperationException
     * (for example from the methods specified by the Record
     * interface which must be implemented by ONode data and metadata)
     * or ONodeLock to throw an ONodeOperationException.
     * </p>
     *
     * @param credentials The user or module credentials requesting
     *                    the mutex to lock this SuperBlock.
     *                    Must not be null.
     *
     * @return This SuperBlock's Mutex.  Never null.
     */
    public abstract Mutex mutex (
                                 Credentials credentials
                                 )
        throws SuperBlockSecurityException;


    /**
     * <p>
     * Returns a snapshot of the metadata for this object system,
     * including number of records free and used, last access time,
     * and so on.
     * </p>
     *
     * @return This super block's metadata.  Never null.
     */
    public abstract ONodeMetadata metadata ();


    /**
     * <p>
     * Returns a reference to the type of the object
     * system (akin to the magic number of a file
     * system in a UNIX-like operating system).
     * </p>
     *
     * <p>
     * The magic number of a SuperBlock may be used to
     * retrieve the ObjectSystemType which is its
     * type by calling:
     * <code> my_module.kernelObject ( ObjectSystemType.class, ref ) </code>,
     * where <code> ref </code> is the reference returned
     * by this method.
     * </p>
     *
     * @return A reference to the ObjectSystemType which created
     *         this SuperBlock.  Always an ObjectSystemTypeIdentifier.
     *         Never null.
     */
    public abstract Identifier objectSystemTypeRef ();


    /**
     * <p>
     * Returns the region containing all ONodes in this SuperBlock.
     * </p>
     *
     * <p>
     * Typically a copy of the in-memory region of ONodes is returned.
     * </p>
     *
     * <p>
     * It is assumed that anyone who has access to this SuperBlock
     * should also have full access to all its ONodes.  Thus the actual
     * ONodes are returned, rather than references to them.
     * </p>
     *
     * @param credentials Who is asking for the operation, such as
     *                    a user Credentials or a Module's Credentials.
     *                    Must not be null.
     *
     * @param progress The progress meter which can be used by
     *                 callers to monitor how close to completion
     *                 the requested operation is.  Must not be null.
     *
     * @return This SuperBlock's ONodes.  Never null.  May be 0-length.
     *         Contains only ONodes, no other object types.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (see above).
     *
     * @throws SuperBlockOperationException If the specified credentials
     *                                      are not allowed to access
     *                                      all the ONodes of this
     *                                      SuperBlock.
     */
    public abstract Sequence<ONode> onodes (
                                            Credentials credentials,
                                            Progress progress
                                            )
        throws I18nIllegalArgumentException,
               SuperBlockOperationException;


    /**
     * <p>
     * Returns the quota block for this super block, specifying
     * hard and soft limits for users, groups and so on for
     * using this object system.
     * </p>
     *
     * @return This SuperBlock's quota block.  Never null.
     */
    public abstract Quotas quotas ();


    /**
     * <p>
     * Returns the options which were used to mount this
     * object system.
     * </p>
     *
     * <p>
     * Every SuperBlock:
     * </p>
     *
     * <ul>
     *   <li> <i>must</i> return all non-default options; and </li>
     *   <li> <i>may</i> return any default options.
     * </ul>
     *
     * <p>
     * For example, suppose the default option for a SuperBlock
     * is "foobar=7".  Then if the object system is mounted
     * with "foobar=9", it must report this as an option.
     * However if it is mounted with "foobar=7", or with no
     * "foobar" option specified at all, it may report foobar
     * as an option, though it is not required to.
     * </p>
     *
     * @param credentials Who is asking for the operation, such as
     *                    a user Credentials or a Module's Credentials.
     *                    Must not be null.
     *
     * @param progress The progress meter which can be used by
     *                 callers to monitor how close to completion
     *                 the requested operation is.  Must not be null.
     *
     * @param placeholder The buffer to fill with the option
     *                    settings that were used to mount
     *                    this SuperBlock.  Must not be null null.
     *
     * @return The actual region of the buffer filled by the
     *         mount options.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (see above).
     *
     * @throws SuperBlockOperationException If the specified Buffer is not
     *                                      large enough to contain the mount
     *                                      options.
     */
    public Region readMountOptions (
                                    Credentials credentials,
                                    Progress progress,
                                    Buffer placeholder
                                    )
        throws I18nIllegalArgumentException,
               SuperBlockOperationException;


    /**
     * <p>
     * The number of references using this SuperBlock (object system
     * administration, mount, and so on).
     * </p>
     *
     * <p>
     * Only when
     * <code>references ().size () == 0</code>
     * can this SuperBlock be destroyed.
     * </p>
     *
     * <p>
     * Note that calling <code> references ().dereference () </code>
     * is equivalent to calling a a superblock's <code> put_super () </code>
     * method in Linux.
     * </p>
     *
     * @return This SuperBlock's referers.  Never null.
     */
    public abstract ReferenceCount references ();


    /**
     * <p>
     * Remounts the mounted object system represented by
     * this superblock.
     * </p>
     *
     * @param credentials Who is asking for the operation, such as
     *                    a user Credentials or a Module's Credentials.
     *                    Must not be null.
     *
     * @param progress The progress meter which can be used by
     *                 callers to monitor how close to completion
     *                 the requested operation is.  Must not be null.
     *
     * @param mount_options The mount options.  Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (see above).
     *
     * @throws SuperBlockOperationException If this SuperBlock is
     *                                      in a bad state, or if
     *                                      anything goes horribly
     *                                      wrong while re-mounting.
     */
    public abstract void remountObjectSystem (
                                              Credentials credentials,
                                              Progress progress,
                                              Buffer mount_options
                                              )
        throws I18nIllegalArgumentException,
               SuperBlockOperationException;


    /**
     * <p>
     * Returns the root entry in this object system.
     * </p>
     *
     * @return The root OEntry in this object system.  Never null.
     */
    public abstract OEntry root ();


    /**
     * <p>
     * Returns the security policy for this SuperBlock.
     * Any and all methods requiring Credentials parameters
     * are checked against super block permissions in
     * this security policy.
     * </p>
     *
     * @see musaico.kernel.objectsystem.SuperBlockFlag
     * @see musaico.kernel.objectsystem.SuperBlockPermission
     * @see musaico.kernel.objectsystem.SuperBlockPermissions
     *
     * @return This SuperBlock's security policy.  Never null.
     */
    public abstract Security<SuperBlockFlag> security ();


    /**
     * <p>
     * Returns the Space defining the Regions of all ONodes in this
     * SuperBlock, such as an ArraySpace for an object system
     * which addresses Fields by array index positions.
     * </p>
     *
     * @return The Space defining the Regions covered by ONodes
     *         in this object system.  Never null.
     */
    public abstract Space space ();


    /**
     * <p>
     * Synchronizes all dirty ONodes in this object system,
     * inducing the backing paged area to swap out to the
     * backing driver(s).
     * </p>
     *
     * <p>
     * This operation blocks the caller.
     * </p>
     *
     * @param credentials Who is asking for the operation, such as
     *                    a user Credentials or a Module's Credentials.
     *                    Must not be null.
     *
     * @param progress The progress meter which can be used by
     *                 callers to monitor how close to completion
     *                 the requested operation is.  Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (see above).
     *
     * @throws SuperBlockOperationException If the backing paged area
     *                                      throws any exceptions, or if
     *                                      generally anything goes
     *                                      horribly wrong.
     */
    public abstract void syncObjectSystem (
                                           Credentials credentials,
                                           Progress progress
                                           )
        throws I18nIllegalArgumentException,
               SuperBlockOperationException;


    /**
     * <p>
     * Writes the specified object data out, causing the backing
     * paged area to swap out dirty data to the backing
     * driver(s).
     * </p>
     *
     * <p>
     * This operation blocks the caller.
     * </p>
     *
     * @param credentials Who is asking for the operation, such as
     *                    a user Credentials or a Module's Credentials.
     *                    Must not be null.
     *
     * @param progress The progress meter which can be used by
     *                 callers to monitor how close to completion
     *                 the requested operation is.  Must not be null.
     *
     * @param onode The object node to write out.  Must not be null.
     *              Must be a valid ONode in this object system.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (see above).
     *
     * @throws SuperBlockOperationException If the underlying paged
     *                                      area throws any exceptions,
     *                                      or if generally anything
     *                                      goes horribly wrong.
     */
    public abstract void write (
                                Credentials credentials,
                                Progress progress,
                                ONode onode
                                )
        throws I18nIllegalArgumentException,
               SuperBlockOperationException;


    /**
     * <p>
     * Writes out this SuperBlock, inducing the backing
     * PagedArea to write out dirty super block metadata.
     * </p>
     *
     * <p>
     * This operation blocks the caller.
     * </p>
     *
     * @param credentials Who is asking for the operation, such as
     *                    a user Credentials or a Module's Credentials.
     *                    Must not be null.
     *
     * @param progress The progress meter which can be used by
     *                 callers to monitor how close to completion
     *                 the requested operation is.  Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (see above).
     *
     * @throws SuperBlockOperationException If the underlying paged
     *                                      area throws any exceptions,
     *                                      or if generally anything
     *                                      goes horribly wrong.
     */
    public abstract void writeSuperBlock (
                                          Credentials credentials,
                                          Progress progress
                                          )
        throws I18nIllegalArgumentException,
               SuperBlockOperationException;


    /**
!!!
struct super_block {
        struct list_head        s_list;         / Keep this first /
        dev_t                   s_dev;          / search index; _not_ kdev_t /
        unsigned char           s_dirt;
        unsigned char           s_blocksize_bits;
        unsigned long           s_blocksize;
        loff_t                  s_maxbytes;     / Max file size /
        struct file_system_type *s_type;
!!!;
        struct inode *(*alloc_inode)(struct super_block *sb);
        void (*destroy_inode)(struct inode *);

        void (*dirty_inode) (struct inode *);
        int (*write_inode) (struct inode *, struct writeback_control *wbc);
        void (*drop_inode) (struct inode *);
        void (*delete_inode) (struct inode *);

        void (*put_super) (struct super_block *);
        void (*write_super) (struct super_block *);
!!!;
?        int (*sync_fs)(struct super_block *sb, int wait);
-        int (*freeze_fs) (struct super_block *);
-        int (*unfreeze_fs) (struct super_block *);
*        int (*statfs) (struct dentry *, struct kstatfs *);
*        int (*remount_fs) (struct super_block *, int *, char *);
-        void (*clear_inode) (struct inode *);
!!! not for now: *        void (*umount_begin) (struct super_block *);

!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*        int (*show_options)(struct seq_file *, struct vfsmount *);
*        int (*show_stats)(struct seq_file *, struct vfsmount *);
#ifdef CONFIG_QUOTA
*        ssize_t (*quota_read)(struct super_block *, int, char *, size_t, loff_t);
*        ssize_t (*quota_write)(struct super_block *, int, const char *, size_t, loff_t);
#endif
?        int (*bdev_try_to_free_page)(struct super_block*, struct page*, gfp_t);

!!!;
        const struct super_operations   *s_op;
        const struct dquot_operations   *dq_op;
        const struct quotactl_ops       *s_qcop;
!!! NO exportfs (for now):        const struct export_operations *s_export_op;
!!! NO FS_REQUIRES_DEV, FS_HAS_SUBTYPE, etc right now:        unsigned long           s_flags;
*        unsigned long           s_magic;
*        struct dentry           *s_root;
???        struct rw_semaphore     s_umount;
        struct mutex            s_lock;
        int                     s_count;
        int                     s_need_sync;
        atomic_t                s_active;
#ifdef CONFIG_SECURITY
NOT EVEN USED BY LINUX FILE SYSTEMS:        void                    *s_security;
#endif
        struct xattr_handler    **s_xattr;

*        struct list_head        s_inodes;       / all inodes /
        struct hlist_head       s_anon;         / anonymous dentries for (nfs) exporting /
*        struct list_head        s_files;
        / s_dentry_lru and s_nr_dentry_unused are protected by dcache_lock /
only ceph uses dentry_LRU:        struct list_head        s_dentry_lru;   / unused dentry lru /
        int                     s_nr_dentry_unused;     / # of dentry on lru /

*        struct block_driver     *s_bdev;
?        struct backing_dev_info *s_bdi;
no idea what mtd is, but we don't want it:        struct mtd_info         *s_mtd;
        struct list_head        s_instances;

!!!
*/
}
