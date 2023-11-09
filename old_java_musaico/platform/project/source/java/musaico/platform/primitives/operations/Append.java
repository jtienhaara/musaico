package musaico.platform.primitives.operations;

import java.io.Serializable;


!!!
/**
 * <p>
 * Appends a fixed value(s) to the end of the input value(s).
 * </p>
 *
 * <p>
 * For example, the following code might be used to append
 * 3 to the end of the input values { 1, 2 }:
 * </p>
 *
 * <pre>
 *     Type<Integer> integer = ...;
 *     Instance<Integer> my_sequence =
 *         integer.builder ()
 *                .add ( 1 )
 *                .add ( 2 )
 *                .build ();
 *     for ( int i : my_sequence.call ( new Append<Integer> ( 3 ) ) )
 *     {
 *         System.out.println ( "" + i );
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
public class Append<ELEMENT extends Object>
    implements Operation<ELEMENT, ELEMENT>, Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20130905L;
    private static final String serialVersionHash =
        "0x6E8A8887B3ADBFF74B93BCF43B108C65E162FBB8";


    /** The 
    /** The class of output returned by this pipe. */
    private final Class<OUTPUT> outputClass;

    /** The default "no value" value in case of pipe failure. */
    private final OUTPUT none;

    /** The operations in the pipe, in order. */
    private final Operation<?, ?> [] pipedOperations;


    /**
     * <p>
     * Creates a new pipe with the specified final output class and
     * default "no value" value in case of failure, and the specified
     * operations to pipe together.
     * </p>
     *
     * @param output_class The class of values which will be output
     *                     by this Pipe's evaluate () operation.
     *                     Must not be null.
     *
     * @param none The "no value" value to fall back on for the output
     *             value in case of failure.  Must not be null.
     *
     * @param operations The operations to execute in the pipe, in order.
     *                   Must not be null.  Must not contain any
     *                   null elements.
     */
    @SuppressWarnings("rawtypes") // Stupid Java forces generic array then complains
    public Pipe (
                 Class<OUTPUT> output_class,
                 OUTPUT none,
                 Operation<?, ?> ... operations
                 )
    {
        this.outputClass = output_class;
        this.none = none;
        this.pipedOperations = new Operation [ operations.length ];
        System.arraycopy ( operations, 0,
                           this.pipedOperations, 0, operations.length );
    }


    /**
     * @see musaico.foundation.types.Operation#alwaysFail(musaico.foundation.types.TypingEnvironment, musaico.foundation.types.Typing.Violation)
     */
    @Override
    public AlwaysFail<INPUT, OUTPUT> alwaysFail (
                                                 TypingEnvironment environment,
                                                 Typing.Violation violation
                                                 )
    {
        return new AlwaysFail<INPUT, OUTPUT> ( this.outputClass,
                                               violation,
                                               this.none );
    }


    /**
     * @see musaico.foundation.types.CompositeOperation#duplicate(musaico.foundation.types.Operation[])
     */
    public CompositeOperation<INPUT, OUTPUT> duplicate (
                                                        Operation<?, ?> ... operations
                                                        )
    {
        return new Pipe<INPUT, OUTPUT> ( this.outputClass,
                                         this.none,
                                         operations );
    }


    /**
     * @see musaico.foundation.types.Operation#evaluate(musaico.foundation.types.Value)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast exceptions explicitly caught
    public Value<OUTPUT> evaluate (
                                   Value<INPUT> input
                                   )
    {
        Value<Object> current_input = (Value<Object>) input;
        Operation<Object, Object> current_operation = null;
        Value<Object> current_output = null;
        try
        {
            for ( Operation<?, ?> operation : this.pipedOperations )
            {
                current_operation = (Operation<Object, Object>) operation;
                current_output = current_operation.evaluate ( current_input );

                current_input = current_output;
            }
        }
        catch ( ClassCastException e )
        {
            final Typing.Violation violation =
                new Typing.Violation ( Typing.TypeCasterMustBeRegistered.CONTRACT,
                                       TypingEnvironment.NONE,
                                       new Object [] { current_operation,
                                                       current_input,
                                                       current_output },
                                       e );
            return new NoValue<OUTPUT> ( this.outputClass,
                                         violation,
                                         this.none );
        }

        if ( current_output == null
             || ! this.outputClass.isAssignableFrom ( current_output.expectedClass () ) )
        {
            final Typing.Violation violation =
                new Typing.Violation ( Typing.TypeCasterMustBeRegistered.CONTRACT,
                                       TypingEnvironment.NONE,
                                       new Object [] { current_operation,
                                                       current_input,
                                                       current_output },
                                       new IllegalStateException ( "Invalid output " + current_output ) );
            return new NoValue<OUTPUT> ( this.outputClass,
                                         violation,
                                         this.none );
        }

        return (Value<OUTPUT>) current_output;
    }


    /**
     * @see musaico.foundation.types.Operation#id()
     */
    @Override
    public String id ()
    {
        final StringBuilder sbuf = new StringBuilder ();
        boolean is_first_operation = true;
        for ( Operation<?, ?> operation : this.pipedOperations )
        {
            if ( is_first_operation )
            {
                is_first_operation = false;
            }
            else
            {
                sbuf.append ( " | " );
            }

            sbuf.append ( "( " + operation + " )" );
        }

        return sbuf.toString ();
    }


    /**
     * @see musaico.foundation.types.CompositeOperation#operations()
     */
    public Operation<?, ?> [] operations ()
    {
        final Operation<?, ?> [] operations =
            new Operation<?, ?> [ this.pipedOperations.length ];
        System.arraycopy ( this.pipedOperations, 0,
                           operations, 0, this.pipedOperations.length );

        return operations;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return this.id ();
    }
}
