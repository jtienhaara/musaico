package musaico.foundation.structure;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * Turns non-Serializable objects into Serializable ones (such as
 * String representations) in java.
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
 * @see musaico.foundation.structure.MODULE#COPYRIGHT
 * @see musaico.foundation.structure.MODULE#LICENSE
 */
public class Serializables
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Do not let anyone instantiate this class.
    // It only has static methods.
    private Serializables ()
    {
    }


    /**
     * <p>
     * Makes an array of objects serializable.
     * </p>
     *
     * <p>
     * The default implementation casts each non-Serializable object to
     * a String, and leaves each Serializable object alone.
     * </p>
     *
     * @param objects The objects to make Serializable.
     *                Must not be null.  Can contain null elements.
     *
     * @return The serializable version of the array.  Never null.
     */
    public static Serializable [] makeArrayOfSerializables (
            Object [] objects
            )
    {
        final Serializable [] serializables =
            new Serializable [ objects.length ];
        for ( int s = 0; s < serializables.length; s ++ )
        {
            serializables [ s ] =
                Serializables.makeSerializable ( objects [ s ] );
        }

        return serializables;
    }


    /**
     * <p>
     * Makes an array of objects serializable.
     * </p>
     *
     * <p>
     * The default implementation casts each non-Serializable object to
     * a String, and leaves each Serializable object alone.
     * </p>
     *
     * @param objects The objects to make Serializable.
     *                Must not be null.  Can contain null elements.
     *
     * @return The serializable version of the iterable.  Never null.
     */
    public static Serializable [] makeArrayOfSerializables (
            Iterable<?> objects
            )
    {
        final List<Serializable> serializables_list =
            new ArrayList<Serializable> ();
        for ( Object object : objects )
        {
            final Serializable serializable =
                Serializables.makeSerializable ( object );
            serializables_list.add ( serializable );
        }

        final Serializable [] template =
            new Serializable [ serializables_list.size () ];
        final Serializable [] serializables =
            serializables_list.toArray ( template );

        return serializables;
    }


    /**
     * <p>
     * Makes an object serializable.
     * </p>
     *
     * <p>
     * The default implementation casts a non-Serializable object to
     * a String, and leaves a Serializable object alone.
     * </p>
     *
     * @param object The object to make Serializable.
     *               Can be null.
     *
     * @return The serializable version of the array.  Never null.
     */
    public static Serializable makeSerializable (
            Object object
            )
    {
        final Serializable serializable;
        if ( object == null )
        {
            serializable = "NULL";
        }
        else if ( object instanceof Object [] )
        {
            Object [] array = (Object []) object;
            serializable = makeArrayOfSerializables ( array );
        }
        else if ( object instanceof Iterable )
        {
            Iterable<?> iterable = (Iterable<?>) object;
            serializable = makeArrayOfSerializables ( iterable );
        }
        else if ( object instanceof Serializable )
        {
            serializable = (Serializable) object;
        }
        else
        {
            serializable =
                StringRepresentation.of ( object,
                                          StringRepresentation.DEFAULT_OBJECT_LENGTH );
        }

        return serializable;
    }
}
