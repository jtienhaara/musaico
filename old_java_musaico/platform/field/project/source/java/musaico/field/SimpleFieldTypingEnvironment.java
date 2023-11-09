package musaico.field;


import java.io.Serializable;


import musaico.types.Instance;
import musaico.types.RuntimeTypeException;
import musaico.types.SimpleTypingEnvironment;
import musaico.types.Type;
import musaico.types.TypeException;


/**
 * <p>
 * Simple registry of Type representations, by raw data type Class, plus
 * facilities to create Fields.
 * </p>
 *
 *
 * <p>
 * In Java, every TypingEnvironment must be Serializable in order
 * to play nicely across RMI, even if the Instances it covers
 * are not themselves Serializable.
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
public class SimpleFieldTypingEnvironment
    extends SimpleTypingEnvironment
    implements FieldTypingEnvironment, Serializable
{
    /**
     * @see musaico.field.FieldTypingEnvironment#create(String,RAW_VALUE)
     */
    public 
        <RAW_VALUE extends Object,STORAGE_VALUE extends Object>
                           Field create (
                                         String id,
                                         RAW_VALUE value
                                         )
        throws RuntimeTypeException
    {
        try
        {
            final Instance value_instance = this.create ( value );
            final Field field;
            field = new SimpleField ( id, value_instance );

            return field;
        }
        catch ( RuntimeTypeException type_exception )
        {
            throw type_exception;
        }
        catch ( Throwable t )
        {
            this.exceptionHandler ().handle ( t );

            // If the exception handler didn't throw a
            // RuntimeTypeException, then just return null.
            return null;
        }
    }


    /**
     * @see musaico.field.FieldTypingEnvironment#create(String,Class<RAW_CLASS>,RAW_VALUE)
     */
    public 
        <RAW_VALUE extends Object,STORAGE_VALUE extends Object>
                           Field create (
                                         String id,
                                         Class<RAW_VALUE> raw_class,
                                         RAW_VALUE value
                                         )
        throws RuntimeTypeException
    {
        try
        {
            final Instance value_instance = this.create ( raw_class, value );
            final Field field = new SimpleField ( id, value_instance );

            return field;
        }
        catch ( RuntimeTypeException type_exception )
        {
            throw type_exception;
        }
        catch ( Throwable t )
        {
            this.exceptionHandler ().handle ( t );

            // If the exception handler didn't throw a
            // RuntimeTypeException, then just return null.
            return null;
        }
    }


    /**
     * @see musaico.field.FieldTypingEnvironment#create(String,Type<STORAGE_VALUE>,RAW_VALUE)
     */
    public 
        <RAW_VALUE extends Object,STORAGE_VALUE extends Object>
                           Field create (
                                         String id,
                                         Type<STORAGE_VALUE> type,
                                         RAW_VALUE value
                                         )
        throws RuntimeTypeException
    {
        try
        {
            final Instance value_instance = this.create ( type, value );
            final Field field = new SimpleField ( id, value_instance );

            return field;
        }
        catch ( RuntimeTypeException type_exception )
        {
            throw type_exception;
        }
        catch ( Throwable t )
        {
            this.exceptionHandler ().handle ( t );

            // If the exception handler didn't throw a
            // RuntimeTypeException, then just return null.
            return null;
        }
    }


    /**
     * @see musaico.field.FieldTypingEnvironment#duplicateField(Field)
     */
    public Field duplicateField (
                                 Field field
                                 )
        throws RuntimeTypeException
    {
        final String id    = field.id ();
        final Type<?> type = field.type ();
        final Object value = field.value ();

        final Field new_field = this.create ( id, type, value );

        return new_field;
    }
}
