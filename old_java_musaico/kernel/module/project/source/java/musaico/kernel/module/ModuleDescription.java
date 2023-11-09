package musaico.kernel.module;

import java.io.Serializable;


import musaico.i18n.message.Message;


/**
 * <p>
 * A description of a Module, including its name and version,
 * but also author, license, usage information, and so on.
 * </p>
 *
 * <p>
 * Each Module implementation loads the description information
 * in from the module file / URL / and so on.  For example, a
 * tarball module implementation might open the file
 * <code> LICENSE.txt </code> or <code> COPYING </code> and so on
 * to read in the license informaiton; or a Java jar implementation
 * might read in a Properties file from the jar; and so on.
 * </p>
 *
 * <p>
 * The creator of the Module is responsible for packaging up
 * the correct files so that the description can be loaded
 * by the appropriate module implementation.  The module implementation
 * then reports only what is found in the module package.
 * </p>
 *
 * <p>
 * Every ModuleDescription must contain all parameters listed
 * in the ModuleDescription.REQUIRED array.
 * </p>
 *
 *
 * <p>
 * The parameters which must be internationalized in order
 * to be easily localizable should follow the rules of
 * <code> musaico.i18n.text.Text </code>.  In Java, by default
 * this means providing a resource bundle called
 * <code> Messages.properties </code> in the Module package.
 * The <code> messages.properties </code> file contains the
 * default locale's text for each module description parameter.
 * For example:
 * </p>
 *
 * <pre>
 *     # Messages.properties for the "xml_driver" module.
 *     module_overview = \
 *         The XML driver module provides a Driver \
 *         to read and write XML streams of data. \
 *         This driver is stackable, so that XML can be read \
 *         from and written to different sources (files, URLs \
 *         and so on).  The data handled by the driver is opaque \
 *         to the driver, so it can be used to back various different \
 *         types of object systems.
 * </pre>
 *
 * <p>
 * The author of the module therefore need only provide the default
 * locale (in this case English), allowing others to translate
 * the text into other locales by providing the appropriate
 * resource bundles (Messages_fr.properties and so on).
 * </p>
 *
 * <p>
 * Some Java Modules might simply read in all module description
 * parameters from the same resource bundle.  For example the
 * Messages.properties file above might also contain:
 * </p>
 *
 * <pre>
 *     module_authors = Johann Tienhaara
 *     module_copyrights = 2010 Johann Tienhaara, 2011 Johann Tienhaara, \
 *                         2011 Jane Doe
 *     module_license_types = GPL_3
 *     module_maintainers = Jane Doe
 *     module_url_binary = http://www.musaico.org/downloads/modules/musaico_xml-1.0.0.binary.jar
 *     module_url_source = http://www.musaico.org/downloads/modules/musaico_xml-1.0.0.source.jar
 * </pre>
 *
 * <p>
 * See the documentation for the type of Module package
 * (jar, rpm, tarball, and so on) to find out where the Module
 * retrieves its parameters from and how to lay out and format
 * your package.
 * </p>
 *
 *
 * <p>
 * In Java, every ModuleDescription and its contents must be Serializable
 * in order to play nicely across RMI.
 * </p>
 *
 * <p>
 * The <code> equals () </code> and <code> hashCode () </code>
 * methods must be overrided by the ModuleIdentifier implementation,
 * so that ModuleIdentifiers can be safely placed in hash tables.
 * </p>
 *
 * <p>
 * Generally it is a good idea to override the <code> toString () </code>
 * method, too, to aid in debugging.
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
public interface ModuleDescription
    extends Message, Serializable
{
    // Every ModuleDescription must implement all of the methods
    // in Message.  Typically it is simplest to just
    // extend musaico.i18n.messages.SimpleMessage, and check to
    // make sure all the REQUIRED parameters are there at
    // constructor time.


    /** 
     * <p>
     * The author(s) of this module.  This is typically the
     * person(s) or entity(ies) who wrote the module, but not
     * necessarily any other contributors (bug fixers and so
     * on).  The author should be considered an authority on
     * licensing, if not also on the current state of the
     * module's code.
     * </p>
     *
     * @see musaico.kernel.module.ModuleDescription.MAINTAINERS
     *
     * <p>
     * Type: <code> java.lang.String [] </code>
     * </p>
     */
    public static final String AUTHORS = "module_authors";

    /**
     * <p>
     * Describes configuration options for the module.
     * </p>
     *
     * <p>
     * This parameter must be localizable (though the package
     * need not come pre-loaded with localized versions of the
     * text).  See the top of the ModuleDescription
     * documentation for more information on international
     * text.
     * </p>
     *
     * <p>
     * Type: <code> musaico.i18n.text.I18nString </code>
     * </p>
     */
    public static final String CONFIGURATION = "module_configuration";

    /** 
     * <p>
     * All copyright owner(s) of this module, and the year(s)
     * for which each author holds copyright.
     * </p>
     *
     * <p>
     * Typically the author(s), maintainer(s) and generally
     * anyone who has ever fixed a bug or added a feature to
     * the Module will be listed here.
     * </p>
     *
     * @see musaico.kernel.module.ModuleDescription.AUTHORS
     * @see musaico.kernel.module.ModuleDescription.MAINTAINERS
     *
     * <p>
     * Type: <code> musaico.kernel.module.descriptions.Copyright [] </code>
     * </p>
     */
    public static final String COPYRIGHTS = "module_copyrights";

    /**
     * <p>
     * Other module(s) on which this module depends.
     * </p>
     *
     * <p>
     * This is for the information of humans.  The actual dependency
     * resolution is done programmatically as needed by the Module
     * implementation itself, by calling <code> Module.findModule () </code>,
     * <code> Module.loadModule () </code> and
     * <code> Module.unloadModule () </code>.
     * </p>
     *
     * <p>
     * Type: <code> musaico.kernel.module.ModuleIdentifier [] </code>
     * </p>
     */
    public static final String DEPENDENCIES = "module_dependencies";

    /**
     * <p>
     * This module has been marked deprecated, as of the specified
     * date/time.  New modules should not depend on this one.
     * </p>
     *
     * @see musaico.kernel.module.ModuleDescription#REPLACED_BY_MODULE
     *
     * <p>
     * Type: <code> musaico.time.Time </code>
     * </p>
     */
    public static final String DEPRECATED_SINCE = "module_deprecated_since";

    /**
     * <p>
     * The identifier for this Module, such as "musaico_xml" version "1.0.0".
     * </p>
     *
     * <p>
     * Type: <code> musaico.kernel.module.ModuleIdentifier </code>
     * </p>
     */
    public static final String ID = "module_id";

    /**
     * <p>
     * The full human-readable text of the license(s) for the described
     * Module.  If multiple licenses apply to the Module, then the
     * text of all of them shall be included here as one big
     * piece of text.
     * </p>
     *
     * <p>
     * If the Module being described uses any custom license(s), then
     * this is one big piece of non-localizable custom text.
     * Otherwise, the standard licenses known to Musaico are filled
     * in with the required parameters of this module description
     * whenever rendering into localized text: AUTHORS, COPYRIGHTS,
     * MAINTAINERS, URL_SOURCE and so on.
     * </p>
     *
     * <p>
     * All of the terms
     * of the license(s) must be complied with in order to legally
     * distribute (and possibly also use) the described Module.
     * If multiple licenses are provided then all terms of all
     * licenses must be complied with.  In the event the licenses
     * conflict with each other, the author(s) should be consulted,
     * but ultimately the courts would have to decide...
     * </p>
     *
     * <p>
     * If your module allows distribution under any of a number
     * of different licenses, then you must bundle up each one
     * with its own license(s).  For example, a Module which
     * may be distributed under <i> either </i> GPL <i> or </i> the
     * modified BSD license must provide two packages: one with
     * only the GPL, and one with only the modified BSD license.
     * </p>
     *
     * <p>
     * Type: <code> musaico.i18n.text.I18nString </code>
     * </p>
     */
    public static final String LICENSE_TEXT = "module_license_text";

    /**
     * <p>
     * The type(s) of license(s) for distribution (and possibly
     * also for usage) for the described Module.  This is an array
     * specifying the types of all licenses; the full text of
     * all combined licenses can be found in the LICENSE_TEXT parameter.
     * For example, LICENSE_TYPES might include
     * <code> { LicenseType.GPL_3, LicenseType.APACHE_2,
     *          LicenseType.CREATIVE_COMMONS_ATTRIBUTION_SHARE_ALIKE_3 } </code>,
     * which would provide developers and other Modules alike a
     * quick view of the licensing mechanism(s) for the module.
     * </p>
     *
     * <p>
     * If the Module being described uses any custom license(s), then
     * it must also provide the full non-localizable text for
     * the license(s).  Custom licenses are strongly discouraged.
     * </p>
     *
     * <p>
     * All of the terms
     * of these licenses must be complied with in order to legally
     * distribute (and possibly also use) the described Module.
     * If multiple licenses are provided then all terms of all
     * licenses must be complied with.  In the event the licenses
     * conflict with each other, the author(s) should be consulted,
     * but ultimately the courts would have to decide...
     * </p>
     *
     * <p>
     * If your module allows distribution under any of a number
     * of different licenses, then you must bundle up each one
     * with its own license(s).  For example, a Module which
     * may be distributed under either GPL or the modified BSD
     * license must provide two packages: one with only the GPL,
     * and one with only the modified BSD license.
     * </p>
     *
     * <p>
     * Type: <code> musaico.kernel.module.descriptions.License [] </code>
     * </p>
     */
    public static final String LICENSE_TYPES = "module_license_types";

    /** 
     * <p>
     * The current maintainer(s) of this module, as of the
     * time it was packaged up.  This is often the same as
     * the author(s), though in cases where they are different,
     * the maintainers should be considered the experts on
     * the code and technical details.
     * </p>
     *
     * @see musaico.kernel.module.ModuleDescription.AUTHORS
     *
     * <p>
     * Type: <code> java.lang.String [] </code>
     * </p>
     */
    public static final String MAINTAINERS = "module_maintainers";

    /**
     * <p>
     * A full inline description of the Module, including
     * API, tutorials, and so on.
     * </p>
     *
     * <p>
     * An inline description for a module can be bulky, but it
     * also guarantees that the module's documentation travels
     * with it.  URLs typically have more up-to-date information;
     * but URLs also come and go...  When a company gets bought
     * out suddenly all the API documentation recorded in Google
     * for the past 15 years disappears, and the new owner company
     * makes an absolute mess of porting over all the archived
     * documentation to new URLs.
     * </p>
     *
     * <p>
     * This parameter must be localizable (though the package
     * need not come pre-loaded with localized versions of the
     * text).  See the top of the ModuleDescription
     * documentation for more information on international
     * text.
     * </p>
     *
     * @see musaico.kernel.module.ModuleDescription.OVERVIEW
     * @see musaico.kernel.module.ModuleDescription.SYNOPSIS_USAGE
     *
     * <p>
     * Type: <code> musaico.i18n.text.I18nString </code>
     * </p>
     */
    public static final String MANUAL = "module_manual";


    /**
     * <p>
     * A brief (typically one sentence) description of this
     * Module.
     * </p>
     *
     * <p>
     * This parameter must be localizable (though the package
     * need not come pre-loaded with localized versions of the
     * text).  See the top of the ModuleDescription
     * documentation for more information on international
     * text.
     * </p>
     *
     * @see musaico.kernel.module.ModuleDescription.MANUAL
     * @see musaico.kernel.module.ModuleDescription.SYNOPSIS_USAGE
     *
     * <p>
     * Type: <code> musaico.i18n.text.I18nString </code>
     * </p>
     */
    public static final String OVERVIEW = "module_overview";

    /**
     * <p>
     * This module has been replaced by the specified module.
     * New modules should not depend on this one.
     * </p>
     *
     * @see musaico.kernel.module.ModuleDescription#DEPRECATED_SINCE
     *
     * <p>
     * Type: <code> musaico.kernel.module.ModuleIdentifier </code>
     * </p>
     */
    public static final String REPLACED_BY_MODULE = "module_replaced_by_module";

    /**
     * <p>
     * How to use the module, exposing important elements such
     * as drivers ("/dev/foo"), object systems ("foo_object_system")
     * and so on.
     * </p>
     *
     * <p>
     * This parameter must be localizable (though the package
     * need not come pre-loaded with localized versions of the
     * text).  See the top of the ModuleDescription
     * documentation for more information on international
     * text.
     * </p>
     *
     * @see musaico.kernel.module.ModuleDescription.MANUAL
     * @see musaico.kernel.module.ModuleDescription.OVERVIEW
     *
     * <p>
     * Type: <code> musaico.i18n.text.I18nString </code>
     * </p>
     */
    public static final String SYNOPSIS_USAGE = "module_synopsis_usage";

    /**
     * <p>
     * How much of the code for this module has been covered by
     * tests, a number representing a 0-100 percentage
     * (0%, 33.12345%, 95% and so on).
     * </p>
     *
     * <p>
     * Type: <code> java.lang.Double </code>
     * </p>
     */
    public static final String TEST_COVERAGE = "module_test_coverage";

    /**
     * <p>
     * The URL whence this module's binary package can be retrieved.
     * </p>
     *
     * <p>
     * Depending on the Module implementation, this description
     * might be the actual location of the binary module
     * (such as "file:///tmp/musaico_xml-1.0.0.binary.jar"), or
     * it might be a URL to the module's website
     * (such as "http://www.musaico.org/downloads/modules/musaico_xml-1.0.0.binary.jar").
     * </p>
     *
     * <p>
     * This URL need not point directly to the packaged module,
     * as long as it leads a clever human to the right spot.
     * For example, if the actual packaged modules is
     * stored at
     * "http://www.musaico.org/nightlybuilds/modules/musaico_xml-2011-04-25.binary.jar",
     * then providing the URL "http://www.musaico.org/nightlybuilds/modules"
     * should be sufficient.  The version of the module will tell
     * the clever human the rest of what they need to know to
     * find the actual package.
     * </p>
     *
     * <p>
     * Type: <code> java.lang.String </code>
     * </p>
     */
    public static final String URL_BINARY = "module_url_binary";

    /**
     * <p>
     * URL for reporting bugs with the module.
     * </p>
     *
     * <p>
     * Type: <code> java.lang.String </code>
     * </p>
     */
    public static final String URL_BUGS = "module_url_bugs";

    /**
     * <p>
     * URL to a full description of the module.
     * </p>
     *
     * <p>
     * Module builders should make every effort to localize
     * the documentation provided at this URL.
     * </p>
     *
     * <p>
     * Type: <code> java.lang.String </code>
     * </p>
     */
    public static final String URL_DESCRIPTION = "module_url_description";

    /**
     * <p>
     * URL for discussing features of the module (often a forum or mailing
     * list archive).
     * </p>
     *
     * <p>
     * Type: <code> java.lang.String </code>
     * </p>
     */
    public static final String URL_DEVELOPERS = "module_url_developers";

    /**
     * <p>
     * URL to examples of using the module.
     * </p>
     *
     * <p>
     * Module builders should make every effort to localize
     * the documentation provided at this URL.
     * </p>
     *
     * <p>
     * Type: <code> java.lang.String </code>
     * </p>
     */
    public static final String URL_EXAMPLES = "module_url_examples";

    /**
     * <p>
     * The URL whence this module's source code can be retrieved.
     * </p>
     *
     * <p>
     * Depending on the Module implementation, this description
     * might be the actual location of the module's source code
     * (such as "http://192.168.0.1:8080/repos/musaico_xml/trunk"), or
     * it might be a URL to the module's website
     * (such as "http://www.musaico.org/downloads/modules/musaico_xml-1.0.0.source.jar").
     * </p>
     *
     * <p>
     * This URL need not point directly to the module's source,
     * as long as it leads a clever human to the right spot.
     * For example, if the actual module source is
     * archived at
     * "http://www.musaico.org/nightlybuilds/modules/musaico_xml-2011-04-25.source.jar",
     * then providing the URL "http://www.musaico.org/nightlybuilds/modules"
     * should be sufficient.  The version of the module will tell
     * the clever human the rest of what they need to know to
     * find the actual source code.
     * </p>
     *
     * <p>
     * Type: <code> java.lang.String </code>
     * </p>
     */
    public static final String URL_SOURCE = "module_url_source";

    /**
     * <p>
     * URL to the tests which exercise the module (either source
     * or binary or both, and possibly also including test run
     * reports).
     * </p>
     *
     * <p>
     * Type: <code> java.lang.String </code>
     * </p>
     */
    public static final String URL_TESTS = "module_url_tests";

    /**
     * <p>
     * URL to information about troubleshooting the module,
     * such as instrumentation details, logging information,
     * debugging tips, and so on.
     * </p>
     *
     * <p>
     * Module builders should make every effort to localize
     * the documentation provided at this URL.
     * </p>
     *
     * <p>
     * Type: <code> java.lang.String </code>
     * </p>
     */
    public static final String URL_TROUBLESHOOTING = "module_url_troubleshooting";

    /**
     * <p>
     * URL to user discussion of the module (typically a forum or
     * mailing list archive).
     * </p>
     *
     * <p>
     * Type: <code> java.lang.String </code>
     * </p>
     */
    public static final String URL_USERS = "module_url_users";

    /**
     * <p>
     * URLs pointing to information related to, but outside the
     * scope of, the module (such as libraries on which the module
     * depends, protocols it implements and so on).
     * </p>
     *
     * <p>
     * This is an array of URLs.
     * </p>
     *
     * <p>
     * Type: <code> java.lang.String [] </code>
     * </p>
     */
    public static final String URLS_SEE_ALSO = "module_url_see_also";


    /** Parameters which every ModuleDescription must provide. */
    public static final String [] REQUIRED = new String []
    {
        ModuleDescription.AUTHORS,
        ModuleDescription.COPYRIGHTS,
        ModuleDescription.ID,
        ModuleDescription.LICENSE_TEXT,
        ModuleDescription.LICENSE_TYPES,
        ModuleDescription.MAINTAINERS,
        ModuleDescription.OVERVIEW,
        ModuleDescription.URL_BINARY,
        ModuleDescription.URL_SOURCE
    };


    /** Parameters which ModuleDescriptions can provide, if they
     *  choose. */
    public static final String [] OPTIONAL = new String []
    {
        ModuleDescription.CONFIGURATION,
        ModuleDescription.DEPENDENCIES,
        ModuleDescription.DEPRECATED_SINCE,
        ModuleDescription.MANUAL,
        ModuleDescription.REPLACED_BY_MODULE,
        ModuleDescription.SYNOPSIS_USAGE,
        ModuleDescription.TEST_COVERAGE,
        ModuleDescription.URL_BUGS,
        ModuleDescription.URL_DESCRIPTION,
        ModuleDescription.URL_DEVELOPERS,
        ModuleDescription.URL_EXAMPLES,
        ModuleDescription.URL_TESTS,
        ModuleDescription.URL_TROUBLESHOOTING,
        ModuleDescription.URL_USERS,
        ModuleDescription.URLS_SEE_ALSO
    };


    /** The text for an end-to-end module description, for
     *  converting the entire thing into an I18nString. */
    public static final String FULL_TEXT =
          "Module:           [%" + ModuleDescription.ID + "%]\n"
        + "License(s):       [%" + ModuleDescription.LICENSE_TYPES + "%]\n"
        + "Full License Text:\n[%" + ModuleDescription.LICENSE_TEXT + "%]\n"
        + "\n\n"
        + "Copyright (c) [%" + ModuleDescription.COPYRIGHTS + "%]\n"
        + "\n"
        + "OVERVIEW\n[%" + ModuleDescription.OVERVIEW + "%]\n\n"
        + "SYNOPSIS\n[%" + ModuleDescription.SYNOPSIS_USAGE + "%]\n\n"
        + "CONFIGURATION[%" + ModuleDescription.CONFIGURATION + "%]\n"
        + "\n"
        + "Author(s):        [%" + ModuleDescription.AUTHORS + "%]\n"
        + "Maintainer(s):    [%" + ModuleDescription.MAINTAINERS + "%]\n"
        + "\n"
        + "URL for binaries: [%" + ModuleDescription.URL_BINARY + "%]\n"
        + "URL for source:   [%" + ModuleDescription.URL_SOURCE + "%]\n"
        + "URL for bugs:     [%" + ModuleDescription.URL_BUGS + "%]\n"
        + "Description at:   [%" + ModuleDescription.URL_DESCRIPTION + "%]\n"
        + "Developers forum: [%" + ModuleDescription.URL_DEVELOPERS + "%]\n"
        + "Examples at:      [%" + ModuleDescription.URL_EXAMPLES + "%]\n"
        + "Tests at:         [%" + ModuleDescription.URL_TESTS + "%]\n"
        + "Troubleshooting:  [%" + ModuleDescription.URL_TROUBLESHOOTING + "%]\n"
        + "Users forum:      [%" + ModuleDescription.URL_USERS + "%]\n"
        + "\n"
        + "See also: [%" + ModuleDescription.URLS_SEE_ALSO + "%]\n"
        + "\n"
        + "Dependencies:     [%" + ModuleDescription.DEPENDENCIES + "%]\n"
        + "Deprecated?:      [%" + ModuleDescription.DEPRECATED_SINCE + "%]\n"
        + "Replaced by?:     [%" + ModuleDescription.REPLACED_BY_MODULE + "%]\n"
        + "Test coverage %:  [%" + ModuleDescription.TEST_COVERAGE + "%]\n"

        + "MANUAL\n[%" + ModuleDescription.MANUAL + "%]";
}
