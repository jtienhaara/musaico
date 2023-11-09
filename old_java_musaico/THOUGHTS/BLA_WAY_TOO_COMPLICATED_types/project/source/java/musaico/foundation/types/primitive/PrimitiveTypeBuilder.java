package musaico.types.primitive;

import java.io.Serializable;


import musaico.types.SimpleTypeBuilder;
import musaico.types.Type;


/**
 * <p>
 * Builds primitive types (numbers, text and so on).
 * </p>
 *
 * <p>
 * In Java, all TypeBuilder implementations must be Serializable
 * to play nicely across RMI, even if the Instances they
 * represent are not Serializable.
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
 * Copyright (c) 2012 Johann Tienhaara
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
public class PrimitiveTypeBuilder<STORAGE_VALUE extends Serializable>
    extends SimpleTypeBuilder<STORAGE_VALUE>
    implements Serializable
{
    /**
     * <p>
     * Creates a new type builder for the specified type name and
     * storage class.
     * </p>
     *
     * <p>
     * For example, the following creates a type builder that will
     * construct a type called "first_name" with String storage class.
     * </p>
     *
     * <pre>
     *     new PrimitiveTypeBuilder<String> ( "number", Number.class,
     *                                        new Long(0L) );
     * </pre>
     *
     * @param name The name of the type to be built.  Must not be null.
     *
     * @param storage_class The storage class for the type to be
     *                      built.  Defines the class of value that
     *                      will be stored inside instances of the type.
     *                      Must not be null.
     *
     * @param none The "no value" value for the type to be built.
     *             An Instance whose raw value is equal to this
     *             none value is treated specially
     *             in certain cases, such as when querying the "next"
     *             or "previous" value from a data structure.
     *             Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public PrimitiveTypeBuilder (
                                 String name,
                                 Class<STORAGE_VALUE> storage_class,
                                 STORAGE_VALUE none
                                 )
    {
        super ( name, storage_class, none );
    }


    /**
     * @see musaico.types.TypeBuilder#build()
     */
    @Override
    public Type<STORAGE_VALUE> build ()
    {
        return new PrimitiveType<STORAGE_VALUE> ( this.name (),
                                                  this.storageClass (),
                                                  this.rawClasses (),
                                                  this.none (),
                                                  this.tags (),
                                                  this.typeCastersFrom (),
                                                  this.typeCastersTo () );
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "PrimitiveTypeBuilder " + this.name ();
    }
}
