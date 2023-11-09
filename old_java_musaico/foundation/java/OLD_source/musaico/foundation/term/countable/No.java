package musaico.foundation.term.countable;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Operation;
import musaico.foundation.term.Term;
import musaico.foundation.term.Type;

import musaico.foundation.term.multiplicities.Empty;


/**
 * <p>
 * No value at all.
 * </p>
 *
 *
 * <p>
 * In Java every Multiplicity must be Serializable in order to
 * play nicely across RMI.  However users of the Multiplicity
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
public class No<VALUE extends Object>
    extends AbstractCountable<VALUE>
    implements Empty<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( No.class );


    /**
     * <p>
     * Creates a new No value.
     * </p>
     *
     * @param type The Type of this Term, such as a Type&lt;String&gt;
     *             for No Strings, and so on.  Must not be null.
     */
    public No (
            Type<VALUE> type
            )
        throws ParametersMustNotBeNull.Violation
    {
        super ( type );       // type
        // No array values.
    }


    /**
     * <p>
     * Creates a new No-element Term.
     * </p>
     *
     * @param type The Type of this Term, such as a Type&lt;String&gt;
     *             for a Term of Strings, and so on.  Must not be null.
     *
     * @param elements The empty Elements of this No-valued Term.
     *                 Must contain 0 elements.  Must not be null.
     */
    protected No (
            Type<VALUE> type,
            Elements<VALUE> elements
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.Length.MustEqualZero.Violation
    {
        super ( type,         // type
                elements );   // array

        classContracts.check ( Parameter2.Length.MustEqualZero.CONTRACT,
                               elements
 );
    }


    /**
     * @see musaico.foundation.term.TermPipeline#duplicate()
     */
    @Override
    public final No<VALUE> duplicate ()
        throws ReturnNeverNull.Violation
    {
        return new No<VALUE> ( this.type () );
    }


    /**
     * @see musaico.foundation.term.countable.AbstractCountable#isFixedLengthInit()
     */
    @Override
    protected final boolean isFixedLengthInit ()
    {
        // Always exactly 0 elements.
        return true;
    }
}
