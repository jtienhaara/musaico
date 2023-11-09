package musaico.kernel.memory.paging.buffer;


import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.ReferenceCount;

import musaico.buffer.Buffer;
import musaico.buffer.BufferException;

import musaico.field.Field;
import musaico.field.FieldTypingEnvironment;

import musaico.kernel.memory.paging.KernelPaging;
import musaico.kernel.memory.paging.Page;

import musaico.region.Position;
import musaico.region.Region;
import musaico.region.Size;

import musaico.types.Instance;
import musaico.types.RuntimeTypeException;
import musaico.types.Tags;
import musaico.types.Type;
import musaico.types.TypeException;


/**
 * <p>
 * Wraps a Buffer with page access routins, which tell the kernel's
 * least-recently-used list about updates, set dirty flags, and so on.
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
public class PageAccessBufferWrapper
    implements Buffer, Serializable
{
    /** The raw underlying buffer.  Can't be final, in case it is
     *  an immutable implementation which returns a new Buffer
     *  each time its <code> set () </code> method is called. */
    private Buffer rawBuffer;

    /** The kernel's least-recently-used list and clean/dirty flags. */
    private final KernelPaging kernelPaging;

    /** The Page to wrap. */
    private final Page page;


    /**
     * <p>
     * Creates a new PageAccessBufferWrapper for the specified Page
     * and kernel's LRU.
     * </p>
     *
     * @param raw_buffer The underlying raw buffer containing the
     *                   data which this wrapper proxies.  Must not be null.
     *
     * @param kernel_paging The kernel's LRU and clean/dirty page lists.
     *                      Must not be null.
     *
     * @param page The page to dirty for every set, and move to the front
     *             of the LRU every time get/set is called.
     *             Must not be null.
     */
    public PageAccessBufferWrapper (
                                    Buffer raw_buffer,
                                    KernelPaging kernel_paging,
                                    Page page
                                    )
    {
        if ( raw_buffer == null
             || kernel_paging == null
             || page == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a PageAccessBufferWrapper with raw buffer [%raw_buffer%] page [%page%] kernel paging [%kernel_paging%]",
                                                     "raw_buffer", raw_buffer,
                                                     "page", page,
                                                     "kernel_paging", kernel_paging );
        }

        this.rawBuffer = raw_buffer;
        this.kernelPaging = kernel_paging;
        this.page = page;
    }


    /**
     * @see musaico.buffer.Buffer#environment()
     *
     * Final for speed.
     */
    public final FieldTypingEnvironment environment ()
    {
        return this.rawBuffer.environment ();
    }


    /**
     * @see musaico.buffer.Buffer#get(Position)
     *
     * Final for speed.
     */
    public final Field get (
                            Position position
                            )
    {
        this.kernelPaging.recent ( this.page );
        return this.rawBuffer.get ( position );
    }


    /**
     * @see musaico.types.Instance#isa(java.lang.Class)
     */
    @Override
    public boolean isa (
                        Class<Type<?>> type_class
                        )
    {
        return this.rawBuffer.isa ( type_class );
    }


    /**
     * <p>
     * Returns the underlying raw buffer.
     * </p>
     *
     * @return The raw underlying buffer.  Never null.
     */
    public Buffer raw ()
    {
        return this.rawBuffer;
    }


    /**
     * @see musaico.buffer.Buffer#references()
     *
     * Final for speed.
     */
    public final ReferenceCount references ()
    {
        // Count references to the page instead of to the
        // underlying raw buffer.
        return this.page.referenceCount ();
    }


    /**
     * @see musaico.buffer.Buffer#region()
     *
     * Final for speed.
     */
    public final Region region ()
    {
        return this.rawBuffer.region ();
    }


    /**
     * @see musaico.buffer.Buffer#set(Buffer,Field)
     *
     * Final for speed.
     */
    public final Buffer set (
                             Position position,
                             Field field
                             )
        throws BufferException
    {
        this.kernelPaging.recent ( this.page );

        // Replace our raw buffer with the new one, in case
        // the implementation is immutable.
        this.rawBuffer = this.rawBuffer.set ( position, field );

        this.kernelPaging.dirty ( this.page );

        return this.rawBuffer;
    }


    /**
     * @see musaico.types.Instance#tags()
     */
    @Override
    public Tags tags ()
    {
        return this.rawBuffer.tags ();
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
        return this.rawBuffer.type ();
    }


    /**
     * @see musaico.types.Instance#validate()
     */
    @Override
    public boolean validate ()
        throws RuntimeTypeException
    {
        return this.rawBuffer.validate ();
    }


    /**
     * @see musaico.types.Instance#value()
     */
    @Override
    public Object value ()
    {
        Object buffer_value = this.rawBuffer.value ();
        if ( buffer_value == this.rawBuffer )
        {
            return this;
        }
        else
        {
            return buffer_value;
        }
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
        RAW_VALUE cast_buffer = this.rawBuffer.value ( raw_class );
        if ( cast_buffer == this.rawBuffer )
        {
            if ( raw_class.isInstance ( this ) )
            {
                return (RAW_VALUE) this;
            }

            // Throw a TypeException to the typing exception handler.
            this.environment ().exceptionHandler ()
                .handle ( new TypeException ( "PageAccessBufferWrapper [%buffer%] cannot cast itself to [%type%] [%raw_class%]",
                                              "buffer", this,
                                              "type", "?",
                                              "raw_class", raw_class ) );

            // If the exception handler didn't throw a
            // RuntimeTypeException, return null.
            return null;
        }
        else
        {
            return cast_buffer;
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
        STORAGE_VALUE cast_buffer = this.rawBuffer.value ( type );
        if ( cast_buffer == this.rawBuffer )
        {
            if ( type.storageClass ().isInstance ( this ) )
            {
                return (STORAGE_VALUE) this;
            }

            // Throw a TypeException to the typing exception handler.
            this.environment ().exceptionHandler ()
                .handle ( new TypeException ( "PageAccessBufferWrapper [%buffer%] cannot cast itself to [%type%] [%raw_class%]",
                                              "buffer", this,
                                              "type", type,
                                              "raw_class", "?" ) );

            // If the exception handler didn't throw a
            // RuntimeTypeException, return null.
            return null;
        }
        else
        {
            return cast_buffer;
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
        RAW_VALUE cast_buffer = this.rawBuffer.value ( raw_class, type );
        if ( cast_buffer == this.rawBuffer )
        {
            if ( raw_class.isInstance ( this ) )
            {
                return (RAW_VALUE) this;
            }

            // Throw a TypeException to the typing exception handler.
            this.environment ().exceptionHandler ()
                .handle ( new TypeException ( "PageAccessBufferWrapper [%buffer%] cannot cast itself to [%type%] [%raw_class%]",
                                              "buffer", this,
                                              "type", type,
                                              "raw_class", raw_class ) );

            // If the exception handler didn't throw a
            // RuntimeTypeException, return null.
            return null;
        }
        else
        {
            return cast_buffer;
        }
    }
}
