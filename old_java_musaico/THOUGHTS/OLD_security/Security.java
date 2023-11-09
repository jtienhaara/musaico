package musaico.security;

import java.io.Serializable;


import musaico.io.Reference;


/**
 * <p>
 * The basic interface for security configurations for Musaico.
 * </p>
 *
 * <p>
 * For example, some objects might provide security configurations
 * in the UNIX user-group-other permissions format; while others
 * might be restricted through capabilities; and so on.
 * </p>
 *
 *
 * <p>
 * In Java, every Security implementation must implement Serializable
 * in order to play nicely across RMI.
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
 * Copyright (c) 2010 Johann Tienhaara
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
public interface Security<PERMISSION extends Reference>
    extends Serializable
{
    /**
     * <p>
     * Given a request for the specified permissions, returns
     * the granted permissions.
     * </p>
     *
     * @return The context-dependent granted Permissions.  Never null.
     */
    public abstract Permissions<PERMISSION> request (
                                                     Permissions<PERMISSION> requested_permissions
                                                     );
}
