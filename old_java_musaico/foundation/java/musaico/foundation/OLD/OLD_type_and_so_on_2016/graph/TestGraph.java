package musaico.foundation.graph;

import java.util.HashMap;
import java.util.Map;

import musaico.foundation.contract.Contract;
import musaico.foundation.contract.CheckedViolation;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.value.Value;
import musaico.foundation.value.ValueClass;

import musaico.foundation.value.classes.StandardValueClass;

public class TestGraph
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    @SuppressWarnings("unchecked")
    public static void main (
                             String [] args
                             )
    {
        final Graph<String, Integer> one_node = // No arcs.
            new StandardGraph<String, Integer> (
                new StandardValueClass<String> ( // node_value_class
                    String.class, // element_class
                    "" ),         // none
                new StandardValueClass<Integer> ( // arc_value_class
                    Integer.class, // element_class
                    0 ),           // none
                "singularity", // entry
                "singularity" ); // exit
        System.out.println ( "Singularity graph:" );
        System.out.println ( one_node.toStringDetails () );

        final Graph<String, Integer> cities1 =
            new StandardGraph<String, Integer> (
                new StandardValueClass<String> ( // node_value_class
                    String.class, // element_class
                    "" ),         // none
                new StandardValueClass<Integer> ( // arc_value_class
                    Integer.class, // element_class
                    0 ),           // none
                "Halifax", // entry
                "Albufeira", // exit
                new Arc<String, Integer> ( "Halifax", 2000, "Albufeira" ) );
        System.out.println ( "Cities graph # 1:" );
        System.out.println ( cities1.toStringDetails () );

        final Graph<String, Integer> cities2 =
            new StandardGraph<String, Integer> (
                new StandardValueClass<String> ( // node_value_class
                    String.class, // element_class
                    "" ),         // none
                new StandardValueClass<Integer> ( // arc_value_class
                    Integer.class, // element_class
                    0 ),           // none
                "Halifax", // entry
                "Albufeira", // exit
                new Arc<String, Integer> ( "Halifax", 2000, "Albufeira" ) );
        System.out.println ( "Cities graph # 2:" );
        System.out.println ( cities2.toStringDetails () );

        final Graph<String, Integer> cities3 =
            new StandardGraph<String, Integer> (
                new StandardValueClass<String> ( // node_value_class
                    String.class, // element_class
                    "" ),         // none
                new StandardValueClass<Integer> ( // arc_value_class
                    Integer.class, // element_class
                    0 ),           // none
                "Halifax", // entry
                "Albufeira", // exit
                new Arc<String, Integer> ( "Halifax", 1000, "Toronto" ),
                new Arc<String, Integer> ( "Toronto", 2000, "Reykjavik" ),
                new Arc<String, Integer> ( "Reykjavik", 2000, "Toronto" ),
                new Arc<String, Integer> ( "Reykjavik", 1000, "Lisboa" ),
                new Arc<String, Integer> ( "Lisboa", 1000, "Reykjavik" ),
                new Arc<String, Integer> ( "Lisboa", 500, "Albufeira" ) );
        System.out.println ( "Cities graph # 3:" );
        System.out.println ( cities3.toStringDetails () );

        // Circle graph:
        final Graph<String, Integer> cities4 =
            new StandardGraph<String, Integer> (
                new StandardValueClass<String> ( // node_value_class
                    String.class, // element_class
                    "" ),         // none
                new StandardValueClass<Integer> ( // arc_value_class
                    Integer.class, // element_class
                    0 ),           // none
                "Halifax", // entry
                "Halifax", // exit
                new Arc<String, Integer> ( "Halifax", 2000, "Albufeira" ),
                new Arc<String, Integer> ( "Albufeira", 2000, "Halifax" ) );
        System.out.println ( "Cities graph # 4:" );
        System.out.println ( cities4.toStringDetails () );

        final Graph<String, Integer> vacation =
            new StandardGraph<String, Integer> (
                new StandardValueClass<String> ( // node_value_class
                    String.class, // element_class
                    "" ),         // none
                new StandardValueClass<Integer> ( // arc_value_class
                    Integer.class, // element_class
                    0 ),           // none
                "Home", // entry
                "Home", // exit
                new Arc<String, Integer> ( "Home", 25, "Halifax" ),
                new SubGraph<String, Integer> ( cities3, 4500 ),
                new Arc<String, Integer> ( "Albufeira", 4525, "Home" ) );
        System.out.println ( "Vacation graph:" );
        System.out.println ( vacation.toStringDetails () );



        final Graph<Integer, Contract<?, ?>> operation1 =
            new StandardGraph<Integer, Contract<?, ?>> (
                new StandardValueClass<Integer> (        // node_value_class
                    Integer.class, // element_class
                    0 ),           // none
                new StandardValueClass<Contract<?, ?>> ( // arc_value_class
                    Contract.class, // element_class
                    Contract.NONE ),
                0, // entry
                2, // exit
                new Arc<Integer, Contract<?, ?>> ( 0, Greeting.CONTRACT, 1 ),
                new Arc<Integer, Contract<?, ?>> ( 1, Adjective.CONTRACT, 1 ),
                new Arc<Integer, Contract<?, ?>> ( 1, Addressee.CONTRACT, 2 ) );
        System.out.println ( "Operation # 1:" );
        System.out.println ( operation1.toStringDetails () );
    }




    public static class Greeting
        implements Contract<Value<String>, CheckedViolation>
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
                                         Value<String> evidence
                                         )
        {
            for ( String greeting : evidence )
            {
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
        public final CheckedViolation violation (
                Object plaintiff,
                Value<String> evidence
                )
        {
            return new CheckedViolation ( this,
                                          "Not a greeting.", // description
                                          plaintiff,
                                          evidence );
        }
        @Override
        public final CheckedViolation violation (
                Object plaintiff,
                Value<String> evidence,
                Throwable cause
                )
        {
            return new CheckedViolation ( this,
                                          "Not a greeting.", // description
                                          plaintiff,
                                          evidence,
                                          cause );
        }

        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }
    }




    public static class Adjective
        implements Contract<Value<String>, CheckedViolation>
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
                                         Value<String> evidence
                                         )
        {
            for ( String greeting : evidence )
            {
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
        public final CheckedViolation violation (
                Object plaintiff,
                Value<String> evidence
                )
        {
            return new CheckedViolation ( this,
                                          "Not an adjective.", // description
                                          plaintiff,
                                          evidence );
        }
        @Override
        public final CheckedViolation violation (
                Object plaintiff,
                Value<String> evidence,
                Throwable cause
                )
        {
            return new CheckedViolation ( this,
                                          "Not an adjective.", // description
                                          plaintiff,
                                          evidence,
                                          cause );
        }

        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }
    }




    public static class Addressee
        implements Contract<Value<String>, CheckedViolation>
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
                                         Value<String> evidence
                                         )
        {
            for ( String greeting : evidence )
            {
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
        public final CheckedViolation violation (
                Object plaintiff,
                Value<String> evidence
                )
        {
            return new CheckedViolation ( this,
                                          "Not an addressee.", // description
                                          plaintiff,
                                          evidence );
        }
        @Override
        public final CheckedViolation violation (
                Object plaintiff,
                Value<String> evidence,
                Throwable cause
                )
        {
            return new CheckedViolation ( this,
                                          "Not an addressee.", // description
                                          plaintiff,
                                          evidence,
                                          cause );
        }

        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }
    }
}
