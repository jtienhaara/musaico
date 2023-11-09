package musaico.foundation.term;

import java.io.Serializable;

import java.math.BigDecimal;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Violation;

import musaico.foundation.contract.guarantees.ReturnNeverNull;
import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.AllEncompassingDomain;
import musaico.foundation.domains.SpecificDomain;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.order.Order;

import musaico.foundation.pipeline.Pipeline;


/**
 * <p>
 * An object which is or leads to a value: possibly no value,
 * or possibly one value, or possibly multiple values;
 * or possibly an ongoing expression being evaluated,
 * which will eventually output value(s); or possibly
 * a blocking operation which might or might not
 * generate value(s); or possibly an error; and so on.
 * </p>
 *
 * <p>
 * A Term can be safely queried and passed around without checking
 * for exceptions or worrying about nulls, by calling the
 * appropriate methods.  One main intent is to reduce the quantity of
 * code required to produce intermediate results; null checks and
 * exception handling are often best done at the "end" of a sequence
 * of transformations and processing, rather than at every single
 * step along the way.
 * </p>
 *
 * <p>
 * For example, consider the following method:
 * </p>
 *
 * <pre>
 *     public Term<Integer> previousNaturalNumber ( int successor );
 * </pre>
 *
 * <p>
 * Then the following code will print the result of calling the
 * method, or "0" when there are no more values:
 * </p>
 *
 * <pre>
 *     int successor = ...;
 *     int predecessor = previousNaturalNumber ( successor )
 *                           .orDefault ( 0 );
 *     System.out.println ( "Previous natural number: " + predecessor );
 * </pre>
 *
 * <p>
 * Or consider the following method:
 * </p>
 *
 * <pre>
 *     public Term<InputStream> open ();
 * </pre>
 *
 * <p>
 * Then the caller might, on failure, throw the default TermViolation
 * if something went wrong:
 * </p>
 *
 * <pre>
 *     InputStream in = open ().orThrowUnchecked ();
 * </pre>
 *
 * <p>
 * Or the caller may wish to throw a different exception up the stack.
 * For example, to throw an IOException when the open method fails:
 * </p>
 *
 * <pre>
 *     InputStream in = open ().orThrow ( IOException.class );
 * </pre>
 *
 * <p>
 * Or if the caller wishes to wrap the declared TermViolation in
 * a CheckedTermViolation:
 * </p>
 *
 * <pre>
 *     InputStream in = open ().orThrowChecked ();
 * </pre>
 *
 * <p>
 * Or the caller may choose to throw their own exception:
 * </p>
 *
 * <pre>
 *     InputStream in =
 *         open ().orThrow ( new MyException ( "Failed to open" ) );
 * </pre>
 *
 * <p>
 * Or the caller might want to fall back on a default value such
 * as null:
 * </p>
 *
 * <pre>
 *     InputStream in = open ().orNull ();
 * </pre>
 *
 * <p>
 * Or the caller might want to perform a series of file operations,
 * and only check for errors once, at the very end.  For example,
 * supposing a number of other methods are available to accept
 * Terms as inputs, and return Terms as outputs:
 * </p>
 *
 * <pre>
 *     Term<InputStream> in = open (); // Open the stream.
 *     while ( in.hasValue () // Continue as long as we have an input stream.
 *             &amp;&amp; available ( in ) &gt;= 0 ) // Continue until EOF.
 *     {
 *         in = read ( in, 1024 ); // Read 1024 bytes from the stream.
 *     }
 *     in = close ( in ); // Close the stream.
 *
 *     // Now handle errors:
 *     if ( ! in.hasValue () )
 *     {
 *         // Here we can induce an exception, perform some failure logic,
 *         // exit the program, and so on, depending on our needs.
 *         in.orThrowUnchecked ();
 *     }
 * </pre>
 *
 * <p>
 * In the above example, the control flow of opening, reading in, and
 * closing a file is kept together (albeit without AutoCloseable in this
 * trivial example...) without strafing it with confusing exception-handling
 * code.
 * </p>
 *
 * <p>
 * In this way the caller is also not tied to the method provider's
 * decision about how to handle exceptional conditions, and the
 * method provider also does not have to agonize over whether
 * to throw checked or unchecked exceptions, return null, and so on,
 * each with their own detriments and dangers.
 * </p>
 *
 * <p>
 * In the example above, the various methods (open (), read (), close ())
 * can return success or failure without choosing between throwing
 * exceptions, returning nulls, returning booleans (true = success,
 * false = failure), and so on, causing a single library to become
 * inconsistent and painful to use because every interface generates
 * errors in different ways than every other interface within the
 * very same library.
 * </p>
 *
 * <p>
 * The default expectation a single value.  In some
 * cases, though, the caller may be willing to accept multiple
 * values -- for example, in the case where "at least one result value"
 * is required.  In this case a Finite or Infinite result is returned.  The
 * caller may access the multiple values of a Finite result by
 * iterating through them, or may choose to retrieve only the first
 * by calling <code> head () </code>.  Calling any of the
 * <code> or... () </code> methods (orNull (), orThrowUnchecked (),
 * and so on), one can expect only one result.
 * </p>
 *
 * <p>
 * An Infinite result requires caution and special method calls
 * to avoid infinite loops, and the <code> iterator () </code>
 * for an Infinite result always steps over 0 elements,
 * to avoid infinite loops.
 * </p>
 *
 * <pre>
 *     SomeNumbers some_numbers = new SomeNumbers ( 1, 2, 3, 4, 5, 6, 7, 8 );
 *     int a_prime_number = some_numbers.primes ()
 *                              .head ()
 *                              .orThrowUnchecked ();
 * </pre>
 *
 * <p>
 * In the above example, the caller expects at least one -- but possibly
 * more than one -- prime number(s) to be returned.  If none are returned,
 * then the caller throws a TermViolaion exception.
 * If one or more prime numbers are returned, then the
 * <code> a_prime_number </code> integer is set to the first value.
 * </p>
 *
 * <p>
 * Every Term can also serve as the head of a Pipeline of Operations.
 * For example, to select the last element of term "x" and append
 * term "y" to the end,
 * <code> x.select ().last ().edit ().append ( y ).output () </cpde>
 * could be invoked.
 * </p>
 *
 * <p>
 * Each Pipeline method of a Term returns a new expression Term,
 * using its Type's OperationPipeline to build the expression.
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
 * @see musaico.foundation.term.MODULE#COPYRIGHT
 * @see musaico.foundation.term.MODULE#LICENSE
 */
