package musaico.platform.mutex;

import java.io.Serializable;


/**
 * <p>
 * A request token for a fork in the dining philosophers problem.
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
public class Dibs
    implements Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20130214L;
    private static final String serialVersionHash =
        "0x14E96987F7E029B8CAEB9191E84A37651BBF2B60";


    /** No Dibs at all. */
    public static final Dibs NONE = new Dibs ( Fork.NONE );


    private final Fork fork;

    public Dibs (
                 Fork fork
                 )
    {
        this.fork = fork;
    }


    public Fork fork ()
    {
        return this.fork;
    }


    public String toString ()
    {
        return "Dibs_for_" + this.fork;
    }
}
