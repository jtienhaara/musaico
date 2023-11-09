package musaico.kernel.memory;


import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Identifier;

import musaico.region.Region;
import musaico.region.Space;

import musaico.security.Credentials;


/**
 * <p>
 * A region of memory, the Segment provides controls over
 * how pages of memory are accessed (security, page fault
 * mechanism, and so on).
 * </p>
 *
 * <p>
 * A Segment is heavily dependent on its corresponding
 * PagedArea to map pages from slow / persistent
 * memory to fast memory to even faster caches, and so
 * on.  The PagedArea handles all that fun stuff,
 * while the Segment controls access to it via security
 * mechanisms and a simple-to-use request mechanism.
 * </p>
 *
 * @see musaico.kernel.memory.paging.PagedArea
 *
 * <p>
 * Roughly equivalent to a combination of <code> vm_area </code>
 * and <code> vm_operations </code> in Linux.
 * </p>
 *
 * <p>
 * Note that currently there is nothing akin to NUMA policies
 * in Linux (set_policy, get_policy, migrate, and so on), since
 * there is nothing akin to nodes.  This could conceivably change
 * in future, but for now there is no obvious need.
 * </p>
 *
 * <p>
 * In Java, every Segment must be Serializable in order to play
 * nicely over RMI.  Note, however,
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
public interface Segment
    extends Serializable
{
    /**
     * <p>
     * Closes a Segment.
     * </p>
     *
     * @param credentials Who is executing the operation on
     *                    this Segment, such as user
     *                    credentials or module credentials
     *                    and so on.  Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     *
     * @throws SegmentOperationException If anything goes horribly wrong.
     *
     * @throws SegmentSecurityException If the specified credentials
     *                                  may not perform this operation
     *                                  on this Segment.
     */
    public abstract void close (
                                Credentials credentials
                                )
        throws I18nIllegalArgumentException,
               SegmentOperationException;


    /**
     * <p>
     * Releases this segment, detaching it from the paged area, the
     * memory requests queue, and so on.
     * </p>
     *
     * <p>
     * Typically the result of a user freeing a virtual buffer
     * and so on.
     * </p>
     */
    public abstract void free ();


    /**
     * <p>
     * Pulls the top MemoryRequest off the queue and executes it.
     * </p>
     *
     * <p>
     * Typically a kernel Task calls this whenever the Segment has
     * requests to be fulfilled.
     * </p>
     *
     * <p>
     * This method should not be invoked again until the first
     * has been completed (this invocation returns).
     * Therefore it is a good idea to only ever invoke this
     * method from a single thread.
     * </p>
     */
    public abstract void handleOneRequest ();


    /**
     * <p>
     * Returns the unique identifier for this Segment.
     * </p>
     *
     * <p>
     * The identifier must be unique throughout all segments
     * in this Kernel.
     * </p>
     *
     * @return This Segment's unique identifier.  Never null.
     */
    public abstract SegmentIdentifier id ();


    /**
     * <p>
     * Returns true if this Segment has one or more MemoryRequests
     * to fulfill.
     * </p>
     *
     * @return True if this Segment has MemoryRequests to fulfill,
     *         false if its queue is empty.
     */
    public abstract boolean isRequestWaiting ();


    /**
     * <p>
     * Opens this Segment.
     * </p>
     *
     * @param credentials Who is executing the operation on
     *                    this Segment, such as user
     *                    credentials or module credentials
     *                    and so on.  Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     *
     * @throws SegmentOperationException If anything goes horribly wrong.
     *
     * @throws SegmentSecurityException If the specified credentials
     *                                  may not perform this operation
     *                                  on this Segment.
     */
    public abstract void open (
                               Credentials credentials
                               )
        throws I18nIllegalArgumentException,
               SegmentOperationException;


    /**
     * <p>
     * Returns a reference to this Segment's paged area
     * (in-memory Pages, swapped out Pages, and so on).
     * </p>
     *
     * <p>
     * Except in special circumstances there should be no need
     * to retrieve the PagedArea from a Segment.  Send MemoryRequests
     * to the Segment via <code> request () </code> instead, and
     * let the Segment call methods on the PagedArea on your
     * behalf.
     * </p>
     *
     * @see musaico.kernel.memory.Segment#request(musaico.kernel.memory.MemoryRequest)
     *
     * <p>
     * The PagedArea contains the page lookup for this
     * Segment.  So to retrieve the specific Page for field position
     * <code> field_position </code>:
     * </p>
     *
     * <pre>
     *     Kernel kernel = ...;
     *     Credentials credentials = ...;
     *     Segment segment = ...;
     *     Position field_position = ...;
     *     PagedAreaIdentifier paged_area_id = (PagedAreaIdentifier)
     *         segment.pagedAreaRef ();
     *     PagedArea paged_area = kernel.objects ().get ( paged_area_id );
     *     Page parent_page =
     *                paged_area.pageTable ().get ( field_position );
     * </pre>
     *
     * @return The identifier of the PagedArea which controls
     *         page swapping for this Segment.  Always a
     *         <code> PagedAreaIdentifier </code>, even though this
     *         interface's method signature cannot depend on the
     *         PagedAreaIdentifier class.  Never null.
     */
    public abstract Identifier pagedAreaRef ();


    /**
     * <p>
     * Changes the PagedArea for this Segment.
     * </p>
     *
     * <p>
     * This can happen, for example, when a segment of memory
     * is mmap'ed (or un-mmap'ed).  The segment might change
     * from using a standard swap space backing to using an
     * object system record as its backing.
     * </p>
     *
     * <p>
     * Callers should release the entire PagedArea if it is
     * not going to be re-used.
     * </p>
     *
     * <p>
     * Implementors must be very careful to make this
     * method (and all other methods which could be
     * affected by it, such as pageFault()) thread-safe.
     * </p>
     *
     * @param credentials Who is executing the operation on
     *                    this Segment, such as user
     *                    credentials or module credentials
     *                    and so on.  Must not be null.
     *
     * @param paged_area_id The identifier of the new paged area
     *                      to back this segment.  Must be a
     *                      <code> PagedAreaIdentifier </code>, even
     *                      though this interface cannot depend on
     *                      that class.  Must not be null.
     *
     * @return The previous PagedAreaIdentifier for this Segment.
     *         Always a PagedAreaIdentifier.  Never null.
     *
     * @throws I18nIllegalArgumentException If the parameters are invalid.
     *
     * @throws SegmentOperationException If the PagedArea cannot
     *                                   be switched (for example,
     *                                   if the caller only has
     *                                   read-only access to this Segment).
     *
     * @throws SegmentSecurityException If the specified credentials
     *                                  may not perform this operation
     *                                  on this Segment.
     */
    public abstract Identifier pagedArea (
                                          Credentials credentials,
                                          Identifier paged_area_id
                                          )
        throws I18nIllegalArgumentException,
               SegmentOperationException;


    /**
     * <p>
     * Returns the size and layout of this Segment, in terms
     * of Fields in the PagedArea.
     * </p>
     *
     * @return This Segment's Region, in terms of the start position,
     *         end position, size and layout of Fields in the segment's
     *         PagedArea.  Never null.
     */
    public abstract Region region ();


    /**
     * <p>
     * Queues up and handles the specified request.
     * </p>
     *
     * <p>
     * The request is handled asynchronously, in another thread.
     * To wait for the result, call <code> MemoryRequest.waitFor () </code>.
     * </p>
     *
     * @param request The request to this Segment.  Must not be null.
     *
     * @return The same request.  Never null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public abstract <RESPONSE extends Serializable>
        MemoryRequest<RESPONSE> request (
                                         MemoryRequest<RESPONSE> request
                                         )
        throws I18nIllegalArgumentException;


    /**
     * <p>
     * Returns the security framework for this segment.
     * </p>
     *
     * <p>
     * This framework can be used to request permissions to
     * perform segment operations.  For example, if a user
     * process which owns this segment tries to write to a
     * copy-on-write page of memory, permission might be
     * granted; on the other hand, trying to write to a read-only
     * page would generate a security violation.
     * </p>
     *
     * @return This segment's securty framework.  Never null.
     */
    public abstract SegmentSecurity security ();


    /**
     * <p>
     * Returns the Space describing the positions, size and
     * regions of Fields in this Segment.
     * </p>
     *
     * @return This Segment's Space, in terms of the positions,
     *         size and region of Fields in the segment's
     *         PagedArea.  For example, an ArraySpace indexed
     *         by integer offset, or a TimeSpace indexed by
     *         times, and so on.  Never null.
     */
    public abstract Space space ();
}
