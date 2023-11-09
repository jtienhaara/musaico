package table;


public class TableEntry
{
    public static final TableEntry [] EMPTY_ARRAY =
        new TableEntry [ 0 ];

    private final String name;
    private final Table arc;
    private final Table targetNode;

    public TableEntry (
            String name,
            Table arc,
            Table target_node
            )
    {
        this.name = name;
        this.arc = arc;
        this.targetNode = target_node;
    }

    public final String name ()
    {
        return this.name;
    }

    public final Table arc ()
    {
        return this.arc;
    }

    public final Table targetNode ()
    {
        return this.targetNode;
    }

    public final TableEntry replaceSelfWith (
            Table replacement
            )
    {
        if ( this.arc != Table.SELF
             && this.targetNode != Table.SELF )
        {
            return this;
        }
        else if ( this.arc == Table.SELF
                  && this.targetNode == Table.SELF )
        {
            return new TableEntry (
                           this.name,         // name
                           replacement,       // arc
                           replacement );     // target_node
        }
        else if ( this.arc == Table.SELF )
        {
            return new TableEntry (
                           this.name,         // name
                           replacement,       // arc
                           this.targetNode ); // target_node
        }
        else // this.targetNode == Table.SELF
        {
            return new TableEntry (
                           this.name,         // name
                           this.arc,          // arc
                           replacement );     // target_node
        }
    }

    public final boolean equals (
            Object object
            )
    {
        if ( object == null )
        {
            return false;
        }
        else if ( object == this )
        {
            return true;
        }
        else if ( object.getClass () != this.getClass () )
        {
            return false;
        }

        final TableEntry that = (TableEntry) object;
        if ( this.name == null )
        {
            if ( that.name != null )
            {
                return false;
            }
        }
        else if ( that.name == null )
        {
            return false;
        }
        else if ( ! this.name.equals ( that.name ) )
        {
            return false;
        }

        if ( this.arc == null )
        {
            if ( that.arc != null )
            {
                return false;
            }
        }
        else if ( that.arc == null )
        {
            return false;
        }
        else if ( ! this.arc.equals ( that.arc ) )
        {
            return false;
        }

        if ( this.targetNode == null )
        {
            if ( that.targetNode != null )
            {
                return false;
            }
        }
        else if ( that.targetNode == null )
        {
            return false;
        }
        else if ( ! this.targetNode.equals ( that.targetNode ) )
        {
            return false;
        }

        return true;
    }

    public final int hashCode ()
    {
        return this.name.hashCode ()
            + 17 * this.arc.hashCode ()
            + 31 * this.targetNode.hashCode ();
    }

    public final String toString ()
    {
        return this.name + " -- " + this.arc + " --> " + this.targetNode;
    }
}
