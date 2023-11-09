package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.Parameter1;


/**
 * <p>
 * The maximum amount of time to block during
 * <code> Synchronicity.await ( ... ) </code>.  Used by Types,
 * Namespaces and Terms to provide default settings when requested
 * to block indefinitely.
 * </p>
 *
 *
 * <p>
 * In Java every metadatum is Serializable in order to play nicely
 * over RMI.  Every metadatum must also provide a zero-args constructor
 * to create a fallback "none" value.
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
public class MetaBlockingTimeLimit
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( MetaBlockingTimeLimit.class );


    // How long to wait, maximum, in milliseconds, or 0L for indefinite
    // waiting.
    private final long maximumBlockingTimeMilliseconds;


    /**
     * <p>
     * Creates a new MetaBlockingTimeLimit, specifying how many milliseconds
     * (maximum) to wait for a blocking call.
     * </p>
     *
     * @param maximum_blocking_time_milliseconds How many milliseconds
     *                                           (maximum) to wait for
     *                                           a blocking call.
     *                                           If 0L then wait
     *                                           indefinitely.
     *                                           Must be greater than or
     *                                           equal to 0L.
     */
    public MetaBlockingTimeLimit (
                                  long maximum_blocking_time_milliseconds
                                  )
        throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation
    {
        classContracts.check ( Parameter1.MustBeGreaterThanOrEqualToZero.CONTRACT,
                               maximum_blocking_time_milliseconds );

        this.maximumBlockingTimeMilliseconds =
            maximum_blocking_time_milliseconds;
    }


    /**
     * <p>
     * Creates a MetaBlockingTimeLimit which will block indefinitely.
     * </p>
     */
    public MetaBlockingTimeLimit ()
    {
        this ( 0L );
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
                                 Object object
                                 )
    {
        if ( object == null )
        {
            return false;
        }
        else if ( object == this )
        {
            return true;
        }
        else if ( ! ( object instanceof MetaBlockingTimeLimit ) )
        {
            return false;
        }

        MetaBlockingTimeLimit that = (MetaBlockingTimeLimit) object;
        if ( this.maximumBlockingTimeMilliseconds !=
             that.maximumBlockingTimeMilliseconds () )
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
        return (int) this.maximumBlockingTimeMilliseconds;
    }


    /**
     * @return How long to wait (maximum) in milliseconds for a blocking
     *         call to finish.  Always greater than or equal to 0L.
     */
    public final long maximumBlockingTimeMilliseconds ()
    {
        return this.maximumBlockingTimeMilliseconds;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return "BlockingTime: " + this.maximumBlockingTimeMilliseconds
            + " ms";
    }
}
