package musaico.kernel.objectsystem.onode;

import java.io.Serializable;


import musaico.io.ReferenceCount;
import musaico.io.SimpleReferenceCount;

import musaico.security.Credentials;


/**
 * <p>
 * References to an ONode.
 * </p>
 *
 *
 * <p>
 * In Java every ONodeReferences must be Serializable in order
 * to play nicely over RMI.
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
public class SimpleONodeReferences
    implements ONodeReferences, Serializable
{
    /** Number of hard links (as opposed to symbolic links)
     *  pointing to the ONode. */
    private final ReferenceCount referencedByHardLinks =
        new SimpleReferenceCount ();

    /** Number of Cursors that have the ONode open for reading
     *  right now. */
    private final ReferenceCount referencedByReaders =
        new SimpleReferenceCount ();

    /** Number of Cursors that have the ONode open for writing
     *  right now. */
    private final ReferenceCount referencedByWriters =
        new SimpleReferenceCount ();


    /**
     * @see musaico.kernel.objectsystem.ONode#count()
     */
    @Override
    public long count ()
    {
        return
            this.referencedByHardLinks.count ()
            + this.referencedByReaders.count ()
            + this.referencedByWriters.count ();
    }


    /**
     * @see musaico.kernel.objectsystem.ONode#hardLinks(musaico.security.Credentials)
     */
    @Override
    public ReferenceCount hardLinks (
				     Credentials credentials
				     )
    {
        return this.referencedByHardLinks;
    }


    /**
     * @see musaico.kernel.objectsystem.ONode#readers(musaico.security.Credentials)
     */
    @Override
    public ReferenceCount readers (
				   Credentials credentials
				   )
    {
        return this.referencedByReaders;
    }


    /**
     * @see musaico.kernel.objectsystem.ONode#writers(musaico.security.Credentials)
     */
    @Override
    public ReferenceCount writers (
				   Credentials credentials
				   )
    {
        return this.referencedByWriters;
    }
}
