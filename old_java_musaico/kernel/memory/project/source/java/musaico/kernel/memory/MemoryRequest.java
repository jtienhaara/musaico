package musaico.kernel.memory;

import java.io.Serializable;


import musaico.i18n.Internationalized;

import musaico.i18n.exceptions.I18nIllegalArgumentException;
import musaico.i18n.exceptions.TimeoutException;

import musaico.i18n.message.Message;

import musaico.io.Identifier;

import musaico.security.Credentials;

import musaico.time.AbsoluteTime;


/**
 * <p>
 * A request to read, write or modify some area of memory, such
 * as a Segment.
 * </p>
 *
 * <p>
 * Some implementations sort, merge and and schedule access requests,
 * so that, for example, 2 requests to read are handled simultaneously.
 * </p>
 *
 * <p>
 * The generic RESPONSE parameter for a MemoryRequest defines the
 * class of object expected in response to the request once the
 * operation has completed.  For example a
 * <code> MemoryRequest&lt;Region&gt; </code> might be sent to a
 * Segment for a READ or WRITE operation which returns the actual
 * Region of the Buffer read into / written from.
 * And so on.
 * </p>
 *
 *
 * <p>
 * In Java every MemoryRequest must be Serializable in order to
 * play nicely over RMI.
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
public interface MemoryRequest<RESPONSE extends Serializable>
    extends Serializable
{
    /**
     * <p>
     * Returns the Credentials of the requester (such as a User
     * id or a Module id and so on, depending on whence the request
     * originated).
     * </p>
     *
     * <p>
     * The memory request handler typically uses these Credentials
     * to perform operations such as swapping, and may also do
     * security checks of its own before proceeding.
     * </p>
     *
     * @return The Credentials of the requester.  Never null.
     */
    public abstract Credentials credentials ();


    /**
     * <p>
     * Finishes this request unsuccessfully.
     * </p>
     *
     * <p>
     * The memory handling this request should invoke this method
     * whenever it cannot successfully complete the request,
     * for example because of a swap failure or other memory
     * exception.
     * </p>
     *
     * @param response_exception The exception explaining why
     *                           this request failed.  Must not be null.
     */
    public abstract void failure (
                                  Internationalized<Message,String> response_exception
                                  );


    /**
     * <p>
     * Returns true if this request is either queued up to be executed
     * or still executing.  Returns false if it has already succeeded,
     * failed or timed out.
     * </p>
     *
     * @return True if this request has not yet finished, false if it
     *         has already finished (successfully or not).
     */
    public abstract boolean isActive ();


    /**
     * <p>
     * Returns true if this request has not timed out or failed.
     * </p>
     *
     * <p>
     * While a request is still active the <code> isSuccessful () </code>
     * method will always return true, so be sure to check
     * <code> isActive () </code> to make sure it has been
     * fulfilled or timed out before you interpret the isSuccessful ()
     * result.
     * </p>
     *
     * @return True if this request has not timed out or failed,
     *         false if a failure or timeout was generated.
     */
    public abstract boolean isSuccessful ();


    /**
     * <p>
     * Returns the response to this reqest IF this request has
     * successfully completed
     * (<code> ! isActive () &amp;&amp; isSuccessful () </code>).
     * </p>
     *
     * @return The response to this request, as specified by the
     *         memory handler's call to <code> success () </code>.
     *         Can be null if this request failed or has not yet
     *         completed successfully.
     */
    public RESPONSE response ();


    /**
     * <p>
     * Returns the response to this reqest IF this request
     * ended in failure or timeout.
     * (<code> isActive () &amp;&amp; ! isSuccessful () </code>).
     * </p>
     *
     * @return The response to this request, as specified by the
     *         memory handler's call to <code> failure () </code>.
     *         Can be null if this request has not yet failed
     *         or if it completed successfully.
     */
    public Internationalized<Message,String> responseFailure ();


    /**
     * <p>
     * Completes this request with a successful response object.
     * </p>
     *
     * <p>
     * Called by the memory request handler once the requested operation
     * has been completed successfully.
     * </p>
     *
     * @param response The successful response for this request, such
     *                 as the Region of Fields read into / written from.
     *                 Must not be null.
     */
    public abstract void success (
                                  RESPONSE response
                                  );


    /**
     * <p>
     * Returns the identifier of the Segment or other memory object
     * to which this request is addressed.
     * </p>
     *
     * @return The target of this request, such as a SegmentIdentifier.
     *         Never null.
     */
    public abstract Identifier targetRef ();


    /**
     * <p>
     * Returns the timeout time, an absolute time by which this
     * request must be fulfilled, otherwise the <code> failure () </code>
     * method will be invoked with a TimeoutException.
     * </p>
     *
     * @return The timeout time for this request, the time by which
     *         this request must be fulfilled or it will be marked
     *         failed.  Never null.
     */
    public abstract AbsoluteTime timeoutTime ();


    /**
     * <p>
     * Puts the current Thread to sleep until either this
     * request has been fulfilled or the <code> timeoutTime () </code>
     * has been reached.
     * </p>
     *
     * <p>
     * After successfully waiting for the operation to complete (no
     * exceptions thrown, no timeout), the response will be available to the
     * caller by executing <code> response () </code>.
     * </p>
     *
     * <p>
     * If the request fails or if it times out, then an exception
     * will be thrown and also stored in the request, available to the
     * caller by executing <code> responseFailure () </code>.
     *
     * @return This MemoryRequest.  Never null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     *
     * @throws TimeoutException If the request cannot be fulfilled
     *                          before the timeout time is reached.
     *
     * @throws MemoryException If anything goes horribly wrong during
     *                         the operation.  For example, a swap failure
     *                         might occur if the backing store for
     *                         some virtual memory is not accessible.
     *                         In this case the resulting state of
     *                         any relevant data structures (such as
     *                         the Buffer used to read or write from/to
     *                         an area of memory) cannot be guaranteed
     *                         to be in any particular state.
     *                         The operation failed, but for example
     *                         some data may have already been read or
     *                         written before the failure.
     */
    public abstract MemoryRequest<RESPONSE> waitFor ()
        throws I18nIllegalArgumentException,
               TimeoutException,
               MemoryException;


    // Specific memory requests may provide other methods, such as
    // returning a Buffer to read into / write from.
}
