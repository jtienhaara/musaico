package musaico.foundation.filter.stream;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;


import musaico.foundation.filter.FilterState;

import musaico.foundation.structure.ClassName;


/**
 * <p>
 * A FilterStream that collects all of the KEPT elements, all of the
 * DISCARDED elements, or both, as the elements are being filtered.
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
public class FilteredCollection<ELEMENT>
    implements FilterStream<ELEMENT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // The kept elements, or null.
    private final Collection<ELEMENT> keptElementsOrNull;

    // The discarded elements, or null.
    private final Collection<ELEMENT> discardedElementsOrNull;


    /**
     * <p>
     * Creates a new FilteredCollection to store both the kept
     * elements and the discarded elements, each in its own
     * List.
     * </p>
     */
    public FilteredCollection ()
    {
        this ( new ArrayList<ELEMENT> (),   // kept_elements_or_null
               new ArrayList<ELEMENT> () ); // discarded_elements_or_null
    }


    /**
     * <p>
     * Creates a new FilteredCollection with the specified Collections
     * of kept and discarded elements.
     * </p>
     *
     * @param kept_elements_or_null The Collection to which kept elements
     *                              will be added, or null to not store
     *                              kept elements.  Can be null.
     *
     * @param discarded_elements_or_null The Collection to which discarded
     *                                   elements will be added, or null
     *                                   to not store discarded elements.
     *                                   Can be null.
     */
    public FilteredCollection (
            Collection<ELEMENT> kept_elements_or_null,
            Collection<ELEMENT> discarded_elements_or_null
            )
    {
        this.keptElementsOrNull = kept_elements_or_null;
        this.discardedElementsOrNull = discarded_elements_or_null;
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

        final FilteredCollection<?> that = (FilteredCollection<?>) object;
        try
        {
            if ( this.keptElementsOrNull ==  null )
            {
                if ( that.keptElementsOrNull != null )
                {
                    return false;
                }
            }
            else if ( that.keptElementsOrNull == null )
            {
                return false;
            }
            else if ( ! this.keptElementsOrNull.equals ( that.keptElementsOrNull ) )
            {
                return false;
            }

            if ( this.discardedElementsOrNull ==  null )
            {
                if ( that.discardedElementsOrNull != null )
                {
                    return false;
                }
            }
            else if ( that.discardedElementsOrNull == null )
            {
                return false;
            }
            else if ( ! this.discardedElementsOrNull.equals ( that.discardedElementsOrNull ) )
            {
                return false;
            }
        }
        catch ( ConcurrentModificationException e )
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
            FilterState filter_state
            )
    {
        if ( filter_state == null
             || ! filter_state.isKept ()
             || filter_state == FilterStream.CONTINUE
             || filter_state == FilterStream.END )
        {
            // Add to the discarded Collection.
            if ( this.discardedElementsOrNull != null )
            {
                this.discardedElementsOrNull.add ( element );
            }
        }
        else
        {
            // Add to the kept Collection.
            if ( this.keptElementsOrNull != null )
            {
                this.keptElementsOrNull.add ( element );
            }
        }

        return FilterStream.CONTINUE;
    }


    /**
     * @see musaico.foundation.filter.stream.FilterStream#filterEnd(musaico.foundation.filter.FilterState)
     */
    @Override
    public final FilterState filterEnd (
            FilterState filter_state
            )
        throws NullPointerException
    {
        if ( filter_state == null
             || filter_state == FilterStream.CONTINUE
             || filter_state == FilterStream.END )
        {
            return FilterState.DISCARDED;
        }
        else
        {
            return filter_state;
        }
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
        int hash_code = ClassName.of ( this.getClass () ).hashCode ();
        if ( this.keptElementsOrNull != null )
        {
            hash_code += this.keptElementsOrNull.hashCode ();
        }
        if ( this.discardedElementsOrNull != null )
        {
            hash_code += this.discardedElementsOrNull.hashCode ();
        }

        return hash_code;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return ClassName.of ( this.getClass () );
    }
}
