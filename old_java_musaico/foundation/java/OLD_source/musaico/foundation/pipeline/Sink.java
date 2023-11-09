package musaico.foundation.pipeline;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * An object can build the output from a Pipeline.
 * </p>
 *
 * <p>
 * Typically a Sink is also a toplevel Pipeline, though not a Subsidiary.
 * </p>
 *
 * <p>
 * Typically the source of a Pipeline is not a Sink.
 * For example, a Term is a source and a toplevel Pipeline, but not a Sink.
 * Calling <code> Term.select ().first () </code> will return a toplevel
 * Pipeline which is also a Sink, and so can be used either
 * to continue processing (for example, with
 * <code> Term.select ().first ().edit ().append ( ... ).end () </code>),
 * or to produce output (for example, with
 * <code> Term.select ().first ().output () </code>).
 * </p>
 *
 *
 * <p>
 * In Java every Sink must be Serializable in order to
 * play nicely across RMI.  However users of the Sink
 * must be careful, since the values and expected data stored inside
 * might not be Serializable.
 * </p>
 *
 * <p>
 * In Java every Sink must implement equals (), hashCode ()
 * and toString ().
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
public interface Sink<STREAM extends Object, PIPELINE extends Sink<STREAM, PIPELINE, OUTPUT, JOIN_SOURCE, FORK_TARGET>, OUTPUT extends Object, JOIN_SOURCE extends Sink<STREAM, ?, ?, JOIN_SOURCE, FORK_TARGET>, FORK_TARGET extends Source<JOIN_SOURCE, STREAM, ?>>
    extends Pipeline<STREAM, PIPELINE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * @return A new Sink with exactly the same state
     *         as this one.  If this Sink is also a Pipeline,
     *         then all of its Subsidiary children (if it as any)
     *         that have already been <code> end () </code>ed
     *         will also be duplicated.  If this Sink is also a Subsidiary
     *         then no parent or ancestor Pipelines are copied,
     *         only this Subsidiary and all of its Subsidiary
     *         children (if it as any) that have already been
     *         <code> end () </code>ed.  Never null.
     */
    public abstract PIPELINE duplicate ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Creates and returns a new parallel Branch Subsidiary.
     * </p>
     *
     * <p>
     * Can be used in the same ways that <code> join ( ... ) </code>
     * can be used, but with the sub-branches potentially executing
     * concurrently, and with a potentially disjunctive,
     * rather than conjunctive, outcome.
     * </p>
     *
     * <p>
     * If the examples given for the <code> join ( ... ) </code>
     * method were changed to use <code> fork ( ... ) </code> instead, they
     * would, depending on the Sink, execute the sub-branches
     * concurrently, so that the results would not necessarily be
     * top 50 followed by bottom 50; they might be interleaved:
     * </p>
     *
     * <pre>
     *     final Order<ProductSales> sales_in_dollars_most_to_least = ...;
     *     final Order<ProductSales> sales_in_dollars_least_to_most =
     *         sales_in_dollars_most_to_least.opposite ();
     *     final Term<ProductSales> product_sales = ...;
     *     final Term<ProductSales> top_and_bottom_sellers =
     *         all.orderBy ( sales_in_dollars_most_to_least ).end ()
     *            .fork () // Top 50
     *                .select ().first ( 50L )
     *            .fork () // Bottom 50
     *                .select ().last ( 50L )
     *                .orderBy ( sales_in_dollars_least_to_most ).end ()
     *            .end ()
     *            .output ();
     * </pre>
     *
     * <pre>
     *     final Term<AccountActivity> all_account_activity = ...;
     *     final Filter<AccountActivity> is_this_year = ...;
     *     final Filter<AccountActivity> is_opening_balance = ...;
     *     final Filter<AccountActivity> is_activity = ...;
     *     final Filter<AccountActivity> is_closing_balance = ...;
     *     final Order<AccountActivity> order_by_date = ...;
     *     final Order<AccountActivity> order_by_type = ...;
     *     final Term<AccountActivity> this_year =
     *         all_account_activity.select ()
     *                                 .where ( is_this_year )
     *                             .end ()
     *                             .fork ()
     *                                 .where ( is_opening_balance ).end ()
     *                             .fork ()
     *                                 .where ( is_activity ).end ()
     *                                 .orderBy ( order_by_date,
     *                                            order_by_type ).end ()
     *                             .fork ()
     *                                 .where ( is_closing_balance ).end ()
     *                             .end ()
     *                             .output ();
     * </pre>
     *
     * <p>
     * A chain of Forks is a set of parallel branches of the Sink.  So calling
     * <code> Fork.abc ().fork ().def ().fork ().egh () </code>
     * sets up parallel sub-branches <code> abc () | def () | egh () </code>.
     * By contrast, Join is sequential.
     * </p>
     *
     * @see musaico.foundation.pipeline.Sink#join(musaico.foundation.pipeline.Sink[])
     *
     * @param outputs Zero or more Source(s) to which the outputs from
     *                the Fork branch will be redirected, rather than sending
     *                the outputs downstream to this Sink's output ()
     *                or further in this Pipeline.  These
     *                can be used to spawn pipelines which do
     *                not affect this parent, and/or to spawn pipelines
     *                which perform processing before being re-integrated
     *                with the parent by a call to
     *                <code> join ( ... ) </code>.  Can be empty,
     *                to send the Fork outputs to this Sink's output ()
     *                and downstream in this Pipeline.
     *                Must not be null.  Must not contain any null elements.
     *
     * @return The new parallel Fork sub-pipeline.  Never null.
     */
    @SuppressWarnings("unchecked") // Generic varargs possible heap pollution.
    public abstract Fork<STREAM, PIPELINE, OUTPUT, JOIN_SOURCE, FORK_TARGET, ?> fork (
            FORK_TARGET ... outputs
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Creates and returns a new sequential Join Subsidiary.
     * </p>
     *
     * <p>
     * Can be used, for example, to join together
     * one selection of elements, so that they can be ordered or filtered
     * and so on separately from other joins.
     * </p>
     *
     * <p>
     * Or to select only first the "highest" 50 elements,
     * sorted from highest to lowest, following by the "lowest"
     * 50 elements, sorted from lowest to highest:
     * </p>
     *
     * <pre>
     *     final Order<ProductSales> sales_in_dollars_most_to_least = ...;
     *     final Order<ProductSales> sales_in_dollars_least_to_most =
     *         sales_in_dollars_most_to_least.opposite ();
     *     final Term<ProductSales> product_sales = ...;
     *     final Term<ProductSales> top_and_bottom_sellers =
     *         all.orderBy ( sales_in_dollars_most_to_least ).end ()
     *            .join () // Top 50
     *                .select ().first ( 50L )
     *            .join () // Bottom 50
     *                .select ().last ( 50L )
     *                .orderBy ( sales_in_dollars_least_to_most ).end ()
     *            .end ()
     *            .output ();
     * </pre>
     *
     * <p>
     * Or can be used to output first a number of elements
     * matching one Filter, followed by a number of elements matching
     * another Filter, and so on.  For example:
     * </p>
     *
     * <pre>
     *     final Term<AccountActivity> all_account_activity = ...;
     *     final Filter<AccountActivity> is_this_year = ...;
     *     final Filter<AccountActivity> is_opening_balance = ...;
     *     final Filter<AccountActivity> is_activity = ...;
     *     final Filter<AccountActivity> is_closing_balance = ...;
     *     final Order<AccountActivity> order_by_date = ...;
     *     final Order<AccountActivity> order_by_type = ...;
     *     final Term<AccountActivity> this_year =
     *         all_account_activity.select ()
     *                                 .where ( is_this_year )
     *                             .end ()
     *                             .join ()
     *                                 .where ( is_opening_balance ).end ()
     *                             .join ()
     *                                 .where ( is_activity ).end ()
     *                                 .orderBy ( order_by_date,
     *                                            order_by_type ).end ()
     *                             .join ()
     *                                 .where ( is_closing_balance ).end ()
     *                             .end ()
     *                             .output ();
     * </pre>
     *
     * <p>
     * The result of this joining might then be a Term with
     * the following AccountActivities:
     * </p>
     *
     * <pre>
     *     Opening balance (2017/01/01) = ...
     *     Deposit (2016/02/08) = ...
     *     Withdrawal (2016/02/14) = ...
     *     Transfer (2016/03/04) = ...
     *     Purchase (2016/04/01) = ...
     *     Transfer (2016/04/01) = ...
     *     Closing balance = ...
     * </pre>
     *
     * <p>
     * And so on.
     * </p>
     *
     * <p>
     * Alternatively, a logical pipeline, such as a
     * <code> TypePipeline </code> can "AND" together multiple Types,
     * so that the Type built from the pipeline can only accept Terms
     * which meet the requirements of Type #1 AND Type #2 AND Type #3,
     * and so on:
     * </p>
     *
     * <pre>
     *     final Type<String> text = ...;
     *     final Type<String> at_least_1_character = ...;
     *     final Type<String> at_least_1_element = ...;
     *     final Type<String> non_empty_strings =
     *         text.join ()
     *                 .edit ().append ( number ).end ()
     *             .join ()
     *                 .edit ().append ( text ).end ()
     *             .join ()
     *                 .edit ().append ( date ).end ()
     *             .end ()
     *             .buildType ();
     * </pre>
     *
     * <p>
     * A chain of Joins is a sequential branch of the Pipeline.  So calling
     * <code> Join.abc ().join ().def ().join ().egh () </code>
     * sets up sequence <code> { abc (), def (), egh () } </code>.
     * By contrast, Fork is a parallel branch.
     * </p>
     *
     * @see musaico.foundation.pipeline.Sink#fork(musaico.foundation.pipeline.Source[])
     *
     * <p>
     * The distinction between sequential and parallel, or conjunctive
     * and disjunctive, can be used in different ways by different
     * Pipelines.  One Pipeline might treat sequential sub-branches
     * as pipelines to join together in one Thread on one processor,
     * while another might treat them as logical "AND"s, such as
     * in a Pipeline of Types, in which a Term must meet the requirements of
     * Type #1 AND Type #2 AND Type #3, and so on.
     * </p>
     *
     * @param inputs Zero or more Pipelines from which the outputs will be
     *               redirected to the Join branch, rather than receiving
     *               its inputs from upstream in this pipeline.  These
     *               can be used to join / merge multiple pipelines together.
     *               Can be empty, to receive the Join inputs from upstream
     *               in this pipeline.  Must not be null.
     *               Must not contain any null elements.
     *
     * @return The new sequential Join sub-pipeline.  Never null.
     */
    @SuppressWarnings("unchecked") // Generic varargs possible heap pollution.
    public abstract Join<STREAM, PIPELINE, OUTPUT, JOIN_SOURCE, FORK_TARGET, ?> join (
            JOIN_SOURCE ... inputs
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation;


    /**
     * @return The built output from the pipeline.  Depending on the context,
     *         this could be an Operation, or an expression Term
     *         with an input Term and an Operation pipeline to apply
     *         to it, and so on.  Never null.
     */
    public abstract OUTPUT output ()
        throws ReturnNeverNull.Violation;
}
