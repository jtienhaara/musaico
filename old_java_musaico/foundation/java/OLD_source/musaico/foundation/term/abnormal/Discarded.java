package musaico.foundation.term.abnormal;

import java.io.Serializable;

import java.util.Iterator;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.FilterState;

import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;
import musaico.foundation.term.Type;

import musaico.foundation.term.contracts.TermFilterStateMustNotBeKept;


/**
 * <p>
 * A Term which has been discarded by some kind of filter.
 * </p>
 *
 *
 * <p>
 * In Java every Pipeline must be Serializable in order to
 * play nicely across RMI.  However users of the Pipeline
 * must be careful, since the values and expected data stored inside
 * might not be Serializable.
 * </p>
 *
 * <p>
 * In Java every Pipeline must implement equals (), hashCode ()
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
 * @see musaico.foundation.term.abnormal.MODULE#COPYRIGHT
 * @see musaico.foundation.term.abnormal.MODULE#LICENSE
 */
public class Discarded<VALUE extends Object>
    extends AbstractWrapped<VALUE, Term<VALUE>> // A Discarded can wrap anything.
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new Discarded term.
     * </p>
     *
     * @param type The Type of this Term,, such as a Type&lt;String&gt;
     *              for a Term of Strings, and so on.
     *              Must not be null.
     *
     * @param discarded The FilterState of this Term, such as
     *                  <code> FilterState.DISCARDED </code>.
     *                  Must always return <code> isKept () == false </code>.
     *                  Must not be null.
     *
     * @param wrapped The Term wrapped by this Discarded.  Must not be null.
     */
    public Discarded (
            Type<VALUE> type,
            FilterState discarded,
            Term<VALUE> wrapped
            )
        throws ParametersMustNotBeNull.Violation,
               TermFilterStateMustNotBeKept.Violation
    {
        super ( type,      // type
                discarded, // discarded
                wrapped ); // wrapped
    }


    /**
     * @see musaico.foundation.term.TermPipeline#duplicate()
     */
    @Override
    public final Discarded<VALUE> duplicate ()
        throws ReturnNeverNull.Violation
    {
        try
        {
            return new Discarded<VALUE> (
                this.type (),
                this.filterState (),
                this.unwrap () );
        }
        catch ( TermFilterStateMustNotBeKept.Violation violation )
        {
            throw ReturnNeverNull.CONTRACT.violation (
                      this,        // plaintiff
                      violation ); // evidence
        }
    }


    /**
     * @see musacico.foundation.term.abnormal.Abnormal#violation()
     */
    @Override
    public final TermViolation violation ()
        throws ReturnNeverNull.Violation
    {
        return TermMustNotBeDiscarded.CONTRACT.violation (
            this,   // plaintiff
            this ); // evidence
    }
}
