package musaico.foundation.wiring.components;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import musaico.foundation.wiring.Board;
import musaico.foundation.wiring.Conductor;
import musaico.foundation.wiring.Schematic;
import musaico.foundation.wiring.Selector;
import musaico.foundation.wiring.Tap;
import musaico.foundation.wiring.Wire;


public class StandardBoard
    implements Board, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    private static final int INFINITE_LOOP = 4096;


    private final String id;
    private final Schematic schematic;
    private final int schematicVersion;
    private final Conductor [] conductors;
    private final Wire [] wires;
    private final Map<Conductor, Wire []> wiresFrom =
        new HashMap<Conductor, Wire []> ();
    private final Map<Board, Cable> daughterboardCables =
        new HashMap<Board, Cable> ();
    private final Board [] daughterboards;
    private final Board [] motherboards;
    private final int hashCode;


    public StandardBoard (
            String id,
            Schematic schematic,
            int schematic_version,
            Conductor [] conductors,
            Selector [] selectors,
            Schematic [] slots,
            Board ... motherboards
            )
    {
        int hash_code = 0;
        if ( id == null )
        {
            this.id = "NO_ID";
        }
        else
        {
            this.id = id;
            hash_code += this.id.hashCode ();
        }

        if ( schematic == null )
        {
            this.schematic = Schematic.NONE;
        }
        else
        {
            this.schematic = schematic;
            hash_code += this.schematic.hashCode ();
        }

        this.schematicVersion = schematic_version;
        hash_code += this.schematicVersion;

        if ( conductors == null )
        {
            this.conductors = new Conductor [ 0 ];
        }
        else
        {
            this.conductors = new Conductor [ conductors.length ];
            System.arraycopy ( conductors, 0,
                               this.conductors, 0, conductors.length );
            for ( int c = 0; c < this.conductors.length; c ++ )
            {
                if ( this.conductors [ c ] == null )
                {
                    this.conductors [ c ] = Conductor.NONE;
                }
                else
                {
                    hash_code += this.conductors [ c ].hashCode ();
                }
            }
        }

        if ( motherboards == null
             || motherboards.length == 0 )
        {
            this.motherboards = new Board [ 0 ];
        }
        else
        {
            this.motherboards = new Board [ motherboards.length ];
            System.arraycopy ( motherboards, 0,
                               this.motherboards, 0, motherboards.length );
            for ( int m = 0; m < this.motherboards.length; m ++ )
            {
                if ( this.motherboards [ m ] == null )
                {
                    this.motherboards [ m ] = Board.NONE;
                }
            }
        }

        // To be filled in during finishBuilding ():
        if ( slots == null )
        {
            this.daughterboards = new Board [ 0 ];
        }
        else
        {
            this.daughterboards = new Board [ slots.length ];

            for ( int d = 0; d < slots.length; d ++ )
            {
                if ( slots [ d ] != null )
                {
                    hash_code += slots [ d ].hashCode ();
                }
            }
        }

        this.wires = this.finishBuilding ( selectors, slots );

        final Map<Conductor, List<Wire>> temp_wires_from =
            new HashMap<Conductor, List<Wire>> ();
        for ( Wire wire : this.wires )
        {
            for ( Conductor end : wire.ends () )
            {
                List<Wire> wires_from_end =
                    temp_wires_from.get ( end );
                if ( wires_from_end == null )
                {
                    wires_from_end = new ArrayList<Wire> ();
                    temp_wires_from.put ( end, wires_from_end );
                }
                wires_from_end.add ( wire );
            }
        }

        for ( Conductor conductor : temp_wires_from.keySet () )
        {
            final List<Wire> wires_from_list =
                temp_wires_from.get ( conductor );
            final Wire [] template =
                new Wire [ wires_from_list.size () ];
            final Wire [] wires_from =
                wires_from_list.toArray ( template );
            this.wiresFrom.put ( conductor, wires_from );
        }

        this.hashCode = hash_code;
    }

    private final Wire [] finishBuilding (
            Selector [] selectors_array,
            Schematic [] slots
            )
    {
        final LinkedHashSet<Selector> selectors =
            new LinkedHashSet<Selector> ();
        if ( selectors_array != null )
        {
            for ( Selector selector : selectors_array )
            {
                if ( selector == null )
                {
                    continue;
                }

                selectors.add ( selector );
            }
        }

        if ( slots == null )
        {
            slots = new Schematic [ 0 ];
        }

        for ( int d = 0; d < slots.length; d ++ )
        {
            if ( slots [ d ] == null )
            {
                continue;
            }

            final Cable daughterboard_cable =
                new Cable ( this );
            slots [ d ].addConductors ( daughterboard_cable );
            final Board daughterboard =
                slots [ d ].build ( this );
            daughterboard_cable.connect ( daughterboard );
            this.daughterboards [ d ] = daughterboard;
            this.daughterboardCables.put ( daughterboard,
                                           daughterboard_cable );
        }

        final List<Wire []> wires_list =
            new ArrayList<Wire []> ();
        int num_wires = 0;
        boolean is_last_chance = false;
        for ( int il = 0;
              il < StandardBoard.INFINITE_LOOP && selectors.size () > 0;
              il ++ )
        {
            final int num_wires_before = num_wires;

            num_wires = this.buildWires ( selectors, wires_list, num_wires );

            if ( num_wires == num_wires_before )
            {
                if ( is_last_chance )
                {
                    break;
                }

                is_last_chance = true;
            }
        }

        num_wires = this.mergeTaps ( selectors, wires_list, num_wires );

        if ( wires_list.size () == 0 )
        {
            return new Wire [ 0 ];
        }
        else if ( wires_list.size () == 1 )
        {
            return wires_list.get ( 0 );
        }

        final Wire [] wires = new Wire [ num_wires ];
        int offset = 0;
        for ( Wire [] some_wires : wires_list )
        {
            System.arraycopy ( some_wires, 0,
                               wires, offset, some_wires.length );
            offset += some_wires.length;
        }

        return wires;
    }

    // @return num_wires (total of all List<Wire[]> wires that have been built so far this round).
    private int buildWires (
            LinkedHashSet<Selector> selectors,
            List<Wire []> wires_list,
            int num_wires
            )
    {
        final Conductor [] conductors =
            new Conductor [ this.conductors.length + num_wires ];
        System.arraycopy ( this.conductors, 0,
                           conductors, 0, this.conductors.length );
        int conductors_offset = this.conductors.length;
        for ( Wire [] some_wires : wires_list )
        {
            System.arraycopy ( some_wires, 0,
                               conductors, conductors_offset, some_wires.length );
            conductors_offset += some_wires.length;
        }

        for ( Selector selector : new LinkedHashSet<Selector> ( selectors ) )
        {
            final Wire [] wires =
                selector.buildWires ( this,         // board
                                      conductors ); // conductors
            if ( wires == null
                 || wires.length == 0 )
            {
                continue;
            }

            selectors.remove ( selector );

            wires_list.add ( wires );

            num_wires += wires.length;

            for ( Wire wire : wires )
            {
                if ( ! ( wire instanceof Tap ) )
                {
                    continue;
                }

                final Tap tap = (Tap) wire;

                num_wires = this.buildTap ( selectors, wires_list, num_wires, tap );
            }
        }

        return num_wires;
    }

    // @return num_wires (total of all List<Wire[]> wires that have been built so far this round).
    private final int buildTap (
            LinkedHashSet<Selector> selectors,
            List<Wire []> wires_list,
            int num_wires,
            Tap tap
            )
    {
        final Wire tapped_wire = tap.tappedWire ();
        if ( tapped_wire == null )
        {
            return num_wires;
        }

        num_wires = this.removeWire ( wires_list, num_wires, tapped_wire );

        final Wire [] tap_wires = tap.taps ();
        wires_list.add ( tap_wires );
        num_wires += tap_wires.length;

        return num_wires;
    }

    // @return num_wires (total of all List<Wire[]> wires that have been built so far this round).
    private int removeWire (
            List<Wire []> wires_list,
            int num_wires,
            Wire wire_to_remove
            )
    {
        for ( int wc = 0; wc < wires_list.size (); wc ++ )
        {
            Wire [] wires_chunk = wires_list.get ( wc );
            List<Integer> removed_indices = new ArrayList<Integer> ();
            for ( int w = 0; w < wires_chunk.length; w ++ )
            {
                if ( wire_to_remove.equals ( wires_chunk [ w ] ) )
                {
                    removed_indices.add ( w );
                }
            }

            if ( removed_indices.size () == 0 )
            {
                continue;
            }

            num_wires -= removed_indices.size ();

            final Wire [] new_chunk =
                new Wire [ wires_chunk.length - removed_indices.size () ];
            wires_list.remove ( wc );

            if ( new_chunk.length == 0 )
            {
                continue;
            }

            wires_list.add ( wc, new_chunk );

            int start_index = 0;
            int end_index = -1;
            int offset = 0;
            for ( int ri = 0; ri <= removed_indices.size (); ri ++ )
            {
                final int removed_index;
                if ( ri < removed_indices.size () )
                {
                    removed_index = removed_indices.get ( ri );
                    end_index = removed_index - 1;
                }
                else
                {
                    removed_index = -1;
                    end_index = wires_chunk.length - 1;
                }

                if ( end_index < start_index )
                {
                    start_index = removed_index + 1;
                    continue;
                }

                System.arraycopy ( wires_chunk, start_index,
                                   new_chunk, offset, end_index - start_index + 1 );

                offset += end_index - start_index + 1;
                start_index = end_index + 1;
            }
        }

        return num_wires;
    }

    // @return num_wires (total of all List<Wire[]> wires that have been built so far this round).
    private int mergeTaps (
            LinkedHashSet<Selector> selectors,
            List<Wire []> wires_list,
            int num_wires
            )
    {
        final LinkedHashMap<Wire, List<Tap>> taps = new LinkedHashMap<Wire, List<Tap>> ();
        for ( Wire [] wires_chunk : wires_list )
        {
            for ( Wire maybe_tap : wires_chunk )
            {
                if ( ! ( maybe_tap instanceof Tap ) )
                {
                    continue;
                }

                final Tap tap = (Tap) maybe_tap;
                final Wire tapped_wire = tap.tappedWire ();
                List<Tap> taps_by_tapped_wire = taps.get ( tapped_wire );
                if ( taps_by_tapped_wire == null )
                {
                    taps_by_tapped_wire = new ArrayList<Tap> ();
                    taps.put ( tapped_wire, taps_by_tapped_wire );
                }

                taps_by_tapped_wire.add ( tap );
            }
        }

        for ( Wire tapped_wire : taps.keySet () )
        {
            final List<Tap> taps_by_tapped_wire = taps.get ( tapped_wire );
            if ( taps_by_tapped_wire.size () <= 1 )
            {
                continue;
            }

            // !!! TODO This is a horrible hack.
            // !!! TODO What if the Tap classes are different?
            final List<Conductor []> tapper_chunks = new ArrayList<Conductor []> ();
            int num_tappers = 0;
            for ( Tap tap : taps_by_tapped_wire )
            {
                final Conductor [] tappers = tap.tappers ();
                tapper_chunks.add ( tappers );
                num_tappers += tappers.length;
            }

            int offset = 0;
            final Conductor [] tappers = new Conductor [ num_tappers ];
            for ( Conductor [] tappers_chunk : tapper_chunks )
            {
                System.arraycopy ( tappers_chunk, 0,
                                   tappers, offset, tappers_chunk.length );
                offset += tappers_chunk.length;
            }

            final Tap replacement_tap =
                new StandardTap ( tapped_wire,
                                  tappers );

            for ( Tap tap : taps_by_tapped_wire )
            {
                num_wires = this.removeWire ( wires_list, num_wires, tap );
                for ( Wire tap_wire : tap.taps () )
                {
                    num_wires = this.removeWire ( wires_list, num_wires, tap_wire );
                }
            }

            wires_list.add ( new Wire [] { replacement_tap } );
            num_wires ++;

            num_wires = this.buildTap ( selectors,
                                        wires_list,
                                        num_wires,
                                        replacement_tap );
        }

        return num_wires;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
            Object object
            )
    {
        if ( object == this )
        {
            return true;
        }
        else
        {
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
     * @see musaico.foundation.wiring.Board#id()
     */
    public final String id ()
    {
        return this.id;
    }

    /**
     * @see musaico.foundation.wiring.Board#schematic()
     */
    @Override
    public final Schematic schematic ()
    {
        return this.schematic;
    }

    /**
     * @see musaico.foundation.wiring.Board#schematicVersion()
     */
    public final int schematicVersion ()
    {
        return this.schematicVersion;
    }

    /**
     * @see musaico.foundation.wiring.Board#conductors()
     */
    @Override
    public final Conductor [] conductors ()
    {
        final Conductor [] conductors =
            new Conductor [ this.conductors.length ];
        System.arraycopy ( this.conductors, 0,
                           conductors, 0, this.conductors.length );
        return conductors;
    }


    /**
     * @see musaico.foundation.wiring.Board#motherboards()
     */
    @Override
    public final Board [] motherboards ()
    {
        final Board [] motherboards =
            new Board [ this.motherboards.length ];
        System.arraycopy ( this.motherboards, 0,
                           motherboards, 0, this.motherboards.length );
        return motherboards;
    }

    /**
     * @see musaico.foundation.wiring.Board#daughterboards()
     */
    @Override
    public final Board [] daughterboards ()
    {
        final Board [] daughterboards =
            new Board [ this.daughterboards.length ];
        System.arraycopy ( this.daughterboards, 0,
                           daughterboards, 0, this.daughterboards.length );
        return daughterboards;
    }


    /**
     * @see musaico.foundation.wiring.Board#wires()
     */
    @Override
    public final Wire [] wires ()
    {
        final Wire [] wires =
            new Wire [ this.wires.length ];
        System.arraycopy ( this.wires, 0,
                           wires, 0, this.wires.length );
        return wires;
    }

    /**
     * @see musaico.foundation.wiring.Board#wiresFrom(musaico.foundation.wiring.Conductor)
     */
    @Override
    public final Wire [] wiresFrom ( Conductor end )
    {
        if ( end == null )
        {
            return new Wire [ 0 ];
        }

        final Wire [] wires_from = this.wiresFrom.get ( end );
        if ( wires_from == null
             || wires_from.length == 0 )
        {
            return new Wire [ 0 ];
        }

        final Wire [] defensive_copy =
            new Wire [ wires_from.length ];
        System.arraycopy ( wires_from, 0,
                           defensive_copy, 0, wires_from.length );
        return defensive_copy;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return "board [ " + this.id + " ]";
    }
}
