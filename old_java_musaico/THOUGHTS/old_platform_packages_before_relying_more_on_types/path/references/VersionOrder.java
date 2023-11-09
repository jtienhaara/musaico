package musaico.foundation.io.references;

import java.io.Serializable;


import musaico.foundation.i18n.Internationalized;

import musaico.foundation.i18n.exceptions.L10n;

import musaico.foundation.i18n.message.Message;
import musaico.foundation.i18n.message.SimpleMessageBuilder;

import musaico.foundation.io.AbstractOrder;
import musaico.foundation.io.Comparison;
import musaico.foundation.io.DictionaryOrder;
import musaico.foundation.io.Order;


/**
 * <p>
 * The default natural order for versions, comparing first by
 * major release, then minor, then sub release numbers, with
 * sections divided by dots ("."), and optionally followed by
 * appendices such as pre-release version names and numbers
 * ("a1", "rc13", "-canada-rc2", and so on).
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
public class VersionOrder
    extends AbstractOrder<Version>
    implements Serializable
{
    /** The default version order, first by major release, then
     *  by minor release, then sub-releases, and so on, and finally
     *  alphanumerically by any trailing names ("-canada", "rc1"
     *  and so on). */
    public static final VersionOrder DEFAULT =
	new VersionOrder ();


    /** Compares each segment of two Versions by dictionary (alphanumeric)
     *  order. */
    private final Order<String> segmentOrder = Order.DICTIONARY;


    /**
     * <p>
     * Creates a new VersionOrder.
     * </p>
     */
    public VersionOrder ()
    {
        super ( "Version order" );
    }


    /**
     * @see musaico.foundation.io.Order#compare(java.lang.Object,java.lang.Object)
     *
     * Final for speed.
     */
    public final Comparison compareValues (
                                           Version left,
                                           Version right
                                           )
    {
        if ( left == null )
        {
            if ( right == null )
            {
                // null == null.
                return Comparison.LEFT_EQUALS_RIGHT;
            }

            // null > any Version.
            return Comparison.INCOMPARABLE_LEFT;
        }
        else if ( right == null )
        {
            // any Version < null.
            return Comparison.INCOMPARABLE_RIGHT;
        }


	String [] segments_left = left.segments ();
	String [] segments_right = right.segments ();

	for ( int s = 0; s < segments_left.length; s ++ )
	{
	    if ( s >= segments_right.length )
	    {
		// a.b.c > a.b,
		return Comparison.LEFT_GREATER_THAN_RIGHT;
	    }

	    Comparison comparison =
		this.segmentOrder.compareValues ( segments_left [ s ],
                                                  segments_right [ s ] );
	    if ( ! comparison.equals ( Comparison.LEFT_EQUALS_RIGHT ) )
	    {
		return comparison;
	    }
	}

	// All segments matched, the versions are identical.
        return Comparison.LEFT_EQUALS_RIGHT;
    }
}
