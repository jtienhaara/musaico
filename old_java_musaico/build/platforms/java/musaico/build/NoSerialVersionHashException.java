package musaico.build;


/**
 * <p>
 * Exception thrown when trying to call the static serialVersionHash
 * field of a Serializable class, containing a hash of its content
 * so that changes to the class will be pointed out at compile time,
 * forcing the developer to change the serialVersionUID of the Serializable
 * class.
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
public class NoSerialVersionHashException
    extends Exception
{
    /** The MusaicoModule to which this class belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of this class's module, in YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    public NoSerialVersionHashException (
                                         String message
                                         )
    {
        super ( message );
    }

    public NoSerialVersionHashException ( String message,
                                          Throwable cause )
    {
        super ( message, cause );
    }
}
