package musaico.kernel_modules.drivers.byte_stream;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Filter;
import musaico.io.FilterState;
import musaico.io.Path;
import musaico.io.Progress;
import musaico.io.Reference;
import musaico.io.ReferenceCount;
import musaico.io.SimpleReferenceCount;

import musaico.buffer.Buffer;
import musaico.buffer.BufferException;
import musaico.buffer.BufferTools;

import musaico.field.Field;
import musaico.field.FieldTypingEnvironment;

import musaico.io.markers.RecordEnd;
import musaico.io.markers.RecordStart;

import musaico.io.references.SimpleSoftReference;

import musaico.kernel.driver.BlockDriver;
import musaico.kernel.driver.Driver;
import musaico.kernel.driver.DriverIdentifier;
import musaico.kernel.driver.DriverException;
import musaico.kernel.driver.EjectableDriver;
import musaico.kernel.driver.SchedulableDriver;
import musaico.kernel.driver.SpecialDriver;
import musaico.kernel.driver.StreamDriver;
import musaico.kernel.driver.SuspendableDriver;
import musaico.kernel.driver.TransactionalDriver;
import musaico.kernel.driver.TransactionException;

import musaico.kernel.memory.Segment;

import musaico.kernel.module.Module;

import musaico.kernel.objectsystem.Cursor;
import musaico.kernel.objectsystem.PollRequest;
import musaico.kernel.objectsystem.PollResponse;
import musaico.kernel.objectsystem.RecordFlag;
import musaico.kernel.objectsystem.RecordOperationException;
import musaico.kernel.objectsystem.RecordPermission;
import musaico.kernel.objectsystem.RecordPermissions;

import musaico.kernel.objectsystem.cursors.SimpleCursor;

import musaico.kernel.task.Scheduler;

import musaico.kernel.types.SimpleKernelTypingEnvironment;

import musaico.region.Position;
import musaico.region.Region;
import musaico.region.Size;
import musaico.region.Space;
import musaico.region.SparseRegionBuilder;

import musaico.region.array.ArrayPosition;
import musaico.region.array.ArraySpace;

import musaico.security.Credentials;
import musaico.security.Permissions;
import musaico.security.Security;


