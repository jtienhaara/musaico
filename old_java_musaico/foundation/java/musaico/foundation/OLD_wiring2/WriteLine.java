package musaico.foundation.wiring;

import java.io.File;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.Writer;

import java.util.ArrayList;
import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import musaico.foundation.structure.StringRepresentation;


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
public class WriteLine
    implements Wiring.Circuit
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    public static final Pattern NEWLINE =
        Pattern.compile ( "!!!" );

    @Override
    public final Wiring.Schematic schematic (
            Wiring wiring
            )
    {
        final Wiring.Type none_type =
            Wiring.Type.none ( wiring );
        final Wiring.Type any_type =
            Wiring.Type.any ( wiring );
        final Wiring.Type writer_type =
            Wiring.Type.forClass ( wiring,
                                   Writer.class );
        final Wiring.Type output_stream_type =
            Wiring.Type.forClass ( wiring,
                                   OutputStream.class );
        final Wiring.Type file_type =
            Wiring.Type.forClass ( wiring,
                                   File.class );
        final Wiring.Type string_type =
            Wiring.Type.forClass ( wiring,
                                   String.class );

        // WriteLine:
        final Wiring.Metadata schematic_metadata =
            new Wiring.Metadata (
                    wiring,        // wiring
                    -1L,           // id
                    -1L,           // namespace_id
                    "write-line"   // name
                    );             // no tags for now
        final Wiring.Schematic schematic =
            new Wiring.Schematic (
                    wiring,        // wiring
                    -1L,           // id
                    schematic_metadata, // metadata
                    this,          // circuit
                    any_type,      // configuration_data_type !!!! hack for now.  Make it only accept valid configuration objects in future.
                    none_type );   // data_type

        // Namespace for WriteLine's terminals:
        final Wiring.Namespace schematic_namespace =
            new Wiring.Namespace (
                    wiring,            // wiring
                    -1L,               // id
                    schematic_metadata // metadata
                    );

        // Terminal to configure a new Writer:
        final Wiring.Metadata writer_metadata =
            new Wiring.Metadata (
                    wiring,        // wiring
                    -1L,           // id
                    schematic_namespace, // namespace
                    "writer"       // name
                    );             // no tags for now
        final Wiring.Terminal writer_terminal =
            new Wiring.Terminal (
                    wiring,        // wiring
                    -1L,           // id
                    writer_metadata, // metadata
                    schematic,     // schematic
                    writer_type ); // type

        // Terminal to configure a new OutputStream:
        final Wiring.Metadata output_stream_metadata =
            new Wiring.Metadata (
                    wiring,        // wiring
                    -1L,           // id
                    schematic_namespace, // namespace
                    "output-stream" // name
                    );             // no tags for now
        final Wiring.Terminal output_stream_terminal =
            new Wiring.Terminal (
                    wiring,        // wiring
                    -1L,           // id
                    output_stream_metadata, // metadata
                    schematic,     // schematic
                    output_stream_type ); // type

        // Terminal to configure a new File:
        final Wiring.Metadata file_metadata =
            new Wiring.Metadata (
                    wiring,        // wiring
                    -1L,           // id
                    schematic_namespace, // namespace
                    "file"         // name
                    );             // no tags for now
        final Wiring.Terminal file_terminal =
            new Wiring.Terminal (
                    wiring,        // wiring
                    -1L,           // id
                    file_metadata, // metadata
                    schematic,     // schematic
                    file_type );   // type

        // Terminal to configure a new path:
        final Wiring.Metadata path_metadata =
            new Wiring.Metadata (
                    wiring,        // wiring
                    -1L,           // id
                    schematic_namespace, // namespace
                    "path"         // name
                    );             // no tags for now
        final Wiring.Terminal path_terminal =
            new Wiring.Terminal (
                    wiring,        // wiring
                    -1L,           // id
                    path_metadata, // metadata
                    schematic,     // schematic
                    string_type ); // type

        // Terminal to read lines that will be output
        // to the currently configured output target:
        final Wiring.Metadata in_metadata =
            new Wiring.Metadata (
                    wiring,        // wiring
                    -1L,           // id
                    schematic_namespace, // namespace
                    "in"           // name
                    );             // no tags for now
        final Wiring.Terminal in_terminal =
            new Wiring.Terminal (
                    wiring,        // wiring
                    -1L,           // id
                    in_metadata,   // metadata
                    schematic,     // schematic
                    string_type ); // type

        // Terminal to output error messages:
        final Wiring.Metadata error_metadata =
            new Wiring.Metadata (
                    wiring,        // wiring
                    -1L,           // id
                    schematic_namespace, // namespace
                    "error"        // name
                    );             // no tags for now
        final Wiring.Terminal error_terminal =
            new Wiring.Terminal (
                    wiring,        // wiring
                    -1L,           // id
                    error_metadata, // metadata
                    schematic,     // schematic
                    string_type ); // type

        // Ground terminal, for discarding bad inputs etc:
        final Wiring.Metadata ground_metadata =
            new Wiring.Metadata (
                    wiring,        // wiring
                    -1L,           // id
                    schematic_namespace, // namespace
                    "ground"       // name
                    );             // no tags for now
        final Wiring.Terminal ground_terminal =
            new Wiring.Terminal (
                    wiring,        // wiring
                    -1L,           // id
                    ground_metadata, // metadata
                    schematic,     // schematic
                    any_type );    // type

        wiring.metadataTable.add ( writer_metadata );
        wiring.metadataTable.add ( output_stream_metadata );
        wiring.metadataTable.add ( file_metadata );
        wiring.metadataTable.add ( path_metadata );
        wiring.metadataTable.add ( in_metadata );
        wiring.metadataTable.add ( error_metadata );
        wiring.metadataTable.add ( ground_metadata );
        wiring.metadataTable.add ( schematic_metadata );
        wiring.namespaceTable.add ( schematic_namespace );
        wiring.terminalTable.add ( writer_terminal );
        wiring.terminalTable.add ( output_stream_terminal );
        wiring.terminalTable.add ( file_terminal );
        wiring.terminalTable.add ( path_terminal );
        wiring.terminalTable.add ( in_terminal );
        wiring.terminalTable.add ( error_terminal );
        wiring.terminalTable.add ( ground_terminal );
        wiring.schematicTable.add ( schematic );

        return schematic;
    }

    @Override
    public final Wiring.Circuit start (
            Wiring.Chip chip
            )
    {
        final Object target =
            chip.configurationValue ();
        if ( target == null )
        {
            // Default to stdout.
            final PrintWriter stdout =
                new PrintWriter ( System.out );
            chip.configure ( stdout );
        }
        else if ( target instanceof OutputStream )
        {
            final PrintWriter out =
                new PrintWriter ( (OutputStream) target );
            chip.configure ( out );
        }
        else if ( target instanceof File )
        {
            try
            {
                final PrintWriter out =
                    new PrintWriter (
                        new FileWriter ( (File) target )
                        );
                chip.configure ( out );
            }
            catch ( IOException e )
            {
                throw new IllegalArgumentException ( "ERROR Could not open"
                                                     + " FileWriter for file "
                                                     + ( (File) target ).getName ()
                                                     + " while starting "
                                                     + this
                                                     + " "
                                                     + chip
                                                     + " ("
                                                     + ( (File) target ).getAbsolutePath ()
                                                     + "): "
                                                     + e.getMessage (),
                                                     e );
            }
        }
        else if ( target instanceof String )
        {
            final File file = new File ( (String) target );
            try
            {
                final Writer out =
                    new FileWriter ( file );
                chip.configure ( out );
            }
            catch ( IOException e )
            {
                throw new IllegalArgumentException ( "ERROR Could not open"
                                                     + " FileWriter for file "
                                                     + (String) target
                                                     + " while starting "
                                                     + this
                                                     + " "
                                                     + chip
                                                     + " ("
                                                     + file.getAbsolutePath ()
                                                     + "): "
                                                     + e.getMessage (),
                                                     e );
            }
        }
        else if ( target instanceof PrintWriter )
        {
            // Npthing more to do.
        }
        else if ( target instanceof Writer )
        {
            final PrintWriter out = new PrintWriter ( (Writer) target );
            chip.configure ( out );
        }
        else
        {
            throw new IllegalArgumentException ( "ERROR Invalid target "
                                                 + StringRepresentation.of ( target,
                                                                             StringRepresentation.DEFAULT_OBJECT_LENGTH )
                                                 + " while starting "
                                                 + this );
        }

        return this;
    }

    @Override
    public final Wiring.Circuit stop (
            Wiring.Chip chip
            )
    {
        // !!! Maybe flush anything left in the buffer?

        final PrintWriter target = chip.configurationValue ( PrintWriter.class );
        // !!! try
        // !!! {
            target.close ();
        // !!! }
        // !!! catch ( IOException e )
        // !!! {
        // !!!     throw new IllegalStateException ( "ERROR: Could not stop "
        // !!!                                       + this
        // !!!                                       + " "
        // !!!                                       + chip
        // !!!                                       + ": "
        // !!!                                       + e.getMessage (),
        // !!!                                       e );
        // !!! }

        return this;
    }

    @Override
    public final Wiring.Carrier [] pull (
            Wiring.Chip chip,
            Wiring.Wire wire
            )
    {
        // Sorry bub, nothing for you.
        return new Wiring.Carrier [ 0 ];
    }

    @Override
    public final Wiring.Circuit push (
            Wiring.Chip chip,
            Wiring.Wire wire,
            Wiring.Carrier [] carriers
            )
    {
        if ( carriers.length == 0 )
        {
            return this;
        }

        // Reconfiguration?
        final String wire_name = wire.leg ().terminalName ();
        final Wiring.Carrier last = carriers [ carriers.length - 1 ];
        if ( "writer".equals ( wire_name ) )
        {
            final Writer new_target =
                last.dataValue ( Writer.class );
            if ( new_target == null )
            {
                // Default to stdout.
                final PrintWriter stdout =
                    new PrintWriter ( System.out );
                chip.configure ( stdout );
            }
            else if ( new_target instanceof PrintWriter )
            {
                chip.configure ( (PrintWriter) new_target );
            }
            else
            {
                final PrintWriter out = new PrintWriter ( new_target );
                chip.configure ( out );
            }
        }
        else if ( "output-stream".equals ( wire_name ) )
        {
            final OutputStream new_target =
                last.dataValue ( OutputStream.class );
            if ( new_target == null )
            {
                // Default to stdout.
                final PrintWriter stdout =
                    new PrintWriter ( System.out );
                chip.configure ( stdout );
            }
            else
            {
                final PrintWriter target = new PrintWriter ( new_target );
                chip.configure ( target );
            }
        }
        else if ( "file".equals ( wire_name ) )
        {
            final File new_target =
                last.dataValue ( File.class );
            if ( new_target == null )
            {
                // Default to stdout.
                final PrintWriter stdout =
                    new PrintWriter ( System.out );
                chip.configure ( stdout );
            }
            else
            {
                try
                {
                    final PrintWriter target =
                        new PrintWriter (
                            new FileWriter ( new_target )
                            );
                    chip.configure ( target );
                }
                catch ( IOException e )
                {
                    throw new IllegalArgumentException ( "ERROR Could not open"
                                                         + " FileWriter for file "
                                                         + new_target.getName ()
                                                         + " while starting "
                                                         + this
                                                         + " "
                                                         + chip
                                                         + " ("
                                                         + new_target.getAbsolutePath ()
                                                         + "): "
                                                         + e.getMessage (),
                                                         e );
                }
            }
        }
        else if ( "path".equals ( wire_name ) )
        {
            final String new_target =
                last.dataValue ( String.class );
            if ( new_target == null )
            {
                // Default to stdout.
                final PrintWriter stdout =
                    new PrintWriter ( System.out );
                chip.configure ( stdout );
            }
            else
            {
                final File new_file = new File ( new_target );
                try
                {
                    final PrintWriter target =
                        new PrintWriter (
                            new FileWriter ( new_file )
                            );
                    chip.configure ( target );
                }
                catch ( IOException e )
                {
                    throw new IllegalArgumentException ( "ERROR Could not open"
                                                         + " FileWriter for file "
                                                         + new_target
                                                         + " while starting "
                                                         + this
                                                         + " "
                                                         + chip
                                                         + " ("
                                                         + new_file.getAbsolutePath ()
                                                         + "): "
                                                         + e.getMessage (),
                                                         e );
                }
            }
        }

        if ( ! "in".equals ( wire_name ) )
        {
            // Nothing more we can do for ya, bub.
            return this;
        }

        final PrintWriter writer =
            chip.configurationValue ( PrintWriter.class );
        // !!! try
        // !!! {
            for ( Wiring.Leg in : chip.legs ( "in" ) )
            {
                final Wiring.Carrier [] lines = in.pull ();
                for ( Wiring.Carrier line : lines )
                {
                    final String text =
                        line.dataValue ( String.class );
                    writer.println ( text );
                }
            }
            // !!! }
            // !!! catch ( IOException e )
            // !!! {
            // !!!     final Wiring.Type string_type =
            // !!!         Wiring.Type.forClass ( chip.wiring (),
            // !!!                                String.class );
            // !!! 
            // !!!     final String error_message =
            // !!!         "ERROR failed to write output"
            // !!!         + " to "
            // !!!         + StringRepresentation.of ( writer,
            // !!!                                     StringRepresentation.DEFAULT_OBJECT_LENGTH )
            // !!!         + " for Circuit "
            // !!!         + this
            // !!!         + " while trying to pull data"
            // !!!         + " from Chip "
            // !!!         + chip
            // !!!         + " on Wire "
            // !!!         + wire
            // !!!         + ": "
            // !!!         + e.getMessage ();
            // !!!     final Wiring.Carrier error =
            // !!!         this.createError ( chip,
            // !!!                            string_type,
            // !!!                            error_message,
            // !!!                            e );
            // !!!     for ( Wiring.Leg leg : chip.legs ( "error" ) )
            // !!!     {
            // !!!         leg.push ( error );
            // !!!     }
            // !!!     final Wiring.Leg [] drains = chip.legs ( "drain" );
            // !!!     final Wiring.Leg [] grounds = chip.legs ( "ground" );
            // !!! }

        return this;
    }

    private final Wiring.Carrier createError (
            Wiring.Chip chip,
            Wiring.Type string_type,
            String error_message,
            Exception cause_or_null
            )
    {
        final IOException exception =
            new IOException ( error_message );
        if ( cause_or_null != null )
        {
            exception.initCause ( cause_or_null );
        }

        final Wiring.Metadata error_metadata =
            new Wiring.Metadata (
                chip.wiring (), // wiring
                -1L,           // id
                chip.metadata ().namespaceID, // namespace_id
                "write-line-error" // name
                                 );             // no tags for now
        final Wiring.Data error_data =
            new Wiring.Data (
                chip.wiring (), // wiring
                -1L,            // id
                string_type,    // type
                exception );    // value
        final Wiring.Carrier error =
            new Wiring.Carrier (
                chip.wiring (), // wiring
                -1L,            // id
                error_metadata,  // metadata
                error_data );    // data

        chip.wiring ().metadataTable.add ( error_metadata );
        chip.wiring ().dataTable.add ( error_data );
        chip.wiring ().carrierTable.add ( error );

        return error;
    }
}
