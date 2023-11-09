package musaico.foundation.term.blocking;

import java.io.Serializable;

import java.rmi.RemoteException;

import java.math.BigDecimal;

import java.lang.reflect.Constructor;

import java.util.Iterator;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Violation;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter4;
import musaico.foundation.contract.obligations.Parameter5;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;
import musaico.foundation.domains.Clock;
import musaico.foundation.domains.Seconds;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.term.CheckedTermViolation;
import musaico.foundation.term.Countable;
import musaico.foundation.term.Idempotent;
import musaico.foundation.term.NonBlocking;
import musaico.foundation.term.ReadOnceThenIdempotent;
import musaico.foundation.term.Operation;
import musaico.foundation.term.Select;
import musaico.foundation.term.Type;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;
import musaico.foundation.term.ZeroOrOne;

import musaico.foundation.term.abnormal.Error;

import musaico.foundation.term.contracts.TermMustBeCountable;
import musaico.foundation.term.contracts.TermMustBeNonBlocking;

import musaico.foundation.term.expression.Expression;

import musaico.foundation.term.finite.No;

import musaico.foundation.term.incomplete.CompletionPossible;

import musaico.foundation.term.iterators.TermIterator;

import musaico.foundation.term.operations.CumulativeOperation;


/**
 * <p>
 * A conditional Term which might not yet have been retrieved, and
 * can be used to either block the caller until the result comes in,
 * or process the eventual result asynchronously.
 * </p>
 *
 * <p>
 * For example, the result of an I/O operation, such as receiving
 * the response from a networking request, or reading content from
 * a file, or querying a database, and so on.
 * </p>
 *
 * <p>
 * A Blocking result is an Incomplete result.  Callers not expecting
 * a Blocking result will fail on this term.
 * </p>
 *
 * <p>
 * But callers who are willing to
 * wait for the result can call <code> await () </code>,
 * specify how long they are willing to wait, and then block until
 * the result comes in.
 * </p>
 *
 * <p>
 * Alternatively, a caller who is willing to wait can create a
 * callback (Operation) and pipe the result to it asynchronously,
 * whenever it comes in.
 * </p>
 *
 * <p>
 * This flexibility allows the
 * caller to decide how to manage concurrency, and can be used
 * to decouple information services from concurrency concerns,
 * while ensuring that callers must explicitly decide to wait
 * for a blocking result.
 * </p>
 *
 * <p>
 * Those who do not wish to fail on calls to <code> orNone () </code>
 * and so on should use the Asynchronous class instead.  Asynchronous
 * is NOT a Term, but can be used to force the caller to choose
 * between blocking and handling the result asynchronously.
 * </p>
 *
 *
 * <p>
 * In Java every conditional Term must be Serializable in order to
 * play nicely across RMI.  However users of the conditional Terms
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
 * @see musaico.foundation.term.blocking.MODULE#COPYRIGHT
 * @see musaico.foundation.term.blocking.MODULE#LICENSE
 */
