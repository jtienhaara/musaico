package musaico.foundation.construct;

import java.io.Serializable;


import musaico.foundation.structure.ClassName;


/**
 * <p>
 * An error caused while a constructing an object, caused, for example,
 * by a null or illegal argument to a Constructor, and so on.
 * </p>
 *
 * <p>
 * For example, suppose a <code> Constructor&lt;Integer&gt; </code>
 * accepts a String parameter, and expects the String to contain only
 * numeric digits '0' through '9'.  Then calling
 * <code> constructor.apply ( null ) </code> would induce a
 * ConstructionException; as would invoking
 * <code> constructor.apply ( "z" ) </code>; as would calling
 * <code> constructor.apply ( "" + Long.MAX_VALUE ) </code>.
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
 * @see musaico.foundation.construct.MODULE#COPYRIGHT
 * @see musaico.foundation.construct.MODULE#LICENSE
 */
public class ConstructionException
    extends Exception
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** How many stack frames will be output, maximum, during
     *  printStackTrace()? */
    public static final int MAXIMUM_STACK_FRAMES = 7;


    // The Construct that generated this exception.
    // Typically a Constructor.
    private final Construct<?> construct;


    /**
     * <p>
     * Creates a new ConstructionException.
     * </p>
     *
     * @param construct The Construct that generated this exception.
     *                  Typically a Constructor.
     *                  Should not be null, but no checking is done.
     *
     * @param message The Exception message, explaining what this
     *                ConstructionException means, why it was caused,
     *                and what the state of the world was at the
     *                time the exception was created.
     *                Should not be null, but no checking is done.
     */
    public ConstructionException (
            Construct<?> construct,
            String message
            )
    {
        super ( message );

        this.construct = construct;
    }


    /**
     * <p>
     * Creates a new ConstructionException.
     * </p>
     *
     * @param construct The Construct that generated this exception.
     *                  Typically a Constructor.
     *                  Should not be null, but no checking is done.
     *
     * @param message The Exception message, explaining what this
     *                ConstructionException means, why it was caused,
     *                and what the state of the world was at the
     *                time the exception was created.
     *                Should not be null, but no checking is done.
     *
     * @param cause The Exception or other Throwable that induced
     *              this ConstructionException.  Can be null.
     */
    public ConstructionException (
            Construct<?> construct,
            String message,
            Throwable cause
            )
    {
        this ( construct, message );

        if ( cause != null )
        {
            this.initCause ( cause );
        }
    }


    /**
     * @return The Construct that generated this exception.
     *         Typically a Constructor.
     *         Should not be null, but no checking is done.
     */
    public final Construct<?> construct ()
    {
        return this.construct;
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

        final ConstructionException that = (ConstructionException) object;

        if ( this.construct == null )
        {
            if ( that.construct != null )
            {
                return false;
            }
        }
        else if ( that.construct == null )
        {
            return false;
        }
        else if ( ! this.construct.equals ( that.construct ) )
        {
            return false;
        }

        if ( ! super.equals ( that ) )
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
        int hash_code = 0;
        if ( this.construct != null )
        {
            hash_code += 31 * this.construct.hashCode ();
        }

        return hash_code;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        final StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( ClassName.of ( this.getClass () ) );
        sbuf.append ( " { " );
        sbuf.append ( " construct = " + this.construct () );
        sbuf.append ( ", " + this.getMessage () );
        sbuf.append ( " }" );
        return sbuf.toString ();
    }
}
