package musaico.foundation.wiring;

import musaico.foundation.filter.Filter;

public interface Selector
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    public static final Selector NONE = new NoSelector ();


    public abstract Filter<Conductor> [] ends ();

    public abstract Wire [] buildWires (
            Board board,
            Conductor ... conductors
            );
}
