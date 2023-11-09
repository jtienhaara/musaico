package musaico.foundation.io.types;


import java.io.Serializable;

import musaico.foundation.io.Progress;

import musaico.foundation.types.TypeCastException;
import musaico.foundation.types.TypeCaster;
import musaico.foundation.types.TypeException;


/**
 * <p>
 * Casts Progresses to (long) positions out of 100.
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
public class CastProgressToLong
    implements TypeCaster<Progress,Long>, Serializable
{
    /**
     * @see musaico.foundation.types.TypeCaster#cast(FROM,Class)
     */
    public Long cast (
                      Progress from,
                      Class to_class
                      )
        throws TypeException
    {
        if ( from == null )
        {
            throw new TypeCastException ( "Cannot cast Progress [%progress%] to [%to_class%]",
                                          "progress", from,
                                          "to_class", to_class );
        }

        double completed = from.completed ();
        long truncated_step_number_out_of_100 = (long) ( 100.0D * completed );
        if ( truncated_step_number_out_of_100 < 0L )
        {
            truncated_step_number_out_of_100 = 0L;
        }
        else if ( truncated_step_number_out_of_100 > 100L )
        {
            truncated_step_number_out_of_100 = 100L;
        }

        return new Long ( truncated_step_number_out_of_100 );
    }
}
