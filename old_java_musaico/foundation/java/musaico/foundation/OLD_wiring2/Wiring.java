package musaico.foundation.wiring;

import java.io.Serializable;

import java.lang.reflect.Array;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import java.util.regex.Pattern;


import musaico.foundation.filter.DiscardAll;
import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;
import musaico.foundation.filter.KeepAll;

import musaico.foundation.filter.composite.And;

import musaico.foundation.filter.container.ContainerFilter;
import musaico.foundation.filter.container.LengthFilter;

import musaico.foundation.filter.elements.ElementsFilter;
import musaico.foundation.filter.elements.IncludesOnly;
import musaico.foundation.filter.elements.IncludesOnlyClasses;

import musaico.foundation.filter.equality.Equal;
import musaico.foundation.filter.equality.EqualTo;

import musaico.foundation.structure.StringRepresentation;


// Never allow mutability in a row.  We have to replace the row to mutate it. ;

/**
 * <p>
 * !!!
 * </p>
 *
 *
 * <br> </br>
 * <br> </br>
 *
 * <hr> </hr>
 *
 * <br> </br>
 * <br> </br>
 *
 *
 * <p>
 * For copyright and licensing information refer to:
 * </p>
 *
 * @see musaico.foundation.wiring.MODULE#COPYRIGHT
 * @see musaico.foundation.wiring.MODULE#LICENSE
 */
