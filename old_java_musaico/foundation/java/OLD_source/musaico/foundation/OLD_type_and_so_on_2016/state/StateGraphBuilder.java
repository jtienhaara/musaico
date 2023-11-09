package musaico.foundation.state;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import java.util.regex.Pattern;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Contract;
import musaico.foundation.contract.UncheckedViolation;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;
import musaico.foundation.domains.LeftAndRight;

import musaico.foundation.domains.string.StringPattern;

import musaico.foundation.filter.Domain;
import musaico.foundation.filter.FilterState;

import musaico.foundation.graph.Arc;
import musaico.foundation.graph.ArcMustBeInGraph;
import musaico.foundation.graph.CountableGraph;
import musaico.foundation.graph.Graph;
import musaico.foundation.graph.ImmutableGraph;
import musaico.foundation.graph.MutableGraph;
import musaico.foundation.graph.NodeMustBeInGraph;
import musaico.foundation.graph.StandardGraph;
import musaico.foundation.graph.SubGraph;

import musaico.foundation.machine.Machine;

import musaico.foundation.value.Countable;
import musaico.foundation.value.Maybe;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueClass;
import musaico.foundation.value.ZeroOrOne;

import musaico.foundation.value.builder.ValueBuilder;

import musaico.foundation.value.classes.StandardValueClass;

import musaico.foundation.value.contracts.ElementsMustBelongToDomain;

import musaico.foundation.value.finite.No;
import musaico.foundation.value.finite.One;


