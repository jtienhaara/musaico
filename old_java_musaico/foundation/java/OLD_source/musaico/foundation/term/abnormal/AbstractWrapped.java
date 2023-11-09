package musaico.foundation.term.abnormal;

import java.io.Serializable;

import java.util.Iterator;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.FilterState;

import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;
import musaico.foundation.term.Type;

import musaico.foundation.term.contracts.TermFilterStateMustNotBeKept;


/**
 * <p>
 * Implements boilerplate functionality for Wrapped terms.
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
 * @see musaico.foundation.term.abnormal.MODULE#COPYRIGHT
 * @see musaico.foundation.term.abnormal.MODULE#LICENSE
 */
public abstract class AbstractWrapped<VALUE extends Object, WRAPPED extends Term<VALUE>>
    extends AbstractAbnormal<VALUE>
    implements Wrapped<VALUE, WRAPPED>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( AbstractWrapped.class );


    // The wrapped Term.
    private final WRAPPED wrapped;


    /**
     * <p>
     * Creates a new AbstractWrapped term whose <code> filterState () </code>
     * is <code> FilterState.DISCARDED </code>.
     * </p>
     *
     * @param type The Type of this Term,, such as a Type&lt;String&gt;
     *              for a Term of Strings, and so on.
     *              Must not be null.
     *
     * @param wrapped The Term wrapped by this.  Must not be null.
     */
    public AbstractWrapped (
            Type<VALUE> type,
            WRAPPED wrapped
            )
        throws ParametersMustNotBeNull.Violation
    {
        super ( type ); // type

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               wrapped );

        this.wrapped = wrapped;
    }


    /**
     * <p>
     * Creates a new AbstractWrapped term.
     * </p>
     *
     * @param type The Type of this Term,, such as a Type&lt;String&gt;
     *              for a Term of Strings, and so on.
     *              Must not be null.
     *
     * @param discarded The FilterState of this Term, such as
     *                  <code> FilterState.DISCARDED </code>.
     *                  Must always return <code> isKept () == false </code>.
     *                  Must not be null.
     *
     * @param wrapped The Term wrapped by this.  Must not be null.
     */
    public AbstractWrapped (
            Type<VALUE> type,
            FilterState discarded,
            WRAPPED wrapped
            )
        throws ParametersMustNotBeNull.Violation,
               TermFilterStateMustNotBeKept.Violation
    {
        super ( type,        // type
                discarded ); // discarded

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               wrapped );

        this.wrapped = wrapped;
    }


    // Every Sink must implement
    // musaico.foundation.pipeline.Sink#duplicate()


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     *
     * Can be overridden by derived classes.
     */
    @Override
    public boolean equals (
            Object object
            )
    {
        if ( object == null )
        {
            return false;
        }
        else if ( object == this )
        {
            return true;
        }
        else if ( this.getClass () != object.getClass () )
        {
            return false;
        }

        final AbstractWrapped<?, ?> that = (AbstractWrapped<?, ?>) object;
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

        final TermViolation this_violation = this.violation ();
        final TermViolation that_violation = that.violation ();
        if ( this_violation == null )
        {
            if ( that_violation != null )
            {
                return false;
            }
        }
        else if ( that_violation == null )
        {
            return  false;
        }
        else if ( ! this_violation.equals ( that_violation ) )
        {
            return false;
        }

        if ( this.wrapped == null )
        {
            if ( that.wrapped != null )
            {
                return false;
            }
        }
        else if ( that.wrapped == null )
        {
            return  false;
        }
        else if ( ! this.wrapped.equals ( that.wrapped ) )
        {
            return false;
        }

        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     *
     * Can be overridden by derived classes.
     */
    @Override
    public final int hashCode ()
    {
        return 31 * this.getClass ().getName ().hashCode ()
            + 17 * this.wrapped.hashCode ()
            + this.type ().hashCode ();
    }


    /**
     * @see java.lang.Object#toString()
     *
     * Can be overridden by derived classes.
     */
    @Override
    public String toString ()
    {
        return "" + this.type ()
            + " [ " + ClassName.of ( this.getClass () ) + " ]"
            + " { "
            + this.wrapped
            +" }";
    }


    /**
     * @see musaico.foundation.term.abnormal.Wrapped#unwrap()
     */
    @Override
    public final WRAPPED unwrap ()
        throws ReturnNeverNull.Violation
    {
        return this.wrapped;
    }


    // Every Abnormal must implement
    // musacico.foundation.term.abnormal.Abnormal#violation()
}
