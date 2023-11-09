package musaico.buffer.types;


import java.io.Serializable;


import musaico.buffer.Buffer;

import musaico.field.Field;
import musaico.field.FieldTypingEnvironment;

import musaico.region.Position;
import musaico.region.Region;

import musaico.types.TypeCastException;
import musaico.types.TypeCaster;
import musaico.types.TypeException;


/**
 * <p>
 * Casts Buffers to some other type of objects, such as Strings
 * or FooBars, by relying on the constituent Fields to do all
 * the work.
 * </p>
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
public abstract class AbstractBufferCaster<TO extends Object, INTERMEDIARY extends Object>
    implements TypeCaster<Buffer,TO>, Serializable
{
    /**
     * @see musaico.types.TypeCaster#cast(java.lang.Object,java.lang.Class)
     */
    public TO cast (
                    Buffer buffer,
                    Class to_class
                    )
        throws TypeException
    {
        FieldTypingEnvironment environment = buffer.environment ();
        Region buffer_region = buffer.region ();
        INTERMEDIARY cast_under_construction = null;
        for ( Position position : buffer_region )
        {
            Field field = buffer.get ( position );
            if ( field == null )
            {
                // Skip holes in the buffer.
                continue;
            }

            final TO field_as_x = (TO) field.value ( to_class );
            cast_under_construction =
                this.add ( field_as_x, cast_under_construction );
        }

        TO result = this.build ( cast_under_construction );

        return result;
    }


    /**
     * <p>
     * Adds the specified element to the cast being built,
     * and returns an object to pass into the next add ()
     * or build () call.
     * </p>
     *
     * <p>
     * For example, if the cast is to a String, then the
     * element would be a String which is added to a StringBuilder,
     * and the StringBuilder is returned.
     * </p>
     *
     * @param element The element to add to the cast being built.
     *                Must not be null.
     *
     * @param cast_under_construction The cast-specific object
     *                                being built, such as a
     *                                StringBuilder when casting
     *                                to a String.  Must be null
     *                                on the first call, not null on
     *                                subsequent calls during a cast.
     *
     * @return The cast under construction, which can be used to
     *         add to the cast or build the final result.  Never null.
     */
    protected abstract INTERMEDIARY add (
                                         TO element,
                                         INTERMEDIARY cast_under_construction
                                         );


    /**
     * <p>
     * Constructs the final cast from a given buffer.
     * </p>
     *
     * @param cast_under_construction The cast to build and finish,
     *                                such as a StringBuilder during
     *                                a cast to a String.
     *                                Must not be null.
     */
    protected abstract TO build (
                                 INTERMEDIARY cast_under_construction
                                 );
}
