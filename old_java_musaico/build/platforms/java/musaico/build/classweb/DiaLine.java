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
public class DiaLine
    extends DiaLinear
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    public static final String conn_endpoints1 = "conn_endpoints1";
    public static final double [] default_conn_endpoints1 =
        new double [] { 0D, 0D };

    public static final String conn_endpoints2 = "conn_endpoints2";
    public static final double [] default_conn_endpoints2 =
        new double [] { 20D, 10D };

    public static final String line_width = "line_width";
    public static final double default_line_width = 0.1D;

    public static final String line_style = "line_style";
    public static enum line_style_enum
    {
        SOLID ( "0" ),
        DASHES ( "1" ),
        DOTS ( "2" ),
        DASHES_AND_DOTS ( "3" );
        private final String xml;
        private line_style_enum ( String xml )
        {
            this.xml = xml;
        }
        public final String toXML ()
        {
            return this.xml;
        }
    }
    public static final line_style_enum default_line_style =
        line_style_enum.SOLID;

    public static final String dashlength = "dashlength";
    public static final double default_dashlength = 0.4D;


    private double [] current_conn_endpoints1 = default_conn_endpoints1;
    private double [] current_conn_endpoints2 = default_conn_endpoints2;
    private double current_line_width = default_line_width;
    private line_style_enum current_line_style = default_line_style;
    private double current_dashlength = default_dashlength;

    public DiaLine ()
    {
        super (
               new DiaChunk.XML []
               {
                   new DiaChunk.XML (
                       "line(1)",
                       "        <dia:attribute name=\"conn_endpoints\">\n"
                       + "          <dia:point val=\"" ),
                   new DiaChunk.XML (
                       conn_endpoints1,
                       DiaChunk.XML.fromDouble ( default_conn_endpoints1 ) ),
                   new DiaChunk.XML (
                       "line(2)",
                       "\"/>\n"
                       + "          <dia:point val=\"" ),
                   new DiaChunk.XML (
                       conn_endpoints2,
                       DiaChunk.XML.fromDouble ( default_conn_endpoints2 ) ),
                   new DiaChunk.XML (
                       "line(3)",
                       "\"/>\n"
                       + "        </dia:attribute>\n"
                       + "        <dia:attribute name=\"numcp\">\n"
                       + "          <dia:int val=\"1\"/>\n"
                       + "        </dia:attribute>\n"
                       + "        <dia:attribute name=\"line_width\">\n"
                       + "          <dia:real val=\"" ),
                   new DiaChunk.XML (
                       line_width,
                       DiaChunk.XML.fromDouble ( default_line_width ) ),
                   new DiaChunk.XML (
                       "line(4)",
                       "\"/>\n"
                       + "        </dia:attribute>\n"
                       + "        <dia:attribute name=\"line_style\">\n"
                       + "          <dia:enum val=\"" ),
                   new DiaChunk.XML (
                       line_style,
                       default_line_style.toXML () ),
                   new DiaChunk.XML (
                       "line(5)",
                       "\"/>\n"
                       + "        </dia:attribute>\n"
                       + "        <dia:attribute name=\"dashlength\">\n"
                       + "          <dia:real val=\"" ),
                   new DiaChunk.XML (
                       dashlength,
                       DiaChunk.XML.fromDouble ( default_dashlength ) ),
                   new DiaChunk.XML (
                       "line(6)",
                       "\"/>\n"
                       + "        </dia:attribute>\n" )
               } );

        this.type ( "Standard - Line" );
    }


    @Override
    public DiaLine connections (
                                DiaLinear.Connection ... connections
                                )
    {
        if ( super.connections ( connections ) == null )
        {
            return null;
        }

        final DiaObject from = connections [ 0 ].to;
        final DiaObject to = connections [ connections.length - 1 ].to;

        this.conn_endpoints1 ( from.obj_pos () );
        this.conn_endpoints2 ( to.obj_pos () );

        return this;
    }

    public double [] conn_endpoints1 ()
    {
        return this.current_conn_endpoints1;
    }
    public DiaLine conn_endpoints1 (
                                    double [] new_conn_endpoints1
                                    )
    {
        this.current_conn_endpoints1 = new_conn_endpoints1;
        return (DiaLine)
            this.set ( conn_endpoints1,
                       DiaChunk.XML.fromDouble ( new_conn_endpoints1 ) )
                .obj_pos ( new_conn_endpoints1 )
                .obj_bb ( this.calculateBoundingBox () );
    }

    public double [] conn_endpoints2 ()
    {
        return this.current_conn_endpoints2;
    }
    public DiaLine conn_endpoints2 (
                                    double [] new_conn_endpoints2
                                    )
    {
        this.current_conn_endpoints2 = new_conn_endpoints2;
        return (DiaLine)
            this.set ( conn_endpoints2,
                       DiaChunk.XML.fromDouble ( new_conn_endpoints2 ) )
                .obj_bb ( this.calculateBoundingBox () );
    }

    public final double [] calculateBoundingBox ()
    {
        final double [] bb = new double [ 4 ];

        if ( this.current_conn_endpoints1 [ 0 ]
             <= this.current_conn_endpoints2 [ 0 ] )
        {
            bb [ 0 ] = this.current_conn_endpoints1 [ 0 ];
            bb [ 2 ] = this.current_conn_endpoints2 [ 0 ];
        }
        else
        {
            bb [ 0 ] = this.current_conn_endpoints2 [ 0 ];
            bb [ 2 ] = this.current_conn_endpoints1 [ 0 ];
        }

        if ( this.current_conn_endpoints1 [ 1 ]
             <= this.current_conn_endpoints2 [ 1 ] )
        {
            bb [ 1 ] = this.current_conn_endpoints1 [ 1 ];
            bb [ 3 ] = this.current_conn_endpoints2 [ 1 ];
        }
        else
        {
            bb [ 1 ] = this.current_conn_endpoints2 [ 1 ];
            bb [ 3 ] = this.current_conn_endpoints1 [ 1 ];
        }

        return bb;
    }

    public double line_width ()
    {
        return this.current_line_width;
    }
    public DiaLine line_width (
                               double new_line_width
                               )
    {
        this.current_line_width = new_line_width;
        final double [] current_obj_pos = this.obj_pos ();
        return
            this.set ( line_width,
                       DiaChunk.XML.fromDouble ( new_line_width ) );
    }

    public line_style_enum line_style ()
    {
        return this.current_line_style;
    }
    public DiaLine line_style (
                               line_style_enum new_line_style
                               )
    {
        this.current_line_style = new_line_style;
        return this.set ( line_style,
                          new_line_style.toXML () );
    }

    public double dashlength ()
    {
        return this.current_dashlength;
    }
    public DiaLine dashlength (
                               double new_dashlength
                               )
    {
        this.current_dashlength = new_dashlength;
        final double [] current_obj_pos = this.obj_pos ();
        return
            this.set ( dashlength,
                       DiaChunk.XML.fromDouble ( new_dashlength ) );
    }



    public final DiaLine set (
                             String name,
                             String content
                             )
    {
        return (DiaLine) super.set ( name, content );
    }
}
