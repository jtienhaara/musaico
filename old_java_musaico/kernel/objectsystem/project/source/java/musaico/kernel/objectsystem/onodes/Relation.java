package musaico.kernel.objectsystem.onodes;

import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Path;
import musaico.io.Sequence;

import musaico.kernel.objectsystem.onode.OEntry;
import musaico.kernel.objectsystem.onode.ONode;
import musaico.kernel.objectsystem.onode.ONodeOperationException;

import musaico.security.Credentials;


/**
 * <p>
 * Represents the relationship between one ONode and one or more
 * other ONodes referenced symbolically by their paths.
 * </p>
 *
 * <p>
 * For example, a symbolic link is a Relation which can be stored
 * in an ONode.
 * </p>
 *
 * <p>
 * Every Relation must provide a public static <code> TYPE_ID </code>
 * RelationTypeIdentifier.
 * </p>
 *
 * @see musaico.kernel.objectsystem.onodes.relations
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
 * Copyright (c) 2009, 2010, 2011, 2012 Johann Tienhaara
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
public interface Relation
    extends ONode, Serializable
{
    /**
     * <p>
     * Returns the path(s) to the ONode(s) which this
     * relation references.
     * </p>
     *
     * <p>
     * For example, a symbolic link will return a single Path
     * to the symbolically linked-to ONode.
     * </p>
     *
     * <p>
     * Each Path can be resolved relative to the OEntry pointing
     * to this ONode.  The Path might be relative to that OEntry,
     * or it might be absolute.
     * </p>
     *
     * @param credentials Who is asking for this operation
     *                    to be performed.  Must have "relatives"
     *                    and read access for this ONode.
     *                    Must not be null.
     *
     * @param entry The OEntry which points to this Relation ONode.
     *              Must not be null.
     *
     * @return The path(s) to the ONode(s) in the relation.
     *         Never null.  Always contains at least one member.
     *
     * @throws I18nIllegalArgumentException If the specified parameters
     *                                      are invalid (see above).
     */
    public abstract Sequence<Path> relatives (
					      Credentials credentials,
					      OEntry entry
                                              )
        throws I18nIllegalArgumentException,
               ONodeOperationException;


    /**
     * <p>
     * Creates or returns an OEntry representing this Relation,
     * but does not add it to the OEntry tree.
     * </p>
     *
     * <p>
     * Any time an ONode needs to resolve or "follow" a Relation,
     * it does so by calling this method.  It does not add the returned
     * OEntry to the kernel objects, nor does not store the OEntry in
     * the tree, since the OEntry is meant to be a helper, not
     * an actual entry in the object system.
     * </p>
     *
     * <p>
     * The caller can then use the resolved OEntry as if it were a proper
     * OEntry.
     * </p>
     *
     * <p>
     * For example, a symbolic link Relation might return an
     * OEntry with the same path passed in, but pointing to
     * the ONode resolved at the other end of the symbolic link.
     * </p>
     *
     * <p>
     * Or a union Relation might return an OEntry with the same
     * path passed in, but pointing to an in-memory ONode which
     * spans all the children of the union'ed object nodes.
     * </p>
     *
     * <p>
     * And so on.
     * </p>
     *
     * @param credentials Who is asking for this operation
     *                    to be performed.  Must have "resolve
     *                    relation", read and execute access
     *                    for this Relation ONode.
     *                    Must not be null.
     *
     * @param entry The OEntry which points to this parent ONode.
     *              Must not be null.
     *
     * @return The OEntry representing this resolved relation,
     *         either newly created or (if a previously created OEntry
     *         returned by this method can be reused, or if the
     *         resolution is another OEntry in the object system)
     *         an existing OEntry.  Never null.
     *
     * @throws I18nIllegalArgumentException If the specified parameters
     *                                      are invalid (see above).
     *
     * @throws ONodeOperationException if anything goes horribly wrong.
     */
    public abstract OEntry resolveRelation (
					    Credentials credentials,
					    OEntry entry
                                            )
        throws I18nIllegalArgumentException,
               ONodeOperationException;
}
