package ebnf;

import java.io.Serializable;

public class ForwardReference<NODE extends Node>
    implements Node, Serializable
{
    private final Serializable lock = new String ();
    private Node resolved = null;

    @Override
    public final NodeVisitor.VisitStatus accept (
                                                 NodeVisitor visitor,
                                                 VisitorState state
                                                 )
        throws IllegalStateException
    {
        state.push ( this );
        try
        {
            final NodeVisitor.VisitStatus status;
            synchronized ( this.lock )
            {
                if ( this.resolved == null )
                {
                    throw new IllegalStateException ( "ForwardRefernce "
                                                      + this.toString ()
                                                      + " not yet resolved" );
                }

                status = this.resolved.accept ( visitor, state );
            }

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

    public NODE resolve (
                         NODE node
                         )
        throws IllegalStateException
    {
        synchronized ( this.lock )
        {
            if ( this.resolved != null )
            {
                throw new IllegalStateException ( "Already resolved to "
                                                  + this.resolved
                                                  + " before trying to"
                                                  + " resolve to "
                                                  + node );
            }

            this.resolved = node;
        }

        return node;
    }

    @Override
    public String toString ()
    {
        synchronized ( this.lock )
        {
            if ( this.resolved != null )
            {
                if ( this.resolved instanceof AbstractRule )
                {
                    AbstractRule rule = (AbstractRule) this.resolved;
                    return rule.name ();
                }
                else
                {
                    return this.resolved.toString ();
                }
            }
            else
            {
                return super.toString ();
            }
        }
    }
}
