package musaico.foundation.metadata;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.domains.ClassName;


/**
 * <p>
 * No origin at all, perhaps referring to an object which does
 * not exist anyway, so could not possibly have been created on
 * a particular host or thread.
 * </p>
 *
 *
 * <p>
 * In Java every metadatum must implement:
 * </p>
 *
 * @see java.lang.Object#equals(java.lang.Object)
 * @see java.lang.Object#hashCode()
 * @see java.lang.Object#toString()
 *
 * <p>
 * In Java every metadatum stored by a Metadata must be
 * Serializable in order to play nicely over RMI.
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
 * @see musaico.foundation.metadata.MODULE#COPYRIGHT
 * @see musaico.foundation.metadata.MODULE#LICENSE
 */
public class NoOrigin
    implements Origin, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


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

        // Every NoOrigin is identical to every other
        // NoOrigin of the same class.
        return true;
    }


    /**
     * @see java.lang.hashCode()
     */
    @Override
    public int hashCode ()
    {
        return 0;
    }


    /**
     * @see musaico.foundation.metadata.Origin#hostAndProcessID()
     */
    @Override
    public final String hostAndProcessID ()
        throws ReturnNeverNull.Violation
    {
        return "noHostOrProcess";
    }


    /**
     * @see musaico.foundation.metadata.Origin#stackTrace()
     */
    @Override
    public final String stackTrace ()
        throws ReturnNeverNull.Violation
    {
        return "";
    }


    /**
     * @see musaico.foundation.metadata.Origin#threadID()
     */
    @Override
    public final String threadID ()
        throws ReturnNeverNull.Violation
    {
        return "noThread";
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this.getClass () );
    }
}
