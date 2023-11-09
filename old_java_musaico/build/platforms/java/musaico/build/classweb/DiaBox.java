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
public class DiaBox
    extends DiaObject
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    public static final String elem_width = "elem_width";
    public static final double default_elem_width = 10.0D;

    public static final String elem_height = "elem_height";
    public static final double default_elem_height = 5.0D;

    public static final String elem_corner = "elem_corner";
    public static final double [] default_elem_corner =
        new double [] { DiaObject.default_obj_pos [ 0 ],
                        DiaObject.default_obj_pos [ 1 ] };

    public static final String border_width = "border_width";
    public static final double default_border_width = 0.1D;

    public static final String border_colour = "border_colour";
    public static final int [] default_border_colour =
        new int [] { 0x00,
                     0x00,
                     0x00 };

    public static final String inner_colour = "inner_colour";
    public static final int [] default_inner_colour =
        new int [] { 0xFF,
                     0xFF,
                     0xFF };

    public static final String show_background = "show_background";
    public static final boolean default_show_background = true;

    public static final String corner_radius = "corner_radius";
    public static final double default_corner_radius = 0.0D;


    private double current_elem_width = default_elem_width;
    private double current_elem_height = default_elem_height;
    private double [] current_elem_corner = default_elem_corner;
    private double current_border_width = default_border_width;
    private int [] current_border_colour = default_border_colour;
    private int [] current_inner_colour = default_inner_colour;
    private boolean current_show_background = default_show_background;
    private double current_corner_radius = default_corner_radius;

    public DiaBox ()
    {
        super (
               new DiaChunk.XML []
               {
                   new DiaChunk.XML (
                       "box(1)",
                       "      <dia:attribute name=\"elem_corner\">\n"
                       + "        <dia:point val=\"" ),
                   new DiaChunk.XML (
                       elem_corner,
                       DiaChunk.XML.fromDouble ( default_elem_corner ) ),
                   new DiaChunk.XML (
                       "box(2)",
                       "\"/>\n"
                       + "      </dia:attribute>\n"
                       + "      <dia:attribute name=\"elem_width\">\n"
                       + "        <dia:real val=\""),
                   new DiaChunk.XML (
                       elem_width,
                       DiaChunk.XML.fromDouble ( default_elem_width ) ),
                   new DiaChunk.XML (
                       "box(3)",
                       "\"/>\n"
                       + "      </dia:attribute>\n"
                       + "      <dia:attribute name=\"elem_height\">\n"
                       + "        <dia:real val=\"" ),
                   new DiaChunk.XML (
                       elem_height,
                       DiaChunk.XML.fromDouble ( default_elem_height ) ),
                   new DiaChunk.XML (
                       "box(4)",
                       "\"/>\n"
                       + "      </dia:attribute>\n"
                       + "      <dia:attribute name=\"border_width\">\n"
                       + "        <dia:real val=\"" ),
                   new DiaChunk.XML (
                       border_width,
                       DiaChunk.XML.fromDouble ( default_border_width ) ),
                   new DiaChunk.XML (
                       "box(5)",
                       "\"/>\n"
                       + "      </dia:attribute>\n"
                       + "      <dia:attribute name=\"border_color\">\n"
                       + "        <dia:color val=\"#" ),
                   new DiaChunk.XML (
                       border_colour,
                       DiaChunk.XML.fromHex ( default_border_colour ) ),
                   new DiaChunk.XML (
                       "box(6)",
                       "\"/>\n"
                       + "      </dia:attribute>\n"
                       + "      <dia:attribute name=\"inner_color\">\n"
                       + "        <dia:color val=\"#" ),
                   new DiaChunk.XML (
                       inner_colour,
                       DiaChunk.XML.fromHex ( default_inner_colour ) ),
                   new DiaChunk.XML (
                       "box(7)",
                       "\"/>\n"
                       + "      </dia:attribute>\n"
                       + "      <dia:attribute name=\"show_background\">\n"
                       + "        <dia:boolean val=\"" ),
                   new DiaChunk.XML (
                       show_background,
                       "" + default_show_background ),
                   new DiaChunk.XML (
                       "box(8)",
                       "\"/>\n"
                       + "      </dia:attribute>\n"
                       + "      <dia:attribute name=\"corner_radius\">\n"
                       + "        <dia:real val=\"" ),
                   new DiaChunk.XML (
                       corner_radius,
                       DiaChunk.XML.fromDouble ( default_corner_radius ) ),
                   new DiaChunk.XML (
                       "box(9)",
                       "\"/>\n"
                       + "      </dia:attribute>\n" )
               } );

        this.type ( "Standard - Box" );
    }

    @Override
    public DiaBox obj_pos (
                           double [] new_obj_pos
                           )
    {
        final DiaBox box = (DiaBox)
            super.obj_pos ( new_obj_pos )
                 .obj_bb (
                     new double [] {
                         new_obj_pos [ 0 ] - 0.05D,
                         new_obj_pos [ 1 ] - 0.05D,
                         new_obj_pos [ 0 ] + this.current_elem_width + 0.05D,
                         new_obj_pos [ 1 ] + this.current_elem_height + 0.05D
                     } );
        return box.elem_corner (
                     new double [] {
                         new_obj_pos [ 0 ],
                         new_obj_pos [ 1 ]
                     } );
    }

    public double elem_width ()
    {
        return this.current_elem_width;
    }
    public DiaBox elem_width (
                              double new_elem_width
                              )
    {
        this.current_elem_width = new_elem_width;
        final double [] current_obj_pos = this.obj_pos ();
        return (DiaBox)
            this.set ( elem_width,
                       DiaChunk.XML.fromDouble ( new_elem_width ) )
                .obj_bb (
                    new double [] {
                        current_obj_pos [ 0 ] - 0.05D,
                        current_obj_pos [ 1 ] - 0.05D,
                        current_obj_pos [ 0 ] + this.current_elem_width + 0.05D,
                        current_obj_pos [ 1 ] + this.current_elem_height + 0.05D
                    } );
    }

    public double elem_height ()
    {
        return this.current_elem_height;
    }
    public DiaBox elem_height (
                               double new_elem_height
                               )
    {
        this.current_elem_height = new_elem_height;
        final double [] current_obj_pos = this.obj_pos ();
        return (DiaBox)
            this.set ( elem_height,
                       DiaChunk.XML.fromDouble ( new_elem_height ) )
                .obj_bb (
                    new double [] {
                        current_obj_pos [ 0 ] - 0.05D,
                        current_obj_pos [ 1 ] - 0.05D,
                        current_obj_pos [ 0 ] + this.current_elem_width + 0.05D,
                        current_obj_pos [ 1 ] + this.current_elem_height + 0.05D
                    } );
    }

    public double [] elem_corner ()
    {
        return this.current_elem_corner;
    }
    public DiaBox elem_corner (
                               double [] new_elem_corner
                               )
    {
        this.current_elem_corner = new_elem_corner;
        return this.set ( elem_corner,
                          DiaChunk.XML.fromDouble ( new_elem_corner ) );
    }

    public double border_width ()
    {
        return this.current_border_width;
    }
    public DiaBox border_width (
                                double new_border_width
                                )
    {
        this.current_border_width = new_border_width;
        return this.set ( border_width,
                          DiaChunk.XML.fromDouble ( new_border_width ) );
    }

    public int [] border_colour ()
    {
        return this.current_border_colour;
    }
    public DiaBox border_colour (
                                 int [] new_border_colour
                                 )
    {
        this.current_border_colour = new_border_colour;
        return this.set ( border_colour,
                          DiaChunk.XML.fromHex ( new_border_colour ) );
    }

    public int [] inner_colour ()
    {
        return this.current_inner_colour;
    }
    public DiaBox inner_colour (
                                int [] new_inner_colour
                                )
    {
        this.current_inner_colour = new_inner_colour;
        return this.set ( inner_colour,
                          DiaChunk.XML.fromHex ( new_inner_colour ) );
    }

    public boolean show_background ()
    {
        return this.current_show_background;
    }
    public DiaBox show_background (
                                   boolean new_show_background
                                   )
    {
        this.current_show_background = new_show_background;
        return this.set ( show_background,
                          "" + new_show_background );
    }

    public double corner_radius ()
    {
        return this.current_corner_radius;
    }
    public DiaBox corner_radius (
                                 double new_corner_radius
                                 )
    {
        this.current_corner_radius = new_corner_radius;
        return this.set ( corner_radius,
                          DiaChunk.XML.fromDouble ( new_corner_radius ) );
    }

    public final DiaBox set (
                             String name,
                             String content
                             )
    {
        return (DiaBox) super.set ( name, content );
    }
}
