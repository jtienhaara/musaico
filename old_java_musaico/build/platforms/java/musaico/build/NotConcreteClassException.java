package musaico.build;


/**
 * <p>
 * Exception thrown when trying determine the serialVersion information
 * of a class or interface that is not even Serializable.
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
public class NotConcreteClassException
    extends Exception
{
    /** The MusaicoModule to which this class belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of this class's module, in YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    public NotConcreteClassException (
                                      String message
                                      )
    {
        super ( message );
    }

    public NotConcreteClassException ( String message,
                                       Throwable cause )
    {
        super ( message, cause );
    }
}
