package musaico.foundation.wiring;

import musaico.foundation.filter.Filter;

public interface Selector
    extends Tagged<Selector>
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    public static final Selector EXTERNAL =
        new ExternalSelector ();


    public abstract Filter<Conductor> filter ();
}
