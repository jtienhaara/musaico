package musaico.kernel.objectsystem;

import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Reference;
import musaico.io.SimpleTypedIdentifier;

import musaico.io.references.SimpleSoftReference;
import musaico.io.references.UUIDReference;

import musaico.kernel.KernelNamespaces;


/**
 * <p>
 * Uniquely identifies a Cursor within the KernelNamespaces.CURSORS
 * namespace.
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
public class CursorIdentifier
    extends SimpleTypedIdentifier<Cursor>
    implements Serializable
{
    /** A CursorIdentifier pointing to no cursor.
     *  Useful for stepping through an index of CursorIdentifiers. */
    public static final CursorIdentifier NONE =
        new CursorIdentifier ( new SimpleSoftReference<String> ( "no_cursor" ) );


    /**
     * <p>
     * Creates a new CursorIdentifier with a UUID to uniquely
     * identify it.
     * </p>
     */
    public CursorIdentifier ()
    {
        this ( new UUIDReference () );
    }


    /**
     * <p>
     * Creates a new CursorIdentifier with the specified name.
     * </p>
     *
     * <p>
     * This method is provided for sub-classing CursorIdentifier
     * with a different (non-UUID) type of name.  Use it wisely.
     * </p>
     *
     * @param cursor_name The unique name of the cursor.
     *                    Must not be null.
     *
     * @throws I18nIllegalArgumentException If the specified parameters
     *                                      are invalid (see above).
     */
    protected CursorIdentifier (
                                Reference name
                                )
    {
        super ( KernelNamespaces.CURSORS, name, Cursor.class );
    }
}
