package musaico.foundation.filter.composite;

import java.io.Serializable;


import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;


/**
 * <p>
 * Logically ORs together the filter results of one or
 * more child Filters, so that a given object is KEPT only if
 * one or more of the child filter results is KEPT.
 * </p>
 *
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
 * @see musaico.foundation.filter.composite.MODULE#COPYRIGHT
 * @see musaico.foundation.filter.composite.MODULE#LICENSE
 */
public class Or<GRAIN extends java.lang.Object>
    extends AbstractCompositeFilter<GRAIN>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new Or filter to combine all the specified
     * child filters.
     * </p>
     *
     * <p>
     * An object is DISCARDED (filtered out) unless AT LEAST ONE
     * of the child filters KEPT it.
     * </p>
     *
     * @param filters The child Filter(s).  If null, then no filtering
     *                will be done.  If any element is null, then that
     *                element will be removed from the filters.
     */
    @SuppressWarnings("unchecked") // Possible heap pollution generic varargs.
    public Or (
            Iterable<? extends Filter<GRAIN>> filters
            )
        throws NullPointerException
    {
        // Throws NullPointerException:
        super ( filters );
    }


    /**
     * <p>
     * Creates a new Or filter to combine all the specified
     * child filters.
     * </p>
     *
     * <p>
     * An object is DISCARDED (filtered out) unless AT LEAST ONE
     * of the child filters KEPT it.
     * </p>
     *
     * @param filters The child Filter(s).  If null, then no filtering
     *                will be done.  If any element is null, then that
     *                element will be removed from the filters.
     */
    @SuppressWarnings("unchecked") // Possible heap pollution generic varargs.
    public Or (
            Filter<GRAIN> ... filters
            )
        throws NullPointerException
    {
        // Throws NullPointerException:
        super ( filters );
    }


    /**
     * @see musaico.foundation.io.Filter#filter(java.lang.Object)
     */
    @Override
    public final FilterState filter (
            GRAIN grain
            )
        throws NullPointerException
    {
        if ( grain == null )
        {
            return FilterState.DISCARDED;
        }

        final Filter<GRAIN> [] filters = this.myFilters ();
        if ( filters.length == 0 )
        {
            return FilterState.KEPT;
        }

        for ( Filter<GRAIN> filter : filters )
        {
            if ( filter.filter ( grain ).isKept () )
            {
                return FilterState.KEPT;
            }
        }

        // No kept results.
        return FilterState.DISCARDED;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "( "
            + this.toStringComponents ( "||" )
            + " )";
    }
}
