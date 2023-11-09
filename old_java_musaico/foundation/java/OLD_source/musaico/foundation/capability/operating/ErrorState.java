package musaico.foundation.capability.operating;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Violation;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * A bad state for an operable object, typically (though not necessarily)
 * preventing it from performing any further work.
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
public class ErrorState
    extends OperatingState
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( ErrorState.class );


    // The violation which caused this ErrorState.
    private final Violation violation;


    /**
     * <p>
     * Creates a new ErrorState.
     * </p>
     *
     * @param violation The cause of the new ErrorState.
     *                  Must not be null.
     */
    public <VIOLATION extends Throwable & Violation>
            ErrorState (
                        VIOLATION violation
                        )
        throws ParametersMustNotBeNull.Violation
    {
        super ();

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               violation );

        this.violation = violation;
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
        else if ( ! super.equals ( object ) )
        {
            return false;
        }

        final ErrorState that = (ErrorState) object;

        if ( this.violation == null )
        {
            if ( that.violation != null )
            {
                return false;
            }
        }
        else if ( that.violation == null )
        {
            return false;
        }
        else if ( ! this.violation.equals ( that.violation ) )
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
        return super.hashCode ()
            + 7
            * ( this.violation == null
                ? 0
                : this.violation.hashCode () );
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return super.toString ()
            + ": "
            + this.violation;
    }


    /**
     * @retun The Violation and Throwable which caused this error state.
     *        Never null.
     */
    @SuppressWarnings("unchecked") // Constructor checks Throwable & Violation.
    public final <VIOLATION extends Throwable & Violation>
            VIOLATION violation ()
        throws ReturnNeverNull.Violation
    {
        return (VIOLATION) this.violation;
    }
}
