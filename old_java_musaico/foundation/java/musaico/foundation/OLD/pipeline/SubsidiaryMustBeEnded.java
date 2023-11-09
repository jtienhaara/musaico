package musaico.foundation.pipeline;

import java.io.Serializable;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;
import musaico.foundation.contract.UncheckedViolation;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;


/**
 * <p>
 * A Contract requiring that a Subsidiary must have been
 * <code> end () </code>ed previously.
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
 * @see musaico.foundation.pipeline.MODULE#COPYRIGHT
 * @see musaico.foundation.pipeline.MODULE#LICENSE
 */
public class SubsidiaryMustBeEnded
    implements Contract<Subsidiary<?, ?, ?, ?>, SubsidiaryMustBeEnded.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The parameters-must-not-be-null obligation singleton. */
    public static final SubsidiaryMustBeEnded CONTRACT =
        new SubsidiaryMustBeEnded ();


    /**
     * <p>
     * Only the singleton or derived classes can access the constructor
     * directly.  Use SubsidiaryMustBeEnded.CONTRACT instead.
     * </p>
     */
    protected SubsidiaryMustBeEnded ()
    {
    }


    /**
     * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
     */
    @Override
    public final String description ()
    {
        return "The Subsidiary must have been end () ed previously.";
    }

    @Override
    public final FilterState filter (
                                     Subsidiary<?, ?, ?, ?> evidence
                                     )
    {
        if ( evidence == null )
        {
            return FilterState.DISCARDED;
        }
        else if ( evidence.isEnded () )
        {
            return FilterState.KEPT;
        }
        else
        {
            return FilterState.DISCARDED;
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
     */
    @Override
    public final SubsidiaryMustBeEnded.Violation violation (
            Object plaintiff,
            Subsidiary<?, ?, ?, ?> evidence
            )
    {
        return new SubsidiaryMustBeEnded.Violation (
                       this,
                       plaintiff,
                       evidence );
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object, java.lang.Throwable)
     */
    @Override
    public final SubsidiaryMustBeEnded.Violation violation (
            Object plaintiff,
            Subsidiary<?, ?, ?, ?> evidence,
            Throwable cause
            )
    {
        final SubsidiaryMustBeEnded.Violation violation =
            this.violation ( plaintiff, evidence );
        if ( cause != null )
        {
            violation.initCause ( cause );
        }

        return violation;
    }


    /**
     * <p>
     * A violation of the parameters-must-not-be-null contract.
     * </p>
     */
    public static class Violation
        extends UncheckedViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            SubsidiaryMustBeEnded.serialVersionUID;

        /**
         * <p>
         * Creates a SubsidiaryMustBeEnded.Violation.
         * </p>
         */
        public Violation (
                          SubsidiaryMustBeEnded obligation,
                          Object plaintiff,
                          Subsidiary<?, ?, ?, ?> evidence
                          )
        {
            super ( obligation,
                    "The Subsidiary was never end () ed.", // description
                    Contracts.makeSerializable ( plaintiff ),
                    Contracts.makeSerializable ( evidence ) );
        }
    }
}
