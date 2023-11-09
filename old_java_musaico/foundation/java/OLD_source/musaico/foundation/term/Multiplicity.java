package musaico.foundation.term;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Iterator;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Violation;

import musaico.foundation.contract.guarantees.ReturnNeverNull;
import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.order.Order;


/**
 * <p>
 * An object which can have an element, some elements, or no elements.
 * </p>
 *
 * <p>
 * Provides the data interface for a Term, allowing callers to expect
 * single values, or iterate through 0 or more elements, and so on.
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
 * @see musaico.foundation.term.MODULE#COPYRIGHT
 * @see musaico.foundation.term.MODULE#LICENSE
 */
public interface Multiplicity<VALUE extends Object>
    extends Iterable<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * @return An immutable, Countable representation of this Multiplicity,
     *         if this is Countable.  Otherwise No value.
     *         Always immutable; the value(s) will not change from
     *         one inspection to the next.  Never null.
     */
    public abstract Countable<VALUE> countable ()
        throws ReturnNeverNull.Violation;


    // Every Multiplicity must implement java.lang.Object#equals(java.lang.Object)


    /**
     * <p>
     * Returns a kept filter state, such as <code> FilterState.KEPT </code>,
     * if this is a successful result (one or more values),
     * or a discarded filter state, such as
     * <code> FilterState.DISCARDED </code>, if this is no value, or an
     * error, and so on.
     * </p>
     *
     * <p>
     * Some Multiplicities might embed additional information
     * in their FilterStates.  For example, a Multiplicity which has
     * a natural order might return a musaico.foundation.order.Rank.
     * </p>
     *
     * @return A kept FilterState if this result was One or more values;
     *         a discarded FilerState if this result was No value or an Error
     *         and so on.  Never null.
     */
    public abstract FilterState filterState ()
        throws ReturnNeverNull.Violation;


    // Every Multiplicity must implement java.lang.Object#hashCode()


    /**
     * @return True if this Multiplicity has one or more elements,
     *         false otherwise.
     */
    public abstract boolean hasValue ();


    /**
     * <p>
     * Returns a Term with only the first One element,
     * if this has one or more elements;
     * One or Finite or Infinite elements; or returns a Term with No value,
     * if this is empty or an Error and so on.
     * </p>
     *
     * @return A Term with the first One value from this value,
     *         or a Term with No value, if this is an Empty Term or an Error
     *         and so on.  Never null.
     */
    public abstract Maybe<VALUE> head ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns the first N elements of this value, if this has
     * One or Finite or Infinite multiplicity; or returns a Term
     * with No value if this is empty.
     * </p>
     *
     * @param num_elements The number of elements to retrieve
     *                     from this value.  If 0L is passed,
     *                     then No value is returned.
     *                     Must be greater than or equal to 0L.
     *
     * @return The first N elements from this value, or an Empty Multiplicity
     *         if this result is Empty.  Never null.
     */
    public abstract Countable<VALUE> head (
                                           long num_elements
                                           )
        throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Creates an iterator which will happily step through the
     * (possibly finite, possibly infinite) elements
     * in this Multiplicity.  The iterator will abort after
     * the specified number of iterations, throwing
     * an "IteratorMustBeFinite" violation at the start of
     * the (max + 1)th iteration.
     * </p>
     *
     * @param maximum_iterations The maximum number of iterations
     *                           the returned Iterator will perform
     *                           before throwing a Violation of
     *                           the IteratorMustBeFinite contract.
     *                           Must be greater than 0L.
     *
     * @return An Iterator which will step over elements up to
     *         the specified maximum, throwing an
     *         IteratorMustBeFinite.Violation at the start of the (max+1)th
     *         step.  Never null.  Never contains any null elements.
     *
     * @see musaico.foundation.term.iterators.IteratorMustBeFinite
     */
    public abstract Iterator<VALUE> indefiniteIterator (
            long maximum_iterations
            )
        throws Parameter1.MustBeGreaterThanZero.Violation,
               ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation;


    /**
     * <p>
     * Creates and returns a new Iterator to step through each
     * element of this Multiplicity.
     * </p>
     *
     * <p>
     * For example, One value will iterate over its one element,
     * a Many will iterate over its array of elements, No value will
     * iterate over zero elements, and so on.
     * </p>
     *
     * <p>
     * An empty Multiplicity (including an Empty Term,
     * an Error, an Partial value, and so on)
     * will iterate over 0 elements.
     * </p>
     *
     * <p>
     * Note that an Infinite or Indefinite Multiplicity will iterate
     * over 0 elements.  In order to process the elements of
     * an Infinite or Indefinite Multiplicity, you must call
     * <code> indefiniteIterator () </code>.
     * </p>
     *
     * <p>
     * Any value whose Type allows mutability (i.e. the value's
     * elements might change over time) must return an iterator that
     * is a snapshot in time of the value's elements, such as
     * an UnchangingIterator.
     * </p>
     *
     * @return A newly created Iterator to step through the element(s)
     *         of this Multiplicity.  Never null.  Never contains any
     *         null elements.
     *
     * @see java.lang.Iterable#iterator()
     * @see musaico.foundation.term.infinite.Cyclical#cycle()
     * @see musaico.foundation.term.Multiplicity#infiniteIterator()
     * @see musaico.foundation.term.iterators.UnchangingIterator
     */
    @Override
    public abstract Iterator<VALUE> iterator ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation;


    /**
     * <p>
     * Returns either the single value, or a specific default
     * value in case of no value or multiple values (Finite or Infinite).
     * </p>
     *
     * <p>
     * For example, an object <code> db </code> with method
     * <code> readName () </code> which returns a
     * <code> Term&lt;String&gt; </code> might be used with
     * the following code:
     * </p>
     *
     * <pre>
     *     String name = db.readName ().orDefault ( "" );
     *     if ( name.equals ( "" ) ) ... failure code ...
     *     else ... success code ...
     * </pre>
     *
     * @param default_value The value to return in case an exception
     *                      occurred.  Must not be null.
     *
     * @return The single value requested, or the specified
     *         default value in case of no value or multiple values
     *         (Finite or Infinite).  Never null.
     */
    public abstract VALUE orDefault (
                                     VALUE default_value
                                     )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns either the single value as expected, or the default
     * "none" value if no value or multiple values (Finite or Infinite)
     * were returned.
     * </p>
     *
     * <p>
     * For example, an object <code> db </code> with method
     * <code> readName () </code> which returns a
     * <code> Term&lt;String&gt; </code> might be used with
     * the following code:
     * </p>
     *
     * <pre>
     *     Object name = db.readName ().orNone ();
     *     if ( name instanceof String ) ... success code ...
     *     else if ( name instanceof NoData ) ... failure code ...
     * </pre>
     *
     * @return Either the requested single value,
     *         or a "none" value if this is no value or multiple values
     *         (Finite or Infinite).  Never null.
     */
    public abstract VALUE orNone ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns the expected single value, or null if this is
     * no value or multiple values (Finite or Infinite).
     * </p>
     *
     * <p>
     * For example, an object <code> db </code> with method
     * <code> readName () </code> which returns a
     * <code> Term&lt;String&gt; </code> might be used with
     * the following code:
     * </p>
     *
     * <pre>
     *     String name = db.readName ().orNull ();
     *     if ( name == null ) ... failure code ...
     *     else ... success code ...
     * </pre>
     *
     * @return The expected single value, or null if this is no value
     *         or multiple values (Finite or Infinite).  Can be null.
     */
    public abstract VALUE orNull ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns the expected single value, or throws an instance of
     * the specified exception.
     * </p>
     *
     * <p>
     * The exception must provide either a single argument constructor
     * <code> MyException ( String message ) </code>, or a no args
     * constructor <code> MyException () </code>.  Reflection is used
     * to construct the instance at call time.
     * </p>
     *
     * <p>
     * For example, an object <code> db </code> with method
     * <code> readName () </code> which returns a
     * <code> Term&lt;String&gt; </code> might be used with
     * the following code:
     * </p>
     *
     * <pre>
     *     String name =
     *         db.readName ()
     *           .orThrow ( new MyException.class );
     * </pre>
     *
     * @param exception_class The exception class to instantiate and throw
     *                        if any exception occurred.
     *                        Can be a checked or unchecked (runtime)
     *                        exception class.  Must not be null.
     *
     * @return The expected value.  Never null.
     *
     * @throws An instance of the specified exception class in case any
     *         (checked or unchecked) exception occurred.
     */
    public abstract <EXCEPTION extends Exception>
        VALUE orThrow (
                       Class<EXCEPTION> exception_class
                       )
        throws EXCEPTION,
               ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns the expected single value, or throws the specified
     * Exception if this is not exactly one value (no value
     * or multiple values).
     * </p>
     *
     * <p>
     * For example, an object <code> db </code> with method
     * <code> readName () </code> which returns a
     * <code> Term&lt;String&gt; </code> might be used with
     * the following code:
     * </p>
     *
     * <pre>
     *     try
     *     {
     *         String name =
     *             db.readName ()
     *               .orThrow ( new MyException ( "Failed to read name" ) );
     *     }
     *     catch ( MyException e )
     *     {
     *         ...
     *     }
     * </pre>
     *
     * @param exception The exception to throw if any exception occurred.
     *                  Can be a checked or unchecked (runtime)
     *                  exception.  Must not be null.
     *
     * @return The expected value.  Never null.
     *
     * @throws The specified exception in case any (checked or unchecked)
     *         exception occurred.  Always exactly the "exception"
     *         parameter passed in.
     */
    public abstract <EXCEPTION extends Exception>
        VALUE orThrow (
                       EXCEPTION exception
                       )
        throws EXCEPTION,
               ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns the expected single value, or throws a CheckedTermViolation
     * if this Multiplicity does not have exactly one element.
     * </p>
     *
     * <p>
     * For example, the following code will throw a checked exception
     * (NOT a RuntimeException) whenever the String Term has no value,
     * or more than one elements:
     * </p>
     *
     * <pre>
     *     try
     *     {
     *         final Term<String> term = ...;
     *         final String text = term.orThrowChecked ();
     *         ...
     *     }
     *     catch ( TermViolation violation )
     *     {
     *         ...
     *     }
     * </pre>
     *
     * @return This value.  Never null.
     *
     * @throws TermViolation If this value does not have exactly one element.
     */
    public abstract VALUE orThrowChecked ()
        throws TermViolation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns the expected single value, or throws an UncheckedTermViolation
     * if this Multiplicity does not have exactly one element.
     * </p>
     *
     * <p>
     * For example, the following code will throw an unchecked exception
     * (a RuntimeException) whenever the String Term has no value,
     * or more than one elements:
     * </p>
     *
     * <pre>
     *     final Term<String> term = ...;
     *     final String text = term.orThrowUnchecked ();
     *     ...
     * </pre>
     *
     * @return This value.  Never null.
     *
     * @throws UncheckedTermViolation If this value does not have
     *                                exactly one element.
     */
    public abstract VALUE orThrowUnchecked ()
        throws UncheckedTermViolation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns the expected single value, or throws a Violation of
     * the specified Contract if this is not One value.
     * </p>
     *
     * <p>
     * For example, an object <code> db </code> with method
     * <code> readRow ( id ) </code> which returns a
     * <code> Term&lt;String&gt; </code> might be used with
     * the following code:
     * </p>
     *
     * <pre>
     *     Row row = db.readRow ( id )
     *         .orViolation ( IdentifiedObjectMustExist.CONTRACT,
     *                        this, // plaintiff
     *                        id ); // evidence
     *     ... success code ...
     *     // On failure, the violation of the contract unravels the stack.
     * </pre>
     *
     * @param contract The Contract which will be violated
     *                 if this Multiplicity does not have exactly 1 element.
     *                 Must not be null.
     *
     * @param plaintiff The object under contract.  For
     *                  example, if object x requires a Multiplicity
     *                  of exactly 1 element, then object x
     *                  would be the object under contract,
     *                  and this Multiplicity would be the evidence.
     *                  Must not be null.
     *
     * @return The expected value.  Never null.
     *
     * @throws VIOLATION If this is not a single element Multiplicity.
     */
    public abstract <TERM extends Multiplicity<?>, VIOLATION extends Throwable & Violation>
        VALUE orViolation (
                           Contract<TERM, VIOLATION> contract,
                           Object plaintiff
                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation,
               VIOLATION;


    /**
     * <p>
     * Returns the expected single value, or throws a Violation of
     * the specified Contract if this is not One value.
     * </p>
     *
     * <p>
     * For example, an object <code> db </code> with method
     * <code> readRow ( id ) </code> which returns a
     * <code> Term&lt;String&gt; </code> might be used with
     * the following code:
     * </p>
     *
     * <pre>
     *     Row row = db.readRow ( id )
     *         .orViolation ( IdentifiedObjectMustExist.CONTRACT,
     *                        this, // plaintiff
     *                        id ); // evidence
     *     ... success code ...
     *     // On failure, the violation of the contract unravels the stack.
     * </pre>
     *
     * @param contract The Contract which will be violated
     *                 if this Multiplicity does not have exactly 1 element.
     *                 Must not be null.
     *
     * @param plaintiff The object under contract.  For
     *                  example, if object x has a method
     *                  obligation "parameters must not be
     *                  null" and someone calls x.method ( p )
     *                  with parameter p = null, then object x
     *                  would be the object under contract.
     *                  Must not be null.
     *
     * @param evidence The evidence, to be checked by
     *                 this contract.  For
     *                 example, if object x has a method
     *                 obligation "parameters must not be
     *                 null" and someone calls x.method ( p )
     *                 with parameter p = null, then parameter p
     *                 would be the evidence.
     *                 Can be null.  Can contain null elements.
     *
     * @return The expected value.  Never null.
     *
     * @throws VIOLATION If this is not a single element Multiplicity.
     */
    public abstract <EVIDENCE extends Object, VIOLATION extends Throwable & Violation>
        VALUE orViolation (
                           Contract<EVIDENCE, VIOLATION> contract,
                           Object plaintiff,
                           EVIDENCE evidence
                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation,
               VIOLATION;


    // Every Multiplicity must implement java.lang.Object#toString()
}
