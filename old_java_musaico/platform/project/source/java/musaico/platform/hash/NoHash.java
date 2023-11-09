package musaico.platform.hash;

import java.io.Serializable;


/**
 * <p>
 * An empty, useless algorithm-less hash.  (Everything hashes to
 * a single 0 byte!)
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
 * Copyright (c) 2012 Johann Tienhaara
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
public class NoHash
    implements Hash, Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20130916L;
    private static final String serialVersionHash =
        "0x61AC98B19A9B0A635172D9E3677CDDF6F43867DD";


    /**
     * @see musaico.foundation.hash.Hash#bytes()
     */
    @Override
    public byte [] bytes ()
    {
        return new byte [] { 0 };
    }
}
