package musaico.foundation.term.abnormal;

import java.io.Serializable;

import java.lang.reflect.Constructor;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Iterator;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Violation;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.term.CheckedTermViolation;
import musaico.foundation.term.Countable;
import musaico.foundation.term.Idempotent;
import musaico.foundation.term.Just;
import musaico.foundation.term.NonBlocking;
import musaico.foundation.term.NotOne;
import musaico.foundation.term.Operation;
import musaico.foundation.term.Select;
import musaico.foundation.term.Type;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;

import musaico.foundation.term.finite.No;

import musaico.foundation.term.iterators.TermIterator;


/**
 * <p>
 * A single term, but one with a warning (Contract Violation) attached.
 * </p>
 *
 * <p>
 * A Warning term is more than No value, but less than One value
 * because of the contract Violation.
 * So it is treated more or less as a failed result, although
 * <code> hasValue () </code> returns true.  The caller can
 * invoke <code> orPartial () </code> to convert this Warning term
 * into a whole One value, dropping the warning Violation.
 * </p>
 *
 *
 * <p>
 * In Java every conditional Term must be Serializable in order to
 * play nicely across RMI.  However users of the conditional Term
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
 * @see musaico.foundation.term.abnormal.MODULE#COPYRIGHT
 * @see musaico.foundation.term.abnormal.MODULE#LICENSE
 */
