package musaico.foundation.value;

import java.io.Serializable;

import java.lang.reflect.Constructor;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;


/**
 * <p>
 * Forces the user to decide whether to block or asynchronously handle
 * a Blocking Value.
 * </p>
 *
 * @see musaico.foundation.value.Blocking
 *
 *
 * <p>
 * In Java every Synchronicity must be Serializable in order to
 * play nicely across RMI.  However users of the Asynchronous class
 * must be careful, since the values and expected data stored inside
 * might not be Serializable.
 * </p>
 *
 * <p>
 * In Java every Synchronicity must implement equals (), hashCode ()
 * and toString ().
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
 * @see musaico.foundation.value.MODULE#COPYRIGHT
 * @see musaico.foundation.value.MODULE#LICENSE
 */
public class Asynchronous<VALUE extends Object>
    implements Synchronicity<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Enforces static parameter obligations and so on for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( Asynchronous.class );


    /** Enforces parameter obligations and so on for us. */
    private final ObjectContracts contracts;

    /** The Blocking result wrapped by this Asynchronous. */
    private final Blocking<VALUE> blockingValue;


    /**
     * <p>
     * Creates a new Asynchronous result, forcing the caller to
     * decide whether to wait for the result synchronously
     * with <code> onBlocking () <code>, or process the result
     * asynchronously with <code> pipe () <code>.
     * </p>
     *
     * @param blocking_value The Blocking Value which will be filled
     *                       in asynchronously.  Must not be null.
     */
    public Asynchronous (
                         Blocking<VALUE> blocking_value
                         )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               blocking_value );

        this.blockingValue = blocking_value;

        this.contracts = new ObjectContracts ( this );
    }


    /**
     * @see musaico.foundation.value.Synchronicity#async()
     */
    public Blocking<VALUE> async ()
        throws ReturnNeverNull.Violation
    {
        return this.blockingValue;
    }


    /**
     * @see musaico.foundation.value.Synchronicity#async(musaico.foundation.value.ValueProcessor)
     */
    @Override
    public Blocking<VALUE> async (
                                  ValueProcessor<VALUE> value_processor
                                  )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return this.blockingValue.pipe ( value_processor );
    }


    /**
     * @see musaico.foundation.value.Synchronicity#await()
     */
    @Override
    public NonBlocking<VALUE> await ()
    {
        return this.await ( Long.MAX_VALUE ); // Wait forever.
    }


    /**
     * @see musaico.foundation.value.Synchronicity#await(long)
     */
    @Override
    public NonBlocking<VALUE> await (
                                     long timeout_in_nanoseconds
                                     )
    {
        return this.blockingValue.onBlocking ( timeout_in_nanoseconds );
    }


    /**
     * @see musaico.foundation.typing.blockingMaxNanoseconds()
     */
    @Override
    public final long blockingMaxNanoseconds ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        final long blocking_max_nanoseconds =
            this.blockingValue.blockingMaxNanoseconds ();

        this.contracts.check ( Return.AlwaysGreaterThanOrEqualToZero.CONTRACT,

                               blocking_max_nanoseconds );

        return blocking_max_nanoseconds;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
                           Object obj
                           )
    {
        if ( obj == null
             || ! ( obj instanceof Asynchronous ) )
        {
            return false;
        }

        Asynchronous<?> that = (Asynchronous<?>) obj;

        if ( this.blockingValue == null )
        {
            if ( that.blockingValue != null )
            {
                return false;
            }
        }
        else if ( that.blockingValue == null )
        {
            return false;
        }
        else if ( ! this.blockingValue.equals ( that.blockingValue ) )
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
        return 11 + this.blockingValue.hashCode ();
    }


    /**
     * @see java.lang.toString()
     */
    @Override
    public String toString ()
    {
        return "Asynchronous:" + this.blockingValue.toString ();
    }
}
