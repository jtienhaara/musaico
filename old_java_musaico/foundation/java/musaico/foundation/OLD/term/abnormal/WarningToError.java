package musaico.foundation.term.abnormal;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.term.Operation;
import musaico.foundation.term.Term;
import musaico.foundation.term.Type;


/**
 * <p>
 * Converts every input Warning into an output Error, and passes through
 * everything else untouched.
 * </p>
 *
 *
 * <p>
 * Operations must not be stateful.  However they may change the state
 * of the world, such as printing a message to the display, or logging
 * some information, and so on.
 * </p>
 *
 *
 * <p>
 * In Java, every Operation must implement equals (), hashCode ()
 * and toString().
 * </p>
 *
 * <p>
 * In Java every Operation must be Serializable in order to
 * play nicely across RMI.
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
public class WarningToError<VALUE extends Object>
    implements Operation<VALUE, VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( WarningToError.class );


    // Enforces parameter obligations and so on for us.
    private final Advocate contracts;

    // The Type of input Terms, with a "must be a Warning" constraint.
    private final Type<VALUE> inputType;

    // The Type of output Terms, with one less "must be a Warning" constraint,
    // and allowing Error Terms as output.
    private final Type<VALUE> outputType;


    /**
     * <p>
     * Creates a new WarningToError Operation.
     * </p>
     *
     * @param unwrapped_type The Type of the unwrapped Term inside each
     *                       Wrapped Term.  Must not be null.
     */
    @SuppressWarnings("unchecked") // Automatic generic array creation.
    public WarningToError (
            Type<VALUE> unwrapped_type
            )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               unwrapped_type );

        this.inputType =
            unwrapped_type.refine ()
                          .constraints ()
                              .edit ()
                                  .append ()
                                  .sequence ( TermMustBeWarning.CONTRACT )
                              .end () // edit ()
                          .end () // constraints
                          .buildType ();

        this.outputType =
            unwrapped_type.refine ()
                          .constraints ()
                              .edit ()
                                  .removeAll ()
                              .end () // edit ()
                          .end () // constraints
                          .allowTerms ( Error.class )
                          .buildType ();

        this.contracts = new Advocate ( this );
    }


    /**
     * @see musaico.foundation.term.Operation#apply(musaico.foundation.term.Term)
     */
    @Override
    public final Term<VALUE> apply (
            Term<VALUE> input
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               input );

        if ( ! ( input instanceof Warning ) )
        {
            return input;
        }

        final Warning<VALUE> warning = (Warning<VALUE>) input;
        final Error<VALUE> error =
            new Error<VALUE> ( this.outputType,
                               warning.violation () );

        return error;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
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

        final WarningToError<?> that = (WarningToError<?>) object;

        if ( this.inputType == null )
        {
            if ( that.inputType != null )
            {
                return false;
            }
        }
        else if ( that.inputType == null )
        {
            return false;
        }
        else if ( ! this.inputType.equals ( that.inputType ) )
        {
            return false;
        }

        // We assume the outputTypes are the same, since they are
        // constructed from equal input types.

        return true;
    }


    /**
     * @see musaico.foundation.term.Operation#errorType()
     */
    @Override
    public final Type<VALUE> errorType ()
        throws ReturnNeverNull.Violation
    {
        // We don't really have an errorType, since we allow everything
        // through except Warnings.
        return this.outputType;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return 19 * ClassName.of ( this.getClass () ).hashCode ()
            + ( this.inputType == null
                    ? 0
                    : this.inputType.hashCode () );
    }


    /**
     * @see musaico.foundation.term.Operation#inputType()
     */
    @Override
    public final Type<VALUE> inputType ()
        throws ReturnNeverNull.Violation
    {
        return this.inputType;
    }


    /**
     * @see musaico.foundation.term.Operation#outputType()
     */
    @Override
    public final Type<VALUE> outputType ()
        throws ReturnNeverNull.Violation
    {
        return this.outputType;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return ClassName.of ( this )
            + " ( " + this.inputType + " )";
    }
}
