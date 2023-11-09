package musaico.foundation.machine;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import musaico.foundation.contract.Contract;
import musaico.foundation.contract.UncheckedViolation;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.graph.Arc;
import musaico.foundation.graph.Graph;
import musaico.foundation.graph.StandardGraph;
import musaico.foundation.graph.SubGraph;

import musaico.foundation.value.Maybe;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueClass;

import musaico.foundation.value.abnormal.Abnormal;

import musaico.foundation.value.classes.StandardValueClass;

import musaico.foundation.value.finite.One;

public class TestMachine
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void main (
                             String [] args
                             )
        throws Exception
    {
        final Parameter before_parameters = new Parameter ( "start" );
        final Parameter greeting_parameter = new Parameter ( "greeting" );
        final Parameter adjectives_parameter =
            new Parameter ( "adjectives..." );
        final Parameter addressee_parameter = new Parameter ( "addressee" );
        final Graph<Parameter, Contract<Value<?>, ?>> operation1 =
            new StandardGraph<Parameter, Contract<Value<?>, ?>> (
                new StandardValueClass<Parameter> ( // node_value_class
                    Parameter.class,  // element_class
                    Parameter.NONE ), // none
                new StandardValueClass<Contract<Value<?>, ?>> ( // arc_value_class
                    Contract.class, // element_class
                    (Contract<Value<?>, ?>) // none
                        ( (Contract<?, UncheckedViolation>) Contract.NONE ) ),
                before_parameters,
                addressee_parameter,
                new Arc<Parameter, Contract<Value<?>, ?>> ( before_parameters, Greeting.CONTRACT, greeting_parameter ),
                new Arc<Parameter, Contract<Value<?>, ?>> ( greeting_parameter, Adjective.CONTRACT, adjectives_parameter ),
                new Arc<Parameter, Contract<Value<?>, ?>> ( greeting_parameter, Addressee.CONTRACT, addressee_parameter ),
                new Arc<Parameter, Contract<Value<?>, ?>> ( adjectives_parameter, Adjective.CONTRACT, adjectives_parameter ),
                new Arc<Parameter, Contract<Value<?>, ?>> ( adjectives_parameter, Addressee.CONTRACT, addressee_parameter ) );
        System.out.println ( "Operation # 1:" );
        System.out.println ( operation1.toStringDetails () );

        final ValueClass<String> string_value_class =
            new StandardValueClass<String> (
                String.class, // element_class
                "" );         // none

        // Greetings:
        final One<String> hello =
            new One<String> ( string_value_class, "hello" );
        final One<String> greetings =
            new One<String> ( string_value_class, "greetings" );
        final One<String> welcome =
            new One<String> ( string_value_class, "welcome" );

        // Adjectives:
        final One<String> wonderful =
            new One<String> ( string_value_class, "wonderful" );
        final One<String> cruel =
            new One<String> ( string_value_class, "cruel" );
        final One<String> brave =
            new One<String> ( string_value_class, "brave" );
        final One<String> _new =
            new One<String> ( string_value_class, "new" );
        final One<String> beautiful =
            new One<String> ( string_value_class, "beautiful" );

        // Addressees:
        final One<String> world =
            new One<String> ( string_value_class, "world" );
        final One<String> you =
            new One<String> ( string_value_class, "you" );
        final One<String> sir =
            new One<String> ( string_value_class, "sir" );
        final One<String> madam =
            new One<String> ( string_value_class, "madam" );
        final One<String> dear =
            new One<String> ( string_value_class, "dear" );

        final Parameter entry1 = operation1.entry ();
        final Parameter exit1 = operation1.exit ();
        final InputsMachine<String, Parameter> machine1 =
            new InputsMachine<String, Parameter> (
                "test_operation1",
                operation1,
                string_value_class,
                entry1 );


        // Explore the chain of inputs from a multi-parameter transition:
        final Value<String> [] inputs_chain =
            new Value [] { hello, brave, _new, world };
        System.out.println ( "Transitions for test ( "
                             + Arrays.toString ( inputs_chain )
                             + " ):" );
        Value<Parameter> last_state = null;
        int input_num = inputs_chain.length - 1;
        InputsMachine<String, Parameter> copy =
            machine1.state ( entry1 );
        Transitions<String, Parameter, Contract<Value<?>, ?>> transitions;
        transitions = copy.transition ( inputs_chain );
        final Maybe<Parameter> one_state = transitions.execute ();
        while ( one_state != last_state )
        {
            if ( one_state instanceof Abnormal )
            {
                throw new IllegalArgumentException ( "Could not transition inputs machine with " + Arrays.toString ( inputs_chain ) + ", expected " + inputs_chain.length + " input(s) to be processed, in machine:\n" + copy + "\nInputs processed so far: " + Arrays.toString ( copy.inputs () ) + "\nResult of transition attempt: " + one_state );
            }

            System.out.print ( "    " + ( input_num + 1 ) + ". " );

            last_state = one_state;
            one_state = last_state.cause ();

            System.out.print ( one_state.orNull () );
            if ( input_num >= 0 )
            {
                if ( copy.inputs ().length <= input_num )
                {
                    System.err.println ( "" );
                    throw new IllegalStateException ( "Transition did not work: only " + copy.inputs ().length + " inputs processed, expected at least " + ( input_num + 1 ) + ", in machine:\n" + copy + "\nInputs processed so far: " + Arrays.toString ( copy.inputs () ) );
                }

                System.out.print ( " --{" );
                System.out.print ( copy.inputs () [ input_num ].orNull () );
                System.out.print ( "}--> " );
                System.out.print ( last_state.orNull () );
            }

            System.out.println ( "" );

            input_num --;
        }
        System.out.println ( "" );
        System.out.println ( "" );



        final Value<String> [] [] passes =
            new Value [] []
        {
            new Value [] { hello, world },
            new Value [] { hello, brave, _new, world },
            new Value [] { hello, brave, _new, beautiful, wonderful, cruel, world },
            new Value [] { greetings, madam },
            new Value [] { welcome, you }
        };

        for ( Value<String> [] test : passes )
        {
            copy = machine1.state ( entry1 );
            transitions = copy.transition ( test );
            final Maybe<Parameter> one_state = transitions.execute ();
            System.out.println ( "Test ( "
                                 + Arrays.toString ( test )
                                 + " ): "
                                 + end_state );

            if ( ! exit1.equals ( end_state.orNull () ) )
            {
                throw new Exception ( "Test ( "
                                      + Arrays.toString ( test )
                                      + " ) SHOULD have led to exit state"
                                      + " but led to " + end_state );
            }
            else if ( ! Arrays.equals ( test, copy.inputs () ) )
            {
                throw new Exception ( "Test ( "
                                      + Arrays.toString ( test )
                                      + " ) SHOULD have recorded the inputs"
                                      + " but only recorded: "
                                      + Arrays.toString ( copy.inputs () ) );
            }

            System.out.println ( "    SUCCESS" );
            System.out.println ( "" );
        }


        final Value<String> [] [] fails =
            new Value [] []
        {
            new Value [] { hello },
            new Value [] { hello, world, brave },
            new Value [] { madam },
            new Value [] { }
        };

        for ( Value<String> [] test : fails )
        {
            copy = machine1.state ( entry1 );
            transitions = copy.transition ( test );
            final Maybe<Parameter> one_state = transitions.execute ();
            System.out.println ( "Test ( "
                                 + Arrays.toString ( test )
                                 + " ): "
                                 + end_state );

            if ( exit1.equals ( end_state.orNull () ) )
            {
                throw new Exception ( "Test ( "
                                      + Arrays.toString ( test )
                                      + " ) should NOT have led to exit state" );
            }
            else if ( end_state.orNull () == null
                      && copy.inputs ().length > 0 )
            {
                throw new Exception ( "Test ( "
                                      + Arrays.toString ( test )
                                      + " ) should NOT have recorded any inputs" );
            }
            else if ( end_state.orNull () != null
                      && ! Arrays.equals ( test, copy.inputs () ) )
            {
                throw new Exception ( "Test ( "
                                      + Arrays.toString ( test )
                                      + " ) SHOULD have recorded the inputs, even though it did not proceed all the way to the exit state"
                                      + " but only recorded: "
                                      + Arrays.toString ( copy.inputs () ) );
            }

            System.out.println ( "    SUCCESS" );
            System.out.println ( "" );
        }




        System.out.println ( "" );
        System.out.println ( "" );
        System.out.println ( "Now test SubGraph"
                             + " and pushing / popping machine:" );
        System.out.println ( "" );

        final Graph<Parameter, Contract<Value<?>, ?>> sub_operation2 =
            new StandardGraph<Parameter, Contract<Value<?>, ?>> (
                new StandardValueClass<Parameter> ( // node_value_class
                    Parameter.class,  // element_class
                    Parameter.NONE ), // none
                new StandardValueClass<Contract<Value<?>, ?>> ( // arc_value_class
                    Contract.class, // element_class
                    (Contract<Value<?>, ?>) // none
                        ( (Contract<?, UncheckedViolation>) Contract.NONE ) ),
                adjectives_parameter, // entry
                adjectives_parameter, // exit
                new Arc<Parameter, Contract<Value<?>, ?>> ( adjectives_parameter, Adjective.CONTRACT, adjectives_parameter ) );


        final Graph<Parameter, Contract<Value<?>, ?>> operation2 =
            new StandardGraph<Parameter, Contract<Value<?>, ?>> (
                new StandardValueClass<Parameter> ( // node_value_class
                    Parameter.class,  // element_class
                    Parameter.NONE ), // none
                new StandardValueClass<Contract<Value<?>, ?>> ( // arc_value_class
                    Contract.class, // element_class
                    (Contract<Value<?>, ?>) // none
                        ( (Contract<?, UncheckedViolation>) Contract.NONE ) ),
                before_parameters, // entry
                addressee_parameter, // exit
                new Arc<Parameter, Contract<Value<?>, ?>> ( before_parameters, Greeting.CONTRACT, greeting_parameter ),
                new Arc<Parameter, Contract<Value<?>, ?>> ( greeting_parameter, Adjective.CONTRACT, adjectives_parameter ),
                new Arc<Parameter, Contract<Value<?>, ?>> ( greeting_parameter, Addressee.CONTRACT, addressee_parameter ),
                new SubGraph<Parameter, Contract<Value<?>, ?>> ( sub_operation2, Adjective.CONTRACT ),
                new Arc<Parameter, Contract<Value<?>, ?>> ( adjectives_parameter, Addressee.CONTRACT, addressee_parameter ) );

        System.out.println ( "Operation # 2:" );
        System.out.println ( operation2.toStringDetails () );

        final Parameter entry2 = operation2.entry ();
        final Parameter exit2 = operation2.exit ();
        final InputsMachine<String, Parameter> machine2 =
            new InputsMachine<String, Parameter> (
                "test_operation2",
                operation2,
                string_value_class,
                entry2 );

        for ( Value<String> [] test : passes )
        {
            copy = machine2.state ( entry2 );
            transitions = copy.transition ( test );
            final Maybe<Parameter> one_state = transitions.execute ();
            System.out.println ( "Test ( "
                                 + Arrays.toString ( test )
                                 + " ): "
                                 + end_state );

            if ( ! exit2.equals ( end_state.orNull () ) )
            {
                throw new Exception ( "Test ( "
                                      + Arrays.toString ( test )
                                      + " ) SHOULD have led to exit state"
                                      + " but led to " + end_state );
            }
            else if ( ! Arrays.equals ( test, copy.inputs () ) )
            {
                throw new Exception ( "Test ( "
                                      + Arrays.toString ( test )
                                      + " ) SHOULD have recorded the inputs"
                                      + " but only recorded: "
                                      + Arrays.toString ( copy.inputs () ) );
            }

            System.out.println ( "    SUCCESS" );
            System.out.println ( "" );
        }

        final Value<String> [] start_inputs =
            new Value [] { hello, brave };
        final Value<String> [] push_inputs =
            new Value [] { _new };
        final Value<String> [] sub_inputs =
            new Value [] { beautiful, wonderful, cruel };
        final Value<String> [] end_inputs =
            new Value [] { world };

        final LinkedHashMap<Value<String> [], Graph<Parameter, Contract<Value<?>, ?>>> expected_top_graphs =
            new LinkedHashMap<Value<String> [], Graph<Parameter, Contract<Value<?>, ?>>> ();
        expected_top_graphs.put ( start_inputs, operation2 );
        expected_top_graphs.put ( push_inputs, sub_operation2 );
        expected_top_graphs.put ( sub_inputs, sub_operation2 );
        expected_top_graphs.put ( end_inputs, operation2 );

        copy = machine2.state ( entry2 );
        Graph<Parameter, Contract<Value<?>, ?>> expected_top_graph =
            operation2;
        int step = 0;
        for ( Value<String> [] inputs : expected_top_graphs.keySet () )
        {
            if ( ! copy.graphCurrent ().equals ( expected_top_graph ) )
            {
                throw new IllegalStateException ( "Incorrect graph at step # " + step + ": before input " + Arrays.toString ( inputs ) + ", the machine should be inside graph " + expected_top_graph.toStringDetails () + " but it is currently inside " + copy.graphCurrent ().toStringDetails () );
            }

            transitions = copy.transition ( inputs );
            final Maybe<Parameter> one_state = transitions.execute ();
            System.out.println ( "Stage ( "
                                 + Arrays.toString ( inputs )
                                 + " ): "
                                 + end_state
                                 + " stack = "
                                 + copy.graphStackDepth ()
                                 + " graphs" );

            expected_top_graph = expected_top_graphs.get ( inputs );
            if ( ! copy.graphCurrent ().equals ( expected_top_graph ) )
            {
                throw new IllegalStateException ( "Incorrect graph at step # " + step + ": after input " + Arrays.toString ( inputs ) + ", the machine should be inside graph " + expected_top_graph.toStringDetails () + " but it is currently inside " + copy.graphCurrent ().toStringDetails () );
            }

            step ++;
        }

        System.out.println ( "    SubGraph machine:" );
        System.out.println ( "    SUCCESS" );
        System.out.println ( "" );
    }




    public static class Parameter
    {
        public static final Parameter NONE =
            new Parameter ( "" );


        private final String name;
        public Parameter (
                          String name
                          )
        {
            this.name = name;
        }
        public final String toString ()
        {
            return this.name;
        }
    }


    public static class Greeting
        implements Contract<Value<?>, UncheckedViolation>
    {
        private static final long serialVersionUID = -1;
        public static final Greeting CONTRACT = new Greeting ();
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
            return "Each String must be a greeting, one of: "
                + this.words
                + ".";
        }

        @Override
        public final FilterState filter (
                                         Value<?> evidence
                                         )
        {
            for ( Object greeting_object : evidence )
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
        public final UncheckedViolation violation (
                Object plaintiff,
                Value<?> evidence
                )
        {
            return new UncheckedViolation ( this,
                                            "Not a greeting.", // description
                                            plaintiff,
                                            evidence );
        }
        @Override
        public final UncheckedViolation violation (
                Object plaintiff,
                Value<?> evidence,
                Throwable cause
                )
        {
            return new UncheckedViolation ( this,
                                            "Not a greeting.", // description
                                            plaintiff,
                                            evidence,
                                            cause );
        }

        @Override
        public final String toString ()
        {
            return "greeting input";
        }
    }




    public static class Adjective
        implements Contract<Value<?>, UncheckedViolation>
    {
        private static final long serialVersionUID = -1;
        public static final Adjective CONTRACT = new Adjective ();
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
            return "Each String must be an adjective, one of: "
                + this.words
                + ".";
        }

        @Override
        public final FilterState filter (
                                         Value<?> evidence
                                         )
        {
            for ( Object adjective_object : evidence )
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
        public final UncheckedViolation violation (
                Object plaintiff,
                Value<?> evidence
                )
        {
            return new UncheckedViolation ( this,
                                            "Not an adjective.", // description
                                            plaintiff,
                                            evidence );
        }
        @Override
        public final UncheckedViolation violation (
                Object plaintiff,
                Value<?> evidence,
                Throwable cause
                )
        {
            return new UncheckedViolation ( this,
                                            "Not an adjective.", // description
                                            plaintiff,
                                            evidence,
                                            cause );
        }

        @Override
        public final String toString ()
        {
            return "adjective input";
        }
    }




    public static class Addressee
        implements Contract<Value<?>, UncheckedViolation>
    {
        private static final long serialVersionUID = -1;
        public static final Addressee CONTRACT = new Addressee ();
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
            return "Each String must be someone or something you can address,"
                + " one of: "
                + this.words
                + ".";
        }

        @Override
        public final FilterState filter (
                                         Value<?> evidence
                                         )
        {
            for ( Object addressee_object : evidence )
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
        public final UncheckedViolation violation (
                Object plaintiff,
                Value<?> evidence
                )
        {
            return new UncheckedViolation ( this,
                                            "Not an addressee.", // description
                                            plaintiff,
                                            evidence );
        }
        @Override
        public final UncheckedViolation violation (
                Object plaintiff,
                Value<?> evidence,
                Throwable cause
                )
        {
            return new UncheckedViolation ( this,
                                            "Not an addressee.", // description
                                            plaintiff,
                                            evidence,
                                            cause );
        }

        @Override
        public final String toString ()
        {
            return "addressee input";
        }
    }
}
