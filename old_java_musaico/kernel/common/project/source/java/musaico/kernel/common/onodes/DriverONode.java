package musaico.kernel.common.onodes;

import java.io.Serializable;


import musaico.buffer.Buffer;

import musaico.i18n.exceptions.I18nIllegalArgumentException;
import musaico.i18n.exceptions.I18nIllegalStateException;

import musaico.kernel.driver.Driver;

import musaico.kernel.module.Module;

import musaico.kernel.objectsystem.Record;
import musaico.kernel.objectsystem.RecordFlag;
import musaico.kernel.objectsystem.RecordOperationException;
import musaico.kernel.objectsystem.RecordTypeIdentifier;

import musaico.kernel.objectsystem.onode.ONodeIdentifier;
import musaico.kernel.objectsystem.onode.ONodeMetadata;
import musaico.kernel.objectsystem.onode.ONodeOperations;

import musaico.kernel.objectsystem.superblock.SuperBlock;

import musaico.region.Region;

import musaico.security.Security;


/**
 * <p>
 * An ONode which points to a Driver, allowing a driver to be treated
 * as an object in the object system, and opened for reading and writing.
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
public class DriverONode
    extends SimpleONode
    implements Serializable
{
    /** The type identifier representing all driver records. */
    public static final RecordTypeIdentifier TYPE_ID =
        new RecordTypeIdentifier ( "driver_record" );


    /** The Driver to which this DriverONode points. */
    private Driver driver;

    /** Configuration settings for the driver.
     *  Shutdown and re-initialization required after changing
     *  the configuration. */
    private final Record configuration;


    /**
     * <p>
     * Creates a new DriverONode with the specified driver and
     * configuration settings.
     * </p>
     *
     * <p>
     * This ONode does NOT fill in any metadata.  That is left
     * to the caller, and this ONode's operations methods.
     * </p>
     *
     * @param module The module which provides this ONode access
     *               to the kernel and other kernel modules.
     *               Must not be null.
     *
     * @param super_block The SuperBlock which created this ONode.
     *                    Must not be null.
     *
     * @param id The unique identifier of this ONode within the SuperBlock.
     *           Must not be null.
     *
     * @param ops The operations for this ONode.  Must not be null.
     *
     * @param security The security manager for this ONode.
     *                 For example, UNIX-like security.
     *                 Must not be null.
     *
     * @param driver The Driver backing this ONode.  When the ONode
     *               is opened for read/write access, it is the
     *               underlying driver doing the work.
     *               Must not be null.
     *
     * @param metadata The metadata builder for this ONode.
     *                 Keeps the state of all times (created time,
     *                 modified time) and other metadata for
     *                 the ONode, and builds snapshots whenever asked.
     *                 Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (see above).
     */
    public DriverONode (
                        Module module,
                        SuperBlock super_block,
                        ONodeIdentifier id,
                        ONodeOperations ops,
                        Security<RecordFlag> security,
                        Driver driver,
                        ONodeMetadata metadata,
			Record configuration
                        )
    {
        super ( module,
                super_block,
                id,
                ops,
                security,
                driver,
                metadata );

        if ( configuration == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a DriverONode with driver [%driver%] configuration [%configuration%]",
                                                     "driver", driver,
						     "configuration", configuration );
        }

        this.driver = driver;
	this.configuration = configuration;
    }


    /**
     * <p>
     * Returns the configuration settings for the Driver backing this onode.
     * </p>
     *
     * <p>
     * The configuration settings can be read and written but will not affect
     * the underlying Driver until shutdown and re-initialization.
     * </p>
     *
     * @return The conbfiguration settiugs that will be in effect the next time
     *         this onode's Driver is initialized.  Never null.
     */
    public Record configuration ()
    {
	return this.configuration;
    }


    /**
     * <p>
     * Returns the driver for this ONode.
     * </p>
     *
     * <p>
     * The same as calling <code> data () </code> except that no
     * casting is necessary (from raw Record to Driver).
     * </p>
     *
     * @return The Driver which this DriverONode points to.
     *         Never null.
     */
    public Driver driver ()
    {
        return this.driver;
    }
}
