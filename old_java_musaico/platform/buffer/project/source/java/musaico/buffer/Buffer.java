package musaico.buffer;


import java.io.Serializable;


import musaico.field.Field;
import musaico.field.FieldTypingEnvironment;

import musaico.io.ReferenceCount;

import musaico.region.Position;
import musaico.region.Region;

import musaico.types.Instance;


/**
 * <p>
 * Represents a Buffer of Fields for I/O requests and responses.
 * </p>
 *
 * <p>
 * Buffers are NOT thread-safe!  Create a ThreadSafeBufferWrapper
 * if you wish to call <code> set ( ... ) </code> from concurrent
 * threads.
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
public interface Buffer
    extends Instance, Serializable
{
    // Every Buffer must implement all the methods of Instance.


    /**
     * <p>
     * Returns the fields typing environment which can be used to create
     * new Fields for this buffer.
     * </p>
     *
     * @return This buffer's typing environment.  Never null.
     */
    public abstract FieldTypingEnvironment environment ();


    /**
     * <p>
     * Returns the nth field from this buffer.
     * </p>
     */
    public abstract Field get (
                               Position position
                               );


    /**
     * <p>
     * Returns the ReferenceCount for this Buffer.
     * </p>
     *
     * <p>
     * Increment the reference count whenever the Buffer is in use.
     * </p>
     *
     * <p>
     * Decrement the reference count when finished.  This will cause
     * the buffer to be freed by calling its BufferManager's free ()
     * method.
     * </p>
     */
    public abstract ReferenceCount references ();


    /**
     * <p>
     * Returns a description of this Buffer's size and structure,
     * which may be passed around over RMI safely without inducing
     * serialization of the whole Buffer.
     * </p>
     *
     * <p>
     * The Region defines the start and end positions of this Buffer,
     * how to step through the Positions of its fields, and
     * may also help in indexing Markers (such as RecordStart
     * and RecordEnd markers).
     * </p>
     *
     * <p>
     * The Region defined by the Buffer can be used to step through all
     * the Fields of the Buffer.  For example:
     * </p>
     *
     * <pre>
     *     Buffer buffer = ... ;
     *     for ( Position position = buffer.region ().start ();
     *           ! ( position instanceof NoSuchPosition );
     *           position = position.step ( 1L ) )
     *     {
     *         Field field = buffer.get ( position );
     *         ...
     *     }
     * </pre>
     *
     * <p>
     * Or, to step through the Fields in reverse order:
     * </p>
     *
     * <pre>
     *     Buffer buffer = ... ;
     *     for ( Position position = buffer.region ().end ();
     *           ! ( position instanceof NoSuchPosition );
     *           position = position.step ( -1L ) )
     *     {
     *         Field field = buffer.get ( position );
     *         ...
     *     }
     * </pre>
     *
     * <p>
     * Because Positions refer to the Buffer's Region, Buffer
     * implementors should take care not to build up large
     * Region data structures.  For example, storing a Buffer's
     * Fields in the Region is a no-no.  However storing
     * and index in the Region, containing a lookup of
     * Markers-to-OffsetFromPositions to locate RecordStart and
     * RecordEnd positions quickly, might be acceptable.
     * </p>
     *
     * @return The Region describing this Buffer's size and
     *         structure.  Never null.
     */
    public abstract Region region ();


    /**
     * <p>
     * Writes the nth field to this buffer.
     * </p>
     *
     * <p>
     * Typically this method is used to overwrite the contents of
     * a specified Field or write a new Field to a specific position
     * in the Buffer.
     * </p>
     *
     * <p>
     * When a Field is set to null at the beginning or middle of
     * the Buffer, a hole is left in the Buffer.  This hole can be
     * removed by calling
     * <code> BufferTools.compact ( buffer ) </code>, if desired.
     * </p>
     *
     * @param position The position within this buffer.
     *                 Must not be null.  Must not be a NoSuchPosition.
     *                 Must be a valid position within this Buffer.
     *
     * @param field The field to store at the specified position.
     *              Can be null.
     *
     * @return This Buffer.  Never null.
     *
     * @throws BufferOverflowException if the specified position is
     *         beyond the capacity of this buffer.
     */
    public abstract Buffer set (
                                Position position,
                                Field field
                                )
        throws BufferException;
}
