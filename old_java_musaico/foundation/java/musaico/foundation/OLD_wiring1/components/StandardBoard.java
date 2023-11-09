package musaico.foundation.wiring;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

import java.util.Arrays;
import java.util.Iterator;

import java.util.logging.Level;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.contract.obligations.EveryParameter;
import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter4;
import musaico.foundation.contract.obligations.Parameter3;

import musaico.foundation.iterator.ArrayIterator;

import musaico.foundation.structure.StringRepresentation;


/**
 * <p>
 * Basic implementation of a Board ({...}).
 * </p>
 *
 *
 * <p>
 * In Java every Board must be Serializable in order to
 * play nicely across RMI.
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
public class StandardBoard
    implements Board, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;

    // Checks obligations on constructor static method parameters and so on.
    private static final Advocate classAdvocate =
        new Advocate ( StandardBoard.class );


    // The unique identifier of this Board within a given Context.
    private final String id;

    // The Components of this wiring Board.
    private final Component [] components;

    // The indices of the input / output Terminals of this wiring Board.
    private final int [] terminals;

    // Hash code.
    private final int hashCode;

    // Checks contract obligations and guarantees for us.
    private final Advocate advocate;


    /**
     * <p>
     * Creates a new StandardBoard.
     * </p>
     *
     * @param id The unique identifier of this Board within
     *           a given Context.  Must not be null.
     *
     * @param components The Components in this wiring Board.
     *                   Must contain at least 2 Components, the entry
     *                   terminal (components [ 0 ]) and the exit terminal
     *                   (componentss [ components.length - 1 ]).
     *                   The entry terminal cannot have any wires
     *                   leading to it.  The exit terminal cannot
     *                   have any wires leading from it.
     *                   Must not be null.
     *                   Must not contain any null elements.
     */
    public StandardBoard (
            String id,
            Component [] components
            )
        throws EveryParameter.MustNotBeNull.Violation,
               EveryParameter.MustContainNoNulls.Violation,
               Parameter4.Length.MustBeGreaterThanZero.Violation,
               BoardMustBeConnected.Violation
    {
        classAdvocate.check ( EveryParameter.MustNotBeNull.CONTRACT,
                              id, components );
        classAdvocate.check ( EveryParameter.MustContainNoNulls.CONTRACT,
                              (Object []) components );
        classAdvocate.check ( Parameter4.Length.MustBeGreaterThanZero.CONTRACT,
                              components );

        this.id = id;
        this.components = components;

        classAdvocate.check ( BoardMustBeConnected.CONTRACT,
                              this );

        final int [] terminal_indices =
            new int [ this.components.length ];
        int num_terminals = 0;
        int hash_code = id.hashCode () * 31;
        for ( int c = 0; c < this.components.length; c ++ )
        {
            final Component component = this.components [ c ];
            if ( component instanceof Terminal )
            {
                terminal_indices [ num_terminals ] = c;
            }

            hash_code += component.hashCode ();
        }

        this.terminals = new int [ num_terminals ];
        if ( num_terminals > 0 )
        {
            System.arraycopy ( terminal_indices, 0,
                               this.terminals, 0, num_terminals );
        }

        this.hashCode = hash_code;

        this.advocate = new Advocate ( this );
    }


    /**
     * @see musaico.foundation.wiring.Board#component(int)
     */
    @Override
    public final Component component (
            int index
            )
        throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
               Parameter1.MustBeLessThan.Violation
    {
        this.advocate.check ( Parameter1.MustBeGreaterThanOrEqualToZero.CONTRACT,
                              index );
        this.advocate.check ( new Parameter1.MustBeLessThan<Integer> ( this.components.length ),
                              index );

        return this.components [ index ];
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
        else if ( object == null )
        {
            return false;
        }
        else if ( object.getClass () != this.getClass () )
        {
            return false;
        }

        final StandardBoard that =
            (StandardBoard) object;
        if ( this.components == null )
        {
            if ( that.components != null )
            {
                return false;
            }
        }
        else if ( that.components == null )
        {
            return false;
        }
        else if ( ! Arrays.equals ( this.components, that.components ) )
        {
            return false;
        }

        return true;
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
    @Override
    public final String id ()
        throws Return.NeverNull.Violation
    {
        return this.id;
    }


    /**
     * @see musaico.foundation.wiring.Board#indexOf(musaico.foundation.wiring.Component)
     */
    @Override
    public final int indexOf (
            Component component
            )
        throws EveryParameter.MustNotBeNull.Violation,
               Parameter1.MustBeMemberOf.Violation,
               Return.AlwaysGreaterThanOrEqualToZero.Violation,
               Return.AlwaysLessThan.Violation
    {
        this.advocate.check ( EveryParameter.MustNotBeNull.CONTRACT,
                              component );

        for ( int n = 0; n < this.components.length; n ++ )
        {
            if ( this.components [ n ].equals ( component ) )
            {
                return n;
            }
        }

        // Not found.
        throw new Parameter1.MustBeMemberOf<Component> ( this.components )
            .violation ( this,
                         component );
    }


    /**
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public final Iterator<Component> iterator ()
        throws Return.NeverNull.Violation
    {
        return new ArrayIterator<Component> ( this.components );
    }


    /**
     * @see musaico.foundation.wiring.Board#size()
     */
    @Override
    public final int size ()
        throws Return.AlwaysGreaterThanZero.Violation
    {
        return this.components.length;
    }


    /**
     * @see musaico.foundation.wiring.Board#terminals()
     */
    @Override
    public final int [] terminals ()
        throws Return.NeverNull.Violation
    {
        return this.terminals;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return this.id;
    }


    /**
     * @see musaico.foundation.wiring.Board#toStringDetails()
     */
    @Override
    public final String toStringDetails ()
        throws Return.NeverNull.Violation
    {
        //!!! fix this rubbish to just output @Components and ~wires, not try to treat it like a graph.
        final StringWriter sw = new StringWriter ();
        final PrintWriter pw = new PrintWriter ( sw );

        pw.print ( this.id () );
        pw.print ( " {" );
        pw.println ();

        final String node_indent = "  ";
        final String arc_indent = "    ";
        final int [] node_indices = new int [ this.components.length ];
        Arrays.fill ( node_indices, -1 );
        final boolean [] is_printed = new boolean [ this.components.length ];
        node_indices [ 0 ] = 0; // Entry node.
        int num_nodes = 1;
        for ( int n = 0; n < node_indices.length; n ++ )
        {
            if ( node_indices [ n ] < 0 )
            {
                // No more nodes to print.
                break;
            }

            final Component node = this.components [ node_indices [ n ] ];
            pw.print ( node_indent );
            pw.print ( "@" );
            pw.print ( "" + node );
            pw.println ();

            is_printed [ node_indices [ n ] ] = true;

            for ( int a : node.wiresOut () )
            {
                final Component arc = this.components [ a ];
                pw.print ( arc_indent );
                pw.print ( "~" );
                pw.print ( "" + arc );
                pw.print ( " @" );
                boolean is_first = true;
                for ( int tn : arc.wiresOut () )
                {
                    if ( is_first )
                    {
                        is_first = false;
                    }
                    else
                    {
                        pw.print ( ", " );
                    }

                    final Component to_node = this.components [ tn ];
                    pw.print ( "@" );
                    pw.print ( "" + to_node );

                    if ( ! is_printed [ tn ] )
                    {
                        node_indices [ num_nodes ] = tn;
                        num_nodes ++;
                    }
                }

                pw.println ();
            }
        }

        pw.flush ();
        return sw.toString ();
    }
}
