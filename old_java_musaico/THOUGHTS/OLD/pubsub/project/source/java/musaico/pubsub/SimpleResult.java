package musaico.pubsub;


import java.io.Serializable;


/**
 * <p>
 * Represents a simple Result from publishing or handling an event.
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
 * Copyright (c) 2009 Johann Tienhaara
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
public class SimpleResult
    implements Result, Serializable
{
    /** A simple "unhandled" response, indicating the subscriber(s)
     *  did not do anything in response to the publication. */
    public static final SimpleResult UNHANDLED =
        new SimpleResult ( "unhandled" );


    /**
     * <p>
     * Returns the "unhandled" result.
     * </p>
     */
    public static SimpleResult unhandled ()
    {
        return SimpleResult.UNHANDLED;
    }


    /**
     * <p>
     * Ors together two Results.
     * </p>
     */
    public static final SimpleResult or (
                                         Serializable result1,
                                         Serializable result2
                                         )
    {
        return null; // !!!
    }


    /** The name of this result (such as "unhandled", "handled", and
     *  so on). */
    private final String resultID;


    /**
     * <p>
     * Creates a new SimpleResult from the specified result ID.
     * </p>
     */
    private SimpleResult (
                          String result_id
                          )
    {
        this.resultID = result_id;
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString ()
    {
        return this.resultID;
    }
}
