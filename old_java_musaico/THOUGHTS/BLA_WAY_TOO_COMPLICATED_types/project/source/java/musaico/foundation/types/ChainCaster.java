package musaico.types;


import java.io.Serializable;


/**
 * <p>
 * Casts from X to Y through some intermediate type, using
 * existing TypeCasters.
 * </p>
 *
  <p>
    For example, to cast from X to Y using two existing
    TypeCasters, X to String and String to Y, the following
    would work:
  </p>

  <pre>
    TypeCaster<X,String> cast_x_to_string = ...;
    TypeCaster<String,Y> cast_string_to_y = ...;
    TypeCaster<X,Y> cast_x_to_y =
        new ChainCaster<X,String,Y> ( cast_x_to_string,
                                      cast_string_to_y );
  </pre>
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
 * Copyright (c) 2011, 2012 Johann Tienhaara
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
public class ChainCaster<X extends Object, INTERMEDIARY extends Object, Y extends Object>
    implements TypeCaster<X,Y>, Serializable
{
    /** The from class (X). */
    private final Class<X> fromClass;

    /** The type caster which takes X as input and outputs INTERMEDIARY. */
    private final TypeCaster<X,INTERMEDIARY> xToIntermediary;

    /** The class of intermediary data, such as String.class if
     *  Strings are used as the intermediary between X and Y. */
    private final Class<INTERMEDIARY> intermediateClass;

    /** The type caster which takes INTERMEDIARY as input and outputs Y. */
    private final TypeCaster<INTERMEDIARY,Y> intermediaryToY;

    /** The to class (Y). */
    private final Class<Y> toClass;


    /**
     * <p>
     * Creates a new ChainCaster which will use the specified
     * intermediate type casters to perform casting from X
     * through INTERMEDIARY to Y.
     * </p>
     *
     * @param x_class The base "from" class X.  For example String.class
     *                or Number.class and so on.  Must not be null.
     *
     * @param x_to_intermediary The TypeCaster which takes X as
     *                          input and outputs INTERMEDIARY.
     *                          Must not be null.
     *
     * @param intermediate_class The intermediate class of values
     *                           to cast through, such as String.class.
     *                           Must not be null.
     *
     * @param intermediary_to_y The TypeCaster which takes INTERMEDIARY
     *                          as input and outputs Y.
     *                          Must not be null.
     *
     * @param y_class The base "to" class Y.  For example String.class
     *                or Number.class and so on.  Must not be null.
     */
    public ChainCaster (
                        Class<X> x_class,
                        TypeCaster<X,INTERMEDIARY> x_to_intermediary,
                        Class<INTERMEDIARY> intermediate_class,
                        TypeCaster<INTERMEDIARY,Y> intermediary_to_y,
                        Class<Y> y_class
                        )
    {
        this.fromClass = x_class;
        this.xToIntermediary = x_to_intermediary;
        this.intermediateClass = intermediate_class;
        this.intermediaryToY = intermediary_to_y;
        this.toClass = y_class;
    }


    /**
     * @see musaico.types.TypeCaster#cast(java.lang.Object)
     *
     * Final for speed.
     */
    public final Y cast (
                         X x
                         )
        throws TypeException
    {
        INTERMEDIARY intermediary = this.xToIntermediary.cast ( x );
        Y result = this.intermediaryToY.cast ( intermediary );

        return result;
    }


    /**
     * @see musaico.types.TypeCaster#fromClass()
     */
    public Class<X> fromClass ()
    {
        return this.fromClass;
    }


    /**
     * @see musaico.types.TypeCaster#toClass()
     */
    public Class<Y> toClass ()
    {
        return this.toClass;
    }








    /**
     * <p>
     * Creates and registers a new NoTypeCaster from the
     * specified source type to the specified target type
     * in the specified TypingEnvironment.
     * </p>
     *
     * <p>
     * Runtime casts from the source type to the target type will fail
     * with the returned NoTypeCaster.
     * </p>
     *
     * @param from_type The source type.  Must not be null.
     *
     * @param to_type The target type.  Must not be null.
     *
     * @param intermediate_class The intermediate class of values
     *                           to cast through, such as String.class.
     *                           Must not be null.
     *
     * @param types The types registry from which the specified
     *              intermeiate class's type will be pulled.
     *                Must not be null.
     *
     * @param casters The type casters registry from which the casters
     *                from source to intermediary and from intermediary
     *                to target will be pulled, and also into which the
     *                newly created NoTypeCaster will be registered.
     *                Must not be null.
     *
     * @throws TypeException If the type casters registry throws one.
     */
    public static
        <FROM extends Object, MID extends Object, TO extends Object>
        void register (
                       Type<FROM> from_type,
                       Type<TO> to_type,
                       Class<MID> intermediate_class,
                       TypesRegistry types,
                       TypeCastersRegistry casters
                       )
        throws TypeException
    {
        Type<MID> intermediate_type = types.type ( intermediate_class );
        TypeCaster<FROM,MID> x_to_intermediary =
            casters.get ( from_type, intermediate_type );
        TypeCaster<MID,TO> intermediary_to_y =
            casters.get ( intermediate_type, to_type );
        TypeCaster<FROM,TO> type_caster =
            new ChainCaster<FROM,MID,TO> ( from_type.storageClass (),
                                           x_to_intermediary,
                                           intermediate_class,
                                           intermediary_to_y,
                                           to_type.storageClass () );
        casters.put ( from_type, to_type, type_caster );
    }
}
