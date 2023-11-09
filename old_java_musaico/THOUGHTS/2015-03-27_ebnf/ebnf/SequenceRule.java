package ebnf;

import java.io.Serializable;

public class SequenceRule
    extends AbstractRule
    implements Serializable
{
    public SequenceRule (
                         String name,
                         Node [] sequence
                         )
    {
        super ( name,
                sequence,
                "", // start
                "", // separator
                "" ); // end
    }

    protected final NodeVisitor.VisitStatus visitChildren (
                                                           Node [] children,
                                                           NodeVisitor visitor,
                                                           VisitorState state
                                                           )
    {
        for ( Node node : children )
        {
            final NodeVisitor.VisitStatus status =
                node.accept ( visitor, state );

            switch ( status )
            {
            case CONTINUE:
            case POP:
                continue;

            case FAIL:
            case ABORT:
            default:
                return status;
            }
        }

        // Matched the entire sequence.
        return NodeVisitor.VisitStatus.CONTINUE;
    }
}
