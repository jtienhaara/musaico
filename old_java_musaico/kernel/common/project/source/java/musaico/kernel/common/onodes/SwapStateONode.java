package musaico.kernel.common.onodes;

import java.io.Serializable;


import musaico.buffer.Buffer;

import musaico.i18n.exceptions.I18nIllegalArgumentException;
import musaico.i18n.exceptions.I18nIllegalStateException;

import musaico.kernel.memory.paging.SwapState;

import musaico.kernel.memory.Segment;

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
 * An ONode which points to a SwapState, allowing a swap state to be treated
 * as an object in the object system, and opened for reading and writing.
 * </p>
 *
 * <p>
 * One or more SwapStateONodes can be referenced to build up a new
 * swapping memory system, which can be useful, for example, in
 * building custom object systems (for example, swapped in from a
 * webserver to the local file system to in-memory objects, or
 * some completely different swap system according to the needs
 * of the customized object system).
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
public class SwapStateONode
    extends SimpleONode
    implements Serializable
{
    /** The type identifier representing all swap state records. */
    public static final RecordTypeIdentifier TYPE_ID =
        new RecordTypeIdentifier ( "swap_state_record" );


    /** The SwapState to which this SwapStateONode points, such as
     *  a swap state representing data on remote webservers, or a
     *  swap state representing data in local or remote database
     *  servers, or a swap state representing files on the local
     *  file system, or in-memory fields swap state and so on. */
    private SwapState swapState;

    /** Configuration settings for the swap system.
     *  Shutdown and re-initialization required after changing
     *  the configuration. */
    private final Record configuration;


    /**
     * <p>
     * Creates a new SwapStateONode with the specified swap state and
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
     * @param data The segment and paged area backing this ONode.
     *             The segment's Security restricts this ONode's
     *             internal operations, whereas the ONode's own
     *             Security is used to restrict the outside world.
     *             Must not be null.
     *
     * @param metadata The metadata builder for this ONode.
     *                 Keeps the state of all times (created time,
     *                 modified time) and other metadata for
     *                 the ONode, and builds snapshots whenever asked.
     *                 Must not be null.
     *
     * @param swap_state The SwapState to which this SwapStateONode points,
     *                   such as a swapped-out-to-database servers
     *                   swap state, or a swapped-out-to-local-filesystem
     *                   swap state, or a swapped-in-to-memory state,
     *                   and so on.  Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (see above).
     */
    public SwapStateONode (
			   Module module,
			   SuperBlock super_block,
			   ONodeIdentifier id,
			   ONodeOperations ops,
			   Security<RecordFlag> security,
			   Record data,
			   ONodeMetadata metadata,
			   SwapState swap_state,
			   Record configuration
			   )
    {
        super ( module,
                super_block,
                id,
                ops,
                security,
                data,
                metadata );

        if ( swap_state == null
	     || configuration == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a SwapStateONode with swap state [%swap_state%] configuration [%configuration%]",
                                                     "swap_state", swap_state,
						     "configuration", configuration );
        }

        this.swapState = swap_state;
	this.configuration = configuration;
    }


    /**
     * <p>
     * Returns the configuration settings for the SwapState backing this onode.
     * </p>
     *
     * <p>
     * The configuration settings can be read and written but will not affect
     * the underlying SwapState until shutdown and re-initialization.
     * </p>
     *
     * @return The conbfiguration settiugs that will be in effect the next time
     *         this onode's SwapState is initialized.  Never null.
     */
    public Record configuration ()
    {
	return this.configuration;
    }


    /**
     * <p>
     * Returns the swap state for this ONode.
     * </p>
     *
     * @return The SwapState which this SwapStateONode points to.
     *         Never null.
     */
    public SwapState swapState ()
    {
        return this.swapState;
    }
}
