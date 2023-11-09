package musaico.types;


import java.io.Serializable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;

import java.util.LinkedHashMap;


import musaico.condition.Conditional;
import musaico.condition.Failed;
import musaico.condition.Successful;

import musaico.i18n.exceptions.I18nIllegalArgumentException;


/**
 * <p>
 * Basic data and functionality for most types.
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
public class SimpleType<STORAGE_VALUE extends Object>
    implements Type<STORAGE_VALUE>, Serializable
{
    /** The universal name of this type. */
    private final String name;

    /** The class in which data will be stored by instances
     *  of this type. */
    private final Class<STORAGE_VALUE> storageClass;

    /** The "no such value" value for this type, such as Float.NaN
     *  or some constant such as "NO TEXT" or a special instance
     *  of the storage class and so on. */
    private final STORAGE_VALUE none;

    /** The raw classes supported by this Type.  For example
     *  a numeric Type might support the raw classes Float.class,
     *  Decimal.class, Integer.class, BigDecimal.class and so on.
     *  Every raw class derives from the storage class, such
     *  as Number.class. */
    private final Class<? extends STORAGE_VALUE> [] rawClasses;

    /** The tags for this type, including constraints on what values
     *  are allowed for instances of this type. */
    private final Tag [] tags;

    /** The type casters FROM instances of this Type to instances
     *  of other Types.  (Possibly augmented by the TO casters
     *  of other Types!) */
    private final LinkedHashMap<Class<?>, TypeCaster<? extends STORAGE_VALUE, ?>>
        typeCastersFromThisType;

    /** The type casters TO instances of this Type from instances
     *  of other Types.  (Possibly augmented by the FROM casters
     *  of other Types!) */
    private final LinkedHashMap<Class<?>, TypeCaster<?, ? extends STORAGE_VALUE>>
        typeCastersToThisType;


    /**
     * <p>
     * Creates a new SimpleType with the specified name
     * and storage class.
     * </p>
     *
     * <p>
     * Should generally only be constructed by a TypeBuilder.
     * </p>
     *
     * @param name The universal name of this type,
     *             such as "car" or "truck" or "bank_account".
     *             The name should be as locale-neutral as possible,
     *             since it is universal across all locales.
     *             Must not be null.
     *
     * @param storage_class The raw storage class of the type.
     *                      Must not be null.
     *
     * @param tags Other typing tags to add checks or flags to this
     *             type.  For example, a Password type might have
     *             a PrivacyTag to flag that Instance data should
     *             not be readily handed out.  Must not be null.
     *             Must not contain any null elements.
     *
     * @param none The "no value" value for this type.  An Instance
     *             of this type whose raw value is equal to this
     *             none value is treated specially
     *             in certain cases, such as when querying the "next"
     *             or "previous" value from a data structure.
     *             Must not be null.
     *
     * @param type_casters_from_this_type The TypeCasters FROM this type's
     *                                    raw class(es).  Can be empty.
     *                                    Must not be null.  Must not contain
     *                                    any null elements.
     *
     * @param type_casters_to_this_type The TypeCasters from other raw classes
     *                                  TO this type's raw class(es).
     *                                  Can be empty.  Must not be null.
     *                                  Must not contain any null elements.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    protected SimpleType (
                          String name,
                          Class<STORAGE_VALUE> storage_class,
                          Class<? extends STORAGE_VALUE> [] raw_classes,
                          STORAGE_VALUE none,
                          Tag [] tags,
                          TypeCaster<? extends STORAGE_VALUE, ?> [] type_casters_from_this_type,
                          TypeCaster<?, ? extends STORAGE_VALUE> [] type_casters_to_this_type
                          )
        throws I18nIllegalArgumentException
    {
        boolean is_ok_parameters = true;
        if ( name == null
             || storage_class == null
             || raw_classes == null
             || none == null
             || tags == null
             || type_casters_from_this_type == null
             || type_casters_to_this_type == null )
        {
            is_ok_parameters = false;
        }
        else
        {
            // Make sure there are no null elements.
            for ( Class<? extends STORAGE_VALUE> raw_class : raw_classes )
            {
                if ( raw_class == null )
                {
                    is_ok_parameters = false;
                    break;
                }
            }
            for ( Tag tag : tags )
            {
                if ( tag == null )
                {
                    is_ok_parameters = false;
                    break;
                }
            }
            for (TypeCaster<? extends STORAGE_VALUE, ?> type_caster_from : type_casters_from_this_type )
            {
                if ( type_caster_from == null )
                {
                    is_ok_parameters = false;
                }
            }
            for (TypeCaster<?, ? extends STORAGE_VALUE> type_caster_to : type_casters_to_this_type )
            {
                if ( type_caster_to == null )
                {
                    is_ok_parameters = false;
                }
            }
        }

        if ( ! is_ok_parameters )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a SimpleType with name [%name%] storage class [%storage_class%] raw classes [%raw_classes%] tags [%tags%] none value [%none%] from type casters [%type_casters_from_this_type%] to type casters [%type_casters_to_this_type%]",
                                                     "name", name,
                                                     "storage_class", storage_class,
                                                     "raw_classes", raw_classes,
                                                     "tags", tags,
                                                     "none", none,
                                                     "type_casters_from_this_type", type_casters_from_this_type,
                                                     "type_casters_to_this_type", type_casters_to_this_type );
        }

        this.name = name;
        this.storageClass = storage_class;
        this.rawClasses = new Class [ raw_classes.length ];
        System.arraycopy ( raw_classes, 0,
                           this.rawClasses, 0, this.rawClasses.length );
        this.tags = new Tag [ tags.length ];
        System.arraycopy ( tags, 0,
                           this.tags, 0, this.tags.length );

        this.none = none;

        this.typeCastersFromThisType =
            new LinkedHashMap<Class<?>, TypeCaster<? extends STORAGE_VALUE, ?>> ();
        for ( TypeCaster<? extends STORAGE_VALUE, ?> from_this_type :
                  type_casters_from_this_type )
        {
            this.typeCastersFromThisType.put ( from_this_type.toClass (),
                                               from_this_type );
        }

        this.typeCastersToThisType =
            new LinkedHashMap<Class<?>, TypeCaster<?, ? extends STORAGE_VALUE>> ();
        for ( TypeCaster<?, ? extends STORAGE_VALUE> to_this_type :
                  type_casters_to_this_type )
        {
            this.typeCastersToThisType.put ( to_this_type.fromClass (),
                                             to_this_type );
        }
    }


    /**
     * @see musaico.types.Tag#check(Instance)
     */
    public void check(
                      Instance instance
                      )
        throws TypeException
    {
        // Step through all our Tags and check each of them.
        for ( int t = 0; t < this.tags.length; t ++ )
        {
            // Throw an exception if the tag / constraint / etc fails
            // to validate the instance.
            this.tags [ t ].check ( instance );
        }
    }


    /**
     * @see java.lang.equals(java.lang.Object)
     */
    public boolean equals (
                           Object that
                           )
    {
        if ( that == null )
        {
            return false;
        }
        else if ( this.getClass () == that.getClass () )
        {
            // Two Types of the same Class<Type> are always equivalent.
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * @see java.lang.hashCode()
     */
    public int hashCode ()
    {
        return this.getClass ().hashCode ();
    }


    /**
     * @see musaico.types.Type#isa(Class<Type<?>>)
     */
    public boolean isa (
                        Class<Type<?>> type_class
                        )
    {
        if ( type_class == null )
        {
            return false;
        }

        return type_class.isInstance ( this );
    }


    /**
     * @see musaico.types.Type#name()
     */
    @Override
    public String name ()
    {
        return this.name;
    }


    /**
     * @see musaico.types.Type#none()
     */
    @Override
    public STORAGE_VALUE none ()
    {
        return this.none;
    }


    /**
     * @see musaico.types.Type#rawClasses()
     */
    @Override
    public Class<? extends STORAGE_VALUE> [] rawClasses ()
    {
        Class<? extends STORAGE_VALUE> [] raw_classes =
            new Class [ this.rawClasses.length ];
        System.arraycopy ( this.rawClasses, 0,
                           raw_classes, 0, raw_classes.length );

        return raw_classes;
    }


    /**
     * <p>
     * Returns the class of raw values stored in Instances of this
     * Type.
     * </p>
     *
     * <p>
     * For example, a <code> Type&lt;String&gt; </code> will
     * return <code> String.class </code> as its storage class.
     * </p>
     *
     * <p>
     * This is the most generalized class handled by this
     * Type.  For instance, a NumberType might return Number
     * as its storage class; but it would probably be registered
     * in the TypingEnvironment under Number, Long, Integer, Float,
     * Double, and so on.  The NumberType itself could work
     * with any Number-derived objects, but in order to look
     * it up in the registry, every specific sub-class would
     * be registered individually.
     * </p>
     *
     * @return This Type's storage class.  Never null.
     */
    @Override
    public Class<STORAGE_VALUE> storageClass ()
    {
        return this.storageClass;
    }


    /**
     * @see musaico.types.Type#tags()
     */
    public final Tag [] tags ()
    {
        Tag [] tags = new Tag [ this.tags.length ];
        System.arraycopy ( this.tags, 0,
                           tags, 0, tags.length );
        return tags;
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString ()
    {
        String type_class_name = this.getClass ().getName ();
        int last_dot = type_class_name.lastIndexOf ( '.' );
        if ( last_dot >= 0 )
        {
            type_class_name = type_class_name.substring ( last_dot + 1 );
        }

        return type_class_name;
    }


    /**
     * @see musaico.types.TypeCaster#typeCasterFromThisType(java.lang.Object, java.lang.Class)
     */
    @Override
    public <FROM extends STORAGE_VALUE, TO extends Object>
        Conditional< TypeCaster<FROM, TO>, TypeException >
            typeCasterFromThisType (
                                    FROM raw_value,
                                    Class<TO> to_class
                                    )
        throws I18nIllegalArgumentException
    {
        if ( raw_value == null
             || to_class == null )
        {
            throw new I18nIllegalArgumentException ( "Type [%type%] has no type caster from [%from_value%] to [%to_class%]",
                                                     "type", this,
                                                     "from_value", raw_value,
                                                     "to_class", to_class );
        }

        // UGLY having to search like this, would be nice to
        // find a way to look up in log(n) time without using
        // oodles of storage space etc.  Consider the type
        // "Number", which has no type casters to or from
        // Number.class, but supports a number of raw classes
        // which extend Number.class (such as Double.class,
        // Integer.class and BigDecimal.class).  What should
        // we look up when someone passes in an Integer?  Maybe
        // our Map has Integer as a key...  But then what happens
        // when someone passes in an object of class FooBar which
        // is an extension of the Type's raw class Foo?  Then do
        // we have to search through the raw value's class hierarchy
        // to find the class we're looking for?!?  In most cases
        // it's better to search the raw classes of this type,
        // because there is often only one.  However this approach
        // is not good overall, need to think of something better...
        for ( Class<?> caster_to_class : this.typeCastersFromThisType.keySet () )
        {
            if ( to_class.isAssignableFrom ( caster_to_class ) )
            {
                TypeCaster<FROM, TO> type_caster =
                    (TypeCaster<FROM, TO>) this.typeCastersFromThisType.get ( caster_to_class );

                Conditional< TypeCaster<FROM, TO>, TypeException >
                    conditional =
                        new Successful< TypeCaster<FROM, TO>, TypeException > (
                            (Class<TypeCaster<FROM, TO>>) type_caster.getClass (),
                            type_caster );

                return conditional;
            }
        }

        TypeCaster<FROM, TO> none =
            new NoTypeCaster<FROM, TO> ( (Class<FROM>) raw_value.getClass (),
                                         to_class );
        Conditional< TypeCaster<FROM, TO>, TypeException > conditional =
            new Failed< TypeCaster<FROM, TO>, TypeException > (
                (Class<TypeCaster<FROM, TO>>) none.getClass (),
                new TypeException ( "Type [%type%] has no type caster from [%from_value%] to [%to_class%]",
                                    "type", this,
                                    "from_value", raw_value,
                                    "to_class", to_class ),
                none );

        return conditional;
    }


    /**
     * @see musaico.types.TypeCaster#typeCastersFromThisType()
     */
    @Override
    public TypeCaster<? extends STORAGE_VALUE, ?> [] typeCastersFromThisType()
    {
        TypeCaster<? extends STORAGE_VALUE, ?> [] type_casters_from_this_type =
            new TypeCaster [ this.typeCastersFromThisType.size () ];
        int tc = 0;
        for ( TypeCaster<? extends STORAGE_VALUE, ?> from_this_type :
                  this.typeCastersFromThisType.values () )
        {
            type_casters_from_this_type [ tc ] = from_this_type;
            tc ++;
        }

        return type_casters_from_this_type;
    }


    /**
     * @see musaico.types.TypeCaster#typeCasterToThisType(java.lang.Object, java.lang.Class)
     */
    @Override
    public <FROM extends Object, TO extends STORAGE_VALUE>
        Conditional< TypeCaster<FROM, TO>, TypeException >
            typeCasterToThisType (
                                  FROM raw_value,
                                  Class<TO> to_class
                                  )
        throws I18nIllegalArgumentException
    {
        if ( raw_value == null
             || to_class == null )
        {
            throw new I18nIllegalArgumentException ( "Type [%type%] has no type caster from [%from_value%] to [%to_class%]",
                                                     "type", this,
                                                     "from_value", raw_value,
                                                     "to_class", to_class );
        }

        // UGLY having to search like this, would be nice to
        // find a way to look up in log(n) time without using
        // oodles of storage space etc.  Consider the type
        // "Number", which has no type casters to or from
        // Number.class, but supports a number of raw classes
        // which extend Number.class (such as Double.class,
        // Integer.class and BigDecimal.class).  What should
        // we look up when someone passes in an Integer?  Maybe
        // our Map has Integer as a key...  But then what happens
        // when someone passes in an object of class FooBar which
        // is an extension of the Type's raw class Foo?  Then do
        // we have to search through the raw value's class hierarchy
        // to find the class we're looking for?!?  In most cases
        // it's better to search the raw classes of this type,
        // because there is often only one.  However this approach
        // is not good overall, need to think of something better...
        for ( Class<?> caster_from_class : this.typeCastersToThisType.keySet () )
        {
            if ( caster_from_class.isInstance ( raw_value ) )
            {
                TypeCaster<FROM, TO> type_caster =
                    (TypeCaster<FROM, TO>) this.typeCastersToThisType.get ( caster_from_class );

                Conditional< TypeCaster<FROM, TO>, TypeException >
                    conditional =
                        new Successful< TypeCaster<FROM, TO>, TypeException > (
                            (Class<TypeCaster<FROM, TO>>) type_caster.getClass (),
                            type_caster );

                return conditional;
            }
        }

        TypeCaster<FROM, TO> none =
            new NoTypeCaster<FROM, TO> ( (Class<FROM>) raw_value.getClass (),
                                         to_class );
        Conditional< TypeCaster<FROM, TO>, TypeException >
            conditional =
                new Failed< TypeCaster<FROM, TO>, TypeException > (
                    (Class<TypeCaster<FROM, TO>>) none.getClass (),
                    new TypeException ( "Type [%type%] has no type caster from [%from_value%] to [%to_class%]",
                                        "type", this,
                                        "from_value", raw_value,
                                        "to_class", to_class ),
                    new NoTypeCaster<FROM, TO> ( (Class<FROM>) raw_value.getClass (),
                                                 to_class ) );

        return conditional;
    }


    /**
     * @see musaico.types.TypeCaster#typeCastersToThisType()
     */
    public TypeCaster<?, ? extends STORAGE_VALUE> [] typeCastersToThisType()
    {
        TypeCaster<?, ? extends STORAGE_VALUE> [] type_casters_to_this_type =
            new TypeCaster [ this.typeCastersToThisType.size () ];
        int tc = 0;
        for ( TypeCaster<?, ? extends STORAGE_VALUE> to_this_type :
                  this.typeCastersToThisType.values () )
        {
            type_casters_to_this_type [ tc ] = to_this_type;
            tc ++;
        }

        return type_casters_to_this_type;
    }
}
