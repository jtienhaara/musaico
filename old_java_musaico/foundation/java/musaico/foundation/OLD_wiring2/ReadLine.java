package musaico.foundation.wiring;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;

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
public class ReadLine
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
        final Wiring.Type any_type =
            Wiring.Type.any ( wiring );
        final Wiring.Type reader_type =
            Wiring.Type.forClass ( wiring,
                                   Reader.class );
        final Wiring.Type input_stream_type =
            Wiring.Type.forClass ( wiring,
                                   InputStream.class );
        final Wiring.Type file_type =
            Wiring.Type.forClass ( wiring,
                                   File.class );
        final Wiring.Type string_type =
            Wiring.Type.forClass ( wiring,
                                   String.class );
        final Wiring.Type string_builder_type =
            Wiring.Type.forClass ( wiring,
                                   StringBuilder.class );

        // ReadLine:
        final Wiring.Metadata schematic_metadata =
            new Wiring.Metadata (
                    wiring,        // wiring
                    -1L,           // id
                    -1L,           // namespace_id
                    "read-line"    // name
                    );             // no tags for now
        final Wiring.Schematic schematic =
            new Wiring.Schematic (
                    wiring,        // wiring
                    -1L,           // id
                    schematic_metadata, // metadata
                    this,          // circuit
                    any_type,      // configuration_data_type !!!! hack for now.  Make it only accept valid configuration objects in future.
                    string_builder_type ); // data_type

        // Namespace for ReadLine's terminals:
        final Wiring.Namespace schematic_namespace =
            new Wiring.Namespace (
                    wiring,            // wiring
                    -1L,               // id
                    schematic_metadata // metadata
                    );

        // Terminal to configure a new Reader:
        final Wiring.Metadata reader_metadata =
            new Wiring.Metadata (
                    wiring,        // wiring
                    -1L,           // id
                    schematic_namespace, // namespace
                    "reader"       // name
                    );             // no tags for now
        final Wiring.Terminal reader_terminal =
            new Wiring.Terminal (
                    wiring,        // wiring
                    -1L,           // id
                    reader_metadata, // metadata
                    schematic,     // schematic
                    reader_type ); // type

        // Terminal to configure a new InputStream:
        final Wiring.Metadata input_stream_metadata =
            new Wiring.Metadata (
                    wiring,        // wiring
                    -1L,           // id
                    schematic_namespace, // namespace
                    "input-stream" // name
                    );             // no tags for now
        final Wiring.Terminal input_stream_terminal =
            new Wiring.Terminal (
                    wiring,        // wiring
                    -1L,           // id
                    input_stream_metadata, // metadata
                    schematic,     // schematic
                    input_stream_type ); // type

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

        // Terminal to output lines from the currently
        // configured input source:
        final Wiring.Metadata out_metadata =
            new Wiring.Metadata (
                    wiring,        // wiring
                    -1L,           // id
                    schematic_namespace, // namespace
                    "out"          // name
                    );             // no tags for now
        final Wiring.Terminal out_terminal =
            new Wiring.Terminal (
                    wiring,        // wiring
                    -1L,           // id
                    out_metadata,  // metadata
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

        wiring.metadataTable.add ( reader_metadata );
        wiring.metadataTable.add ( input_stream_metadata );
        wiring.metadataTable.add ( file_metadata );
        wiring.metadataTable.add ( path_metadata );
        wiring.metadataTable.add ( out_metadata );
        wiring.metadataTable.add ( error_metadata );
        wiring.metadataTable.add ( ground_metadata );
        wiring.metadataTable.add ( schematic_metadata );
        wiring.namespaceTable.add ( schematic_namespace );
        wiring.terminalTable.add ( reader_terminal );
        wiring.terminalTable.add ( input_stream_terminal );
        wiring.terminalTable.add ( file_terminal );
        wiring.terminalTable.add ( path_terminal );
        wiring.terminalTable.add ( out_terminal );
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
        final Object source =
            chip.configurationValue ();
        if ( source == null )
        {
            // Default to stdin.
            final Reader stdin =
                new InputStreamReader ( System.in );
            chip.configure ( stdin );
        }
        else if ( source instanceof InputStream )
        {
            final Reader in =
                new InputStreamReader ( (InputStream) source );
            chip.configure ( in );
        }
        else if ( source instanceof File )
        {
            try
            {
                final Reader in =
                    new FileReader ( (File) source );
                chip.configure ( in );
            }
            catch ( IOException e )
            {
                throw new IllegalArgumentException ( "ERROR Could not open"
                                                     + " FileReader for file "
                                                     + ( (File) source ).getName ()
                                                     + " while starting "
                                                     + this
                                                     + " "
                                                     + chip
                                                     + " ("
                                                     + ( (File) source ).getAbsolutePath ()
                                                     + "): "
                                                     + e.getMessage (),
                                                     e );
            }
        }
        else if ( source instanceof String )
        {
            final File file = new File ( (String) source );
            try
            {
                final Reader in =
                    new FileReader ( file );
                chip.configure ( in );
            }
            catch ( IOException e )
            {
                throw new IllegalArgumentException ( "ERROR Could not open"
                                                     + " FileReader for file "
                                                     + (String) source
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
        else if ( source instanceof Reader )
        {
            // Npthing more to do.
        }
        else
        {
            throw new IllegalArgumentException ( "ERROR Invalid source "
                                                 + StringRepresentation.of ( source,
                                                                             StringRepresentation.DEFAULT_OBJECT_LENGTH )
                                                 + " while starting "
                                                 + this );
        }

        StringBuilder buffer = chip.dataValue ( StringBuilder.class );
        if ( buffer == null )
        {
            buffer = new StringBuilder ();
            chip.dataUpdate ( buffer );
        }

        return this;
    }

    @Override
    public final Wiring.Circuit stop (
            Wiring.Chip chip
            )
    {
        // !!! Maybe flush anything left in the buffer?

        final Reader source = chip.configurationValue ( Reader.class );
        try
        {
            source.close ();
        }
        catch ( IOException e )
        {
            throw new IllegalStateException ( "ERROR: Could not stop "
                                              + this
                                              + " "
                                              + chip
                                              + ": "
                                              + e.getMessage (),
                                              e );
        }

        return this;
    }

    @Override
    public final Wiring.Carrier [] pull (
            Wiring.Chip chip,
            Wiring.Wire wire
            )
    {
        final Wiring.Type string_type =
            Wiring.Type.forClass ( chip.wiring (),
                                   String.class );

        final Reader reader =
            chip.configurationValue ( Reader.class );
        final StringBuilder buffer =
            chip.dataValue ( StringBuilder.class );
        try
        {
            final char [] block = new char [ 4096 ];
            while ( reader.ready () )
            {
                final int num_chars_read =
                    reader.read ( block );
                buffer.append ( block, 0, num_chars_read );
            }
        }
        catch ( IOException e )
        {
            final String error_message =
                "ERROR failed to read input"
                + " from "
                + StringRepresentation.of ( reader,
                                            StringRepresentation.DEFAULT_OBJECT_LENGTH )
                + " for Circuit "
                + this
                + " while trying to pull data"
                + " from Chip "
                + chip
                + " on Wire "
                + wire
                + ": "
                + e.getMessage ();
            final Wiring.Carrier error =
                this.createError ( chip,
                                   string_type,
                                   error_message,
                                   e );
            for ( Wiring.Leg leg : chip.legs ( "error" ) )
            {
                leg.push ( error );
            }
            final Wiring.Leg [] drains = chip.legs ( "drain" );
            final Wiring.Leg [] grounds = chip.legs ( "ground" );
        }

        if ( ! "out".equals ( wire.leg ().terminalName () ) )
        {
            // Nothing we can do for ya, bub.
            return new Wiring.Carrier [ 0 ];
        }

        int buffer_length = buffer.length ();
        if ( buffer_length == 0 )
        {
            // Nothing we can do.
            return new Wiring.Carrier [ 0 ];
        }

        final List<Wiring.Carrier> lines_list = new ArrayList<Wiring.Carrier> ();
        final Matcher newline = NEWLINE.matcher ( buffer );
        int offset = 0;
        while ( newline.find ( offset ) )
        {
            final int next = newline.start ();

            final String text =
                buffer.substring ( offset, next - offset - 1 );

            final Wiring.Carrier line = this.createLine ( chip, string_type, text );
            lines_list.add ( line );

            offset = newline.end ();
        }

        if ( lines_list.size () == 0 )
        {
            // No newlines yet.
            return new Wiring.Carrier [ 0 ];
        }

        // Remove the buffered lines we're about to ship out.
        buffer.delete ( 0, offset );

        final Wiring.Carrier [] template =
            new Wiring.Carrier [ lines_list.size () ];
        final Wiring.Carrier [] lines =
            lines_list.toArray ( template );

        return lines;
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
        if ( "reader".equals ( wire_name ) )
        {
            final Reader new_source =
                last.dataValue ( Reader.class );
            if ( new_source == null )
            {
                // Default to stdin.
                final Reader stdin =
                    new InputStreamReader ( System.in );
                chip.configure ( stdin );
            }
            else
            {
                chip.configure ( new_source );
            }
        }
        else if ( "input-stream".equals ( wire_name ) )
        {
            final InputStream new_source =
                last.dataValue ( InputStream.class );
            if ( new_source == null )
            {
                // Default to stdin.
                final Reader stdin =
                    new InputStreamReader ( System.in );
                chip.configure ( stdin );
            }
            else
            {
                final Reader source = new InputStreamReader ( new_source );
                chip.configure ( source );
            }
        }
        else if ( "file".equals ( wire_name ) )
        {
            final File new_source =
                last.dataValue ( File.class );
            if ( new_source == null )
            {
                // Default to stdin.
                final Reader stdin =
                    new InputStreamReader ( System.in );
                chip.configure ( stdin );
            }
            else
            {
                try
                {
                    final Reader source = new FileReader ( new_source );
                    chip.configure ( source );
                }
                catch ( IOException e )
                {
                    throw new IllegalArgumentException ( "ERROR Could not open"
                                                         + " FileReader for file "
                                                         + new_source.getName ()
                                                         + " while starting "
                                                         + this
                                                         + " "
                                                         + chip
                                                         + " ("
                                                         + new_source.getAbsolutePath ()
                                                         + "): "
                                                         + e.getMessage (),
                                                         e );
                }
            }
        }
        else if ( "path".equals ( wire_name ) )
        {
            final String new_source =
                last.dataValue ( String.class );
            if ( new_source == null )
            {
                // Default to stdin.
                final Reader stdin =
                    new InputStreamReader ( System.in );
                chip.configure ( stdin );
            }
            else
            {
                final File new_file = new File ( new_source );
                try
                {
                    final Reader source = new FileReader ( new_file );
                    chip.configure ( source );
                }
                catch ( IOException e )
                {
                    throw new IllegalArgumentException ( "ERROR Could not open"
                                                         + " FileReader for file "
                                                         + new_source
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
        else
        {
            // No reconfiguration, nothing to push.
            return this;
        }

        // After we've reconfigured the input source,
        // send the buffer contents (if any) to ground.
        StringBuilder buffer = chip.dataValue ( StringBuilder.class );
        if ( buffer.length () > 0 )
        {
            final Wiring.Leg [] grounds = chip.legs ( "ground" );
            if ( grounds.length > 0 )
            {
                final Wiring.Type string_type =
                    Wiring.Type.forClass ( chip.wiring (),
                                           String.class );

                final String unpulled_text =
                    buffer.toString ();
                final Wiring.Carrier line =
                    this.createLine ( chip, string_type, unpulled_text );
                final Wiring.Carrier [] discards =
                    new Wiring.Carrier [] { line };

                for ( Wiring.Leg ground : grounds )
                {
                    ground.push ( discards );
                }
            }

            buffer.delete ( 0, buffer.length () );
        }

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
                "read-line-error" // name
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

    private final Wiring.Carrier createLine (
            Wiring.Chip chip,
            Wiring.Type string_type,
            String text
            )
    {
        final Wiring.Metadata line_metadata =
            new Wiring.Metadata (
                chip.wiring (), // wiring
                -1L,           // id
                chip.metadata ().namespaceID, // namespace_id
                "read-line"    // name
                                 );             // no tags for now
        final Wiring.Data line_data =
            new Wiring.Data (
                chip.wiring (), // wiring
                -1L,            // id
                string_type,    // type
                text );         // value
        final Wiring.Carrier line =
            new Wiring.Carrier (
                chip.wiring (), // wiring
                -1L,            // id
                line_metadata,  // metadata
                line_data );    // data

        chip.wiring ().metadataTable.add ( line_metadata );
        chip.wiring ().dataTable.add ( line_data );
        chip.wiring ().carrierTable.add ( line );

        return line;
    }
}
