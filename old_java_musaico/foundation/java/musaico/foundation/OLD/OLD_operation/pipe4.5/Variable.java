package musaico.foundation.operation;

import java.io.Serializable;

import java.util.List;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter4;
import musaico.foundation.contract.obligations.Parameter5;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;
import musaico.foundation.term.Type;

import musaico.foundation.term.abnormal.Error;

import musaico.foundation.term.countable.No;

import musaico.foundation.term.infinite.Cyclical;


/**
 * <p>
 * A Pipe which always causes a TermViolation, unless it is bound
 * to some other Pipe, in each given Context.
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
 * @see musaico.foundation.operation.MODULE#COPYRIGHT
 * @see musaico.foundation.operation.MODULE#LICENSE
 */
public class Variable<VALUE extends Object>
    implements Pipe<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( Variable.class );


     // Enforces parameter obligations and so on for us.
    private final Advocate contracts;

    // The Type of this free variable, when it is still unbound.
    private final Type<VALUE> unboundType;


    /**
     * <p>
     * Creates a new free Variable.
     * </p>
     *
     * @param unbound_type The Type of this free Variable before binding.
     *                     Can be more specific once it is bound,
     *                     depending on what it gets bound to.
     *                     Must not be null.
     */
    public Variable (
            Type<VALUE> unbound_type
            )
    throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               unbound_type );

        this.unboundType = unbound_type;

        this.contracts = new Advocate ( this );
    }


    /**
     * @see musaico.foundation.operation.Pipe#close(musaico.foundation.operation.Context)
     */
    @Override
    public final void close (
            Context context
            )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               context );

        final Pipe<VALUE> bound_variable = context.binding ( this );
        if ( bound_variable == null
             || bound_variable == this )
        {
            // Nothing to close.
        }
        else
        {
            bound_variable.close ( context );
        }
    }


    /**
     * @see musaico.foundation.operation.Pipe#inputPipes(musaico.foundation.operation.Context)
     */
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"}) // Generic array creation.
    public final Pipe<?> [] inputPipes (
            Context context
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               context );

        final Pipe<VALUE> bound_variable = context.binding ( this );
        if ( bound_variable == null
             || bound_variable == this )
        {
            // Still a free variable.
            this.stillAFreeVariableViolation ( context );

            return (Pipe<?> [])
                new Pipe [ 0 ];
        }
        else
        {
            return bound_variable.inputPipes ( context );
        }
    }


    /**
     * @see musaico.foundation.operation.Pipe#outputPipes(musaico.foundation.operation.Context)
     */
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"}) // Generic array creation.
    public final Pipe<?> [] outputPipes (
            Context context
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               context );

        final Pipe<VALUE> bound_variable = context.binding ( this );
        if ( bound_variable == null
             || bound_variable == this )
        {
            // Still a free variable.
            this.stillAFreeVariableViolation ( context );

            return (Pipe<?> [])
                new Pipe [ 0 ];
        }
        else
        {
            return bound_variable.outputPipes ( context );
        }
    }


    /**
     * @see musaico.foundation.operation.Pipe#read(java.util.List, java.util.List, java.util.List, long, long, musaico.foundation.operation.Context)
     */
    @Override
    public final long read (
            List<Countable<VALUE>> output_finite,
            List<Countable<VALUE>> output_cycle_or_null,
            List<Term<VALUE>> output_other,
            long finite_offset,
            long minimum_finite_elements,
            Context context
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter4.MustBeGreaterThanOrEqualToZero.Violation,
               Parameter5.MustBeGreaterThanOrEqualToZero.Violation,
               Return.AlwaysGreaterThanOrEqualToNegativeOne.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               output_finite, output_other, context );
        // output_cycle_or_null can be null.
        this.contracts.check ( Parameter4.MustBeGreaterThanOrEqualToZero.CONTRACT,
                               finite_offset );
        this.contracts.check ( Parameter5.MustBeGreaterThanOrEqualToZero.CONTRACT,
                               minimum_finite_elements );

        final Term<VALUE> cached = context.term ( this );
        if ( cached != null
             && ! ( cached instanceof Countable )
             && ! ( cached instanceof Cyclical ) )
        {
            output_other.add ( cached );
            return -1L;
        }

        final Pipe<VALUE> bound_variable = context.binding ( this );
        if ( bound_variable == null
             || bound_variable == this )
        {
            // Still a free variable.
            final Error<VALUE> error =
                this.stillAFreeVariableViolation ( context );
            output_other.add ( error );

            return -1L;
        }
        else
        {
            return bound_variable.read (
                output_finite,
                output_cycle_or_null,
                output_other,
                finite_offset,
                minimum_finite_elements,
                context );
        }
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "Variable [ " + this.unboundType + " ]";
    }


    /**
     * @see musaico.foundation.operation.Pipe#type(musaico.foundation.operation.Context)
     */
    @Override
    public final Type<VALUE> type (
            Context context
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               context );

        final Pipe<VALUE> bound_variable = context.binding ( this );
        if ( bound_variable == null
             || bound_variable == this )
        {
            // Still a free variable.
            this.stillAFreeVariableViolation ( context );

            return this.unboundType;
        }
        else
        {
            return bound_variable.type ( context );
        }
    }


    private final Error<VALUE> stillAFreeVariableViolation (
            Context context
            )
    {
        final VariableMustBeBound variable_must_be_bound =
            new VariableMustBeBound ( this );
        final VariableMustBeBound.Violation violation =
            variable_must_be_bound.violation (
                    context,                                   // plaintiff
                    new No<VALUE> ( this.type ( context ) ) ); // evidence
        final Error<VALUE> error =
            new Error<VALUE> ( this.type ( context ),
                               violation );
        context.term ( this, error );

        return error;
    }
}
