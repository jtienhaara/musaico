package musaico.kernel.objectsystem;

import java.io.Serializable;


import musaico.io.Path;

import musaico.io.references.SimpleSoftReference;


/**
 * <p>
 * Advisory and other flags for accessing Records.
 * </p>
 *
 *
 * <p>
 * In Java, every RecordFlag must be Serializable in order
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
public class RecordFlag
    extends SimpleSoftReference<Path>
    implements Serializable
{
    /** Advisory flag: record will be accessed sequentially. */
    public static final RecordFlag SEQUENTIAL_ACCESS =
        new RecordFlag ( new Path ( "/flags/record/sequential" ) );

    /** Advisory flag: record will be accessed randomly. */
    public static final RecordFlag RANDOM_ACCESS =
        new RecordFlag ( new Path ( "/flags/record/random" ) );


    /**
     * <p>
     * Creates a new RecordFlag with the specified path.
     * </p>
     *
     * @param path The unique path identifier for this RecordFlag.
     *             Must not be null.
     */
    protected RecordFlag (
                          Path path
                          )
    {
        super ( path );
    }
}
