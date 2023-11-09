package musaico.foundation.value.control;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.value.Operation;
import musaico.foundation.value.Type;


/**
 * <p>
 * A Control mode, such as how to operate a processor (sleeping until
 * some Control event is received, or running as a server, or recurring
 * periodically, and so on), how to read data (blocking or asynchronously,
 * and so on), and so on.
 * </p>
 *
 * <p>
 * The Mode class is abstract, and must be overridden by a Mode implementation
 * that is actually useful (such as OperatingMode or ReadMode and so on).
 * </p>
 *
 *
 * <p>
 * In Java, every Mode must be Serializable in order to play nicely
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
 * @see musaico.foundation.value.control.MODULE#COPYRIGHT
 * @see musaico.foundation.value.control.MODULE#LICENSE
 */
public abstract class Mode
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** No mode at all, for Controls which do not require any Mode. */
    public static final Mode NONE = new NoMode ();


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( Mode.class );


    // The name of this Mode.
    private final String name;


    /**
     * <p>
     * Creates a new Mode.
     * </p>
     *
     * @param name The name of the new Mode.  Must not be null.
     */
    public Mode (
                 String name
                 )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
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

        final Mode that = (Mode) object;

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
        return 29
            * ( this.name == null
                ? 0
                : this.name.hashCode () );
    }


    /**
     * @return The name of this Mode.  Never null.
     */
    public final String name ()
        throws ReturnNeverNull.Violation
    {
        return this.name;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( Mode.class )
            + " [ " + this.name + " ]";
    }
}
