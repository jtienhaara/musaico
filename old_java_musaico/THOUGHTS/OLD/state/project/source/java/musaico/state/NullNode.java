package musaico.state;


import java.io.Serializable;


import musaico.io.Reference;


/**
 * <p>
 * Represents no reachable Node.
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
public final class NullNode
    implements Node, Serializable
{
    /** Only one NullNode in the whole universe.  Unless RMI is used... */
    private static final NullNode singleton = new NullNode ();


    /** No need for anyone but us to construct a NullNode. */
    private NullNode ()
    {
    }


    /**
     * @see musaico.state.Node#arc(musaico.io.Reference)
     */
    public Arc arc (
                    Reference label
                    )
    {
        return null;
    }


    /**
     * <p>
     * Returns the singleton NullNode.
     * </p>
     *
     * <p>
     * (Note that NullNode is Serializable, so may not be the
     * same singleton across RMI.)
     * </p>
     */
    public static final NullNode get ()
    {
        return NullNode.singleton;
    }




    /**
     * @see musaico.state.Node#automatic()
     */
    public Arc automatic ()
    {
        return null;
    }


    /**
     * @see musaico.state.Node#hasArcTo( Node )
     */
    public boolean hasArcTo (
                             Node target
                             )
    {
        return false;
    }


    /**
     * @see musaico.state.Node#labels()
     */
    public Reference [] labels ()
    {
        return new Reference [ 0 ];
    }


    /**
     * @see musaico.state.Node#otherwiseArc()
     */
    public Arc otherwiseArc ()
    {
        return null;
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString ()
    {
        return "NullNode";
    }
}
