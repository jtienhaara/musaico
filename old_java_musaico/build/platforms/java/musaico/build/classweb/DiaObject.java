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
public class DiaObject
    extends DiaChunk
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    public static final String type = "type";
    public static final String default_type = "Standard - UNKNOWN";

    public static final String id = "id";
    public static final int default_id = -1;

    public static final String obj_pos = "obj_pos";
    public static final double [] default_obj_pos =
        new double [] { 0D,
                        0D };

    public static final String obj_bb = "obj_bb";
    public static final double [] default_obj_bb =
        new double [] { default_obj_pos [ 0 ] - 0.05D,
                        default_obj_pos [ 1 ] - 0.05D,
                        default_obj_pos [ 0 ] + 0.05D,
                        default_obj_pos [ 1 ] + 0.05D };


    private String current_type = default_type;
    private int current_id = default_id;
    private double [] current_obj_pos = default_obj_pos;
    private double [] current_obj_bb = default_obj_bb;

    public DiaObject (
                      DiaChunk.XML ... contents
                      )
    {
        super (
               createObjectXML ( contents )
               );
    }


    private static final DiaChunk.XML [] createObjectXML (
                                                          DiaChunk.XML [] contents
                                                          )
    {
        final DiaChunk.XML [] xml = new DiaChunk.XML [ 10 + contents.length ];
        xml [ 0 ] =
            new DiaChunk.XML (
                "object(1)",
                "    <dia:object type=\"" );
        xml [ 1 ] =
            new DiaChunk.XML (
                type,
                default_type );
        xml [ 2 ] =
            new DiaChunk.XML (
                "object(2)",
                "\" version=\"0\" id=\"O" );
        xml [ 3 ] =
            new DiaChunk.XML (
                id,
                "" + default_id );
        xml [ 4 ] =
            new DiaChunk.XML (
                "object(3)",
                "\">\n"
                + "      <dia:attribute name=\"obj_pos\">\n"
                + "        <dia:point val=\"" );
        xml [ 5 ] =
            new DiaChunk.XML (
                obj_pos,
                DiaChunk.XML.fromDouble ( default_obj_pos ) );
        xml [ 6 ] =
            new DiaChunk.XML (
                "object(4)",
                "\"/>\n"
                + "      </dia:attribute>\n"
                + "      <dia:attribute name=\"obj_bb\">\n"
                + "        <dia:rectangle val=\"" );
        xml [ 7 ] =
            new DiaChunk.XML (
                obj_bb,
                DiaChunk.XML.fromPairs ( default_obj_bb ) );
        xml [ 8 ] =
            new DiaChunk.XML (
                "object(5)",
                "\"/>\n"
                + "      </dia:attribute>\n" );

        System.arraycopy ( contents, 0,
                           xml, 9, contents.length );

        xml [ xml.length - 1 ] =
            new DiaChunk.XML (
                "object(6)",
                "    </dia:object>\n" );

        return xml;
    }

    public String type ()
    {
        return this.current_type;
    }
    public DiaObject type (
                           String new_type
                           )
    {
        this.current_type = new_type;
        return this.set ( type,
                          new_type );
    }

    public int id ()
    {
        return this.current_id;
    }
    public DiaObject id (
                         int new_id
                         )
    {
        this.current_id = new_id;
        return this.set ( id,
                          "" + new_id );
    }

    public double [] obj_pos ()
    {
        return this.current_obj_pos;
    }
    public DiaObject obj_pos (
                              double [] new_obj_pos
                              )
    {
        this.current_obj_pos = new_obj_pos;
        return this.set ( obj_pos,
                          DiaChunk.XML.fromDouble ( new_obj_pos ) )
                   .obj_bb (
                       new double [] {
                           this.current_obj_pos [ 0 ] - 0.05D,
                           this.current_obj_pos [ 1 ] - 0.05D,
                           this.current_obj_pos [ 0 ] + 0.05D,
                           this.current_obj_pos [ 1 ] + 0.05D
                       } );
    }

    public double [] obj_bb ()
    {
        return this.current_obj_bb;
    }
    public DiaObject obj_bb (
                             double [] new_obj_bb
                             )
    {
        this.current_obj_bb = new_obj_bb;
        return this.set ( obj_bb,
                          DiaChunk.XML.fromPairs ( new_obj_bb ) );
    }


    public DiaObject set (
                          String name,
                          String content
                          )
    {
        return (DiaObject) super.set ( name, content );
    }


    @Override
    public String toString ()
    {
        return this.getClass ().getSimpleName () + " 'O" + this.id () + "'";
    }
}
