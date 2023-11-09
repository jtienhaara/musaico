package musaico.foundation.term.incomplete;

import java.io.Serializable;


import musaico.module.AbstractMusaicoModule;
import musaico.module.License;


/**
 * <p>
 * Module settings for the musaico.foundation.term.incomplete module.
 * </p>
 *
 *
 * <p>
 * In Java every MusaicoModule must be Serializable in order
 * to play nicely over RMI.
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
 * <p>
 * For copyright and licensing information, please refer to:
 * </p>
 *
 * @see musaico.foundation.term.incomplete.MODULE#COPYRIGHT
 * @see musaico.foundation.term.incomplete.MODULE#LICENSE
 */
public final class MODULE
    extends AbstractMusaicoModule
    implements Serializable
{
    /** Human-readable information about where this module may be
     *  downloaded and/or purchased, bug tracked, and so on,
     *  such as a world wide web HTTP(S) address, or a mailing
     *  address, and so on. */
    private static final String ADDRESS = "http://www.musaico.org";

    /** Human-readable contact information about the person(s)
     *  to contact for information, bug reports, contributions,
     *  payments, and so on.  For example, the developer(s)
     *  and/or maintainers of this module's source code,
     *  and/or the maintainers and/or project manager(s) responsible
     *  for roadmapping, version control, binary distributions,
     *  testing, and/or the entities responsible for selling this
     *  module, and so on.  Can take the form of email address(es)
     *  or mailing address(es) and so on. */
    private static final String CONTACT =
        "Johann Tienhaara <jtienhaara@yahoo.com>";

    /** Human-readable copyright information for this module. */
    private static final String COPYRIGHT =
        "Copyright (c) 2009-2018 Johann Tienhaara <jtienhaara@yahoo.com>";

    /** Human-readable description of the purpose of this module. */
    private static final String DESCRIPTION =
        "Incomplete terms, including Partial values (which can never be completed), as well as the basis for other incomplete terms (such as Blocking terms, which will possibly complete some day, and guaranteed-to-complete terms, such as expressions to be evaluated on complete input terms, without interacting with the outside world, and so on).";

    /** Run "make serializable" to test and/or generate a new hash. */
    private static final String HASH =
        "0x7C825BC36AD1DE630787BB84313A9AFC20BE8E83";

    /** The license under which binaries and/or source code of this
     *  module may be distributed.
     *  @see musaico.module.License */
    private static final License LICENSE = License.GPL3;

    /** The serialVersionUID for all classes in this module.
     *  Set this to the Gregorian calendar date on which you last
     *  edited any class in this module.  YYYYMMDD format.
     *  Package-private so that the classes of this module can declare
     *  this VERSION as their serialVersionUID. */
    static final long VERSION = 20170429;


    private static final long serialVersionUID = VERSION;


    /**
     * <p>
     * The module containing metadata about the source files in this
     * directory: versioning information, licensing information,
     * copyright information, and so on.
     * </p>
     */
    public static final MODULE MODULE = new MODULE ();


    /**
     * <p>
     * Creates the singleton MODULE.
     * </p>
     */
    private MODULE ()
    {
    }


    /**
     * @see musaico.build.MusaicoModule#address()
     */
    @Override
    public final String address ()
    {
        return ADDRESS;
    }


    /**
     * @see musaico.build.MusaicoModule#contact()
     */
    @Override
    public final String contact ()
    {
        return CONTACT;
    }


    /**
     * @see musaico.build.MusaicoModule#copyright()
     */
    @Override
    public final String copyright ()
    {
        return COPYRIGHT;
    }


    /**
     * @see musaico.build.MusaicoModule#description()
     */
    @Override
    public final String description ()
    {
        return DESCRIPTION;
    }


    /**
     * @see musaico.build.MusaicoModule#hash()
     */
    @Override
    public final String hash ()
    {
        return HASH;
    }


    /**
     * @see musaico.build.MusaicoModule#license()
     */
    @Override
    public final License license ()
    {
        return LICENSE;
    }


    /**
     * @see musaico.build.MusaicoModule#version()
     */
    @Override
    public final long version ()
    {
        return VERSION;
    }
}
