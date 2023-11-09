package ebnf;

import java.io.Serializable;

public class ZeroOrMoreRule
    extends AbstractRule
    implements Serializable
{
    public ZeroOrMoreRule (
                           String name,
                           Node zero_or_more
                           )
    {
        super ( name,
                new Node [] { zero_or_more }, // sequence
                "{", // start
                "", // separator
                "}" ); // end
    }

    protected final NodeVisitor.VisitStatus visitChildren (
                                                           Node [] children,
                                                           NodeVisitor visitor,
                                                           VisitorState state
                                                           )
    {
        for ( int num_matches = 0; ; num_matches ++ )
        {
            System.err.println ( "!!! VISITING " + children.length + " CHILDREN..." );
            for ( Node node : children )
            {
                final NodeVisitor.VisitStatus status =
                    node.accept ( visitor, state );

                System.err.println ( "!!! CHILD " + node.getClass().getSimpleName () + " '" + node + "' : " + status + " ( # " + num_matches + " )" );
                switch ( status )
                {
                case CONTINUE:
                    break; // Carry on the num_matches loop.

                case POP:
                case FAIL:
                    return NodeVisitor.VisitStatus.POP;

                case ABORT:
                default:
                    return status;
                }
            }
        }
    }
}
