package musaico.kernel_modules.drivers.sql;


import musaico.i18n.exceptions.I18nIllegalArgumentException;
import musaico.i18n.exceptions.I18nIllegalStateException;

import musaico.io.Marker;
import musaico.io.Path;
import musaico.io.Position;
import musaico.io.Reference;

import musaico.buffer.Buffer;
import musaico.buffer.BufferException;

import musaico.field.Field;

import musaico.io.markers.RecordEnd;
import musaico.io.markers.RecordStart;

import musaico.io.positions.EndPosition;
import musaico.io.positions.StartPosition;

import musaico.io.references.SimpleSoftReference;

import musaico.kernel.driver.Driver;

import musaico.kernel.driver.service.DriverGraph;
import musaico.kernel.driver.service.DriverRequests;

import musaico.kernel.module.Module;

import musaico.kernel.objectsystem.Cursor;

import musaico.service.ServiceContract;

import musaico.service.contracts.AnythingGoes;

import musaico.state.Label;
import musaico.state.Machine;
import musaico.state.SimpleMachine;
import musaico.state.TraversalException;


/**
 * <p>
 * A driver which produces SQL statements (as Strings).
 * </p>
 *
 * <p>
 * For example, a "read" might result in a "SELECT" statement,
 * or perhaps a "SHOW TABLES" statement, and so on, depending
 * on the Position being read from.  On the other hand, a
 * "write" might result in an "INSERT" statement, or a "CREATE TABLE"
 * statement, and so on, depending on the Position being written
 * to.
 * </p>
 *
 * <p>
 * The records in a SQL driver are as follows:
 * </p>
 *
 * <pre>
 *     +----------------+
 *     |     Server     | Host, port, vendor
 *     +----------------+
 *     |    Database    | Database name
 *     +----------------+
 *     |     Table      | Table name
 *     +----------------+
 *     |      Row       | Row #
 *     +----------------+
 *     |     Column     | Column name
 *     +----------------+
 * </pre>
 *
 * <p>
 * The data returned by reading looks something like:
 * </p>
 *
 * <pre>
 *     +----------------------------------------------------------------------+
 *     | All servers | All DBs on server0 | All tables in DB0 | All rows tab0 |
 *     +----------------------------------------------------------------------+
 * </pre>
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
 * <pre>
 * Copyright (c) 2009, 2010, 2011 Johann Tienhaara
 * All rights reserved.
 *
 * This file is part of Musaico.
 *
 * Musaico is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Musaico is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Musaico.  If not, see &lt;http://www.gnu.org/licenses/&gt;.
 * </pre>
 */
