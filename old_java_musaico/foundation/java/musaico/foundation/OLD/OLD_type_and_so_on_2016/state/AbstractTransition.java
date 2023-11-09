package musaico.foundation.state;

import java.io.Serializable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.LinkedHashSet;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.machine.Machine;

import musaico.foundation.value.Countable;

import musaico.foundation.value.builder.ValueBuilder;


/**
 * <p>
 * Provides boilerplate code for most Transition implementations.
 * </p>
 *
 *
 * <p>
 * In Java every Transition must be Serializable in order to
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
public abstract class AbstractTransition
    implements Transition, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final Advocate classContracts =
        new Advocate ( AbstractTransition.class );

    // Formats the timestamps for debug messages.
    private static final DateFormat TIMESTAMP =
        new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss.S zzz" );


    // Checks method obligations and guarantees.
    private final Advocate contracts;

    // The tapes which might be used at runtime by this transition.
    private final LinkedHashSet<Tape> tapes;


    /**
     * <p>
     * Creates a new AbstractTransition.
     * </p>
     *
     * @param tapes Zero or more tapes that are required to transition.
     *              Must not be null.  Must not contain any null elements.
     */
    public AbstractTransition (
                               Tape ... tapes
                               )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               (Object) tapes );
        classContracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               tapes );

        this.tapes = new LinkedHashSet<Tape> ();
        for ( Tape tape : tapes )
        {
            this.tapes.add ( tape );
        }

        this.contracts = new Advocate ( this );
    }


    /**
     * <p>
     * Writes out the specified debugging information
     * to the specified TapeMachine.  Convenience method.
     * </p>
     *
     * @param debug_machine The TapeMachine which records debugging
     *                      information.  Must not be TapeMachine.NONE.
     *                      Must not be null.
     *
     * @param timestamp The timestamp to record for the debug message,
     *                  down to the millisecond.  Must not be null.
     *
     * @param message The text to output to the specified
     *                debugging machine.  Must not be null.
     */
    public static final void debugMessage (
                                           TapeMachine debug_machine,
                                           Date timestamp,
                                           String message
                                           )
    {
        if ( debug_machine == null
             || debug_machine == TapeMachine.NONE )
        {
            return;
        }

        final String timestamp_string =
            AbstractTransition.TIMESTAMP.format ( timestamp );

        final String full_message =
            timestamp_string + " " + message;

        final TapeMachine message_machine = new TapeMachine ( full_message );

        System.err.println ( "!!! MESSAGE MACHINE =\n" + message_machine.graph ().toStringDetails () );
        System.err.println ( "!!! DEBUG MACHINE =\n" + message_machine.graph ().toStringDetails () );
        debug_machine.transition ( message_machine );

        // Don't care if it failed.
    }


    /**
     * @return The Advocate for this transition.  Checks parameter
     *         obligations, return guarantees, and so on for us.
     *         Never null.
     *
     * Protected for use by derived classes only.
     */
    protected final Advocate contracts ()
        throws ReturnNeverNull.Violation
    {
        return this.contracts;
    }


    /**
     * <p>
     * Writes out the specified debugging information
     * to the specified TapeMachine.  Convenience method.
     * </p>
     *
     * @param debug_machine The TapeMachine which records debugging
     *                      information.  Must not be TapeMachine.NONE.
     *                      Must not be null.
     *
     * @param timestamp The timestamp to record for the debug message,
     *                  down to the millisecond.  Must not be null.
     *
     * @param message The text to output to the specified
     *                debugging machine.  Must not be null.
     */
    protected final void debug (
                                TapeMachine debug_machine,
                                Date timestamp,
                                String message
                                )
    {
        AbstractTransition.debugMessage ( debug_machine,
                                          timestamp,
                                          message );
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

        final AbstractTransition that = (AbstractTransition) object;

        for ( Tape this_tape : this.tapes )
        {
            if ( ! that.tapes.contains ( this_tape ) )
            {
                return false;
            }
        }

        for ( Tape that_tape : that.tapes )
        {
            if ( ! this.tapes.contains ( that_tape ) )
            {
                return false;
            }
        }

        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return this.getClass ().getName ().hashCode ();
    }


    /**
     * @see musaico.foundation.tape.Transition#isTapeRequired(musaico.foundation.tape.Tape)
     */
    @Override
    public final boolean isTapeRequired (
                                         Tape tape
                                         )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               tape );

        if ( this.tapes.contains ( tape ) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * @see musaico.foundation.tape.Transition#tapes()
     */
    @Override
    public final Countable<Tape> tapes ()
        throws ReturnNeverNull.Violation
    {
        final ValueBuilder<Tape> builder =
            new ValueBuilder<Tape> ( Tape.VALUE_CLASS,
                                     this.tapes );
        final Countable<Tape> tapes = builder.build ();

        return tapes;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this.getClass () );
    }


    // Every AbstractTransition must implement
    // musaico.foundation.tape.Transition#transition(musaico.foundation.tape.TapeMachine[])
}
