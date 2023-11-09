package musaico.i18n;


import java.io.Serializable;


/**
 * <p>
 * Generic LocalizationException.  Thrown by the Musaico
 * internationalization and localization system whenever
 * a localized representation cannot be built.  Not to
 * be used outside of the musaico/i18n module!
 * </p>
 *
 * <p>
 * This is the only exception thrown by Musaico for which it
 * is acceptable to have no localized error messages.
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
 * Copyright (c) 2009 Johann Tienhaara
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
public class LocalizationException
    extends Exception
    implements Serializable
{
    /**
     * <p>
     * Creates a new LocalizationException with the
     * specified message.
     * </p>
     */
    public LocalizationException (
                                  String message
                                  )
    {
        super ( message );
    }


    /**
     * <p>
     * Creates a new LocalizationException with the
     * specified message and root cause.
     * </p>
     */
    public LocalizationException (
                                  String message,
                                  Throwable cause
                                  )
    {
        super ( message, cause );
    }
}
