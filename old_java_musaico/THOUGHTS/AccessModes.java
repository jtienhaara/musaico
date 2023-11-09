package musaico.kernel.objectsystem;


/**
 * <p>
 * The access modes of an object (readable, writable, and so on).
 * </p>
 *
 * <p>
 * For example, to determine if the user who owns an ONode can write
 * to it:
 * </p>
 *
 * <pre>
 *     ONode onode...;
 *     AccessModes modes = ( onode.modes () &gt;&gt; AccessModes.USER )
 *                 &amp; AccessModes.ALL; 
 *     if ( ( modes &amp; AccessModes.WRITABLE ) != 0 )
 *     {
 *         // Writable...
 *     }
 *     else
 *     {
 *         // Not writable...
 *     }
 * </pre>
 *
 * <p>
 * Alternatively, use one of the helpers:
 * </p>
 *
 * <pre>
 *     if ( ( onode.modes () &amp; AccessModes.USER_WRITABLE ) != 0 )
 *     {
 *         // Writable...
 *     }
 *     else
 *     {
 *         // Not writable...
 *     }
 * </pre>
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
public class AccessModes
{
    /** FLAGS bitshift */
    public static final int         FLAGS =  0;

    /** USER bitshift */
    public static final int         USER =   8;

    /** GROUP bitshift */
    public static final int         GROUP = 16;

    /** OTHER bitshift */
    public static final int         OTHER = 24;


    /** RWX bitmask */
    public static final int         RWX = 0xFF;

    /** READABLE bitmask */
    public static final int         READABLE = 0x01;

    /** WRITABLE bitmask */
    public static final int         WRITABLE = 0x02;

    /** EXECUTABLE bitmask */
    public static final int         EXECUTABLE = 0x04;


    /** Helpers: USER_READABLE */
    public static final int         USER_READABLE = READABLE << USER;
    /** Helpers: USER_WRITABLE */
    public static final int         USER_WRITABLE = WRITABLE << USER;
    /** Helpers: USER_EXECUTABLE */
    public static final int         USER_EXECUTABLE = EXECUTABLE << USER;
    /** Helpers: USER_RWX */
    public static final int         USER_RWX = RWX << USER;

    /** Helpers: GROUP_READABLE */
    public static final int         GROUP_READABLE = READABLE << GROUP;
    /** Helpers: GROUP_WRITABLE */
    public static final int         GROUP_WRITABLE = WRITABLE << GROUP;
    /** Helpers: GROUP_EXECUTABLE */
    public static final int         GROUP_EXECUTABLE = EXECUTABLE << GROUP;
    /** Helpers: GROUP_RWX */
    public static final int         GROUP_RWX = RWX << GROUP;

    /** Helpers: OTHER_READABLE */
    public static final int         OTHER_READABLE = READABLE << OTHER;
    /** Helpers: OTHER_WRITABLE */
    public static final int         OTHER_WRITABLE = WRITABLE << OTHER;
    /** Helpers: OTHER_EXECUTABLE */
    public static final int         OTHER_EXECUTABLE = EXECUTABLE << OTHER;
    /** Helpers: OTHER_RWX */
    public static final int         OTHER_RWX = RWX << OTHER;


    /** The bits for this AccessModes (such as
     *  <code>USER_RWX | OTHER_READABLE</code>). */
    private final int               bits;


    /**
     * <p>
     * Creates a new AccessModes with the specified bits.
     * </p>
     */
    public AccessModes (
                        int bits
                        )
    {
        this.bits = bits;
    }


    /**
     * <p>
     * Returns this bits for this AccessModes.
     * </p>
     */
    public int bits ()
    {
        return this.bits;
    }


    /**
     * @see java.lang.Object#equals(Object)
     */
    public boolean equals (
                           Object obj
                           )
    {
        if ( obj == null )
        {
            return false;
        }
        else if ( obj instanceof AccessModes )
        {
            AccessModes modes = (AccessModes) obj;
            if ( this.bits () == modes.bits () )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString ()
    {
        int bits = this.bits ();
        int flags = bits >> AccessModes.FLAGS;
        int user = bits >> AccessModes.USER;
        int group = bits >> AccessModes.GROUP;
        int other = bits >> AccessModes.OTHER;

        String modes_str =
            ( ( user & AccessModes.READABLE ) != 0      ? "r" : "-" )
            + ( ( user & AccessModes.WRITABLE ) != 0    ? "w" : "-" )
            + ( ( user & AccessModes.EXECUTABLE ) != 0  ? "x" : "-" )
            + ( ( group & AccessModes.READABLE ) != 0   ? "r" : "-" )
            + ( ( group & AccessModes.WRITABLE ) != 0   ? "w" : "-" )
            + ( ( group & AccessModes.EXECUTABLE ) != 0 ? "x" : "-" )
            + ( ( other & AccessModes.READABLE ) != 0   ? "r" : "-" )
            + ( ( other & AccessModes.WRITABLE ) != 0   ? "w" : "-" )
            + ( ( other & AccessModes.EXECUTABLE ) != 0 ? "x" : "-" );

        return modes_str;
    }
}
