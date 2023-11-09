package musaico.build.classweb;

import java.io.IOException;
import java.io.PrintWriter;
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
public class DiaChunk
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;




    public static final String xmlize (
                                       String text
                                       )
    {
        return text
            .replaceAll ( "<", "&lt;" )
            .replaceAll ( ">", "&gt;" );
    }




    public static class XML
        implements Serializable
    {
        private static final long serialVersionUID =
            DiaChunk.serialVersionUID;

        public static final String fromDouble (
                                               double... values
                                               )
        {
            final StringBuilder sbuf = new StringBuilder ();
            boolean is_first = true;
            for ( double value : values )
            {
                if ( is_first )
                {
                    is_first = false;
                }
                else
                {
                    sbuf.append ( "," );
                }

                sbuf.append ( "" + value );
            }
            return sbuf.toString ();
        }

        public static final String fromPairs (
                                              double... values
                                              )
        {
            final StringBuilder sbuf = new StringBuilder ();
            for ( int p = 0; p < values.length; p += 2 )
            {
                if ( p > 0 )
                {
                    sbuf.append ( ";" );
                }

                final double [] pair;
                if ( ( p + 1 ) >= values.length )
                {
                    pair = new double [] { values [ p ] };
                }
                else
                {
                    pair = new double [] { values [ p ], values [ p + 1 ] };
                }

                final String pair_string = DiaChunk.XML.fromDouble ( pair );
                sbuf.append ( pair_string );
            }
            return sbuf.toString ();
        }

        public static final String fromHex (
                                            int... values
                                            )
        {
            final String [] hex_strings = new String [ values.length ];
            int longest = 2;
            for ( int h = 0; h < values.length; h ++ )
            {
                final int hex = values [ h ];
                hex_strings [ h ] = Integer.toHexString ( hex );
                final int length = hex_strings [ h ].length ();
                if ( length > longest )
                {
                    longest = length;
                }
            }

            final StringBuilder sbuf = new StringBuilder ();
            for ( String hex_string : hex_strings )
            {
                final int length = hex_string.length ();
                for ( int filler = length; filler < longest; filler ++ )
                {
                    sbuf.append ( "0" );
                }
                sbuf.append ( hex_string );
            }

            return sbuf.toString ();
        }

        public static final String fromInt (
                                            int... values
                                            )
        {
            final StringBuilder sbuf = new StringBuilder ();
            boolean is_first = true;
            for ( int value : values )
            {
                if ( is_first )
                {
                    is_first = false;
                }
                else
                {
                    sbuf.append ( "," );
                }

                sbuf.append ( "" + value );
            }
            return sbuf.toString ();
        }

        public static final String fromPairs (
                                              int... values
                                              )
        {
            final StringBuilder sbuf = new StringBuilder ();
            for ( int p = 0; p < values.length; p += 2 )
            {
                if ( p > 0 )
                {
                    sbuf.append ( ";" );
                }

                final int [] pair;
                if ( ( p + 1 ) >= values.length )
                {
                    pair = new int [] { values [ p ] };
                }
                else
                {
                    pair = new int [] { values [ p ], values [ p + 1 ] };
                }

                final String pair_string = DiaChunk.XML.fromInt ( pair );
                sbuf.append ( pair_string );
            }
            return sbuf.toString ();
        }

        public final String name;
        public final String defaultContent;

        // MUTABLE:
        private String content;

        public XML (
                    String name,
                    String default_content
                    )
        {
            this ( name, default_content, default_content );
        }

        public XML (
                    String name,
                    String default_content,
                    String content
                    )
        {
            this.name = name;
            this.defaultContent = default_content;
            this.content = content;
        }

        /**
         * @return The content of this chunk.  Never null.
         */
        public String content ()
        {
            return this.content;
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
            if ( object == this )
            {
                return true;
            }
            else if ( object == null )
            {
                return false;
            }
            else if ( ! ( object instanceof DiaChunk.XML ) )
            {
                return false;
            }

            final DiaChunk.XML that = (DiaChunk.XML) object;
            if ( ! this.name.equals ( that.name ) )
            {
                return false;
            }
            else if ( ! this.defaultContent.equals ( that.defaultContent ) )
            {
                return false;
            }
            else if ( ! this.content ().equals ( that.content () ) )
            {
                return false;
            }

            return true;
        }

        @Override
        public final String toString ()
        {
            return this.name + " = " + this.content ();
        }
    }




    /** Toplevel diagram XML. */
    public static final DiaChunk.XML DIAGRAM_HEAD =
        new DiaChunk.XML (
            "dia:diagram(head)",
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<dia:diagram xmlns:dia=\"http://www.lysator.liu.se/~alla/dia/\">\n"
            + "  <dia:diagramdata>\n"
            + "    <dia:attribute name=\"background\">\n"
            + "      <dia:color val=\"#ffffff\"/>\n"
            + "    </dia:attribute>\n"
            + "    <dia:attribute name=\"pagebreak\">\n"
            + "      <dia:color val=\"#000099\"/>\n"
            + "    </dia:attribute>\n"
            + "    <dia:attribute name=\"paper\">\n"
            + "      <dia:composite type=\"paper\">\n"
            + "        <dia:attribute name=\"name\">\n"
            + "          <dia:string>#Letter#</dia:string>\n"
            + "        </dia:attribute>\n"
            + "        <dia:attribute name=\"tmargin\">\n"
            + "          <dia:real val=\"2.5399999618530273\"/>\n"
            + "        </dia:attribute>\n"
            + "        <dia:attribute name=\"bmargin\">\n"
            + "          <dia:real val=\"2.5399999618530273\"/>\n"
            + "        </dia:attribute>\n"
            + "        <dia:attribute name=\"lmargin\">\n"
            + "          <dia:real val=\"2.5399999618530273\"/>\n"
            + "        </dia:attribute>\n"
            + "        <dia:attribute name=\"rmargin\">\n"
            + "          <dia:real val=\"2.5399999618530273\"/>\n"
            + "        </dia:attribute>\n"
            + "        <dia:attribute name=\"is_portrait\">\n"
            + "          <dia:boolean val=\"true\"/>\n"
            + "        </dia:attribute>\n"
            + "        <dia:attribute name=\"scaling\">\n"
            + "          <dia:real val=\"1\"/>\n"
            + "        </dia:attribute>\n"
            + "        <dia:attribute name=\"fitto\">\n"
            + "          <dia:boolean val=\"false\"/>\n"
            + "        </dia:attribute>\n"
            + "      </dia:composite>\n"
            + "    </dia:attribute>\n"
            + "    <dia:attribute name=\"grid\">\n"
            + "      <dia:composite type=\"grid\">\n"
            + "        <dia:attribute name=\"width_x\">\n"
            + "          <dia:real val=\"1\"/>\n"
            + "        </dia:attribute>\n"
            + "        <dia:attribute name=\"width_y\">\n"
            + "          <dia:real val=\"1\"/>\n"
            + "        </dia:attribute>\n"
            + "        <dia:attribute name=\"visible_x\">\n"
            + "          <dia:int val=\"1\"/>\n"
            + "        </dia:attribute>\n"
            + "        <dia:attribute name=\"visible_y\">\n"
            + "          <dia:int val=\"1\"/>\n"
            + "        </dia:attribute>\n"
            + "        <dia:composite type=\"color\"/>\n"
            + "      </dia:composite>\n"
            + "    </dia:attribute>\n"
            + "    <dia:attribute name=\"color\">\n"
            + "      <dia:color val=\"#d8e5e5\"/>\n"
            + "    </dia:attribute>\n"
            + "    <dia:attribute name=\"guides\">\n"
            + "      <dia:composite type=\"guides\">\n"
            + "        <dia:attribute name=\"hguides\"/>\n"
            + "        <dia:attribute name=\"vguides\"/>\n"
            + "      </dia:composite>\n"
            + "    </dia:attribute>\n"
            + "  </dia:diagramdata>\n" );
    public static final DiaChunk.XML DIAGRAM_TAIL =
        new DiaChunk.XML (
            "dia:diagram(tail)",
            "</dia:diagram>\n" );

    public static final DiaChunk.XML LAYER_HEAD1 =
        new DiaChunk.XML (
            "dia:layer(head1)",
            "  <dia:layer name=\"" );
    public static final DiaChunk.XML LAYER_NAME =
        new DiaChunk.XML (
            "name",
            "Background" );
    public static final DiaChunk.XML LAYER_HEAD2 =
        new DiaChunk.XML (
            "dia:layer(head2)",
            "\" visible=\"true\" active=\"true\">\n" );
    public static final DiaChunk.XML LAYER_TAIL =
        new DiaChunk.XML (
            "dia:layer(tail)",
            "  </dia:layer>\n" );

    public static final DiaChunk.XML GROUP_HEAD =
        new DiaChunk.XML (
            "dia:group(head)",
            "    <dia:group>\n" );
    public static final DiaChunk.XML GROUP_TAIL =
        new DiaChunk.XML (
            "dia:group(tail)",
            "    </dia:group>\n" );




    // The XML output of this object.
    private final LinkedHashMap<String, DiaChunk.XML> xml;

    /**
     * <p>
     * Creates a new DiaChunk with the specified content templates.
     * </p>
     */
    public DiaChunk (
                     DiaChunk.XML ... xml
                     )
    {
        this.xml = new LinkedHashMap<String, DiaChunk.XML> ();
        for ( DiaChunk.XML chunk : xml )
        {
            this.xml.put ( chunk.name, chunk );
        }
    }


    /**
     * @return All of the XML chunks for this object, in order of output.
     *         Never null.  Never contains any null elements.
     */
    public final List<DiaChunk.XML> get ()
    {
        return new ArrayList<DiaChunk.XML> ( this.xml.values () );
    }


    /**
     * @return The specified XML chunk, or null if no such chunk name
     *         exists.
     */
    public final DiaChunk.XML get (
                                   String name
                                   )
    {
        return this.xml.get ( name );
    }


    /**
     * @return The content from the specified XML chunk,
     *         or null if no such chunk name exists.
     */
    public final String getContent (
                                    String name
                                    )
    {
        final DiaChunk.XML chunk = this.xml.get ( name );
        if ( chunk == null )
        {
            return null;
        }

        return chunk.content ();
    }


    /**
     * <p>
     * Sets the content of the specified XML chunk, and returns this object.
     * </p>
     */
    public DiaChunk set (
                         String name,
                         String content
                         )
    {
        final DiaChunk.XML chunk = this.xml.get ( name );
        chunk.content = content;

        return this;
    }


    /**
     * <p>
     * Writes out the XML content to the specified StringBuilder.
     * </p>
     */
    public final void printTo (
                               StringBuilder out
                               )
    {
        for ( DiaChunk.XML chunk : this.xml.values () )
        {
            out.append ( chunk.content () );
        }
    }


    /**
     * <p>
     * Writes out the XML content to the specified Writer.
     * </p>
     */
    public final void printTo (
                               PrintWriter out
                               )
        throws IOException
    {
        for ( DiaChunk.XML chunk : this.xml.values () )
        {
            out.print ( chunk.content () );
        }
    }
}
