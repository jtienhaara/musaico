package musaico.field;


import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.types.Instance;
import musaico.types.RuntimeTypeException;
import musaico.types.SimpleTags;
import musaico.types.Tags;
import musaico.types.Type;
import musaico.types.TypeException;
import musaico.types.TypingEnvironment;


/**
 * <p>
 * Empty / null field.
 * </p>
 *
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
public class NullField
    implements Field, Serializable
{
    /** The standard identifier for NullField. */
    private final String id = "NULL";


    /**
     * <p>
     * Creates a new NullField.
     * </p>
     */
    public NullField ()
    {
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
        return false;
    }


    /**
     * @see musaico.types.Instance#tags()
     */
    public Tags tags ()
    {
        return new SimpleTags ();
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString ()
    {
        return this.id ();
    }


    /**
     * @see musaico.types.Instance#type()
     */
    public 
        <STORAGE_VALUE extends Object>
                               Type<STORAGE_VALUE> type ()
    {
        return Type.NULL;
    }


    /**
     * @see musaico.types.Instance#validate()
     */
    public boolean validate ()
        throws RuntimeTypeException
    {
        return false;
    }


    /**
     * @see musaico.types.Instance#value()
     */
    public Object value ()
    {
        // A null field's storage value is itself!
        return this;
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
        throw new RuntimeTypeException ( "Cannot cast null field to [%cast_to%]",
                                         "cast_to", raw_class );
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
        throw new RuntimeTypeException ( "Cannot cast null field to [%cast_to%]",
                                         "cast_to", type );
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
        throw new RuntimeTypeException ( "Cannot cast null field to [%cast_to%]",
                                         "cast_to", raw_class );
    }
}
