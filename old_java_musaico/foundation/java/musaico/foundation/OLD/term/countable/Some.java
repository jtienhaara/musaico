package musaico.foundation.term.countable;

import java.io.Serializable;

import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Type;

import musaico.foundation.term.multiplicities.OneOrMore;


/**
 * <p>
 * Zero or more values, which, if mutable, are variable length.
 * </p>
 *
 * <p>
 * Some typically represents a single-dimensional array of
 * elements which can be modified, along the lines of a List or Set,
 * and so on.
 * </p>
 *
 * <p>
 * Unlike No, One, and Many, Some is not fixed length unless it
 * has a Type which declares it to be Immutable.
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
public class Some<VALUE extends Object>
    extends AbstractCountable<VALUE>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( Some.class );


    /**
     * <p>
     * Creates Some (0 or more) elements.
     * </p>
     *
     * @param type The Type of this Term, such as a Type&lt;String&gt;
     *             for Some Strings, and so on.  Must not be null.
     *
     * @param iterable The Iterable value (such as a Multiplicity
     *                 or a Collection and so on) of 0 or more elements
     *                 comprising this value.  Depending
     *                 on the Type, these elements will end up
     *                 being immutable or mutable.
     *                 Mutable elements can also be variable length.
     *                 Must not be null.  Must not contain any null elements.
     */
    public Some (
            Type<VALUE> type,
            Iterable<VALUE> iterable
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation
    {
        super ( type,       // type
                iterable ); // iterable
    }


    /**
     * <p>
     * Creates Some (0 or more) elements.
     * </p>
     *
     * @param type The Type of this Term, such as a Type&lt;String&gt;
     *             for Some Strings, and so on.  Must not be null.
     *
     * @param is_fixed_length True if this multiplicity is always exactly
     *                        the same length; false if its length can
     *                        be changed whenever it has a Type
     *                        that declares its terms to be Mutable.
     *
     * @param array The array  of 0 or more elements comprising
     *              this value.  Depending on the Type,
     *              these elements will end up being immutable or mutable.
     *              Mutable elements can also be variable length.
     *              Must not be null.  Must not contain any null elements.
     */
    @SuppressWarnings({"unchecked","varargs"})//Possible heap pollution varargs
    @SafeVarargs
    public Some (
            Type<VALUE> type,
            VALUE ... array
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation
    {
        super ( type,       // type
                array );    // array
    }


    /**
     * <p>
     * Creates a new Some element(s) (0 or more) Term.
     * </p>
     *
     * @param type The Type of this Term, such as a Type&lt;String&gt;
     *             for a Term of Strings, and so on.  Must not be null.
     *
     * @param elements The 0 or more Elements of this Some-valued Term.
     *                 Can be any length.  Must not be null.
     */
    protected Some (
            Type<VALUE> type,
            Elements<VALUE> elements
            )
        throws ParametersMustNotBeNull.Violation
    {
        super ( type,         // type
                elements );   // array
    }


    /**
     * @see musaico.foundation.term.TermPipeline#duplicate()
     */
    @Override
    public final Some<VALUE> duplicate ()
        throws ReturnNeverNull.Violation
    {
        return new Some<VALUE> ( this.type (),
                                 this.elements ()
                                     .duplicate () );
    }


    /**
     * @see musaico.foundation.term.countable.AbstractCountable#isFixedLengthInit()
     */
    @Override
    protected final boolean isFixedLengthInit ()
    {
        // Variable length.
        return false;
    }
}
