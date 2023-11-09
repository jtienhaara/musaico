package ebnf;

import java.util.ArrayList;
import java.util.List;

public class VisitorState
{
    private final List<Node> stack = new ArrayList<Node> ();

    public final List<Node> stack ()
    {
        return new ArrayList<Node> ( this.stack );
    }

    public final void push (
                            Node node
                            )
    {
        this.stack.add ( 0, node );
    }

    public final Node pop (
                           Node expected_node
                           )
        throws IllegalStateException
    {
        final Node actual_node = this.stack.get ( 0 );
        if ( actual_node != expected_node )
        {
            throw new IllegalStateException ( "Pop: Expected node "
                                              + expected_node
                                              + " on top of the stack,"
                                              + " but found "
                                              + actual_node );
        }
        this.stack.remove ( 0 );
        return actual_node;
    }

    public final Node peek ()
    {
        final Node node = this.stack.get ( 0 );
        return node;
    }
}
