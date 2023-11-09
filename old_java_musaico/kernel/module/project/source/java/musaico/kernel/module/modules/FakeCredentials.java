package musaico.kernel.module;

import java.io.Serializable;


import musaico.io.Reference;

import musaico.io.references.SimpleSoftReference;

import musaico.security.Credentials;


/**
 * <p>
 * Fake credentials.  For testing only.
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
public final class FakeCredentials
    implements Credentials, Serializable
{
    private final Reference id;


    public FakeCredentials ()
    {
        this ( new SimpleSoftReference<String> ( "fake" ) );
    }


    public FakeCredentials (
                            Reference id
                            )
    {
        this.id = id;
    }


    /**
     * @see musaico.security.Credentials#id()
     */
    public Reference id ()
    {
        return null;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals (
                           Object other
                           )
    {
        return false;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode ()
    {
        return 0;
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString ()
    {
        return "Fake credentials!";
    }
}
