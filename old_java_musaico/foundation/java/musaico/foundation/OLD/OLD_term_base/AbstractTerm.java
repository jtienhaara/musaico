!!!;
package musaico.foundation.term.finite;

import java.io.Serializable;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Violation;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.StringRepresentation;

import musaico.foundation.domains.array.ArrayObject;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.term.CheckedTermViolation;
import musaico.foundation.term.Countable;
import musaico.foundation.term.Immutable;
import musaico.foundation.term.Operation;
import musaico.foundation.term.Select;
import musaico.foundation.term.Type;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;
import musaico.foundation.term.ZeroOrOne;

import musaico.foundation.term.contracts.CountableMustIncludeIndices;

import musaico.foundation.term.iterators.TermIterator;


/**
 * <p>
 * The basis for any 1-dimensional Countable implementation which has
 * two or more values (such as Many).
 * </p>
 *
 * <p>
 * Each AbstractTerm implementation must implement either
 * Idempotent or Unidempotent (or a derived interface such as
 * Immutable or Mutable), to aid systems using the terms.
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
 * @see musaico.foundation.term.finite.MODULE#COPYRIGHT
 * @see musaico.foundation.term.finite.MODULE#LICENSE
 */
public abstract class AbstractTerm<VALUE extends Object>
    implements Countable<VALUE>, Term<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( AbstractTerm.class );


    // Enforces parameter obligations and so on for us.
    private final Advocate contracts;

    // The domain(s) of this multiple-element term.
    private final Just<Type<VALUE>> types;

    // The cause of this term, such as an Unidempotent term of which
    // this is an Idempotent snapshot; or a Warning or Partial value;
    // and so on.  This term can be its own cause.
    private final Term<?> cause;


    /**
     * <p>
     * Creates a new AbstractTerm term.
     * </p>
     *
     * @param types The Type(s) of the conditional Term.
     *              Each <code> type.termContract.filter ( this ) </code>
     *              must return FilterState.KEPT.
     *              Must not be null.
     *
     * @param cause The optional cause of this term, such as a
     *              non-Idempotent term of which this is a snapshot,
     *              or a Warning, or a Partial value, and so on.
     *              If null, then this term is its own cause.
     *              Can be null.
     */
    public AbstractTerm (
                             Just<Type<VALUE>> types,
                             Term<?> cause // Can be null.
                             )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               types );

        this.types = types;
        if ( cause == null )
        {
            this.cause = this;
        }
        else
        {
            this.cause = cause;
        }

        this.contracts = new Advocate ( this );

        // The concrete implementation must check the contract(s)
        // and element domain(s) of all type(s).
    }


    /**
     * @return The specified index, converted if offset from LAST/BACKWARD,
     *         and clamped to the specified range.
     */
    public static final long clamp (
                                    long index,
                                    long start,
                                    long end
                                    )
    {
        final long length;
        if ( index == Countable.NONE
             || index == Countable.AFTER_LAST
             || start < 0L
             || end < 0L )
        {
            return Countable.NONE;
        }
        else if ( end >= start )
        {
            length = end - start + 1L;
            if ( index >= Countable.FIRST
                 && index < length )
            {
                return start + index;
            }
            else if ( index >= length )
            {
                return Countable.NONE;
            }
            // Index < -1L, a backward-counting index.
            else if ( ( index - Countable.BACKWARD ) >= length )
            {
                return Countable.NONE;
            }

            final long reversed_index =
                Countable.BACKWARD
                - index
                + end;

            return reversed_index;
        }
        else // start > end )
        {
            // Descending: start, start-1, start-2, ..., end+2, end+1, end.
            length = start - end + 1L;
            if ( index >= Countable.FIRST
                 && index < length )
            {
                return start - index;
            }
            else if ( index >= length )
            {
                return Countable.NONE;
            }
            // Index < -1L, a backward-counting index.
            else if ( ( index - Countable.BACKWARD ) >= length )
            {
                return Countable.NONE;
            }

            final long reversed_index =
                index
                - Countable.BACKWARD
                + end;

            return reversed_index;
        }
    }


    /**
     * @see musaico.foundation.term.Countable#asArrayObject()
     */
    @Override
    public final ArrayObject<VALUE> asArrayObject ()
        throws ReturnNeverNull.Violation
    {
        final VALUE [] elements = this.internalElements ();
        final ArrayObject<VALUE> array_object =
            new ArrayObject<VALUE> ( this.type!!!.elementClass (),
                                     elements );

        return array_object;
    }


    /**
     * @see musaico.foundation.term.Countable#at(long)
     */
    @Override
    public final ZeroOrOne<VALUE> at (
                                      long index
                                      )
        throws ReturnNeverNull.Violation
    {
        final VALUE [] elements = this.internalElements ();
        final long length = (long) elements.length;
        final long start = 0L;
        final long end = length - 1L;
        final long final_index = AbstractTerm.clamp ( index, start, end );
        if ( final_index < start
             || final_index > end )
        {
            final CountableMustIncludeIndices.Violation violation =
                new CountableMustIncludeIndices ( index )
                    .violation ( this,
                                 this );
            final No<VALUE> no_such_element =
                new No<VALUE> ( this.type,
                                this, // cause
                                violation );
            return no_such_element;
        }

        final VALUE element = elements [ (int) final_index ];
        final One<VALUE> one_element =
            new One<VALUE> ( this.type,
                             element );
        return one_element;
    }


    /**
     * @see musaico.foundation.term.Term#await(java.math.BigDecimal)
     */
    @Override
    public final AbstractTerm<VALUE> await (
            BigDecimal timeout_in_seconds
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        // We aren't waiting for a result, we already know what we are.
        return this;
    }


    /**
     * @see musaico.foundation.term.Term#blockingMaxSeconds()
     */
    @Override
    public final BigDecimal blockingMaxSeconds ()
    {
        return BigDecimal.ZERO;
    }


    /**
     * @see musaico.foundation.term.Term#cancel(musaico.foundation.term.TermViolation)
     */
    @Override
    public final AbstractTerm<VALUE> cancel (
                                                 TermViolation violation
                                                 )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        // We aren't waiting for a result, we already know what we are.
        return this;
    }


    /**
     * @see musaico.foundation.term.Term#cause()
     */
    @Override
    public final Term<?> cause ()
        throws ReturnNeverNull.Violation
    {
        return this.cause;
    }


    /**
     * @see musaico.foundation.term.Term#causeRoot()
     */
    @Override
    public final Term<?> causeRoot ()
        throws ReturnNeverNull.Violation
    {
        if ( this.cause == this )
        {
            return this;
        }
        else
        {
            return this.cause.causeRoot ();
        }
    }


    /**
     * @see musaico.foundation.term.Term#consequence()
     */
    @Override
    public final Term<?> consequence ()
        throws ReturnNeverNull.Violation
    {
        return this;
    }


    /**
     * @see musaico.foundation.term.Term#consequenceLeaf()
     */
    @Override
    public final Term<?> consequenceLeaf ()
        throws ReturnNeverNull.Violation
    {
        final Term<?> consequence = this.consequence ();
        if ( consequence == this )
        {
            return this;
        }
        else
        {
            return consequence.consequenceLeaf ();
        }
    }


    /**
     * @see musaico.foundation.term.Term#countable()
     */
    @Override
    public final AbstractTerm<VALUE> countable ()
        throws ReturnNeverNull.Violation
    {
        return this;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
                                 Object obj
                                 )
    {
        if ( obj == this )
        {
            return true;
        }
        else if ( obj == null )
        {
            return false;
        }
        else if ( ! ( obj instanceof AbstractTerm )
                  && ! ( obj instanceof Range ) )
        {
            return false;
        }

        if ( obj instanceof Range )
        {
            // Let Range do all the hard work.
            final Range<?> range = (Range<?>) obj;
            return range.equals ( this );
        }

        AbstractTerm<?> that = (AbstractTerm<?>) obj;
        if ( ! this.type.elementClass ().equals (
                   that.type ().elementClass () ) )
        {
            return false;
        }

        VALUE [] this_elements = this.internalElements ();
        Object [] that_elements = that.internalElements ();

        if ( this_elements.length != that_elements.length )
        {
            return false;
        }

        for ( int e = 0;
              e < this_elements.length && e < that_elements.length;
              e ++ )
        {
            final VALUE this_element = this_elements [ e ];
            final Object that_element = that_elements [ e ];

            if ( this_element == null )
            {
                if ( that_element != null )
                {
                    // Null != any object.
                    return false;
                }
            }
            else if ( that_element == null )
            {
                // Any VALUE != null.
                return false;
            }

            if ( ! this_element.equals ( that_element ) )
            {
                // The current element does not match.
                return false;
            }
        }

        // Same length AbstractTerms, every element is equal.
        return true;
    }


    /**
     * @see musaico.foundation.term.Term#filter()
     */
    @Override
    public final FilterState filter ()
    {
        return FilterState.KEPT;
    }


    /**
     * @see musaico.foundation.term.Term#hasValue()
     */
    @Override
    public final boolean hasValue ()
    {
        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        int hash_code = 0;
        hash_code += this.getClass ().hashCode ();
        hash_code += ( this.type == null ? 0 : this.type.hashCode () );

        return hash_code;
    }


    /**
     * @see musaico.foundation.term.Term#head()
     */
    @Override
    public final ZeroOrOne<VALUE> head ()
        throws ReturnNeverNull.Violation
    {
        final VALUE [] internal_elements = this.internalElements ();
        if ( internal_elements == null
             || internal_elements.length < 1 )
        {
            return new No<VALUE> (
                this.type,
                TermMustBeOne.CONTRACT.violation ( this,
                                                    this ) );
        }

        VALUE first_value = internal_elements [ 0 ];
        return new One<VALUE> ( this.type,
                                first_value );
    }


    /**
     * @see musaico.foundation.term.Term#head(long)
     */
    @Override
    public final Countable<VALUE> head (
                                        long num_elements
                                        )
        throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( Parameter1.MustBeGreaterThanOrEqualToZero.CONTRACT,
                               num_elements );

        if ( num_elements == 0L )
        {
            // Create No value with default TermMustBeOne violation.
            final No<VALUE> empty_head =
                new No<VALUE> ( this.type, // type
                                this );    // cause
            return empty_head;
        }
        else if ( num_elements == 1L )
        {
            return this.head ();
        }

        final VALUE [] elements = this.internalElements ();
        final long length = (long) elements.length;
        if ( num_elements >= length )
        {
            // We have either exactly enough or not enough elements.
            // Might as well just return this term as-is.
            return this;
        }

        final Range<VALUE> head =
            new Range<VALUE> ( this.type,                   // type
                               this,                        // cause
                               elements,                    // elements
                               0L,                          // start
                               num_elements - 1L );         // end
        return head;
    }


    /**
     * @see musaico.foundation.term.Term#indefiniteIterator(long)
     */
    @Override
    public final Iterator<VALUE> indefiniteIterator (
                                                     long maximum_iterations
                                                     )
        throws Parameter1.MustBeGreaterThanZero.Violation
    {
        return this.iterator ();
    }


    /**
     * @return The elements of this AbstractTerm, used only
     *         internally by AbstractTerm when determining length
     *         and so on.  Often the actual underpinning of
     *         the AbstractTerm's content is returned,
     *         so the caller must not modify the elements in any way.
     *         For all AbstractTerm concrete classes in the packages
     *         musaico.foundation.term.*, once set, the contents
     *         of the elements returned may not ever change.
     *         However classes from outside this package may do away
     *         with this restriction, so that modifications to the
     *         original Term can occur after an unchanging snapshot
     *         of the elements has been returned.
     *         The elements returned will never be modified.  So if
     *         this AbstractTerm term is Idempotent, its element(s)
     *         can be returned as-is.  But if it is not Idempotent it
     *         must create a new array of its elements, a snapshot of
     *         the elements which will never change.
     *         Can only ever be null during the constructor (because
     *         AbstractTerm calls toString () during its constructor,
     *         before the derived class has had a chance to set up its
     *         elements; the only method affected by this null during
     *         construction is AbstractTerm.toString ()).
     *         After construction is complete, never null.
     *         After construction is complete, always contains at least
     *         one element.  Never contains any null elements.
     */
    protected abstract VALUE [] internalElements ();


    /**
     * @see musaico.foundation.term.Term#iterator()
     */
    @Override
    public Iterator<VALUE> iterator ()
    {
        return new TermIterator<VALUE> ( this.internalElements () );
    }


    /**
     * @see musaico.foundation.term.Countable#length()
     */
    @Override
    public final long length ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        return (long) this.internalElements ().length;
    }


    /**
     * @see musaico.foundation.term.Term#orDefault(java.lang.Object)
     */
    @Override
    public final VALUE orDefault (
                                  VALUE default_value
                                  )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               default_value );

        // Expecting a single result.  return the default.
        return default_value;
    }


    /**
     * @see musaico.foundation.term.Term#orNone()
     */
    @Override
    public final VALUE orNone ()
    {
        // Expecting a single result.  Return the none object.
        return this.type.none ();
    }


    /**
     * @see musaico.foundation.term.Term#orNull()
     */
    @Override
    public final VALUE orNull ()
    {
        // Expecting a single result.  Return null.
        return null;
    }


    /**
     * @see musaico.foundation.term.Term#orPartial()
     */
    @Override
    public final AbstractTerm<VALUE> orPartial ()
    {
        return this;
    }


    /**
     * @see musaico.foundation.term.Term#orThrow(java.lang.Class)
     */
    @Override
    public final <EXCEPTION extends Exception>
        VALUE orThrow (
                       Class<EXCEPTION> exception_class
                       )
        throws EXCEPTION,
               ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               exception_class );

        Exception cause_exception = null;
        EXCEPTION exception = null;
        try
        {
            // First try single arg MyException ( String message ).
            final Constructor<EXCEPTION> constructor =
                exception_class.getConstructor ( String.class );
            exception =
                constructor.newInstance ( this.termViolation ().getMessage () );
        }
        catch ( Exception e )
        {
            cause_exception = e;
            try
            {
                // Now try 0 args constructor.
                final Constructor<EXCEPTION> constructor =
                    exception_class.getConstructor ();
                exception = constructor.newInstance ();
            }
            catch ( Exception e2 )
            {
                exception = null;
            }
        }

        if ( exception == null )
        {
            final ReturnNeverNull.Violation violation =
                ReturnNeverNull.CONTRACT.violation ( this,
                                                     "Could not instantiate exception class " + exception_class.getName () );
            if ( cause_exception != null )
            {
                violation.initCause ( cause_exception );
            }

            throw violation;
        }

        exception.initCause ( this.termViolation () );

        throw exception;
    }


    /**
     * @see musaico.foundation.term.Term#orThrow(java.lang.Exception)
     */
    @Override
    public final <EXCEPTION extends Exception>
        VALUE orThrow (
                       EXCEPTION exception
                       )
        throws EXCEPTION,
               ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               exception );

        if ( exception.getCause () == null )
        {
            exception.initCause ( this.termViolation () );
        }

        // Expecting a single result.  Throw the specified exception.
        throw exception;
    }


    /**
     * @see musaico.foundation.term.Term#orThrowChecked()
     */
    @Override
    public final VALUE orThrowChecked ()
        throws CheckedTermViolation
    {
        // Expecting a single result.  Throw the single value violation.
        throw new CheckedTermViolation ( this.termViolation () );
    }


    /**
     * @see musaico.foundation.term.Term#orThrowUnchecked()
     */
    @Override
    public final VALUE orThrowUnchecked ()
        throws TermViolation
    {
        // Expecting a single result.
        throw this.termViolation ();
    }


    /**
     * @see musaico.foundation.term.Term#orViolation(musaico.foundation.contract.Contract,java.lang.Object, java.lang.Object)
     */
    @Override
    public final <EVIDENCE extends Object, VIOLATION extends Throwable & Violation>
        VALUE orViolation (
                           Contract<EVIDENCE, VIOLATION> contract,
                           Object plaintiff,
                           EVIDENCE evidence
                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation,
               VIOLATION
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               contract, plaintiff, evidence );

        final VIOLATION violation =
            contract.violation ( plaintiff,
                                 evidence );
        violation.initCause ( this.termViolation () );
        throw violation;
    }


    /**
     * @see musaico.foundation.term.Term#pipe(musaico.foundation.term.Operation)
     */
    @Override
    public final <OUTPUT extends Object>
        Term<OUTPUT> pipe (
                            Operation<VALUE, OUTPUT> operation
                            )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               operation );

        return operation.apply ( this );
    }


    /**
     * @see musaico.foundation.term.Countable#range(long, long)
     */
    @Override
    public final Immutable<VALUE> range (
                                         long start,
                                         long end
                                         )
        throws ReturnNeverNull.Violation
    {
        final VALUE [] elements = this.internalElements ();
        final long length = (long) elements.length;
        final long first = 0L;
        final long last = length - 1L;
        final long final_start = AbstractTerm.clamp ( start, first, last );
        final long final_end = AbstractTerm.clamp ( end, first, last );
        if ( final_start < first
             || final_start > last
             || final_end < first
             || final_end > last )
        {
            final CountableMustIncludeIndices.Violation violation =
                new CountableMustIncludeIndices ( start,
                                                  end )
                    .violation ( this,
                                 this );
            final No<VALUE> no_such_range =
                new No<VALUE> ( this.type,
                                this, // cause
                                violation );
            return no_such_range;
        }

        if ( final_start == final_end )
        {
            // Range of 1 element.
            final VALUE element = elements [ (int) final_start ];
            final One<VALUE> one_element =
                new One<VALUE> ( this.type,
                                 element );
            return one_element;
        }

        // More than one element in the range.
        return new Range<VALUE> ( this.type,
                                  this, // cause
                                  elements,
                                  final_start,
                                  final_end );
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        final VALUE [] elements = this.internalElements ();
        if ( elements == null ) // During constructor
        {
            return "{ ... }";
        }

        final String as_string =
            StringRepresentation.of (
                elements,
                0 ); // Let the caller limit String length, if they choose.

        return as_string;
    }


    /**
     * @see musaico.foundation.term.Term#types()
     */
    @Override
    public final Just<Type<VALUE>> types ()
    {
        return this.types;
    }


    // Every AbstractTerm must implement NotOne.termViolation ().
}
