package musaico.region.types;


import java.io.Serializable;

import musaico.region.Position;
import musaico.region.Region;
import musaico.region.SparseRegion;

import musaico.region.time.AbsoluteTimePosition;
import musaico.region.time.TimeRegion;

import musaico.types.TypeCastException;
import musaico.types.TypeCaster;
import musaico.types.TypeException;


/**
 * <p>
 * Casts Regions to Strings.
 * </p>
 *
 * <p>
 * Each Region is cast to its endpoints as: <code> region:{start..end}; </code>
 * Each sparse region is cast to its sub-regions as:
 * <code> sparse_region:{{start1..end1},{start2..end2},...,{startN..endN}}; </code>
 * </p>
 *
 * <p>
 * Regions which belong to customized spaces (such as a TimeRegion,
 * whose time interval between points depends on its TimeSpace)
 * also include extra information in their String casts
 * (such as <code> [interval=...] </code> for a TimeRegion)
 * after the <code> } </code> close brace but before the <code> ; </code>
 * semi-colon.  For example:
 * <code> region:{0s,10s}[interval=1s]; </code>
 * </p>
 *
 * <p>
 * Each start position and each end position is cast to a String
 * using the appropriate Position-to-String typecaster.
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
 * Copyright (c) 2010, 2011 Johann Tienhaara
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
public class CastRegionToString
    implements TypeCaster<Region,String>, Serializable
{
    /** Casts start and end Positions to strings for us. */
    private final CastPositionToString positionToString =
        new CastPositionToString ();

    /** Casts start and end Sizes to strings for us. */
    private final CastSizeToString sizeToString =
        new CastSizeToString ();


    /**
     * @see musaico.types.TypeCaster#cast(FROM,Class)
     */
    public String cast (
                        Region from,
                        Class to_class
                        )
        throws TypeException
    {
        StringBuilder sbuf = new StringBuilder ();
        Position start = from.start ();
        Position end = from.end ();

        String start_string =
            this.positionToString.cast ( start, String.class );
        String end_string =
            this.positionToString.cast ( end, String.class );

        sbuf.append ( "region:{" );

        if ( ! ( from instanceof SparseRegion ) )
        {
            // Regular Region.
            sbuf.append ( start_string );
            sbuf.append ( ".." );
            sbuf.append ( end_string );
        }
        else
        {
            // Sparse Region.
            SparseRegion sparse_region = (SparseRegion) from;
            for ( long sr = 0L; sr < sparse_region.numRegions (); sr ++ )
            {
                // Recurse to cast the sub-region to a String.
                Region sub_region = sparse_region.region ( sr );
                String sub_region_string =
                    this.cast ( sub_region, String.class );
                sbuf.append ( sub_region_string );
            }
        }

        sbuf.append ( "}" );

        if ( from instanceof TimeRegion )
        {
            sbuf.append ( "[interval=" );
            String interval_as_string =
                this.sizeToString.cast ( from.space ().one (), String.class );
            sbuf.append ( interval_as_string );
            sbuf.append ( ",timetype=" );
            if ( ( from.start () instanceof AbsoluteTimePosition ) )
            {
                sbuf.append ( "absolute" );
            }
            else
            {
                sbuf.append ( "relative" );
            }
            sbuf.append ( "]" );
        }

        sbuf.append ( ";" );

        return sbuf.toString ();
    }
}
