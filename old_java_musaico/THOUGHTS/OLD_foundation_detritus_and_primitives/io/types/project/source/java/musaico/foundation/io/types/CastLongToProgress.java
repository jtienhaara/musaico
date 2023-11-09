package musaico.foundation.io.types;


import java.io.Serializable;


import musaico.foundation.io.Progress;
import musaico.foundation.io.Reference;

import musaico.foundation.io.progresses.SimpleProgress;

import musaico.foundation.types.TypeCastException;
import musaico.foundation.types.TypeCaster;
import musaico.foundation.types.TypeException;


/**
 * <p>
 * Casts values from Long to Progress, assuming 100 steps of which
 * the Long value have been completed.
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
public class CastLongToProgress
    implements TypeCaster<Long,Progress>, Serializable
{
    /**
     * @see musaico.foundation.types.TypeCaster#cast(FROM,Class)
     */
    public Progress cast (
                          Long from,
                          Class to_class
                          )
        throws TypeException
    {
        long num_steps = from;
        if ( num_steps <= 0L )
        {
            throw new TypeCastException ( "Cannot cast from Long [%long_value%] to Progress class [%to_class%]: long value out of range",
                                          "long_value", from,
                                          "to_class", to_class );
        }

        Progress progress = new SimpleProgress ();
        Reference frame = progress.push ( 100L );
        for ( long step = 0L; step < num_steps; step ++ )
        {
            progress.step ( frame );
        }

        return progress;
    }
}
