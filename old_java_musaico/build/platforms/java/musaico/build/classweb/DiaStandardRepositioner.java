package musaico.build.classweb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * <p>
 * Given a lookup of DiaChunks, repositions everything to make a nice,
 * pretty diagram.
 * </p>
 *
 * <p>
 * Repositioning should occur before connecting classes for
 * generalizations, associations, and so on.
 * </p>
 */
public class DiaStandardRepositioner
    implements DiaRepositioner
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    public static interface Cell
    {
        public abstract int [] cell ();
    }


    public static class Sinker
        implements Cell, Comparable<Sinker>
    {
        // Immutable:
        private final Class<?> concept;
        private final DiaObject key;
        private final List<DiaChunk> chunks;

        // Typically only set once, by multiple calls to connect ( ... ),
        // during initialization:
        private final Set<Sinker> pulls = new HashSet<Sinker> ();
        private final Set<Sinker> weights = new HashSet<Sinker> ();

        // Mutable:
        private final int [] cell; // 3D vector.

        public Sinker (
                       Class<?> concept,
                       List<DiaChunk> chunks,
                       int [] initial_cell
                       )
        {
            this.concept = concept;
            this.chunks = new ArrayList<DiaChunk> ( chunks );
            this.cell = new int [ 3 ];
            this.cell [ 0 ] = initial_cell [ 0 ];
            this.cell [ 1 ] = initial_cell [ 1 ];
            this.cell [ 2 ] = initial_cell [ 2 ];

            DiaObject key = null;
            for ( DiaChunk chunk : this.chunks )
            {
                if ( chunk instanceof DiaObject )
                {
                    key = (DiaObject) chunk;
                    break;
                }
            }

            this.key = key;
        }


        public final void addPull (
                                   Sinker that
                                   )
        {
            this.pulls.add ( that );
        }

        public final void addWeight (
                                     Sinker that
                                     )
        {
            this.weights.add ( that );
        }

        @Override
        public final int [] cell ()
        {
            return this.cell;
        }

        public final void cell (
                                int [] new_cell
                                )
        {
            this.cell [ 0 ] = new_cell [ 0 ];
            this.cell [ 1 ] = new_cell [ 1 ];
            this.cell [ 2 ] = new_cell [ 2 ];
        }

        public final void cell (
                                int x,
                                int y,
                                int z
                                )
        {
            this.cell [ 0 ] = x;
            this.cell [ 1 ] = y;
            this.cell [ 2 ] = z;
        }

        public final List<DiaChunk> chunks ()
        {
            return new ArrayList<DiaChunk> ( this.chunks );
        }

        @Override
        public int compareTo (
                              Sinker that
                              )
        {
            // return the heaviest sinker first.
            final double this_weight = this.weight ();
            final double that_weight = that.weight ();
            final int diff = (int) that_weight - (int) this_weight;
            if ( diff != 0 )
            {
                return diff;
            }

            if ( that_weight < this_weight )
            {
                return -1;
            }
            else if ( that_weight > this_weight )
            {
                return 1;
            }
            else
            {
                return 0;
            }
        }

        public final Class<?> concept ()
        {
            return this.concept;
        }

        public double [] direction ()
        {
            final double weight = this.weight ();
            final double [] direction =
                new double [] { 0D, 0D, 0D - weight };

            for ( Sinker that : this.pulls )
            {
                if ( this.isWeight ( that ) )
                {
                    // Can't be a pull and a weight both at once.
                    continue;
                }

                int [] that_cell = that.cell ();
                double [] toward_that = new double [] {
                    (double) ( that_cell [ 0 ] - this.cell [ 0 ] ),
                    (double) ( that_cell [ 1 ] - this.cell [ 1 ] )
                };

                for ( int t = 0; t < 2; t ++ )
                {
                    if ( toward_that [ t ] >= 0D )
                    {
                        toward_that [ t ] -= 1D;
                    }
                    else
                    {
                        toward_that [ t ] += 1D;
                    }
                }

                direction [ 0 ] += toward_that [ 0 ];
                direction [ 1 ] += toward_that [ 1 ];

                final double [] that_direction = that.direction ();
                direction [ 0 ] += that_direction [ 0 ];
                direction [ 1 ] += that_direction [ 1 ];
            }

            final double horizontal_length =
                direction [ 0 ] + direction [ 1 ];
            if ( horizontal_length == 0D )
            {
                return new double [] { 0D, 0D, 0D - weight };
            }

            direction [ 0 ] /= horizontal_length;
            direction [ 1 ] /= horizontal_length;

            return direction;
        }

        public final boolean isWeight (
                                       Sinker that
                                       )
        {
            return this.weights.contains ( that );
        }

        public final DiaObject key ()
        {
            return this.key;
        }


        public double speed ()
        {
            return this.weight ();
        }

        @Override
        public String toString ()
        {
            final StringBuilder sbuf = new StringBuilder ();
            sbuf.append ( this.concept ().getSimpleName () + "\n" );
            sbuf.append ( "    weight =    " + this.weight () + "\n" );
            sbuf.append ( "    speed =     " + this.speed () + "\n" );
            final int [] cell = this.cell ();
            sbuf.append ( "    cell =      { "
                          + cell [ 0 ]
                          + ", "
                          + cell [ 1 ]
                          + ", "
                          + cell [ 2 ]
                          + " }"
                          + "\n" );
            final double [] direction = this.direction ();
            sbuf.append ( "    direction = { "
                          + direction [ 0 ]
                          + ", "
                          + direction [ 1 ]
                          + ", "
                          + direction [ 2 ]
                          + " }"
                          + "\n" );

            return sbuf.toString ();
        }

        private double weight ()
        {
            if ( this.cell [ 2 ] == 0 )
            {
                return 0D;
            }

            double weight = 1D;
            for ( Sinker that : this.weights )
            {
                if ( that.isWeight ( this ) )
                {
                    // These two Sinkers can't both pull each
                    // other, so cancel themselves out.
                    continue;
                }

                final double that_weight = that.weight ();
                weight += that_weight * 0.5D;
            }

            return weight;
        }
    }




    /**
     * @see musaico.build.classweb.DiaRepositioner#reposition(java.util.LinkedHashMap)
     */
    public final void reposition (
                                  LinkedHashMap<Object, List<DiaChunk>> dia_chunks
                                  )
    {
        final List<Object> concepts =
            new ArrayList<Object> ( dia_chunks.keySet () );

        // Create a cube of Cells.
        final int one_side = (int)
            Math.sqrt ( (double) concepts.size () )
            + 1;
        final Cell [] [] [] space = new Cell [ one_side ] [] [];
        for ( int space_x = 0; space_x < one_side; space_x ++ )
        {
            space [ space_x ] = new Cell [ one_side ] [];
            for ( int space_y = 0; space_y < one_side; space_y ++ )
            {
                space [ space_x ] [ space_y ] =
                    new Cell [ one_side * 128 ];
            }
        }

        // At the top of the cube, place all the concept
        // objects as "Sinkers", in an "X" formation.
        // Each one will sink, rotate and shift according
        // to various forces: its own weight, the number
        // of heavier objects pulling it down, the number
        // of objects piled on its back, and so on.
        int x = 0;
        int y = 0;
        int z = one_side * 128 - 1;
        final Map<DiaObject, Sinker> sinkers_map =
            new HashMap<DiaObject, Sinker> ();
        final Map<DiaLinear, String> lines = new HashMap<DiaLinear, String> ();
        for ( Object concept : concepts )
        {
            final List<DiaChunk> chunks = dia_chunks.get ( concept );
            for ( DiaChunk chunk : chunks )
            {
                if ( chunk instanceof DiaLinear )
                {
                    lines.put ( (DiaLinear) chunk, "" + concept );
                }
            }

            if ( ! ( concept instanceof Class ) )
            {
                // Skip the associations and generalizations and so on.
                // Only Classes are sinkers in our search.
                continue;
            }

            final Sinker sinker = new Sinker ( (Class<?>) concept,
                                               chunks,
                                               new int [] { x, y, z } );
            final DiaObject key = sinker.key ();
            if ( key == null )
            {
                // A Class generated some DiaChunks that are not
                // DiaObjects, for some reason.  Skip.
                continue;
            }

            sinkers_map.put ( key, sinker );
            space
                [ x ]
                [ y ]
                [ z ] = sinker;

            // Now figure out where the next Sinker will go.
            x ++;
            if ( x >= one_side )
            {
                x = 0;
                y ++;
            }
        }

        // Now figure out the weight of each Sinker.
        for ( DiaLinear line : lines.keySet () )
        {
            final String line_name = lines.get ( line );

            final List<Sinker> heavies = new ArrayList<Sinker> ();
            final List<Sinker> lights = new ArrayList<Sinker> ();
            boolean is_first = true;
            for ( DiaLinear.Connection connection : line.connections () )
            {
                final Sinker sinker = sinkers_map.get ( connection.to );
                if ( sinker == null )
                {
                    continue;
                }

                if ( is_first )
                {
                    heavies.add ( sinker );
                    is_first = false;
                }
                else
                {
                    // Not the first Sinker in the line connections.
                    lights.add ( sinker );
                }
            }

            for ( Sinker top : lights )
            {
                for ( Sinker bottom : heavies )
                {
                    if ( top != bottom )
                    {
                        bottom.addWeight ( top );
                        top.addPull ( bottom );
                    }
                }
            }
        }

        final List<Sinker> sorted =
            new ArrayList<Sinker> ( sinkers_map.values () );
        Collections.sort ( sorted );

        this.sink ( space, sorted );

        // Now translate the cellular positions to places in the
        // diagram.
        this.diagramPositions ( sorted, dia_chunks );
    }


    public void sink (
                      Cell [] [] [] space,
                      List<Sinker> sinkers
                      )
    {
        final Map<Sinker, Integer> frozen = new HashMap<Sinker, Integer> ();
        for ( Sinker sinker : sinkers )
        {
            frozen.put ( sinker, 0 );
        }

        final int max_iterations = 10000;
        for ( int iteration = 0; iteration < max_iterations; iteration ++ )
        {
            this.sinkIteration ( space, sinkers, frozen );

            boolean is_all_sunk_to_bottom = true;
            for ( Sinker sinker : sinkers )
            {
                if ( sinker.cell () [ 2 ] != 0 )
                {
                    is_all_sunk_to_bottom = false;
                    break;
                }
            }

            if ( is_all_sunk_to_bottom )
            {
                break;
            }
        }
    }

    public void sinkIteration (
                               Cell [] [] [] space,
                               List<Sinker> sinkers,
                               Map<Sinker, Integer> frozen
                               )
    {
        for ( Sinker sinker : sinkers )
        {
            this.sinkIteration ( space, sinker, frozen );
        }
    }

    public void sinkIteration (
                               Cell [] [] [] space,
                               Sinker sinker,
                               Map<Sinker, Integer> frozen
                               )
    {
        final double speed = sinker.speed ();
        final double [] direction = sinker.direction ();
        final int [] cell = sinker.cell ();

        double sink_variation = (double)
            ( ( cell [ 0 ] * cell [ 1 ] ) % 3 )
            * -1D
            + 0.5;

        if ( cell [ 2 ] == 0 )
        {
            // Already at the bottom.
            return;
        }

        final double [] target = new double [ 3 ];
        target [ 0 ] = (double) cell [ 0 ] + direction [ 0 ] * speed;
        target [ 1 ] = (double) cell [ 1 ] + direction [ 1 ] * speed;
        target [ 2 ] = (double) cell [ 2 ] + direction [ 2 ] * speed
            + sink_variation;

        final int [] [] rounds = new int [ 3 ] [];
        int num_possible_new_cells = 1;
        for ( int dim = 0; dim < 3; dim ++ )
        {
            rounds [ dim ] = new int [ 2 ];
            final double unrounded = target [ dim ];
            final int rounded = (int) unrounded;
            rounds [ dim ] [ 0 ] = rounded;
            if ( unrounded < (double) rounded )
            {
                rounds [ dim ] [ 1 ] = rounded - 1;
                num_possible_new_cells *= 2;
            }
            else if ( unrounded > (double) rounded )
            {
                rounds [ dim ] [ 1 ] = rounded + 1;
                num_possible_new_cells *= 2;
            }
            else
            {
                rounds [ dim ] [ 1 ] = rounded;
            }
        }

        for ( int rx = 0; rx < 2; rx ++ )
        {
            for ( int ry = 0; ry < 2; ry ++ )
            {
                for ( int rz = 0; rz < 2; rz ++ )
                {
                    final int x =
                        this.clamp ( rounds [ 0 ] [ rx ], space.length );
                    final int y =
                        this.clamp ( rounds [ 1 ] [ ry ], space [ 0 ].length );
                    final int z =
                        this.clamp ( rounds [ 2 ] [ rz ], space [ 0 ] [ 0 ].length );

                    final Cell existing_cell = space [ x ] [ y ] [ z ];

                    if ( existing_cell == null )
                    {
                        sinker.cell ( x, y, z );

                        space [ cell [ 0 ] ] [ cell [ 1 ] ] [ cell [ 2 ] ] =
                            null;
                        space [ x ] [ y ] [ z ] = sinker;

                        frozen.put ( sinker, 0 );
                        return;
                    }
                }
            }
        }

        // Bounce this sinker, it couldn't find an empty cell.
        this.bounce ( space, sinker, frozen,
                      speed, direction, cell );
    }

    public void bounce (
                        Cell [] [] [] space,
                        Sinker sinker,
                        Map<Sinker, Integer> frozen,
                        double speed,
                        double [] direction,
                        int [] cell
                        )
    {
        final int how_long_frozen = frozen.get ( sinker );

        final double x_tweak;
        if ( ( how_long_frozen % 2 ) == 0 )
        {
            x_tweak = (double) how_long_frozen;
        }
        else
        {
            x_tweak = 0D - (double) how_long_frozen;
        }
        final double y_tweak = 0D - x_tweak;

        double [] bounce = new double [] {
            ( direction [ 0 ] + x_tweak ) * -1D * speed,
            ( direction [ 1 ] * -1D + y_tweak ) * speed,
            direction [ 2 ] * -1D * speed
        };
        double [] bounce_to = new double [] {
            (double) cell [ 0 ] + bounce [ 0 ],
            (double) cell [ 1 ] + bounce [ 1 ],
            (double) cell [ 2 ] + bounce [ 2 ]
        };

        int x = this.clamp ( (int) bounce_to [ 0 ], space.length );
        int y = this.clamp ( (int) bounce_to [ 1 ], space [ 0 ].length );
        int z = this.clamp ( (int) bounce_to [ 2 ], space [ 0 ] [ 0 ].length );

        final Cell existing_cell = space [ x ] [ y ] [ z ];

        if ( existing_cell == null )
        {
            sinker.cell ( x, y, z );
            space [ cell [ 0 ] ] [ cell [ 1 ] ] [ cell [ 2 ] ] = null;
            space [ x ] [ y ] [ z ] = sinker;
            // Don't remove from frozen just yet, if it was frozen
            // last iteration.  It might be bouncing then frozen
            // then bouncing then frozen and so on.  Leaving it
            // in frozen for a bit without incrementing the number
            // will reduce the chances of getting stuck permanently
            // ever so slightly.
            return;
        }

        // Otherwise we don't even have room to bounce, so we stay put.
        frozen.put ( sinker, how_long_frozen + 1 );
    }

    public int clamp ( int value, int length )
    {
        if ( value < 0 )
        {
            return 0;
        }
        else if ( value >= length )
        {
            return length - 1;
        }
        else
        {
            return value;
        }
    }


    public void diagramPositions (
                                  List<Sinker> sinkers,
                                  LinkedHashMap<Object, List<DiaChunk>> dia_chunks
                                  )
    {
        double [] jitter = new double [] { 2.0D, 1.0D };
        for ( Sinker sinker : sinkers )
        {
            final Class<?> concept = sinker.concept ();
            final int [] cell = sinker.cell ();
            final double x_offset;
            final double y_offset;
            if ( cell [ 2 ] == 0 )
            {
                // Sunk to the bottom.
                x_offset = 0D;
                y_offset = 0D;
            }
            else
            {
                // Rotated around some central object.
                final double radians = (double) cell [ 2 ] * Math.PI / 11D;
                x_offset = Math.cos ( radians ) * 1.0D;
                y_offset = Math.sin ( radians ) * 0.5D;
            }

            final double x =
                (double) cell [ 0 ];
            final double y =
                (double) cell [ 1 ];

            final double [] main_obj_new_position = new double [ 2 ];
            main_obj_new_position [ 0 ] = x * 10.0D + x_offset
                + Math.cos ( (double) cell [ 0 ] * Math.PI / 11D ) * jitter [ 0 ];
            main_obj_new_position [ 1 ] = y * 3.0D + y_offset
                + Math.sin ( (double) cell [ 1 ] * Math.PI / 11D ) * jitter [ 1 ];

            final DiaObject key = sinker.key ();
            final double [] main_obj_old_position = key.obj_pos ();
            final double [] translation = new double []
            {
                main_obj_new_position [ 0 ] - main_obj_old_position [ 0 ],
                main_obj_new_position [ 1 ] - main_obj_old_position [ 1 ]
            };

            for ( DiaChunk chunk : dia_chunks.get ( concept ) )
            {
                if ( chunk instanceof DiaObject )
                {
                    DiaObject dia_object = (DiaObject) chunk;

                    final double [] old_position =
                        dia_object.obj_pos ();
                    final double [] new_position =
                        new double [] {
                            old_position [ 0 ] + translation [ 0 ],
                            old_position [ 1 ] + translation [ 1 ]
                    };

                    dia_object.obj_pos ( new_position );
                }
            }
        }

        // Force re-calculate the line endpoints.
        for ( List<DiaChunk> chunks : dia_chunks.values () )
        {
            for ( DiaChunk chunk : chunks )
            {
                if ( chunk instanceof DiaLinear )
                {
                    final DiaLinear line = (DiaLinear) chunk;
                    final DiaLinear.Connection [] connections =
                        line.connections ();
                    if ( connections.length == 2 )
                    {
                        // Change the connection points, too.
                        line.connect ( connections [ 0 ].to,
                                       connections [ 1 ].to );
                    }
                    else
                    {
                        // Just change the positions.
                        line.connections ( connections );
                    }
                }
            }
        }
    }
}
