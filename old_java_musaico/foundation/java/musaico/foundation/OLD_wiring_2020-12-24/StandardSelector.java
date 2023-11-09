package musaico.foundation.wiring;

import java.io.Serializable;


import musaico.foundation.filter.DiscardAll;
import musaico.foundation.filter.Filter;

import musaico.foundation.structure.ClassName;


public class StandardSelector
    extends AbstractTagged<Selector>
    implements Selector, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    private final Filter<Conductor> filter;
    private final int hashCode;

    public StandardSelector (
            Filter<Conductor> filter
            )
    {
        if ( filter == null )
        {
            this.filter = new DiscardAll<Conductor> ();
        }
        else
        {
            this.filter = filter;
        }

        // We have to do a lot of hash codes when
        // looking up Selectors in Maps at runtime.
        // So it's important to pre-load the hash
        // code -- both for speed, and also to make
        // sure it never changes!
        this.hashCode = this.filter.hashCode ();
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
            Object object
            )
    {
        if ( this == object )
        {
            return true;
        }
        else
        {
            // We have to do a lot of equals ()
            // checks on Selectors.  And in any case
            // we don't want two Selectors that are
            // defined the same way to be considered
            // equal when looking them up in Maps.
            // So we only check for identity equality.
            return false;
        }
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return this.hashCode;
    }

    /**
     * @see musaico.foundation.wiring.Selector#filter()
     */
    @Override
    public final Filter<Conductor> filter ()
    {
        return this.filter;
    }
}
