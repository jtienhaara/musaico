package musaico.kernel.objectsystem;

import java.io.Serializable;


import musaico.io.Progress;
import musaico.io.Reference;

import musaico.region.Position;

import musaico.security.Credentials;
import musaico.security.Permissions;


/**
 * <p>
 * Represents an open cursor which points to an OEntry and maintains
 * seek position and so on within streamed or block data.
 * </p>
 *
 * <p>
 * A Cursor is akin to a FILE structure in a UNIX-like operating system.
 * Just as there can be many FILEs open, pointing to different byte
 * positions in a given i-node, there can be many Cursors open, pointing
 * to different Field positions in a given ONode.
 * </p>
 *
 * <p>
 * Generally a single Cursor should only be accessed from a single
 * thread.  Cursors shall be thread-safe when modifying their
 * internal states; however it usually makes no sense to share
 * a single Cursor position within data between two or more threads.
 * </p>
 *
 *
 * <p>
 * Because object systems can conceivably be distributed, every Cursor
 * must be Serializable in order to play nicely over RMI.  However, more
 * so even than with the rest of the object system, designers must
 * be very very careful to prevent network node re-location of a Cursor,
 * unless the entire object system to which the Cursor points is
 * relocated at the same time.  In general, a Cursor should never
 * be relocated from one network node to another.  And a Cursor
 * should absolutely never be sent from one network node to another
 * under any other circumstances.
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
 * Copyright (c) 2009, 2010, 2011 Johann Tienhaara
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
public interface Cursor
    extends Serializable
{
    /**
     * <p>
     * Returns the OEntry to which this Cursor points.
     * </p>
     *
     * <p>
     * Note that the OEntry interface has not yet been defined,
     * so the Cursor interface must declare the return type
     * of this method as a generic Reference.  However each and
     * every implementation of Cursor should declare OEntry
     * as its return type.
     * </p>
     *
     * <p>
     * To retrieve the path to this Cursor, invoke
     * <code>( (OEntry) cursor.entry () ).path ()</code>.
     * </p>
     *
     * <p>
     * To retrieve a Reference to the ONode for this Cursor, invoke
     * <code>( (OEntry) cursor.entry () ).onode ()</code>.
     * </p>
     *
     * @see musaico.kernel.objectsystem.onode.OEntry
     *
     * @return The OEntry for this Cursor.  Always an OEntry.
     *         Never null.
     */
    public abstract Reference entry ();


    /**
     * <p>
     * Returns the identifier for this Curosr, unique among all
     * other Cursor identifiers in the kernel.
     * </p>
     *
     * @return This Cursor's identifier.  Never null.
     */
    public abstract CursorIdentifier id ();


    /**
     * <p>
     * Returns true if this cursor has been opened, false if it
     * has never been opened or if it has been opened but then closed.
     * </p>
     *
     * @return True if this cursor is open, false if not.
     */
    public abstract boolean isOpen ();


    /**
     * <p>
     * True if this Cursor is open in read mode,
     * </p>
     *
     * @see musaico.kernel.objectsystem.Cursor#permissions()
     *
     * @return True if this Cursor is open in read mode, false if
     *         it is not yet open or if it does not allow reading.
     */
    public abstract boolean isReader ();


    /**
     * <p>
     * True if this Cursor is open in write mode,
     * </p>
     *
     * @see musaico.kernel.objectsystem.Cursor#permissions()
     *
     * @return True if this Cursor is open in write mode, false if
     *         it is not yet open or if it does not allow writing.
     */
    public abstract boolean isWriter ();


    /**
     * <p>
     * The owner of this Cursor.
     * </p>
     *
     * <p>
     * A Cursor is inherently insecure because it exposes the
     * module credentials or user credentials to various Drivers
     * and object systems from various modules in the kernel.
     * Therefore it is a good idea to create a custom temporary
     * Credentials for each Cursor you create, and delete the
     * credentials when the Cursor is freed.
     * </p>
     *
     * @return This Cursor's ownership.  Never null.
     */
    public abstract Credentials owner ();


    /**
     * <p>
     * Returns the access permissions ("mode") with which this cursor
     * was created.
     * </p>
     *
     * <p>
     * Can also include advisory flags such as
     * <code> RecordFlag.RANDOM_ACCESS </code> or
     * <code> RecordFlag.SEQUENTIAL_ACCESS </code> and so on.
     * </p>
     *
     * <p>
     * The Permissions returned should never contain the
     * actual KernelCredentials of the owner of the Cursor,
     * it should always be a reference.
     * </p>
     *
     * @see musaico.kernel.objectsystem.Cursor#owner();
     *
     * @return The Permissions with which this Cursor was
     *         created, such as a
     *         <code> Permissions&lt;RecordPermission&gt; </code>
     *         with <code> RecordPermission.WRITE </code> and
     *         <code> RecordPermission.APPEND </code>.  Note that
     *         these Permissions are not necessarily granted by
     *         the kernel, they are requested permissions.
     *         Never null.
     */
    public abstract Permissions<RecordFlag> permissions ();


    /**
     * <p>
     * Returns the current position of the cursor within the data
     * structure of fields pointed to.
     * </p>
     *
     * <p>
     * The position is driver-dependent, and may be a numeric
     * value, a String value, or some combination.
     * </p>
     *
     * <p>
     * Each Cursor should initially point to a StartPosition.
     * </p>
     *
     * @return The current offset into this Cursor.  Never null.
     */
    public abstract Position position ();


    /**
     * <p>
     * Sets the current position of the cursor.
     * </p>
     *
     * <p>
     * The position is driver-dependent, and may be a numeric
     * value, a String value, or some combination.
     * </p>
     *
     * <p>
     * Typically this method is called by the Driver to replace
     * a generic position (such as a StartPosition or an
     * OffsetFromPosition) with a driver-specific Position.
     * </p>
     *
     * @param position The new offset for this Cursor.  Never null.
     *
     * @return The old offset of this Cursor.  Never null.
     */
    public abstract Position position (
                                       Position position
                                       );


    /**
     * <p>
     * The progress meter for the current operation being executed
     * this Cursor (if any).
     * </p>
     *
     * @return The progress meter for this Cursor's currently
     *         executing operation.  Never null.
     */
    public abstract Progress progress ();


    /**
     * <p>
     * The record (such as a flat record, hierarchical object, symbolic
     * link, driver, and so on) pointed to by this cursor.
     * </p>
     *
     * <p>
     * For example, opening /foo/bar might open a flat
     * record or an object, whereas opening /my/link might
     * open a symbolic link, and opening /dev/data might open
     * a cursor to a driver.
     * </p>
     *
     * @see musaico.kernel.objectsystem.Record
     *
     * @see musaico.kernel.objectsystem.records.FlatRecord
     * @see musaico.kernel.objectsystem.records.ObjectRecord
     * @see musaico.kernel.objectsystem.records.Relation
     *
     * @see musaico.kernel.objectsystems.records.RawDriverRecord
     */
    public abstract Record record ();
}
