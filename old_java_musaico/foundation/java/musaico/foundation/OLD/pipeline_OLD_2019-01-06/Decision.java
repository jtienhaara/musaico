package musaico.foundation.pipeline;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;

import musaico.foundation.order.Order;


/**
 * <p>
 * The Boolean logic interface shared by Where (filter) and When (if)
 * clauses.
 * </p>
 *
 * <p>
 * In the case of Where clauses, elements of each input stream are
 * removed if they do not meet the filter(s) imposed by the Where clause.
 * The filtering is done element-by-element.
 * </p>
 *
 * <p>
 * In the case of When clauses, pipeline processing only proceeds
 * when an input object matches its filter(s).  The filtering is done
 * on each entire input object.
 * </p>
 *
 * @see musaico.foundation.pipeline.When
 * @see musaico.foundation.pipeline.Where
 *
 * <p>
 * The precedence of operators is: 1) and; 2) xor; 3) or.
 * </p>
 *
 * <pre>
 *     w or x xor y and z
 *         = w or ( x xor ( y and z ) )
 * </pre>
 *
 * <pre>
 *     w or x xor y and z | ( y and z )  ( x xor ... ) ( w or ... ) |Decision
 *     +------------------+-----------------------------------------+---------+
 *     | F  F     F     F | F and F = F | F xor F = F | F or F = F  |    F    |
 *     | F  F     F     T | F and T = F | F xor F = F | F or F = F  |    F    |
 *     | F  F     T     F | T and F = F | F xor F = F | F or F = F  |    F    |
 *     | F  F     T     T | T and T = T | F xor T = T | F or T = T  |    T    |
 *     | F  T     F     F | F and F = F | T xor F = T | F or T = T  |    T    |
 *     | F  T     F     T | F and T = F | T xor F = T | F or T = T  |    T    |
 *     | F  T     T     F | T and F = F | T xor F = T | F or T = T  |    T    |
 *     | F  T     T     T | T and T = T | T xor T = F | F or F = F  |    F    |
 *     | T  F     F     F | F and F = F | F xor F = F | T or F = T  |    T    |
 *     | T  F     F     T | F and T = F | F xor F = F | T or F = T  |    T    |
 *     | T  F     T     F | T and F = F | F xor F = F | T or F = T  |    T    |
 *     | T  F     T     T | T and T = T | F xor T = T | T or T = T  |    T    |
 *     | T  T     F     F | F and F = F | T xor F = T | T or T = T  |    T    |
 *     | T  T     F     T | F and T = F | T xor F = T | T or T = T  |    T    |
 *     | T  T     T     F | T and F = F | T xor F = T | T or T = T  |    T    |
 *     | T  T     T     T | T and T = T | T xor T = F | T or F = T  |    T    |
 *     +------------------+-----------------------------------------+---------+
 * </pre>
 *
 *
 * <p>
 * In Java every Where must be Serializable in order to
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
 * @see musaico.foundation.pipeline.MODULE#COPYRIGHT
 * @see musaico.foundation.pipeline.MODULE#LICENSE
 */
