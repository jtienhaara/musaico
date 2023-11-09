package musaico.security;

import java.io.Serializable;


import musaico.io.Reference;


/**
 * <p>
 * Defines permissions for a security configuration or a request
 * for permissions.
 * </p>
 *
 * <p>
 * A given permission is a Reference object, and a Permissions
 * object includes zero or more permission References.
 * </p>
 *
 * <p>
 * Typically a Permissions implementation specifies a certain
 * range of permission References.  For example, Permissions
 * might be typed as:
 * </p>
 *
 * <ul>
 *   <li>
 *     <code> Permissions&lt;Command&gt; </code>
 *     For permissions to execute Commands.
 *   </li>
 *
 *   <li>
 *     <code> Permissions&lt;FileAccess&gt; </code>
 *     For file access permissions (read, write, execute, and so on).
 *   </li>
 *
 *   <li>
 *     <code> Permissions&lt;Attribute&gt; </code>
 *     For permissions to access specific attributes.
 *   </li>
 * </ul>
 *
 * <p>
 * And so on.
 * </p>
 *
 *
 * <p>
 * In Java, every Permissions must implement equals() and
 * hashCode().
 * </p>
 *
 * <p>
 * In Java, all Permissions must be Serializable in order to play
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
 * Copyright (c) 2010, 2011 Johann Tienhaara
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
public interface Permissions<PERMISSION extends Reference>
    extends Serializable
{
    /**
     * <p>
     * Returns the allowed permissions.
     * </p>
     *
     * @return The allowed (requested or granted) permissions.  Never null.
     */
    public abstract PERMISSION [] allowed ();


    /**
     * <p>
     * Returns the credentials for these (requested / granted) Permissions.
     * </p>
     *
     * <p>
     * For example, a user or group name, and so on.
     * </p>
     *
     * @return The credentials to which these Permissions apply.
     *         For example, a user.  Never null.
     */
    public abstract Credentials credentials ();


    /**
     * <p>
     * Returns true if the specified permission is included
     * in this (requested/granted) permissions set, or false otherwise.
     * </p>
     *
     * @param permission The permission to inspect.  Must not be null.
     *
     * @return True if the permission is (requested / granted),
     *         false otherwise.
     */
    public abstract boolean isAllowed (
                                       PERMISSION permission
                                       );


    /**
     * <p>
     * Returns true if all of the specified permissions are
     * included in this (requested/granted) permissions set,
     * or false otherwise.
     * </p>
     *
     * @param permissions The permissions to inspect.  Must not be null.
     *
     * @return True if all of the permissions are (requested / granted),
     *         false otherwise.
     */
    public abstract boolean isAllowed (
                                       Permissions<PERMISSION> permissions
                                       );


    /**
     * <p>
     * Returns the target for the (requested / granted) permissions.
     * </p>
     *
     * <p>
     * For example, a reference to a file, or a reference to an
     * object, and so on.
     * </p>
     *
     * @return The target of these (requested / granted) permissions.
     *         A context-dependent reference to some entity.  Never null.
     */
    public abstract Reference target ();
}
