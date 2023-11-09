package musaico.kernel.memory.segments;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Identifier;

import musaico.kernel.memory.MemoryRequest;
import musaico.kernel.memory.MemoryRequestListener;
import musaico.kernel.memory.Segment;


/**
 * <p>
 * A test memory request listener that executes every
 * segment request as soon as it is received by the segment.
 * </p>
 *
 *
 * <p>
 * In Java, because every memory object can be used across RMI
 * environment, every MemoryRequestListener must either be Serializable
 * or a UnicastRemoteObject and so on.
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
public class DumbMemoryRequestListener
    implements MemoryRequestListener, Serializable
{
    /** Lock all critical sections on this token: */
    private final Serializable lock = new String ();

    /** The segments to which this listener is listening. */
    private final Map<Identifier,Segment> segments =
        new HashMap<Identifier,Segment> ();


    /**
     * @see musaico.kernel.memory.MemoryRequestListener#registerSegment(musaico.kernel.memory.Segment)
     */
    @Override
    public void registerSegment (
                                 Segment segment
                                 )
    {
        synchronized ( this.lock )
        {
            this.segments.put ( segment.id (), segment );
        }
    }


    /**
     * @see musaico.kernel.memory.MemoryRequestListener#requestNotification(musaico.kernel.memory.MemoryRequest)
     */
    @Override
    public <RESPONSE extends Serializable>
        void requestNotification (
                                  MemoryRequest<RESPONSE> request
                                  )
    {
        final Segment segment;
        synchronized ( this.lock )
        {
            segment = this.segments.get ( request.targetRef () );
        }

        if ( segment == null )
        {
            // Fail the request.
            request.failure ( new I18nIllegalArgumentException ( "Listener [%listener%] cannot trigger handling of memory request [%memory_request%] for unknown segment [%segment_ref%]",
                                                                 "listener", this,
                                                                 "memory_request", request,
                                                                 "segment_ref", request.targetRef () ) );
            return;
        }

        segment.handleOneRequest ();

        if ( request.isActive () )
        {
            // Kill it.
            request.failure ( new I18nIllegalArgumentException ( "Listener [%listener%] triggered segment [%segment%] handling request [%memory_request%] but it did not finish for some reason",
                                                                 "listener", this,
                                                                 "segment", segment,
                                                                 "memory_request", request ) );
        }
    }


    /**
     * @see musaico.kernel.memory.MemoryRequestListener#unregisterSegment(musaico.kernel.memory.Segment)
     */
    @Override
    public void unregisterSegment (
                                   Segment segment
                                   )
    {
        synchronized ( this.lock )
        {
            this.segments.remove ( segment.id () );
        }
    }
}
