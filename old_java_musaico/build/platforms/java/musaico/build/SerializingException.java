package musaico.build;


/**
 * <p>
 * Exception thrown when trying to serialize an instance of a
 * Serializable class.
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
public class SerializingException
    extends Exception
{
    /** The MusaicoModule to which this class belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of this class's module, in YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    public SerializingException (
                                 String message
                                 )
    {
        super ( message );
    }

    public SerializingException ( String message,
                                  Throwable cause )
    {
        super ( message, cause );
    }
}
