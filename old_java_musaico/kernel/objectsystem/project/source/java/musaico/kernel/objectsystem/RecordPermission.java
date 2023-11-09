package musaico.kernel.objectsystem;

import java.io.Serializable;


import musaico.io.Path;


/**
 * <p>
 * Permission to access a Record in some way, such
 * as to read, write or execute the record.
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
public class RecordPermission
    extends RecordFlag
    implements Serializable
{
    /** Permission to create a new Record. */
    public static final RecordPermission CREATE =
        new RecordPermission ( new Path ( "/permissions/record/create" ) );

    /** Permission to read a Record. */
    public static final RecordPermission READ =
        new RecordPermission ( new Path ( "/permissions/record/read" ) );

    /** Permission to write to a Record. */
    public static final RecordPermission WRITE            =
        new RecordPermission ( new Path ( "/permissions/record/write" ) );

    /** Permission to execute the contents of a Record. */
    public static final RecordPermission EXECUTE          =
        new RecordPermission ( new Path ( "/permissions/record/exec" ) );

    /** Perission to append to the end of a Record.
     *  An existing Record will be truncated if NOT opened in Append mode. */
    public static final RecordPermission APPEND           =
        new RecordPermission ( new Path ( "/permissions/record/append" ) );

    /** Perission to use a Record's Mutex and lock it for
     *  exclusive access. */
    public static final RecordPermission LOCK             =
        new RecordPermission ( new Path ( "/permissions/record/lock" ) );

    // Linux include/linux/fs.h also has MAY_OPEN and MAY_ACCESS.
    // Leaving these out for now...
    // Also Linux seems to have file access permissions strewn and
    // duplicated all over the place (e.g. linux/stat.h S_IS*, the top
    // of linux/fs.h F_* and so on).  i_mode, f_mode and so on
    // use different flags, and user space does too (O_*).


    /**
     * <p>
     * Creates a new RecordPermission with the specified path.
     * </p>
     *
     * @param path The unique path identifier for this RecordPermission.
     *             Must not be null.
     */
    protected RecordPermission (
                                Path path
                                )
    {
        super ( path );
    }
}
