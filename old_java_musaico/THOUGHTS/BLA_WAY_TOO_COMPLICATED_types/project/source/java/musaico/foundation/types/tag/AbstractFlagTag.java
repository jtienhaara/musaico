package musaico.types.tag;


import java.io.Serializable;


import musaico.types.Instance;
import musaico.types.Tag;


/**
 * <p>
 * A flag Tag, indicating something about Instances to which the
 * flag applies.
 * </p>
 *
 * <p>
 * Flags do not do anything during the <code> check ( Instance ) </code>
 * method.  Flags are simply indicators to application developers.
 * </p>
 *
 *
 * <p>
 * In Java, all Tags must be Serializable in order
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
 * Copyright (c) 2010 Johann Tienhaara
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
public abstract class AbstractFlagTag
    implements Tag, Serializable
{
    /**
     * @see musaico.types.Tag#check(Instance)
     */
    public final void check (
                             Instance instance
                             )
    {
        // Do nothing.  This is just a flag.
    }


    /**
     * @see java.lang.Object#toString()
     */
    public abstract String toString ();
}
