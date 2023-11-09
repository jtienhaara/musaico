package musaico.foundation.operations.edit;

import java.io.Serializable;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


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
import musaico.foundation.term.Type;
import musaico.foundation.term.Term;

import musaico.foundation.term.abnormal.Error;

import musaico.foundation.term.builder.TermBuilder;

import musaico.foundation.term.contracts.TermMustBeCountable;
import musaico.foundation.term.contracts.TermMustMeetAtLeastOneContract;

import musaico.foundation.term.infinite.Cyclical;
import musaico.foundation.term.infinite.TermMustBeCyclical;


/**
 * <p>
 * Returns the set union between specific Term(s)
 * and each input Term, containing each element which is present
 * either in the specified Term(s) or in the input Term or in both.
 * </p>
 *
 * <p>
 * Multiple copies of the same element will be kept, even though
 * this is a set operation at heart.  For example, if the input Term
 * contains the element "A" twice, but the specified term does not
 * contain "A", then "A" shall appear twice in the output.
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
public class Union<VALUE extends Object>
    extends AbstractElementalOperation<VALUE, VALUE>
    implements ElementalOperation<VALUE, VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( Union.class );


    // Enforces parameter obligations and so on for us.
    private final Advocate contracts;

    // The set(s) against which each input will be union'd.
    private final Term<VALUE> [] sets;

    // True if any of the set(s) to union against is a Cyclical value,
    // causing the output from this set operation to be Cyclical, too.
    private final boolean isAnySetCyclical;

    // The elements among the initial constructor sets which are
    // contained in the Union of the set(s).
    private final LinkedHashSet<VALUE> setUnion;

    // If any of the initial constructor sets is something we can't
    // take a set Union from, then set the errorSet to the first
    // bad one.
    private final Term<VALUE> errorSet;


    /**
     * <p>
     * Creates a new Union operation.
     * </p>
     *
     * @param type The Type of Terms accepted as inputs to, and returned
     *             as outputs from, this Operation, such as
     *             a <code> Type<Integer> </code> or
     *             a Type<String> and so on.  Must not be null.
     *
     * @param sets The term(s) whose elements shall be union'ed
     *             with each input.  Must not be null.
     *             Must not contain any null elements.
     */
    @SuppressWarnings({"rawtypes", "unchecked"}) // Generic array creation.
    public Union (
                  Type<VALUE> type,
                  Term<VALUE> ... sets
                  )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        super ( type,
                type );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               (Object) sets );
        classContracts.check ( Parameter2.MustContainNoNulls.CONTRACT,
                               sets );

        this.sets = (Term<VALUE> []) new Term [ sets.length ];
        System.arraycopy ( sets, 0,
                           this.sets, 0, sets.length );

        LinkedHashSet<VALUE> set_union = new LinkedHashSet<VALUE> ();
        Term<VALUE> first_error_set = null;
        boolean is_any_set_cyclical = false;
        for ( Term<VALUE> set : this.sets )
        {
            if ( set instanceof Cyclical )
            {
                final Cyclical<VALUE> cyclical = (Cyclical<VALUE>) set;
                is_any_set_cyclical = true;
                for ( VALUE element : cyclical.cycle () )
                {
                    set_union.add ( element );
                }
            }
            else if ( set instanceof Countable )
            {
                for ( VALUE element : set )
                {
                    set_union.add ( element );
                }
            }
            else
            {
                // Don't know what to do with this Term.
                is_any_set_cyclical = false;
                set_union = null;
                first_error_set = set;
                break;
            }
        }

        this.isAnySetCyclical = is_any_set_cyclical;
        this.setUnion = set_union;
        this.errorSet = first_error_set;

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
        if ( this.errorSet != null )
        {
            // At least one of the set(s) to union with each input
            // was of some unknown class, e.g. Error or Acyclical and so on.
            final Error<VALUE> error =
                this.createInputError ( this.errorSet );
            return error;
        }

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

        final Set<VALUE> input_set =
            new HashSet<VALUE> ();
        final TermBuilder<VALUE> output_builder =
            new TermBuilder<VALUE> ( this.outputType () );
        final long num_requested_elements =
            minimum_output_size.numElements ();
        long num_output_elements = 0L;
        for ( VALUE element : input_elements )
        {
            input_set.add ( element );
            output_builder.add ( element );
            num_output_elements ++;

            if ( num_output_elements >= num_requested_elements )
            {
                break;
            }
        }

        for ( VALUE element : this.setUnion )
        {
            if ( ! input_set.contains ( element ) )
            {
                output_builder.add ( element );
                num_output_elements ++;

                if ( num_output_elements >= num_requested_elements )
                {
                    break;
                }
            }
        }

        final boolean is_output_cyclical =
            this.isAnySetCyclical
            || is_input_cyclical;

        final Term<VALUE> output;
        if ( is_output_cyclical )
        {
            output = output_builder.buildCyclical ();
        }
        else
        {
            output = output_builder.build ();
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

        final Union<?> that = (Union<?>) object;

        if ( this.sets == null )
        {
            if ( that.sets != null )
            {
                return false;
            }
        }
        else if ( that.sets == null )
        {
            return false;
        }
        else if ( this.sets.length != that.sets.length )
        {
            return false;
        }

        for ( int s = 0; s < this.sets.length; s ++ )
        {
            final Term<VALUE> this_set = this.sets [ s ];
            final Term<?> that_set = that.sets [ s ];
            if ( ! this_set.equals ( that_set ) )
            {
                return false;
            }
        }

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
            + 13 * ( this.sets == null
                         ? 0
                         : this.sets.length );
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

        // Union could require every element to be checked
        // (and it still might not be able to output even a single
        // element if the Union turns out to be empty).
        return ElementalOperation.BlockSize.ALL;
    }


    /**
     * @return The set(s) against which each input Term is union'd.
     *         Never null.
     */
    @SuppressWarnings({"rawtypes", "unchecked"}) // Generic array creation.
    public Term<VALUE> [] sets ()
        throws ReturnNeverNull.Violation
    {
        final Term<VALUE> [] sets = (Term<VALUE> [])
            new Term [ this.sets.length ];
        System.arraycopy ( this.sets, 0,
                           sets, 0, this.sets.length );

        return sets;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        final StringBuilder sets_buf = new StringBuilder ();
        if ( this.sets == null ) // Can happen during constructor.
        {
            return ClassName.of ( this.getClass () )
                + " { ... }";
        }

        return ClassName.of ( this.getClass () )
            + " { "
            + StringRepresentation.of ( this.sets,
                                        StringRepresentation.DEFAULT_ARRAY_LENGTH )
            + " }";
    }
}
