package musaico.foundation.contract.guarantees;

import java.io.Serializable;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;
import musaico.foundation.contract.UncheckedViolation;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;


/**
 * <p>
 * A method which throws ReturnNeverNull.Violation
 * expects the return value to never be null.  If, for some reason,
 * the method cannot keep its own guarantee, it will send itself to
 * arbitration, possibly inducing the runtime exception.
 * </p>
 *
 *
 * <p>
 * In Java, every Contract must implement equals () and hashCode ().
 * </p>
 *
 * <p>
 * In Java, every Contract must be Serializable in order to play
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
 * @see musaico.foundation.contract.guarantees.MODULE#COPYRIGHT
 * @see musaico.foundation.contract.guarantees.MODULE#LICENSE
 */
public class ReturnNeverNull
    implements Contract<Object, ReturnNeverNull.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The return-never-null guarantee singleton. */
    public static final ReturnNeverNull CONTRACT =
        new ReturnNeverNull ();


    /**
     * <p>
     * Only the singleton or derived classes can access the constructor
     * directly.  Use ReturnNeverNull.CONTRACT instead.
     * </p>
     */
    protected ReturnNeverNull ()
    {
    }

    /**
     * @see musaico.foundation.contract.Contract#description()
     */
    @Override
    public final String description ()
    {
        return "The return value is guaranteed to never be null.";
    }


    /**
     * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
     */
    @Override
    public final FilterState filter (
                                     Object evidence
                                     )
    {
        if ( evidence == null )
        {
            return FilterState.DISCARDED;
        }
        else
        {
            return FilterState.KEPT;
        }
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return ClassName.of ( this.getClass () );
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     *
     * Final for speed.
     */
    @Override
    public final ReturnNeverNull.Violation violation (
                                                      Object plaintiff,
                                                      Object evidence
                                                      )
    {
        return new ReturnNeverNull.Violation ( this,
                                               plaintiff,
                                               evidence );
    }

    @Override
    public final ReturnNeverNull.Violation violation (
                                                      Object plaintiff,
                                                      Object evidence,
                                                      Throwable cause
                                                      )
    {
        final ReturnNeverNull.Violation violation =
            this.violation ( plaintiff, evidence );
        if ( cause != null )
        {
            violation.initCause ( cause );
        }

        return violation;
    }


    /**
     * <p>
     * A violation of the return-never-null contract.
     * </p>
     */
    public static class Violation
        extends UncheckedViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            ReturnNeverNull.serialVersionUID;

        /**
         * <p>
         * Creates a ReturnNeverNull.Violation.
         * </p>
         */
        public Violation (
                          ReturnNeverNull guarantee,
                          Object plaintiff,
                          Object evidence
                          )
        {
            super ( guarantee,
                    "The return value was null.", // description
                    Contracts.makeSerializable ( plaintiff ),
                    Contracts.makeSerializable ( evidence ) );
        }
    }
}
