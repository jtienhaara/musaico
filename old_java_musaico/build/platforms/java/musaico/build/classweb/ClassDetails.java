package musaico.build.classweb;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


/**
 * <p>
 * A list of class details for a specific class / interface.
 * </p>
 */
public class ClassDetails<DETAIL extends Object>
    implements Iterable<DETAIL>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // The class / interface from which these details derive.
    private final Class<?> classOrInterface;

    // The type of class detail, such as ClassDetail.NAME or
    // ClassDetail.STEREOTYPE or ClassDetail.MEMBERS and so on.
    private final ClassDetail<DETAIL> type;

    // The list of details of the type from the specific class / interface.
    private final List<DETAIL> details;


    /**
     * <p>
     * Creates a new ClassDetails of the specified type from the specified
     * class / interface containing the specified values.
     * </p>
     */
    public ClassDetails (
                         Class<?> class_or_interface,
                         ClassDetail<DETAIL> type,
                         List<DETAIL> details
                         )
    {
        this.classOrInterface = class_or_interface;
        this.type = type;
        this.details = new ArrayList<DETAIL> ( details );
    }


    /**
     * @return The class / interface from which these details derive.
     *         Never null.
     */
    public final Class<?> classOrInterface ()
    {
        return this.classOrInterface;
    }

    /**
     * @return The list of details of the type from the specific
     *         class / interface.  Never null.  Never contains
     *         any null elements.
     */
    public final List<DETAIL> details ()
    {
        return new ArrayList<DETAIL> ( this.details );
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public final boolean equals (
                                 Object object
                                 )
    {
        if ( object == this )
        {
            return true;
        }
        else if ( object == null )
        {
            return false;
        }
        else if ( ! this.getClass ().equals ( object.getClass () ) )
        {
            return false;
        }

        final ClassDetails<?> that = (ClassDetails<?>) object;

        if ( ! this.classOrInterface.equals ( that.classOrInterface ) )
        {
            return false;
        }
        else if ( ! this.type.equals ( that.type ) )
        {
            return false;
        }
        else if ( ! this.details.equals ( that.details ) )
        {
            return false;
        }

        return true;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public final int hashCode ()
    {
        return
            31 * this.classOrInterface.getName ().hashCode ()
            + 17 * this.type.hashCode ()
            + this.details.size ();
    }

    /**
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public final Iterator<DETAIL> iterator ()
    {
        return new Iterator<DETAIL> ()
            {
                private final Iterator<DETAIL> iterator = details.iterator ();

                /**
                 * @see java.util.Iterator#hasNext()
                 */
                @Override
                public final boolean hasNext ()
                {
                    return this.iterator.hasNext ();
                }

                /**
                 * @see java.util.Iterator#next()
                 */
                @Override
                public final DETAIL next ()
                {
                    return this.iterator.next ();
                }

                /**
                 * @see java.util.Iterator#remove()
                 */
                @Override
                public final void remove ()
                    throws UnsupportedOperationException
                {
                    throw new UnsupportedOperationException ( "ClassDetails Iterator"
                                                              + ".remove () not supported" );
                }
            };
    }

    /**
     * @return The type of class detail, such as ClassDetail.NAME or
     *         ClassDetail.STEREOTYPE or ClassDetail.MEMBERS and so on.
     *         Never null.
     */
    public final ClassDetail<DETAIL> type ()
    {
        return this.type;
    }
}
