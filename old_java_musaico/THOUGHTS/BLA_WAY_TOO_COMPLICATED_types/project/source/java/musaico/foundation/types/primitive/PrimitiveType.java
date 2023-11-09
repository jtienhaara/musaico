package musaico.types.primitive;


import java.io.Serializable;


import musaico.types.Tag;
import musaico.types.SimpleType;
import musaico.types.TypeCaster;


/**
 * <p>
 * Cross-platform, dynamic primitive type (text, numbers, dates, and so
 * on).
 * </p>
 *
 * <p>
 * In Java, all Primitive implementations must be Serializable
 * to play nicely across RMI.
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
public class PrimitiveType<STORAGE_VALUE extends Serializable>
    extends SimpleType<STORAGE_VALUE>
    implements Serializable
{
    /**
     * <p>
     * Creates a new PrimitiveType with the specified universal name
     * and storage class.
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
     * @param type_casters_from The TypeCasters FROM this type's
     *                          raw class(es).  Can be empty.
     *                          Must not be null.  Must not contain
     *                          any null elements.
     *
     * @param type_casters_to The TypeCasters from other raw classes
     *                        TO this type's raw class(es).
     *                        Can be empty.  Must not be null.
     *                        Must not contain any null elements.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    protected PrimitiveType (
                             String name,
                             Class<STORAGE_VALUE> storage_class,
                             Class<? extends STORAGE_VALUE> [] raw_classes,
                             STORAGE_VALUE none,
                             Tag [] tags,
                             TypeCaster<? extends STORAGE_VALUE, ?> [] type_casters_from,
                             TypeCaster<?, ? extends STORAGE_VALUE> [] type_casters_to
                             )
    {
        super ( name, storage_class, raw_classes, none, tags,
                type_casters_from, type_casters_to );
    }
}
