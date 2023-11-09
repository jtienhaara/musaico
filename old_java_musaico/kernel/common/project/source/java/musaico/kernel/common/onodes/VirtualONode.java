package musaico.kernel.common.onodes;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import musaico.buffer.Buffer;

import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.i18n.log.Level;

import musaico.field.Field;

import musaico.io.Path;
import musaico.io.Reference;
import musaico.io.Sequence;

import musaico.kernel.common.oentries.SimpleOEntry;

import musaico.kernel.memory.Segment;

import musaico.kernel.module.Module;
import musaico.kernel.module.ModuleOperationException;

import musaico.mutex.Mutex;

import musaico.kernel.objectsystem.Cursor;
import musaico.kernel.objectsystem.PollRequest;
import musaico.kernel.objectsystem.PollResponse;
import musaico.kernel.objectsystem.Record;
import musaico.kernel.objectsystem.RecordLock;
import musaico.kernel.objectsystem.RecordOperationException;
import musaico.kernel.objectsystem.RecordFlag;
import musaico.kernel.objectsystem.RecordPermission;
import musaico.kernel.objectsystem.RecordPermissions;
import musaico.kernel.objectsystem.RecordTypeIdentifier;

import musaico.kernel.objectsystem.onode.FillOEntryCallback;
import musaico.kernel.objectsystem.onode.OEntry;
import musaico.kernel.objectsystem.onode.OEntryIdentifier;
import musaico.kernel.objectsystem.onode.OEntryFactory;
import musaico.kernel.objectsystem.onode.OEntryOperationException;
import musaico.kernel.objectsystem.onode.ONode;
import musaico.kernel.objectsystem.onode.ONodeIdentifier;
import musaico.kernel.objectsystem.onode.ONodeLock;
import musaico.kernel.objectsystem.onode.ONodeMetadata;
import musaico.kernel.objectsystem.onode.ONodeOperationException;
import musaico.kernel.objectsystem.onode.ONodeReferences;
import musaico.kernel.objectsystem.onode.ONodeSecurityException;
 
import musaico.kernel.objectsystem.onodes.FlatONode;
import musaico.kernel.objectsystem.onodes.ObjectONode;
import musaico.kernel.objectsystem.onodes.Relation;
import musaico.kernel.objectsystem.onodes.RelationTypeIdentifier;

import musaico.kernel.objectsystem.superblock.SuperBlock;
import musaico.kernel.objectsystem.superblock.SuperBlockIdentifier;
import musaico.kernel.objectsystem.superblock.SuperBlockOperationException;

import musaico.region.Position;
import musaico.region.Region;
import musaico.region.Space;

import musaico.security.Credentials;
import musaico.security.Permissions;
import musaico.security.Security;

import musaico.time.AbsoluteTime;
import musaico.time.Time;


