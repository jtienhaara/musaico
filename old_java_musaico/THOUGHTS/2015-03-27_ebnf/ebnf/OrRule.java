package ebnf;

import java.io.Serializable;

public class OrRule
    extends AbstractRule
    implements Serializable
{
    public OrRule (
                   String name,
                   Node [] sequence
                   )
    {
        super ( name,
                sequence,
                "", // start
                "|", // separator
                "" ); // end
    }

    protected final NodeVisitor.VisitStatus visitChildren (
                                                           Node [] children,
                                                           NodeVisitor visitor,
                                                           VisitorState state
                                                           )
    {
        System.err.println ( "!!! OR RULE VISITING " + children.length + " CHILDREN" );
        for ( Node node : children )
        {
            System.err.println ( "!!! or child " + node + " visiting " + visitor );
            final NodeVisitor.VisitStatus status =
                node.accept ( visitor, state );

            System.err.println ( "!!! OR RULE CHILD '" + node + "' -> " + status );
            switch ( status )
            {
            case CONTINUE:
                return status;

            case POP:
            case FAIL:
                continue;

            case ABORT:
            default:
                return status;
            }
        }

        // Did not match any of the OR-ed children.
        return NodeVisitor.VisitStatus.FAIL;
    }
}
