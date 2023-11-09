package musaico.i18n;


import java.io.Serializable;

import java.util.Locale;


/**
 * <p>
 * An internationalized representation.
 * </p>
 *
 * <p>
 * The Internationalized knows how to build Localized versions
 * of itself (each of which, in turn, knows how to localize the
 * representation for its own locale).
 * </p>
 *
 * <pre>
 *     Internationalized &lt;X&gt;  --&gt;  Localized &lt;X&gt;
 *     + value (): X                        + value (): X
 *       Returns the international X          Returns the localized X
 * </pre>
 *
 *
 * <p>
 * In Java, every Localized must be Serializable in order to play
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
 * Copyright (c) 2010 Johann Tienhaara
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
public interface Internationalized<INPUT extends Serializable,OUTPUT extends Serializable>
    extends Serializable
{
    /**
     * <p>
     * Builds a Localized version of this Internationalized.
     * </p>
     *
     * <p>
     * The Localized can then be used to build a new localized
     * representation.
     * </p>
     *
     * @param locale The locale for the new Localized.
     *               Must not be null.
     *
     * @return The new Localized.  Never null.
     */
    public abstract Localized<INPUT,OUTPUT> localize (
                                                      Locale locale
                                                      );


    /**
     * <p>
     * Returns the international representation.
     * </p>
     *
     * @return The international representation.  Never null.
     */
    public abstract INPUT value ();
}
