package ebnf;

import java.io.Serializable;

public class OptionalRule
    extends AbstractRule
    implements Serializable
{
    public OptionalRule (
                         String name,
                         Node optional
                         )
    {
        super ( name,
                new Node [] { optional }, // sequence
                "[", // start
                "", // separator
                "]" ); // end
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
                // Optional rule was not completed, but that's OK.
                // carry on with the visits.
                return NodeVisitor.VisitStatus.POP;

            case ABORT:
            default:
                return status;
            }
        }

        // Matched the optional rule.
        return NodeVisitor.VisitStatus.CONTINUE;
    }
}
