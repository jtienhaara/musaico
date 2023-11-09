package musaico.kernel;


import musaico.io.Identifier;
import musaico.io.Path;
import musaico.io.SimpleIdentifier;

import musaico.io.references.UUIDReference;


/**
 * <p>
 * The static namespaces for Identifiers for kernel objects
 * (Drivers, SuperBlocks, and so on).
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
public final class KernelNamespaces
{
    /** Top-level namespace for all kernel objects.  Each kernel
     *  is given a UUID, in case kernels want to share their
     *  kernel objects for some reason.  This way, every kernel
     *  object is always identified inside a particular kernel's
     *  namespace. */
    public static final Identifier KERNEL =
        new SimpleIdentifier ( Identifier.NONE,
                               new UUIDReference () );

    /** ======================== Drivers layer ======================== */
    /** Namespace for kernel Drivers. */
    public static final Identifier DRIVERS =
        new SimpleIdentifier ( KernelNamespaces.KERNEL,
                               new Path ( "sys/drivers" ) );

    /** ========================= Memory layer ======================== */
    /** Namespace for the kernel memory layer, including kernel paging. */
    public static final Identifier MEMORY_LAYER =
        new SimpleIdentifier ( KernelNamespaces.KERNEL,
                               new Path ( "sys/memory" ) );

    /** Namespace for kernel memory allocators. */
    public static final Identifier MEMORY =
        new SimpleIdentifier ( KernelNamespaces.MEMORY_LAYER,
                               new Path ( "memory" ) );

    /** Namespace for kernel memory Segments. */
    public static final Identifier SEGMENTS =
        new SimpleIdentifier ( KernelNamespaces.MEMORY_LAYER,
                               new Path ( "segments" ) );

    /** Namespace for kernel memory PagedAreas. */
    public static final Identifier PAGED_AREAS =
        new SimpleIdentifier ( KernelNamespaces.MEMORY_LAYER,
                               new Path ( "paged_areas" ) );

    /** Namespace for kernel memory SwapSystems. */
    public static final Identifier SWAP_SYSTEMS =
        new SimpleIdentifier ( KernelNamespaces.MEMORY_LAYER,
                               new Path ( "swapsystems" ) );

    /** ======================== Modules layer ======================== */
    /** Namespace for kernel dynamically loadable Modules. */
    public static final Identifier MODULES =
        new SimpleIdentifier ( KernelNamespaces.KERNEL,
                               new Path ( "sys/modules" ) );

    /** ===================== Object Systems layer ==================== */
    /** Namespace for the kernel object systems layer . */
    public static final Identifier OBJECT_SYSTEMS_LAYER =
        new SimpleIdentifier ( KernelNamespaces.KERNEL,
                               new Path ( "sys/objectsystems" ) );

    /** Namespace for kernel Cursors in the object systems layer. */
    public static final Identifier CURSORS =
        new SimpleIdentifier ( KernelNamespaces.OBJECT_SYSTEMS_LAYER,
                               new Path ( "cursors" ) );

    /** Namespace for kernel ObjectSystemTypes. */
    public static final Identifier OBJECT_SYSTEM_TYPES =
        new SimpleIdentifier ( KernelNamespaces.OBJECT_SYSTEMS_LAYER,
                               new Path ( "objectsystemtypes" ) );

    /** Namespace for kernel OEntries in the object systems layer. */
    public static final Identifier OENTRIES =
        new SimpleIdentifier ( KernelNamespaces.OBJECT_SYSTEMS_LAYER,
                               new Path ( "oentries" ) );

    /** Namespace for record types (flat record, object, and so on)
     *  in the object systems layer. */
    public static final Identifier RECORD_TYPES =
        new SimpleIdentifier ( KernelNamespaces.OBJECT_SYSTEMS_LAYER,
                               new Path ( "recordtypes" ) );

    /** Namespace for relation record types (symbolic links, unions, and so on)
     *  in the object systems layer. */
    public static final Identifier RELATION_TYPES =
        new SimpleIdentifier ( KernelNamespaces.RECORD_TYPES,
                               new Path ( "relationtypes" ) );

    /** Namespace for kernel SuperBlocks in the object systems layer. */
    public static final Identifier SUPER_BLOCKS =
        new SimpleIdentifier ( KernelNamespaces.OBJECT_SYSTEMS_LAYER,
                               new Path ( "objectsystems" ) );

    /** Namespace for kernel quota types in the object systems layer. */
    public static final Identifier QUOTAS =
        new SimpleIdentifier ( KernelNamespaces.OBJECT_SYSTEMS_LAYER,
                               new Path ( "quotas" ) );

    /** Namespace for quota categories (user, group, ...) in the
     *  object systems layer. */
    public static final Identifier QUOTA_CATEGORIES =
        new SimpleIdentifier ( KernelNamespaces.QUOTAS,
                               new Path ( "categories" ) );

    /** Namespace for quota resource types (ONodes, fields, ...) in the
     *  object systems layer. */
    public static final Identifier QUOTA_RESOURCES =
        new SimpleIdentifier ( KernelNamespaces.QUOTAS,
                               new Path ( "resources" ) );


    /**
     * <p>
     * This class should never be instantiated.  It holds static
     * constants, nothing more.
     * </p>
     */
    private KernelNamespaces ()
    {
    }
}
