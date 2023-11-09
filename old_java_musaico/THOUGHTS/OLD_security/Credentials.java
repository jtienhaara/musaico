package musaico.security;

import java.io.Serializable;


import musaico.io.Reference;


/**
 * <p>
 * Credentials for a security request.
 * </p>
 *
 * <p>
 * For example, a user name and password, or a signature, and so on,
 * which in some way originated a request for permission to do
 * something.
 * </p>
 *
 * <p>
 * Security providers should be careful to shut down access to
 * promiscuous Credentials (for example, a privileged user who
 * shares their user name and password with the world, resulting
 * in a flood of identical Credentials requesting access to
 * secure data).
 * </p>
 *
 * <p>
 * Developers should also be careful to never place anything
 * confidential in a Credentials implementation, since it may
 * be shared with non-trustworthy agents.  For example, never
 * store a plaintext password in a Credentials.
 * </p>
 *
 *
 * <p>
 * In Java every Credentials must implement equals() and hashCode().
 * </p>
 *
 * <p>
 * In Java every Credentials must be Serializable in order to play
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
 * Copyright (c) 2009, 2012 Johann Tienhaara
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
public interface Credentials
    extends Serializable
{
    /**
     * <p>
     * An identifier for this Credentials (such as a user ID,
     * an IP address, a session ID, and so on).
     * </p>
     *
     * @return The identifier for this Credentials.  Never null.
     */
    public abstract Reference id ();
}
