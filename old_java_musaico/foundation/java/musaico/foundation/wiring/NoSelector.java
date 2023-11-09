package musaico.foundation.wiring;

import java.io.Serializable;

import musaico.foundation.filter.Filter;


public class NoSelector
    implements Selector, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    // Protected constructor.
    // Use Selector.NONE instead.
    protected NoSelector ()
    {
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
            Object object
            )
    {
        if ( object == this )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return 0;
    }


    /**
     * @see musaico.foundation.wiring.Selector#ends()
     */
    @Override
    @SuppressWarnings({"rawtypes", "unchecked"}) // Un-parameterized array, cast Filter [] -> Filter<C> [].
    public final Filter<Conductor> [] ends ()
    {
        // SuppressWarnings({"rawtypes", "unchecked"}) Un-parameterized array, cast Filter [] -> Filter<C> [].
        return (Filter<Conductor> [])
            new Filter [ 0 ];
    }

    /**
     * @see musaico.foundation.wiring.Selector#buildWires(musaico.foundation.wiring.Conductor[])
     */
    @Override
    public final Wire [] buildWires (
            Board board,
            Conductor ... conductors
            )
    {
        return new Wire [ 0 ];
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return "selector.none";
    }
}
