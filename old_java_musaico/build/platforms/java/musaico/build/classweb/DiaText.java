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
public class DiaText
    extends DiaObject
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    public static final String text = "text";
    public static final String default_text = "(no text)";

    public static final String font_family = "font_family";
    public static enum font_family_enum
    {
        SANS ( "sans" ),
        SERIF ( "serif" );
        private final String xml;
        private font_family_enum ( String xml )
        {
            this.xml = xml;
        }
        public final String toXML ()
        {
            return this.xml;
        }
    }
    public static final font_family_enum default_font_family =
        font_family_enum.SANS;

    public static final String font_style = "font_style";
    public static enum font_style_enum
    {
        NORMAL ( "0" ),
        BOLD ( "1" ),
        ITALIC ( "2" ),
        BOLD_ITALIC ( "3" );
        private final String xml;
        private font_style_enum ( String xml )
        {
            this.xml = xml;
        }
        public final String toXML ()
        {
            return this.xml;
        }
    }
    public static final font_style_enum default_font_style =
        font_style_enum.NORMAL;

    public static final String font_name = "font_name";
    public static enum font_name_enum
    {
        HELVETICA ( "Helvetica" ),
        ARIAL ( "Arial" );
        private final String xml;
        private font_name_enum ( String xml )
        {
            this.xml = xml;
        }
        public final String toXML ()
        {
            return this.xml;
        }
    }
    public static final font_name_enum default_font_name =
        font_name_enum.HELVETICA;

    public static final String height = "height";
    public static final double default_height = 0.8D;

    public static final String pos = "pos";
    public static final double [] default_pos = DiaObject.default_obj_pos;

    public static final String colour = "colour";
    public static final int [] default_colour =
        new int [] { 0x00,
                     0x00,
                     0x00 };

    public static final String alignment = "alignment";
    public static enum alignment_enum
    {
        LEFT ( "0" ),
        CENTRE ( "1" ),
        RIGHT ( "2" );
        private final String xml;
        private alignment_enum ( String xml )
        {
            this.xml = xml;
        }
        public final String toXML ()
        {
            return this.xml;
        }
    }
    public static final alignment_enum default_alignment =
        alignment_enum.LEFT;

    public static final String valign = "valign";
    public static enum valign_enum
    {
        TOP ( "0" ),
        BOTTOM ( "1" ),
        CENTRE ( "2" ),
        FIRST_LINE ( "3" );
        private final String xml;
        private valign_enum ( String xml )
        {
            this.xml = xml;
        }
        public final String toXML ()
        {
            return this.xml;
        }
    }
    public static final valign_enum default_valign =
        valign_enum.FIRST_LINE;


    private String current_text = default_text;
    private font_family_enum current_font_family;
    private font_style_enum current_font_style;
    private font_name_enum current_font_name;
    private double current_height = default_height;
    private double [] current_pos = default_pos;
    private int [] current_colour = default_colour;
    private alignment_enum current_alignment;
    private valign_enum current_valign;

    public DiaText ()
    {
        super (
               new DiaChunk.XML []
               {
                   new DiaChunk.XML (
                       "text(1)",
                       "      <dia:attribute name=\"text\">\n"
                       + "        <dia:composite type=\"text\">\n"
                       + "          <dia:attribute name=\"string\">\n"
                       + "            <dia:string>#" ),
                   new DiaChunk.XML (
                       text,
                       DiaChunk.xmlize ( default_text ) ),
                   new DiaChunk.XML (
                       "text(2)",
                       "#</dia:string>\n"
                       + "          </dia:attribute>\n"
                       + "          <dia:attribute name=\"font\">\n"
                       + "            <dia:font family=\"" ),
                   new DiaChunk.XML (
                       font_family,
                       default_font_family.toXML () ),
                   new DiaChunk.XML (
                       "text(3)",
                       "\" style=\"" ),
                   new DiaChunk.XML (
                       font_style,
                       default_font_style.toXML () ),
                   new DiaChunk.XML (
                       "text(4)",
                       "\" name=\"" ),
                   new DiaChunk.XML (
                       font_name,
                       default_font_name.toXML () ),
                   new DiaChunk.XML (
                       "text(5)",
                       "\"/>\n"
                       + "          </dia:attribute>\n"
                       + "          <dia:attribute name=\"height\">\n"
                       + "            <dia:real val=\"" ),
                   new DiaChunk.XML (
                       height,
                       "" + default_height ),
                   new DiaChunk.XML (
                       "text(6)",
                       "\"/>\n"
                       + "          </dia:attribute>\n"
                       + "          <dia:attribute name=\"pos\">\n"
                       + "            <dia:point val=\"" ),
                   new DiaChunk.XML (
                       pos,
                       DiaChunk.XML.fromDouble ( default_pos ) ),
                   new DiaChunk.XML (
                       "text(7)",
                       "\"/>\n"
                       + "          </dia:attribute>\n"
                       + "          <dia:attribute name=\"color\">\n"
                       + "            <dia:color val=\"#" ),
                   new DiaChunk.XML (
                       colour,
                       DiaChunk.XML.fromHex ( default_colour ) ),
                   new DiaChunk.XML (
                       "text(8)",
                       "\"/>\n"
                       + "          </dia:attribute>\n"
                       + "          <dia:attribute name=\"alignment\">\n"
                       + "            <dia:enum val=\"" ),
                   new DiaChunk.XML (
                       alignment,
                       default_alignment.toXML () ),
                   new DiaChunk.XML (
                       "text(9)",
                       "\"/>\n"
                       + "          </dia:attribute>\n"
                       + "        </dia:composite>\n"
                       + "      </dia:attribute>\n"
                       + "      <dia:attribute name=\"valign\">\n"
                       + "        <dia:enum val=\"" ),
                   new DiaChunk.XML (
                       valign,
                       default_valign.toXML () ),
                   new DiaChunk.XML (
                       "text(10)",
                       "\"/>\n"
                       + "      </dia:attribute>\n" )
               } );

        this.type ( "Standard - Text" );
    }

    public double guessWidth ()
    {
        return (double) this.current_text.length ()
            * this.current_height
            * 0.8D; // Arbitrary factor.
    }

    @Override
    public DiaText obj_pos (
                            double [] new_obj_pos
                            )
    {
        final DiaText text = (DiaText)
            super.obj_pos ( new_obj_pos )
                 .obj_bb (
                     new double [] {
                         new_obj_pos [ 0 ] - 0.05D,
                         new_obj_pos [ 1 ] - 0.05D,
                         new_obj_pos [ 0 ] + this.guessWidth () + 0.05D,
                         new_obj_pos [ 1 ] + this.current_height + 0.05D
                     } );
        return text.pos ( new_obj_pos );
    }

    public String text ()
    {
        return this.current_text;
    }
    public DiaText text (
                         String new_text
                         )
    {
        this.current_text = new_text;
        return this.set ( text,
                          DiaChunk.xmlize ( new_text ) );
    }

    public font_family_enum font_family ()
    {
        return this.current_font_family;
    }
    public DiaText font_family (
                                font_family_enum new_font_family
                                )
    {
        this.current_font_family = new_font_family;
        return this.set ( font_family,
                          new_font_family.toXML () );
    }

    public font_style_enum font_style ()
    {
        return this.current_font_style;
    }
    public DiaText font_style (
                               font_style_enum new_font_style
                               )
    {
        this.current_font_style = new_font_style;
        return this.set ( font_style,
                          new_font_style.toXML () );
    }

    public font_name_enum font_name ()
    {
        return this.current_font_name;
    }
    public DiaText font_name (
                              font_name_enum new_font_name
                              )
    {
        this.current_font_name = new_font_name;
        return this.set ( font_name,
                          new_font_name.toXML () );
    }

    public double height ()
    {
        return this.current_height;
    }
    public DiaText height (
                           double new_height
                           )
    {
        this.current_height = new_height;
        final double [] current_obj_pos = this.obj_pos ();
        return (DiaText)
            this.set ( height,
                       DiaChunk.XML.fromDouble ( new_height ) )
                .obj_bb (
                    new double [] {
                        current_obj_pos [ 0 ] - 0.05D,
                        current_obj_pos [ 1 ] - 0.05D,
                        current_obj_pos [ 0 ] + this.guessWidth () + 0.05D,
                        current_obj_pos [ 1 ] + this.current_height + 0.05D
                    } );
    }

    public double [] pos ()
    {
        return this.current_pos;
    }
    public DiaText pos (
                        double [] new_pos
                        )
    {
        this.current_pos = new_pos;
        return this.set ( pos,
                          DiaChunk.XML.fromDouble ( new_pos ) );
    }

    public int [] colour ()
    {
        return this.current_colour;
    }
    public DiaText colour (
                           int [] new_colour
                           )
    {
        this.current_colour = new_colour;
        return this.set ( colour,
                          DiaChunk.XML.fromHex ( new_colour ) );
    }

    public alignment_enum alignment ()
    {
        return this.current_alignment;
    }
    public DiaText alignment (
                              alignment_enum new_alignment
                              )
    {
        this.current_alignment = new_alignment;
        return this.set ( alignment,
                          new_alignment.toXML () );
    }

    public valign_enum valign ()
    {
        return this.current_valign;
    }
    public DiaText valign (
                           valign_enum new_valign
                           )
    {
        this.current_valign = new_valign;
        return this.set ( valign,
                          new_valign.toXML () );
    }


    public final DiaText set (
                              String name,
                              String content
                              )
    {
        return (DiaText) super.set ( name, content );
    }
}
