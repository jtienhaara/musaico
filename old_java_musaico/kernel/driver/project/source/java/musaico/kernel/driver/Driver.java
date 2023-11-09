package musaico.kernel.driver;


import musaico.buffer.Buffer;

import musaico.io.Path;
import musaico.io.Progress;
import musaico.io.Reference;

import musaico.kernel.objectsystem.Cursor;
import musaico.kernel.objectsystem.Record;
import musaico.kernel.objectsystem.RecordTypeIdentifier;

import musaico.region.Position;
import musaico.region.Region;

import musaico.state.Node;


/**
 * <p>
 * Represents a driver in the Musaico system.  Similar to a character
 * or block driver in an operating system kernel, but reading and writing
 * high-level fields (instead of bytes) to build up objects (instead of
 * unstructured byte streams).
 * </p>
 *
 * <p>
 * Every Driver must implement the "raw" methods defined in this
 * interface and the super interface musaico.kernel.objectsystem.Record.
 * The "raw" methods may be used to read and write Fields directly
 * to/from a Driver.
 * </p>
 *
 * <p>
 * However any Driver which is to be used in an object system
 * must also implement the BlockDriver interface.
 * </p>
 *
 * <p>
 * Raw Driver access may be opened and closed on any driver by invoking
 * the <code> open () </code> and <code> close () </code> methods defined
 * by the Record interface with null ONode parameters.
 * On the other hand, block access for an ObjectSystem may only be
 * opened and closed on a <code> BlockDriver </code> by passing
 * non-null ONode parameters.
 * </p>
 *
 * <p>
 * If a non-null ONode parameter is passed to a raw-only Driver
 * (one which does not implement the BlockDriver interface) during
 * <code> open () </code> or <code> close () </code>, the raw
 * driver MUST throw an exception.  Raw-only drivers cannot be
 * used as the backing stores for object systems.
 * </p>
 *
 * @see musaico.kernel.driver.BlockDriver
 *
 * <p>
 * When <code> open () </code> and <code> close () </code> are called
 * on a Driver, it incremented and decrements its reference count.
 * </p>
 *
 * <p>
 * No driver may be <code> open () </code>ed or <code> closed () </code>ed
 * unless it is in the initialized state.
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
public interface Driver
    extends Record
{
    /** The Record type identifier representing all drivers.
     *  This is used in an object system to create a node
     *  which provides access to the Driver via the object system. */
    public static final RecordTypeIdentifier TYPE_ID =
        new RecordTypeIdentifier ( "driver" );


    /** An uninitialized Driver.  Must be transitioned to either
     *  state CONFIGURED or state AUTO_CONFIGURED. */
    public static final Reference UNINITIALIZED =
        new Path ( "/state/driver/uninitialized" );

    /** A manually configured Driver.  Must be transitioned to
     *  "initialized" before the Driver is ready to handle requests. */
    public static final Reference CONFIGURED =
        new Path ( "/state/driver/configured" );

    /** A Driver which will be auto-configured when it is
     *  initialized.  Must be transitioned to "initialized"
     *  before the Driver is ready to handle requests. */
    public static final Reference AUTO_CONFIGURED =
        new Path ( "/state/driver/auto_configured" );

    /** An initialized Driver.  The Driver can handle requests. */
    public static final Reference INITIALIZED =
        new Path ( "/state/driver/initialized" );

    /** A Driver which has been frozen indefinitely. */
    public static final Reference SUSPENDED =
        new Path ( "/state/driver/suspended" );


    // Every Driver must implement all Record methods.
    // See musaico.kernel.objectsystem.Record.


    /**
     * <p>
     * Auto-detects the configuration settings for this driver,
     * and returns the configured driver.
     * </p>
     *
     * <p>
     * Depending on the Driver, the return value may or may
     * not be the same as the Driver object on which this
     * method was called.  In particular, some Drivers are
     * "topevel classes" which instantiate themselves and
     * return the instances whenever configured or auto-configured.
     * The caller must be certain to continue working on the
     * driver returned by autoConfigure () / configure ().
     * </p>
     *
     * <p>
     * After a driver has been successfully auto-configured, it can
     * be <code> initialize </code>d with the configuration data which was
     * auto-detected.
     * </p>
     *
     * <p>
     * May only be called from the UNINITIALIZED, CONFIGURED or
     * AUTO_CONFIGURED states.  Otherwise an exception is thrown.
     * </p>
     *
     * @return The freshly auto-configured Driver.  Might or might not
     *         be the same as the Driver on which the call was
     *         made, depending on the Driver.  Never null.
     */
    public abstract Driver autoConfigure ()
        throws DriverException;


    /**
     * <p>
     * Configures this driver with the specified buffer of settings.
     * </p>
     *
     * <p>
     * Depending on the Driver, the return value may or may
     * not be the same as the Driver object on which this
     * method was called.  In particular, some Drivers are
     * "topevel classes" which instantiate themselves and
     * return the instances whenever configured or auto-configured.
     * The caller must be certain to continue working on the
     * driver returned by autoConfigure () / configure ().
     * </p>
     *
     * <p>
     * The input buffer must conform to the driver's expectations.
     * For example, if a specific type of Field is mandatory, or if
     * particular named Fields are required, then they must be provided
     * in the input buffer.
     * </p>
     *
     * <p>
     * May be called from any state.  However the effects of calling
     * from many states are driver-dependent, and may result
     * in exceptions or new driver instances (rather than configuring
     * the original driver, it is cloned with the specified configuration).
     *  See the individual drivers for details.
     * </p>
     *
     * <p>
     * All drivers must be able to gracefully handle configure
     * calls from the UNINITIALIZED, CONFIGURED and AUTO_CONFIGURED
     * states.  These are normal events.
     * </p>
     *
     * @param configuration_buffer The buffer containing the configuration
     *                             parameters for this driver.
     *                             Must not be null.
     *
     * @return The freshly configured Driver.  Might or might not
     *         be the same as the Driver on which the call was
     *         made, depending on the Driver.  Never null.
     */
    public abstract Driver configure (
                                      Buffer configuration_buffer
                                      )
        throws DriverException;


    /**
     * <p>
     * Returns the unique identifier of this Driver.
     * </p>
     *
     * @return This driver's id.  Never null.
     */
    public abstract DriverIdentifier id ();


    /**
     * <p>
     * Initializes the driver so that it is ready to read, write,
     * and so on.
     * </p>
     *
     * <p>
     * Drivers often allocate memory, set up data structures, open
     * network connections, start threads, and so on during
     * initialization.
     * </p>
     *
     * <p>
     * The driver may only be initialized if it is either in state
     * AUTO_CONFIGURED or in state CONFIGURED.
     * </p>
     *
     * <p>
     * Control returns only after all necessary startup has
     * been completed and the driver is ready to accept
     * read, write, and so on requests.
     * </p>
     */
    public abstract void initialize ()
        throws DriverException;


    /**
     * <p>
     * Shuts down this driver, cleaning up data structures,
     * freeing memory, closing connections, and so on.
     * </p>
     *
     * <p>
     * May only be called from the Driver.INITIALIZED state.
     * </p>
     */
    public abstract void shutdown ()
        throws DriverException;



    /**
     * <p>
     * Returns the name of the state the driver is in
     * (such as INITIALIZED, SUSPENDED, and so on).
     * </p>
     *
     * @return This driver's state name.  Never null.
     */
    public abstract Reference state ();
}
