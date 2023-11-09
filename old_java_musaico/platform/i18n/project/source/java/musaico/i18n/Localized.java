package musaico.i18n;


import java.io.Serializable;

import java.util.Locale;


/**
 * <p>
 * A localized representation, which knows how to convert the
 * internationalized input value into the appropriate output
 * for a specific locale.
 * </p>
 *
 * <pre>
 *     Internationalized &lt;X&gt;  --&gt;  Localized &lt;X&gt;
 *     + value (): X                        + value (): X
 *       Returns the international X          Returns the localized X
 * </pre>
 *
 * <p>
 * Typically the Localized implementation does not actually
 * localize the object it wraps until it is asked to.  For instance,
 * when a Localized&lt;String&gt; is first created, the international
 * representation is simply wrapped.  Later, when the <code> value () </code>
 * method is invoked for the first time, the international String
 * is looked up in a localized strings database, and the localized
 * String is returned.  The localized String is typically cached
 * and returned immediately upon subsequent calls to <code> value () </code>.
 * </p>
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
public interface Localized<INPUT extends Serializable,OUTPUT extends Serializable>
    extends Serializable
{
    /**
     * <p>
     * Returns the Internationalized representation.
     * </p>
     *
     * @return The internationalized representation.
     *         Never null.
     */
    public abstract Internationalized<INPUT,OUTPUT> internationalize ();


    /**
     * <p>
     * Returns the locale for this localized representation.
     * </p>
     *
     * @return The locale for this localized representation.
     *         Never null.
     */
    public abstract Locale locale ();


    /**
     * <p>
     * Builds or returns the localized representation.
     * </p>
     *
     * @return The localized representation.  Never null.
     *
     * @throws LocalizationException If there is no representation
     *                               of the internationalized object
     *                               in this locale, or if the
     *                               locale cannot be found, and so on.
     */
    public abstract OUTPUT value ()
        throws LocalizationException;
}
