package musaico.foundation.wiring;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.contract.obligations.EveryParameter;
import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;

import musaico.foundation.structure.ClassName;


/**
 * <p>
 * One pin of a Chip, connecting a Component on its parent Board
 * (the Board the Chip is mounted on) to / from a Terminal on the
 * integrated Board inside the Chip.
 * </p>
 *
 * <p>
 * A Leg is effectively an input wire or an output wire
 * for the Chip as a Component.  It can be used as either one;
 * context (the Chip itself) determines which.
 * </p>
 *
 *
 * <p>
 * In Java every Leg must be Serializable in order to
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
public class Leg
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;

    // Checks obligations on constructor static method parameters and so on.
    private static final Advocate classAdvocate =
        new Advocate ( Leg.class );


    // The index of the Component on the parent Board to / from which
    // this Leg connects the Chip.
    private final int parentComponent;

    // The index of the Terminal on the internal Board of the Chip
    // to / from which this Leg connects the parent Component.
    private final int internalTerminal;


    /**
     * <p>
     * Creates a new Leg.
     * </p>
     *
     * @param parent_component The index of the Component on the
     *                         parent Board to / from which
     *                         this Leg connects the Chip.
     *                         Must be greater than or equal to 0.
     *                         Must be less than the size ()
     *                         of the parent Board.
     *
     * @param internal_terminal The index of the Terminal on the
     *                          internal Board of the Chip
     *                       to / from which this Leg connects
     *                       the parent Component.
     *                       Must be greater than or equal to 0.
     *                       Must be less than the number
     *                       of terminals () on the Chip's
     *                       internal Board.
     */
    public Leg (
            int parent_component,
            int internal_terminal
            )
        throws EveryParameter.MustBeGreaterThanOrEqualToZero.Violation,
               Parameter2.MustBeGreaterThanOrEqualToZero.Violation
    {
        classAdvocate.check ( EveryParameter.MustBeGreaterThanOrEqualToZero.CONTRACT,
                              parent_component, internal_terminal );

        this.parentComponent = parent_component;
        this.internalTerminal = internal_terminal;
    }


    /**
     * @return The index of the Terminal on the internal Board of the Chip
     *         to / from which this Leg connects the parent Component.
     *         Always greater than or equal to 0.
     */
    public final int internalTerminal ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        return this.internalTerminal;
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

        final Leg that = (Leg) object;
        if ( that.parentComponent != this.parentComponent )
        {
            return false;
        }
        else if ( that.internalTerminal != this.internalTerminal )
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
        return this.parentComponent * 31
            + this.internalTerminal * 17;
    }


    /**
     * @return The index of the Component on the parent Board to / from which
     *          this Leg connects the Chip.
     *          Always greater than or equal to 0.
     */
    public final int parentComponent ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        return this.parentComponent;
    }


    /**
     *
     * <p>
     * Returns the parent component indices of the specified Legs.
     * </p>
     *
     * @param legs The Legs whose parent component indices
     *             will be returned.  Must not be null.
     *             Must not contain any null elements.
     *
     * @return The indices of the parent components of the
     *         specified Legs.  Never null.  Always 0 or greater.
     */
    public static final int [] parentComponents (
            Leg [] legs
            )
        throws EveryParameter.MustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        classAdvocate.check ( EveryParameter.MustNotBeNull.CONTRACT,
                              (Object []) legs );
        classAdvocate.check ( Parameter1.MustContainNoNulls.CONTRACT,
                              legs );

        final int [] parent_components = new int [ legs.length ];
        for ( int l = 0; l < legs.length; l ++ )
        {
            parent_components [ l ] = legs [ l ].parentComponent ();
        }

        return parent_components;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return ClassName.of ( this.getClass () )
            + " ( "
            + this.parentComponent
            + " ~ "
            + this.internalTerminal
            + " )";
    }
}
