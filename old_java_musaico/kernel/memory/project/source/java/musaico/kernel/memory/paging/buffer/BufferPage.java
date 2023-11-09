package musaico.kernel.memory.paging.buffer;

import java.io.Serializable;


import musaico.buffer.Buffer;

import musaico.kernel.memory.paging.Page;


/**
 * <p>
 * A Page of in-memory Fields.
 * </p>
 *
 * <p>
 * The Buffer of Fields (raw memory) can be retrieved and
 * manipulated through this Page.  However this Buffer
 * should never be accessed directly by Tasks.  Instead,
 * a VirtualBuffer layer should straddle one or more
 * Pages, so that this Page can be swapped out without
 * anyone ever maintaining a reference directly to the
 * Buffer.
 * </p>
 *
 * @see musaico.kernel.memory.paging.pagedareas
 *
 * <p>
 * In Java, all Pages must be Serializable.  Note, however,
 * that a Page may contain non-Serializable data.
 * (For example, a buffer Page with non-Serializable Fields.)
 * Therefore great care must be taken when passing Pages
 * back and forth across RMI.
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
 * Copyright (c) 2009, 2010, 2012 Johann Tienhaara
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
public interface BufferPage
    extends Page, Serializable
{
    /**
     * <p>
     * Returns this page's in-memory Buffer.  DO NOT STORE THE
     * BUFFER IN A VARIABLE.  You must retrieve the Buffer for
     * every action you wish to take on it.
     * </p>
     *
     * <p>
     * If this BufferPage is ever swapped out, the Buffer must
     * be freed.
     * </p>
     *
     * <p>
     * The Region described by the Buffer <b> must </b> have the same layout
     * and size as the Page.
     * </p>
     *
     * <p>
     * This method should be final for speed, since it will likely be
     * accessed frequently.
     * </p>
     *
     * @return This Page's Buffer.  Never null.
     */
    public abstract Buffer buffer ();
}