public class Blocking<VALUE extends Object>
    implements CompletionPossible<VALUE>, ReadOnceThenIdempotent<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( Blocking.class );


    // Enforces parameter obligations and so on for us.
    private final Advocate contracts;

    // The type of this term.
    private final Type<VALUE> type;

    // The cause of this term, such as an Unidempotent term of which
    // this is an Idempotent snapshot; or a Warning or Partial value;
    // and so on.  This term can be its own cause.
    private final Term<?> cause;

    // The violation that causes trouble for callers whenever
    // this.orXYZ () is called.
    private final TermViolation termViolation;

    // The result we block on or pipe asynchronously.
    private final Result<VALUE> result;


    /**
     * <p>
     * Creates a new Blocking result, allowing the caller to
     * decide whether to wait for the result synchronously
     * with <code> await () <code>, or process the result
     * asynchronously with <code> pipe () <code>.
     * </p>
     *
     * <p>
     * This constructor reuses the same TermViolation as the
     * specified asynchronous result.
     * </p>
     *
     * @param result The asynchronous result which will either eventually
     *               receive a final conditional Term,
     *               or timeout or be cancelled while blocking.
     *               Must not be null.
     */
    public Blocking (
                     Result<VALUE> result
                     )
        throws ParametersMustNotBeNull.Violation
    {
        this ( null, // cause
               result );
    }


    /**
     * <p>
     * Creates a new Blocking result, allowing the caller to
     * decide whether to wait for the result synchronously
     * with <code> await () <code>, or process the result
     * asynchronously with <code> pipe () <code>.
     * </p>
     *
     * <p>
     * This constructor reuses the same TermViolation as the
     * specified asynchronous result.
     * </p>
     *
     * @param cause The optional cause of this term, such as a
     *              non-Idempotent term of which this is a snapshot,
     *              or a Warning, or a Partial value, and so on.
     *              If null, then this term is its own cause.
     *              Can be null.
     *
     * @param result The asynchronous result which will either eventually
     *               receive a final conditional Term,
     *               or timeout or be cancelled while blocking.
     *               Must not be null.
     */
    public Blocking (
                     Term<?> cause,
                     Result<VALUE> result
                     )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               result );

        this.type = result.type ();
        this.termViolation =
            TermMustBeNonBlocking.CONTRACT.violation ( this,
                                                        this );
        if ( cause == null )
        {
            this.cause = this;
        }
        else
        {
            this.cause = cause;
        }
        this.result = result;

        this.contracts = new Advocate ( this );
    }


    /**
     * <p>
     * Creates a new Blocking result, allowing the caller to
     * decide whether to wait for the result synchronously
     * with <code> await () <code>, or process the result
     * asynchronously with <code> pipe () <code>.
     * </p>
     *
     * <p>
     * This constructor reuses a different TermViolation from the
     * specified asynchronous result.
     * </p>
     *
     * @param result The asynchronous result which will either eventually
     *               receive a final conditional Term,
     *               or timeout or be cancelled while blocking.
     *               Must not be null.
     *
     * @param term_violation The exception which caused the blocking
     *                       conditional Term.  Must not be null.
     */
    public Blocking (
                     Result<VALUE> result,
                     TermViolation term_violation
                     )
        throws ParametersMustNotBeNull.Violation
    {
        this ( null, // cause
               result,
               term_violation );
    }


    /**
     * <p>
     * Creates a new Blocking result, allowing the caller to
     * decide whether to wait for the result synchronously
     * with <code> await () <code>, or process the result
     * asynchronously with <code> pipe () <code>.
     * </p>
     *
     * <p>
     * This constructor reuses a different TermViolation from the
     * specified asynchronous result.
     * </p>
     *
     * @param cause The optional cause of this term, such as a
     *              non-Idempotent term of which this is a snapshot,
     *              or a Warning, or a Partial value, and so on.
     *              If null, then this term is its own cause.
     *              Can be null.
     *
     * @param result The asynchronous result which will either eventually
     *               receive a final conditional Term,
     *               or timeout or be cancelled while blocking.
     *               Must not be null.
     *
     * @param term_violation The exception which caused the blocking
     *                       conditional Term.  Must not be null.
     */
    public Blocking (
                     Term<?> cause,
                     Result<VALUE> result,
                     TermViolation term_violation
                     )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               result, term_violation );

        this.type = result.type ();
        if ( cause == null )
        {
            this.cause = this;
        }
        else
        {
            this.cause = cause;
        }
        this.termViolation = term_violation;
        this.result = result;

        this.contracts = new Advocate ( this );
    }


    /**
     * <p>
     * Creates a new Blocking term from the specified Blocking term(s).
     * Once all Blocking term(s) have reported in (each one either
     * generating a result or timing out), the final result will be
     * ready for this new Blocking term.
     * </p>
     *
     * <p>
     * The final result of the new Blocking Term will be
     * calcuclated by calling <code> logic.apply ( result ) </code>,
     * then applying the output operation to the next result,
     * then the next output operation to the result after that, and so on.
     * For example, if four Blocking terms are passed in, and their
     * results are received in the order 3, 1, 4, 2, then the
     * final result will be:
     * </p>
     *
     * <pre>
     *     logic.apply ( result#3 )
     *          .orNone ().apply ( result#1 )
     *          .orNone ().apply ( result#4 )
     *          .orNone ().apply ( result#2 );
     * </pre>
     *
     * @param type The Type that is expected for
     *             the final result of this Blocking term.
     *             Must not be null.
     *
     * @param clock The Clock used to measure start time and elapsed time.
     *              Also used to calculate the remainder of await ( ... ) time
     *              after the input is ready (if any at all), and await the
     *              remainder of the time on the output, in case it is also
     *              a Blocking or similar Incomplete result.
     *              Must not be null.
     *
     * @param logic The logic to perform on each result as it comes in,
     *              typically <code> AndTerms </code>
     *              or <code> OrTerms </code>.  Must not be null.
     *
     * @param blocking_terms 0 or more Blocking terms to wait upon
     *                       before this new Blocking term's result
     *                       is finalized.  Must not be null.
     *                       Must not contain any null elements.
     */
    @SuppressWarnings("unchecked") // Generic varargs heap pollution.
    public Blocking (
                     Type<VALUE> type,
                     Clock clock,
                     CumulativeOperation<VALUE, VALUE> logic,
                     Blocking<VALUE> ... blocking_terms
                     )
        throws ParametersMustNotBeNull.Violation,
               Parameter4.MustContainNoNulls.Violation
    {
        this ( type,
               null, // cause
               clock,
               logic,
               classContracts.check (
                   Parameter4.MustContainNoNulls.CONTRACT,
                   blocking_terms ) );
    }


    /**
     * <p>
     * Creates a new Blocking term from the specified Blocking term(s).
     * Once all Blocking term(s) have reported in (each one either
     * generating a result or timing out), the final result will be
     * ready for this new Blocking term.
     * </p>
     *
     * <p>
     * The final result of the new Blocking Term will be
     * calcuclated by calling <code> logic.apply ( result ) </code>,
     * then applying the output operation to the next result,
     * then the next output operation to the result after that, and so on.
     * For example, if four Blocking terms are passed in, and their
     * results are received in the order 3, 1, 4, 2, then the
     * final result will be:
     * </p>
     *
     * <pre>
     *     logic.apply ( result#3 )
     *          .orNone ().apply ( result#1 )
     *          .orNone ().apply ( result#4 )
     *          .orNone ().apply ( result#2 );
     * </pre>
     *
     * @param type The type that is expected for
     *             the final result of this Blocking term.
     *             Must not be null.
     *
     * @param cause The optional cause of this term, such as a
     *              non-Idempotent term of which this is a snapshot,
     *              or a Warning, or a Partial value, and so on.
     *              If null, then this term is its own cause.
     *              Can be null.
     *
     * @param clock The Clock used to measure start time and elapsed time.
     *              Also used to calculate the remainder of await ( ... ) time
     *              after the input is ready (if any at all), and await the
     *              remainder of the time on the output, in case it is also
     *              a Blocking or similar Incomplete result.
     *              Must not be null.
     *
     * @param logic The logic to perform on each result as it comes in,
     *              typically <code> AndTerms </code>
     *              or <code> OrTerms </code>.  Must not be null.
     *              Must always output a NonBlocking term.
     *
     * @param blocking_terms 0 or more Blocking terms to wait upon
     *                       before this new Blocking term's result
     *                       is finalized.  Must not be null.
     *                       Must not contain any null elements.
     */
    @SuppressWarnings("unchecked") // Generic varargs heap pollution.
    public Blocking (
                     Type<VALUE> type,
                     Term<?> cause,
                     Clock clock,
                     CumulativeOperation<VALUE, VALUE> logic,
                     Blocking<VALUE> ... blocking_terms
                     )
        throws ParametersMustNotBeNull.Violation,
               Parameter5.MustContainNoNulls.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               logic, blocking_terms, clock );
        classContracts.check ( Parameter5.MustContainNoNulls.CONTRACT,
                               blocking_terms );

        // First figure out the max timeout of the child Blocking terms.
        BigDecimal max_timeout_in_seconds = BigDecimal.ZERO;
        TermViolation violation = null;
        for ( Blocking<VALUE> child_blocking : blocking_terms )
        {
            final Result<VALUE> child_async =
                child_blocking.result ();
            final BigDecimal timeout_in_seconds =
                child_async.maxTimeoutInSeconds ();
            if ( timeout_in_seconds.compareTo ( max_timeout_in_seconds ) > 0 )
            {
                max_timeout_in_seconds = timeout_in_seconds;
            }

            if ( violation == null )
            {
                violation = child_blocking.termViolation ();
            }
        }

        if ( violation == null )
        {
            // Create a default timed out term violation.
            violation =
                TermMustBeNonBlocking.CONTRACT.violation ( this,
                                                            this );
        }

        this.result =
            new MultipleBlockingTermsResult<VALUE> ( type,
                                                     clock,
                                                     max_timeout_in_seconds,
                                                     logic,
                                                     blocking_terms );

        this.type = type;
        if ( cause == null )
        {
            this.cause = this;
        }
        else
        {
            this.cause = cause;
        }
        this.termViolation = violation;

        this.contracts = new Advocate ( this );
    }


    /**
     * @return The Result from this Blocking term.
     *         Protected.  Usable only by other Terms in this package
     *         as well as derived Blocking Terms.  The Result
     *         is not intended to be given out publicly.  Never null.
     */
    protected Result<VALUE> result ()
    {
        return this.result;
    }


    /**
     * @see musaico.foundation.term.Term#await(java.math.BigDecimal)
     *
     * <p>
     * Blocks until the result comes in, or until the specified
     * timeout period has elapsed, whichever comes first.
     * </p>
     */
    @Override
    public final NonBlocking<VALUE> await (
            BigDecimal timeout_in_seconds
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        // Make sure the Result has at least started working
        // toward a final value.
        // Could return an ErrorState if it refuses to start:
        this.result.start ();

        final NonBlocking<VALUE> awaited_value =
            this.result.await ( this,
                                timeout_in_seconds );

        return awaited_value;
    }


    /**
     * @see musaico.foundation.term.Term#blockingMaxSeconds()
     */
    @Override
    public final BigDecimal blockingMaxSeconds ()
    {
        return this.result.maxTimeoutInSeconds ();
    }


    /**
     * @see musaico.foundation.term.Term#cancel(musaico.foundation.term.TermViolation)
     */
    @Override
    public final NonBlocking<VALUE> cancel (
                                            TermViolation violation
                                            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        // Could return an ErrorState if it refuses to stop:
        this.result.stop ();

        // Return the final value of the result, which could
        // be Cancelled, or it could be a final value that was
        // completed before we got to it, and so on.
        final NonBlocking<VALUE> result =
            this.result.await ( this, BigDecimal.ZERO );

        return result;
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
     * @see musaico.foundation.term.incomplete.Incomplete#clock()
     */
    @Override
    public final Clock clock ()
        throws ReturnNeverNull.Violation
    {
        return this.result.clock ();
    }


    /**
     * @see musaico.foundation.term.Term#consequence()
     */
    @Override
    public final NonBlocking<?> consequence ()
        throws ReturnNeverNull.Violation
    {
        final NonBlocking<VALUE> partial_or_final_value =
            this.result.partialTerm ();

        return partial_or_final_value;
    }


    /**
     * @see musaico.foundation.term.Term#consequenceLeaf()
     */
    @Override
    public final Term<?> consequenceLeaf ()
        throws ReturnNeverNull.Violation
    {
        return this.consequence ().consequenceLeaf ();
    }


    /**
     * @see musaico.foundation.term.Term#countable()
     */
    @Override
    public final Countable<VALUE> countable ()
        throws ReturnNeverNull.Violation
    {
        // We cannot block here.
        if ( this.result.hasFinalTerm () )
        {
            // Finished blocking.  Delegate the call.
            final NonBlocking<VALUE> result =
                this.result.await ( this, BigDecimal.ZERO );
            return result.countable ();
        }
        else
        {
            // Not done blocking yet.  Return No countable values.
            return new No<VALUE> ( this.type,
                                   this,
                                   TermMustBeCountable.CONTRACT.violation (
                                          this,
                                          this,
                                          this.termViolation ) );
        }
    }


    /**
     * @see musaico.foundation.term.incomplete.Incomplete#elapsedTimeInSeconds()
     */
    @Override
    public final BigDecimal elapsedTimeInSeconds ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        return this.result.elapsedTimeInSeconds ();
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
        else if ( obj == null
             || ! ( obj instanceof Blocking ) )
        {
            return false;
        }

        Blocking<?> that = (Blocking<?>) obj;

        if ( this.result != that.result )
        {
            // Must actually be the same asynchronous result we're
            // waiting for, not one that is equal().
            return false;
        }

        // Everything is all matchy-matchy.
        return true;
    }


    /**
     * @see musaico.foundation.term.Term#filter()
     */
    @Override
    public final FilterState filter ()
    {
        // We cannot block here.
        if ( this.result.hasFinalTerm () )
        {
            // Finished blocking.  Delegate the call.
            final NonBlocking<VALUE> result =
                this.result.await ( this, BigDecimal.ZERO );
            return result.filter ();
        }
        else
        {
            // Not done blocking yet.  Return DISCARDED.
            return FilterState.DISCARDED;
        }
    }


    /**
     * @see musaico.foundation.term.Term#hasValue()
     */
    @Override
    public final boolean hasValue ()
    {
        // We cannot block here.
        if ( this.result.hasFinalTerm () )
        {
            // Finished blocking.  Delegate the call.
            final NonBlocking<VALUE> result =
                this.result.await ( this, BigDecimal.ZERO );
            return result.hasValue ();
        }
        else
        {
            // Not done blocking yet.  Return false.
            return false;
        }
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

        hash_code *= 17;

        return hash_code;
    }


    /**
     * @see musaico.foundation.term.Term#head()
     */
    @Override
    public final ZeroOrOne<VALUE> head ()
        throws ReturnNeverNull.Violation
    {
        // We cannot block here.
        if ( this.result.hasFinalTerm () )
        {
            // Finished blocking.  Delegate the call.
            final NonBlocking<VALUE> result =
                this.result.await ( this, BigDecimal.ZERO );
            return result.head ();
        }
        else if ( this.result.hasPartialTerm () )
        {
            // Didn't finish blocking, but there is at least something
            // waiting for us as a partial result.  Delegate the call.
            final NonBlocking<VALUE> partial_result =
                this.result.partialTerm ();
            return partial_result.head ();
        }
        else
        {
            // Not done blocking yet.  Return No head.
            return new No<VALUE> ( this.type,
                                   this.termViolation );
        }
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

        // We cannot block here.
        if ( this.result.hasFinalTerm () )
        {
            // Finished blocking.  Delegate the call.
            final NonBlocking<VALUE> result =
                this.result.await ( this, BigDecimal.ZERO );
            return result.head ( num_elements );
        }
        else if ( this.result.hasPartialTerm () )
        {
            // Didn't finish blocking, but there is at least something
            // waiting for us as a partial result.  Delegate the call.
            final NonBlocking<VALUE> partial_result =
                this.result.partialTerm ();
            return partial_result.head ( num_elements );
        }
        else
        {
            // Not done blocking yet.  Return No head.
            return new No<VALUE> ( this.type,
                                   this.termViolation );
        }
    }


    /**
     * @see musaico.foundation.term.Term#idempotent()
     */
    @Override
    public final Idempotent<VALUE> idempotent ()
    {
        // We cannot block here.
        final NonBlocking<VALUE> result_so_far;
        if ( this.result.hasFinalTerm () )
        {
            // Finished blocking.  Delegate the call.
            result_so_far =
                this.result.await ( this, BigDecimal.ZERO );
        }
        else if ( this.result.hasPartialTerm () )
        {
            // Didn't finish blocking, but there is at least something
            // waiting for us as a partial result.  Delegate the call.
            result_so_far = result.partialTerm ();
        }
        else
        {
            // Not done blocking yet and no partial result.
            result_so_far = new No<VALUE> ( this.type,
                                            this.termViolation );
        }

        final Idempotent<VALUE> idempotent = result_so_far.idempotent ();

        return idempotent;
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
     * @see musaico.foundation.term.Term#iterator()
     */
    @Override
    public final Iterator<VALUE> iterator ()
    {
        // We cannot block here.
        if ( this.result.hasFinalTerm () )
        {
            // Finished blocking.  Delegate the call.
            final NonBlocking<VALUE> result =
                this.result.await ( this, BigDecimal.ZERO );
            return result.iterator ();
        }
        else
        {
            // Not done blocking yet.  Return an empty Iterator.
            return new TermIterator<VALUE> ();
        }
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

        // We cannot block here.
        if ( this.result.hasFinalTerm () )
        {
            // Finished blocking.  Delegate the call.
            final NonBlocking<VALUE> result =
                this.result.await ( this, BigDecimal.ZERO );
            return result.orDefault ( default_value );
        }
        else
        {
            // Not done blocking yet.  Return the specified default.
            return default_value;
        }
    }


    /**
     * @see musaico.foundation.term.Term#orNone()
     */
    @Override
    public final VALUE orNone ()
    {
        // We cannot block here.
        if ( this.result.hasFinalTerm () )
        {
            // Finished blocking.  Delegate the call.
            final NonBlocking<VALUE> result =
                this.result.await ( this, BigDecimal.ZERO );
            return result.orNone ();
        }
        else
        {
            // Not done blocking yet.  Return none.
            return this.type.none ();
        }
    }


    /**
     * @see musaico.foundation.term.Term#orNull()
     */
    @Override
    public final VALUE orNull ()
    {
        // We cannot block here.
        if ( this.result.hasFinalTerm () )
        {
            // Finished blocking.  Delegate the call.
            final NonBlocking<VALUE> result =
                this.result.await ( this, BigDecimal.ZERO );
            return result.orNull ();
        }
        else
        {
            // Not done blocking yet.  Return null.
            return null;
        }
    }


    /**
     * @see musaico.foundation.term.Term#orPartial()
     */
    @Override
    public final NonBlocking<VALUE> orPartial ()
    {
        // Return the final result, if available; otherwise the current
        // partial result, if any; otherwise No value.
        return this.result.partialTerm ()
            .orPartial ();
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

        // We cannot block here.
        if ( this.result.hasFinalTerm () )
        {
            // Finished blocking.  Delegate the call.
            final NonBlocking<VALUE> result =
                this.result.await ( this, BigDecimal.ZERO );
            return result.orThrow ( exception_class );
        }

        // Not done blocking yet.  Throw an exception.
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

        // We cannot block here.
        if ( this.result.hasFinalTerm () )
        {
            // Finished blocking.  Delegate the call.
            final NonBlocking<VALUE> result =
                this.result.await ( this, BigDecimal.ZERO );
            return result.orThrow ( exception );
        }

        // Not done blocking yet.  Throw the exception.
        if ( exception.getCause () == null )
        {
            exception.initCause ( this.termViolation () );
        }

        throw exception;
    }


    /**
     * @see musaico.foundation.term.Term#orThrowChecked()
     */
    @Override
    public final VALUE orThrowChecked ()
        throws CheckedTermViolation
    {
        // We cannot block here.
        if ( this.result.hasFinalTerm () )
        {
            // Finished blocking.  Delegate the call.
            final NonBlocking<VALUE> result =
                this.result.await ( this, BigDecimal.ZERO );
            return result.orThrowChecked ();
        }
        else
        {
            // Not done blocking yet.  Throw our term violation.
            throw new CheckedTermViolation ( this.termViolation );
        }
    }


    /**
     * @see musaico.foundation.term.Term#orThrowUnchecked()
     */
    @Override
    public final VALUE orThrowUnchecked ()
        throws TermViolation
    {
        // We cannot block here.
        if ( this.result.hasFinalTerm () )
        {
            // Finished blocking.  Delegate the call.
            final NonBlocking<VALUE> result =
                this.result.await ( this, BigDecimal.ZERO );
            return result.orThrowUnchecked ();
        }
        else
        {
            // Not done blocking yet.  Throw an unchecked violation.
            throw this.termViolation;
        }
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

        // We cannot block here.
        if ( this.result.hasFinalTerm () )
        {
            // Finished blocking.  Delegate the call.
            final NonBlocking<VALUE> result =
                this.result.await ( this, BigDecimal.ZERO );
            return result.orViolation ( contract, plaintiff, evidence );
        }

        // Not done blocking yet.  Throw a violation of the contract.
        final VIOLATION violation =
            contract.violation ( plaintiff,
                                 evidence );
        violation.initCause ( this.termViolation );
        throw violation;
    }


    /**
     * @see musaico.foundation.term.Term#pipe(musaico.foundation.term.Operation)
     *
     * <p>
     * Queues up the specified Operation to handle the result
     * asynchronously, whenever it comes in.
     * </p>
     *
     * <p>
     * NOTE: If the result has already been asynchronously set, then
     * the specified Operation will handle the result
     * immediately, in this thread.
     * </p>
     *
     * @return Either a new Expression, awaiting this term's
     *         final result before applying the specified operation;
     *         or, if this term is already finished blocking,
     *         then the processed final result right away.
     *         Never null.
     */
    @Override
    @SuppressWarnings("unchecked") // Generic varargs creation result.async(o).
    public final <OUTPUT extends Object>
        Term<OUTPUT> pipe (
                           Operation<VALUE, OUTPUT> operation
                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               operation );

        // Make sure the Result has at least started working
        // toward a final value.
        // Could return an ErrorState if it refuses to start:
        this.result.start ();

        final Term<OUTPUT> expression_or_final_output =
            this.result.async ( this,
                                operation );

        return expression_or_final_output;
    }


    /**
     * @see java.lang.toString()
     */
    @Override
    public final String toString ()
    {
        if ( this.contracts == null ) // Constructor time only.
        {
            return ClassName.of ( this.getClass () );
        }

        StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( ClassName.of ( this.getClass () ) );
        sbuf.append ( "<" );
        sbuf.append ( this.type );
        sbuf.append ( ">" );

        return sbuf.toString ();
    }


    /**
     * @see musaico.foundation.term.Term#type()
     */
    @Override
    public final Type<VALUE> type ()
    {
        return this.type;
    }


    /**
     * @see musaico.foundation.term.NotOne#termViolation()
     */
    @Override
    public final TermViolation termViolation ()
        throws ReturnNeverNull.Violation
    {
        this.contracts.check ( ReturnNeverNull.CONTRACT,
                               this.termViolation );

        return this.termViolation;
    }
}