public class Wiring
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    public static enum DoOrUndo
    {
        DO,
        UNDO;
    }

    public static abstract class AbstractEvent<HOST extends Object, REDO extends Object, UNDO extends Object>
    {
        public final Wiring wiring;
        public final HOST host; // A single Table, or a whole Wiring database, etc.
        public final String action;
        public final REDO redoData;
        public final UNDO undoData;
        public AbstractEvent (
                Wiring wiring,
                HOST host,
                String action,
                REDO redo_data,
                UNDO undo_data
                )
        {
            this.wiring = wiring;
            this.host = host;
            this.action = action;
            this.redoData = redo_data;
            this.undoData = undo_data;
        }

        protected void assertEquals (
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

        protected void assertEquals (
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


    public static abstract class Column<COLUMN_ROW extends Wiring.Row, VALUE extends Object>
    {
        public final Wiring wiring;
        public final String name;

        // For initializing Types that would otherwise
        // cause stack overflow:
        private final Class<VALUE> instanceClass;

        private final Serializable lock = new String ( "lock" );

        // MUTABLE, synchronize on lock:
        private long typeID;

        public Column (
                Wiring wiring,
                String name,
                Class<VALUE> instance_class
                )
        {
            this.wiring = wiring;
            this.name = name;
            this.instanceClass = instance_class;

            this.typeID = -1L;
        }

        @SuppressWarnings("unchecked") // Cast Class<?> - Class<VALUE>
        public Column (
                String name,
                Wiring.Type type
                )
        {
            this.wiring = type.wiring ();
            this.name = name;
            // SuppressWarnings("unchecked"):
            this.instanceClass = (Class<VALUE>) type.instanceClass;

            this.typeID = type.id;
        }

        public abstract VALUE value (
                COLUMN_ROW row
                );

        // Alias for value ( ... ).
        public final VALUE from (
                COLUMN_ROW row
                )
        {
            return this.value ( row );
        }

        public final Wiring.Type type ()
        {
            final Wiring.Table<Wiring.Type> type_table =
                this.wiring.typeTable;

            final Wiring.Type type;
            synchronized ( this.lock )
            {
                if ( this.typeID < 0L )
                {
                    type = Wiring.Type.forClass ( wiring,
                                                  this.instanceClass );
                    this.typeID = type.id;
                }
                else
                {
                    type = type_table.row ( this.typeID );
                }
            }

            return type;
        }

        @SuppressWarnings("unchecked") // Cast VALUE to AS_VALUE
        public <AS_VALUE extends Object>
            AS_VALUE value (
                COLUMN_ROW row,
                Class<AS_VALUE> as_class
                )
        {
            if ( ! this.type ().checkClass ( as_class ).isKept () )
            {
                throw new IllegalArgumentException ( "ERROR Cannot return "
                                                     + this
                                                     + " for row "
                                                     + row
                                                     + " as "
                                                     + as_class.getSimpleName () );
            }

            final Object value = this.value ( row );
            // SuppressWarnings("unchecked"):
            return (AS_VALUE) value;
        }

        // Alias for value ( ..., ... ).
        @SuppressWarnings("unchecked") // Cast Object to Long, Wiring.Row to AS_VALUE.
        public final <AS_VALUE extends Object>
            AS_VALUE from (
                COLUMN_ROW row,
                Class<AS_VALUE> as_class
                )
        {
            return this.value ( row, as_class );
        }
    }


    public static abstract class ForeignKey<COLUMN_ROW extends Wiring.Row, FOREIGN_ROW extends Wiring.Row>
        extends Wiring.Column<COLUMN_ROW, Long>
    {
        private final Wiring.Table<FOREIGN_ROW> foreignTable;

        public ForeignKey (
                String name,
                Wiring.Table<FOREIGN_ROW> foreign_table
                )
        {
            super ( foreign_table.wiring,
                    name,
                    Long.class );

            this.foreignTable = foreign_table;
        }

        // Every ForeignKey must implement:
        // musaico.foundation.wiring.Wiring.Column#value(musaico.foundation.wiring.Wiring.Row)
        // returning the Long ID of the referenced row.

        @SuppressWarnings("unchecked") // Cast Object to Long, Wiring.Row to AS_VALUE.
        public <AS_VALUE extends Object>
            AS_VALUE value (
                COLUMN_ROW row,
                Class<AS_VALUE> as_class
                )
        {
            if ( as_class.isAssignableFrom ( this.foreignTable.rowClass ) )
            {
                // SuppressWarnings("unchecked"): Cast Object to Long.
                final Long foreign_key = this.value ( row );
                final FOREIGN_ROW foreign_row =
                    this.foreignTable.row ( foreign_key );
                // SuppressWarnings("unchecked"): Cast Wiring.Row to AS_VALUE.
                return (AS_VALUE) foreign_row;
            }

            return super.value ( row,
                                 as_class );
        }
    }


    public static abstract class Row
    {
        public final Wiring.Table<?> table;
        public final long id;
        public final long timestampNanoseconds;

        public static class TableColumn
            extends Wiring.Column<Wiring.Row, Wiring.Table<?>>
        {
            public static final String NAME = "table";

            @SuppressWarnings("unchecked") // Cast Class<Table> - Class<Table<?>>.
            private TableColumn (
                    Wiring wiring
                    )
            {
                super ( wiring,
                        Wiring.Row.TableColumn.NAME,
                        // SuppressWarnings("unchecked"):
                        (Class<Wiring.Table<?>>) ( (Class<?>) Wiring.Table.class ) );
            }

            @Override
            public final Wiring.Table<?> value (
                    Wiring.Row row
                    )
            {
                return row.table;
            }
        }

        public static class IDColumn
            extends Wiring.Column<Wiring.Row, Long>
        {
            public static final String NAME = "id";

            public IDColumn (
                    Wiring wiring
                    )
            {
                super ( wiring,
                        Wiring.Row.IDColumn.NAME,
                        Long.class );
            }

            @Override
            public final Long value (
                    Wiring.Row row
                    )
            {
                return row.id;
            }
        }

        public static class TimestampNanosecondsColumn
            extends Wiring.Column<Wiring.Row, Long>
        {
            public static final String NAME = "timestampNanoseconds";

            public TimestampNanosecondsColumn (
                    Wiring wiring
                    )
            {
                super ( wiring,
                        Wiring.Row.TimestampNanosecondsColumn.NAME,
                        Long.class );
            }

            @Override
            public final Long value (
                    Wiring.Row row
                    )
            {
                return row.timestampNanoseconds;
            }
        }

        public <SPECIFIC_ROW extends Wiring.Row>
            Row (
                Wiring wiring,
                Class<SPECIFIC_ROW> row_class,
                long row_id
                )
        {
            this ( wiring == null
                       ? null
                       : wiring.forRowClass ( row_class ),
                   row_id );
        }

        public <SPECIFIC_ROW extends Wiring.Row>
            Row (
                Wiring.Table<SPECIFIC_ROW> table,
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
            this.timestampNanoseconds =
                this.wiring ().nanoseconds ( this.table, this.id );

            final Wiring wiring = this.wiring ();
            this.table.addColumns ( new Wiring.Row.TableColumn ( wiring ),
                                    new Wiring.Row.IDColumn ( wiring ),
                                    new Wiring.Row.TimestampNanosecondsColumn ( wiring ) );
        }

        @Override
        public final int hashCode ()
        {
            return (int) this.id;
        }

        @Override
        public boolean equals (
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
                Wiring.Row that
                );

        @Override
        public String toString ()
        {
            return this.getClass ().getSimpleName () + "[" + this.id + "]";
        }

        // Alias for this.table.select ( ... ).from ( ..., .... ).
        @SuppressWarnings("unchecked") // Cast Object to Long, Wiring.Row to AS_VALUE.
        public final <AS_VALUE extends Object>
            AS_VALUE join (
                Class<AS_VALUE> as_class,
                String ... column_names
                )
        {
            // This implementation is inefficient.
            // We should go straight to the long IDs and join them
            // together that way.
            Wiring.Row from_row = this;
            for ( int c = 0; c < ( column_names.length - 1 ); c ++ )
            {
                final Wiring.Column<Wiring.Row, ? extends Object> column =
                    (Wiring.Column<Wiring.Row, ?>)
                    from_row.table.column ( column_names [ c ] );
                if ( ! ( column instanceof Wiring.ForeignKey ) )
                {
                    throw new IllegalArgumentException (
                                  "ERROR join () requires the first "
                                  + ( column_names.length - 1 )
                                  + " columns to be ForeignKeys, but "
                                  + column_names [ c ]
                                  + " (index " + c + ")"
                                  + " is a "
                                  + column.getClass ().getSimpleName ()
                                  + ": "
                                  + StringRepresentation.of (
                                        column_names,
                                        StringRepresentation.DEFAULT_ARRAY_LENGTH,
                                        StringRepresentation.DEFAULT_OBJECT_LENGTH ) );
                }

                final ForeignKey<Wiring.Row, Wiring.Row> foreign_key =
                    (ForeignKey<Wiring.Row, Wiring.Row>) column;
                from_row = foreign_key.value ( from_row, Wiring.Row.class );
            }

            final Wiring.Column<Wiring.Row, ? extends Object> column =
                (Wiring.Column<Wiring.Row, ?>)
                from_row.table.column ( column_names [ column_names.length - 1 ] );
            final AS_VALUE value = column.value ( from_row, as_class );
            return value;
        }

        public final Wiring wiring ()
        {
            return this.table.wiring;
        }
    }


    public static abstract class RowWithMetadata
        extends Wiring.Row
    {
        public final long metadataID;

        public static class MetadataColumn
            extends Wiring.ForeignKey<Wiring.RowWithMetadata, Wiring.Metadata>
        {
            public static final String NAME = "metadata";

            public MetadataColumn (
                    Wiring wiring
                    )
            {
                super ( Wiring.RowWithMetadata.MetadataColumn.NAME, // name
                        wiring.metadataTable );
            }

            @Override
            public final Long value (
                    Wiring.RowWithMetadata row
                    )
            {
                return row.metadataID;
            }
        }

        public <SPECIFIC_ROW extends Wiring.RowWithMetadata>
            RowWithMetadata (
                Wiring.Table<SPECIFIC_ROW> table,
                long row_id,
                long metadata_id
                )
        {
            super ( table,      // table
                    row_id );   // row_id

            this.metadataID = metadata_id;

            final Wiring wiring = this.wiring ();
            this.table.addColumns (
                new Wiring.RowWithMetadata.MetadataColumn ( wiring ) );
        }

        public <SPECIFIC_ROW extends Wiring.RowWithMetadata>
            RowWithMetadata (
                Wiring.Table<SPECIFIC_ROW> table,
                long row_id,
                Wiring.Metadata metadata
                )
        {
            super ( table,     // table
                    row_id );  // row_id

            this.metadataID = metadata.id;

            final Wiring wiring = this.wiring ();
            this.table.addColumns (
                new Wiring.RowWithMetadata.MetadataColumn ( wiring ) );
        }

        @Override
        public boolean equals (
                Object other
                )
        {
            if ( this == other )
            {
                return true;
            }
            // Check class, all other data:
            else if ( ! super.equals ( other ) )
            {
                return false;
            }

            final Wiring.RowWithMetadata that =
                (Wiring.RowWithMetadata) other;
            if ( this.metadataID != that.metadataID )
            {
                return false;
            }

            return true;
        }

        @Override
        public String toString ()
        {
            return this.getClass ().getSimpleName () + ":" + this.path ();
        }

        public final Wiring.Metadata metadata ()
        {
            final Wiring.Table<Wiring.Metadata> metadata_table =
                this.wiring ().metadataTable;
            final Wiring.Metadata metadata =
                metadata_table.row ( this.metadataID );
            return metadata;
        }

        public final Wiring.Namespace namespaceOrNull ()
        {
            final Wiring.Metadata metadata = this.metadata ();
            if ( metadata.namespaceID < 0L )
            {
                return null;
            }

            final Wiring.Table<Wiring.Namespace> namespace_table =
                this.wiring ().namespaceTable;
            final Wiring.Namespace namespace =
                namespace_table.row ( metadata.namespaceID );
            return namespace;
        }

        public final String name ()
        {
            final Wiring.Metadata metadata = this.metadata ();
            return metadata.name;
        }

        public final String path ()
        {
            return this.metadata ().path ();
        }

        public void tag (
                Wiring.Tag... add_tags
                )
        {
            final Wiring.Metadata old_metadata = this.metadata ();
            final Wiring.Metadata new_metadata = old_metadata.tag ( add_tags );
            final Wiring.Table<Wiring.Metadata> metadata_table =
                this.wiring ().metadataTable;
            metadata_table.replace ( new_metadata );
        }

        public void tag (
                String... add_tag_names
                )
        {
            final Wiring.Metadata old_metadata = this.metadata ();
            final Wiring.Metadata new_metadata = old_metadata.tag ( add_tag_names );
            final Wiring.Table<Wiring.Metadata> metadata_table =
                this.wiring ().metadataTable;
            metadata_table.replace ( new_metadata );
        }

        public void untag (
                Wiring.Tag... remove_tags
                )
        {
            final Wiring.Metadata old_metadata = this.metadata ();
            final Wiring.Metadata new_metadata = old_metadata.untag ( remove_tags );
            final Wiring.Table<Wiring.Metadata> metadata_table =
                this.wiring ().metadataTable;
            metadata_table.replace ( new_metadata );
        }

        public void untag (
                String... remove_tag_names
                )
        {
            final Wiring.Metadata old_metadata = this.metadata ();
            final Wiring.Metadata new_metadata = old_metadata.untag ( remove_tag_names );
            final Wiring.Table<Wiring.Metadata> metadata_table =
                this.wiring ().metadataTable;
            metadata_table.replace ( new_metadata );
        }

        public Wiring.Tag [] tags ()
        {
            final Wiring.Metadata metadata = this.metadata ();
            final Wiring.Tag [] tags = metadata.tags ();
            return tags;
        }
    }


    public static abstract class RowWithData
        extends Wiring.RowWithMetadata
    {
        public final long dataID;

        public static class DataColumn
            extends Wiring.ForeignKey<Wiring.RowWithData, Wiring.Data>
        {
            public static final String NAME = "data";

            public DataColumn (
                    Wiring wiring
                    )
            {
                super ( Wiring.RowWithData.DataColumn.NAME, // name
                        wiring.dataTable );
            }

            @Override
            public final Long value (
                    Wiring.RowWithData row
                    )
            {
                return row.dataID;
            }
        }

        public <SPECIFIC_ROW extends Wiring.RowWithData>
            RowWithData (
                Wiring.Table<SPECIFIC_ROW> table,
                long row_id,
                long metadata_id,
                long data_id
                )
        {
            super ( table,       // table
                    row_id,      // row_id
                    metadata_id ); // metadata_id

            this.dataID = data_id;

            final Wiring wiring = this.wiring ();
            this.table.addColumns (
                new Wiring.RowWithData.DataColumn ( wiring ) );
        }

        public <SPECIFIC_ROW extends Wiring.RowWithData>
            RowWithData (
                Wiring.Table<SPECIFIC_ROW> table,
                long row_id,
                Wiring.Metadata metadata,
                Wiring.Data data
                )
        {
            super ( table,     // table
                    row_id,    // row_id
                    metadata ); // metadata

            this.dataID = data.id;

            final Wiring wiring = this.wiring ();
            this.table.addColumns (
                new Wiring.RowWithData.DataColumn ( wiring ) );
        }

        @Override
        public boolean equals (
                Object other
                )
        {
            if ( this == other )
            {
                return true;
            }
            // Check class, all other data:
            else if ( ! super.equals ( other ) )
            {
                return false;
            }

            final Wiring.RowWithData that =
                (Wiring.RowWithData) other;
            if ( this.dataID != that.dataID )
            {
                return false;
            }

            return true;
        }

        @Override
        public String toString ()
        {
            return super.toString () + "="
                + StringRepresentation.of (
                      this.dataValue (),
                      StringRepresentation.DEFAULT_ARRAY_LENGTH,
                      StringRepresentation.DEFAULT_OBJECT_LENGTH );
        }

        public final Wiring.Data data ()
        {
            final Wiring.Table<Wiring.Data> data_table =
                this.wiring ().dataTable;
            final Wiring.Data data = data_table.row ( this.dataID );

            return data;
        }

        public final void dataUpdate (
                Object [] data_value
                )
        {
            final Wiring wiring = this.wiring ();
            final Wiring.Data old_data = this.data ();
            final Wiring.Data new_data = old_data.update ( data_value );
            wiring.dataTable.replace ( new_data );
        }

        public final Object [] dataValue ()
        {
            final Wiring.Data data = this.data ();
            final Object [] value = data.value;
            return value;
        }

        public final <DATA extends Object> DATA [] dataValue (
                Class<DATA> data_class
                )
        {
            final Wiring.Data data = this.data ();
            final DATA value [] = data.value ( data_class );
            return value;
        }
    }




    public static class Table<ROW extends Wiring.Row>
    {
        public final Wiring wiring;
        public final Class<ROW> rowClass;

        private final Serializable lock = new String ( "lock" );

        // Synchronize access to these fields on this.lock:
        private final LinkedHashMap<Long, ROW> rows = new LinkedHashMap<Long, ROW> ();
        private long nextID = 0L;

        private final LinkedHashMap<String, Wiring.Column<? extends Wiring.Row, ? extends Object>> columns =
            new LinkedHashMap<String, Wiring.Column<? extends Wiring.Row, ? extends Object>> ();

        public Table (
                Wiring wiring,
                Class<ROW> row_class
                )
        {
            this.wiring = wiring;
            this.rowClass = row_class;
        }

        @SafeVarargs // Possible heap pollution Wiring.Column<?, ?> ...
        protected final void addColumns (
                Wiring.Column<? extends Wiring.Row, ? extends Object> ... columns
                )
        {
            for ( Wiring.Column<? extends Wiring.Row, ? extends Object> column : columns )
            {
                if ( this.columns.containsKey ( column.name ) )
                {
                    continue;
                }

                this.columns.put ( column.name, column );
            }
        }

        public final Wiring.Column<? extends Wiring.Row, ? extends Object> column (
                String name
                )
        {
            final Wiring.Column<? extends Wiring.Row, ? extends Object> column =
                this.columns.get ( name );
            if ( column == null )
            {
                throw new IllegalArgumentException ( "ERROR No such column '"
                                                     + name
                                                     + "' in "
                                                     + this );
            }

            return column;
        }

        // Alias for column ( ... ).
        public final Wiring.Column<? extends Wiring.Row, ? extends Object> select (
                String name
                )
        {
            return this.column ( name );
        }

        public static class NextIDEvent
            extends Wiring.AbstractEvent<Wiring.Table<?>, Long, Long>
        {
            public NextIDEvent (
                    Wiring.Table<?> host,
                    long next_id
                    )
            {
                super ( host.wiring,
                        host,
                        "nextID",
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
                    this.assertEquals ( "redo", "nextID",
                                        (long) this.undoData,
                                        this.host.nextID );
                    this.host.nextID = this.redoData;
                }

                this.wiring.sidechain ( Wiring.DoOrUndo.DO,
                                        this );
            }

            @Override
            public final void undo ()
            {
                synchronized ( this.host.lock )
                {
                    this.assertEquals ( "undo", "nextID",
                                        (long) this.redoData,
                                        this.host.nextID );
                    this.host.nextID = this.undoData;
                }

                this.wiring.sidechain ( Wiring.DoOrUndo.UNDO,
                                        this );
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
                super ( host.wiring,
                        host,
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
                    this.assertEquals ( "redo", "rows",
                                        this.undoData,
                                        this.host.rows );
                    this.host.rows.clear ();
                    this.host.rows.putAll ( this.redoData );
                }

                this.wiring.sidechain ( Wiring.DoOrUndo.DO,
                                        this );
            }

            @Override
            public final void undo ()
            {
                synchronized ( this.host.lock )
                {
                    this.assertEquals ( "undo", "rows",
                                        this.redoData,
                                        this.host.rows );
                    this.host.rows.clear ();
                    this.host.rows.putAll ( this.undoData );
                }

                this.wiring.sidechain ( Wiring.DoOrUndo.UNDO,
                                        this );
            }
        }

        public static class RowEvent<EVENT_ROW extends Wiring.Row>
            extends Wiring.AbstractEvent<Table<EVENT_ROW>, Long, EVENT_ROW>
        {
            public RowEvent (
                    Wiring.Table<EVENT_ROW> host,
                    long row_id,
                    EVENT_ROW row
                    )
            {
                super ( host.wiring,
                        host,
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
                    this.assertEquals ( "redo", "row",
                                        this.undoData,
                                        this.host.rows.get ( this.redoData ) );
                }

                this.wiring.sidechain ( Wiring.DoOrUndo.DO,
                                        this );
            }

            @Override
            public final void undo ()
            {
                this.wiring.sidechain ( Wiring.DoOrUndo.UNDO,
                                        this );
            }
        }

        public static class RowsEvent<EVENT_ROW extends Wiring.Row>
            extends Wiring.AbstractEvent<Table<EVENT_ROW>, long [], EVENT_ROW []>
        {
            public RowsEvent (
                    Wiring.Table<EVENT_ROW> host,
                    long [] row_ids,
                    EVENT_ROW [] rows
                    )
            {
                super ( host.wiring,
                        host,
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
                        this.assertEquals ( "redo", "rows",
                                            row,
                                            this.host.rows.get ( row_id ) );
                    }
                }

                this.wiring.sidechain ( Wiring.DoOrUndo.DO,
                                        this );
            }

            @Override
            public final void undo ()
            {
                this.wiring.sidechain ( Wiring.DoOrUndo.UNDO,
                                        this );
            }
        }

        public static class WhereEvent<EVENT_ROW extends Wiring.Row, COLUMN_VALUE extends Object>
            extends Wiring.AbstractEvent<Table<EVENT_ROW>, Wiring.Column<? extends Wiring.Row, COLUMN_VALUE>, EVENT_ROW []>
        {
            public final Filter<COLUMN_VALUE> filter;

            public WhereEvent (
                    Wiring.Table<EVENT_ROW> host,
                    Wiring.Column<? extends Wiring.Row, COLUMN_VALUE> column,
                    Filter<COLUMN_VALUE> filter,
                    EVENT_ROW [] rows
                    )
            {
                super ( host.wiring,
                        host,
                        "row",
                        column,
                        rows );

                this.filter = filter;
            }

            public final Column<? extends Wiring.Row, COLUMN_VALUE> column ()
            {
                return this.redoData;
            }

            public final EVENT_ROW [] rows ()
            {
                return this.undoData;
            }

            @Override
            @SuppressWarnings("unchecked") // Cast Wiring.Column<?, CV> - Wiring.Column<Wiring.Row, CV>
            public final void redo ()
            {
                // SuppressWarnings("unchecked"):
                final Wiring.Column<Wiring.Row, COLUMN_VALUE> column =
                    (Wiring.Column<Wiring.Row, COLUMN_VALUE>)
                    this.redoData;
                final List<EVENT_ROW> matching_rows_list =
                    new ArrayList<EVENT_ROW> ();
                synchronized ( this.host.lock )
                {
                    for ( EVENT_ROW row : this.host.rows.values () )
                    {
                        final COLUMN_VALUE value = column.from ( row );
                        if ( filter.filter ( value ).isKept () )
                        {
                            matching_rows_list.add ( row );
                        }
                    }
                }

                final EVENT_ROW [] template = (EVENT_ROW [])
                    Array.newInstance ( this.host.rowClass, matching_rows_list.size () );
                final EVENT_ROW [] matching_rows = matching_rows_list.toArray ( template );

                this.assertEquals ( "redo", "matching_rows",
                                    this.undoData,
                                    matching_rows );

                this.wiring.sidechain ( Wiring.DoOrUndo.DO,
                                        this );
            }

            @Override
            public final void undo ()
            {
                this.wiring.sidechain ( Wiring.DoOrUndo.UNDO,
                                        this );
            }
        }


        // Potential deadlock if we ever allow
        //tables to lock each other, or metadata
        // table then another table (reverse order).
        // Make sure we always lock row table
        // first, then metadata table inside
        // the first synchronized block.
        private final void updateMetadata (
                Wiring.RowWithMetadata row
                )
        {
            final long modified_timestamp =
                this.wiring.milliseconds ( this,
                                           row.id );

            // Update the metadata table:
            row.tag ( new Wiring.Tag.Modified ( modified_timestamp ) );
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

        @SuppressWarnings("unchecked") // Possible heap pollution ROW...
        public final Wiring.Table<ROW> add (
                ROW... rows
                )
        {
            final Wiring.Table.ChangeRowsEvent<ROW> event;
            synchronized ( this.lock )
            {
                final LinkedHashMap<Long, ROW> old_rows =
                    new LinkedHashMap<Long, ROW> ( this.rows );
                for ( ROW row : rows )
                {
                    if ( this.rows.containsKey ( row.id ) )
                    {
                        this.rows.clear ();
                        this.rows.putAll ( old_rows );
                        throw new IllegalArgumentException (
                                      "ERROR Cannot add row "
                                      + row.id
                                      + " to "
                                      + this
                                      + ": row already exists"
                                      + " while trying to add "
                                      + StringRepresentation.of (
                                            rows,
                                            StringRepresentation.DEFAULT_ARRAY_LENGTH,
                                            StringRepresentation.DEFAULT_OBJECT_LENGTH ) );
                    }

                    this.rows.put ( row.id, row );
                }

                // Update metadata AFTER we're sure that
                // there were no problems.
                if ( Wiring.RowWithMetadata.class.isAssignableFrom ( this.rowClass ) )
                {
                    for ( ROW row : rows )
                    {
                        if ( row instanceof Wiring.RowWithMetadata )
                        {
                            // Potential deadlock if we ever allow
                            // tables to lock each other, or metadata
                            // table then another table (reverse order).
                            // Make sure we always lock row table
                            // first, then metadata table inside
                            // the first synchronized block.
                            updateMetadata ( (Wiring.RowWithMetadata) row );
                        }
                    }
                }

                final LinkedHashMap<Long, ROW> new_rows =
                    new LinkedHashMap<Long, ROW> ( this.rows );

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

        @SuppressWarnings("unchecked") // Possible heap pollution ROW...
        public final Wiring.Table<ROW> addOrReplace (
                ROW... rows
                )
        {
            final Wiring.Table.ChangeRowsEvent<ROW> event;
            synchronized ( this.lock )
            {
                final LinkedHashMap<Long, ROW> old_rows =
                    new LinkedHashMap<Long, ROW> ( this.rows );
                for ( ROW row : rows )
                {
                    // Caller does not care whether the row
                    // already exists or not.
                    this.rows.put ( row.id, row );
                }

                // Update metadata AFTER we're sure that
                // there were no problems.
                if ( Wiring.RowWithMetadata.class.isAssignableFrom ( this.rowClass ) )
                {
                    for ( ROW row : rows )
                    {
                        if ( row instanceof Wiring.RowWithMetadata )
                        {
                            // Potential deadlock if we ever allow
                            // tables to lock each other, or metadata
                            // table then another table (reverse order).
                            // Make sure we always lock row table
                            // first, then metadata table inside
                            // the first synchronized block.
                            updateMetadata ( (Wiring.RowWithMetadata) row );
                        }
                    }
                }

                final LinkedHashMap<Long, ROW> new_rows =
                    new LinkedHashMap<Long, ROW> ( this.rows );

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
        public final Wiring.Table<ROW> remove (
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
                        throw new IllegalArgumentException (
                                      "ERROR No such row id "
                                      + row_id
                                      + " when trying to remove"
                                      + " rows from "
                                      + this
                                      + ": "
                                      + StringRepresentation.of (
                                            row_ids,
                                            StringRepresentation.DEFAULT_ARRAY_LENGTH,
                                            StringRepresentation.DEFAULT_OBJECT_LENGTH ) );
                    }

                    removed_rows [ r ] = removed_row;
                }

                final LinkedHashMap<Long, ROW> new_rows =
                    new LinkedHashMap<Long, ROW> ( this.rows );

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

        @SuppressWarnings("unchecked") // Possible heap pollution ROW...
        public final Wiring.Table<ROW> remove (
                ROW... rows
                )
        {
            final Wiring.Table.ChangeRowsEvent<ROW> event;
            synchronized ( this.lock )
            {
                final LinkedHashMap<Long, ROW> old_rows =
                    new LinkedHashMap<Long, ROW> ( this.rows );
                for ( ROW row : rows )
                {
                    this.rows.remove ( row.id );
                }

                final LinkedHashMap<Long, ROW> new_rows =
                    new LinkedHashMap<Long, ROW> ( this.rows );

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

        @SuppressWarnings("unchecked") // Possible heap pollution ROW...
        public final Wiring.Table<ROW> replace (
                ROW... rows
                )
        {
            final Wiring.Table.ChangeRowsEvent<ROW> event;
            synchronized ( this.lock )
            {
                final LinkedHashMap<Long, ROW> old_rows =
                    new LinkedHashMap<Long, ROW> ( this.rows );
                for ( ROW row : rows )
                {
                    if ( ! this.rows.containsKey ( row.id ) )
                    {
                        this.rows.clear ();
                        this.rows.putAll ( old_rows );
                        throw new IllegalArgumentException (
                                      "ERROR Cannot replace row "
                                      + row.id
                                      + " to "
                                      + this
                                      + ": row does not exist"
                                      + " while trying to replace "
                                      + StringRepresentation.of (
                                            rows,
                                            StringRepresentation.DEFAULT_ARRAY_LENGTH,
                                            StringRepresentation.DEFAULT_OBJECT_LENGTH ) );
                    }

                    this.rows.put ( row.id, row );
                }

                // Update metadata AFTER we're sure that
                // there were no problems.
                if ( Wiring.RowWithMetadata.class.isAssignableFrom ( this.rowClass ) )
                {
                    for ( ROW row : rows )
                    {
                        if ( row instanceof Wiring.RowWithMetadata )
                        {
                            // Potential deadlock if we ever allow
                            // tables to lock each other, or metadata
                            // table then another table (reverse order).
                            // Make sure we always lock row table
                            // first, then metadata table inside
                            // the first synchronized block.
                            updateMetadata ( (Wiring.RowWithMetadata) row );
                        }
                    }
                }

                final LinkedHashMap<Long, ROW> new_rows =
                    new LinkedHashMap<Long, ROW> ( this.rows );

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

        @SuppressWarnings("unchecked") // Cast ? [] to ROW [].
        public final ROW [] rows ()
        {
            final long [] row_ids;
            final ROW [] rows;
            final Wiring.Table.RowsEvent<ROW> event;
            synchronized ( this.lock )
            {
                // SuppressWarnings("unchecked"):
                row_ids = new long [ this.rows.size () ];
                rows = (ROW [])
                    Array.newInstance ( this.rowClass, this.rows.size () );
                int r = 0;
                for ( long row_id : this.rows.keySet () )
                {
                    row_ids [ r ] = row_id;
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

                    r ++;
                }
            }

            event = new Wiring.Table.RowsEvent<ROW> ( this,
                                                      row_ids,
                                                      rows );

            this.wiring.history ( event );

            return event.rows ();
        }

        @SuppressWarnings("unchecked") // Cast Wiring.Column<?, CV> - Wiring.Column<Wiring.Row, CV>, ? [] to ROW [].
        public final <COLUMN_VALUE extends Object>
            ROW [] where (
                Wiring.Column<? extends Wiring.Row, COLUMN_VALUE> column,
                Filter<COLUMN_VALUE> filter
                )
        {
            // SuppressWarnings("unchecked"):
            final Wiring.Column<Wiring.Row, COLUMN_VALUE> generic_column =
                (Wiring.Column<Wiring.Row, COLUMN_VALUE>)
                column;
            final List<ROW> matching_rows_list =
                new ArrayList<ROW> ();
            synchronized ( this.lock )
            {
                for ( ROW row : this.rows.values () )
                {
                    final COLUMN_VALUE value = generic_column.from ( row );
                    if ( filter.filter ( value ).isKept () )
                    {
                        matching_rows_list.add ( row );
                    }
                }
            }

            final ROW [] template = (ROW [])
                Array.newInstance ( this.rowClass, matching_rows_list.size () );
            final ROW [] matching_rows = matching_rows_list.toArray ( template );

            final Wiring.Table.WhereEvent<ROW, COLUMN_VALUE> event =
                new Wiring.Table.WhereEvent<ROW, COLUMN_VALUE> ( this,
                                                                 column,
                                                                 filter,
                                                                 matching_rows );

            this.wiring.history ( event );

            return event.rows ();
        }
    }




    public static class Carrier
        extends Wiring.RowWithData
    {
        public Carrier (
                Wiring wiring,
                long id,
                long metadata_id,
                long data_id
                )
        {
            super ( wiring.carrierTable,  // table
                    id,                   // row_id
                    metadata_id,          // metadata_id
                    data_id );            // data_id
        }

        public Carrier (
                Wiring wiring,
                long id,
                Wiring.Metadata metadata,
                Wiring.Data data
                )
        {
            super ( wiring.carrierTable,  // table
                    id,                   // row_id
                    metadata,             // metadata
                    data );               // data
        }

        @Override
        protected boolean equalsRow (
                Wiring.Row row
                )
        {
            // No extra fields to check.
            return true;
        }
    }

    public static class Tag
        implements Comparable<Tag>
    {
        public static class Time
            extends Wiring.Tag
        {
            public Time (
                    String name,
                    long value
                    )
            {
                super ( name,
                        value );
            }
        }

        public static class Created
            extends Wiring.Tag.Time
        {
            public static final String NAME = "created";
            public Created (
                    long milliseconds_since_utc_0
                    )
            {
                super ( NAME, milliseconds_since_utc_0 );
            }
        }

        public static class Modified
            extends Wiring.Tag.Time
        {
            public static final String NAME = "modified";
            public Modified (
                    long milliseconds_since_utc_0
                    )
            {
                super ( NAME, milliseconds_since_utc_0 );
            }
        }

        public static class Tagged
            extends Wiring.Tag.Time
        {
            public static final String NAME = "tagged";
            public Tagged (
                    long milliseconds_since_utc_0
                    )
            {
                super ( NAME, milliseconds_since_utc_0 );
            }
        }

        public static class Dequeued
            extends Wiring.Tag.Time
        {
            public static final String NAME = "dequeued";
            public Dequeued (
                    long milliseconds_since_utc_0
                    )
            {
                super ( NAME, milliseconds_since_utc_0 );
            }
        }

        public static class Enqueued
            extends Wiring.Tag.Time
        {
            public static final String NAME = "enqueued";
            public Enqueued (
                    long milliseconds_since_utc_0
                    )
            {
                super ( NAME, milliseconds_since_utc_0 );
            }
        }


        public final String name;
        public final Object value;

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
                Wiring.Tag that
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
            else if ( this.getClass () != object.getClass () )
            {
                return false;
            }

            final Wiring.Tag that = (Tag) object;
            if ( ! this.name.equals ( that.name ) )
            {
                return false;
            }

            // The value is not considered in equality
            // so that, for example, Map lookup will work
            // for Tags, no matter how the value changes.
            return true;
        }

        @Override
        public final String toString ()
        {
            final StringBuilder sbuf = new StringBuilder ();
            sbuf.append ( "[ " );
            sbuf.append ( this.name );
            if ( this.value != Boolean.TRUE )
            {
                sbuf.append ( " = " );
                sbuf.append ( StringRepresentation.of (
                                  this.value,
                                  StringRepresentation.DEFAULT_OBJECT_LENGTH ) );
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

        public static class NamespaceColumn
            extends Wiring.ForeignKey<Wiring.Metadata, Wiring.Namespace>
        {
            public static final String NAME = "namespace";

            public NamespaceColumn (
                    Wiring wiring
                    )
            {
                super ( Wiring.Metadata.NamespaceColumn.NAME, // name
                        wiring.namespaceTable );
            }

            @Override
            public final Long value (
                    Wiring.Metadata row
                    )
            {
                return row.namespaceID;
            }
        }

        public static class NameColumn
            extends Wiring.Column<Wiring.Metadata, String>
        {
            public static final String NAME = "name";

            public NameColumn (
                    Wiring wiring
                    )
            {
                super ( wiring,
                        Wiring.Metadata.NameColumn.NAME, // name
                        String.class );
            }

            @Override
            public final String value (
                    Wiring.Metadata row
                    )
            {
                return row.name;
            }
        }

        public static class TagsColumn
            extends Wiring.Column<Wiring.Metadata, Wiring.Tag []>
        {
            public static final String NAME = "tags";

            public TagsColumn (
                    Wiring wiring
                    )
            {
                super ( wiring,
                        Wiring.Metadata.TagsColumn.NAME, // name
                        Wiring.Tag [].class );
            }

            @Override
            public final Wiring.Tag [] value (
                    Wiring.Metadata row
                    )
            {
                return row.tags ();
            }
        }

        // This constructor calls Wiring.fillTags ( ... )
        // to create Wiring.Tag.Created and Wiring.Tag.Modified
        // tags, as well as any other custom ones.
        public Metadata (
                Wiring wiring,
                long id,
                long namespace_id,
                String name,
                Wiring.Tag... tags
                )
        {
            super ( wiring.metadataTable,  // table
                    id );                  // row_id

            if ( namespace_id >= 0L
                 && "/".equals ( name ) )
            {
                throw new IllegalArgumentException (
                              "ERROR Only a root metadata"
                              + " with no parent namespace"
                              + " may have name = '/'"
                              + " while constructing Wiring.Metadata "
                              + id
                              + "  namespace "
                              + namespace_id
                              + " name "
                              + name
                              + " tags "
                              + StringRepresentation.of (
                                    tags,
                                    StringRepresentation.DEFAULT_ARRAY_LENGTH,
                                    StringRepresentation.DEFAULT_OBJECT_LENGTH ) );
            }

            this.namespaceID = namespace_id;
            this.name = name;

            for ( Wiring.Tag tag : tags )
            {
                this.tags.put ( tag.name, tag );
            }

            this.table.addColumns (
                new Wiring.Metadata.NamespaceColumn ( wiring ),
                new Wiring.Metadata.NameColumn ( wiring ),
                new Wiring.Metadata.TagsColumn ( wiring ) );

            this.wiring ().fillTags ( this, this.tags );
        }

        // This constructor calls Wiring.fillTags ( ... )
        // to create Wiring.Tag.Created and Wiring.Tag.Modified
        // tags, as well as any other custom ones.
        public Metadata (
                Wiring wiring,
                long id,
                Wiring.Namespace namespace,
                String name,
                Wiring.Tag... tags
                )
        {
            super ( wiring.metadataTable,  // table
                    id );                  // row_id

            if ( namespace != null
                 && "/".equals ( name ) )
            {
                throw new IllegalArgumentException (
                              "ERROR Only a root metadata"
                              + " with no parent namespace"
                              + " may have name = '/'"
                              + " while constructing Wiring.Metadata "
                              + id
                              + "  namespace "
                              + namespace
                              + " name "
                              + name
                              + " tags "
                              + StringRepresentation.of (
                                    tags,
                                    StringRepresentation.DEFAULT_ARRAY_LENGTH,
                                    StringRepresentation.DEFAULT_OBJECT_LENGTH ) );
            }

            this.namespaceID = namespace.id;
            this.name = name;

            for ( Wiring.Tag tag : tags )
            {
                this.tags.put ( tag.name, tag );
            }

            this.table.addColumns (
                new Wiring.Metadata.NamespaceColumn ( wiring ),
                new Wiring.Metadata.NameColumn ( wiring ),
                new Wiring.Metadata.TagsColumn ( wiring ) );

            this.wiring ().fillTags ( this, this.tags );
        }

        // This constructor copies the specified tags, without
        // calling Wiring.fillTags ( ... ) or updating
        // the modified tag.
        private Metadata (
                Wiring.Metadata previous_version,
                LinkedHashMap<String, Wiring.Tag> tags_map
                )
        {
            super ( previous_version == null  // table
                        ? null
                        : previous_version.table,
                    previous_version == null  // row_id
                        ? -1L
                        : previous_version.id );

            this.namespaceID = previous_version.namespaceID;
            this.name = previous_version.name;

            this.tags.putAll ( tags_map );

            this.table.addColumns (
                new Wiring.Metadata.NamespaceColumn ( this.wiring () ),
                new Wiring.Metadata.NameColumn ( this.wiring () ),
                new Wiring.Metadata.TagsColumn ( this.wiring () ) );
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
            else if ( ! this.tags.equals ( that.tags ) )
            {
                return false;
            }

            return true;
        }

        public final void diffTags (
                Wiring.Metadata that,
                List<Wiring.Tag> added_list,
                List<Wiring.Tag> modified_list,
                List<Wiring.Tag> removed_list
                )
        {
            final Wiring.Metadata big;
            final Wiring.Metadata small;
            List<Wiring.Tag> added_to_small_list;
            List<Wiring.Tag> removed_from_small_list;
            if ( this.tags.size () >= that.tags.size () )
            {
                big = this;
                small = that;
                added_to_small_list = added_list;
                removed_from_small_list = removed_list;
            }
            else
            {
                big = that;
                small = this;
                added_to_small_list = removed_list;
                removed_from_small_list = added_list;
            }

            int num_found_small_tags = 0;
            for ( Wiring.Tag big_tag : big.tags.values () )
            {
                final Wiring.Tag small_tag = small.tags.get ( big_tag.name );
                if ( small_tag == null )
                {
                    added_to_small_list.add ( big_tag );
                }
                else if ( ! big_tag.equals ( small_tag ) )
                {
                    modified_list.add ( big_tag );
                    num_found_small_tags ++;
                }
            }

            if ( num_found_small_tags < small.tags.size () )
            {
                for ( Wiring.Tag small_tag : small.tags.values () )
                {
                    final Wiring.Tag big_tag = big.tags.get ( small_tag.name );
                    if ( big_tag == null )
                    {
                        removed_from_small_list.add ( small_tag );
                    }
                }
            }
        }

        public final Wiring.Namespace namespaceOrNull ()
        {
            if ( this.namespaceID < 0L )
            {
                return null;
            }

            final Wiring.Table<Wiring.Namespace> namespace_table =
                this.wiring ().namespaceTable;
            final Wiring.Namespace namespace =
                namespace_table.row ( this.namespaceID );
            return namespace;
        }

        public final String path ()
        {
            final StringBuilder sbuf = new StringBuilder ();
            Wiring.Metadata metadata = this;
            final Set<Wiring.Metadata> already_visited =
                new HashSet<Wiring.Metadata> ();
            while ( metadata != null )
            {
                if ( already_visited.contains ( metadata ) )
                {
                    // Prevent infinite loops.
                    break;
                }

                already_visited.add ( metadata );

                final String name = metadata.name;
                if ( ! "/".equals ( name ) )
                {
                    sbuf.insert ( 0, "/" );
                    sbuf.insert ( 1, name );
                }
                else if ( sbuf.length () == 0 )
                {
                    // Root path.
                    sbuf.append ( "/" );
                }
                else
                {
                    // The root namespsce.
                    // We've already prefixed the path with "/",
                    // no need to add another slash.
                }

                final Wiring.Namespace namespace =
                    metadata.namespaceOrNull ();
                if ( namespace == null )
                {
                    metadata = null;
                }
                else
                {
                    metadata = namespace.metadata ();
                }
            }

            final String path = sbuf.toString ();
            return path;
        }

        public Wiring.Metadata tag (
                Wiring.Tag... add_tags
                )
        {
            final LinkedHashMap<String, Wiring.Tag> new_tags_map =
                new LinkedHashMap<String, Wiring.Tag> ( this.tags );
            new_tags_map.putAll ( this.tags );
            boolean is_tagged_tag_included = false;
            for ( Wiring.Tag add_tag : add_tags )
            {
                new_tags_map.put ( add_tag.name, add_tag );

                if ( ! is_tagged_tag_included
                     && Wiring.Tag.Tagged.NAME.equals ( add_tag.name ) )
                {
                    is_tagged_tag_included = true;
                }
            }

            if ( ! is_tagged_tag_included )
            {
                // Update the tagged timestamp:
                final long tagged_milliseconds =
                    this.wiring ().milliseconds ( this.table,
                                                  this.id );
                final Wiring.Tag.Tagged tag_tagged =
                    new Wiring.Tag.Tagged ( tagged_milliseconds );
                new_tags_map.put ( tag_tagged.name, tag_tagged );
            }

            return new Wiring.Metadata ( this,
                                         new_tags_map );
        }

        public Wiring.Metadata tag (
                String... add_tag_names
                )
        {
            final LinkedHashMap<String, Wiring.Tag> new_tags_map =
                new LinkedHashMap<String, Wiring.Tag> ( this.tags );
            for ( String add_tag_name : add_tag_names )
            {
                final Wiring.Tag add_tag = new Wiring.Tag ( add_tag_name );
                new_tags_map.put ( add_tag_name, add_tag );
            }

            // Update the tagged timestamp
            // (even if someone tried to set it to TRUE):
            final long tagged_milliseconds =
                this.wiring ().milliseconds ( this.table,
                                              this.id );
            final Wiring.Tag.Tagged tag_tagged =
                new Wiring.Tag.Tagged ( tagged_milliseconds );
            new_tags_map.put ( tag_tagged.name, tag_tagged );

            return new Wiring.Metadata ( this,
                                         new_tags_map );
        }

        @Override
        public String toString ()
        {
            return this.getClass ().getSimpleName () + ":" + this.path ();
        }

        public Wiring.Metadata untag (
                Wiring.Tag... remove_tags
                )
        {
            final LinkedHashMap<String, Wiring.Tag> new_tags_map =
                new LinkedHashMap<String, Wiring.Tag> ( this.tags );
            for ( Wiring.Tag remove_tag : remove_tags )
            {
                new_tags_map.remove ( remove_tag.name );
            }

            // Update the tagged timestamp:
            final long tagged_milliseconds =
                this.wiring ().milliseconds ( this.table,
                                              this.id );
            final Wiring.Tag.Tagged tag_tagged =
                new Wiring.Tag.Tagged ( tagged_milliseconds );
            new_tags_map.put ( tag_tagged.name, tag_tagged );

            return new Wiring.Metadata ( this,
                                         new_tags_map );
        }

        public Wiring.Metadata untag (
                String... remove_tag_names
                )
        {
            final LinkedHashMap<String, Wiring.Tag> new_tags_map =
                new LinkedHashMap<String, Wiring.Tag> ( this.tags );
            for ( String remove_tag_name : remove_tag_names )
            {
                new_tags_map.remove ( remove_tag_name );
            }

            // Update the tagged timestamp:
            final long tagged_milliseconds =
                this.wiring ().milliseconds ( this.table,
                                              this.id );
            final Wiring.Tag.Tagged tag_tagged =
                new Wiring.Tag.Tagged ( tagged_milliseconds );
            new_tags_map.put ( tag_tagged.name, tag_tagged );

            return new Wiring.Metadata ( this,
                                         new_tags_map );
        }

        public Wiring.Tag [] tags ()
        {
            final Wiring.Tag [] template =
                new Wiring.Tag [ this.tags.size () ];
            final Wiring.Tag [] tags =
                this.tags.values ().toArray ( template );
            return tags;
        }
    }

    public static class Namespace
        extends Wiring.RowWithMetadata
    {
        public Namespace (
                Wiring wiring,
                long id,
                long metadata_id
                )
        {
            super ( wiring.namespaceTable,  // table
                    id,                     // row_id
                    metadata_id );          // metadata_id
        }

        public Namespace (
                Wiring wiring,
                long id,
                Wiring.Metadata metadata
                )
        {
            super ( wiring.namespaceTable,  // table
                    id,                     // row_id
                    metadata );             // metadata
        }

        @Override
        protected boolean equalsRow (
                Wiring.Row row
                )
        {
            // No extra fields to check.
            return true;
        }
    }

    public static class Data
        extends Wiring.Row
    {
        public final long typeID;
        public final Object [] value;

        public static class TypeColumn
            extends Wiring.ForeignKey<Wiring.Data, Wiring.Type>
        {
            public static final String NAME = "type";

            public TypeColumn (
                    Wiring wiring
                    )
            {
                super ( Wiring.Data.TypeColumn.NAME, // name
                        wiring.typeTable );
            }

            @Override
            public final Long value (
                    Wiring.Data row
                    )
            {
                return row.typeID;
            }
        }

        public static class ValueColumn
            extends Wiring.Column<Wiring.Data, Object>
        {
            public static final String NAME = "value";

            public ValueColumn (
                    Wiring wiring
                    )
            {
                super ( Wiring.Data.ValueColumn.NAME, // name
                        Wiring.Type.forClass ( wiring, // type
                                               Object [].class ) );
            }

            @Override
            public final Object [] value (
                    Wiring.Data row
                    )
            {
                return row.value;
            }
        }

        public Data (
                Wiring wiring,
                long id,
                long type_id,
                Object ... value
                )
        {
            super ( wiring.dataTable,  // table
                    id );              // row_id

            this.typeID = type_id;
            this.value = value;

            // Check the value against the type:
            final Wiring.Table<Wiring.Type> type_table =
                this.wiring ().typeTable;
            final Wiring.Type type = type_table.row ( this.typeID );

            if ( ! type.check ( this.value ).isKept () )
            {
                final String maybe_of_class;
                if ( this.value == null )
                {
                    maybe_of_class = "";
                }
                else
                {
                    maybe_of_class =
                        " of class "
                        + value.getClass ().getSimpleName ();
                }

                throw new IllegalArgumentException ( "ERROR Cannot create "
                                                     + type
                                                     + " from value "
                                                     + this.value
                                                     + maybe_of_class );
            }

            this.table.addColumns (
                new Wiring.Data.TypeColumn ( wiring ),
                new Wiring.Data.ValueColumn ( wiring ) );
        }

        public Data (
                Wiring wiring,
                long id,
                Wiring.Type type,
                Object ... value
                )
        {
            super ( wiring.dataTable,  // table
                    id );              // row_id

            this.typeID = type.id;
            this.value = value;

            if ( ! type.check ( this.value ).isKept () )
            {
                final String maybe_of_class;
                if ( this.value == null )
                {
                    maybe_of_class = "";
                }
                else
                {
                    maybe_of_class =
                        " of class "
                        + value.getClass ().getSimpleName ();
                }

                throw new IllegalArgumentException ( "ERROR Cannot create "
                                                     + type
                                                     + " from value "
                                                     + this.value
                                                     + maybe_of_class );
            }

            this.table.addColumns (
                new Wiring.Data.TypeColumn ( wiring ),
                new Wiring.Data.ValueColumn ( wiring ) );
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

            if ( this.value == null )
            {
                if ( that.value != null )
                {
                    return false;
                }
            }
            else if ( that.value == null )
            {
                return false;
            }
            // Compares arrays deeply:
            else if ( this.value != that.value
                      && ! Equal.FILTER.filter ( this.value, that.value ).isKept () )
            {
                return false;
            }

            return true;
        }

        public final Wiring.Type type ()
        {
            final Wiring.Table<Wiring.Type> type_table =
                this.wiring ().typeTable;
            final Wiring.Type type = type_table.row ( this.typeID );
            return type;
        }

        public final Wiring.Data update (
                Object ... new_value
                )
        {
            final Wiring.Type type = this.type ();
            if ( ! type.check ( new_value ).isKept () )
            {
                throw new IllegalArgumentException (
                              "ERROR Cannot update "
                              + this
                              + " type "
                              + type
                              + " with "
                              + new_value.getClass ().getSimpleName ()
                              + " value "
                              + StringRepresentation.of (
                                    new_value,
                                    StringRepresentation.DEFAULT_ARRAY_LENGTH,
                                    StringRepresentation.DEFAULT_OBJECT_LENGTH )
                              );
            }

            final Wiring.Data new_data =
                new Wiring.Data ( this.wiring (),
                                  this.id,
                                  type,
                                  new_value );
            return new_data;
        }

        @SuppressWarnings("unchecked") // Cast Object to DATA
        public final <DATA extends Object> DATA [] value (
                Class<DATA> data_class
                )
        {
            final Wiring.Type type = this.type ();
            if ( ! type.checkClass ( data_class ).isKept () )
            {
                throw new IllegalArgumentException (
                              "ERROR Cannot convert "
                              + this
                              + " type "
                              + type
                              + " to class "
                              + data_class.getSimpleName () );
            }

            // SuppressWarnings("unchecked"):
            return (DATA []) this.value;
        }
    }

    public static class Type
        extends Wiring.RowWithMetadata
    {
        public final Class<?> instanceClass;

        private final ContainerFilter<Object> instanceFilter;

        public static class InstanceClassColumn
            extends Wiring.Column<Wiring.Type, Class<?>>
        {
            public static final String NAME = "instanceClass";

            @SuppressWarnings({"rawtypes", "unchecked"}) // Class.class not Class<?>.class, cast
            public InstanceClassColumn (
                    Wiring wiring
                    )
            {
                super ( wiring,
                        Wiring.Type.InstanceClassColumn.NAME, // name
                        // SuppressWarnings({"rawtypes", "unchecked"}):
                        (Class<Class<?>>) ( (Class<?>) Class.class ) );
            }

            @Override
            public final Class<?> value (
                    Wiring.Type row
                    )
            {
                return row.instanceClass;
            }
        }

        public static class InstanceFilterColumn
            extends Wiring.Column<Wiring.Type, ContainerFilter<Object>>
        {
            public static final String NAME = "instanceFilter";

            @SuppressWarnings({"rawtypes", "unchecked"}) // Filter.class not ContainerFilter<Object>.class, cast
            public InstanceFilterColumn (
                    Wiring wiring
                    )
            {
                super ( wiring,
                        Wiring.Type.InstanceFilterColumn.NAME, // name
                        // SuppressWarnings({"rawtypes", "unchecked"}):
                        (Class<ContainerFilter<Object>>) ( (Class<?>) ContainerFilter.class ) );
            }

            @Override
            public final ContainerFilter<Object> value (
                    Wiring.Type row
                    )
            {
                return row.instanceFilter;
            }
        }

        @SuppressWarnings("unchecked") // Cast Filter<?> to Filter<Object>
        public Type (
                Wiring wiring,
                long id,
                long metadata_id,
                Class<?> instance_class,
                long min,
                long max,
                Filter<? extends Object> element_filter
                )
        {
            super ( wiring.typeTable,  // table
                    id,                // row_id
                    metadata_id );     // metadata_id

            this.instanceClass = instance_class;

            final IncludesOnlyClasses<Object> element_class_filter =
                new IncludesOnlyClasses<Object> ( this.instanceClass );
            final Filter<Object> element_object_filter =
                (Filter<Object>) element_filter;

            final ElementsFilter<Object> elements_filter =
                new IncludesOnly<Object> ( element_class_filter,
                                           element_object_filter );
            final LengthFilter<Object> length_filter =
                new LengthFilter<Object> ( min, max );

            this.instanceFilter =
                new And<Object> ( length_filter,
                                  class_filter,
                                  elements_filter );

            this.table.addColumns (
                new Wiring.Type.InstanceClassColumn ( wiring ),
                new Wiring.Type.InstanceFilterColumn ( wiring ),
                new Wiring.Type.MinColumn ( wiring ),
                new Wiring.Type.MinColumn ( wiring ) );
        }

        @SuppressWarnings("unchecked") // Cast Filter<?> to Filter<Object>
        public Type (
                Wiring wiring,
                long id,
                Wiring.Metadata metadata,
                Class<?> instance_class,
                long min,
                long max,
                Filter<? extends Object> instance_filter
                )
        {
            super ( wiring.typeTable,  // table
                    id,                // row_id
                    metadata );        // metadata

            this.instanceClass = instance_class;


            final IncludesOnlyClasses<Object> element_class_filter =
                new IncludesOnlyClasses<Object> ( this.instanceClass );
            final Filter<Object> element_object_filter =
                (Filter<Object>) element_filter;

            final ElementsFilter<Object> elements_filter =
                new IncludesOnly<Object> ( element_class_filter,
                                           element_object_filter );
            final LengthFilter<Object> length_filter =
                new LengthFilter<Object> ( min, max );

            this.table.addColumns (
                new Wiring.Type.InstanceClassColumn ( wiring ),
                new Wiring.Type.InstanceFilterColumn ( wiring ),
                new Wiring.Type.MinColumn ( wiring ),
                new Wiring.Type.MinColumn ( wiring ) );
        }

        /** @return Helper -- returns Type, inserted into the specified
         *          wiring tables, if necessary, that accepts any
         *          nothing. */
        public static final Wiring.Type none (
                Wiring wiring
                )
        {
            final Wiring.Table<Wiring.Type> type_table =
                wiring.typeTable;
            for ( Wiring.Type type : type_table.rows () )
            {
                final Metadata type_metadata =
                    type.metadata ();
                if ( type_metadata.namespaceID == wiring.namespaceID
                     && "none".equals ( type_metadata.name ) )
                {
                    return type;
                }
            }

            final Wiring.Metadata none_type_metadata =
                new Wiring.Metadata (
                        wiring,             // wiring
                        -1L,                // id
                        wiring.namespaceID, // namespace_id
                        "none"              // name
                        );                  // no tags for now
            final Wiring.Type none_type =
                new Wiring.Type (
                        wiring,             // wiring
                        -1L,                // id
                        none_type_metadata, // metadata
                        Object.class,       // instance_class
                        new DiscardAll<Object> (), // instance_filter
                        0L,                 // min
                        0L );               // max

            final Wiring.Table<Wiring.Metadata> metadata_table =
                wiring.metadataTable;
            metadata_table.add ( none_type_metadata );
            type_table.add ( none_type );

            return none_type;
        }
        /** @return Helper -- returns Type, inserted into the specified
         *          wiring tables, if necessary, that accepts any
         *          non-null data. */
        public static final Wiring.Type any (
                Wiring wiring
                )
        {
            final Wiring.Table<Wiring.Type> type_table =
                wiring.typeTable;
            for ( Wiring.Type type : type_table.rows () )
            {
                final Metadata type_metadata =
                    type.metadata ();
                if ( type_metadata.namespaceID == wiring.namespaceID
                     && "any".equals ( type_metadata.name ) )
                {
                    return type;
                }
            }

            final Wiring.Metadata any_type_metadata =
                new Wiring.Metadata (
                        wiring,             // wiring
                        -1L,                // id
                        wiring.namespaceID, // namespace_id
                        "any"               // name
                        );                  // no tags for now
            final Wiring.Type any_type =
                new Wiring.Type (
                        wiring,            // wiring
                        -1L,               // id
                        any_type_metadata, // metadata
                        Object.class,      // instance_class
                        new KeepAll<Object> (), // instance_filter
                        0L,                // min
                        Long.MAX_VALUE );  // max

            final Wiring.Table<Wiring.Metadata> metadata_table =
                wiring.metadataTable;
            metadata_table.add ( any_type_metadata );
            type_table.add ( any_type );

            return any_type;
        }

        /** @return Helper -- returns Type, inserted into the specified
         *          wiring tables, if necessary, that accepts the
         *          specified class of non-null data. */
        public static final Wiring.Type forClass (
                Wiring wiring,
                Class<?> instance_class
                )
        {
            final Wiring.Table<Wiring.Type> type_table =
                wiring.typeTable;
            final String type_name =
                instance_class.getName (); // Fully qualified package + class name.
            for ( Wiring.Type type : type_table.rows () )
            {
                final Metadata type_metadata =
                    type.metadata ();
                if ( type_metadata.namespaceID == wiring.namespaceID
                     && type_name.equals ( type_metadata.name ) )
                {
                    return type;
                }
            }

            final Wiring.Metadata type_metadata =
                new Wiring.Metadata (
                        wiring,             // wiring
                        -1L,                // id
                        wiring.namespaceID, // namespace_id
                        type_name           // name
                        );                  // no tags for now
            final Wiring.Type type =
                new Wiring.Type (
                        wiring,            // wiring
                        -1L,               // id
                        type_metadata,     // metadata
                        Object.class,      // instance_class
                        new KeepAll<Object> (), // instance_filter
                        1L,                // min
                        1L );              // max

            final Wiring.Table<Wiring.Metadata> metadata_table =
                wiring.metadataTable;

            metadata_table.add ( type_metadata );
            type_table.add ( type );

            return type;
        }

        @Override
        protected boolean equalsRow (
                Wiring.Row row
                )
        {
            final Wiring.Type that = (Wiring.Type) row;
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

        public final FilterState checkClass (
                Class<?> data_class
                )
        {
            if ( data_class == null )
            {
                return FilterState.DISCARDED;
            }
            else if ( ! this.instanceClass.isAssignableFrom ( data_class ) )
            {
                return FilterState.DISCARDED;
            }

            return FilterState.KEPT;
        }

        public final FilterState check (
                Object value
                )
        {
            if ( value == null )
            {
                return FilterState.DISCARDED;
            }
            else if ( this.min > 1L )
            {
                return FilterState.DISCARDED;
            }
            else if ( this.max < 0L )
            {
                return FilterState.DISCARDED;
            }

            final FilterState class_filter_state =
                this.checkClass ( value.getClass () );
            if ( ! class_filter_state.isKept () )
            {
                return class_filter_state;
            }
            else if ( this.instanceFilter == null )
            {
                return class_filter_state;
            }

            final FilterState data_filter_state =
                this.instanceFilter.filter ( value );
            return data_filter_state;
        }

        public final FilterState checkArray (
                Object ... values
                )
        {
            if ( values == null )
            {
                return FilterState.DISCARDED;
            }
            else if ( this.min > ( (long) values.length ) )
            {
                return FilterState.DISCARDED;
            }
            else if ( this.max < ( (long) values.length ) )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( Object value : values )
            {
                filter_state = this.checkClass ( value.getClass () );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
                else if ( this.instanceFilter != null )
                {
                    filter_state = this.instanceFilter.filter ( value );
                    if ( ! filter_state.isKept () )
                    {
                        return filter_state;
                    }
                }
            }

            return filter_state;
        }

        public final FilterState checkData (
                Wiring.Data data
                )
        {
            if ( this.instanceClass.isInstance ( data.value ) )
            {
                return this.check ( data.value );
            }
            else if ( data.value != null
                      && data.value.getClass ().isArray () )
            {
                final Object [] values = (Object []) data.value;
                return this.checkArray ( values );
            }
            else
            {
                return this.check ( data.value );
            }
        }

        public final FilterState checkDataID (
                long data_id
                )
        {
            final Wiring.Table<Wiring.Data> data_table =
                this.wiring ().dataTable;
            final Wiring.Data data = data_table.row ( data_id );

            return this.checkData ( data );
        }

        public final FilterState checkCarrier (
                Wiring.Carrier carrier
                )
        {
            return this.checkDataID ( carrier.dataID );
        }

        public final FilterState checkCarrierID (
                long carrier_id
                )
        {
            final Wiring.Table<Wiring.Carrier> carrier_table =
                this.wiring ().carrierTable;
            final Wiring.Carrier carrier = carrier_table.row ( carrier_id );

            return this.checkCarrier ( carrier );
        }
    }

    public static class CarrierState
        extends Wiring.Row
    {
        public final long carrierID;
        public final long stateID;

        public static class CarrierColumn
            extends Wiring.ForeignKey<Wiring.CarrierState, Wiring.Carrier>
        {
            public static final String NAME = "carrier";

            public CarrierColumn (
                    Wiring wiring
                    )
            {
                super ( Wiring.CarrierState.CarrierColumn.NAME, // name
                        wiring.carrierTable );
            }

            @Override
            public final Long value (
                    Wiring.CarrierState row
                    )
            {
                return row.carrierID;
            }
        }

        public static class StateColumn
            extends Wiring.ForeignKey<Wiring.CarrierState, Wiring.State>
        {
            public static final String NAME = "state";

            public StateColumn (
                    Wiring wiring
                    )
            {
                super ( Wiring.CarrierState.StateColumn.NAME, // name
                        wiring.stateTable );
            }

            @Override
            public final Long value (
                    Wiring.CarrierState row
                    )
            {
                return row.stateID;
            }
        }

        public CarrierState (
                Wiring wiring,
                long id,
                long carrier_id,
                long state_id
                )
        {
            super ( wiring.carrierStateTable,  // table
                    id );                      // row_id

            this.carrierID = carrier_id;
            this.stateID = state_id;

            this.table.addColumns (
                new Wiring.CarrierState.CarrierColumn ( wiring ),
                new Wiring.CarrierState.StateColumn ( wiring ) );
        }

        public CarrierState (
                Wiring wiring,
                long id,
                Wiring.Carrier carrier,
                Wiring.State state
                )
        {
            super ( wiring.carrierStateTable,  // table
                    id );                      // row_id

            this.carrierID = carrier.id;
            this.stateID = state.id;

            this.table.addColumns (
                new Wiring.CarrierState.CarrierColumn ( wiring ),
                new Wiring.CarrierState.StateColumn ( wiring ) );
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
        extends Wiring.RowWithMetadata
    {
        public final long wireBundleID;
        public final long legID;
        public final long wireQueueID;

        public static class WireBundleColumn
            extends Wiring.ForeignKey<Wiring.Wire, Wiring.WireBundle>
        {
            public static final String NAME = "wireBundle";

            public WireBundleColumn (
                    Wiring wiring
                    )
            {
                super ( Wiring.Wire.WireBundleColumn.NAME, // name
                        wiring.wireBundleTable );
            }

            @Override
            public final Long value (
                    Wiring.Wire row
                    )
            {
                return row.wireBundleID;
            }
        }

        public static class LegColumn
            extends Wiring.ForeignKey<Wiring.Wire, Wiring.Leg>
        {
            public static final String NAME = "leg";

            public LegColumn (
                    Wiring wiring
                    )
            {
                super ( Wiring.Wire.LegColumn.NAME, // name
                        wiring.legTable );
            }

            @Override
            public final Long value (
                    Wiring.Wire row
                    )
            {
                return row.legID;
            }
        }

        public static class WireQueueColumn
            extends Wiring.ForeignKey<Wiring.Wire, Wiring.WireQueue>
        {
            public static final String NAME = "wireQueue";

            public WireQueueColumn (
                    Wiring wiring
                    )
            {
                super ( Wiring.Wire.WireQueueColumn.NAME, // name
                        wiring.wireQueueTable );
            }

            @Override
            public final Long value (
                    Wiring.Wire row
                    )
            {
                return row.wireQueueID;
            }
        }

        public Wire (
                Wiring wiring,
                long id,
                long metadata_id,
                long wire_bundle_id,
                long leg_id,
                long wire_queue_id
                )
        {
            super ( wiring.wireTable,  // table
                    id,                // row_id
                    metadata_id );     // metadata_id

            this.wireBundleID = wire_bundle_id;
            this.legID = leg_id;
            this.wireQueueID = wire_queue_id;

            this.table.addColumns (
                new Wiring.Wire.WireBundleColumn ( wiring ),
                new Wiring.Wire.LegColumn ( wiring ),
                new Wiring.Wire.WireQueueColumn ( wiring ) );
        }

        public Wire (
                Wiring wiring,
                long id,
                Wiring.Metadata metadata,
                Wiring.WireBundle wire_bundle,
                Wiring.Leg leg,
                Wiring.WireQueue wire_queue
                )
        {
            super ( wiring.wireTable,  // table
                    id,                // row_id
                    metadata );        // metadata

            this.wireBundleID = wire_bundle.id;
            this.legID = leg.id;
            this.wireQueueID = wire_queue.id;

            this.table.addColumns (
                new Wiring.Wire.WireBundleColumn ( wiring ),
                new Wiring.Wire.LegColumn ( wiring ),
                new Wiring.Wire.WireQueueColumn ( wiring ) );
        }

        @Override
        protected boolean equalsRow (
                Wiring.Row row
                )
        {
            final Wiring.Wire that = (Wiring.Wire) row;
            if ( this.wireBundleID != that.wireBundleID )
            {
                return false;
            }
            else if ( this.legID != that.legID )
            {
                return false;
            }
            else if ( this.wireQueueID != that.wireQueueID )
            {
                return false;
            }

            return true;
        }

        private final void enqueue (
                Wiring.Carrier ... carriers
                )
        {
            if ( carriers.length == 0 )
            {
                return;
            }

            final Wiring.Table<Wiring.WireQueue> wire_queue_table =
                this.wiring ().wireQueueTable;
            final Wiring.WireQueue old_queue =
                wire_queue_table.row ( this.wireQueueID );

            final Wiring.WireQueue new_queue =
                old_queue.add ( carriers );

            final long enqueued_timestamp =
                this.wiring ().milliseconds ( wire_queue_table,
                                              new_queue.id );
            final Wiring.Tag.Enqueued enqueued =
                new Wiring.Tag.Enqueued ( enqueued_timestamp );

            wire_queue_table.replace ( new_queue );

            for ( Wiring.Carrier carrier : carriers )
            {
                carrier.tag ( enqueued );
            }
        }

        public final FilterState checkClass (
                Class<?> data_class
                )
        {
            final Wiring.WireBundle wire_bundle =
                this.wireBundle ();
            final FilterState filter_state =
                wire_bundle.checkClass ( data_class );

            return filter_state;
        }

        public final FilterState check (
                Object ... values
                )
        {
            final Wiring.WireBundle wire_bundle =
                this.wireBundle ();
            final FilterState filter_state =
                wire_bundle.check ( values );

            return filter_state;
        }

        public final FilterState checkDatas (
                Wiring.Data ... datas
                )
        {
            final Wiring.WireBundle wire_bundle =
                this.wireBundle ();
            final FilterState filter_state =
                wire_bundle.checkDatas ( datas );

            return filter_state;
        }

        public final FilterState checkDataIDs (
                long ... data_ids
                )
        {
            final Wiring.WireBundle wire_bundle =
                this.wireBundle ();
            final FilterState filter_state =
                wire_bundle.checkDataIDs ( data_ids );

            return filter_state;
        }

        public final FilterState checkCarriers (
                Wiring.Carrier ... carriers
                )
        {
            final Wiring.WireBundle wire_bundle =
                this.wireBundle ();
            final FilterState filter_state =
                wire_bundle.checkCarriers ( carriers );

            return filter_state;
        }

        public final FilterState checkCarrierIDs (
                long ... carrier_ids
                )
        {
            final Wiring.WireBundle wire_bundle =
                this.wireBundle ();
            final FilterState filter_state =
                wire_bundle.checkCarrierIDs ( carrier_ids );

            return filter_state;
        }

        public final Wiring.Carrier [] dequeue ()
        {
            final Wiring.Table<Wiring.WireQueue> wire_queue_table =
                this.wiring ().wireQueueTable;
            final Wiring.WireQueue old_queue =
                wire_queue_table.row ( this.wireQueueID );

            final Wiring.Carrier [] carriers = old_queue.carriers ();
            if ( carriers.length == 0 )
            {
                return carriers;
            }

            final Wiring.WireQueue new_queue =
                old_queue.removeAll ();

            final long dequeued_timestamp =
                this.wiring ().milliseconds ( wire_queue_table,
                                              new_queue.id );
            final Wiring.Tag.Dequeued dequeued =
                new Wiring.Tag.Dequeued ( dequeued_timestamp );

            wire_queue_table.replace ( new_queue );

            for ( Wiring.Carrier carrier : carriers )
            {
                carrier.tag ( dequeued );
            }

            return carriers;
        }

        public final boolean isTerminated ()
        {
            // Every Wiring.Wire connects to a Wiring.Leg...  right?
            return true;
        }

        public final boolean isWired ()
        {
            for ( Wiring.Wire wire : this.otherEnds () )
            {
                if ( wire.isTerminated () )
                {
                    return true;
                }
            }

            return false;
        }

        public final Wiring.Leg leg ()
        {
            final Wiring.Table<Wiring.Leg> leg_table =
                this.wiring ().legTable;
            final Wiring.Leg leg =
                leg_table.row ( this.legID );

            return leg;
        }

        public final Wiring.Wire [] otherEnds ()
        {
            final Wiring.WireBundle wire_bundle =
                this.wireBundle ();

            final Wiring.Wire [] maybe_other_ends =
                wire_bundle.wires ();
            int my_index = -1;
            for ( int w = 0; w < maybe_other_ends.length; w ++ )
            {
                final Wiring.Wire other_end = maybe_other_ends [ w ];
                if ( other_end.id == this.id )
                {
                    my_index = w;
                    break;
                }
            }

            if ( my_index < 0 )
            {
                // Should be impossible...
                return maybe_other_ends;
            }

            final Wiring.Wire [] other_ends =
                new Wiring.Wire [ maybe_other_ends.length - 1 ];
            System.arraycopy ( maybe_other_ends, 0,
                               other_ends, 0, my_index );
            System.arraycopy ( maybe_other_ends, my_index + 1,
                               other_ends, 0, maybe_other_ends.length - my_index - 1 );
            return other_ends;
        }

        public final Wiring.Carrier [] pull ()
        {
            for ( Wiring.Wire other_end : this.otherEnds () )
            {
                final Wiring.Leg leg = other_end.leg ();
                final Wiring.Chip chip = leg.chip ();
                chip.pull ( other_end );
            }

            final Wiring.Carrier [] dequeued =
                this.dequeue ();
            return dequeued;
        }

        public final void push (
                Wiring.Carrier ... carriers
                )
        {
            final Wiring.WireBundle wire_bundle =
                this.wireBundle ();

            if ( ! wire_bundle.checkCarriers ( carriers )
                              .isKept () )
            {
                throw new IllegalArgumentException (
                              "ERROR Illegal carrier(s)"
                              + " being pushed onto "
                              + wire_bundle
                              + " via "
                              + this
                              + ": "
                              + StringRepresentation.of (
                                    carriers,
                                    StringRepresentation.DEFAULT_ARRAY_LENGTH,
                                    StringRepresentation.DEFAULT_OBJECT_LENGTH ) );
            }

            for ( Wiring.Wire other_end : this.otherEnds () )
            {
                other_end.enqueue ( carriers );
            }
        }

        public final Wiring.WireBundle wireBundle ()
        {
            final Wiring.Table<Wiring.WireBundle> wire_bundle_table =
                this.wiring ().wireBundleTable;
            final Wiring.WireBundle wire_bundle =
                wire_bundle_table.row ( this.wireBundleID );

            return wire_bundle;
        }

        public final Wiring.WireQueue wireQueue ()
        {
            final Wiring.Table<Wiring.WireQueue> wire_queue_table =
                this.wiring ().wireQueueTable;
            final Wiring.WireQueue wire_queue =
                wire_queue_table.row ( this.wireQueueID );

            return wire_queue;
        }
    }

    public static class WireBundle
        extends Wiring.RowWithMetadata
    {
        public final long typeID;

        public static class TypeColumn
            extends Wiring.ForeignKey<Wiring.WireBundle, Wiring.Type>
        {
            public static final String NAME = "type";

            public TypeColumn (
                    Wiring wiring
                    )
            {
                super ( Wiring.WireBundle.TypeColumn.NAME, // name
                        wiring.typeTable );
            }

            @Override
            public final Long value (
                    Wiring.WireBundle row
                    )
            {
                return row.typeID;
            }
        }

        public WireBundle (
                Wiring wiring,
                long id,
                long metadata_id,
                long type_id
                )
        {
            super ( wiring.wireBundleTable,  // table
                    id,                      // row_id
                    metadata_id );           // metadata_id

            this.typeID = type_id;

            this.table.addColumns (
                new Wiring.WireBundle.TypeColumn ( wiring ) );
        }

        public WireBundle (
                Wiring wiring,
                long id,
                Wiring.Metadata metadata,
                Wiring.Type type
                )
        {
            super ( wiring.wireBundleTable,  // table
                    id,                      // row_id
                    metadata );              // metadata

            this.typeID = type.id;

            this.table.addColumns (
                new Wiring.WireBundle.TypeColumn ( wiring ) );
        }

        @Override
        protected boolean equalsRow (
                Wiring.Row row
                )
        {
            final Wiring.WireBundle that = (Wiring.WireBundle) row;
            if ( this.typeID != that.typeID )
            {
                return false;
            }

            return true;
        }

        public final FilterState checkClass (
                Class<?> data_class
                )
        {
            final Wiring.Table<Wiring.Type> type_table =
                this.wiring ().typeTable;
            final Wiring.Type type = type_table.row ( this.typeID );

            return type.checkClass ( data_class );
        }

        public final FilterState check (
                Object ... values
                )
        {
            final Wiring.Table<Wiring.Type> type_table =
                this.wiring ().typeTable;
            final Wiring.Type type = type_table.row ( this.typeID );

            FilterState filter_state = FilterState.KEPT;
            for ( Object value : values )
            {
                filter_state = type.check ( value );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        public final FilterState checkDatas (
                Wiring.Data ... datas
                )
        {
            final Wiring.Table<Wiring.Type> type_table =
                this.wiring ().typeTable;
            final Wiring.Type type = type_table.row ( this.typeID );

            FilterState filter_state = FilterState.KEPT;
            for ( Wiring.Data data : datas )
            {
                filter_state = type.checkData ( data );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        public final FilterState checkDataIDs (
                long ... data_ids
                )
        {
            final Wiring.Table<Wiring.Type> type_table =
                this.wiring ().typeTable;
            final Wiring.Type type = type_table.row ( this.typeID );

            FilterState filter_state = FilterState.KEPT;
            for ( long data_id : data_ids )
            {
                filter_state = type.checkDataID ( data_id );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        public final FilterState checkCarriers (
                Wiring.Carrier ... carriers
                )
        {
            final Wiring.Table<Wiring.Type> type_table =
                this.wiring ().typeTable;
            final Wiring.Type type = type_table.row ( this.typeID );

            FilterState filter_state = FilterState.KEPT;
            for ( Wiring.Carrier carrier : carriers )
            {
                filter_state = type.checkCarrier ( carrier );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        public final FilterState checkCarrierIDs (
                long ... carrier_ids
                )
        {
            final Wiring.Table<Wiring.Type> type_table =
                this.wiring ().typeTable;
            final Wiring.Type type = type_table.row ( this.typeID );

            FilterState filter_state = FilterState.KEPT;
            for ( long carrier_id : carrier_ids )
            {
                filter_state = type.checkCarrierID ( carrier_id );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        public final Wiring.Wire [] wires ()
        {
            final Wiring.Table<Wiring.Wire> wire_table =
                this.wiring ().wireTable;
            final Filter<Long> equals_my_id =
                new EqualTo<Long> ( this.id );
            final Wiring.Wire.WireBundleColumn wire_bundle_column =
                (Wiring.Wire.WireBundleColumn)
                wire_table.column ( Wiring.Wire.WireBundleColumn.NAME );

            final Wiring.Wire [] wires =
                wire_table.where ( wire_bundle_column,
                                   equals_my_id );

            return wires;
        }
    }

    public static class WireQueue
        extends Wiring.RowWithMetadata
    {
        private final long [] carrierIDs;

        public static class CarriersColumn
            extends Wiring.Column<Wiring.WireQueue, Wiring.Carrier []>
        {
            public static final String NAME = "carriers";

            public CarriersColumn (
                    Wiring wiring
                    )
            {
                super ( Wiring.WireQueue.CarriersColumn.NAME, // name
                        Wiring.Type.forClass ( wiring, // type
                                               Wiring.Carrier [].class ) );
            }

            @Override
            public final Wiring.Carrier [] value (
                    Wiring.WireQueue row
                    )
            {
                return row.carriers ();
            }
        }

        public WireQueue (
                Wiring wiring,
                long id,
                long metadata_id,
                long ... carrier_ids
                )
        {
            super ( wiring.wireQueueTable,  // table
                    id,                     // row_id
                    metadata_id );          // metadata_id

            this.carrierIDs = new long [ carrier_ids.length ];
            System.arraycopy ( carrier_ids, 0,
                               this.carrierIDs, 0 , carrier_ids.length );

            this.table.addColumns (
                new Wiring.WireQueue.CarriersColumn ( wiring ) );
        }

        public WireQueue (
                Wiring wiring,
                long id,
                Wiring.Metadata metadata,
                Wiring.Carrier ... carriers
                )
        {
            super ( wiring.wireQueueTable,  // table
                    id,                     // row_id
                    metadata );             // metadata

            this.carrierIDs = new long [ carriers.length ];
            for ( int c = 0; c < carriers.length; c ++ )
            {
                this.carrierIDs [ c ] = carriers [ c ].id;
            }

            this.table.addColumns (
                new Wiring.WireQueue.CarriersColumn ( wiring ) );
        }

        @Override
        protected boolean equalsRow (
                Wiring.Row row
                )
        {
            final Wiring.WireQueue that = (Wiring.WireQueue) row;
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
            // Compares arrays deeply:
            else if ( this.carrierIDs != that.carrierIDs
                      && ! Equal.FILTER.filter ( this.carrierIDs, that.carrierIDs ).isKept () )
            {
                return false;
            }

            return true;
        }

        public final FilterState checkClass (
                Class<?> data_class
                )
        {
            FilterState filter_state = FilterState.KEPT;
            for ( Wiring.Wire wire : this.wires () )
            {
                filter_state =
                    wire.checkClass ( data_class );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        public final FilterState check (
                Object ... values
                )
        {
            FilterState filter_state = FilterState.KEPT;
            for ( Wiring.Wire wire : this.wires () )
            {
                filter_state =
                    wire.check ( values );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        public final FilterState checkDatas (
                Wiring.Data ... datas
                )
        {
            FilterState filter_state = FilterState.KEPT;
            for ( Wiring.Wire wire : this.wires () )
            {
                filter_state =
                    wire.checkDatas ( datas );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        public final FilterState checkDataIDs (
                long ... data_ids
                )
        {
            FilterState filter_state = FilterState.KEPT;
            for ( Wiring.Wire wire : this.wires () )
            {
                filter_state =
                    wire.checkDataIDs ( data_ids );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        public final FilterState checkCarriers (
                Wiring.Carrier ... carriers
                )
        {
            FilterState filter_state = FilterState.KEPT;
            for ( Wiring.Wire wire : this.wires () )
            {
                filter_state =
                    wire.checkCarriers ( carriers );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        public final FilterState checkCarrierIDs (
                long ... carrier_ids
                )
        {
            FilterState filter_state = FilterState.KEPT;
            for ( Wiring.Wire wire : this.wires () )
            {
                filter_state =
                    wire.checkCarrierIDs ( carrier_ids );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
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
            final Wiring.Table<Wiring.Carrier> table =
                this.wiring ().carrierTable;
            final Wiring.Carrier [] carriers = table.rows ( this.carrierIDs );
            return carriers;
        }

        public final long firstID ()
        {
            return this.carrierIDs [ 0 ];
        }

        public final Wiring.Carrier first ()
        {
            final long first_id = this.firstID ();
            final Wiring.Table<Wiring.Carrier> table =
                this.wiring ().carrierTable;
            final Wiring.Carrier first = table.row ( first_id );
            return first;
        }

        public final long lastID ()
        {
            return this.carrierIDs [ this.carrierIDs.length - 1 ];
        }

        public final Wiring.Carrier last ()
        {
            final long last_id = this.lastID ();
            final Wiring.Table<Wiring.Carrier> table =
                this.wiring ().carrierTable;
            final Wiring.Carrier last = table.row ( last_id );
            return last;
        }

        public final Wiring.WireQueue add (
                long ... carrier_ids
                )
        {
            if ( ! this.checkCarrierIDs ( carrier_ids )
                       .isKept () )
            {
                throw new IllegalArgumentException (
                              "ERROR Cannot add"
                              + " carrier IDs to "
                              + this
                              + ": failed check"
                              + " when trying to add "
                              + StringRepresentation.of (
                                    carrier_ids,
                                    StringRepresentation.DEFAULT_ARRAY_LENGTH,
                                    StringRepresentation.DEFAULT_OBJECT_LENGTH ) );
            }

            final long [] new_carrier_ids =
                new long [ this.carrierIDs.length + carrier_ids.length ];
            System.arraycopy ( this.carrierIDs, 0,
                               new_carrier_ids, 0, this.carrierIDs.length );
            System.arraycopy ( carrier_ids, 0,
                               new_carrier_ids, this.carrierIDs.length, carrier_ids.length );

            final Wiring.WireQueue new_wire_queue =
                new Wiring.WireQueue ( this.wiring (),
                                       this.id,
                                       this.metadataID,
                                       new_carrier_ids );

            return new_wire_queue;
        }

        public final Wiring.WireQueue add (
                Wiring.Carrier ... carriers
                )
        {
            if ( ! this.checkCarriers ( carriers )
                       .isKept () )
            {
                throw new IllegalArgumentException (
                              "ERROR Cannot add"
                              + " carriers to "
                              + this
                              + ": failed check"
                              + " when trying to add "
                              + StringRepresentation.of (
                                    carriers,
                                    StringRepresentation.DEFAULT_ARRAY_LENGTH,
                                    StringRepresentation.DEFAULT_OBJECT_LENGTH ) );
            }

            final long [] new_carrier_ids =
                new long [ this.carrierIDs.length + carriers.length ];
            System.arraycopy ( this.carrierIDs, 0,
                               new_carrier_ids, 0, this.carrierIDs.length );
            for ( int c = 0; c < carriers.length; c ++ )
            {
                new_carrier_ids [ this.carrierIDs.length + c ] =
                    carriers [ c ].id;
            }

            final Wiring.WireQueue new_wire_queue =
                new Wiring.WireQueue ( this.wiring (),
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
                this.wiring ().carrierTable;
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
                throw new IllegalArgumentException (
                              "ERROR cannot remove "
                              + carrier_ids.length
                              + " carriers from "
                              + this
                              + " with only "
                              + this.carrierIDs.length
                              + " carriers in the queue: "
                              + StringRepresentation.of (
                                    carrier_ids,
                                    StringRepresentation.DEFAULT_ARRAY_LENGTH,
                                    StringRepresentation.DEFAULT_OBJECT_LENGTH ) );
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
                    throw new IllegalArgumentException (
                                  "ERROR Cannot remove"
                                  + " Carrier index "
                                  + index
                                  + ": Carrier ID "
                                  + remove_carrier_id
                                  + " from "
                                  + this
                                  + " ( "
                                  + StringRepresentation.of (
                                        this.carrierIDs,
                                        StringRepresentation.DEFAULT_ARRAY_LENGTH,
                                        StringRepresentation.DEFAULT_OBJECT_LENGTH )
                                  + " ): "
                                  + StringRepresentation.of (
                                        carrier_ids,
                                        StringRepresentation.DEFAULT_ARRAY_LENGTH,
                                        StringRepresentation.DEFAULT_OBJECT_LENGTH ) );
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
                new Wiring.WireQueue ( this.wiring (),
                                       this.id,
                                       this.metadataID,
                                       new_carrier_ids );
            return new_wire_queue;
        }

        public final Wiring.WireQueue removeAll ()
        {
            final long [] new_carrier_ids = new long [ 0 ];

            final Wiring.WireQueue new_wire_queue =
                new Wiring.WireQueue ( this.wiring (),
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
                                                     + " carriers in the queue" );
            }

            final int new_num_carriers =
                this.carrierIDs.length - (int) num_carriers;
            final long [] new_carrier_ids =
                new long [ new_num_carriers ];
            System.arraycopy ( this.carrierIDs, (int) num_carriers,
                               new_carrier_ids, 0, new_num_carriers );
            int c = 0;
            for ( long new_carrier_id : new_carrier_ids )
            {
                new_carrier_ids [ c ] = new_carrier_id;
                c ++;
            }

            final Wiring.WireQueue new_wire_queue =
                new Wiring.WireQueue ( this.wiring (),
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

        public final Wiring.Wire [] wires ()
        {
            final Wiring.Table<Wiring.Wire> wire_table =
                this.wiring ().wireTable;
            final Filter<Long> equals_my_id =
                new EqualTo<Long> ( this.id );
            final Wiring.Wire.WireQueueColumn wire_queue_column =
                (Wiring.Wire.WireQueueColumn)
                wire_table.column ( Wiring.Wire.WireQueueColumn.NAME );

            final Wiring.Wire [] wires =
                wire_table.where ( wire_queue_column,
                                   equals_my_id );

            return wires;
        }
    }

    public static class Leg
        extends Wiring.RowWithMetadata
    {
        public final long chipID;
        public final long terminalID;

        public static class ChipColumn
            extends Wiring.ForeignKey<Wiring.Leg, Wiring.Chip>
        {
            public static final String NAME = "chip";

            public ChipColumn (
                    Wiring wiring
                    )
            {
                super ( Wiring.Leg.ChipColumn.NAME, // name
                        wiring.chipTable );
            }

            @Override
            public final Long value (
                    Wiring.Leg row
                    )
            {
                return row.chipID;
            }
        }

        public static class TerminalColumn
            extends Wiring.ForeignKey<Wiring.Leg, Wiring.Terminal>
        {
            public static final String NAME = "terminal";

            public TerminalColumn (
                    Wiring wiring
                    )
            {
                super ( Wiring.Leg.TerminalColumn.NAME, // name
                        wiring.terminalTable );
            }

            @Override
            public final Long value (
                    Wiring.Leg row
                    )
            {
                return row.terminalID;
            }
        }

        public Leg (
                Wiring wiring,
                long id,
                long metadata_id,
                long chip_id,
                long terminal_id
                )
        {
            super ( wiring.legTable,  // table
                    id,               // row_id
                    metadata_id );    // metadata_id

            this.chipID = chip_id;
            this.terminalID = terminal_id;

            this.table.addColumns (
                new Wiring.Leg.ChipColumn ( wiring ),
                new Wiring.Leg.TerminalColumn ( wiring ) );
        }

        public Leg (
                Wiring wiring,
                long id,
                Wiring.Metadata metadata,
                Wiring.Chip chip,
                Wiring.Terminal terminal
                )
        {
            super ( wiring.legTable,  // table
                    id,               // row_id
                    metadata );       // metadata

            this.chipID = chip.id;
            this.terminalID = terminal.id;

            this.table.addColumns (
                new Wiring.Leg.ChipColumn ( wiring ),
                new Wiring.Leg.TerminalColumn ( wiring ) );
        }

        @Override
        protected boolean equalsRow (
                Wiring.Row row
                )
        {
            final Wiring.Leg that = (Wiring.Leg) row;
            if ( this.chipID != that.chipID )
            {
                return false;
            }
            else if ( this.terminalID != that.terminalID )
            {
                return false;
            }

            return true;
        }

        public long typeID ()
        {
            final Wiring.Terminal terminal = this.terminal ();
            return terminal.typeID;
        }

        public Wiring.Type type ()
        {
            final Wiring.Terminal terminal = this.terminal ();
            return terminal.type ();
        }

        public final FilterState checkClass (
                Class<?> data_class
                )
        {
            final Wiring.Terminal terminal = this.terminal ();
            return terminal.checkClass ( data_class );
        }

        public final FilterState check (
                Object value
                )
        {
            final Wiring.Terminal terminal = this.terminal ();
            return terminal.check ( value );
        }

        public final FilterState checkData (
                Wiring.Data data
                )
        {
            final Wiring.Terminal terminal = this.terminal ();
            return terminal.checkData ( data );
        }

        public final FilterState checkDataID (
                long data_id
                )
        {
            final Wiring.Terminal terminal = this.terminal ();
            return terminal.checkDataID ( data_id );
        }

        public final FilterState checkCarrier (
                Wiring.Carrier carrier
                )
        {
            final Wiring.Terminal terminal = this.terminal ();
            return terminal.checkCarrier ( carrier );
        }

        public final FilterState checkCarrierID (
                long carrier_id
                )
        {
            final Wiring.Terminal terminal = this.terminal ();
            return terminal.checkCarrierID ( carrier_id );
        }

        public final Wiring.Chip chip ()
        {
            final Wiring.Table<Wiring.Chip> chip_table =
                this.wiring ().chipTable;
            final Wiring.Chip chip =
                chip_table.row ( this.chipID );

            return chip;
        }

        public final boolean isWired ()
        {
            for ( Wiring.Wire wire : this.wires () )
            {
                if ( wire.isWired () )
                {
                    return true;
                }
            }

            return false;
        }

        public final String terminalName ()
        {
            return this.terminal ().name ();
        }

        public final Wiring.Terminal terminal ()
        {
            final Wiring.Table<Wiring.Terminal> terminal_table =
                this.wiring ().terminalTable;
            final Wiring.Terminal terminal =
                terminal_table.row ( this.terminalID );

            return terminal;
        }

        public final Wiring.Wire [] wires ()
        {
            final Wiring.Table<Wiring.Wire> wire_table =
                this.wiring ().wireTable;
            final Filter<Long> equals_my_id =
                new EqualTo<Long> ( this.id );
            final Wiring.Wire.LegColumn leg_column =
                (Wiring.Wire.LegColumn)
                wire_table.column ( Wiring.Wire.LegColumn.NAME );

            final Wiring.Wire [] wires =
                wire_table.where ( leg_column,
                                   equals_my_id );

            return wires;
        }

        public final Wiring.Carrier [] pull ()
        {
            final Wiring.Wire [] wires = this.wires ();
            if ( wires.length == 0 )
            {
                return new Wiring.Carrier [ 0 ];
            }
            else if ( wires.length == 1 )
            {
                final Wiring.Carrier [] carriers =
                    wires [ 0 ].pull ();
                return carriers;
            }

            final List<Wiring.Carrier []> carriers_list =
                new ArrayList<Wiring.Carrier []> ();
            int total_num_carriers = 0;
            for ( Wiring.Wire wire : wires )
            {
                final Wiring.Carrier [] carriers =
                    wire.pull ();
                if ( carriers.length > 0 )
                {
                    carriers_list.add ( carriers );
                    total_num_carriers += carriers.length;
                }
            }

            final Wiring.Carrier [] carriers;
            final int num_carriers_arrays = carriers_list.size ();
            if ( num_carriers_arrays == 0 )
            {
                carriers = new Wiring.Carrier [ 0 ];
            }
            else if ( num_carriers_arrays == 1 )
            {
                carriers = carriers_list.get ( 0 );
            }
            else
            {
                carriers = new Wiring.Carrier [ total_num_carriers ];
                int offset = 0;
                for ( Carrier [] from_one_wire : carriers_list )
                {
                    System.arraycopy ( from_one_wire, 0,
                                       carriers, offset, from_one_wire.length );
                    offset += from_one_wire.length;
                }
            }

            return carriers;
        }

        public final void push (
                Wiring.Carrier ... carriers
                )
        {
            final Wiring.Wire [] wires = this.wires ();
            for ( Wiring.Wire wire : wires )
            {
                wire.push ( carriers );
            }
        }
    }

    public static class Chip
        extends Wiring.RowWithData
    {
        public final long schematicID;
        public final long configurationDataID;

        public static class SchematicColumn
            extends Wiring.ForeignKey<Wiring.Chip, Wiring.Schematic>
        {
            public static final String NAME = "schematic";

            public SchematicColumn (
                    Wiring wiring
                    )
            {
                super ( Wiring.Chip.SchematicColumn.NAME, // name
                        wiring.schematicTable );
            }

            @Override
            public final Long value (
                    Wiring.Chip row
                    )
            {
                return row.schematicID;
            }
        }

        public static class ConfigurationDataColumn
            extends Wiring.ForeignKey<Wiring.Chip, Wiring.Data>
        {
            public static final String NAME = "configurationData";

            public ConfigurationDataColumn (
                    Wiring wiring
                    )
            {
                super ( Wiring.Chip.ConfigurationDataColumn.NAME, // name
                        wiring.dataTable );
            }

            @Override
            public final Long value (
                    Wiring.Chip row
                    )
            {
                return row.configurationDataID;
            }
        }

        public Chip (
                Wiring wiring,
                long id,
                long metadata_id,
                long data_id,
                long schematic_id,
                long configuration_data_id
                )
        {
            super ( wiring.chipTable,  // table
                    id,                // row_id
                    metadata_id,       // metadata_id
                    data_id );         // data_id

            this.schematicID = schematic_id;
            this.configurationDataID = configuration_data_id;

            this.table.addColumns (
                new Wiring.Chip.SchematicColumn ( wiring ),
                new Wiring.Chip.ConfigurationDataColumn ( wiring ) );
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
            super ( wiring.chipTable,  // table
                    id,                // row_id
                    metadata,          // metadata
                    data );            // data

            this.schematicID = schematic.id;

            this.configurationDataID = configuration_data.id;

            this.table.addColumns (
                new Wiring.Chip.SchematicColumn ( wiring ),
                new Wiring.Chip.ConfigurationDataColumn ( wiring ) );
        }

        @Override
        protected boolean equalsRow (
                Wiring.Row row
                )
        {
            final Wiring.Chip that = (Wiring.Chip) row;
            if ( this.schematicID != that.schematicID )
            {
                return false;
            }
            else if ( this.configurationDataID != that.configurationDataID )
            {
                return false;
            }

            return true;
        }

        public final Wiring.Chip start ()
        {
            this.wiring ().controlPlane.start ( this );
            return this;
        }

        public final Wiring.Chip stop ()
        {
            this.wiring ().controlPlane.stop ( this );
            return this;
        }

        public final Wiring.Carrier [] pull (
                Wiring.Wire wire
                )
        {
            return this.wiring ().controlPlane.pull ( this, wire );
        }

        public final Wiring.Chip push (
                Wiring.Wire wire,
                Wiring.Carrier [] carriers
                )
        {
            this.wiring ().controlPlane.push ( this, wire, carriers );
            return this;
        }

        public final Wiring.Data configuration ()
        {
            final Wiring.Table<Wiring.Data> data_table =
                this.wiring ().dataTable;
            final Wiring.Data configuration = data_table.row ( this.configurationDataID );

            return configuration;
        }

        public final Object configurationValue ()
        {
            final Wiring.Data configuration = this.configuration ();
            final Object value = configuration.value;
            return value;
        }

        public final <CONFIGURATION extends Object> CONFIGURATION
            configurationValue (
                Class<CONFIGURATION> configuration_class
                )
        {
            final Wiring.Data configuration = this.configuration ();
            final CONFIGURATION value = configuration.value ( configuration_class );
            return value;
        }

        public final Wiring.Data configure (
                Object configuration_value
                )
        {
            final Wiring.Data old_configuration = this.configuration ();
            final Wiring.Data new_configuration =
                old_configuration.update ( configuration_value );
            this.wiring ().dataTable.replace ( new_configuration );
            return new_configuration;
        }

        public final Wiring.Schematic schematic ()
        {
            final Wiring.Table<Wiring.Schematic> schematic_table =
                this.wiring ().schematicTable;
            final Wiring.Schematic schematic =
                schematic_table.row ( this.schematicID );
            return schematic;
        }

        public final Wiring.Leg [] legs ()
        {
            final Wiring.Table<Wiring.Leg> leg_table =
                this.wiring ().legTable;
            final Filter<Long> equals_my_id =
                new EqualTo<Long> ( this.id );
            final Wiring.Leg.ChipColumn chip_column =
                (Wiring.Leg.ChipColumn)
                leg_table.column ( Wiring.Leg.ChipColumn.NAME );

            final Wiring.Leg [] legs =
                leg_table.where ( chip_column,
                                  equals_my_id );

            return legs;
        }

        public final Wiring.Leg [] legs (
                String name
                )
        {
            final List<Wiring.Leg> legs_list =
                new ArrayList<Wiring.Leg> ();
            for ( Wiring.Leg leg : this.legs () )
            {
                final Wiring.Terminal terminal =
                    leg.terminal ();
                if ( name.equals ( terminal.name () ) )
                {
                    legs_list.add ( leg );
                }
            }

            final Wiring.Leg [] template =
                new Wiring.Leg [ legs_list.size () ];
            final Wiring.Leg [] legs =
                legs_list.toArray ( template );

            return legs;
        }

        public final Wiring.Leg [] legs (
                Pattern pattern
                )
        {
            final List<Wiring.Leg> legs_list =
                new ArrayList<Wiring.Leg> ();
            for ( Wiring.Leg leg : this.legs () )
            {
                final Wiring.Terminal terminal =
                    leg.terminal ();
                if ( pattern.matcher ( terminal.name () ).find () )
                {
                    legs_list.add ( leg );
                }
            }

            final Wiring.Leg [] template =
                new Wiring.Leg [ legs_list.size () ];
            final Wiring.Leg [] legs =
                legs_list.toArray ( template );

            return legs;
        }

        public final Wiring.Leg [] legs (
                Wiring.Terminal terminal
                )
        {
            final List<Wiring.Leg> legs_list =
                new ArrayList<Wiring.Leg> ();
            for ( Wiring.Leg leg : this.legs () )
            {
                if ( leg.terminalID == terminal.id )
                {
                    legs_list.add ( leg );
                }
            }

            final Wiring.Leg [] template =
                new Wiring.Leg [ legs_list.size () ];
            final Wiring.Leg [] legs =
                legs_list.toArray ( template );

            return legs;
        }
    }

    public static interface Circuit
    {
        public abstract Wiring.Schematic schematic (
                Wiring wiring
                );
        public abstract Wiring.Circuit start (
                Wiring.Chip chip
                );
        public abstract Wiring.Circuit stop (
                Wiring.Chip chip
                );
        public abstract Wiring.Carrier [] pull (
                Wiring.Chip chip,
                Wiring.Wire wire
                );
        public abstract Wiring.Circuit push (
                Wiring.Chip chip,
                Wiring.Wire wire,
                Wiring.Carrier [] carriers
                );
    }

    public static class Schematic
        extends Wiring.RowWithMetadata
    {
        public final Wiring.Circuit circuit;
        public final long configurationDataTypeID;
        public final long dataTypeID;

        public static class CircuitColumn
            extends Wiring.Column<Wiring.Schematic, Circuit>
        {
            public static final String NAME = "circuit";

            public CircuitColumn (
                    Wiring wiring
                    )
            {
                super ( Wiring.Schematic.CircuitColumn.NAME, // name
                        Wiring.Type.forClass ( wiring, // type
                                               Circuit.class ) );
            }

            @Override
            public final Circuit value (
                    Wiring.Schematic row
                    )
            {
                return row.circuit;
            }
        }

        public static class ConfigurationDataTypeColumn
            extends Wiring.ForeignKey<Wiring.Schematic, Wiring.Type>
        {
            public static final String NAME = "configurationDataType";

            public ConfigurationDataTypeColumn (
                    Wiring wiring
                    )
            {
                super ( Wiring.Schematic.ConfigurationDataTypeColumn.NAME, // name
                        wiring.typeTable );
            }

            @Override
            public final Long value (
                    Wiring.Schematic row
                    )
            {
                return row.configurationDataTypeID;
            }
        }

        public static class DataTypeColumn
            extends Wiring.ForeignKey<Wiring.Schematic, Wiring.Type>
        {
            public static final String NAME = "dataType";

            public DataTypeColumn (
                    Wiring wiring
                    )
            {
                super ( Wiring.Schematic.DataTypeColumn.NAME, // name
                        wiring.typeTable );
            }

            @Override
            public final Long value (
                    Wiring.Schematic row
                    )
            {
                return row.dataTypeID;
            }
        }

        public Schematic (
                Wiring wiring,
                long id,
                long metadata_id,
                Wiring.Circuit circuit,
                long configuration_data_type_id,
                long data_type_id
                )
        {
            super ( wiring.schematicTable,  // table
                    id,                     // row_id
                    metadata_id );          // metadata_id

            this.circuit = circuit;
            this.configurationDataTypeID = configuration_data_type_id;
            this.dataTypeID = data_type_id;

            this.table.addColumns (
                new Wiring.Schematic.CircuitColumn ( wiring ),
                new Wiring.Schematic.ConfigurationDataTypeColumn ( wiring ),
                new Wiring.Schematic.DataTypeColumn ( wiring ) );
        }

        public Schematic (
                Wiring wiring,
                long id,
                Wiring.Metadata metadata,
                Wiring.Circuit circuit,
                Wiring.Type configuration_data_type,
                Wiring.Type data_type
                )
        {
            super ( wiring.schematicTable,  // table
                    id,                     // row_id
                    metadata );             // metadata

            this.circuit = circuit;
            this.configurationDataTypeID = configuration_data_type.id;
            this.dataTypeID = data_type.id;

            this.table.addColumns (
                new Wiring.Schematic.CircuitColumn ( wiring ),
                new Wiring.Schematic.ConfigurationDataTypeColumn ( wiring ),
                new Wiring.Schematic.DataTypeColumn ( wiring ) );
        }

        @Override
        protected boolean equalsRow (
                Wiring.Row row
                )
        {
            final Wiring.Schematic that = (Wiring.Schematic) row;
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
            else if ( this.configurationDataTypeID != that.configurationDataTypeID )
            {
                return false;
            }
            else if ( this.dataTypeID != that.dataTypeID )
            {
                return false;
            }

            return true;
        }

        public final Wiring.Terminal [] terminals ()
        {
            final Wiring.Table<Wiring.Terminal> terminal_table =
                this.wiring ().terminalTable;
            final Filter<Long> equals_my_id =
                new EqualTo<Long> ( this.id );
            final Wiring.Terminal.SchematicColumn schematic_column =
                (Wiring.Terminal.SchematicColumn)
                terminal_table.column ( Wiring.Terminal.SchematicColumn.NAME );

            final Wiring.Terminal [] terminals =
                terminal_table.where ( schematic_column,
                                       equals_my_id );

            return terminals;
        }

        public final Wiring.Terminal [] terminals (
                String name
                )
        {
            final List<Wiring.Terminal> terminals_list =
                new ArrayList<Wiring.Terminal> ();
            for ( Wiring.Terminal terminal : this.terminals () )
            {
                if ( name.equals ( terminal.name () ) )
                {
                    terminals_list.add ( terminal );
                }
            }

            final Wiring.Terminal [] template =
                new Wiring.Terminal [ terminals_list.size () ];
            final Wiring.Terminal [] terminals =
                terminals_list.toArray ( template );

            return terminals;
        }

        public final Wiring.Terminal [] terminals (
                Pattern pattern
                )
        {
            final List<Wiring.Terminal> terminals_list =
                new ArrayList<Wiring.Terminal> ();
            for ( Wiring.Terminal terminal : this.terminals () )
            {
                if ( pattern.matcher ( terminal.name () ).find () )
                {
                    terminals_list.add ( terminal );
                }
            }

            final Wiring.Terminal [] template =
                new Wiring.Terminal [ terminals_list.size () ];
            final Wiring.Terminal [] terminals =
                terminals_list.toArray ( template );

            return terminals;
        }
    }

    public static class Terminal
        extends Wiring.RowWithMetadata
    {
        public final long schematicID;
        public final long typeID;

        public static class SchematicColumn
            extends Wiring.ForeignKey<Wiring.Terminal, Wiring.Schematic>
        {
            public static final String NAME = "schematic";

            public SchematicColumn (
                    Wiring wiring
                    )
            {
                super ( Wiring.Terminal.SchematicColumn.NAME, // name
                        wiring.schematicTable );
            }

            @Override
            public final Long value (
                    Wiring.Terminal row
                    )
            {
                return row.schematicID;
            }
        }

        public static class TypeColumn
            extends Wiring.ForeignKey<Wiring.Terminal, Wiring.Type>
        {
            public static final String NAME = "type";

            public TypeColumn (
                    Wiring wiring
                    )
            {
                super ( Wiring.Terminal.TypeColumn.NAME, // name
                        wiring.typeTable );
            }

            @Override
            public final Long value (
                    Wiring.Terminal row
                    )
            {
                return row.typeID;
            }
        }

        public Terminal (
                Wiring wiring,
                long id,
                long metadata_id,
                long schematic_id,
                long type_id
                )
        {
            super ( wiring.terminalTable,  // table
                    id,                    // row_id
                    metadata_id );         // metadata_id

            this.schematicID = schematic_id;
            this.typeID = type_id;

            this.table.addColumns (
                new Wiring.Terminal.SchematicColumn ( wiring ),
                new Wiring.Terminal.TypeColumn ( wiring ) );
        }

        public Terminal (
                Wiring wiring,
                long id,
                Wiring.Metadata metadata,
                Wiring.Schematic schematic,
                Wiring.Type type
                )
        {
            super ( wiring.terminalTable,  // table
                    id,                    // row_id
                    metadata );            // metadata

            this.schematicID = schematic.id;
            this.typeID = type.id;

            this.table.addColumns (
                new Wiring.Terminal.SchematicColumn ( wiring ),
                new Wiring.Terminal.TypeColumn ( wiring ) );
        }

        @Override
        protected boolean equalsRow (
                Wiring.Row row
                )
        {
            final Wiring.Terminal that = (Wiring.Terminal) row;
            if ( this.schematicID != that.schematicID )
            {
                return false;
            }
            else if ( this.typeID != that.typeID )
            {
                return false;
            }

            return true;
        }

        public final FilterState checkClass (
                Class<?> data_class
                )
        {
            final Wiring.Type type = this.type ();
            return type.checkClass ( data_class );
        }

        public final FilterState check (
                Object value
                )
        {
            final Wiring.Type type = this.type ();
            return type.check ( value );
        }

        public final FilterState checkData (
                Wiring.Data data
                )
        {
            final Wiring.Type type = this.type ();
            return type.checkData ( data );
        }

        public final FilterState checkDataID (
                long data_id
                )
        {
            final Wiring.Type type = this.type ();
            return type.checkDataID ( data_id );
        }

        public final FilterState checkCarrier (
                Wiring.Carrier carrier
                )
        {
            final Wiring.Type type = this.type ();
            return type.checkCarrier ( carrier );
        }

        public final FilterState checkCarrierID (
                long carrier_id
                )
        {
            final Wiring.Type type = this.type ();
            return type.checkCarrierID ( carrier_id );
        }

        public final Wiring.Type type ()
        {
            final Wiring.Table<Wiring.Type> type_table =
                this.wiring ().typeTable;
            final Wiring.Type type = type_table.row ( this.typeID );
            return type;
        }
    }

    public static class State
        extends Wiring.RowWithMetadata
    {
        public State (
                Wiring wiring,
                long id,
                long metadata_id
                )
        {
            super ( wiring.stateTable,  // table
                    id,                 // row_id
                    metadata_id );      // metadata_id
        }

        public State (
                Wiring wiring,
                long id,
                Wiring.Metadata metadata
                )
        {
            super ( wiring.stateTable,  // table
                    id,                 // row_id
                    metadata );         // metadata
        }

        @Override
        protected boolean equalsRow (
                Wiring.Row row
                )
        {
            // No extra fields to check.
            return true;
        }
    }





    public static final class NoControlPlane
        implements Wiring.Circuit
    {
        @Override
        public final Wiring.Schematic schematic (
                Wiring wiring
                )
        {
            final Wiring.Type no_type = Wiring.Type.none ( wiring );

            // NoControlPlane:
            final Wiring.Metadata schematic_metadata =
                new Wiring.Metadata (
                        wiring,             // wiring
                        -1L,                // id
                        wiring.namespaceID, // namespace_id
                        "no-control-plane"  // name
                        );                  // no tags for now
            final Wiring.Schematic schematic =
                new Wiring.Schematic (
                        wiring,        // wiring
                        -1L,           // id
                        schematic_metadata, // metadata
                        this,          // circuit
                        no_type,       // configuration_data_type
                        no_type );     // data_type

            wiring.metadataTable.add ( schematic_metadata );
            wiring.schematicTable.add ( schematic );

            return schematic;
        }

        @Override
        public final Wiring.Circuit start (
                Wiring.Chip chip
                )
        {
            final Wiring.Circuit circuit = chip.schematic ().circuit;
            circuit.start ( chip );
            return this;
        }

        @Override
        public final Wiring.Circuit stop (
                Wiring.Chip chip
                )
        {
            final Wiring.Circuit circuit = chip.schematic ().circuit;
            circuit.stop ( chip );
            return this;
        }

        @Override
        public final Wiring.Carrier [] pull (
                Wiring.Chip chip,
                Wiring.Wire wire
                )
        {
            final Wiring.Circuit circuit = chip.schematic ().circuit;
            return circuit.pull ( chip,
                                  wire );
        }

        @Override
        public final Wiring.Circuit push (
                Wiring.Chip chip,
                Wiring.Wire wire,
                Wiring.Carrier [] carriers
                )
        {
            final Wiring.Circuit circuit = chip.schematic ().circuit;
            circuit.push ( chip,
                           wire,
                           carriers );
            return this;
        }
    }




    public static class ChangeTablesEvent
        extends Wiring.AbstractEvent<Wiring, LinkedHashMap<Class<?>, Wiring.Table<?>>, LinkedHashMap<Class<?>, Wiring.Table<?>>>
    {
        public ChangeTablesEvent (
                Wiring host,
                String action,
                LinkedHashMap<Class<?>, Wiring.Table<?>> new_tables,
                LinkedHashMap<Class<?>, Wiring.Table<?>> old_tables
                )
        {
            super ( host, // wiring,
                    host,
                    action,
                    new_tables,
                    old_tables );
        }

        @Override
        public final void redo ()
        {
            synchronized ( this.host.lock )
            {
                this.assertEquals ( "redo", "tables",
                                    this.undoData,
                                    this.host.tables );
                this.host.tables.clear ();
                this.host.tables.putAll ( this.redoData );
            }

            this.wiring.sidechain ( Wiring.DoOrUndo.DO,
                                    this );
        }

        @Override
        public final void undo ()
        {
            synchronized ( this.host.lock )
            {
                this.assertEquals ( "undo", "tables",
                                    this.redoData,
                                    this.host.tables );
                this.host.tables.clear ();
                this.host.tables.putAll ( this.undoData );
            }

            this.wiring.sidechain ( Wiring.DoOrUndo.UNDO,
                                    this );
        }
    }

    public static class ForRowClassEvent<ROW extends Wiring.Row>
        extends Wiring.ChangeTablesEvent
    {
        private final Class<ROW> rowClass;
        private final Wiring.Table<ROW> table;

        public ForRowClassEvent (
                Wiring host,
                Class<ROW> row_class,
                Wiring.Table<ROW> table,
                LinkedHashMap<Class<?>, Wiring.Table<?>> new_tables,
                LinkedHashMap<Class<?>, Wiring.Table<?>> old_tables
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

        public final Wiring.Table<ROW> table ()
        {
            return this.table;
        }
    }


    // These tables are automatically added to this.tables
    // at Wiring constructor time:
    public final Wiring.Table<Wiring.Carrier> carrierTable;
    public final Wiring.Table<Wiring.CarrierState> carrierStateTable;
    public final Wiring.Table<Wiring.Chip> chipTable;
    public final Wiring.Table<Wiring.Data> dataTable;
    public final Wiring.Table<Wiring.Leg> legTable;
    public final Wiring.Table<Wiring.Metadata> metadataTable;
    public final Wiring.Table<Wiring.Namespace> namespaceTable;
    public final Wiring.Table<Wiring.Schematic> schematicTable;
    public final Wiring.Table<Wiring.State> stateTable;
    public final Wiring.Table<Wiring.Terminal> terminalTable;
    public final Wiring.Table<Wiring.Type> typeTable;
    public final Wiring.Table<Wiring.Wire> wireTable;
    public final Wiring.Table<Wiring.WireBundle> wireBundleTable;
    public final Wiring.Table<Wiring.WireQueue> wireQueueTable;

    // The control plane for this Wiring, which handles
    // calls to a Chip -- for example, to regulate flow,
    // and/or to gather metrics, and so on.
    // Wiring.NoControlPlane can be used for pass-through
    // to each Chip's circuit.
    public final Wiring.Circuit controlPlane;

    // The ID of the parent Wiring.Namespace in which
    // this Wiring operates.  Can be -1L for no parent.
    public final long parentNamespaceID;

    // The ID of this Wiring's namespace.  All toplevel
    // objects in this Wiring (Wiring.Carriers, Wiring.Chips,
    // Wiring.Wires, and so on) point their namespace IDs
    // to this.
    public final long namespaceID;

    private final Serializable lock = new String ( "lock" );

    // Synchronize these fields on lock:
    private final LinkedHashMap<Class<?>, Wiring.Table<?>> tables = new LinkedHashMap<Class<?>, Wiring.Table<?>> ();
    private final Stack<Wiring.AbstractEvent<?, ?, ?>> history = new Stack<Wiring.AbstractEvent<?, ?, ?>> ();


    public Wiring ()
    {
        this ( new Wiring.NoControlPlane (),
               -1L );
    }

    public Wiring (
            Wiring.Circuit control_plane
            )
    {
        this ( new Wiring.NoControlPlane (),
               -1L );
    }

    public Wiring (
            Wiring.Circuit control_plane,
            Wiring parent
            )
    {
        this ( new Wiring.NoControlPlane (),
               parent.namespaceID );
    }

    public Wiring (
            Wiring.Circuit control_plane,
            long parent_namespace_id
            )
    {
        carrierTable =
            this.forRowClass ( Wiring.Carrier.class );
        carrierStateTable =
            this.forRowClass ( Wiring.CarrierState.class );
        chipTable =
            this.forRowClass ( Wiring.Chip.class );
        legTable =
            this.forRowClass ( Wiring.Leg.class );
        dataTable =
            this.forRowClass ( Wiring.Data.class );
        metadataTable =
            this.forRowClass ( Wiring.Metadata.class );
        namespaceTable =
            this.forRowClass ( Wiring.Namespace.class );
        schematicTable =
            this.forRowClass ( Wiring.Schematic.class );
        terminalTable =
            this.forRowClass ( Wiring.Terminal.class );
        stateTable =
            this.forRowClass ( Wiring.State.class );
        typeTable =
            this.forRowClass ( Wiring.Type.class );
        wireTable =
            this.forRowClass ( Wiring.Wire.class );
        wireBundleTable =
            this.forRowClass ( Wiring.WireBundle.class );
        wireQueueTable =
            this.forRowClass ( Wiring.WireQueue.class );

        this.controlPlane = control_plane;
        this.parentNamespaceID = parent_namespace_id;

        Wiring.Type.forClass ( this,
                               Wiring.Table.class );

        this.namespaceID =
            this.createNamespace ( this.parentNamespaceID );
    }

    protected long createNamespace (
            long parent_namespace_id
            )
    {
        final String name;
        if ( parent_namespace_id < 0L )
        {
            name = "/";
        }
        else
        {
            name = this.getClass ().getSimpleName ();
        }

        final Wiring.Metadata wiring_metadata =
            new Wiring.Metadata (
                this,          // wiring
                -1L,           // id
                parent_namespace_id, // namespace_id
                name           // name
                );             // no tags for now
        final Wiring.Namespace wiring_namespace =
            new Wiring.Namespace (
                this,          // wiring
                -1L,           // id
                wiring_metadata  // metadata
                );

        this.metadataTable.add ( wiring_metadata );
        this.namespaceTable.add ( wiring_namespace );

        return wiring_namespace.id;
    }

    @SuppressWarnings("unchecked") // Cast Wiring.Table<?> -> Wiring.Table<ROW>
    public final <ROW extends Wiring.Row>
        Wiring.Table<ROW> forRowClass (
            Class<ROW> row_class
            )
    {
        final ForRowClassEvent<ROW> event;
        synchronized ( this.lock )
        {
            final LinkedHashMap<Class<?>, Wiring.Table<?>> old_tables =
                new LinkedHashMap<Class<?>, Wiring.Table<?>> ( this.tables );

            // SuppressWarnings("unchecked"):
            final Wiring.Table<ROW> existing_table = (Wiring.Table<ROW>)
                this.tables.get ( row_class );
            if ( existing_table == null )
            {
                final Wiring.Table<ROW> table = new Wiring.Table<ROW> ( this, row_class );
                this.tables.put ( row_class, table );

                final LinkedHashMap<Class<?>, Wiring.Table<?>> new_tables =
                    new LinkedHashMap<Class<?>, Wiring.Table<?>> ( this.tables );

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

    public final Wiring.Chip chip (
            Wiring.Circuit circuit,
            Object [] configuration_value,
            Object [] data_value
            )
    {
        return this.chip ( circuit,             // circuit
                           this.namespaceID,    // namespace_id
                           configuration_value, // conffiguration_value
                           data_value );        // data_value
    }

    public final Wiring.Chip chip (
            Wiring.Circuit circuit,
            Wiring.Namespace namespace,
            Object [] configuration_value,
            Object [] data_value
            )
    {
        return this.chip ( circuit,             // circuit
                           namespace.id,        // namespace_id
                           configuration_value, // configuration_value
                           data_value );        // data_value
    }

    public Wiring.Chip chip (
            Wiring.Circuit circuit,
            long namespace_id,
            Object [] configuration_value,
            Object [] data_value
            )
    {
        final Wiring.Schematic schematic =
            circuit.schematic ( this );

        final String chip_name = schematic.name ();

        final Wiring.Metadata chip_metadata =
            new Wiring.Metadata (
                this,          // wiring
                -1L,           // id
                namespace_id,  // namespace_id
                chip_name      // name
                );             // no tags for now
        final long configuration_type_id =
            schematic.configurationDataTypeID;
        final long data_type_id =
            schematic.dataTypeID;
        final Wiring.Data chip_configuration =
            new Wiring.Data (
                this,          // wiring
                -1L,           // id
                configuration_type_id, // type_id
                configuration_value ); // value
        final Wiring.Data chip_data =
            new Wiring.Data (
                this,          // wiring
                -1L,           // id
                data_type_id,  // type_id
                data_value ); // value
        final Wiring.Chip chip =
            new Wiring.Chip (
                this,           // wiring
                -1L,            // id
                chip_metadata,  // metadata
                schematic,      // schematic
                chip_configuration, // configuration_data
                chip_data       // data
                );

        // Namespace for the chip's legs:
        final Wiring.Namespace chip_namespace =
            new Wiring.Namespace (
                this,              // wiring
                -1L,               // id
                chip_metadata      // metadata
                );

        this.metadataTable.add ( chip_metadata );
        if ( chip_configuration != null )
        {
            this.dataTable.add ( chip_configuration );
        }
        if ( chip_data != null )
        {
            this.dataTable.add ( chip_data );
        }
        this.chipTable.add ( chip );
        this.namespaceTable.add ( chip_namespace );

        for ( Wiring.Terminal terminal : schematic.terminals () )
        {
            final String leg_name = terminal.name ();
            final Wiring.Metadata leg_metadata =
                new Wiring.Metadata (
                    this,          // wiring
                    -1L,           // id
                    chip_namespace.id, // namespace_id
                    leg_name       // name
                    );             // no tags for now
            final Wiring.Leg leg =
                new Wiring.Leg (
                    this,          // wiring
                    -1L,           // id
                    leg_metadata,  // metadata
                    chip,          // chip
                    terminal );    // terminal

            this.metadataTable.add ( leg_metadata );
            this.legTable.add ( leg );
        }

        return chip;
    }

    // Can be overridden to add (or replace or even remove)
    // custom tags.
    public void fillTags (
            Wiring.Metadata metadata,
            LinkedHashMap<String, Wiring.Tag> tags
            )
    {
        // Created, Modified, Tagged timestamps:
        Wiring.Tag tag_created = tags.get ( Wiring.Tag.Created.NAME );
        Wiring.Tag tag_modified = tags.get ( Wiring.Tag.Modified.NAME );
        Wiring.Tag tag_tagged = tags.get ( Wiring.Tag.Tagged.NAME );
        if ( tag_created == null
             || tag_modified == null
             || tag_tagged == null )
        {
            final long milliseconds =
                this.milliseconds ( metadata.table,
                                    metadata.id );

            if ( tag_created == null )
            {
                tag_created =
                    new Wiring.Tag.Created ( milliseconds );
                tags.put ( Wiring.Tag.Created.NAME, tag_created );
            }

            if ( tag_modified == null )
            {
                tag_modified =
                    new Wiring.Tag.Modified ( milliseconds );
                tags.put ( Wiring.Tag.Modified.NAME, tag_modified );
            }

            if ( tag_tagged == null )
            {
                tag_tagged =
                    new Wiring.Tag.Tagged ( milliseconds );
                tags.put ( Wiring.Tag.Tagged.NAME, tag_tagged );
            }
        }
    }

    public final void history (
            Wiring.AbstractEvent<?, ?, ?> event
            )
    {
        synchronized ( this.lock )
        {
            this.history.push ( event );
        }

        this.sidechain ( Wiring.DoOrUndo.DO,
                         event );
    }

    // @return Time in milliseconds since UTC 0.
    //         Typically used for Wiring.Tag.Created,
    //         Modified, Tagged, etc.
    //         Can be overridden during test runs
    //         to generate consistent timestamps.
    public long milliseconds (
            Wiring.Table<? extends Wiring.Row> table,
            long row_id
            )
    {
        return System.currentTimeMillis ();
    }

    public final Wiring.Namespace namespace ()
    {
        final Wiring.Namespace wiring_namespace =
            this.namespaceTable.row ( this.namespaceID );
        return wiring_namespace;
    }

    // @return Time in nanoseconds since some arbitrary point.
    //         Typically used as a unique ID.
    //         Can be overridden during test runs to generate
    //         consistent timestamps.
    public long nanoseconds (
            Wiring.Table<? extends Wiring.Row> table,
            long row_id
            )
    {
        return System.nanoTime ();
    }

    
    /** Triggers any side-effects. */
    public <HOST extends Object, REDO extends Object, UNDO extends Object> void
        sidechain (
            Wiring.DoOrUndo do_or_undo,
            Wiring.AbstractEvent<HOST, REDO, UNDO> event
            )
    {
    }

    @SuppressWarnings("unchecked") // Unchecked generic array creation for varargs And(...) constructor.
    public final Wiring.Type typeRefine (
            Wiring.Type type_base_or_null,
            String refinement_name,
            Wiring.Type type_refiner
            )
    {
        if ( type_base_or_null == null )
        {
            return type_refiner;
        }

        // Definitely not null.
        final Wiring.Type type_base = type_base_or_null;

        final Class<?> class_base = type_base.instanceClass;
        final String name_base = type_base.name ();
        final Class<?> class_refiner = type_refiner.instanceClass;
        final String name_refiner = type_refiner.name ();
        final Class<?> class_common;
        final String maybe_name_common;
        if ( class_base == class_refiner )
        {
            class_common = class_base;
            maybe_name_common = null; // Decide based on filters.
        }
        else if ( class_base.isAssignableFrom ( class_refiner ) )
        {
            class_common = class_refiner;
            maybe_name_common = name_refiner;
        }
        else if ( class_refiner.isAssignableFrom ( class_base ) )
        {
            class_common = class_base;
            maybe_name_common = name_base;
        }
        else
        {
            throw new IllegalArgumentException ( "ERROR Cannot reconcile"
                                                 + " instance classes "
                                                 + class_base.getSimpleName ()
                                                 + " and "
                                                 + class_refiner.getSimpleName ()
                                                 + " while refining "
                                                 + type_base
                                                 + " with "
                                                 + refinement_name
                                                 + " "
                                                 + type_refiner );
        }

        final Filter<Object> filter_base = type_base.instanceFilter;
        final Filter<Object> filter_refiner = type_refiner.instanceFilter;
        final Filter<Object> filter_common;
        final String name_common;
        if ( filter_base instanceof KeepAll )
        {
            if ( filter_refiner instanceof DiscardAll )
            {
                throw new IllegalArgumentException ( "ERROR Cannot reconcile"
                                                     + " instance filters "
                                                     + filter_base
                                                     + " and "
                                                     + filter_refiner
                                                     + " while refining "
                                                     + type_base
                                                     + " with "
                                                     + refinement_name
                                                     + " "
                                                     + type_refiner );
            }

            filter_common = filter_refiner;

            if ( maybe_name_common != null )
            {
                name_common = maybe_name_common;
            }
            else
            {
                name_common = name_refiner;
            }
        }
        else if ( filter_base instanceof DiscardAll )
        {
            if ( ! ( filter_refiner instanceof DiscardAll ) )
            {
                throw new IllegalArgumentException ( "ERROR Cannot reconcile"
                                                     + " instance filters "
                                                     + filter_base
                                                     + " and "
                                                     + filter_refiner
                                                     + " while refining "
                                                     + type_base
                                                     + " with "
                                                     + refinement_name
                                                     + " "
                                                     + type_refiner );
            }

            filter_common = filter_base;

            if ( maybe_name_common != null )
            {
                name_common = maybe_name_common;
            }
            else
            {
                name_common = name_base;
            }
        }
        else if ( filter_refiner instanceof DiscardAll )
        {
            throw new IllegalArgumentException ( "ERROR Cannot reconcile"
                                                 + " instance filters "
                                                 + filter_base
                                                 + " and "
                                                 + filter_refiner
                                                 + " while refining "
                                                 + type_base
                                                 + " with "
                                                 + refinement_name
                                                 + " "
                                                 + type_refiner );
        }
        else if ( filter_base.equals ( filter_refiner ) )
        {
            filter_common = filter_base;

            if ( maybe_name_common != null )
            {
                name_common = maybe_name_common;
            }
            else
            {
                name_common = name_base;
            }
        }
        else
        {
            // SuppressWarnings("unchecked"): unchecked generic array creation for varargs:
            filter_common = new And<Object> ( filter_base,
                                              filter_refiner );

            if ( maybe_name_common != null )
            {
                name_common = maybe_name_common;
            }
            else
            {
                name_common = name_refiner + "-" + name_refiner;
            }
        }

        final long greatest_min;
        if ( type_refiner.min >= type_base.min )
        {
            greatest_min = type_refiner.min;
        }
        else
        {
            greatest_min = type_base.min;
        }

        final long least_max;
        if ( type_refiner.max >= type_base.max )
        {
            least_max = type_refiner.max;
        }
        else
        {
            least_max = type_base.max;
        }

        final Wiring.Metadata type_metadata =
            new Wiring.Metadata (
                this,               // wiring
                -1L,                // id
                this.namespaceID,   // namespace_id
                name_common         // name
                );                  // no tags for now
        final Wiring.Type type =
            new Wiring.Type (
                this,              // wiring
                -1L,               // id
                type_metadata,     // metadata
                class_common,      // instance_class
                filter_common,     // instance_filter
                greatest_min,      // min
                least_max );       // max

        this.metadataTable.add ( type_metadata );
        this.typeTable.add ( type );

        return type;
    }

    public final Wiring.WireBundle wire (
            Wiring.Leg [] ... legs
            )
    {
        final StringBuilder wire_bundle_name =
            new StringBuilder ();
        Wiring.Type common_type = null;

        final String [] [] chip_names = new String [ legs.length ] [];
        final String [] [] leg_names = new String [ legs.length ] [];
        final String [] [] wire_names = new String [ legs.length ] [];
        for ( int a = 0; a < legs.length; a ++ )
        {
            chip_names [ a ] = new String [ legs [ a ].length ];
            leg_names [ a ] = new String [ legs [ a ].length ];
            wire_names [ a ] = new String [ legs [ a ].length ];
            for ( int l = 0; l < legs [ a ].length; l ++ )
            {
                final Wiring.Leg leg = legs [ a ] [ l ];

                chip_names [ a ] [ l ] = leg.chip ().name ();
                leg_names [ a ] [ l ] = leg.name ();
                wire_names [ a ] [ l ] =
                    chip_names [ a ] [ l ] + "-" + leg_names [ a ] [ l ];

                // Throws IllegalArgumentException:
                common_type =
                    typeRefine ( common_type,           // type_base
                                 leg_names [ a ] [ l ], // refinement_name
                                 leg.type () );         // type_refiner

                if ( wire_bundle_name.length () > 0 )
                {
                    wire_bundle_name.append ( "--" );
                }
                wire_bundle_name.append ( wire_names [ a ] [ l ] );
            }
        }

        // Wiring.WireBundleName will be something like:
        //     chip0-leg0--chip1-leg1--chip2-leg2--...--chipN-legN
        // Common case is exactly 2 legs:
        //     chip0-leg0--chip1-leg1
        final Wiring.Metadata wire_bundle_metadata =
            new Wiring.Metadata (
                this,                        // wiring
                -1L,                         // id
                this.namespaceID,            // namespace_id
                wire_bundle_name.toString () // name
                );                           // no tags for now
        final WireBundle wire_bundle =
            new Wiring.WireBundle ( this,                 // wiring
                                    -1L,                  // id
                                    wire_bundle_metadata, // metadata
                                    common_type );        // type

        // Namespace for the wires in the bundle:
        final Wiring.Namespace wire_bundle_namespace =
            new Wiring.Namespace (
                this,                // wiring
                -1L,                 // id
                wire_bundle_metadata // metadata
                );

        this.metadataTable.add ( wire_bundle_metadata );
        this.wireBundleTable.add ( wire_bundle );
        this.namespaceTable.add ( wire_bundle_namespace );

        for ( int a = 0; a < legs.length; a ++ )
        {
            for ( int l = 0; l < legs [ a ].length; l ++ )
            {
                final Wiring.Wire wire =
                    this.wire ( wire_bundle,            // wire_bundle
                                wire_bundle_namespace,  // wire_bundle_namespace
                                wire_names [ a ] [ l ], // leg_name
                                legs [ a ] [ l ] );     // leg
            }
        }

        return wire_bundle;
    }

    private final Wiring.Wire wire (
            Wiring.WireBundle wire_bundle,
            Wiring.Namespace wire_bundle_namespace,
            String wire_name,
            Wiring.Leg leg
            )
    {
        final Wiring.Metadata wire_metadata =
            new Wiring.Metadata (
                    this,                  // wiring
                    -1L,                   // id
                    wire_bundle_namespace, // namespace
                    wire_name              // name
                    ); // No Wiring.Tags for now.
        final Wiring.Namespace wire_namespace =
            new Wiring.Namespace (
                this,            // wiring
                -1L,             // id
                wire_metadata ); // metadata

        final Wiring.Metadata wire_queue_metadata =
            new Wiring.Metadata (
                    this,           // wiring
                    -1L,            // id
                    wire_namespace, // namespace
                    wire_name       // name
                    ); // No Wiring.Tags for now.
        final Wiring.WireQueue wire_queue =
            new Wiring.WireQueue (
                    this,               // wiring
                    -1L,                // id
                    wire_queue_metadata // metadata
                    ); // No Wiring.Carriers in the queue to start.

        final Wiring.Wire wire =
            new Wiring.Wire (
                    this,          // wiring
                    -1L,           // id
                    wire_metadata, // metadata
                    wire_bundle,   // wire_bundle
                    leg,           // leg
                    wire_queue );  // wire_queue

        this.metadataTable.add ( wire_metadata );
        this.namespaceTable.add ( wire_namespace );
        this.metadataTable.add ( wire_queue_metadata );
        this.wireQueueTable.add ( wire_queue );
        this.wireTable.add ( wire );

        return wire;
    }
}
