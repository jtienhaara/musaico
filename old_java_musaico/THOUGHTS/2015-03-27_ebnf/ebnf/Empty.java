package ebnf;

import java.io.Serializable;

public class Empty
    implements Node, Serializable
{
    @Override
    public final NodeVisitor.VisitStatus accept (
                                                 NodeVisitor visitor,
                                                 VisitorState state
                                                 )
    {
        state.push ( this );
        try
        {
            final NodeVisitor.VisitStatus status =
                visitor.visit ( this, state );
            return status;
        }
        catch ( RuntimeException e )
        {
            System.err.println ( "!!! FAILED:" );
            e.printStackTrace ();
            return NodeVisitor.VisitStatus.ABORT;
        }
        finally
        {
            state.pop ( this );
        }
    }

    @Override
    public String toString ()
    {
        return "()";
    }
}
