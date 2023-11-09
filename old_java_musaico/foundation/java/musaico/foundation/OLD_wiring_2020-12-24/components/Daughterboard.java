package musaico.foundation.wiring.components;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import musaico.foundation.structure.StringRepresentation;

import musaico.foundation.wiring.AbstractConductor;
import musaico.foundation.wiring.Board;
import musaico.foundation.wiring.Carrier;
import musaico.foundation.wiring.Conductor;
import musaico.foundation.wiring.Conductivity;
import musaico.foundation.wiring.Schematic;
import musaico.foundation.wiring.Selector;
import musaico.foundation.wiring.StandardSchematic;
import musaico.foundation.wiring.Tag;
import musaico.foundation.wiring.TagsSelector;

import musaico.foundation.wiring.tag.Name;
import musaico.foundation.wiring.tag.TerminalType;


public class Daughterboard
    extends AbstractConductor
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    private final Board motherboard;
    private final Board daughterboard;
    private final Selector motherSelector;
    private final Selector daughterSelector;

    public Daughterboard (
            Board motherboard,
            Board daughterboard
            )
    {
        if ( motherboard == null )
        {
            this.motherboard = new StandardSchematic ().build ();
        }
        else
        {
            this.motherboard = motherboard;
        }

        if ( daughterboard == null )
        {
            this.daughterboard = new StandardSchematic ().build ();
        }
        else
        {
            this.daughterboard = daughterboard;
        }

        this.motherSelector =
            new TagsSelector ( TerminalType.NAME,
                               TerminalType.InternalOrExternal.INTERNAL );
        this.motherSelector.tag ( new Name ( "daughterboard.mother" ) );
        this.daughterSelector =
            new TagsSelector ( TerminalType.NAME,
                               TerminalType.InternalOrExternal.EXTERNAL );
        this.daughterSelector.tag ( new Name ( "daughterboard.daughter" ) );

        final Schematic daughter_schematic =
            this.daughterboard.schematic ();
        daughter_schematic.add ( this );

        final Schematic mother_schematic =
            this.motherboard.schematic ();
        mother_schematic.add ( this );
        this.daughterboard.reconcile ();
        this.motherboard.reconcile ();
    }

    // @see musaico.foundation.wiring.Conductor#transition(musaico.foundation.wiring.Board,musaico.foundation.wiring.Conductivity, musaico.foundation.wiring.Conductivity)
    @Override
    protected final void transition (
            Board board,
            Conductivity old_state,
            Conductivity new_state
            )
    {
        if ( board == null
             || this.daughterboard.equals ( board ) )
        {
            return;
        }

        // Make sure the daughterboard enters the same state.
        switch ( new_state )
        {
        case OFF:
            this.daughterboard.off ();
            break;

        case STOPPED:
            final Conductivity daughterboard_state =
                this.daughterboard.state ();
            if ( daughterboard_state == Conductivity.OFF
                 || daughterboard_state == Conductivity.TURNING_OFF
                 || daughterboard_state == Conductivity.TURNING_ON )
            {
                this.daughterboard.on ();
            }
            else if ( daughterboard_state == Conductivity.STOPPING
                      || daughterboard_state == Conductivity.STARTING
                      || daughterboard_state == Conductivity.RUNNING )

            {
                this.daughterboard.stop ();
            }
            break;

        case RUNNING:
            this.daughterboard.start ();
            break;

        default:
            // Do nothing.
        }
    }

    /**
     * @see musaico.foundation.wiring.Conductor#pull(musaico.foundation.wiring.Board, musaico.foundation.wiring.Selector)
     */
    @Override
    public final Carrier [] pull ( Board source_board, Selector specific_source )
    {
        // The request is coming from either the motherboard
        // or the daughterboard.
        // Whichever board requests us to pull, we find out
        // the name(s) of the Terminal(s) making the request,
        // and lookup the corresponding named Terminal(s) on
        // the other board.
        // For example, if the "configuration" Terminal in
        // the daughterboard pulls, we find the corresponding
        // "configuration" Terminal among the motherboard
        // selected Conductors, and pull from that Terminal.
        // Or if the "health" Terminal in the motherboard
        // pulls, we find the corresponding "health" Terminal
        // among the daughtboard selected Conductors, and
        // pull from that Terminal.
        final Selector source_selector;
        final Board target_board;
        final Selector target_selector;
        if ( source_board == null
             || specific_source == null )
        {
            return new Carrier [ 0 ];
        }
        else if ( source_board == this.motherboard )
        {
            // The motherboard asks us to pull from the daughterboard.
            source_selector = this.motherSelector;
            target_board = this.daughterboard;
            target_selector = this.daughterSelector;
        }
        else if ( source_board == this.daughterboard )
        {
            // The daughterboard asks us to pull from the motherboard.
            source_selector = this.daughterSelector;
            target_board = this.motherboard;
            target_selector = this.motherSelector;
        }
        else
        {
            return new Carrier [ 0 ];
        }

        // Look up:
        // 1) the Conductor(s) requesting the pull
        // 2) the name of each Conductor from the source board
        // 3) the Conductor(s) matching the internal / external selector above from the other board
        // 4) the Conductor(s) with the same name(s) as the source terminals.

        final Conductor [] source_conductors =
            source_board.selectorOwners ( specific_source );

        if ( source_conductors == null
             || source_conductors.length == 0 )
        {
            return new Carrier [ 0 ];
        }

        final Map<Tag, List<Conductor>> target_map =
            new HashMap<Tag, List<Conductor>> ();
        for ( Conductor target_conductor : target_board.select ( target_selector ) )
        {
            final Tag [] name_tags = target_conductor.tags ( Name.NAME );
            if ( name_tags == null
                 || name_tags.length == 0 )
            {
                List<Conductor> unnamed_conductors =
                    target_map.get ( Tag.NONE );
                if ( unnamed_conductors == null )
                {
                    unnamed_conductors = new ArrayList<Conductor> ();
                    target_map.put ( Tag.NONE, unnamed_conductors );
                }

                unnamed_conductors.add ( target_conductor );
                continue;
            }

            for ( Tag name_tag : name_tags )
            {
                List<Conductor> named_conductors =
                    target_map.get ( name_tag );
                if ( named_conductors == null )
                {
                    named_conductors = new ArrayList<Conductor> ();
                    target_map.put ( name_tag, named_conductors );
                }

                named_conductors.add ( target_conductor );
            }
        }

        final LinkedHashMap<Conductor, List<Conductor>> source_target_map =
            new LinkedHashMap<Conductor, List<Conductor>> ();
        for ( Conductor source_conductor : source_conductors )
        {
            final Tag [] name_tags = source_conductor.tags ( Name.NAME );
            if ( name_tags == null
                 || name_tags.length == 0 )
            {
                final List<Conductor> unnamed_conductors =
                    target_map.get ( Tag.NONE );
                if ( unnamed_conductors != null )
                {
                    source_target_map.put ( source_conductor, unnamed_conductors );
                }
                continue;
            }

            for ( Tag name_tag : name_tags )
            {
                final List<Conductor> named_conductors =
                    target_map.get ( name_tag );
                if ( named_conductors != null )
                {
                    source_target_map.put ( source_conductor, named_conductors );
                }
            }
        }

        final List<Carrier []> pulled_list =
            new ArrayList<Carrier []> ();
        int total_length = 0;
        for ( Conductor source_conductor : source_target_map.keySet () )
        {
            final List<Conductor> target_conductors =
                source_target_map.get ( source_conductor );
            for ( Conductor target_conductor : target_conductors )
            {
                if ( target_conductor == null )
                {
                    continue;
                }

                final Carrier [] pulled_from_one =
                    target_conductor.pull ( target_board, target_selector );
            
                if ( pulled_from_one == null
                     || pulled_from_one.length == 0 )
                {
                    continue;
                }

                total_length += pulled_from_one.length;

                pulled_list.add ( pulled_from_one );
            }
        }

        if ( pulled_list.size () == 0 )
        {
            return new Carrier [ 0 ];
        }

        final Carrier [] pulled;
        if ( pulled_list.size () == 1 )
        {
            pulled = pulled_list.get ( 0 );
        }
        else
        {
            pulled = new Carrier [ total_length ];
            int offset = 0;
            for ( Carrier [] some_pulled : pulled_list )
            {
                System.arraycopy ( some_pulled, 0,
                                   pulled, offset, some_pulled.length );
                offset += some_pulled.length;
            }
        }

        return pulled;
    }

    // @see musaico.foundation.wiring.Conductor#push(musaico.foundation.wiring.Board, musaico.foundation.wiring.Selector, musaico.foundation.Carrier[])
    @Override
    public final void push ( Board source_board, Selector specific_source, Carrier ... data )
    {
        // The request is coming from either the motherboard
        // or the daughterboard.
        // Whichever board pushes to us, we find out
        // the name(s) of the Terminal(s) making the request,
        // and lookup the corresponding named Terminal(s) on
        // the other board.
        // For example, if the "configuration" Terminal in
        // the motherboard pushes data, we find the corresponding
        // "configuration" Terminal among the daughterboard
        // selected Conductors, and push to that Terminal.
        // Or if the "log" Terminal in the daughterboard
        // pushes, we find the corresponding "log" Terminal
        // among the motherboard selected Conductors, and
        // push to that Terminal.
        final Selector source_selector;
        final Board target_board;
        final Selector target_selector;
        System.out.println ( "!!! Daughterboard.java push 1 " + source_board + " " + specific_source + " : " + data.length + " " + ( data.length == 1 ? data [ 0 ] : data ) );
        if ( source_board == null
             || specific_source == null
             || data == null
             || data.length == 0 )
        {
        System.out.println ( "!!! Daughterboard.java push 2" );
            return;
        }
        else if ( source_board == this.motherboard )
        {
        System.out.println ( "!!! Daughterboard.java push 3" );
            // The motherboard asks us to push to the daughterboard.
            source_selector = this.motherSelector;
            target_board = this.daughterboard;
            target_selector = this.daughterSelector;
        }
        else if ( source_board == this.daughterboard )
        {
        System.out.println ( "!!! Daughterboard.java push 4" );
            // The daughterboard asks us to push to the motherboard.
            source_selector = this.daughterSelector;
            target_board = this.motherboard;
            target_selector = this.motherSelector;
        }
        else
        {
        System.out.println ( "!!! Daughterboard.java push 5" );
            return;
        }

        System.out.println ( "!!! Daughterboard.java push 6" );
        // Look up:
        // 1) the Conductor(s) requesting the push
        // 2) the name of each Conductor from the source board
        // 3) the Conductor(s) matching the internal / external selector above from the other board
        // 4) the Conductor(s) with the same name(s) as the source terminals.

        final Conductor [] source_conductors =
            source_board.selectorOwners ( specific_source );

        System.out.println ( "!!! Daughterboard.java push 7" );
        if ( source_conductors == null
             || source_conductors.length == 0 )
        {
        System.out.println ( "!!! Daughterboard.java push 8" );
            return;
        }

        System.out.println ( "!!! Daughterboard.java push 9" );
        final Map<Tag, List<Conductor>> target_map =
            new HashMap<Tag, List<Conductor>> ();
        System.out.println ( "!!! Daughterboard.java push 10" );
        for ( Conductor target_conductor : target_board.select ( target_selector ) )
        {
        System.out.println ( "!!! Daughterboard.java push 11" );
            final Tag [] name_tags = target_conductor.tags ( Name.NAME );
        System.out.println ( "!!! Daughterboard.java push 12" );
            if ( name_tags == null
                 || name_tags.length == 0 )
            {
        System.out.println ( "!!! Daughterboard.java push 13" );
                List<Conductor> unnamed_conductors =
                    target_map.get ( Tag.NONE );
        System.out.println ( "!!! Daughterboard.java push 14" );
                if ( unnamed_conductors == null )
                {
        System.out.println ( "!!! Daughterboard.java push 15" );
                    unnamed_conductors = new ArrayList<Conductor> ();
                    target_map.put ( Tag.NONE, unnamed_conductors );
                }

                unnamed_conductors.add ( target_conductor );
        System.out.println ( "!!! Daughterboard.java push 16" );
                continue;
            }

        System.out.println ( "!!! Daughterboard.java push 17" );
            for ( Tag name_tag : name_tags )
            {
        System.out.println ( "!!! Daughterboard.java push 18" );
                List<Conductor> named_conductors =
                    target_map.get ( name_tag );
                System.out.println ( "!!! Daughterboard.java push 19 " + name_tag + " : " + named_conductors );
                if ( named_conductors == null )
                {
        System.out.println ( "!!! Daughterboard.java push 20" );
                    named_conductors = new ArrayList<Conductor> ();
                    target_map.put ( name_tag, named_conductors );
                }

        System.out.println ( "!!! Daughterboard.java push 21 " + target_conductor );
                named_conductors.add ( target_conductor );
        System.out.println ( "!!! Daughterboard.java push 22" );
            }
        }

        final LinkedHashMap<Conductor, List<Conductor>> source_target_map =
            new LinkedHashMap<Conductor, List<Conductor>> ();
        System.out.println ( "!!! Daughterboard.java push 23" );
        for ( Conductor source_conductor : source_conductors )
        {
            System.out.println ( "!!! Daughterboard.java push 24 source conductor = " + source_conductor );
            final Tag [] name_tags = source_conductor.tags ( Name.NAME );
            if ( name_tags == null
                 || name_tags.length == 0 )
            {
        System.out.println ( "!!! Daughterboard.java push 25" );
                final List<Conductor> unnamed_conductors =
                    target_map.get ( Tag.NONE );
                if ( unnamed_conductors != null )
                {
        System.out.println ( "!!! Daughterboard.java push 26" );
                    source_target_map.put ( source_conductor, unnamed_conductors );
                }
        System.out.println ( "!!! Daughterboard.java push 27" );
                continue;
            }

        System.out.println ( "!!! Daughterboard.java push 28" );
            for ( Tag name_tag : name_tags )
            {
                final List<Conductor> named_conductors =
                    target_map.get ( name_tag );
        System.out.println ( "!!! Daughterboard.java push 29 " + name_tag + " : " + named_conductors );
                if ( named_conductors != null )
                {
        System.out.println ( "!!! Daughterboard.java push 30" );
                    source_target_map.put ( source_conductor, named_conductors );
                }
            }
        }

        System.out.println ( "!!! Daughterboard.java push 31" );
        for ( Conductor source_conductor : source_target_map.keySet () )
        {
        System.out.println ( "!!! Daughterboard.java push 32" );
            final List<Conductor> target_conductors =
                source_target_map.get ( source_conductor );
        System.out.println ( "!!! Daughterboard.java push 33" );
            for ( Conductor target_conductor : target_conductors )
            {
        System.out.println ( "!!! Daughterboard.java push 34" );
                if ( target_conductor == null )
                {
        System.out.println ( "!!! Daughterboard.java push 35" );
                    continue;
                }

        System.out.println ( "!!! Daughterboard.java push 36" );
                target_conductor.push ( target_board, target_selector, data );
        System.out.println ( "!!! Daughterboard.java push 37" );
            }
        }
    }

    /**
     * @see musaico.foundation.wiring.Conductor#selectors(musaico.foundation.wiring.Board)
     */
    @Override
    public final Selector [] selectors (
            Board board
            )
    {
        if ( board == null )
        {
            return new Selector [ 0 ];
        }
        else if ( board == this.daughterboard )
        {
            return new Selector [] { this.daughterSelector };
        }
        else if ( board == this.motherboard )
        {
            return new Selector [] { this.motherSelector };
        }
        else
        {
            return new Selector [ 0 ];
        }
    }
}
