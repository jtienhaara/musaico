package musaico.field;


import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.markers.LabelMarker;
import musaico.io.markers.RecordEnd;
import musaico.io.markers.RecordStart;

import musaico.types.Instance;
import musaico.types.RuntimeTypeException;
import musaico.types.Tags;
import musaico.types.Type;
import musaico.types.TypeException;
import musaico.types.TypingEnvironment;


/**
 * <p>
 * Represents a simple atomic field (identifier and value).
 * </p>
 *
 * <p>
 * Construct the SimpleField with an identifier and an
 * Instance of some Type as the value, and then use the
 * Instance methods directly on the SimpleField.
 * </p>
 *
 * <p>
 * For example, to create a <code>String</code> SimpleField,
 * then cast it to a long:
 * </p>
 *
 * <pre>
 *     Field my_field = new SimpleField ( "my_id", "12345" );
 *     long value = my_field.value ( Long.class ).longValue ();
 * </pre>
 *
 * <p>
 * Note that the underlying TypeSystem is responsible for
 * deciding how to deal with type exceptions (such as
 * invalid casts).  Some TypeSystems might be configured
 * to simply log error messages and do their best to
 * carry on, while other TypeSystems will throw
 * <code>RuntimeTypeException</code>s every time anything
 * goes awry.  If your code must handle exceptions, you can
 * either configure the exception handler of the TypeSystem
 * you use, or explicitly catch <code>RuntimeTypeException</code>s,
 * or both.
 * </p>
 *
 *
 * <p>
 * Platforms may impose additional tags and constraints on Fields.
 * </p>
 *
 * <p>
 * In Java, it is recommended that Fields and their contents
 * be made Serializable whenever possible, in order to play
 * nicely over RMI.
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
public class SimpleField
    implements Field, Serializable
{
    /** The identifier for this SimpleField. */
    private final String id;

    /** The instance which provides all the functionality for
     *  the value of this SimpleField.  Stores the value, peforms
     *  casts, tags the value with metadata and constraints, and
     *  so on. */
    private Instance instance;


    /**
     * <p>
     * Creates a SimpleField with the specified initial id, type and value.
     * </p>
     */
    public 
        <RAW_VALUE extends Object,STORAGE_VALUE extends Object>
            SimpleField (
                         String id,
                         TypingEnvironment typing,
                         Type<STORAGE_VALUE> type,
                         RAW_VALUE value
                         )
        throws TypeException
    {
        this ( id, typing.create ( type, value ) );
    }


    /**
     * <p>
     * Creates a SimpleField with the specified initial id and value.
     * </p>
     */
    public 
        <RAW_VALUE extends Object,STORAGE_VALUE extends Object>
            SimpleField (
                         String id,
                         TypingEnvironment typing,
                         RAW_VALUE value
                         )
        throws TypeException
    {
        this ( id, typing.create ( value ) );
    }


    /**
     * <p>
     * Creates a SimpleField with the specified initial id and value.
     * </p>
     */
    public 
        <RAW_VALUE extends Object,STORAGE_VALUE extends Object>
            SimpleField (
                         String id,
                         Instance instance
                         )
        throws TypeException
    {
        if ( id == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create Field with id = [%id%] and instance = [%instance%]",
                                                     "id", null,
                                                     "instance", instance );
        }
        else if ( instance == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create Field with id = [%id%] and instance = [%instance%]",
                                                     "id", id,
                                                     "instance", null );
        }

        this.id = id;
        this.instance = instance;
    }


    /**
     * @see musaico.field.Field#id()
     */
    public String id ()
    {
        return this.id;
    }


    /**
     * @see musaico.types.Instance#isa(java.lang.Class)
     */
    public boolean isa (
                        Class<Type<?>> type_class
                        )
    {
        return this.instance.isa ( type_class );
    }


    /**
     * @see musaico.types.Instance#tags()
     */
    public Tags tags ()
    {
        return this.instance.tags ();
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString ()
    {
        Object value = this.value ();
        if ( value == null )
        {
            return "" + this.id () + " = (" + this.type () + ") null";
        }
        else if ( value instanceof RecordStart )
        {
            return "" + this.id () + " = {";
        }
        else if ( value instanceof RecordEnd )
        {
            RecordEnd record_end = (RecordEnd) value;
            return "}";
        }
        else if ( value instanceof LabelMarker )
        {
            LabelMarker label = (LabelMarker) value;
            return ""+ this.id () + " @" + label;
        }
        else if ( value instanceof String )
        {
            // Not quote-safe.
            return "" + this.id () + " = \"" + this.value () + "\"";
        }
        else if ( value instanceof Number )
        {
            return "" + this.id () + " = " + this.value ();
        }
        else if ( value instanceof byte[] )
        {
            StringBuilder sbuf = new StringBuilder ();
            sbuf.append ( "{" );
            byte [] bytes = (byte []) value;
            for ( int b = 0; b < bytes.length; b ++ )
            {
                sbuf.append ( " 0x" );
                sbuf.append ( Integer.toHexString ( (int) bytes [ b ] ) );
            }

            if ( bytes.length > 0 )
            {
                sbuf.append ( " " );
            }

            sbuf.append ( "}" );

            return sbuf.toString ();
        }
        else
        {
            return "" + this.id () + " = (" + this.type () + ") "
                + this.value ();
        }
    }


    /**
     * @see musaico.types.Instance#type()
     */
    public 
        <STORAGE_VALUE extends Object>
                               Type<STORAGE_VALUE> type ()
    {
        return this.instance.type ();
    }


    /**
     * @see musaico.types.Instance#validate()
     */
    public boolean validate ()
        throws RuntimeTypeException
    {
        return this.instance.validate ();
    }


    /**
     * @see musaico.types.Instance#value()
     */
    public Object value ()
    {
        return this.instance.value ();
    }


    /**
     * @see musaico.types.Instance#value(java.lang.Class)
     */
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
