package musaico.foundation.contract.domains;

import java.io.Serializable;

import java.math.BigDecimal;
import java.math.BigInteger;


import musaico.foundation.contract.Domain;


/**
 * <p>
 * The domain of all numbers less than 0.
 * </p>
 *
 *
 * <p>
 * *** Do not forget to add new domains to AllDomains.java! ***
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
public class LessThanZero
    implements Domain<Number>, Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20130929L;
    private static final String serialVersionHash =
        "0x7317D3E3AFA47232D80E5CD12295E8A4DBA8CD9A";


    /** The LessThanZero domain singleton. */
    public static final LessThanZero DOMAIN =
        new LessThanZero ();


    protected LessThanZero ()
    {
    }


    /**
     * @see musaico.foundation.contract.Domain#isValid(java.lang.Object)
     */
    @Override
    public boolean isValid (
                            Number value
                            )
    {
        if ( value == null )
        {
            return false;
        }
        else if ( value instanceof BigDecimal )
        {
            BigDecimal num = (BigDecimal) value;
            if ( num.compareTo ( BigDecimal.ZERO ) < 0 )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else if ( value instanceof BigInteger )
        {
            BigInteger num = (BigInteger) value;
            if ( num.compareTo ( BigInteger.ZERO ) < 0 )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else if ( value instanceof Double )
        {
            Double num = (Double) value;
            if ( num.isNaN () )
            {
                return false;
            }
            else if ( num.isInfinite () )
            {
                return true;
            }
            else if ( num.doubleValue () < 0D )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else if ( value instanceof Float )
        {
            Float num = (Float) value;
            if ( num.isNaN () )
            {
                return false;
            }
            else if ( num.isInfinite () )
            {
                return true;
            }
            else if ( num.floatValue () < 0F )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else if ( ( value instanceof Integer )
                  || ( value instanceof Long ) )
        {
            long num = value.longValue ();
            if ( num < 0L )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            // Unknown type of number.  Can't compare to 0.
            return false;
        }
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "less than zero";
    }
}
