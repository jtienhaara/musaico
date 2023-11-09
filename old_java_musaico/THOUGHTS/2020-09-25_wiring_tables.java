import java.io.Serializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Stack;

import musaico.foundation.structure.StringRepresentation;

// Never allow mutability in a row.  We have to replace the row to mutate it. ;

public class Wiring
{
    public static abstract class AbstractEvent<HOST extends Object, REDO extends Object, UNDO extends Object>
    {
        public final HOST host;
        public final String action;
        public final REDO redoData;
        public final UNDO undoData;
        public AbstractEvent (
                HOST host,
                String action,
                REDO redo,
                UNDO undo
                )
        {
            this.host = host;
            this.action = action;
            this.redo = redo;
            this.undo = undo;
        }

        protected void assert (
                String redo_or_undo,
                String data_name,
                long expected_data,
                long actual_data
                )
            throws IllegalStateException
        {
            if ( expected_data != actual_data )
            {
                throw new IllegalStateException ( "ERROR during "
                                                  + redo_or_undo
                                                  + " of "
                                                  + this.action
                                                  + ": Expected "
                                                  + data_name
                                                  + " = "
                                                  + expected_data
                                                  + " but found: "
                                                  + actual_data );
            }
        }

        protected void assert (
                String redo_or_undo,
                String data_name,
                Object expected_data,
                Object actual_data
                )
            throws IllegalStateException
        {
            if ( expected_data == actual_data )
            {
                return;
            }
            else if ( expected_data != null )
            {
                if ( actual_data != null )
                {
                    if ( expected_data.equals ( actual_data ) )
                    {
                        return;
                    }
                }
            }

            throw new IllegalStateException ( "ERROR during "
                                              + redo_or_undo
                                              + " of "
                                              + this.action
                                              + ": Expected "
                                              + data_name
                                              + " = "
                                              + expected_data
                                              + " but found: "
                                              + actual_data );
        }

        public abstract void redo ();

        public abstract void undo ();
    }


    public static class Row
    {
        public final Wiring.Table<?> table;
        public final long id;
        public final long timestampNanoseconds;

        public <SPECIFIC_ROW extends Wiring.Row>
            Row (
                Wiring wiring,
                Class<SPECIFIC_ROW> row_class,
                long row_id
                )
        {
            this ( wiring,
                   wiring.forRowClass ( row_class ),
                   row_id );
        }

        public <SPECIFIC_ROW extends Wiring.Row>
            Row (
                Table<SPECIFIC_ROW> table,
                long row_id
                )
        {
            this.table = table;
            if ( row_id < 0L )
            {
                this.id = this.table.nextID ();
            }
            else
            {
                this.id = row_id;
            }
            this.timestampNanoseconds = System.nanoTime ();
        }

        @Override
        public final int hashCode ()
        {
            return (int) this.id;
        }

        @Override
        public final boolean equals (
                Object other
                )
        {
            if ( other == null )
            {
                return false;
            }
            else if ( other == this )
            {
                return true;
            }
            else if ( this.getClass () != other.getClass () )
            {
                return false;
            }

            final Wiring.Row that = (Wiring.Row) other;
            if ( this.id != that.id )
            {
                return false;
            }
            else if ( this.timestampNanoseconds != that.timestampNanoseconds )
            {
                return false;
            }
            else if ( ! this.equalsRow ( that ) )
            {
                return false;
            }

            return true;
        }

        // Row that guaranteed to be same class as this.
        protected abstract boolean equalsRow (
                Row that
                );
    }


    public static class Table<ROW extends Wiring.Row>
    {
        public final Wiring wiring;
        public final Class<ROW> rowClass;

        private final Serializable lock = new String ( "lock" );

        // Synchronize access to these fields on this.lock:
        private final LinkedHashMap<Long, ROW> table = new LinkedHashMap<Long, ROW> ();
        private long nextID = 0L;

        public Table (
                Wiring wiring,
                Class<ROW> row_class
                )
        {
            this.wiring = wiring;
            this.rowClass = row_class;
        }

        public static class NextIDEvent<EVENT_ROW extends Wiring.Row>
            extends Wiring.AbstractEvent<Wiring.Table<EVENT_ROW>, Long, Long>
        {
            public NextIDEvent (
                    Wiring.Table<EVENT_ROW> host,
                    long next_id
                    )
            {
                super ( host,
                        next_id + 1L,
                        next_id );
            }

            public final long nextID ()
            {
                return this.undoData;
            }

            @Override
            public final void redo ()
            {
                synchronized ( this.host.lock )
                {
                    this.assert ( "redo", "nextID",
                                  this.host.nextID,
                                  this.undoData );
                    this.host.nextID = this.redoData;
                }
            }

            @Override
            public final void redo ()
            {
                synchronized ( this.host.lock )
                {
                    this.assert ( "undo", "nextID",
                                  this.host.nextID,
                                  this.redoData );
                    this.host.nextID = this.undoData;
                }
            }
        }

