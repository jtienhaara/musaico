package musaico.foundation.metadata;

import java.io.Serializable;


import musaico.foundation.contract.Arbiter;
import musaico.foundation.contract.Contract;
import musaico.foundation.contract.ObjectContracts;
import musaico.foundation.contract.Violation;

import musaico.foundation.metadata.Metadata;
import musaico.foundation.metadata.Stat;
import musaico.foundation.metadata.Statistics;


/**
 * <p>
 * The contract manager for a specific plaintiff Object which has
 * Metadata.  TrackingContracts tracks contract violation Statistics for
 * its plaintiff.
 * </p>
 *
 * @see musaico.foundation.contract.ObjectContracts
 *
 *
 * <p>
 * In Java every ObjectContracts is Serializable in order to play
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
 * @see musaico.foundation.metadata.MODULE#COPYRIGHT
 * @see musaico.foundation.metadata.MODULE#LICENSE
 */
public class TrackingContracts
    extends ObjectContracts
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The Metadata which tracks Contract violation statistics
     *  for the plaintiff.  Can be a NoMetadata, or a Metadata
     *  with NoStatistics. */
    private final Metadata metadata;


    /**
     * <p>
     * Creates a new TrackingContracts for the specified plaintiff, using
     * the Arbiter specified in the system properties, and tracking
     * statistics in the specified Metadata.
     * </p>
     *
     * <p>
     * Be careful to only call
     * <code> new TrackingContracts ( plaintiff, metadata ) </code>
     * on a plaintiff which is initialized to the point that its
     * <code> toString () </code> method will not throw
     * NullPointerExceptions and so on.  Usually it is best to create
     * the TrackingContracts for a plaintiff as the very last step of
     * the plaintiff's constructor.
     * </p>
     *
     * @param plaintiff The object on whose behalf we will manage contracts.
     *                  A Serializable reference to the specified plaintiff
     *                  will be stored by calling plaintiff.toString ().
     *                  No reference to the actual plaintiff will be kept.
     *                  Must not be null.
     *
     * @param metadata The Metadata used to track violations of the
     *                 plaintiff's Contracts.  Can be NoMetadata.
     *                 Can have NoStatistics.  Must not be null.
     */
    public TrackingContracts (
                              Object plaintiff,
                              Metadata metadata
                              )
    {
        super ( plaintiff );

        this.metadata = metadata;
    }


    /**
     * <p>
     * Creates a new TrackingContracts for the specified plaintiff and with
     * a specific Arbiter, overriding the system properties settings,
     * and tracking statistics in the specified Metadata.
     * </p>
     *
     * <p>
     * This constructor can be used, for example, when certain contract
     * enforcement should always be handled the same way, and not
     * be configurable.
     * </p>
     *
     * <p>
     * Be careful to only call
     * <code> new TrackingContracts ( plaintiff, metadata, arbiter ) </code>
     * on a plaintiff which is initialized to the point that its
     * <code> toString () </code> method will not throw
     * NullPointerExceptions and so on.  Usually it is best to create
     * the TrackingContracts for a plaintiff as the very last step of
     * the plaintiff's constructor.
     * </p>
     *
     * @param plaintiff The object on whose behalf we will manage contracts.
     *                  A Serializable reference to the specified plaintiff
     *                  will be stored by calling plaintiff.toString ().
     *                  No reference to the actual plaintiff will be kept.
     *                  Must not be null.
     *
     * @param metadata The Metadata used to track violations of the
     *                 plaintiff's Contracts.  Can be NoMetadata.
     *                 Can have NoStatistics.  Must not be null.
     */
    public TrackingContracts (
                              Object plaintiff,
                              Metadata metadata,
                              Arbiter arbiter
                              )
    {
        super ( plaintiff, arbiter );

        this.metadata = metadata;
    }


    /**
     * @see musaico.foundation.contract.ObjectContracts#check(musaico.foundation.contract.Contract, java.lang.Object)
     */
    @Override
    public final
        <INSPECTABLE extends Object, EVIDENCE extends INSPECTABLE, VIOLATION extends Throwable & Violation>
        EVIDENCE check (
                        Contract<INSPECTABLE, VIOLATION> contract,
                        EVIDENCE evidence
                        )
        throws VIOLATION
    {
        boolean is_violation = true;
        final EVIDENCE result;
        try
        {
            result = super.check ( contract, evidence );
            is_violation = false;
        }
        finally
        {
            if ( is_violation )
            {
                this.metadata.statistics ()
                             .orNone ()
                             .increment ( Stat.VIOLATIONS );
            }
        }

        return result;
    }


    /**
     * <p>
     * Variant on check () which allows for variable # of arguments
     * to be passed in, for checking arrays (such as checking that
     * all parameters are not null).
     * </p>
     *
     * @see musaico.foundation.contract.ObjectContracts#check(musaico.foundation.contract.Contract, java.lang.Object)
     *
     * @return The evidence.
     */
    @SuppressWarnings("unchecked") // possible heap pollution EVIDENCE varargs
    public final
        <EVIDENCE extends Object, VIOLATION extends Throwable & Violation>
        EVIDENCE [] check (
                           Contract<EVIDENCE [], VIOLATION> contract,
                           EVIDENCE ... evidence
                           )
        throws VIOLATION
    {
        boolean is_violation = true;
        final EVIDENCE [] results;
        try
        {
            results = super.check ( contract, evidence );
            is_violation = false;
        }
        finally
        {
            if ( is_violation )
            {
                this.metadata.statistics ()
                             .orNone ()
                             .increment ( Stat.VIOLATIONS );
            }
        }

        return results;
    }


    /**
     * @return This TrackingContracts's Metadata, which is used to
     *         track contract violation Statistics.  Note that the
     *         Metadata may be a NoMetadata, and/or its Statistics
     *         may be a NoStatistics.  In either case tracking is
     *         effectively disabled.  Never null.
     */
    public final Metadata metadata ()
    {
        return this.metadata;
    }


    /**
     * @see java.lang.Objects#toString()
     */
    @Override
    public String toString ()
    {
        return "Tracking" + super.toString ();
    }
}
