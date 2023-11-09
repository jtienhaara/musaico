package musaico.foundation.filter.composite;

import java.io.Serializable;


import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;


/**
 * <p>
 * Negates the effect of another Filter, so that every KEPT result
 * becomes DISCARDED and vice-versa.
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
public class Not<GRAIN extends Object>
    extends AbstractCompositeFilter<GRAIN>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new Not filter to negate the specified filter.
     * </p>
     *
     * <p>
     * An object is DISCARDED (filtered out) whenever the child
     * filter KEPTs it; and an object is KEPTed whenever the child
     * filter decides it is DISCARDED.
     * </p>
     *
     * @param filter The child Filter to negate.  If null, then
     *               no filtering at all will be done.
     */
    @SuppressWarnings({"rawtypes", // Java forces generic array creation.
                       "unchecked"}) // Java forces generic array creation.
    public Not (
            Filter<GRAIN> filter
            )
        throws NullPointerException
    {
        // Throws NullPointerException:
        super ( new Filter [] { filter } );
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
        if ( filters.length != 1 )
        {
            return FilterState.DISCARDED;
        }

        final FilterState state = filters [ 0 ].filter ( grain );
        final FilterState not_state = state.opposite ();
        return not_state;
    }


    /**
     * @return The Filter that is negated by this Filter.  Never null.
     */
    public final Filter<GRAIN> negatedFilter ()
    {
        final Filter<GRAIN> [] filters = this.myFilters ();
        final Filter<GRAIN> negated_filter = filters [ 0 ];
        return negated_filter;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "! ( "
            + this.toStringComponents ( ", " )
            + " )";
    }
}