public interface Decision<STREAM extends Object, DECISION extends Decision<STREAM, DECISION>>
    extends Filter<STREAM>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Adds another filter to this Decision, such that the operands
     * to this branch must meet the previous filter(s) AND the specified
     * one (subject to operator precedence for this branch of the
     * Decision tree).
     * </p>
     *
     * @return This Decision pipeline.  Never null.
     */
    @SuppressWarnings("unchecked") // Generic array heap pollution.
    public abstract DECISION and (
            Filter<STREAM> ... filters
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation;


    /**
     * @return The condition which must be met before
     *         this When clause will process each input object.
     *         Can be invoked any time, including after this When
     *         has been <code> end () </code>ed.  Never null.
     */
    public abstract Filter<STREAM> condition ()
        throws ReturnNeverNull.Violation;


    // Every Subsidiary must implement
    // musaico.foundation.pipeline.Subsidiary#duplicate(musaico.foundation.pipeline.Parent)

    // Every Subsidiary must implement
    // musaico.foundation.pipeline.Subsidiary#end()

    // Every Subsidiary must implement java.lang.Object#equals(java.lang.Object)

    // Every Subsidiary must implement java.lang.Object#hashCode()


    /**
     * <p>
     * Adds an operation to the pipeline which will pass only
     * an input that has infinitely many elements,
     * or no elements at all, if the input is not infinite.
     * </p>
     *
     * @return This Decision.  Never null.
     */
    public abstract DECISION infinite ()
        throws ReturnNeverNull.Violation;


    // Every Subsidiary must implement
    // musaico.foundation.pipeline.Subsidiary#isEnded()


    /**
     * <p>
     * Adds an operation to the pipeline which will output every input which
     * contains exactly the specified number of elements, or No output
     * for any input which does not have exactly that many elements.
     * </p>
     *
     * <p>
     * For example, to filter out String Terms which do not contain
     * exactly 2 elements:
     * </p>
     *
     * <pre>
     *     Term<String> term = ...;
     *     Term<String> first_and_last_names =
     *         term.where ()
     *               .length ( 2L )
     *             .end ()
     *             .output ();
     * </pre>
     *
     * @param exact_length The exact length of each input.  If a given
     *                     input contains exactly the specified number
     *                     of elements, it is passed out as-is.
     *                     If the input object is not the specified length,
     *                     then a failure shall be output (such as
     *                     an Error Term, or No Term, and so on).
     *                     Must be greater than or equal to 0L.
     *
     * @return This Decision.  Never null.
     */
    public abstract DECISION length (
            long exact_length
            )
        throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Adds an operation to the pipeline which will output every input which
     * contains at the least a specific minimum number of elements, but
     * no more than a specific maximum number of elements, or No output
     * for any input whose length is not in bounds.
     * </p>
     *
     * <p>
     * For example, to filter out any String Term whose value does not
     * have either 2 or 3 elements:
     * </p>
     *
     * <pre>
     *
     * <pre>
     *     Term<String> term = ...;
     *     Term<String> first_maybe_middle_and_last_names =
     *         term.where ()
     *                 .length ( 2L, 3L )
     *             .end ()
     *             .output ();
     * </pre>
     *
     * @param minimum The minimum length of each input.
     *                If an input is shorter than the specified minimum,
     *                then a failure shall be output (such as
     *                an Error Term, or No Term, and so on).
     *                Must be greater than or equal to 0L.
     *
     * @param maximum The maximum length of each input.
     *                If an input is longer than the specified maximum,
     *                then a failure shall be output (such as
     *                an Error Term, or No Term, and so on).
     *                Must be greater than or equal to the minimum parameter.
     *
     * @return This Decision.  Never null.
     */
    public abstract DECISION length (
            long minimum,
            long maximum
            )
        throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
               Parameter2.MustBeGreaterThanOrEqualTo.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Adds an operation to the pipeline which will pass all elements
     * from each input if there are more than 1,
     * or no elements at all, if the input has one or fewer element(s).
     * </p>
     *
     * @return This Decision.  Never null.
     */
    public abstract DECISION multiple ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Adds another filter to this Decision, such that the operands
     * to this branch must meet the previous filter(s) OR the specified
     * one (subject to operator precedence for this branch of the
     * Decision tree).
     * </p>
     *
     * @return The new Decision pipeline.  Never null.
     */
    @SuppressWarnings("unchecked") // Generic array heap pollution.
    public abstract DECISION or (
            Filter<STREAM> ... filters
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation;


    // Every Subsidiary must implement
    // musaico.foundation.pipeline.Subsidiary#parent()


    /**
     * <p>
     * Adds an operation to the pipeline which will pass the one and only
     * element from the input, if the input has exactly one element,
     * or no elements at all, if the input has more than one element.
     * </p>
     *
     * @return This Decision.  Never null.
     */
    public abstract DECISION single ()
        throws ReturnNeverNull.Violation;


    // Every Subsidiary must implement java.lang.Object#toString()


    /**
     * <p>
     * Adds an operation to the pipeline which will output only one instance
     * of each input element, even if duplicates appear in the input.
     * </p>
     *
     * @return This Decision operation.  Never null.
     */
    public abstract DECISION unique ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Adds another filter to this Decision, such that the operands
     * to this branch must meet the previous filter(s) OR the specified
     * one but NOT both (subject to operator precedence for this branch of the
     * Decision tree).
     * </p>
     *
     * @return The new Decision pipeline.  Never null.
     */
    @SuppressWarnings("unchecked") // Generic array heap pollution.
    public abstract DECISION xor (
            Filter<STREAM> ... filters
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation;
}
