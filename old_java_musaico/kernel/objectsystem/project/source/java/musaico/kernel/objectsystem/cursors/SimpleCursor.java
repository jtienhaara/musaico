package musaico.kernel.objectsystem.cursors;

import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Progress;

import musaico.io.progresses.SimpleProgress;

import musaico.kernel.objectsystem.Cursor;
import musaico.kernel.objectsystem.CursorIdentifier;
import musaico.kernel.objectsystem.Record;
import musaico.kernel.objectsystem.RecordFlag;
import musaico.kernel.objectsystem.RecordOperationException;
import musaico.kernel.objectsystem.RecordPermission;

import musaico.kernel.objectsystem.onode.OEntry;

import musaico.region.Position;

import musaico.region.array.ArraySpace;

import musaico.security.Credentials;
import musaico.security.Permissions;


/**
 * <p>
 * A simple open cursor which points to an OEntry and maintains
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
public class SimpleCursor
    implements Cursor, Serializable
{
    /** Lock all critical sections on this token: */
    private final Serializable lock = new String ();

    /** The unique id of this cursor. */
    private final CursorIdentifier id;

    /** The OEntry which this cursor points to.  Essentially
     *  the path within all object systems (/foo/bar and so on). */
    private final OEntry entry;

    /** The permissions with which this cursor was created. */
    private final Permissions<RecordFlag> permissions;

    /** The current position of this cursor.  Can change. */
    private Position position;

    /** Progress meter for the most recent operation.
     *  Never changes the reference, but of course the
     *  content of the progress meter does change
     *  over time. */
    private final Progress progress = new SimpleProgress ();

    /** The Record (a FlatRecord, an ObjectRecord, a RawDriverRecord,
     *  a Relation, and so on) to which this Cursor points. */
    private final CursorRecordWrapper record;


    /**
     * <p>
     * Creates a new SimpleCursor with the specified
     * permissions to operate on the specified OEntry and Record.
     * </p>
     *
     * <p>
     * You should always create a temporay duplicate of your
     * credentials, use the duplicate to create the permissions
     * for this cursor, and then delete the duplicate from the
     * kernel once you are done using this cursor.
     * </p>
     *
     * @param entry The OEntry to which this cursor points,
     *              such as /foo/bar or /dev/xyz.  Must not be null.
     *
     * @param permissions The permissions with which this cursor
     *                    is being created, such as RecordPermission.READ
     *                    and RecordPermission.CREATE.  May also
     *                    include advisory flags, such as
     *                    RecordFlag.RANDOM_ACCESS.  Must not be null.
     *                    Should contain a temporary Credentials
     *                    which should be deleted after the caller
     *                    has finished using this cursor.
     *
     * @param record The record to open this cursor on, such as
     *               an ObjectRecord or a FlatRecord or a RawDriverRecord
     *               and so on.  Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (see above).
     */
    public SimpleCursor (
                         OEntry entry,
                         Permissions<RecordFlag> permissions,
                         Record record
                         )
        throws I18nIllegalArgumentException
    {
        if ( entry == null
             || permissions == null
             || record == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a SimpleCursor at entry [%oentry%] with permissions [%permissions%] pointing to record [%record%]",
                                                     "oentry", entry,
                                                     "permissions", permissions,
                                                     "record", record );
        }

        this.id = new CursorIdentifier ();
        this.entry = entry;
        this.permissions = permissions;
        this.record = new CursorRecordWrapper ( record );

        // We haven't even opened a record yet.
        this.position = ArraySpace.STANDARD.outOfBounds ();

        // !!! Check permissions on the entry or its parent.
        // !!! Create the record etc as necessary.
    }


    /**
     * @see musaico.kernel.objectsystem.Cursor#entry()
     */
    public OEntry entry ()
    {
        return this.entry;
    }


    /**
     * @see musaico.kernel.objectsystem.CursorIdentifier#id()
     */
    public CursorIdentifier id ()
    {
        return this.id;
    }


    /**
     * @see musaico.kernel.objectsystem.CursorIdentifier#isOpen()
     */
    public boolean isOpen ()
    {
        return this.record.isOpen ();
    }


    /**
     * @see musaico.kernel.objectsystem.CursorIdentifier#isReader()
     */
    public boolean isReader ()
    {
        Permissions<RecordFlag> mode = this.permissions ();
        if ( mode.isAllowed ( RecordPermission.READ ) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * @see musaico.kernel.objectsystem.CursorIdentifier#Writer()
     */
    public boolean isWriter ()
    {
        Permissions<RecordFlag> mode = this.permissions ();
        if ( mode.isAllowed ( RecordPermission.WRITE )
             ||  mode.isAllowed ( RecordPermission.APPEND )
             ||  mode.isAllowed ( RecordPermission.CREATE ) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * @see musaico.kernel.objectsystem.Cursor#owner()
     */
    public Credentials owner ()
    {
        return this.permissions.credentials ();
    }


    /**
     * @see musaico.kernel.objectsystem.Cursor#permissions()
     */
    public Permissions<RecordFlag> permissions ()
    {
        return this.permissions;
    }


    /**
     * @see musaico.kernel.objectsystem.Cursor#position()
     */
    public Position position ()
    {
        return this.position;
    }


    /**
     * @see musaico.kernel.objectsystem.Cursor#position(musaico.io.Position)
     */
    public Position position (
                              Position position
                              )
    {
        final Position old_position;
        synchronized ( this.lock )
        {
            old_position = this.position;
            this.position = position;
        }

        return old_position;
    }


    /**
     * @see musaico.kernel.objectsystem.Cursor#progress()
     */
    public Progress progress ()
    {
	return this.progress;
    }


    /**
     * @see musaico.kernel.objectsystem.Cursor#record()
     */
    public Record record ()
    {
        return this.record;
    }
}