/**
 * <p>
 * Builds up a Graph of Value states and Transitions.
 * </p>
 *
 * <p>
 * While it is building up an immutable Graph, the StateGraphBuilder
 * itself acts as a mutable Graph.
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
public class StateGraphBuilder
    implements CountableGraph<Value<?>, Transition>, MutableGraph<Value<?>, Transition>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;




    /**
     * <p>
     * A StateGraphBuilder can only work <code> on () <code> building
     * one Transition at a time.  Two calls to <code> on () </code>
     * without first specifying a destination <code> to () </code>
     * state will induce a violation of this obligation.
     * </p>
     */
    public static class MustBuildOneTransitionAtATime
        implements Contract<LeftAndRight<Transition, Transition>, StateGraphBuilder.MustBuildOneTransitionAtATime.Violation>, Serializable
    {
        private static final long serialVersionUID =
            StateGraphBuilder.serialVersionUID;

        /** The StateGraphBuilder.MustBuildOneTransitionAtATime contract. */
        public static final StateGraphBuilder.MustBuildOneTransitionAtATime CONTRACT =
            new StateGraphBuilder.MustBuildOneTransitionAtATime ();

        /** Creates a new StateGraphBuilder.MustBuildOneTransitionAtATime.
         *  Use StateGraphBuilder.MustBuildOneTransitionAtATime.CONTRACT
         *  instead. */
        protected MustBuildOneTransitionAtATime ()
        {
        }


        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public String description ()
        {
            return "A StateGraphBuilder can only build one Transition"
                + " at a time.  The client must not call on ( ... )"
                + " to begin building a new Transition without finishing"
                + " the first Transition's to ( ... ) destination"
                + " state.";
        }

        /**
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals (
                               Object obj
                               )
        {
            if ( obj == null )
            {
                return false;
            }
            else if ( obj.getClass () != this.getClass () )
            {
                return false;
            }

            return true;
        }

        /**
         * @see musaico.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                                   LeftAndRight<Transition, Transition> transitions
                                   )
        {
            if ( transitions == null )
            {
                return FilterState.DISCARDED;
            }
            else if ( transitions.left () == null
                      || transitions.right () == null )
            {
                return FilterState.KEPT;
            }
            else
            {
                return FilterState.DISCARDED;
            }
        }

        /**
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode ()
        {
            return ClassName.of ( this.getClass () ).hashCode ();
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        /**
         * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
         */
        @Override
        public StateGraphBuilder.MustBuildOneTransitionAtATime.Violation violation (
                Object plaintiff,
                LeftAndRight<Transition, Transition> evidence
                )
        {
            return new StateGraphBuilder.MustBuildOneTransitionAtATime.Violation (
                this,
                plaintiff,
                evidence );
        }

        /**
         * @see musaico.foundation.contract.Contract#violation(java.lang.Object, musaico.foundation.value.Value, java.lang.Throwable)
         */
        @Override
        public StateGraphBuilder.MustBuildOneTransitionAtATime.Violation violation (
                Object plaintiff,
                LeftAndRight<Transition, Transition> evidence,
                Throwable cause
                )
        {
            final StateGraphBuilder.MustBuildOneTransitionAtATime.Violation violation =
                this.violation ( plaintiff,
                                 evidence );

            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
        }

        /**
         * <p>
         * A violation of the StateGraphBuilder.MustBuildOneTransitionAtATime
         * contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
            implements Serializable
        {
            private static final long serialVersionUID =
                StateGraphBuilder.MustBuildOneTransitionAtATime.serialVersionUID;

            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              LeftAndRight<Transition, Transition> evidence
                              )
                throws ParametersMustNotBeNull.Violation
            {
                this ( contract,
                       plaintiff,
                       evidence,
                       null ); // cause
            }

            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              LeftAndRight<Transition, Transition> evidence,
                              Throwable cause
                              )
                throws ParametersMustNotBeNull.Violation
            {
                super ( contract,
                        "A Transition is already under construction: "
                        + ( evidence == null
                            ? null
                            : evidence.left () )
                        + "  Cannot begin building Transition "
                        + ( evidence == null
                            ? null
                            : evidence.right () )
                        + ".", // description
                        plaintiff,
                        evidence,
                        cause );
            }
        }
    }




    /**
     * <p>
     * A StateGraphBuilder must finish working <code> on () <code> building
     * a Transition before it can start looking at a different node.
     * Calling <code> on () </code> followed by <code> from () </code>,
     * will induce a violation of this obligation.
     * </p>
     */
    public static class MustFinishBuildingTransition
        implements Contract<List<Transition>, StateGraphBuilder.MustFinishBuildingTransition.Violation>, Serializable
    {
        private static final long serialVersionUID =
            StateGraphBuilder.serialVersionUID;

        /** The StateGraphBuilder.MustFinishBuildingTransition contract. */
        public static final StateGraphBuilder.MustFinishBuildingTransition CONTRACT =
            new StateGraphBuilder.MustFinishBuildingTransition ();

        /** Creates a new StateGraphBuilder.MustFinishBuildingTransition.
         *  Use StateGraphBuilder.MustFinishBuildingTransition.CONTRACT
         *  instead. */
        protected MustFinishBuildingTransition ()
        {
        }


        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public String description ()
        {
            return "A StateGraphBuilder must finish building a Transition"
                + " once it has started.  The client must not call on ( ... )"
                + " then call from ( ... ) without first finishing"
                + " the Transition's to ( ... ) destination state.";
        }

        /**
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals (
                               Object obj
                               )
        {
            if ( obj == null )
            {
                return false;
            }
            else if ( obj.getClass () != this.getClass () )
            {
                return false;
            }

            return true;
        }

        /**
         * @see musaico.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                                   List<Transition> transitions
                                   )
        {
            if ( transitions == null )
            {
                // No transition being built right now.
                return FilterState.KEPT;
            }
            else if ( transitions.size () == 0 )
            {
                // No transition being built right now.
                return FilterState.KEPT;
            }
            else
            {
                // A non-null transition is being built.
                return FilterState.DISCARDED;
            }
        }

        /**
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode ()
        {
            return ClassName.of ( this.getClass () ).hashCode ();
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        /**
         * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
         */
        @Override
        public StateGraphBuilder.MustFinishBuildingTransition.Violation violation (
                Object plaintiff,
                List<Transition> evidence
                )
        {
            return new StateGraphBuilder.MustFinishBuildingTransition.Violation (
                this,
                plaintiff,
                evidence );
        }

        /**
         * @see musaico.foundation.contract.Contract#violation(java.lang.Object, musaico.foundation.value.Value, java.lang.Throwable)
         */
        @Override
        public StateGraphBuilder.MustFinishBuildingTransition.Violation violation (
                Object plaintiff,
                List<Transition> evidence,
                Throwable cause
                )
        {
            final StateGraphBuilder.MustFinishBuildingTransition.Violation violation =
                this.violation ( plaintiff,
                                 evidence );

            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
        }

        /**
         * <p>
         * A violation of the StateGraphBuilder.MustFinishBuildingTransition
         * contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
            implements Serializable
        {
            private static final long serialVersionUID =
                StateGraphBuilder.MustFinishBuildingTransition.serialVersionUID;

            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              List<Transition> evidence
                              )
                throws ParametersMustNotBeNull.Violation
            {
                this ( contract,
                       plaintiff,
                       evidence,
                       null ); // cause
            }

            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              List<Transition> evidence,
                              Throwable cause
                              )
                throws ParametersMustNotBeNull.Violation
            {
                super ( contract,
                        "A Transition is not finished: "
                        + evidence
                        + ".", // description
                        plaintiff,
                        evidence,
                        cause );
            }
        }
    }




    // Checks constructor and static method obligations.
    private static final Advocate classContracts =
        new Advocate ( StateGraphBuilder.class );


    // Checks method obligations and guarantees.
    private final Advocate contracts;

    // Lock critical sections on this token:
    private final Serializable lock = new String ();

    // The starting point for all Machines traversing the graph-being-built.
    private final Value<?> entry;

    // The finishing point for all Machines traversing the graph-being-built.
    private final Value<?> exit;

    // MUTABLE:
    // The states that have been added to the graph-being-built so far,
    // including the entry and exit states.
    private final LinkedHashSet<Value<?>> states =
        new LinkedHashSet<Value<?>> ();

    // MUTABLE:
    // The Transition Arcs that have been added to the graph-being-built
    // so far.
    private final List<Arc<Value<?>, Transition>> transitionArcs =
        new ArrayList<Arc<Value<?>, Transition>> ();

    // Any states which have not been built up to have paths to the
    // exit state get "automatic arcs" until building is complete.
    private List<Arc<Value<?>, Transition>> automaticExitArcs =
        new ArrayList<Arc<Value<?>, Transition>> ();

    // MUTABLE:
    // The SubGraphs that have been added to the graph-being-built so far.
    // This is a subset of the transitionArcs.
    private final LinkedHashSet<SubGraph<Value<?>, Transition>> subGraphs =
        new LinkedHashSet<SubGraph<Value<?>, Transition>> ();

    // MUTABLE:
    // The current state being built, if any.
    private Value<?> state = null;

    // MUTABLE:
    // The Transitions for the next Arc being built up.  Each Transition
    // chooses an input, or uses up an input if it meets input requirements,
    // or transitions automatically, and so on.
    private List<Transition> transitions = new ArrayList<Transition> ();


    /**
     * <p>
     * Creates a new StateGraphBuilder.
     * </p>
     *
     * @param name The name of the state-graph-being-built, which will also
     *             be used as the value for the entry node in the graph.
     *             Must not be null.
     */
    public StateGraphBuilder (
                              String name
                              )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               name );

        this.entry = new Entry ( name ); // One name entry.
        this.exit = new Exit ( name ); // One name exit.

        this.states.add ( this.entry );
        this.states.add ( this.exit );

        this.automaticExitArcs.add (
            new Arc<Value<?>, Transition> ( this.entry,
                                            Transition.AUTOMATIC,
                                            this.exit ) );

        this.contracts = new Advocate ( this );
    }


    // Protected helper methods to provide a teensy bit of code
    // reuse in an insanely over-ripe pumpkin of a class.
    // =============================================================

    /**
     * @param elements Zero or more elements to turn into a Value.
     *                 Must not be null.  Must not contain any null elements.
     */
    protected final Value<Number> createValue (
                                               Number ... elements
                                               )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        return this.createValue (
            new StandardValueClass<Number> ( // value_class
                Number.class, // element_class
                0 ),          // none
            elements );
    }

    /**
     * @param elements Zero or more elements to turn into a Value.
     *                 Must not be null.  Must not contain any null elements.
     */
    protected final Value<String> createValue (
                                               String ... elements
                                               )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        return this.createValue (
            new StandardValueClass<String> ( // value_class
                String.class,  // element_class
                "" ),          // none
            elements );
    }

    /**
     * @param value_class The ValueClass of each of the specified elements,
     *                    such as a ValueClass&lt;String&gt;,
     *                    or a ValueClass&lt;Integer&gt;, and so on.
     *                    Must not be null.
     *
     * @param elements Zero or more elements to turn into a Value.
     *                 Must not be null.  Must not contain any null elements.
     */
    @SuppressWarnings("unchecked") // Possible heap pollution VALUE ... vararg.
    protected final <VALUE extends Object>
        Value<VALUE> createValue (
                                  ValueClass<VALUE> value_class,
                                  VALUE ... elements
                                  )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               value_class, elements );
        this.contracts.check ( Parameter2.MustContainNoNulls.CONTRACT,
                               elements );

        final ValueBuilder<VALUE> builder =
            new ValueBuilder<VALUE> ( value_class, elements );
        final Value<VALUE> value = builder.build ();
        return value;
    }

    // =============================================================


    /**
     * @see musaico.foundation.tape.StateGraphBuilder#add(musaico.foundation.graph.Arc)
     *
     * @param from The state from which the new Arc will lead.
     *             Must not be null.
     *
     * @param transition The Transition to turn into an Arc and
     *                   add to this builder.  Must not be null.
     *
     * @param from The state to which the new Arc will lead.
     *             Must not be null.
     */
    public StateGraphBuilder add (
                                  Value<?> from,
                                  Transition transition,
                                  Value<?> to
                                  )
        throws ParametersMustNotBeNull.Violation,
               StateGraphBuilder.MustFinishBuildingTransition.Violation,
               ReturnNeverNull.Violation
    {
        return this.add (
                         new Arc<Value<?>, Transition> (
                                                        from,
                                                        transition,
                                                        to ) );
    }


    /**
     * <p>
     * Adds the specified Transition Arc, and its from and to states,
     * to this builder.
     * </p>
     *
     * @param transition_arc The Arc to add to this builder.
     *                       Must not be null.
     *
     * @return This StateGraphBuilder.  Never null.
     */
    public StateGraphBuilder add (
                                  Arc<Value<?>, Transition> transition_arc
                                  )
        throws ParametersMustNotBeNull.Violation,
               StateGraphBuilder.MustFinishBuildingTransition.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               transition_arc );

        synchronized ( this.lock )
        {
            if ( this.transitions.size () > 0 )
            {
                throw StateGraphBuilder.MustFinishBuildingTransition.CONTRACT
                    .violation ( this,
                                 this.transitions );
            }

            this.transitionArcs.add ( transition_arc );

            final Value<?> from = transition_arc.from ();
            if ( ! this.states.contains ( from ) )
            {
                this.states.add ( from );
            }

            final Value<?> to = transition_arc.to ();
            if ( ! this.states.contains ( to ) )
            {
                this.states.add ( to );

                this.automaticExitArcs.add (
                    new Arc<Value<?>, Transition> (
                        to,
                        Transition.AUTOMATIC,
                        this.exit ) );
            }

            if ( transition_arc instanceof SubGraph )
            {
                final SubGraph<Value<?>, Transition> sub_graph =
                    (SubGraph<Value<?>, Transition>) transition_arc;
                this.subGraphs.add ( sub_graph );
            }

            // The "from" state now has a path to the exit, so remove
            // it from the automatic arcs.
            for ( Arc<Value<?>, Transition> automatic_arc
                      : this.automaticExitArcs )
            {
                if ( automatic_arc.isFrom ( from ) )
                {
                    this.automaticExitArcs.remove ( automatic_arc );
                    break;
                }
            }
        }

        return this;
    }


    /**
     * @see musaico.foundation.graph.Graph#arc(musaico.foundation.graph.Arc)
     */
    @Override
    public final ZeroOrOne<Arc<Value<?>, Transition>> arc (
            Arc<Value<?>, Transition> arc
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               arc );

        final Arc<Value<?>, Transition> found_arc;
        synchronized ( this.lock )
        {
            final int index = this.transitionArcs.indexOf ( arc );
            if ( index < 0 )
            {
                final int automatic_index =
                    this.automaticExitArcs.indexOf ( arc );
                if ( index < 0 )
                {
                    found_arc = null;
                }
                else
                {
                    found_arc = this.automaticExitArcs.get ( index );
                }
            }
            else
            {
                found_arc = this.transitionArcs.get ( index );
            }
        }

        final ZeroOrOne<Arc<Value<?>, Transition>> maybe_arc;
        if ( found_arc != null )
        {
            maybe_arc =
                new One<Arc<Value<?>, Transition>> (
                    TapeMachine.ARC_VALUE_CLASS,
                    found_arc );
        }
        else
        {
            final ArcMustBeInGraph<Value<?>, Transition> arc_must_be_in_graph =
                new ArcMustBeInGraph<Value<?>, Transition> ( this );
            final ArcMustBeInGraph.Violation violation =
                arc_must_be_in_graph.violation ( this,
                                                 arc );
            maybe_arc =
                new No<Arc<Value<?>, Transition>> (
                    TapeMachine.ARC_VALUE_CLASS,
                    violation );
        }

        return maybe_arc;
    }


    /**
     * @see musaico.foundation.graph.Graph#arcValueClass()
     */
    @Override
    public final ValueClass<Transition> arcValueClass ()
        throws ReturnNeverNull.Violation
    {
        return Transition.VALUE_CLASS;
    }


    /**
     * @see musaico.foundation.graph.Graph#arcs()
     */
    @Override
    public final Countable<Arc<Value<?>, Transition>> arcs ()
        throws ReturnNeverNull.Violation
    {
        final ValueBuilder<Arc<Value<?>, Transition>> builder =
            new ValueBuilder<Arc<Value<?>, Transition>> (
                TapeMachine.ARC_VALUE_CLASS );

        synchronized ( this.lock )
        {
            builder.addAll ( this.transitionArcs );
            builder.addAll ( this.automaticExitArcs );
        }

        final Countable<Arc<Value<?>, Transition>> arcs =
            builder.build ();

        return arcs;
    }


    /**
     * @see musaico.foundation.graph.Graph#arcs(java.lang.Object)
     */
    @Override
    public final Countable<Arc<Value<?>, Transition>> arcs (
            Value<?> node
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               node );

        final ValueBuilder<Arc<Value<?>, Transition>> builder =
            new ValueBuilder<Arc<Value<?>, Transition>> (
                TapeMachine.ARC_VALUE_CLASS );

        synchronized ( this.lock )
        {
            if ( ! this.states.contains ( node ) )
            {
                final NodeMustBeInGraph<Value<?>> node_must_be_in_graph =
                    new NodeMustBeInGraph<Value<?>> ( this );
                final NodeMustBeInGraph.Violation violation =
                    node_must_be_in_graph.violation ( this,
                                                      node );
                final No<Arc<Value<?>, Transition>> no_such_node =
                    new No<Arc<Value<?>, Transition>> (
                        TapeMachine.ARC_VALUE_CLASS,
                        violation );
                return no_such_node;
            }

            for ( Arc<Value<?>, Transition> arc : this.transitionArcs )
            {
                if ( arc.isFrom ( node ) )
                {
                    builder.add ( arc );
                }
            }

            for ( Arc<Value<?>, Transition> arc : this.automaticExitArcs )
            {
                if ( arc.isFrom ( node ) )
                {
                    builder.add ( arc );
                    break;
                }
            }
        }

        final Countable<Arc<Value<?>, Transition>> arcs =
            builder.build ();

        return arcs;
    }


    /**
     * <p>
     * Builds a new ImmutableGraph which includes the automatically-generated
     * exit Arcs.
     * </p>
     *
     * @return A new, immutable Graph, containing all the state and
     *         Transitions in this builder.  Never null.
     */
    @SuppressWarnings({"rawtypes", // new Arc [].
                "unchecked"}) // Cast Arc [] to Arc<S, T> [].
    public ImmutableGraph<Value<?>, Transition> build ()
        throws ReturnNeverNull.Violation
    {
        final Arc<Value<?>, Transition> [] arcs;
        synchronized ( this.lock )
        {
            arcs = (Arc<Value<?>, Transition> [])
                new Arc [ this.transitionArcs.size ()
                          + this.automaticExitArcs.size () ];
            int a = 0;
            for ( Arc<Value<?>, Transition> arc : this.transitionArcs )
            {
                arcs [ a ] = arc;
                a ++;
            }
            for ( Arc<Value<?>, Transition> arc : this.automaticExitArcs )
            {
                arcs [ a ] = arc;
                a ++;
            }
        }

        return new StandardGraph<Value<?>, Transition> (
                       TapeMachine.STATE_VALUE_CLASS,
                       Transition.VALUE_CLASS,
                       this.entry,
                       this.exit,
                       arcs );
    }


    /**
     * @see musaico.foundation.tape.StateGraphBuilder#choose(musaico.foundation.graph.Graph)
     *
     * @param choices 1 or more values which will be fed to the input
     *                at runtime, in sequence.  Must not be null.
     *                Must not contain any null elements.
     */
    public StateGraphBuilder choose (
                                     Number ... number_choices
                                     )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               StateGraphBuilder.MustBuildOneTransitionAtATime.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               (Object) number_choices );
        this.contracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               number_choices );

        return this.choose (
            new StandardValueClass<Number> ( // value_class
                Number.class, // element_class
                0 ),          // none
            number_choices );
    }


    /**
     * @see musaico.foundation.tape.StateGraphBuilder#choose(musaico.foundation.graph.Graph)
     *
     * @param choices 1 or more values which will be fed to the input
     *                at runtime, in sequence.  Must not be null.
     *                Must not contain any null elements.
     */
    public StateGraphBuilder choose (
                                     String ... string_choices
                                     )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               StateGraphBuilder.MustBuildOneTransitionAtATime.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               (Object) string_choices );
        this.contracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               string_choices );

        return this.choose (
            new StandardValueClass<String> ( // value_class
                String.class,  // element_class
                "" ),          // none
            string_choices );
    }


    /**
     * @see musaico.foundation.tape.StateGraphBuilder#choose(musaico.foundation.graph.Graph)
     *
     * @param choice_value_class The ValueClass of each element
     *                           of the specified choices parameter,
     *                           such as a ValueClass&lt;String&gt;,
     *                           or a ValueClass&lt;Integer&gt;, and so on.
     *                           Must not be null.
     *
     * @param choices 1 or more values which will be fed to the input
     *                at runtime, in sequence.  Must not be null.
     *                Must not contain any null elements.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
        // Possible heap pollution VALUE ... vararg,
        // generic array creation Value [].
    public <VALUE extends Object>
        StateGraphBuilder choose (
                                  ValueClass<VALUE> choice_value_class,
                                  VALUE ... choices
                                  )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation,
               StateGraphBuilder.MustBuildOneTransitionAtATime.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               choice_value_class, (Object) choices );
        this.contracts.check ( Parameter2.MustContainNoNulls.CONTRACT,
                               choices );

        final Value<?> [] choice_values =
            new Value [ choices.length ];
        for ( int c = 0; c < choices.length; c ++ )
        {
            choice_values [ c ] = new One<VALUE> ( choice_value_class,
                                                   choices [ c ] );
        }

        return this.choose ( choice_values );
    }



    /**
     * @see musaico.foundation.tape.StateGraphBuilder#choose(musaico.foundation.graph.Graph)
     *
     * @param choices 1 or more Values which will be fed to the input
     *                at runtime, in sequence.  Must not be null.
     *                Must not contain any null elements.
     */
    public StateGraphBuilder choose (
                                     Value<?> ... choices
                                     )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               StateGraphBuilder.MustBuildOneTransitionAtATime.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               (Object) choices );
        this.contracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               choices );

        final Graph<Value<?>, Transition> choices_graph =
            ChooseInputTransition.createSequence ( choices );

        return this.choose ( choices_graph );
    }


    /**
     * <p>
     * Creates a Transition that will feed the input at runtime,
     * moving the input to a new state, potentially choosing between
     * branches.
     * </p>
     *
     * <p>
     * If no state is currently being built then the entry state will be
     * used by default as the source of the arc to be built up with
     * a new choose transition.
     * </p>
     *
     * @param choices A Graph of choices to feed the input
     *                at runtime.  Must not be null.  Must not contain
     *                any null elements.
     *
     * @return This StateGraphBuilder.  Never null.
     */
    public StateGraphBuilder choose (
                                     Graph<Value<?>, Transition> choices
                                     )
        throws ParametersMustNotBeNull.Violation,
               StateGraphBuilder.MustBuildOneTransitionAtATime.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               choices );

        final ChooseInputTransition choose_transition =
            new ChooseInputTransition ( choices );

        synchronized ( this.lock )
        {
            ChooseInputTransition existing_choose_transition = null;
            for ( Transition transition : this.transitions )
            {
                if ( transition instanceof ChooseInputTransition )
                {
                    existing_choose_transition =
                        (ChooseInputTransition) transition;
                    break;
                }
            }

            if ( existing_choose_transition != null )
            {
                final LeftAndRight<Transition, Transition> transitions =
                    new LeftAndRight<Transition, Transition> (
                        existing_choose_transition,
                        choose_transition );
                throw StateGraphBuilder.MustBuildOneTransitionAtATime.CONTRACT
                    .violation ( this,
                                 transitions );
            }

            if ( this.state == null )
            {
                this.state = this.entry;
            }

            this.transitions.add ( choose_transition );
        }

        return this;
    }


    /**
     * @return The Advocate for this StateGraphBuilder.  Checks parameter
     *         obligations, return guarantees, and so on for us.
     *         Never null.
     *
     * Protected for use by derived classes only.
     */
    protected final Advocate contracts ()
        throws ReturnNeverNull.Violation
    {
        return this.contracts;
    }


    /**
     * <p>
     * Creates a new transition which copies the current state from
     * one tape machine to another tape machine.
     * </p>
     *
     * <p>
     * During the transition, a new Arc with an AutomaticTransition
     * will be written to the target tape machine's graph.  The new Arc
     * will lead out from the tape machine's current state, and lead
     * to the state copied from the source tape machine.  If the state
     * already exists in the target tape machine, then only the Arc
     * will be added.  If the state does not already exist in
     * the target tape machine, then it will be added to
     * the target tape machine's graph.
     * </p>
     *
     * <p>
     * After a suceessful transition at runtime, the target tape machine
     * will change to the copied state.
     * </p>
     *
     * <p>
     * If no Transition is currently being built, then an AutomaticTransition
     * is created from the current state being built to the target state.
     * </p>
     *
     * <p>
     * If No Transition is currently being built, and no state
     * is currently being built, then an AutomaticTransition is created
     * from the entry state to the target state.
     * </p>
     *
     * <p>
     * Tnis method does NOT change which state is currently being built;
     * call <code> from ( ... ) </code> to begin building a different state.
     * </p>
     *
     * @param source The Tape whose tape machine will provide its current
     *               state, to be copied to the target.  Must not be null.
     *
     * @param target The Tape to whose state graph the source tape machine's
     *               state will be copied.  Must not be null.
     *
     * @return This StateGraphBuilder.  Never null.
     */
    public StateGraphBuilder copy (
                                   Tape source,
                                   Tape target
                                   )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               source, target );

        final Copy copy_transition =
            new Copy ( source,
                       target,
                       Transition.AUTOMATIC );

        synchronized ( this.lock )
        {
            // More than one copy transitions are allowed in a
            // composite transition for a single Arc.  So we do not
            // perform the same checks for an existing transition
            // that the on () method performs.
            if ( this.state == null )
            {
                this.state = this.entry;
            }

            this.transitions.add ( copy_transition );
        }

        return this;
    }


    /**
     * @see musaico.foundation.graph.Graph#entry()
     */
    @Override
    public final Value<?> entry ()
        throws ReturnNeverNull.Violation
    {
        return this.entry;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
                           Object object
                           )
    {
        if ( object == this )
        {
            return true;
        }
        else if ( object == null )
        {
            return false;
        }
        else if ( this.getClass () != object.getClass () )
        {
            return false;
        }

        final StateGraphBuilder that = (StateGraphBuilder) object;

        final Value<?> this_entry = this.entry ();
        final Value<?> that_entry = that.entry ();
        if ( ! this_entry.equals ( that_entry ) )
        {
            return false;
        }

        final Value<?> this_exit = this.exit ();
        final Value<?> that_exit = that.exit ();
        if ( ! this_exit.equals ( that_exit ) )
        {
            return false;
        }

        final Countable<Arc<Value<?>, Transition>> this_arcs = this.arcs ();
        final Countable<Arc<Value<?>, Transition>> that_arcs = that.arcs ();
        if ( ! this_arcs.equals ( that_arcs ) )
        {
            return false;
        }

        final Countable<Value<?>> this_nodes = this.nodes ();
        final Countable<Value<?>> that_nodes = that.nodes ();
        if ( ! this_nodes.equals ( that_nodes ) )
        {
            return false;
        }

        return true;
    }


    /**
     * @see musaico.foundation.graph.Graph#exit()
     */
    @Override
    public final Value<?> exit ()
        throws ReturnNeverNull.Violation
    {
        return this.exit;
    }


    /**
     * @see musaico.foundation.tape.StateGraphBuilder#from(musaico.foundation.value.Value)
     *
     * @param elements The zero or more value elements at this state, such as
     *                 a String label, or an empty array, and so on.
     *                 Must not be null.  Must not contain any null elements.
     */
    public StateGraphBuilder from (
                                   Number ... elements
                                   )
        throws ParametersMustNotBeNull.Violation,
               StateGraphBuilder.MustFinishBuildingTransition.Violation,
               Parameter2.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        return this.from (
                          this.createValue ( elements ) );
    }


    /**
     * @see musaico.foundation.tape.StateGraphBuilder#from(musaico.foundation.value.Value)
     *
     * @param elements The zero or more value elements at this state, such as
     *                 a String label, or an empty array, and so on.
     *                 Must not be null.  Must not contain any null elements.
     */
    public StateGraphBuilder from (
                                   String ... elements
                                   )
        throws ParametersMustNotBeNull.Violation,
               StateGraphBuilder.MustFinishBuildingTransition.Violation,
               Parameter2.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        return this.from (
                          this.createValue ( elements ) );
    }


    /**
     * @see musaico.foundation.tape.StateGraphBuilder#from(musaico.foundation.value.Value)
     *
     * @param value_class The ValueClass of each element,
     *                           such as a ValueClass&lt;String&gt;,
     *                           or a ValueClass&lt;Integer&gt;, and so on.
     *                           Must not be null.
     *
     * @param elements The zero or more value elements at this state, such as
     *                 a String label, or an empty array, and so on.
     *                 Must not be null.  Must not contain any null elements.
     */
    @SuppressWarnings("unchecked") // Possible heap pollution VALUE ... vararg.
    public <VALUE extends Object>
            StateGraphBuilder from (
                                    ValueClass<VALUE> value_class,
                                    VALUE ... elements
                                    )
        throws ParametersMustNotBeNull.Violation,
               StateGraphBuilder.MustFinishBuildingTransition.Violation,
               Parameter2.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        return this.from (
                          this.createValue ( value_class,
                                             elements ) );
    }


    /**
     * <p>
     * Begins or continues building a new state with the specified
     * Value.
     * </p>
     *
     * <p>
     * If there is currently no state with the specified value
     * in the graph-being-built, then it will be added.
     * </p>
     *
     * @param state The state to build, such as One String label,
     *              or No value at all, and so on.  Must not be null.
     *
     * @return This StateGraphBuilder.  Never null.
     */
    public StateGraphBuilder from (
                                   Value<?> state
                                   )
        throws ParametersMustNotBeNull.Violation,
               StateGraphBuilder.MustFinishBuildingTransition.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               state );

        synchronized ( this.lock )
        {
            if ( this.transitions.size () > 0 )
            {
                throw StateGraphBuilder.MustFinishBuildingTransition.CONTRACT
                    .violation ( this,
                                 this.transitions );
            }

            if ( ! this.states.contains ( state ) )
            {
                this.states.add ( state );

                this.automaticExitArcs.add (
                    new Arc<Value<?>, Transition> (
                        state,
                        Transition.AUTOMATIC,
                        this.exit ) );
            }

            this.state = state;
        }

        return this;
    }


    /**
     * <p>
     * Adds a SubGraph to the graph-being-built, and begins building
     * a new state in the graph-being-built which is equal to the
     * specified SubGraph's exit state.
     * </p>
     *
     * @param graph The Graph which will be built into a SubGraph
     *              for the graph-being-built.  The specified Graph's
     *              exit state will be the new state-being-built.
     *              Must not be null.  Must not be null.
     *
     * @return This StateGraphBuilder.  Never null.
     */
    public StateGraphBuilder from (
                                   Graph<Value<?>, Transition> graph
                                   )
        throws ParametersMustNotBeNull.Violation,
               StateGraphBuilder.MustFinishBuildingTransition.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               graph );

        final Value<?> graph_entry = graph.entry ();
        final Value<?> graph_exit = graph.exit ();
        final SubGraph<Value<?>, Transition> sub_graph =
            new SubGraph<Value<?>, Transition> ( graph,
                                                 Transition.AUTOMATIC );

        synchronized ( this.lock )
        {
            // First begin the new state-under-construction, or
            // fail if an arc is currently being built.
            // Connect from the exit state of the specified graph.
            this.from ( graph_exit );

            // If necessary, add a SubGraph.
            // If the same Graph already exists as a SubGraph in
            // this builder, then we will just skip this part and
            // re-use the existing SubGraph.
            if ( ! this.subGraphs.contains ( sub_graph ) )
            {
                this.states.add ( graph_entry );
                this.states.add ( graph_exit );
                this.subGraphs.add ( sub_graph );
                this.transitionArcs.add ( sub_graph );
            }
        }

        return this;
    }


    /**
     * @see musaico.foundation.tape.StateGraphBuilder#from(musaico.foundation.graph.Graph)
     *
     * @param machine The TapeMachine whose Graph will be turned into
     *                a SubGraph for the graph-being-built.  The specified
     *                Graph's exit state will be the new state-being-built.
     *                Must not be null.
     *
     * @return This StateGraphBuilder.  Never null.
     */
    public StateGraphBuilder from (
                                   TapeMachine machine
                                   )
        throws ParametersMustNotBeNull.Violation,
               StateGraphBuilder.MustFinishBuildingTransition.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               machine );

        return this.from ( machine.graph () );
    }


    /**
     * @see musaico.foundation.tape.StateGraphBuilder#from(musaico.foundation.value.Value)
     *
     * <p>
     * Begins building a new Transition from the entry state.
     * </p>
     */
    public StateGraphBuilder fromEntry ()
        throws StateGraphBuilder.MustFinishBuildingTransition.Violation,
               ReturnNeverNull.Violation
    {
        return this.from ( this.entry );
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return this.entry.hashCode ();
    }


    /**
     * @see musaico.foundation.graph.Graph#immutable()
     */
    @Override
    public final ImmutableGraph<Value<?>, Transition> immutable ()
        throws ReturnNeverNull.Violation
    {
        return this.build ();
    }


    /**
     * @see musaico.foundation.graph.Graph#node(java.lang.Object)
     */
    @Override
    public final ZeroOrOne<Value<?>> node (
                                           Value<?> node
                                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               node );

        final Value<?> found_node;
        synchronized ( this.lock )
        {
            if ( this.states.contains ( node ) )
            {
                found_node = node;
            }
            else
            {
                found_node = null;
            }
        }

        final ZeroOrOne<Value<?>> maybe_node;
        if ( found_node != null )
        {
            maybe_node = new One<Value<?>> (
                             TapeMachine.STATE_VALUE_CLASS,
                             found_node );
        }
        else
        {
            final NodeMustBeInGraph<Value<?>> node_must_be_in_graph =
                new NodeMustBeInGraph<Value<?>> ( this );
            final NodeMustBeInGraph.Violation violation =
                node_must_be_in_graph.violation ( this,
                                                  node );
            maybe_node = new No<Value<?>> (
                             TapeMachine.STATE_VALUE_CLASS,
                             violation );
        }

        return maybe_node;
    }


    /**
     * @see musaico.foundation.graph.Graph#nodeValueClass()
     */
    @Override
    public final ValueClass<Value<?>> nodeValueClass ()
        throws ReturnNeverNull.Violation
    {
        return TapeMachine.STATE_VALUE_CLASS;
    }


    /**
     * @see musaico.foundation.graph.Graph#nodes()
     */
    @Override
    public final Countable<Value<?>> nodes ()
        throws ReturnNeverNull.Violation
    {
        final ValueBuilder<Value<?>> builder =
            new ValueBuilder<Value<?>> ( TapeMachine.STATE_VALUE_CLASS );
        synchronized ( this.lock )
        {
            builder.addAll ( this.states );
        }

        final Countable<Value<?>> nodes = builder.build ();

        return nodes;
    }


    /**
     * @return The number of arcs in this CountableGraph.  Always greater
     *         than or equal to 0L.
     */
    public final long numArcs ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        synchronized ( this.lock )
        {
            return (long) this.transitionArcs.size ()
                + (long) this.automaticExitArcs.size ();
        }
    }


    /**
     * @return The number of nodes in this CountableGraph.  Always greater
     *         than or equal to 0L.
     */
    public final long numNodes ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        synchronized ( this.lock )
        {
            return (long) this.states.size ();
        }
    }


    /**
     * @see musaico.foundation.graph.CountableGraph#numSubGraphs()
     */
    public final long numSubGraphs ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        synchronized ( this.lock )
        {
            return (long) this.subGraphs.size ();
        }
    }


    /**
     * @see musaico.foundation.tape.StateGraphBuilder#on(musaico.foundation.tape.Transition)
     *
     * <p>
     * Reading is done from the <code> Tape.INPUT </code> tape.
     * </p>
     *
     * @param enumerated_values One or more constants, any of which
     *                          is accepted as an input.  A Contract
     *                          is created to match any one of the
     *                          specified values.  Must not be null.
     *                          Must not contain any null elements.
     */
    @SuppressWarnings("unchecked") // Possible heap pollution DOMAIN... vararg.
    public <DOMAIN extends Object>
            StateGraphBuilder on (
                                  DOMAIN ... enumerated_values
                                  )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.Length.MustBeGreaterThanZero.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        return this.on ( Tape.INPUT,
                         enumerated_values );
    }


    /**
     * @see musaico.foundation.tape.StateGraphBuilder#on(musaico.foundation.tape.Transition)
     *
     * @param tape The Tape to read from, such as Tape.INPUT.
     *             Must not be null.
     *
     * @param enumerated_values One or more constants, any of which
     *                          is accepted as an input.  A Contract
     *                          is created to match any one of the
     *                          specified values.  Must not be null.
     *                          Must not contain any null elements.
     */
    @SuppressWarnings("unchecked") // Possible heap pollution DOMAIN... vararg.
    public <DOMAIN extends Object>
            StateGraphBuilder on (
                                  Tape tape,
                                  DOMAIN ... enumerated_values
                                  )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.Length.MustBeGreaterThanZero.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               enumerated_values );
        this.contracts.check ( Parameter1.Length.MustBeGreaterThanZero.CONTRACT,
                               enumerated_values );
        this.contracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               enumerated_values );

        final Contract<Value<?>, ?> contract =
            Read.createEnumerationContract ( enumerated_values );

        return this.on ( tape,
                         contract );
    }


    /**
     * @see musaico.foundation.tape.StateGraphBuilder#on(musaico.foundation.tape.Transition)
     *
     * @param regular_expression A regular expression which will be used
     *                           to determine whether each String value
     *                           read from the Tape.INPUT tape
     *                           induces a transition (kept)
     *                           or an Error result (discarded).
     *                           Must not be null.
     */
    public StateGraphBuilder on (
                                 Pattern regular_expression
                                 )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return this.on ( Tape.INPUT,
                         regular_expression );
    }


    /**
     * @see musaico.foundation.tape.StateGraphBuilder#on(musaico.foundation.tape.Transition)
     *
     * @param tape The Tape to read from, such as Tape.INPUT.
     *             Must not be null.
     *
     * @param regular_expression A regular expression which will be used
     *                           to determine whether each String value
     *                           read from the specified tape
     *                           induces a transition (kept)
     *                           or an Error result (discarded).
     *                           Must not be null.
     */
    public StateGraphBuilder on (
                                 Tape tape,
                                 Pattern regular_expression
                                 )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               regular_expression );

        return this.on ( tape,
                         new StringPattern ( regular_expression ) ); // read_domain
    }


    /**
     * @see musaico.foundation.tape.StateGraphBuilder#on(musaico.foundation.tape.Transition)
     *
     * @param read_domain A Domain which determines whether each
     *                    value read from the Tape.INPUT tape
     *                    induces a transition (kept)
     *                    or an Error result (discarded).
     *                    Must not be null.
     */
    public <DOMAIN extends Object>
            StateGraphBuilder on (
                                  Domain<DOMAIN> read_domain
                                  )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return this.on ( Tape.INPUT,
                         read_domain );
    }


    /**
     * @see musaico.foundation.tape.StateGraphBuilder#on(musaico.foundation.tape.Transition)
     *
     * @param tape The Tape to read from, such as Tape.INPUT.
     *             Must not be null.
     *
     * @param input_domain A Domain which determines whether each
     *                     value read from the machine corresponding
     *                     to the specified tape induces a transition (kept)
     *                     or an Error result (discarded).
     *                     Must not be null.
     */
    public <DOMAIN extends Object>
            StateGraphBuilder on (
                                  Tape tape,
                                  Domain<DOMAIN> read_domain
                                  )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               read_domain );

        return this.on ( tape,
                         new ElementsMustBelongToDomain<DOMAIN> (
                             read_domain
                             ) );
    }


    /**
     * @see musaico.foundation.tape.StateGraphBuilder#on(musaico.foundation.tape.Transition)
     *
     * @param read_contract A Contract which checks each value read
     *                      from the Tape.INPUT tape, and either
     *                      induces a transition (consumption of
     *                      the value), or returns a Violation of the
     *                      Contract (which can be used to create
     *                      an Error output from the transition ()).
     *                      Must not be null.
     */
    public StateGraphBuilder on (
                                 Contract<Value<?>, ?> read_contract
                                 )
        throws ParametersMustNotBeNull.Violation,
               StateGraphBuilder.MustBuildOneTransitionAtATime.Violation,
               ReturnNeverNull.Violation
    {
        return this.on ( Tape.INPUT,
                         read_contract );
    }


    /**
     * @see musaico.foundation.tape.StateGraphBuilder#on(musaico.foundation.tape.Transition)
     *
     * @param tape The Tape to read from, such as Tape.INPUT.
     *             Must not be null.
     *
     * @param read_contract A Contract which checks each value read
     *                      from the specified tape, and either
     *                      induces a transition (consumption of
     *                      the value), or returns a Violation of the
     *                      Contract (which can be used to create
     *                      an Error output from the transition ()).
     *                      Must not be null.
     */
    public StateGraphBuilder on (
                                 Tape tape,
                                 Contract<Value<?>, ?> read_contract
                                 )
        throws ParametersMustNotBeNull.Violation,
               StateGraphBuilder.MustBuildOneTransitionAtATime.Violation,
               ReturnNeverNull.Violation
    {
        return this.on ( new Read (
                                   tape,
                                   read_contract ) );
    }


    /**
     * <p>
     * When some kind(s) of input(s) match the requirements of
     * the specified transition, a state change will be induced
     * from the state being built.
     * </p>
     *
     * <p>
     * If no state is currently being built then the entry state will be
     * used by default.
     * </p>
     *
     * @param input_transition A Transition which, when matched, will induce
     *                         a state change.  For example, a Read contract.
     *                         This should be some kind of input contract,
     *                         given the nature of this method ("on input X,
     *                         change from state F to state T").
     *                         Must not be null.
     *
     * @return This StateGraphBuilder.  Never null.
     */
    public StateGraphBuilder on (
                                 Transition input_transition
                                 )
        throws ParametersMustNotBeNull.Violation,
               StateGraphBuilder.MustBuildOneTransitionAtATime.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               input_transition );

        synchronized ( this.lock )
        {
            Read existing_on_transition = null;
            for ( Transition transition : this.transitions )
            {
                if ( transition instanceof Read )
                {
                    existing_on_transition = (Read) transition;
                    break;
                }
            }

            if ( existing_on_transition != null )
            {
                final LeftAndRight<Transition, Transition> transitions =
                    new LeftAndRight<Transition, Transition> (
                        existing_on_transition,
                        input_transition );
                throw StateGraphBuilder.MustBuildOneTransitionAtATime.CONTRACT
                    .violation ( this,
                                 transitions );
            }

            if ( this.state == null )
            {
                this.state = this.entry;
            }

            this.transitions.add ( input_transition );
        }

        return this;
    }


    /**
     * @see musaico.foundation.graph.CountableGraph#subGraphs()
     */
    public final Countable<SubGraph<Value<?>, Transition>> subGraphs ()
    {
        final ValueBuilder<SubGraph<Value<?>, Transition>> builder =
            new ValueBuilder<SubGraph<Value<?>, Transition>> (
                TapeMachine.SUB_GRAPH_VALUE_CLASS );

        synchronized ( this.lock )
        {
            builder.addAll ( this.subGraphs );
        }

        final Countable<SubGraph<Value<?>, Transition>> sub_graphs =
            builder.build ();

        return sub_graphs;
    }


    /**
     * @see musaico.foundation.tape.StateGraphBuilder#to(musaico.foundation.value.Value)
     *
     * @param elements The zero or more value elements at the target state,
     *                 such as a String label, or an empty array, and so on.
     *                 Must not be null.  Must not contain any null elements.
     */
    public StateGraphBuilder to (
                                 Number ... elements
                                 )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        return this.to (
                        this.createValue ( elements ) );
    }


    /**
     * @see musaico.foundation.tape.StateGraphBuilder#to(musaico.foundation.value.Value)
     *
     * @param elements The zero or more value elements at the target state,
     *                 such as a String label, or an empty array, and so on.
     *                 Must not be null.  Must not contain any null elements.
     */
    public StateGraphBuilder to (
                                 String ... elements
                                 )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        return this.to (
                        this.createValue ( elements ) );
    }


    /**
     * @see musaico.foundation.tape.StateGraphBuilder#to(musaico.foundation.value.Value)
     *
     * @param value_class The ValueClass of each of the specified elements,
     *                    such as a ValueClass&lt;String&gt;,
     *                    or a ValueClass&lt;Integer&gt;, and so on.
     *                    Must not be null.
     *
     * @param elements The zero or more value elements at the target state,
     *                 such as a String label, or an empty array, and so on.
     *                 Must not be null.  Must not contain any null elements.
     */
    @SuppressWarnings("unchecked") // Possible heap pollution VALUE ... vararg.
    public <VALUE extends Object>
            StateGraphBuilder to (
                                  ValueClass<VALUE> value_class,
                                  VALUE ... elements
                                  )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        return this.to (
                        this.createValue ( value_class,
                                           elements ) );
    }


    /**
     * <p>
     * Completes the current Transition being built, ending the
     * Transition at a target state containing the specified
     * value.
     * </p>
     *
     * <p>
     * If no Transition is currently being built, then an AutomaticTransition
     * is created from the current state being built to the target state.
     * </p>
     *
     * <p>
     * If No Transition is currently being built, and no state
     * is currently being built, then an AutomaticTransition is created
     * from the entry state to the target state.
     * </p>
     *
     * <p>
     * If the state containing the specified value is new,
     * then it will be added to the graph-being-built.
     * </p>
     *
     * <p>
     * Tnis method does NOT change which state is currently being built;
     * call <code> from ( ... ) </code> to begin building a different state.
     * </p>
     *
     * @param state The target state for the Transition to be built.
     *              Must not be null.
     *
     * @return This StateGraphBuilder.  Never null.
     */
    public StateGraphBuilder to (
                                 Value<?> state
                                 )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               state );

        synchronized ( this.lock )
        {
            final Transition transition;
            if ( this.transitions.size () == 1 )
            {
                transition = this.transitions.get ( 0 );

                this.transitions.clear ();
            }
            else if ( this.transitions.size () > 1 )
            {
                transition =
                    new CompositeTransition ( this.transitions );

                this.transitions.clear ();
            }
            else
            {
                if ( this.state == null )
                {
                    this.state = this.entry;
                }

                transition = Transition.AUTOMATIC;
            }

            final Arc<Value<?>, Transition> arc =
                new Arc<Value<?>, Transition> ( this.state, // from
                                                transition, // arc
                                                state ); // to

            this.transitionArcs.add ( arc );

            if ( ! this.states.contains ( state ) )
            {
                this.states.add ( state );
                this.automaticExitArcs.add (
                    new Arc<Value<?>, Transition> (
                        state,
                        Transition.AUTOMATIC,
                        this.exit ) );
            }

            // The "from" state now has a path to the exit, so remove
            // it from the automatic arcs.
            for ( Arc<Value<?>, Transition> automatic_arc
                      : this.automaticExitArcs )
            {
                if ( automatic_arc.isFrom ( this.state ) )
                {
                    this.automaticExitArcs.remove ( automatic_arc );
                    break;
                }
            }
        }

        return this;
    }


    /**
     * <p>
     * Completes the current Transition being built, ending the
     * Transition at the entry state to a SubGraph, and adding an
     * automatic transition into the SubGraph.
     * </p>
     *
     * <p>
     * If no Transition is currently being built, then an AutomaticTransition
     * is created from the current state being built to the SubGraph's
     * entry state.
     * </p>
     *
     * <p>
     * If No Transition is currently being built, and no state
     * is currently being built, then an AutomaticTransition is created
     * from the entry state to the target SubGraph's entry state.
     * </p>
     *
     * <p>
     * A SubGraph will be added to the graph-being-built unless
     * it already exists in the graph-being-built, in which case
     * the new transition will lead to the existing entry state and
     * the existing automatic transition to the SubGraph.
     * </p>
     *
     * <p>
     * Tnis method does NOT change which state is currently being built;
     * call <code> from ( ... ) </code> to begin building a different state.
     * </p>
     *
     * @param graph The Graph which will be built into a SubGraph
     *              for the graph-being-built.  The current
     *              transition being built will end in the specified
     *              Graph's entry state.  Must not be null.
     *              Must not be null.
     *
     * @return This StateGraphBuilder.  Never null.
     *
     * @see musaico.foundation.tape.StateGraphBuilder#to(musaico.foundation.value.Value)
     */
    public StateGraphBuilder to (
                                 Graph<Value<?>, Transition> graph
                                 )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               graph );

        final Value<?> graph_entry = graph.entry ();
        final Value<?> graph_exit = graph.exit ();
        final SubGraph<Value<?>, Transition> sub_graph =
            new SubGraph<Value<?>, Transition> ( graph,
                                                 Transition.AUTOMATIC );

        synchronized ( this.lock )
        {
            // If necessary, add a SubGraph.
            // If the same Graph already exists as a SubGraph in
            // this builder, then we will just skip this part and
            // re-use the existing SubGraph.
            final boolean is_new;
            if ( this.subGraphs.contains ( sub_graph ) )
            {
                is_new = false;
            }
            else
            {
                is_new = true;

                this.states.add ( graph_entry );
                this.states.add ( graph_exit );
                this.subGraphs.add ( sub_graph );

                this.automaticExitArcs.add (
                    new Arc<Value<?>, Transition> (
                        graph_exit,
                        Transition.AUTOMATIC,
                        this.exit ) );
            }

            // Now finish the current arc-under-construction.
            // Connect to the entry state of the specified graph.
            this.to ( graph_entry );

            if ( is_new )
            {
                // Sub-graph transition comes AFTER the transition
                // to the sub-graph's entry node.
                this.transitionArcs.add ( sub_graph );
            }
        }

        return this;
    }


    /**
     * @see musaico.foundation.tape.StateGraphBuilder#to(musaico.foundation.graph.Graph)
     *
     * @param machine The TapeMachine whose Graph will be turned into
     *                a SubGraph for the graph-being-built.  The current
     *                transition being built will end in the specified
     *                State Machine's Graph's entry state.
     *                Must not be null.
     *
     * @return This StateGraphBuilder.  Never null.
     */
    public StateGraphBuilder to (
                                 TapeMachine machine
                                 )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               machine );

        return this.to ( machine.graph () );
    }


    /**
     * @see musaico.foundation.tape.StateGraphBuilder#to(musaico.foundation.value.Value)
     *
     * <p>
     * Completes the current Transition being built, ending the
     * Transition at the entry state for the graph-being-built.
     * </p>
     */
    public StateGraphBuilder toEntry ()
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return this.to ( this.entry );
    }


    /**
     * @see musaico.foundation.tape.StateGraphBuilder#to(musaico.foundation.value.Value)
     *
     * <p>
     * Completes the current Transition being built, ending the
     * Transition at the exit state for the graph-being-built.
     * </p>
     */
    public StateGraphBuilder toExit ()
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return this.to ( this.exit );
    }


    /**
     * @see java.lang.toString()
     */
    @Override
    public String toString ()
    {
        synchronized ( this.lock )
        {
            return "StateGraphBuilder#" + this.hashCode ();
        }
    }


    /**
     * @see musaico.foundation.graph.Graph#toStringDetails()
     */
    @Override
    public String toStringDetails ()
    {
        final StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( "StateGraphBuilder#" );

        synchronized ( this.lock )
        {
            sbuf.append ( "" + this.hashCode () );
            sbuf.append ( " :\n" );

            for ( Arc<Value<?>, Transition> arc : this.transitionArcs )
            {
                sbuf.append ( "    " + arc + "\n" );
            }
        }

        return sbuf.toString ();
    }


    /**
     * @see musaico.foundation.tape.StateGraphBuilder#write(musaico.foundation.tape.Tape, musaico.foundation.value.Value)
     *
     * @param elements The zero or more value elements to write to
     *                 the machine corresponding to Tape.OUTPUT.
     *                 These will be turned into one Value and written
     *                 as a node in the tape machine's graph.
     *                 Must not be null.  Must not contain any null elements.
     */
    public StateGraphBuilder write (
                                    Number ... elements
                                    )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        return this.write (
                           Tape.OUTPUT,
                           this.createValue ( elements ) );
    }


    /**
     * @see musaico.foundation.tape.StateGraphBuilder#write(musaico.foundation.tape.Tape, musaico.foundation.value.Value)
     *
     * @param tape The Tape to output to.  Must not be null.
     *
     * @param elements The zero or more value elements to write to
     *                 the machine corresponding to the specified Tape.
     *                 These will be turned into one Value and written
     *                 as a node in the tape machine's graph.
     *                 Must not be null.  Must not contain any null elements.
     */
    public StateGraphBuilder write (
                                    Tape tape,
                                    Number ... elements
                                    )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        return this.write (
                           tape,
                           this.createValue ( elements ) );
    }


    /**
     * @see musaico.foundation.tape.StateGraphBuilder#write(musaico.foundation.tape.Tape, musaico.foundation.value.Value)
     *
     * @param elements The zero or more value elements to write to
     *                 the machine corresponding to Tape.OUTPUT.
     *                 These will be turned into one Value and written
     *                 as a node in the tape machine's graph.
     *                 Must not be null.  Must not contain any null elements.
     */
    public StateGraphBuilder write (
                                    String ... elements
                                    )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        return this.write (
                           Tape.OUTPUT,
                           this.createValue ( elements ) );
    }


    /**
     * @see musaico.foundation.tape.StateGraphBuilder#write(musaico.foundation.tape.Tape, musaico.foundation.value.Value)
     *
     * @param tape The Tape to output to.  Must not be null.
     *
     * @param elements The zero or more value elements to write to
     *                 the machine corresponding to the specified Tape.
     *                 These will be turned into one Value and written
     *                 as a node in the tape machine's graph.
     *                 Must not be null.  Must not contain any null elements.
     *
     * @param elements The zero or more value elements to write to
     *                 the machine corresponding to the specified tape.
     *                 These will be turned into one Value and written
     *                 as a node in the tape machine's graph.
     *                 Must not be null.  Must not contain any null elements.
     */
    public StateGraphBuilder write (
                                    Tape tape,
                                    String ... elements
                                    )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        return this.write (
                           tape,
                           this.createValue ( elements ) );
    }


    /**
     * @see musaico.foundation.tape.StateGraphBuilder#write(musaico.foundation.tape.Tape, musaico.foundation.value.Value)
     *
     * @param value_class The ValueClass of each of the specified elements,
     *                    such as a ValueClass&lt;String&gt;,
     *                    or a ValueClass&lt;Integer&gt;, and so on.
     *                    Must not be null.
     *
     * @param elements The zero or more value elements to write to
     *                 the machine corresponding to Tape.OUTPUT.
     *                 These will be turned into one Value and written
     *                 as a node in the tape machine's graph.
     *                 Must not be null.  Must not contain any null elements.
     */
    @SuppressWarnings("unchecked") // Possible heap pollution VALUE ... vararg.
    public <VALUE extends Object>
            StateGraphBuilder write (
                                     ValueClass<VALUE> value_class,
                                     VALUE ... elements
                                     )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        return this.write (
                           Tape.OUTPUT,
                           this.createValue ( value_class,
                                              elements ) );
    }


    /**
     * @see musaico.foundation.tape.StateGraphBuilder#write(musaico.foundation.tape.Tape, musaico.foundation.value.Value)
     *
     * @param tape The Tape to output to.  Must not be null.
     *
     * @param elements The zero or more value elements to write to
     *                 the machine corresponding to the specified Tape.
     *                 These will be turned into one Value and written
     *                 as a node in the tape machine's graph.
     *                 Must not be null.  Must not contain any null elements.
     *
     * @param value_class The ValueClass of each of the specified elements,
     *                    such as a ValueClass&lt;String&gt;,
     *                    or a ValueClass&lt;Integer&gt;, and so on.
     *                    Must not be null.
     *
     * @param elements The zero or more value elements to write to
     *                 the machine corresponding to Tape.OUTPUT.
     *                 These will be turned into one Value and written
     *                 as a node in the tape machine's graph.
     *                 Must not be null.  Must not contain any null elements.
     */
    @SuppressWarnings("unchecked") // Possible heap pollution VALUE ... vararg.
    public <VALUE extends Object>
            StateGraphBuilder write (
                                     Tape tape,
                                     ValueClass<VALUE> value_class,
                                     VALUE ... elements
                                     )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        return this.write (
                           tape,
                           this.createValue ( value_class,
                                              elements ) );
    }


    /**
     * @see musaico.foundation.tape.StateGraphBuilder#write(musaico.foundation.tape.Tape, musaico.foundation.value.Value)
     *
     * @param value The node to write out to the Tape.OUTPUT machine.
     *              Must not be null.
     */
    public StateGraphBuilder write (
                                    Value<?> value
                                    )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return this.write ( Tape.OUTPUT,
                            value );
    }


    /**
     * <p>
     * Creates a new transition which writes out one node to
     * a specific tape machine.
     * </p>
     *
     * <p>
     * During the transition, a new Arc with an AutomaticTransition
     * will be written to the tape machine's graph.  The new Arc
     * will lead out from the tape machine's current state, and lead
     * to the state specified here.  If the state already exists in
     * the tape machine, then only the Arc will be added.  If the
     * state does not already exist in the tape machine, then it
     * will be added to the tape machine's graph.
     * </p>
     *
     * <p>
     * After a suceessful transition at runtime, the tape machine
     * will change to the newly written state.
     * </p>
     *
     * <p>
     * If no Transition is currently being built, then an AutomaticTransition
     * is created from the current state being built to the target state.
     * </p>
     *
     * <p>
     * If No Transition is currently being built, and no state
     * is currently being built, then an AutomaticTransition is created
     * from the entry state to the target state.
     * </p>
     *
     * <p>
     * Tnis method does NOT change which state is currently being built;
     * call <code> from ( ... ) </code> to begin building a different state.
     * </p>
     *
     * @param tape The Tape to output to.  Must not be null.
     *
     * @param value The node to write out to the specified tape machine.
     *              Must not be null.
     *
     * @return This StateGraphBuilder.  Never null.
     */
    public StateGraphBuilder write (
                                    Tape tape,
                                    Value<?> value
                                    )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               tape, value );

        final Write write_transition =
            new Write ( tape,
                        Transition.AUTOMATIC,
                        value );

        synchronized ( this.lock )
        {
            // More than one write transitions are allowed in a
            // composite transition for a single Arc.  So we do not
            // perform the same checks for an existing transition
            // that the on () method performs.
            if ( this.state == null )
            {
                this.state = this.entry;
            }

            this.transitions.add ( write_transition );
        }

        return this;
    }


    /**
     * @see musaico.foundation.tape.StateGraphBuilder#write(musaico.foundation.value.Value)
     *
     * <p>
     * Creates a new transition which writes out this graph-being-built's
     * own entry node to the Tape.OUTPUT machine.
     * </p>
     */
    public StateGraphBuilder writeEntry ()
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return this.write ( Tape.OUTPUT,
                            this.entry );
    }


    /**
     * @see musaico.foundation.tape.StateGraphBuilder#write(musaico.foundation.value.Value)
     *
     * <p>
     * Creates a new transition which writes out this graph-being-built's
     * own entry node to a specific tape machine.
     * </p>
     */
    public StateGraphBuilder writeEntry (
                                         Tape tape
                                         )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return this.write ( tape,
                            this.entry );
    }


    /**
     * @see musaico.foundation.tape.StateGraphBuilder#write(musaico.foundation.value.Value)
     *
     * <p>
     * Creates a new transition which writes out this graph-being-built's
     * own entry node to the Tape.OUTPUT machine.
     * </p>
     */
    public StateGraphBuilder writeExit ()
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return this.write ( Tape.OUTPUT,
                            this.exit );
    }


    /**
     * @see musaico.foundation.tape.StateGraphBuilder#write(musaico.foundation.value.Value)
     *
     * <p>
     * Creates a new transition which writes out this graph-being-built's
     * own entry node to a specific tape machine.
     * </p>
     */
    public StateGraphBuilder writeExit (
                                        Tape tape
                                        )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return this.write ( tape,
                            this.exit );
    }
}
// Yes this class is ridiculously long.  Shorten me / split me up,
// if you can do so without losing any of the functionality...
