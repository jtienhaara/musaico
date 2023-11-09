package musaico.buffer;


import musaico.field.Field;

import musaico.io.Comparison;
import musaico.io.Order;
import musaico.io.Reference;

import musaico.region.Criterion;
import musaico.region.Position;
import musaico.region.Region;
import musaico.region.Search;
import musaico.region.Size;
import musaico.region.Space;


/**
 * <p>
 * Provides Buffer internals-independent tools for manipulating
 * Buffers.
 * </p>
 *
 * <p>
 * Methods such as <code> append () </code>, <code> find ()</code>,
 * <code> remove () </code>, and so on are all commonly used for
 * manipulating buffers.  However since they depend on lower-level
 * Buffer internals (<code> get () </code> and <code> set () </code>
 * and so on), they are coded independently.  This ensures the
 * Buffers and (more importantly) the tools can be varied easily.
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
public class BufferTools
{
    /**
     * <p>
     * Appends the field at the specified Position, returning the
     * new Position (offset by 1).
     * </p>
     *
     * <p>
     * Throws a BufferOverflowException if the Buffer's capacity
     * has been reached.
     * </p>
     *
     * <p>
     * If there is already a Field in the specified Position then
     * the next Position after the specified one is written instead;
     * or if that is also not empty, the following Position; and so on.
     * </p>
     *
     * @param buffer The Buffer to append to.  Must not be null.
     *
     * @param position The position from which to append.
     *                 Typically an EndPosition or
     *                 <code> buffer.region ().end () </code>.
     *                 Must not be null.
     *
     * @param field The field to append to the buffer.
     *              Must not be null.
     *
     * @return The position at which the field was appended.
     *         Never null.
     *
     * @throws BufferException If the field could not be appended
     *                         (for example because the buffer
     *                         was out of room).
     */
    public static Position append (
                                   Buffer buffer,
                                   Position position,
                                   Field field
                                   )
        throws BufferException
    {
        // Find the first empty position.
        Region region = buffer.region ();
        final Position original_position = position;
        position = region.expr ( position ).next ();
        final Position out_of_bounds = region.space ().outOfBounds ();
        while ( ! ( position.equals ( out_of_bounds ) ) )
        {
            if ( buffer.get ( position ) == Field.NULL )
            {
                // Here we go, this is the position to append at.
                break;
            }

            position = region.expr ( position ).next ();
        }

        if ( position.equals ( out_of_bounds ) )
        {
            throw new BufferException ( "Nowhere to append field [%field%] to buffer [%buffer%] on or after position [%position%]",
                                        "field", field,
                                        "buffer", buffer,
                                        "position", original_position );
        }

        // Store the Buffer returned in case this buffer is immutable.
        // Throw an exception if the position doesn't work.
        buffer = buffer.set ( position, field );

        return position;
    }


    /**
     * <p>
     * Removes all Fields from the Buffer.
     * </p>
     *
     * @param buffer The Buffer to empty out.  Must not be null.
     *
     * @throws BufferException If anything goes horribly wrong.
     */
    public static void clear (
                              Buffer buffer
                              )
        throws BufferException
    {
        Region region = buffer.region ();
        for ( Position position : region )
        {
            // Store the return Buffer in case this buffer is immutable.
            buffer = buffer.set ( position, null );
        }
    }


    /**
     * <p>
     * Compacts the buffer, removing any and all "holes" (null Fields)
     * from the beginning or middle of the Buffer.
     * </p>
     *
     * <p>
     * Idea: move fields to earlier in the buffer,
     * when holes are found.
     * </p>
     *
     * <pre>
     *     Buffer before: [abcd---efgh---]
     *     Buffer after:  [abcdefgh------]
     * </pre>
     *
     * <pre>
     *     Buffer before: [abcd---efgh---]
     *                         ^  ||||
     *                         |__||||
     *                          ^  |||
     *                          |__||| Move Fields
     *                           ^  ||
     *                           |__||
     *                            ^  |
     *                            |__|
     * </pre>
     *
     * <pre>
     *     Buffer after:  [abcdefgh------]
     *                             ^
     *                             |
     *                     Position returned
     * </pre>
     *
     * @param buffer The Buffer to compact.  Must not be null.
     *
     * @return The Position of the first null Field in the Buffer,
     *         or <code> buffer.space ().outOfBounds () </code>
     *         if the Buffer is entirely full.
     *         Regardless of whether or not any
     *         compaction was done, this Position always points
     *         to the (last non-null field + 1 ) position in the
     *         buffer (if any).  Never null.
     *
     * @throws BufferException If anything goes horribly wrong.
     */
    public static Position compact (
                                    Buffer buffer
                                    )
        throws BufferException
    {
        // Idea: move fields to earlier in the buffer,
        // when holes are found.
        //
        //     Buffer before: [abcd---efgh---]
        //                         ^  ||||
        //                         |__||||
        //                          ^  |||
        //                          |__|||
        //                           ^  ||
        //                           |__||
        //                            ^  |
        //                            |__|
        //
        //     Buffer after:  [abcdefgh------]
        //                             ^
        //                             |
        //                     Position returned
        Position hole_start = null;
        Position hole_end = null;
        Region region = buffer.region ();
        // Step through the buffer looking for holes.
        for ( Position position : region )
        {
            Field field = buffer.get ( position );

            if ( field.equals ( Field.NULL ) )
            {
                // This position is part of a hole.
                if ( hole_start == null )
                {
                    // Start of a new hole.
                    hole_start = position;
                    hole_end = position;
                }
                else
                {
                    // Continuation of a bigger hole.
                    hole_end = position;
                }
            }
            else
            {
                // This position is part of data.
                if ( hole_start != null )
                {
                    // Move the current field back to the start
                    // of the most recent hole.
                    // Blow up real good if there's a BufferException.
                    buffer = buffer.set ( hole_start, field );
                    buffer = buffer.set ( position, null );

                    hole_start = region.expr ( hole_start ).next ();
                    hole_end = position;
                }
                // Else no hole to fill.
            }
        }

        // The first empty position is the start of the hole,
        // if any was found in the data.
        if ( hole_start != null )
        {
            return hole_start;
        }
        else
        {
            return region.space ().outOfBounds ();
        }
    }


    /**
     * <p>
     * Copies the specified contents into the Buffer.
     * </p>
     *
     * <p>
     * Throws a BufferOverflowException if the Buffer's capacity
     * is exceeded.
     * </p>
     *
     * <p>
     * The Fields are copied into the Buffer, starting from
     * the specified target_position and overwriting any/all Fields
     * which were previously stored here.
     * </p>
     *
     * <p>
     * Null Fields will be copied.
     * </p>
     *
     * @param source_buffer The Buffer to copy from.  Must not be null.
     * @param source_region The layout and size of Fields to copy from the
     *                      source buffer.  Must not be null.
     *                      Must be a valid Region within the
     *                      source buffer.
     * @param target_buffer The target Buffer to copy to.  Must not be null.
     * @param target_region The layout and size of Fields to copy to the
     *                      target buffer.  Must not be null.
     *                      Must be a valid Region within the
     *                      target buffer.
     *
     * @return The Region of Fields copied into the target Buffer.
     *         Never null.  (May be 0-length.)
     *
     * @throws BufferOverflowException If the source contents exceed
     *                                 the capacity of the Buffer.
     *
     * @throws BufferException If any of the parameters are invalid,
     *                         or the regions are not sub-regions of their
     *                         corresponding buffers, and so on.
     */
    public static Region copy (
                               Buffer source,
                               Region source_region,
                               Buffer buffer,
                               Region target_region
                               )
        throws BufferException
    {
        if ( source == null
             || ( source instanceof NullBuffer )
             || source_region == null
             || buffer == null
             || ( buffer instanceof NullBuffer )
             || target_region == null )
        {
            throw new BufferException ( "Cannot copy fields from buffer [%from_buffer%] region [%from_region%] to buffer [%to_buffer%] region [%to_region%]",
                                        "from_buffer", source,
                                        "from_region", source_region,
                                        "to_buffer", buffer,
                                        "to_region", target_region );
        }

        // OK, let's get on with the copying.
        Position source_start = source_region.start ();
        Position source_end   = source_region.start ();
        Position target_start = target_region.start ();
        Position target_end   = target_region.start ();
        Size target_size = target_region.size ();
        Position sp;
        Position tp;
        Position first_target_position = target_region.space ().outOfBounds ();
        Position last_target_position = first_target_position;
        long num_fields_copied = 0L;
        Order<Position> source_order = source_region.space ().order ();
        Order<Position> target_order = target_region.space ().order ();
        for ( sp = source_start,
                  tp = target_start;
              source_region.contains ( sp )
                  && target_region.contains ( tp );
              sp = source_region.expr ( sp ).next (),
                  tp = target_region.expr ( tp ).next () )
        {
            Field field = source.get ( sp );
            buffer = buffer.set ( tp, field );

            if ( num_fields_copied == 0L )
            {
                first_target_position = tp;
            }

            last_target_position = tp;

            num_fields_copied ++;
        }

        // Return the region of fields copied to the target buffer.
        final Region copied_to_region;
        if ( num_fields_copied == 0L )
        {
            copied_to_region = target_region.space ().empty ();
        }
        else
        {
            copied_to_region =
                target_region.space ().region ( first_target_position,
                                                last_target_position );
        }

        return copied_to_region;
    }


    /**
     * <p>
     * Copies the specified Field array into the Buffer.
     * </p>
     *
     * <p>
     * Throws a BufferOverflowException if the Buffer's capacity
     * is exceeded.
     * </p>
     *
     * <p>
     * The Fields are copied into the Buffer, overwriting
     * any/all Fields which were previously stored there.
     * </p>
     *
     * <p>
     * Null Fields will be copied.
     * </p>
     *
     * @param source_fields The Field array to copy from.
     *                      Must not be null.  Can contain null elements.
     * @param target_buffer The target Buffer to copy to.  Must not be null.
     * @param target_region The layout and size of Fields to copy to the
     *                      target buffer.  Must not be null.
     *                      Must be a valid Region within the
     *                      target buffer.
     *
     * @return The Region of Fields copied into the target Buffer.
     *         Never null.  (May be 0-length.)
     *
     * @throws BufferOverflowException If the source contents exceed
     *                                 the capacity of the Buffer.
     *
     * @throws BufferException If any of the parameters are invalid,
     *                         or the regions are not sub-regions of their
     *                         corresponding buffers, and so on.
     */
    public static Region copyFromArray (
                                        Field [] fields,
                                        Buffer buffer,
                                        Region target_region
                                        )
        throws BufferException
    {
        if ( fields == null
             || buffer == null
             || ( buffer instanceof NullBuffer )
             || target_region == null )
        {
            throw new BufferException ( "Cannot copy fields from array [%from_array%] to buffer [%to_buffer%] region [%to_region%]",
                                        "from_array", fields,
                                        "to_buffer", buffer,
                                        "to_region", target_region );
        }

        // OK, let's get on with the copying.
        Position target_start = target_region.start ();
        Position target_end   = target_region.start ();
        Size target_size = target_region.size ();
        int sp;
        Position tp;
        Position first_target_position = target_region.space ().outOfBounds ();
        Position last_target_position = first_target_position;
        long num_fields_copied = 0L;
        for ( sp = 0,
                  tp = target_start;
              sp < fields.length
                  && target_region.contains ( tp );
              sp ++,
                  tp = target_region.expr ( tp ).next () )
        {
            Field field = fields [ sp ];
            buffer = buffer.set ( tp, field );

            if ( num_fields_copied == 0L )
            {
                first_target_position = tp;
            }

            last_target_position = tp;

            num_fields_copied ++;
        }

        // Return the region of fields copied to the target buffer.
        final Region copied_to_region;
        if ( num_fields_copied == 0L )
        {
            copied_to_region = target_region.space ().empty ();
        }
        else
        {
            copied_to_region =
                target_region.space ().region ( first_target_position,
                                                last_target_position );
        }

        return copied_to_region;
    }


    /**
     * <p>
     * Returns the first Field matching the specified criterion
     * in the specified region of the specified Buffer.
     * </p>
     *
     * <p>
     * For example, to retrieve the first "last_name" field
     * from a buffer:
     * </p>
     *
     * <pre>
     *     Buffer buffer = ...;
     *     Field field =
     *         BufferTools.findAndGet ( buffer,
     *                                  buffer.region (),
     *                                  new SpecificFieldID ( "last_name" ) );
     * </pre>
     *
     * <p>
     * If the specified Field cannot be found then Field.NULL is returned.
     * </p>
     *
     * <pre>
     *     if ( field == Field.NULL )
     *     {
     *         ...handle "no such field"...
     *     }
     * </pre>
     *
     * @param buffer The Buffer to search through.  Must not be null.
     *
     * @param region The region of the Buffer to search through.
     *               To search through the whole buffer, just use
     *               <code> buffer.region () </code>.
     *               Must not be null.  Must be a valid region of
     *               the specified Buffer.
     *
     * @param criterion The criterion to search for in the specified
     *                  Buffer.  Must not be null.
     *
     * @return The specified Field, or Field.NULL if no such field
     *         exists in the specified Region of the specified Buffer.
     *         Never null.
     */
    public static Field findAndGet (
                                    Buffer buffer,
                                    Region region,
                                    Criterion criterion
                                    )
    {
        if ( buffer == null
             || region == null
             || criterion == null )
        {
            return Field.NULL;
        }

        final Position found = region.search ( criterion ).find ();
        if ( ! buffer.region ().contains ( found ) )
        {
            // Out of bounds = not found.
            return Field.NULL;
        }

        Field field = buffer.get ( found );

        return field;
    }


    /**
     * <p>
     * Finds and removes a Field from the specified region of
     * the specified Buffer.
     * </p>
     *
     * <p>
     * For example, to remove the "last_name" field from a buffer:
     * </p>
     *
     * <pre>
     *     Buffer buffer = ...;
     *     Field removed_field =
     *         BufferTools.findAndRemove ( buffer,
     *                                     buffer.region (),
     *                                     new SpecificFieldID ( "last_name" ) );
     * </pre>
     *
     * @param buffer The Buffer to search and remove from.
     *               Must not be null.
     *
     * @param region The region of the Buffer to search through.
     *               To search the whole buffer, just use
     *               <code> buffer.region () </code>.
     *               Must not be null.  Must be a valid region
     *               of the specified buffer.
     *
     * @param criterion The criterion of field to find and remove
     *                  from the buffer.
     *                  Must not be null.
     *
     * @return The specified removed Field, or Field.NULL if no such field
     *         exists in the specified region of the specified buffer.
     *         Never null.
     *
     * @throws BufferException If the buffer misbehaves when the field
     *                         is removed.
     */
    public static Field findAndRemove (
                                       Buffer buffer,
                                       Region region,
                                       Criterion criterion
                                       )
        throws BufferException
    {
        if ( buffer == null
             || region == null
             || criterion == null )
        {
            return Field.NULL;
        }

        final Position found = region.search ( criterion ).find ();
        if ( ! buffer.region ().contains ( found ) )
        {
            // Out of bounds = nothing matched.
            return Field.NULL;
        }

        Field field = buffer.get ( found );

        // Now remove the field.
        // Store the result buffer in case this buffer is immutable.
        buffer = buffer.set ( found, null );

        return field;
    }


    /**
     * <p>
     * Outputs the whole buffer.  Possibly slow!
     * </p>
     *
     * @param buffer The Buffer to convert to a string.  Must not be null.
     *
     * @return A string representation of the Buffer.
     */
    public static String toString (
                                   Buffer buffer
                                   )
    {
        boolean is_comma_needed = false;
        StringBuilder sbuf = new StringBuilder ();
        Region region = buffer.region ();
        long null_count = 0L;
        for ( Position position : region )
        {
            boolean next_is_comma_needed = true;

            Field field = buffer.get ( position );
            if ( field == Field.NULL )
            {
                null_count ++;
                continue;
            }

            if ( null_count > 0L )
            {
                if ( is_comma_needed )
                {
                    sbuf.append ( ", " );
                }

                sbuf.append ( "null" );

                if ( null_count > 1L )
                {
                    sbuf.append ( " x " + null_count );
                }

                is_comma_needed = true;
            }

            null_count = 0L;

            String field_string = "" + field;

            if ( is_comma_needed )
            {
                if ( field_string.indexOf ( '}' ) == 0 )
                {
                    // No comma before a close brace.
                    sbuf.append ( " " );
                }
                else
                {
                        sbuf.append ( ", " );
                }
            }

            sbuf.append ( field_string );

            if ( field_string.lastIndexOf ( '{' )
                 == ( field_string.length () - 1 ) )
            {
                // No comma after a brace.
                next_is_comma_needed = false;
                sbuf.append ( " " );
            }

            is_comma_needed = next_is_comma_needed;
        }

        return "" + sbuf;
    }
}
