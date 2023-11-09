package musaico.foundation.state;

import java.io.Serializable;

import java.util.LinkedHashSet;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.machine.Machine;

import musaico.foundation.value.ValueClass;

import musaico.foundation.value.classes.StandardValueClass;


/**
 * <p>
 * A Tape is a feed of data into and/or out of a TapeMachine,
 * such as an "input" Tape, an "output" Tape, and so on.
 * </p>
 *
 * <p>
 * Every TapeMachine requires a specific set of Tape feeds in order
 * to execute transitions.  For example, one TapeMachine might transition
 * based only on the data it receives from an "input" Tape.
 * A TapeMachine which generates random numbers might transition
 * by writing data to an "output" Tape.  A translation TapeMachine might
 * take data from an "input" Tape, transform it through a series of
 * state transitions, and write data to an "output" Tape.  And so on.
 * </p>
 *
 * <p>
 * At runtime, the data stream representing each Tape is itself
 * a TapeMachine.  Reading data from the "input" Tape, for example,
 * causes the machine representing that Tape to transition as data
 * is read.  Writing data to the "output" Tape modifies the graph
 * underlying the machine and then changes its state.  And so on.
 * </p>
 *
 * <p>
 * A single TapeMachine can be used as multiple Tapes for multiple
 * other machines.  For example, the machine receiving output data
 * from one TapeMachine's transitions might also provide the input
 * to another TapeMachine.  Or a single TapeMachine might provide
 * the input data to any number of TapeMachines, acting as a queue
 * or a buffer.  And so on.
 * </p>
 *
 * <p>
 * In Java every Tape must be Serializable in order to
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
 * @see musaico.foundation.typing.MODULE#COPYRIGHT
 * @see musaico.foundation.typing.MODULE#LICENSE
 */
public class Tape
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final Advocate classContracts =
        new Advocate ( Tape.class );


    /** Dev Null type tape. */
    public static final Tape NONE = new Tape ( "none" );


    /** The input tape to a TapeMachine. */
    public static final Tape INPUT = new Tape ( "input" );

    /** The output tape from a TapeMachine. */
    public static final Tape OUTPUT = new Tape ( "output" );

    /** The error output tape from a TapeMachine. */
    public static final Tape ERROR = new Tape ( "error" );

    /** The TapeMachine currently attempting to transition.
     *  The machine for this Tape is always provided internally;
     *  it is never a required tape for calling TapeMachine.transition (),
     *  even if the machine's Transitions do require THIS. */
    public static final Tape THIS = new Tape ( "this", false );

    /** Optional DEBUG TapeMachine, for capturing debugging information
     *  from Transitions. */
    public static final Tape DEBUG = new Tape ( "debug", false );


    /** The default ValueClass of Tapes, including contracts and
     *  none value and so on. */
    public static final ValueClass<Tape> VALUE_CLASS =
        new StandardValueClass<Tape> ( Tape.class, Tape.NONE );


    // The id or name of this Tape.
    private final String name;

    // Is this Tape mandatory?  If so, it must be provided as a parameter
    // to TapeMachines and Transitions which require it.  If not mandatory,
    // the TapeMachine will default it to TapeMachine.NONE for
    // Transitions which require it as a parameter.
    private final boolean isMandatory;


    /**
     * <p>
     * Creates a new Tape.
     * </p>
     *
     * @param name The unique identifier of this Tape.  Must not be null.
     */
    public Tape (
                 String name
                 )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        this ( name,
               true ); // is_mandatory
    }


    /**
     * <p>
     * Creates a new Tape.
     * </p>
     *
     * @param name The unique identifier of this Tape.  Must not be null.
     *
     * @param is_mandatory True if TapeMachines and Transitions which
     *                     require this Tape expect the parameter to
     *                     be provided; false if a default machine of
     *                     some sort will be provided to Transitions
     *                     requiring it (such as "this" tape machine,
     *                     or TapeMachine.NONE for "debug", and so on).
     */
    public Tape (
                 String name,
                 boolean is_mandatory
                 )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               name );

        this.name = name;
        this.isMandatory = is_mandatory;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
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
        else if ( this.getClass () != object.getClass () )
        {
            return false;
        }

        final Tape that = (Tape) object;

        if ( ! this.name.equals ( that.name ) )
        {
            return false;
        }

        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return this.name.hashCode ();
    }


    /**
     * @return True if TapeMachines and Transitions which
     *         require this Tape expect the parameter to
     *         be provided; false if a default machine of
     *         some sort will be provided to Transitions
     *         requiring it (such as "this" tape machine,
     *         or TapeMachine.NONE for "debug", and so on).
     */
    public final boolean isMandatory ()
    {
        return this.isMandatory;
    }


    /**
     * @return The name of this Tape, such as "input" or
     *         "output" and so on.  Never null.
     */
    public final String name ()
    {
        return this.name;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return this.name;
    }
}
