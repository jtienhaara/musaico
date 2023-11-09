package musaico.foundation.term.incomplete;

import java.io.Serializable;

import java.util.Iterator;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.FilterState;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Maybe;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;
import musaico.foundation.term.Type;
import musaico.foundation.term.UncheckedTermViolation;

import musaico.foundation.term.contracts.ValueMustBeJust;

import musaico.foundation.term.countable.No;

import musaico.foundation.term.iterators.TermIterator;

import musaico.foundation.term.pipeline.AbstractTerm;


/**
 * <p>
 * Implements boilerplate functionality for Reducible terms.
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
 * @see musaico.foundation.term.incomplete.MODULE#COPYRIGHT
 * @see musaico.foundation.term.incomplete.MODULE#LICENSE
 */
public abstract class AbstractReducible<VALUE extends Object, CANONICAL extends Term<VALUE>>
    extends AbstractTerm<VALUE>
    implements Reducible<VALUE, CANONICAL>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new AbstractReducible term.
     * </p>
     *
     * @param type The Type of this Term,, such as a Type&lt;String&gt;
     *              for a Term of Strings, and so on.
     *              Must not be null.
     */
    public AbstractReducible (
            Type<VALUE> type
            )
        throws ParametersMustNotBeNull.Violation
    {
        super ( type == null // type_pipeline
                    ? null
                    : type.refine () );
    }


    /**
     * @see musaico.foundation.term.Multiplicity#countable()
    */
    @Override
    public final Countable<VALUE> countable ()
        throws ReturnNeverNull.Violation
    {
        return new No<VALUE> ( this.type () );
    }


    // Every Sink must implement
    // musaico.foundation.pipeline.Sink#duplicate()

    // Every Pipeline must implement
    // java.lang.Object#equals(java.lang.Object)


    /**
     * @see musaico.foundation.term.Multiplicity#filterState()
     */
    @Override
    public final FilterState filterState ()
         throws ReturnNeverNull.Violation
     {
         // No elements.
         return FilterState.DISCARDED;
     }


    // Every Pipeline must implement
    // java.lang.Object#hashCode()


    /**
     * @see musaico.foundation.term.Multiplicity#head()
     */
    @Override
    public final Maybe<VALUE> head ()
        throws ReturnNeverNull.Violation
    {
        return new No<VALUE> ( this.type () );
    }


    /**
     *@see musaico.foundation.term.Multiplicity#head(long)
     */
    @Override
    public final Countable<VALUE> head (
            long num_elements
            )
        throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( Parameter1.MustBeGreaterThanOrEqualToZero.CONTRACT,
                                  num_elements );

        return new No<VALUE> ( this.type () );
    }


    /**
     * @see musaico.foundation.term#indefiniteIterator(long)
     */
    @Override
    public final TermIterator<VALUE> indefiniteIterator (
            long maximum_iterations
            )
        throws Parameter1.MustBeGreaterThanZero.Violation
    {
        this.contracts ().check ( Parameter1.MustBeGreaterThanZero.CONTRACT,
                                  maximum_iterations );

        // No elements to iterate over.
        return new TermIterator<VALUE> ();
    }


    /**
     * @see musaico.foundation.term.Term#iterator()
     */
    @Override
    public final TermIterator<VALUE> iterator ()
    {
        // No elements to iterate over.
        return new TermIterator<VALUE> ();
    }


    /**
     * @see musaico.foundation.term.Multiplicity#orNull()
     */
    @Override
    public final VALUE orNull ()
        throws ReturnNeverNull.Violation
    {
        // Reducible values != exactly one value,
        // so return null.
        return null;
    }


    /**
     * @see musaico.foundation.term.Term#orThrowChecked()
     */
    @Override
    public final VALUE orThrowChecked ()
        throws TermViolation
    {
        throw TermMustNotBeReducible.CONTRACT.violation (
                  this,   // plaintiff
                  this ); // evidence
    }


    /**
     * @see musaico.foundation.term.Term#orThrowUnchecked()
     */
    @Override
    public final VALUE orThrowUnchecked ()
        throws UncheckedTermViolation
    {
        final UncheckedTermViolation unchecked_violation =
            new UncheckedTermViolation (
                    TermMustNotBeReducible.CONTRACT.violation (
                        this,   // plaintiff
                        this )  // evidence
                );

        throw unchecked_violation;
    }


    // Every Pipeline must implement
    // java.lang.Object#toString()

    // Every Reducible must implement
    // musacico.foundation.term.incomplete.Reducible#reduce()
}