        public static class ChangeRowsEvent<EVENT_ROW extends Wiring.Row>
            extends Wiring.AbstractEvent<Wiring.Table<EVENT_ROW>, LinkedHashMap<Long, EVENT_ROW>, LinkedHashMap<Long, EVENT_ROW>>
        {
            private final EVENT_ROW [] diffRows;

            @SuppressWarnings("unchecked") // Cast Object[] to EVENT_ROW[]
            public ChangeRowsEvent (
                    Wiring.Table<EVENT_ROW> host,
                    String action,
                    LinkedHashMap<Long, EVENT_ROW> new_rows,
                    LinkedHashMap<Long, EVENT_ROW> old_rows,
                    EVENT_ROW [] diff_rows
                    )
            {
                super ( host,
                        action,
                        new_rows,
                        old_rows );

                // SuppressWarnings("unchecked"):
                this.diffRows = (EVENT_ROW [])
                    Array.newInstance ( host.rowClass,
                                        diff_rows.length );
                System.arraycopy ( diff_rows, 0,
                                   this.diffRows, 0, diff_rows.length );
            }

            @SuppressWarnings("unchecked") // Cast Object[] to EVENT_ROW[]
            public final EVENT_ROW [] diffRows ()
            {
                // SuppressWarnings("unchecked"):
                final EVENT_ROW [] diff_rows = (EVENT_ROW [])
                    Array.newInstance ( host.rowClass,
                                        this.diffRows.length );
                System.arraycopy ( this.diffRows, 0,
                                   diff_rows, 0, this.diffRows.length );
                return diff_rows;
            }

            @Override
            public final void redo ()
            {
                synchronized ( this.host.lock )
                {
                    this.assert ( this, "rows",
                                  this.host.rows,
                                  this.undoData );
                    this.host.rows.clear ();
                    this.host.rows.putAll ( this.redoData );
                }
            }

            @Override
            public final void undo ()
            {
                synchronized ( this.host.lock )
                {
                    this.assert ( this, "rows",
                                  this.host.rows,
                                  this.redoData );
                    this.host.rows.clear ();
                    this.host.rows.putAll ( this.undoData );
                }
            }
        }

        public static class RowEvent
            extends Wiring.AbstractEvent<Table<EVENT_ROW>, Long, EVENT_ROW>
        {
            public RowEvent (
                    Table<EVENT_ROW> host,
                    long row_id,
                    EVENT_ROW row
                    )
            {
                super ( host,
                        "row",
                        row_id,
                        row );
            }

            public final EVENT_ROW row ()
            {
                return this.undoData;
            }

            @Override
            public final void redo ()
            {
                synchronized ( this.host.lock )
                {
                    this.assert ( this.host.rows.get ( this.redoData ), this.undoData );
                }
            }

            @Override
            public final void undo ()
            {
            }
        }

        public static class RowsEvent
            extends Wiring.AbstractEvent<Table<EVENT_ROW>, Long, EVENT_ROW>
        {
            public RowEvent (
                    Table<EVENT_ROW> host,
                    long [] row_ids,
                    EVENT_ROW [] rows
                    )
            {
                super ( host,
                        "row",
                        row_ids,
                        rows );
            }

            public final EVENT_ROW [] rows ()
            {
                return this.undoData;
            }

            @Override
            public final void redo ()
            {
                synchronized ( this.host.lock )
                {
                    for ( int r = 0; r < this.redoData.length; r ++ )
                    {
                        final long row_id = this.redoData [ r ];
                        final EVENT_ROW row = this.undoData [ r ];
                        this.assert ( this.host.rows.get ( row_id ), row );
                    }
                }
            }

            @Override
            public final void undo ()
            {
            }
        }


        public final long nextID ()
        {
            final Wiring.Table.NextIDEvent event;
            synchronized ( this.lock )
            {
                event = new Wiring.Table.NextIDEvent (
                            this,
                            this.nextID );
                event.redo ();
            }

            this.wiring.history ( event );

            return event.nextID ();
        }

        public final Table<ROW> add (
                ROW... rows
                )
        {
            final Wiring.Table.ChangeRowsEvent<ROW> event;
            synchronized ( this.lock )
            {
                final LinkedHashMap<Long, ROW> old_rows =
                    new LinkedHashMap<Long, ROW> ( this.rows )
                for ( ROW row : rows )
                {
                    if ( this.rows.containsKey ( row.id ) )
                    {
                        this.rows.clear ();
                        this.rows.putAll ( old_rows );
                        throw new IllegalArgumentException ( "ERROR Cannot add row "
                                                             + row.id
                                                             + " to "
                                                             + this
                                                             + ": row already exists"
                                                             + " while trying to add "
                                                             + StringRepresentation.of ( rows ) );
                    }

                    this.rows.put ( row.id, row );
                }

                final LinkedHashMap<Long, ROW> new_rows =
                    new LinkedHashMap<Long, ROW> ( this.rows )

                event = new Wiring.Table.ChangeRowsEvent<ROW> (
                            this,
                            "add",
                            new_rows,
                            old_rows,
                            rows );
            }

            this.wiring.history ( event );

            return this;
        }

        public final Table<ROW> addOrReplace (
                ROW... rows
                )
        {
            final Wiring.Table.ChangeRowsEvent<ROW> event;
            synchronized ( this.lock )
            {
                final LinkedHashMap<Long, ROW> old_rows =
                    new LinkedHashMap<Long, ROW> ( this.rows )
                for ( ROW row : rows )
                {
                    // Caller does not care whether the row
                    // already exists or not.
                    this.rows.put ( row.id, row );
                }

                final LinkedHashMap<Long, ROW> new_rows =
                    new LinkedHashMap<Long, ROW> ( this.rows )

                event = new Wiring.Table.ChangeRowsEvent<ROW> (
                            this,
                            "addOrReplace",
                            new_rows,
                            old_rows,
                            rows );
            }

            this.wiring.history ( event );

            return this;
        }

        @SuppressWarnings("unchecked") // Cast Object[] to ROW[]
        public final Table<ROW> remove (
                long ... row_ids
                )
        {
            // SuppressWarnings("unchecked"):
            final ROW [] removed_rows = (ROW [])
                Array.newInstance ( this.rowClass,
                                    row_ids.length );
            final Wiring.Table.ChangeRowsEvent<ROW> event;
            synchronized ( this.lock )
            {
                final LinkedHashMap<Long, ROW> old_rows =
                    new LinkedHashMap<Long, ROW> ( this.rows );
                for ( int r = 0; r < row_ids.length; r ++ )
                {
                    long row_id = row_ids [ r ];
                    final ROW removed_row =
                        this.rows.remove ( row_id );

                    if ( removed_row == null )
                    {
                        this.rows.clear ();
                        this.rows.putAll ( old_rows );
                        throw new IllegalArgumentException ( "ERROR No such row id "
                                                             + row__id
                                                             + " when trying to remove"
                                                             + " rows from "
                                                             + this
                                                             + ": "
                                                             + StringRepresentation.of ( row_ids ) );
                    }

                    removed_rows [ r ] = removed_row;
                }

                final LinkedHashMap<Long, ROW> new_rows =
                    new LinkedHashMap<Long, ROW> ( this.rows )

                event = new Wiring.Table.ChangeRowsEvent<ROW> (
                            this,
                            "remove",
                            new_rows,
                            old_rows,
                            removed_rows );
            }

            this.wiring.history ( event );

            return this;
        }

