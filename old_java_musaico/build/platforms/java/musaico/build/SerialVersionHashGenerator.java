package musaico.build;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import java.rmi.server.UnicastRemoteObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * Serializes a class, hashes the serialized byte form of the
 * class, then turns the hash into a long integer.
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
 * @see musaico.build.MODULE#COPYRIGHT
 * @see musaico.build.MODULE#LICENSE
 */
public class SerialVersionHashGenerator
{
    /** The MusaicoModule to which this class belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    public byte [] hashObjectBytes (
                                    Serializable serializable
                                    )
        throws IOException
    {
        final byte [] hash_bytes;
        if ( ( serializable instanceof Throwable )
             || ( serializable instanceof UnicastRemoteObject ) )
        {
            // Special case for certain classes which have time-dependent
            // data that makes serializing NON-idempotent.
            hash_bytes = new SerialHasher ().hash ( serializable.getClass () );
        }
        else
        {
            // Normal objects.
            hash_bytes = new SerialHasher ().hash ( serializable );
        }

        return hash_bytes;
    }


    public byte [] hashSourceBytes (
                                    String source_filename
                                    )
        throws IOException
    {
        final byte [] hash_bytes =
            new SourceHasher ().hash ( source_filename );

        return hash_bytes;
    }


    public String bytesToHexString (
                                    byte [] bytes
                                    )
    {
        StringBuilder hex = new StringBuilder ();
        hex.append ( "0x" );
        if ( bytes.length == 0 )
        {
            hex.append ( "00" );
        }

        for ( byte input : bytes )
        {
            // Get rid of the sign.  Signed bytes are for sissies.
            int int_value = ( (int) input ) & 0xFF;
            String int_string = Integer.toHexString ( int_value );
            int_string = int_string.toUpperCase ();

            for ( int pad = int_string.length (); pad < 2; pad ++ )
            {
                hex.append ( "0" );
            }

            hex.append ( int_string );
        }

        return hex.toString ();
    }


    public String hashObjectHexString (
                                       Serializable serializable
                                       )
        throws IOException
    {
        final byte [] hash_bytes = this.hashObjectBytes ( serializable );

        final String hash = this.bytesToHexString ( hash_bytes );

        return hash;
    }


    public long hashObject63Bit (
                                 Serializable serializable
                                 )
        throws IOException
    {
        byte [] hash_bytes = new SerialHasher ().hash ( serializable );

        // Sign bit will never be set.  We lossily crunch down 160
        // bits into 60 bits.
        long hash = 0L;
        int shift = hash_bytes.length - 1;
        for ( byte hash_byte : hash_bytes )
        {
            // Get rid of the sign.  Signed bytes are for sissies.
            long hash_int = ( (long) hash_byte ) & 0xFF;
            long truncated_hash_int = hash_int % 8;
            long shifted_truncated_hash_int = truncated_hash_int << shift;
            hash += shifted_truncated_hash_int;
            shift ++;
        }

        return hash;
    }


    public byte [] hash (
                         Class<?> class_to_hash
                         )
        throws NotSerializableException,
               NotConcreteClassException,
               NoSerialVersionHashException,
               SerializingException
    {
        /* !!!
        if ( ! Serializable.class.isAssignableFrom ( class_to_hash ) )
        {
            throw new NotSerializableException ( "Class/interface " + class_to_hash
                                                 + " is not Serializable" );
        }
        else if ( class_to_hash.isInterface ()
                  || class_to_hash.isEnum ()
                  || Modifier.isAbstract ( class_to_hash.getModifiers () ) )
        {
            throw new NotConcreteClassException ( "Class/interface "
                                                  + class_to_hash
                                                  + " is an interface or abstract class or enum or etc" );
        }
        !!! */

        this.checkSerialVersionHashField ( class_to_hash );

        final String source_filename =
            this.sourceFilename ( class_to_hash );
        final byte [] hash;
        try
        {
            // hash = this.hashObjectBytes ( class_to_hash );
            hash = this.hashSourceBytes ( source_filename );
        }
        catch ( Exception e )
        {
            throw new SerializingException ( "Could not hash class "
                                             + class_to_hash, e );
        }

