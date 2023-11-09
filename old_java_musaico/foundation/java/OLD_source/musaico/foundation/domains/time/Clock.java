package musaico.foundation.domains.time;

import java.io.Serializable;

import java.math.BigDecimal;


import musaico.foundation.domains.ClassName;
import musaico.foundation.domains.Seconds;


/**
 * <p>
 * An object which will generate the current time, or which can be
 * overridden by other implementations, for testing purposes,
 * such as to simulate the passing of time.
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
 * @see musaico.foundation.domains.time.MODULE#COPYRIGHT
 * @see musaico.foundation.domains.time.MODULE#LICENSE
 */
public class Clock
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The number of nanoseconds in 1 millisecond (10^6 or 1,000,000). */
    public static final long NANOSECONDS_PER_MILLISECOND = 1000000L;

    /** The standard system Clock. */
    public static final Clock STANDARD = new Clock ();


    /**
     * <p>
     * Creates a new Clock.
     * </p>
     *
     * <p>
     * Use Clock.STANDARD instead, unless you are implementing a sub-class
     * of Clock and must call super ().
     * </p>
     */
    protected Clock ()
    {
    }


    /**
     * @return The current time in seconds since midnight, January 1,
     *         1970 (UN*X time 0).  Never null.
     *
     * @see java.lang.System.currentTimeMillis()
     * @see java.lang.System.nanoTime()
     */
    public BigDecimal currentTimeInSeconds ()
    {
        final long milliseconds = System.currentTimeMillis ();
        final long nanoseconds =
            System.nanoTime () % NANOSECONDS_PER_MILLISECOND;

        final BigDecimal seconds =
            new BigDecimal ( milliseconds )
                .multiply ( Seconds.PER_MILLISECOND )
            .add (
                  new BigDecimal ( nanoseconds )
                  .multiply ( Seconds.PER_NANOSECOND ) );

        return seconds;
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

        // Nothing else to check.
        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return 1;
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
