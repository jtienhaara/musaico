package musaico.foundation.wiring.components;

import java.io.Serializable;

import musaico.foundation.wiring.AbstractConductor;
import musaico.foundation.wiring.Board;
import musaico.foundation.wiring.Carrier;
import musaico.foundation.wiring.Conductivity;
import musaico.foundation.wiring.Selector;


public class Terminal
    extends AbstractConductor
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * @see musaico.foundation.wiring.AbstractConductor#transition(musaico.foundation.wiring.Board, musaico.foundation.wiring.Conductivity, musaico.foundation.wiring.Conductivity)
     */
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
        if ( board == null
             || source == null )
        {
            return new Carrier [ 0 ];
        }

        final Selector [] pull_from_selectors =
            board.selectedBy ( this );
        final Selector [] exclude_selectors =
            new Selector [] { source };

        final Carrier [] pulled =
            this.pullHelper ( board,                  // board
                              exclude_selectors,      // exclude_selectors
                              pull_from_selectors );  // pull_from_selectors

        return pulled;
    }

    // @see musaico.foundation.wiring.Conductor#push(musaico.foundation.wiring.Board, musaico.foundation.wiring.Selector, musaico.foundation.Carrier[])
    @Override
    public final void push ( Board board, Selector source, Carrier ... data )
    {
        if ( board == null
             || source == null
             || data == null
             || data.length == 0 )
        {
            return;
        }

        final Selector [] push_to_selectors =
            board.selectedBy ( this );
        final Selector [] exclude_selectors =
            new Selector [] { source };

        this.pushHelper ( board,                // board
                          exclude_selectors,    // exclude_selectors
                          data,                 // data
                          push_to_selectors );  // push_to_selectors
    }

    /**
     * @see musaico.foundation.wiring.Conductor#selectors(musaico.foundation.wiring.Board)
     */
    @Override
    public final Selector [] selectors (
            Board board
            )
    {
        return new Selector [ 0 ];
    }
}
