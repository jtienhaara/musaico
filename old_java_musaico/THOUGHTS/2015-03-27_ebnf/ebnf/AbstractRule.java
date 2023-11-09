package ebnf;

import java.io.Serializable;

public abstract class AbstractRule
    implements Node, Serializable
{
    public static final String NO_NAME = "NO_NAME";


    private final String name;
    private final Node [] children;
    private final String start;
    private final String separator;
    private final String end;

    public AbstractRule (
                         String name,
                         Node [] children,
                         String start,
                         String separator,
                         String end
                         )
    {
        this.name = name;

        this.children = new Node [ children.length ];
        System.arraycopy ( children, 0,
                           this.children, 0, children.length );

        this.start = start;
        this.separator = separator;
        this.end = end;
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
            System.err.println ( "!!! " + this.getClass ().getSimpleName () + " visiting main node " + this );
            NodeVisitor.VisitStatus status = visitor.visit ( this, state );
            System.err.println ( "!!! DONE " + this.getClass ().getSimpleName () + " visiting main node " + this );

            switch ( status )
            {
            case CONTINUE:
                break;

            case POP:
            case FAIL:
            case ABORT:
            default:
                return status;
            }

            System.err.println ( "!!! " + this.getClass ().getSimpleName () + " visiting children..." );
            status = this.visitChildren ( this.children, visitor, state );
            System.err.println ( "!!! DONE " + this.getClass ().getSimpleName () + " visiting children" );

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

    public final Node [] children ()
    {
        final Node [] children = new Node [ this.children.length ];
        System.arraycopy ( this.children, 0,
                           children, 0, this.children.length );

        return children;
    }

    public final String end ()
    {
        return this.end;
    }

    public final String name ()
    {
        return this.name;
    }

    public final String separator ()
    {
        return this.separator;
    }

    public final String start ()
    {
        return this.start;
    }

    @Override
    public String toString ()
    {
        final StringBuilder sbuf = new StringBuilder ();
        if ( this.name != AbstractRule.NO_NAME )
        {
            sbuf.append ( this.name );
            sbuf.append ( ": " );
        }

        sbuf.append ( this.start );
        boolean is_first = true;
        for ( Node node : this.children )
        {
            if ( is_first )
            {
                is_first = false;
            }
            else
            {
                sbuf.append ( this.separator );
            }

            final String output;
            if ( node instanceof AbstractRule )
            {
                final AbstractRule rule = (AbstractRule) node;
                final String name = rule.name ();
                if ( name != AbstractRule.NO_NAME )
                {
                    output = name;
                }
                else
                {
                    output = "" + rule;
                }
            }
            else
            {
                output = "" + node;
            }

            sbuf.append ( output );
        }

        sbuf.append ( this.end );

        return sbuf.toString ();
    }

    protected abstract NodeVisitor.VisitStatus visitChildren (
                                                              Node [] children,
                                                              NodeVisitor visitor,
                                                              VisitorState state
                                                              );
}