        public final Table<ROW> remove (
                ROW... rows
                )
        {
            final Wiring.Table.ChangeRowsEvent<ROW> event;
            synchronized ( this.lock )
            {
                final LinkedHashMap<Long, ROW> old_rows =
                    new LinkedHashMap<Long, ROW> ( this.rows )
                for ( ROW row : rows )
                {
                    this.rows.remove ( row.id );
                }

                final LinkedHashMap<Long, ROW> new_rows =
                    new LinkedHashMap<Long, ROW> ( this.rows )

                event = new Wiring.Table.ChangeRowsEvent<ROW> (
                            this,
                            "remove",
                            new_rows,
                            old_rows,
                            rows );
            }

            this.wiring.history ( event );

            return this;
        }

        public final Table<ROW> replace (
                ROW... rows
                )
        {
            final Wiring.Table.ChangeRowsEvent<ROW> event;
            synchronized ( this.lock )
            {
                final LinkedHashMap<Long, ROW> old_rows =
                    new LinkedHashMap<Long, ROW> ( this.rows )
                for ( ROW row : rows )
                {
                    if ( ! this.rows.containsKey ( row.id ) )
                    {
                        this.rows.clear ();
                        this.rows.putAll ( old_rows );
                        throw new IllegalArgumentException ( "ERROR Cannot replace row "
                                                             + row.id
                                                             + " to "
                                                             + this
                                                             + ": row does not exist"
                                                             + " while trying to replace "
                                                             + StringRepresentation.of ( rows ) );
                    }

                    this.rows.put ( row.id, row );
                }

                final LinkedHashMap<Long, ROW> new_rows =
                    new LinkedHashMap<Long, ROW> ( this.rows )

                event = new Wiring.Table.ChangeRowsEvent<ROW> (
                            this,
                            "replace",
                            new_rows,
                            old_rows,
                            rows );
            }

            this.wiring.history ( event );

            return this;
        }

        public final ROW row (
                long row_id
                )
        {
            final Wiring.Table.RowEvent<ROW> event;
            synchronized ( this.lock )
            {
                final ROW existing_row = this.rows.get ( row_id );
                if ( existing_row == null )
                {
                    // Possibilities: error, default/none value, etc.
                    event = new Wiring.Table.RowEvent<ROW> ( this,
                                                             row_id,
                                                             null );
                }
                else
                {
                    event = new Wiring.Table.RowEvent<ROW> ( this,
                                                             row_id,
                                                             existing_row );
                }
            }

            this.wiring.history ( event );

            return event.row ();
        }

        @SuppressWarnings("unchecked") // Cast ? [] to ROW [].
        public final ROW [] rows (
                long ... row_ids
                )
        {
            // SuppressWarnings("unchecked"):
            final ROW [] rows = (ROW [])
                Array.newInstance ( this.rowClass, row_ids.length );
            final Wiring.Table.RowsEvent<ROW> event;
            synchronized ( this.lock )
            {
                for ( int r = 0; r < row_ids.length; r ++ )
                {
                    final long row_id = row_ids [ r ];
                    final ROW existing_row = this.rows.get ( row_id );
                    if ( existing_row == null )
                    {
                        // Possibilities: error, default/none value, etc.
                        rows [ r ] = null;
                    }
                    else
                    {
                        rows [ r ] = existing_row;
                    }
                }
            }

            event = new Wiring.Table.RowsEvent<ROW> ( this,
                                                      row_ids,
                                                      rows );

            this.wiring.history ( event );

            return event.rows ();
        }
    }


    public static class Carrier
        extends Wiring.Row
    {
        public final long metadataID;
        public final long dataID;

        public Carrier (
                Wiring wiring,
                long id,
                long metadata_id,
                long data_id
                )
        {
            super ( Wiring.Carrier,
                    id );

            this.metadataID = metadata_id;
            this.dataID = data_id;
        }

        public Carrier (
                Wiring wiring,
                long id,
                Wiring.Metadata metadata,
                Wiring.Data data
                )
        {
            super ( Wiring.Carrier,
                    id );

            this.metadataID = metadata.id;
            this.dataID = data.id;
        }

        @Override
        protected boolean equalsRow (
                Wiring.Row row
                )
        {
            final Wiring.Carrier that = (Wiring.Carrier) row;
            if ( this.metadataID != that.metadataID )
            {
                return false;
            }
            else if ( this.dataID != that.dataID )
            {
                return false;
            }

            return true;
        }
    }

    public static class Tag
        implements Comparable<Tag>
    {
        public static final String name;
        public static final Object value;
        public Tag (
                String name,
                Object value
                )
        {
            this.name = name;
            this.value = value;
        }
        public Tag (
                String name
                )
        {
            this.name = name;
            this.value = Boolean.TRUE;
        }

        @Override
        public final int compareTo (
                Tag that
                )
        {
            if ( that == this )
            {
                return 0;
            }
            else if ( that == null )
            {
                return Integer.MIN_VALUE + 1;
            }
            else if ( this.name == null )
            {
                if ( that.name == null )
                {
                    return 0;
                }
                else
                {
                    return Integer.MAX_VALUE;
                }
            }
            else if ( that.name == null )
            {
                return Integer.MIN_VALUE + 1;
            }
            else if ( this.name.equals ( that.name ) )
            {
                return 0;
            }

            final int name_insensitive_comparison =
                this.name.compareToIgnoreCase ( that.name );
            if ( name_insensitive_comparison != 0 )
            {
                return name_insensitive_comparison;
            }

            final int name_comparison =
                this.name.compareTo ( that.name );
            return name_comparison;
        }

        @Override
        public final int hashCode ()
        {
            return this.name.hashCode ();
        }

        @Override
        public final boolean equals (
                Tag tag
                )
        {
            return this.name.equals ( tag.name );
        }

        @Override
        public final String toString ()
        {
            final StringBuilder sbuf = new StringBuilder ();
            sbuf.append ( "[ " );
            sbuf.append ( this.name );
            if ( this.value != true )
            {
                sbuf.append ( " = " );
                sbuf.append ( StringRepresentation.of ( this.value ) );
            }
            sbuf.append ( " ]" );
            return sbuf.toString ();
        }
    }

