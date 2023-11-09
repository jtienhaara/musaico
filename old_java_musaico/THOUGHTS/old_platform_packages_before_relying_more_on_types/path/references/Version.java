package musaico.foundation.io.references;

import java.io.Serializable;

import java.util.regex.Pattern;


import musaico.foundation.io.Comparison;
import musaico.foundation.io.NaturallyOrdered;
import musaico.foundation.io.Order;
import musaico.foundation.io.SoftReference;


/**
 * <p>
 * Represents a version number.
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
public class Version
    extends SimpleSoftReference<String>
    implements NaturallyOrdered<Version>, Serializable
{
    /** Splits each version into segments, separated by dots
     *  and/or dashes and/or underscores (".", "-", "_"). */
    private static final Pattern SEGMENT_SPLITTER =
	Pattern.compile ( "[\\.\\-\\_]" );


    /** Represents any version.  Typically used only for matching,
     *  not for representing an object's actual version!
     *  Must be declared AFTER SEGMENT_SPLITTER. */
    public static final Version ANY = new Version ();


    /** The segments of this version number, separated by dots / dashes /
     *  underscore / and so on. */
    private final String [] segments;


    /**
     * <p>
     * Creates a new Version number.
     * </p>
     */
    public Version (
                    String version
                    )
    {
        super ( version );

	segments = Version.SEGMENT_SPLITTER.split ( version );
    }


    /**
     * <p>
     * Creates the singleton which matches any Version number.
     * </p>
     */
    private Version ()
    {
        this ( "*" );
    }


    /**
     * @see java.lang.Object#equals(Object)
     */
    public boolean equals (
                           Object that
                           )
    {
	if ( that == null
	     || ! ( that instanceof Version ) )
	{
	    return false;
	}

	Comparison comparison =
	    VersionOrder.DEFAULT.compareValues ( this, (Version) that );
	if ( comparison.equals ( Comparison.LEFT_EQUALS_RIGHT ) )
	{
	    return true;
	}
	else
	{
	    return false;
	}
    }


    // hashCode, toString implemented by SimpleSoftReference.


    /**
     * @see musaico.foundation.io.NaturallyOrdered#orderIndex()
     */
    public Version orderIndex ()
    {
	return this;
    }


    /**
     * @see musaico.foundation.io.NaturallyOrdered#order()
     */
    public Order<Version> order ()
    {
        return VersionOrder.DEFAULT;
    }


    /**
     * <p>
     * Returns the segments of this version, split by ".", "-", "_" and
     * so on (dots, dashes, underscores, ...).
     * </p>
     *
     * @return The segments of this Version, from most major to most minor.
     *         Never null.  Never contains any null elements.
     */
    public String [] segments ()
    {
	return this.segments;
    }
}
