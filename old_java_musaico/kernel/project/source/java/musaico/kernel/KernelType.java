package musaico.kernel;


import java.io.Serializable;


import musaico.types.Type;


/**
 * <p>
 * Base type class for all kernel objects (Memory, Segment, Page, ONode,
 * Process and so on).
 * </p>
 *
 * <p>
 * In Java, all KernelTypes must be Serializable and use Serializable
 * raw values in order to play nicely across RMI.
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
public class KernelType<STORAGE_VALUE extends Serializable>
    extends SimpleType<STORAGE_VALUE>
    implements Serializable
{
    /**
     * <p>
     * Creates a new KernelType with the specified internationalized name
     * and storage class.
     * </p>
     *
     * <p>
     * Each localized type name should go into the appropriate locale
     * file, keyed by the specified internationalized name.
     * For example, the "type.car" internationalized name might
     * be localized as "car" in English and "auto" in French.
     * Alternatively type system designers might choose to keep
     * all localized type names identical, so that programming
     * documentation is easier to write.  It's up to the type
     * system developer.
     * </p>
     *
     * @param i18n_name The internationalized name of this type,
     *                  such as "type.car".  Must not be null.
     *
     * @param storage_class The raw storage class of the type.  Other
     *                      classes might be covered by the type, too,
     *                      but they are cast internally to the
     *                      specified type for storage in an Instance.
     *                      For example, real numbers might cover
     *                      Doubles, Floats and BigDecimals, but only
     *                      be stored as Doubles.  Must not be null.
     *
     * @param tags Other typing tags to add checks or flags to this
     *             type.  For example, a Password type might have
     *             a PrivacyTag to flag that Instance data should
     *             not be readily handed out.  Must not be null.
     *             Must not contain any null elements.
     *
     * @param storage_casters_from Casters which take an input object
     *                             of some other class and convert it
     *                             to the storage class.  For example,
     *                             a real number type might cast from
     *                             Floats and BigDecimals to Doubles
     *                             so that users can create real Instances
     *                             from Float or BigDecimal data.
     *                             Must not be null.  Must not contain
     *                             any null elements.
     *
     * @param storage_casters_to Casters which take an object of the
     *                           storage class and convert it to an
     *                           output object of some other class.
     *                           For example, a real number type might
     *                           cast from Doubles to Floats and BigDecimals
     *                           so that users of Instances can
     *                           request real Types with Float.class
     *                           or BigDecimal.class.  Must not be
     *                           null.  Must not contain any null
     *                           elements.
     */
    protected KernelType (
                          String i18n_name,
                          Class<STORAGE_VALUE> storage_class,
                          Tag [] tags,
                          Map<Class<?>,TypeCaster<?,STORAGE_VALUE>> storage_casters_from,
                          Map<Class<?>,TypeCaster<STORAGE_VALUE,?>> storage_casters_to
                          )
    {
        super ( i18n_name, storage_class,
                tags,
                storage_casters_from, storage_casters_to );
    }
}
