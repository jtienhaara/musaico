package musaico.foundation.pipeline;

import java.io.Serializable;

import java.util.Collection;
import java.util.Set;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;


/**
 * <p>
 * A Pipeline and Subsidiary which executes only on input objects which
 * are kept by all its filters, effectively an "if" clause.
 * </p>
 *
 * <p>
 * Pipelines are not necessarily specific to Types, Terms
 * and Operations (@see musaico.foundation.term), but since that is
 * what they were designed for, the examples here all related to those
 * classes of objects.
 * </p>
 *
 * <p>
 * For example, a When pipeline might be created by calling
 * <code> Pipeline.when ( term_has_more_than_1_element ) </code>,
 * where the <code> term_has_more_than_1_element </code> Filter matches
 * only OneOrMore terms that do not contain exactly 1 element.
 * </p>
 *
 * <p>
 * Each When pipeline is one branch of a tree of one or more
 * When(s), each of which is applied to an input
 * only if the previous When(s) were not, and only if
 * the condition is met.  A new tree of When(s) can be
 * created by calling <code> Pipeline.when ( ... ) </code>.  Additional
 * branches can be added to the root tree by calling
 * <code> When.otherwise ( ... ) </code>
 * or <code> When.otherwise () </code> with no parameters
 * (for the default catch-all branch).
 * </p>
 *
 * @see musaico.foundation.pipeline.Decision
 *
 *
 * <p>
 * In Java every Pipeline must implement <code> equals () </code>,
 * <code> hashCode () </code> and <code> toString () </code>.
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
 * @see musaico.foundation.pipeline.MODULE#COPYRIGHT
 * @see musaico.foundation.pipeline.MODULE#LICENSE
 */
public interface When<STREAM extends Object, PARENT extends Pipeline<STREAM, PARENT>, POOL extends Object, WHEN extends When<STREAM, PARENT, POOL, WHEN>>
    extends Branch<STREAM, WHEN, STREAM, PARENT>, Decision<POOL, When<STREAM, PARENT, POOL, WHEN>>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    // Every Decision must implement
    // musaico.foundation.pipeline.Decision#and(musaico.foundation.filter.Filter[])

    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#check()

    // Every Decision must implement
    // musaico.foundation.pipeline.Decision#condition()

    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#copy(musaico.foundation.pipeline.Pipeline)

    // Every Subsidiary must implement
    // musaico.foundation.pipeline.Subsidiary#duplicate(musaico.foundation.pipeline.Parent)

    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#edit()

    // Every Decision must implement
    // musaico.foundation.pipeline.Decision#empty()

    // Every Subsidiary must implement
    // musaico.foundation.pipeline.Subsidiary#end()

    // Every Pipeline must implement java.lang.Object#equals(java.lang.Object)

    // Every Pipeline must implement java.lang.Object#hashCode()

    // Every Decision must implement
    // musaico.foundation.pipeline.Decision#infinite()

    // Every Subsidiary must implement
    // musaico.foundation.pipeline.Subsidiary#isEnded()

    // Every Decision must implement
    // musaico.foundation.pipeline.Decision#length(long)

    // Every Decision must implement
    // musaico.foundation.pipeline.Decision#length(long, long)

    // Every Decision must implement
    // musaico.foundation.pipeline.Decision#multiple()

    // Every Decision must implement
    // musaico.foundation.pipeline.Decision#or(musaico.foundation.filter.Filter[])

    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#orderBy(musaico.foundation.order.Order[])


    /**
     * <p>
     * Creates an alternative When branch to respond to any other
     * input object which does not meet the previous When
     * branches added to the parent Pipeline in the same condition tree
     * (<code> when ( ... )...otherwise ( ... )...otherwise () </code>).
     * </p>
     *
     * <p>
     * For example, to handle each Blocking term in one way,
     * but every other Term in another way, one might:
     * </code>
     *
     * <pre>
     *     final Term<VALUE> maybe_blocking = ...;
     *     final Term<VALUE> result = maybe_blocking
     *         .when ( Blocking.FILTER )   // Start condition # 1.
     *           .always ()                // Get BlockingPipeline.
     *           .await ( BigDecimal.TEN ) // Block for up to 10s.
     *         .otherwise ()               // End cond.#1, start # 2.
     *           .select ().first ()       // Get the 1st non-blocking element.
     *         .end ()                     // End condition # 2.
     *         .output ();
     * </pre>
     *
     * @return Another When branch that will add operations
     *         for processing inut objects which did not meet any of the
     *         previous condition branch(es) in the current tree of When(s).
     *         Never null.
     */
    public abstract WHEN otherwise ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Creates an alternative When to handle input objects
     * which do not meet the previous When branches added
     * to the parent Pipeline in the same condition tree
     * (<code> when ( ... )...otherwise ( ... )...otherwise () </code>),
     * but which do match the specified filter.
     * </p>
     *
     * @param condition The Filter which must return a kept result
     *                  for each input object in order for the operations
     *                  in the new When to be applied to it.
     *                  Must not be null.
     *
     * @return Another When branch that will add operations
     *         for processing input objects which did not meet any of the
     *         conditions of the previous branches in the current
     *         tree of When(s), but which do match the specified condition.
     *         Never null.
     */
    public abstract WHEN otherwise (
            Filter<POOL> condition
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    // Every Subsidiary must implement
    // musaico.foundation.pipeline.Subsidiary#parent()

    // Every Branch must implement
    // musaico.foundation.pipeline.Branch#previousOrNull()

    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#select()

    // Every Decision must implement
    // musaico.foundation.pipeline.Decision#single()

    // Every Pipeline must implement java.lang.Object#toString()

    // Every Decision must implement
    // musaico.foundation.pipeline.Decision#unique()

    /**
     * <p>
     * Pushes a new branch on the conditional tree, effectively allowing
     * parenthetical conditions such as:
     * </p>
     *
     * <pre>
     *     pipeline.when ( x )
     *                 .when ( y )
     *                     .or ( z )
     *                 .end ()
     *             .end ()
     * </pre>
     *
     * <p>
     * The above example would be evaluated as <code> x and ( y or z ) </code>.
     * </p>
     *
     * @see musaico.foundation.pipeline.Pipeline#when(musaico.foundation.filter.Filter)
     */
    // Can't find a way to override when () without the compiler moaning.
    // Oh well, we don't need to override the signature; it would just
    // be nice to keep the above documentation associated with the method,
    // instead of dangling in the middle of nowhere.


    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#where(musaico.foundation.filter.Filter[])

    // Every Decision must implement
    // musaico.foundation.pipeline.Decision#xor(musaico.foundation.filter.Filter[])
}
