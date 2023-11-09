package musaico.platform.primitives.casters;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.types.Cast;
import musaico.foundation.types.NoValue;
import musaico.foundation.types.Operation;
import musaico.foundation.types.Typing;
import musaico.foundation.types.TypingEnvironment;
import musaico.foundation.types.Value;
import musaico.foundation.types.ValueBuilder;


/**
 * <p>
 * Casts String ids to their corresponding Operations in specific
 * TypingEnvironments.
 * </p>
 *
 * <p>
 * Only operations which exist in this caster's parent typing environment
 * will be cast.  Thus passing in 5 ids, only 2 of which are known, will
 * result in 2 Operations being output.
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
public class CastStringToOperation
    extends Cast<String, Operation>
    implements Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20130915L;
    private static final String serialVersionHash =
        "0xE9C0D6545D8C45FAFA107C66DB6D908041E43ABB";


    /** Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( CastStringToOperation.class );


    /** The typing environment in which to look up operations. */
    private final TypingEnvironment environment;


    /**
     * <p>
     * Creates a new CastStringToOperation type caster for operations
     * in the specified environment.
     * </p>
     *
     * @param environment The typing environment in which to look up
     *                    each Operation by id.  Must not be null.
     */
    public CastStringToOperation (
                                  TypingEnvironment environment
                                  )
    {
        super ( String.class,
                Operation.class,
                Operation.NONE );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               environment );

        this.environment = environment;
    }


    /**
     * @see musaico.foundation.types.Cast#evaluate(java.lang.Object)
     *
     * Final for optimization / speed.
     */
    @Override
    @SuppressWarnings("rawtypes") // Constructor needs Operation.class
    public final Value<Operation> evaluate (
                                            Value<String> from
                                            )
    {
        final ValueBuilder<Operation> builder =
            new ValueBuilder<Operation> ( this.targetClass (),
                                          this.none () );
        for ( String id : from )
        {
            final Operation<?, ?> operation =
                this.environment.operation ( this.environment, id );
            builder.add ( operation );
        }

        final Value<Operation> result = builder.build ();

        return result;
    }
}
