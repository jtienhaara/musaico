package musaico.foundation.term.abnormal;

import java.io.Serializable;

import java.lang.reflect.Constructor;

import java.math.BigDecimal;

import java.util.ArrayList;


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
 * An empty Term caused by an error (such as invalid inputs to
 * a function).
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
 * @see musaico.foundation.term.abnormal.MODULE#COPYRIGHT
 * @see musaico.foundation.term.abnormal.MODULE#LICENSE
 */
public class Error<VALUE extends Object>
    implements Abnormal<VALUE>, Idempotent<VALUE>, NonBlocking<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( Error.class );


    // Enforces parameter obligations and so on for us.
    private final Advocate contracts;

    // The type of the conditional value.
    private final Type<VALUE> type;

    // The cause of this term, such as an Unidempotent term of which
    // this is an Idempotent snapshot; or a Warning or Partial value;
    // and so on.  This value can be its own cause.
    private final Term<?> cause;

    // The violation which caused this Error.
    private final TermViolation termViolation;


    /**
     * <p>
     * Creates a new Error failure, caused by the specified violation
     * of some contract.
     * </p>
     *
     * @param type The Type that the caller was hoping to retrieve.
     *             Must not be null.
     *
     * @param term_violation The exception which caused this Error failure.
     *                       Can be null.
     */
    public <VIOLATION extends Throwable & Violation>
        Error (
               Type<VALUE> type,
               VIOLATION violation
               )
        throws ParametersMustNotBeNull.Violation
    {
        this ( type,
               null, // cause
               violation );
    }


    /**
     * <p>
     * Creates a new Error failure, as a consequence of the specified
     * cause term.
     * </p>
     *
     * @param cause The cause of this term, such as a
     *              Blocking operation which failed, and so on.
     *              Must not be null.
     */
    public Error (
                  NotOne<VALUE> cause
                  )
        throws ParametersMustNotBeNull.Violation
    {
        this ( cause == null
                   ? null
                   : cause.type (),
               cause,
               cause == null
                   ? null
                   : cause.termViolation () );
    }


    /**
     * <p>
     * Creates a new Error failure, caused by the specified violation
     * of some contract.
     * </p>
     *
     * @param type The Type that the caller was hoping to retrieve.
     *             Must not be null.
     *
     * @param cause The optional cause of this term, such as a
     *              non-Idempotent term of which this is a snapshot,
     *              or a Warning, or a Partial term, and so on.
     *              If null, then this term is its own cause.
     *              Can be null.
     *
     * @param term_violation The exception which caused this Error failure.
     *                       Can be null.
     */
    public <VIOLATION extends Throwable & Violation>
        Error (
               Type<VALUE> type,
               Term<?> cause, // Can be null.
               VIOLATION violation
               )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               type,
                               violation );

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
            this.termViolation = (TermViolation) violation;
        }
        else
        {
            this.termViolation = new TermViolation ( violation );
        }

        this.contracts = new Advocate ( this );
    }


    /**
     * @see musaico.foundation.term.Term#await(java.math.BigDecimal)
     */
    @Override
    public final Error<VALUE> await (
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
    public final Error<VALUE> cancel (
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
    public final No<VALUE> consequence ()
        throws ReturnNeverNull.Violation
    {
        return new No<VALUE> ( this );
    }


    /**
     * @see musaico.foundation.term.Term#consequenceLeaf()
     */
    @Override
    public final No<VALUE> consequenceLeaf ()
        throws ReturnNeverNull.Violation
    {
        return this.consequence ();
    }


    /**
     * @see musaico.foundation.term.Term#countable()
     */
    @Override
    public final No<VALUE> countable ()
        throws ReturnNeverNull.Violation
    {
        return this.consequence ();
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
             || obj.getClass () != this.getClass () )
        {
            return false;
        }

        Error<?> that = (Error<?>) obj;
        if ( ! this.type.elementClass ().equals (
                   that.type.elementClass () ) )
        {
            return false;
        }

        // Everything matches.
        return true;
    }


    /**
     * @see musaico.foundation.term.Term#filter()
     */
    @Override
    public final FilterState filter ()
    {
        return FilterState.DISCARDED;
    }


    /**
     * @see musaico.foundation.term.Term#hasValue()
     */
    @Override
    public final boolean hasValue ()
    {
        return false;
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
    public final No<VALUE> head ()
        throws ReturnNeverNull.Violation
    {
        return new No<VALUE> ( this.type,              // type
                               this.termViolation );  // violation
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
    public final Error<VALUE> idempotent ()
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

        return default_term;
    }


    /**
     * @see musaico.foundation.term.Term#orNone()
     */
    @Override
    public final VALUE orNone ()
    {
        return this.type.none ();
    }


    /**
     * @see musaico.foundation.term.Term#orNull()
     */
    @Override
    public final VALUE orNull ()
    {
        return null;
    }


    /**
     * @see musaico.foundation.term.Term#orPartial()
     */
    @Override
    public final Error<VALUE> orPartial ()
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

        throw exception;
    }


    /**
     * @see musaico.foundation.term.Term#orThrowChecked()
     */
    @Override
    public final VALUE orThrowChecked ()
        throws CheckedTermViolation
    {
        throw new CheckedTermViolation ( this.termViolation );
    }


    /**
     * @see musaico.foundation.term.Term#orThrowUnchecked()
     */
    @Override
    public final VALUE orThrowUnchecked ()
        throws TermViolation
    {
        throw this.termViolation;
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
        violation.initCause ( this.termViolation );
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
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               operation );

        Term<OUTPUT> operation_result =
            operation.apply ( this );

        this.contracts.check ( ReturnNeverNull.CONTRACT,
                               operation_result );

        return operation_result;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        if ( this.contracts == null ) // Constructor time only.
        {
            return ClassName.of ( this.getClass () );
        }

        // Print all details about the violation, the same way we
        // would if we printed the violation and its underlying contract
        // and so on directly.
        return ClassName.of ( this.getClass () )
            + "<" + this.type + ">: "
            + this.termViolation ();
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
