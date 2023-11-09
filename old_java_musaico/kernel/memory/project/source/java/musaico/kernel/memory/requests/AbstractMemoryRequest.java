package musaico.kernel.memory.requests;

import java.io.Serializable;


import musaico.i18n.Internationalized;

import musaico.i18n.exceptions.I18nIllegalArgumentException;
import musaico.i18n.exceptions.TimeoutException;

import musaico.i18n.message.Message;

import musaico.io.Identifier;

import musaico.kernel.memory.MemoryException;
import musaico.kernel.memory.MemoryRequest;

import musaico.security.Credentials;

import musaico.time.AbsoluteTime;
import musaico.time.RelativeTime;


/**
 * <p>
 * Handles the boilerplate code for all memory requests, such as
 * storing responses and response failures.
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
public abstract class AbstractMemoryRequest<RESPONSE extends Serializable>
    implements MemoryRequest<RESPONSE>, Serializable
{
    /** Synchronize critical sections on this lock. */
    private final Serializable lock = new String ();

    /** The Credentials of the requester.  Typically the user id
     *  or Module id asking for the memory operation. */
    private final Credentials credentials;

    /** The successful response to this request. */
    private RESPONSE response = null;

    /** The failure/timeout response to this request. */
    private Internationalized<Message,String> responseFailure = null;

    /** Has this request been fulfilled / failed / timed out yet?
     *  If not it is still active. */
    private boolean isActive = true;

    /** Did this request fail or timeout?  If not then it is successful
     *  (though until isActive = false it has not completed). */
    private boolean isSuccessful = true;

    /** The target Segment / other memory request handler by which
     *  this request is to be handled. */
    private final Identifier targetRef;

    /** The absolute time at which this request times out.
     *  !!! Currently based on system time, which is less than ideal
     *  !!! since the user of the system can change it under our feet... */
    private final AbsoluteTime timeoutTime;

    /** Sleep on this token when waitFor () is invoked: */
    private final Serializable sleepToken = new String ();


    /**
     * <p>
     * Creates a new AbstractMemoryRequest to be fulfilled by the
     * specified target within the specified relative time.
     * </p>
     *
     * @param credentials The Credentials of the requester.
     *                    Typically the memory request handler
     *                    will do some initial security checks,
     *                    and these Credentials will also be
     *                    used for sub-operations such as swapping.
     *                    Must not be null.
     *
     * @param target_id The id of the memory request handler
     *                  which will fulfill this request.
     *                  For example, the id of a Segment.
     *                  Must not be null.
     *
     * @param timeout Amount of time which the handler has to complete
     *                this request, relative to right now.  Must not
     *                be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public AbstractMemoryRequest (
                                  Credentials credentials,
                                  Identifier target_id,
                                  RelativeTime timeout
                                  )
        throws I18nIllegalArgumentException
    {
        this ( credentials,
               target_id,
               timeout == null
               ? (AbsoluteTime) null
               : (AbsoluteTime) AbsoluteTime.now ().add ( timeout ) );
    }


    /**
     * <p>
     * Creates a new AbstractMemoryRequest to be fulfilled by the
     * specified target before the specified time.
     * </p>
     *
     * @param credentials The Credentials of the requester.
     *                    Typically the memory request handler
     *                    will do some initial security checks,
     *                    and these Credentials will also be
     *                    used for sub-operations such as swapping.
     *                    Must not be null.
     *
     * @param target_id The id of the memory request handler
     *                  which will fulfill this request.
     *                  For example, the id of a Segment.
     *                  Must not be null.
     *
     * @param timeout_time Time at which this request will timeout.
     *                     Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public AbstractMemoryRequest (
                                  Credentials credentials,
                                  Identifier target_id,
                                  AbsoluteTime timeout_time
                                  )
        throws I18nIllegalArgumentException
    {
        if ( credentials == null
             || target_id == null
             || timeout_time == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a [%memory_request_class%] for credentials [%credentials%] with target id [%target_id%] timeout time [%timeout_time%]",
                                                     "memory_request_class", this.getClass (),
                                                     "credentials", credentials,
                                                     "target_id", target_id,
                                                     "timeout_time", timeout_time );
        }

        this.credentials = credentials;
        this.targetRef = target_id;
        this.timeoutTime = timeout_time;
    }


    /**
     * @see musaico.kernel.memory.MemoryRequest#credentials()
     */
    public final Credentials credentials ()
    {
        return this.credentials;
    }


    /**
     * @see musaico.kernel.memory.MemoryRequest#failure(musaico.i18n.Internationalized)
     */
    public final void failure (
                               Internationalized<Message,String> response_exception
                               )
    {
        synchronized ( this.lock )
        {
            if ( ! this.isActive )
            {
                // Sorry bub, can't finish this request twice.
                return;
            }

            this.isActive = false;
            this.isSuccessful = false;
            this.responseFailure = response_exception;
        }

        synchronized ( this.sleepToken )
        {
            // Although unlikely there could conceivably be
            // more than one thread waiting for this one request
            // to finish.
            this.sleepToken.notifyAll ();
        }
    }


    /**
     * @see musaico.kernel.memory.MemoryRequest#isActive()
     */
    public boolean isActive ()
    {
        synchronized ( this.lock )
        {
            return this.isActive;
        }
    }


    /**
     * @see musaico.kernel.memory.MemoryRequest#isSuccessful()
     */
    public boolean isSuccessful ()
    {
        synchronized ( this.lock )
        {
            return this.isSuccessful;
        }
    }


    /**
     * @see musaico.kernel.memory.MemoryRequest#response()
     */
    public RESPONSE response ()
    {
        synchronized ( this.lock )
        {
            return this.response;
        }
    }


    /**
     * @see musaico.kernel.memory.MemoryRequest#responseFailure()
     */
    public Internationalized<Message,String> responseFailure ()
    {
        synchronized ( this.lock )
        {
            return this.responseFailure;
        }
    }


    /**
     * @see musaico.kernel.memory.MemoryRequest#success(java.io.Serializable)
     */
    public void success (
                         RESPONSE response
                         )
    {
        synchronized ( this.lock )
        {
            if ( ! this.isActive )
            {
                // Sorry bub, can't finish this request twice.
                return;
            }

            this.isActive = false;
            this.isSuccessful = true;
            this.response = response;
        }

        synchronized ( this.sleepToken )
        {
            // Although unlikely there could conceivably be
            // more than one thread waiting for this one request
            // to finish.
            this.sleepToken.notifyAll ();
        }
    }


    /**
     * @see musaico.kernel.memory.MemoryRequest#targetRef()
     */
    public Identifier targetRef ()
    {
        return this.targetRef;
    }


    /**
     * @see musaico.kernel.memory.MemoryRequest#timeoutTime()
     */
    public AbsoluteTime timeoutTime ()
    {
        return this.timeoutTime;
    }


    /**
     * @see musaico.kernel.memory.MemoryRequest#waitFor()
     */
    public MemoryRequest waitFor ()
        throws I18nIllegalArgumentException,
               TimeoutException,
               MemoryException
    {
        AbsoluteTime now = AbsoluteTime.now ();
        long milliseconds_to_wait;
        try
        {
            milliseconds_to_wait =
                this.timeoutTime.subtract ( now ).milliseconds ();
        }
        catch ( Exception e )
        {
            // Already expired!
            milliseconds_to_wait = 1L;
        }

        synchronized ( this.sleepToken )
        {
            if ( this.isActive () )
            {
                try
                {
                    this.sleepToken.wait ( milliseconds_to_wait );
                }
                catch ( InterruptedException e )
                {
                    // Down below we will throw a timeout exception.
                }
            }
        }

        synchronized ( this.lock )
        {
            if ( this.isActive () )
            {
                TimeoutException timeout_exception =
                    new TimeoutException ( "Failed to complete [%memory_request_class%] [%memory_request%] before [%timeout_time%]",
                                           "memory_request_class", this.getClass (),
                                           "memory_request", this,
                                           "timeout_time", this.timeoutTime );
                this.failure ( timeout_exception );
                throw timeout_exception;
            }

            if ( ! this.isSuccessful () )
            {
                Internationalized<Message,String> response_failure =
                    this.responseFailure ();
                if ( response_failure != null )
                {
                    if ( response_failure instanceof MemoryException )
                    {
                        MemoryException memory_exception =
                            (MemoryException) response_failure;
                        throw memory_exception;
                    }
                    else if ( response_failure instanceof TimeoutException )
                    {
                        TimeoutException timeout_exception =
                            (TimeoutException) response_failure;
                        throw timeout_exception;
                    }
                    else
                    {
                        throw new MemoryException ( response_failure );
                    }
                }

                // Else caller will have to find out by inspecting what
                // went wrong.
            }
        }

        return this;
    }
}
