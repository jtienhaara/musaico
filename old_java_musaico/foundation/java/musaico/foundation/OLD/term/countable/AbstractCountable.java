package musaico.foundation.term.countable;

import java.io.Serializable;

import java.lang.reflect.Array;

import java.math.BigDecimal;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Contract;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.Parameter3;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;
import musaico.foundation.domains.StringRepresentation;

import musaico.foundation.domains.array.ArrayObject;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.order.Order;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Maybe;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;
import musaico.foundation.term.Type;
import musaico.foundation.term.UncheckedTermViolation;

import musaico.foundation.term.contracts.TypeMustBeInstanceOfClass;
import musaico.foundation.term.contracts.ValueMustBeJust;

import musaico.foundation.term.iterators.ArrayIterator;
import musaico.foundation.term.iterators.InfiniteLoopProtector;

import musaico.foundation.term.pipeline.AbstractTerm;


/**
 * <p>
 * The basis for Countable terms, including One, No, Many,
 * Some, and so on.
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
 * @see musaico.foundation.term.countable.MODULE#COPYRIGHT
 * @see musaico.foundation.term.countable.MODULE#LICENSE
 */
public abstract class AbstractCountable<VALUE extends Object>
    extends AbstractTerm<VALUE>
    implements Countable<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( AbstractCountable.class );


    // The elements of this value.  Can be ImmutableElements,
    // MutableElements, VariableLengthElements, and so on.
    private final Elements<VALUE> elements;


    /**
     * <p>
     * Creates a new AbstractCountable.
     * </p>
     *
     * @param type The Type of this Term, such as a Type&lt;String&gt;
     *              for a Term of Strings, and so on.
     *              Must not be null.
     *
     * @param one_value The single element comprising this value.
     *                  Depending on the Type, this element will
     *                  end up being immutable or mutable.  Depending
     *                  on this.isFixedLength (), a mutable element
     *                  might also end up being variable length.  And so on.
     *                  Must not be null.
     */
    public AbstractCountable (
            Type<VALUE> type,
            VALUE one_value
            )
        throws ParametersMustNotBeNull.Violation
    {
        this ( type,                           // type
               AbstractCountable.createArray ( // array
                   one_value ) );
    }


    /**
     * <p>
     * Creates a new AbstractCountable.
     * </p>
     *
     * @param type The Type of this Term, such as a Type&lt;String&gt;
     *              for a Term of Strings, and so on.
     *              Must not be null.
     *
     * @param iterable The Iterable value (such as a Term
     *                 or a Collection and so on) of 0 or more elements
     *                 comprising this value.  Depending on
     *                 the Type, these elements will end up
     *                 being immutable or mutable.
     *                 Depending on this.isFixedLength (), mutable elements
     *                 might also end up being variable length.  And so on.
     *                 Must not be null.  Must not contain any null elements.
     */
    public AbstractCountable (
            Type<VALUE> type,
            Iterable<VALUE> iterable
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation
    {
        this ( type,                           // type
               AbstractCountable.createArray ( // array
                   iterable ) );
    }


    /**
     * <p>
     * Creates a new AbstractCountable.
     * </p>
     *
     * @param type The Type of this Term, such as a Type&lt;String&gt;
     *              for a Term of Strings, and so on.
     *              Must not be null.
     *
     * @param array The array of 0 or more elements comprising this value.
     *              Depending on the Type, these elements will
     *              end up being immutable or mutable.  Depending
     *              on this.isFixedLength (), mutable elements might also
     *              end up being variable length.  And so on.
     *              Must not be null.  Must not contain any null elements.
     */
    @SuppressWarnings({"unchecked","varargs"})//Possible heap pollution varargs
    @SafeVarargs
    public AbstractCountable (
            Type<VALUE> type,
            VALUE ... array
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation
    {
        this ( type,            // type
               type == null     // elements
                   ? null
                   // Throws ParametersMustNotBeNull.Violation,
                   //        Parameter2.MustContainNoNulls.Violation:
                   : new ImmutableElements<VALUE> (
                         type.elementClass (),
                         array )
               );
    }


    /**
     * <p>
     * Creates a new AbstractCountable.
     * </p>
     *
     * @param type The Type of this Term, such as a Type&lt;String&gt;
     *              for a Term of Strings, and so on.
     *              Must not be null.
     *
     * @param elements The 0 or more Elements comprising this value.
     *              Depending on the Type, these elements will
     *              end up being recreated as immutable or mutable.  Depending
     *              on this.isFixedLength (), mutable elements might also
     *              end up being variable length.  And so on.
     *              Must not be null.
     */
    @SuppressWarnings("unchecked") // Cast ElementsType.class - Cl<Ty<EL>>,
        // Unchecked varargs to TypePipeline.where ().
    public AbstractCountable (
            Type<VALUE> type,
            Elements<VALUE> elements
            )
        throws ParametersMustNotBeNull.Violation
    {
        // Pass a TypePipeline to our parent, refined from the input Type
        // in order to allow the Countable elements.
        // AbstractTerm takes care of refining the Type further, for example
        // to allow Terms of this Class.
        super ( type == null
                    ? null
                    : elements == null
                          ? type.refine ()
                          : type.refine ()
                                .allowElements (
                                    elements.array () ) );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               elements );

        final boolean is_fixed_length = this.isFixedLengthInit ();

        final Filter<Type<VALUE>> is_elemental =
            // Cast F<Type<?>> - F<Type<V>>, we'll only ever pass Type<V>'s.
            (Filter<Type<VALUE>>)
            // More contortions to get generics to do anything useful...
            new TypeMustBeInstanceOfClass ( (Class<Type<VALUE>>)
                                            ( (Class<?>)
                                              ElementsType.class ) );
        for ( Type<VALUE> componenttype : this.type ().refine ()
                                                      .where ( is_elemental )
                                                      .end ()
                                                      .output () ) //Term<Type>
        {
            ElementsType<VALUE> elements_type =
                (ElementsType<VALUE>) type;
            elements = elements_type.buildElements (
                           elements,
                           is_fixed_length );
        }

        this.elements = elements;
    }


    @SuppressWarnings("unchecked") // Cast Array.newInstance(...) - E [],
        // cast element class to Class<ELEMENT>.
    private static <ELEMENT extends Object>
        ELEMENT [] createArray (
            ELEMENT one_value
            )
        throws ConcurrentModificationException
    {
        if ( one_value == null )
        {
            return null;
        }

        final Class<ELEMENT> element_class = (Class<ELEMENT>)
            one_value.getClass ();
        final ELEMENT [] array = (ELEMENT [])
            Array.newInstance ( element_class, 1 );
        array [ 0 ] = one_value;
        return array;
    }


    @SuppressWarnings("unchecked") // Cast Array.newInstance(...) - E [],
        // cast ArrayObject.guessElementClass(...) to Class<ELEMENT>.
    private static <ELEMENT extends Object>
        ELEMENT [] createArray (
            Iterable<ELEMENT> iterable
            )
        throws ConcurrentModificationException
    {
        if ( iterable == null )
        {
            return null;
        }
        else if ( iterable instanceof AbstractCountable )
        {
            final AbstractCountable<ELEMENT> countable =
                (AbstractCountable<ELEMENT>) iterable;
            final ELEMENT [] original_array = countable.elements.array ();
            final long start = countable.elements.start ();
            final long end = countable.elements.end ();
            if ( start == 0L
                 && end == ( (long) original_array.length - 1L ) )
            {
                return original_array;
            }

            final long length;
            final int direction;
            if ( start <= end )
            {
                length = end - start + 1L;
                direction = 1;
            }
            else
            {
                length = start - end + 1L;
                direction = -1;
            }

            final Class<ELEMENT> element_class =
                countable.elements.elementClass ();
            final ELEMENT [] array = (ELEMENT [])
                Array.newInstance ( element_class, (int) length );
            int index = (int) start;
            for ( int e = 0; e < (int) length; e ++ )
            {
                array [ e ] = original_array [ index ];
                index += direction;
            }

            return array;
        }
        else if ( iterable instanceof Countable )
        {
            final Countable<ELEMENT> countable =
                (Countable<ELEMENT>) iterable;
            final ELEMENT [] array = countable.cast ().toArray ();
            return array;
        }
        else if ( iterable instanceof Collection )
        {
            // Can conceivably fail with ConcurrentModificationException:
            final Collection<ELEMENT> collection =
                (Collection<ELEMENT>) iterable;
            if ( collection.size () == 0 )
            {
                return (ELEMENT []) new Object [ 0 ];
            }

            Class<ELEMENT> element_class = (Class<ELEMENT>)
                ArrayObject.guessElementClass ( collection );
            final ELEMENT [] template = (ELEMENT [])
                Array.newInstance ( element_class, collection.size () );
            final ELEMENT [] array = collection.toArray ( template );
            return array;
        }

        // Ok let's put on our boots and wade through the muck...
        // Should never throw ClassCastException, since we know the
        // Iterable's elements are all ELEMENTs (although Java
        // has, in the past, allowed Iterable<FOO> to contain
        // non-FOO elements...  Thanks for that...  and thanks
        // even more to the wonderfully anarchical developers who
        // deliberately took advantage of it, causing mysterious
        // code implosions when Java locked the back door to generics...).
        final Class<ELEMENT> element_class = (Class<ELEMENT>)
            ArrayObject.guessElementClass ( iterable );
        final ArrayObject<ELEMENT> array_object =
            new ArrayObject<ELEMENT> ( element_class,
                                       iterable );
        final ELEMENT [] array = array_object.array ();
        return array;
    }


    /**
     * @return This AbstractCountable's Elements.  Only for use by
     *         derived classes; should never be shared with the outside world.
     *         Never null.
     */
    protected final Elements<VALUE> elements ()
        throws ReturnNeverNull.Violation
    {
        return this.elements;
    }


    /**
     * @see musaico.foundation.term.Countable#at(long)
     */
    @Override
    @SuppressWarnings("try") // Lock.close() somehow throws InterruptedEx.
    public final Maybe<VALUE> at (
            long index
            )
        throws ReturnNeverNull.Violation
    {
        final VALUE element;
        try
        {
            try ( final AutoCloseable lock = this.elements.lock () )
            {
                final long final_index =
                    this.elements.clamp ( index,            // index
                                          0L,               // start
                                          Long.MAX_VALUE ); // end
                if ( final_index < 0L )
                {
                    element = null;
                }
                else
                {
                    final VALUE [] elements = this.elements.array ();
                    element = elements [ (int) final_index ];
                }
            }
        }
        catch ( Exception e )
        {
            throw ReturnNeverNull.CONTRACT.violation (
                this, // plaintiff
                null, // evidence
                e );  // cause
        }

        final Maybe<VALUE> zero_or_one_element;
        if ( element == null )
        {
            zero_or_one_element =
                new No<VALUE> ( this.type () );
        }
        else
        {
            zero_or_one_element =
                new One<VALUE> ( this.type (),
                                 element );
        }

        return zero_or_one_element;
    }


    /**
     * @return The specified index, converted if offset from LAST/BACKWARD,
     *         and clamped to the specified range.
     */
    public static final long clamp (
                                    long index,
                                    long start,
                                    long end,
                                    long out_of_bounds
                                    )
    {
        if ( end >= out_of_bounds )
        {
            end = out_of_bounds - 1L;
        }

        if ( index == Countable.AFTER_LAST
             || start < 0L
             || end < 0L
             || start >= out_of_bounds )
        {
            return Countable.NONE;
        }

        final long length;
        if ( end >= start )
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
     * @see musaico.foundation.term.Countable#clamp(long)
     */
    @Override
    public final long clamp (
            long index
            )
        throws Return.AlwaysGreaterThanOrEqualToNegativeOne.Violation
    {
        return this.elements.clamp ( index,            // index
                                     0L,               // start
                                     Long.MAX_VALUE ); // end
    }


    /**
     * @see musaico.foundation.term.Multiplicity#countable()
    */
    @Override
    public final Countable<VALUE> countable ()
        throws ReturnNeverNull.Violation
    {
        return this;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
            Object object
            )
    {
        if ( object == this )
        {
            return true;
        }
        else if ( object == null )
        {
            return false;
        }
        else if ( this.getClass () != object.getClass () )
        {
            return false;
        }

        AbstractCountable<?> that = (AbstractCountable<?>) object;

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

        //
        // We just worry about whether the elements are the same.
        final VALUE [] this_elements = this.elements.array ();
        final Object [] that_elements = that.elements.array ();

        final long this_start = this.elements.start ();
        final long that_start = that.elements.start ();

        final long this_end = this.elements.end ();
        final long that_end = that.elements.end ();

        final long this_length;
        final int this_direction;
        if ( this_start < this_end )
        {
            this_length = this_end - this_start + 1L;
            this_direction = 1;
        }
        else
        {
            this_length = this_start - this_end + 1L;
            this_direction = -1;
        }
        final long that_length;
        final int that_direction;
        if ( that_start < that_end )
        {
            that_length = that_end - that_start + 1L;
            that_direction = 1;
        }
        else
        {
            that_length = that_start - that_end + 1L;
            that_direction = -1;
        }

        if ( this_length != that_length )
        {
            return false;
        }

        int this_index = (int) this_start;
        int that_index = (int) that_start;
        for ( int e = 0;
              e < (int) this_length && e < (int) that_length;
              e ++ )
        {
            final VALUE this_element = this_elements [ this_index ];
            final Object that_element = that_elements [ that_index ];

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
            else if ( ! this_element.equals ( that_element ) )
            {
                // The current element does not match.
                return false;
            }

            this_index += this_direction;
            that_index += that_direction;
        }

        // Same types, same lengths, and every element is equal.
        return true;
    }


    /**
     * @see musaico.foundation.term.Multiplicity#filterState()
     */
    @Override
    public final FilterState filterState ()
         throws ReturnNeverNull.Violation
     {
         if ( this.elements.length () == 0L )
         {
             // 0 elements.
             return FilterState.DISCARDED;
         }
         else
         {
             // At least 1 element.
             return FilterState.KEPT;
         }
     }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return 31 * this.type ().hashCode ()
            + 7 * this.getClass ().getName ().hashCode ()
            + this.elements.elementClass ().getName ().hashCode ();
    }


    /**
     * @see musaico.foundation.term.Multiplicity#head()
     */
    @Override
    public final Maybe<VALUE> head ()
        throws ReturnNeverNull.Violation
    {
        return this.at ( 0L );
    }


    /**
     *@see musaico.foundation.term.Multiplicity#head(long)
     */
    @Override
    @SuppressWarnings({"unchecked", // Cast Array.newInstance () - VALUE [].
                "try"}) // Lock.close() somehow throws InterruptedEx.
    public final Countable<VALUE> head (
            long num_elements
            )
        throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
               ReturnNeverNull.Violation
    {
        if ( num_elements <= 0L
             || num_elements > (long) Integer.MAX_VALUE )
        {
            return new No<VALUE> ( this.type () );
        }

        final VALUE [] range_array;
        try
        {
            try ( final AutoCloseable lock = this.elements.lock () )
            {
                final long length = this.elements.length ();
                if ( length < num_elements )
                {
                    return new No<VALUE> ( this.type () );
                }

                range_array = (VALUE [])
                    Array.newInstance ( this.elements.elementClass (),
                                        (int) num_elements );

                final int direction = (int) this.elements.direction ();
                final VALUE [] elements = this.elements.array ();
                int index = (int) this.elements.start ();
                for ( int e = 0; e < range_array.length; e ++ )
                {
                    range_array [ e ] = elements [ index ];
                    index += direction;
                }
            }
        }
        catch ( Exception e )
        {
            throw ReturnNeverNull.CONTRACT.violation (
                this, // plaintiff
                null, // evidence
                e );  // cause
        }

        final Countable<VALUE> head;
        if ( range_array.length == 1
             && this.elements.isFixedLength () )
        {
            head = new One<VALUE> ( this.type (),
                                    range_array [ 0 ] );
        }
        else
        {
            head = new Some<VALUE> ( this.type (),
                                     range_array );
        }

        return head;
    }


    /**
     * @see musaico.foundation.term.Multiplicity#indefiniteIterator(long)
     */
    @Override
    public final Iterator<VALUE> indefiniteIterator (
            long maximum_iterations
            )
        throws Parameter1.MustBeGreaterThanZero.Violation,
               ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        final InfiniteLoopProtector infinite_loop_protector =
            new InfiniteLoopProtector ( maximum_iterations );
        final VALUE [] elements = this.elements.array ();
        final int start = (int) this.elements.start ();
        final int end = (int) this.elements.end ();
        final ArrayIterator<VALUE> iterator =
            new ArrayIterator<VALUE> ( infinite_loop_protector,
                                       elements,
                                       start,
                                       end );
        return iterator;
    }


    /**
     * @return True if this Term is always exactly the same
     *         number of elements (such as One or No);
     *         false if its length can be changed whenever it has
     *         a Type that declares its Terms to be Mutable.
     */
    protected abstract boolean isFixedLengthInit ();


    /**
     * @return True if this Term is always exactly
     *         the same length; false if its length can
     *         be changed whenever it has a Type
     *         that declares its Terms to be Mutable.
     */
    public final boolean isFixedLength ()
    {
        return this.elements.isFixedLength ();
    }


    /**
     *@see java.lang.Iterable#iterator()
     */
    @Override
    public final Iterator<VALUE> iterator ()
    {
        final VALUE [] elements = this.elements.array ();
        final int start = (int) this.elements.start ();
        final int end = (int) this.elements.end ();
        final ArrayIterator<VALUE> iterator =
            new ArrayIterator<VALUE> ( elements,
                                       start,
                                       end );
        return iterator;
    }


    /**
     * @see musaico.foundation.term.Countable#length()
     */
    @Override
    public final long length ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        return this.elements.length ();
    }


    /**
     * @see musaico.foundation.term.Multiplicity#orNull()
     */
    @Override
    public final VALUE orNull ()
        throws ReturnNeverNull.Violation
    {
        final long start = this.elements.start ();
        final long end = this.elements.end ();
        if ( start != end )
        {
            // Not exactly 1 element.
            return null;
        }

        final VALUE [] elements = this.elements.array ();
        return elements [ (int) start ];
    }


    /**
     * @see musaico.foundation.term.Multiplicity#orThrowChecked()
     */
    @Override
    public final VALUE orThrowChecked ()
        throws ValueMustBeJust.Violation,
               ReturnNeverNull.Violation
    {
        final VALUE element = this.orNull ();
        if ( element == null )
        {
            final ValueMustBeJust.Violation term_violation =
                ValueMustBeJust.CONTRACT.violation ( this,   // plaintiff
                                                     this ); // evidence
            throw term_violation;
        }
        else
        {
            return element;
        }
    }


    /**
     * @see musaico.foundation.term.Multiplicity#orThrowUnchecked()
     */
    @Override
    public final VALUE orThrowUnchecked ()
        throws UncheckedTermViolation,
               ReturnNeverNull.Violation
    {
        final VALUE element = this.orNull ();
        if ( element == null )
        {
            final ValueMustBeJust.Violation term_violation =
                ValueMustBeJust.CONTRACT.violation ( this,   // plaintiff
                                                     this ); // evidence
            final UncheckedTermViolation unchecked_violation =
                new UncheckedTermViolation ( term_violation );
            throw unchecked_violation;
        }
        else
        {
            return element;
        }
    }


    /**
     *@see musaico.foundation.term.Countable#range(long, long)
     */
    @Override
    @SuppressWarnings({"unchecked", // Cast Array.newInstance () - VALUE [].
                "try"}) // Lock.close() somehow throws InterruptedEx.
    public final Countable<VALUE> range (
            long start,
            long end
            )
        throws ReturnNeverNull.Violation
    {
        final VALUE [] range_array;
        try
        {
            try ( final AutoCloseable lock = this.elements.lock () )
            {
                final long clamped_start =
                    this.elements.clamp ( start,            // index
                                          0L,               // start
                                          Long.MAX_VALUE ); // end
                if ( clamped_start < 0L )
                {
                    return new No<VALUE> ( this.type () );
                }

                final long clamped_end =
                    this.elements.clamp ( end,              // index
                                          0L,               // start
                                          Long.MAX_VALUE ); // end
                if ( clamped_end < 0L )
                {
                    return new No<VALUE> ( this.type () );
                }

                final long num_elements;
                final int direction;
                if ( clamped_start <= clamped_end )
                {
                    num_elements = clamped_end - clamped_start + 1;
                    direction = 1;
                }
                else
                {
                    num_elements = clamped_start - clamped_end + 1;
                    direction = -1;
                }

                if ( num_elements > (long) Integer.MAX_VALUE )
                {
                    return new No<VALUE> ( this.type () );
                }

                range_array = (VALUE [])
                    Array.newInstance ( this.elements.elementClass (),
                                        (int) num_elements );

                final VALUE [] elements = this.elements.array ();
                int index = (int) clamped_start;
                for ( int e = 0; e < range_array.length; e ++ )
                {
                    range_array [ e ] = elements [ index ];
                    index += direction;
                }
            }
        }
        catch ( Exception e )
        {
            throw ReturnNeverNull.CONTRACT.violation (
                this, // plaintiff
                null, // evidence
                e );  // cause
        }

        final Countable<VALUE> range;
        if ( range_array.length == 1
             && this.elements.isFixedLength () )
        {
            range = new One<VALUE> ( this.type (),
                                     range_array [ 0 ] );
        }
        else
        {
            range = new Some<VALUE> ( this.type (),
                                      range_array );
        }

        return range;
    }


    /**
     * @see musaico.foundation.term.Countable#read(java.lang.Object[], int, int);
     */
    @Override
    @SuppressWarnings("try") // Lock.close() somehow throws InterruptedEx.
    public final int read (
            VALUE [] array,
            int offset,
            int length,
            long first_element_index
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustBeGreaterThanOrEqualToZero.Violation,
               Parameter3.MustBeGreaterThanOrEqualToZero.Violation,
               Return.AlwaysGreaterThanOrEqualToNegativeOne.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               (Object) array );
        classContracts.check ( Parameter2.MustBeGreaterThanOrEqualToZero.CONTRACT,
                               offset );
        classContracts.check ( Parameter3.MustBeGreaterThanOrEqualToZero.CONTRACT,
                               length );

        int num_elements_read = length;
        try
        {
            try ( final AutoCloseable lock = this.elements.lock () )
            {
                final long clamped_start =
                    this.elements.clamp ( first_element_index, // index
                                          0L,                  // start
                                          Long.MAX_VALUE );    // end
                if ( clamped_start < 0L )
                {
                    return -1;
                }

                final int direction = (int) this.elements.direction ();

                final VALUE [] elements = this.elements.array ();
                int index = (int) clamped_start;
                for ( int e = 0; e < length; e ++ )
                {
                    if ( index >= elements.length )
                    {
                        num_elements_read = e;
                        break;
                    }

                    array [ e + offset ] = elements [ index ];
                    index += direction;
                }
            }
        }
        catch ( Exception e )
        {
            throw Return.AlwaysGreaterThanOrEqualToNegativeOne.CONTRACT.violation (
                this, // plaintiff
                null, // evidence
                e );  // cause
        }

        return num_elements_read;
    }


    /**
     * @see java.lang.Object#toString()
     *
     * Can be overridden by derived classes.
     */
    @Override
    public String toString ()
    {
        final String type_name = "" + this.type ();

        final Class<?> this_class = this.getClass ();
        final String class_name;
        if ( this_class == Many.class
             || this_class == No.class
             || this_class == One.class
             || this_class == Some.class )
        {
            class_name = "";
        }
        else
        {
            class_name = ClassName.of ( this.getClass () )
                + " ";
        }

        final String elements_string;
        if ( this.elements == null ) // E.g. during constructor.
        {
            elements_string = "(elements not yet constructed)";
        }
        else
        {
            final VALUE [] array = this.elements.array ();
            final long start = this.elements.start ();
            final long end = this.elements.end ();
            final String maybe_range;
            if ( start == 0L
                 && end == ( (long) array.length - 1L ) )
            {
                maybe_range = "";
            }
            else
            {
                maybe_range = " [ " + start + " .. " + end + " ]";
            }

            elements_string =
                StringRepresentation.of (
                    array,
                    0 )  // Let derived classes and/or the caller
                         // limit the String length, if they choose.
                + maybe_range;
        }

        return type_name + " " + class_name + elements_string;
    }
}
