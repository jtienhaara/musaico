package ebnf;

import java.io.Serializable;

public interface Node
{
    public abstract NodeVisitor.VisitStatus accept (
                                                    NodeVisitor visitor,
                                                    VisitorState state
                                                    );
}
