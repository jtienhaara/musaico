
package musaico.foundation.domains;


/**
 * <p>
 * A helper class which creates nice, short, but helpful class names,
 * such as "Tire" or "Vehicle.Tire" or "Vehicle.Tire$3", excluding
 * the package path, but including the useful details that a human
 * would typically want when reading, say, an error message.
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
 * @see musaico.foundation.typing.MODULE#COPYRIGHT
 * @see musaico.foundation.typing.MODULE#LICENSE
 */
public class ClassName
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * @return A nice simple class name, such as "String"
     *         or "Tire", and so on, from the specified
     *         class name String.  Never null.
     */
    public static final String from (
                                     String class_name
                                     )
    {
        if ( class_name == null )
        {
            return "<null class>";
        }

        final int last_dot = class_name.lastIndexOf ( '.' );
        if ( last_dot < 0 )
        {
            return class_name;
        }

        final String simple_class_name =
            class_name.substring ( last_dot + 1 );
        return simple_class_name;
    }


    /**
     * @return A nice simple class name, such as "String"
     *         or "Tire" or "Vehicle.Tire" or "Vehicle.Tire$3"
     *         and so on.  Never null.
     */
    public static final String of (
                                   Class<?> main_class
                                   )
    {
        if ( main_class == null )
        {
            return "<null class>";
        }

        final StringBuilder sbuf = new StringBuilder ();
        Class<?> enclosing_class = main_class.getEnclosingClass ();
        for ( int ec = 0;
              ec < 6 && enclosing_class != null;
              ec ++ )
        {
            sbuf.append ( enclosing_class.getSimpleName () );
            sbuf.append ( "." );

            enclosing_class = enclosing_class.getEnclosingClass ();
        }

        final Class<?> declaring_class = main_class.getDeclaringClass ();
        if ( declaring_class != null )
        {
            sbuf.append ( declaring_class.getSimpleName () );
            sbuf.append ( "$" );
        }

        sbuf.append ( main_class.getSimpleName () );

        return sbuf.toString ();
    }


    /**
     * @return A nice simple class name, such as "String"
     *         or "Tire" or "Vehicle.Tire" or "Vehicle.Tire$3"
     *         and so on, from the specified Object's class.
     *         Never null.
     */
    public static final String of (
                                   Object main_object
                                   )
    {
        if ( main_object == null )
        {
            return "<null.getClass ()>";
        }

        return ClassName.of ( main_object.getClass () );
    }
}
