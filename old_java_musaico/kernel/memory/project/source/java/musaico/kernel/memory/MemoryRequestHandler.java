package musaico.kernel.memory;

import java.io.Serializable;


/**
 * <p>
 * An handler for MemoryRequests.
 * </p>
 *
 * <p>
 * For example, one handler might handle read, write and resize
 * requests sent to a Segment backed by a PagedArea.  And so on.
 * </p>
 *
 *
 * <p>
 * In Java every MemoryRequestHandler must be Serializable in order
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
public interface MemoryRequestHandler
    extends Serializable
{
    /**
     * <p>
     * Handles the specified request, such as reading or writing
     * from/to memory.
     * </p>
     *
     * <p>
     * Any exceptions or Throwables must be caught and stored in
     * the MemoryRequest itself, so that the requester (most likely
     * on a different thread) will find out about the issues that
     * prevented the request from being fulfilled.
     * </p>
     *
     * <p>
     * Once the request has been fulfilled, its success() method shall be
     * invoked by this method.
     * </p>
     *
     * @param request The MemoryRequest to execute.  Must not be null.
     */
    public abstract <RESPONSE extends Serializable>
        void handleRequest (
                            MemoryRequest<RESPONSE> request
                            );
}
