package musaico.platform.primitives.casters;

import java.io.Serializable;


import musaico.foundation.types.Cast;
import musaico.foundation.types.NoValue;
import musaico.foundation.types.Operation;
import musaico.foundation.types.Typing;
import musaico.foundation.types.Value;
import musaico.foundation.types.ValueBuilder;


/**
 * <p>
 * Casts Operations to their String ids.
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
 * Copyright (c) 2013 Johann Tienhaara
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
@SuppressWarnings("rawtypes") // Constructor needs Operation.class
public class CastOperationToString
    extends Cast<Operation,String>
    implements Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20130915L;
    private static final String serialVersionHash =
        "0x6F3F48DA27EAE734499B6441C1FC31EDE5B6CAC7";


    /**
     * <p>
     * Creates a new CastOperationToString type caster.
     * </p>
     */
    public CastOperationToString ()
    {
        super ( Operation.class,
                String.class,
                "" ); // None
    }


    /**
     * @see musaico.foundation.types.Cast#evaluate(java.lang.Object)
     *
     * Final for optimization / speed.
     */
    @Override
    @SuppressWarnings("rawtypes") // Constructor needs Operation.class
    public final Value<String> evaluate (
                                         Value<Operation> from
                                         )
    {
        final ValueBuilder<String> builder =
            new ValueBuilder<String> ( this.targetClass (),
                                       this.none () );
        for ( Operation<?, ?> operation : from )
        {
            final String id = operation.id ();
            builder.add ( id );
        }

        final Value<String> result = builder.build ();

        return result;
    }
}
