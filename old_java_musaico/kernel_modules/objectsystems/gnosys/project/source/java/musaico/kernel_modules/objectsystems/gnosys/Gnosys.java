package musaico.kernel_modules.objectsystems.gnosys;

import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Identifier;
import musaico.io.Path;
import musaico.io.Identifier;
import musaico.io.Progress;

import musaico.buffer.Buffer;

import musaico.io.references.SimpleSoftReference;

import musaico.kernel.swappers.pages.BlockPageFactory;

import musaico.kernel.swappers.swappers.BufferBlockSwapper;

import musaico.kernel.driver.BlockDriver;
import musaico.kernel.driver.Driver;

import musaico.kernel.memory.KernelPaging;
import musaico.kernel.memory.MemoryException;
import musaico.kernel.memory.PageFactory;
import musaico.kernel.memory.Segment;
import musaico.kernel.memory.SegmentFactory;
import musaico.kernel.memory.SegmentIdentifier;
import musaico.kernel.memory.SimplePageFactory;
import musaico.kernel.memory.Swapper;
import musaico.kernel.memory.SwapState;
import musaico.kernel.memory.SwapStates;

import musaico.kernel.memory.pages.BufferPageFactory;

import musaico.kernel.memory.security.NoSegmentSecurity; // !!! TEMPORARY HACK

import musaico.kernel.memory.segments.SimpleSegmentFactory;

import musaico.kernel.module.Module;
import musaico.kernel.module.ModuleOperationException;

import musaico.kernel.objectsystem.ObjectSystemFactory;
import musaico.kernel.objectsystem.ObjectSystemFactoryException;
import musaico.kernel.objectsystem.ObjectSystemFactoryLock;
import musaico.kernel.objectsystem.ObjectSystemIdentifier;
import musaico.kernel.objectsystem.ObjectSystemTypeFlag;
import musaico.kernel.objectsystem.ObjectSystemTypeIdentifier;
import musaico.kernel.objectsystem.ObjectSystemTypePermission;
import musaico.kernel.objectsystem.ObjectSystemTypePermissions;
import musaico.kernel.objectsystem.OEntry;
import musaico.kernel.objectsystem.ONode;
import musaico.kernel.objectsystem.RecordOperationException;
import musaico.kernel.objectsystem.SuperBlock;
import musaico.kernel.objectsystem.SuperBlockOperationException;

import musaico.kernel.objectsystems.onodes.DriverONode;

import musaico.mutex.Mutex;

import musaico.security.Credentials;
import musaico.security.Security;


