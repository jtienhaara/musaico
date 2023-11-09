

    /**
     * <p>
     * Pops the current graph from the stack, returning
     * the exit state of the popped graph (which must also be
     * a state in the lower graph on the stack).
     * </p>
     *
     * <p>
     * If the current top graph on the stack is also the bottom graph
     * on the stack, then the graph is not popped, the machine simply
     * remains in the exit state of its main graph, but the One exit state
     * is still returned since a successful transition occurred to
     * put this machine in the exit state.
     * </p>
     *
     * @return The One exit state of the top graph on the stack.
     *         Never null.
     */
    public One<STATE> pop ()
        throws ReturnNeverNull.Violation
    {
        final Graph<STATE, ARC> popped_graph;
        synchronized ( this.lock )
        {
            if ( this.graphStack.size () == 1 )
            {
                popped_graph = this.graphStack.peek ();
            }
            else
            {
                popped_graph = this.graphStack.pop ();
            }
        }

        final One<STATE> one_state =
            new One<STATE> ( popped_graph.nodeValueClass (),
                             popped_graph.exit () );

        return one_state;
    }


    /**
     * <p>
     * Pushes the specified graph onto the stack, returning
     * the entry state of the pushed graph (which must also be
     * a state in the previous top graph).
     * </p>
     *
     * @param graph The graph to push onto the stack.
     *              Its entry and exit nodes must both exist in the current
     *              top Graph on the stack, otherwise the push will fail.
     *              Must not be null.
     *
     * @return The One entry state of the top graph on the stack.
     *         Never null.
     */
    public One<STATE> push (
                            Graph<STATE, ARC> graph
                            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               graph );

        synchronized ( this.lock )
        {
            final Graph<STATE, ARC> top_graph = this.graphStack.peek ();
            final NodeMustBeInGraph<STATE> state_must_be_in_graph =
                new NodeMustBeInGraph<STATE> ( top_graph );
            classContracts.check ( state_must_be_in_graph,
                                   graph.entry () );
            classContracts.check ( state_must_be_in_graph,
                                   graph.exit () );

            this.graphStack.push ( graph );
        }

        final One<STATE> one_state =
            new One<STATE> ( graph.nodeValueClass (),
                             graph.entry () );

        return one_state;
    }












!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!;


    /**
     * @return The Arc over which this transition will occur, when
     *         it is <code> execute () </code>d.  The Arc connects
     *         the "from" state and "to" state of the Machine,
     *         and can contain label data (such as filters, operations,
     *         and so on which are executed along with this transition).
     *         Never null.
     */
    public abstract Arc<STATE, ARC> arc ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Transitions out of one state, caused by one input.
     * </p>
     *
     * @param graph The current Graph being transitioned through.
     *              Must not be null.
     *
     * @param state The One current state.  Must not be null.
     *
     * @param input The single input into the state machine.
     *              Must not be null.
     */
    protected Maybe<STATE> transition (
                                       Graph<STATE, ARC> graph,
                                       One<STATE> one_state,
                                       INPUT input
                                       )
    {
        final STATE current_state = one_state.orNull ();

        final Value<Arc<STATE, ARC>> arcs =
            graph.arcs ( current_state );

        Exception first_failed_transition = null;
        for ( Arc<STATE, ARC> arc : arcs )
        {
            try
            {
                final ZeroOrOne<STATE> new_state =
                    this.transition ( graph,
                                      current_state,
                                      input,
                                      arc );

                final STATE state = new_state.orNull ();
                if ( state != null )
                {
                    if ( arc instanceof SubGraph )
                    {
                        final SubGraph<STATE, ARC> sub_graph
                            = (SubGraph<STATE, ARC>) arc;
                        return this.push ( sub_graph.graph () );
                    }

                    return new_state;
                }
                else if ( first_failed_transition == null
                          && ( new_state instanceof NotOne ) )
                {
                    final NotOne<STATE> failed_new_state =
                        (NotOne<STATE>) new_state;
                    first_failed_transition =
                        failed_new_state.valueViolation ();
                }

                // Keep searching for an arc out of the current state.
            }
            catch ( RuntimeException e )
            {
                // ClassCastException, or a bug in the
                // contract, and so on.  Skip this arc.
                if ( first_failed_transition == null )
                {
                    first_failed_transition = e;
                }
            }
        }

        @SuppressWarnings("unchecked") // Generic array creation
        final InputsMustCauseTransition.Violation violation =
            this.inputsMustCauseTransition ().violation ( this,
                                                          null, // cause
                                                          input );
        if ( first_failed_transition != null )
        {
            violation.initCause ( first_failed_transition );
        }

        final ValueClass<STATE> state_value_class = graph.nodeValueClass ();
        final Error<STATE> could_not_transition =
            new Error<STATE> ( state_value_class,
                               one_state, // cause
                               violation );
        return could_not_transition;
    }


    /**
     * <p>
     * Tries to transition out of the specified state, across the specified
     * arc, triggered by the specified input.
     * </p>
     *
     * @param graph The current Graph being transitioned through.
     *              Must not be null.
     *
     * @param state The One state in the specified graph to transition from.
     *              Must not be null.
     *
     * @param input The input into this state machine.
     *              Must not be null.
     *
     * @param arc The arc to transition over.  Must not be null.
     *
     * @return The One newly transitioned-to state, if the transition
     *         succeeded; or No state if the transition failed.
     *         Never null.
     */
    protected abstract ZeroOrOne<STATE> transition (
            Graph<STATE, ARC> graph,
            STATE state,
            INPUT input,
            Arc<STATE, ARC> arc
            )
        throws ReturnNeverNull.Violation;
