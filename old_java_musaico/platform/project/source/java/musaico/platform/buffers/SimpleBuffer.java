package musaico.buffer;


import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;


import musaico.i18n.exceptions.I18nIllegalArgumentException;
import musaico.i18n.exceptions.I18nIllegalStateException;

import musaico.io.Order;
import musaico.io.ReferenceCount;
import musaico.io.SimpleReferenceCount;

import musaico.field.Field;
import musaico.field.FieldTypingEnvironment;

import musaico.region.Position;
import musaico.region.Region;
import musaico.region.Size;
import musaico.region.Space;

import musaico.types.Instance;
import musaico.types.RuntimeTypeException;
import musaico.types.Tags;
import musaico.types.Type;
import musaico.types.TypeException;

import musaico.types.instances.SimpleInstance;


/**
 * <p>
 * A simple, in-memory array of Fields Buffer.
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
public class SimpleBuffer
    implements Buffer, Serializable
{
    /** Counter for identifiers. */
    private static long counter = -1L;


    /** The environment which we used to create Fields
     *  for this Buffer. */
    private final FieldTypingEnvironment environment;

    /** The Region defining the size and structure of this Buffer. */
    private final Region              region;

    /** The array of Fields. */
    private final Field []            fields;

    /** The Instance which takes care of typing and typecasting
     *  for us. */
    private final Instance            instance;

    /** A quick lookup of Field offsets by their identifiers.  Null until
     *  the first time find () is invoked.  Multiple fields can have
     *  the same id, so an array of offsets is stored per field id. */
    // !!! private Map<String,long[]>        fieldsLookupCache;

    /** Reference count to this Buffer. */
    private ReferenceCount            references;


    /**
     * <p>
     * Creates a new SimpleBuffer of the specified size and layout.
     * </p>
     *
     * <p>
     * By default a SimpleBuffer is of type BufferType.
     * </p>
     *
     * @param environment The environment for creating Fields for
     *                    this Buffer.  Must not be null.
     *
     * @param region The layout and size of this Buffer.  Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (see above).
     *
     * @throws I18nIllegalStateException If the typing environment
     *                                   is in a bad state.
     */
    public SimpleBuffer (
                         FieldTypingEnvironment environment,
                         Region region
                         )
        throws I18nIllegalArgumentException
    {
        this ( environment, region, new BufferType () );
    }


    /**
     * <p>
     * Creates a new SimpleBuffer of the specified size, layout
     * and type.
     * </p>
     *
     * <p>
     * The BufferType can be extended to provide additional
     * constraints and so on on a Buffer.
     * </p>
     *
     * @param environment The environment for creating Fields for
     *                    this Buffer.  Must not be null.
     *
     * @param region The layout and size of this Buffer.  Must not be null.
     *
     * @param buffer_type The type of Buffer this will be, such as
     *                    a plain vanilla BufferType.
     *                    Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (see above).
     *
     * @throws I18nIllegalStateException If the typing environment
     *                                   is in a bad state.
     */
    public SimpleBuffer (
                         FieldTypingEnvironment environment,
                         Region region,
                         BufferType buffer_type
                         )
        throws I18nIllegalArgumentException
    {
        if ( environment == null
             || region == null
             || buffer_type == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create new [%buffer_class%] with environment [%environment%] region [%region%] buffer type [%buffer_type%]",
                                                     "buffer_class", this.getClass ().getName (),
                                                     "environment", environment,
                                                     "region", region,
                                                     "buffer_type", buffer_type );
        }

        this.environment = environment;
        this.region = region;

        Size size = this.region.size ();
        Space space = this.region.space ();
        long num_positions = (long)
            space.expr ( size ).ratio ( space.one () );
        if ( num_positions > Integer.MAX_VALUE )
        {
            throw new I18nIllegalArgumentException ( "Cannot create new [%buffer_class%] with environment [%environment%] region [%region%] buffer type [%buffer_type%]",
                                                     "buffer_class", this.getClass ().getName (),
                                                     "environment", environment,
                                                     "region", region,
                                                     "buffer_type", buffer_type );
        }

        int capacity = (int) num_positions;

        this.fields = new Field [ capacity ];
        // !!! this.fieldsLookupCache = null;

        this.references = new SimpleReferenceCount ();

        try
        {
            this.instance = new SimpleInstance ( this.environment,
                                                 buffer_type,
                                                 this );
        }
        catch ( TypeException e )
        {
            throw new I18nIllegalStateException ( "Cannot create new [%buffer_class%] with environment [%environment%] region [%region%] buffer type [%buffer_type%]",
                                                  "buffer_class", this.getClass ().getName (),
                                                  "environment", environment,
                                                  "region", region,
                                                  "buffer_type", buffer_type,
                                                  "cause", e );
        }
    }


    /**
     * @see musaico.buffer.Buffer#environment()
     */
    @Override
    public FieldTypingEnvironment environment ()
    {
        return this.environment;
    }


    /**
     * @see musaico.buffer.Buffer#get(Position)
     *
     * Final for speed.
     */
    @Override
    public final Field get (
                            Position position
                            )
    {
        int index = this.indexOf ( position );
        if ( index < 0 )
        {
            // No such position.
            return Field.NULL;
        }

        Field field = this.fields [ index ];
        if ( field == null )
        {
            // Empty field.
            return Field.NULL;
        }
        else
        {
            return field;
        }
    }


    /**
     * <p>
     * Returns the array index of the specified position.
     * </p>
     *
     * @param position The position to find the index of.
     *                 Must not be null.
     *
     * @return The array index corresponding to the specified position,
     *         or a negative number if no such position exists
     *         in this buffer's region.  Always less than
     *         this.fields.length.
     *
     * <p>
     * Final for speed.
     * </p>
     */
    private final int indexOf (
                               Position position
                               )
    {
        if ( ! this.region.contains ( position ) )
        {
            // No such position in this buffer.
            return -1;
        }

        Space space = this.region.space ();
        long index = (long) space.expr ( position )
            .subtract ( this.region.start () )
            .ratio ( space.one () );

        if ( index < 0L
             || index >= (long) this.fields.length )
        {
            return -1;
        }

        return (int) index;
    }


    /**
     * @see musaico.types.Instance#isa(java.lang.Class)
     */
    @Override
    public boolean isa (
                        Class<Type<?>> type_class
                        )
    {
        return this.instance.isa ( type_class );
    }


    /**
     * @see musaico.buffer.Buffer#references()
     */
    @Override
    public ReferenceCount references ()
    {
        return this.references;
    }


    /**
     * @see musaico.buffer.Buffer#region()
     *
     * Final for speed.
     */
    @Override
    public final Region region ()
    {
        return this.region;
    }


    /**
     * @see musaico.buffer.Buffer#set(Position,Field)
     *
     * Final for speed.
     */
    @Override
    public final Buffer set (
                             Position position,
                             Field field
                             )
        throws BufferException
    {
        int index = this.indexOf ( position );
        if ( index < 0 )
        {
            throw new BufferOverflowException ( "Impossible field position [%position%] in buffer [%buffer%] region [%region%] while setting field [%field%]",
                                                "position", position,
                                                "buffer", this,
                                                "region", region,
                                                "field", field );
        }

        // Set the field.
        this.fields [ index ] = field;

        /* !!!
        // If this Buffer already has a lookup cache, add the specified
        // field to it.
        if ( this.fieldsLookupCache != null )
        {
            long [] old_fields_by_id =
                this.fieldsLookupCache.get ( field.id () );
            long [] new_fields_by_id;
            if ( old_fields_by_id == null )
            {
                new_fields_by_id = new long [ 1 ];
                new_fields_by_id [ 0 ] = position;
            }
            else
            {
                new_fields_by_id = new long [ old_fields_by_id.length + 1 ];
                int fpos = 0;
                for ( int o = 0; o < old_fields_by_id.length; o ++ )
                {
                    if ( position < old_fields_by_id [ o ] )
                    {
                        // Insert the new field in order.
                        new_fields_by_id [ fpos ] = position;
                        fpos ++;
                    }

                    new_fields_by_id [ fpos ] = old_fields_by_id [ o ];
                    fpos ++;
                }
            }

            this.fieldsLookupCache.put ( field.id (), new_fields_by_id );
        }
        !!! */

        return this;
    }


    /**
     * @see musaico.types.Instance#tags()
     */
    @Override
    public Tags tags ()
    {
        return this.instance.tags ();
    }


    /**
     * @see java.lang.Object#toString()
     *
     * Deliberately not final.  Anyone can override toString (),
     * if desired, for debugging and so on.
     */
    @Override
    public String toString ()
    {
        return "SimpleBuffer"
            + this.hashCode () + " [ " + this.region ().size () + " ]";
    }


    /**
     * @see musaico.types.Instance#type()
     */
    @Override
    public 
        <STORAGE_VALUE extends Object>
                               Type<STORAGE_VALUE> type ()
    {
        return this.instance.type ();
    }


    /**
     * @see musaico.types.Instance#validate()
     */
    @Override
    public boolean validate ()
        throws RuntimeTypeException
    {
        return this.instance.validate ();
    }


    /**
     * @see musaico.types.Instance#value()
     */
    @Override
    public Object value ()
    {
        return this.instance.value ();
    }


    /**
     * @see musaico.types.Instance#value(java.lang.Class)
     */
    @Override
    public 
        <RAW_VALUE extends Object,STORAGE_VALUE extends Object>
                           RAW_VALUE value (
                                            Class<RAW_VALUE> raw_class
                                            )
        throws RuntimeTypeException
    {
        return this.instance.value ( raw_class );
    }


    /**
     * @see musaico.types.Instance#value(musaico.types.Type)
     */
    @Override
    public 
        <STORAGE_VALUE extends Object>
                               STORAGE_VALUE value (
                                                    Type<STORAGE_VALUE> type
                                                    )
        throws RuntimeTypeException
    {
        return this.instance.value ( type );
    }


    /**
     * @see musaico.types.Instance#value(java.lang.Class,musaico.types.Type)
     */
    @Override
    public 
        <RAW_VALUE extends Object,STORAGE_VALUE extends Object>
                           RAW_VALUE value (
                                            Class<RAW_VALUE> raw_class,
                                            Type<STORAGE_VALUE> type
                                            )
        throws RuntimeTypeException
    {
        return this.instance.value ( raw_class, type );
    }
}
