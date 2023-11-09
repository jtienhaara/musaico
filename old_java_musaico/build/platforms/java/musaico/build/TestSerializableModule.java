package musaico.build;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.List;


import musaico.module.MusaicoModule;


/**
 * <p>
 * Checks the serial version hash of a class vs. a freshly generated
 * hash of all the specified classes ("the module").  If the serial
 * version hash as coded does not match the generated hash, throws
 * an exception.  Useful for making sure maintainers keep the
 * serial version UID of each class up to date, since a change in the
 * hash fails the compile stage and forces the developer to update
 * the serial version tidbits.
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
public class TestSerializableModule
{
    /** The MusaicoModule to which this class belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    public static void main (
                             String [] class_names
                             )
        throws ClassNotFoundException,
               NoSerialVersionHashException,
               NotConcreteClassException,
               NotSerializableException,
               SerializingException
    {
        StringBuilder messages = new StringBuilder ();

        ModuleSerialVersionHashGenerator hasher =
            new ModuleSerialVersionHashGenerator ();

        // For now we're using 160 bit SHA-1, but this string of bytes
        // should be long enough that the underlying hasher can change
        // in future without requiring a change to this class.
        byte [] running_hash = new byte [ 64 ];
        for ( int b = 0; b < running_hash.length; b ++ )
        {
            running_hash [ b ] = 0;
        }
        int max_bytes = 0;

        final List<Class<?>> classes_to_test =
            new ArrayList<Class<?>> ();
        for ( Class<?> class_to_hash : hasher.filterClasses ( class_names ) )
        {
            final String class_name = class_to_hash.getName ();

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

            classes_to_test.add ( class_to_hash );
            // Throw Exceptions right up the hole.
        }

        final byte [] module_hash = new byte [ max_bytes ];
        System.arraycopy ( running_hash, 0,
                           module_hash, 0, max_bytes );

        final String expected_hash =
            hasher.bytesToHexString ( module_hash );
        System.out.println ( "Module serial version hash: " + expected_hash );

        for ( Class<?> class_to_test : classes_to_test )
        {
            final String class_name = class_to_test.getName ();
            System.out.println ( "TestSerializableModule " + class_name );

            try
            {
                final Field module_constant;
                try
                {
                    module_constant =
                        class_to_test.getDeclaredField ( "PARENT_MODULE" );
                }
                catch ( NoSuchFieldException e )
                {
                    messages.append ( "ERROR: No PARENT_MODULE defined for "
                                      + class_to_test );
                    messages.append ( "\n" );
                    messages.append ( "Declare a public static final PARENT_MODULE pointing" );
                    messages.append ( "\n" );
                    messages.append ( "to this package's MODULE class, which must implement" );
                    messages.append ( "\n" );
                    messages.append ( "musaico.module.MusaicoModule." );
                    messages.append ( "\n" );
                    messages.append ( "This will allow all classes in the module (including this" );
                    messages.append ( "\n" );
                    messages.append ( "one) to have their cumulative serial version hash calculated." );
                    messages.append ( "\n" );
                    messages.append ( "\n" );

                    continue;
                }

                module_constant.setAccessible ( true );

                final MusaicoModule module = (MusaicoModule)
                    module_constant.get ( null );

                module_constant.setAccessible ( false );


                final Method get_hash =
                    module.getClass ().getDeclaredMethod ( "hash" );
                final String actual_hash = (String)
                    get_hash.invoke ( module );

                if ( ! actual_hash.equals ( expected_hash ) )
                {
                    messages.append ( "ERROR: Incorrect serialVersionHash returned by "
                                      + class_to_test + ".MODULE.hash ()" );
                    messages.append ( "\n" );
                    messages.append ( "    Actual:     " + actual_hash );
                    messages.append ( "\n" );
                    messages.append ( "    Calculated: " + expected_hash );
                    messages.append ( "\n" );
                    messages.append ( "Replace the serialVersionHash with the calculated value above,\n" );
                    messages.append ( "Modify the serialVersionUID to today's date in YYYYMMDD format,\n" );
                    messages.append ( "compile the class, and test serializable again.\n" );
                    messages.append ( "\n" );
                    messages.append ( "\n" );
                }
            }
            catch ( Exception e )
            {
                messages.append ( "Could not hash class / retrieve actual serialVersionHash from class " + class_to_test + " :" );
                messages.append ( "\n" );
                messages.append ( "    " + e );
                messages.append ( "\n" );
                messages.append ( "\n" );
            }
        }

        System.out.println ( "" );
        System.out.println ( "" );

        if ( messages.length () > 0 )
        {
            throw new SerializingException ( messages.toString () );
        }

        System.out.println ( "PASSED" );

        System.exit ( 0 );
    }
}