    public static class Metadata
        extends Wiring.Row
    {
        public final long namespaceID;
        public final String name;

        private final LinkedHashMap<String, Wiring.Tag> tags = new LinkedHashMap<String, Wiring.Tag> ();

        public Metadata (
                Wiring wiring,
                long id,
                long namespace_id,
                String name,
                Tag... tags
                )
        {
            super ( Wiring.Metadata.class,
                    id );

            this.namespaceID = namespace_id;
            this.name = name;
            for ( Tag tag : tags )
            {
                this.tags.put ( tag.name, tag );
            }
        }

        public Metadata (
                Wiring wiring,
                long id,
                Wiring.Namespace namespace,
                String name,
                Tag... tags
                )
        {
            super ( Wiring.Metadata.class,
                    id );

            this.namespaceID = namespace.id;
            this.name = name;
            for ( Tag tag : tags )
            {
                this.tags.put ( tag.name, tag );
            }
        }

        @Override
        protected boolean equalsRow (
                Wiring.Row row
                )
        {
            final Wiring.Metadata that = (Wiring.Metadata) row;
            if ( this.namespaceID != that.namespaceID )
            {
                return false;
            }

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

            if ( this.tags == null )
            {
                if ( that.tags != null )
                {
                    return false;
                }
            }
            else if ( that.tags == null )
            {
                return false;
            }
            else if ( this.tags.length != that.tags.length )
            {
                return false;
            }
            else if ( ! Arrays.equals ( this.tags, that.tags ) )
            {
                return false;
            }

            return true;
        }

        public Wiring.Metadata tag (
                Tag... add_tags
                )
        {
            final Tag [] old_tags = this.tags ();
            final Tag [] new_tags = new Tag [ old_tags.length + add_tags.length ];
            System.arraycopy ( old_tags, 0,
                               new_tags, 0, old_tags.length );
            System.arraycopy ( add_tags, 0,
                               new_tags, old_tags.length, add_tags.length );
            return new Wiring.Metadata ( this.id,
                                               this.namespaceID,
                                               this.name,
                                               new_tags );
        }

        public Wiring.Metadata tag (
                String... add_tags
                )
        {
            final Tag [] old_tags = this.tags ();
            final Tag [] new_tags = new Tag [ old_tags.length + add_tags.length ];
            System.arraycopy ( old_tags, 0,
                               new_tags, 0, old_tags.length );
            int nt = old_tags.length;
            for ( String add_tag : add_tags )
            {
                new_tags [ nt ] = new Tag ( add_tag );
                nt ++;
            }
            return new Wiring.Metadata ( this.id,
                                               this.namespaceID,
                                               this.name,
                                               new_tags );
        }

        public Wiring.Metadata untag (
                Tag... remove_tags
                )
        {
            final LinkedHashMap<String, Tag> new_tags_map =
                new LinkedHashMap<String, Tag> ( this.tags );
            for ( Tag remove_tag : remove_tags )
            {
                new_tags_map.remove ( remove_tag.name );
            }
            final Tag [] template = new Tag [ new_tags_map.size () ];
            final Tag [] new_tags = (Tag [])
                new_tags_map.value ().toArray ( template );
            return new_tags;
        }

        public Wiring.Metadata untag (
                String... remove_tag_names
                )
        {
            final LinkedHashMap<String, Tag> new_tags_map =
                new LinkedHashMap<String, Tag> ( this.tags );
            for ( String remove_tag_name : remove_tag_names )
            {
                new_tags_map.remove ( remove_tag_name );
            }
            final Tag [] template = new Tag [ new_tags_map.size () ];
            final Tag [] new_tags = (Tag [])
                new_tags_map.value ().toArray ( template );
            return new_tags;
        }

        public Wiring.Tag [] tags ()
        {
            final Wiring.Tag [] template =
                new Tag [ this.tags.size () ];
            final Wiring.Tag [] tags =
                this.tags.values ().toArray ( template );
            return tags;
        }
    }

    public static class Namespace
        extends Wiring.Row
    {
        public final String name;
        public final long metadataID;

        public Namespace (
                Wiring wiring,
                long id,
                String name,
                long metadata_id
                )
        {
            super ( wiring,
                    id );

            this.name = name;
            this.metadataID = metadata_id;
        }

        public Namespace (
                Wiring wiring,
                long id,
                String name,
                Wiring.Metadata metadata
                )
        {
            super ( wiring,
                    id );

            this.name = name;
            this.metadataID = metadata.id;
        }

        @Override
        protected boolean equalsRow (
                Wiring.Row row
                )
        {
            final Wiring.Namespace that = (Wiring.Namespace) row;

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

            if ( this.metadataID != that.metadataID )
            {
                return false;
            }

            return true;
        }
    }