public interface Term<VALUE extends Object>
    extends Multiplicity<VALUE>, TermPipeline.TermSink<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** Matches any and all Terms, including null. */
    @SuppressWarnings("unchecked") // Cast Class<Term> to Class<Term<?>>.
    public static final Filter<Term<?>> FILTER_ALL =
        new SpecificDomain<Object, Term<?>> ( AllEncompassingDomain.DOMAIN,
                                              (Class<Term<?>>)
                                              ( (Class<?>) Term.class ) );


    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#check()

    // Every Term must implement
    // musaico.foundation.term.Multiplicity#countable()


    /**
     * <p>
     * Returns true if this Term claims to be an instance
     * of every specified Type; false otherwise.
     * </p>
     *
     * <p>
     * Note that declaring itself to be an instance of a Type
     * is not the same as checking that the claim holds true.
     * A Term might declare that it instantiates a Type, but the only way
     * to prove it is true is to check it with against Type's constraints.
     * </p>
     *
     * <p>
     * A typical example of a Term declaring a Type that it does not,
     * in fact, adhere to is the output of an Operation, which declares
     * a specific output Type, but does not check every output to ensure
     * it meets all constraints.  The one Operation which is guaranteed
     * to always output a valid Term of its declared output Type
     * is the check Operation, accessible via a pipeline as
     * <code> check () </code>.  For example,
     * <code> term.check ().output () </code> will produce
     * a valid output Term of the input Term's Type, and
     * <code> type.check ().from ( term ).output () </code> will
     * check that a Term is a valid instance of the specified
     * (possibly less general) Type.
     * </p>
     *
     * <p>
     * If the specified types are empty, then true is returned.
     * </p>
     *
     * @param types The Type(s) that this Term might or might not claim
     *              to instantiate.  Can be empty.  Must not be null.
     *              Must not contain any null elements.
     *
     * @return True if this Term declares the specified Type(s),
     *         or if the <code> types </code> parameter is empty;
     *         false if it does not claim to be an instance
     *         of every one of the specified Type(s).
     */
    public abstract boolean declares (
            Type<?>... types
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation;


    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#duplicate()

    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#edit()

    // Every Term must implement java.lang.Object#equals(java.lang.Object)

    // Every Term must implement
    // musaico.foundation.term.Multiplicity#filterState()

    // Every Pipeline must implement
    // musaico.foundation.pipeline.Sink#fork()

    // Every Term must implement java.lang.Object#hashCode()

    // Every Term must implement
    // musaico.foundation.term.Multiplicity#hasValue()

    // Every Term must implement
    // musaico.foundation.term.Multiplicity#head()

    // Every Term must implement
    // musaico.foundation.term.Multiplicity#head(long)

    // Every Term must implement
    // musaico.foundation.term.Multiplicity#indefiniteIterator(long)

    // Every Multiplicity must implement
    // java.lang.iterable#iterator ()

    // Every Pipeline must implement
    // musaico.foundation.pipeline.Sink#join()

    // Every TermPipeline.TermSink must implement
    // musaico.foundation.term.TermPipeline.TermSink#operations()

    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#orderBy(musaico.foundation.order.Order[])

    // Every Term must implement
    // musaico.foundation.term.Multiplicity#orDefault(java.lang.Object);

    // Every Term must implement
    // musaico.foundation.term.Multiplicity#orNone ()

    // Every Term must implement
    // musaico.foundation.term.Multiplicity#orNull ()

    // Every Term must implement
    // musaico.foundation.term.Multiplicity#orThrow (java.lang.Class)

    // Every Term must implement
    // musaico.foundation.term.Multiplicity#orThrow (java.lang.Exception)

    // Every Term must implement
    // musaico.foundation.term.Multiplicity#orThrowChecked ()

    // Every Term must implement
    // musaico.foundation.term.Multiplicity#orThrowUnchecked ()

    // Every Term must implement
    // musaico.foundation.term.Multiplicity#orViolation(musaico.foundation.contract.Contract, java.lang.Object)

    // Every Term must implement
    // musaico.foundation.term.Multiplicity#orViolation(musaico.foundation.contract.Contract, java.lang.Object, java.lang.Object)


    // Every TermPipeline must implement
    // musaico.foundation.term.TermPipeline#pipe(musaico.foundation.term.Operation)

    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#select()

    // Every Pipeline must implement
    // musaico.foundation.pipeline.Parent#thisPipeline()

    // Every Term must implement java.lang.Object#toString()

    // Every TermPipeline must implement
    // musaico.foundation.term.TermPipeline#type()

    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#when(musaico.foundation.filter.Filter)

    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#where(musaico.foundation.filter.Filter[])
}
