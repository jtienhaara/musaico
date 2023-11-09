package musaico.foundation.graph;

import java.util.LinkedHashMap;


import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.value.Maybe;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueViolation;

import musaico.foundation.value.finite.No;
import musaico.foundation.value.finite.One;

import musaico.foundation.value.normal.ValueMustNotBeEmpty;


public class TTT
{
    // Ugly hack for @*%# generics.
    private static class SillyValue<VALUE_CLASS> // VALUE_CLASS = Value<...>
    {
        @SuppressWarnings("unchecked")
        public Class<VALUE_CLASS> getValueClass ()
        {
            return (Class<VALUE_CLASS>) Value.class;
        }
    }

    public static interface Operation
        extends Graph<Value<?>, StandardArc<Value<?>, Value<?>>, Value<?>, Value<?>>
    {
        public abstract Value<?> evaluate ( Value<?> ... parameters );
    }

    public static class AbstractOperation
        extends StandardGraph<Value<?>, Value<?>>
        implements Operation
    {
        public AbstractOperation (
                                  Filter<Value<?>> ... parameter_types
                                  )
        {
            super ( new SillyValue<Value<?>> ().getValueClass (),
                    createArcs ( parameter_types ) );
        }

        public static LinkedHashMap<Value<String>, StandardArc<Value<String>, Value<String>> []> createArcs (
            Filter<Value<?>> ... parameter_types
            )
        {
            final LinkedHashMap<Value<String>, StandardArc<Value<String>, Value<String>> []> arcs =
                new LinkedHashMap<Value<String>, StandardArc<Value<String>, Value<String>> []> ();

            final ValueViolation violation =
                ValueMustNotBeEmpty.CONTRACT.violation ( this,
                                                         "" );
            Value<?> node = new No<Object> ( Object.class,
                                             violation );
            for ( Filter<Value<?>> parameter_type : parameter_types )
            {
final Value<?>
                final StandardArc<Value<?>, Value<?>> arc =
                    new Standard
        }
    }

    public static void main ( String [] args )
    {
        final Filter<Value<String>> any_string =
            new Filter<Value<String>> ()
            {
                @Override
                public final FilterState filter (
                                                 Value<String> grain
                                                 )
                {
                    if ( grain == null )
                    {
                        return FilterState.DISCARDED;
                    }
                    else
                    {
                        return FilterState.KEPT;
                    }
                }
            };

        StandardArc<Value<String>, String> param;

        final LinkedHashMap<Value<String>, StandardArc<Value<String>, Value<String>> []> concatenate_arcs =
            new LinkedHashMap<Value<String>, StandardArc<Value<String>, Value<String>> []> ();

        param = new StandardArc<Value<String>, Value<String>> ( any_string, "parameters" );
        concatenate_arcs.add ( "start", param );

        param = new StandardArc<Value<String>, Value<String>> ( any_string, "parameters" );
        concatenate_arcs.add ( new No<String> ( String.class, violation ),
                               param );

        final StandardGraph<Value<String>, Value<String>> concatenate =
            new StandardGraph<Value<String>, Value<String>> (
                new SillyValue<Value<String>> ().getValueClass (),
                concatenate_arcs );

final Value<String> 
        for ( Value<String> output : concatenate.traverse ( 
    }
}
