package musaico.build;


import java.lang.reflect.Field;


/**
 * <p>
 * Relies on one of the more specific TestSerializable* classes
 * to check the serial version hashes of all the classes in a module
 * vs. some freshly generated hash (of each individual class
 * for TestSerializableClass, or of the sum of all classes in the module
 * for TestSerializableModule).  If the serial version hash as
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
public class TestSerializable
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
        // Test the whole module.
        TestSerializableModule.main ( class_names );
    }
}
