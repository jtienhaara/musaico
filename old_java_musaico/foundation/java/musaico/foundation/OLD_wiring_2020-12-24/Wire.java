package musaico.foundation.wiring;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import musaico.foundation.filter.Filter;

import musaico.foundation.structure.ClassName;


public class Wire
    extends AbstractConductor
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    private final Selector parentsSelector;
    private final Selector childrenSelector;

    public Wire (
            Selector parents_selector,
            Selector children_selector
            )
    {
        this.parentsSelector = parents_selector;
        this.childrenSelector = children_selector;
    }

    public final Selector childrenSelector (
            Board board
            )
    {
        return this.childrenSelector;
    }

    public final Conductor [] children (
            Board board
            )
    {
        return board.select ( this.parentsSelector );
    }

    public final Selector parentsSelector (
            Board board
            )
    {
        return this.parentsSelector;
    }

    public final Conductor [] parents (
            Board board
            )
    {
        return board.select ( this.childrenSelector );
    }

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

    /**
     * @see musaico.foundation.wiring.Conductor#pull(musaico.foundation.wiring.Board, musaico.foundation.wiring.Selector)
     */
    @Override
    public final Carrier [] pull ( Board board, Selector source )
    {
        if ( board == null
             || source == null )
        {
            return new Carrier [ 0 ];
        }

        final Selector pull_from_selector;
        final Selector [] pull_from_selectors;
        final Selector [] excluded_selectors;
        if ( source == this.parentsSelector )
        {
            // Pull from each child via the parent's children selector.
            pull_from_selectors = new Selector [] { this.childrenSelector };
            excluded_selectors = new Selector [] { this.parentsSelector };
        }
        else if ( source == this.childrenSelector )
        {
            // Pull from each parent via its own children selector.
            pull_from_selectors = new Selector [] { this.parentsSelector };
            excluded_selectors = new Selector [] { this.childrenSelector };
        }
        else
        {
            return new Carrier [ 0 ];
        }

        final Carrier [] pulled =
            this.pullHelper ( board,                  // board
                              excluded_selectors,     // excluded_selectors
                              pull_from_selectors );  // pull_from_selector

        return pulled;
    }

    /**
     * @see musaico.foundation.wiring.Conductor#push(musaico.foundation.wiring.Board, musaico.foundation.wiring.Selector, musaico.foundation.Carrier[])
     */
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

        final Selector [] push_to_selectors;
        final Selector [] excluded_selectors;
        if ( source == this.parentsSelector )
        {
            push_to_selectors = new Selector [] { this.childrenSelector };
            excluded_selectors = new Selector [] { this.parentsSelector };
        }
        else if ( source == this.childrenSelector )
        {
            push_to_selectors = new Selector [] { this.parentsSelector };
            excluded_selectors = new Selector [] { this.childrenSelector };
        }
        else
        {
            return;
        }

        this.pushHelper ( board,                // board
                          excluded_selectors,   // excluded_selectors
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
        return new Selector []
            {
                this.parentsSelector,
                this.childrenSelector
            };
    }

    /**
     * @see musaico.foundation.wiring.AbstractConductor#toString()
     */
    @Override
    public final String toString ()
    {
        return ClassName.of ( this.getClass () )
            + "[" + this.parentsSelector
            + "-" + this.childrenSelector
            + "]";
    }
}
