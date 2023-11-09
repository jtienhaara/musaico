package musaico.foundation.wiring;

import java.io.Serializable;

import java.util.List;


import musaico.foundation.filter.Filter;
import musaico.foundation.filter.DiscardAll;

import musaico.foundation.filter.equality.EqualTo;

import musaico.foundation.filter.membership.MemberOf;


public class ExternalSelector
    extends AbstractTagged<Selector>
    implements Selector, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    private final Filter<Conductor> filter =
        new DiscardAll<Conductor> ();

    @Override
    public final Filter<Conductor> filter ()
    {
        
        return this.filter;
    }
}
