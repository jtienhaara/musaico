package musaico.kernel.objectsystem.onode;

import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Reference;

import musaico.kernel.objectsystem.RecordOperationException;


/**
 * <p>
 * Creates OEntries for an object system reading in the contents
 * of an hierarchical object.
 * </p>
 *
 * @see musaico.kernel.objectsystem.records#ObjectRecord
 *
 *
 * <p>
 * In Java, every OEntryFactory must be Serializable in order to play
 * nicely over RMI.
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
public interface OEntryFactory
    extends Serializable
{
    /**
     * <p>
     * Creates a new OEntry with the specified name, ONode reference
     * and parent OEntry.
     * </p>
     *
     * @param name The single level of a path representing this
     *             OEntry's name inside its parent OEntry.
     *             For example, when creating an OEntry with
     *             full path "/foo/bar", the name would be "bar".
     *
     * @param parent_entry The parent of this OEntry.  For example,
     *                     when creating an OEntry with full
     *                     path "/foo/bar", the parent would be
     *                     the OEntry for "/foo".  Must not be null.
     *
     * @param onode_ref The reference to the ONode representing this
     *                  OEntry's data.  Must not be null.
     *
     * @return The newly created OEntry.  Never null.
     *
     * @throws I18nIllegalArgumentException If the specified parameters
     *                                      are incorrect (see above).
     *
     * @throws RecordOperationException If something goes horribly wrong.
     */
    public abstract OEntry createOEntry (
                                         String name,
                                         OEntry parent_entry,
                                         Reference onode_ref
                                         )
        throws I18nIllegalArgumentException,
               RecordOperationException;
}
