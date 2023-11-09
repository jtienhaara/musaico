package musaico.build;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import java.rmi.server.UnicastRemoteObject;

import java.util.Calendar;
import java.util.Date;


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
public class ModuleSerialVersionHashGenerator
    extends SerialVersionHashGenerator
{
    /** The MusaicoModule to which this class belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    @Override
    protected void checkSerialVersionHashField (
                                                Class<?> class_to_hash
                                                )
        throws NoSerialVersionHashException,
               SerializingException
    {
        // Do nothing, we do not need a serialVersionHash in each
        // class since the MODULE has it.
    }


    public static void main (
                             String [] class_names
                             )
        throws Exception
    {
        ModuleSerialVersionHashGenerator hasher =
            new ModuleSerialVersionHashGenerator ();

        System.out.println ( "" );
        System.out.println ( "" );

        // For now we're using 160 bit SHA-1, but this string of bytes
        // should be long enough that the underlying hasher can change
        // in future without requiring a change to this class.
        byte [] running_hash = new byte [ 64 ];
        for ( int b = 0; b < running_hash.length; b ++ )
        {
            running_hash [ b ] = 0;
        }
        int max_bytes = 0;

        for ( Class<?> class_to_hash : hasher.filterClasses ( class_names ) )
        {
            final String class_name = class_to_hash.getName ();
            try
            {
                final byte [] hash =
                    hasher.hash ( class_to_hash );

                for ( int b = 0; b < hash.length; b ++ )
                {
                    running_hash [ b ] += hash [ b ];
                }

                if ( hash.length > max_bytes )
                {
                    max_bytes = hash.length;
                }
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

        final byte [] module_hash = new byte [ max_bytes ];
        System.arraycopy ( running_hash, 0,
                           module_hash, 0, max_bytes );

        final String hex_hash_string =
            hasher.bytesToHexString ( module_hash );

        System.out.println ( "" );
        System.out.println ( "serialVersionHash:" );
        System.out.println ( "    " + hex_hash_string );

        System.out.println ( "" );
    }
}
