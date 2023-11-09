package ebnf;

public interface NodeVisitor
{
    public static enum VisitStatus
    {
        /** Success.  Continue walking through the node graph.
         *  1 or more matches found. */
        CONTINUE,

        /** Success.  Stop walking through this node's children,
         *  but carry on with its siblings.  0 matches found. */
        POP,

        /** Failure.  Stop walking through this node's children,
         *  but carry on with its siblings if possible.  0 matches found. */
        FAIL,

        /** Failure.  Immediately stop walking through the node graph.
         *  0 matches found. */
        ABORT;
    }


    /**
     * <p>
     * Visits a single node in the graph.
     * </p>
     *
     * @param node The node to visit.  Must not be null.
     *
     * @param state The current state of the visitor.  Must not be null.
     *
     * @return The visit status: CONTINUE, POP, FAIL or ABORT.
     *
     * @see ebnf.NodeVisitor.VisitStatus
     */
    public abstract NodeVisitor.VisitStatus visit (
                                                   Node node,
                                                   VisitorState state
                                                   );
}
