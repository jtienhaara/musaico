package musaico.kernel.objectsystem.onodes;

import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.kernel.objectsystem.RecordFlag;
import musaico.kernel.objectsystem.RecordTypeIdentifier;

import musaico.kernel.objectsystem.onode.OEntry;
import musaico.kernel.objectsystem.onode.ONodeOperationException;
import musaico.kernel.objectsystem.onode.ONodeSecurityException;

import musaico.security.Credentials;
import musaico.security.Security;


/**
 * <p>
 * Record for accessing and maintaining the child ONodes of
 * an hierarchical object.
 * </p>
 *
 * <p>
 * This record is responsible for maintaining the links and
 * "dot entries" (".", "..") in the object ONode.
 * </p>
 *
 *
 * <p>
 * Because object systems can conceivably be distributed, every ObjectChildren
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
 * Copyright (c) 2012 Johann Tienhaara
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
public interface ObjectChildren
    extends Serializable
{
    /**
     * <p>
     * Adds the specified ONode to this children record,
     * and performs all linking operations necessary
     * (including linking the new child's ".." entry to
     * the parent ONode, if the child is an hierarchical object).
     * </p>
     *
     * @param credentials Who is asking for this operation
     *                    to be performed.  Must have append and
     *                    write access for this ONode.
     *                    Must not be null.
     *
     * @param child_entry The OEntry pointing to the child ONode
     *                    to be added to this children
     *                    record.  Must be un-linked from its
     *                    previous parent BEFORE adding it to this
     *                    record.  Must not be null.
     *
     * @throws ONodeSecurityException If the specified credentials
     *                                do not have permission to
     *                                perform this operation.
     *
     * @throws ONodeOperationException If the specified child entry
     *                                 cannot be added to this
     *                                 children Record, for example
     *                                 because it belongs to another
     *                                 object system altogether.
     */
    public abstract void addChild (
                                   Credentials credentials,
                                   OEntry child_entry
                                   )
        throws ONodeSecurityException,
               ONodeOperationException;


    /**
     * <p>
     * Creates a new child ONode with the specified name and type.
     * </p>
     *
     * <p>
     * The new child's ONode type specifies one of:
     * </p>
     *
     * <ul>
     *   <li> A flat ONode (a set or sequence of Fields with no
     *        child records or objects of its own).
     *        Required ONode type-specific parameters: None. </li>
     *
     *   <li> An object (a set or sequence of child flat ONodes
     *        and/or objects, with no Fields).
     *        Required ONode type-specific parameters: None. </li>
     *
     *   <li> A relation (which relates one ONode to one or more
     *        others).
     *        Required ONode type-specific parameters:
     *        One or more <code> Path </code>s to
     *        related ONodes.  (For example, a SymbolicLink
     *        requires one Path, whereas a union might
     *        require two or more Paths.) </li>
     *
     *   <li> A Driver ONode (such as /dev/foo pointing to a
     *        FooDriver).
     *        Required ONode type-specific parameters:
     *        The Driver backing the new ONode to be created. </li>
     * </ul>
     *
     * <p>
     * Depending on the ONode, and and also depending on the makeup
     * of this object system, a different flavour of ONode will be created.
     * </p>
     *
     * <p>
     * For example, an hierarchical object
     * from one object system might have many Relation types
     * to choose from (SymbolicLinks and others),
     * and another object system might disallow Relations
     * altogether but allow other types of ONodes to be
     * created.
     * </p>
     *
     * <p>
     * Once complete, the ObjectOnode to which this children record
     * belongs will be <code> link </code>ed to the child ONode.
     * If the newly created child is an ObjectONode then it will
     * have a "." entry <code> link </code>ed to itself, as well
     * as a ".." entry <code> link </code>ed to the parent ONode.
     * </p>
     *
     * @see musaico.kernel.objectsystem.ONode#link(musaico.security.Credentials,musaico.kernel.objectsystem.onode.OEntry)
     *
     * <p>
     * An child OEntry pointing to the new child ONode is also
     * created, with the specified name, and the new OEntry
     * is returned.
     * </p>
     *
     * <p>
     * Note: In Linux the equivalent method would be
     * <code> create () </code> or <code> mkdir () </code>
     * or <code> symlink () </code> or <code> mknod () </code>
     * and so on from the inode structure.  However Linux only returns
     * a disconnected dentry (with no inode), and the caller
     * has to call <code> d_instantiate ( dentry, inode ) </code>
     * to attach an inode to the dentry.  This seems dangerous
     * and I can't think of any good reason for Musaico to delay
     * attaching the OEntry and ONode, so we are deliberately
     * munging together two Linux concepts (create + d_instantiate)
     * into one here.  (Also <code> ext2_create () </code> does
     * do both.)
     * </p>
     *
     * @param credentials Who is asking for this operation
     *                    to be performed.  Must have create
     *                    and write access for this ONode.
     *                    Must not be null.
     *
     * @param entry The OEntry which points to this parent ONode.
     *              Must not be null.
     *
     * @param type The type of ONode to create, such as
     *             <code> FlatONode.TYPE_ID </code>,
     *             <code> ObjectONode.TYPE_ID </code>,
     *             <code> SymbolicLink.TYPE_ID </code>,
     *             <code> DriverONode.TYPE_ID </code>
     *             and so on.  Note that not all object
     *             systems support all ONode types; if
     *             the requested ONode type is not
     *             supported by this object system then
     *             an ONodeOperationException will be thrown.
     *             Must not be null.
     *
     * @param name The name of the new ONode to create,
     *             such as "bar" to create a child ONode at "foo/bar".
     *             Must not be null.
     *
     * @param mode The Security for the ONode to be created, such as
     *             traditional UNIX-style user/group/other read/write/execute
     *             permissions, or possibly something more complex or
     *             esoteric.  Must not be null.
     *
     * @param onode_specific_parameters Additional parameters
     *                                  required for the specified
     *                                  type of ONode (see the
     *                                  list of types, FlatONode,
     *                                  ObjectONode, Relation and
     *                                  so on in the main description
     *                                  above for required parameters
     *                                  by ONode type).  Can be
     *                                  omitted, empty or null.
     *
     * @return The successfully created ONode's OEntry,
     *         relative to the specified parent OEntry.  Note that
     *         the created ONode may have another OEntry as well,
     *         if the specified parent's path contains symbolic
     *         links and so on.  Never null.
     *
     * @throws ONodeOperationException If any failure occurs
     *                                 due to security or
     *                                 object system limitations
     *                                 or ONode limitations are
     *                                 encountered.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public abstract OEntry create (
				   Credentials credentials,
				   OEntry entry,
                                   RecordTypeIdentifier type,
                                   String name,
                                   Security<RecordFlag> mode,
                                   Object... onode_specific_parameters
                                   )
        throws ONodeOperationException,
               I18nIllegalArgumentException;


    /**
     * <p>
     * Removes the specified ONode from this children record,
     * and performs all linking operations necessary
     * (including un-linking the child's ".." entry to
     * the parent ONode, if the child is an hierarchical object).
     * </p>
     *
     * @param credentials Who is asking for this operation
     *                    to be performed.  Must have
     *                    write access for this ONode.
     *                    Must not be null.
     *
     * @param child_entry The OEntry pointing to the child ONode
     *                    to be removed from this children
     *                    record.  Must not be null.
     *
     * @throws ONodeSecurityException If the specified credentials
     *                                do not have permission to
     *                                perform this operation.
     *
     * @throws ONodeOperationException If the specified child entry
     *                                 cannot be removed from this
     *                                 children Record, for example
     *                                 because it is not a child in
     *                                 the first place.
     */
    public abstract void removeChild (
                                      Credentials credentials,
                                      OEntry child_entry
                                      )
        throws ONodeSecurityException,
               ONodeOperationException;
}
