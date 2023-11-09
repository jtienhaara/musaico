package table;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TableBuilder
{
    public static enum Type
    {
        ARC,
        TARGET_NODE;
    }

    public static class Child
    {
        public final String entryName;
        public final TableBuilder parent;
        public final TableBuilder.Type type;
        public final Map<TableBuilder.Type, Table> tables;

        public Child (
                String entry_name,
                TableBuilder parent,
                TableBuilder.Type type,
                Map<TableBuilder.Type, Table> tables
                )
        {
            this.entryName = entry_name;
            this.parent = parent;
            this.type = type;
            this.tables = tables;
        }
    }


    private final Serializable lock = new String ( "lock" );

    private final List<TableEntry> entries =
        new ArrayList<TableEntry> ();
    private final Map<String, Tag> tags =
        new HashMap<String, Tag> ();

    private final TableBuilder.Child child;


    public TableBuilder ()
    {
        this.child = null;
    }

    public TableBuilder (
            TableBuilder.Child child
            )
    {
        this.child = child;
    }

    public final TableBuilder arc (
            String arc_name
            )
    {
        final Map<TableBuilder.Type, Table> tables =
            new HashMap<TableBuilder.Type, Table> ();
        final TableBuilder target_node_builder =
            new TableBuilder (
                new TableBuilder.Child (
                    arc_name,
                    this,
                    TableBuilder.Type.TARGET_NODE,
                    tables ) );
        final TableBuilder arc_builder =
            new TableBuilder (
                new TableBuilder.Child (
                    arc_name,
                    target_node_builder,
                    TableBuilder.Type.ARC,
                    tables ) );
        return arc_builder;
    }

    public final Table build ()
    {
        final TableEntry [] entries;
        final Tag [] tags;
        synchronized ( this.lock )
        {
            final TableEntry [] entries_template =
                new TableEntry [ this.entries.size () ];
            entries = this.entries.toArray ( entries_template );

            final Tag [] tags_template =
                new Tag [ this.tags.size () ];
            tags = this.tags.values ().toArray ( tags_template );
        }

        final Table table = new Table (
                                    entries,
                                    tags
                                    );
        return table;
    }

    protected final void childPopped (
            TableBuilder.Child child
            )
    {
        if ( this.child != null )
        {
            // Not for us.
            return;
        }

        final String entry_name = child.entryName;

        final Table maybe_arc =
            child.tables.get ( TableBuilder.Type.ARC );
        final Table maybe_target_node =
            child.tables.get ( TableBuilder.Type.TARGET_NODE );

        final Table arc;
        if ( maybe_arc == null )
        {
            arc = Table.EMPTY;
        }
        else
        {
            arc = maybe_arc;
        }

        final Table target_node;
        if ( maybe_target_node == null )
        {
            target_node = Table.EMPTY;
        }
        else
        {
            target_node = maybe_target_node;
        }

        final TableEntry entry =
            new TableEntry (
                entry_name,
                arc,
                target_node );

        synchronized ( this.lock )
        {
            this.entries.add ( entry );
        }
    }

    public final TableBuilder pop ()
    {
        if ( this.child == null )
        {
            throw new IllegalStateException ( "ERROR not a child table: "
                                              + this );
        }

        final Table table = this.build ();
        this.child.tables.put ( this.child.type, table );
        this.child.parent.childPopped ( this.child );
        return this.child.parent;
    }

    public final TableBuilder tag (
            String name,
            Object [] values,
            String [] value_strings
            )
    {
        final Tag tag = new Tag ( name,
                                  values,
                                  value_strings );
        synchronized ( this.lock )
        {
            this.tags.put ( name, tag );
        }

        return this;
    }

    public final TableBuilder tag (
            String name,
            Object value,
            String value_string
            )
    {
        final Tag tag = new Tag ( name,
                                  value,
                                  value_string );
        synchronized ( this.lock )
        {
            this.tags.put ( name, tag );
        }

        return this;
    }

    public final TableBuilder tag (
            String name,
            String [] value_strings
            )
    {
        final Tag tag = new Tag ( name,
                                  value_strings,
                                  value_strings );
        synchronized ( this.lock )
        {
            this.tags.put ( name, tag );
        }

        return this;
    }

    public final TableBuilder tag (
            String name,
            String value_string
            )
    {
        final Tag tag = new Tag ( name,
                                  value_string,
                                  value_string );
        synchronized ( this.lock )
        {
            this.tags.put ( name, tag );
        }

        return this;
    }

    public final TableBuilder to ()
    {
        if ( this.child == null
             || this.child.type != TableBuilder.Type.ARC )
        {
            throw new IllegalStateException ( "TableBuilder is not an arc: "
                                              + this );
        }

        return this.pop ();
    }
}