    public static class Data
        extends Wiring.Row
    {
        public final long typeID;
        public final Object data;

        public Data (
                Wiring wiring,
                long id,
                long type_id,
                Object data
                )
        {
            super ( wiring,
                    id );

            this.typeID = type_id;
            this.data = data;
        }

        public Data (
                Wiring wiring,
                long id,
                Wiring.Type type,
                Object data
                )
        {
            super ( wiring,
                    id );

            this.typeID = type.id;
            this.data = data;
        }

        @Override
        protected boolean equalsRow (
                Wiring.Row row
                )
        {
            final Wiring.Data that = (Wiring.Data) row;
            if ( this.typeID != that.typeID )
            {
                return false;
            }

            if ( this.data == null )
            {
                if ( that.data != null )
                {
                    return false;
                }
            }
            else if ( that.data == null )
            {
                return false;
            }
            else if ( this.data.isArray () )
            {
                if ( ! that.data.isArray () )
                {
                    return false;
                }

                // SuppressWarnings("unchecked"):
                final Object [] this_array = (Object []) this.data;
                final Object [] that_array = (Object []) that.data;
                if ( ! Arrays.equals ( this_array, that_array ) )
                {
                    return false;
                }
            }
            else if ( ! this.data.equals ( that.data ) )
            {
                return false;
            }

            return true;
        }
    }

    public static class Type
        extends Wiring.Row
    {
        public final long metadataID;
        public final Class<?> instanceClass;
        public final Filter<? extends Object> instanceFilter;

        public Type (
                Wiring wiring,
                long id,
                long metadata_id,
                Class<?> instance_class,
                Filter<? extends Object> instance_filter
                )
        {
            super ( wiring,
                    id );

            this.metadataID = metadata_id;
            this.instanceClass = instance_class;
            this.instance_filter = instance_filter;
        }

        public Type (
                Wiring wiring,
                long id,
                Wiring.Metadata metadata,
                Class<?> instance_class,
                Filter<? extends Object> instance_filter
                )
        {
            super ( wiring,
                    id );

            this.metadataID = metadata.id;
            this.instanceClass = instance_class;
            this.instance_filter = instance_filter;
        }

        @Override
        protected boolean equalsRow (
                Wiring.Row row
                )
        {
            final Wiring.Type that = (Wiring.Type) row;
            if ( this.metadataID != that.metadataID )
            {
                return false;
            }

            if ( this.instanceClass == null )
            {
                if ( that.instanceClass != null )
                {
                    return false;
                }
            }
            else if ( that.instanceClass == null )
            {
                return false;
            }
            else if ( ! this.instanceClass.equals ( that.instanceClass ) )
            {
                return false;
            }

            if ( this.instanceFilter == null )
            {
                if ( that.instanceFilter != null )
                {
                    return false;
                }
            }
            else if ( that.instanceFilter == null )
            {
                return false;
            }
            else if ( ! this.instanceFilter.equals ( that.instanceFilter ) )
            {
                return false;
            }

            return true;
        }
    }

    public static class CarrierState
        extends Wiring.Row
    {
        public final long carrierID;
        public final long stateID;

        public CarrierState (
                Wiring wiring,
                long id,
                long carrier_id,
                long state_id
                )
        {
            super ( wiring,
                    id );

            this.carrierID = carrier_id;
            this.stateID = state_id;
        }

        public CarrierState (
                Wiring wiring,
                long id,
                Wiring.Carrier carrier,
                Wiring.State state
                )
        {
            super ( wiring,
                    id );

            this.carrierID = carrier.id;
            this.stateID = state.id;
        }

        @Override
        protected boolean equalsRow (
                Wiring.Row row
                )
        {
            final Wiring.CarrierState that = (Wiring.CarrierState) row;
            if ( this.carrierID != that.carrierID )
            {
                return false;
            }
            else if ( this.stateID != that.stateID )
            {
                return false;
            }

            return true;
        }
    }

    public static class Wire
        extends Wiring.Row
    {
        public final long metadataID;
        public final long wireBundleID;
        public final long chipLegID;
        public final long wireQueueID;

        public Wire (
                Wiring wiring,
                long id,
                long metadata_id,
                long wire_bundle_id,
                long chip_leg_id,
                long wire_queue_id
                )
        {
            super ( wiring,
                    id );

            this.metadataID = metadata_id;
            this.wireBundleID = wire_bundle_id;
            this.chipLegID = chip_leg_id;
            this.wireQueueID = wire_queue_id;
        }

        public Wire (
                Wiring wiring,
                long id,
                Wiring.Metadata metadata,
                Wiring.WireBundle wire_bundle,
                Wiring.ChipLeg chip_leg,
                Wiring.WireQueue wire_queue
                )
        {
            super ( wiring,
                    id );

            this.metadataID = metadata.id;
            this.wireBundleID = wire_bundle.id;
            this.chipLegID = chip_leg.id;
            this.wireQueueID = wire_queue.id;
        }

        @Override
        protected boolean equalsRow (
                Wiring.Row row
                )
        {
            final Wiring.Wire that = (Wiring.Wire) row;
            if ( this.metadataID != that.metadataID )
            {
                return false;
            }
            else if ( this.wireBundleID != that.wireBundleID )
            {
                return false;
            }
            else if ( this.chipLegID != that.chipLegID )
            {
                return false;
            }
            else if ( this.wireQueueID != that.wireQueueID )
            {
                return false;
            }

            return true;
        }
    }

    public static class WireBundle
        extends Wiring.Row
    {
        public final long metadataID;
        public final long typeID;

        public WireBundle (
                Wiring wiring,
                long id,
                long metadata_id,
                long type_id
                )
        {
            super ( wiring,
                    id );

            this.metadataID = metadata_id;
            this.typeID = type_id;
        }

        public WireBundle (
                Wiring wiring,
                long id,
                Wiring.Metadata metadata,
                Wiring.Type type
                )
        {
            super ( wiring,
                    id );

            this.metadataID = metadata.id;
            this.typeID = type.id;
        }

        @Override
        protected boolean equalsRow (
                Wiring.Row row
                )
        {
            final Wiring.WireBundle that = (Wiring.WireBundle) row;
            if ( this.metadataID != that.metadataID )
            {
                return false;
            }
            else if ( this.typeID != that.typeID )
            {
                return false;
            }

            return true;
        }
    }

