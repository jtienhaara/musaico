package musaico.kernel;


import musaico.time.AbsoluteTime;


/**
 * <p>
 * Access to the primitives in this kernel's underlying platform,
 * such as retrieving the current system time.
 * </p>
 *
 * <p>
 * The PlatformOperations are typically passed directly
 * to untrusted module code, via kernel modules.  So care must
 * be taken by implementers to not expose sensitive platform
 * data or break fragile platforms.
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
public interface PlatformOperations
{
    /**
     * <p>
     * Returns the current system time for the kernel.
     * </p>
     *
     * <p>
     * Most kernels will simply return <code> AbsoluteTime.now () </code>.
     * However kernels designed with stepped timing (such as sequenced
     * graphics kernels, or automated testing kernels) might return
     * a stale system time -- the time recorded at the start of the
     * current "slice" of processing.
     * </p>
     *
     * @return This kernel's current system time.  Never null.
     */
    public AbsoluteTime now ();
}
