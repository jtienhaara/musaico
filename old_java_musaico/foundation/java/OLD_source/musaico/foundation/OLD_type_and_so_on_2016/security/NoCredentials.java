package musaico.foundation.security;

import java.io.Serializable;


/**
 * <p>
 * No Credentials at all for a security request.
 * </p>
 *
 *
 * <p>
 * In Java every Credentials must implement equals() and hashCode().
 * </p>
 *
 * <p>
 * In Java every Credentials must be Serializable in order to play
 * nicely over RMI.
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
 * @see musaico.foundation.security.MODULE#COPYRIGHT
 * @see musaico.foundation.security.MODULE#LICENSE
 */
public class NoCredentials
    implements Credentials, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new NoCredentials.
     * </p>
     *
     * <p>
     * Should only be called from Credentials.NONE, and anywhere else a
     * "no credentials" constant is created (such as User.NONE).
     * </p>
     */
    protected NoCredentials ()
    {
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
                           Object object
                           )
    {
        if ( object == null )
        {
            return false;
        }
        else if ( object.getClass () == this.getClass () )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return 0;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "NoCredentials";
    }
}