/**
 * <p>
 * An abstract driver which provides access to raw InputStreams
 * and OutputStreams to the Driver one level up the stack.
 * </p>
 *
 * <p>
 * This driver provides an interface to the byte-level Java world of I/O
 * from the higher level I/O of Musaico.  Musaico I/O involves Fields
 * rather than bytes, and can be built on byte-level I/O Drivers
 * such as files and HTTP connections, or other higher level protocol
 * libraries, such as JDBC or JMX, or on entirely logical constructs,
 * such as a math or rules engine.  Since the user of Musaico must be
 * able to configure stacks of Drivers which encode, decode and
 * filter, this ByteStreamDriver provides the basis for adding byte-level
 * Drivers to the Driver stack (file access, HTTP access, and so on).
 * </p>
 *
 * <p>
 * Implementors of the ByteStreamDriver must provide implementations
 * of <code> openInputStream () </code> and
 * <code> openOutputStream () </code>, and their corresponding
 * close methods.  The ByteStreamDriver invokes either or both
 * stream open methods during an open () call, checking permissions
 * and only invoking the appropriate stream methods.  Thereafter
 * it stores the input and output streams in a lookup by Cursor
 * until close () is called, at which time it invokes the
 * implementor's <code> closeInputStream () </code> and/or
 * <code> closeOutputStream () </code>.
 * </p>
 *
 * <p>
 * In the meantime, if the implementor can detect stream
 *  disconnects (such as a network connection which has gone
 * down), then it must call <code> disconnectInputStream () </code>
 * and/or <code> disconnectOutputStream () </code>.  The next
 * time the ByteStreamDriver's in (), out (), read () or write ()
 * methods are called, it will respond depending on the state
 * of the Cursor:
 * </p>
 *
 * <ul>
 *   <li> If the Cursor was in the middle of a transaction
 *        when the input or output stream was disrupted,
 *        then <code> disconnectTransaction () </code> will be
 *        invoked.  The default implementation aborts the
 *        transaction.  Implementors can override this
 *        behaviour if disconnects in the middle of a
 *        transaction are deemed to be OK. </li>
 *   <li> Otherwise <code> reconnectInputStream () </code> or
 *        <code> reconnectOutputStream () </code> is called.
 *        The default implementations simply invoke
 *        the appropriate stream open methods.  Implementors
 *        can override this behaviour if disconnects in the
 *        middle of an open Cursor are not deemed to be OK,
 *        or if additional work is required to re-establish
 *        the connection. </li>
 * </ul>
 *
 * <p>
 * Of course even if a ByteStreamDriver is placed at the
 * top of a Driver stack, or below a Driver which does not
 * know how to deal with StreamDrivers, it still provides
 * read () and write () capabilities, reading and writing
 * Fields containing chunks of bytes (4096 by default).
 * </p>
 *
 * <p>
 * Methods which must be implemented by every ByteStreamDriver
 * implementor:
 * </p>
 *
 * <ul>
 *   <li> autoConfigure () </li>
 *   <li> configure () </li>
 *   <li> closeInputStream () </li>
 *   <li> closeOutputStream () </li>
 *   <li> initialize () </li>
 *   <li> openInputStream () </li>
 *   <li> openOutputStream () </li>
 *   <li> shutdown () </li>
 *   <li> state () </li>
 * </ul>
 *
 * <p>
 * Methods which may be optionally implemented:
 * </p>
 *
 * <ul>
 *   <li> disconnectTransaction () </li>
 *   <li> reconnectInputStream () </li>
 *   <li> reconnectOutputStream () </li>
 * </ul>
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
 * Copyright (c) 2011 Johann Tienhaara
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
public abstract class ByteStreamDriver
    implements Driver, StreamDriver // !!! , TransactionalDriver
{
    /** The master byte_stream driver id. */
    public static final DriverIdentifier DRIVER_ID =
        new DriverIdentifier ( "byte_stream" );


    /** Synchronize all transactions and writes on this token: */
    private final Serializable lock = new String ();

    /** The number of references to this record. */
    private final ReferenceCount referenceCount =
        new SimpleReferenceCount ();

    /** The number of times open () has been called with read
     *  mode. */
    private final ReferenceCount readers =
        new SimpleReferenceCount ();

    /** The number of times open () has been called with write
     *  mode. */
    private final ReferenceCount writers =
        new SimpleReferenceCount ();

    /** The Module of which this byte stream driver is a part.
     *  Provides logger, field typing environment, and so on. */
    private final Module module;

    /** Unique identifier for this byte stream driver. */
    private final DriverIdentifier id;

    /** Security for this byte stream driver. */
    private final Security<RecordFlag> security;

    /** Lookup of stream data by Cursor. */
    private final Map<Cursor,Streams> streamsByCursor =
        new HashMap<Cursor,Streams>();

    /** Lookup of Cursors by InputStream. */
    private final Map<InputStream,Set<Cursor>> cursorsByInputStream =
        new HashMap<InputStream,Set<Cursor>>();

    /** Lookup of Cursors by OutputStream. */
    private final Map<OutputStream,Set<Cursor>> cursorsByOutputStream =
        new HashMap<OutputStream,Set<Cursor>>();

    /** Whether transactions are allowed.  Set to false after releasing
     *  an InputStream or an OutputStream to the wild -- then we have
     *  no control over transactional integrity. */
    private boolean isTransactionSafe = true;


    /**
     * <p>
     * Creates a new ByteStreamDriver.
     * </p>
     *
     * @param module The module which loaded this ByteStreamDriver
     *               and provides logging, field typing, and so
     *               on for this driver.  Must not be null.
     *
     * @param id The unique identifier for this driver, such as
     *           DriverIdentifier( "/dev/foo" ).  Must not be null.
     *
     * @param security The security for this ByteStreamDriver.
     *                 Used by this driver to check whether
     *                 requests may be fulfilled.
     *                 Must not be null.
     */
    public ByteStreamDriver (
                             Module module,
                             DriverIdentifier id,
			     Security<RecordFlag> security
                             )
    {
        if ( module == null
             || id == null
	     || security == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a new ByteStreamDriver with module [%module%] id [%id%] security [%security%]",
                                                     "module", module,
                                                     "id", id,
						     "security" );
        }

        this.module = module;
        this.id = id;
	this.security = security;
    }


    /**
     * <p>
     * Returns the number of bytes per Field for read () calls.
     * </p>
     *
     * <p>
     * Each Field read from this byte stream will
     * contain an array of no more than this many bytes.  For
     * example, if the bytes per field is 4096 (the default),
     * and read () is called on a byte stream containing 5000 bytes,
     * then the read () call will read in 2 bytes: one containing
     * 4096 bytes, the second containing 4 bytes.
     * </p>
     *
     * <p>
     * The ByteStreamDriver implementation class can override this
     * method in order to change the fixed byte [] size (for example
     * to 256 or 65,536), or also in order to vary the byte []
     * size depending on the position within the stream (in order
     * to, for example, read a known header size into one or more
     * Fields, followed by an unknown payload size into fixed
     * chunks, and so on).  The ByteStreamDriver calls this method
     * repeatedly during read ().
     * </p>
     *
     * <p>
     * Variable length byte [] sizes will cause the default
     * byteStreamPosition () to fail.  Therefore the ByteStreamDriver
     * implementation must override that method whenever
     * bytesPerField () varies.
     * </p>
     *
     * @param cursor The cursor which is reading from this driver.
     *               Must not be null.
     *
     * @param in The stream which is being read.  Must not be null.
     *
     * @param byte_position The index into the byte stream from which
     *                      a chunk of bytes are about to be read.
     *                      Must be 0L or greater.
     *
     * @return The number of bytes to read in.  Always greater than 0.
     */
    protected int bytesPerField (
                                 Cursor cursor,
                                 InputStream in,
                                 long byte_position
                                 )
    {
        return 4096;
    }


    /**
     * <p>
     * Given a Field Position within an input or output stream for
     * this driver, returns the corresponding byte position.
     * </p>
     *
     * <p>
     * By default StartPosition returns 0, EndPosition throws
     * an exception since the stream length is unknown, and
     * OffsetFromPosition from the start returns
     * ( position.offset () * bytesPerField () ).
     * </p>
     *
     * <p>
     * This method can be overridden by the class implementing
     * ByteStreamDriver, for example to handle different types
     * of Positions.
     * </p>
     *
     * <p>
     * This method is invoked by the read () and write () methods.
     * </p>
     *
     * @param field_position The Field position to convert to a byte
     *                       position in the byte stream.  Must not be null.
     *                       Must be a valid position for this Driver.
     *
     * @return The corresponding byte position within the stream.
     *         Never less than 0L.
     *
     * @throws DriverException If the specified Position is invalid
     *                         or unknown for this Driver.
     */
    protected long byteStreamPosition (
                                       Position field_position
                                       )
        throws DriverException
    {
        if ( field_position instanceof ArrayPosition )
        {
            ArrayPosition array_position = (ArrayPosition) field_position;
            return array_position.index ();
        }
        else
        {
            throw new DriverException ( "Byte stream driver [%byte_stream_driver%] cannot convert field position [%field_position%] to a position in the byte stream",
                                        "byte_stream_driver", this,
                                        "field_position", field_position );
        }
    }


    /**
     * @see musaico.kernel.objectsystem.Record#close(musaico.objectsystem.Cursor)
     */
    @Override
    public void close (
                       Cursor cursor
                       )
        throws RecordOperationException
    {
        this.module.traceEnter ( "ByteStreamDriver.close ()" );

        DriverException close_exception = null;

	Progress progress = cursor.progress ();
        Reference progress_id = progress.push ( 5L );

        synchronized ( this.lock )
        {
            Reference driver_state = this.state ();
            if ( ! driver_state.equals ( Driver.INITIALIZED ) )
            {
                this.module.traceFail ( "ByteStreamDriver.close ()" );
                throw new DriverException ( "Byte stream driver [%byte_stream_driver%] must be in state [%expected_state%] but is in state [%actual_state%]",
                                            "byte_stream_driver", this,
                                            "expected_state", Driver.INITIALIZED,
                                            "actual_state", driver_state );
            }

            progress.step ( progress_id );

            final Streams streams = this.streamsByCursor.get ( cursor );
            if ( streams == null )
            {
                progress.pop ( progress_id );
                this.module.traceFail ( "ByteStreamDriver.close ()" );
                throw new DriverException ( "Byte stream driver [%byte_stream_driver%] has not been opened by cursor [%cursor%]",
                                            "byte_stream_driver", this,
                                            "cursor", cursor );
            }

            progress.step ( progress_id );

            // At this point we no longer care whether the streams
            // were disconnected UNLESS we have a transaction going on.
            if ( streams.state ().equals ( TransactionalDriver.STARTED )
                 || streams.state ().equals ( TransactionalDriver.PREPARED ) )
            {
                // This transaction has not finished.  The caller
                // must either (prepare and) commit the transaction,
                // or roll it back.
                progress.pop ( progress_id );
                this.module.traceFail ( "ByteStreamDriver.close ()" );
                throw new TransactionException ( "Byte stream driver [%byte_stream_driver%] cursor [%cursor%] has an unfinished transaction; commit or rollback the transaction",
                                                 "byte_stream_driver", this,
                                                 "cursor", cursor );
            }

            this.streamsByCursor.remove ( cursor );
            final long reference_count = this.referenceCount.decrement ();

            progress.step ( progress_id );

            InputStream in = streams.in ();
            if ( in != null )
            {
                final long num_readers = this.readers.decrement ();
                Set<Cursor> in_cursors = this.cursorsByInputStream.get ( in );
                in_cursors.remove ( cursor );
                if ( in_cursors.size () == 0L )
                {
                    try
                    {
                        this.closeInputStream ( in );
                    }
                    catch ( DriverException e )
                    {
                        // Throw the first DriverException later.
                        if ( close_exception == null )
                        {
                            close_exception = e;
                        }
                    }

                    this.cursorsByInputStream.remove ( in );
                }
            }

            progress.step ( progress_id );

            OutputStream out = streams.out ();
            if ( out != null )
            {
                final long num_readers = this.readers.decrement ();
                Set<Cursor> out_cursors =
                    this.cursorsByOutputStream.get ( out );
                out_cursors.remove ( cursor );
                if ( out_cursors.size () == 0L )
                {
                    try
                    {
                        this.closeOutputStream ( out );
                    }
                    catch ( DriverException e )
                    {
                        // Throw the first DriverException later.
                        if ( close_exception == null )
                        {
                            close_exception = e;
                        }
                    }

                    this.cursorsByOutputStream.remove ( out );
                }
            }

            progress.step ( progress_id );
        }

        progress.pop ( progress_id );

        if ( close_exception != null )
        {
            this.module.traceFail ( "ByteStreamDriver.close ()" );
            throw new DriverException ( "Could not close byte stream driver [%byte_stream_driver%] cursor [%cursor%]",
                                                 "byte_stream_driver", this,
                                                 "cursor", cursor,
                                                 "cause", close_exception );
        }

        this.module.traceExit ( "ByteStreamDriver.close ()" );
    }


    /**
     * <p>
     * Closes the specified input stream and performs any additional
     * cleanup required.
     * </p>
     *
     * @param in The input stream for one or more Cursors
     *           to access this driver.  Must not be null.
     *
     * @throws DriverException If the stream cannot be closed for
     *                         some reason (hardware failure and so on).
     */
    protected abstract void closeInputStream (
                                              InputStream in
                                              )
        throws DriverException;


    /**
     * <p>
     * Closes the specified output stream and performs any additional
     * cleanup required.
     * </p>
     *
     * @param out The output stream for one or more Cursors
     *            to access this driver.  Must not be null.
     *
     * @throws DriverException If the stream cannot be closed for
     *                         some reason (hardware failure and so on).
     */
    protected abstract void closeOutputStream (
                                               OutputStream out
                                               )
        throws DriverException;


    // Every ByteStreamDriver must implement autoConfigure ().

    // Every ByteStreamDriver must implement configure ().


    /**
     * @see musaico.kernel.driver.Driver#id()
     */
    @Override
    public DriverIdentifier id ()
    {
        return this.id;
    }


    /**
     * @see musaico.kernel.driver.StreamDriver#in(musaico.kernel.objectsystem.Cursor)
     */
    @Override
    public InputStream in (
                           Cursor cursor
                           )
        throws DriverException
    {
        this.module.traceEnter ( "ByteStreamDriver.in ()" );

	Progress progress = cursor.progress ();
        Reference progress_id = progress.push ( 4L );

        final InputStream in;
        synchronized ( this.lock )
        {
            Reference driver_state = this.state ();
            if ( ! driver_state.equals ( Driver.INITIALIZED ) )
            {
                this.module.traceFail ( "ByteStreamDriver.in ()" );
                throw new DriverException ( "Byte stream driver [%byte_stream_driver%] must be in state [%expected_state%] but is in state [%actual_state%]",
                                            "byte_stream_driver", this,
                                            "expected_state", Driver.INITIALIZED,
                                            "actual_state", driver_state );
            }

            progress.step ( progress_id );

            final Streams streams = this.streamsByCursor.get ( cursor );
            if ( streams == null )
            {
                progress.pop ( progress_id );
                this.module.traceFail ( "ByteStreamDriver.in ()" );
                throw new DriverException ( "Byte stream driver [%byte_stream_driver%] has not been opened by cursor [%cursor%]",
                                            "byte_stream_driver", this,
                                            "cursor", cursor );
            }

            progress.step ( progress_id );

            // We can't return the InputStream in the middle of a transaction.
            if ( streams.state ().equals ( TransactionalDriver.STARTED )
                 || streams.state ().equals ( TransactionalDriver.PREPARED ) )
            {
                // This transaction has not finished.  The caller
                // must either (prepare and) commit the transaction,
                // or roll it back.
                progress.pop ( progress_id );
                this.module.traceFail ( "ByteStreamDriver.in ()" );
                throw new TransactionException ( "Byte stream driver [%byte_stream_driver%] cursor [%cursor%] has an unfinished transaction; commit or rollback the transaction",
                                                 "byte_stream_driver", this,
                                                 "cursor", cursor );
            }

            progress.step ( progress_id );

            // Retrieve the input stream for the specified cursor.
            in = streams.in ();
            if ( in == null )
            {
                this.module.traceFail ( "ByteStreamDriver.in ()" );
                throw new DriverException ( "Byte stream driver [%byte_stream_driver%] has not been opened with read access by cursor [%cursor%]",
                                            "byte_stream_driver", this,
                                            "cursor", cursor );
            }

            progress.step ( progress_id );

            // Any transactions in the middle of their operations are
            // guaranteed to fail now.
            this.isTransactionSafe = false;
        }

        this.module.traceExit ( "ByteStreamDriver.in ()" );

        return in;
    }


    // Every ByteStreamDriver must implement initialize ().




    /**
     * @see musaico.kernel.objectsystem.Record#mmap(musaico.objectsystem.Cursor,musaico.kernel.memory.Segment)
     */
    @Override
    public void mmap (
                      Cursor cursor,
                      Segment segment
                      )
        throws RecordOperationException
    {
        this.module.traceEnter ( "ByteStreamDriver.mmap ()" );
        // TODO !!! what to do here?
        this.module.traceFail ( "ByteStreamDriver.in ()" );
        throw new DriverException ( "!!! MMAP NOT YET IMPLEMENTED" );
    }


    /**
     * @see musaico.kernel.objectsystem.Record#open(musaico.objectsystem.Cursor)
     */
    @Override
    public void open (
                      Cursor cursor
                      )
        throws RecordOperationException
    {
        this.module.traceEnter ( "ByteStreamDriver.open ()" );

        DriverException open_exception = null;

	Progress progress = cursor.progress ();
        Reference progress_id = progress.push ( 9L );

        synchronized ( this.lock )
        {
            Reference driver_state = this.state ();
            if ( ! driver_state.equals ( Driver.INITIALIZED ) )
            {
                this.module.traceFail ( "ByteStreamDriver.open ()" );
                throw new DriverException ( "Byte stream driver [%byte_stream_driver%] must be in state [%expected_state%] but is in state [%actual_state%]",
                                            "byte_stream_driver", this,
                                            "expected_state", Driver.INITIALIZED,
                                            "actual_state", driver_state );
            }

            progress.step ( progress_id );

            Streams streams = this.streamsByCursor.get ( cursor );
            if ( streams != null )
            {
                progress.pop ( progress_id );
                this.module.traceFail ( "ByteStreamDriver.open ()" );
                throw new DriverException ( "Byte stream driver [%byte_stream_driver%] has already been opened by cursor [%cursor%]",
                                            "byte_stream_driver", this,
                                            "cursor", cursor );
            }

            OutputStream out = null;
            InputStream in = null;

            progress.step ( progress_id );

            if ( cursor.permissions ().isAllowed ( RecordPermission.WRITE ) )
            {
                final long num_writers = this.writers.increment ();
                try
                {
                    out = this.openOutputStream ( cursor );
                    Set<Cursor> cursors = this.cursorsByOutputStream.get ( out );
                    if ( cursors == null )
                    {
                        cursors = new HashSet<Cursor> ();
                        this.cursorsByOutputStream.put ( out, cursors );
                    }

                    cursors.add ( cursor );
                }
                catch ( DriverException e )
                {
                    // Throw the first open() Exception later.
                    if ( open_exception == null )
                    {
                        open_exception = e;
                    }
                }
            }

            progress.step ( progress_id );

            if ( cursor.permissions ().isAllowed ( RecordPermission.READ ) )
            {
                final long num_readers = this.readers.increment ();
                try
                {
                    in = this.openInputStream ( cursor );
                    Set<Cursor> cursors = this.cursorsByInputStream.get ( in );
                    if ( cursors == null )
                    {
                        cursors = new HashSet<Cursor> ();
                        this.cursorsByInputStream.put ( in, cursors );
                    }

                    cursors.add ( cursor );
                }
                catch ( DriverException e )
                {
                    // Throw the first open exception later.
                    if ( open_exception == null )
                    {
                        open_exception = e;
                    }
                }
            }

            progress.step ( progress_id );

            streams = new Streams ( in, out );

            this.streamsByCursor.put ( cursor, streams );

            progress.step ( progress_id );

            final long reference_count = this.referenceCount.increment ();

            progress.step ( progress_id );

            cursor.position ( this.region ( cursor ).start () );

            progress.step ( progress_id );
        }

        progress.pop ( progress_id );

        if ( open_exception != null )
        {
            this.module.traceFail ( "ByteStreamDriver.open ()" );
            throw new DriverException ( "Could not open byte stream driver [%byte_stream_driver%] cursor [%cursor%]",
                                        "byte_stream_driver", this,
                                        "cursor", cursor,
                                        "cause", open_exception );
        }

        this.module.traceExit ( "ByteStreamDriver.open ()" );
    }


    /**
     * <p>
     * Opens or reuses an input stream for the specified cursor.
     * </p>
     *
     * <p>
     * Some drivers might create a new InputStream for each Cursor,
     * while others might each open only a single InputStream across
     * all Cursors.
     * </p>
     *
     * <p>
     * Be sure to call close () when done with this Record.
     * </p>
     *
     * @param cursor The Cursor connection to this driver.
     *               The cursor might have opened this record
     *               to stream in fields from a driver, or to
     *               randomly access a persistent object, and so on.
     *               Must not be null.
     *
     * @return The (existing or new) input stream.  Never null.
     *
     * @throws DriverException If the stream cannot be opened for
     *                         some reason (hardware failure and so on).
     */
    protected abstract InputStream openInputStream (
                                                    Cursor cursor
                                                    )
        throws DriverException;


    /**
     * <p>
     * Opens or reuses an output stream for the specified cursor.
     * </p>
     *
     * <p>
     * Some drivers might create a new OutputStream for each Cursor,
     * while others might each open only a single OutputStream across
     * all Cursors.
     * </p>
     *
     * <p>
     * Be sure to call close () when done with this Record.
     * </p>
     *
     * @param cursor The Cursor connection to this driver.
     *               The cursor might have opened this record
     *               to stream in fields from a driver, or to
     *               randomly access a persistent object, and so on.
     *               Must not be null.
     *
     * @return The (existing or new) output stream.  Never null.
     *
     * @throws DriverException If the stream cannot be opened for
     *                         some reason (hardware failure and so on).
     */
    protected abstract OutputStream openOutputStream (
                                                      Cursor cursor
                                                      )
        throws DriverException;


    /**
     * @see musaico.kernel.driver.StreamDriver#out(musaico.kernel.objectsystem.Cursor)
     */
    @Override
    public OutputStream out (
                             Cursor cursor
                             )
        throws DriverException
    {
        this.module.traceEnter ( "ByteStreamDriver.out ()" );

	Progress progress = cursor.progress ();
        Reference progress_id = progress.push ( 4L );

        final OutputStream out;
        synchronized ( this.lock )
        {
            Reference driver_state = this.state ();
            if ( ! driver_state.equals ( Driver.INITIALIZED ) )
            {
                this.module.traceFail ( "ByteStreamDriver.out ()" );
                throw new DriverException ( "Byte stream driver [%byte_stream_driver%] must be in state [%expected_state%] but is in state [%actual_state%]",
                                            "byte_stream_driver", this,
                                            "expected_state", Driver.INITIALIZED,
                                            "actual_state", driver_state );
            }

            progress.step ( progress_id );

            final Streams streams = this.streamsByCursor.get ( cursor );
            if ( streams == null )
            {
                progress.pop ( progress_id );
                this.module.traceFail ( "ByteStreamDriver.out ()" );
                throw new DriverException ( "Byte stream driver [%byte_stream_driver%] has not been opened by cursor [%cursor%]",
                                            "byte_stream_driver", this,
                                            "cursor", cursor );
            }

            progress.step ( progress_id );

            // We can't return the OutputStream in the middle of a transaction.
            if ( streams.state ().equals ( TransactionalDriver.STARTED )
                 || streams.state ().equals ( TransactionalDriver.PREPARED ) )
            {
                // This transaction has not finished.  The caller
                // must either (prepare and) commit the transaction,
                // or roll it back.
                progress.pop ( progress_id );
                this.module.traceFail ( "ByteStreamDriver.out ()" );
                throw new TransactionException ( "Byte stream driver [%byte_stream_driver%] cursor [%cursor%] has an unfinished transaction; commit or rollback the transaction",
                                                 "byte_stream_driver", this,
                                                 "cursor", cursor );
            }

            progress.step ( progress_id );

            // Retrieve the output stream for the specified cursor.
            out = streams.out ();
            if ( out == null )
            {
                this.module.traceFail ( "ByteStreamDriver.out ()" );
                throw new DriverException ( "Byte stream driver [%byte_stream_driver%] has not been opened with write access by cursor [%cursor%]",
                                            "byte_stream_driver", this,
                                            "cursor", cursor );
            }

            progress.step ( progress_id );

            // Any transactions in the middle of their operations are
            // guaranteed to fail now.
            this.isTransactionSafe = false;
        }

        this.module.traceExit ( "ByteStreamDriver.out ()" );

        return out;
    }


    /**
     * @see musaico.kernel.objectsystem.Record#poll(musaico.kernel.objectsystem.Cursor,musaico.kernel.objectsystem.PollRequest,musaico.kernel.objectsystem.PollResponse)
     */
    @Override
    public void poll (
                      Cursor cursor,
                      PollRequest request,
                      PollResponse response
                      )
        throws RecordOperationException,
               I18nIllegalArgumentException
    {
        this.module.traceEnter ( "ByteStreamDriver.poll ()" );

        this.module.traceFail ( "ByteStreamDriver.poll ()" );

        throw new DriverException ( "!!! POLLING HAS NOT YET BEEN IMPLEMENTED!" );
    }


    /**
     * @see musaico.kernel.objectsystem.Record#read(musaico.objectsystem.Cursor,musaico.buffer.Buffer,musaico.io.Region)
     */
    @Override
    public Region read (
                        Cursor cursor,
                        Buffer read_fields,
                        Region read_into_region
                        )
        throws RecordOperationException
    {
        this.module.traceEnter ( "ByteStreamDriver.read ()" );

	Progress progress = cursor.progress ();
        Reference progress_id = progress.push ( 6L );

        SparseRegionBuilder actual_buffer_regions_builder = null;
        final Space space = cursor.record ().space ();
        final Size one = space.one ();
        try
        {
            synchronized ( this.lock )
            {
                Reference driver_state = this.state ();
                if ( ! driver_state.equals ( Driver.INITIALIZED ) )
                {
                    progress.pop ( progress_id );
                    this.module.traceFail ( "ByteStreamDriver.read ()" );
                    throw new DriverException ( "Byte stream driver [%byte_stream_driver%] must be in state [%expected_state%] but is in state [%actual_state%]",
                                                "byte_stream_driver", this,
                                                "expected_state", Driver.INITIALIZED,
                                                "actual_state", driver_state );
                }

                progress.step ( progress_id );

                final Streams streams = this.streamsByCursor.get ( cursor );
                if ( streams == null )
                {
                    progress.pop ( progress_id );
                    this.module.traceFail ( "ByteStreamDriver.read ()" );
                    throw new DriverException ( "Byte stream driver [%byte_stream_driver%] has not been opened by cursor [%cursor%]",
                                                "byte_stream_driver", this,
                                                "cursor", cursor );
                }

                progress.step ( progress_id );

                InputStream in = streams.in ();
                if ( in == null )
                {
                    progress.pop ( progress_id );
                    this.module.traceFail ( "ByteStreamDriver.read ()" );
                    throw new DriverException ( "Byte stream driver [%byte_stream_driver%] has not been opened with read access by cursor [%cursor%]",
                                                "byte_stream_driver", this,
                                                "cursor", cursor );
                }

                progress.step ( progress_id );

                Region from_region = this.region ( cursor );
                Position from_position = cursor.position ();
                long byte_seek_position;
                try
                {
                    byte_seek_position =
                        this.byteStreamPosition ( from_position );
                }
                catch ( DriverException e )
                {
                    progress.pop ( progress_id );
                    this.module.traceFail ( "ByteStreamDriver.read ()" );
                    throw e;
                }

                progress.step ( progress_id );

                int chunk_size =
                    this.bytesPerField ( cursor, in, byte_seek_position );
                byte [] chunk = new byte [ chunk_size ];

                Reference read_progress = progress.push ( 100000L );

                for ( Position to_position : read_into_region )
                {
                    progress.step ( read_progress );
                    int chunk_bytes_read = 0;
                    chunk_size =
                        this.bytesPerField ( cursor, in, byte_seek_position );
                    if ( chunk_size > chunk.length )
                    {
                        chunk = new byte [ chunk_size ];
                    }
                    boolean is_eof = false;
                    int chunk_position = 0;
                    while ( chunk_position < chunk_size )
                    {
                        final int num_bytes_read;
                        try
                        {
                            num_bytes_read =
                                in.read ( chunk, chunk_position,
                                          chunk_size - chunk_position );
                        }
                        catch ( IOException e )
                        {
                            progress.pop ( read_progress );
                            progress.pop ( progress_id );
                            this.module.traceFail ( "ByteStreamDriver.read ()" );
                            throw new DriverException ( "Byte stream driver [%byte_stream_driver%] failed to read at [%byte_position%]",
                                                        "byte_stream_driver", this,
                                                        "byte_position", byte_seek_position,
                                                        "cause", e );
                        }

                        if ( num_bytes_read < 0 )
                        {
                            is_eof = true;
                            break;
                        }
                        chunk_position += num_bytes_read;
                    }

                    final byte [] field_bytes = new byte [ chunk_position ];
                    System.arraycopy ( chunk, 0,
                                       field_bytes, 0, chunk_position );
                    String field_id = "bytes";
                    final Field bytes_field =
                        read_fields.environment ().create ( field_id,
                                                            byte [].class,
                                                            field_bytes );

                    read_fields.set ( to_position, bytes_field );

                    final Region one_position =
                        space.region ( to_position, to_position );
                    if ( actual_buffer_regions_builder == null )
                    {
                        // Create a new sparse region builder.
                        actual_buffer_regions_builder =
                            space.sparseRegionBuilder ()
                            .concatenate ( one_position );
                    }
                    else
                    {
                        // Add to the existing sparse region builder.
                        actual_buffer_regions_builder =
                            actual_buffer_regions_builder.concatenate ( one_position );
                    }

                    byte_seek_position += (long) field_bytes.length;
                    from_position = from_region.expr ( from_position ).next ();

                    progress.step ( read_progress );

                    if ( is_eof )
                    {
                        break;
                    }
                }

                progress.pop ( read_progress );
            }
        }
        catch ( BufferException e )
        {
            progress.pop ( progress_id );
            this.module.traceFail ( "ByteStreamDriver.read ()" );
            throw new DriverException ( e );
        }

        progress.step ( progress_id );

        final Region actual_buffer_region;
        if ( actual_buffer_regions_builder == null )
        {
            actual_buffer_region = space.empty ();
        }
        else
        {
            actual_buffer_region = actual_buffer_regions_builder.build ();
        }

        progress.step ( progress_id );

        this.module.traceExit ( "ByteStreamDriver.read ()" );

        return actual_buffer_region;
    }


    /**
     * @see musaico.kernel.objectsystem.Record#region(musaico.kernel.objectsystem.Cursor)
     */
    @Override
    public Region region (
                          Cursor cursor
                          )
        throws RecordOperationException
    {
        this.module.traceEnter ( "ByteStreamDriver.region ()" );

        synchronized ( this.lock )
        {
            Reference driver_state = this.state ();
            if ( ! driver_state.equals ( Driver.INITIALIZED ) )
            {
                this.module.traceFail ( "ByteStreamDriver.region ()" );
                throw new DriverException ( "Byte stream driver [%byte_stream_driver%] must be in state [%expected_state%] but is in state [%actual_state%]",
                                            "byte_stream_driver", this,
                                            "expected_state", Driver.INITIALIZED,
                                            "actual_state", driver_state );
            }

            final Streams streams = this.streamsByCursor.get ( cursor );
            if ( streams == null )
            {
                this.module.traceFail ( "ByteStreamDriver.region ()" );
                throw new DriverException ( "Byte stream driver [%byte_stream_driver%] has not been opened by cursor [%cursor%]",
                                            "byte_stream_driver", this,
                                            "cursor", cursor );
            }
        }

        this.module.traceExit ( "ByteStreamDriver.region ()" );

        // We don't know how many blocks of bytes are available, so just
        // return an unlimited stream of byte blocks.
        return ArraySpace.STANDARD.region ( ArraySpace.STANDARD.origin (),
                                            ArraySpace.STANDARD.max () );
    }


    /**
     * @see musaico.kernel.objectsystem.Record#seek(musaico.objectsystem.Cursor,musaico.io.Position)
     */
    @Override
    public Security<RecordFlag> security ()
    {
	return this.security;
    }


    /**
     * @see musaico.kernel.objectsystem.Record#seek(musaico.objectsystem.Cursor,musaico.io.Position)
     */
    @Override
    public Position seek (
                          Cursor cursor,
                          Position position
                          )
        throws RecordOperationException
    {
        this.module.traceEnter ( "ByteStreamDriver.seek ()" );

        final Position found_position;
        if ( position instanceof ArrayPosition )
        {
            found_position = position;
        }
        else
        {
            found_position = ArraySpace.STANDARD.outOfBounds ();
        }

        cursor.position ( found_position );

        this.module.traceEnter ( "ByteStreamDriver.seek ()" );

        return found_position;
    }


    // Every ByteStreamDriver must implement shutdown ().


    /**
     * @see musaico.kernel.objectsystem.Record#space()
     */
    @Override
    public Space space ()
    {
        this.module.traceEnter ( "ByteStreamDriver.space ()" );
        this.module.traceExit ( "ByteStreamDriver.space ()" );

        return ArraySpace.STANDARD;
    }


    // Every ByteStreamDriver must implement state ().


    /**
     * @see musaico.kernel.objectsystem.Record#sync(musaico.objectsystem.Cursor,boolean)
     */
    @Override
    public void sync (
                      Cursor cursor,
                      boolean is_metadata_only
                      )
        throws RecordOperationException
    {
        this.module.traceEnter ( "ByteStreamDriver.sync ()" );

        this.module.traceFail ( "ByteStreamDriver.sync ()" );

        throw new DriverException ( "!!! SYNC NOT IMPLEMNTED YET" );
        // !!! this.flush ( progress ); // TODO !!! ???
    }


    /**
     * @see musaico.kernel.driver.TransactionalDriver#transactionCommit(musaico.objectsystem.Cursor)
     */
    /* !!!
    @Override
    public void transactionCommit (
                                   Cursor cursor
                                   )
        throws DriverException
    {
        this.module.traceEnter ( "ByteStreamDriver.transactionCommit ()" );

        !!!;
        // fail if this.isTransactionSafe = false;
            Reference driver_state = this.state ();
            if ( ! driver_state.equals ( Driver.INITIALIZED ) )
            {
                progress.pop ( progress_id );
                this.module.traceFail ( "ByteStreamDriver.transactionCommit ()" );
                throw new DriverException ( "Byte stream driver [%byte_stream_driver%] must be in state [%expected_state%] but is in state [%actual_state%]",
                                            "byte_stream_driver", this,
                                            "expected_state", Driver.INITIALIZED,
                                            "actual_state", driver_state );
            }

            this.module.traceExit ( "ByteStreamDriver.transactionCommit ()" );
    }
    !!! */


    /**
     * @see musaico.kernel.driver.TransactionalDriver#transactionPrepareCommit(musaico.objectsystem.Cursor)
     */
    /* !!!
    @Override
    public void transactionPrepareCommit (
                                          Cursor cursor
                                          )
        throws DriverException
    {
        this.module.traceEnter ( "ByteStreamDriver.transactionPrepareCommit ()" );

        !!!;
        // fail if this.isTransactionSafe = false;

        this.module.traceExit ( "ByteStreamDriver.transactionPrepareCommit ()" );
    }
    !!! */


    /**
     * @see musaico.kernel.driver.TransactionalDriver#transactionRollback(musaico.objectsystem.Cursor)
     */
    /* !!!
    @Override
    public void transactionRollback (
                                     Cursor cursor
                                     )
        throws DriverException
    {
        this.module.traceEnter ( "ByteStreamDriver.transactionRollback ()" );

        !!!;

        this.module.traceExit ( "ByteStreamDriver.transactionRollback ()" );
    }
    !!! */


    /**
     * @see musaico.kernel.driver.TransactionalDriver#transactionStart(musaico.objectsystem.Cursor,musaico.buffer.Buffer)
     */
    /* !!!
    @Override
    public void transactionStart (
                                  Cursor cursor,
                                  Buffer transaction_configuration
                                  )
        throws DriverException
    {
        this.module.traceEnter ( "ByteStreamDriver.transactionStart ()" );

        !!!;

        this.module.traceExit ( "ByteStreamDriver.transactionStart ()" );
    }
    !!! */


    /**
     * @see musaico.kernel.objectsystem.Record#write(musaico.objectsystem.Cursor,musaico.buffer.Buffer,musaico.io.Region)
     */
    @Override
    public Region write (
                         Cursor cursor,
                         Buffer write_fields,
                         Region write_from_region
                         )
        throws RecordOperationException
    {
        this.module.traceEnter ( "ByteStreamDriver.write ()" );

	Progress progress = cursor.progress ();
        Reference progress_id = progress.push ( 4000L );

        SparseRegionBuilder actual_buffer_regions_builder = null;
        final Space space = cursor.record ().space ();
        final Size one = space.one ();
        synchronized ( this.lock )
        {
            Reference driver_state = this.state ();
            if ( ! driver_state.equals ( Driver.INITIALIZED ) )
            {
                progress.pop ( progress_id );
                this.module.traceFail ( "ByteStreamDriver.write ()" );
                throw new DriverException ( "Byte stream driver [%byte_stream_driver%] must be in state [%expected_state%] but is in state [%actual_state%]",
                                            "byte_stream_driver", this,
                                            "expected_state", Driver.INITIALIZED,
                                            "actual_state", driver_state );
            }

            progress.step ( progress_id );

            final Streams streams = this.streamsByCursor.get ( cursor );
            if ( streams == null )
            {
                progress.pop ( progress_id );
                this.module.traceFail ( "ByteStreamDriver.write ()" );
                throw new DriverException ( "Byte stream driver [%byte_stream_driver%] has not been opened by cursor [%cursor%]",
                                            "byte_stream_driver", this,
                                            "cursor", cursor );
            }

            progress.step ( progress_id );

            OutputStream out = streams.out ();
            if ( out == null )
            {
                progress.pop ( progress_id );
                this.module.traceFail ( "ByteStreamDriver.write ()" );
                throw new DriverException ( "Byte stream driver [%byte_stream_driver%] has not been opened with write access by cursor [%cursor%]",
                                            "byte_stream_driver", this,
                                            "cursor", cursor );
            }

            progress.step ( progress_id );

            Region to_region = this.region ( cursor );
            Position to_position = cursor.position ();
            final long byte_seek_position;
            try
            {
                byte_seek_position =
                    this.byteStreamPosition ( to_position );
            }
            catch ( DriverException e )
            {
                progress.pop ( progress_id );
                this.module.traceFail ( "ByteStreamDriver.write ()" );
                throw e;
            }

            progress.step ( progress_id );

            Reference write_progress = progress.push ( 100000L );

            for ( Position from_position : write_from_region )
            {
                progress.step ( write_progress );
                Field chunk = write_fields.get ( from_position );
                byte [] bytes_to_write = chunk.value ( byte [].class );

                try
                {
                    out.write ( bytes_to_write );
                }
                catch ( IOException e )
                {
                    progress.pop ( write_progress );
                    progress.pop ( progress_id );
                    this.module.traceFail ( "ByteStreamDriver.write ()" );
                    throw new DriverException ( "Byte stream driver [%byte_stream_driver%] failed to write at [%byte_position%]",
                                                "byte_stream_driver", this,
                                                "byte_position", byte_seek_position,
                                                "cause", e );
                }

                final Region one_position =
                    space.region ( from_position, from_position );
                if ( actual_buffer_regions_builder == null )
                {
                    // Create a new sparse region builder.
                    actual_buffer_regions_builder =
                        space.sparseRegionBuilder ()
                        .concatenate ( one_position );
                }
                else
                {
                    // Add to the existing sparse region builder.
                    actual_buffer_regions_builder =
                        actual_buffer_regions_builder.concatenate ( one_position );
                }

                to_position = to_region.expr ( to_position ).next ();

                progress.step ( write_progress );
            }

            progress.pop ( write_progress );

            try
            {
                out.flush ();
            }
            catch ( IOException e )
            {
                progress.pop ( progress_id );
                this.module.traceFail ( "ByteStreamDriver.write ()" );
                throw new DriverException ( "Byte stream driver [%byte_stream_driver%] failed to write at [%byte_position%]",
                                            "byte_stream_driver", this,
                                            "byte_position", byte_seek_position,
                                            "cause", e );
            }
        }

        progress.step ( progress_id );

        final Region actual_buffer_region;
        if ( actual_buffer_regions_builder == null )
        {
            actual_buffer_region = space.empty ();
        }
        else
        {
            actual_buffer_region = actual_buffer_regions_builder.build ();
        }

        progress.step ( progress_id );

        this.module.traceExit ( "ByteStreamDriver.write ()" );

        return actual_buffer_region;
    }
}
