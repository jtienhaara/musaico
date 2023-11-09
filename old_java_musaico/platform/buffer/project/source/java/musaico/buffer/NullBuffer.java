package musaico.buffer;


import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;
import musaico.i18n.exceptions.I18nIllegalStateException;

import musaico.io.ReferenceCount;
import musaico.io.SimpleReferenceCount;

import musaico.field.Field;
import musaico.field.FieldTypingEnvironment;
import musaico.field.SimpleFieldTypingEnvironment;

import musaico.region.Position;
import musaico.region.Region;

import musaico.region.array.ArraySpace;

import musaico.types.Instance;
import musaico.types.NoTypingEnvironment;
import musaico.types.RuntimeTypeException;
import musaico.types.SimpleTags;
import musaico.types.Tags;
import musaico.types.Type;
import musaico.types.TypeException;

import musaico.types.instances.SimpleInstance;


/**
 * <p>
 * Represents an empty read-only Buffer.
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
public class NullBuffer
    implements Buffer, Serializable
{
    /** The definition of this buffer's size and structure. */
    private final Region              region;

    /** Reference count to this Buffer. */
    private final ReferenceCount      references;

    /** The Instance which does our Musaico typing work for us. */
    private final Instance            instance;


    /**
     * <p>
     * Creates a new NullBuffer.
     * </p>
     *
     * @throws I18nIllegalStateException If the underlying typing
     *                                   environment used by NullBuffer
     *                                   has a bug.
     */
    public NullBuffer ()
    {
        this.region = ArraySpace.STANDARD.empty ();
        this.references = new SimpleReferenceCount ();

        try
        {
            this.instance = new SimpleInstance ( NoTypingEnvironment.SINGLETON,
                                                 new BufferType (),
                                                 this );
        }
        catch ( TypeException e )
        {
            throw new I18nIllegalStateException ( "Bug in NullBuffer underlying typing constructs",
                                                  "cause", e );
        }
    }


    /**
     * @see musaico.buffer.Buffer#environment()
     */
    public FieldTypingEnvironment environment ()
    {
        return new SimpleFieldTypingEnvironment ();
    }


    /**
     * @see musaico.buffer.Buffer#get(Position)
     */
    public final Field get (
                            Position position
                            )
    {
        // No fields.
        return Field.NULL;
    }


    /**
     * @see musaico.types.Instance#isa(Class<Type<?>>)
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
    public ReferenceCount references ()
    {
        return this.references;
    }


    /**
     * @see musaico.buffer.Buffer#region()
     *
     * Final for speed.
     */
    public final Region region ()
    {
        return this.region;
    }


    /**
     * @see musaico.buffer.Buffer#set(Position,Field)
     */
    public final Buffer set (
                             Position position,
                             Field field
                             )
        throws BufferException
    {
        throw new BufferException( "Cannot set field at position [%position%] to [%field%] in read-only [%buffer_class%] [%buffer%]",
                                   "position", position,
                                   "field", field,
                                   "buffer_class", this.getClass ().getName (),
                                   "buffer", this );
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
     * @see musaico.types.Instance#value(Class<RAW_VALUE>)
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
     * @see musaico.types.Instance#value(Type<STORAGE_VALUE>)
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
     * @see musaico.types.Instance#value(Class<RAW_VALUE>,Type<STORAGE_TYPE>)
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
