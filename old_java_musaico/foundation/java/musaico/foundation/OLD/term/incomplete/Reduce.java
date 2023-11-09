package musaico.foundation.term.incomplete;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.term.Operation;
import musaico.foundation.term.Term;
import musaico.foundation.term.Type;

import musaico.foundation.term.abnormal.Error;


/**
 * <p>
 * Reduces each Reducible input Term, or passes through all other Terms
 * as-is.
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
 * @see musaico.foundation.term.incomplete.MODULE#COPYRIGHT
 * @see musaico.foundation.term.incomplete.MODULE#LICENSE
 */
public class Reduce<VALUE extends Object>
    implements Operation<VALUE, VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( Reduce.class );


    // Enforces parameter obligations and so on for us.
    private final Advocate contracts;

    // The Type of input Terms, which allows Reducible inputs.
    private final Type<VALUE> inputType;

    // The Type of output Terms, which is the same as the input Type,
    // but with "Reducible" terms not allowed.
    private final Type<VALUE> outputType;

    // The Type of error Terms, which is the same as the output Type,
    // but always allows Error output Terms.
    private final Type<VALUE> errorType;


    /**
     * <p>
     * Creates a new Reduce Operation.
     * </p>
     *
     * @param type The Type of Terms allowed as inputs and outputs,
     *             excluding Reducible Terms.  Must not be null.
     */
    @SuppressWarnings("unchecked") // Automatic generic array creation.
    public Reduce (
            Type<VALUE> reduced_type
            )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               reduced_type );

        this.inputType = reduced_type.refine ()
                                     .allowTerms ( Reducible.class )
                                     .buildType ();
        this.outputType =
            reduced_type.refine ()
                        .constraints ()
                            .edit ()
                                .append ()
                                .sequence ( TermMustNotBeReducible.CONTRACT )
                            .end () // edit ()
                        .end () // constraints
                        .buildType ();
        this.errorType =
            reduced_type.refine ()
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

        if ( ! ( input instanceof Reducible ) )
        {
            return input;
        }

        Reducible<VALUE, ?> reducible = (Reducible<VALUE, ?>) input;
        Term<VALUE> reduced = null;
        int infinite_loop_protector = 0;
        for ( infinite_loop_protector = 0;
              infinite_loop_protector < 1024;
              infinite_loop_protector ++ )
        {
            reduced = reducible.reduce ();
            if ( reduced instanceof Reducible )
            {
                reducible = (Reducible<VALUE, ?>) reduced;
            }
            else
            {
                break;
            }
        }

        if ( infinite_loop_protector >= 1024 )
        {
            final TermMustBeReducible.Violation violation =
                TermMustBeReducible.CONTRACT.violation (
                    this, // plaintiff
                    input ); // evidence
            final Error<VALUE> infinite_loop =
                new Error<VALUE> ( this.errorType,
                                   violation );
            return infinite_loop;
        }

        // Paranoid check:
        this.contracts.check ( ReturnNeverNull.CONTRACT,
                               reduced );

        return reduced;
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

        final Reduce<?> that = (Reduce<?>) object;

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
        return this.errorType;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return 17 * ClassName.of ( this.getClass () ).hashCode ()
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
