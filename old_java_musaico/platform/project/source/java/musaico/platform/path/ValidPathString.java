package musaico.platform.path;

import java.io.Serializable;

import java.util.Comparator;


import musaico.foundation.contract.Domain;


/**
 <p>
 * The domain of String representations of Paths, which must match
 * certain requirements (must not end with a single trailing backslash,
 * for example).
 * </p>
 *
 *
 * <p>
 * In Java every Domain must be Serializable in order to play nicely
 * across RMI.
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
 * Copyright (c) 2013 Johann Tienhaara
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
public class ValidPathString
    implements Domain<String>, Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20131015L;
    private static final String serialVersionHash =
        "0xEB5A10EDC2BE159676941E962268F363B42EBF29";


    /** The singleton contract for checking that a String can
     *  make a valid Path (no trailing backslashes and so on). */
    public static final ValidPathString DOMAIN =
        new ValidPathString ();


    /**
     * @see musaico.foundation.contract.Domain#isValid(java.lang.Object)
     */
    @Override
    public boolean isValid (
                            String value
                            )
    {
        if ( value == null )
        {
            return false;
        }

        // Trailing backslashes must come in pairs.
        int num_trailing_backslashes = 0;
        for ( int c = value.length () - 1; c >= 0; c -- )
        {
            final char ch = value.charAt ( c );
            if ( ch != '\\' )
            {
                break;
            }

            num_trailing_backslashes ++;
        }

        if ( ( num_trailing_backslashes % 2 ) != 0 )
        {
            return false;
        }

        return true;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "valid path String";
    }
}
