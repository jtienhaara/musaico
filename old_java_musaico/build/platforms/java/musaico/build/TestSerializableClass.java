package musaico.build;


import java.lang.reflect.Field;


/**
 * <p>
 * Checks the serial version hash of a class vs. its serialVersionObject ()
 * being serialized and hashed fresh.  If the serial version hash as
 * coded does not match the generated hash, throws an exception.  Useful
 * for making sure maintainers keep the serial version UID of each class up
 * to date, since a change in the hash fails the compile stage and forces
 * the developer to update the serial version tidbits.
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
public class TestSerializableClass
{
    /** The MusaicoModule to which this class belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    public static void main (
                             String [] class_names
                             )
        throws ClassNotFoundException,
               SerializingException
    {
        StringBuilder messages = new StringBuilder ();

        SerialVersionHashGenerator hasher = new SerialVersionHashGenerator ();
        for ( Class<?> class_to_test : hasher.filterClasses ( class_names ) )
        {
            final String class_name = class_to_test.getName ();
            System.out.println ( "TestSerializableClass "
                                 + class_name );

            final String expected_hash;
            try
            {
                final byte [] hash_bytes = hasher.hash ( class_to_test );
                expected_hash = hasher.bytesToHexString ( hash_bytes );
            }
            catch ( NotSerializableException e )
            {
                // Don't bother with this class, it is not Serializable.
                continue;
            }
            catch ( NotConcreteClassException e )
            {
                // Don't bother with this class, it is an interface or
                // abstract class.
                continue;
            }
            catch ( NoSerialVersionHashException e )
            {
                throw new SerializingException ( "Class " + class_name
                                                 + " is Serializable"
                                                 + " but does not provide"
                                                 + " serialVersionHash",
                                                 e );
            }
            // Throw SerializingException right up the hole.

            try
            {
                final Field serial_version_hash =
                    class_to_test.getDeclaredField ( "serialVersionHash" );

                serial_version_hash.setAccessible ( true );

                final String actual_hash = (String)
                    serial_version_hash.get ( null );

                serial_version_hash.setAccessible ( false );

                if ( ! actual_hash.equals ( expected_hash ) )
                {
                    messages.append ( "Incorrect serialVersionHash in class "
                                      + class_to_test );
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