        return hash;
    }


    protected void checkSerialVersionHashField (
                                                Class<?> class_to_hash
                                                )
        throws NoSerialVersionHashException,
               SerializingException
    {
        final Field serial_version_hash;
        try
        {
            serial_version_hash =
                class_to_hash.getDeclaredField ( "serialVersionHash" );
            if ( serial_version_hash == null )
            {
                throw new NoSerialVersionHashException ( "" + class_to_hash
                                                         + " must provide a private static String serialVersionHash field"
                                                         + " which contains a hash of the serialVersionObject (or \"\" initially)" );
            }
        }
        catch ( NoSerialVersionHashException e )
        {
            throw e;
        }
        catch ( Exception e )
        {
            throw new NoSerialVersionHashException ( "" + class_to_hash
                                                     + " must provide a private static String serialVersionHash field"
                                                     + " which contains a hash of the serialVersionObject (or \"\" initially)",
                                                     e );
        }

        final Field static_final;
        byte [] hash;
        try
        {
            static_final = Field.class.getDeclaredField ( "modifiers" );

            //
            // Ugly horrible kludge courtesy:
            // http://stackoverflow.com/questions/3301635/change-private-static-final-field-using-java-reflection
            //
            static_final.setAccessible ( true );
            static_final.setInt ( serial_version_hash,
                                  serial_version_hash.getModifiers ()
                                  & ~ Modifier.FINAL );
            serial_version_hash.setAccessible ( true );
            final String original_hash_hex_string = (String)
                serial_version_hash.get ( null );
            serial_version_hash.set ( null, "" ); // blank hash string.
            serial_version_hash.setAccessible ( false );
            static_final.setAccessible ( false );

            static_final.setAccessible ( true );
            static_final.setInt ( serial_version_hash,
                                  serial_version_hash.getModifiers ()
                                  | Modifier.FINAL );
            serial_version_hash.setAccessible ( true );
            serial_version_hash.set ( null, original_hash_hex_string );
            serial_version_hash.setAccessible ( false );
            static_final.setAccessible ( true );
        }
        catch ( Exception e )
        {
            throw new SerializingException ( "Could not hash class "
                                             + class_to_hash, e );
        }
    }


    private final Map<Class<?>, String> sourceFilenames =
        new HashMap<Class<?>, String> ();

    public Class<?> [] filterClasses (
                                      String [] class_names
                                      )
        throws ClassNotFoundException
    {
        final List<Class<?>> classes_list = new ArrayList<Class<?>> ();
        for ( String unfiltered_class_name : class_names )
        {
            String class_name =
                unfiltered_class_name.replaceAll ( "\\$.+$", "" );
            if ( ! class_name.equals ( unfiltered_class_name ) )
            {
                continue;
            }
            else if ( class_name.endsWith ( "MODULE" ) )
            {
                continue;
            }
            else if ( class_name.startsWith ( "musaico.module." ) )
            {
                // Don't expect the musaico.module package, which defines
                // how modules work, to provide its own serial version
                // hashes.  Leave it alone.
                continue;
            }

            final Class<?> class_to_instantiate;
            try
            {
                class_to_instantiate = Class.forName ( class_name );
            }
            catch ( Exception e )
            {
                throw new ClassNotFoundException ( "Error loading "
                                                   + class_name
                                                   + ": ",
                                                   e );
            }

            classes_list.add ( class_to_instantiate );

            final String source_filename =
                class_to_instantiate.getSimpleName ()
                + ".java";
            this.sourceFilenames.put ( class_to_instantiate,
                                       source_filename );
        }

        final Class<?> [] template = new Class<?> [ classes_list.size () ];
        final Class<?> [] classes = classes_list.toArray ( template );

        return classes;
    }


    /** Call filterClasses() first. */
    public String sourceFilename (
                                  Class<?> compiled_class
                                  )
    {
        return this.sourceFilenames.get ( compiled_class );
    }


    public static void main (
                             String [] class_names
                             )
        throws Exception
    {
        SerialVersionHashGenerator hasher =
            new SerialVersionHashGenerator ();

        System.out.println ( "" );
        System.out.println ( "" );

        for ( Class<?> class_to_hash : hasher.filterClasses ( class_names ) )
        {
            final String class_name = class_to_hash.getName ();
            try
            {
                final byte [] hash_bytes =
                    hasher.hash ( class_to_hash );
                final String hash =
                    hasher.bytesToHexString ( hash_bytes );

                System.out.println ( "" );
                System.out.println ( class_name + " serialVersionHash:" );
                System.out.println ( "    " + hash );
            }
            catch ( NotSerializableException e )
            {
                // Not serializable.
                // Quietly carry on to the next class.
            }
            catch ( NotConcreteClassException e )
            {
                // Not a concrete class.
                // Quietly carry on to the next class.
            }
            // NoSerialVersionHashException,
            // SerializingException.
        }

        System.out.println ( "" );
    }
}
