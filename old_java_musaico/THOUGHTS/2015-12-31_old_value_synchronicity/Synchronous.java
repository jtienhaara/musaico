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
 * what could have been, but in fact is not, a Blocking Value.
 * </p>
 *
 * @see musaico.foundation.value.Blocking
 * @see musaico.foundation.value.NonBlocking
 *
 * <p>
 * For convenience, a Synchronous value implements all the methods
 * of the Value interface directly.  Therefore the caller need not
 * invoke <code> await ( ... ) </code> to access the result, as
 * long as the caller is not interested in the <i> class </i> of
 * Value.  For example the caller can invoke <code> orNone () </code>
 * on a Synchronous result.
 * </p>
 *
 *
 * <p>
 * In Java every Synchronicity must be Serializable in order to
 * play nicely across RMI.  However users of the Synchronous class
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
public class Synchronous<VALUE extends Object>
    extends CompositeValue<VALUE>
    implements Synchronicity<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Enforces static parameter obligations and so on for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( Synchronous.class );


    /** The NonBlocking result wrapped by this Synchronous. */
    private final NonBlocking<VALUE> nonBlockingValue;


    /**
     * <p>
     * Creates a new Synchronous result, forcing the caller to
     * decide whether to wait for the result synchronously
     * with <code> onBlocking () <code>, or process the result
     * asynchronously with <code> pipe () <code>.
     * </p>
     *
     * @param non_blocking_value The NonBlocking Value which will be filled
     *                           in asynchronously.  Must not be null.
     */
    public Synchronous (
                        NonBlocking<VALUE> non_blocking_value
                        )
        throws ParametersMustNotBeNull.Violation
    {
        super ( non_blocking_value );

        this.nonBlockingValue = non_blocking_value;
    }


    /**
     * @see musaico.foundation.value.Synchronicity#async()
     */
    public final NonBlocking<VALUE> async ()
        throws ReturnNeverNull.Violation
    {
        return this.nonBlockingValue;
    }


    /**
     * @see musaico.foundation.value.Synchronicity#async(musaico.foundation.value.ValueProcessor)
     */
    @Override
    public final Value<VALUE> async (
                                     ValueProcessor<VALUE> value_processor
                                     )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return this.nonBlockingValue.pipe ( value_processor );
    }


    /**
     * @see musaico.foundation.value.Synchronicity#await()
     */
    @Override
    public final NonBlocking<VALUE> await ()
    {
        return this.await ( 0L ); // No need to wait.
    }


    /**
     * @see musaico.foundation.value.Synchronicity#await(long)
     */
    @Override
    public final NonBlocking<VALUE> await (
                                           long timeout_in_nanoseconds
                                           )
    {
        return this.nonBlockingValue; // No need to wait.
    }


    /**
     * @see musaico.foundation.typing.blockingMaxNanoseconds()
     */
    @Override
    public final long blockingMaxNanoseconds ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        return 0L;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
                                 Object obj
                                 )
    {
        if ( obj == this )
        {
            return true;
        }
        else if ( obj == null
             || ! ( obj instanceof Synchronous ) )
        {
            return false;
        }
        else if ( ! super.equals ( obj ) )
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
    public final int hashCode ()
    {
        return 11 + this.nonBlockingValue.hashCode ();
    }


    /**
     * @see java.lang.toString()
     */
    @Override
    public final String toString ()
    {
        return "Synchronous:" + this.nonBlockingValue.toString ();
    }
}
