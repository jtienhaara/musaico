package musaico.kernel.objectsystem.onode;

import java.io.Serializable;


import musaico.io.ReferenceCount;

import musaico.security.Credentials;


/**
 * <p>
 * Maintains the runtime references to an ONode, such as the number
 * of Cursors open for reading or writing, or the number of
 * hard links from other ONodes to this one, and so on.
 * </p>
 *
 *
 * <p>
 * In Java every ONodeReferences must be Serializable in order
 * to play nicely over RMI.  However kernel designers must be
 * careful not to maintain an ONode or its references in more than
 * one VM -- otherwise the two or more sets of references can
 * be incremented and decremented independently.
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
public interface ONodeReferences
    extends Serializable
{
    /** The immutable references to an ONode which cannot
     *  be referenced, such as ONode.NONE. */
    public static final ONodeReferences NONE = new NoONodeReferences ();




    /**
     * <p>
     * Returns the total number of references (hard links,
     * readers, writers and so on) to the ONode.
     * </p>
     *
     * @return The total number of references to the ONode.
     *         Always 0 or greater.
     */
    public abstract long count ();


    /**
     * <p>
     * The number of hard links pointing to this ONode (parent object,
     * children, and hard links).
     * </p>
     *
     * <p>
     * Excludes symbolic links and other relation records.  Only
     * "hard links" are referenced.
     * </p>
     *
     * <p>
     * Only when all of the reference counts are 0
     * (<code> hardLinks ( ... ).size () == 0 </code>,
     * <code> readers ( ... ).size () == 0 </code> and so on)
     * can this ONode be destroyed.
     * </p>
     *
     * @param credentials The User or Module identifier wishing to
     *                    retrieve the ReferenceCount for the ONode.
     *                    Must not be null.
     *
     * @return This ONode's hard links (stored with the ONode).  Never null.
     *
     * @throws ONodeSecurityException If the specified credentials may
     *                                not retrieve the ReferenceCount
     *                                for the ONode.
     */
    public abstract ReferenceCount hardLinks (
					      Credentials credentials
					      )
        throws ONodeSecurityException;


    /**
     * <p>
     * The number of readers of this ONode.
     * </p>
     *
     * <p>
     * Each section of code which is currently interacting with the
     * ONode must have a read reference to the ONode.
     * </p>
     *
     * <p>
     * At any point after finishing a [sequence of] ONode operation[s],
     * the code can decrement the references count to the ONode.
     * </p>
     *
     * <p>
     * Only when all of the reference counts are 0
     * (<code> hardLinks ( ... ).size () == 0 </code>,
     * <code> readers ( ... ).size () == 0 </code> and so on)
     * can this ONode be destroyed.
     * </p>
     *
     * @param credentials The User or Module identifier wishing to
     *                    retrieve the ReferenceCount for the ONode.
     *                    Must not be null.
     *
     * @return This ONode's in-memory reader references.  Never null.
     *
     * @throws ONodeSecurityException If the specified credentials may
     *                                not retrieve the ReferenceCount
     *                                for the ONode.
     */
    public abstract ReferenceCount readers (
					    Credentials credentials
					    )
        throws ONodeSecurityException;


    /**
     * <p>
     * The number of writers of this ONode.
     * </p>
     *
     * <p>
     * Each section of code which currently has the ONode open for
     * writing must have both a read reference and a write reference
     * to the ONode.
     * </p>
     *
     * <p>
     * After the write access is closed, writers should be decremented.
     * </p>
     *
     * <p>
     * Only when all of the reference counts are 0
     * (<code> hardLinks ( ... ).size () == 0 </code>,
     * <code> readers ( ... ).size () == 0 </code> and so on)
     * can this ONode be destroyed.
     * </p>
     *
     * @param credentials The User or Module identifier wishing to
     *                    retrieve the ReferenceCount for the ONode.
     *                    Must not be null.
     *
     * @return This ONode's in-memory writer references.  Never null.
     *
     * @throws ONodeSecurityException If the specified credentials may
     *                                not retrieve the ReferenceCount
     *                                for the ONode.
     */
    public abstract ReferenceCount writers (
					    Credentials credentials
					    )
        throws ONodeSecurityException;
}
