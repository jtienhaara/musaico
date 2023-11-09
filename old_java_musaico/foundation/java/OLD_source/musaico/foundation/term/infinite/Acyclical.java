package musaico.foundation.term.infinite;

import java.io.Serializable;

import java.util.Iterator;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Violation;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;
import musaico.foundation.domains.StringRepresentation;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;
import musaico.foundation.term.Type;
import musaico.foundation.term.UncheckedTermViolation;

import musaico.foundation.term.contracts.ValueMustBeJust;

import musaico.foundation.term.countable.Many;
import musaico.foundation.term.countable.No;
import musaico.foundation.term.countable.One;

import musaico.foundation.term.iterators.InfiniteLoopProtector;
import musaico.foundation.term.iterators.IteratorMustHaveNextObject;
import musaico.foundation.term.iterators.TermIterator;

import musaico.foundation.term.multiplicities.Infinite;

import musaico.foundation.term.pipeline.AbstractTerm;


/**
 * <p>
 * An infinite, non-repeating sequence of of elements.
 * </p>
 *
 * <p>
 * Unlike Just one value, an Acyclical term does not have one single element.
 * So operations which expect a single element will fail.
 * The <code> iterator () </code> method also iterates over 0
 * Acyclical elements, in order to prevent infinite loops.
 * The <code> infiniteIterator () </code> can be used for
 * operations which do expect multiple values, in order to iterate over
 * some of the elements of the Acyclical (CAUTION: infinite loops!).
 * Or the first One element of the Acyclical can be returned
 * by calling head ().  And so on.
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
 * @see musaico.foundation.term.infinite.MODULE#COPYRIGHT
 * @see musaico.foundation.term.infinite.MODULE#LICENSE
 */
public abstract class Acyclical<VALUE extends Object>
    extends AbstractTerm<VALUE>
    implements Infinite<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( Acyclical.class );


    /**
     * <p>
     * Creates a new Acyclical infinite term.
     * </p>
     *
     * @param type The Type of this Term,, such as a Type&lt;String&gt;
     *              for a Term of Strings, and so on.
     *              Must not be null.
     */
    public Acyclical (
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
        // Can't make an Infinite term Countable.
        return new No<VALUE> ( this.type () );
    }


    // Every Pipeline must implement java.lang.Object#equals(java.lang.Object)


    /**
     * @see musaico.foundation.term.Multiplicity#filterState()
     */
    @Override
    public final FilterState filterState ()
         throws ReturnNeverNull.Violation
     {
         // At least 1 element.
         return FilterState.KEPT;
     }


    /**
     * @see java.lang.Object#hashCode()
     *
     * Can be overridden by derived classes.
     */
    @Override
    public int hashCode ()
    {
        return 31 * this.getClass ().getName ().hashCode ()
            + this.type ().hashCode ();
    }


    /**
     * @see musaico.foundation.term.Multiplicity#head()
     */
    @Override
    public final One<VALUE> head ()
        throws IteratorMustHaveNextObject.Violation,
               ReturnNeverNull.Violation
    {
        final Iterator<VALUE> iterator = this.infiniteIterator ();
        final VALUE first_element;
        if ( ! iterator.hasNext () )
        {
            first_element = null;
        }
        else
        {
            first_element = iterator.next ();
        }

        if ( first_element == null )
        {
            final IteratorMustHaveNextObject.Violation violation =
                IteratorMustHaveNextObject.CONTRACT.violation (
                    this,            // plaintiff
                    iterator );      // evidence
            throw violation;
        }

        return new One<VALUE> (
            this.type (),         // type
            first_element );      // one_value
    }


    /**
     *@see musaico.foundation.term.Multiplicity#head(long)
     */
    @Override
    public final Countable<VALUE> head (
            long num_elements
            )
        throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
               IteratorMustHaveNextObject.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( Parameter1.MustBeGreaterThanOrEqualToZero.CONTRACT,
                                  num_elements );

        if ( num_elements <= 0L
             || num_elements > (long) Integer.MAX_VALUE )
        {
            return new No<VALUE> ( this.type () );
        }
        else if ( num_elements == 1L )
        {
            return this.head ();
        }

        final Type<VALUE> type = this.type ();
        final Iterator<VALUE> iterator = this.infiniteIterator ();
        final VALUE [] head = type.array ( (int) num_elements );
        for ( int h = 0; h < head.length; h ++ )
        {
            if ( ! iterator.hasNext () )
            {
                final IteratorMustHaveNextObject.Violation violation =
                    IteratorMustHaveNextObject.CONTRACT.violation (
                        this,            // plaintiff
                        iterator );      // evidence
                throw violation;
            }

            head [ h ] = iterator.next ();
        }

        return new Many<VALUE> (
            this.type (),         // type
            head );               // array
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

        final Iterator<VALUE> infinite_iterator = this.infiniteIterator ();
        final InfiniteLoopProtector infinite_protector =
            new InfiniteLoopProtector ( maximum_iterations );
        final TermIterator<VALUE> protected_iterator =
            new TermIterator<VALUE> (
                infinite_iterator,
                infinite_protector );

        return protected_iterator;
    }


    // Every Infinite must implement
    // musaico.foundation.term.multiplicities.Infinite#infiniteIterator()


    /**
     * @see musaico.foundation.term.Term#iterator()
     */
    @Override
    public final TermIterator<VALUE> iterator ()
    {
        // Infinite Terms do not iterate, in order to prevent
        // infinite loops.
        return new TermIterator<VALUE> ();
    }


    /**
     * @see musaico.foundation.term.Multiplicity#orNull()
     */
    @Override
    public final VALUE orNull ()
        throws ReturnNeverNull.Violation
    {
        // Infinite values != exactly one value,
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
        // Expecting a single result.
        final ValueMustBeJust.Violation term_violation =
            ValueMustBeJust.CONTRACT.violation ( this,   // plaintiff
                                                 this ); // evidence
        throw term_violation;
    }


    /**
     * @see musaico.foundation.term.Term#orThrowUnchecked()
     */
    @Override
    public final VALUE orThrowUnchecked ()
        throws UncheckedTermViolation
    {
        // Expecting a single result.
        final ValueMustBeJust.Violation term_violation =
            ValueMustBeJust.CONTRACT.violation ( this,   // plaintiff
                                                 this ); // evidence
        final UncheckedTermViolation unchecked_violation =
            new UncheckedTermViolation ( term_violation );
        throw unchecked_violation;
    }


    /**
     * @see java.lang.Object#toString()
     *
     * Can be overridden by derived classes.
     */
    @Override
    public String toString ()
    {
        return "" + this.type ()
            + " [ " + ClassName.of ( this.getClass () ) + " ] { ... }";
    }
}
