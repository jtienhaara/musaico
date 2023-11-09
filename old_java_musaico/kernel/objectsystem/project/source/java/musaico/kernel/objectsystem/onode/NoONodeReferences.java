package musaico.kernel.objectsystem.onode;

import java.io.Serializable;


import musaico.io.ReferenceCount;
import musaico.io.SimpleReferenceCount;

import musaico.security.Credentials;


/**
 * <p>
 * References to an ONode which cannot be referenced
 * (such as ONode.NONE).
 * </p>
 *
 * <p>
 * No matter what the caller tries to do to increment the
 * references to the ONode, they will never be recorded.
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
public class NoONodeReferences
    implements ONodeReferences, Serializable
{
    /**
     * @see musaico.kernel.objectsystem.ONode#count()
     */
    @Override
    public long count ()
    {
        return 0L;
    }


    /**
     * @see musaico.kernel.objectsystem.ONode#hardLinks(musaico.security.Credentials)
     */
    @Override
    public ReferenceCount hardLinks (
				     Credentials credentials
				     )
    {
        return new SimpleReferenceCount ();
    }


    /**
     * @see musaico.kernel.objectsystem.ONode#readers(musaico.security.Credentials)
     */
    @Override
    public ReferenceCount readers (
				   Credentials credentials
				   )
    {
        return new SimpleReferenceCount ();
    }


    /**
     * @see musaico.kernel.objectsystem.ONode#writers(musaico.security.Credentials)
     */
    @Override
    public ReferenceCount writers (
				   Credentials credentials
				   )
    {
        return new SimpleReferenceCount ();
    }
}
