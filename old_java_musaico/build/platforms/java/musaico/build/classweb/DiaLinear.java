package musaico.build.classweb;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * <p>
 * An in-memory representation of a Dia (a drawing program) diagram object.
 * </p>
 */
public class DiaLinear
    extends DiaObject
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * A connection by some line's endpoint to another object.
     * </p>
     */
    public static class Connection
        extends DiaChunk.XML
    {
        private static final long serialVersionUID =
            DiaLinear.serialVersionUID;

        public static enum Point
        {
            TOP_LEFT ( 0 ),
            TOP_CENTRE ( 1 ),
            TOP_RIGHT ( 2 ),
            CENTRE_LEFT ( 3 ),
            CENTRE_RIGHT ( 4 ),
            BOTTOM_LEFT ( 5 ),
            BOTTOM_CENTRE ( 6 ),
            BOTTOM_RIGHT ( 7 );
            private final int xml;
            private Point (
                           int xml
                           )
            {
                this.xml = xml;
            }
            public final String toXML ()
            {
                return "" + this.xml;
            }
        }

        public final int handle;
        public final DiaObject to;
        public final Point point;

        public Connection (
                           int handle,
                           DiaObject to,
                           DiaLinear.Connection.Point point
                           )
        {
            super ( "connection" + handle, "" );

            this.handle = handle;
            this.to = to;
            this.point = point;
        }

        /**
         * @see musaico.build.classweb.DiaChunk.XML.content()
         */
        @Override
        public final String content ()
        {
            final StringBuilder sbuf = new StringBuilder ();
            sbuf.append ( "        <dia:connection handle=\"" );
            sbuf.append ( "" + this.handle );
            sbuf.append ( "\" to=\"O" );
            sbuf.append ( "" + this.to.id () );
            sbuf.append ( "\" connection=\"" );
            sbuf.append ( this.point.toXML () );
            sbuf.append ( "\"/>\n" );

            return sbuf.toString ();
        }
    }


    /**
     * <p>
     * All connections by some line's endpoint(s) to other objects.
     * </p>
     */
    public static class Connections
        extends DiaChunk.XML
    {
        private static final long serialVersionUID =
            DiaLinear.serialVersionUID;

        public final List<DiaLinear.Connection> connections;

        public Connections (
                            DiaLinear.Connection ... connections
                            )
        {
            super ( "connections", "" );

            this.connections = new ArrayList<DiaLinear.Connection> ();
            for ( DiaLinear.Connection connection : connections )
            {
                this.connections.add ( connection );
            }
        }

        /**
         * @see musaico.build.classweb.DiaChunk.XML.content()
         */
        @Override
        public final String content ()
        {
            final StringBuilder sbuf = new StringBuilder ();
            sbuf.append ( "      <dia:connections>\n" );
            for ( DiaLinear.Connection connection : this.connections )
            {
                sbuf.append ( connection.content () );
            }
            sbuf.append ( "      </dia:connections>\n" );

            return sbuf.toString ();
        }
    }


    public static final String connections = "connections";
    public static final DiaLinear.Connections default_connections =
        new DiaLinear.Connections ();


    private DiaLinear.Connections current_connections;

    public DiaLinear (
                      DiaChunk.XML ... contents
                      )
    {
        super (
               createObjectXML ( contents )
               );

        final List<DiaChunk.XML> xml_chunks = this.get ();
        for ( DiaChunk.XML xml_chunk : xml_chunks )
        {
            if ( xml_chunk instanceof DiaLinear.Connections )
            {
                this.current_connections = (DiaLinear.Connections)
                    xml_chunk;
                break;
            }
        }
    }


    private static final DiaChunk.XML [] createObjectXML (
                                                          DiaChunk.XML [] contents
                                                          )
    {
        final DiaChunk.XML [] xml = new DiaChunk.XML [ contents.length + 1 ];

        System.arraycopy ( contents, 0,
                           xml, 0, contents.length );

        xml [ xml.length - 1 ] = new DiaLinear.Connections ();

        return xml;
    }


    public DiaLinear connect (
                              DiaObject from,
                              DiaObject to
                              )
    {
        final double diff_x = to.obj_pos () [ 0 ] - from.obj_pos () [ 0 ];
        final double diff_y = to.obj_pos () [ 1 ] - from.obj_pos () [ 1 ];
        final DiaLinear.Connection.Point from_point;
        final DiaLinear.Connection.Point to_point;
        if ( diff_y == 0D )
        {
            if ( diff_x < 0D )
            {
                to_point = DiaLinear.Connection.Point.CENTRE_RIGHT;
                from_point = DiaLinear.Connection.Point.CENTRE_LEFT;
            }
            else
            {
                to_point = DiaLinear.Connection.Point.CENTRE_LEFT;
                from_point = DiaLinear.Connection.Point.CENTRE_RIGHT;
            }
        }
        else
        {
            final double ratio = Math.abs ( diff_x / diff_y );
            if ( ratio < 0.5D )
            {
                if ( diff_y < 0D )
                {
                    to_point = DiaLinear.Connection.Point.BOTTOM_CENTRE;
                    from_point = DiaLinear.Connection.Point.TOP_CENTRE;
                }
                else
                {
                    to_point = DiaLinear.Connection.Point.TOP_CENTRE;
                    from_point = DiaLinear.Connection.Point.BOTTOM_CENTRE;
                }
            }
            else if ( ratio > 2D )
            {
                if ( diff_x < 0D )
                {
                    to_point = DiaLinear.Connection.Point.CENTRE_RIGHT;
                    from_point = DiaLinear.Connection.Point.CENTRE_LEFT;
                }
                else
                {
                    to_point = DiaLinear.Connection.Point.CENTRE_LEFT;
                    from_point = DiaLinear.Connection.Point.CENTRE_RIGHT;
                }
            }
            else
            {
                if ( diff_y < 0D )
                {
                    if ( diff_x < 0D )
                    {
                        to_point = DiaLinear.Connection.Point.BOTTOM_RIGHT;
                        from_point = DiaLinear.Connection.Point.TOP_LEFT;
                    }
                    else
                    {
                        to_point = DiaLinear.Connection.Point.BOTTOM_LEFT;
                        from_point = DiaLinear.Connection.Point.TOP_RIGHT;
                    }
                }
                else
                {
                    if ( diff_x < 0D )
                    {
                        to_point = DiaLinear.Connection.Point.TOP_RIGHT;
                        from_point = DiaLinear.Connection.Point.BOTTOM_LEFT;
                    }
                    else
                    {
                        to_point = DiaLinear.Connection.Point.TOP_LEFT;
                        from_point = DiaLinear.Connection.Point.BOTTOM_RIGHT;
                    }
                }
            }
        }

        this.connections (
            new DiaLinear.Connection ( 0,
                                       from,
                                       from_point ),
            new DiaLinear.Connection ( 1,
                                       to,
                                       to_point )
                                 );

        return this;
    }

    public DiaLinear.Connection [] connections ()
    {
        final DiaLinear.Connection [] template =
            new DiaLinear.Connection [ this.current_connections.connections.size () ];
        final DiaLinear.Connection [] connections =
            this.current_connections.connections.toArray ( template );
        return connections;
    }
    public DiaLinear connections (
                                  DiaLinear.Connection ... connections
                                  )
    {
        this.current_connections.connections.clear ();
        for ( DiaLinear.Connection connection : connections )
        {
            this.current_connections.connections.add ( connection );
        }

        return this;
    }
}
