package musaico.kernel.module.descriptions;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.i18n.message.Message;
import musaico.i18n.message.SimpleMessageBuilder;

import musaico.i18n.text.I18nString;

import musaico.io.Reference;

import musaico.kernel.module.ModuleDescription;


/**
 * <p>
 * A distribution license (which may also include usage and
 * other licenses).
 * </p>
 *
 * <p>
 * The full text for each license is stored in the Messages.properties
 * file in musaico.kernel.module.descriptions.
 * </p>
 *
 * <p>
 * Most of this information originally came from
 * <a href="http://www.gnu.org/licenses/license-list.html">
 *     the license list at gnu.org
 * </a>, either directly or from links on the web page.
 * Referenced April 25, 2011.  (Happy birthday Ria!)
 * </p>
 *
 *
 * <p>
 * In Java, every license must be Serializable in order to play
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
 * Copyright (c) 2011 Johann Tienhaara
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
public class License
    implements Reference, Serializable
{
    // !!!!!!!!!!!! LOTS OF LICENSES LEFT TO FILL IN!!!


    /** All standard licenses, stored internally. */
    private static final Map<String,License> STANDARD_LICENSES =
        new HashMap<String,License> ();



    /** <a href="http://www.gnu.org/licenses/agpl.html">
     *      GNU Affero General Public License version 3
     *  </a> */
    public static final License AGPL_3 = new License ( "AGPL-3" );

    /** <a href="http://www.apache.org/licenses/LICENSE-2.0">
     *      Apache License version 2.0
     *  </a> */
    public static final License APACHE_2 = new License ( "Apache-2.0" );

    /** <a href="http://www.perlfoundation.org/artistic_license_2_0">
     *      Artistic License version 2.0
     *  </a> */
    public static final License ARTISTIC_2 = new License ( "Artistic-2.0" );

    /** <a href="http://www.xfree86.org/3.3.6/COPYRIGHT2.html#5">
     *      Modified BSD License
     *  </a> */
    public static final License BSD = new License ( "BSD" );

    /** <a href="http://www.gnu.org/prep/maintain/html_node/License-Notices-for-Other-Files.html">
     *      GNU All-Permissive License
     *  </a> */
    public static final License GNU_ALL_PERMISSIVE = new License ( "GNU-All-Permissive" );

    /** <a href="http://www.gnu.org/licenses/old-licenses/gpl-2.0.html">
     *      GNU Public License version 2
     *  </a> */
    public static final License GPL_2 = new License ( "GPL-2" );

    /** <a href="http://www.gnu.org/licenses/gpl.html">
     *      GNU Public License version 3
     *  </a> */
    public static final License GPL_3 = new License ( "GPL-3" );

    /** <a href="http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html">
     *      GNU Lesser General Public License version 2.1
     * </a> */
    public static final License LGPL_2_1 = new License ( "LGPL-2.1" );

    /** <a href="http://www.gnu.org/licenses/lgpl.html">
     *      GNU Lesser General Public License version 3
     * </a> */
    public static final License LGPL_3 = new License ( "LGPL-3" );

    /** <a href="http://www.xfree86.org/3.3.6/COPYRIGHT2.html#5">
     *      Modified BSD License
     *  </a> */
    public static final License MODIFIED_BSD = License.BSD;


    /**
     * <p>
     * Returns the specified standard license, or null if no such
     * standard License exists.
     * </p>
     *
     * @param license_id The identifier of the License to return.
     *                   Must not be null.
     *
     * @return The specified License, or null if it cannot be found.
     */
    public static License lookup (
                                  String license_id
                                  )
    {
        if ( license_id == null )
        {
            return null;
        }

        return STANDARD_LICENSES.get ( license_id );
    }


    /** The unique idenfitier of this license. */
    private final String id;

    /** The Message specifying the license, if it is custom
     *  (i.e. not provided as a static constant here). */
    private final I18nString fullText;


    /**
     * <p>
     * Creates a new license with the specified id, pointing
     * to the standard resource bundle for localized versions
     * of the license text.
     * </p>
     *
     * @param id The unique identifier for this license.
     *           Must not be null.
     */
    private License (
                     String id
                     )
    {
        this.id = id;
        this.fullText = null; // Look it up from the standard
        // Messages.properties files for Musaico.

        if ( License.STANDARD_LICENSES.containsKey ( this.id ) )
        {
            throw new IllegalStateException ( "License BUG: License " + id + " already exists!" );
        }

        License.STANDARD_LICENSES.put ( id, this );
    }


    /**
     * <p>
     * Creates a custom license.
     * </p>
     *
     * @param id The unique identifier for this license.  Must not
     *           conflict with any of the standard known license
     *           names (GPL and so on).  Must not be null.
     *
     * @param full_text The full internationalized text describing
     *                  the license.  Must not be null.
     *
     * @throws I18nIllegalArgumentException If the parameters are
     *                                      invalid (see above).
     */
    public License (
                    String id,
                    I18nString full_text
                    )
    {
        if ( id == null
             || full_text == null
             || License.STANDARD_LICENSES.containsKey ( id ) )
        {
            throw new I18nIllegalArgumentException ( "Cannot create license [%id%] with full text [%full_text%]",
                                                     "id", id,
                                                     "full_text", full_text );
        }

        this.id = id;
        this.fullText = full_text;
    }


    /**
     * <p>
     * Builds the full text for this license from the specified
     * ModuleDescription (authors, copyrights, and so on).
     * </p>
     *
     * @param description The full description of the Module
     *                    whose license will be fleshed out
     *                    (including copyrights and so on).
     *                    Must not be null.  Must be a valid
     *                    ModuleDescription, including all
     *                    required parameters.
     *
     * @return The internationalized text of the license.
     *         Never null.
     *
     * @throws I18nIllegalArgumentException If the parameters are invalid
     *                                      (see above).
     */
    public I18nString fullText (
                                ModuleDescription description
                                )
        throws I18nIllegalArgumentException
    {
        if ( this.fullText != null )
        {
            // The full text is custom.
            return this.fullText;
        }

        // Standard license.  Pull the text from our Messages.properties
        // file.
        Message i18n_license_with_module_parameters =
            new SimpleMessageBuilder ()
            .id ( this.id )
            .parameter ( ModuleDescription.AUTHORS,
                         description.parameter ( ModuleDescription.AUTHORS ) )
            .parameter ( ModuleDescription.COPYRIGHTS,
                         description.parameter ( ModuleDescription.COPYRIGHTS ) )
            .parameter ( ModuleDescription.ID,
                         description.parameter ( ModuleDescription.ID ) )
            .parameter ( ModuleDescription.LICENSE_TYPES,
                         description.parameter ( ModuleDescription.LICENSE_TYPES ) )
            .parameter ( ModuleDescription.MAINTAINERS,
                         description.parameter ( ModuleDescription.MAINTAINERS ) )
            .parameter ( ModuleDescription.OVERVIEW,
                         description.parameter ( ModuleDescription.OVERVIEW ) )
            .parameter ( ModuleDescription.URL_BINARY,
                         description.parameter ( ModuleDescription.URL_BINARY ) )
            .parameter ( ModuleDescription.URL_SOURCE,
                         description.parameter ( ModuleDescription.URL_SOURCE ) )
            .parameter ( ModuleDescription.CONFIGURATION,
                         description.parameter ( ModuleDescription.CONFIGURATION ) )
            .parameter ( ModuleDescription.DEPENDENCIES,
                         description.parameter ( ModuleDescription.DEPENDENCIES ) )
            .parameter ( ModuleDescription.DEPRECATED_SINCE,
                         description.parameter ( ModuleDescription.DEPRECATED_SINCE ) )
            .parameter ( ModuleDescription.MANUAL,
                         description.parameter ( ModuleDescription.MANUAL ) )
            .parameter ( ModuleDescription.REPLACED_BY_MODULE,
                         description.parameter ( ModuleDescription.REPLACED_BY_MODULE ) )
            .parameter ( ModuleDescription.SYNOPSIS_USAGE,
                         description.parameter ( ModuleDescription.SYNOPSIS_USAGE ) )
            .parameter ( ModuleDescription.TEST_COVERAGE,
                         description.parameter ( ModuleDescription.TEST_COVERAGE ) )
            .parameter ( ModuleDescription.URL_BUGS,
                         description.parameter ( ModuleDescription.URL_BUGS ) )
            .parameter ( ModuleDescription.URL_DESCRIPTION,
                         description.parameter ( ModuleDescription.URL_DESCRIPTION ) )
            .parameter ( ModuleDescription.URL_DEVELOPERS,
                         description.parameter ( ModuleDescription.URL_DEVELOPERS ) )
            .parameter ( ModuleDescription.URL_EXAMPLES,
                         description.parameter ( ModuleDescription.URL_EXAMPLES ) )
            .parameter ( ModuleDescription.URL_TESTS,
                         description.parameter ( ModuleDescription.URL_TESTS ) )
            .parameter ( ModuleDescription.URL_TROUBLESHOOTING,
                         description.parameter ( ModuleDescription.URL_TROUBLESHOOTING ) )
            .parameter ( ModuleDescription.URL_USERS,
                         description.parameter ( ModuleDescription.URL_USERS ) )
            .parameter ( ModuleDescription.URLS_SEE_ALSO,
                         description.parameter ( ModuleDescription.URLS_SEE_ALSO ) )
            .build ();

        I18nString full_text =
            new I18nString ( i18n_license_with_module_parameters );

        return full_text;
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString ()
    {
        return this.id;
    }
}
