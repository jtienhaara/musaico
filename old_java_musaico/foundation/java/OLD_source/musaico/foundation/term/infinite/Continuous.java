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

import musaico.foundation.domains.comparability.BoundedDomain;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Maybe;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;
import musaico.foundation.term.Type;
import musaico.foundation.term.UncheckedTermViolation;

import musaico.foundation.term.contracts.ValueMustBeJust;

import musaico.foundation.term.countable.Many;
import musaico.foundation.term.countable.No;
import musaico.foundation.term.countable.One;

import musaico.foundation.term.iterators.IteratorMustHaveNextObject;
import musaico.foundation.term.iterators.TermIterator;

import musaico.foundation.term.multiplicities.Infinite;

import musaico.foundation.term.pipeline.AbstractTerm;


/**
 * <p>
 * An infinite Term whose values are not discrete, such as
 * the real numbers between 0 and 1, or the wavelengths of visible
 * light, and so on.
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
 * <p>
 * In Java every Multiplicity must implement equals (), hashCode ()
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
public class Continuous<VALUE extends Object>
    extends AbstractTerm<VALUE>
    implements Infinite<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( Continuous.class );


    // The bounds of this Continuous term, such as [ 0, 1 ] or ( 0, 1 )
    // or >= 0 and so on.
    private final BoundedDomain<VALUE> bounds;


    /**
     * <p>
     * Creates a new Continuous (non-discrete) infinite term.
     * </p>
     *
     * @param type The Type of this Term,, such as a Type&lt;BigDecimal&gt;
     *              for a Term of real numbers, and so on.
     *              Must not be null.
     *
     * @param bounds The upper and lower bounds of this Term.  Can be
     *               closed, open, one-sided, and so on: [ 0, 1 ]
     *               or ( -100, 100 ) or &gt; 0.0 or &lt;= 1000
     *               or [ 1, 2 ) or ( 0, 100 ], and so on.
     *               Must not be null.
     */
    public Continuous (
            Type<VALUE> type,
            BoundedDomain<VALUE> bounds
            )
        throws ParametersMustNotBeNull.Violation
    {
        super ( type == null // type_pipeline
                    ? null
                    : type.refine () );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               bounds );

        this.bounds = bounds;
    }


    /**
     * @return The bounds of this Continuous term (if any).
     *         Can be closed bounds [ start, end ]
     *         ( start, end ) or combinations, such as
     *         [ start, end ) or ( start, end ] or &gt; start
     *         or &gt;= start or &t; end or &lt;= end, and so on.
     *         Never null.
     */
    public final BoundedDomain<VALUE> bounds ()
        throws ReturnNeverNull.Violation
    {
        return this.bounds;
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


    /**
     * @see musaico.foundation.term.TermPipeline#duplicate()
     */
    @Override
    public final Continuous<VALUE> duplicate ()
        throws ReturnNeverNull.Violation
    {
        return new Continuous<VALUE> ( this.type (),
                                       this.bounds );
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
            Object object
            )
    {
        if ( object == null )
        {
            return false;
        }
        else if ( object == this )
        {
            return true;
        }
        else if ( this.getClass () != object.getClass () )
        {
            return false;
        }

        final Continuous<?> that = (Continuous<?>) object;
        final Type<VALUE> this_type = this.type ();
        final Type<?> that_type = that.type ();
        if ( this_type == null )
        {
            if ( that_type != null )
            {
                return false;
            }
        }
        else if ( that_type == null )
        {
            return false;
        }
        else if ( ! this_type.equals ( that_type ) )
        {
            return false;
        }

        if ( this.bounds == null )
        {
            if ( that.bounds != null )
            {
                return false;
            }
        }
        else if ( that.bounds == null )
        {
            return  false;
        }
        else if ( ! this.bounds.equals ( that.bounds ) )
        {
            return false;
        }

        return true;
    }


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
     */
    @Override
    public final int hashCode ()
    {
        return 31 * this.getClass ().getName ().hashCode ()
            + 11 * this.bounds.hashCode ()
            + this.type ().hashCode ();
    }


    /**
     * @see musaico.foundation.term.Multiplicity#head()
     */
    @Override
    public final Maybe<VALUE> head ()
        throws ReturnNeverNull.Violation
    {
        if ( this.bounds.minimumEndPoint () == BoundedDomain.EndPoint.CLOSED )
        {
            final One<VALUE> one_head =
                new One<VALUE> ( this.type (),             // type
                                 this.bounds.minimum () ); // one_value
            return one_head;
        }

        // Otherwise we have no way of determining the head, since
        // either the minimum is open or there is no minimum.
        final No<VALUE> no_head = new No<VALUE> ( this.type () );
        return no_head;
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

        // Otherwise we have no way of determining the head, since
        // we have been asked for more than one non-discrete "elements".
        final No<VALUE> no_head = new No<VALUE> ( this.type () );
        return no_head;
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

        // Continuous Terms can't even iterate indefinitely, since they
        // have no discrete elements.
        return new TermIterator<VALUE> ();
    }


    /**
     @see musaico.foundation.term.multiplicities.Infinite#infiniteIterator()
    */
    @Override
    public final TermIterator<VALUE> infiniteIterator ()
    {
        // Continuous Terms can't even iterate infinitely, since they
        // have no discrete elements.
        return new TermIterator<VALUE> ();
    }


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
            + " { "
            + ClassName.of ( this.getClass () )
            + " "
            + this.bounds
            + " }";
    }
}
