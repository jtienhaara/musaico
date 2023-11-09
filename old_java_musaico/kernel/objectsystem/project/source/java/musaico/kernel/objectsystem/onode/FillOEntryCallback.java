package musaico.kernel.objectsystem.onode;

import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Reference;

import musaico.kernel.objectsystem.RecordTypeIdentifier;

import musaico.region.Position;


/**
 * <p>
 * Function which fills in an OEntry while reading in an Object.
 * </p>
 *
 * <p>
 * Called by <code>Record.readObject ()</code>.
 * </p>
 *
 * <p>
 * This allows the kernel to add entries to an object in
 * different ways depending on the type of the record being added.
 * </p>
 *
 * @see musaico.kernel.objectsystem.record.Record#readObject()
 *
 *
 * <p>
 * Because object systems can conceivably be distributed, every
 * FillOEntryCallback must be Serializable in order to play nicely
 * over RMI.
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
public interface FillOEntryCallback
    extends Serializable
{
    /**
     * <p>
     * Adds the specified OEntry to the specified object.
     * </p>
     *
     * <p>
     * The child_record_type specifies whether the child
     * is an hierarchical object or a flat record, a driver node,
     * a relation such as a symbolic link or a union,
     * or possibly even something more complex, such as a
     * FIFO or the equivalent of a socket, and so on.
     * </p>
     *
     * @param object The parent object whose child ONode is to
     *               be filled in and added.  Must not be null.
     *
     * @param child_position_within_object Where in the region of
     *                                     child OEntries the child
     *                                     is to be placed (such
     *                                     as the end of the ONode's
     *                                     region, or optionally a
     *                                     more precise position,
     *                                     such as the midpoint).
     *                                     Must not be null.
     *
     * @param child_record_type The type of record which the child
     *                          represents, such as
     *                          <code> FlatRecord.TYPE_ID </code>,
     *                          <code> ObjectRecord.TYPE_ID </code>,
     *                          <code> SymbolicLink.TYPE_ID </code>,
     *                          <code> Driver.TYPE_ID </code>, and so
     *                          on.  Must correspond to the class of
     *                          ONode implemented by child_onode.
     *                          Must not be null.
     *
     * @param child_name The OEntry name for the child to read in,
     *                   such as "bar" when filling in the child
     *                   "foo/bar".  Must not be null.
     *
     * @param child_onode The child ONode to add to the object.
     *                    Must not be null.
     *
     * @param handback_token A token that can be used by the
     *                       FillOEntryCallback to, for example,
     *                       keep track of its position in a buffer,
     *                       or keep a count of child entries,
     *                       and so on.  Is null the first call,
     *                       while subsequent calls will have
     *                       the previous return value passed in.
     *
     * @return An optional handback token.  Whatever is returned on
     *         this invocation will be passed in as the handback_token
     *         parameter with the next child entry to be filled in.
     *         Can be null.
     */
    public abstract Serializable fill (
                                       OEntry object,
                                       Position child_position_within_object,
                                       RecordTypeIdentifier child_record_type,
                                       String child_name,
                                       ONode child_onode,
                                       Serializable handback_token
                                       )
        throws I18nIllegalArgumentException,
               ONodeOperationException;
}
