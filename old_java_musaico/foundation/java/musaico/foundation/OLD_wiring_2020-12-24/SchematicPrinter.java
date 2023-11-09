package musaico.foundation.wiring;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;


import musaico.foundation.filter.Filter;


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
        this.out.println ( "" + schematic + ":" );
        this.out.println ( "  version: " + schematic.version () );

        this.out.println ( "  conductors:" );
        final Conductor [] conductors = board.conductors ();
        if ( conductors == null )
        {
            this.out.println ( "    null" );
            this.out.flush ();
            return;
        }

        for ( Conductor conductor : conductors )
        {
            if ( conductor == null )
            {
                this.out.println ( "  - null" );
                continue;
            }

            this.out.println ( "  - " + conductor + ":" );

            this.out.println ( "    tags:" );
            final Tag [] tags = conductor.tags ();
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

            this.out.println ( "    selectors:" );
            final Selector [] selectors = conductor.selectors ( board );
            if ( selectors == null )
            {
                this.out.println ( "      null" );
                continue;
            }

            for ( Selector selector : selectors )
            {
                if ( selector == null )
                {
                    this.out.println ( "    - null" );
                    continue;
                }

                this.out.println ( "    - " + selector + ":" );
                final Filter<Conductor> filter = selector.filter ();
                this.out.println ( "      filter: " + filter );
                if ( filter == null )
                {
                    continue;
                }

                this.out.println ( "      matches:" );
                for ( Conductor maybe_selected : conductors )
                {
                    if ( maybe_selected == null )
                    {
                        continue;
                    }

                    try
                    {
                        if ( filter.filter ( maybe_selected ).isKept () )
                        {
                            this.out.println ( "      - " + maybe_selected );
                        }
                    }
                    catch ( Exception e )
                    {
                        this.out.println ( "      - \"failed to match " + maybe_selected + " due to " + e + "\"" );
                    }
                }
            }
        }

        this.out.flush ();
    }
}