public class Warning<VALUE extends Object>
    implements Abnormal<VALUE>, Idempotent<VALUE>, NonBlocking<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces constructor parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( Warning.class );


    // Enforces parameter obligations and so on for us.
    private final Advocate contracts;

    // The type of the conditional Term.
    private final Type<VALUE> type;

    // The cause of this term, such as an Unidempotent term of which
    // this is an Idempotent snapshot; or a Warning or Partial value;
    // and so on.  This term can be its own cause.
    private final Term<?> cause;

    // The TermViolation which will be thrown when the caller
    // asks for a single value with <code> orThrowUnchecked () </code>.
    private final TermViolation warning;

    // The term which generated warnings, such as One value
    // or Many values, and so on.
    private final Just<VALUE> consequence;


    /**
     * <p>
     * Creates a new Warning term.
     * </p>
     *
     * @param type The Type of the conditional Term.
     *             Must not be null.
     *
     * @param violation The contract Violation which led to only a
     *                  Warning result.  Must not be null.
     *
     * @param consequence The wrapped Term, which can be treated as
     *                    a successful result if the caller chooses,
     *                    since this is only a Warning.  Must not be null.
     */
    public <VIOLATION extends Throwable & Violation>
        Warning (
                 Type<VALUE> type,
                 VIOLATION violation,
                 Just<VALUE> consequence
                 )
        throws ParametersMustNotBeNull.Violation
    {
        this ( type,
               null, // cause
               violation,
               consequence );
    }


    /**
     * <p>
     * Creates a new Warning, as a consequence of the specified
     * cause term.
     * </p>
     *
     * @param cause The cause of this term, such as a
     *              Blocking operation which did not fail, but did
     *              generate warning signs, and so on.
     *              Must not be null.
     *
     * @param consequence The wrapped Term, which can be treated as
     *                    a successful result if the caller chooses,
     *                    since this is only a Warning.  Must not be null.
     */
    public Warning (
                    NotOne<?> cause,
                    Just<VALUE> consequence
                    )
        throws ParametersMustNotBeNull.Violation
    {
        this ( consequence == null
                   ? null
                   : consequence.type (),
               cause,
               cause == null
                   ? null
                   : cause.termViolation (),
               consequence );
    }


    /**
     * <p>
     * Creates a new Warning term, better than No value but less than
     * One.
     * </p>
     *
     * @param type The Type of the conditional Term.
     *             Must not be null.
     *
     * @param cause The optional cause of this term, such as a
     *              non-Idempotent term of which this is a snapshot,
     *              or a Warning, or a Partial value, and so on.
     *              If null, then this term is its own cause.
     *              Can be null.
     *
     * @param violation The contract Violation which led to only a
     *                  Warning result.  Must not be null.
     *
     * @param consequence The wrapped Term, which can be treated as
     *                    a successful result if the caller chooses,
     *                    since this is only a Warning.  Must not be null.
     */
    public <VIOLATION extends Throwable & Violation>
        Warning (
                 Type<VALUE> type,
                 Term<?> cause, // Can be null.
                 VIOLATION violation,
                 Just<VALUE> consequence
                 )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               type, consequence );

        this.type = type;
        if ( cause == null )
        {
            this.cause = this;
        }
        else
        {
            this.cause = cause;
        }

        if ( violation instanceof TermViolation )
        {
            this.warning = (TermViolation) violation;
        }
        else
        {
            this.warning =
                new TermViolation ( violation );
        }

        this.consequence = consequence;

        this.contracts = new Advocate ( this );
    }


    /**
     * @see musaico.foundation.term.Term#await(java.math.BigDecimal)
     */
    @Override
    public final Warning<VALUE> await (
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
    public final BigDecimal blockingMaxSeconds ()
        throws ReturnNeverNull.Violation
    {
        return BigDecimal.ZERO;
    }


    /**
     * @see musaico.foundation.term.Term#cancel(musaico.foundation.term.TermViolation)
     */
    @Override
    public final Warning<VALUE> cancel (
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
    public final Just<VALUE> consequence ()
        throws ReturnNeverNull.Violation
    {
        return this.consequence;
    }


    /**
     * @see musaico.foundation.term.Term#consequenceLeaf()
     */
    @Override
    public final Term<?> consequenceLeaf ()
        throws ReturnNeverNull.Violation
    {
        return this.consequence.consequenceLeaf ();
    }


    /**
     * @see musaico.foundation.term.Term#countable()
     */
    @Override
    public final No<VALUE> countable ()
        throws ReturnNeverNull.Violation
    {
        return new No<VALUE> ( this );
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
             || ! ( obj instanceof Warning ) )
        {
            return false;
        }

        Warning<?> that = (Warning<?>) obj;
        if ( ! this.type.elementClass ().equals (
                   that.type.elementClass () ) )
        {
            return false;
        }

        if ( this.warning == null )
        {
            if ( that.warning != null )
            {
                return false;
            }
        }
        else if ( that.warning == null )
        {
            return false;
        }
        else if ( ! this.warning.equals ( that.warning ) )
        {
            return false;
        }

        if ( this.consequence == null )
        {
            if ( that.consequence != null )
            {
                return false;
            }
        }
        else if ( that.consequence == null )
        {
            return false;
        }
        else if ( ! this.consequence.equals ( that.consequence ) )
        {
            return false;
        }

        // Everything is equal.
        return true;
    }


    /**
     * @see musaico.foundation.term.Term#filter()
     */
    @Override
    public final FilterState filter ()
    {
        // Not a whole One result, so return DISCARDED.
        return FilterState.DISCARDED;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        int hash_code = 0;
        hash_code += ( this.type == null ? 0 : this.type.hashCode () );
        hash_code += ( this.consequence == null ? 0 : this.consequence.hashCode () );
        hash_code += ( this.warning == null ? 0 : ClassName.of ( this.warning.getClass () ).hashCode () );

        return hash_code;
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
     * @see musaico.foundation.term.Term#head()
     */
    @Override
    public final No<VALUE> head ()
        throws ReturnNeverNull.Violation
    {
        // The caller must ask for <code> orPartial () </code> in order
        // to return the head of the one value.
        return new No<VALUE> ( this.type,          // type
                               this.warning );     // violation
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

        // Just use the single head () method.
        return this.head ();
    }


    /**
     * @see musaico.foundation.term.Term#idempotent()
     */
    @Override
    public final Warning<VALUE> idempotent ()
    {
        return this;
    }


    /**
     * @see musaico.foundation.term.Term#indefiniteIterator(long)
     */
    @Override
    public final TermIterator<VALUE> indefiniteIterator (
                                                          long maximum_iterations
                                                          )
        throws Parameter1.MustBeGreaterThanZero.Violation
    {
        return this.iterator ();
    }


    /**
     * @see musaico.foundation.term.Term#iterator()
     */
    public final TermIterator<VALUE> iterator ()
    {
        // Do not iterate over a Warning result.
        // The caller must ask for <code> orPartial () </code> in order
        // to iterate over the one value.
        return new TermIterator<VALUE> ();
    }


    /**
     * @see musaico.foundation.term.Term#orDefault(java.lang.Object)
     */
    @Override
    public final VALUE orDefault (
                                  VALUE default_term
                                  )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               default_term );

        // Expecting a result with no warning.  Return the specified default.
        return default_term;
    }


    /**
     * @see musaico.foundation.term.Term#orNone()
     */
    @Override
    public final VALUE orNone ()
    {
        // Expecting a result with no warning.  Return the none object.
        return this.type.none ();
    }


    /**
     * @see musaico.foundation.term.Term#orNull()
     */
    @Override
    public final VALUE orNull ()
    {
        // Expecting a result with no warning.  Return null.
        return null;
    }


    /**
     * @see musaico.foundation.term.Term#orPartial()
     */
    @Override
    public final Just<VALUE> orPartial ()
    {
        return this.consequence;
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
                constructor.newInstance ( this.warning.getMessage () );
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

        exception.initCause ( this.warning );

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
            exception.initCause ( this.warning );
        }

        // Expecting a result with no warning.  Throw the specified exception.
        throw exception;
    }


    /**
     * @see musaico.foundation.term.Term#orThrowChecked()
     */
    @Override
    public final VALUE orThrowChecked ()
        throws CheckedTermViolation
    {
        // Expecting a result with no warning.  Throw the TermViolation.
        throw new CheckedTermViolation ( this.warning );
    }


    /**
     * @see musaico.foundation.term.Term#orThrowUnchecked()
     */
    @Override
    public final VALUE orThrowUnchecked ()
        throws TermViolation
    {
        // Expecting a result with no warning.  Throw a runtime exception.
        throw this.warning;
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
        violation.initCause ( this.warning );
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
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return "" + ClassName.of ( this.getClass () )
            + " ( " + this.consequence + " )";
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
                               this.warning );

        return this.warning;
    }
}
