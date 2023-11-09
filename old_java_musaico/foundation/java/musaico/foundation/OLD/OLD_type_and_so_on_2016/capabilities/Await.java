package musaico.foundation.capability.administrative;

import java.io.Serializable;

import java.math.BigDecimal;


import musaico.foundation.capability.StandardCapability;

import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.contract.obligations.Parameter1;

import musaico.foundation.domains.ClassName;


/**
 * <p>
 * Asks the controllee to block the caller until a value is ready
 * before returning it.
 * </p>
 *
 *
 * <p>
 * In Java, every Control must be Serializable in order to play nicely
 * over RMI.  WARNING: Parameters such as Operations sent over RMI Controls
 * (requesters, subscribers, and so on) generally must be UnicastRemoteObjects
 * in order to properly pass messages from one machine to the other, rather
 * than simply serializing each requester or subscriber and replying locally
 * on the controlled machine.
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
 * @see musaico.foundation.capability.administrative.MODULE#COPYRIGHT
 * @see musaico.foundation.capability.administrative.MODULE#LICENSE
 */
public class Await
    extends ReadCapability
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** # of nanoseconds in 1 second.  For Await.toString (). */
    public static final BigDecimal NANOSECONDS_PER_SECOND =
        new BigDecimal ( "1000000000000" );


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( Await.class );


    // The maximum timeout to wait, in nanoseconds, before giving up
    // and returning an error.
    private final long maxTimeoutInNanoseconds;


    /**
     * <p>
     * Creates a new Await control.
     * </p>
     *
     * @param max_timeout_in_nanoseconds The maximum amount of time,
     *                                   in nanoseconds, to wait for
     *                                   a publication before giving up.
     *                                   Must be greater than
     *                                   or equal to 0L nanoseconds.
     */
    public static final Await (
            long max_timeout_in_nanoseconds
            )
        throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation
    {
        classContracts.check ( Parameter1.MustBeGreaterThanOrEqualToZero.CONTRACT,
                               max_timeout_in_nanoseconds );
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

        final Control that = (Control) object;

        if ( ! super.equals ( that ) )
        {
            return false;
        }

        if ( this.maxTimeoutInNanoseconds != that.maxTimeoutInNanoseconds )
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
        return 23
            * (int) this.maxTimeoutInNanoseconds;
    }


    /**
     * @return The maximum amount of time, in nanoseconds, to wait for
     *         a publication before giving up.  Always greater than
     *         or equal to 0L nanoseconds.
     */
    public final long maxTimeoutInNanoseconds ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        return this.maxTimeoutInNanoseconds;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        final BigDecimal seconds =
            new BigDecimal ( this.maxTimeoutInNanoseconds )
            .divide ( NANOSECONDS_PER_SECOND );

        return super.toString ()
            + " ( "
            + seconds
            + " )";
    }
}
