package musaico.foundation.operations.edit;

import java.io.Serializable;

import java.util.List;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.Parameter4;
import musaico.foundation.contract.obligations.Parameter5;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;
import musaico.foundation.domains.StringRepresentation;

import musaico.foundation.filter.Filter;

import musaico.foundation.operations.AbstractElementalOperation;

import musaico.foundation.term.Countable;
import musaico.foundation.term.ElementalOperation;
import musaico.foundation.term.Idempotent;
import musaico.foundation.term.Term;
import musaico.foundation.term.Type;

import musaico.foundation.term.abnormal.Error;

import musaico.foundation.term.builder.TermBuilder;

import musaico.foundation.term.contracts.TermMustBeCountable;
import musaico.foundation.term.contracts.TermMustMeetAtLeastOneContract;

import musaico.foundation.term.infinite.Cyclical;
import musaico.foundation.term.infinite.TermMustBeCyclical;


/**
 * <p>
 * Returns each input Term with the elements in a specific
 * range of indices removed, with a specific replacement sequence
 * of elements inserted in its place.
 * </p>
 *
 * <p>
 * Has the same end effect as first calling
 * <code> remove ( start_index, end_index ) </code> then calling
 * <code> insert ( start_index, replacement ) </code>.
 * </p>
 *
 * @see musaico.foundation.term.Edit#remove(long, long)
 * @see musaico.foundation.term.Edit#insert(long, musaico.foundation.term.Countable)
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
 * @see musaico.foundation.operations.edit.MODULE#COPYRIGHT
 * @see musaico.foundation.operations.edit.MODULE#LICENSE
 */
public class Replace<VALUE extends Object>
    extends AbstractElementalOperation<VALUE, VALUE>
    implements ElementalOperation<VALUE, VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( Replace.class );


    // Enforces parameter obligations and so on for us.
    private final Advocate contracts;


    /**
     * <p>
     * Creates a new Replace operation.
     * </p>
     *
     * @param type The Type of Terms accepted as inputs to, and returned
     *             as outputs from, this Operation, such as
     *             a <code> Type<Integer> </code> or
     *             a Type<String> and so on.  Must not be null.
     *
     * @param start_index The beginning of the range of elements to
     *                    replace in each input Term.  
     *                    Can be any number.
     *
     * @param end_index The last index of the range of elements to
     *                  replace in each input Term.  
     *                  Can be any number.
     *
     * @param replacement The elements to insert in place of the
     *                    range of elements in each input Term.
     *                    Can be empty.  Must not be null.
     */
    public Replace (
                    Type<VALUE> type
                    )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        super ( type,
                type );

        this.contracts = new Advocate ( this );
    }


    /**
     * @see musaico.foundation.operations.AbstractElementalOperation#applyElements(musaico.foundation.term.Idempotent, musaico.foundation.term.ElementalOperation.BlockSize)
     */
    @Override
    protected final Term<VALUE> applyElements (
            Idempotent<VALUE> input,
            ElementalOperation.BlockSize minimum_output_size
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        !!!;
        final Iterable<VALUE> input_elements;
        final boolean is_input_cyclical;
        if ( input instanceof Countable )
        {
            input_elements = input;
            is_input_cyclical = false;
        }
        else if ( input instanceof Cyclical )
        {
            final Cyclical<VALUE> cyclical = (Cyclical<VALUE>) input;
            input_elements = cyclical.cycle ();
            is_input_cyclical = true;
        }
        else
        {
            final Error<VALUE> error =
                this.createInputError ( input );
            return error;
        }

        final TermBuilder<VALUE> output_builder =
            new TermBuilder<VALUE> ( this.outputType () );
        final long num_requested_elements =
            minimum_output_size.numElements ();

        for ( VALUE element : input_elements )
        {
            !!!;
        }

        final boolean is_output_cyclical = !!!;

        final Term<VALUE> output_term =
            this.createOutputTerm ( input,
                                    num_requested_elements,
                                    is_output_cyclical,
                                    output_elements );

        return output_term;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
                                 Object object
                                 )
    {
        if ( object == this )
        {
            return true;
        }
        else if ( object == null )
        {
            return false;
        }
        else if ( this.getClass () != object.getClass () )
        {
            return false;
        }

        final !!!<?> that = (!!!<?>) object;

        // Everything is all matchy-matchy.
        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return this.inputType ().hashCode ()
            + 23 * !!!;
    }


    /**
     * @see musaico.foundation.term.ElementalOperation#minimumInputSize(musaico.foundation.term.ElementalOperation.BlockSize)
     */
    @Override
    public final ElementalOperation.BlockSize minimumInputSize (
            ElementalOperation.BlockSize for_output_size
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               for_output_size );

        !!!;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return ClassName.of ( this.getClass () )
            + !!!;
    }
}