public class SQL
    implements Driver
{
    /** The index into the label stack of the server to read/write: */
    public static final int SERVER = 0;

    /** The index into the label stack of the database to read/write: */
    public static final int DATABASE = 1;

    /** The index into the label stack of the table to read/write: */
    public static final int TABLE = 2;

    /** The index into the label stack of the row to read/write: */
    public static final int ROW = 3;

    /** The index into the label stack of the column to read/write: */
    public static final int COLUMN = 4;


    /** The Module which loaded this driver in, and which provides memory
     *  management and security and so on for this driver. */
    private final Module module;

    /** The state machine depicting the state we are in (INITIALIZED,
     *  CONFIGURED, and so on). */
    private final Machine machine;


    /**
     * <p>
     * Creates a new SQL driver, loaded by the specified Module.
     * </p>
     *
     * @param module The module which loaded this driver in, and
     *               which provides memory management, security,
     *               logging, and so on.  Must not be null.
     */
    public SQL (
                Module module
                )
    {
        if ( module == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create SQL driver with module [%module%]",
                                                     "module", module );
        }

        this.module = module;

        this.machine = new SimpleMachine ( DriverGraph.GRAPH );

        try
        {
            this.machine.transition ();
        }
        catch ( TraversalException e )
        {
            throw new I18nIllegalStateException ( "Cannot transition into the DriverGraph starting state",
                                                  "cause", e );
        }
    }


    /**
     * @see musaico.kernel.driver.Driver#autoConfigure()
     */
    public void autoConfigure ()
        throws I18nSQLException
    {
        // Do nothing.
        this.transition ( DriverRequests.AUTO_CONFIGURE );
    }


    /**
     * @see musaico.kernel.driver.Driver#close(Cursor)
     */
    public void close (
                       Cursor cursor
                       )
        throws I18nSQLException
    {
        // Do nothing.
        this.transition ( DriverRequests.CLOSE );
    }


    /**
     * @see musaico.kernel.driver.Driver#configure(Buffer)
     */
    public void configure (
                           Buffer configuration_buffer
                           )
        throws I18nSQLException
    {
        // Do nothing.
        this.transition ( DriverRequests.CONFIGURE );
    }


    /**
     * @see musaico.kernel.driver.Driver#configureExpectations()
     */
    public ServiceContract configureExpectations ()
    {
        return new AnythingGoes ();
    }


    /**
     * @see musaico.kernel.driver.Driver#initialize()
     */
    public void initialize ()
        throws I18nSQLException
    {
        // Do nothing.
        this.transition ( DriverRequests.INITIALIZE );
    }


    /**
     * @see musaico.kernel.driver.Driver#open(Cursor)
     */
    public void open (
                      Cursor cursor
                      )
        throws I18nSQLException
    {
        this.transition ( DriverRequests.OPEN );

        cursor.position ( new StartPosition () );
    }


    /**
     * @see musaico.kernel.driver.Driver#read(Cursor,Buffer,Region)
     */
    public void read (
                      Cursor cursor,
                      Buffer buffer,
                      Region read_into_region
                      )
        throws I18nSQLException
    {
        this.transition ( DriverRequests.READ );

        final Position position = cursor.position ();
        String [] sql_names = this.sqlNames ( cursor, position );

        int sql_type = sql_names.length - 1;

        final String sql;
        String table_name;  // For use inside the switch only.
        String row_num;     // For use inside the switch only.
        String column_name; // For use inside the switch only.
        switch ( sql_type )
        {
        case SQL.SERVER:
            sql = "SHOW DATABASES;";
            break;

        case SQL.DATABASE:
            sql = "SHOW TABLES;";
            break;

        case SQL.TABLE:
            table_name = sql_names [ SQL.TABLE ];
            sql = "SELECT COUNT( * ) FROM " + table_name + ";";
            break;

        case SQL.ROW:
            table_name = sql_names [ SQL.TABLE ];
            sql = "DESCRIBE TABLE " + table_name + ";";
            break;

        case SQL.COLUMN:
            table_name = sql_names [ SQL.TABLE ];
            row_num = sql_names [ SQL.ROW ];
            column_name = sql_names [ SQL.COLUMN ];
            sql = "SELECT " + column_name + " FROM " + table_name
                + " OFFSET " + row_num + " LIMIT 1;";
            break;

        default:
            throw new I18nSQLException ( "Cannot access SQL driver [%sql%] unknown position [%position%]",
                                         "sql", this,
                                         "position", position );
        }

        try
        {
            Field f_sql_server =
                buffer.space ().environment ().create ( "sql_server",
                                                        sql_names [ SQL.SERVER ] );
            buffer.tools ().append ( new StartPosition (), f_sql_server );

            if ( sql_type >= SQL.DATABASE )
            {
                Field f_sql_database =
                    buffer.space ().environment ().create ( "sql_database",
                                                            sql_names [ SQL.DATABASE ] );
            }

            Field f_sql = buffer.space ().environment ().create ( "sql", sql );
            buffer.tools ().append ( new StartPosition (), f_sql );

            // We have no way of adjusting the position of the Cursor.
            // If we're on a stack, though, another driver will do that
            // for us.
        }
        catch ( BufferException e )
        {
            throw new I18nSQLException ( "Failed to read from SQLDriver [%sql%]",
                                         "sql", this,
                                         "cause", e );
        }
    }


    /**
     * @see musaico.kernel.driver.Driver#seek(Cursor,Position)
     */
    public Position seek (
                          Cursor cursor,
                          Position position
                          )
        throws I18nSQLException
    {
        this.transition ( DriverRequests.SEEK );

        String [] sql_names = this.sqlNames ( cursor, position );

        cursor.position ( position ); // !!!!!!!!!!!!!!!

        return position;
    }


    /**
     * @see musaico.kernel.driver.Driver#shutdown()
     */
    public void shutdown ()
        throws I18nSQLException
    {
        // Do nothing.
        this.transition ( DriverRequests.SHUTDOWN );
    }



    /**
     * <p>
     * Returns the String [] stack of server name, database name,
     * table, row, column, and so on equivalent to any old
     * Position (if possible).
     * </p>
     *
     * <p>
     * For example, <code> sqlNames ( new StartPosition () ) </code>
     * will return a String [] array containing only the server name.
     * (A <code> read () </code> from that position
     * would result in something along the lines of "SHOW DATABASES;"
     * as output.)
     * </p>
     *
     * @param cursor The cursor for which the String [] name stack is being
     *               requested.  For error message purposes only.
     *               Can be null.
     * @param position The position which will be translated
     *                 to a String [] stack of names.  Must not be null.
     *
     * @return The String [] stack of (server, database, table, row,
     *         column, and so on) names.  Never null.
     *
     * @throws I18nSQLException If the position cannot be translated.
     */
    public String [] sqlNames (
                               Cursor cursor,
                               Position position
                               )
        throws I18nSQLException
    {
        if ( position == null )
        {
            throw new I18nSQLException ( "SQL driver [%sql%] cannot create a stack of names from cursor [%cursor%] position [%position%]",
                                         "sql", this,
                                         "cursor", cursor,
                                         "position", position );
        }

        if ( position instanceof StartPosition )
        {
            return new String []
                {
                    "!!!serverfixme1"
                };
        }
        else if ( position instanceof EndPosition )
        {
            return new String []
                {
                    "!!!serverfixme2"
                };
        }
        else if ( position instanceof Marker )
        {
            Marker marker = (Marker) position;
            String marker_name = "" + marker.label ();
            marker_name = "!!!serverfixme3/" + marker_name;
            marker_name =
                marker_name
                .replace ( "/start$", "" )
                .replace ( "/end$", "" );
            return marker_name.split ( "/" );
        }

        // Don't know what to do with that type of Position.
        throw new I18nSQLException ( "SQL driver [%sql%] cannot create a stack of names from cursor [%cursor%] position [%position%]",
                                     "sql", this,
                                     "cursor", cursor,
                                     "position", position );
    }


    /**
     * @see musaico.kernel.driver.Driver#state()
     */
    public Reference state ()
    {
        return new SimpleSoftReference<String> ( "" + this.machine.state () );
    }


    /**
     * <p>
     * Tries to transition based on the specified trigger, and
     * throws an I18nSQLException if we can't.
     * </p>
     *
     * @param trigger The trigger causing the transition.
     *                Must not be null.
     *
     * @throws I18nSQLException If anything goes wrong
     *                          (for example, the starting
     *                          state cannot be transitioned
     *                          with the specified trigger).
     */
    protected void transition (
                               Label trigger
                               )
        throws I18nSQLException
    {
        try
        {
            this.machine.transition ( trigger );
        }
        catch ( TraversalException e )
        {
            throw new I18nSQLException ( "SQL driver [%driver%] cannot handle [%trigger%]",
                                         "driver", this,
                                         "trigger", trigger,
                                         "cause", e );
        }
    }


    /**
     * @see musaico.kernel.driver.Driver#write(musaico.kernel.objectsystem.Cursor,musaico.buffer.Buffer,musaico.io.Region)
     */
    public void write (
                       Cursor cursor,
                       Buffer buffer,
                       Region write_from_region
                       )
        throws I18nSQLException
    {
        this.transition ( DriverRequests.WRITE );

        final Position position = cursor.position ();
        String [] sql_names = this.sqlNames ( cursor, position );

        int sql_type = sql_names.length - 1;

        final String sql;
        String table_name;  // For use inside the switch only.
        String row_num;     // For use inside the switch only.
        String column_name; // For use inside the switch only.
        switch ( sql_type )
        {
        case SQL.SERVER:
            // !!! GO THROUGH CREATING A DATABASE FOR EACH FIELD/RECORD
            // !!! IN THE INPUT BUFFER!!!
            sql = "!!!SHOW DATABASES;";
            break;

        case SQL.DATABASE:
            sql = "SHOW TABLES;";
            break;

        case SQL.TABLE:
            table_name = sql_names [ SQL.TABLE ];
            sql = "SELECT COUNT( * ) FROM " + table_name + ";";
            break;

        case SQL.ROW:
            table_name = sql_names [ SQL.TABLE ];
            sql = "DESCRIBE TABLE " + table_name + ";";
            break;

        case SQL.COLUMN:
            table_name = sql_names [ SQL.TABLE ];
            row_num = sql_names [ SQL.ROW ];
            column_name = sql_names [ SQL.COLUMN ];
            sql = "SELECT " + column_name + " FROM " + table_name
                + " OFFSET " + row_num + " LIMIT 1;";
            break;

        default:
            throw new I18nSQLException ( "Cannot access SQL driver [%sql%] unknown position [%position%]",
                                         "sql", this,
                                         "position", position );
        }

        try
        {
            Field f_sql_server =
                buffer.space ().environment ().create ( "sql_server",
                                                        sql_names [ SQL.SERVER ] );
            buffer.tools ().append ( new StartPosition (), f_sql_server );

            if ( sql_type >= SQL.DATABASE )
            {
                Field f_sql_database =
                    buffer.space ().environment ().create ( "sql_database",
                                                            sql_names [ SQL.DATABASE ] );
            }

            Field f_sql = buffer.space ().environment ().create ( "sql", sql );
            buffer.tools ().append ( new StartPosition (), f_sql );

            // We have no way of adjusting the position of the Cursor.
            // If we're on a stack, though, another driver will do that
            // for us.
        }
        catch ( BufferException e )
        {
            throw new I18nSQLException ( "Failed to write to SQLDriver [%sql%]",
                                         "sql", this,
                                         "cause", e );
        }
    }
}
