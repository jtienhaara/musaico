package musaico.foundation.value;

import java.io.Serializable;

import java.lang.reflect.Constructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;
import musaico.foundation.contract.ObjectContracts;
import musaico.foundation.contract.Violation;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;


/**
 * <p>
 * An immutable, multi-dimensional (two or more dimensions) Countable
 * value which has two or more values.
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
 * @see musaico.foundation.value.MODULE#COPYRIGHT
 * @see musaico.foundation.value.MODULE#LICENSE
 */
public abstract class AbstractMultiple<VALUE extends Object>
    implements Multiple<VALUE>, Dimensional<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( Matrix.class );


    // Enforces parameter obligations and so on for us.
    private final ObjectContracts contracts;

    // The expected class of the conditional value.
    private final Class<VALUE> expectedClass;

    // The # of dimensions of the elements array.
    // For an N-dimension matrix, we use the last N indices of
    // the 9-dimensional elements array.
    private final int dimensions;

    // The 9-dimensional elements array.
    // For an N-dimension matrix, we use the last N indices of
    // the 9-dimensional elements array.
    // The first (9 - N) dimensions are all exactly 1 sub-matrix long.
    private final VALUE [] [] [] [] [] [] [] [] [] elements;


    /**
     * <p>
     * Creates a new Matrix value.
     * </p>
     *
     * @param expected_class The expected class of the conditional Value.
     *                       For example, the expected class for a
     *                       <code> Value&lt;Number&gt; </code>
     *                       is always <code> Number.class </code>.
     *                       Must not be null.
     *
     * @param elements The 1-or-more-dimensional matrix of elements.
     *                 Must not be null.  Must not contain any null elements.
     */
    public Matrix (
                   Class<VALUE> expected_class,
                   VALUE [] elements
                   )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               expected_class, elements );
        classContracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               elements );

        this.expectedClass = expected_class;
        this.elements =
            Array.newInstance ( this.expectedClass,
                                new int [] { 1, 1, 1, 1, 1, 1, 1, 1,
                                             elements.length } );

        System.arraycopy ( elements, 0, elements.size,
                           this.elements [ 0 ] [ 0 ] [ 0 ] [ 0 ] [ 0 ] [ 0 ] [ 0 ] [ 0 ],
                           0, elements.size );

        this.contracts = new ObjectContracts ( this );
    }


    /**
     * @see musaico.foundation.value.Value#and(musaico.foundation.value.Value)
     */
    @Override
    @SuppressWarnings("unchecked") // Generic varargs array creation.
    public final Value<VALUE> and (
                                   Value<VALUE> that
                                   )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               that );

        if ( that instanceof Error )
        {
            // Matrix value this & Error value that = that.
            return that;
        }
        else if ( that instanceof Timeout )
        {
            // Matrix value this & Timeout value that = that.
            return that;
        }
        else if ( that instanceof Blocking )
        {
            // Matrix value this & Blocking value that = new Blocking value.
            final Blocking<VALUE> blocking_that =
                (Blocking<VALUE>) that;
            final AndValues<VALUE> logic =
                new AndValues<VALUE> ( this );
            return new Blocking<VALUE> ( this.expectedClass, // exp.class
                                         logic,           // logic
                                         blocking_that ); // blocking_values
        }
        else if ( that instanceof Warning )
        {
            // Matrix value this & Warning value that = that.
            return that;
        }
        else if ( that instanceof Partial )
        {
            // Matrix value this & Partial value that = that.
            return that;
        }
        else if ( that instanceof One )
        {
            // Matrix value this & One value that = that.
            return that;
        }
        else if ( that instanceof Countable )
        {
            // Matrix value this & Countable value that
            //     = shorter of the two.
            final Countable<VALUE> countable_that =
                (Countable<VALUE>) that;
            if ( this.length () <= countable_that.length () )
            {
                // This is shorter.
                return this;
            }
            else
            {
                // That is shorter.
                return that;
            }
        }
        else if ( that instanceof Just )
        {
            // Matrix value this & Just value that = this.
            return this;
        }
        else // Any other Unjust value, including No.
        {
            // Matrix value this & Unjust value that = that.
            return that;
        }
    }


    /**
     * @see musaico.foundation.value.Dimensional#at(long[])
     */
    @Override
    public final Dimensional<VALUE> at (
                                        long... indices
                                        )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               indices );

        if ( indices.length > 1 )
        {
            final DimensionalMustHaveAtLeastNDimensions contract =
                new DimensionalMustHaveAtLeastNDimensions ( (long) indices.length );
            final DimensionalMustHaveAtLeastNDimensions.Violation violation =
                contract.violation ( this, this );
            return new Error<VALUE> ( this.expectedClass (),
                                      violation );
        }
        else if ( indices.length == 0 )
        {
            return this;
        }

        final List<VALUE> elements = this.internalElements ();
        if ( indices [ 0 ] < 0L
             || indices [ 0 ] >= (long) elements.size () )
        {
            final DimensionalMustHaveIndices contract =
                new DimensionalMustHaveIndices ( indices );
            final DimensionalMustHaveIndices.Violation violation =
                contract.violation ( this, this );
            return new Error<VALUE> ( this.expectedClass (),
                                      violation );
        }

        final VALUE element = elements.get ( (int) indices [ 0 ] );
        final One<VALUE> one = new One<VALUE> ( this.expectedClass (),
                                                element );

        return one;
    }


    /**
     * @see musaico.foundation.value.Value#blockingMaxNanoseconds()
     */
    @Override
    public final long blockingMaxNanoseconds ()
    {
        return 0L;
    }


    /**
     * @see musaico.foundation.value.Value#countable()
     */
    @Override
    public final Matrix<VALUE> countable ()
        throws ReturnNeverNull.Violation
    {
        return this;
    }


    /**
     * <p>
     * Creates a new Countable value based on this one, but with
     * the specified (zero or more) contents.
     * </p>
     *
     * <p>
     * The default implementation simply uses a ValueBuilder to
     * create No value, One value, or Many values.  Derived implementations
     * can override this behaviour to create objects of specific classes.
     * </p>
     *
     * @param contents The elements for the new Matrix.
     *                 Must not be null.  Must not contain any null
     *                 elements.
     *
     * @param no_value_violation The optional default violation to use when
     *                           creating No values.  If null then a
     *                           new no value violation is created if
     *                           needed.  Can be null.
     *
     * @return The newly created Countable value.  Never null.
     */
    protected Countable<VALUE> createNewCountable (
                                                   List<VALUE> elements,
                                                   ValueViolation no_value_violation
                                                   )
    {
        final ValueBuilder<VALUE> builder =
            new ValueBuilder<VALUE> ( this.expectedClass,
                                      no_value_violation );
        builder.addAll ( elements );
        return builder.build ();
    }


    /**
     * @see musaico.foundation.value.Dimensional#dimensions()
     */
    @Override
    public final long dimensions ()
    {
        return 1L;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
                                 Object obj
                                 )
    {
        if ( obj == null
             || ! ( obj instanceof Matrix ) )
        {
            return false;
        }

        Matrix<?> that = (Matrix<?>) obj;
        if ( ! this.expectedClass ().equals ( that.expectedClass () ) )
        {
            return false;
        }

        if ( this.length () != that.length () )
        {
            return false;
        }

        final Iterator<VALUE> this_iterator =
            this.internalElements ().iterator ();
        final Iterator<?> that_iterator =
            that.internalElements ().iterator ();
        while ( this_iterator.hasNext ()
                && that_iterator.hasNext () )
        {
            final VALUE this_element = this_iterator.next ();
            final Object that_element = that_iterator.next ();

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

        if ( this_iterator.hasNext ()
             != that_iterator.hasNext () )
        {
            // Different length Matrix's.
            return false;
        }

        // Same length Matrixs, every element is equal.
        return true;
    }


    /**
     * @see musaico.foundation.value.Value#expectedClass()
     */
    @Override
    public final Class<VALUE> expectedClass ()
    {
        return this.expectedClass;
    }


    /**
     * @see musaico.foundation.value.Value#filter()
     */
    @Override
    public final FilterState filter ()
    {
        return FilterState.KEPT;
    }


    /**
     * @see musaico.foundation.value.Dimensional#has(long[])
     */
    @Override
    public final boolean has (
                              long... indices
                              )
        throws ParametersMustNotBeNull.Violation,
               Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               indices );

        if ( indices.length == 0 )
        {
            return true;
        }
        else if ( indices.length > 1 )
        {
            return false;
        }

        final List<VALUE> elements = this.internalElements ();
        if ( indices [ 0 ] < 0L
             || indices [ 0 ] >= (long) elements.size () )
        {
            return false;
        }

        return true;
    }


    /**
     * @see musaico.foundation.value.Value#hasValue()
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
        hash_code += ( this.expectedClass == null ? 0 : this.expectedClass.hashCode () );

        return hash_code;
    }


    /**
     * @see musaico.foundation.value.Value#head()
     */
    @Override
    public final One<VALUE> head ()
        throws ReturnNeverNull.Violation
    {
        VALUE first_value = this.iterator ().next ();
        return new One<VALUE> ( this.expectedClass,
                                first_value );
    }


    /**
     * @see musaico.foundation.value.Dimensional#indices(long[])
     */
    @Override
    public final long indices (
                               long... indices
                               )
        throws ParametersMustNotBeNull.Violation,
               Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               indices );

        if ( indices.length > 0 )
        {
            return 0L;
        }

        final List<VALUE> elements = this.internalElements ();
        return (long) elements.size ();
    }


    /**
     * @return The List of elements in this Matrix, used only
     *         internally by Matrix when determining length
     *         and so on.  Often the actual underpinning of
     *         the Matrix's content is returned,
     *         so the caller must not modify the list in any way.
     *         For all Matrix concrete classes in the package
     *         musaico.foundation.value, once set, the contents
     *         of the list may not ever change.  However classes from
     *         outside this package may do away with this restriction.
     *         The List returned will never be modified.  So if
     *         this Matrix value is Idempotent, its element(s)
     *         can be returned as-is.  But if it is not Idempotent it
     *         must create a new List of its elements, a snapshot of
     *         the elements which will never change.
     *         Can only ever be null during the constructor (because
     *         Matrix calls toString () during its constructor,
     *         before the derived class has had a chance to set up its
     *         elements; the only method affected by this null during
     *         construction is Matrix.toString ()).
     *         After construction is complete, never null.
     *         Never contains any null elements.
     */
    protected abstract List<VALUE> internalElements ();


    /**
     * @see musaico.foundation.value.Value#iterator()
     */
    @Override
    public Iterator<VALUE> iterator ()
    {
        return new ValueIterator<VALUE> ( this.internalElements () );
    }


    /**
     * @see musaico.foundation.value.Countable#length()
     */
    @Override
    public final long length ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        return (long) this.internalElements ().size ();
    }


    /**
     * @see musaico.foundation.value.Value#onBlocking(long)
     */
    @Override
    public final Matrix<VALUE> onBlocking (
                                                     long timeout_in_nanoseconds
                                                     )
    {
        // We aren't waiting for a result, we already know what we are.
        return this;
    }


    /**
     * @see musaico.foundation.value.Value#or(musaico.foundation.value.Value)
     */
    @Override
    @SuppressWarnings("unchecked") // Generic varargs array creation.
    public final Value<VALUE> or (
                                  Value<VALUE> that
                                  )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               that );

        if ( that instanceof Error )
        {
            // Matrix value this | Error value that = that.
            return that;
        }
        else if ( that instanceof Timeout )
        {
            // Matrix value this | Timeout value that = that.
            return that;
        }
        else if ( that instanceof Blocking )
        {
            // Matrix value this | Blocking value that
            //     = new Blocking value.
            final Blocking<VALUE> blocking_that =
                (Blocking<VALUE>) that;
            final OrValues<VALUE> logic =
                new OrValues<VALUE> ( this );
            return new Blocking<VALUE> ( this.expectedClass, // exp.class
                                         logic,           // logic
                                         blocking_that ); // blocking_values
        }
        else if ( that instanceof Warning )
        {
            // Matrix value this | Warning value that = this.
            return this;
        }
        else if ( that instanceof Partial )
        {
            // Matrix value this | Partial value that = this.
            return this;
        }
        else if ( that instanceof Countable )
        {
            // Matrix values this | Countable values that
            //     = new Many (regardless of this Matrixs class).
            final Countable<VALUE> countable_that = (Countable<VALUE>) that;
            final List<VALUE> values = new ArrayList<VALUE> ();
            values.addAll ( this.internalElements () );
            for ( VALUE value : countable_that )
            {
                values.add ( value );
            }

            return new Many<VALUE> ( this.expectedClass,
                                     values );
        }
        else if ( that instanceof Just )
        {
            // Matrix value this | Just value that = that.
            return that;
        }
        else // Any other Unjust value, including No.
        {
            // Matrix value this | Unjust value that = this.
            return this;
        }
    }


    /**
     * @see musaico.foundation.value.Value#orDefault(java.lang.Object)
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
     * @see musaico.foundation.value.Value#orNone()
     */
    @Override
    public final VALUE orNone ()
    {
        // Expecting a single result.  Return the none object.
        return DefaultValuesRegistry.get ().noneGenerator ( this ).none ();
    }


    /**
     * @see musaico.foundation.value.Value#orNull()
     */
    @Override
    public final VALUE orNull ()
    {
        // Expecting a single result.  Return null.
        return null;
    }


    /**
     * @see musaico.foundation.value.Value#orPartial()
     */
    @Override
    public final Matrix<VALUE> orPartial ()
    {
        return this;
    }


    /**
     * @see musaico.foundation.value.Value#orThrow(java.lang.Class)
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

        Exception cause = null;
        EXCEPTION exception = null;
        try
        {
            // First try single arg MyException ( String message ).
            final Constructor<EXCEPTION> constructor =
                exception_class.getConstructor ( String.class );
            exception =
                constructor.newInstance ( this.valueViolation ().getMessage () );
        }
        catch ( Exception e )
        {
            cause = e;
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
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            throw violation;
        }

        exception.initCause ( this.valueViolation () );

        throw exception;
    }


    /**
     * @see musaico.foundation.value.Value#orThrow(java.lang.Exception)
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
            exception.initCause ( this.valueViolation () );
        }

        // Expecting a single result.  Throw the specified exception.
        throw exception;
    }


    /**
     * @see musaico.foundation.value.Value#orThrowChecked()
     */
    @Override
    public final VALUE orThrowChecked ()
        throws ValueViolation
    {
        // Expecting a single result.  Throw the single value violation.
        throw this.valueViolation ();
    }


    /**
     * @see musaico.foundation.value.Value#orThrowUnchecked()
     */
    @Override
    public final VALUE orThrowUnchecked ()
        throws UncheckedValueViolation
    {
        // Expecting a single result.  Wrap the single value violation
        // in a runtime exception, and throw it.
        throw new UncheckedValueViolation ( this.valueViolation () );
    }


    /**
     * @see musaico.foundation.value.Value#orViolation(musaico.foundation.contract.Contract,java.lang.Object, java.lang.Object)
     */
    @Override
    public final <INSPECTABLE extends Object, VIOLATION extends Throwable & Violation>
        VALUE orViolation (
                           Contract<INSPECTABLE, VIOLATION> contract,
                           Object plaintiff,
                           INSPECTABLE inspectable_data
                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation,
               VIOLATION
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               contract, plaintiff, inspectable_data );

        final VIOLATION violation =
            contract.violation ( plaintiff,
                                 inspectable_data );
        violation.initCause ( this.valueViolation () );
        throw violation;
    }


    /**
     * @see musaico.foundation.value.Value#pipe(musaico.foundation.value.ValueProcessor)
     */
    @Override
    public final Value<VALUE> pipe (
                                    ValueProcessor<VALUE> value_processor
                                    )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               value_processor );

        return value_processor.process ( this );
    }


    /**
     * <p>
     * Converts this Matrix's elements into human-readable text.
     * </p>
     *
     * @return The String representation of this Matrix's elements.
     *         Never null.
     */
    protected final String convertElementsToString ()
        throws ReturnNeverNull.Violation
    {
        final StringBuilder sbuf = new StringBuilder ();
        boolean is_first = true;
        sbuf.append ( "{" );
        for ( int dimension = 0; dimension < 9; dimension ++ )
        {
            !!!;
        for ( Object element : elements ) // Don't bother w/ValueIterator safety
        {
            if ( is_first )
            {
                is_first = false;
            }
            else
            {
                sbuf.append ( "," );
            }

            final String element_as_string =
                One.convertActualValueToString ( element );

            sbuf.append ( " " + element_as_string );
        }

        if ( ! is_first )
        {
            sbuf.append ( " " );
        }

        sbuf.append ( "}" );

        return sbuf.toString ();
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        final List<VALUE> elements = this.internalElements ();
        if ( elements == null ) // During constructor
        {
            return "{ ... }";
        }

        final String as_string =
            Matrix.convertActualValuesToString ( elements );

        return as_string;
    }


    // Every Matrix must implement NotOne.valueViolation ().
}
