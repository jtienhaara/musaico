package musaico.state;

import java.io.Serializable;


/**
 * <p>
 * Represents the singleton label for an Arc entering a Graph from
 * the outside.
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
 * Copyright (c) 2009, 2011 Johann Tienhaara
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
public class EnterLabel
    extends UniqueLabel
    implements Serializable
{
    /** The singleton EnterLabel. */
    private static final EnterLabel singleton = new EnterLabel ();


    /**
     * <p>
     * Private singleton constructor.
     * </p>
     *
     * <p>
     * Invoke <code>EnterLabel.get ()</code> to retrieve the
     * EnterLabel.
     * </p>
     */
    private EnterLabel ()
    {
    }


    /**
     * <p>
     * Returns the EnterLabel.
     * </p>
     */
    public static EnterLabel get ()
    {
        return EnterLabel.singleton;
    }
}
