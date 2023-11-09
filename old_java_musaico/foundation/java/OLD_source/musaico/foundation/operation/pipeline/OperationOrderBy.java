package musaico.foundation.term.multiplicities;

import java.io.Serializable;

import java.util.List;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.order.Comparison;
import musaico.foundation.order.Order;

import musaico.foundation.pipeline.OrderBy;
import musaico.foundation.pipeline.Pipeline;

import musaico.foundation.term.OperationPipeline;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermPipeline;


/**
 * <p>
 * Sorts or re-arranges the elements in Term values.
 * </p>
 *
 * <p>
 * For example, to arrange the elements of a String Term by some
 * specific "dictionary" sort Order, then, whenever the "dictionary"
 * Order considers two elements to be the same, by the "ascii"
 * Order, and then reverse the sorted order, the following code
 * might be used:
 * </p>
 *
 * <pre>
 *     final Term<String> term = ...;
 *     final Order<String> dictionary = ...;
 *     final Order<String> ascii = ...;
 *     final Term<String>  =
 *         term.orderBy ( dictionary, ascii )
 *             .reverse ()
 *             .apply ();
 * </pre>
 *
 * <p>
 * The output of the <code> apply () </code> method would be
 * the a term whose value contains the reverse-sorted elements
 * of the input term's value.
 * </p>
 *
 *
 * <p>
 * In Java every SubPipeline must implement <code> equals () </code>,
 * <code> hashCode () </code> and <code> toString () </code>.
 * </p>
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
public class TermOrderBy<VALUE extends Object, PARENT extends TermPipeline<VALUE, PARENT>>
    extends AbstractTermSubPipeline<VALUE, PARENT, OrderBy<VALUE, PARENT>, OrderBy<VALUE, ?>>
    implements OrderBy<VALUE, PARENT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( TermOrderBy.class );


    /**
     * <p>
     * Creates a new TermOrderBy.
     * </p>
     *
     * @param parent The parent of this SubPipeline.  Must not be null.
     *
     * @param slave The OrderBy operation pipeline that will perform
     *              the heavy lifting.  Must not be null.
     *
     * @param orders Zero or more hierarchy of Orders to sort by.
     *               If the first Order considers two elements to be the same,
     *               then the second Order is applied.  If the second Order
     *               also considers the two elements to be the same, then
     *               the third Order is applied.  And so on.  Can be empty.
     *               Must not be null.  Must not contain any null elements.
     */
    @SuppressWarnings("unchecked") // Generic array heap pollution.
    public TermOrderBy (
            PARENT parent,
            OrderBy<VALUE, ?> slave
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
     * @see java.util.Comparator.compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public final int compare (
            VALUE left,
            VALUE right
            )
    {
        return this.slave ().compare ( left, right );
    }


    /**
     * @see musaico.foundation.order.Order#compareValues(java.lang.Object, java.lang.Object)
     */
    @Override
    public final Comparison compareValues (
            VALUE left,
            VALUE right
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation:
        return this.slave ().compareValues ( left, right );
    }


    /**
     * @see musaico.foundation.pipeline.SubPipeline#duplicate(musaico.foundation.pipeline.Pipeline)
     */
    @Override
    public final TermOrderBy<VALUE, PARENT> duplicate (
            PARENT duplicate_parent
            )
        throws ReturnNeverNull.Violation
    {
        return this.duplicateUgly ( duplicate_parent );
    }

    // Yes this is atrocious.  Sorry.
    @SuppressWarnings("unchecked")
    private final <SLAVE extends OrderBy<VALUE, SLAVE_PARENT>, SLAVE_PARENT extends OperationPipeline<VALUE, SLAVE_PARENT>>
        TermOrderBy<VALUE, PARENT> duplicateUgly (
            PARENT duplicate_parent
            )
        throws ReturnNeverNull.Violation
    {
        return new TermOrderBy<VALUE, PARENT> (
            duplicate_parent,
            ( (SLAVE) this.slave () ).duplicate (
                (SLAVE_PARENT)
                    duplicate_parent.operations () )
          );
    }


    /**
     * @see musaico.foundation.pipeline.OrderBy#reverse()
     */
    @Override
    public final OrderBy<VALUE, PARENT> reverse ()
        throws ReturnNeverNull.Violation
    {
        this.slave ().reverse ();
        return this;
    }


    /**
     * @see musaico.foundation.order.Order#reverseOrder()
     */
    @Override
    public final Order<VALUE> reverseOrder ()
        throws ReturnNeverNull.Violation
    {
        return this.slave ().reverseOrder ();
    }


    /**
     * @see musaico.foundation.pipeline.OrderBy#shuffle(java.util.Iterator)
     */
    @Override
    public final <RANK extends Comparable<? super RANK>>
        OrderBy<VALUE, PARENT> shuffle (
            Iterable<RANK> ranker
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation:
        this.slave ().shuffle ( ranker );
        return this;
    }


    /**
     * @see musaico.foundation.order.Order#sort(java.lang.Object[])
     */
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"}) // Generic array creation.
    public final VALUE [] sort (
            VALUE [] array
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation,
        // Parameter1.MustContainNoNulls.Violation:
        return this.slave ().sort ( array );
    }


    /**
     * @see musaico.foundation.order.Order#sort(java.lang.Iterable)
     */
    @Override
    public final List<VALUE> sort (
            Iterable<VALUE> iterable
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation,
        // Parameter1.MustContainNoNulls.Violation:
        return this.slave ().sort ( iterable );
    }
}
