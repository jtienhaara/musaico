package musaico.kernel.objectsystem.onodes;

import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.kernel.objectsystem.RecordFlag;
import musaico.kernel.objectsystem.RecordTypeIdentifier;

import musaico.kernel.objectsystem.onode.OEntry;
import musaico.kernel.objectsystem.onode.ONode;
import musaico.kernel.objectsystem.onode.ONodeOperationException;

import musaico.security.Credentials;
import musaico.security.Security;


/**
 * <p>
 * Represents a hierarchical object ONode, which may contain Fields,
 * other ONodes, and so on.
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
 * Copyright (c) 2010, 2011, 2012 Johann Tienhaara
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
public interface ObjectONode
    extends ONode, Serializable
{
    /** The type identifier representing all object ONodes. */
    public static final RecordTypeIdentifier TYPE_ID =
        new RecordTypeIdentifier ( "object" );


    /**
     * <p>
     * Returns this object's child ONodes and operations, allowing
     * callers to open, close, read and write child nodes from/to
     * this ONode.
     * </p>
     *
     * @return This ONOde's child ONodes.  Never null.
     */
    public abstract ObjectChildren children ();
}
