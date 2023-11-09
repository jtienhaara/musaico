package musaico.foundation.wiring;

import java.io.Serializable;


public class NoConductor
    extends AbstractConductor
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    @Override
    protected final void transition (
            Board board,
            Conductivity old_state,
            Conductivity new_state
            )
    {
        // Do nothing.
    }

    // @see musaico.foundation.wiring.Conductor#pull(musaico.foundation.wiring.Board, musaico.foundation.wiring.Selector)
    @Override
    public final Carrier [] pull ( Board board, Selector source )
    {
        return new Carrier [ 0 ];
    }

    // @see musaico.foundation.wiring.Conductor#push(musaico.foundation.wiring.Board, musaico.foundation.wiring.Selector, musaico.foundation.Carrier[])
    @Override
    public final void push ( Board board, Selector source, Carrier ... data )
    {
    }

    // @see musaico.foundation.wiring.Conductor#selectors(musaico.foundation.wiring.Board)
    @Override
    public final Selector [] selectors (
            Board board
            )
    {
        return new Selector [ 0 ];
    }
}
