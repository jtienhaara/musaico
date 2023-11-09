package musaico.foundation.filter.stream;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;


import musaico.foundation.filter.FilterState;

import musaico.foundation.filter.membership.InstanceOf;

import musaico.foundation.structure.ClassName;
import musaico.foundation.structure.StringRepresentation;


/**
 * <p>
 * Discards each container unless it contains instances of all the
 * classes in a specific set.
 * </p>
 *
 * <p>
 * An empty container is kept.
 * </p>
 *
 *
 * <p>
 * In Java, every FilterStream must be Serializable in order to play
 * nicely over RMI.
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
 * @see musaico.foundation.filter.stream.MODULE#COPYRIGHT
 * @see musaico.foundation.filter.stream.MODULE#LICENSE
 */
public class KeptAllClasses<ELEMENT extends Object>
    implements FilterStream<ELEMENT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Lock critical sections on this object:
    private final Serializable lock = new String ( "lock" );

    // MUTABLE:
    // The lookup of classes, all of which must be kept when filtering
    // a container in order for the container to be kept.
    private final LinkedHashMap<Class<?>, Boolean> keptClasses;


    /**
     * <p>
     * Creates a new KeptAllClasses.
     * </p>
     *
     * @param instance_of_filter The InstanceOfFilter whose classes are to be kept.
     *                           The filter itself is not used.  Instead, its
     *                           <code> classesToKeep () </code> is extracted.
     *                           If null, then an empty set of classes will be used.
     *                           DO NOT PASS NULL!
     */
    public KeptAllClasses (
            InstanceOf<ELEMENT> instance_of_filter
            )
    {
        this ( instance_of_filter == null // classes_to_keep
                   ? null
                   : instance_of_filter.classesToKeep () );
    }


    /**
     * <p>
     * Creates a new KeptAllClasses.
     * </p>
     *
     * @param classes The classes to be kept.
     *                If null, then an empty set of classes will be used.
     *                DO NOT PASS NULL!
     */
    public KeptAllClasses (
            Collection<Class<?>> classes
            )
    {
        this.keptClasses = new LinkedHashMap<Class<?>, Boolean> ();
        if ( classes != null )
        {
            for ( Class<?> instance_class : classes )
            {
                this.keptClasses.put ( instance_class, Boolean.FALSE );
            }
        }
    }


    /**
     * <p>
     * Creates a new KeptAllClasses.
     * </p>
     *
     * @param classes The classes to be kept.
     *                If null, then an empty set of classes will be used.
     *                DO NOT PASS NULL!
     */
    public KeptAllClasses (
            Class<?> ... classes
            )
    {
        this.keptClasses = new LinkedHashMap<Class<?>, Boolean> ();
        if ( classes != null )
        {
            for ( Class<?> instance_class : classes )
            {
                this.keptClasses.put ( instance_class, Boolean.FALSE );
            }
        }
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
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
        else if ( this.getClass () != object.getClass () )
        {
            return false;
        }

        final KeptAllClasses<?> that = (KeptAllClasses<?>) object;
        final LinkedHashMap<Class<?>, Boolean> this_kept_classes;
        synchronized ( this.lock )
        {
            if ( this.keptClasses == null )
            {
                this_kept_classes = null;
            }
            else
            {
                this_kept_classes = new LinkedHashMap<Class<?>, Boolean> ( this.keptClasses );
            }
        }
        final LinkedHashMap<?, Boolean> that_kept_classes;
        synchronized ( that.lock )
        {
            if ( that.keptClasses == null )
            {
                that_kept_classes = null;
            }
            else
            {
                that_kept_classes = new LinkedHashMap<Object, Boolean> ( that.keptClasses );
            }
        }

        if ( this_kept_classes == null )
        {
            if ( that_kept_classes != null )
            {
                return false;
            }
        }
        else if ( that_kept_classes == null )
        {
            return false;
        }
        else if ( ! this_kept_classes.equals ( that_kept_classes ) )
        {
            return false;
        }

        return true;
    }


    /**
     * @see musaico.foundation.filter.stream.FilterStream#filtered(java.lang.Object, musaico.foundation.filter.FilterState)
     */
    @Override
    public final FilterState filtered (
            ELEMENT element,
            FilterState element_filter_state
            )
        throws NullPointerException
    {
        if ( element_filter_state == null
             || element_filter_state == FilterStream.CONTINUE
             || element_filter_state == FilterStream.END )
        {
            return FilterState.DISCARDED;
        }
        else if ( element_filter_state.isKept () )
        {
            synchronized ( this.lock )
            {
                for ( Class<?> instance_class : this.keptClasses.keySet () )
                {
                    if ( instance_class.isInstance ( element ) )
                    {
                        this.keptClasses.put ( instance_class, Boolean.TRUE );
                    }
                }
            }
        }

        return FilterStream.CONTINUE;
    }


    /**
     * @see musaico.foundation.filter.stream.FilterStream#filterEnd(musaico.foundation.filter.FilterState)
     */
    @Override
    public final FilterState filterEnd (
            FilterState container_filter_state
            )
        throws NullPointerException
    {
        if ( container_filter_state == null
             || container_filter_state == FilterStream.CONTINUE
             || container_filter_state == FilterStream.END )
        {
            return FilterState.DISCARDED;
        }
        else if ( ! container_filter_state.isKept () )
        {
            return container_filter_state;
        }

        synchronized ( this.lock )
        {
            for ( Class<?> instance_class : this.keptClasses.keySet () )
            {
                final boolean is_kept = this.keptClasses.get ( instance_class ).booleanValue ();
                if ( ! is_kept )
                {
                    return FilterState.DISCARDED;
                }
            }
        }

        return FilterState.KEPT;
    }


    /**
     * @see musaico.foundation.filter.stream.FilterStream#filterStart()
     */
    @Override
    public final FilterState filterStart ()
    {
        return FilterState.KEPT;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        final int classes_hashcode;
        synchronized ( this.lock )
        {
            classes_hashcode = this.keptClasses.hashCode ();
        }

        return ClassName.of ( this.getClass () ).hashCode ()
            * classes_hashcode;
    }


    /**
     * @return The lookup of classes whose instances were kept (true) / not kept (false)
     *         during filtering.  Never null.
     */
    public final LinkedHashMap<Class<?>, Boolean> keptClasses ()
    {
        synchronized ( this.lock )
        {
            return new LinkedHashMap<Class<?>, Boolean> ( this.keptClasses );
        }
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        final List<Class<?>> classes;
        synchronized ( this.lock )
        {
            classes = new ArrayList<Class<?>> ( this.keptClasses.keySet () );
        }

        final String classes_string =
            StringRepresentation.of ( classes,
                                      StringRepresentation.DEFAULT_ARRAY_LENGTH );

        return ClassName.of ( this.getClass () + " " + classes_string );
    }
}
