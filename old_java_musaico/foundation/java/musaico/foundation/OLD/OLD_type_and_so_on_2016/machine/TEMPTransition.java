            STATE last_state = state;
            for ( INPUT input : inputs )
            {
                while ( true )
                {
                    final STATE previous_state = maybe_state.orNull ();
                    maybe_state = this.transition ( top_graph,
                                                    (One<STATE>) maybe_state,
                                                    input );

                    if ( maybe_state.orNull () != null )
                    {
                        // We successfully transitioned using the
                        // specified input.
                        // Stop trying to transition with the same input.
                        break;
                    }
                    else if ( previous_state == top_graph.exit ()
                              && this.graphStackDepth () > 1L )
                    {
                        // We couldn't transition, but we're at the
                        // exit state of a sub-graph.  So pop the
                        // sub-graph off the stack.
                        // Then re-use the same input in the previous
                        // graph on the stack.
                        maybe_state = this.pop ();
                        top_graph = this.graphCurrent ();
                        continue;
                    }
                    else
                    {
                        // Failed to transition, and we're not at the
                        // exit state of a nested graph.
                        // Stop trying to transition with the same input.
                        break;
                    }
                }

                if ( maybe_state.orNull () == null )
                {
                    // Abort, do not process any more inputs.
                    break;
                }

                // Definitely not null at this point:
                last_state = maybe_state.orNull ();

                // In case of a push, the top graph might have changed.
                top_graph = this.graphCurrent ();
            }

            this.forceState ( last_state );


