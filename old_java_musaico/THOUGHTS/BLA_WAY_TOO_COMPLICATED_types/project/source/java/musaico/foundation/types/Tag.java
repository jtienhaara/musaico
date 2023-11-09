package musaico.types;


import java.io.Serializable;


/**
 * <p>
 * A Tag providing some information, and optionally restrictions,
 * on Instances.
 * </p>
 *
 * <p>
 * For example, a Type is a Tag, and a Constraint is also a Tag.
 * </p>
 *
 * <p>
 * A Tag might also be a flag.  For example, a <code> PrivacyTag </code>
 * might be used to indicate that an Instance should not be shared
 * over the network, or stored unencrypted.
 * </p>
 *
 *
 * <p>
 * In Java, all Tags must be Serializable in order
 * to play nicely over RMI.
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
public interface Tag
    extends Serializable
{
    /**
     * <p>
     * Checks the specified Instance against this Tag, and
     * throws a TypeException if the Instance is not valid
     * for this Tag.
     * </p>
     *
     * <p>
     * For example, a Type would check the Instance against all
     * its Constraints; a Constraint would validate the Instance
     * against itself; and other Tags might have entirely different
     * checks, or none at all.
     * </p>
     *
     * @param instance The Instance to check.
     *                 Must not be null.
     *
     * @throws TypeException If the Instance is not valid for this Tag.
     */
    public abstract void check (
                                Instance instance
                                )
        throws TypeException;
}