    public static class WireQueue
        extends Wiring.Row
    {
        public final long metadataID;

        private final long [] carrierIDs;

        public WireQueue (
                Wiring wiring,
                long id,
                long metadata_id,
                long ... carrier_ids
                )
        {
            super ( wiring,
                    id );

            this.metadataID = metadata_id;

            this.carrierIDs = new long [ carrier_ids.length ];
            System.arraycopy ( carrier_ids, 0,
                               this.carrierIDs, 0 , carrier_ids.length );
        }

        public WireQueue (
                Wiring wiring,
                long id,
                Wiring.Metadata metadata,
                Wiring.Carrier ... carriers
                )
        {
            super ( wiring,
                    wire.id );

            this.metadataID = metadata_id;

            this.carrierIDs = new long [ carriers.length ];
            for ( int c = 0; c < carriers.length; c ++ )
            {
                this.carrierIDs [ c ] = carriers [ c ].id;
            }
        }

        @Override
        protected boolean equalsRow (
                Wiring.Row row
                )
        {
            final Wiring.WireQueue that = (Wiring.WireQueue) row;
            if ( this.metadataID != that.metadataID )
            {
                return false;
            }

            if ( this.carrierIDs == null )
            {
                if ( that.carrierIDs != null )
                {
                    return false;
                }
            }
            else if ( that.carrierIDs == null )
            {
                return false;
            }
            else if ( ! Arrays.equals ( this.carrierIDs, that.carrierIDs ) )
            {
                return false;
            }

            return true;
        }

        public final long length ()
        {
            return (long) this.carrierIDs.length;
        }

        public final long [] carrierIDs ()
        {
            final long [] carrier_ids = new long [ this.carrierIDs.length ];
            System.arraycopy ( this.carrierIDs, 0,
                               carrier_ids, 0, this.carrierIDs.length );
            return carrier_ids;
        }

        public final Wiring.Carrier [] carriers ()
        {
            final Table<Wiring.Carrier> table = this.wiring.forRowClass ( Wiring.Carrier );
            final Carrier [] carriers = table.rows ( this.carrierIDs );
            return carriers;
        }

        public final long firstID ()
        {
            return this.carrierIDs [ 0 ];
        }

        public final Carrier first ()
        {
            final long first_id = this.firstID ();
            final Table<Wiring.Carrier> table = this.wiring.forRowClass ( Wiring.Carrier );
            final Carrier first = table.row ( first_id );
            return first;
        }

        public final long lastID ()
        {
            return this.carrierIDs [ this.carrierIDs.length - 1 ];
        }

        public final Carrier last ()
        {
            final long last_id = this.lastID ();
            final Table<Wiring.Carrier> table = this.wiring.forRowClass ( Wiring.Carrier );
            final Carrier last = table.row ( last_id );
            return last;
        }

        public final Wiring.WireQueue add (
                long ... carrier_ids
                )
        {
            final long [] mew_carrier_ids =
                new long [ this.carrierIDs.length + carrier_ids.length ];
            System.arraycopy ( this.carrierIDs, 0,
                               new_carrier_ids, 0, this.carrierIDs.length );
            System.arraycopy ( carrier_ids, 0,
                               new_carrier_ids, this.carrierIDs.length, carrier_ids.length );
            final Wiring.WireQueue new_wire_queue =
                new Wiring.WireQueue ( this.wiring,
                                       this.id,
                                       this.metadataID,
                                       new_carrier_ids );
            return new_wire_queue;
        }

        public final Wiring.WireQueue add (
                Carrier ... carriers
                )
        {
            final long [] mew_carrier_ids =
                new long [ this.carrierIDs.length + carriers.length ];
            System.arraycopy ( this.carrierIDs, 0,
                               new_carrier_ids, 0, this.carrierIDs.length );
            for ( int c = 0; c < carriers.length; c ++ )
            {
                new_carrier_ids [ this.carrierIDs.length + c ] =
                    carriers [ c ].id;
            }
            final Wiring.WireQueue new_wire_queue =
                new Wiring.WireQueue ( this.wiring,
                                       this.id,
                                       this.metadataID,
                                       new_carrier_ids );
            return new_wire_queue;
        }

        /* !!!
        public final Wiring.WireQueue insert (
                Comparator<Wiring.Carrier> order,
                long ... carrier_ids
                )
        {
            final Wiring.Carrier [] carriers =
                this.wiring.forRowClass ( Wiring.Carrier.class )
                    .rows ( carrier_ids );
            return this.insert ( order, carriers );
        }

        public final Wiring.WireQueue insert (
                Comparator<Wiring.Carrier> order,
                Wiring.Carrier ... carriers
                )
        {
            final Wiring.Carrier [] my_carriers =
                this.carriers ();

            for ( Wiring.Carrier carrier : carriers )
            {
                final int insert_position =
                    Arrays.binarySearch ( my_carriers, carrier, order );
                
            !!!;
        }
        !!! */

        public final Wiring.WireQueue remove (
                long ... carrier_ids
                )
        {
            if ( carrier_ids.length > this.carrierIDs.length )
            {
                throw new IllegalArgumentException ( "ERROR cannot remove "
                                                     + carrier_ids.length
                                                     + " carriers from "
                                                     + this
                                                     + " with only "
                                                     + this.carrierIDs.length
                                                     + " carriers in the queue: "
                                                     + StringRepresentation.of ( carrier_ids ) );
            }

            final LinkedHashSet<Long> new_carrier_ids_set =
                new LinkedHashSet<Long> ();
            for ( long old_carrier_id : this.carrierIDs )
            {
                new_carrier_ids_set.add ( old_carrier_id );
            }
            int index = 0;
            for ( long remove_carrier_id : carrier_ids )
            {
                if ( ! new_carrier_ids_set.remove ( remove_carrier_id ) )
                {
                    throw new IllegalArgumentException ( "ERROR Cannot remove"
                                                         + " Carrier index "
                                                         + index
                                                         + ": Carrier ID "
                                                         + remove_carrier_id
                                                         + " from "
                                                         + this
                                                         + " ( "
                                                         + StringRepresentation.of ( this.carrierIDs )
                                                         + " ): "
                                                         + StringRepresentation.of ( carrier_ids ) );
                }
                index ++;
            }

            final long [] new_carrier_ids =
                new long [ new_carrier_ids_set.size () ];
            int c = 0;
            for ( long new_carrier_id : new_carrier_ids_set )
            {
                new_carrier_ids [ c ] = new_carrier_id;
                c ++;
            }

            final Wiring.WireQueue new_wire_queue =
                new Wiring.WireQueue ( this.wiring,
                                       this.id,
                                       this.metadataID,
                                       new_carrier_ids );
            return new_wire_queue;
        }

        public final Wiring.WireQueue removeFirst (
                long num_carriers
                )
        {
            if ( num_carriers > (long) this.carrierIDs.length )
            {
                throw new IllegalArgumentException ( "ERROR cannot remove first "
                                                     + num_carriers
                                                     + " carriers from "
                                                     + this
                                                     + " with only "
                                                     + this.carrierIDs.length
                                                     + " carriers in the queue: "
                                                     + StringRepresentation.of ( carrier_ids ) );
            }

            final int new_num_carriers =
                this.carrierIDs.length - (int) num_carriers;
            final long [] new_carrier_ids =
                new long [ new_num_carriers ];
            System.arraycopy ( this.carrierIDs, (int) num_carriers,
                               new_carrier_ids, 0, new_num_carriers );
            int c = 0;
            for ( long new_carrier_id : new_carrier_ids_set )
            {
                new_carrier_ids [ c ] = new_carrier_id;
                c ++;
            }

            final Wiring.WireQueue new_wire_queue =
                new Wiring.WireQueue ( this.wiring,
                                       this.id,
                                       this.metadataID,
                                       new_carrier_ids );
            return new_wire_queue;
        }

        /* !!!
        public final Wiring.WireQueue sort (
                Comparator<Wiring.Carrier> order
                )
        {
            !!!;
        }
        !!! */
    }

