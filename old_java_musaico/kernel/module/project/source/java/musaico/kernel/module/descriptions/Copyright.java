package musaico.kernel.module.descriptions;

import java.io.Serializable;

import java.util.regex.Pattern;


import musaico.i18n.exceptions.I18nIllegalArgumentException;


/**
 * <p>
 * A single year and copyright holder, such as "2011 Jane Doe".
 * </p>
 *
 *
 * <p>
 * In Java every Copyright must be Serializable in order to play
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
public class Copyright
    implements Serializable
{
    /** Regular expression matching the year and holder of
     *  a copyright string. */
    private static final Pattern REGEX =
        Pattern.compile ( "^[ ]*(Copyright)?[ ]*(\\(c\\))?[ ]*([0-9]+)[ ]+([^ ].*)[ ]*$" );



    /** The year of this copyright.
     *  Is it OK to just store this as an int??? */
    private final int year;

    /** The owner of the copyright, such as a person or corporation
     *  and so on. */
    private final String holder;


    /**
     * <p>
     * Creates a new Copyright from the specified year for the
     * specified owner.
     * </p>
     *
     * @param year The year of the copyright, such as 2011.  Must
     *             be in Gregorian Calendar years.  Since
     *             transistorized computers did not exist before 1900,
     *             should probably be at least 1900.
     *
     * @param holder The holder of the copyright, such as "Jane Doe"
     *               or "ACME Corp" and so on.  Must not be null.
     *
     * @throws I18nIllegalArgumentException If the specified parameters
     *                                      are invalid (see above).
     */
    public Copyright (
                      int year,
                      String holder
                      )
        throws I18nIllegalArgumentException
    {
        if ( year < 1900
             || holder == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a Copyright in year [%year%] for [%holder%]",
                                                     "year", year,
                                                     "holder", holder );
        }

        this.year = year;
        this.holder = holder;
    }


    /**
     * @return The holder of this copyright.  Never null.
     */
    public String holder ()
    {
        return this.holder;
    }


    /**
     * <p>
     * Parses the specified copyright text into a Copyright.
     * </p>
     *
     * <p>
     * The text must have a year and one or more names, optionally
     * preceded by "Copyright" and/or "(c)".
     * </p>
     *
     * @param copyright_string The copyright string to parse.
     *                         Must not be null.
     *
     * @return The parsed Copyright.  Never null.
     *
     * @throws I18nIllegalArgumentException If the specified parameters
     *                                      are invalid (see above).
     */
    public static Copyright parse (
                                   String copyright_string
                                   )
        throws I18nIllegalArgumentException
    {
        try
        {
            String year_string =
                Copyright.REGEX.matcher ( copyright_string )
                .replaceAll ( "$3" );
            int year = Integer.parseInt ( year_string );
            String holder =
                Copyright.REGEX.matcher ( copyright_string )
                .replaceAll ( "$4" );
            return new Copyright ( year, holder );
        }
        catch ( Exception e )
        {
            throw new I18nIllegalArgumentException ( "Could not parse [%copyright_string%] into a Copyright object",
                                                     "copyright_string", copyright_string,
                                                     "cause", e );
        }
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString ()
    {
        return this.year + " " + this.holder;
    }


    /**
     * @return The year of this copyright.
     */
    public int year ()
    {
        return this.year;
    }
}
