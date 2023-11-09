package musaico.kernel.objectsystem.onode;

import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Path;
import musaico.io.Reference;
import musaico.io.SimpleTypedIdentifier;

import musaico.io.references.SimpleSoftReference;

import musaico.kernel.KernelNamespaces;


/**
 * <p>
 * Uniquely identifies an OEntry within the
 * KernelNamespaces.OENTRIES namespace.
 * </p>
 *
 *
 * <p>
 * In Java, every Identifier must be Serializable in order
 * to play nicely across RMI.
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
public class OEntryIdentifier
    extends SimpleTypedIdentifier<OEntry>
    implements Serializable
{
    /** Always points to the root of all object systems ("/"). */
    public static final OEntryIdentifier ROOT =
        new OEntryIdentifier ( new Path ( "/" ) );


    /** An OEntryIdentifier pointing to no OEntry.
     *  Useful for stepping through an index of OEntryIdentifiers. */
    public static final OEntryIdentifier NONE =
        new OEntryIdentifier ( new Path ( "/no/oentry" ) );


    /**
     * <p>
     * Creates a new OEntryIdentifier with the specified
     * OEntry path.
     * </p>
     *
     * @param oentry_path The absolute path to the OEntry.
     *                    Must not be null.
     *
     * @throws I18nIllegalArgumentException If the specified parameters
     *                                      are invalid (see above).
     */
    public OEntryIdentifier (
                             Path oentry_path
                             )
        throws I18nIllegalArgumentException
    {
        super ( KernelNamespaces.SUPER_BLOCKS, oentry_path,
                OEntry.class );
    }
}
