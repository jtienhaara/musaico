package musaico.foundation.type;

import java.util.HashMap;
import java.util.Map;

import musaico.foundation.contract.Contract;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.graph.Arc;
import musaico.foundation.graph.Graph;
import musaico.foundation.graph.StandardGraph;

import musaico.foundation.machine.Machine;
import musaico.foundation.machine.InputsMachine;

import musaico.foundation.value.Maybe;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueViolation;

import musaico.foundation.value.abnormal.Error;

import musaico.foundation.value.builder.ValueBuilder;

import musaico.foundation.value.finite.No;
import musaico.foundation.value.finite.One;

import musaico.foundation.value.normal.Multiple;


public class TestOperation
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void main (
                             String [] args
                             )
        throws Exception
    {
        final Symbol greeting_input = new BasicSymbol ( "greeting" );
        final Symbol adjectives_input =
            new BasicSymbol ( "adjectives..." );
        final Symbol output_type = Text.TYPE;
        final Graph<Symbol, Type> operation1 =
            new StandardGraph<Symbol, Type> (
                Symbol.class, // node_class
                Type.class, // arc_class
                greeting_input,
                output_type,
                new Arc<Symbol, Type> ( greeting_input, Greeting.TYPE, adjectives_input ),
                new Arc<Symbol, Type> ( adjectives_input, Adjective.TYPE, adjectives_input ),
                new Arc<Symbol, Type> ( adjectives_input, Addressee.TYPE, output_type ) );
        System.out.println ( "Operation # 1:" );
        System.out.println ( operation1 );

        // Greetings:
        final One<String> hello =
            new One<String> ( String.class, "hello" );
        final One<String> greetings =
            new One<String> ( String.class, "greetings" );
        final One<String> welcome =
            new One<String> ( String.class, "welcome" );

        // Adjectives:
        final One<String> wonderful =
            new One<String> ( String.class, "wonderful" );
        final One<String> cruel =
            new One<String> ( String.class, "cruel" );
        final One<String> brave =
            new One<String> ( String.class, "brave" );
        final One<String> _new =
            new One<String> ( String.class, "new" );
        final One<String> beautiful =
            new One<String> ( String.class, "beautiful" );

        // Addressees:
        final One<String> world =
            new One<String> ( String.class, "world" );
        final One<String> you =
            new One<String> ( String.class, "you" );
        final One<String> sir =
            new One<String> ( String.class, "sir" );
        final One<String> madam =
            new One<String> ( String.class, "madam" );
        final One<String> dear =
            new One<String> ( String.class, "dear" );

        final One<Symbol> entry = operation1.entry ();
        final One<Symbol> exit = operation1.exit ();
        final OperationBody<String> printer = new Printer ();
        final Operation operation =
            new Operation ( "greet", operation1, printer );


        final long overall_start_time = System.nanoTime ();

        final Value<?> [] [] passes =
            new Value [] []
        {
            new Value [] { hello, world },
            new Value [] { hello, brave, _new, world },
            new Value [] { greetings, madam },
            new Value [] { welcome, you }
        };

        for ( Value<?> [] test : passes )
        {
            final long start_time = System.nanoTime ();

            final Value<Object> output = operation.invoke ( test );

            final long end_time = System.nanoTime ();
            final double duration_ns =
                (double) ( end_time - start_time )
                / 1000000.0D;

            System.out.println ( "Test ( "
                                 + valuesToString ( test )
                                 + " ):\n    "
                                 + output
                                 + "\n        "
                                 + "[ " + duration_ns + " ms ]" );

            if ( ! String.class.equals ( output.expectedClass () ) )
            {
                throw new Exception ( "Test ( "
                                      + valuesToString ( test )
                                      + " ) SHOULD have output a String" );
            }

            System.out.println ( "    SUCCESS" );
            System.out.println ( "" );
        }


        final Value<?> [] [] fails =
            new Value [] []
        {
            new Value [] { hello },
            new Value [] { hello, world, brave },
            new Value [] { madam },
            new Value [] { }
        };

        for ( Value<?> [] test : fails )
        {
            final long start_time = System.nanoTime ();

            final Value<Object> output = operation.invoke ( test );

            final long end_time = System.nanoTime ();
            final double duration_ns =
                (double) ( end_time - start_time )
                / 1000000.0D;

            System.out.println ( "Test ( "
                                 + valuesToString ( test )
                                 + " ):\n    "
                                 + output
                                 + "\n        "
                                 + "[ " + duration_ns + " ms ]" );

            if ( ( output instanceof One )
                 && ! Operation.class.equals ( output.expectedClass () ) )
            {
                throw new Exception ( "Test ( "
                                      + valuesToString ( test )
                                      + " ) should have output a CURRIED OPERATION"
                                      + " but output " + output );
            }
            else if ( ! ( output instanceof One )
                      && ! ( output instanceof Error ) )
            {
                throw new Exception ( "Test ( "
                                      + valuesToString ( test )
                                      + " ) should have output an ERROR"
                                      + " but output "
                                      + ClassName.of ( output.getClass () )
                                      + " '" + output + "'"
                                      + " with Value.expectedClass () = "
                                      + ClassName.of ( output.expectedClass () ) );
            }

            System.out.println ( "    SUCCESS" );
            System.out.println ( "" );
        }

        final long overall_end_time = System.nanoTime ();
        final double overall_duration_ns =
            (double) ( overall_end_time - overall_start_time )
            / 1000000.0D;

        System.out.println ( "Duration of whole test:"
                             + " [ " + overall_duration_ns + " ms ]" );
    }





    public static class Printer
        implements OperationBody<String>
    {
        private static final long serialVersionUID = MODULE.VERSION;
        @Override
        public Value<String> execute (
                InputsMachine<Object, Symbol, Type> inputs
                )
            throws ParametersMustNotBeNull.Violation,
                   ReturnNeverNull.Violation
        {
            final ValueBuilder<String> outputter =
                new ValueBuilder<String> ( String.class );
            for ( Value<Object> input : inputs.inputs () )
            {
                final String input_string = "" + input.orNull ();
                outputter.add ( input_string );
            }

            final Value<String> output = outputter.build ();

            return output;
        }
    }




    public static class Text
        implements Type
    {
        private static final long serialVersionUID = -1;
        public static final Text TYPE = new Text ();

        @Override
        public final String description ()
        {
            return "The type of Strings.";
        }

        @Override
        public final FilterState filter (
                                         Value<?> inspectable
                                         )
        {
            for ( Object text_object : inspectable )
            {
                if ( ! ( text_object instanceof String ) )
                {
                    return FilterState.DISCARDED;
                }
            }

            return FilterState.KEPT;
        }
        @Override
        public final ValueViolation violation (
                Object plaintiff,
                Value<?> inspectable
                )
        {
            return new ValueViolation ( this,
                                        "The value ("
                                        + inspectable
                                        + ") is not Text.", // description
                                        plaintiff,
                                        inspectable );
        }
        @Override
        public final ValueViolation violation (
                Object plaintiff,
                Value<?> inspectable,
                Throwable cause
                )
        {
            return new ValueViolation ( this,
                                        "The value ("
                                        + inspectable
                                        + ") is not Text.", // description
                                        plaintiff,
                                        inspectable,
                                        cause );
        }

        @Override
        public Graph<Symbol, Type> graph ()
            throws ReturnNeverNull.Violation
        {
            return null; // !!!
        }

        @Override
        public Machine<Value<Object>, Symbol> machine ()
            throws ReturnNeverNull.Violation
        {
            return null; // !!!
        }

        @Override
        public final String name ()
        {
            return "text";
        }

        @Override
        public final String toString ()
        {
            return this.name ();
        }
    }




    public static class Greeting
        implements Type
    {
        private static final long serialVersionUID = -1;
        public static final Greeting TYPE = new Greeting ();
        private final Map<String, FilterState> words;
        public Greeting ()
        {
            this.words = new HashMap<String, FilterState> ();
            this.words.put ( "hello", FilterState.KEPT );
            this.words.put ( "greetings", FilterState.KEPT );
            this.words.put ( "welcome", FilterState.KEPT );
        }

        @Override
        public final String description ()
        {
            return "A greeting, such as hello.";
        }

        @Override
        public final FilterState filter (
                                         Value<?> inspectable
                                         )
        {
            for ( Object greeting_object : inspectable )
            {
                if ( ! ( greeting_object instanceof String ) )
                {
                    return FilterState.DISCARDED;
                }

                final String greeting = (String) greeting_object;

                final String stripped =
                    greeting
                    .replaceAll ( "[^a-zA-Z0-9]+", "" )
                    .toLowerCase ();
                final FilterState filter_state =
                    this.words.get ( stripped );
                if ( filter_state == null
                     || ! filter_state.isKept () )
                {
                    return FilterState.DISCARDED;
                }
            }

            return FilterState.KEPT;
        }
        @Override
        public final ValueViolation violation (
                Object plaintiff,
                Value<?> inspectable
                )
        {
            return new ValueViolation ( this,
                                        "The value ("
                                        + inspectable
                                        + ") is not a Greeting.", // description
                                        plaintiff,
                                        inspectable );
        }
        @Override
        public final ValueViolation violation (
                Object plaintiff,
                Value<?> inspectable,
                Throwable cause
                )
        {
            return new ValueViolation ( this,
                                        "The value ("
                                        + inspectable
                                        + ") is not a Greeting.", // description
                                        plaintiff,
                                        inspectable,
                                        cause );
        }

        @Override
        public Graph<Symbol, Type> graph ()
            throws ReturnNeverNull.Violation
        {
            return null; // !!!
        }

        @Override
        public Machine<Value<Object>, Symbol> machine ()
            throws ReturnNeverNull.Violation
        {
            return null; // !!!
        }

        @Override
        public final String name ()
        {
            return "greeting";
        }

        @Override
        public final String toString ()
        {
            return this.name ();
        }
    }




    public static class Adjective
        implements Type
    {
        private static final long serialVersionUID = -1;
        public static final Adjective TYPE = new Adjective ();
        private final Map<String, FilterState> words;
        public Adjective ()
        {
            this.words = new HashMap<String, FilterState> ();
            this.words.put ( "wonderful", FilterState.KEPT );
            this.words.put ( "cruel", FilterState.KEPT );
            this.words.put ( "brave", FilterState.KEPT );
            this.words.put ( "new", FilterState.KEPT );
            this.words.put ( "beautiful", FilterState.KEPT );
        }

        @Override
        public final String description ()
        {
            return "An adjective, such as 'brave' or 'new'.";
        }

        @Override
        public final FilterState filter (
                                         Value<?> inspectable
                                         )
        {
            for ( Object adjective_object : inspectable )
            {
                if ( ! ( adjective_object instanceof String ) )
                {
                    return FilterState.DISCARDED;
                }

                final String adjective = (String) adjective_object;

                final String stripped =
                    adjective
                    .replaceAll ( "[^a-zA-Z0-9]+", "" )
                    .toLowerCase ();
                final FilterState filter_state =
                    this.words.get ( stripped );
                if ( filter_state == null
                     || ! filter_state.isKept () )
                {
                    return FilterState.DISCARDED;
                }
            }

            return FilterState.KEPT;
        }
        @Override
        public final ValueViolation violation (
                Object plaintiff,
                Value<?> inspectable
                )
        {
            return new ValueViolation ( this,
                                        "The value ("
                                        + inspectable
                                        + ") is not an Adjective.", // description
                                        plaintiff,
                                        inspectable );
        }
        @Override
        public final ValueViolation violation (
                Object plaintiff,
                Value<?> inspectable,
                Throwable cause
                )
        {
            return new ValueViolation ( this,
                                        "The value ("
                                        + inspectable
                                        + ") is not an Adjective.", // description
                                        plaintiff,
                                        inspectable,
                                        cause );
        }

        @Override
        public Graph<Symbol, Type> graph ()
            throws ReturnNeverNull.Violation
        {
            return null; // !!!
        }

        @Override
        public Machine<Value<Object>, Symbol> machine ()
            throws ReturnNeverNull.Violation
        {
            return null; // !!!
        }

        @Override
        public final String name ()
        {
            return "adjective";
        }

        @Override
        public final String toString ()
        {
            return this.name ();
        }
    }




    public static class Addressee
        implements Type
    {
        private static final long serialVersionUID = -1;
        public static final Addressee TYPE = new Addressee ();
        private final Map<String, FilterState> words;
        public Addressee ()
        {
            this.words = new HashMap<String, FilterState> ();
            this.words.put ( "world", FilterState.KEPT );
            this.words.put ( "you", FilterState.KEPT );
            this.words.put ( "sir", FilterState.KEPT );
            this.words.put ( "madam", FilterState.KEPT );
            this.words.put ( "dear", FilterState.KEPT );
        }

        @Override
        public final String description ()
        {
            return "An addressee, such as the world at large, or you, or her,"
                + " or Mr. Bungle, and so on.";
        }

        @Override
        public final FilterState filter (
                                         Value<?> inspectable
                                         )
        {
            for ( Object addressee_object : inspectable )
            {
                if ( ! ( addressee_object instanceof String ) )
                {
                    return FilterState.DISCARDED;
                }

                final String addressee = (String) addressee_object;

                final String stripped =
                    addressee
                    .replaceAll ( "[^a-zA-Z0-9]+", "" )
                    .toLowerCase ();
                final FilterState filter_state =
                    this.words.get ( stripped );
                if ( filter_state == null
                     || ! filter_state.isKept () )
                {
                    return FilterState.DISCARDED;
                }
            }

            return FilterState.KEPT;
        }
        @Override
        public final ValueViolation violation (
                Object plaintiff,
                Value<?> inspectable
                )
        {
            return new ValueViolation ( this,
                                        "The value ("
                                        + inspectable
                                        + ") is not an Addressee.", // description
                                        plaintiff,
                                        inspectable );
        }
        @Override
        public final ValueViolation violation (
                Object plaintiff,
                Value<?> inspectable,
                Throwable cause
                )
        {
            return new ValueViolation ( this,
                                        "The value ("
                                        + inspectable
                                        + ") is not an Addressee.", // description
                                        plaintiff,
                                        inspectable,
                                        cause );
        }

        @Override
        public Graph<Symbol, Type> graph ()
            throws ReturnNeverNull.Violation
        {
            return null; // !!!
        }

        @Override
        public Machine<Value<Object>, Symbol> machine ()
            throws ReturnNeverNull.Violation
        {
            return null; // !!!
        }

        @Override
        public final String name ()
        {
            return "addressee";
        }

        @Override
        public final String toString ()
        {
            return this.name ();
        }
    }




    public static String valuesToString ( Value<?> ... values )
    {
        final StringBuilder sbuf = new StringBuilder ();
        boolean is_first = true;
        for ( Value<?> value : values )
        {
            if ( is_first )
            {
                is_first = false;
            }
            else
            {
                sbuf.append ( ", " );
            }

            if ( value instanceof One )
            {
                sbuf.append ( "" + value.orNull () );
            }
            else if ( value instanceof Multiple )
            {
                sbuf.append ( "" + value );
            }
            else if ( value.hasValue () )
            {
                sbuf.append ( ClassName.of ( value.getClass () ) );
                sbuf.append ( "<" );
                sbuf.append ( ClassName.of ( value.expectedClass () ) );
                sbuf.append ( ">" );
            }
            else
            {
                sbuf.append ( "" + value );
            }
        }

        return sbuf.toString ();
    }
}
