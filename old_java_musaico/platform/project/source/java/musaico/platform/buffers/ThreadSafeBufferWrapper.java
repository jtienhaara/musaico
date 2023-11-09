package musaico.buffer;


import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.ReferenceCount;

import musaico.field.Field;
import musaico.field.FieldTypingEnvironment;

import musaico.region.Position;
import musaico.region.Region;
import musaico.region.Size;

import musaico.types.Instance;
import musaico.types.RuntimeTypeException;
import musaico.types.Tags;
import musaico.types.Type;


/**
 * <p>
 * Wraps a Buffer with a thread-safe wrapper.  This wrapper may
 * be invoked concurrently from multiple threads.
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
public class ThreadSafeBufferWrapper
    implements Buffer, Serializable
{
    /** The Buffer to wrap.  If the unsafe buffer is immutable
     *  then this will change every time set () is called. */
    private Buffer unsafe;

    /** The token to synchronize on for critical sections. */
    private final Serializable lock = new String ();


    /**
     * <p>
     * Creates a new ThreadSafeBufferWrapper for the specified Buffer.
     * </p>
     */
    public ThreadSafeBufferWrapper (
                                    Buffer unsafe_buffer
                                    )
    {
        if ( unsafe_buffer == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot wrap a null Buffer in a new [%buffer_class%]",
                                                     "buffer_class", this.getClass ().getName () );
        }

        this.unsafe = unsafe_buffer;
    }


    /**
     * @see musaico.buffer.Buffer#environment()
     */
    public FieldTypingEnvironment environment ()
    {
        return this.unsafe.environment ();
    }


    /**
     * @see musaico.buffer.Buffer#get(Position)
     */
    public Field get (
                      Position position
                      )
    {
        // Synchronize in case of iterators etc
        // in the underlying unsafe implementation.
        synchronized ( this.lock )
        {
            return this.unsafe ().get ( position );
        }
    }


    /**
     * @see musaico.types.Instance#isa(Class<Type<?>>)
     */
    @Override
    public boolean isa (
                        Class<Type<?>> type_class
                        )
    {
        // No need to lock.
        return this.unsafe ().isa ( type_class );
    }


    /**
     * @see musaico.buffer.Buffer#references()
     */
    public ReferenceCount references ()
    {
        return this.unsafe ().references ();
    }


    /**
     * @see musaico.buffer.Buffer#region()
     *
     * Final for speed.
     */
    public final Region region ()
    {
        // !!! If Region ever gets any "setter" methods
        // !!! then we'll need to wrap it, too.
        return this.unsafe ().region ();
    }


    /**
     * @see musaico.buffer.Buffer#set(Buffer,Field)
     */
    public Buffer set (
                       Position position,
                       Field field
                       )
        throws BufferException
    {
        // Synchronize in case of iterators etc
        // in the underlying unsafe implementation.
        synchronized ( this.lock )
        {
            Buffer unsafe_buffer = this.unsafe ();
            Buffer result_buffer =
                unsafe_buffer.set ( position, field );

            // If the unsafe buffer was immutable, it will return
            // a new Buffer.  We store that.
            if ( result_buffer != unsafe_buffer
                 && result_buffer != null )
            {
                this.unsafe = result_buffer;
            }
        }

        return this;
    }


    /**
     * @see musaico.types.Instance#tags()
     */
    @Override
    public Tags tags ()
    {
        // Synchronize just in case the tags for this buffer
        // change.
        synchronized ( this.lock )
        {
            return this.unsafe ().tags ();
        }
    }


    /**
     * @see musaico.types.Instance#type()
     */
    @Override
    public 
        <STORAGE_VALUE extends Object>
                               Type<STORAGE_VALUE> type ()
    {
        // No need to lock.
        return this.unsafe ().type ();
    }


    /**
     * <p>
     * Returns the unsafe Buffer protected from the perils of
     * careless multi-threading by this fearless ThreadSafeBufferWrapper.
     * Good luck in the cruel world, unsafe Buffer, and godspeed.
     * </p>
     *
     * @return The thread-unsafe buffer.  Never null.
     */
    public Buffer unsafe ()
    {
        return this.unsafe;
    }


    /**
     * @see musaico.types.Instance#validate()
     */
    @Override
    public boolean validate ()
        throws RuntimeTypeException
    {
        // Synchronize just in case the validity of this buffer
        // changes.
        synchronized ( this.lock )
        {
            return this.unsafe ().validate ();
        }
    }


    /**
     * @see musaico.types.Instance#value()
     */
    @Override
    public Object value ()
    {
        // Synchronize just in case the content of this buffer
        // changes.
        synchronized ( this.lock )
        {
            return this.unsafe ().value ();
        }
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
        // Synchronize while casting just in case the content of
        // this buffer changes.
        synchronized ( this.lock )
        {
            return this.unsafe ().value ( raw_class );
        }
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
        // Synchronize while casting just in case the content of
        // this buffer changes.
        synchronized ( this.lock )
        {
            return this.unsafe ().value ( type );
        }
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
        // Synchronize while casting just in case the content of
        // this buffer changes.
        synchronized ( this.lock )
        {
            return this.unsafe ().value ( raw_class, type );
        }
    }
}
