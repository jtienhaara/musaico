package musaico.foundation.filter;

import java.io.Serializable;


import musaico.foundation.structure.ClassName;


/**
 * <p>
 * Filters FilterState objects, keeping only FilterStates
 * that are kept.
 * </p>
 *
 *
 * <p>
 * KeptFilter is not generic because I don't see the point
 * in filtering only specific derivations of FilterState.
 * </p>
 *
 * <p>
 * In Java, every Filter must be Serializable in order to play
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
 * @see musaico.foundation.filter.MODULE#COPYRIGHT
 * @see musaico.foundation.filter.MODULE#LICENSE
 */
public class KeptFilter
    implements Filter<FilterState>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Singleton KeptFilter. */
    public static final KeptFilter FILTER = new KeptFilter ();


    // Creates a new KeptFilter.  Use the KeptFilter.FILTER
    // singleton instead.
    private KeptFilter ()
    {
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public final boolean equals (
            Object object
            )
    {
        if ( object == null )
        {
            return false;
        }
        else if ( object == this )
        {
            return true;
        }
        else if ( object.getClass () != this.getClass () )
        {
            return false;
        }

        // Everything is all matchy-matchy.
        return true;
    }


    /**
     * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
     */
    @Override
    public final FilterState filter (
            FilterState filter_state
            )
    {
        if ( filter_state == null )
        {
            return FilterState.DISCARDED;
        }
        else
        {
            return filter_state; // If kept, return kept.  If not kept, returned discarded.
        }
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return ClassName.of ( this.getClass () ).hashCode ();
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
