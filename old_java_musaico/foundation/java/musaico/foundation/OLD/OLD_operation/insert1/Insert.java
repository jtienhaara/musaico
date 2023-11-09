package musaico.foundation.operation;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.order.NaturalOrder;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Operation;
import musaico.foundation.term.Term;
import musaico.foundation.term.Type;

import musaico.foundation.term.abnormal.Error;

import musaico.foundation.term.contracts.CountableLengthMustBe;
import musaico.foundation.term.contracts.ValueMustBeCountable;

import musaico.foundation.term.builder.TermBuilder;

import musaico.foundation.term.countable.Countable;

import musaico.foundation.term.incomplete.Reduce;
import musaico.foundation.term.incomplete.TermMustBeReducible;

import musaico.foundation.term.infinite.Cyclical;
import musaico.foundation.term.infinite.TermMustBeCyclical;



/**
 * <p>
 * !!!
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
public class Insert<VALUE extends Object>
    implements Operation<VALUE, VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( AbstractTerm.class );


    // The contract for the elements to insert, each input, and the output.
    // The elements to insert, the input, and the output must each be either:
    // 1) Countable with 0 - Integer.MAX_VALUE elements; or
    // 2) Cyclical, with header and cycle that are
    //    each less than or equal to Integer.MAX_VALUE in length.
    public static final TermMustMeetAtLeastOneContract CONTRACT =
        new TermMustMeetAtLeastOneContract (
            new TermMustMeetAllContracts (
                TermMustBeCountable.CONTRACT,
                CountableLengthMustBe.INT_32_SIZED ),
            new TermMustMeetAllContracts (
                TermMustBeCyclical.CONTRACT,
                new CyclicalHeaderMustMeet (
                    CountableLengthMustBe.INT_32_SIZED
                ),
                new CyclicalCycleMustMeet (
                    CountableLengthMustBe.INT_32_SIZED
                )
            )
        );


    // Enforces parameter obligations and so on for us.
    private final Advocate contracts;

    // The input/output Type of this Operation.
    private final Type<VALUE> type;

    // The error output Type of this Operation.
    private final Type<VALUE> errorType;

    // Where to insert the elements.
    private final Term<VALUE> insertAt;

    // The elements to insert.
    private final Term<VALUE> elements;


    /**
     * <p>
     * Creates a new Insert Operation.
     * </p>
     *
     * @param type The Type of each input Term.  Must not be null.
     *
     * @param insert_at The index or indices at which to insert the
     *                  specified element(s) into each input Term.
     *                  Must not be null.
     *
     * @param elements The element(s) to insert at each index.
     *                 Must not be null.
     */
    @SuppressWarnings("unchecked") // Automatic generic array creation,
        // TermMustMeetAtLeastOneContract constructor
    public Insert (
            Type<VALUE> type,
            Term<Long> insert_at,
            Term<VALUE> elements
            )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               type, insert_at, elements );

        this.type = type.refine ()
                        .where ( Insert.CONTRACT )
                        .end () // where
                        .buildType ();

        this.errorType = type.refine ()
                             .allowTerms ( Error.class )
                             .buildType ();

        this.contracts = new Advocate ( this );
    }


    /**
     * @see musaico.foundation.term.Operation#apply(musaico.foundation.term.Term)
     */
    @Override
    public final Term<VALUE> apply (
            Term<VALUE> unfiltered_input
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               unfiltered_input );

        final Term<VALUE> insert_at =
            this.insertAt.when ( TermMustBeReducible.CONTRACT )
                             .pipe ( new Reduce<VALUE> ( this.type ) )
                         .end () // when
                         // Make sure the insert at indices are sorted.
                         .orderBy ( new NaturalOrder<Long> () )
                         .output ();
        if ( ! ValueMustBeCountable.CONTRACT.fiter ( insert_at )
                                            .isKept () )
        {
            final TermViolation violation =
                ValueMustBeCountable.CONTRACT.violation (
                    this,     // plaintiff
                    insert ); // evidence
            return new Error<VALUE> ( this.errorType,
                                      violation );
        }

        final Term<VALUE> elements =
            this.elements.when ( TermMustBeReducible.CONTRACT )
                             .pipe ( new Reduce<VALUE> ( this.type ) )
                         .end () // when
                         .output ();
        if ( ! Insert.CONTRACT.fiter ( elements ).isKept () )
        {
            final TermViolation violation =
                Insert.CONTRACT.violation ( this,       // plaintiff
                                            elements ); // evidence
            return new Error<VALUE> ( this.errorType,
                                      violation );
        }

        final Term<VALUE> input =
            unfiltered_input.when ( TermMustBeReducible.CONTRACT )
                                .pipe ( new Reduce<VALUE> ( this.type ) )
                            .end () // when
                            .output ();
        if ( ! Insert.CONTRACT.fiter ( input ).isKept () )
        {
            final TermViolation violation =
                Insert.CONTRACT.violation ( this,    // plaintiff
                                            input ); // evidence
            return new Error<VALUE> ( this.errorType,
                                      violation );
        }

        final TermBuilder<VALUE> builder =
            new TermBuilder<VALUE> ( this.type );
        long input_index = 0L;
        for ( Long unclamped_insert_index : insert_at )
        {
            final long insert_index;
            if ( input instanceof !!!
 = input.clamp ( unclamped_insert_index );
            if ( insert_index < 0L )
            {
                final Error<VALUE> 
        }

        if ( input instanceof Countable )
        {
            final Countable<VALUE> countable = (Countable<VALUE>) input;
            final long length = countable.length ();
            if ( length > (long) Integer.MAX_VALUE )
            {
                

        if ( ! ( input instanceof Wrapped ) )
        {
            final TermMustBeWrapped.Violation violation =
                TermMustBeWrapped.CONTRACT.violation (
                    this,    // plaintiff
                    input ); // evidence
            final Error<VALUE> error =
                new Error<VALUE> ( this.errorType,
                                   violation );
            return error;
        }

        final Wrapped<VALUE, ?> wrapped = (Wrapped<VALUE, ?>) input;

        final Term<VALUE> unwrapped = wrapped.unwrap ();

        // Paranoid check:
        this.contracts.check ( ReturnNeverNull.CONTRACT,
                               unwrapped );

        return unwrapped;
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

        final Unwrap<?> that = (Unwrap<?>) object;

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
