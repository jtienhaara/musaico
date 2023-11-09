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
public class DiaGeneralization
    extends DiaLinear
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    public static final String orth_endpoints1 = "orth_endpoints1";
    public static final double [] default_orth_endpoints1 =
        new double [] { 0D, 0D };

    public static final String orth_endpoints2 = "orth_endpoints2";
    public static final double [] default_orth_endpoints2 =
        new double [] { 20D, 10D };

    public static final String orient1 = "orient1";
    public static final String orient2 = "orient2";
    public static final String orient3 = "orient3";
    public static enum orient_enum
    {
        LEFT_RIGHT ( 0 ),
        UP_DOWN ( 1 );
        private final int xml;
        private orient_enum ( int xml )
        {
            this.xml = xml;
        }
        public final String toXML ()
        {
            return "" + this.xml;
        }
    }
    public static final orient_enum default_orient1 =
        orient_enum.UP_DOWN;
    public static final orient_enum default_orient2 =
        orient_enum.LEFT_RIGHT;
    public static final orient_enum default_orient3 =
        orient_enum.UP_DOWN;

    public static final String text_colour = "text_colour";
    public static final int [] default_text_colour =
        new int [] { 0x00,
                     0x00,
                     0x00 };

    public static final String line_colour = "line_colour";
    public static final int [] default_line_colour =
        new int [] { 0x00,
                     0x00,
                     0x00 };

    public static final String name = "name";
    public static final String default_name = "";

    public static final String stereotype = "stereotype";
    public static final String default_stereotype = "";


    private double [] current_orth_endpoints1 = default_orth_endpoints1;
    private double [] current_orth_endpoints2 = default_orth_endpoints2;
    private orient_enum current_orient1 = default_orient1;
    private orient_enum current_orient2 = default_orient2;
    private orient_enum current_orient3 = default_orient3;
    private int [] current_text_colour = default_text_colour;
    private int [] current_line_colour = default_line_colour;
    private String current_name = default_name;
    private String current_stereotype = default_stereotype;

    public DiaGeneralization ()
    {
        super (
               new DiaChunk.XML []
               {
                   new DiaChunk.XML (
                       "generalization(1)",
                       "      <dia:attribute name=\"meta\">\n"
                       + "        <dia:composite type=\"dict\"/>\n"
                       + "      </dia:attribute>\n"
                       + "      <dia:attribute name=\"orth_points\">\n"
                       + "          <dia:point val=\"" ),
                   new DiaChunk.XML (
                       orth_endpoints1,
                       DiaChunk.XML.fromDouble ( default_orth_endpoints1 ) ),
                   new DiaChunk.XML (
                       "generalization(2)",
                       "\"/>\n"
                       + "          <dia:point val=\"" ),
                   new DiaChunk.XML (
                       orth_endpoints1 + ".corner",
                       DiaChunk.XML.fromDouble ( default_orth_endpoints1 ) ),
                   new DiaChunk.XML (
                       "generalization(3)",
                       "\"/>\n"
                       + "          <dia:point val=\"" ),
                   new DiaChunk.XML (
                       orth_endpoints2 + ".corner",
                       DiaChunk.XML.fromDouble ( default_orth_endpoints2 ) ),
                   new DiaChunk.XML (
                       "generalization(4)",
                       "\"/>\n"
                       + "          <dia:point val=\"" ),
                   new DiaChunk.XML (
                       orth_endpoints2,
                       DiaChunk.XML.fromDouble ( default_orth_endpoints2 ) ),
                   new DiaChunk.XML (
                       "generalization(5)",
                       "\"/>\n"
                       + "      </dia:attribute>\n"
                       + "      <dia:attribute name=\"orth_orient\">\n"
                       + "        <dia:enum val=\"" ),
                   new DiaChunk.XML (
                       orient1,
                       default_orient1.toXML () ),
                   new DiaChunk.XML (
                       "generalization(6)",
                       "\"/>\n"
                       + "        <dia:enum val=\"" ),
                   new DiaChunk.XML (
                       orient2,
                       default_orient2.toXML () ),
                   new DiaChunk.XML (
                       "generalization(7)",
                       "\"/>\n"
                       + "        <dia:enum val=\"" ),
                   new DiaChunk.XML (
                       orient3,
                       default_orient3.toXML () ),
                   new DiaChunk.XML (
                       "generalization(8)",
                       "\"/>\n"
                       + "      </dia:attribute>\n"
                       + "      <dia:attribute name=\"orth_autoroute\">\n"
                       + "        <dia:boolean val=\"true\"/>\n"
                       + "      </dia:attribute>\n"
                       + "      <dia:attribute name=\"text_colour\">\n"
                       + "        <dia:color val=\"#" ),
                   new DiaChunk.XML (
                       text_colour,
                       DiaChunk.XML.fromHex ( default_text_colour ) ),
                   new DiaChunk.XML (
                       "generalization(9)",
                       "\"/>\n"
                       + "      </dia:attribute>\n"
                       + "      <dia:attribute name=\"line_colour\">\n"
                       + "        <dia:color val=\"#" ),
                   new DiaChunk.XML (
                       line_colour,
                       DiaChunk.XML.fromHex ( default_line_colour ) ),
                   new DiaChunk.XML (
                       "generalization(10)",
                       "\"/>\n"
                       + "      </dia:attribute>\n"
                       + "      <dia:attribute name=\"name\">\n"
                       + "        <dia:string>#" ),
                   new DiaChunk.XML (
                       name,
                       DiaChunk.xmlize ( default_name ) ),
                   new DiaChunk.XML (
                       "generalization(11)",
                       "#</dia:string>\n"
                       + "      </dia:attribute>\n"
                       + "      <dia:attribute name=\"stereotype\">\n"
                       + "        <dia:string>#" ),
                   new DiaChunk.XML (
                       stereotype,
                       DiaChunk.xmlize ( default_stereotype ) ),
                   new DiaChunk.XML (
                       "generalization(12)",
                       "#</dia:string>\n"
                       + "      </dia:attribute>\n" )
               } );

        this.type ( "UML - Generalization" );
    }


    @Override
    public DiaGeneralization connections (
                                          DiaLinear.Connection ... connections
                                          )
    {
        if ( super.connections ( connections ) == null )
        {
            return null;
        }

        final DiaObject from = connections [ 0 ].to;
        final DiaObject to = connections [ connections.length - 1 ].to;

        this.orth_endpoints1 ( from.obj_pos () );
        this.orth_endpoints2 ( to.obj_pos () );

        return this;
    }


    public double [] orth_endpoints1 ()
    {
        return this.current_orth_endpoints1;
    }
    public DiaGeneralization orth_endpoints1 (
                                              double [] new_orth_endpoints1
                                              )
    {
        this.current_orth_endpoints1 = new_orth_endpoints1;
        return (DiaGeneralization)
            this.set ( orth_endpoints1,
                       DiaChunk.XML.fromDouble ( new_orth_endpoints1 ) )
                .updateOrthEndpoints ()
                .obj_pos ( new_orth_endpoints1 )
                .obj_bb ( this.calculateBoundingBox () );
    }

    public double [] orth_endpoints2 ()
    {
        return this.current_orth_endpoints2;
    }
    public DiaGeneralization orth_endpoints2 (
                                              double [] new_orth_endpoints2
                                              )
    {
        this.current_orth_endpoints2 = new_orth_endpoints2;
        return (DiaGeneralization)
            this.set ( orth_endpoints2,
                       DiaChunk.XML.fromDouble ( new_orth_endpoints2 ) )
                .updateOrthEndpoints ()
                .obj_bb ( this.calculateBoundingBox () );
    }

    private DiaGeneralization updateOrthEndpoints ()
    {
        final DiaLinear.Connection [] connections = this.connections ();
        final int from = 0;
        final int to = connections.length - 1;

        final double [] from_pos = connections [ from ].to.obj_pos ();
        final double [] to_pos = connections [ to ].to.obj_pos ();
        final double [] halfway = new double [] {
            ( from_pos [ 0 ] + to_pos [ 0 ] ) / 2D,
            ( from_pos [ 1 ] + to_pos [ 1 ] ) / 2D
        };

        final orient_enum new_orient1;
        final orient_enum new_orient2;
        final orient_enum new_orient3;

        final double [] new_corner_endpoints1;
        final double [] new_corner_endpoints2;

        switch ( connections [ from ].point )
        {
        case TOP_LEFT:
        case TOP_CENTRE:
        case TOP_RIGHT:
        case BOTTOM_LEFT:
        case BOTTOM_CENTRE:
        case BOTTOM_RIGHT:
            switch ( connections [ to ].point )
            {
            case TOP_LEFT:
            case TOP_CENTRE:
            case TOP_RIGHT:
                new_orient1 = orient_enum.UP_DOWN;
                new_orient2 = orient_enum.LEFT_RIGHT;
                new_orient3 = orient_enum.UP_DOWN;
                new_corner_endpoints1 = new double [] {
                    from_pos [ 0 ], halfway [ 1 ]
                };
                new_corner_endpoints2 = new double [] {
                    to_pos [ 0 ], halfway [ 1 ]
                };
                break;
            case CENTRE_LEFT:
            case CENTRE_RIGHT:
                new_orient1 = orient_enum.LEFT_RIGHT;
                new_orient2 = orient_enum.UP_DOWN;
                new_orient3 = orient_enum.LEFT_RIGHT;
                new_corner_endpoints1 = new double [] {
                    halfway [ 0 ], from_pos [ 1 ]
                };
                new_corner_endpoints2 = new double [] {
                    halfway [ 0 ], to_pos [ 1 ]
                };
                break;
            case BOTTOM_LEFT:
            case BOTTOM_CENTRE:
            case BOTTOM_RIGHT:
            default:
                new_orient1 = orient_enum.UP_DOWN;
                new_orient2 = orient_enum.LEFT_RIGHT;
                new_orient3 = orient_enum.UP_DOWN;
                new_corner_endpoints1 = new double [] {
                    from_pos [ 0 ], halfway [ 1 ]
                };
                new_corner_endpoints2 = new double [] {
                    to_pos [ 0 ], halfway [ 1 ]
                };
                break;
            }
            break;
        case CENTRE_LEFT:
        case CENTRE_RIGHT:
        default:
            new_orient1 = orient_enum.LEFT_RIGHT;
            new_orient2 = orient_enum.UP_DOWN;
            new_orient3 = orient_enum.LEFT_RIGHT;
            new_corner_endpoints1 = new double [] {
                halfway [ 0 ], from_pos [ 1 ]
            };
            new_corner_endpoints2 = new double [] {
                halfway [ 0 ], to_pos [ 1 ]
            };
            break;
        }

        return this
            .orient1 ( new_orient1 )
            .orient2 ( new_orient2 )
            .orient3 ( new_orient3 )
            .set ( orth_endpoints1 + ".corner",
                   DiaChunk.XML.fromDouble ( new_corner_endpoints1 ) )
            .set ( orth_endpoints2 + ".corner",
                   DiaChunk.XML.fromDouble ( new_corner_endpoints2 ) );
    }

    public final double [] calculateBoundingBox ()
    {
        final double [] bb = new double [ 4 ];

        if ( this.current_orth_endpoints1 [ 0 ]
             <= this.current_orth_endpoints2 [ 0 ] )
        {
            bb [ 0 ] = this.current_orth_endpoints1 [ 0 ];
            bb [ 2 ] = this.current_orth_endpoints2 [ 0 ];
        }
        else
        {
            bb [ 0 ] = this.current_orth_endpoints2 [ 0 ];
            bb [ 2 ] = this.current_orth_endpoints1 [ 0 ];
        }

        if ( this.current_orth_endpoints1 [ 1 ]
             <= this.current_orth_endpoints2 [ 1 ] )
        {
            bb [ 1 ] = this.current_orth_endpoints1 [ 1 ];
            bb [ 3 ] = this.current_orth_endpoints2 [ 1 ];
        }
        else
        {
            bb [ 1 ] = this.current_orth_endpoints2 [ 1 ];
            bb [ 3 ] = this.current_orth_endpoints1 [ 1 ];
        }

        return bb;
    }

    public orient_enum orient1 ()
    {
        return this.current_orient1;
    }
    public DiaGeneralization orient1 (
                                     orient_enum new_orient1
                                     )
    {
        this.current_orient1 = new_orient1;
        return this.set ( orient1,
                          new_orient1.toXML () );
    }

    public orient_enum orient2 ()
    {
        return this.current_orient2;
    }
    public DiaGeneralization orient2 (
                                     orient_enum new_orient2
                                     )
    {
        this.current_orient2 = new_orient2;
        return this.set ( orient2,
                          new_orient2.toXML () );
    }

    public orient_enum orient3 ()
    {
        return this.current_orient3;
    }
    public DiaGeneralization orient3 (
                                     orient_enum new_orient3
                                     )
    {
        this.current_orient3 = new_orient3;
        return this.set ( orient3,
                          new_orient3.toXML () );
    }

    public int [] text_colour ()
    {
        return this.current_text_colour;
    }
    public DiaGeneralization text_colour (
                                          int [] new_text_colour
                                          )
    {
        this.current_text_colour = new_text_colour;
        return this.set ( text_colour,
                          DiaChunk.XML.fromHex ( new_text_colour ) );
    }

    public int [] line_colour ()
    {
        return this.current_line_colour;
    }
    public DiaGeneralization line_colour (
                                          int [] new_line_colour
                                          )
    {
        this.current_line_colour = new_line_colour;
        return this.set ( line_colour,
                          DiaChunk.XML.fromHex ( new_line_colour ) );
    }

    public String name ()
    {
        return this.current_name;
    }
    public DiaGeneralization name (
                                   String new_name
                                   )
    {
        this.current_name = new_name;
        return this.set ( name,
                          DiaChunk.xmlize ( new_name ) );
    }

    public String stereotype ()
    {
        return this.current_stereotype;
    }
    public DiaGeneralization stereotype (
                                         String new_stereotype
                                         )
    {
        this.current_stereotype = new_stereotype;
        return this.set ( stereotype,
                          DiaChunk.xmlize ( new_stereotype ) );
    }


    public final DiaGeneralization set (
                                        String name,
                                        String content
                                        )
    {
        return (DiaGeneralization) super.set ( name, content );
    }
}
