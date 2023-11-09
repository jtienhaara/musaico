package musaico.kernel;


import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Progress;

import musaico.security.Credentials;


/**
 * <p>
 * Encapsulates a request to some kernel object or other, possibly
 * from a user, or possibly from another kernel object.
 * </p>
 *
 * <p>
 * A KernelOperation contains the original request metadata, such
 * as the Credentials of the user or module making the request,
 * the Progress meter to indicate the current
 * status of the request, a trace of the kernel calls made,
 * various statistics such as number of exceptions thrown, profiling
 * times, number of failures and so on.
 * </p>
 *
 * <p>
 * The Kernel itself creates each KernelOperation, and configures
 * it according to which pieces of metadata should be tracked and
 * which should be suppressed.  Generally the fewer pieces of metadata
 * that are tracked, the quicker the runtime operation of the system,
 * but the more difficult troubleshooting will be.
 * </p>
 *
 *
 * <p>
 * In Java every KernelOperation must be Serializable in order
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
public interface KernelOperation
    extends Serializable
{
    /**
     * <p>
     * Returns the Credentials of the User or Module requesting
     * the operation.
     * </p>
     *
     * @return The Credentials of the User or Module requesting the
     *         operation in the first place.  Never null.
     */
    public abstract Credentials credentials ();


    /**
     * <p>
     * Returns the progress meter for this operation state.
     * </p>
     *
     * <p>
     * Each method involved in an operation shall record its own
     * progress through the steps it must take in order to complete
     * the operation.
     * </p>
     *
     * @return This operation state's progress meter.  Never null.
     */
    public abstract Progress progress ();
}
