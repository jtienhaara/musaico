package musaico.foundation.term.multiplicities;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.pipeline.Pipeline;
import musaico.foundation.pipeline.Where;

import musaico.foundation.term.OperationPipeline;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermPipeline;


/**
 * <p>
 * Removes elements from Terms' values.
 * </p>
 *
 * <p>
 * For example, to filter out all elements of a Number term's value which
 * are not in the range 0..10, and return only one element
 * per unique Number, removing duplicates, the following code
 * might be used:
 * </p>
 *
 * <pre>
 *     final Term<Number> term = ...;
 *     final Term<Number> greater_than_or_equal_to_0 =
 *         term.where ( GreaterThanOrEqualToZero.DOMAIN )
 *             .where ( new LessThanOrEqualToNumber ( 10 ) )
 *             .unique ()
 *             .apply ();
 * </pre>
 *
 * <p>
 * The output of the <code> apply () </code> method would be
 * some subset of the Numbers ( 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 ),
 * depending on the input, with no duplicates.
 * </p>
 *
 *
 * <p>
 * In Java every SubPipeline must be Serializable in order to
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
 * @see musaico.foundation.term.multiplicities.MODULE#COPYRIGHT
 * @see musaico.foundation.term.multiplicities.MODULE#LICENSE
 */
public class TermWhere<VALUE extends Object, PARENT extends TermPipeline<VALUE, PARENT>>
    extends AbstractTermSubPipeline<VALUE, PARENT, Where<VALUE, PARENT>, Where<VALUE, ?>>
    implements Where<VALUE, PARENT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new TermWhere.
     * </p>
     *
     * @param parent The parent of this SubPipeline.  Must not be null.
     *
     * @param slave The Where operation pipeline that will perform
     *              the heavy lifting.  Must not be null.
     *
     * @param filters Zero or more Filters to be applied.  Can be empty.
     *                Must not be null.  Must not contain any null elements.
     */
    @SuppressWarnings("unchecked") // Generic array heap pollution.
    public TermWhere (
            PARENT parent,
            Where<VALUE, ?> slave
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ClassCastException
    {
        // Throws ParametersMustNotBeNull.Violation,
        // Parameter1.MustContainNoNulls.Violation:
        super ( parent,  // parent
                slave ); // slave
    }


    /**
     * @see musaico.foundation.pipeline.Where#and(musaico.foundation.filter.Filter[])
     */
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"}) // Generic array creation,
        // generic array heap pollution.
    public final Where<VALUE, PARENT> and (
            Filter<VALUE> ... filters
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation,
        // Parameter1.MustContainNoNulls.Violation:
        this.slave ().and ( filters );

        return this;
    }


    /**
     * @see musaico.foundation.pipeline.Where#byLength(long)
     */
    @Override
    public final Where<VALUE, PARENT> byLength (
            long exact_length
            )
        throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
               ReturnNeverNull.Violation
    {
        // Throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation:
        this.slave ().byLength ( exact_length );

        return this;
    }


    /**
     * @see musaico.foundation.pipeline.Where#byLength(long,long)
     */
    @Override
    public final Where<VALUE, PARENT> byLength (
            long minimum,
            long maximum
            )
        throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
               Parameter2.MustBeGreaterThanOrEqualTo.Violation,
               ReturnNeverNull.Violation
    {
        // Throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
        // Parameter2.MustBeGreaterThanOrEqualToZero.Violation:
        this.slave ().byLength ( minimum, maximum );

        return this;
    }


    /**
     * @see musaico.foundation.pipeline.SubPipeline#duplicate(musaico.foundation.pipeline.Pipeline)
     */
    @Override
    public final TermWhere<VALUE, PARENT> duplicate (
            PARENT duplicate_parent
            )
        throws ReturnNeverNull.Violation
    {
        return this.duplicateUgly ( duplicate_parent );
    }

    // Yes this is atrocious.  Sorry.
    @SuppressWarnings("unchecked")
    private final <SLAVE extends Where<VALUE, SLAVE_PARENT>, SLAVE_PARENT extends OperationPipeline<VALUE, SLAVE_PARENT>>
        TermWhere<VALUE, PARENT> duplicateUgly (
            PARENT duplicate_parent
            )
        throws ReturnNeverNull.Violation
    {
        return new TermWhere<VALUE, PARENT> (
            duplicate_parent,
            ( (SLAVE) this.slave () ).duplicate (
                (SLAVE_PARENT)
                    duplicate_parent.operations () )
          );
    }


    /**
     * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
     */
    @Override
    public final FilterState filter (
            VALUE grain
            )
    {
        if ( grain == null )
        {
            return FilterState.DISCARDED;
        }

        final FilterState state = this.slave ()
            .filter ( grain );

        return state;
    }


    /**
     * @see musaico.foundation.pipeline.Where#infinite()
     */
    @Override
    public final Where<VALUE, PARENT> infinite ()
        throws ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation:
        this.slave ().infinite ();

        return this;
    }


    /**
     * @see musaico.foundation.pipeline.Where#multiple()
     */
    @Override
    public final Where<VALUE, PARENT> multiple ()
        throws ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation:
        this.slave ().multiple ();

        return this;
    }


    /**
     * @see musaico.foundation.pipeline.Where#or(musaico.foundation.filter.Filter[])
     */
    @Override
    @SuppressWarnings("unchecked") // Generic array heap pollution.
        public final Where<VALUE, PARENT> or (
            Filter<VALUE> ... filters
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation,
        // Parameter1.MustContainNoNulls.Violation:
        this.slave ().or ( filters );

        return this;
    }


    /**
     * @see musaico.foundation.pipeline.Where#single()
     */
    @Override
    public final Where<VALUE, PARENT> single ()
        throws ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation:
        this.slave ().single ();

        return this;
    }


    /**
     * @see musaico.foundation.pipeline.Where#unique()
     */
    @Override
    public final Where<VALUE, PARENT> unique ()
        throws ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation:
        this.slave ().unique ();

        return this;
    }


    /**
     * @see musaico.foundation.pipeline.Where#xor(musaico.foundation.filter.Filter[])
     */
    @Override
    @SuppressWarnings("unchecked") // Generic array heap pollution.
        public final Where<VALUE, PARENT> xor (
            Filter<VALUE> ... filters
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation,
        // Parameter1.MustContainNoNulls.Violation:
        this.slave ().xor ( filters );

        return this;
    }
}
