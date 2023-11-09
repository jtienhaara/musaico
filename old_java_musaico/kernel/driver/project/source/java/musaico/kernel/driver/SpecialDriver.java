package musaico.kernel.driver;


import musaico.field.Attribute;
import musaico.field.Field;

import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Reference;

import musaico.kernel.objectsystem.Cursor;


/**
 * <p>
 * A Driver with "special" (driver-specific) command(s).
 * </p>
 *
 * <p>
 * Currently the interface is here for future trailblazers.  At the
 * time of writing, there are no intended uses.
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
 * Copyright (c) 2009, 2010, 2011, 2012 Johann Tienhaara
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
public interface SpecialDriver
    extends Driver
{
    /**
     * <p>
     * Performs some special, driver-dependent command on this driver.
     * </p>
     *
     * <p>
     * State pre-requisites (Driver.INITIALIZED and so on) depend on
     * the driver and on the driver and on the special command.
     * </p>
     *
     * @param cursor The Cursor connection to this driver.  Must not be null.
     *
     * @param special_command The type of special command to perform.
     *                        For example, get_statistics, or set_protocol,
     *                        and so on.  Driver-dependent.  Must
     *                        be one of the commands returned by
     *                        specialCommands ().  Must not be null.
     *
     * @param special_parameters Zero or more parameters for use by
     *                           the special command.  Must match the
     *                           attributes returned by
     *                           specialParameters ( special_command ).
     *                           Must not be null.  Must not contain
     *                           any null elements.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     *
     * @throws DriverException If the driver encounters a problem while
     *                         trying to execute the special command,
     *                         including an invalid state (such as
     *                         uninitialized).
     */
    public abstract void special (
                                  Cursor cursor,
                                  Reference special_command,
                                  Field ... special_parameters
                                  )
        throws I18nIllegalArgumentException,
               DriverException;


    /**
     * <p>
     * Returns the special commands which this driver can perform.
     * </p>
     *
     * <p>
     * The results might change over time, depending on the driver.
     * </p>
     *
     * @return A copy of the array of special commands which
     *         this driver can perform.  Never null.  Never contains
     *         any null elements.
     */
    public abstract Reference [] specialCommands ();


    /**
     * <p>
     * Returns the parameter names and types for the specified special
     * command.
     * </p>
     *
     * @param special_command The command being queried.
     *                        Must be one of the commands returned by
     *                        specialCommands ().  Must not be null.
     *
     * @return A copy of the array of Attributes defining the names
     *         and types of parameters to pass when executing the
     *         special command.  Note that the array is always returned
     *         in sorted order with the ArrayPosition of each parameter
     *         corresponding to the Attribute's position in the array.
     *         For example, parameters [ 0 ] will always have an
     *         ArrayPosition ( 0 ), and parameters [ n ] will always have
     *         an ArrayPosition ( n ).  Never contains any null elements.
     *         Never null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public abstract Attribute<?> [] specialParameters (
                                                       Reference special_command
                                                       )
        throws I18nIllegalArgumentException;
}
