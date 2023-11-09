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
 * Two or more values of fixed length.
 * </p>
 *
 * <p>
 * Many typically represents a single-dimensional array of
 * elements, along the lines of a List or Set, and so on.
 * </p>
 *
 * <p>
 * Unlike One, Many does not have one single element.
 * So operations which expect a single element will fail.
 * Or, by using the <code> iterator () </code> method,
 * operations which do expect multiple values can iterate over
 * the elements of the Many.  Or the first One element of
 * the Many can be returned by calling head ().
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
public class Many<VALUE extends Object>
    extends AbstractCountable<VALUE>
    implements OneOrMore<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( Many.class );


    /**
     * <p>
     * Creates Many (more than 1) elements.
     * </p>
     *
     * @param type The Type of this Term, such as a Type&lt;String&gt;
     *              for a Term of Strings, and so on.
     *              Must not be null.
     *
     * @param iterable The Iterable value (such as a Term
     *                 or a Collection and so on) of 2 or more elements
     *                 comprising this value.  Depending on the
     *                 Type, these elements will end up
     *                 being immutable or mutable.  Depending
     *                 on this.isFixedLength (), mutable elements
     *                 might also end up being variable length.
     *                 Must contain more than 1 elements.  Must not be null.
     *                 Must not contain any null elements.
     */
    public Many (
            Type<VALUE> type,
            Iterable<VALUE> iterable
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation,
               Parameter2.Length.MustBeGreaterThanOne.Violation
    {
        super ( type,       // type
                iterable ); // iterable

        try
        {
            classContracts.check ( Parameter2.MustBeGreaterThanOne.CONTRACT,
                                   this.elements ().length () );
        }
        catch ( Parameter2.MustBeGreaterThanOne.Violation violation )
        {
            throw Parameter2.Length.MustBeGreaterThanOne.CONTRACT
                .violation ( this,       // plaintiff
                             iterable ); // evidence
        }
    }


    /**
     * <p>
     * Creates Many (more than 1) elements.
     * </p>
     *
     * @param type The Type of this Term, such as a Type&lt;String&gt;
     *              for a Term of Strings, and so on.
     *              Must not be null.
     *
     * @param array The Many elements of this value.  If this value
     *              has a Type that declares its Terms Mutable,
     *              then these elements can change over time, though
     *              they will always be the same length.
     *              Must contain more than 1 elements.  Must not be null.
     *              Must not contain any null elements.
     */
    @SuppressWarnings({"unchecked","varargs"})//Possible heap pollution varargs
    @SafeVarargs
    public Many (
            Type<VALUE> type,
            VALUE ... array
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation,
               Parameter2.Length.MustBeGreaterThanOne.Violation
    {
        super ( type,       // type
                array );    // array

        try
        {
            classContracts.check ( Parameter2.MustBeGreaterThanOne.CONTRACT,
                                   this.elements ().length () );
        }
        catch ( Parameter2.MustBeGreaterThanOne.Violation violation )
        {
            throw Parameter2.Length.MustBeGreaterThanOne.CONTRACT
                .violation ( this,    // plaintiff
                             array ); // evidence
        }
    }


    /**
     * <p>
     * Creates a new Many-elements Term.
     * </p>
     *
     * @param type The Type of this Term, such as a Type&lt;String&gt;
     *             for a Term of Strings, and so on.  Must not be null.
     *
     * @param elements The multi-valued Elements of this Many-valued Term.
     *                 Must contain 2 or more elements.  Must not be null.
     */
    protected Many (
            Type<VALUE> type,
            Elements<VALUE> elements
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.Length.MustBeGreaterThanOne.Violation
    {
        super ( type,         // type
                elements );   // array

        classContracts.check ( Parameter2.Length.MustBeGreaterThanOne.CONTRACT,
                               elements );
    }


    /**
     * @see musaico.foundation.term.TermPipeline#duplicate()
     */
    @Override
    public final Many<VALUE> duplicate ()
        throws ReturnNeverNull.Violation
    {
        return new Many<VALUE> ( this.type (),
                                 this.elements ()
                                     .duplicate () );
    }


    /**
     * @see musaico.foundation.term.countable.AbstractCountable#isFixedLengthInit()
     */
    @Override
    protected final boolean isFixedLengthInit ()
    {
        // Always exactly N element(s), even if those N element(s) are mutable.
        return true;
    }
}