    public static class ChipLeg
        extends Wiring.Row
    {
        public final long metadataID;
        public final long chipID;
        public final long schematicConnectionID;

        public ChipLeg (
                Wiring wiring,
                long id,
                long metadata_id,
                long chip_id,
                long schematic_connection_id
                )
        {
            super ( wiring,
                    id );

            this.metadataID = metadata_id;
            this.chipID = chip_id;
            this.schematicConnectionID = schematic_connection_id;
        }

        public ChipLeg (
                Wiring wiring,
                long id,
                Wiring.Metadata metadata,
                Wiring.Chip chip,
                Wiring.SchematicConnection schematic_connection
                )
        {
            super ( wiring,
                    id );

            this.metadataID = metadata.id;
            this.chipID = chip.id;
            this.schematicConnectionID = schematic_connection.id;
        }

        @Override
        protected boolean equalsRow (
                Wiring.Row row
                )
        {
            final Wiring.ChipLeg that = (Wiring.ChipLeg) row;
            if ( this.metadataID != that.metadataID )
            {
                return false;
            }
            else if ( this.chipID != that.chipID )
            {
                return false;
            }
            else if ( this.schematicConnectionID != that.schematicConnectionID )
            {
                return false;
            }

            return true;
        }

        public long typeID ()
        {
            final Table<Wiring.SchematicConnection> table =
                this.wiring.forRowClass ( Wiring.SchematicConnection );
            final Wiring.SchematicConnection schematic_connection =
                table.row ( this.schematicConnectionID );
            return schematic_connection.typeID;
        }
    }

    public static class Chip
        extends Wiring.Row
    {
        public final long metadataID;
        public final long schematicID;
        public final long configurationDataID;
        public final long dataID;

        public Chip (
                Wiring wiring,
                long id,
                long metadata_id,
                long schematic_id,
                long configuration_data_id,
                long data_id
                )
        {
            super ( wiring,
                    id );

            this.metadataID = metadata_id;
            this.schematicID = schematic_id;
            this.configurationDataID = configuration_data_id;
            this.dataID = data_id;
        }

        public Chip (
                Wiring wiring,
                long id,
                Wiring.Metadata metadata,
                Wiring.Schematic schematic,
                Wiring.Data configuration_data,
                Wiring.Data data
                )
        {
            super ( wiring,
                    id );

            this.metadataID = metadata.id;
            this.schematicID = schematic.id;
            this.configurationDataID = configuration_data.id;
            this.dataID = data.id;
        }

        @Override
        protected boolean equalsRow (
                Wiring.Row row
                )
        {
            final Wiring.Chip that = (Wiring.Chip) row;
            if ( this.metadataID != that.metadataID )
            {
                return false;
            }
            else if ( this.schematicID != that.schematicID )
            {
                return false;
            }
            else if ( this.configurationDataID != that.configurationDataID )
            {
                return false;
            }
            else if ( this.dataID != that.dataID )
            {
                return false;
            }

            return true;
        }
    }

    public static interface Circuit
    {
        public Chip configure (
                Wiring.Metadata metadata,
                Wiring.Data configuration
                );
        public Circuit start (
                Wiring.Chip instance
                );
        public Circuit stop (
                Wiring.Chip instance
                );
        public Circuit flow (
                Chip instance
                );
    }

    public static class Schematic
        extends Wiring.Row
    {
        public final long metadataID;
        public final Wiring.Circuit circuit;

        public Schematic (
                Wiring wiring,
                long id,
                long metadata_id,
                Wiring.Circuit circuit
                )
        {
            super ( wiring,
                    id );

            this.metadataID = metadata_id;
            this.circuit = circuit;
        }

        public Schematic (
                Wiring wiring,
                long id,
                Wiring.Metadata metadata,
                Wiring.Circuit circuit
                )
        {
            super ( wiring,
                    id );

            this.metadataID = metadata.id;
            this.circuit = circuit;
        }

        @Override
        protected boolean equalsRow (
                Wiring.Row row
                )
        {
            final Wiring.Schematic that = (Wiring.Schematic) row;
            if ( this.metadataID != that.metadataID )
            {
                return false;
            }

            if ( this.circuit == null )
            {
                if ( that.circuit != null )
                {
                    return false;
                }
            }
            else if ( that.circuit == null )
            {
                return false;
            }
            else if ( ! this.circuit.equals ( that.circuit ) )
            {
                return false;
            }

            return true;
        }
    }

