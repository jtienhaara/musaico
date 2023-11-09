package musaico.foundation.capability.operating;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.contract.obligations.EveryParameter;

import musaico.foundation.structure.ClassName;


/**
 * <p>
 * The current state of a an operable object, such as started, stopped
 * or paused.
 * </p>
 *
 * <p>
 * In Java, every OperatingState must be Serializable in order to play nicely
 * over RMI.
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
 * @see musaico.foundation.capability.MODULE#COPYRIGHT
 * @see musaico.foundation.capability.MODULE#LICENSE
 */
public class OperatingState
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( OperatingState.class );


    /** No state, the operable object has not yet started. */
    public static final OperatingState NONE =
        new OperatingState ( "NONE" );

    /** In the process of starting, the operable object will soon
     *  be STARTED. */
    public static final OperatingState STARTING =
        new OperatingState ( "STARTING" );

    /** Started, the operable object is performing its work. */
    public static final OperatingState STARTED =
        new OperatingState ( "STARTED" );

    /** In the process of pausing, the operable object will soon
     *  be PAUSED. */
    public static final OperatingState PAUSING =
        new OperatingState ( "PAUSING" );

    /** Paused, the operable object is not performing work, but it might
     *  resume in future. */
    public static final OperatingState PAUSED =
        new OperatingState ( "PAUSED" );

    /** In the process of resuming, the operable object will soon
     *  be RESUMED. */
    public static final OperatingState RESUMING =
        new OperatingState ( "RESUMING" );

    /** In the process of stopping, the operable object will soon
     *  be STOPPED. */
    public static final OperatingState STOPPING =
        new OperatingState ( "STOPPING" );

    /** Stopped, the operable object is finished performing its work. */
    public static final OperatingState STOPPED =
        new OperatingState ( "STOPPED" );


    // The name of this OperatingState.
    private final String name;


    /**
     * <p>
     * Creates a new OperatingState.
     * </p>
     *
     * <p>
     * Protected.  Used by derived classes whose names are simply
     * their class names, such as "MyState".
     * </p>
     */
    public OperatingState ()
    {
        this.name = ClassName.of ( this.getClass () );
    }


    /**
     * <p>
     * Creates a new OperatingState.
     * </p>
     *
     * @param name The name of the new OperatingState.
     *             Must not be null.
     */
    public OperatingState (
            String name
            )
        throws EveryParameter.MustNotBeNull.Violation
    {
        classContracts.check ( EveryParameter.MustNotBeNull.CONTRACT,
                               name );

        this.name = name;
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
        else if ( object.getClass () != this.getClass () )
        {
            return false;
        }

        final OperatingState that = (OperatingState) object;

        if ( this.name == null )
        {
            if ( that.name != null )
            {
                return false;
            }
        }
        else if ( that.name == null )
        {
            return false;
        }
        else if ( ! this.name.equals ( that.name ) )
        {
            return false;
        }

        // Everything is all matchy-matchy.
        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return 31
            * ( this.name == null
                ? 0
                : this.name.hashCode () );
    }


    /**
     * @return The name of this OperatingState.  Never null.
     */
    public final String name ()
        throws Return.NeverNull.Violation
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
