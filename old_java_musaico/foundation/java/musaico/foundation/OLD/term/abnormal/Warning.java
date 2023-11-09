package musaico.foundation.term.abnormal;

import java.io.Serializable;

import java.util.Iterator;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;
import musaico.foundation.term.Type;


/**
 * <p>
 * A Term wrapped in a warning message, which can either be
 * turned into an Error, or unwrapped to return the original Term.
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
public class Warning<VALUE extends Object>
    extends AbstractWrapped<VALUE, Term<VALUE>> // A Warning can wrap anything.
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( Warning.class );


    // The TermViolation which led to this Warning.
    private final TermViolation violation;


    /**
     * <p>
     * Creates a new Warning term.
     * </p>
     *
     * @param type The Type of this Term,, such as a Type&lt;String&gt;
     *              for a Term of Strings, and so on.
     *              Must not be null.
     *
     * @param violation The TermViolation which led to this AbstractWrapped,
     *                  such as "warning: deprecated operation"
     *                  or that sort of thing.  Must not be null.
     *
     * @param wrapped The Term wrapped by this Warning.  Must not be null.
     */
    public Warning (
            Type<VALUE> type,
            TermViolation violation,
            Term<VALUE> wrapped
            )
        throws ParametersMustNotBeNull.Violation
    {
        super ( type,                  // type
                wrapped );             // wrapped

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               violation );

        this.violation = violation;
    }


    /**
     * @see musaico.foundation.term.TermPipeline#duplicate()
     */
    @Override
    public final Warning<VALUE> duplicate ()
        throws ReturnNeverNull.Violation
    {
        return new Warning<VALUE> ( this.type (),
                                    this.violation (),
                                    this.unwrap () );
    }


    /**
     * @see musacico.foundation.term.abnormal.Abnormal#violation()
     */
    @Override
    public final TermViolation violation ()
        throws ReturnNeverNull.Violation
    {
        return this.violation;
    }
}