/**
 * <p>
 * Provides a top layer ONode for all object systems in a
 * kernel.  The top layer of VirtualONodes is used by the
 * kernel for object system-independent security and so
 * on, but delegates to the object system's ONode methods
 * in order to carry out object system-specific operations.
 * </p>
 *
 * <p>
 * The VirtualONode performs argument and error checking
 * before passing on each call to the object system's ONode
 * implementation.  In some cases the bulk of
 * the work is done by the VirtualONode, so there
 * is no need for the object system to do anything.
 * </p>
 *
 * <p>
 * The VirtualONode implements all of the ONode
 * operations available (hierarchical objects, flat objects,
 * relations, and so on) so that callers can expect proper
 * exceptions if they operate on the wrong type of node
 * (ONodeOperationException) rather than runtime casting
 * exceptions.
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
public class VirtualONode
    implements ONode, FlatONode, ObjectONode, Relation, Serializable
{
    /** The module which created this ONode and gives
     *  us access to kernel functionality. */
    private final Module module;

    /** The actual ONode for which this VirtualONode provides security,
     *  metadata and so on wrappers. */
    private final ONode onode;


    /**
     * <p>
     * Creates a new VirtualONode inside the specified
     * Module.
     * </p>
     *
     * @param module The kernel module from which this
     *               VirtualONode was created.  Must not
     *               be null.  The module gives this VirtualONode
     *               access to the kernel.
     *
     * @param onode The underlying ONode for this virtual ONode.
     *              This virtual ONode wraps all calls, performing
     *              security checks, updating most metadata and so on.
     *              Must not be null.
     *
     * @throws I18nIllegalArgumentException If the specified parameters
     *                                      are invalid.
     */
    public VirtualONode (
                         Module module,
			 ONode onode
                         )
    {
        if ( module == null
	     || onode == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a VirtualONode with module [%module%] ONode [%onode%]",
                                                     "module", module,
						     "onode", onode );
        }

        this.module = module;
	this.onode = onode;

        this.module.traceEnter ( "VirtualONode()" );
        this.module.traceExit ( "VirtualONode()" );
    }


    /**
     * <p>
     * Checks that the specified child OEntry is actually a child of the
     * specified parent ONode.
     * </p>
     *
     * <p>
     * You should always lock a MutexLock on the parent and child ONodes
     * before calling this method.  See removeChildObject() for example.
     * </p>
     *
     * <pre>
     *     MutexLock mutex_lock =
     *         new MutexLock ( parent_onode.mutex ( credentials ),
     *                         child_onode.mutex ( credentials ) )
     *     mutex_lock.lock ();
     *     try
     *     {
     *         checkParentChild ( credentials, parent_onode, child_entry );
     *         ...retrieve the child onode...
     *     }
     *     finally
     *     {
     *         mutex_lock.unlock ();
     *     }
     * </pre>
     *
     * @param credentials The credentials to use for looking up the
     *                    child's information.  Must not be null.
     *
     * @param parent_onode The parent ONode to check against.
     *                     Must not be null.
     *
     * @param child_entry The child OEntry whose parent will be checked
     *                    against the specified parent ONode.
     *                    Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (see above).
     *
     * @throws ONodeOperationException If the child's information cannot
     *                                 be retrieved for some reason,
     *                                 or if it is not a child of the
     *                                 specified parent.
     */
    public void checkParentChild (
                                  Credentials credentials,
                                  ONode parent_onode,
                                  OEntry child_entry
                                  )
        throws I18nIllegalArgumentException,
               ONodeOperationException
    {
        this.module.traceEnter ( "VirtualONode.checkParentChild()" );

        if ( credentials == null
             || parent_onode == null
             || child_entry == null )
        {
            this.module.traceFail ( "VirtualONode.checkParentChild()" );
            throw new I18nIllegalArgumentException ( "Cannot check parent child validity with credentials [%credentials%] for parent ONode [%parent_onode%] child OEntry [%child_entry%]",
                                                     "credentials", credentials,
                                                     "parent_onode", parent_onode,
                                                     "child_entry", child_entry );
        }

        // Check that parent and child are actually parent and child.
        final ONode actual_parent_onode;
        try
        {
            // Just checking for equality, don't need any permissions.
            final OEntry actual_parent_entry = child_entry.parent ();
            actual_parent_onode =
                this.getONodeAndCheckPermissions ( credentials,
						   actual_parent_entry );
        }
        catch ( ONodeOperationException e )
        {
            this.module.traceFail ( "VirtualONode.checkParentChild()" );
            throw e;
        }

        if ( ! actual_parent_onode.equals ( parent_onode ) )
        {
            this.module.traceFail ( "VirtualONode.checkParentChild()" );
            throw new ONodeOperationException ( "OEntry [%child_entry%] does not belong to parent ONode [%parent_onode%] (actually belongs to [%actual_parent_onode%])",
                                                "child_entry", child_entry,
                                                "parent_onode", parent_onode,
                                                "actual_parent_onode", actual_parent_onode );
        }

        this.module.traceExit ( "VirtualONode.checkParentChild()" );
    }


    /**
     * <p>
     * Checks that the specified Credentials have the requested
     * permissions, or throws a security exception if not.
     * </p>
     *
     * @param credentials The credentals to check permissions against.
     *                    Must not be null.
     *
     * @param onode The ONode to check.  Must not be null.
     *
     * @param requested_flags The permissions to check on the
     *                        ONode, such as { RecordPermission.READ,
     *                        RecordFlag.SEQUENTIAL }.  Must not be null.
     *                        Must not contain any null elements.
     *
     * @return The permissions granted.  These permissions are always
     *         a superset of those requested (otherwise a security
     *         exception would be thrown).  Never null.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (see above).
     *
     * @throws ONodeSecurityException If the specified ONode does not
     *                                grant the requested permissions.
     */
    private Permissions<RecordFlag> checkPermissions (
                                                      Credentials credentials,
                                                      ONode onode,
                                                      RecordFlag... requested_flags
                                                      )
        throws ONodeSecurityException
    {
        Permissions<RecordFlag> requested_permissions =
            new RecordPermissions ( credentials,
                                    onode.id (),
                                    requested_flags );

        Permissions<RecordFlag> granted_permissions =
            onode.security ().request ( requested_permissions );
        if ( ! granted_permissions.isAllowed ( requested_permissions ) )
        {
            throw new ONodeSecurityException ( "Access denied: credentials [%credentials%] ONode [%onode%] (requested: [%requested_permissions%] granted: [%granted_permissions%]",
                                               "credentials", credentials,
                                               "onode", onode,
                                               "requested_permissions", requested_permissions,
                                               "granted_permissions", granted_permissions );
        }

        // Done, the permissions have been granted.
        return granted_permissions;
    }


    /**
     * <p>
     * Verifies that the ONode pointed to by the specified OEntry
     * is, in fact, this ONode, then checks the requested
     * permissions.
     * </p>
     *
     * @param credentials The credentals to check permissions against.
     *                    Must not be null.
     *
     * @param entry The OEntry whose ONode shall be checked.
     *              Must not be null.
     *
     * @param requested_flags The permissions to check on the
     *                        ONode, such as { RecordPermission.READ,
     *                        RecordFlag.SEQUENTIAL }.  Must not be null.
     *                        Must not contain any null elements.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (see above).
     *
     * @throws ONodeSecurityException If the specified ONode does not
     *                                grant the requested permissions,
     *                                or if the specified entry does
     *                                not actually point to this ONode,
     *                                or if any other security violation
     *                                occurs (such as the module being
     *                                not allowed to look up the ONode
     *                                by the kernel).
     *
     * @throws ONodeOperationException If the specified ONode cannot
     *                                 be returned (for example, because
     *                                 the OEntry is corrupt).
     */
    private void checkThisONodeAndPermissions (
                                               Credentials credentials,
                                               OEntry entry,
                                               RecordFlag... requested_flags
                                               )
        throws I18nIllegalArgumentException,
               ONodeSecurityException,
               ONodeOperationException
    {
        if ( credentials == null
             || entry == null
             || requested_flags == null
             || entry.onodeRef () == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot check permissions on entry [%entry%] credentials [%credentials%] requested flags [%requested_flags%]",
                                                     "credentials", credentials,
                                                     "entry", entry,
                                                     "requested_flags", requested_flags );
        }

        final ONodeIdentifier onode_ref = entry.onodeRef ();
        if ( onode_ref == null )
        {
            throw new ONodeOperationException ( "OEntry [%entry%] has invalid ONode reference [%onode_ref%] pointing to ONode [%onode%] with security [%security%]",
                                                "entry", entry,
                                                "onode_ref", onode_ref,
                                                "onode", "?",
                                                "security", "?" );
        }

        if ( ! onode_ref.equals ( this.onode.id () ) )
        {
            throw new ONodeSecurityException ( "Expected OEntry reference to ONode [%expected_onode_ref%] but [%entry%] refers to [%actual_onode_ref%]",
                                               "expected_onode_ref", this.onode.id (),
                                                "entry", entry,
                                               "actual_onode_ref", onode_ref );
        }

        this.checkPermissions ( credentials,
                                this.onode,
                                requested_flags );
    }


    /**
     * @see musaico.kernel.objectsystem.onodes.ObjectONode#create(musaico.security.Credentials,musaico.kernel.objectsystem.onode.OEntry,musaico.kernel.objectsystem.RecordTypeIdentifier,java.lang.String,musaico.security.Security,java.lang.Object...)
     */
    @Override
    public OEntry create (
                          Credentials credentials,
                          OEntry entry,
                          RecordTypeIdentifier type,
                          String name,
                          Security<RecordFlag> mode,
                          Object... onode_specific_parameters
                          )
        throws ONodeOperationException,
               I18nIllegalArgumentException
    {
        this.module.traceEnter ( "VirtualONode.create()" );

        if ( entry == null
             || credentials == null
             || type == null
             || name == null
             || mode == null )
        {
            this.module.traceFail ( "VirtualONode.create()" );
            throw new I18nIllegalArgumentException ( "Cannot create child ONode for OEntry [%entry%] Credentials [%credentials%] type [%type%] name [%name%] mode [%mode%] parameters [%onode_specific_parameters%]",
                                                     "entry", entry,
                                                     "credentials", credentials,
                                                     "type", type,
                                                     "name", name,
                                                     "mode", mode,
                                                     "onode_specific_parameters", onode_specific_parameters );
        }

        try
        {
            this.checkThisONodeAndPermissions ( credentials,
                                                entry,
                                                RecordPermission.WRITE,
                                                RecordPermission.EXECUTE );
        }
        catch ( ONodeOperationException e )
        {
            this.module.traceFail ( "VirtualONode.create()" );
            throw e;
        }

        final OEntry child_entry;

        final ONodeLock mutex_lock =
            new ONodeLock ( this.onode.mutex ( credentials ) );
        mutex_lock.lock ();
        try
        {
            // Now create the child ONode.
            if ( ! (this.onode instanceof ObjectONode ) )
            {
                this.module.traceFail ( "VirtualONode.create()" );
                throw new ONodeOperationException ( "ONode [%onode%] at [%entry%] does not support creating flat records, objects, relations or other types of ONodes",
                                                    "onode", this,
                                                    "entry", entry );
            }

	    ObjectONode object_onode = (ObjectONode) this.onode;
            try
            {
                child_entry =
		    object_onode.create ( credentials,
					  entry,
					  type,
					  name,
					  mode,
					  onode_specific_parameters );
            }
            catch ( ONodeOperationException e )
            {
                this.module.traceFail ( "VirtualONode.create()" );
                throw e;
            }

            // Now add links to the newly created ONode so that it is
            // properly referenced from the parent object's OEntry
            // (including reference count).
            try
            {
                this.link ( credentials,
                            child_entry );
            }
            catch ( ONodeOperationException e )
            {
                this.module.traceFail ( "VirtualONode.create()" );
                throw e;
            }

            // Touch the metadata time(s) for the ONode
            // (accessed time, modified time, and so on).
            ONodeMetadata metadata = this.onode.metadata ();
            Time accessed_time = this.module.platform ().now ();
            try
            {
                metadata.writeValue ( credentials,
                                      metadata.metaTimeAccessed (),
                                      accessed_time );
                metadata.writeValue ( credentials,
                                      metadata.metaTimeModified (),
                                      accessed_time );
            }
            catch ( Exception e )
            {
                this.module.traceFail ( "VirtualONode.create()" );
                throw new ONodeOperationException ( "Failed to update times for entry [%entry%] onode [%onode%] to [%time%]",
                                                    "entry", entry,
                                                    "onode", onode,
                                                    "time", accessed_time,
                                                    "cause", e );
            }
        }
        finally
        {
            mutex_lock.unlock ();
        }

        // If the parent OEntry contains symbolic links, we'll
        // need to create a new OEntry for the new child ONode,
        // so that the symbolically linked path is maintained.
        final OEntry path_entry;
        if ( child_entry.parent () == entry )
        {
            path_entry = child_entry;
        }
        else
        {
            path_entry = createCopyOfOEntryWithParent ( entry, child_entry );
        }

        this.module.traceExit ( "VirtualONode.create()" );
        return path_entry;
    }


    /**
     * <p>
     * Create a copy of the specified child_entry under the parent
     * OEntry.
     * </p>
     *
     * <p>
     * Typically this is used to create an OEntry which is NOT stored
     * in the cache, and which contains a symbolic link somewhere in
     * its path.
     * </p>
     *
     * @param parent_entry The parent OEntry in which to create the
     *                     duplicate child.  Must not be null.
     *
     * @param child_entry The child OEntry to duplicate.  Must not be null.
     *
     * @return A newly created OEntry whose parent is the specified
     *         parent_entry.  Never null.
     *
     * @throws I18nIllegalArgumentException If the specified parameters
     *                                      are invalid.  (See above.)
     */
    private OEntry createCopyOfOEntryWithParent (
                                                 OEntry parent_entry,
                                                 OEntry child_entry
                                                 )
        throws I18nIllegalArgumentException
    {
        if ( parent_entry == null
             || child_entry == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot copy OEntry [%child_entry%] into parent [%parent_entry%]",
                                                     "parent_entry", parent_entry,
                                                     "child_entry", child_entry );
        }

        SimpleOEntry copy_entry =
            new SimpleOEntry ( this.module,
                               parent_entry,
                               child_entry.name (),
                               (SuperBlockIdentifier) child_entry.superBlockRef (),
                               child_entry.onodeRef (),
                               child_entry.isMounted () );

        // Copy out the child OEntries (if any -- typically there aren't).
        Sequence<OEntry> grandchildren_to_copy =
            child_entry.children ();
        for ( OEntry grandchild_to_copy : grandchildren_to_copy )
        {
            OEntry grandchild =
                this.createCopyOfOEntryWithParent ( copy_entry,
                                                    grandchild_to_copy );
            try
            {
                copy_entry.addChild ( this.module.credentials (),
                                      grandchild );
            }
            catch ( OEntryOperationException e )
            {
                // Unexpected!  The copy_entry was immutable or
                // something...  So the OEntry tree from something
                // like a symbolic link or a union is incomplete,
                // but the underlying ONodes are still OK.
                // Log it but carry on.
                this.module.logger ().log ( Level.ERROR,
                                            Locale.getDefault (),
                                            "Failed to child to parent OEntry [%parent_entry%] with child OEntry [%child_entry%]",
                                            "parent_entry", copy_entry,
                                            "child_entry", grandchild,
                                            "cause", e );
            }
        }

        return copy_entry;
    }


    /**
     * <p>
     * Returns the ONode from the specified OEntry, after checking
     * the requested permissions.
     * </p>
     *
     * @param credentials The credentals to check permissions against.
     *                    Must not be null.
     *
     * @param entry The OEntry whose ONode shall be retrieved.
     *              Must not be null.
     *
     * @param requested_flags The permissions to check on the
     *                        ONode, such as { RecordPermission.READ,
     *                        RecordFlag.SEQUENTIAL }.  Must not be null.
     *                        Must not contain any null elements.
     *
     * @return The specified ONode, if the specified permissions are
     *         granted.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (see above).
     *
     * @throws ONodeSecurityException If the specified ONode does not
     *                                grant the requested permissions,
     *                                or if any other security violation
     *                                occurs (such as the module being
     *                                not allowed to look up the ONode
     *                                by the kernel).
     *
     * @throws ONodeOperationException If the specified ONode cannot
     *                                 be returned (for example, because
     *                                 the OEntry is corrupt).
     */
    private ONode getONodeAndCheckPermissions (
                                               Credentials credentials,
                                               OEntry entry,
                                               RecordFlag... requested_flags
                                               )
        throws I18nIllegalArgumentException,
               ONodeSecurityException,
               ONodeOperationException
    {
        if ( credentials == null
             || entry == null
             || requested_flags == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot check permissions on entry [%entry%] credentials [%credentials%] requested flags [%requested_flags%]",
                                                     "credentials", credentials,
                                                     "entry", entry,
                                                     "requested_flags", requested_flags );
        }

        final ONodeIdentifier onode_ref = entry.onodeRef ();
        if ( onode_ref == null )
        {
            throw new ONodeOperationException ( "OEntry [%entry%] has invalid ONode reference [%onode_ref%] pointing to ONode [%onode%] with security [%security%]",
                                                "entry", entry,
                                                "onode_ref", onode_ref,
                                                "onode", "?",
                                                "security", "?" );
        }

        final ONode onode;
        try
        {
            onode = this.module.getKernelObject ( onode_ref );
        }
        catch ( ModuleOperationException e )
        {
            throw new ONodeOperationException ( "Access denied: Module exception while retrieving ONode reference [%onode_ref%] with security [%security%]",
                                                "onode_ref", onode_ref,
                                                "security", "?",
                                                "cause", e );
        }

        if ( onode == null
             || onode.security () == null )
        {
            throw new ONodeOperationException ( "OEntry [%entry%] has invalid ONode reference [%onode_ref%] pointing to ONode [%onode%] with security [%security%]",
                                                "entry", entry,
                                                "onode_ref", onode_ref,
                                                "onode", onode,
                                                "security", ( onode == null ? "?" : onode.security () ) );
        }

        this.checkPermissions ( credentials,
                                this.onode,
                                requested_flags );

        return onode;
    }


    /**
     * @see musaico.kernel.objectsystem.onode.ONode#data()
     */
    @Override
    public Record data ()
    {
        return this.onode.data ();
    }


    /**
     * @see musaico.kernel.objectsystem.onode.ONode#id()
     */
    @Override
    public ONodeIdentifier id ()
    {
        return this.onode.id ();
    }


    /**
     * @see musaico.kernel.objectsystem.onode.ONode#link(musaico.security.Credentials,musaico.kernel.objectsystem.onode.OEntry)
     */
    @Override
    public long link (
                      Credentials credentials,
                      OEntry entry
                      )
        throws ONodeOperationException,
               I18nIllegalArgumentException
    {
        this.module.traceEnter ( "VirtualONode.link()" );

        if ( credentials == null
	     || entry == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot link OEntry [%entry%] to target ONode with id [%target_onode_ref%] with credentials [%credentials%]",
                                                     "entry", entry,
                                                     "credentials", credentials,
                                                     "target_onode_ref", this.onode.id () );
        }

        final long num_references_to_onode;

        final ONodeLock mutex_lock =
            new ONodeLock ( this.onode.mutex ( credentials ) );
        mutex_lock.lock ();
        try
        {
            // Let the ONode itself handle the link in its own way.
            try
            {
                this.onode.link ( credentials,
                                  entry );
            }
            catch ( ONodeOperationException e )
            {
                this.module.traceFail ( "VirtualONode.link()" );
                throw e;
            }

            // The specified entry is a hard link to the ONode.
            // Increment the ONode's hard link reference count.
            final long num_hard_links =
                this.onode.references ().hardLinks ( credentials )
                .increment ();

            num_references_to_onode = this.onode.references ().count ();

            // link () does not modify any of the ONode's metadata times.
        }
        finally
        {
            mutex_lock.unlock ();
        }

        this.module.traceExit ( "VirtualONode.link()" );

        return num_references_to_onode;
    }


    /**
     * @see musaico.kernel.objectsystem.onode.ONode#lookup(musaico.security.Credentials,musaico.kernel.objectsystem.onode.OEntry,musaico.io.Path)
     */
    @Override
    public OEntry lookup (
                          Credentials credentials,
                          OEntry entry,
                          Path path
                          )
        throws ONodeOperationException,
               I18nIllegalArgumentException
    {
        this.module.traceEnter ( "VirtualONode.lookup()" );

        if ( entry == null
             || credentials == null
             || path == null )
        {
            this.module.traceFail ( "VirtualONode.lookup()" );
            throw new I18nIllegalArgumentException ( "Cannot lookup child OEntry in parent [%parent_entry%] Credentials [%credentials%] at path [%path%]",
                                                     "parent_entry", entry,
                                                     "credentials", credentials,
                                                     "path", path );
        }

        final OEntry parent_entry;
        if ( path.isAbsolute () )
        {
            // Get the root OEntry by its absolute path.
            try
            {
                parent_entry =
                    this.module.getKernelObject ( OEntryIdentifier.ROOT );
            }
            catch ( ModuleOperationException e )
            {
                this.module.traceFail ( "VirtualONode.lookup()" );
                throw new ONodeOperationException ( "Access denied: Module exception while retrieving root OEntry",
                                                    "cause", e );
            }
        }
        else
        {
            parent_entry = entry;
        }

        final ONode onode;
        try
        {
            // We have to be able to read and execute the ONode whose child or
            // grandchild has been requested.
            onode =
                this.getONodeAndCheckPermissions ( credentials,
                                                   entry,
                                                   RecordPermission.READ,
                                                   RecordPermission.EXECUTE );
        }
        catch ( ONodeOperationException e )
        {
            this.module.traceFail ( "VirtualONode.lookup()" );
            throw e;
        }

        // If the path is empty, just return this OEntry.
        if ( path.depth () == 0 )
        {
            this.module.traceExit ( "VirtualONode.lookup()" );
            return entry;
        }

        // Now step through the path until we find the entry
        // we're looking for.
        OEntry looked_up_entry = parent_entry;

        final ONodeLock mutex_lock =
            new ONodeLock ( this.onode.mutex ( credentials ) );
        mutex_lock.lock ();
        try
        {
            String [] path_names = path.names ();
            OEntry current_parent_entry = parent_entry;

            for ( int p = 0; p < path_names.length; p ++ )
            {
                String name = path_names [ p ];

                try
                {
                    looked_up_entry = current_parent_entry.child ( name );
                }
                catch ( OEntryOperationException e )
                {
                    this.module.traceFail ( "VirtualONode.lookup()" );
                    throw new ONodeOperationException ( "No such OEntry [%path%] sub-path [%sub-path%] under [%parent_entry%]",
                                                        "path", path,
                                                        "sub_path", name,
                                                        "parent_entry", current_parent_entry,
                                                        "cause", e );
                }
            }

            // Touch the metadata time(s) for the ONode
            // (accessed time, modified time, and so on).
            ONodeMetadata metadata = this.onode.metadata ();
            Time accessed_time = this.module.platform ().now ();
            try
            {
                metadata.writeValue ( credentials,
                                      metadata.metaTimeAccessed (),
                                      accessed_time );
            }
            catch ( Exception e )
            {
                this.module.traceFail ( "VirtualONode.lookup()" );
                throw new ONodeOperationException ( "Failed to update times for entry [%entry%] onode [%onode%] to [%time%]",
                                                    "entry", entry,
                                                    "onode", onode,
                                                    "time", accessed_time,
                                                    "cause", e );
            }
        }
        finally
        {
            mutex_lock.unlock ();
        }

        this.module.traceExit ( "VirtualONode.lookup()" );

        return looked_up_entry;
    }


    /**
     * @see musaico.kernel.objectsystem.onode.ONode#metadata()
     */
    @Override
    public ONodeMetadata metadata ()
    {
        return this.onode.metadata ();
    }


    /**
     * @see musaico.kernel.objectsystem.onode.ONode#move(musaico.security.Credentials,musaico.kernel.objectsystem.onode.OEntry,musaico.kernel.objectsystem.onode.OEntry)
     */
    @Override
    public void move (
                      Credentials credentials,
                      OEntry old_entry,
                      OEntry new_entry
                      )
        throws ONodeOperationException,
               I18nIllegalArgumentException
    {
        this.module.traceEnter ( "VirtualONode.move()" );

        if ( old_entry == null
             || credentials == null
             || new_entry == null )
        {
            this.module.traceFail ( "VirtualONode.move()" );
            throw new I18nIllegalArgumentException ( "Cannot move OEntry [%old_entry%] with credentials [%credentials%] to [%new_entry%]",
                                                     "old_entry", old_entry,
                                                     "credentials", credentials,
                                                     "new_entry", new_entry );
        }

        final OEntry old_parent_entry = old_entry.parent ();
        final OEntry new_parent_entry = new_entry.parent ();
        final ONode old_parent_onode;
        final ONode new_parent_onode;
        try
        {
            // We have to be able to:
            //   - write and execute the old parent ONode
            //   - write and execute the new parent ONode
            //   - write the new child ("victim") ONode
            // Even if the child is an hierarchical object, we don't
            // need execute permissions on it; just write permissions.
            old_parent_onode =
                this.getONodeAndCheckPermissions ( credentials,
                                                   old_parent_entry,
                                                   RecordPermission.WRITE,
                                                   RecordPermission.EXECUTE );
            new_parent_onode =
                this.getONodeAndCheckPermissions ( credentials,
                                                   new_parent_entry,
                                                   RecordPermission.WRITE,
                                                   RecordPermission.EXECUTE );
	    // Our (child) ONode:
	    this.getONodeAndCheckPermissions ( credentials,
					       old_entry,
					       RecordPermission.WRITE );
        }
        catch ( ONodeOperationException e )
        {
            this.module.traceFail ( "VirtualONode.move()" );
            throw e;
        }

        // If the child is not an hierarchical object, we'll need to link ".."
        // to its new parent ONode.
        final boolean is_child_hierarchical_object = false;

        final ONodeLock mutex_lock;
        if ( old_parent_onode == new_parent_onode )
        {
            // Rename.
            // The old parent and the new parent are one and the same.
            mutex_lock =
                new ONodeLock ( old_parent_onode.mutex ( credentials ),
                                this.onode.mutex ( credentials ) );
        }
        else
        {
            // Move.
            // The old parent and the new parent are different.
            mutex_lock =
                new ONodeLock ( old_parent_onode.mutex ( credentials ),
                                new_parent_onode.mutex ( credentials ),
                                this.onode.mutex ( credentials ) );
        }

        mutex_lock.lock ();
        try
        {
            // Throw an exception if the specified child isn't actually
            // the child of the specified parent.
            try
            {
                this.checkParentChild ( credentials,
                                        old_parent_onode,
                                        old_entry );
            }
            catch ( ONodeOperationException e )
            {
                this.module.traceFail ( "VirtualONode.move()" );
                throw e;
            }

            // Now move the child node out of the old parent.
            // The underlying ObjectONode takes care of moving ".."
            // and so on. GenericONodeOperations doesn't do much, but
            // some object systems may allow only writing, not moving.
            // We'll handle the references from the parent ONodes
            // to the child, the delegated method just needs to do
            // special stuff (like move ".." for hierarchical object
            // nodes).
            try
            {
                this.onode.move ( credentials,
                                  old_entry,
                                  new_entry );
            }
            catch ( ONodeOperationException e )
            {
                this.module.traceFail ( "VirtualONode.move()" );
                throw e;
            }

            // If the parent has changed, remove the hard link
            // from the old parent to the child, and add a new
            // hard link from the new parent to the child.
            if ( new_parent_onode != old_parent_onode )
            {
                try
                {
                    // The parent ONode's child entry refers to the
                    // child object.
                    this.unlink ( credentials,
                                  old_entry );
                }
                catch ( ONodeOperationException e )
                {
                    this.module.traceFail ( "VirtualONode.move()" );
                    throw e;
                }

                try
                {
                    // The new parent ONode's child entry refers to the
                    // child object.
                    this.link ( credentials,
                                new_entry );
                }
                catch ( ONodeOperationException e )
                {
                    // Uh oh.  Better try to restore the old parent's
                    // link, don't want to leave the poor child
                    // lost, cold and alone in the middle of nowhere.
                    // Maybe someone someday will invent virtual leashes,
                    // like those ones you see at shopping malls,
                    // tired moms absent-mindedly yanking Junior by
                    // the wrist while they window shop for crap they
                    // don't need.
                    try
                    {
                        this.link ( credentials,
				    old_entry );
                    }
                    catch ( ONodeOperationException e2 )
                    {
                        // Rubbish.
                        // The link from the old parent was removed.
                        // The link from the new parent FAILED.
                        // Re-linking the child to the old parent ALSO FAILED.
                        // Corrupt state - the child is now in limbo.
                        // !!! WHAT TO DO?
                    }

                    this.module.traceFail ( "VirtualONode.move()" );
                    throw e;
                }
            }

            // Touch the metadata time(s) for the ONodes
            // (accessed time, modified time, and so on).
            ONodeMetadata old_parent_metadata = old_parent_onode.metadata ();
            ONodeMetadata new_parent_metadata = new_parent_onode.metadata ();
            ONodeMetadata child_metadata = this.onode.metadata ();
            Time accessed_time = this.module.platform ().now ();
            try
            {
                old_parent_metadata.writeValue ( credentials,
                                                 old_parent_metadata.metaTimeAccessed (),
                                                 accessed_time );
                old_parent_metadata.writeValue ( credentials,
                                                 old_parent_metadata.metaTimeModified (),
                                                 accessed_time );
                if ( old_parent_onode != new_parent_onode )
                {
                    new_parent_metadata.writeValue ( credentials,
                                                     new_parent_metadata.metaTimeAccessed (),
                                                     accessed_time );
                    new_parent_metadata.writeValue ( credentials,
                                                     new_parent_metadata.metaTimeModified (),
                                                     accessed_time );
                }
                child_metadata.writeValue ( credentials,
                                            child_metadata.metaTimeAccessed (),
                                            accessed_time );
                child_metadata.writeValue ( credentials,
                                            child_metadata.metaTimeModified (),
                                            accessed_time );
            }
            catch ( Exception e )
            {
                this.module.traceFail ( "VirtualONode.move()" );
                throw new ONodeOperationException ( "Failed to update times for entry [%entry%] onode [%onode%] to [%time%]",
                                                    "entry", new_entry,
                                                    "onode", this.onode,
                                                    "time", accessed_time,
                                                    "cause", e );
            }
        }
        finally
        {
            mutex_lock.unlock ();
        }

        this.module.traceExit ( "VirtualONode.move()" );
    }


    /**
     * @see musaico.kernel.objectsystem.onode.ONode#mutex(musaico.security.Credentials)
     */
    @Override
    public Mutex mutex (
                        Credentials credentials
                        )
        throws ONodeSecurityException
    {
        this.checkPermissions ( credentials,
                                this.onode,
                                RecordPermission.LOCK );

        return this.onode.mutex ( credentials );
    }


    /**
     * @see musaico.kernel.objectsystem.onodes.ObjectONode#readChildObject(musaico.security.Credentials,musaico.kernel.objectsystem.onode.OEntry)
     */
    @Override
    public void readObject (
                            Credentials credentials,
                            OEntry entry
                            )
        throws I18nIllegalArgumentException,
               ONodeOperationException
    {
        this.module.traceEnter ( "VirtualONode.readObject()" );

        if ( ! ( this.onode instanceof ObjectONode ) )
        {
            this.module.traceFail ( "VirtualONode.readObject()" );
            throw new ONodeOperationException ( "ReadObject not supported by ONode [%onode%] at [%entry%]",
                                                "onode", this.onode,
                                                "entry", entry );
        }

        // Check permissions to read this ObjectONode.
        try
        {
            this.checkThisONodeAndPermissions ( credentials,
                                                entry,
                                                RecordPermission.READ,
                                                RecordPermission.EXECUTE );
        }
        catch ( ONodeOperationException e )
        {
            this.module.traceFail ( "VirtualONode.readObject()" );
            throw e;
        }

        ObjectONode object_onode = (ObjectONode) this.onode;

        try
        {
            object_onode.readObject ( credentials, entry );
        }
        catch ( I18nIllegalArgumentException e )
        {
            this.module.traceFail ( "VirtualONode.readObject()" );
            throw e;
        }
        catch ( ONodeOperationException e )
        {
            this.module.traceFail ( "VirtualONode.readObject()" );
            throw e;
        }

        this.module.traceExit ( "VirtualONode.readObject()" );
    }


    /**
     * @see musaico.kernel.objectsystem.onode.ONode#references()
     */
    @Override
    public ONodeReferences references ()
    {
        return this.onode.references ();
    }


    /**
     * @see musaico.kernel.objectsystem.onodes.Relation#relatives(musaico.security.Credentials,musaico.kernel.objectsystem.onode.OEntry)
     */
    @Override
    public Sequence<Path> relatives (
                                     Credentials credentials,
                                     OEntry entry
                                     )
        throws I18nIllegalArgumentException,
               ONodeOperationException
    {
        this.module.traceEnter ( "VirtualONode.relatives()" );

        if ( ! ( this.onode instanceof Relation ) )
        {
            this.module.traceFail ( "VirtualONode.relatives()" );
            throw new ONodeOperationException ( "Relatives not supported by ONode [%onode%] at [%entry%]",
                                                "onode", this.onode,
                                                "entry", entry );
        }

        // Check permissions to read the relatives of this Relation.
        try
        {
            this.checkThisONodeAndPermissions ( credentials,
                                                entry,
                                                RecordPermission.READ,
                                                RecordPermission.EXECUTE );
        }
        catch ( ONodeOperationException e )
        {
            this.module.traceFail ( "VirtualONode.relatives()" );
            throw e;
        }

        Relation relation = (Relation) this.onode;
        final Sequence<Path> relatives;

        try
        {
            relatives = relation.relatives ( credentials, entry );
        }
        catch ( I18nIllegalArgumentException e )
        {
            this.module.traceFail ( "VirtualONode.relatives()" );
            throw e;
        }
        catch ( ONodeOperationException e )
        {
            this.module.traceFail ( "VirtualONode.relatives()" );
            throw e;
        }

        this.module.traceExit ( "VirtualONode.relatives()" );

        return relatives;
    }


    /**
     * @see musaico.kernel.objectsystem.onodes.ObjectONode#removeChildObject(musaico.security.Credentials,musaico.kernel.objectsystem.onode.OEntry,musaico.kernel.objectsystem.onode.OEntry)
     */
    @Override
    public void removeChildObject (
                                   Credentials credentials,
                                   OEntry parent_entry,
                                   OEntry child_entry
                                   )
        throws ONodeOperationException,
               I18nIllegalArgumentException
    {
        this.module.traceEnter ( "VirtualONode.removeChildObject()" );

        if ( parent_entry == null
             || credentials == null
             || child_entry == null )
        {
            this.module.traceFail ( "VirtualONode.removeChildObject()" );
            throw new I18nIllegalArgumentException ( "Cannot remove object for Credentials [%credentials%] from parent OEntry [%parent_entry%] child OEntry [%child_entry%]",
                                                     "credentials", credentials,
                                                     "parent_entry", parent_entry,
                                                     "child_entry", child_entry );
        }

        // Check this parent ONode.
        try
        {
            this.checkThisONodeAndPermissions ( credentials,
                                                parent_entry,
                                                RecordPermission.WRITE,
                                                RecordPermission.EXECUTE );
        }
        catch ( ONodeOperationException e )
        {
            this.module.traceFail ( "VirtualONode.removeChildObject()" );
            throw e;
        }

        // Get the child ONode and check permissions.
        final ONode child_onode;
        try
        {
            // No permissions currently required on the child node.
	    child_onode = this.getONodeAndCheckPermissions ( credentials,
                                                             child_entry );
        }
        catch ( ONodeOperationException e )
        {
            this.module.traceFail ( "VirtualONode.removeChildObject()" );
            throw e;
        }

        final ONodeLock mutex_lock =
            new ONodeLock ( this.onode.mutex ( credentials ),
                            child_onode.mutex ( credentials ) );
        mutex_lock.lock ();
        try
        {
            // Throw an exception if the specified child isn't actually
            // the child of the specified parent.
            try
            {
                this.checkParentChild ( credentials,
                                        this.onode, child_entry );
            }
            catch ( ONodeOperationException e )
            {
                this.module.traceFail ( "VirtualONode.removeChildObject()" );
                throw e;
            }

	    // The underlying ONode must be an ObjectONode.
            if ( ! ( this.onode instanceof ObjectONode )
                 || ! ( child_onode instanceof ObjectONode ) )
            {
                // Either the parent ONode implementation or the child
                // is not an hierarchical object -- and we can only
                // perform this operation on object parent and object
                // child, so throw an exception.
                this.module.traceFail ( "VirtualONode.removeChildObject()" );
                throw new ONodeOperationException ( "RemoveChildObject not supported by parent ONode [%parent_onode%] at [%parent_entry%] while trying to remove child ONode [%child_onode%] at [%child_entry%]",
                                                    "parent_onode", this.onode,
                                                    "parent_entry", parent_entry,
                                                    "child_onode", child_onode,
						    "child_entry", child_entry );
            }

            // Now let the implementation do the hard work of
            // removing the data pointing to the child ONode.
            ObjectONode object_onode = (ObjectONode) this.onode;
            try
            {
		object_onode.removeChildObject ( credentials,
                                                 parent_entry,
                                                 child_entry );
            }
            catch ( ONodeOperationException e )
            {
                this.module.traceFail ( "VirtualONode.removeChildObject()" );
                throw e;
            }

            // Now remove the hard links from parent to child and from
            // child to parent.
            try
            {
                // A child entry in the parent is hard linked to this
                // object ONode, so sever the link.
                for ( OEntry a_child_entry : parent_entry.children () )
                {
                    if ( a_child_entry.onodeRef ().equals ( child_onode.id () ) )
                    {
                        child_onode.unlink ( credentials,
                                             a_child_entry );
                    }
                }

                // Remove links from the child's children to the parent
                // ONode (such as "..").
                for ( OEntry a_grandchild_entry : child_entry.children () )
                {
                    if ( a_grandchild_entry.onodeRef ().equals ( this.onode.id () ) )
                    {
                        this.onode.unlink ( credentials,
                                            a_grandchild_entry );
                    }
                }
            }
            catch ( ONodeOperationException e )
            {
                this.module.traceFail ( "VirtualONode.removeChildObject()" );
                throw e;
            }

            // Touch the metadata time(s) for the ONode
            // (accessed time, modified time, and so on).
            ONodeMetadata parent_metadata = this.onode.metadata ();
            Time accessed_time = this.module.platform ().now ();
            try
            {
                parent_metadata.writeValue ( credentials,
                                             parent_metadata.metaTimeAccessed (),
                                             accessed_time );
                parent_metadata.writeValue ( credentials,
                                             parent_metadata.metaTimeModified (),
                                             accessed_time );
            }
            catch ( Exception e )
            {
                this.module.traceFail ( "VirtualONode.removeChildObject()" );
                throw new ONodeOperationException ( "Failed to update times for entry [%entry%] onode [%onode%] to [%time%]",
                                                    "entry", parent_entry,
                                                    "onode", this.onode,
                                                    "time", accessed_time,
                                                    "cause", e );
            }

            ONodeMetadata child_metadata = child_onode.metadata ();
            try
            {
                child_metadata.writeValue ( credentials,
                                            child_metadata.metaTimeAccessed (),
                                            accessed_time );
                child_metadata.writeValue ( credentials,
                                            child_metadata.metaTimeDeleted (),
                                            accessed_time );
            }
            catch ( Exception e )
            {
                this.module.traceFail ( "VirtualONode.removeChildObject()" );
                throw new ONodeOperationException ( "Failed to update times for entry [%entry%] onode [%onode%] to [%time%]",
                                                    "entry", child_entry,
                                                    "onode", child_onode,
                                                    "time", accessed_time,
                                                    "cause", e );
            }
        }
        finally
        {
            mutex_lock.unlock ();
        }

        this.module.traceExit ( "VirtualONode.removeChildObject()" );
    }


    /**
     * @see musaico.kernel.objectsystem.onodes.Relation#resolveRelation(musaico.security.Credentials,musaico.kernel.objectsystem.onode.OEntry)
     */
    @Override
    public OEntry resolveRelation (
				   Credentials credentials,
				   OEntry entry
				   )
        throws ONodeOperationException,
               I18nIllegalArgumentException
    {
        this.module.traceEnter ( "VirtualONode.resolveRelation()" );

        if ( entry == null
             || credentials == null )
        {
            this.module.traceFail ( "VirtualONode.resolveRelation()" );
            throw new I18nIllegalArgumentException ( "Cannot read relation ONode with OEntry [%entry%] Credentials [%credentials%]",
                                                     "entry", entry,
                                                     "credentials", credentials );
        }

        final ONode onode;
        try
        {
            onode =
                this.getONodeAndCheckPermissions ( credentials,
                                                   entry,
                                                   RecordPermission.READ );
        }
        catch ( ONodeOperationException e )
        {
            this.module.traceFail ( "VirtualONode.resolveRelation()" );
            throw e;
        }

        final OEntry resolved_entry;

        final ONodeLock mutex_lock =
            new ONodeLock ( this.onode.mutex ( credentials ) );
        mutex_lock.lock ();
        try
        {
            // Call the ONode's resolveRelation().
            // Typically the GenericONode are used,
            // and they invoke Relation.resolveRelation().
            if ( ! ( this.onode instanceof Relation ) )
            {
                this.module.traceFail ( "VirtualONode.resolveRelation()" );
                throw new ONodeOperationException ( "ReadRelation not supported by ONode [%onode%] at [%entry%]",
                                                    "onode", this.onode,
                                                    "entry", entry );
            }

            Relation relation = (Relation) this.onode;
            try
            {
                resolved_entry =
		    relation.resolveRelation ( credentials, entry );
            }
            catch ( ONodeOperationException e )
            {
                this.module.traceFail ( "VirtualONode.resolveRelation()" );
                throw e;
            }

            // Touch the metadata time(s) for the ONode
            // (accessed time, modified time, and so on).
            ONodeMetadata metadata = this.onode.metadata ();
            Time accessed_time = this.module.platform ().now ();
            try
            {
                metadata.writeValue ( credentials,
                                      metadata.metaTimeAccessed (),
                                      accessed_time );
            }
            catch ( Exception e )
            {
                this.module.traceFail ( "VirtualONode.resolveRelation()" );
                throw new ONodeOperationException ( "Failed to update times for entry [%entry%] onode [%onode%] to [%time%]",
                                                    "entry", entry,
                                                    "onode", onode,
                                                    "time", accessed_time,
                                                    "cause", e );
            }
        }
        finally
        {
            mutex_lock.unlock ();
        }

        this.module.traceExit ( "VirtualONode.resolveRelation()" );

        return resolved_entry;
    }


    /**
     * @see musaico.kernel.objectsystem.onode.ONode#security()
     */
    @Override
    public Security security ()
    {
        return this.onode.security ();
    }


    /**
     * @see musaico.kernel.objectsystem.onode.ONode#superBlockRef()
     */
    @Override
    public SuperBlockIdentifier superBlockRef ()
    {
        return (SuperBlockIdentifier) this.onode.superBlockRef ();
    }


    /**
     * @see musaico.kernel.objectsystem.onode.ONode#truncate(musaico.security.Credentials,musaico.kernel.objectsystem.onode.OEntry,musaico.io.Region)
     */
    @Override
    public void truncate (
                          Credentials credentials,
                          OEntry entry,
                          Region truncate_everything_outside_of_region
                          )
        throws ONodeOperationException,
               I18nIllegalArgumentException
    {
        this.module.traceEnter ( "VirtualONode.truncate()" );

        if ( entry == null
             || credentials == null
             || truncate_everything_outside_of_region == null )
        {
            this.module.traceFail ( "VirtualONode.truncate()" );
            throw new I18nIllegalArgumentException ( "Cannot truncate OEntry [%entry%] with credentials [%credentials%] to region [%truncate_everything_outside_of_region%]",
                                                     "entry", entry,
                                                     "credentials", credentials,
                                                     "truncate_everything_outside_of_region", truncate_everything_outside_of_region );
        }

        final ONode onode;
        try
        {
            onode =
                this.getONodeAndCheckPermissions ( credentials,
                                                   entry,
                                                   RecordPermission.WRITE );
        }
        catch ( ONodeOperationException e )
        {
            this.module.traceFail ( "VirtualONode.truncate()" );
            throw e;
        }

        final ONodeLock mutex_lock =
            new ONodeLock ( this.onode.mutex ( credentials ) );
        mutex_lock.lock ();
        try
        {
            // GenericONode sets a region field in the
            // metadata, and releases all the pages outside the
            // owned region / fills in blanks to expand the
            // old region to the new size, and moves the StartRecord and
            // EndRecord fields to cover the new region.
            // Other implementations may or may not support
            // truncating.
            // See mm/truncate.c in Linux.  I think maybe only
            // swap space can be truncated using the kernel function...???
            // Not sure, but nowhere else is the inode_operations->truncate
            // method implemented.
            try
            {
                this.onode.truncate ( credentials, entry,
				      truncate_everything_outside_of_region );
            }
            catch ( ONodeOperationException e )
            {
                this.module.traceFail ( "VirtualONode.truncate()" );
                throw e;
            }

            // Touch the metadata time(s) for the ONode
            // (accessed time, modified time, and so on).
            ONodeMetadata metadata = this.onode.metadata ();
            Time accessed_time = this.module.platform ().now ();
            try
            {
                metadata.writeValue ( credentials,
                                      metadata.metaTimeAccessed (),
                                      accessed_time );
                metadata.writeValue ( credentials,
                                      metadata.metaTimeModified (),
                                      accessed_time );
            }
            catch ( Exception e )
            {
                this.module.traceFail ( "VirtualONode.truncate()" );
                throw new ONodeOperationException ( "Failed to update times for entry [%entry%] onode [%onode%] to [%time%]",
                                                    "entry", entry,
                                                    "onode", onode,
                                                    "time", accessed_time,
                                                    "cause", e );
            }
        }
        finally
        {
            mutex_lock.unlock ();
        }

        this.module.traceExit ( "VirtualONode.truncate()" );
    }


    /**
     * @see musaico.kernel.objectsystem.onode.ONode#unlink(musaico.security.Credentials,musaico.kernel.objectsystem.onode.OEntry)
     */
    @Override
    public long unlink (
                        Credentials credentials,
                        OEntry entry
                        )
        throws ONodeOperationException,
               I18nIllegalArgumentException
    {
        this.module.traceEnter ( "VirtualONode.unlink()" );

        if ( entry == null
             || credentials == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot unlink OEntry [%entry%] from target ONode with id [%target_onode_ref%] with credentials [%credentials%]",
                                                     "entry", entry,
                                                     "credentials", credentials,
                                                     "target_onode_ref", this.onode.id () );
        }

        final long num_references_to_onode;

        final ONodeLock mutex_lock =
            new ONodeLock ( this.onode.mutex ( credentials ) );
        mutex_lock.lock ();
        try
        {
            // Let the ONode itself handle the unlink, if it needs
	    // to do anything special.
	    try
            {
		this.onode.unlink ( credentials,
				    entry );
	    }
	    catch ( ONodeOperationException e )
            {
		this.module.traceFail ( "VirtualONode.unlink()" );
		throw e;
	    }

            // The specified entry is a hard link to the specified ONode.
            // Decrement the ONode's hard link reference count.
            final long num_hard_links =
                this.onode.references ().hardLinks ( credentials )
                .decrement ();

            num_references_to_onode = this.onode.references ().count ();

            // unlink () does not modify any of the ONode's metadata times.
        }
        finally
        {
            mutex_lock.unlock ();
        }

        this.module.traceExit ( "VirtualONode.unlink()" );

        return num_references_to_onode;
    }
}
