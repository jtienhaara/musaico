package ebnf;

import java.io.Serializable;

public class Literal
    implements Node, Serializable
{
    private final String text;

    public Literal (
                    String text
                    )
    {
        this.text = text;
    }

    @Override
    public final NodeVisitor.VisitStatus accept (
                                                 NodeVisitor visitor,
                                                 VisitorState state
                                                 )
    {
        state.push ( this );
        try
        {
            System.err.println ( "!!! literal '" + this.text + "' visiting " + visitor );
            final NodeVisitor.VisitStatus status =
                visitor.visit ( this, state );
            System.err.println ( "!!! literal '" + this.text + "' visited -> " + status );
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

    public String text ()
    {
        return this.text;
    }

    @Override
    public String toString ()
    {
        final String nice_text =
            "\""
            + this.text
                  .replaceAll ( "\\\\", "\\\\\\\\" )
                  .replaceAll ( "\"", "\\\\\"" )
            + "\"";
        return nice_text;
    }
}
