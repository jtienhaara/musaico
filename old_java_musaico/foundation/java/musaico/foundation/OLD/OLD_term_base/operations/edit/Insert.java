package musaico.foundation.operations.edit;

import java.io.Serializable;

import java.util.Collection;
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
import musaico.foundation.term.Immutable;
import musaico.foundation.term.Term;
import musaico.foundation.term.Type;

import musaico.foundation.term.abnormal.Error;

import musaico.foundation.term.contracts.CountableMustIncludeIndices;
import musaico.foundation.term.contracts.TermMustBeCountable;
import musaico.foundation.term.contracts.TermMustMeetAtLeastOneContract;

import musaico.foundation.term.finite.AbstractMultiple;
import musaico.foundation.term.finite.Many;

import musaico.foundation.term.infinite.Cyclical;
import musaico.foundation.term.infinite.TermMustBeCyclical;


/**
 * <p>
 * Adds a specific sub-value to each input term,
 * at a specific offset index.
 * </p>
 *
 * <p>
 * For example, if the input Term contains the elements
 * <code> A, B, C, D, E </code> and the sub-value <code> 100, 200 </code>
 * is inserted at index <code> 2L </code> then the resulting
 * view will contain <code> { A, B, 100, 200, C, D, E } </code>.
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
 * @see musaico.foundation.operations.edit.MODULE#COPYRIGHT
 * @see musaico.foundation.operations.edit.MODULE#LICENSE
 */
public class Insert<VALUE extends Object>
    extends AbstractElementalOperation<VALUE, VALUE>
    implements ElementalOperation<VALUE, VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( Insert.class );


    // Enforces parameter obligations and so on for us.
    private final Advocate contracts;

    // The index at which to insert the sub-value.
    // Can be negative.  See musaico.foundation.term.Countable and/or
    // musaico.foundation.term.Select#at(long).
    private final long index;

    // The sub-value to prepend/append/insert somewhere in the middle.
    private final Countable<VALUE> subValue;


    /**
     * <p>
     * Creates a new Insert operation.
     * </p>
     *
     * @param type The Type of Terms accepted as inputs to, and returned
     *             as outputs from, this Operation, such as
     *             a <code> Type<Integer> </code> or
     *             a Type<String> and so on.  Must not be null.
     *
     * @param index The index at which to insert the specified sub-value,
     *              starting at <code> 0L </code> to insert before the
     *              first element, and ending at <code> length () </code> to
     *              insert after the last element.  To insert before
     *              the 3rd element, <code> 2L </code> could be used, or
     *              to insert before the 3rd last element,
     *              <code> Countable.FROM_END + 2L </code> could be used.
     *              Can be positive or negative.
     *
     * @param sub_value The element(s) to insert into each input term.
     *                  Can be empty.  Must not be null.
     */
    public Insert (
                   Type<VALUE> type,
                   long index,
                   Countable<VALUE> sub_value
                   )
        throws ParametersMustNotBeNull.Violation
    {
        super ( type,
                type );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               sub_value );

        this.index = index;
        this.subValue = sub_value;

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
        if ( this.subValue.length () == 0L )
        {
            return input;
        }
        else if ( ! ( input instanceof Countable ) )
        {
            // Nothing we can do with this.
            if ( input.hasValue () )
            {
                // Could be infinite.
                final Error<VALUE> error =
                    new Error<VALUE> (
                        this.outputType (),
                        input,
                        TermMustBeCountable.CONTRACT
                                           .violation ( this,
                                                        input ) );
                return error;
            }
            else
            {
                return input;
            }
        }

        final Countable<VALUE> countable = (Countable<VALUE>) input;

        final long input_length = countable.length ();
        final long insert_at;
        if ( this.index == input_length
             || this.index == Countable.AFTER_LAST )
        {
            // After end.
            insert_at = input_length;
        }
        else
        {
            // 0L <= insert_at < input_length.
            insert_at =
                AbstractMultiple.clamp ( this.index, 0L, input_length );
        }

        if ( insert_at == Countable.NONE )
        {
            // Index is out of range for the specified input.
            final Error<VALUE> error =
                new Error<VALUE> (
                    this.outputType (),
                    input,
                    new CountableMustIncludeIndices ( this.index )
                        .violation ( this,
                                     input ) );
            return error;
        }
        else if ( insert_at > minimum_output_size.numElements () )
        {
            // Don't bother inserting since the caller won't look
            // at the inserted element(s) anyway.
            return countable;
        }

        // Guaranteed at this point: 0L <= insert_at <= input_length.
        // If == input_length, then we append the sub-value to the end.
        // Otherwise we insert it before the i'th element.
        if ( input_length == 0L )
        {
            // We can't possibly return more elements than this, even
            // if requested by the caller.
            return this.subValue;
        }

        // Guaranteed at this point: this.subValue.length () > 0L
        // and input_length > 0L.
        // Minimum output size is 2, unless the caller requested fewer.
        // We also know for certain that the minimum output size includes
        // the insert_at index.
        final Many<VALUE> output;
        if ( insert_at == 0L )
        {
            if ( this.subValue.length () > minimum_output_size.numElements () )
            {
                // Don't bother inserting the elements since the caller
                // will not look past the end of the inserted sub-value
                // anyway.
                return this.subValue;
            }

            output = new Many<VALUE> ( this.subValue, countable );
        }
        else if ( insert_at == input_length )
        {
            output = new Many<VALUE> ( countable, this.subValue );
        }
        else
        {
            final Immutable<VALUE> before_insert =
                countable.range ( 0L, insert_at - 1L );
            final Immutable<VALUE> after_insert =
                countable.range ( insert_at, input_length - 1 );

            output = new Many<VALUE> ( before_insert,
                                       this.subValue,
                                       after_insert );
        }

        return output;
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

        final Insert<?> that = (Insert<?>) object;

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
            + 23 * ( this.subValue == null
                         ? 0
                         : this.subValue.hashCode () )
            + (int) this.index;
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

        final long minimum_num_outputs = for_output_size.numElements ();
        if ( this.index > minimum_num_outputs )
        {
            return for_output_size;
        }
        else if ( minimum_num_outputs == 0L )
        {
            return for_output_size;
        }
        else
        {
            final long sub_term_num_elements =
                this.subValue.length ();
            final long first_half_plus_sub_term =
                this.index + sub_term_num_elements;
            final long minimum_num_inputs;
            if ( first_half_plus_sub_term >= minimum_num_outputs )
            {
                minimum_num_inputs = this.index;
            }
            else
            {
                minimum_num_inputs =
                    minimum_num_outputs - sub_term_num_elements;
            }

            final ElementalOperation.BlockSize minimum_input_size =
                new ElementalOperation.BlockSize ( minimum_num_inputs );

            return minimum_input_size;
        }
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return ClassName.of ( this.getClass () )
            + " ( " + this.index + ","
            + " " + this.subValue + " )";
    }
}
