package musaico.build.classweb;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

import java.util.zip.GZIPOutputStream;


/**
 * <p>
 * Converts all classes into a rough UML class diagram in Dia
 * (a drawing program) format.
 * </p>
 */
public class DiaUML
    extends ClassExtractDetails
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a DiaUML which filters, sorts and so on
     * the class details for each class using specific
     * ClassDetailsListProcessors before creating Dia
     * UML diagrams containing the details.
     * </p>
     */
    public static class Factory
        implements ClassListProcessorFactory
    {
        private final OutputStream rawOutputStream;
        private final DiaRepositioner repositioner;
        public Factory (
                        final OutputStream raw_output_stream
                        )
        {
            this ( raw_output_stream,
                   new DiaStandardRepositioner () );
        }

        public Factory (
                        final OutputStream raw_output_stream,
                        DiaRepositioner repositioner
                        )
        {
            this.rawOutputStream = raw_output_stream;
            this.repositioner = repositioner;
        }

        /**
         * @see musaico.build.classweb.ClassListProcessorFactory#create(musaico.build.classweb.ClassDetailsListProcessor[])
         */
        @Override
        public final ClassListProcessor create (
                                                ClassDetailsListProcessor [] details_processors
                                                )
            throws IllegalStateException
        {
            return new DiaUML ( details_processors,
                                this.rawOutputStream,
                                this.repositioner );
        }
    }




    // We have to finish GZIPping after the diagram output is complete.
    private final GZIPOutputStream gzip;

    // Output Dia object XML to to this (gzips etc).
    private final PrintWriter out;

    // Re-positions Dia objects to make a pretty diagram.
    private final DiaRepositioner repositioner;


    /**
     * <p>
     * Creates a new DiaUML that will take a list of classes,
     * and for each one, extract all details and pass them through to
     * the specified ClassDetailsListProcessors for further processing.
     * </p>
     */
    public DiaUML (
                   ClassDetailsListProcessor [] details_processors,
                   OutputStream out,
                   DiaRepositioner repositioner
                   )
        throws IllegalStateException
    {
        super ( details_processors );

        try
        {
            this.gzip = new GZIPOutputStream ( out );
            this.out =
                new PrintWriter (
                    new OutputStreamWriter (
                        this.gzip ) );
        }
        catch ( IOException e )
        {
            final String message =
                "Could not create DiaUML for output stream "
                + out;
            System.err.println ( message
                                 + ": " + e );
            throw new IllegalStateException ( message );
        }

        this.repositioner = repositioner;
    }


    /**
     * @see musaico.build.classweb.ClassListProcessor#process(java.util.List)
     */
    @Override
    public List<Class<?>> process (
                                   List<Class<?>> input
                                   )
        throws IllegalStateException
    {
        System.out.println ( "Begin creating UML diagram"
                             + " for Dia (a drawing program)..." );

        final LinkedHashMap<Object, List<DiaChunk>> dia_chunks =
            new LinkedHashMap<Object, List<DiaChunk>> ();

        this.beginDiagram ( dia_chunks );

        final int [] current_id = new int [] { 0 };

        // Class diagram elements:
        for ( Class<?> class_or_interface : input )
        {
            final List<ClassDetails<?>> details =
                this.processDetails ( class_or_interface );

            this.createDiaObjects ( class_or_interface, details, dia_chunks,
                                    current_id );
        }

        // Generalizations / inheritance:
        this.generalizeClasses ( dia_chunks, current_id );

        // Re-position everything:
        this.repositioner.reposition ( dia_chunks );

        // Finish Dia state.
        this.endDiagram ( dia_chunks );

        // Print everything to the output writer.
        try
        {
            for ( List<DiaChunk> one_thing : dia_chunks.values () )
            {
                for ( DiaChunk dia_chunk : one_thing )
                {
                    dia_chunk.printTo ( this.out );
                    this.out.print ( "\n" );
                }
            }

            this.out.flush ();

            // Finish gzip'ping the .dia file/output.
            if ( this.gzip != null )
            {
                this.gzip.finish ();
                this.gzip.flush ();
            }
        }
        catch ( IOException e )
        {
            e.printStackTrace ();
            e.printStackTrace ( this.out );
            throw new IllegalStateException ( "Failed to write DiaUML to "
                                              + this.out,
                                              e );
        }
        finally
        {
            System.out.println ( "...Done creating UML diagram"
                                 + " for Dia (a drawing program)." );
        }

        return input;
    }


    public void generalizeClasses (
                                   LinkedHashMap<Object, List<DiaChunk>> dia_chunks,
                                   int [] current_id
                                   )
    {
        for ( Object something
                  : new ArrayList<Object> ( dia_chunks.keySet () ) )
        {
            if ( ! ( something instanceof Class ) )
            {
                continue;
            }

            final Class<?> class_or_interface =
                (Class<?>) something;

            if ( class_or_interface == Object.class )
            {
                continue;
            }

            final DiaObject specific =
                this.diaObject ( dia_chunks.get ( class_or_interface ) );
            if ( specific == null )
            {
                continue;
            }

            final Class<?> superclass = class_or_interface.getSuperclass ();
            if ( dia_chunks.containsKey ( superclass ) )
            {
                final DiaObject general =
                    this.diaObject ( dia_chunks.get ( superclass ) );
                if ( general != null )
                {
                    final List<DiaChunk> inheritance_chunks =
                        connect ( general, specific, current_id );
                    dia_chunks.put ( class_or_interface.getName ()
                                     + " --|> "
                                     + superclass.getName (),
                                     inheritance_chunks );
                }
            }

            for ( Class<?> in : class_or_interface.getInterfaces () )
            {
                if ( dia_chunks.containsKey ( in ) )
                {
                    final DiaObject general =
                        this.diaObject ( dia_chunks.get ( in ) );
                    if ( general != null )
                    {
                        final List<DiaChunk> inheritance_chunks =
                            connect ( general, specific, current_id );
                        dia_chunks.put ( class_or_interface.getName ()
                                         + " --|> "
                                         + in.getName (),
                                         inheritance_chunks );
                    }
                }
            }
        }
    }


    public List<DiaChunk> connect (
                                   DiaObject from,
                                   DiaObject to,
                                   int [] current_id
                                   )
    {
        final DiaGeneralization inheritance = new DiaGeneralization ();
        inheritance.id ( current_id [ 0 ] );
        current_id [ 0 ] ++;

        inheritance.connect ( from, to );

        final List<DiaChunk> inheritance_chunks =
            new ArrayList<DiaChunk> ();

        inheritance_chunks.add ( inheritance );

        return inheritance_chunks;
    }


    public DiaObject diaObject (
                                List<DiaChunk> diagram_object
                                )
    {
        for ( DiaChunk chunk : diagram_object )
        {
            if ( chunk instanceof DiaObject )
            {
                final DiaObject dia_object = (DiaObject) chunk;
                return  dia_object;
            }
        }

        return null;
    }


    public void beginDiagram (
                              LinkedHashMap<Object, List<DiaChunk>> dia_chunks
                              )
    {
        final List<DiaChunk> diagram_head_chunks =
            new ArrayList<DiaChunk> ();
        diagram_head_chunks.add (
            new DiaChunk (
                DiaChunk.DIAGRAM_HEAD,
                DiaChunk.LAYER_HEAD1,
                DiaChunk.LAYER_NAME,
                DiaChunk.LAYER_HEAD2 ) );

        dia_chunks.put ( "diagram_head",
                         diagram_head_chunks );
    }


    public void endDiagram (
                            LinkedHashMap<Object, List<DiaChunk>> dia_chunks
                            )
    {
        final List<DiaChunk> diagram_tail_chunks =
            new ArrayList<DiaChunk> ();
        diagram_tail_chunks.add (
            new DiaChunk (
                DiaChunk.LAYER_TAIL,
                DiaChunk.DIAGRAM_TAIL ) );

        dia_chunks.put ( "diagram_tail",
                         diagram_tail_chunks );
    }


    public void createDiaObjects (
                                  Class<?> class_or_interface,
                                  List<ClassDetails<?>> details,
                                  LinkedHashMap<Object, List<DiaChunk>> dia_chunks,
                                  int [] current_id
                                  )
    {
        if ( dia_chunks.containsKey ( class_or_interface ) )
        {
            // Already done.
            return;
        }

        final double x = (double) ( dia_chunks.size () % 6 ) * 8D;
        final double y = (double) ( dia_chunks.size () / 6 ) * 4D;
        final double [] obj_pos = new double [] { x, y };
        List<DiaChunk> uml_class_chunks =
            this.createUMLClass ( class_or_interface,
                                  details,
                                  current_id,
                                  obj_pos );

        dia_chunks.put ( class_or_interface, uml_class_chunks );
    }


    public List<DiaChunk> createUMLClass (
                                          Class<?> class_or_interface,
                                          List<ClassDetails<?>> details,
                                          int [] current_id,
                                          double [] obj_pos
                                          )
    {
        final List<DiaChunk> uml_class = new ArrayList<DiaChunk> ();

        uml_class.add (
            new DiaChunk (
                DiaChunk.GROUP_HEAD ) );

        final int box_id = current_id [ 0 ];
        current_id [ 0 ] ++;

        int num_texts = 0;
        double widest_text = 1D;
        for ( ClassDetails<?> detail : details )
        {
            if ( detail.type () == ClassDetail.NAME
                 || detail.type () == ClassDetail.STEREOTYPE )
            {
                for ( Object detail_object : detail )
                {
                    final DiaText text = new DiaText ();
                    text.id ( current_id [ 0 ] );
                    current_id [ 0 ] ++;
                    text.obj_pos ( new double []
                        {
                            obj_pos [ 0 ],
                            obj_pos [ 1 ] + (double) num_texts * 1D
                        } );

                    final String detail_string;
                    if ( detail.type () == ClassDetail.NAME )
                    {
                        detail_string = "" + detail_object;
                    }
                    else if ( detail.type () == ClassDetail.STEREOTYPE )
                    {
                        detail_string = "<<" + detail_object + ">>";
                        text.font_style ( DiaText.font_style_enum.ITALIC );
                    }
                    else
                    {
                        detail_string = "" + detail_object;
                    }

                    text.text ( detail_string );
                    text.alignment ( DiaText.alignment_enum.CENTRE );
                    text.valign ( DiaText.valign_enum.CENTRE );
                    uml_class.add ( text );

                    num_texts ++;
                    double width = text.guessWidth ();
                    if ( width > widest_text )
                    {
                        widest_text = width;
                    }
                }
            }
        }

        final DiaBox box = new DiaBox ();
        box.id ( box_id );
        box.elem_width ( widest_text );
        box.elem_height ( (double) num_texts * 1D );
        box.obj_pos ( new double []
            {
                obj_pos [ 0 ] - box.elem_width () / 2D,
                obj_pos [ 1 ] - box.elem_height () / 2D
            } );
        box.corner_radius ( 0.5D );
        uml_class.add ( 1, box );

        uml_class.add (
            new DiaChunk (
                DiaChunk.GROUP_TAIL ) );

        return uml_class;
    }
}
