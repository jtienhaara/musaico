package musaico.state;

import java.io.Serializable;


/**
 * <p>
 * Represents the singleton label for an Arc exiting a Graph
 * to the outside.
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
public class ExitLabel
    extends UniqueLabel
    implements Serializable
{
    /** The singleton ExitLabel. */
    private static final ExitLabel singleton = new ExitLabel ();


    /**
     * <p>
     * Private singleton constructor.
     * </p>
     *
     * <p>
     * Invoke <code>ExitLabel.get ()</code> to retrieve the
     * ExitLabel.
     * </p>
     */
    private ExitLabel ()
    {
    }


    /**
     * <p>
     * Returns the ExitLabel.
     * </p>
     */
    public static ExitLabel get ()
    {
        return ExitLabel.singleton;
    }
}
