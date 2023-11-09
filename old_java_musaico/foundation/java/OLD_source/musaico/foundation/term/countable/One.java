package musaico.foundation.term.countable;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Just;
import musaico.foundation.term.Maybe;
import musaico.foundation.term.Type;

import musaico.foundation.term.multiplicities.OneOrMore;


/**
 * <p>
 * A single-valued Term.
 * </p>
 *
 *
 * <p>
 * In Java every conditional Term must be Serializable in order to
 * play nicely across RMI.  However users of the conditional Term
 * must be careful, since the values and expected data stored inside
 * might not be Serializable.
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
 * @see musaico.foundation.term.countable.MODULE#COPYRIGHT
 * @see musaico.foundation.term.countable.MODULE#LICENSE
 */
public class One<VALUE extends Object>
    extends AbstractCountable<VALUE>
    implements Just<VALUE>, Maybe<VALUE>, OneOrMore<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( One.class );


    /**
     * <p>
     * Creates a new One element Term.
     * </p>
     *
     * @param type The Type of this Term, such as a Type&lt;String&gt;
     *             for a Term of Strings, and so on.  Must not be null.
     *
     * @param one_value The single element of this new One-valued Term.
     *                  Must not be null.
     */
    public One (
            Type<VALUE> type,
            VALUE one_value
            )
        throws ParametersMustNotBeNull.Violation
    {
        super ( type,         // type
                one_value );  // one_value
    }


    /**
     * <p>
     * Creates a new One element Term.
     * </p>
     *
     * @param type The Type of this Term, such as a Type&lt;String&gt;
     *             for a Term of Strings, and so on.  Must not be null.
     *
     * @param one_element The single-valued Elements of this One-valued Term.
     *                    Must contain exactly 1 element.  Must not be null.
     */
    protected One (
            Type<VALUE> type,
            Elements<VALUE> one_element
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.Length.MustEqualOne.Violation
    {
        super ( type,         // type
                one_element );  // one_value

        classContracts.check ( Parameter2.Length.MustEqualOne.CONTRACT,
                               one_element );
    }


    /**
     * @see musaico.foundation.term.TermPipeline#duplicate()
     */
    @Override
    public final One<VALUE> duplicate ()
        throws ReturnNeverNull.Violation
    {
        return new One<VALUE> ( this.type (),
                                this.elements ()
                                    .duplicate () );
    }


    /**
     * @see musaico.foundation.term.countable.AbstractCountable#isFixedLengthInit()
     */
    @Override
    protected final boolean isFixedLengthInit ()
    {
        // Always exactly 1 element, even if that 1 element is mutable.
        return true;
    }
}
