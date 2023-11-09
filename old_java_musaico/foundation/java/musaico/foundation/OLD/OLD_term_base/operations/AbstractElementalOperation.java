package musaico.foundation.operations;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.Parameter3;
import musaico.foundation.contract.obligations.Parameter4;
import musaico.foundation.contract.obligations.Parameter5;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;

import musaico.foundation.operations.AbstractOperation;

import musaico.foundation.term.Countable;
import musaico.foundation.term.ElementalOperation;
import musaico.foundation.term.Idempotent;
import musaico.foundation.term.Invocation;
import musaico.foundation.term.Just;
import musaico.foundation.term.NonBlocking;
import musaico.foundation.term.Pipeline;
import musaico.foundation.term.Term;
import musaico.foundation.term.Type;
import musaico.foundation.term.TermViolation;

import musaico.foundation.term.abnormal.Error;

import musaico.foundation.term.contracts.OperationOutputMustMeet;
import musaico.foundation.term.contracts.TermMustBeCountable;
import musaico.foundation.term.contracts.TermMustMeetAtLeastOneContract;

import musaico.foundation.term.finite.Many;
import musaico.foundation.term.finite.No;
import musaico.foundation.term.finite.One;

import musaico.foundation.term.infinite.Cyclical;
import musaico.foundation.term.infinite.TermMustBeCyclical;

import musaico.foundation.term.normal.Finite;
import musaico.foundation.term.normal.TermMustNotBeEmpty;


/**
 * <p>
 * Boilerplate code for operations which work on the individual elements
 * of each input Term.
 * </p>
 *
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
 * @see musaico.foundation.operations.MODULE#COPYRIGHT
 * @see musaico.foundation.operations.MODULE#LICENSE
 */
public abstract class AbstractElementalOperation<INPUT extends Object, OUTPUT extends Object>
    extends AbstractOperation<INPUT, OUTPUT>
    implements ElementalOperation<INPUT, OUTPUT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( AbstractElementalOperation.class );


    // Enforces parameter obligations and so on for us.
    private final Advocate contracts;


    /**
     * <p>
     * Creates a new AbstractElementalOperation.
     * </p>
     *
     * @param input_type The Type of terms accepted as inputs to
     *                   this Operations.  Must not be null.
     *
     * @param output_type The Type of terms returned as outputs from
     *                    this Operation.  Must not be null.
     */
    public AbstractElementalOperation (
                                       Type<INPUT> input_type,
                                       Type<OUTPUT> output_type
                                       )
        throws ParametersMustNotBeNull.Violation
    {
        super ( input_type,
                output_type );

        this.contracts = new Advocate ( this );
    }


    /**
     * @see musaico.foundation.term.Operation#apply(musaico.foundation.term.Term)
     */
    public final Term<OUTPUT> apply (
                                     Term<INPUT> input
                                     )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return this.apply ( input,
                            ElementalOperation.BlockSize.ALL );
    }


    /**
     * @see musaico.foundation.term.ElementalOperation#apply(musaico.foundation.term.Term, musaico.foundation.term.ElementalOperation.BlockSize)
     */
    @Override
    public final Term<OUTPUT> apply (
            Term<INPUT> input,
            ElementalOperation.BlockSize minimum_output_size
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               input );

        // If the input is mutable, get a snapshot of it.
        // If the input is incomplete, for example
        // an Expression which might or might not have been
        // evaluated already, or a Blocking term which might or might
        // not have a result, then convert it to an Idempotent term.
        // If it is incomplete, then No value or something similar
        // will be returned, and we will abort in the following step.
        final Idempotent<INPUT> idempotent_input = input.idempotent ();

        if ( ! ( idempotent_input instanceof Just ) // e.g. Error
             || ! idempotent_input.hasValue () ) // e.g. No
        {
            // Can't operate on No elements / Error elements / etc.
            return new No<OUTPUT> ( this.outputType (),
                                    idempotent_input,
                                    TermMustNotBeEmpty.CONTRACT.violation (
                                        this,
                                        idempotent_input )
                                    );
        }
        else if ( minimum_output_size.numElements () == 0L )
        {
            // No elements are needed by the next Operation
            // in the chain anyway, so return No Value.
            return new No<OUTPUT> ( this.outputType (),
                                    idempotent_input );
        }

        final Term<OUTPUT> output_term =
            this.applyElements ( idempotent_input,
                                 minimum_output_size );

        return output_term;
    }


    /**
     * <p>
     * Performs the operation on each input element, adding output
     * elements to the specified list.
     * </p>
     *
     * <p>
     * Protected.  Called by
     * <code> AbstractElementalOperation.apply ( Term, ElementalOperation.BlockSize ) </code>.
     * </p>
     *
     * @param idempotent_input The input Term whose elements
     *                         are to be operated upon.  Idempotent, so that
     *                         the elements will never change while applying
     *                         the operation.  Can be Cyclical, Error,
     *                         and so on.  Must not be null.
     *
     * @param minimum_output_size The minimum number of elements this
     *                            ElementalOperation must output to
     *                            the next ElementalOperation's input,
     *                            in order for it to generate
     *                            its complete output.  Can be zero or one or
     *                            a few elements, such as when only the first
     *                            element will be output by the next
     *                            ElementalOperation anyway, so no further
     *                            elements are needed; or can be
     *                            <code> ElementalOperation.BlockSize.ALL </code>
     *                            if the next ElementalOperation
     *                            cannot complete its work until
     *                            this ElementalOperation has generated
     *                            its complete, final output.  If
     *                            ElementalOperation.BlockSize.BlockSize.ALL
     *                            is passed in, then this Operation must
     *                            completely transform the input, performing
     *                            exactly the same behaviour as
     *                            <code> apply ( input ) </code>.
     *                            Must not be null.
     */
    protected abstract Term<OUTPUT> applyElements (
            Idempotent<INPUT> idempotent_input,
            ElementalOperation.BlockSize minimum_output_size
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustBeGreaterThanOrEqualToZero.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Creates an Error output, caused by an input value.
     * </p>
     *
     * @param input The input value which caused the error.
                    Must not be null.
    */
    @SuppressWarnings("unchecked") // TermMustMeetAtLeastOneContract constr.
    protected final Error<OUTPUT> createInputError (
                                                    Term<INPUT> input
                                                    )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               input );

        final Error<OUTPUT> error =
            new Error<OUTPUT> ( this.outputType (),
                                input,
                                new TermMustMeetAtLeastOneContract (
                                        TermMustBeCountable.CONTRACT,
                                        TermMustBeCyclical.CONTRACT )
                                    .violation ( this,
                                                 input ) );

        return error;
    }
}
