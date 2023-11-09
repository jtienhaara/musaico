package musaico.platform.primitives.casters;

import java.io.Serializable;

import java.math.BigDecimal;
import java.math.BigInteger;


import musaico.foundation.contract.Domain;

import musaico.foundation.types.Cast;
import musaico.foundation.types.NoValue;
import musaico.foundation.types.Typing;
import musaico.foundation.types.Value;
import musaico.foundation.types.ValueBuilder;


/**
 * <p>
 * Casts values from BigDecimal to BigInteger.
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
 * Copyright (c) 2012, 2013 Johann Tienhaara
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
public class CastBigDecimalToBigInteger
    extends Cast<BigDecimal,BigInteger>
    implements Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20130902L;
    private static final String serialVersionHash =
        "0x147055C981091993F29AFBAF4CA3783D08F238E3";


    /** All source values which can be cast exactly to the target type
     *  belong to this domain: */
    public static final Domain<BigDecimal> DOMAIN =
        new Domain<BigDecimal> ()
    {
        private static final long serialVersionUID =
            CastBigDecimalToBigInteger.serialVersionUID;
        public final boolean isValid (
                                      BigDecimal value
                                      )
        {
            try
            {
                value.toBigIntegerExact ();
                return true;
            }
            catch ( NumberFormatException e )
            {
                return false;
            }
        }
    };

    /** Any time we can't cast exactly, this contract is violated: */
    public static final Typing.ValueMustBeInDomain<BigDecimal> CONTRACT =
        new Typing.ValueMustBeInDomain<BigDecimal> ( DOMAIN );


    /**
     * <p>
     * Creates a new CastBigDecimalToBigInteger type caster.
     * </p>
     */
    public CastBigDecimalToBigInteger ()
    {
        super ( BigDecimal.class,
                BigInteger.class,
                BigInteger.ZERO );
    }


    /**
     * @see musaico.foundation.types.Cast#evaluate(java.lang.Object)
     *
     * Final for optimization / speed.
     */
    @Override
    public final Value<BigInteger> evaluate (
                                             Value<BigDecimal> from
                                             )
    {
        final ValueBuilder<BigInteger> builder =
            new ValueBuilder<BigInteger> ( this.targetClass (),
                                           this.none () );
        for ( BigDecimal big_decimal : from )
        {
            final BigInteger big_integer;
            try
            {
                big_integer = big_decimal.toBigIntegerExact ();
                builder.add ( big_integer );
            }
            catch ( NumberFormatException e )
            {
                return new NoValue<BigInteger> ( this.targetClass (),
                                                 CONTRACT.violation ( this, big_decimal ),
                                                 this.none () );
            }
        }

        final Value<BigInteger> result = builder.build ();

        return result;
    }
}
