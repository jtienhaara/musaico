package musaico.foundation.wiring;

import java.io.Serializable;

import java.util.ArrayList;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.UncheckedViolation;

import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;
import musaico.foundation.filter.KeepAll;

import musaico.foundation.filter.elements.AllElementsFilter;
import musaico.foundation.filter.elements.ElementsFilter;


/**
 * <p>
 * A guarantee that every Component in each wiring Board
 * can be reached from the Board's entry Component
 * and can reach the Board's exit Component.
 * </p>
 *
 *
 * <p>
 * In Java, every Contract must implement equals () and hashCode ().
 * </p>
 *
 * <p>
 * In Java, every Contract must be Serializable in order to play
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
 * @see musaico.foundation.wiring.MODULE#COPYRIGHT
 * @see musaico.foundation.wiring.MODULE#LICENSE
 */
public class BoardMustBeConnected
    implements Contract<Board, BoardMustBeConnected.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * A violation thrown because of a Board that is not fully wired.
     * </p>
     */
    public static class Violation
        extends UncheckedViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            BoardMustBeConnected.serialVersionUID;

        /**
         * <p>
         * Creates a new BoardMustBeConnected.Violation with the specified
         * details.
         * </p>
         *
         * @param contract The violated contract.  Must not be null.
         *
         * @param plaintiff The object whose contract was
         *                  violated.  Must not be null.
         *
         * @param evidence The wiring Board that violated the contract.
         *                 Must not be null.
         */
        public <VIOLATING_NODE extends Object>
            Violation (
                    BoardMustBeConnected contract,
                    Object plaintiff,
                    Board evidence
                    )
        {
            super ( contract,
                    "The wiring Board " // description
                    + evidence
                    + " contains disconnected node(s) and/or arc(s): "
                    + contract.filter ().discards (
                          evidence,                      // container
                          new ArrayList<Component> () ), // discards_or_null
                    plaintiff,
                    evidence );
        }
    }




    /** Ensures all Components in every wiring Board are connected. */
    public static final BoardMustBeConnected CONTRACT =
        new BoardMustBeConnected ();


    /**
     * @see musaico.foundation.contract.Contract#description()
     */
    @Override
    public String description ()
    {
        return "Each wiring Board's Components must all be connected"
            + " by at least one path from the entry Component, and by"
            + " at least one path to the exit Component.";
    }


    /**
     * <p>
     * Creates a filter that can be used to check the Components
     * of a Board for connectivity.
     * </p>
     */
    public final ElementsFilter<Component> filter ()
    {
        final ElementsFilter<Component> filter =
            new AllElementsFilter<Filter<Component>, Component> (
                new KeepAll<Component> (),                // qualifier
                new ConnectedComponentsFilterStream () ); // quantifier
        return filter;
    }


    /**
     * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
     */
    @Override
    public final FilterState filter (
            Board board
            )
        throws Return.NeverNull.Violation
    {
        if ( board == null )
        {
            return FilterState.DISCARDED;
        }

        final ElementsFilter<Component> connected_filter =
            this.filter ();
        final FilterState filter_state =
            connected_filter.filter ( board ); // Iterable
        return filter_state;
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     */
    @Override
    public BoardMustBeConnected.Violation violation (
            Object plaintiff,
            Board evidence
            )
    {
        return new BoardMustBeConnected.Violation ( this,
                                                    plaintiff,
                                                    evidence );
    }


    /**
     * <p>
     * Helper method.  Always passes this BoardMustBeConnected contract
     * as the first parameter to the full method, and sets the specified
     * initial cause (if any).
     * </p>
     *
     * @see musaico.foundation.wiring.BoardMustBeConnected#violation(musaico.foundation.contract.Contract, java.lang.Object, java.lang.Object)
     */
    public BoardMustBeConnected.Violation violation (
            Object plaintiff,
            Board evidence,
            Throwable cause
            )
    {
        final BoardMustBeConnected.Violation violation =
            this.violation ( plaintiff,
                             evidence );

        if ( cause != null )
        {
            violation.initCause ( cause );
        }

        return violation;
    }
}