/**
 * <p>
 * A factory to mount and unmount Gnosys object systems.
 * </p>
 *
 *
 * <p>
 * In Java, every ObjectSystemFactory must be Serializable in order
 * to play nicely across RMI.  Even a non-distributed object system type
 * must have a Serializable ObjectSystemFactory, so that the
 * knowledge of how to create and manipulate them can be shared across
 * nodes in a distributed kernel.
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
public class Gnosys
    implements ObjectSystemFactory, Serializable
{
    /** The unique object system type id for all gnosys object systems. */
    public static final ObjectSystemTypeIdentifier OBJECT_SYSTEM_TYPE =
        new ObjectSystemTypeIdentifier ( "gnosys" );


    /** Protect all critical sections by locking a MutexLock
     *  on this mutex: */
    private final Mutex mutex;

    /** The Module which provides access to the kernel and other
     *  kernel modules. */
    private final Module module;

    /** The security manager for this object system factory. */
    private final Security<ObjectSystemTypeFlag> security;


    /**
     * <p>
     * Creates a new Gnosys object system factory inside the
     * specified Module.
     * </p>
     *
     * @param module The Module which provides this Gnosys factory
     *               with access to the kernel and other kernel
     *               modules.  Must not be null.
     *
     * @param security The security manager for this object system.
     *                 Determines who is allowed to perform
     *                 operations.  Must not be null.
     *
     * @throws I18nIllegalArgumentException If the parameters are
     *                                      invalid (see above).
     */
    public Gnosys (
                   Module module,
                   Security<ObjectSystemTypeFlag> security
                   )
        throws I18nIllegalArgumentException
    {
        if ( module == null
             || security == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a Gnosys factory inside module [%module%] with security [%security%]",
                                                     "module", module );
        }

        this.mutex = new Mutex ( this );
        this.module = module;
        this.security = security;
    }


    /**
     * @see musaico.kernel.objectsystem.ObjectSystemFactory#id()
     */
    public ObjectSystemTypeIdentifier id ()
    {
        return Gnosys.OBJECT_SYSTEM_TYPE;
    }


    /**
     * @see musaico.kernel.objectsystem.ObjectSystemFactory#mutex()
     */
    public Mutex mutex ()
    {
        return this.mutex;
    }


    /**
     * @see musaico.kernel.objectsystem.ObjectSystemFactory#mount(musaico.security.Credentials,musaico.io.Progress,musaico.kernel.objectsystem.OEntry,musaico.io.Identifier,musaico.buffer.Buffer)
     */
    public SuperBlock mount (
                             Credentials credentials,
                             Progress progress,
                             OEntry mount_point,
                             OEntry driver_entry,
                             Buffer mount_options
                             )
        throws ObjectSystemFactoryException
    {
        ObjectSystemIdentifier super_block_id =
            new ObjectSystemIdentifier ( mount_point.path () );

        final KernelPaging kernel_paging;
        final BlockDriver block_driver;
        try
        {
            // Get the kernel paging / LRU.
            kernel_paging =
                this.module.getKernelObject ( KernelPaging.KERNEL_OBJECT_ID );

            // Lookup the driver.
            ONode onode =
                this.module.getKernelObject ( driver_entry.onodeRef () );
            if ( ! ( onode instanceof DriverONode ) )
            {
                throw new ObjectSystemFactoryException ( "Cannot mount [%object_system_type%] at [%mount_point%] from driver [%driver_entry%]: Not a driver",
                                                         "object_system_type", this.id (),
                                                         "mount_point", mount_point,
                                                         "driver_entry", driver_entry );
            }

            DriverONode driver_onode = (DriverONode) onode;
            final Driver driver;
            try
            {
                driver = driver_onode.driver ();
            }
            catch ( RecordOperationException e )
            {
                throw new ObjectSystemFactoryException ( "Cannot mount [%object_system_type%] at [%mount_point%] from driver [%driver_entry%]: DriverONode is mis-configured",
                                                         "object_system_type", this.id (),
                                                         "mount_point", mount_point,
                                                         "driver_entry", driver_entry );
            }

            if ( ! ( driver instanceof BlockDriver ) )
            {
                throw new ObjectSystemFactoryException ( "Cannot mount [%object_system_type%] at [%mount_point%] from driver [%driver_entry%]: Driver is not a block driver",
                                                         "object_system_type", this.id (),
                                                         "mount_point", mount_point,
                                                         "driver_entry", driver_entry );
            }

            block_driver = (BlockDriver) driver;
        }
        catch ( ModuleOperationException e )
        {
            throw new ObjectSystemFactoryException ( e );
        }

        SwapState swapped_out = SwapStates.SWAPPED_OUT_TO_PERSISTENT_STORAGE;
        SwapState swapped_in = SwapStates.SWAPPED_IN_TO_FIELDS;
        Swapper [] swappers = new Swapper []
        {
            new BufferBlockSwapper ( swapped_out,
                                     swapped_in )
        };
        PageFactory page_factory =
            new SimplePageFactory ( swapped_out,
                                    new BlockPageFactory ( driver_entry,
                                                           block_driver ),
                                    swapped_in,
                                    new BufferPageFactory () );
        SegmentFactory segment_factory =
            new SimpleSegmentFactory ( kernel_paging,
                                       swappers,
                                       page_factory );

        // One segment and paged area for the whole object system:
        SegmentIdentifier segment_id = new SegmentIdentifier ();
        final Segment segment;
        try
        {
            segment =
                segment_factory.createSegment ( segment_id,
                                                module.physicalMemory (),
                                                new NoSegmentSecurity (), // !!!!!!!!!!!!!!!!!!!!!!!!!! NO SECURITY
                                                credentials );
        }
        catch ( MemoryException e )
        {
            throw new ObjectSystemFactoryException ( e );
        }

        GnSuperBlock super_block =
            new GnSuperBlock ( this.module,
                               this.id (),
                               super_block_id,
                               mount_point,
                               mount_options,
                               kernel_paging,
                               segment_factory,
                               segment );

        try
        {
            super_block.setup ( progress );
        }
        catch ( SuperBlockOperationException e )
        {
            throw new ObjectSystemFactoryException ( e );
        }

        return super_block;
    }


    /**
     * @see musaico.kernel.objectsystem.ObjectSystemFactory#security()
     */
    public Security<ObjectSystemTypeFlag> security ()
    {
        return this.security;
    }


    /**
     * @see musaico.kernel.objectsystem.ObjectSystemFactory#unmount(musaico.security.Credentials,musaico.io.Progress,musaico.kernel.objectsystem.ObjectSystemIdentifier)
     */
    public void unmount (
                         Credentials credentials,
                         Progress progress,
                         ObjectSystemIdentifier super_block_id
                         )
        throws ObjectSystemFactoryException
    {
        throw new ObjectSystemFactoryException ( "!!! NOT IMPLEMENTED" ); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!
    }
}
