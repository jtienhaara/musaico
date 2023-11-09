package musaico.foundation.wiring.components;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;


import musaico.foundation.filter.Filter;

import musaico.foundation.wiring.Board;
import musaico.foundation.wiring.Conductor;
import musaico.foundation.wiring.Schematic;
import musaico.foundation.wiring.Selector;
import musaico.foundation.wiring.Tag;
import musaico.foundation.wiring.Tags;
import musaico.foundation.wiring.Tap;
import musaico.foundation.wiring.Wire;


/**
 * <p>
 * Prints out a plain text representation of a Schematic.
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
public class SchematicPrinter
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    // Where we write the text representations of Schematics.
    private final PrintWriter out;


    /**
     * <p>
     * Creates a SchematicPrinter to write to stdout.
     * </p>
     */
    public SchematicPrinter ()
    {
        this ( new PrintWriter ( new OutputStreamWriter ( System.out ) ) );
    }

    /**
     * <p>
     * Creates a new SchematicPrinter.
     * </p>
     *
     * @param out The PrintWriter to which a text representation
     *            of each Schematic will be written.  If null,
     *            then stdout will be used by default.
     */
    public SchematicPrinter (
            PrintWriter out
            )
    {
        if ( out == null )
        {
            this.out = new PrintWriter ( new OutputStreamWriter ( System.out ) );
        }
        else
        {
            this.out = out;
        }
    }


    /**
     * <p>
     * Prints the Schematic for the specified Board.
     * </p>
     *
     * @param board The Board whose Schematic will be printed.
     *              If null, then nothing will be prined.
     *              ZIP, ZILCH, ZERO.  Too bad.
     *              Don't pass null, fool.
     */
    public void print (
            Board board
            )
    {
        if ( board == null )
        {
            return;
        }

        this.print ( board.schematic (), board );
    }


    /**
     * <p>
     * Prints a text representation of the specified
     * Schematic.
     * </p>
     *
     * @param schematic The Schematic to print out.
     *                  If null, then nothing will be printed.
     */
    public void print (
            Schematic schematic
            )
    {
        if ( schematic == null )
        {
            return;
        }

        final Board throwaway_board =
            schematic.build ();

        this.print ( schematic, throwaway_board );
    }

    private void print (
            Schematic schematic,
            Board board
            )
    {
        this.out.println ( "" + schematic.id () + ":" );
        this.out.println ( "  version: " + schematic.version () );

        this.out.println ( "  conductors:" );
        final Conductor [] conductors = board.conductors ();
        if ( conductors == null )
        {
            this.out.println ( "    null" );
            this.out.flush ();
            return;
        }

        final Conductor [] conductors_and_wires;
        final Wire [] wires = board.wires ();
        if ( wires == null )
        {
            conductors_and_wires = conductors;
        }
        else
        {
            conductors_and_wires =
                new Conductor [ conductors.length + wires.length ];
            System.arraycopy ( conductors, 0,
                               conductors_and_wires, 0, conductors.length );
            System.arraycopy ( wires, 0,
                               conductors_and_wires, conductors.length, wires.length );
        }

        for ( Conductor conductor : conductors )
        {
            if ( conductor == null )
            {
                this.out.println ( "  - null" );
                continue;
            }

            this.out.println ( "  - " + conductor.id () + ":" );

            this.out.println ( "    tags:" );
            final Tags tags = conductor.tags ();
            if ( tags == null )
            {
                this.out.println ( "      null" );
            }
            else
            {
                for ( Tag tag : tags )
                {
                    if ( tag == null )
                    {
                        this.out.println ( "    - null" );
                    }
                    else
                    {
                        this.out.println ( "    - " + tag );
                    }
                }
            }
        }

        this.out.println ( "  selectors:" );
        final Selector [] selectors = schematic.selectors ();
        if ( selectors == null )
        {
            this.out.println ( "    null" );
            this.out.flush ();
            return;
        }

        for ( Selector selector : selectors )
        {
            if ( selector == null )
            {
                this.out.println ( "  - null" );
                continue;
            }

            this.out.println ( "  - " + selector + ":" );
            for ( Filter<Conductor> filter : selector.ends () )
            {
                this.out.println ( "    - end: " + filter );
                if ( filter == null )
                {
                    continue;
                }

                this.out.println ( "      matches:" );
                for ( Conductor maybe_selected : conductors_and_wires )
                {
                    if ( maybe_selected == null )
                    {
                        continue;
                    }

                    try
                    {
                        if ( filter.filter ( maybe_selected ).isKept () )
                        {
                            this.out.println ( "      - " + maybe_selected.id () );
                        }
                    }
                    catch ( Exception e )
                    {
                        this.out.println ( "      - \"failed to match " + maybe_selected.id () + " due to " + e + "\"" );
                    }
                }
            }
        }

        this.out.println ( "  wires:" );
        if ( wires == null )
        {
            this.out.println ( "    null" );
            this.out.flush ();
            return;
        }

        for ( Wire wire : wires )
        {
            if ( wire == null )
            {
                this.out.println ( "  - null" );
                continue;
            }

            this.out.println ( "  - " + wire.id () + ":" );

            this.out.println ( "    tags:" );
            final Tags tags = wire.tags ();
            if ( tags == null )
            {
                this.out.println ( "      null" );
            }
            else
            {
                for ( Tag tag : tags )
                {
                    if ( tag == null )
                    {
                        this.out.println ( "    - null" );
                    }
                    else
                    {
                        this.out.println ( "    - " + tag );
                    }
                }
            }

            this.out.println ( "    ends:" );
            final Conductor [] ends = wire.ends ();
            if ( ends == null )
            {
                this.out.println ( "      null" );
            }
            else
            {
                for ( Conductor end : ends )
                {
                    if ( end == null )
                    {
                        this.out.println ( "    - null" );
                    }
                    else
                    {
                        this.out.println ( "    - " + end.id () );
                    }
                }
            }

            if ( wire instanceof Tap )
            {
                final Tap tap = (Tap) wire;

                this.out.println ( "    tapped_wire:" );
                final Wire tapped_wire = tap.tappedWire ();
                if ( tapped_wire == null )
                {
                    this.out.println ( "      null" );
                }
                else
                {
                    this.out.println ( "      " + tapped_wire.id () );
                }

                this.out.println ( "    tappers:" );
                final Conductor [] tappers = tap.tappers ();
                if ( tappers == null )
                {
                    this.out.println ( "      null" );
                }
                else
                {
                    for ( Conductor tapper : tappers )
                    {
                        if ( tapper == null )
                        {
                            this.out.println ( "    - null" );
                        }
                        else
                        {
                            this.out.println ( "    - " + tapper.id () );
                        }
                    }
                }

                this.out.println ( "    tap_wires:" );
                final Wire [] tap_wires = tap.taps ();
                if ( tap_wires == null )
                {
                    this.out.println ( "      null" );
                }
                else
                {
                    for ( Wire tap_wire : tap_wires )
                    {
                        if ( tap_wire == null )
                        {
                            this.out.println ( "    - null" );
                        }
                        else
                        {
                            this.out.println ( "    - " + tap_wire.id () );
                        }
                    }
                }
            }
        }

        this.out.flush ();
    }
}
