package musaico.kernel.objectsystem.types;


import java.io.Serializable;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


import musaico.kernel.objectsystem.Cursor;
import musaico.kernel.objectsystem.Record;

import musaico.kernel.objectsystem.onode.ONode;

import musaico.kernel.objectsystem.onodes.FlatONode;
import musaico.kernel.objectsystem.onodes.ObjectONode;
import musaico.kernel.objectsystem.onodes.Relation;

import musaico.kernel.objectsystem.onodes.relations.SymbolicLink;

import musaico.types.SimpleTypeSystem;
import musaico.types.Type;
import musaico.types.TypeException;
import musaico.types.TypeSystem;
import musaico.types.TypingEnvironment;


/**
 * <p>
 * TypeSystem of ObjectSystem (object system) types, such as ONode and Record
 * and so on.
 * </p>
 *
 * <p>
 * ObjectSystem types are all cross-platform,
 * so they can be shared between C, JavaScript, Tcl, Java,
 * and so on platforms.
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
 * Copyright (c) 2009 Johann Tienhaara
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
public class ObjectSystemTypeSystem
    extends SimpleTypeSystem
    implements Serializable
{
    /**
     * <p>
     * Creates a new ObjectSystemTypeSystem beneath the specified
     * type system.
     * </p>
     *
     * @param parent_type_system The parent type system, of which this
     *                           is a child.  Must not be null.
     *
     * @throws TypeException If the parent type system is null
     *                       or invalid.
     */
    public ObjectSystemTypeSystem (
                              TypeSystem parent_type_system
                              )
        throws TypeException
    {
        super ( parent_type_system );
    }






    /**
     * <p>
     * Static method creates an ObjectSystem type system, and adds
     * the standard ObjectSystem types, inside the specified
     * TypingEnvironment.
     * </p>
     */
    public static TypeSystem standard (
                                       TypingEnvironment environment
                                       )
        throws TypeException
    {
        // Create the ObjectSystem type system.
        TypeSystem object_system_types =
            new ObjectSystemTypeSystem ( environment.root () );

        // Create the types.
        Type<Cursor> cursor_type = new CursorType ();
        Type<ONode> onode_type = new ONodeType ();
        Type<Record> record_type = new RecordType ();

        // Add the types to the typing environment.
        environment.register ( Cursor.class, object_system_types, cursor_type );

        environment.register ( ONode.class, object_system_types, onode_type );

        environment.register ( Record.class, object_system_types, record_type );
        environment.register ( FlatONode.class, object_system_types,
                               record_type );
        environment.register ( ObjectONode.class, object_system_types,
                               record_type );
        environment.register ( Relation.class, object_system_types,
                               record_type );
        environment.register ( SymbolicLink.class, object_system_types,
                               record_type );

        return object_system_types;
    }
}