    public static class SchematicConnection
        extends Wiring.Row
    {
        public final long metadataID;
        public final long schematicID;
        public final long typeID;

        public SchematicConnection (
                Wiring wiring,
                long id,
                long metadata_id,
                long schematic_id,
                long type_id
                )
        {
            super ( wiring,
                    id );

            this.metdataID = metadata_id;
            this.schematicID = schematic_id;
            this.typeID = type_id;
        }

        public SchematicConnection (
                Wiring wiring,
                long id,
                Wiring.Metadata metadata,
                Wiring.Schematic schematic,
                Wiring.Type type
                )
        {
            super ( wiring,
                    id );

            this.metdataID = metadata.id;
            this.schematicID = schematic.id;
            this.typeID = type.id;
        }

        @Override
        protected boolean equalsRow (
                Wiring.Row row
                )
        {
            final Wiring.SchematicConnection that = (Wiring.SchematicConnection) row;
            if ( this.metadataID != that.metadataID )
            {
                return false;
            }
            else if ( this.schematicID != that.schematicID )
            {
                return false;
            }
            else if ( this.typeID != that.typeID )
            {
                return false;
            }

            return true;
        }
    }

    public static class State
        extends Wiring.Row
    {
        public final long metadataID;

        public State (
                Wiring wiring,
                long id,
                long metadata_id
                )
        {
            super ( wiring,
                    id );

            this.metadataID = metadata_id;
        }

        public State (
                Wiring wiring,
                long id,
                Wiring.Metadata metadata
                )
        {
            super ( wiring,
                    id );

            this.metadataID = metadata.id;
        }

        @Override
        protected boolean equalsRow (
                Wiring.Row row
                )
        {
            final Wiring.State that = (Wiring.State) row;
            if ( this.metadataID != that.metadataID )
            {
                return false;
            }

            return true;
        }
    }


    public static class ChangeTablesEvent
        extends Wiring.AbstractEvent<Wiring, LinkedHashMap<Class<?>, Table<?>>, LinkedHashMap<Class<?>, Table<?>>>
    {
        public ChangeTablesEvent (
                Wiring host,
                String action,
                LinkedHashMap<Class<?>, Table<?>> new_tables,
                LinkedHashMap<Class<?>, Table<?>> old_tables
                )
        {
            super ( host,
                    action,
                    new_tables,
                    old_tables );
        }

        @Override
        public final void redo ()
        {
            synchronized ( this.host.lock )
            {
                this.assert ( this, "tables",
                              this.host.tables,
                              this.undoData );
                this.host.tables.clear ();
                this.host.tables.putAll ( this.redoData );
            }
        }

        @Override
        public final void undo ()
        {
            synchronized ( this.host.lock )
            {
                this.assert ( this, "tables",
                              this.host.tables,
                              this.redoData );
                this.host.tables.clear ();
                this.host.tables.putAll ( this.undoData );
            }
        }
    }

    public static class ForRowClassEvent<ROW extends Wiring.Row>
        extends ChangeTablesEvent
    {
        private final Class<ROW> rowClass;
        private final Table<ROW> table;

        public ForRowClassEvent (
                Wiring host,
                Class<ROW> row_class,
                Table<ROW> table,
                LinkedHashMap<Class<?>, Table<?>> new_tables,
                LinkedHashMap<Class<?>, Table<?>> old_tables
                )
        {
            super ( host,
                    "forRowClass",
                    new_tables,
                    old_tables );

            this.rowClass = row_class;
            this.table = table;
        }

        public final Class<ROW> rowClass ()
        {
            return this.rowClass;
        }

        public final Table<ROW> table ()
        {
            return this.table;
        }
    }


    private final Serializable lock = new String ( "lock" );

    // Synchronize these fields on lock:
    private final LinkedHashMap<Class<?>, Table<?>> tables = new LinkedHashMap<Class<?>, Table<?>> ();
    private final Stack<Wiring.AbstractEvent<?, ?, ?>> history = new Stack<Wiring.AbstractEvent<?, ?, ?>> ();

    @SuppressWarnings("unchecked") // Cast Table<?> -> Table<ROW>
    public final <ROW extends Wiring.Row>
        Wiring.Table<ROW> forRowClass (
            Class<ROW> row_class
            )
    {
        final ForRowClassEvent<ROW> event;
        synchronized ( this.lock )
        {
            final LinkedHashMap<Class<?>, Table<?>> old_tables =
                new LinkedHashMap<Class<?>, Table<?>> ( this.tables );

            final LinkedHashMap<Class<?>, Table<?>> new_tables;

            // SuppressWarnings("unchecked"):
            final Wiring.Table<ROW> existing_table =
                this.tables.get ( row_class );
            if ( existing_table == null )
            {
                final Table<ROW> table = new Wiring.Table<ROW> ( row_class );
                this.tables.put ( row_class, table );

                final LinkedHashMap<Class<?>, Table<?>> new_tables =
                    new LinkedHashMap<Class<?>, Table<?>> ( this.tables );

                event = new ForRowClassEvent<ROW> ( this,
                                                    row_class,
                                                    table,
                                                    old_tables,
                                                    new_tables );
            }
            else
            {
                event = new ForRowClassEvent<ROW> ( this,
                                                    row_class,
                                                    existing_table,
                                                    old_tables,
                                                    old_tables );
            }

            this.history ( event );
        }

        return event.table ();
    }
}
