package musaico.foundation.term.abnormal;

import java.io.Serializable;

import java.util.Iterator;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;
import musaico.foundation.term.Type;


/**
 * <p>
 * An Error, typically caused by feeding an input into an Operation
 * which does not meet its input Type, or by configuring
 * a Pipeline with a selection or filter that is impossible for
 * a given input Term, such as selecting the middle element
 * of an empty Term).
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
public class Error<VALUE extends Object>
    extends AbstractAbnormal<VALUE>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( Error.class );


    // The TermViolation which led to this Error.
    private final TermViolation violation;


    /**
     * <p>
     * Creates a new Error term.
     * </p>
     *
     * @param type The Type of this Term,, such as a Type&lt;String&gt;
     *              for a Term of Strings, and so on.
     *              Must not be null.
     *
     * @param violation The TermViolation which led to this Error,
     *                  such as "can't extract the middle element(s)
     *                  from an Empty term" and that sort of thing.
     *                  Must not be null.
     */
    public Error (
            Type<VALUE> type,
            TermViolation violation
            )
        throws ParametersMustNotBeNull.Violation
    {
        super ( type ); // type

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               violation );

        this.violation = violation;
    }


    /**
     * @see musaico.foundation.term.TermPipeline#duplicate()
     */
    @Override
    public final Error<VALUE> duplicate ()
        throws ReturnNeverNull.Violation
    {
        return new Error<VALUE> ( this.type (),
                                  this.violation () );
    }


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

        final Error<?> that = (Error<?>) object;
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
            + 17 * this.violation ().hashCode ()
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
            + this.violation ()
            +" }";
    }


    /**
     * @see musacico.foundation.term.abnormal.Abnormal#violation()
     */
    @Override
    public final TermViolation violation ()
        throws ReturnNeverNull.Violation
    {
        return this.violation;
    }
}
