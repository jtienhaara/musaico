package musaico.kernel.objectsystem.onode;


import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Identifier;
import musaico.io.Reference;
import musaico.io.Path;
import musaico.io.Sequence;

import musaico.security.Credentials;


/**
 * <p>
 * Represents an entry in an object (either another object or a flat record
 * of fields, a symbolic link or other relation, a driver and so on).
 * </p>
 *
 * <p>
 * An OEntry corresponds to a DEntry (directory entry) in a UNIX-like
 * file system.  Whereas a directory might contain other directories
 * or files (sequences of bytes), an object in Musaico can contain
 * other objects or flat records (sequences/sets of Fields with no
 * hierarchy).
 * </p>
 *
 *
 * <p>
 * In Java, every OEntry must be Serializable in order to play
 * nicely over RMI, even if the object system type to which the OEntry
 * belongs is not distributable.  This is because a distributed
 *kernel may distribute OEntries to other nodes, each OEntry
 * referring back to fixed nodes on the network.
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
 * Copyright (c) 2009, 2011 Johann Tienhaara
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
public interface OEntry
    extends Reference, Serializable
{
    /**
     * <p>
     * Adds a child OEntry to this OEntry.
     * </p>
     *
     * <p>
     * Be careful to call this method only on an OEntry which is
     * local.  In a distributed kernel, if this OEntry has been
     * serialized from another network node, your add () won't
     * be terribly useful!
     * </p>
     *
     * @param credentials Who is asking for the operation, such as
     *                    a user Credentials or a Module's Credentials.
     *                    Must not be null.
     *
     * @param child_entry The child OEntry to add.  Must not be null.
     *
     * @throws I18nIllegalArgumentException If the parameters are invalid
     *                                      (see above).
     *
     * @throws OEntryOperationException If this OEntry is immutable or
     *                                  does not point to an hierarchical
     *                                  object or if the specified
     *                                  child entry's name conflicts
     *                                  with an existing one.
     */
    public abstract void addChild (
                                   Credentials credentials,
                                   OEntry child_entry
                                   )
        throws I18nIllegalArgumentException,
               OEntryOperationException;


    /**
     * <p>
     * Returns a specific child of this OEntry.
     * </p>
     *
     * <p>
     * For example, to get the child named "foo":
     * </p>
     *
     * <pre>
     *     OEntry entry = ...;
     *     OEntry foo_child = entry.child ( "foo" );
     * </pre>
     *
     * <p>
     * Implementors should be careful to not actually store the
     * children directly, but rather store their references
     * (<code> child.name () </code> or <code> child.path () </code>,
     * depending on whether relative or absolute references are
     * more desirable for a given situation), and
     * look them up as needed.  Otherwise serialization across a
     * distributed system could conceivably lead to a massive
     * tree of serialized OEntries.
     * </p>
     *
     * @param child_name The name of the child OEntry to return.
     *                   Must not be null.
     *
     * @return The specified child of this OEntry.
     *         Never null.
     *
     * @throws OEntryOperationException If the specified child does
     *                                  not exist under this OEntry.
     */
    public abstract OEntry child (
                                  String child_name
                                  )
        throws OEntryOperationException;


    /**
     * <p>
     * Returns the children of this OEntry.
     * </p>
     *
     * <p>
     * For example, to get the first child:
     * </p>
     *
     * <pre>
     *     OEntry entry = ...;
     *     OEntry first_child = entry.children ().first ();
     * </pre>
     *
     * <p>
     * Or to get the 2nd child:
     * </p>
     *
     * <pre>
     *     OEntry entry = ...;
     *     OEntry child2 =
     *         entry.children ().after ( entry.children ().first () );
     * </pre>
     *
     * <p>
     * And so on.
     * </p>
     *
     * <p>
     * Implementors should be careful to not actually store the
     * children directly, but rather store their references
     * (<code> child.name () </code> or <code> child.path () </code>,
     * depending on whether relative or absolute references are
     * more desirable for a given situation), and
     * look them up as needed.  Otherwise serialization across a
     * distributed system could conceivably lead to a massive
     * tree of serialized OEntries.
     * </p>
     *
     * @return The children of this OEntry.
     *         Never null, even if there are no children.
     */
    public abstract Sequence<OEntry> children ();


    /**
     * <p>
     * Factory-type method to create an OEntry with this entry as
     * its parent.
     * </p>
     *
     * <p>
     * This method creates the OEntry but does NOT add it to the
     * hierarchy.  Call <code> addChild () </code> to add it
     * to the tree.
     * <p>
     *
     * <p>
     * Also note that this method does NOT link the newly
     * created entry to the ONode (see <code> ONode.link() <code>).
     * This is because OEntries are frequently created
     * "temporarily", in order to resolve Relations and follow
     * symbolic links and so on.
     * </p>
     *
     * @param credentials Who is asking for the operation, such as
     *                    a user Credentials or a Module's Credentials.
     *                    Must not be null.
     *
     * @param name The name of the child entry to create.
     *             Must not be null.
     *
     * @param super_block_ref The object system super block which
     *                        owns the entry, typically (but not
     *                        always) the same as the super block
     *                        of the specified ONode.  (Example
     *                        exception: ".." points to the
     *                        super block of the ONode it is a
     *                        child of; but ".."'s parent ONode
     *                        might itself be the root of a mounted
     *                        object system, and therefore have
     *                        a different super block ref than
     *                        the parent-parent which ".." points
     *                        to.  What a mouthful.)  Must be a
     *                        SuperBlockIdentifier (not yet defined).
     *                        Must not be null.
     *
     * @param onode The ONode to which the newly created OEntry
     *              will point.  Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public abstract OEntry create (
                                   Credentials credentials,
                                   String name,
                                   Identifier super_block_ref,
                                   ONode onode,
                                   boolean is_mounted
                                   )
        throws I18nIllegalArgumentException;


    /**
     * <p>
     * Returns true if this object node is part of an already-mounted
     * object system, false if it is the root of an as-yet-unmounted
     * object system.
     * </p>
     *
     * @return True if this OEntry is part of a mounted object system,
     *         false if it is the root of an unmounted object system.
     */
    public abstract boolean isMounted ();


    /**
     * <p>
     * Returns the name of this OEntry (one piece of a path,
     * such as "foo" under the root path "/foo/bar").
     * </p>
     *
     * @return This OEntry's object name.  Never null.
     */
    public abstract String name ();


    /**
     * <p>
     * Returns a reference to this OEntry's corresponding ONode.
     * </p>
     *
     * @return A reference to this OEntry's ONode.  Possibly null,
     *         if the ONode does not yet exist.
     */
    public abstract ONodeIdentifier onodeRef ();


    /**
     * <p>
     * Returns this OEntry's parent OEntry.
     * </p>
     *
     * <p>
     * Implementors should be careful to not actually store the
     * parent directly, but rather store its reference, and
     * look it up as needed.  Otherwise serialization across a
     * distributed kernel will lead to a massive tree of serialized
     * OEntries!
     * </p>
     *
     * @return This OEntry's parent.  Never null.  Returns
     *         <code> musaico.kernel.oentries.SimpleOEntry.NO_SUCH_OENTRY </code>
     *         if this is the root OEntry.
     */
    public abstract OEntry parent ();


    /**
     * <p>
     * Returns the path to this OEntry from the root of all object
     * systems.
     * </p>
     *
     * @return This OEntry's path.  Never null.
     */
    public abstract Path path ();


    /**
     * <p>
     * Removes a child OEntry from this OEntry.
     * </p>
     *
     * <p>
     * Be careful to call this method only on an OEntry which is
     * local.  In a distributed kernel, if this OEntry has been
     * serialized from another network node, your remove () won't
     * be terribly useful!
     * </p>
     *
     * @param credentials Who is asking for the operation, such as
     *                    a user Credentials or a Module's Credentials.
     *                    Must not be null.
     *
     * @param child_entry The child OEntry to remove.  Must not be null.
     *                    Must be a child of this OEntry.
     *
     * @throws I18nIllegalArgumentException If the parameters are invalid
     *                                      (see above).
     *
     * @throws OEntryOperationException If this OEntry is immutable or
     *                                  does not point to an hierarchical
     *                                  object.
     */
    public void removeChild (
                             Credentials credentials,
                             OEntry child_entry
                             )
        throws I18nIllegalArgumentException,
               OEntryOperationException;


    /**
     * <p>
     * Returns a reference to the super block which governs
     * the object system to which this OEntry belongs.
     * </p>
     *
     * <p>
     * Because the SuperBlockIdentifier has not yet been
     * defined, this interface must declare a generic Identifier
     * as the return value.  However every implementation shall
     * declare and return <code> SuperBlockIdentifier </code>.
     * </p>
     *
     * @return A reference to this OEntry's parent superblock.
     *         Always a SuperBlockIdentifier.  Never null.
     */
    public abstract Identifier superBlockRef ();
}
/* !!!
        int (*d_revalidate)(struct dentry *, struct nameidata *);
        int (*d_hash) (struct dentry *, struct qstr *);
        int (*d_compare) (struct dentry *, struct qstr *, struct qstr *);
        int (*d_delete)(struct dentry *);
        void (*d_release)(struct dentry *);
        void (*d_iput)(struct dentry *, struct inode *);
        char *(*d_dname)(struct dentry *, char *, int);
        !!! */
