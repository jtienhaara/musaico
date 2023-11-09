package musaico.foundation.term.incomplete;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Iterator;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.time.Clock;

import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;
import musaico.foundation.term.Type;

import musaico.foundation.term.abnormal.AbstractWrapped;


/**
 * <p>
 * One or more value(s), but not all the caller asked for.
 * </p>
 *
 * <p>
 * For example, if a caller requests to be given permissions to
 * read, write and execute a file, the response might come back
 * as Partial permissions -- with only read and execute granted,
 * for example.
 * </p>
 *
 * <p>
 * Or while waiting for a Blocking Term to complete, a caller might
 * periodically receive the Partial values so far.
 * </p>
 *
 * <p>
 * And so on.
 * </p>
 *
 * <p>
 * The partial values can be turned into a complete Term by calling
 * <code> unwrap () </code> on the Partial Term.
 * </p>
 *
 * <p>
 * A Partial value does not change over time, so it is CompletionImpossible,
 * unlike a Blocking or similar term, which might eventually complete.
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
public class Partial<VALUE extends Object>
    extends AbstractWrapped<VALUE, Term<VALUE>> // A Partial can wrap anything.
    implements CompletionImpossible<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( Partial.class );


    // The clock that measured the elapsed time in seconds.
    private final Clock clock;

    // The amount of time that elapsed before this partial result
    // was created.
    private final BigDecimal elapsedTimeInSeconds;


    /**
     * <p>
     * Creates a new Partial term.
     * </p>
     *
     * @param type The Type of this Term,, such as a Type&lt;String&gt;
     *              for a Term of Strings, and so on.
     *              Must not be null.
     *
     * @param wrapped The partial results.  Must not be null.
     */
    public Partial (
            Type<VALUE> type,
            Term<VALUE> wrapped
            )
        throws ParametersMustNotBeNull.Violation
    {
        this ( type,                   // type
               wrapped,                // wrapped
               Clock.STANDARD,         // clock
               BigDecimal.ZERO );      // elapsed_time_in_seconds
    }


    /**
     * <p>
     * Creates a new Partial term.
     * </p>
     *
     * @param type The Type of this Term,, such as a Type&lt;String&gt;
     *              for a Term of Strings, and so on.
     *              Must not be null.
     *
     * @param wrapped The partial results.  Must not be null.
     *
     * @param cause The Incomplete Term which caused this partial result,
     *              such as a Blocking term.  Must not be null.
     */
    public Partial (
            Type<VALUE> type,
            Term<VALUE> wrapped,
            Incomplete<VALUE> cause
            )
        throws ParametersMustNotBeNull.Violation
    {
        this ( type,                   // type
               wrapped,                // wrapped
               cause == null           // clock
                   ? null
                   : cause.clock (),
               cause == null           // elapsed_time_in_seconds
                   ? null
               : cause.elapsedTimeInSeconds () );
    }


    /**
     * <p>
     * Creates a new Partial term.
     * </p>
     *
     * @param type The Type of this Term,, such as a Type&lt;String&gt;
     *              for a Term of Strings, and so on.
     *              Must not be null.
     *
     * @param wrapped The partial results.  Must not be null.
     *
     * @param clock The clock by which the elapsed time is measured,
     *              such as Clock.STANDARD.  Must not be null.
     *
     * @param elapsed_time_in_seconds How many seconds elapsed before this
     *                                Partial result was generated,
     *                                if this was caused by a timeout
     *                                or similar event; or BigDecimal.ZERO
     *                                if either no time elapsed, or there
     *                                was no time involved in the result
     *                                (such as when Partial permissions
     *                                are returned from a security request).
     *                                Must not be null.
     */
    public Partial (
            Type<VALUE> type,
            Term<VALUE> wrapped,
            Clock clock,
            BigDecimal elapsed_time_in_seconds
            )
        throws ParametersMustNotBeNull.Violation
    {
        super ( type,                  // type
                wrapped );             // wrapped

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               clock, elapsed_time_in_seconds );

        this.clock = clock;
        this.elapsedTimeInSeconds = elapsed_time_in_seconds;
    }


    /**
     * @see musaico.foundation.term.incomplete.Incomplete#clock()
     */
    @Override
    public final Clock clock ()
        throws ReturnNeverNull.Violation
    {
        return this.clock;
    }


    /**
     * @see musaico.foundation.term.TermPipeline#duplicate()
     */
    @Override
    public final Partial<VALUE> duplicate ()
        throws ReturnNeverNull.Violation
    {
        return new Partial<VALUE> ( this.type (),
                                    this.unwrap () );
    }


    /**
     * @see musaico.foundation.term.incomplete.Incomplete#elapsedTimeInSeconds()
     */
    @Override
    public final BigDecimal elapsedTimeInSeconds ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        return this.elapsedTimeInSeconds;
    }


    /**
     * @see musacico.foundation.term.abnormal.Abnormal#violation()
     */
    @Override
    public final TermViolation violation ()
        throws ReturnNeverNull.Violation
    {
        return TermMustNotBePartial.CONTRACT.violation (
                   this,              // plaintiff
                   this );            // evidence
    }
}
