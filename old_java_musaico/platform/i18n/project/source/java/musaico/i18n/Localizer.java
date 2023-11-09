package musaico.i18n;


import java.io.Serializable;

import java.util.Locale;


/**
 * <p>
 * Given an input value and a Locale, localizes the input to an
 * output value.
 * </p>
 *
 *
 * <p>
 * In Java, all Localizers must be serializable in order to play
 * nicely across RMI.
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
public interface Localizer<INPUT extends Serializable,OUTPUT extends Serializable>
    extends Serializable
{
    /**
     * <p>
     * Localizes the specified international representation into
     * output for the specified locale.
     * </p>
     *
     * @param international_input The input to localize.  Must not be null.
     * @param locale The locale to localize to.  Must not be null.
     *
     * @return The localized output.  Never null.
     *
     * @throws LocalizationException If anything goes wrong (no such locale,
     *                               no localized representation for the
     *                               given locale, and so on).
     */
    public abstract OUTPUT localize (
                                     INPUT international_input,
                                     Locale locale
                                     )
        throws LocalizationException;
}
