package musaico.foundation.wiring;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.obligations.EveryParameter;

import musaico.foundation.filter.Filter;


public class StandardBoard
    extends AbstractTagged<Board>
    implements Board, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    private static final Advocate classAdvocate =
        new Advocate ( StandardBoard.class );


    private final Schematic schematic;

    private final Serializable lock;
    private Conductivity state;
    private final List<Conductor> conductors;
    private final Map<Selector, Conductor []> selections;
    private final Map<Conductor, Selector []> selectedBys;
    private final Map<Selector, Conductor []> selectorOwners;
    private int version;

    public StandardBoard (
            Schematic schematic,
            Serializable schematic_lock
            )
        throws EveryParameter.MustNotBeNull.Violation
    {
        classAdvocate.check ( EveryParameter.MustNotBeNull.CONTRACT,
                              schematic, schematic_lock );

        this.schematic = schematic;

        this.lock = schematic_lock;
        this.state = Conductivity.OFF;
        this.conductors = new ArrayList<Conductor> ();
        this.selections = new HashMap<Selector, Conductor []> ();
        this.selectedBys = new HashMap<Conductor, Selector []> ();
        this.selectorOwners = new HashMap<Selector, Conductor []> ();
        this.version = this.schematic.version () - 1;

        this.reconcile ();
    }

    @Override
    public final Schematic schematic ()
    {
        return this.schematic;
    }

    @Override
    public final void reconcile ()
        throws IllegalStateException
    {
        final int new_version;
        final int old_version;
        final List<Conductor> reconciled_conductors =
            new ArrayList<Conductor> ();

        synchronized ( this.lock )
        {
            new_version = this.schematic.version ();
            old_version = this.version;
            if ( new_version == old_version )
            {
                return;
            }

            for ( Conductor conductor : this.schematic.conductors () )
            {
                reconciled_conductors.add ( conductor );
            }
        }

        final Map<Selector, Conductor []> reconciled_selections =
            new HashMap<Selector, Conductor []> ();
        this.reconcileSelections ( reconciled_conductors,
                                   reconciled_selections ); // throws IllegalStateException

        synchronized ( this.lock )
        {
            if ( this.version != old_version )
            {
                throw new IllegalStateException ( "ERROR Cannot reconcile version " + old_version + " to " + new_version
                                                  + " of " + this + " :"
                                                  + " already reconciled to version "
                                                  + this.version );
            }

            this.conductors.clear ();
            this.conductors.addAll ( reconciled_conductors );
            this.selections.clear ();
            this.selections.putAll ( reconciled_selections );

            Map<Conductor, LinkedHashSet<Selector>> selected_bys_map =
                new HashMap<Conductor, LinkedHashSet<Selector>> ();
            Map<Selector, LinkedHashSet<Conductor>> selector_owners_map =
                new HashMap<Selector, LinkedHashSet<Conductor>> ();
            for ( Conductor selector_owner : this.conductors )
            {
                final Selector [] selectors = selector_owner.selectors ( this );
                for ( Selector selector : selectors )
                {
                    final Conductor [] selection = this.selections.get ( selector );
                    if ( selection == null )
                    {
                        // Most likely another Board owns the other end of the Selector.
                        continue;
                    }

                    for ( Conductor selected : selection )
                    {
                        LinkedHashSet<Selector> selected_bys =
                            selected_bys_map.get ( selected );
                        if ( selected_bys == null )
                        {
                            selected_bys = new LinkedHashSet<Selector> ();
                            selected_bys_map.put ( selected, selected_bys );
                        }

                        selected_bys.add ( selector );
                    }

                    LinkedHashSet<Conductor> selector_owners_set =
                        selector_owners_map.get ( selector );
                    if ( selector_owners_set == null )
                    {
                        selector_owners_set = new LinkedHashSet<Conductor> ();
                        selector_owners_map.put ( selector, selector_owners_set );
                    }

                    selector_owners_set.add ( selector_owner );
                }
            }
            this.selectedBys.clear ();
            for ( Conductor selected : this.conductors )
            {
                final LinkedHashSet<Selector> selected_bys_set =
                    selected_bys_map.get ( selected );
                final Selector [] selected_bys;
                if ( selected_bys_set == null )
                {
                    selected_bys = new Selector [ 0 ];
                }
                else
                {
                    final Selector [] template =
                        new Selector [ selected_bys_set.size () ];
                    selected_bys = selected_bys_set.toArray ( template );
                }

                this.selectedBys.put ( selected, selected_bys );
            }

            this.selectorOwners.clear ();
            for ( Selector selector : selector_owners_map.keySet () )
            {
                final LinkedHashSet<Conductor> selector_owners_set =
                    selector_owners_map.get ( selector );
                final Conductor [] selector_owners =
                    new Conductor [ selector_owners_set.size () ];
                int so = 0;
                for ( final Conductor selector_owner : selector_owners_set )
                {
                    selector_owners [ so ] = selector_owner;
                    so ++;
                }
                
                this.selectorOwners.put ( selector, selector_owners );
            }

            this.version = new_version;
        }
    }

    private final void reconcileSelections (
            List<Conductor> reconciled_conductors,
            Map<Selector, Conductor []> reconciled_selections
            )
        throws IllegalStateException
    {
        final LinkedHashSet<Conductor> unselected_conductors =
            new LinkedHashSet<Conductor> ();

        unselected_conductors.addAll ( reconciled_conductors );

        final LinkedHashMap<Selector, List<Conductor>> remaining_selectors =
            new LinkedHashMap<Selector, List<Conductor>> ();
        for ( Conductor conductor : reconciled_conductors )
        {
            for ( Selector selector : conductor.selectors ( this ) )
            {
                List<Conductor> parents =
                    remaining_selectors.get ( selector );
                if ( parents == null )
                {
                    parents = new ArrayList<Conductor> ();
                    remaining_selectors.put ( selector,
                                              parents );
                }

                parents.add ( conductor );
            }
        }

        final LinkedHashMap<Selector, Integer> selector_dimensions =
            new LinkedHashMap<Selector, Integer> ();

        for ( int dimension = 0; dimension <= 32768; dimension ++ )
        {
            if ( dimension >= 32768 )
            {
                throw new IllegalStateException ( "Somehow entered infinite loop trying to reconcileSelections () for " + this );
            }

            final Set<Conductor> selected_conductors =
                new HashSet<Conductor> ();
            final Set<Selector> used_up_selectors =
                new HashSet<Selector> ();
            final LinkedHashSet<Conductor> new_conductors =
                new LinkedHashSet<Conductor> ();
            final Map<Wire, List<Wire>> wire_taps =
                new HashMap<Wire, List<Wire>> ();
            for ( Selector selector : remaining_selectors.keySet () )
            {
                final List<Conductor> selected_list = new ArrayList<Conductor> ();
                for ( Conductor conductor : unselected_conductors )
                {
                    if ( selector.filter ().filter ( conductor ).isKept () )
                    {
                        selected_list.add ( conductor );
                    }
                }

                if ( selected_list.isEmpty () )
                {
                    continue;
                }

                selected_conductors.addAll ( selected_list );
                used_up_selectors.add ( selector );

                selector_dimensions.put ( selector, dimension );

                // Now create a wire tying together any/all selected conductors.
                final List<Conductor> parents_list =
                    remaining_selectors.get ( selector );

                final Conductor [] parents_template =
                    new Conductor [ parents_list.size () ];
                final Conductor [] parents =
                    parents_list.toArray ( parents_template );

                final Conductor [] children_template =
                    new Conductor [ selected_list.size () ];
                final Conductor [] children =
                    selected_list.toArray ( children_template );

                final Selector parents_selector =
                    new FixedSelector ( parents );
                final Selector children_selector = selector;

                final Wire wire =
                    new Wire ( parents_selector,
                               children_selector );

                reconciled_selections.put ( parents_selector, parents );
                reconciled_selections.put ( children_selector, children );

                new_conductors.add ( wire );

                for ( Conductor child : children )
                {
                    if ( child instanceof Wire )
                    {
                        final Wire selected_wire =
                            (Wire) child;
                        List<Wire> selected_by =
                            wire_taps.get ( selected_wire );
                        if ( selected_by == null )
                        {
                            selected_by = new ArrayList<Wire> ();
                            wire_taps.put ( selected_wire, selected_by );
                        }

                        selected_by.add ( wire );
                    }
                }
            }

            final Map<Selector, List<Conductor []>> tapped_selections =
                new HashMap<Selector, List<Conductor []>> ();
            for ( Wire tapped_wire : wire_taps.keySet () )
            {
                // Break up the wire so that every parent
                // and every child connects to every grandparent,
                // instead of every child connecting directly
                // to every parent.
                final Selector parents_selector =
                    tapped_wire.parentsSelector ( this );
                final Selector children_selector =
                    tapped_wire.childrenSelector ( this );

                final Conductor [] parents =
                    reconciled_selections.get ( parents_selector );
                final Conductor [] children =
                    reconciled_selections.get ( children_selector );

                final Selector tap_parents_selector =
                    new FixedSelector ( parents );
                final Selector tap_children_selector =
                    new FixedSelector ( children );

                final List<Wire> taps =
                    wire_taps.get ( tapped_wire );

                final Conductor [] parent_taps =
                    new Conductor [ taps.size () ];
                final Conductor [] child_taps =
                    new Conductor [ taps.size () ];

                for ( int t = 0; t < taps.size (); t ++ )
                {
                    final Wire tap = taps.get ( t );

                    final Selector grandparents_selector =
                        tap.parentsSelector ( this );
                    final Selector tapped_wire_selector =
                        tap.childrenSelector ( this );

                    final Wire children_tap =
                        new Wire ( grandparents_selector,
                                   tap_children_selector );
                    final Wire parents_tap =
                        new Wire ( grandparents_selector,
                                   tap_parents_selector );

                    new_conductors.add ( parents_tap );
                    new_conductors.add ( children_tap );

                    new_conductors.remove ( tap );

                    final Conductor [] both_taps =
                        new Conductor [] { parents_tap, children_tap };
                    reconciled_selections.put ( tapped_wire_selector, both_taps );

                    parent_taps [ t ] = parents_tap;
                    child_taps [ t ] = children_tap;
                }

                List<Conductor []> tapped_parents_selection =
                    tapped_selections.get ( parents_selector );
                if ( tapped_parents_selection == null )
                {
                    tapped_parents_selection = new ArrayList<Conductor []> ();
                    tapped_selections.put ( parents_selector,
                                            tapped_parents_selection );
                }
                tapped_parents_selection.add ( parent_taps );

                List<Conductor []> tapped_children_selection =
                    tapped_selections.get ( children_selector );
                if ( tapped_children_selection == null )
                {
                    tapped_children_selection = new ArrayList<Conductor []> ();
                    tapped_selections.put ( children_selector,
                                            tapped_children_selection );
                }
                tapped_children_selection.add ( child_taps );

                reconciled_selections.put ( tap_parents_selector, parents );
                reconciled_selections.put ( tap_children_selector, children );

                new_conductors.remove ( tapped_wire );
                unselected_conductors.remove ( tapped_wire );
                reconciled_conductors.remove ( tapped_wire );
            }

            for ( Selector selector : tapped_selections.keySet () )
            {
                final List<Conductor []> selection_list =
                    tapped_selections.get ( selector );
                if ( selection_list.size () == 1 )
                {
                    final Conductor [] taps = selection_list.get ( 0 );
                    reconciled_selections.put ( selector, taps );
                    continue;
                }

                int total_length = 0;
                for ( Conductor [] taps : selection_list )
                {
                    total_length += taps.length;
                }
                final Conductor [] all_taps =
                    new Conductor [ total_length ];
                int offset = 0;
                for ( Conductor [] taps : selection_list )
                {
                    System.arraycopy ( taps, 0,
                                       all_taps, offset, taps.length );
                    offset += taps.length;
                }

                reconciled_selections.put ( selector, all_taps );
            }

            unselected_conductors.removeAll ( selected_conductors );
            remaining_selectors.keySet ().removeAll ( used_up_selectors );

            unselected_conductors.addAll ( new_conductors );
            reconciled_conductors.addAll ( new_conductors );

            if ( remaining_selectors.size () == 0
                 || new_conductors.size () == 0 )
            {
                break;
            }
        }

        if ( remaining_selectors.size () > 0 )
        {
            // !!! Probably do nothing...?
        }
    }

    @Override
    public final int version ()
    {
        synchronized ( this.lock )
        {
            return this.version;
        }
    }

    /**
     * @see musaico.foundation.wiring.Board#state)(
     */
    @Override
    public final Conductivity state ()
    {
        synchronized ( this.lock )
        {
            return this.state;
        }
    }

    @Override
    public final void on ()
    {
        final List<Conductor> conductors;
        synchronized ( this.lock )
        {
            conductors = new ArrayList<Conductor> ( this.conductors );

            if ( this.state == Conductivity.OFF )
            {
                this.state = Conductivity.STOPPED;
            }
        }

        for ( Conductor conductor : conductors )
        {
            conductor.on ( this );
        }
    }

    @Override
    public final void off ()
    {
        final List<Conductor> conductors;
        synchronized ( this.lock )
        {
            conductors = new ArrayList<Conductor> ( this.conductors );

            this.state = Conductivity.OFF;
        }

        for ( Conductor conductor : conductors )
        {
            conductor.off ( this );
        }
    }

    @Override
    public final void start ()
    {
        final List<Conductor> conductors;
        synchronized ( this.lock )
        {
            conductors = new ArrayList<Conductor> ( this.conductors );

            this.state = Conductivity.RUNNING;
        }

        for ( Conductor conductor : conductors )
        {
            conductor.start ( this );
        }
    }

    @Override
    public final void stop ()
    {
        final List<Conductor> conductors;
        synchronized ( this.lock )
        {
            conductors = new ArrayList<Conductor> ( this.conductors );

            if ( this.state == Conductivity.RUNNING )
            {
                this.state = Conductivity.STOPPED;
            }
            else if ( this.state == Conductivity.OFF )
            {
                return;
            }
        }

        for ( Conductor conductor : conductors )
        {
            conductor.stop ( this );
        }
    }

    /**
     * @see musaico.foundation.wiring.Board#conductors()
     */
    @Override
    public final Conductor [] conductors ()
    {
        final Conductor [] conductors;
        synchronized ( this.lock )
        {
            final Conductor [] template =
                new Conductor [ this.conductors.size () ];
            conductors = this.conductors.toArray ( template );
        }

        return conductors;
    }

    @Override
    public final Conductor [] select ( Selector selector )
    {
        if ( selector == null )
        {
            return new Conductor [ 0 ];
        }

        final Conductor [] selection;
        synchronized ( this.lock )
        {
            selection = this.selections.get ( selector );
        }

        if ( selection == null )
        {
            // Dynamic Selector, not cached.
            final List<Conductor> dynamic_selection_list =
                new ArrayList<Conductor> ();
            for ( Conductor conductor : this.conductors )
            {
                if ( conductor == null )
                {
                    continue;
                }

                if ( selector.filter ().filter ( conductor ).isKept () )
                {
                    dynamic_selection_list.add ( conductor );
                }
            }

            final Conductor [] template =
                new Conductor [ dynamic_selection_list.size () ];
            final Conductor [] dynamic_selection =
                dynamic_selection_list.toArray ( template );
            return dynamic_selection;
        }
        else if ( selection.length == 0 )
        {
            return new Conductor [ 0 ];
        }

        final Conductor [] defensive_duplicate =
            new Conductor [ selection.length ];
        System.arraycopy ( selection, 0,
                           defensive_duplicate, 0, selection.length );
        return defensive_duplicate;
    }

    @Override
    public final Selector [] selectedBy (
            Conductor selection
            )
    {
        final Selector [] selected_by;
        synchronized ( this.lock )
        {
            selected_by = this.selectedBys.get ( selection );
        }

        if ( selected_by == null
             || selected_by.length == 0 )
        {
            return new Selector [ 0 ];
        }

        final Selector [] defensive_duplicate =
            new Selector [ selected_by.length ];
        System.arraycopy ( selected_by, 0,
                           defensive_duplicate, 0, selected_by.length );
        return defensive_duplicate;
    }

    @Override
    public final Conductor [] selectorOwners ( Selector selector )
    {
        final Conductor [] selector_owners;
        synchronized ( this.lock )
        {
            selector_owners = this.selectorOwners.get ( selector );
        }

        if ( selector_owners == null
             || selector_owners.length == 0 )
        {
            return new Conductor [ 0 ];
        }

        final Conductor [] defensive_duplicate =
            new Conductor [ selector_owners.length ];
        System.arraycopy ( selector_owners, 0,
                           defensive_duplicate, 0, selector_owners.length );
        return defensive_duplicate;
    }

    @Override
    public final void updateCarrierTags ( Direction direction, Carrier ... carriers )
    {
        // !!! TODO
    }

    @Override
    public final void sortCarriers ( Carrier ... carriers )
    {
        // !!! TODO
    }
}
