package musaico.types.environments;


import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;


import musaico.condition.Conditional;
import musaico.condition.Failed;
import musaico.condition.Successful;

import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.types.Instance;
import musaico.types.Tag;
import musaico.types.Type;
import musaico.types.TypeCaster;
import musaico.types.TypeException;
import musaico.types.TypingEnvironment;


/**
 * <p>
 * Represents an instance of some Type.
 * </p>
 *
 * <p>
 * For example, text Strings, integer numbers, dates and times,
 * as well as more complex objects, are all types which can be
 * represented as SimpleInstance objects and cast dynamically
 * to other types.
 * </p>
 *
 *
 * <p>
 * In Java, every SimpleInstance must be Serializable in order
 * to play nicely with RMI.
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
 * Copyright (c) 2009, 2012 Johann Tienhaara
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
public class SimpleInstance
    implements Instance, Serializable
{
    /** The environment in which this instance was created. */
    private final TypingEnvironment environment;

    /** The type of this instance (StringType, FooType, etc). */
    private final Type<Object> type;

    /** The default RAW_VALUE of this SimpleInstance (a String,
     *  Integer, some complex object, and so on). */
    // !!! PROBLEM FOR RMI???  (NOT SERIALIZABLE.)  CHECK!!!!
    private final Object value;

    /** Tags applicable just to this SimpleInstance.
     *  All instance-specific Constraints are always contained in this set. */
    private final Tag [] tags;


    /**
     * <p>
     * Creates a SimpleInstance with the specified value.
     * </p>
     *
     * <p>
     * The value may not ever change.  However casts from the initial
     * Type to other Types may be cached in derivatives of
     * the SimpleInstance.
     * </p>
     *
     * @param environment The typing environment in which this instance
     *                    is being created.  Determines the other types
     *                    and tags available for casting and so on.
     *                    Must not be null.
     *
     * @param type The type of this instance, such as an integer type
     *             or a string type or a domain-specific type and
     *             so on.  Determines what values this instance
     *             may have, and which type casters are available
     *             to cast this instance to other types (such as
     *             from an integer to a string, and so on).
     *             Must not be null.
     *
     * @param value The raw value of this instance, such as 5 for
     *              a numeric instance, or "foobar" for a string
     *              instance, and so on.  Must be valid for the
     *              specified type.  Must not be null.
     *
     * @param tags Zero or more tags applying additional restrictions
     *             to the value of this type and its typecasters and
     *             so on.  For exampole, a privacy tag might prevent
     *             anyone from retrieving the value or casting it.
     *             May be zero-length.  Must not contain any null
     *             elements.  Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     *
     * @throws TypeException If the type or tags do not allow
     *                       the specified value.  For example, passing
     *                       a NaN value to certain numeric types
     *                       might cause a TypeException; or passing
     *                       a 4 character String for a type that
     *                       requires at least 10 characters might
     *                       induce a TypeExceptin; and so on.
     */
    public 
        <STORAGE_VALUE extends Object, RAW_VALUE extends STORAGE_VALUE>
            SimpleInstance (
                            TypingEnvironment environment,
                            Type<STORAGE_VALUE> type,
                            RAW_VALUE value,
                            Tag... tags
                            )
        throws TypeException,
               I18nIllegalArgumentException
    {
        boolean is_parameters_ok = true;
        if ( environment == null
             || type == null
             || value == null
             || tags == null )
        {
            is_parameters_ok = false;
        }
        else
        {
            for ( Tag tag : tags )
            {
                if ( tag == null )
                {
                    is_parameters_ok = false;
                    break;
                }
            }
        }

        if ( ! is_parameters_ok )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a SimpleInstance with environment [%environment%] type [%type%] value [%value%] tags [%tags%]",
                                                     "environment", environment,
                                                     "type", type,
                                                     "value", value,
                                                     "tags", tags );
        }

        this.environment = environment;
        this.type = (Type<Object>) type;
        this.value = value;

        this.tags = new Tag [ tags.length ];
        System.arraycopy ( tags, 0,
                           this.tags, 0, this.tags.length );

        this.type.check ( this ); // Throws TypeException.
        for ( Tag tag : this.tags )
        {
            tag.check ( this ); // Throws TypeException.
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
        if ( type_class == null )
        {
            return false;
        }

        Type<?> type_of_instance = this.type ();

        if ( type_of_instance == null )
        {
            return false;
        }

        return type_class.isInstance ( type_of_instance );
    }


    /**
     * @see musaico.types.Instance#tags()
     */
    @Override
    public Tag [] tags ()
    {
        Tag [] tags = new Tag [ this.tags.length ];
        System.arraycopy ( this.tags, 0,
                           tags, 0, tags.length );
        return tags;
    }


    /**
     * <p>
     * Returns a String representation of this SimpleInstance.
     * </p>
     */
    public String toString ()
    {
        return "SimpleInstance(" + this.value () + " : " + this.type () + ")";
    }


    /**
     * @see musaico.types.Instance#type()
     */
    @Override
    public 
        <STORAGE_VALUE extends Object>
            Type<STORAGE_VALUE> type ()
    {
        return (Type<STORAGE_VALUE>) this.type;
    }


    /**
     * @see musaico.types.Instance#validate()
     */
    @Override
    public void validate ()
        throws TypeException
    {
        // Validate all the tags against this SimpleInstance.
        // Include SimpleInstance tags, Type tags,
        // TypeSystem tags, and so on.
        // If any fail, invoke our environment's exception handler.
        for ( Tag tag : this.environment.tags ( this ) )
        {
            tag.check ( this );
        }

        // All applicable tags validated this SimpleInstance.
    }


    /**
     * @see musaico.types.Instance#value()
     */
    @Override
    public Object value ()
    {
        return this.value;
    }


    /**
     * @see musaico.types.Instance#value(java.lang.Class)
     */
    @Override
    public final 
        <RAW_VALUE extends Object>
            Conditional< RAW_VALUE, TypeException >
                value (
                       Class< RAW_VALUE > raw_class
                       )
    {
	// Simple instanceof check first.
	if ( raw_class != null
	     && raw_class.isInstance ( this.value ) )
	{
	    RAW_VALUE raw_value = (RAW_VALUE) this.value;
            Conditional< RAW_VALUE, TypeException > conditional =
                new Successful< RAW_VALUE, TypeException > ( raw_class,
                                                             raw_value );
            return conditional;
	}

        TypingEnvironment environment = this.environment;
        Type<Object> type = environment.type ( raw_class );

        if ( type == null )
        {
            TypeException ex = new TypeException ( "Cannot retrieve the value of instance [%instance%]: No type representing raw [%raw_class%] in typing environment [%typing_environment%]",
                                                   "instance", this,
                                                   "raw_class", raw_class,
                                                   "typing_environment", environment );

            Conditional< RAW_VALUE, TypeException > conditional =
                new Failed< RAW_VALUE, TypeException > ( raw_class,
                                                         ex,
                                                         Type.NONE.none () );
            return conditional;
        }

        return this.value ( raw_class, type );
    }


    /**
     * @see musaico.types.Instance#value(musaico.types.Type)
     */
    @Override
    public 
        <STORAGE_VALUE extends Object>
            Conditional< STORAGE_VALUE, TypeException >
                value (
                       Type<STORAGE_VALUE> type
                       )
    {
        return this.value ( type.storageClass (), type );
    }


    /**
     * @see musaico.types.Instance#value(java.lang.class, musaico.types.Type)
     */
    @Override
    public 
        <STORAGE_VALUE extends Object, RAW_VALUE extends STORAGE_VALUE>
            Conditional< RAW_VALUE, TypeException >
                value (
                       Class<RAW_VALUE> raw_class,
                       Type<STORAGE_VALUE> type
                       )
    {
        // Cast to the specified Type.
        TypingEnvironment environment =
            this.environment;
        Conditional< RAW_VALUE, TypeException > conditional =
            environment.cast ( this, type, raw_class );

        return conditional;
    }
}
