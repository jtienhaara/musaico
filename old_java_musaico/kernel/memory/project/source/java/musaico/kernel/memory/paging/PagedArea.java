package musaico.kernel.memory.paging;


import java.io.Serializable;


import musaico.buffer.Buffer;

import musaico.field.Field;

import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.kernel.memory.MemoryException;

import musaico.region.Position;
import musaico.region.Region;

import musaico.security.Credentials;
import musaico.security.SecurityException;


/**
 * <p>
 * A PagedArea encapsulates the different layers of
 * storage which make up a virtual buffer, as well as the
 * swapping between these layers.
 * </p>
 *
 * <p>
 * For example, Pages in memory may be swapped
 * out to a Musaico object system when memory
 * is scarce.  Or there may be multiple levels of memory,
 * such as "buffer memory" where every object is represented
 * as sequences of Fields, and "object cache" where every
 * object is accessed in a platform-dependent manner, and
 * "fast", "slow", "slower", "slowest" Driver-backed storage.  In
 * this case, swapping may happen when an object is promoted
 * from buffer memory to the object cache, and again when
 * it is released from the object cache back into buffer
 * memory, and so on.
 * </p>
 *
 * <pre>
 *     +--------+                   +--------+
 *     | Object |                   | Buffer |                   +-------+
 *     | cache  | <- in swap out -> | memory | <- in swap out -> | ONode |
 *     +--------+                   +--------+                   +-------+
 * </pre>
 *
 * <p>
 * This structure is just an example.  The number of swappable
 * layers is limitless.
 * </p>
 *
 * <pre>
 *   0..n VirtualBuffers
 *        1 |
 *          |
 *        1 V
 *       Segments
 *     1..n |
 *          |
 *        1 V     0..n   1
 *     PagedArea ---------> SwapSystem
 *          |               1 | (SwapStates, Swappers, the Space
 *        1 V                 |  describing the Positions, Regions
 *      PageTable             |  and Sizes of a particular
 *          |            1..n |  SwapState, and so on).
 *     1..n V   0..n   1      V
 *        Page ----------> SwapState
 *         /_\             + pageSize etc
 *     _____|____________
 *     |         |       |
 *     Object  Buffer  ONode
 *     cache    page   page   ...and so on Pages...
 *      page     |       |
 *       |       |       |
 *     Object  Buffer  ONode ---- persistent data
 *               |
 *          0..n |
 *             Field
 * </pre>
 *
 *
 * <p>
 * In Java, every PagedArea must be Serializable, in order to
 * play nicely over RMI.  Note, however,
 * that a Page may contain non-Serializable data.
 * (For example, a buffer Page with non-Serializable Fields.)
 * Therefore great care must be taken when passing Pages
 * back and forth across RMI.
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
 * Copyright (c) 2009, 2011 Johann Tienhaara
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
public interface PagedArea
    extends Serializable
{
    /**
     * <p>
     * Releases this entire paged area, closing connections
     * to drivers, freeing memory, and so on.
     * </p>
     *
     * <p>
     * This paged area cannot be re-used once it has been freed.
     * </p>
     *
     * @throws MemoryException If the paged area cannot be
     *                         cleanly freed (for example because
     *                         of an underlying memory or backing
     *                         driver error, and so on).
     */
    public abstract void free ()
        throws MemoryException;


    /**
     * <p>
     * Returns this PagedArea's identifier, unique within the
     * kernel.
     * </p>
     *
     * @return This PagedArea's unique identifier.  Never null.
     */
    public abstract PagedAreaIdentifier id ();


    /**
     * <p>
     * Handles a page fault for a PagedArea.
     * </p>
     *
     * <p>
     * The actual Page which caused the fault must be stored in
     * the PageFault by the end of the invocation <i>unless</i> the
     * method throws an exception, in which case the error
     * which caused the exception (out of memory, no page, and so on)
     * must be stored in the fault before the exception is thrown.
     * </p>
     *
     * <p>
     * If the Linux address_space analogy of PagedArea was
     * 100% accurate (and the vm_area / vm_operations analogy
     * of Segment) then the <code> pageFault () </code> method
     * would be in Segment rather than here.  However in Musaico
     * the paging operations have been pushed down as much as
     * possible, to minimize the spaghetti that external
     * components require (rather than sending noodles down
     * into the paging code, the Segment tries to do all the paging
     * work itself, including calling this method, and external
     * components rely on the facilities exposed by Segment).
     * </p>
     *
     * @param credentials Who is executing the operation on
     *                    this PagedArea, such as user
     *                    credentials or module credentials
     *                    and so on.  Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     *
     * @throws MemoryException If anything goes horribly wrong.
     *
     * @throws SecurityException If the specified credentials
     *                           may not perform the page fault.
     */
    public abstract void pageFault (
                                    Credentials credentials,
                                    PageFault fault
                                    )
        throws I18nIllegalArgumentException,
               MemoryException,
               SecurityException;


    /**
     * <p>
     * Returns the PageTable for this PagedArea.
     * </p>
     *
     * <p>
     * Before doing anything with the PageTable, lock on
     * this paged area's <code> lock () </code>, to
     * ensure atomic transactions with the paged area.
     * </p>
     *
     * @return This PagedArea's PageTable.  Never null.
     */
    public abstract PageTable pageTable ();


    /**
     * <p>
     * Reads Fields in from this PagedArea into the specified
     * Buffer and the specified Region of the Buffer.
     * </p>
     *
     * <p>
     * This operation may induce swapping, so the caller should
     * expect to block.
     * </p>
     *
     * @param credentials The credentials to use to read from
     *                    this paged area.  Typically the
     *                    owner of a Segment backed by this
     *                    PagedArea.  Must not be null.
     *
     * @param memory_position The starting position of this PagedArea
     *                        to begin reading Fields from.
     *                        Must not be null.
     *
     * @param read_into_buffer The Buffer into which Fields will be
     *                         copied from this PagedArea.
     *                         Must not be null.
     *
     * @param read_into_region The Region of the Buffer into which
     *                         Fields will be copied from this PagedArea.
     *                         Must not be null.
     *
     * @return The actual Region of the specified Buffer into which
     *         Fields were read.  Can be empty.  Never null.
     *
     * @throws MemoryException If anything goes wrong, such as a
     *                         SwapException.
     */
    public abstract Region read (
                                 Credentials credentials,
                                 Position memory_position,
                                 Buffer read_into_buffer,
                                 Region read_into_region
                                 )
        throws MemoryException;


    /**
     * <p>
     * Reads a single Field in from this PagedArea.
     * </p>
     *
     * <p>
     * This operation may induce swapping, so the caller should
     * expect to block.
     * </p>
     *
     * @param credentials The credentials to use to read from
     *                    this paged area.  Typically the
     *                    owner of a Segment backed by this
     *                    PagedArea.  Must not be null.
     *
     * @param memory_position The position of this PagedArea
     *                        from which the Field will be read.
     *                        Must not be null.
     *
     * @return The Field at the specified position, or Field.NULL
     *         if the specified position of memory is empty.
     *         Never null.
     *
     * @throws MemoryException If anything goes wrong, such as a
     *                         SwapException.
     */
    public abstract Field readField (
                                     Credentials credentials,
                                     Position memory_position
                                     )
        throws MemoryException;


    /**
     * <p>
     * Specifies a new Region of Fields, resizing the PageTable
     * to meet the region's size requirements.
     * </p>
     *
     * @param credentials The credentials to use to resize
     *                    this paged area.  Typically the
     *                    owner of a Segment backed by this
     *                    PagedArea.  Must not be null.
     *
     * @param region The new Region of Fields to cover.
     *               Must not be null.
     *
     * @return The previous Region of Fields covered by this PagedArea.
     *         Possibly empty.  Never null.
     *
     * @throws SwapException If anything goes wrong during the resize
     *                       (such as a PageTableException).
     */
    public abstract Region resize (
                                   Credentials credentials,
                                   Region region
                                   )
        throws SwapException;


    /**
     * <p>
     * Returns the configuration data for all swap states, such
     * as the specific block driver to be used for a
     * swapped-out-to-block-driver swap state and so on.
     * </p>
     *
     * @return This paged area's swap states configuration.
     *         Never null.
     */
    public abstract SwapConfiguration swapConfiguration ();


    /**
     * <p>
     * Returns the SwapSystem used to swap Pages in and out
     * inside this PagedArea.
     * </p>
     *
     * <p>
     * This SwapSystem may be shared by other PagedAreas.
     * </p>
     *
     * @return This PagedArea's SwapSystem.  Never null.
     */
    public SwapSystem swapSystem ();


    /**
     * <p>
     * Synchronizes the specified Pages.
     * </p>
     *
     * <p>
     * If the in_page is dirty, then it will be written
     * out, overwriting the out_page.
     * </p>
     *
     * <p>
     * Otherwise, if the in_page is clean, then the out_page
     * will be read in, overwriting the in_page.
     * </p>
     *
     * <p>
     * The implementation must update the KernelPaging
     * manager provided by the kernel, calling clean () and
     * so on.
     * </p>
     *
     * @param credentials Who is requesting the synchronize operation
     *                    (typically the owner of a segment backed
     *                    by this paged area).  Must not be null.
     *
     * @param in_page The swapped-in page to synchronize.  Must not be null.
     *                Must be an instance of a "swapped in" Page class
     *                for this paged area's Swapper.
     *
     * @param out_page The swapped-out page to synchronize.  Must not be null.
     *                 Must be an instance of a "swapped out" Page class
     *                 for this paged area's Swapper.
     *
     * @throws SwapException If the Pages cannot be synchronized
     *                       for any reason (invalid pages, out
     *                       of memory, driver I/O error, and so on).
     */
    public abstract void synchronize (
                                      Credentials credentials,
                                      Page in_page,
                                      Page out_page
                                      )
        throws SwapException;


    /**
     * <p>
     * Writes Fields out to this PagedArea from the specified
     * Buffer and the specified Region of the Buffer.
     * </p>
     *
     * <p>
     * This operation may induce swapping, so the caller should
     * expect to block.
     * </p>
     *
     * @param credentials The credentials to use to write to
     *                    this paged area.  Typically the
     *                    owner of a Segment backed by this
     *                    PagedArea.  Must not be null.
     *
     * @param memory_position The starting position of this PagedArea
     *                        to begin writing Fields to.
     *                        Must not be null.
     *
     * @param write_from_buffer The Buffer from which Fields will be
     *                          copied into this PagedArea.
     *                          Must not be null.
     *
     * @param write_from_region The Region of the Buffer from which
     *                          Fields will be copied into this PagedArea.
     *                          Must not be null.
     *
     * @return The actual Region of the specified Buffer from which
     *         Fields were written.  Can be empty.  Never null.
     *
     * @throws MemoryException If anything goes wrong, such as a
     *                         SwapException.
     */
    public abstract Region write (
                                  Credentials credentials,
                                  Position memory_position,
                                  Buffer write_from_buffer,
                                  Region write_from_region
                                  )
        throws MemoryException;


    /**
     * <p>
     * Writes a single Field out to this PagedArea.
     * </p>
     *
     * <p>
     * This operation may induce swapping, so the caller should
     * expect to block.
     * </p>
     *
     * @param credentials The credentials to use to write to
     *                    this paged area.  Typically the
     *                    owner of a Segment backed by this
     *                    PagedArea.  Must not be null.
     *
     * @param memory_position The position of this PagedArea
     *                        to which the Field will be written.
     *                        Must not be null.
     *
     * @param write_field The Field to write to this PagedArea.
     *                    Must not be null.
     *
     * @throws MemoryException If anything goes wrong, such as a
     *                         SwapException.
     */
    public abstract void writeField (
                                     Credentials credentials,
                                     Position memory_position,
                                     Field write_field
                                     )
        throws MemoryException;


    /**
       !!!
        int (*writepage)(struct page *page, struct writeback_control *wbc);
        int (*readpage)(struct file *, struct page *);
        void (*sync_page)(struct page *);

        * Write back some dirty pages from this mapping. *
        int (*writepages)(struct address_space *, struct writeback_control *);

        * Set a page dirty.  Return true if this dirtied it *
        int (*set_page_dirty)(struct page *page);

        int (*readpages)(struct file *filp, struct address_space *mapping,
                        struct list_head *pages, unsigned nr_pages);

        int (*write_begin)(struct file *, struct address_space *mapping,
                                loff_t pos, unsigned len, unsigned flags,
                                struct page **pagep, void **fsdata);
        int (*write_end)(struct file *, struct address_space *mapping,
                                loff_t pos, unsigned len, unsigned copied,
                                struct page *page, void *fsdata);

        * Unfortunately this kludge is needed for FIBMAP. Don't use it *
        sector_t (*bmap)(struct address_space *, sector_t);
        void (*invalidatepage) (struct page *, unsigned long);
        int (*releasepage) (struct page *, gfp_t);
        ssize_t (*direct_IO)(int, struct kiocb *, const struct iovec *iov,
                        loff_t offset, unsigned long nr_segs);
        int (*get_xip_mem)(struct address_space *, pgoff_t, int,
                                                void **, unsigned long *);
        * migrate the contents of a page to the specified target *
        int (*migratepage) (struct address_space *,
                        struct page *, struct page *);
        int (*launder_page) (struct page *);
        int (*is_partially_uptodate) (struct page *, read_descriptor_t *,
                                        unsigned long);
        int (*error_remove_page)(struct address_space *, struct page *);
    */
}
