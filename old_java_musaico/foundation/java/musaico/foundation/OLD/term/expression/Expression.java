package musaico.foundation.term.expression;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Collection;
import java.util.Iterator;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.domains.time.Clock;

import musaico.foundation.term.Term;
import musaico.foundation.term.TermPipeline;

import musaico.foundation.term.incomplete.AbstractReducible;


/**
 * <p>
 * A lazily evaluated Term containing some TermPipeline.
 * </p>
 *
 * <p>
 * An Expression can be used, for example, to (eventually)
 * pass an input Term to a sequence of math operators,
 * or to an Operation which casts to another type, and so on.
 * </p>
 *
 * <p>
 * An Expression is guaranteed to produce a result.  However that result
 * might, itself, be incomplete.  For example, an Expression which
 * casts a Partial term to another Type might result in another
 * Partial term.  Or an Expression might produce another Expression,
 * or a Blocking term, and so on.
 * </p>
 *
 * <p>
 * An Expression produces a result each time it is
 * <code> reduce () </code>ed.  The result might be different each time,
 * depending on the Expression.  For example, an Expression which
 * returns a Term containing the number of seconds elapsed since
 * its input Term was first created would return a value that
 * increases monotonically over time.
 * </p>
 *
 *
 * <p>
 * In Java every conditional Term must be Serializable in order to
 * play nicely across RMI.  However users of the conditional Terms
 * must be careful, since the terms and expected data stored inside
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
 * @see musaico.foundation.term.expression.MODULE#COPYRIGHT
 * @see musaico.foundation.term.expression.MODULE#LICENSE
 */
public class Expression<VALUE extends Object>
    extends AbstractReducible<VALUE, Term<VALUE>>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( Expression.class );


    // Enforces parameter obligations and so on for us.
    private final Advocate contracts;

    // Lock critical sections on this token:
    private final Serializable lock = new String ( "lock" );

    // The TermPipeline to execute.  Contains the input Term and
    // the OperationPipeline which will be applied to the input.
    private final TermPipeline.TermSink<VALUE> pipeline;

    // The Clock used to measure start time and elapsed time.
    // Also used to calculate the remainder of await ( ... ) time
    // after the input is ready (if any at all), and await the
    // remainder of the time on the output, in case it is also
    // a Blocking or similar Incomplete result.
    private final Clock clock;

    // MUTABLE:
    // When this Expression first started await ( ... ), waiting
    // for the input term to complete (since the input could be
    // another Expression, or a Blocking term, and so on).
    // Always null until await ( ... ) is called on the input term.
    private BigDecimal startTimeInSeconds = null;

    // MUTABLE:
    // How long processing took, from the start time (when await ( ... )
    // started waiting for the input term to complete) until the
    // operation(s) were applied to the input in order to produce the
    // final output of this Expression.  Always null until processing
    // has finished.
    private BigDecimal elapsedTimeInSeconds = null;


    /**
     * <p>
     * Creates a new Expression, passing a term through one or more
     * Operation(s).
     * </p>
     *
     * @param pipeline The input Term and Operations to apply to the input
     *                 each time <code> reduce () </code> is called.
     *                 A defensive copy is made, so that the pipeline
     *                 of this Expression cannot change over time.
     *                 Must not be null.
     *
     * @param clock The Clock used to measure start time and elapsed time.
     *              Also used to calculate the remainder of await ( ... ) time
     *              after the input is ready (if any at all), and await the
     *              remainder of the time on the output, in case it is also
     *              a Blocking or similar Incomplete result.
     *              Must not be null.
     */
    public Expression (
            TermPipeline.TermSink<VALUE> pipeline,
            Clock clock
            )
        throws ParametersMustNotBeNull.Violation
    {
        super ( pipeline == null // type
                    ? null
                    : pipeline.type () );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               pipeline, clock );

        this.pipeline = pipeline.duplicate ();
        this.clock = clock;

        this.contracts = new Advocate ( this );
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
     * @see musaico.foundation.term.TermPipeline.TermSink#duplicate()
     */
    @Override
    public final Expression<VALUE> duplicate ()
        throws ReturnNeverNull.Violation
    {
        return new Expression<VALUE> ( this.pipeline, // pipeline
                                       this.clock );  // clock
    }


    /**
     * @see musaico.foundation.term.incomplete.Incomplete#elapsedTimeInSeconds()
     */
    @Override
    public final BigDecimal elapsedTimeInSeconds ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        final BigDecimal start_time;
        synchronized ( this.lock )
        {
            if ( this.startTimeInSeconds == null )
            {
                // Processing has not yet started.
                return BigDecimal.ZERO;
            }

            if ( this.elapsedTimeInSeconds != null )
            {
                return this.elapsedTimeInSeconds;
            }

            start_time = this.startTimeInSeconds;
        }

        // Processing has started but is still underway.
        final BigDecimal current_time =
            this.clock.currentTimeInSeconds ();
        final BigDecimal elapsed_time_so_far =
            current_time.subtract ( start_time );

        return elapsed_time_so_far;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
            Object obj
            )
    {
        if ( ! super.equals ( obj ) )
        {
            return false;
        }
        else if ( ! ( obj instanceof Expression ) )
        {
            return false;
        }

        Expression<?> that = (Expression<?>) obj;

        if ( this.pipeline == null )
        {
            if ( that.pipeline != null )
            {
                return false;
            }
        }
        else if ( that.pipeline == null )
        {
            return false;
        }
        else if ( ! this.pipeline.equals ( that.pipeline ) )
        {
            return false;
        }

        // We don't care about the clock.
        // Everything else is all matchy-matchy.
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
        hash_code *= 13;
        hash_code += ( this.pipeline == null ? 0 : this.pipeline.hashCode () );

        return hash_code;
    }


    /**
     * @return The input Term to this Expression.  Never null.
     */
    public final Term<?> inputTerm ()
        throws ReturnNeverNull.Violation
    {
        TermPipeline.TermSink<?> input = this.pipeline.input ();
        int infinite_loop_protector = 0;
        while ( ! ( input instanceof Term ) )
        {
            if ( input.input () == input )
            {
                // Uh-oh.  Somehow there is no input Term.
                throw ReturnNeverNull.CONTRACT.violation (
                          this,    // plaintiff
                          null );  // evidence
            }

            input = input.input ();

            infinite_loop_protector ++;
            if ( infinite_loop_protector > 4096 )
            {
                throw ReturnNeverNull.CONTRACT.violation (
                          this,    // plaintiff
                          null );  // evidence
            }
        }

        return (Term<?>) input;
    }


    /**
     * @return A defensive copy of this Expression's TermPipeline.
     *         Never null.
     */
    public final TermPipeline.TermSink<VALUE> pipeline ()
        throws ReturnNeverNull.Violation
    {
        return this.pipeline.duplicate ();
    }


    /**
     * @see musacico.foundation.term.incomplete.Reducible#reduce()
     */
    @Override
    public final Term<VALUE> reduce ()
        throws ReturnNeverNull.Violation
    {
        final Term<VALUE> output = this.pipeline.output ();
        return output;
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

        return ClassName.of ( this.getClass () )
            + "(" + this.pipeline + ")";
    }
}
