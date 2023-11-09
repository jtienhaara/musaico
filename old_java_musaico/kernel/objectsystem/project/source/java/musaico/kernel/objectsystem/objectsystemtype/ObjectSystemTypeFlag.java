package musaico.kernel.objectsystem.objectsystemtype;

import java.io.Serializable;


import musaico.io.Path;

import musaico.io.references.SimpleSoftReference;


/**
 * <p>
 * Advisory and other flags for accessing ObjectSystemFactories.
 * </p>
 *
 *
 * <p>
 * In Java, every ObjectSystemTypeFlag must be Serializable in order
 * to play nicely across RMI.
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
public class ObjectSystemTypeFlag
    extends SimpleSoftReference<Path>
    implements Serializable
{
    /**
     * <p>
     * Creates a new ObjectSystemTypeFlag with the specified path.
     * </p>
     *
     * @param path The unique path identifier for this ObjectSystemTypeFlag.
     *             Must not be null.
     */
    protected ObjectSystemTypeFlag (
                                    Path path
                                    )
    {
        super ( path );
    }
}
