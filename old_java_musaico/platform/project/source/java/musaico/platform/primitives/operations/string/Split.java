package musaico.platform.primitives.operations.string;

import java.io.Serializable;

import java.util.regex.Pattern;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.types.AbstractOperation;
import musaico.foundation.types.NoValue;
import musaico.foundation.types.Value;
import musaico.foundation.types.ValueBuilder;


/**
 * <p>
 * Splits each input String by some regular expression.
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
public class Split
    extends AbstractOperation<String, String>
    implements Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20130915L;
    private static final String serialVersionHash =
        "0xDB811EF0C9792387D395D721366496819F833C36";


    /** Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( Split.class );


    /** The regular expression used to split the text into parts. */
    private final Pattern partRegex;


    /**
     * <p>
     * Creates a new Split operation to divide each String into parts.
     * </p>
     *
     * @param part_regex The regular expression used to split each
     *                   String into parts.  Must not be null.
     */
    public Split (
                  Pattern part_regex
                  )
        throws ParametersMustNotBeNull.Violation
    {
        super ( "split["
                + ( part_regex == null ? "null"
                        : part_regex.pattern () )
                + "]",
                String.class,
                "" ); // None

        Split.classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                                     part_regex );

        this.partRegex = part_regex;
    }


    /**
     * @see musaico.foundation.types.Operation#evaluate(musaico.foundation.types.Value)
     */
    @Override
    public Value<String> evaluate (
                                   Value<String> input
                                   )
    {
        if ( input instanceof NoValue )
        {
            return input;
        }

        final ValueBuilder<String> builder =
            new ValueBuilder<String> ( String.class, "" );
        for ( String text : input )
        {
            for ( String part : this.partRegex.split ( text ) )
            {
                builder.add ( part );
            }
        }

        return builder.build ();
    }
}
