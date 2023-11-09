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
import musaico.foundation.term.Maybe;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;
import musaico.foundation.term.Type;
import musaico.foundation.term.UncheckedTermViolation;

import musaico.foundation.term.contracts.ValueMustBeJust;

import musaico.foundation.term.countable.Many;
import musaico.foundation.term.countable.No;
import musaico.foundation.term.countable.One;

import musaico.foundation.term.iterators.CompositeIterator;
import musaico.foundation.term.iterators.CyclicalIterator;
import musaico.foundation.term.iterators.InfiniteLoopProtector;
import musaico.foundation.term.iterators.IteratorMustHaveNextObject;
import musaico.foundation.term.iterators.TermIterator;
import musaico.foundation.term.iterators.UnchangingIterator;

import musaico.foundation.term.multiplicities.Infinite;
import musaico.foundation.term.multiplicities.OneOrMore;

import musaico.foundation.term.pipeline.AbstractTerm;


/**
 * <p>
 * A cyclical sequence of of elements, which repeats itself infinitely.
 * </p>
 *
 * <p>
 * Unlike One value, a Cyclical term does not have one single element.
 * So operations which expect a single element will fail.
 * The <code> iterator () </code> method also iterates over 0
 * Cyclical elements, in order to prevent infinite loops.
 * Or, by using the <code> cycleIterator () </code> method,
 * operations which do expect multiple values can iterate over
 * one cycle of the Cyclical term.  Or the first One element of
 * the Cyclical can be returned by calling head ().
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
public class Cyclical<VALUE extends Object>
    extends AbstractTerm<VALUE>
    implements Infinite<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( Cyclical.class );


    // The element(s) which come before the cycle starts, if any.
    private final Countable<VALUE> header;

    // One cycle of this infinite, cyclical sequence of elements.
    private final OneOrMore<VALUE> cycle;


    /**
     * <p>
     * Creates a new Cyclical (infinite elements) term.
     * </p>
     *
     * @param type The Type of this Term,, such as a Type&lt;String&gt;
     *              for a Term of Strings, and so on.
     *              Must not be null.
     *
     * @param header The element(s) which come before the cycle starts,
     *               if any.  Must not be null.
     *
     * @param one_cycle The 1 or more (countable) elements
     *                  of one cycle of this infinite Cyclical.
     *                  Must not be null.
     */
    public Cyclical (
            Type<VALUE> type,
            OneOrMore<VALUE> one_cycle
            )
        throws ParametersMustNotBeNull.Violation
    {
        this ( type,                   // type
               new No<VALUE> ( type ), // header
               one_cycle );            // one_cycle
    }


    /**
     * <p>
     * Creates a new Cyclical (infinite elements) term.
     * </p>
     *
     * @param type The Type of this Term,, such as a Type&lt;String&gt;
     *              for a Term of Strings, and so on.
     *              Must not be null.
     *
     * @param header The element(s) which come before the cycle starts,
     *               if any.  Must not be null.
     *
     * @param one_cycle The 1 or more (countable) elements
     *                  of one cycle of this infinite Cyclical.
     *                  Must not be null.
     */
    public Cyclical (
            Type<VALUE> type,
            Countable<VALUE> header,
            OneOrMore<VALUE> one_cycle
            )
        throws ParametersMustNotBeNull.Violation
    {
        super ( type == null // type_pipeline
                    ? null
                    : type.refine () );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               header, one_cycle );

        this.header = header;
        this.cycle = one_cycle;
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
     * @return One cycle of this infinite, cyclical sequence of elements.
     *         Never null.
     */
    public final OneOrMore<VALUE> cycle ()
        throws ReturnNeverNull.Violation
    {
        return this.cycle;
    }


    /**
     * @see musaico.foundation.term.TermPipeline#duplicate()
     */
    @Override
    public final Cyclical<VALUE> duplicate ()
        throws ReturnNeverNull.Violation
    {
        return new Cyclical<VALUE> ( this.type (),
                                     this.header,
                                     this.cycle () );
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

        final Cyclical<?> that = (Cyclical<?>) object;
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

        if ( this.cycle == null )
        {
            if ( that.cycle != null )
            {
                return false;
            }
        }
        else if ( that.cycle == null )
        {
            return  false;
        }
        else if ( ! this.cycle.equals ( that.cycle ) )
        {
            return false;
        }

        if ( this.header == null )
        {
            if ( that.header != null )
            {
                return false;
            }
        }
        else if ( that.header == null )
        {
            return  false;
        }
        else if ( ! this.header.equals ( that.header ) )
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
            + 19 * this.cycle.hashCode ()
            + 11 * this.header.hashCode ()
            + this.type ().hashCode ();
    }


    /**
     * @see musaico.foundation.term.Multiplicity#head()
     */
    @Override
    public final Maybe<VALUE> head ()
        throws IteratorMustHaveNextObject.Violation,
               ReturnNeverNull.Violation
    {
        if ( this.header.hasValue () )
        {
            return this.header.head ();
        }
        else
        {
            return this.cycle.head ();
        }
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

        final long header_length = this.header.length ();
        if ( num_elements <= header_length )
        {
            return this.header.head ( num_elements );
        }
        else if ( header_length == 0L
                  && num_elements <= this.cycle.length () )
        {
            return this.cycle.head ( num_elements );
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
     * @return The element(s) which come before the cycle starts, if any.
     *         Never null.
     */
    public final Countable<VALUE> header ()
        throws ReturnNeverNull.Violation
    {
        return this.header;
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


    /**
     @see musaico.foundation.term.multiplicities.Infinite#infiniteIterator()
    */
    @Override
    public final UnchangingIterator<VALUE> infiniteIterator ()
    {
        if ( this.header.hasValue () )
        {
            return new CompositeIterator<VALUE> (
                           this.header,
                           this.cycle );
        }
        else
        {
            return new CyclicalIterator<VALUE> ( this.cycle );
        }
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
        final String header_string;
        if ( ! this.header.hasValue () )
        {
            header_string = "";
        }
        else
        {
            header_string = " " + this.header + ",";
        }

        return "" + this.type ()
            + " [ " + ClassName.of ( this.getClass () ) + " ] "
            + header_string
            +" "
            + this.cycle
            + ", ...";
    }
}
