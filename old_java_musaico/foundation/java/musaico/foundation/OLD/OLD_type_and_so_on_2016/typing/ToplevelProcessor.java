package musaico.foundation.typing;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.metadata.Metadata;
import musaico.foundation.metadata.StandardMetadata;
import musaico.foundation.metadata.TrackingContracts;

import musaico.foundation.value.AsynchronousResult;
import musaico.foundation.value.NonBlocking;
import musaico.foundation.value.Value;


/**
 * <p>
 * The toplevel Processor, which delegates the processing of Expressions
 * to child Processor(s).
 * </p>
 *
 *
 * <p>
 * In Java every Processor must be either Serializable or a
 * UnicastRemoteObject in order to play nicely with RMI.
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
 * <p>
 * For copyright and licensing information refer to:
 * </p>
 *
 * @see musaico.foundation.typing.MODULE#COPYRIGHT
 * @see musaico.foundation.typing.MODULE#LICENSE
 */
public class ToplevelProcessor
    implements Processor, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks method obligations and guarantees, and tracks violations.
    private final TrackingContracts contracts;

    // Metadata for this ToplevelProcessor, including Statistics about
    // contract violations.
    private final Metadata metadata;


    /**
     * <p>
     * Creates a new ToplevelProcessor.
     * </p>
     *
     * <p>
     * Package-private.  Used by Processor.TOPLEVEL only.
     * </p>
     */
    ToplevelProcessor ()
    {
        this.metadata = new StandardMetadata ();
        this.contracts = new TrackingContracts ( this, this.metadata );
    }


    /**
     * <p>
     * Returns the Processor which will be used to evaluate the
     * specified Expression.
     * </p>
     *
     * @param expression The Expression to be processed.
     *                   Must not be null.
     *
     * @return The Processor which will be used to process the specified
     *         Expression.  Never null.
     */
    public Processor delegateProcessingFor (
                                            Expression<?> expression
                                            )
    {
        return this; // !!! TODO Delegate processing!
    }


    /**
     * @see musaico.foundation.typing.Processor#process(musaico.foundation.typing.Expression, musaico.foundation.value.AsynchronousResult)
     */
    @SuppressWarnings("unchecked") // Cast NonBlocking<?> to Value<Object>.
    public <INPUT extends Object, OUTPUT extends Object>
        void process (
                      Expression<OUTPUT> expression,
                      AsynchronousResult<OUTPUT> result
                      )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               expression, result );

        // !!! TODO delegate instead of executing in-thread!

        final Map<Term<?>, NonBlocking<?>> term_values =
            new HashMap<Term<?>, NonBlocking<?>> ();
        final List<Expression<?>> unevaluated_expressions =
            new ArrayList<Expression<?>> ();

        unevaluated_expressions.add ( expression );

        Expression<?> current_expression = expression;
        while ( current_expression != null )
        {
            final List<Term<?>> inputs = current_expression.inputs ();

            boolean has_expression_inputs = false;
            for ( Term<?> input : inputs )
            {
                if ( term_values.containsKey ( input ) )
                {
                    // Even if it's an Expression, we've already
                    // evaluated it.
                    continue;
                }

                if ( input instanceof Expression )
                {
                    final Expression<?> expression_input =
                        (Expression<?>) input;
                    unevaluated_expressions.add ( 0, expression_input );
                    has_expression_inputs = true;
                }
                else
                {
                    // Block indefinitely.
                    term_values.put ( input, input.value ().await () );
                }
            }

            if ( has_expression_inputs )
            {
                current_expression = unevaluated_expressions.get ( 0 );
                continue;
            }

            unevaluated_expressions.remove ( current_expression );

            final Operation<?> operation =
                current_expression.operation ();

            final List<Value<Object>> input_values =
                new ArrayList<Value<Object>> ();
            for ( Term<?> input : inputs )
            {
                final NonBlocking<?> input_value =
                    term_values.get ( input );
                input_values.add ( (Value<Object>) input_value );
            }

            final NonBlocking<?> output_value =
                operation.evaluate ( input_values )
                .onBlocking ( result.maxTimeoutInNanoseconds () );

            term_values.put ( current_expression, output_value );

            if ( unevaluated_expressions.size () > 0 )
            {
                current_expression = unevaluated_expressions.get ( 0 );
            }
            else
            {
                // We're done.
                current_expression = null;
            }
        }

        final NonBlocking<OUTPUT> output_value =
            (NonBlocking<OUTPUT>) term_values.get ( expression );

        // Safety check - blow up real good if there is a bug above:
        this.contracts.check ( ReturnNeverNull.CONTRACT,
                               output_value );

        result.setFinalResult ( output_value );
    }
}
