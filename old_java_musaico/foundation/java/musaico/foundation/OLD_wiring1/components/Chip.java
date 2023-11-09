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
import musaico.foundation.contract.obligations.Parameter3;
import musaico.foundation.contract.obligations.Parameter4;

import musaico.foundation.iterator.ArrayIterator;

import musaico.foundation.structure.StringRepresentation;


/**
 * <p>
 * A Component comprised of an internal Board with legs leading
 * into / out of the internal Board from / to other Components on
 * the parent Board.
 * </p>
 *
 * <p>
 * Each of the Chip's wireIns () leads from a Component on the
 * parent Board to a Terminal on the internal Board.
 * </p>
 *
 * <p>
 * Each of the Chip's wireOuts () leads from a Terminal on the
 * internal Board to a Component on the parent Board.
 * </p>
 *
 *
 * <p>
 * In Java every Component must be Serializable in order to
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
public class Chip
    extends AbstractComponent
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;

    // Checks obligations on constructor static method parameters and so on.
    private static final Advocate classAdvocate =
        new Advocate ( Chip.class );


    // The internal Board.
    private final Board board;

    // The input Legs of this Chip, leading from Components
    // on the parent Board to Terminals on the internal Board.
    private final Leg [] legsIn;

    // The output Legs of this Chip, leading from Terminals
    // on the internal Board to Components on the parent Board.
    private final Leg [] legsOut;

    // Checks contract obligations and guarantees for us.
    private final Advocate advocate;


    /**
     * <p>
     * Creates a new Chip.
     * </p>
     *
     * @param id The unique identifier of this Board within
     *           a given Context.  Must not be null.
     *
     * @param board The internal Board containing this Chip's logic.
     *              Must not be null.
     *
     * @param legs_in The connected input pins into this Chip.
     *                Each leg in must reference an internalTerminal ()
     *                index that is less than the number of
     *                terminals () on the specified Board.
     *                Must not be null.  Must not contain
     *                any null elements.
     *
     * @param legs_out The connected output pins from this Chip.
     *                 Each leg in must reference an internalTerminal ()
     *                 index that is less than the number of
     *                 terminals () on the specified Board.
     *                 Must not be null.  Must not contain
     *                 any null elements.
     */
    public Chip (
            String id,
            Board board,
            Leg [] legs_in,
            Leg [] legs_out
            )
        throws EveryParameter.MustNotBeNull.Violation,
               Parameter3.MustContainNoNulls.Violation,
               Parameter4.MustContainNoNulls.Violation
    {
        super ( id,                                  // id
                Leg.parentComponents (
                  classAdvocate.check ( Parameter3.MustContainNoNulls.CONTRACT,
                                        legs_in ) ),  // wires_in
                Leg.parentComponents (
                  classAdvocate.check ( Parameter4.MustContainNoNulls.CONTRACT,
                                        legs_out ) ) ); // wires_out

        final int [] terminals = board.terminals ();
        for ( Leg leg_in : legs_in )
        {
            final int leg_in_terminal = leg_in.internalTerminal ();
            classAdvocate.check ( new Parameter3.MustBeLessThan<Integer> ( terminals.length ),
                                  leg_in_terminal );
        }
        for ( Leg leg_out : legs_out )
        {
            final int leg_out_terminal = leg_out.internalTerminal ();
            classAdvocate.check ( new Parameter4.MustBeLessThan<Integer> ( terminals.length ),
                                  leg_out_terminal );
        }

        this.board = board;
        this.legsIn = legs_in;
        this.legsOut = legs_out;

        this.advocate = new Advocate ( this );
    }


    /**
     * @see musaico.foundation.wiring.Component#apply(musaico.foundation.wiring.Component, musaico.foundation.wiring.Context)
     */
    @Override
    public final void apply (
            Component input,
            MutableContext private_context
            )
        throws EveryParameter.MustNotBeNull.Violation
    {
        private_context.log ( Level.FINEST, "Enter " + this );

        // !!!;
        private_context.output ( input );

        private_context.log ( Level.FINEST, "Exit " + this );
    }


    /**



    /**
     * @see musaico.foundation.wiring.AbstractComponent#equalsComponent(musaico.foundation.wiring.Component)
     */
    protected final boolean equalsComponent (
            Component component
            )
        throws Parameter1.MustBeInstanceOf.Violation,
               EveryParameter.MustNotBeNull.Violation
    {
        final Chip that =
            (Chip) component;

        if ( this.legsIn == null )
        {
            if ( that.legsIn != null )
            {
                return false;
            }
        }
        else if ( that.legsIn == null )
        {
            return false;
        }
        else if ( ! Arrays.equals ( this.legsIn, that.legsIn ) )
        {
            return false;
        }

        if ( this.legsOut == null )
        {
            if ( that.legsOut != null )
            {
                return false;
            }
        }
        else if ( that.legsOut == null )
        {
            return false;
        }
        else if ( ! Arrays.equals ( this.legsOut, that.legsOut ) )
        {
            return false;
        }

        return true;
    }
}
