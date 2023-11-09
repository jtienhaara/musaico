package musaico.i18n.localizers;


import java.io.Serializable;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;


import musaico.i18n.LocalizationException;
import musaico.i18n.Localizer;


/**
 * <p>
 * Given String input value and a Locale, localizes the input to an
 * output String.
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
public class StringLocalizer
    extends AbstractLocalizer<String,String>
    implements Serializable
{
    /**
     * <p>
     * Creates a new StringLocalizer with the specified parameters
     * as guides for loading Java resource bundles.
     * </p>
     *
     * @param resource_bundle_names The base name(s) of resource bundle(s),
     *                              each a fully qualified class name
     *                              (such as "musaico.l10n.Messages"
     *                              or "musaico.l10n.TimeFormatters"
     *                              and so on).  Searched through in order
     *                              to find a resource bundle with the
     *                              right String.  Must not be null.
     * @param loader The class loader from which to load the
     *               resource bundle.  For example,
     *               <code> Thread.current ().getClassLoader () </code>.
     *               Must not be null.
     * @param source The control which gives information for the
     *               resource bundle loading process.  For example,
     *               <code> ResourceBundle.Control.FORMAT_DEFAULT </code>.
     *               Must not be null.
     */
    public StringLocalizer (
                            String [] resource_bundle_names,
                            ClassLoader loader,
                            ResourceBundle.Control source
                            )
    {
        super ( resource_bundle_names,
                loader,
                source );
    }


    /**
     * @see musaico.i18n.localizers.AbstractLocalizer#getInternationalID(INPUT)
     */
    public String getInternationalID (
                                      String international_input
                                      )
    {
        return international_input;
    }
}
