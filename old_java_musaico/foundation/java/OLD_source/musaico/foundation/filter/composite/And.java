package musaico.foundation.filter.composite;

import java.io.Serializable;


import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;
import musaico.foundation.filter.AbstractCompositeFilter;


/**
 * <p>
 * Logically ANDs together the filter results of one or
 * more child Filters, so that a given object will be KEPT only if
 * all of the child filter results are KEPT.
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
public class And<GRAIN extends Object>
    extends AbstractCompositeFilter<GRAIN>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new And filter to combine all the specified
     * child filters.
     * </p>
     *
     * <p>
     * An object is DISCARDED (filtered out) unless ALL of the child
     * filters decide it will be KEPT.
     * </p>
     *
     * @param filters The child Filter(s).  If null, then no filtering
     *                will be done.  If any element is null, then that
     *                element will be removed from the filters.
     */
    @SuppressWarnings("unchecked") // Possible heap pollution generic varargs.
    public And (
                Iterable<? extends Filter<GRAIN>> filters
                )
        throws NullPointerException
    {
        // Throws NullPointerException:
        super ( filters );
    }


    /**
     * <p>
     * Creates a new And filter to combine all the specified
     * child filters.
     * </p>
     *
     * <p>
     * An object is DISCARDED (filtered out) unless ALL of the child
     * filters decide it will be KEPT.
     * </p>
     *
     * @param filters The child Filter(s).  If null, then no filtering
     *                will be done.  If any element is null, then that
     *                element will be removed from the filters.
     */
    @SuppressWarnings("unchecked") // Possible heap pollution generic varargs.
    public And (
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
            if ( ! filter.filter ( grain ).isKept () )
            {
                return FilterState.DISCARDED;
            }
        }

        return FilterState.KEPT;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        StringBuilder sbuf = new StringBuilder ();
        boolean is_first_filter = true;
        for ( Filter<GRAIN> filter : this.myFilters () )
        {
            if ( is_first_filter )
            {
                is_first_filter = false;
            }
            else
            {
                sbuf.append ( " && " );
            }

            sbuf.append ( "( " + filter + " )" );
        }

        return sbuf.toString ();
    }
}
